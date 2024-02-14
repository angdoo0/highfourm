import React, { useState, useEffect, } from 'react';
import { AudioOutlined } from '@ant-design/icons'; //PDF 파일용. 삭제x

import { Space } from 'antd';
import axios from 'axios';
import PageTitle from '../../Common/PageTitle';
import { BtnBlack, BtnFilter, SearchInput, SearchSelectBox } from '../../Common/Module';
import BasicTable from '../../Common/Table/BasicTable';
import { Document, Page } from 'react-pdf'; //PDF파일용. 삭제 x
import { useNavigate, useLocation, useParams } from 'react-router';
const onSearch = (value, _e, info) => console.log(info?.source, value);

const OrderList = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [orders, setOrders] = useState([]);
  const { orderId } = useParams();
  const searchParams = new URLSearchParams(location.search);
  const searchTypeParam = searchParams.get('searchType');
  const searchValueParam = searchParams.get('search');
  const [searchType, setSearchType] = useState('주문 번호');
  const currentURL = window.location.pathname;

  useEffect(() => {
    async function fetchData() {
      try {
        let res;

        if (currentURL === '/orders/search') {
          res = await axios.get('/api/orders/search', {
            params: {
              searchType: searchTypeParam,
              search: searchValueParam,
            },
          });
        } else {
          res = await axios.get('/api/orders');
        }

        const { orders, productNames } = res.data;
        const updatedOrders = orders.map(order => {
          const detailsWithProductName = order.orderDetails.map(detail => ({
            ...detail,
            productName: productNames[detail.productId]
          }));
          return { ...order, orderDetails: detailsWithProductName };
        });
        setOrders(updatedOrders);
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
      title: '담당자',
      dataIndex: 'manager',
    },
    {
      title: '품목',
      dataIndex: 'orderDetails',
      render: details => {
        console.log(details);
        if (details.length === 1) {
          return <span>{details[0].productName}</span>;
        } else if (details.length > 1) {
          return <span>{`${details[0].productName} 외 ${details.length - 1}건`}</span>;
        }
      }
    },
    {
      title: '주문일',
      dataIndex: 'orderDate',
    },
    {
      title: '납기일',
      dataIndex: 'dueDate',
    },
    {
      title: '금액',
      dataIndex: 'orderDetails',
      render: details => {
        const totalPrice = details.reduce((sum, detail) => {
          return sum + (detail.productAmount * detail.unitPrice);
        }, 0);
        return <span>{totalPrice.toLocaleString()}원</span>;
      }
    },
    {
      title: '상태',
      dataIndex: 'endingState',
      render: endingState => (endingState ? "완료" : "진행중")
    },
  ];

  const SelectChangeHandler = (value) => {
    setSearchType(value);
  };

  const onSearch = (value) => {
    navigate(`/orders/search?searchType=${encodeURIComponent(searchType)}&search=${encodeURIComponent(value)}`);
  }


  return (
    <>
      <PageTitle value={'주문 관리'} />
      <div style={{ display: 'flex', gap: '10px 24px', alignItems: 'center' }}>
        <SearchSelectBox selectValue={['주문 번호', '거래처명', '담당자', '품목']} SelectChangeHandler={SelectChangeHandler} />
        <SearchInput id={'search'} name={'search'} onSearch={onSearch} />
      </div>
      <div className='order-list-page'>
        <div className='order-list-wrap'>
          <div className='order-new'>
            <BtnBlack value={'주문 등록'} onClick={() => window.location.href = '/orders/new'} />
          </div>
        </div>
      </div>
      <div className='.clickable-table tbody'>
       <BasicTable dataSource={orders} defaultColumns={defaultColumns} />
      </div>
    </>
  )
}
export default OrderList;