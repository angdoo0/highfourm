import React from 'react';
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import SideBar from './component/common/SideBar';

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
    </div>
  );
}

export default App;
