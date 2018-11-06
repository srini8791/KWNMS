#!/bin/bash
set -e
pushd .
cd ../opennms/core/web-assets
mvn package -DskipTests
cp target/dist/assets/onms-assets.js ../../../opennms/target/opennms-22.0.1/jetty-webapps/opennms/assets/onms-assets.js
cp target/dist/assets/onms-assets.min.js ../../../opennms/target/opennms-22.0.1/jetty-webapps/opennms/assets/onms-assets.min.js
popd

