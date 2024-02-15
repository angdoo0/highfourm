import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate, useLocation } from 'react-router-dom';
import { BtnBlack, SearchInput, SearchSelectBox } from '../../Common/Module';
import BasicTable from '../../Common/Table/BasicTable';
import KeyTable from '../../Common/Table/KeyTable';
import PageTitle from '../../Common/PageTitle';

const MaterialOrderHistory = () => {

  const [dataSource, setDataSource] = useState([]);
  const [waitingData, setWaitingData] = useState([]);
  const [completedData, setCompletedData] = useState([]);
  const [searchType, setSearchType] = useState('자재코드');
  const currentURL = window.location.pathname;
  const location = useLocation();
  const navigate = useNavigate();


  useEffect(() => {
    async function fetchData() {
      try {
        let res;

        if (currentURL === '/materials/order-history/search') {
          const searchParams = new URLSearchParams(location.search);
          const searchTypeParam = searchParams.get('searchType');
          const searchValueParam = searchParams.get('search');

          res = await axios.get('/api/materials/order-history/search', {
            params: {
              searchType: searchTypeParam,
              search: searchValueParam,
            },
          });
        } else {
          res = await axios.get('/api/materials/order-history');
        }


        const materialRequest = await res.data.map((rowData) => ({
          key: rowData.materialHistoryId,
          materialHistoryId: rowData.materialHistoryId,
          orderDate: rowData.orderDate,
          recievingDate: rowData.recievingDate,
          materialId: rowData.materialId,
          materialName: rowData.materialName,
          standard: rowData.standard,
          unit: rowData.unit,
          supplier: rowData.supplier,
          restStock: rowData.restStock,
          materialInventory: rowData.materialInventory,
          usedAmount: rowData.LeausedAmountdTime,
          inboundAmount: rowData.inboundAmount,
          orderAmount: rowData.orderAmount,
          unitPrice: rowData.unitPrice,
          note: rowData.note,
          totalPrice: rowData.totalPrice,
        }));
        setDataSource(materialRequest);
        filterData(materialRequest)
      } catch (e) {
        console.error(e.message);
      }
    }
    fetchData();
  }, [currentURL, location.search]);

  const filterData = (data) => {
    const waitingData = data.filter(item => !item.inboundAmount && !item.recievingDate);
    const completedData = data.filter(item => item.inboundAmount && item.recievingDate);

    setWaitingData(waitingData);
    setCompletedData(completedData);
  };

  const formatNumber = (number) => {
    if (typeof number === 'undefined' || number === null) {
      return '';
    }
    return number.toLocaleString('ko-KR');
  };

  const defaultColumnsOne = [
    {
      title: '발주코드',
      dataIndex: 'materialHistoryId',
      render: (text, record) => <a href={`/materials/order-history/edit/${record.materialHistoryId}`}>{text}</a>,
    },
    {
      title: '발주일',
      dataIndex: 'orderDate',
    },
    {
      title: '입고일',
      dataIndex: 'recievingDate',
    },
    {
      title: '자재코드',
      dataIndex: 'materialId',
    },
    {
      title: '자재명',
      dataIndex: 'materialName',
    },
    {
      title: '단위',
      dataIndex: 'unit',
    },
    {
      title: '공급처',
      dataIndex: 'supplier',
    },
    {
      title: '이월재고량',
      dataIndex: 'restStock',
      render: (text) => formatNumber(text),
    },
    {
      title: '재고량',
      dataIndex: 'materialInventory',
      render: (text) => formatNumber(text),
    },
    {
      title: '사용량',
      dataIndex: 'usedAmount',
      render: (text) => formatNumber(text),
    },
    {
      title: '입고량',
      dataIndex: 'inboundAmount',
      render: (text) => formatNumber(text),
    },
    {
      title: '발주량',
      dataIndex: 'orderAmount',
      render: (text) => formatNumber(text),
    },
    {
      title: '규격/사양',
      dataIndex: 'standard',
    },
    {
      title: '입고 단가',
      dataIndex: 'unitPrice',
      render: (text) => `${formatNumber(text)}원`,
    },
    {
      title: '금액',
      dataIndex: 'totalPrice',
      render: (text) => `${formatNumber(text)}원`,
    },
    {
      title: '비고',
      dataIndex: 'note',
    },
  ];

  const SelectChangeHandler = (value) => {
    setSearchType(value);
  };

  const onSearch = (value) => {
    navigate(`/materials/order-history/search?searchType=${encodeURIComponent(searchType)}&search=${encodeURIComponent(value)}`);
  }

  return (
    <div>
      <PageTitle value={'원자재 관리 < 수급 내역 관리'} />
      <div style={{ display: 'flex', gap: '12px', marginBottom: '15px' }}>
        <SearchSelectBox selectValue={['자재코드', '자재명', '발주일', '입고일']} SelectChangeHandler={SelectChangeHandler} />
        <SearchInput id={'search'} name={'search'} onSearch={onSearch} />
      </div>
      <div style={{ marginBottom: '24px' }}>
        <BtnBlack value={'수급내역 등록'} onClick={() => window.location.href = '/materials/order-history/new'} />
      </div>
      <div style={{ display: 'flex', flexDirection: 'column' }}>
        <div style={{ border: '1px solid #d9d9d9', padding: '30px 35px', marginBottom: '20px' }}>
          <div style={{ display: 'flex', alignItems: 'center' }}>
            <h2 style={{ fontSize: '16px', display: 'inline-block' }}>입고대기</h2>
            <hr style={{ color: '#000', width: '520px', marginLeft: '20px' }} />
          </div>
          <div style={{ marginTop: '20px', height: '300px', overflowY: 'auto' }} className ="clickable-table">
            <KeyTable 
              dataSource={waitingData} 
              defaultColumns={defaultColumnsOne} 
              setDataSource={setDataSource} 
              url="materials/order-history/edit"
              keyName="materialHistoryId"
              />
          </div>
        </div>
        <div style={{ border: '1px solid #d9d9d9', padding: '30px 35px', marginBottom: '20px' }}>
          <div style={{ display: 'flex', alignItems: 'center' }}>
            <h2 style={{ fontSize: '16px', display: 'inline-block' }}>입고완료</h2>
            <hr style={{ color: '#000', width: '520px', marginLeft: '20px' }
            } />
          </div>
          <div style={{ marginTop: '20px', height: '300px', overflowY: 'auto' }}>
            <BasicTable dataSource={completedData} defaultColumns={defaultColumnsOne} setDataSource={setDataSource} />
          </div>
        </div>
      </div>
    </div>
  )
}

export default MaterialOrderHistory
