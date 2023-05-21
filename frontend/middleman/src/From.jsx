import React, { useState } from 'react'
import { sendRequest } from './api'
import Table from './Table'
import Table2d from './Table2d'
import Result from './Result'

const Form = () => {
  const [demandData, setDemandData] = useState([])
  const [sellingCostData, setSellingCostData] = useState([])
  const [supplyData, setSupplyData] = useState([])
  const [purchaseCostData, setPpurchaseCostData] = useState([])
  const [transportCostData, setTransportCostData] = useState([])
  const [dataResponse, setDataResponse] = useState(null);

  const handleTableData = (data, tableType) => {
    if (tableType === 'demand') {
      setDemandData(data)
    } else if (tableType === 'sellingCost') {
      setSellingCostData(data)
    } else if (tableType === 'supply') {
        setSupplyData(data)
    } else if (tableType === 'purchaseCost') {
        setPpurchaseCostData(data)
    } else if (tableType === 'transportCost') {
        setTransportCostData(data)
    }
  }

  async function handleFormData() {
    console.log('Demand Data:', demandData)
    console.log('Selling Cost Data:', sellingCostData)
    console.log('Supply Data:', supplyData)
    console.log('Purchase Cost Data:', purchaseCostData)
    console.log('Transport Cost Data:', transportCostData)

    const data = {
        demand: demandData,
        sellingCost: sellingCostData,
        supply: supplyData,
        purchaseCost: purchaseCostData,
        transportCost: transportCostData,
      };

    console.log(JSON.stringify(data));
    const dataResponse = await sendRequest(JSON.stringify(data)).then().catch(console.log);
    setDataResponse(dataResponse)
  }

  return (
    <div>
      <h1>Middleman</h1>
      <hr></hr>
      <div>
        <h2>Input</h2>
        <h3>Demand</h3>
        <Table onSendData={(data) => handleTableData(data, 'demand')} dataLimit={3} />
        <h3>Selling Cost</h3>
        <Table onSendData={(data) => handleTableData(data, 'sellingCost')} dataLimit={3} />
        <h3>Supply</h3>
        <Table onSendData={(data) => handleTableData(data, 'supply')} dataLimit={2} />
        <h3>Purchase Cost</h3>
        <Table onSendData={(data) => handleTableData(data, 'purchaseCost')} dataLimit={2} />
        <h3>Transport Cost</h3>
        <Table2d onSendData={(data) => handleTableData(data, 'transportCost')}></Table2d>
        <br></br>
        <button onClick={handleFormData}>Send Data</button>
      </div>
      <hr></hr>
      <div>
        <h2>Result</h2>
        {dataResponse && <Result data={dataResponse} />}
      </div>
    </div>
  )
}

export default Form
