#!/bin/bash
set -e
pushd .
cd ../opennms/target/opennms-22.0.1/bin
sudo ./opennms stop
cd ../../../opennms-provision/opennms-provisiond
mvn install -DskipTests
cp target/opennms-provisiond-22.0.1.jar ../../target/opennms-22.0.1/lib/
cd ../../target/opennms-22.0.1/logs
rm -f *
popd

