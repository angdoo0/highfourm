import React, { useState, useEffect } from 'react';
import { Menu } from 'antd';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import axios from 'axios';
import {
  faFilePen, faListCheck, faTable, faIndustry, faCalculator, faChartSimple,
  faChartLine, faUserGroup, faRightFromBracket
} from '@fortawesome/free-solid-svg-icons'

const SideBar = () => {
  function getItem(label, key, icon, children) {
    return {
      key,
      icon,
      children,
      label,
    };
  }

  const mode = 'inline';
  const theme = 'light';
  const [selectedMenu, setSelectedMenu] = useState(null);
  const [currentPage, setCurrentPage] = useState('');
  const [openKeys, setOpenKeys] = useState([]);

  useEffect(() => {
    // 현재 페이지 URL 가져오기
    const currentURL = window.location.pathname;
    // 페이지 URL에 따라 선택한 메뉴 식별
    const menuKey = getMenuKeyFromURL(currentURL);
    // 선택한 메뉴 설정
    setSelectedMenu(menuKey);

    // 현재 페이지가 sub1 메뉴 하위 메뉴인 경우 sub1 메뉴를 열도록 설정
    if (currentURL.startsWith('/materials/')) {
      setOpenKeys(['sub1']);
    } else if (currentURL.startsWith('/work-performance')) {
      setOpenKeys(['sub2']);
    }

    // 현재 페이지 설정
    setCurrentPage(currentURL);
  }, []);

  const onClickSubmit = () => {
    axios({
      method: 'POST',
      url: 'http://localhost:8080/api/logout',
    })
      .then((res) => {
        console.log(res);
      })
      .catch((error) => console.log(error))
  };

  const infoMenu = [
    getItem('원자재관리', 'sub1', <FontAwesomeIcon icon={faListCheck} />, [
      getItem(<a href="/materials/stock">원자재 조회/등록</a>, '1'),
      getItem(<a href="/materials/order-history">원자재 수급내역관리</a>, '2'),
    ]),
    getItem(<a href="/bom">제품별 공정/소요자재 관리</a>, '3', <FontAwesomeIcon icon={faTable} />),
    getItem(<a href="/orders">주문관리</a>, '4', <FontAwesomeIcon icon={faFilePen} />),
  ];
  const productionMenu = [
    getItem(<a href="/production-plan">생산 계획 수립</a>, '5', <FontAwesomeIcon icon={faIndustry} />),
    getItem(<a href="/mrp">자재 소요량 산출</a>, '6', <FontAwesomeIcon icon={faCalculator} />),
    getItem('작업 실적 관리', 'sub2', <FontAwesomeIcon icon={faChartLine} />, [
      getItem(<a href="/work-performance/new">작업 실적 등록</a>, '7'),
      getItem(<a href="/work-performance">작업 실적 조회</a>, '8'),
    ]),
  ];
  const inquiryMenu = [
    getItem(<a href="/production-status">생산 현황 조회</a>, '9', <FontAwesomeIcon icon={faChartSimple} />),
    getItem(<a href="/production-performance">생산 실적 조회</a>, '10', <FontAwesomeIcon icon={faChartLine} />),
  ];

  const bottomMenu = [
    getItem(<a href="/users">사용자 관리</a>, '11', <FontAwesomeIcon icon={faUserGroup} />),
    getItem(<a href="/" onClick={onClickSubmit}>로그아웃</a>, '12', <FontAwesomeIcon icon={faRightFromBracket} />),
  ];


  // URL에 따른 선택한 메뉴 식별 함수
  const getMenuKeyFromURL = (url) => {
    if (url.startsWith('/materials/stock')) {
      return '1';
    } else if (url === '/materials/order-history' || url === '/materials/order-history/new') {
      return '2';
    } else if (url === '/bom') {
      return '3';
    } else if (url === '/orders') {
      return '4';
    } else if (url === '/production-plan') {
      return '5';
    } else if (url === '/mrp') {
      return '6';
    } else if (url === '/work-performance/new') {
      return '7';
    } else if (url === '/work-performance') {
      return '8';
    } else if (url === '/production-status') {
      return '9';
    } else if (url === '/production-performance' || url === '/production-performance/chart' || url === '/production-performance/controll-chart') {
      return '10';
    } else if (url === '/users/') {
      return '11';
    } else {
      return null;
    }
  };

  const handleClick = (e) => {
    setSelectedMenu(e.key);
  };

  const handleOpenChange = (keys) => {
    setOpenKeys(keys);
  };


  return (
    <div style={{ padding: '40px 20px 40px 20px', width: '300px', backgroundColor: '#fff', height: '100vh', borderRight: '1px solid #ccc', boxSizing: 'border-box', display: 'flex', flexDirection: 'column', justifyContent: 'space-between', zIndex: '100', position: 'fixed' }}>
      <div>
        <div style={{ paddingBottom: '60px' }}>
          <a href='/orders'>
            <svg width="138px" height="40px" viewBox="0 0 112 28" fill="none" xmlns="http://www.w3.org/2000/svg" >
              <path d="M18.12 8.07H0.48V3.45H6.57V0.0299993H12.48V3.45H18.12V8.07ZM19.17 27.75V0.84H24.75V10.5H28.05V16.05H24.75V27.75H19.17ZM9.51 23.85C8.33 23.85 7.24 23.67 6.24 23.31C5.24 22.95 4.37 22.44 3.63 21.78C2.89 21.12 2.31 20.33 1.89 19.41C1.47 18.49 1.26 17.47 1.26 16.35C1.26 15.23 1.46 14.22 1.86 13.32C2.28 12.42 2.85 11.65 3.57 11.01C4.31 10.37 5.18 9.88 6.18 9.54C7.2 9.18 8.31 9 9.51 9C10.71 9 11.81 9.18 12.81 9.54C13.83 9.88 14.7 10.37 15.42 11.01C16.16 11.65 16.73 12.42 17.13 13.32C17.55 14.22 17.76 15.23 17.76 16.35C17.76 17.47 17.55 18.49 17.13 19.41C16.71 20.33 16.13 21.12 15.39 21.78C14.65 22.44 13.77 22.95 12.75 23.31C11.75 23.67 10.67 23.85 9.51 23.85ZM9.51 19.14C10.37 19.14 11.06 18.88 11.58 18.36C12.1 17.82 12.36 17.15 12.36 16.35C12.36 15.55 12.1 14.89 11.58 14.37C11.06 13.83 10.37 13.56 9.51 13.56C8.65 13.56 7.96 13.83 7.44 14.37C6.92 14.89 6.66 15.55 6.66 16.35C6.66 17.15 6.92 17.82 7.44 18.36C7.96 18.88 8.65 19.14 9.51 19.14ZM49.6629 27.75V0.84H55.3629V27.75H49.6629ZM38.4429 20.34C37.0429 20.34 35.7429 20.12 34.5429 19.68C33.3629 19.22 32.3329 18.59 31.4529 17.79C30.5929 16.99 29.9129 16.05 29.4129 14.97C28.9129 13.87 28.6629 12.68 28.6629 11.4C28.6629 10.12 28.9029 8.94 29.3829 7.86C29.8629 6.76 30.5329 5.81 31.3929 5.01C32.2729 4.21 33.3129 3.59 34.5129 3.15C35.7129 2.69 37.0229 2.46 38.4429 2.46C39.8629 2.46 41.1629 2.69 42.3429 3.15C43.5229 3.59 44.5329 4.21 45.3729 5.01C46.2329 5.81 46.8929 6.76 47.3529 7.86C47.8329 8.94 48.0729 10.12 48.0729 11.4C48.0729 12.68 47.8329 13.87 47.3529 14.97C46.8729 16.05 46.2029 16.99 45.3429 17.79C44.4829 18.59 43.4629 19.22 42.2829 19.68C41.1229 20.12 39.8429 20.34 38.4429 20.34ZM38.4429 15.33C39.6429 15.33 40.6029 14.96 41.3229 14.22C42.0629 13.48 42.4329 12.54 42.4329 11.4C42.4329 10.26 42.0629 9.32 41.3229 8.58C40.6029 7.84 39.6429 7.47 38.4429 7.47C37.2429 7.47 36.2729 7.84 35.5329 8.58C34.8129 9.32 34.4529 10.26 34.4529 11.4C34.4529 12.54 34.8129 13.48 35.5329 14.22C36.2729 14.96 37.2429 15.33 38.4429 15.33ZM58.2117 26.25V21.45H68.1417V17.4H59.7117V13.02H63.2217V5.94H59.6217V1.23H82.6017V5.94H79.0017V13.02H82.5117V17.4H74.0817V21.45H84.0117V26.25H58.2117ZM68.8317 5.94V13.02H73.3917V5.94H68.8317ZM106.975 15.21V0.84H111.775V15.21H106.975ZM101.365 15.06V10.32H99.6246C99.1646 11.7 98.3246 12.78 97.1046 13.56C95.9046 14.34 94.4546 14.73 92.7546 14.73C91.6746 14.73 90.6746 14.57 89.7546 14.25C88.8546 13.93 88.0746 13.47 87.4146 12.87C86.7546 12.27 86.2346 11.55 85.8546 10.71C85.4946 9.87 85.3146 8.92 85.3146 7.86C85.3146 6.82 85.4946 5.87 85.8546 5.01C86.2346 4.13 86.7546 3.39 87.4146 2.79C88.0746 2.17 88.8546 1.69 89.7546 1.35C90.6746 1.01 91.6746 0.84 92.7546 0.84C94.4746 0.84 95.9446 1.26 97.1646 2.1C98.3846 2.94 99.2246 4.08 99.6846 5.52H101.365V0.899999H105.895V15.06H101.365ZM92.8746 10.53C93.6346 10.53 94.2646 10.28 94.7646 9.78C95.2646 9.28 95.5146 8.64 95.5146 7.86C95.5146 7.08 95.2646 6.44 94.7646 5.94C94.2646 5.44 93.6346 5.19 92.8746 5.19C92.1146 5.19 91.4846 5.44 90.9846 5.94C90.4846 6.44 90.2346 7.08 90.2346 7.86C90.2346 8.64 90.4846 9.28 90.9846 9.78C91.4846 10.28 92.1146 10.53 92.8746 10.53ZM89.1246 16.23H111.775V27.45H89.1246V16.23ZM94.6146 20.28V23.13H106.285V20.28H94.6146Z" fill="#1C1C1C" />
            </svg>
          </a>
        </div>
        <div style={{ width: '260px', }}>
          <span style={{ width: '87px', height: '14px', paddingBottom: '20px', color: '#808080' }}>정보</span>
          <Menu
            style={{
              width: '260px',
            }}
            mode={mode}
            theme={theme}
            items={infoMenu}
            selectedKeys={[selectedMenu]}
            openKeys={openKeys}
            onClick={handleClick}
            onOpenChange={handleOpenChange}
          />
          <span style={{ width: '87px', height: '14px', paddingBottom: '20px', color: '#808080' }}>생산</span>
          <Menu
            style={{
              width: '260px',
            }}
            mode={mode}
            theme={theme}
            items={productionMenu}
            selectedKeys={[selectedMenu]}
            openKeys={openKeys}
            onClick={handleClick}
            onOpenChange={handleOpenChange}
          />
          <span style={{ width: '87px', height: '14px', paddingBottom: '20px', color: '#808080' }}>조회</span>
          <Menu
            style={{
              width: '260px',
            }}
            mode={mode}
            theme={theme}
            items={inquiryMenu}
            selectedKeys={[selectedMenu]}
            openKeys={openKeys}
            onClick={handleClick}
            onOpenChange={handleOpenChange}
          />
        </div>
      </div>
      <div>
        <Menu
          style={{
            width: '260px', bottom: '0',
          }}
          mode={mode}
          theme={theme}
          items={bottomMenu}
          selectedKeys={[selectedMenu]}
          onClick={handleClick}
        />
      </div>
    </div>
  );
};

export default SideBar;