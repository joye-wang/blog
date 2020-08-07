cd blog-admin-vue
npm run build

rm -rf ../blog-api/src/main/resources/static/admin
mv dist ../blog-api/src/main/resources/static/admin

cd ../blog-api
mvn clean package

scp target/blog.war root@118.89.168.141:/opt/apache-tomcat-9.0.29/webapps/
