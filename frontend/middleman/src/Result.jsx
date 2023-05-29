import React from 'react'

const Result = ({ data }) => {
  const { individualProfits, optimalTransport, totalCost, income, profit } = data


  return (
    <div className="container">
      <h2>Individual Profits</h2>
      <table className="table table-bordered">
        <tbody>
          {individualProfits.map((row, rowIndex) => (
            <tr key={rowIndex}>
              {row.map((value, columnIndex) => (
                <td key={columnIndex}>{value}</td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
      <h2>Optimal Transport</h2>
      <table className="table table-bordered">
        <tbody>
          {optimalTransport.map((row, rowIndex) => (
            <tr key={rowIndex}>
              {row.map((value, columnIndex) => (
                <td key={columnIndex}>{value}</td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
      <h2>Financial result</h2>
      <table className="table table-bordered">
        <tbody>
          <tr>
            <th>Total Cost</th>
            <td>{totalCost}</td>
          </tr>
          <tr>
            <th>Income</th>
            <td>{income}</td>
          </tr>
          <tr>
            <th>Profit</th>
            <td>{profit}</td>
          </tr>
        </tbody>
      </table>
    </div>
  )
}

export default Result
