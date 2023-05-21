import React, { useState } from 'react';

const Table2d = ({ onSendData }) => {
  const initialArray = [[null, null, null], [null, null, null]];
  const [array, setArray] = useState(initialArray);
  const [selectedCell, setSelectedCell] = useState({ row: null, column: null });

  const handleCellClick = (row, column) => {
    setSelectedCell({ row, column });
  };

  const handleNumberChange = (event) => {
    const { value } = event.target;
    const newArray = [...array];
    newArray[selectedCell.row][selectedCell.column] = value !== '' ? Number(value) : null;
    setArray(newArray);
  };
  

  const handleSendData = () => {
    onSendData(array);
  };

  const handleReset = () => {
    setArray(initialArray);
    setSelectedCell({ row: null, column: null });
  };

  const isAnyCellEmpty = array.some(row => row.some(cell => cell === null));

  return (
    <div>
      <table>
        <thead>
          <tr>
            <th>Value</th>
          </tr>
        </thead>
        <tbody>
          {array.map((row, rowIndex) => (
            <tr key={rowIndex}>
              {row.map((value, columnIndex) => (
                <td
                  key={columnIndex}
                  onClick={() => handleCellClick(rowIndex, columnIndex)}
                  className={selectedCell.row === rowIndex && selectedCell.column === columnIndex ? 'selected' : ''}
                >
                  <input type="number" value={value || ''} onChange={handleNumberChange} />
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
      <div>
        <button onClick={handleSendData} disabled={isAnyCellEmpty}>
          Apply
        </button>
        <button onClick={handleReset}>Reset</button>
      </div>
    </div>
  );
};

export default Table2d;
