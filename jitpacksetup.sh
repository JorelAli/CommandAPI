#!/bin/sh

echo "Old Java version:"
java -version

echo "Installing Java 16"
wget https://github.com/sormuras/bach/raw/master/install-jdk.sh
source install-jdk.sh --feature 16
java -version
