import React from 'react';
import './App.module.css';
import robots from './mockdata/robots.json';
import Robot from "./components/Robots";
import styles from './App.module.css';
import logo from './assets/images/logo.svg';
import ShoppingCart from './components/ShoppingCart';

function App() {
  return (
    <div className={styles.app}>
      <div className={styles.appHeader}>
        <img src={logo} className={styles.appLogo} alt=""/>
        <h1>罗伯特机器人炫酷吊炸天online购物平台的名字要长</h1>
      </div>
      <ShoppingCart/>
      <div className={styles.robotList}>
        {robots.map(r => <Robot id={r.id} name={r.name} email={r.email}/>)}
      </div>
    </div>
  );
}

export default App;
