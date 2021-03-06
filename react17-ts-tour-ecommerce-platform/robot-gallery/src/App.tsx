import React, {useState, useEffect} from 'react';
import './App.module.css';
import robots from './mockdata/robots.json';
import Robot from "./components/Robots";
import styles from './App.module.css';
import logo from './assets/images/logo.svg';
import ShoppingCart from './components/ShoppingCart';
import {Simulate} from "react-dom/test-utils";

interface Props {
  username: string
}

interface State {
  robotGallery: any[],
  count: number
}

const App: React.FC = () => {
  const [count, setCount] = useState<number>(0);
  const [robotGallery, setRobotGallery] = useState<any>([])
  const [loading, setLoading] = useState<boolean>(false)
  const [error, setError] = useState<string>()

  useEffect(() => {
    document.title = `点击${count}次`
  }, [count])

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true)
      try {
        const response = await fetch("https://jsonplaceholder.typicode.com/users")
        const data = await response.json()
        setRobotGallery(data)
      } catch (e: any) {
        setError(e.message)
      }
      setLoading(false)
    }
    fetchData()
  }, [])

  return (
    <div className={styles.app}>
      <div className={styles.appHeader}>
        <img src={logo} className={styles.appLogo} alt=""/>
        <h1>罗伯特机器人炫酷吊炸天online购物平台的名字要长</h1>
      </div>
      <button onClick={() => setCount(count + 1)}>Click</button>
      <span>count: {count}</span>
      <ShoppingCart/>
      {!error || error !== "" && <div>网站出错：{error}</div>}
      { !loading ?(
        <div className={styles.robotList}>
          {robotGallery.map(r => <Robot id={r.id} name={r.name} email={r.email}/>)}
        </div>)
        : (<h2>loading 加载中</h2>)
      }
    </div>
  );

}

export default App;
