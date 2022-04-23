import React, {useState} from 'react';
import './App.module.css';
import robots from './mockdata/robots.json';
import Robot from "./components/Robots";
import styles from './App.module.css';
import logo from './assets/images/logo.svg';
import ShoppingCart from './components/ShoppingCart';

interface Props {
}

interface State {
  robotGallery: any[],
  count: number
}

const App: React.FC = () => {
  const [count, setCount] = useState<number>(0);

  return (
    <div className={styles.app}>
      <div className={styles.appHeader}>
        <img src={logo} className={styles.appLogo} alt=""/>
        <h1>罗伯特机器人炫酷吊炸天online购物平台的名字要长</h1>
      </div>
      <button onClick={() => setCount(count + 1)}>Click</button>
      <span>count: {count}</span>
      <ShoppingCart/>
      {/*<div className={styles.robotList}>*/}
      {/*  {this.state.robotGallery.map(r => <Robot id={r.id} name={r.name} email={r.email}/>)}*/}
      {/*</div>*/}
    </div>
  );

}

export default App;
