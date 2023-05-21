package com.app.middleman.service;

import com.app.middleman.model.RequestData;
import com.app.middleman.model.ResponseData;
import org.springframework.stereotype.Service;

@Service
public class DataService {

    public ResponseData calculate(RequestData requestData){
        //tutaj wkladasz logie
        ResponseData responseData = new ResponseData();
        return responseData;
    }
}
