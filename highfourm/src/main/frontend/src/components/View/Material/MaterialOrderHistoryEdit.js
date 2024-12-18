import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { BtnBlue, BtnWhite, InputBar } from '../../Common/Module';
import axios from 'axios';
import PageTitle from '../../Common/PageTitle';

const MaterialOrderHistoryEdit = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    recievingDate: '',
    inboundAmount: '',
    note: '',
    materialInventory: '',
  });
  const [dataSource, setDataSource] = useState([]);
  const { orderHistoryId } = useParams()

  useEffect(() => {
    async function fetchData() {
      try {
        const res = await axios.get(`/api/materials/order-history/edit/${orderHistoryId}`);
        const materialRequest = {
          key: res.data.materialHistoryId,
          materialHistoryId: res.data.materialHistoryId,
          orderDate: res.data.orderDate,
          recievingDate: res.data.recievingDate,
          materialId: res.data.materialId,
          standard: res.data.standard,
          supplier: res.data.supplier,
          inboundAmount: res.data.inboundAmount,
          orderAmount: res.data.orderAmount,
          unitPrice: res.data.unitPrice,
          note: res.data.note,
        };
        setDataSource(materialRequest);
      } catch (e) {
        console.error(e.message);
      }
    }
    fetchData();
  }, [orderHistoryId]);

  const onChange = (e) => {
    setDataSource({ ...dataSource, [e.target.name]: e.target.value });
  };

  const onClickSubmit = (e) => {
    e.preventDefault(); // 폼의 기본 동작 방지
    const isConfirmed = window.confirm('등록하시겠습니까?');

    if (isConfirmed) {
      axios({
        method: 'POST',
        url: `/api/materials/order-history/edit/${orderHistoryId}`,
        data: JSON.stringify(dataSource),
        headers: { 'Content-Type': 'application/json' },
      })
        .then((res) => {
          console.log(res);
          window.alert('등록되었습니다.');
          navigate('/materials/order-history');
        })
        .catch((error) => console.log(error))
    };
  }


  const goBackNavigate = () => {
    navigate(-1)
  }

  return (
    <div>
      <PageTitle value={'입고내역 등록'} />
      <form id='userForm' method='post' action='/api/materials/order-history' onSubmit={onClickSubmit}>
        <div className='flex-line'>
          <div className='flex-div'>
            <label htmlFor='empNo' className='label-title'>발주일</label>
            <InputBar value={dataSource.orderDate} id={'orderDate'} name={'orderDate'} placeholderMsg={'발주일'} disabled={true} />
          </div>
          <div className='flex-div'>
            <label htmlFor='userName' className='label-title'>입고일</label>
            <InputBar type={'date'} id={'recievingDate'} name={'recievingDate'} placeholderMsg={'입고일'} onChange={onChange} required={true} />
          </div>
        </div>
        <div className='flex-line'>
          <div className='flex-div'>
            <label htmlFor='userName' className='label-title'>자재코드</label>
            <InputBar id={'materialId '} value={dataSource.materialId} name={'materialId'} placeholderMsg={'자재코드'} disabled={true} />
          </div>
          <div className='flex-div'>
            <label htmlFor='birth' className='label-title'>공급처</label>
            <InputBar id={'supplier'} value={dataSource.supplier} name={'supplier'} placeholderMsg={'공급처'} disabled={true} />
          </div>
        </div>
        <div className='flex-line'>
          <div className='flex-div'>
            <label htmlFor='position' className='label-title'>규격/사양</label>
            <InputBar id={'standard'} value={dataSource.standard} name={'standard'} placeholderMsg={'규격/사양'} disabled={true} />
          </div>
          <div className='flex-div'>
            <label htmlFor='position' className='label-title'>발주량</label>
            <InputBar id={'orderAmount'} value={dataSource.orderAmount} name={'orderAmount'} placeholderMsg={'발주량'} disabled={true} />
          </div>
        </div>
        <div className='flex-line'>
          <div className='flex-div'>
            <label htmlFor='position' className='label-title'>입고량</label>
            <InputBar id={'inboundAmount'} name={'inboundAmount'} placeholderMsg={'입고량'} onChange={onChange} required={true} />
          </div>
          <div className='flex-div'>
            <label htmlFor='birth' className='label-title'>입고단가</label>
            <InputBar id={'unitPrice'} value={dataSource.unitPrice} name={'unitPrice'} placeholderMsg={'입고단가'} disabled={true} />
          </div>
        </div>
        <div className='flex-line'>
          <div className='flex-div'>
            <label htmlFor='birth' className='label-title'>비고</label>
            <InputBar id={'note'} name={'note'} placeholderMsg={'비고'} onChange={onChange} />
          </div>
        </div>
        <div className='flex-btn'>
          <BtnBlue type='submit' value={'입고내역 등록'} />
          <BtnWhite value={'취소'} onClick={e => navigate('/materials/order-history')} />
        </div>
      </form>
    </div>
  )
}

export default MaterialOrderHistoryEdit
