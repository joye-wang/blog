import axios from 'axios'
import qs from 'qs'
import {
  Message,
  MessageBox
} from 'element-ui'
import router from '@/router'

// 创建axios实例
const instance = axios.create({
  baseURL:process.env.VUE_APP_SERVER_URL
});

instance.interceptors.request.use(
  config => {
    // 将post请求的json data转化为表单数据
    config.transformRequest = [function (data) {
      return qs.stringify(data);
    }];
    return config;
  },
  error => {
    return Promise.reject(error);
  });

// 添加响应拦截器
instance.interceptors.response.use(
  response => {
    if (response.data.code !== window.RESPONSE.SUCCESS) {
      Message.error(response.data.message);
      return Promise.reject(response);
    }
    return response;
  },
  error => {
    Message.closeAll();
    if (error.response) {
      if (error.response.status == 401 || error.response.status == 403) {
        MessageBox.confirm('登录已过期，请重新登录').then(() => {
          router.push('login');
        })
      } else
        Message.error(error.response.status + ":" + error.response.data);
    } else {
      Message.error("请求失败");
    }
    return Promise.reject(error);
  });

export default instance