#!/bin/sh
build() {
    for folder in ./*/; do
    	echo $folder
		cd $folder
		if [ -f pom.xml ]; then
			mvn clean package
		else
			build
		fi
		cd ..
    done
}

build