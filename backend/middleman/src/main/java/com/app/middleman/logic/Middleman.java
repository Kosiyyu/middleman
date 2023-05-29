package com.app.middleman.logic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import static java.util.Arrays.stream;
import java.util.LinkedList;
import java.util.List;
import static java.util.stream.Collectors.toCollection;

public class Middleman {
    private static int incomeMax;
    private static int costAll;
    private static int profitAll;
    private static int[] demand;
    private static int[] supply;
    private static int[][] transportCosts;
    private static int[] purchaseCosts;
    private static int[] sellingPrices;
    private static Profit[][] matrixProfit;
    private static Shipment[][] matrix;
    private static boolean isUnBalanced;

    public Middleman(int[] supply, int[] demand, int[][] transportCosts, int[] purchaseCosts, int[] sellingPrices) {
        this.supply = supply;
        this.demand = demand;
        this.transportCosts = transportCosts;
        this.purchaseCosts = purchaseCosts;
        this.sellingPrices = sellingPrices;
    }

    public static int getIncomeMax() {
        return incomeMax;
    }

    public static int getCostAll() {
        return costAll;
    }

    public static int getProfitAll() {
        return profitAll;
    }

    public static Profit[][] getMatrixProfit() {
        return matrixProfit;
    }

    public static Shipment[][] getMatrix() {
        return matrix;
    }

    public static class Shipment {
        final int costPerUnit;
        final int purchaseCost;
        final int sellingPrice;
        final int r, c;
        int quantity;


        public Shipment(int q, int cpu, int purchaseCost, int sellingPrice, int r, int c) {
            quantity = q;
            costPerUnit = cpu;
            this.purchaseCost = purchaseCost;
            this.sellingPrice = sellingPrice;
            this.r = r;
            this.c = c;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    public static class Profit {
        int profitValue;
        Shipment shipment;
        int r, c;

        Profit(Shipment shipment, int r, int c) {
            this.shipment = shipment;
            this.profitValue = shipment.sellingPrice - shipment.costPerUnit - shipment.purchaseCost;
            this.r = r;
            this.c = c;
        }

        public int getProfitValue() {
            return profitValue;
        }

        public Shipment getShipment() {
            return shipment;
        }
    }


    static void init(int[] src, int[] dst, int[][] transportCosts1, int[] purchaseCosts1, int[] sellingPrice1) {

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
        transportCosts = new int[supply.length][demand.length];
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

            purchaseCosts = new int[3];
            purchaseCosts[0] = purchaseCosts1[0];
            purchaseCosts[1] = purchaseCosts1[1];
            purchaseCosts[2] = 0;
            sellingPrices = new int[4];
            sellingPrices[0] = sellingPrice1[0];
            sellingPrices[1] = sellingPrice1[1];
            sellingPrices[2] = sellingPrice1[2];
            sellingPrices[3] = 0;
        } else {
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
        for (int r = 0; r < supply.length; r++) {
            for (int c = 0; c < demand.length; c++) {
                if (c == demand.length - 1)
                    matrixProfit[r][c] = new Profit(new Shipment(matrix[r][c].quantity, 0, 0, 0, r, c), r, c);
                else if (matrix[r][c] != null)
                    matrixProfit[r][c] = new Profit(matrix[r][c], r, c);
                //else
                //    matrixProfit[r][c] = new Profit(new Shipment(-5, transportCosts[r][c], purchaseCosts[r], sellingPrices[c], r, c), r, c);
                if (matrixProfit[r][c] != null)
                    System.out.print(matrixProfit[r][c].profitValue + " ");
                else
                    System.out.print("- ");
            }
            System.out.println("");
        }
        System.out.println("\n");
        for (int r = 0; r < supply.length; r++) {
            for (int c = 0; c < demand.length; c++) {
                matrixProfit[r][c].shipment = null;
                //else
                //    matrixProfit[r][c] = new Profit(new Shipment(-5, transportCosts[r][c], purchaseCosts[r], sellingPrices[c], r, c), r, c);
            }
            System.out.println("");
        }
    }

    public static int findMaxValuable(Profit[] arr) {
        //for(Profit profit : arr)
        //    System.out.println(profit.profitValue);
        int index = -1;
        double maxValuable = -Double.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            if (maxValuable != Math.max(maxValuable, arr[i].profitValue)) {
                maxValuable = Math.max(maxValuable, arr[i].profitValue);
                index = i;
            }

        }
        return index;
    }

    static void maxElementMatrix() {
        Profit[] arrTmp;
        for (int r = 0, northwest = 0; r < supply.length; r++) {
            //System.out.println("Supply przed " + supply[r]);
            //System.out.println("r = " + r);
            arrTmp = matrixProfit[r];
            for (int c = northwest; c < demand.length; c++) {
                int suitableDemand = findMaxValuable(arrTmp);
                while (suitableDemand != -1 && demand[suitableDemand] == 0) {
                    arrTmp[suitableDemand].profitValue = -Integer.MAX_VALUE;
                    suitableDemand = findMaxValuable(arrTmp);
                    if (demand.length == 4)
                        if (demand[0] == 0 && demand[1] == 0 && demand[2] == 0 && demand[3] == 0 || suitableDemand == -1)
                            break;
                    if (demand[0] == 0 && demand[1] == 0 && demand[2] == 0 || suitableDemand == -1)
                        break;
                }

                int quantity = Math.min(supply[r], demand[suitableDemand]);
                //System.out.println("index = " + suitableDemand + " quantity " + quantity);
                if (quantity > 0) {
                    if (demand.length - 1 == c)
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
                arrTmp[suitableDemand].profitValue = -Integer.MAX_VALUE;
            }
        }

        for (int r = 0; r < supply.length; r++) {
            for (int c = 0; c < demand.length; c++) {
                if (matrixProfit[r][c].shipment != null)
                    System.out.print(matrixProfit[r][c].shipment.quantity + " ");
                else
                    System.out.print("x ");
            }
            System.out.println("");
        }

        for (int r = 0; r < supply.length; r++) {
            for (int c = 0; c < demand.length; c++) {
                matrix[r][c] = matrixProfit[r][c].shipment;
                if (demand.length - 1 == c)
                    matrixProfit[r][c].profitValue = 0;
                else
                    matrixProfit[r][c].profitValue = sellingPrices[c] - purchaseCosts[r] - transportCosts[r][c];
                // System.out.print(matrixProfit[r][c].profitValue + " ");
            }
            //System.out.print("\n");
        }
    }

    static void calcTheBestIncome() {
        for (int r = 0; r < supply.length; r++) {
            for (int c = 0; c < demand.length; c++) {
                if (matrixProfit[r][c].shipment == null && demand.length - 1 != c)
                    matrixProfit[r][c].shipment = new Shipment(0, transportCosts[r][c], purchaseCosts[r], sellingPrices[c], r, c);
                else if (matrixProfit[r][c].shipment == null)
                    matrixProfit[r][c].shipment = new Shipment(0, 0, 0, 0, r, c);
            }
        }
        Profit[][] matrixProfitTmp = new Profit[supply.length][demand.length];
        for (int r = 0; r < supply.length; r++) {
            for (int c = 0; c < demand.length; c++) {
                matrixProfitTmp[r][c] = matrixProfit[r][c];
            }
        }

        int income = 0;
        int profit = 0;
        int cost = 0;
        incomeMax = 0;
        profitAll = 0;
        costAll = 0;
        for (int r = 0; r < supply.length; r++) {
            for (int c = 0; c < demand.length; c++) {
                incomeMax += matrixProfit[r][c].shipment.sellingPrice * matrixProfit[r][c].shipment.quantity;
                profitAll += matrixProfit[r][c].profitValue * matrixProfit[r][c].shipment.quantity;
                costAll += (matrixProfit[r][c].shipment.costPerUnit + matrixProfit[r][c].shipment.purchaseCost) * matrixProfit[r][c].shipment.quantity;
            }
        }
        for (int r = 0; r < supply.length; r++) {
            for (int c = 0; c < demand.length; c++) {
                if (matrixProfitTmp[r][c].profitValue < 0 && matrixProfitTmp[r][c].shipment.quantity > 0) {
                    matrixProfitTmp[r][demand.length - 1].shipment.quantity += matrixProfitTmp[r][c].shipment.quantity;
                    matrixProfitTmp[supply.length - 1][c].shipment.quantity += matrixProfitTmp[r][c].shipment.quantity;
                    matrixProfitTmp[r][c].shipment.quantity = 0;
                }
                income += matrixProfitTmp[r][c].shipment.sellingPrice * matrixProfitTmp[r][c].shipment.quantity;
                profit += matrixProfitTmp[r][c].profitValue * matrixProfitTmp[r][c].shipment.quantity;
                cost += (matrixProfitTmp[r][c].shipment.costPerUnit + matrixProfitTmp[r][c].shipment.purchaseCost) * matrixProfitTmp[r][c].shipment.quantity;
            }
        }
        if (profit > profitAll) {
            for (int r = 0; r < supply.length; r++) {
                for (int c = 0; c < demand.length; c++) {
                    matrixProfit[r][c] = matrixProfitTmp[r][c];
                    matrix[r][c] = matrixProfit[r][c].shipment;
                    incomeMax = income;
                    profitAll = profit;
                    costAll = cost;
                }
            }
        }
    }

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

    public void executeCalculation()
    {
        init(supply, demand, transportCosts, purchaseCosts, sellingPrices);
        maxElementMatrix();
        calcTheBestIncome();
        printResult();
    }

    public static void main(String[] args) {
        int[] src = {20, 30};
        int[] dst = {10, 28, 27};
        int[][] transportCosts = {{8, 14, 17}, {12, 9, 19}};
        int[] purchaseCosts = {10, 12};
        int[] sellingPrice = {30, 25, 30};

        init(src, dst, transportCosts, purchaseCosts, sellingPrice);
        maxElementMatrix();
        calcTheBestIncome();
        //initShipmentMatrixWithFiction();
        //steppingStone();
        //calcAlfaAndBeta();
        //printMatrix();
        printResult();
    }
}
