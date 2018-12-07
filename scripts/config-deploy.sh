#!/bin/bash
set -e
pushd .
cd ../opennms/target/opennms-22.0.1/bin
sudo ./opennms stop
cd ../../../opennms-config
mvn install -DskipTests
cp target/opennms-config-22.0.1.jar ../target/opennms-22.0.1/lib/opennms-config-22.0.1.jar
cd ../target/opennms-22.0.1/logs
rm -f *
popd

