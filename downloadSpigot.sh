#!/bin/sh
if [ -f ./BuildTools.jar ]; then

	echo "BuildTools.jar found. Checking for available versions of spigot:"

	mkdir buildspigot
	cp BuildTools.jar buildspigot
	cd buildspigot

	versions=(1.13 1.13.1 1.13.2 1.14 1.14.3 1.14.4 1.15 1.16.1)

	for version in ${versions[@]}; do
		if mvn dependency:get --batch-mode -q -Dartifact=org.spigotmc:spigot:${version}-R0.1-SNAPSHOT; then
			echo "Found spigot version $version"
		else
			echo "Building Spigot $version"
			java -jar BuildTools.jar --rev $version
		fi
	done

	cd ..
	rm -rf buildspigot
else
	echo "Couldn't find the BuildTools.jar!"
fi
echo "Done!"
