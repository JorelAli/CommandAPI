#!/bin/sh
echo "Installing Spigot libraries..."
mvn org.apache.maven.plugins:maven-dependency-plugin:2.1:get -DrepoUrl=https://repo.codemc.io/ -Dartifact=org.spigotmc:spigot:1.13-R0.1-SNAPSHOT
mvn org.apache.maven.plugins:maven-dependency-plugin:2.1:get -DrepoUrl=https://repo.codemc.io/ -Dartifact=org.spigotmc:spigot:1.13.1-R0.1-SNAPSHOT
mvn org.apache.maven.plugins:maven-dependency-plugin:2.1:get -DrepoUrl=https://repo.codemc.io/ -Dartifact=org.spigotmc:spigot:1.13.2-R0.1-SNAPSHOT
mvn org.apache.maven.plugins:maven-dependency-plugin:2.1:get -DrepoUrl=https://repo.codemc.io/ -Dartifact=org.spigotmc:spigot:1.14-R0.1-SNAPSHOT
mvn org.apache.maven.plugins:maven-dependency-plugin:2.1:get -DrepoUrl=https://repo.codemc.io/ -Dartifact=org.spigotmc:spigot:1.14.3-R0.1-SNAPSHOT
mvn org.apache.maven.plugins:maven-dependency-plugin:2.1:get -DrepoUrl=https://repo.codemc.io/ -Dartifact=org.spigotmc:spigot:1.14.4-R0.1-SNAPSHOT
mvn org.apache.maven.plugins:maven-dependency-plugin:2.1:get -DrepoUrl=https://repo.codemc.io/ -Dartifact=org.spigotmc:spigot:1.15-R0.1-SNAPSHOT
mvn org.apache.maven.plugins:maven-dependency-plugin:2.1:get -DrepoUrl=https://repo.codemc.io/ -Dartifact=org.spigotmc:spigot:1.16.1-R0.1-SNAPSHOT
mvn org.apache.maven.plugins:maven-dependency-plugin:2.1:get -DrepoUrl=https://repo.codemc.io/ -Dartifact=org.spigotmc:spigot:1.16.2-R0.1-SNAPSHOT
mvn org.apache.maven.plugins:maven-dependency-plugin:2.1:get -DrepoUrl=https://repo.codemc.io/ -Dartifact=org.spigotmc:spigot:1.16.4-R0.1-SNAPSHOT
mvn org.apache.maven.plugins:maven-dependency-plugin:2.1:get -DrepoUrl=https://repo.codemc.io/ -Dartifact=org.spigotmc:spigot:1.16.5-R0.1-SNAPSHOT
echo "Done!"
