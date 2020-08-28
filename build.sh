cd blog-admin-vue
npm run build

rm -rf ../blog-api/src/main/resources/static/admin
mv dist ../blog-api/src/main/resources/static/admin

cd ../blog-api
mvn clean package -P prod

systemctl stop blog

mv target/blog.jar /blog/daemon/

systemctl start blog