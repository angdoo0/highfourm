import React from 'react';
import { Table } from 'antd';
import EditableRow from './EditableRow';
import EditableCell from './EditableCell';

const BasicTable = ({ dataSource, defaultColumns, setDataSource, pagination, onRowClick }) => {
  const handleSave = (row) => {
    const newData = [...dataSource];
    const index = newData.findIndex((item) => row.key === item.key);
    const item = newData[index];
    newData.splice(index, 1, {
      ...item,
      ...row,
    });
    setDataSource(newData);
  };
  const components = {
    body: {
      row: EditableRow,
      cell: EditableCell,
    },
  };
  const columns = defaultColumns.map((col) => {
    if (!col.editable) {
      return col;
    }
    return {
      ...col,
      onCell: (record) => ({
        record,
        editable: col.editable,
        dataIndex: col.dataIndex,
        title: col.title,
        handleSave,
      }),

    };
  });

  const onRow = (record, rowIndex) => {
    return {
      onClick: event => {
        onRowClick && onRowClick(record, rowIndex, event);
      },
    };
  };

  const rowClassName = (record, index) => {
    return `clickable-row ${record.isBelowSafetyStock ? 'below-safety-stock' : ''}`
  };

  return (
    <div>
      <Table
        components={components}
        rowClassName={rowClassName} // 수정된 부분
        bordered
        dataSource={dataSource}
        columns={columns}
        size="middle"
        pagination={pagination}
        onRow={onRow}
      />
    </div>
  );

};

export default BasicTable;