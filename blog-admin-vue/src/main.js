import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios'
// 引入全局样式
import '@/styles/index.css'

import '@/config/global'

Vue.prototype.$http = axios;

Vue.use(ElementUI);

new Vue({
    router,
    render: h => h(App)
}).$mount('#app');