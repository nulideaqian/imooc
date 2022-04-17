import { createApp } from 'vue'
import App from './App.vue'

console.log("env: ", import.meta.env)
createApp(App).mount('#app')
