import java.util.ArrayList;
import static java.util.Arrays.stream;
import java.util.LinkedList;
import java.util.List;
import static java.util.stream.Collectors.toCollection;

public class Middleman {
    private static double incomeMax;
    private static double costAll;
    private static double profitAll;
    private static int[] demand;
    private static int[] supply;
    private static double[][] transportCosts;
    private static double[] purchaseCosts;
    private static double[] sellingPrices;
    private static Profit[][] matrixProfit;
    private static Shipment[][] matrix;
    private static boolean isUnBalanced;

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

//        src[0] = 20;
//        src[1] = 30; // Podaż od dostawców
//        dst[0] = 10;
//        dst[1] = 28;
//        dst[2] = 27; // Popyt od odbiorców

        supply = src;
        demand = dst;

        // fix imbalance
        int totalSrc = 0;
        for (int s : src)
            totalSrc += s;
        int totalDst = 0;
        for (int s : dst)
            totalDst += s;
        if (totalSrc > totalDst) {
            isUnBalanced = true;
            demand = new int[4];
            demand[0] = dst[0];
            demand[1] = dst[1];
            demand[2] = dst[2];
            demand[3] = totalSrc - totalDst;
            supply = new int[3];
            supply[0] = src[0];
            supply[1] = src[1];
            supply[2] = 0;
        } else if (totalDst > totalSrc) {
            isUnBalanced = true;
            demand = new int[4];
            demand[0] = dst[0];
            demand[1] = dst[1];
            demand[2] = dst[2];
            demand[3] = 0;
            supply = new int[3];
            supply[0] = src[0];
            supply[1] = src[1];
            supply[2] = totalDst - totalSrc;
        }
        transportCosts = new double[supply.length][demand.length];
        if (supply.length != src.length || demand.length != dst.length) {
            transportCosts[0][0] = transportCosts1[0][0];
            transportCosts[0][1] = transportCosts1[0][1];
            transportCosts[0][2] = transportCosts1[0][2];
            transportCosts[0][3] = 0;
            transportCosts[1][0] = transportCosts1[1][0];
            transportCosts[1][1] = transportCosts1[1][1];
            transportCosts[1][2] = transportCosts1[1][2];
            transportCosts[1][3] = 0;
            transportCosts[2][0] = sellingPrice1[0];
            transportCosts[2][1] = sellingPrice1[1];
            transportCosts[2][2] = sellingPrice1[2];
            transportCosts[2][3] = 0;

            purchaseCosts = new double[3];
            purchaseCosts[0] = purchaseCosts1[0];
            purchaseCosts[1] = purchaseCosts1[1];
            purchaseCosts[2] = 0;
            sellingPrices = new double[4];
            sellingPrices[0] = sellingPrice1[0];
            sellingPrices[1] = sellingPrice1[1];
            sellingPrices[2] = sellingPrice1[2];
            sellingPrices[3] = 0;
        }
        else
        {
            isUnBalanced = false;
            transportCosts = transportCosts1;
            purchaseCosts = purchaseCosts1;
            sellingPrices = sellingPrice1;
        }

        matrix = new Shipment[supply.length][demand.length]; // + 1 Fiction Sender and Fiction Receiver
        matrixProfit = new Profit[supply.length][demand.length];
        System.out.println("transport costs");
        for (int r = 0, northwest = 0; r < supply.length; r++) {
            for (int c = 0; c < demand.length; c++) {
                System.out.print(transportCosts[r][c] + " ");
            }
            System.out.print("\n");
        }
        System.out.println("PurchaseCost");
        for (int r = 0, northwest = 0; r < supply.length; r++)
            System.out.print(purchaseCosts[r] + " ");
        System.out.println("\n");
        System.out.println("Selling Price");
        for (int c = 0; c < demand.length; c++) {
            System.out.print(sellingPrices[c] + " ");
        }
        System.out.println("\n");
        for (int r = 0, northwest = 0; r < supply.length; r++)
            for (int c = 0; c < demand.length; c++) {
                matrix[r][c] = new Shipment(0, transportCosts[r][c], purchaseCosts[r], sellingPrices[c], r, c);
            }
        System.out.println("Profit matrix");
        for (int r = 0; r < supply.length; r++)
        {
            for (int c = 0; c < demand.length; c++) {
                if(c == demand.length - 1)
                    matrixProfit[r][c] = new Profit(new Shipment(matrix[r][c].quantity, 0, 0, 0, r, c), r, c);
                else if(matrix[r][c] != null)
                    matrixProfit[r][c] = new Profit(matrix[r][c], r, c);
                //else
                //    matrixProfit[r][c] = new Profit(new Shipment(-5, transportCosts[r][c], purchaseCosts[r], sellingPrices[c], r, c), r, c);
                if(matrixProfit[r][c] != null)
                    System.out.print(matrixProfit[r][c].profitValue + " ");
                else
                    System.out.print("- ");
            }
            System.out.println("");
        }
        System.out.println("\n");
        for (int r = 0; r < supply.length; r++)
        {
            for (int c = 0; c < demand.length; c++) {
                    matrixProfit[r][c].shipment =  null;
                //else
                //    matrixProfit[r][c] = new Profit(new Shipment(-5, transportCosts[r][c], purchaseCosts[r], sellingPrices[c], r, c), r, c);
            }
            System.out.println("");
        }
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
        Profit[] arrTmp;
        for (int r = 0, northwest = 0; r < supply.length; r++)
        {
            //System.out.println("Supply przed " + supply[r]);
            //System.out.println("r = " + r);
            arrTmp = matrixProfit[r];
            for (int c = northwest; c < demand.length; c++) {
                int suitableDemand = findMaxValuable(arrTmp);
                while(suitableDemand != -1 && demand[suitableDemand] == 0)
                {
                    arrTmp[suitableDemand].profitValue = -Double.MAX_VALUE;
                    suitableDemand = findMaxValuable(arrTmp);
                    if(demand.length == 4)
                        if(demand[0] == 0 && demand[1] == 0 && demand[2] == 0 && demand[3] == 0 || suitableDemand == -1)
                            break;
                    if(demand[0] == 0 && demand[1] == 0 && demand[2] == 0 || suitableDemand == -1)
                        break;
                }

                int quantity = Math.min(supply[r], demand[suitableDemand]);
                //System.out.println("index = " + suitableDemand + " quantity " + quantity);
                if (quantity > 0) {
                    if(demand.length - 1 == c)
                        matrixProfit[r][suitableDemand].shipment = new Shipment(quantity, 0, 0, 0, r, c);
                    else
                        matrixProfit[r][suitableDemand].shipment = new Shipment(quantity, transportCosts[r][c], purchaseCosts[r], sellingPrices[c], r, c);
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
                if(matrixProfit[r][c].shipment != null)
                    System.out.print(matrixProfit[r][c].shipment.quantity + " ");
                else
                    System.out.print("x ");
            }
            System.out.println("");
        }

        for (int r = 0; r < supply.length; r++)
        {
            for (int c = 0; c < demand.length; c++) {
                    matrix[r][c] = matrixProfit[r][c].shipment;
                    if(demand.length - 1 == c)
                        matrixProfit[r][c].profitValue = 0;
                    else
                        matrixProfit[r][c].profitValue = sellingPrices[c] - purchaseCosts[r] - transportCosts[r][c];
                   // System.out.print(matrixProfit[r][c].profitValue + " ");
            }
            //System.out.print("\n");
        }
    }

    static void calcTheBestIncome()
    {
        for (int r = 0; r < supply.length; r++) {
            for (int c = 0; c < demand.length; c++) {
                if(matrixProfit[r][c].shipment == null && demand.length - 1 != c)
                    matrixProfit[r][c].shipment = new Shipment(0, transportCosts[r][c], purchaseCosts[r], sellingPrices[c], r, c);
                else if(matrixProfit[r][c].shipment == null)
                    matrixProfit[r][c].shipment = new Shipment(0, 0, 0, 0, r, c);
            }
        }
        Profit[][] matrixProfitTmp = new Profit[supply.length][demand.length];
        for (int r = 0; r < supply.length; r++) {
            for (int c = 0; c < demand.length; c++) {
                matrixProfitTmp[r][c] = matrixProfit[r][c];
            }
        }

        double income = 0;
        double profit = 0;
        double cost = 0;
        incomeMax = 0;
        profitAll = 0;
        costAll = 0;
        for (int r = 0; r < supply.length; r++) {
            for (int c = 0; c < demand.length; c++) {
                incomeMax += matrixProfit[r][c].shipment.sellingPrice * matrixProfit[r][c].shipment.quantity;
                profitAll += matrixProfit[r][c].profitValue * matrixProfit[r][c].shipment.quantity;
                costAll +=  (matrixProfit[r][c].shipment.costPerUnit + matrixProfit[r][c].shipment.purchaseCost) * matrixProfit[r][c].shipment.quantity;
            }
        }
        for (int r = 0; r < supply.length; r++) {
             for (int c = 0; c < demand.length; c++) {
                 if(matrixProfitTmp[r][c].profitValue < 0 && matrixProfitTmp[r][c].shipment.quantity > 0)
                 {
                     matrixProfitTmp[r][demand.length - 1].shipment.quantity += matrixProfitTmp[r][c].shipment.quantity;
                     matrixProfitTmp[supply.length - 1][c].shipment.quantity += matrixProfitTmp[r][c].shipment.quantity;
                     matrixProfitTmp[r][c].shipment.quantity = 0;
                 }
                 income += matrixProfitTmp[r][c].shipment.sellingPrice * matrixProfitTmp[r][c].shipment.quantity;
                 profit += matrixProfitTmp[r][c].profitValue * matrixProfitTmp[r][c].shipment.quantity;
                 cost +=  (matrixProfitTmp[r][c].shipment.costPerUnit + matrixProfitTmp[r][c].shipment.purchaseCost) * matrixProfitTmp[r][c].shipment.quantity;
             }
        }
        if(profit > profitAll)
        {
            for (int r = 0; r < supply.length; r++) {
                for (int c = 0; c < demand.length; c++) {
                    matrixProfit[r][c] = matrixProfitTmp[r][c];
                    incomeMax = income;
                    profitAll = profit;
                    costAll = cost;
                }
            }
        }
    }

//    static void steppingStone() {
//        System.out.println("Stepping stone");
//        double maxIncome = 0;
//        Shipment[] move = null;
//        Shipment leaving = null;
//
//        fixDegenerateCase();
//
//        for (int r = 0; r < supply.length; r++) {
//            for (int c = 0; c < demand.length; c++) {
//
//                if (matrix[r][c] != null)
//                    continue;
//
//                Shipment trial = new Shipment(0, transportCosts[r][c], purchaseCosts[r], sellingPrices[c], r, c);
//                Shipment[] path = getClosedPath(trial);
//
//                double income = 0;
//                double lowestQuantity = Integer.MAX_VALUE;
//                Shipment leavingCandidate = null;
//
//                boolean plus = true;
//                for (Shipment s : path) {
//                    if (plus) {
//                        income += (s.sellingPrice - s.costPerUnit - s.purchaseCost);
//                    } else {
//                        income -= (s.sellingPrice - s.costPerUnit - s.purchaseCost);
//                        if (s.quantity < lowestQuantity) {
//                            leavingCandidate = s;
//                            lowestQuantity = s.quantity;
//                        }
//                    }
//                    plus = !plus;
//                }
//                System.out.print("income = " + income + " maxIncome = " + maxIncome + " \n");
//                if (income > maxIncome) {
//                    move = path;
//                    leaving = leavingCandidate;
//                    maxIncome = income;
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
//            for (int r = 0; r < supply.length; r++)
//            {
//                for (int c = 0; c < demand.length; c++) {
//                    matrixProfit[r][c].shipment = matrix[r][c];
//                }
//            }
//            steppingStone();
//        }
//        for (int r = 0; r < supply.length; r++)
//        {
//            for (int c = 0; c < demand.length; c++) {
//                matrixProfit[r][c].shipment = matrix[r][c];
//            }
//        }
//    }

//    static LinkedList<Shipment> matrixToList() {
//        return stream(matrix)
//                .flatMap(row -> stream(row))
//                .filter(s -> s != null)
//                .collect(toCollection(LinkedList::new));
//    }

//    @SuppressWarnings("empty-statement")
//    static Shipment[] getClosedPath(Shipment s) {
//        LinkedList<Shipment> path = matrixToList();
//        path.addFirst(s);
//
//        // remove (and keep removing) elements that do not have a
//        // vertical AND horizontal neighbor
//        while (path.removeIf(e -> {
//            Shipment[] nbrs = getNeighbors(e, path);
//            return nbrs[0] == null || nbrs[1] == null;
//        }));
//
//        // place the remaining elements in the correct plus-minus order
//        Shipment[] stones = path.toArray(new Shipment[path.size()]);
//        Shipment prev = s;
//        for (int i = 0; i < stones.length; i++) {
//            stones[i] = prev;
//            prev = getNeighbors(prev, path)[i % 2];
//        }
//        return stones;
//    }

//    static Shipment[] getNeighbors(Shipment s, List<Shipment> lst) {
//        Shipment[] nbrs = new Shipment[2];
//        for (Shipment o : lst) {
//            if (o != s) {
//                if (o.r == s.r && nbrs[0] == null)
//                    nbrs[0] = o;
//                else if (o.c == s.c && nbrs[1] == null)
//                    nbrs[1] = o;
//                if (nbrs[0] != null && nbrs[1] != null)
//                    break;
//            }
//        }
//        return nbrs;
//    }

//    static void fixDegenerateCase() {
//        final double eps = 0;
//
//        if (supply.length + demand.length - 1 != matrixToList().size()) {
//
//            for (int r = 0; r < supply.length; r++)
//                for (int c = 0; c < demand.length; c++) {
//                    if (matrix[r][c] == null) {
//                        Shipment dummy = new Shipment(eps, transportCosts[r][c], purchaseCosts[r], sellingPrices[c], r, c);
//                        if (getClosedPath(dummy).length == 0) {
//                            matrix[r][c] = dummy;
//                            return;
//                        }
//                    }
//                }
//        }
//    }

    static void printResult() {
        System.out.println("Optimal solution");
        //double totalIncome = 0;

        for (int r = 0; r < supply.length; r++) {
            for (int c = 0; c < demand.length; c++) {
                Profit s = matrixProfit[r][c];
                //Shipment s = matrix[r][c];
                if (s.shipment != null && s.r == r && s.c == c) {
                    System.out.print(s.shipment.quantity + " ");
                    //totalIncome += (s.shipment.quantity * s.profitValue);
                } else
                    System.out.printf("  x  ");
            }
            System.out.println();
        }
        System.out.println("Total cost: " + costAll);
        System.out.println("Total income: " + profitAll);
        System.out.println("Total cost: " + incomeMax);

    }




    public static void main(String[] args) {
        int[] src = {20, 30};
        int[] dst = {10, 28, 27};
        double[][] transportCosts = {{ 8, 14, 17 }, { 12, 9, 19 }};
        double[] purchaseCosts = { 10, 12 };
        double[] sellingPrice = { 30, 25, 30 };

        init(src, dst, transportCosts, purchaseCosts, sellingPrice);
        maxElementMatrix();
        calcTheBestIncome();
        //initShipmentMatrixWithFiction();
        //steppingStone();
        //calcAlfaAndBeta();
        //printMatrix();
        printResult();
    }

































//
//    static void steppingStone() {
//        double maxIncrease = 0;
//        Profit[] move = null;
//        Profit leaving = null;
//
//        fixDegenerateCase();
////        for(int r = 0; r < supply.length; r++) {
////            for(int c = 0; c < demand.length; c++)
////            {
////                matrixProfit[r][c] = new Profit(matrix[r][c], r, c);
////            }
////        }
//        for (int r = 0; r < supply.length; r++) {
//            for (int c = 0; c < demand.length; c++) {
//                if (matrixProfit[r][c] != null && matrixProfit[r][c].shipment != null)
//                    continue;
//
//                Profit trial = new Profit(new Shipment(0, transportCosts[r][c], purchaseCosts[r], sellingPrices[c], r, c), r, c);
//                Profit[] path = getClosedPath(trial);
//
//                double increase = 0;
//                double lowestQuantity = Integer.MAX_VALUE;
//                Profit leavingCandidate = null;
//
//                boolean plus = true;
//                for (Profit s : path) {
//                    if (plus) {
//                        increase += s.profitValue;
//                    } else {
//                        increase -= s.profitValue;
//                        if (s.shipment.quantity < lowestQuantity) {
//                            leavingCandidate = s;
//                            lowestQuantity = s.shipment.quantity;
//                            System.out.println("lowestQuantity changed: " + lowestQuantity);
//                        }
//                    }
//                    plus = !plus;
//                }
//                if (increase > maxIncrease) {
//                    move = path;
//                    leaving = leavingCandidate;
//                    maxIncrease = increase;
//                }
//            }
//        }
//
//        if (move != null && leaving != null) {
//            double q = leaving.shipment.quantity;
//            boolean plus = true;
//            for (Profit s : move) {
//                s.shipment.quantity += plus ? q : -q;
//                matrixProfit[s.r][s.c] = s.shipment.quantity == 0 ? null : s;
//                plus = !plus;
//            }
//            steppingStone();
//        }
//    }
//
//    static LinkedList<Profit> matrixToList() {
////        LinkedList<Profit> list = new LinkedList<>();
////        for (Profit[] row : matrixProfit) {
////            for (Profit s : row) {
////                if (s != null && s.shipment != null) {
////                    System.out.println("matrixToList worked");
////                    list.add(s);
////                }
////            }
////        }
////        return list;
//        return stream(matrixProfit)
//                .flatMap(row -> stream(row))
//                .filter(s -> s != null)
//                .collect(toCollection(LinkedList::new));
//    }
//
//    static Profit[] getClosedPath(Profit s) {
//        LinkedList<Profit> path = matrixToList();
//        path.addFirst(s);
//
//        // remove (and keep removing) elements that do not have a
//        // vertical AND horizontal neighbor
//        while (path.removeIf(e -> {
//            System.out.println("getClosedPath worked");
//            Profit[] nbrs = getNeighbors(e, path);
//            return nbrs[0] == null || nbrs[1] == null;
//        }));
//
//        // place the remaining elements in the correct plus-minus order
//        Profit[] stones = path.toArray(new Profit[path.size()]);
//        Profit prev = s;
//        for (int i = 0; i < stones.length; i++) {
//            System.out.println("getClosedPath 2 worked");
//            stones[i] = prev;
//            prev = getNeighbors(prev, path)[i % 2];
//        }
//        return stones;
//    }
//    //
//    static Profit[] getNeighbors(Profit s, LinkedList<Profit> lst) {
//        Profit[] nbrs = new Profit[2];
//        for (Profit o : lst) {
//            if (o.shipment != s.shipment) {
//                //System.out.println("getNeighbors worked");
//                if (o.r == s.r && nbrs[0] == null)
//                    nbrs[0] = o;
//                else if (o.c == s.c && nbrs[1] == null)
//                    nbrs[1] = o;
//                if (nbrs[0] != null && nbrs[1] != null)
//                    break;
//            }
//        }
//        return nbrs;
//    }
//
//    static void fixDegenerateCase() {
//        final double eps = Double.MIN_VALUE;
//
//        if (supply.length + demand.length - 1 != matrixToList().size()) {
//            System.out.println("fixDegenerateCase worked");
//            for (int r = 0; r < supply.length; r++)
//                for (int c = 0; c < demand.length; c++) {
//                    if (matrixProfit[r][c] == null || matrixProfit[r][c].shipment == null) {
//                        Profit dummy = new Profit(new Shipment(eps, transportCosts[r][c], purchaseCosts[r], sellingPrices[c], r, c), r, c);
//                        if (getClosedPath(dummy).length == 0) {
//                            matrixProfit[r][c] = dummy;
//                            return;
//                        }
//                    }
//                }
//        }
//        else
//            System.out.println("fixDegenerateCase not worked");
//    }
//
//    static double calculateRevenue() {
//        double revenue = 0;
//
//        for (Profit[] row : matrixProfit) {
//            for (Profit s : row) {
//                if (s != null && s.shipment != null) {
//                    revenue += s.shipment.sellingPrice * s.shipment.quantity;
//                }
//            }
//        }
//
//        return revenue;
//    }
//
//    static double calculateTotalCost() {
//        double totalCost = 0;
//
//        for (Profit[] row : matrixProfit) {
//            for (Profit s : row) {
//                if (s != null && s.shipment != null) {
//                    totalCost += (s.shipment.costPerUnit + s.shipment.purchaseCost) * s.shipment.quantity;
//                }
//            }
//        }
//
//        return totalCost;
//    }
//
//    static double calculateProfit() {
//        return calculateRevenue() - calculateTotalCost();
//    }
//
//    static void printMatrix() {
//        System.out.println("Matrix:");
//
//        for (Profit[] row : matrixProfit) {
//            for (Profit s : row) {
//                if (s != null && s.shipment != null) {
//                    System.out.printf("%6.1f ", s.shipment.quantity);
//                } else {
//                    System.out.print("   0   ");
//                }
//            }
//            System.out.println();
//        }
//    }
//
//    static void printResults() {
//        System.out.println("\nResults:");
//
//        System.out.println("Revenue: " + calculateRevenue());
//        System.out.println("Total Cost: " + calculateTotalCost());
//        System.out.println("Profit: " + calculateProfit());
//
////        System.out.println("\nPurchase Costs:");
////        for (int r = 0; r < supply.length; r++) {
////            System.out.println("Supplier " + (r + 1) + ": " + purchaseCosts[r]);
////        }
////
////        System.out.println("\nSelling Prices:");
////        for (int c = 0; c < demand.length; c++) {
////            System.out.println("Customer " + (c + 1) + ": " + sellingPrices[c]);
////        }
//    }


}
