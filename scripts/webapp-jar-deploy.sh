#!/bin/bash
set -e
pushd .
cd ../opennms/target/opennms-22.0.1/bin
sudo ./opennms stop
cd ../../../opennms-webapp
mvn install -DskipTests
cp target/opennms-webapp-22.0.1/WEB-INF/lib/opennms-webapp-22.0.1.jar ../target/opennms-22.0.1/jetty-webapps/opennms/WEB-INF/lib/
cd ../target/opennms-22.0.1/logs
rm -f *
popd

