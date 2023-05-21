import React from 'react';

const Result = ({ data }) => {
  const { individualProfits, optimalTransport, totalCost, income, profit } = data;

  return (
    <div>
      <h2>Individual Profits</h2>
      <table>
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
      <table>
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

      <h2>Total Cost</h2>
      <p>{totalCost}</p>

      <h2>Income</h2>
      <p>{income}</p>

      <h2>Profit</h2>
      <p>{profit}</p>
    </div>
  );
};

export default Result;
