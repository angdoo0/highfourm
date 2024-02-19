import { Select } from 'antd';
import axios from 'axios';
import React, { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { BtnBlue, InputBar } from '../../Common/Module';

const StockNew = ({ onSubmit, onSubmitSuccess }) => {
  const [manageValue, setManageValue] = useState("");
  const [leadTimeDisabled, setLeadTimeDisabled] = useState(false);
  const [formData, setFormData] = useState(null);
  const navigate = useNavigate();
  const formRef = useRef(null);

  const selectValue = (value) => {
    setManageValue(value);
    setLeadTimeDisabled(value === '3');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const materialRequest = {
      materialId: formData.get('materialId'),
      materialName: formData.get('materialName'),
      unit: formData.get('unit'),
      managementId: parseInt(formData.get('managementId')),
      totalStock: 0, 
      safetyStock: formData.get('managementId') === '3' ? 0 : parseInt(formData.get('safetyStock')),
      maxStock: parseInt(formData.get('maxStock')),
      leadTime: formData.get('managementId') === '3' ? 0 : parseInt(formData.get('LeadTime')),
    };

    // 사용자에게 데이터를 한 번 더 확인 요청
    const isConfirmed = window.confirm("원자재를 저장하시겠습니까?");
    
    if (isConfirmed) {
      try {
        const response = await axios.post('/api/materials/stock/new', JSON.stringify(materialRequest), {
          headers: {
            'Content-Type': 'application/json',
          },
        });
        console.log('Material added successfully');
        if (onSubmitSuccess) {
          onSubmitSuccess();
          navigate('/materials/stock')
        }
      } catch (error) {
        console.error('Error adding material:', error);
      }

      if (onSubmit) {
        onSubmit();
      }
    }
  };

  

  const ManageOptions = [
    { value: '1', label: '정량' },
    { value: '2', label: '정기' },
    { value: '3', label: '수동' },
  ];

  return (
    <>
      <form id='stockNewForm' method='post' ref={formRef}  style={{ borderTop: '1px solid #ccc' }} onSubmit={handleSubmit}>
        <div style={{ display: 'flex', flexDirection: 'row' }}>
          <div className='modal-line' style={{ marginRight: '40px' }}>
            <div className='modal-div' >
              <label htmlFor='materialId' className='label-title'>자재코드:</label>
              <InputBar inputId={'materialId'} name={'materialId'} id={'materialId'} placeholderMsg={'자재코드'} required={true}/>
            </div>
            <div className='modal-div' style={{ marginBottom: '15px', display: 'flex', alignItems: 'center' }}>
              <label htmlFor='unit' className='label-title'>단위:</label>
              <InputBar inputId={'unit'} name={'unit'} id={'unit'} placeholderMsg={'단위'} required={true}/>
            </div>
            <div className='modal-div' style={{ marginBottom: '15px' }}>
              <label htmlFor='managementId' className='label-title'>재고관리 방식:</label>
              <Select
                name={'managementId'}
                defaultValue="재고관리 방식 선택"
                style={{
                  width: '200px',
                  height: '40px'
                }}
                onChange={selectValue}
                options={ManageOptions}
                allowClear={false}
                required
              />
              <InputBar type='hidden' name={'managementId'} value={manageValue} />
            </div>
            <div className='modal-div' style={{ marginBottom: '15px' }}>
              <label htmlFor='maxStock' className='label-title'>최대재고:</label>
              <InputBar inputId={'maxStock'} name={'maxStock'} id={'maxStock'} placeholderMsg={'최대재고'} required={true}/>
            </div>
          </div>

          <div className='modal-line'>
            <div className='modal-div' style={{ marginBottom: '15px' }}>
              <label htmlFor='materialName' className='label-title'>자재명:</label>
              <InputBar inputId={'materialName'} name={'materialName'} id={'materialName'} placeholderMsg={'자재명'} required={true} />
            </div>
            <div className='modal-div' style={{ marginBottom: '15px' }}>
              <label htmlFor='safetyStock' className='label-title'>안전재고:</label>
              <InputBar inputId={'safetyStock'} name={'safetyStock'} id={'safetyStock'} placeholderMsg={'안전재고'} disabled={leadTimeDisabled} />
            </div>
            <div className='modal-div' style={{ marginBottom: '15px' }}>
              <label htmlFor='LeadTime' className='label-title'>LeadTime:</label>
              <InputBar inputId={'LeadTime'} name={'LeadTime'} id={'LeadTime'} placeholderMsg={'LeadTime'} disabled={leadTimeDisabled} />
            </div>
          </div>
        </div>
        <div>
          <BtnBlue value={'저장'} type={'submit'}  onClick={() => setFormData(new FormData(document.getElementById('stockNewForm')))} />
        </div>
      </form>
    </>
  );
};
export default StockNew;