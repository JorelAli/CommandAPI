# commandapi-test-maven-plugin

Forked from https://github.com/tr7zw/spigotmc-maven-plugin

A custom implementation of [@tr7zw](https://github.com/tr7zw)'s spigotmc-maven-plugin with a few extra tweaks and improvements to speed things up. Designed specifically for the CommandAPI's `commandapi-test` module.

## Goals

- Primarily use PaperSpigot for testing because it's faster
- Faster loading sequence with `-DIReallyKnowWhatIAmDoingISwear=`
- Don't use "Done" as the main test for determining if it should be listening for output or not

**Original README.md below:**

-----

# SpigotMC Maven Plugin

Forked from https://github.com/jaxzin/spigotmc-maven-plugin

A Maven Plugin for running a SpigotMC server.
The main use case is integration testing Bukkit/Spigot plugins with NMS access.

# Usage

## Example from my NBT-Api's pom.xml:
```
<project>
...
    <build>
        <plugins>
            <plugin>
                <groupId>de.tr7zw.spigotmc.maven</groupId>
                <artifactId>tr-spigotmc-maven-plugin</artifactId>
                <version>0.0.1-SNAPSHOT</version>
				<configuration>
					<versions>1.8.8-R0.1-SNAPSHOT-latest, 1.9.2-R0.1-SNAPSHOT-latest, 1.9.4-R0.1-SNAPSHOT-latest, 1.10.2-R0.1-SNAPSHOT-latest,1.11.2,1.12.2,1.13.2,1.14.1</versions>
					<works>Success!</works>
					<error>WARNING!</error>
					<filename>item-nbt-api-plugin-1.8.3-SNAPSHOT.jar</filename>
				</configuration>
                <executions>
                    <execution>
                        <id>start-server</id>
                        <goals>
                            <goal>start</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>stop-server</id>
                        <goals>
                            <goal>stop</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

## How it works

During the maven integration-test phase a spigot server will be started once for each version in the versions configuration.
Put the the versions in a upwards compatible order, since the world will only be deleted at the beginning.
On each start the maven plugin will watch out for a given message rather the plugin works or errors out.

For testing:
```
mvn integration-test
```

## Todo

- Get the plugin file non static
- Get the older versions running without "-R0.1-SNAPSHOT-latest"
- Make the project more universal