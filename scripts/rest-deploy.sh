#!/bin/bash
set -e
pushd .
cd ../opennms/target/opennms-22.0.1/bin
sudo ./opennms stop
cd ../../../opennms-webapp-rest
mvn package -DskipTests
cp target/opennms-webapp-rest-22.0.1/WEB-INF/lib/opennms-webapp-rest-22.0.1.jar ../target/opennms-22.0.1/jetty-webapps/opennms/WEB-INF/lib/
cd ../target/opennms-22.0.1/logs
rm -f *
cd ../bin
sudo ./opennms -t start
popd

