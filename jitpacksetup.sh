#!/bin/sh
versions=(1.13 1.13.1 1.13.2 1.14 1.14.3 1.14.4 1.15 1.16.1 1.16.2 1.16.4)

for version in ${versions[@]}; do
    mvn org.apache.maven.plugins:maven-dependency-plugin:2.1:get -DrepoUrl=https://repo.codemc.io/ -Dartifact=org.spigotmc:spigot:${version}-R0.1-SNAPSHOT
done