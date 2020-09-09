// vue.config.js
module.exports = {
  publicPath: 'admin',
  devServer: {
    // npm项目访问端口
    port: 9001,
    proxy: 'http://localhost:8081'
  }

}