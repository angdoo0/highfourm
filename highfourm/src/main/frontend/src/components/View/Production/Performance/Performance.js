import React, { useState, useEffect } from 'react';
import { Input, Space } from 'antd';
import axios from 'axios';
import { DownOutlined, UserOutlined } from '@ant-design/icons';
import { Button, Dropdown, message, Tooltip, Popconfirm, Table, FLex } from 'antd';
import { BtnBlack, BtnBlue, BtnWhite, BtnFilter, InputBar, StepBar, SearchSelectBox, SearchInput } from '../../../Common/Module';
import { useNavigate, useLocation, useParams } from 'react-router';
import BasicTable from '../../../Common/Table/BasicTable';
import PageTitle from '../../../Common/PageTitle';


const Performance = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const [performance, setPerformance] = useState([]);
  const [searchType, setSearchType] = useState('주문 번호');
  const searchTypeParam = searchParams.get('searchType');
  const searchValueParam = searchParams.get('search');
  const currentURL = window.location.pathname;

  useEffect(() => {
    async function fetchData() {
      try {
        let res;

        if (currentURL === '/production-performance/search') {
          res = await axios.get('/api/production-performance/search', {
            params: {
              searchType: searchTypeParam,
              search: searchValueParam,
            },
          });
        } else {
          res = await axios.get('/api/production-performance');
        }

        const performanceData = res.data;
        if (performanceData) {
          setPerformance(performanceData);
        } else {
          console.log('No performance data received:', res.data);
        }
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    }
    fetchData();
  }, [location, searchTypeParam, searchValueParam]);

  const defaultColumns = [
    {
      title: '주문 번호',
      dataIndex: 'orderId',
    },
    {
      title: '거래처명',
      dataIndex: 'vendor',
    },
    {
      title: '품명',
      dataIndex: 'productName',
    },
    {
      title: '주문일',
      dataIndex: 'orderDate',
    },
    {
      title: '착수일',
      dataIndex: 'productionStartDate',
      render: (processedData) => processedData ? processedData : '착수 전',
    },
    {
      title: '납기일',
      dataIndex: 'dueDate',
    },
    {
      title: '주문수량',
      dataIndex: 'productAmount',
    },
    {
      title: '생산수량',
      dataIndex: 'totalProductionAmount',
    },
    {
      title: '진행 상태',
      dataIndex: 'endingState',
      render: (endingState) => endingState == true ? '완료' : '진행 중',
    },
    {
      title: '통계조회',
      dataIndex: 'productionPlanId',
      render: (productionPlanId) => <a href={`/production-performance/${productionPlanId}/chart`}>보기</a>
    },
  ];


  const SelectChangeHandler = (value) => {
    setSearchType(value);
  };

  const onSearch = (value) => {
    navigate(`/production-performance/search?searchType=${encodeURIComponent(searchType)}&search=${encodeURIComponent(value)}`);
  }

  return (
    <div className='perfomance-page'>
      <PageTitle value={'생산 실적 조회'} />
      <div style={{ display: 'flex', gap: '12px', marginBottom: '15px' }}>
        <SearchSelectBox selectValue={['주문 번호', '거래처명', '품명']} SelectChangeHandler={SelectChangeHandler} />
        <SearchInput id={'search'} name={'search'} onSearch={onSearch} />
      </div>
      <div>
        <BasicTable dataSource={performance} defaultColumns={defaultColumns} />
      </div>
    </div>
  );
};

export default Performance;