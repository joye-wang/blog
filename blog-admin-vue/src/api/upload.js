import http from '@/util/http'
import axios from 'axios'

export function getUploadToken() {
  return http.get('/qiniu/token');
}

export function upload(name, file) {
  return getUploadToken().then(res => {
    let param = new FormData();
    param.append("token", res.data.data);
    param.append("file", file);
    param.append("resource_key", name);
    return axios({
      url: window.QINIU.UPLOAD_URL,
      method: 'post',
      data: param,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(res => {
      // TODO 错误判断
      res.data.url = window.QINIU.CDN_PREFIX + res.data.key;
      return res;
    })
  })
}