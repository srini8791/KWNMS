#!/bin/sh
pushd .
cd ..
./clean.pl
./compile.pl -DskipTests
popd
