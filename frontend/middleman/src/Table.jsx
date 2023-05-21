import React, { useState } from 'react'

const Table = ({ onSendData, dataLimit }) => {
  const [data, setData] = useState([])
  const [inputValue, setInputValue] = useState('')

  const handleAddData = () => {
    if (data.length < dataLimit && inputValue.trim() !== '') {
      setData([...data, inputValue])
      setInputValue('')
    }
  }
  
  const handleSendData = () => {
    onSendData(data)
  }

  const handleReset = () => {
    setData([])
    setInputValue('')
  }

  return (
    <div>
      <table>
        <thead>
          <tr>
            <th>Value</th>
          </tr>
        </thead>
        <tbody>
          {data.map((value, index) => (
            <tr key={index}>
              <td>{value}</td>
            </tr>
          ))}
        </tbody>
      </table>
      <div>
        <input
          type="number"
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
        />
        <button onClick={handleAddData} disabled={data.length >= dataLimit}>
          Add
        </button>
        <button onClick={handleSendData} disabled={data.length !== dataLimit}>Apply</button>
        <button onClick={handleReset}>Reset</button>
      </div>
    </div>
  )
}

export default Table
