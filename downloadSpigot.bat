@echo off
:start
if not exist build-works (
    mkdir build-works
    if %ERRORLEVEL% == 0 (
        echo Created folder: build-works
    )
)

if not exist libs (
    mkdir libs
    if %ERRORLEVEL% == 0 (
        echo Created folder: libs
    )
)

if exist build-works\BuildTools.jar (
	cd build-works
	if not exist spigot-1.13.jar title Building Spigot 1.13 && java -jar BuildTools.jar --rev 1.13
	if not exist spigot-1.13.1.jar title Building Spigot 1.13.1 && java -jar BuildTools.jar --rev 1.13.1
	if not exist spigot-1.13.2.jar title Building Spigot 1.13.2 && java -jar BuildTools.jar --rev 1.13.2
	if not exist spigot-1.14.2.jar title Building Spigot 1.14.2 && java -jar BuildTools.jar --rev 1.14.2
	if not exist spigot-1.14.3.jar title Building Spigot 1.14.3 && java -jar BuildTools.jar --rev 1.14.3
	if not exist spigot-1.14.4.jar title Building Spigot 1.14.4 && java -jar BuildTools.jar --rev 1.14.4
	if not exist spigot-1.15.2.jar title Building Spigot 1.15.2 && java -jar BuildTools.jar --rev 1.15.2
	if not exist spigot-1.16.1.jar title Building Spigot 1.16.1 && java -jar BuildTools.jar --rev 1.16.1
	cd ..
	
	title Copying files
	copy build-works\spigot-1.13.jar libs
	copy build-works\spigot-1.13.1.jar libs
	copy build-works\spigot-1.13.2.jar libs
	copy build-works\spigot-1.14.2.jar libs
	copy build-works\spigot-1.14.3.jar libs
	copy build-works\spigot-1.14.4.jar libs
	copy build-works\spigot-1.15.2.jar libs
	copy build-works\spigot-1.16.1.jar libs
	
	title Done!
	echo Done!
	pause
) else (
	bitsadmin /transfer DownloadBT https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar %CD%\build-works\BuildTools.jar
	goto start
)
