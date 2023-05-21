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
