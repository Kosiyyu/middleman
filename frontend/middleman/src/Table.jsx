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
    <div className="container">
    <table className="table table-bordered">
      <thead className="table-dark">
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
    <div className="d-flex justify-content-center">
      <input
        type="number"
        value={inputValue}
        onChange={(e) => setInputValue(e.target.value)}
        className="form-control me-2"
      />
      <button onClick={handleAddData} disabled={data.length >= dataLimit} className="btn btn-primary me-2">
        Add
      </button>
      <button onClick={handleSendData} disabled={data.length !== dataLimit} className="btn btn-primary me-2">
        Apply
      </button>
      <button onClick={handleReset} className="btn btn-secondary">
        Reset
      </button>
    </div>
  </div>
  )
}

export default Table
