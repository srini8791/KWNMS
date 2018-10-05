#!/bin/bash
set -e
pushd .
cd ../opennms/target/opennms-22.0.1/bin
sudo ./opennms stop
cd ../../../opennms-config-model
mvn install -DskipTests
cp target/opennms-config-model-22.0.1.jar ../target/opennms-22.0.1/lib/opennms-config-model-22.0.1.jar
cp target/opennms-config-model-22.0.1.jar ../target/opennms-22.0.1/jetty-webapps/opennms-remoting/webstart/
cp target/opennms-config-model-22.0.1.jar ../target/opennms-22.0.1/system/org/opennms/opennms-config-model/22.0.1/
cp src/main/resources/xsds/users.xsd ../target/opennms-22.0.1/share/xsds/
cd ../opennms-model
mvn install -DskipTests
cp target/opennms-model-22.0.1.jar ../target/opennms-22.0.1/jetty-webapps/opennms-remoting/webstart/
cp target/opennms-model-22.0.1.jar ../target/opennms-22.0.1/system/org/opennms/opennms-model/22.0.1/
cp target/opennms-model-22.0.1.jar ../target/opennms-22.0.1/lib/
cd ../target/opennms-22.0.1/logs
rm -f *
popd

