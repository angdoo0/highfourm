import React from 'react';
import login from './component/login';
import table from './component/common/table';
import sidebar from './component/common/sidebar';
import module from './component/common/module';
import container from './component/common/container';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

function App() {
  return (
    <div className="App">
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
