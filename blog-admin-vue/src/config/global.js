// 文章状态
 window.ARTICLE = {
  SHOW: 1,
  HIDDEN: 0
};

window.RESPONSE = {
  SUCCESS:1
}

// 设置登录过期时间为15天
window.LOGIN_VALID_DAY = 15
// 最近一次登录时间
window.LOGIN_TIME = "loginTime"

window.QINIU = {
  // 上传后，资源访问前缀
  CDN_PREFIX: "http://cdn.tianyu-studio.cn/",
  // 上传路径
  UPLOAD_URL: "http://upload.qiniu.com/"
}