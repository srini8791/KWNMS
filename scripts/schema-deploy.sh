#!/bin/bash
set -e
pushd .
cd ../opennms/target/opennms-22.0.1/bin
sudo ./opennms stop
cd ../../../core/schema
mvn install -DskipTests
cp target/org.opennms.core.schema-22.0.1.jar ../target/opennms-22.0.1/lib/org.opennms.core.schema-22.0.1.jar
cp target/org.opennms.core.schema-22.0.1-liquibase.jar ../target/opennms-22.0.1/lib/org.opennms.core.schema-22.0.1-liquibase.jar
cd ../../target/opennms-22.0.1/logs
rm -f *
popd

