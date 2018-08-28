#!/bin/sh
pushd .
cd ../opennms
sudo ./clean.pl
./compile.pl -DskipTests
./assemble.pl -p fulldir -DskipTests
popd

