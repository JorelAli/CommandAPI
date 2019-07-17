#!/bin/sh
if [ -f ./BuildTools.jar ]; then
	if [ ! -f ./spigot-1.13.2.jar ]; then
		echo "Building Spigot 1.13.2"
		java -jar BuildTools.jar --rev 1.13.2
	fi

	if [ ! -f ./spigot-1.14.jar ]; then
		echo "Building Spigot 1.14"
		java -jar BuildTools.jar --rev 1.14
	fi
fi
echo "Copying files to ./spigotlibs"
mkdir spigotlibs
cp ./spigot-1.13.2.jar spigotlibs
cp ./spigot-1.14.jar spigotlibs
echo "Done!"
