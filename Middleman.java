import java.util.LinkedList;

public class Middleman {

    private static int[] demand;
    private static int[] supply;
    private static double[][] transportCosts;
    private static double[] purchaseCosts;
    private static double[] sellingPrices;
    private static Profit[][] matrixProfit;
    private static Shipment[][] matrix;
    private static class Shipment {
        final double costPerUnit;
        final double purchaseCost;
        final double sellingPrice;
        final int r, c;
        double quantity;

        public Shipment(double q, double cpu, double purchaseCost, double sellingPrice, int r, int c) {
            quantity = q;
            costPerUnit = cpu;
            this.purchaseCost = purchaseCost;
            this.sellingPrice = sellingPrice;
            this.r = r;
            this.c = c;
        }
    }
    private static class Profit {
        double profitValue;
        Shipment shipment;
        int r,c;
        Profit(Shipment shipment, int r, int c)
        {
            this.shipment = shipment;
            this.profitValue = shipment.sellingPrice - shipment.costPerUnit - shipment.purchaseCost;
            this.r = r;
            this.c = c;
        }
    }


    static void init(int[] src, int[] dst, double[][] transportCosts1, double[] purchaseCosts1, double[] sellingPrice1 ) {
        int numSources = 2; // Liczba dostawców
        int numDestinations = 3; // Liczba odbiorców
        transportCosts = new double[numSources][numDestinations];
        purchaseCosts = new double[numSources];
        sellingPrices = new double[numDestinations];
        matrix = new Shipment[numSources + 1][numDestinations + 1]; // + 1 Fiction Sender and Fiction Receiver
        matrixProfit = new Profit[numSources + 1][numDestinations + 1];
//        src[0] = 20;
//        src[1] = 30; // Podaż od dostawców
//        dst[0] = 10;
//        dst[1] = 28;
//        dst[2] = 27; // Popyt od odbiorców

        supply = src;
        demand = dst;

        // Jednostkowe koszty transportu
        transportCosts[0][0] = 8;
        transportCosts[0][1] = 14;
        transportCosts[0][2] = 17;
        transportCosts[1][0] = 12;
        transportCosts[1][1] = 9;
        transportCosts[1][2] = 19;

        // Jednostkowe koszty zakupu
        purchaseCosts[0] = 10;
        purchaseCosts[1] = 12;

        // Ceny sprzedaży
        sellingPrices[0] = 30;
        sellingPrices[1] = 25;
        sellingPrices[2] = 30;
    }

    public static int findMaxValuable(Profit[] arr)
    {
        int index = -1;
        double maxValuable = Double.MIN_VALUE;
        for(int i = 0; i < arr.length; i++)
        {
            maxValuable = Math.max(maxValuable, arr[i].profitValue);
            index = i;
        }
        return index;
    }

    static void northWestCornerRule() {
        for (int r = 0, northwest = 0; r < supply.length; r++)
            for (int c = northwest; c < demand.length; c++) {
                int suitableDemand = findMaxValuable(matrixProfit[r]);
                int quantity = Math.min(supply[r], demand[suitableDemand]);
                if (quantity > 0) {
                    matrix[r][c] = new Shipment(quantity, transportCosts[r][c], purchaseCosts[r], sellingPrices[c], r, c);
                    supply[r] -= quantity;
                    demand[c] -= quantity;
                    if (supply[r] == 0) {
                        northwest = c;
                        break;
                    }
                }
            }
        for (int r = 0; r < supply.length; r++)
        {
            for (int c = 0; c < demand.length; c++) {
                if(matrix[r][c] != null)
                    matrixProfit[r][c] = new Profit(matrix[r][c], r, c);
                else
                    matrixProfit[r][c] = new Profit(new Shipment(0, transportCosts[r][c], purchaseCosts[r], sellingPrices[c], r, c), r, c);
                System.out.print(matrixProfit[r][c].profitValue + " ");
            }
            System.out.println("");
        }
        System.out.println("\n");
        for (int r = 0; r < supply.length; r++)
        {
            for (int c = 0; c < demand.length; c++) {
                if(matrix[r][c] != null)
                    System.out.print(matrix[r][c].quantity + " ");
                else
                    System.out.print(0 + " ");
            }
            System.out.println("");
        }
    }

    static void steppingStone() {
        double maxReduction = 0;
        Shipment[] move = null;
        Shipment leaving = null;

        fixDegenerateCase();

        for (int r = 0; r < supply.length; r++) {
            for (int c = 0; c < demand.length; c++) {
                if (matrix[r][c] != null)
                    continue;

                Shipment trial = new Shipment(0, transportCosts[r][c], purchaseCosts[r], sellingPrices[c], r, c);
                Shipment[] path = getClosedPath(trial);

                double reduction = 0;
                double lowestQuantity = Integer.MAX_VALUE;
                Shipment leavingCandidate = null;

                boolean plus = true;
                for (Shipment s : path) {
                    if (plus) {
                        reduction += s.costPerUnit;
                    } else {
                        reduction -= s.costPerUnit;
                        if (s.quantity < lowestQuantity) {
                            leavingCandidate = s;
                            lowestQuantity = s.quantity;
                        }
                    }
                    plus = !plus;
                }
                if (reduction < maxReduction) {
                    move = path;
                    leaving = leavingCandidate;
                    maxReduction = reduction;
                }
            }
        }

        if (move != null && leaving != null) {
            double q = leaving.quantity;
            boolean plus = true;
            for (Shipment s : move) {
                s.quantity += plus ? q : -q;
                matrix[s.r][s.c] = s.quantity == 0 ? null : s;
                plus = !plus;
            }
            steppingStone();
        }
    }

    static LinkedList<Shipment> matrixToList() {
        LinkedList<Shipment> list = new LinkedList<>();
        for (Shipment[] row : matrix) {
            for (Shipment s : row) {
                if (s != null) {
                    list.add(s);
                }
            }
        }
        return list;
    }

    static Shipment[] getClosedPath(Shipment s) {
        LinkedList<Shipment> path = matrixToList();
        path.addFirst(s);

        // remove (and keep removing) elements that do not have a
        // vertical AND horizontal neighbor
        while (path.removeIf(e -> {
            Shipment[] nbrs = getNeighbors(e, path);
            return nbrs[0] == null || nbrs[1] == null;
        }));

        // place the remaining elements in the correct plus-minus order
        Shipment[] stones = path.toArray(new Shipment[path.size()]);
        Shipment prev = s;
        for (int i = 0; i < stones.length; i++) {
            stones[i] = prev;
            prev = getNeighbors(prev, path)[i % 2];
        }
        return stones;
    }
    //
    static Shipment[] getNeighbors(Shipment s, LinkedList<Shipment> lst) {
        Shipment[] nbrs = new Shipment[2];
        for (Shipment o : lst) {
            if (o != s) {
                if (o.r == s.r && nbrs[0] == null)
                    nbrs[0] = o;
                else if (o.c == s.c && nbrs[1] == null)
                    nbrs[1] = o;
                if (nbrs[0] != null && nbrs[1] != null)
                    break;
            }
        }
        return nbrs;
    }

    static void fixDegenerateCase() {
        final double eps = Double.MIN_VALUE;

        if (supply.length + demand.length - 1 != matrixToList().size()) {

            for (int r = 0; r < supply.length; r++)
                for (int c = 0; c < demand.length; c++) {
                    if (matrix[r][c] == null) {
                        Shipment dummy = new Shipment(eps, transportCosts[r][c], purchaseCosts[r], sellingPrices[c], r, c);
                        if (getClosedPath(dummy).length == 0) {
                            matrix[r][c] = dummy;
                            return;
                        }
                    }
                }
        }
    }

    static double calculateRevenue() {
        double revenue = 0;

        for (Shipment[] row : matrix) {
            for (Shipment s : row) {
                if (s != null) {
                    revenue += s.sellingPrice * s.quantity;
                }
            }
        }

        return revenue;
    }

    static double calculateTotalCost() {
        double totalCost = 0;

        for (Shipment[] row : matrix) {
            for (Shipment s : row) {
                if (s != null) {
                    totalCost += s.costPerUnit * s.quantity;
                }
            }
        }

        return totalCost;
    }

    static double calculateProfit() {
        return calculateRevenue() - calculateTotalCost();
    }
//
//    static void printMatrix() {
//        System.out.println("Matrix:");
//
//        for (Shipment[] row : matrix) {
//            for (Shipment s : row) {
//                if (s != null) {
//                    System.out.printf("%6.1f ", s.quantity);
//                } else {
//                    System.out.print("   -   ");
//                }
//            }
//            System.out.println();
//        }
//    }

//    static void printResults() {
//        System.out.println("\nResults:");
//
//        System.out.println("Revenue: " + calculateRevenue());
//        System.out.println("Total Cost: " + calculateTotalCost());
//        System.out.println("Profit: " + calculateProfit());
//
//        System.out.println("\nPurchase Costs:");
//        for (int r = 0; r < supply.length; r++) {
//            System.out.println("Supplier " + (r + 1) + ": " + purchaseCosts[r]);
//        }
//
//        System.out.println("\nSelling Prices:");
//        for (int c = 0; c < demand.length; c++) {
//            System.out.println("Customer " + (c + 1) + ": " + sellingPrices[c]);
//        }
//    }

    public static void main(String[] args) {
        int[] src = {20, 30};
        int[] dst = {10, 28, 27};
        double[][] transportCosts1 = {};
        double[] purchaseCosts1 = {};
        double[] sellingPrice1 = {};
        init(src, dst, transportCosts1, sellingPrice1, purchaseCosts1);
        northWestCornerRule();
        steppingStone();

//        printMatrix();
//        printResults();
    }
}
