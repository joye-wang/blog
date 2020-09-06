### 个人博客
- 代码是前后端分离的，但运行时不分离（将前端页面复制到static目录下）
- 使用session做登录验证
- 使用七牛做存储

### How to use
1. 修改application-{profiles}.properties 为你自己的配置
2. 在blog目录下，运行下文的build脚本，会在target目录生成blog.jar

### Linux systemd service
```
[Unit]
Description=blog
After=network.target

[Service]
WorkingDirectory=/blog/
ExecStart=/usr/bin/java -jar daemon/blog.jar

[Install]
WantedBy=multi-user.target
```

### build脚本

```
#!/bin/bash
set -e

cd blog-admin-vue
npm run build

rm -rf ../blog/src/main/resources/static/admin
mv dist ../blog/src/main/resources/static/admin

cd ../blog
mvn clean package -P prod
```