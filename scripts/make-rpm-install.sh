#!/bin/bash
set -e
pushd .
cd ../opennms
sudo ./clean.pl
./compile.pl -DskipTests
./makerpm.sh --scripts
popd

