if exist BuildTools.jar (
	mkdir spigotlibs
	if not exist spigot-1.13.jar echo Building Spigot 1.13 && java -jar BuildTools.jar --rev 1.13
	if not exist spigot-1.13.1.jar echo Building Spigot 1.13.1 && java -jar BuildTools.jar --rev 1.13.1
	if not exist spigot-1.13.2.jar echo Building Spigot 1.13.2 && java -jar BuildTools.jar --rev 1.13.2
	
	if not exist spigot-1.14.jar echo Building Spigot 1.14 && java -jar BuildTools.jar --rev 1.14
	if not exist spigot-1.14.1.jar echo Building Spigot 1.14.1 && java -jar BuildTools.jar --rev 1.14.1
	if not exist spigot-1.14.2.jar echo Building Spigot 1.14.2 && java -jar BuildTools.jar --rev 1.14.2
	if not exist spigot-1.14.3.jar echo Building Spigot 1.14.3 && java -jar BuildTools.jar --rev 1.14.3
	if not exist spigot-1.14.4.jar echo Building Spigot 1.14.4 && java -jar BuildTools.jar --rev 1.14.4
	
	copy spigot-1.13.jar spigotlibs
	copy spigot-1.13.1.jar spigotlibs
	copy spigot-1.13.2.jar spigotlibs
	
	copy spigot-1.14.jar spigotlibs
	copy spigot-1.14.1.jar spigotlibs
	copy spigot-1.14.2.jar spigotlibs
	copy spigot-1.14.3.jar spigotlibs
	copy spigot-1.14.4.jar spigotlibs
	
	echo Done!
)
