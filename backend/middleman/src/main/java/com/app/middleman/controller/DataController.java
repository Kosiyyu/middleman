package com.app.middleman.controller;

import com.app.middleman.model.RequestData;
import com.app.middleman.model.ResponseData;
import com.app.middleman.service.DataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/middleman/")
public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService){
        this.dataService = dataService;
    }

    @PostMapping
    public ResponseEntity<ResponseData> post(@RequestBody RequestData requestData){
        try {

            return ResponseEntity.status(200).body(dataService.calculate(requestData));

        }
        catch (Exception e){
            return ResponseEntity.status(400).body(null);
        }
    }
}
