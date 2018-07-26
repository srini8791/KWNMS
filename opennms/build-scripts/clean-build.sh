#!/bin/sh
pushd .
cd ..
./clean.pl
./compile.pl -DskipTests
./assemble.pl -p fulldir
popd
