import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate, useLocation } from 'react-router-dom';
import { Modal } from 'antd';
import { BtnBlack, SearchInput, SearchSelectBox } from '../../Common/Module';
import BasicTable from '../../Common/Table/BasicTable';
import StockNew from './StockNew';
import PageTitle from '../../Common/PageTitle';

const StockList = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [dataSource, setDataSource] = useState([]);
  const [searchType, setSearchType] = useState('자재코드');
  const currentURL = window.location.pathname;
  const location = useLocation();
  const navigate = useNavigate();

  const formatNumber = (number) => {
    if (typeof number === 'undefined' || number === null) {
      return '';
    }
    return number.toLocaleString('ko-KR');
  };

  const defaultColumnsOne = [
    {
      title: '자재 코드',
      dataIndex: 'materialId',
      render: (text, record) => {
        if (record.isBelowSafetyStock) {
          return <span style={{ fontWeight: 'bold' }}>{text}</span>;
        }
        return text;
      },
    },
    {
      title: '자재명',
      dataIndex: 'materialName',
      render: (text, record) => {
        if (record.isBelowSafetyStock) {
          return <span style={{ fontWeight: 'bold' }}>{text}</span>;
        }
        return text;
      },
    },
    {
      title: '총재고량',
      dataIndex: 'totalStock',
      render: (text, record) => {
        formatNumber(text);
        if (record.isBelowSafetyStock) {
          return <span style={{ color: 'red', fontWeight: 'bold' }}>{text}</span>;
        }
        return text;
      },
    },
    {
      title: '단위',
      dataIndex: 'unit',
    },
    {
      title: '재고관리 방식',
      dataIndex: 'managementName',
    },
    {
      title: '안전재고',
      dataIndex: 'safetyStock',
      render: (text) => formatNumber(text),
    },
    {
      title: '최대재고',
      dataIndex: 'maxStock',
      render: (text) => formatNumber(text),
    },
    {
      title: 'LeadTime',
      dataIndex: 'leadTime',
    },
  ];

  useEffect(() => {
    async function fetchData() {
      try {
        let res;

        if (currentURL === '/materials/stock/search') {
          const searchParams = new URLSearchParams(location.search);
          const searchTypeParam = searchParams.get('searchType');
          const searchValueParam = searchParams.get('search');

          res = await axios.get('/api/materials/stock/search', {
            params: {
              searchType: searchTypeParam,
              search: searchValueParam,
            },
          });
        } else {
          res = await axios.get('/api/materials/stock');
        }

        const materialRequest = await res.data.map((rowData) => ({
          key: rowData.materialId,
          materialId: rowData.materialId,
          materialName: rowData.materialName,
          unit: rowData.unit,
          totalStock: rowData.totalStock,
          managementName: rowData.managementName,
          safetyStock: rowData.safetyStock,
          maxStock: rowData.maxStock,
          leadTime: rowData.leadTime,
          isBelowSafetyStock: rowData.totalStock <= rowData.safetyStock * 1.1,
        }));

        const sortedData = [...materialRequest];
        sortedData.sort((a, b) => {
          if (a.isBelowSafetyStock) return -1;
          return 1;
        });

        setDataSource(sortedData);
      } catch (e) {
        console.error(e.message);
      }
    }
    fetchData();
  }, [currentURL, location.search]);

  const SelectChangeHandler = (value) => {
    setSearchType(value);
  };

  const onSearch = (value) => {
    navigate(`/materials/stock/search?searchType=${encodeURIComponent(searchType)}&search=${encodeURIComponent(value)}`);
  }

  //원자재 등록 버튼 클릭 시 모달 오픈
  const showModal = () => {
    setIsModalOpen(true);
    document.body.style.overflow = 'hidden';
  };

  const handleStockNewSubmit = () => {
    setIsModalOpen(false);
  };
  const handleCancel = () => {
    setIsModalOpen(false);
  };

  return (
    <div>
      <PageTitle value={'원자재관리 > 조회/등록'} />
      <div style={{ display: 'flex', gap: '12px', marginBottom: '15px' }}>
        <SearchSelectBox selectValue={['자재코드', '자재명', '재고관리 방식']} SelectChangeHandler={SelectChangeHandler} />
        <SearchInput id={'search'} name={'search'} onSearch={onSearch} />
      </div>
      <div style={{ marginBottom: '24px' }}>
        <BtnBlack value={'원자재 등록'} onClick={showModal} />
        <Modal
          title='원자재 등록'
          open={isModalOpen}
          footer={null}
          onCancel={handleCancel}
          width='50%'
        >
          <StockNew onSubmit={handleStockNewSubmit} />
        </Modal>
      </div>
      <div style={{ width: '1200px', display: 'flex', gap: '10px', flexDirection: 'column' }} >
        <BasicTable
          dataSource={dataSource}
          defaultColumns={defaultColumnsOne}
          setDataSource={setDataSource}
          rowClassName={(record) => (record.isBelowSafetyStock ? 'below-safety-stock' : '')}
        />

      </div>
    </div>
  )
};

export default StockList;