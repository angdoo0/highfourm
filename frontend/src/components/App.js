import React from 'react';
import Login from './components/Login';
import Table from './components/Common/Table';
import Sidebar from './components/Common/Sidebar';
import Container from './components/common/Container';
import OrderList from './components/Order/OrderList';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';


function App() {
  return (
    <div className="App">
      <Container />
      <div style={{ margin: '120px 0 0 300px', height: '100vh' }}>
        <Router >
          <Routes>
            <Route path='/' element={<login />} />
            <Route path='/container' element={<Container />} />
            <Route path='/orders' element={<OrderList />} />
          </Routes>
        </Router>
      </div>
      <h1>편의를 위해 만들어둠.  서버 정상 작동중</h1>
    </div>
  );
}

export default App;
