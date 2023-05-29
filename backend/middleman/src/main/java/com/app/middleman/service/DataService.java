package com.app.middleman.service;

import com.app.middleman.logic.Middleman;
import com.app.middleman.model.RequestData;
import com.app.middleman.model.ResponseData;
import org.springframework.stereotype.Service;

@Service
public class DataService {

    public ResponseData calculate(RequestData requestData){
        //tutaj wkladasz logie
        Middleman middleman = new Middleman(requestData.getSupply(), requestData.getDemand(), requestData.getTransportCost(), requestData.getPurchaseCost(), requestData.getSellingCost());
        middleman.executeCalculation();
        ResponseData responseData = new ResponseData();
        responseData.setIncome(middleman.getIncomeMax());
        responseData.setProfit(middleman.getProfitAll());
        responseData.setTotalCost(middleman.getCostAll());
        Middleman.Profit[][] matrixProfit = middleman.getMatrixProfit();
        int[][] profitScalarMatrix = new int[requestData.getSupply().length][requestData.getDemand().length];
        int[][] optimalTransportScalarMatrix = new int[requestData.getSupply().length][requestData.getDemand().length];
        for(int r = 0; r < requestData.getSupply().length; r++)
        {
            for(int c = 0; c < requestData.getDemand().length; c++)
            {
                profitScalarMatrix[r][c] = matrixProfit[r][c].getProfitValue();
                optimalTransportScalarMatrix[r][c] = matrixProfit[r][c].getShipment().getQuantity();
            }
        }

        responseData.setOptimalTransport(optimalTransportScalarMatrix);
        responseData.setIndividualProfits(profitScalarMatrix);

        return responseData;
    }
}
