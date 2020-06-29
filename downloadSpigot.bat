@ECHO OFF

IF exist BuildTools.jar (

	mkdir buildspigot 
	copy BuildTools.jar buildspigot 
	cd buildspigot

	FOR %%v IN (
		1.13
		1.13.1
		1.13.2
		1.14
		1.14.3
		1.14.4
		1.15
		1.16.1
	) DO (
		@ECHO OFF
		mvn dependency:get --batch-mode -q -Dartifact=org.spigotmc:spigot:%%v-R0.1-SNAPSHOT
		IF errorlevel 1 (
			echo Building Spigot %%v
			java -jar BuildTools.jar --rev %%v
		) ELSE (
			echo Found spigot version %%v
		)
	)
	
	cd ..
	del /f /s /q buildspigot 1>nul
	rmdir /s /q buildspigot
	echo Done!
	pause
	exit
) ELSE (
	echo Couldn't find the BuildTools.jar! && pause exit
)
