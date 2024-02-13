import React, { useEffect } from 'react';
import { Layout, theme } from 'antd';
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import '../App.css';
import SideBar from './Common/SideBar';
import Login from './View/Login/Login';
import Notice from './View/Login/Notice';
import StockList from './View/Material/StockList';
import OrderList from './View/Order/OrderList';
import OrderNew from './View/Order/OrderNew';
import Bom from './View/Production/Bom';
import Mrp from './View/Production/Mrp/Mrp';
import MrpDetail from './View/Production/Mrp/MrpDetail';
import Status from './View/Production/Status';
import WorkPerformance from './View/Production/WorkPerformance';
import WorkPerformanceNew from './View/Production/WorkPerformanceNew';
import Performance from './View/Production/Performance/Performance';
import UserList from './View/User/UserList';
import MaterialOrderHistory from './View/Material/MaterialOrderHistory';
import MaterialOrderHistoryNew from './View/Material/MaterialOrderHistoryNew';
import MaterialOrderHistoryEdit from './View/Material/MaterialOrderHistoryEdit';
import Join from './View/User/Join';
import ProductionPlan from './View/Production/ProductionPlan/ProductionPlan';
import PerformanceChart from './View/Production/Performance/PerformanceChart';
import PerformanceControllChart from './View/Production/Performance/PerformanceControllChart';
import BomDetail from './View/Production/BomDetail';
const { Content } = Layout;

function App() {
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();
  return (
    <div className="App" style={{ position: 'relative' }}>
      <SideBar />
      <Content
        style={{
          padding: '152px 32px 32px 332px',
          background: colorBgContainer,
          borderRadius: borderRadiusLG,
          boxSizing: 'border-box',
        }}
      >
        <div>
          <Router>
            <Routes>
              <Route path='/users/join' element={<Join />} />
              <Route path='/notice' element={<Notice />} />
              <Route path='/orders' element={<OrderList />} />
              <Route path='/orders/search' element={<OrderList />} />
              <Route path='/orders/new' element={<OrderNew />} />
              <Route path='/materials/stock' element={<StockList />} />
              <Route path='/materials/stock/search' element={<StockList />} />
              <Route path='/materials/order-history' element={<MaterialOrderHistory />} />
              <Route path='/materials/order-history/search' element={<MaterialOrderHistory />} />
              <Route path='/materials/order-history/new' element={<MaterialOrderHistoryNew />} />
              <Route path='/materials/order-history/edit/:orderHistoryId' element={<MaterialOrderHistoryEdit />} />
              <Route path='/users' element={<UserList />} />
              <Route path='/users/search' element={<UserList />} />
              <Route path='/mrp' element={<Mrp />} />
              <Route path='/mrp/search' element={<MrpDetail />} />
              <Route path='/mrp/:productionPlanId/*' element={<MrpDetail />} />
              <Route path='/production-plan' element={<ProductionPlan />} />
              <Route path='/production-plan/search' element={<ProductionPlan />} />
              <Route path='/bom' element={<Bom />} />
              <Route path='/bom/detail/:productId' element={<BomDetail />} />
              <Route path='/production-status' element={<Status />} />
              <Route path='/work-performance' element={<WorkPerformance />} />
              <Route path='/work-performance/new' element={<WorkPerformanceNew />} />
              <Route path='/production-performance' element={<Performance />} />
              <Route path='/production-performance/search' element={<Performance />} />
              <Route path='/production-performance/:productionPlanId/chart' element={<PerformanceChart />} />
              <Route path='/production-performance/:productionPlanId/controll-chart' element={<PerformanceControllChart />} />
            </Routes>
          </Router>
        </div>
      </Content>
    </div>
  );
}


export default App;
