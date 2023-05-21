package com.app.middleman.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData {
    int [][] individualProfits;
    int [][] optimalTransport;
    int totalCost;
    int income;
    int profit;
}
