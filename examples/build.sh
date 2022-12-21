#!/bin/sh

# Exit if any command fails
set -e

# Find all pom.xml files
for folder in $(find $PWD -name "pom.xml" | xargs dirname); do
	cd $folder && mvn clean package
done