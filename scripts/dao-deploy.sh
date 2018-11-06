#!/bin/bash
set -e
pushd .
cd ../opennms/target/opennms-22.0.1/bin
sudo ./opennms stop
cd ../../../opennms-dao-api
mvn install -DskipTests
cp target/opennms-dao-api-22.0.1.jar ../target/opennms-22.0.1/jetty-webapps/opennms-remoting/webstart/
cp target/opennms-dao-api-22.0.1.jar ../target/opennms-22.0.1/system/org/opennms/opennms-dao-api/22.0.1/
cp target/opennms-dao-api-22.0.1.jar ../target/opennms-22.0.1/lib/
cd ../opennms-dao
mvn install -DskipTests
cp target/opennms-dao-22.0.1.jar ../target/opennms-22.0.1/jetty-webapps/opennms-remoting/webstart/
cp target/opennms-dao-22.0.1.jar ../target/opennms-22.0.1/system/org/opennms/opennms-dao/22.0.1/
cp target/opennms-dao-22.0.1.jar ../target/opennms-22.0.1/lib/
cd ../target/opennms-22.0.1/logs
rm -f *
popd

