import java.util.LinkedList;

public class Middleman {

    private static int[] demand;
    private static int[] supply;
    private static double[][] transportCosts;
    private static double[] purchaseCosts;
    private static double[] sellingPrices;
    private static Profit[][] matrixProfit;
    private static Shipment[][] matrix;
    private static Shipment[][] shipmentMatrixWithFiction;
    private static double fullQuantityOfDemand;
    private static double alfa[];
    private static double beta[];
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
        matrix = new Shipment[numSources][numDestinations]; // + 1 Fiction Sender and Fiction Receiver
        matrixProfit = new Profit[numSources][numDestinations];
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

        fullQuantityOfDemand = 0;
    }

    public static int findMaxValuable(Profit[] arr)
    {
        //for(Profit profit : arr)
        //    System.out.println(profit.profitValue);
        int index = -1;
        double maxValuable = -Double.MAX_VALUE;
        for(int i = 0; i < arr.length; i++)
        {
            if(maxValuable != Math.max(maxValuable, arr[i].profitValue))
            {
                maxValuable = Math.max(maxValuable, arr[i].profitValue);
                index = i;
            }

        }
        return index;
    }

    static void maxElementMatrix() {
        for (int r = 0, northwest = 0; r < supply.length; r++)
            for (int c = 0; c < demand.length; c++) {
                matrix[r][c] = new Shipment(0, transportCosts[r][c], purchaseCosts[r], sellingPrices[c], r, c);
                if(r == 0)
                    fullQuantityOfDemand += demand[c];
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
        Profit[] arrTmp;
        for (int r = 0, northwest = 0; r < supply.length; r++)
        {
            //System.out.println("r = " + r);
            arrTmp = matrixProfit[r];
            for (int c = northwest; c < demand.length; c++) {
                int suitableDemand = findMaxValuable(arrTmp);
                while(suitableDemand != -1 && demand[suitableDemand] == 0)
                {
                    arrTmp[suitableDemand].profitValue = -Double.MAX_VALUE;
                    suitableDemand = findMaxValuable(arrTmp);
                    if(demand[0] == 0 && demand[1] == 0 && demand[2] == 0 || suitableDemand == -1)
                        break;
                }

                int quantity = Math.min(supply[r], demand[suitableDemand]);
                if (quantity > 0) {
                    matrix[r][suitableDemand] = new Shipment(quantity, transportCosts[r][c], purchaseCosts[r], sellingPrices[c], r, c);
                    supply[r] -= quantity;
                    demand[suitableDemand] -= quantity;
                    //System.out.println(suitableDemand + "supply = " + supply[r]);
                    if (supply[r] == 0) {
                        northwest = c;
                        break;
                    }
                }
                arrTmp[suitableDemand].profitValue = -Double.MAX_VALUE;
            }
        }

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

    static void initShipmentMatrixWithFiction()
    {
        System.out.println("\n\n");
        shipmentMatrixWithFiction = new Shipment[supply.length + 1][demand.length + 1];

        for(int r = 0; r < supply.length; r++)
        {
            for(int c = 0; c < demand.length; c++)
            {
                shipmentMatrixWithFiction[r][c] = matrix[r][c];
                if(r == 0)
                {
                    shipmentMatrixWithFiction[supply.length][c] = new Shipment(demand[c], 0, 0, 0, supply.length, c);
                }
            }
            shipmentMatrixWithFiction[r][demand.length] = new Shipment(supply[r], 0, 0, 0, r, demand.length);
        }
        shipmentMatrixWithFiction[supply.length][demand.length] = new Shipment( fullQuantityOfDemand - shipmentMatrixWithFiction[supply.length][0].quantity - shipmentMatrixWithFiction[supply.length][1].quantity -  shipmentMatrixWithFiction[supply.length][2].quantity - shipmentMatrixWithFiction[0][demand.length].quantity - shipmentMatrixWithFiction[1][demand.length].quantity,
                0, 0, 0, supply.length, demand.length);
        for (int r = 0; r <= supply.length; r++)
        {
            for (int c = 0; c <= demand.length; c++) {
                if(shipmentMatrixWithFiction[r][c] != null)
                    System.out.print(shipmentMatrixWithFiction[r][c].quantity + " ");
                else
                    System.out.print(0 + " ");
            }
            System.out.println("");
        }

    }

    public static void calcAlfaAndBeta()
    {
        alfa = new double[supply.length + 1];
        beta = new double[demand.length + 1];
        for(int i = 0; i < alfa.length - 1; i++)
        {
            alfa[i] = -Double.MAX_VALUE;
        }
        for(int i = 0; i < beta.length - 1; i++)
        {
            beta[i] = -Double.MAX_VALUE;
        }
        alfa[alfa.length - 1] = 0;
        beta[beta.length - 1] = 0;
        boolean isNotBase = true;
        for(int c = 0; c < demand.length - 1; c++)
        {
            isNotBase = true;
            for(int r = 0; r < supply.length; r++)
            {
                if(shipmentMatrixWithFiction[r][c].quantity == 0)
                {
                    isNotBase = false;
                    break;
                }
            }
            if(isNotBase)
            {
                beta[c] = 0;
            }
        }
        //System.out.println(alfa.length + " " + beta.length);
        for(int r = supply.length - 1; r >= 0; r--)
        {
            for(int c = 0; c < demand.length - 1; c++)
            {
                if(shipmentMatrixWithFiction[r][c].quantity == 0)
                {
                    if(alfa[r] == -Double.MAX_VALUE && beta[c] != -Double.MAX_VALUE)
                    {
                        if(r != matrixProfit.length && matrixProfit[r][c] != null)
                            alfa[r] = matrixProfit[r][c].profitValue - beta[c];
                        else
                            alfa[r] = - beta[c];
                    }
                    if(alfa[r] != -Double.MAX_VALUE && beta[c] == -Double.MAX_VALUE)
                    {
                        if(r != matrixProfit.length && matrixProfit[r][c] != null)
                            beta[c] = matrixProfit[r][c].profitValue - alfa[r];
                        else
                            beta[c] = - alfa[r];
                    }
                }
            }
        }
        System.out.print("Alfa ");
        for(int index = 0; index < alfa.length; index++)
        {
            System.out. print(alfa[index] + " ");
        }
        System.out.println("\n\n");
        System.out.print("Beta ");
        for(int index = 0; index < beta.length; index++)
        {
            System.out. print(beta[index] + " ");
        }
        System.out.println("\n\n");
    }




//    static void steppingStone() {
//        double maxReduction = 0;
//        Shipment[] move = null;
//        Shipment leaving = null;
//
//        fixDegenerateCase();
//
//        for (int r = 0; r < supply.length; r++) {
//            for (int c = 0; c < demand.length; c++) {
//                if (matrix[r][c] != null)
//                    continue;
//
//                Shipment trial = new Shipment(0, transportCosts[r][c], purchaseCosts[r], sellingPrices[c], r, c);
//                Shipment[] path = getClosedPath(trial);
//
//                double reduction = 0;
//                double lowestQuantity = Integer.MAX_VALUE;
//                Shipment leavingCandidate = null;
//
//                boolean plus = true;
//                for (Shipment s : path) {
//                    if (plus) {
//                        reduction += s.costPerUnit;
//                    } else {
//                        reduction -= s.costPerUnit;
//                        if (s.quantity < lowestQuantity) {
//                            leavingCandidate = s;
//                            lowestQuantity = s.quantity;
//                        }
//                    }
//                    plus = !plus;
//                }
//                if (reduction < maxReduction) {
//                    move = path;
//                    leaving = leavingCandidate;
//                    maxReduction = reduction;
//                }
//            }
//        }
//
//        if (move != null && leaving != null) {
//            double q = leaving.quantity;
//            boolean plus = true;
//            for (Shipment s : move) {
//                s.quantity += plus ? q : -q;
//                matrix[s.r][s.c] = s.quantity == 0 ? null : s;
//                plus = !plus;
//            }
//            steppingStone();
//        }
//    }

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

    static void printMatrix() {
        System.out.println("Matrix:");

        for (Shipment[] row : matrix) {
            for (Shipment s : row) {
                if (s != null) {
                    System.out.printf("%6.1f ", s.quantity);
                } else {
                    System.out.print("   -   ");
                }
            }
            System.out.println();
        }
    }

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
        maxElementMatrix();
        initShipmentMatrixWithFiction();
        //steppingStone();
        calcAlfaAndBeta();
        //printMatrix();
//        printResults();
    }
}
