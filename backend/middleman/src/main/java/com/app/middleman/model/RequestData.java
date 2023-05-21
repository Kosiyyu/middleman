package com.app.middleman.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestData {
    private int[] demand;
    private int[] sellingCost;
    private int[] supply;
    private int[] purchaseCost;
    private int[][] transportCost;
}
