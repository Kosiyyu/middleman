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
  const [dataResponse, setDataResponse] = useState(null)

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
      }

    console.log(JSON.stringify(data))
    const dataResponse = await sendRequest(JSON.stringify(data)).then().catch(console.log)
    setDataResponse(dataResponse)
  }

  return (
    <div>
      <h1 className="display-4">Middleman</h1>
      <hr className="my-4"></hr>
      <div>
        <h1>Input</h1>
        <h4>Demand</h4>
        <Table onSendData={(data) => handleTableData(data, 'demand')} dataLimit={3} />
        <h4>Selling Cost</h4>
        <Table onSendData={(data) => handleTableData(data, 'sellingCost')} dataLimit={3} />
        <h4>Supply</h4>
        <Table onSendData={(data) => handleTableData(data, 'supply')} dataLimit={2} />
        <h4>Purchase Cost</h4>
        <Table onSendData={(data) => handleTableData(data, 'purchaseCost')} dataLimit={2} />
        <h4>Transport Cost</h4>
        <Table2d onSendData={(data) => handleTableData(data, 'transportCost')}></Table2d>
        <br className="my-4"></br>
        <button onClick={handleFormData} className="btn btn-primary me-2">Send Data</button>
      </div>
      <hr className="my-4"></hr>
      <div>
        <h1>Result</h1>
        {dataResponse && <Result data={dataResponse} />}
      </div>
    </div>
  )
}

export default Form
