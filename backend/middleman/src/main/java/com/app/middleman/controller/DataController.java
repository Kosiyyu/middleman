package com.app.middleman.controller;

import com.app.middleman.model.RequestData;
import com.app.middleman.model.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/middleman/")
public class DataController {

    @PostMapping
    public ResponseEntity<ResponseData> post(@RequestBody RequestData requestData){
        //do sth with requestData


//        System.out.println("Demand:");
//        for(int i : requestData.getDemand()){
//            System.out.println(i + " ");
//        }
//        System.out.println("Selling Cost:");
//        for(int i : requestData.getSellingCost()){
//            System.out.println(i + " ");
//        }
//        System.out.println("Supply:");
//        for(int i : requestData.getSupply()){
//            System.out.println(i + " ");
//        }
//        System.out.println("Purchase Cost:");
//        for(int i : requestData.getPurchaseCost()){
//            System.out.println(i + " ");
//        }
//        System.out.println("Transport Cost:");
//        for(int i = 0; i < requestData.getTransportCost().length; i++) {
//            for(int j = 0; j < requestData.getTransportCost()[i].length; j++) {
//                System.out.print(requestData.getTransportCost()[i][j] + " ");
//            }
//            System.out.println();
//        }



        //service todo

        //dummy response
        int[][] individualProfits = {
                {12, 1, 3},
                {6, 4, -1}
        };

        int[][] optimalTransport = {
                {10, 0, 10},
                {0, 13, 17}
        };

        int totalCost = 1250;
        int income = 1435;
        int profit = 185;

        ResponseData responseData = new ResponseData(individualProfits, optimalTransport, totalCost, income, profit);
        return ResponseEntity.status(201).body(responseData);
    }
}
