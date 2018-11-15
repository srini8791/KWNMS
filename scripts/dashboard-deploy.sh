#!/bin/bash
set -e
pushd .
cd ../opennms/opennms-webapp
cp src/main/webapp/index.html ../target/opennms-22.0.1/jetty-webapps/opennms/
cp src/main/webapp/images/*.png ../target/opennms-22.0.1/jetty-webapps/opennms/images/
cp src/main/webapp/js/*.js ../target/opennms-22.0.1/jetty-webapps/opennms/js/
cp src/main/webapp/css/*.css ../target/opennms-22.0.1/jetty-webapps/opennms/css/
cp -R src/main/webapp/app/* ../target/opennms-22.0.1/jetty-webapps/opennms/app/
popd
