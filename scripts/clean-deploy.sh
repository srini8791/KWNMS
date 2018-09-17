#!/bin/bash
set -e
pushd .
cd ../opennms
sudo ./clean.pl
./compile.pl -DskipTests
./assemble.pl -p fulldir -DskipTests
cd target/opennms-22.0.1/bin
./runjava -s
sudo ./install -dis
popd

