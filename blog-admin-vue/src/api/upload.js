import http from '@/util/http'
import axios from 'axios'
import { Message } from 'element-ui'

export function getUploadToken() {
  return http.get('/qiniu/token');
}

/**
 * 上传文件到七牛
 * @param file 文件
 * @param name 文件名
 */
export function upload(file, name) {
  return getUploadToken().then(res => {
    let param = new FormData();
    param.append("token", res.data.data);
    param.append("file", file);
    param.append("key", name);
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
    }).catch(error => {
      console.error('上传文件出错', error.response);
      Message.error("上传错误：" + error.response.data.error);
      return error;
    })
  })
}