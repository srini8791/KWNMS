#!/bin/sh
pushd .
cd ..
cd opennms-config-model
mvn install -DskipTests
cd ..
cd opennms-config
mvn install -DskipTests
cd ..
cd opennms-services
mvn install -DskipTests
cd ..
cp opennms-config/target/opennms-config-22.0.1.jar target/opennms-22.0.1/lib/
cp opennms-config-model/target/opennms-config-model-22.0.1.jar target/opennms-22.0.1/lib/
cp opennms-config-model/src/main/resources/xsds/enlinkd-configuration.xsd target/opennms-22.0.1/share/xsds/
cp opennms-services/target/opennms-services-22.0.1.jar target/opennms-22.0.1/lib/
cp opennms-base-assembly/src/main/filtered/etc/enlinkd-configuration.xml target/opennms-22.0.1/etc/
popd
