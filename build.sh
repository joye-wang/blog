#!/bin/bash
set -e

cd blog-admin-vue
npm run build

rm -rf ../blog/src/main/resources/static/admin
mv dist ../blog/src/main/resources/static/admin

cd ../blog
mvn clean package -P prod

systemctl stop blog
mv target/blog.jar /blog/daemon/
systemctl start blog