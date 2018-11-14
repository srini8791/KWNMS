#!/bin/bash
set -e
pushd .
cd ../opennms/opennms-webapp
cp src/main/webapp/index.html ../target/opennms-22.0.1/jetty-webapps/opennms/
cp src/main/webapp/css/style.css ../target/opennms-22.0.1/jetty-webapps/opennms/css/
cp -R src/main/webapp/app/* ../target/opennms-22.0.1/jetty-webapps/opennms/app/
popd
