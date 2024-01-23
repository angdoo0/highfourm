import React from 'react';
import { UploadOutlined, UserOutlined, VideoCameraOutlined } from '@ant-design/icons';
import { Layout, Menu, theme } from 'antd';
const { Header, Content, Footer, Sider } = Layout;
const items = [UserOutlined, VideoCameraOutlined, UploadOutlined, UserOutlined].map(
  (icon, index) => ({
    key: String(index + 1),
    icon: React.createElement(icon),
    label: `nav ${index + 1}`,
  }),
);
const Container = () => {
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();
  return (
    <Header
      style={{
        padding: 0,
        height: '120px',
        width: '100%',
        verticalAlign: 'center',
        background: colorBgContainer,
        borderBottom: '1px solid #ccc',
        position: 'absolute',
        top: 0
      }}
    >
      <h2 style={{ margin: '0 0 0 32px', fontSize: '24px', lineHeight: '120px' }}>제목{/* [[${title}]] */}</h2>
    </Header>
  )
};

export default Container;
