#!/bin/bash
set -e
pushd .
cd ../opennms/target/opennms-22.0.1/bin
sudo ./opennms stop
cd ../../../opennms-model
mvn package -DskipTests
cp target/opennms-model-22.0.1.jar ../target/opennms-22.0.1/jetty-webapps/opennms-remoting/webstart/
cp target/opennms-model-22.0.1.jar ../target/opennms-22.0.1/system/org/opennms/opennms-model/22.0.1/
cp target/opennms-model-22.0.1.jar ../target/opennms-22.0.1/lib/
cd ../target/opennms-22.0.1/logs
rm -f *
cd ../bin
sudo ./opennms -t start
popd

