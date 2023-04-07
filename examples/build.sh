#!/bin/bash

# Exit if any command fails
set -e

# Find all pom.xml files
for folder in $(find $PWD -name "pom.xml" | xargs dirname); do
	if [[ ! $folder =~ "maven-shaded-tests" ]]; then
		cd $folder && mvn clean package
	fi
done
