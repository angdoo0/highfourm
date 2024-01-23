import React from 'react';
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import SideBar from './component/common/SideBar';
import { BtnBlack, BtnBlue, BtnWhite, BtnFilter, InputBar, SearchInput, SearchSelectBox, StepBar } from './component/common/module';

function App() {
  return (
    <div className="App">
      <SideBar />
      <Router>
        <Routes>
          <Route path='/' element={<login />} />
        </Routes>
      </Router>
      <h1>편의를 위해 만들어둠.  서버 정상 작동중</h1>
      <div>

        <BtnBlack value={'검정'} link={'/black'} />
        <BtnBlue value={'파랑'} link={'/blue'} />
        <BtnWhite value={'흰색'} link={'/white'} />
        <BtnFilter value={'완료'} link={'/complete'} />
        <InputBar placeholderMsg={'값을 입력해주세요...'} />
        <SearchInput />
        {/* <SearchSelectBox value={'가', '나', '다'} />  */}
        <StepBar stateNum={1} />
        <StepBar stateNum={2} />
        <StepBar stateNum={3} />
        <StepBar stateNum={4} />

      </div>
    </div>
  );
}

export default App;
