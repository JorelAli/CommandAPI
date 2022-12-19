# kotlin-dsl

A simple example showcasing creating a command using the Kotlin DSL for the CommandAPI!

Key points:

- You do not need to use the `.register()` method
- You do not need to initialise any arguments.
- Add the `commandapi-kotlin-bukkit` dependency to your project
```xml
<dependency>
	<groupId>dev.jorel</groupId>
	<artifactId>commandapi-kotlin-bukkit</artifactId>
	<version>9.0.0-SNAPSHOT</version>
</dependency>
```
- Shade the Kotlin DSL into your plugin jar
```xml
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-shade-plugin</artifactId>
	<version>3.3.1-SNAPSHOT</version>
	<executions>
		<execution>
			<phase>package</phase>
			<goals>
				<goal>shade</goal>
			</goals>
			<configuration>
				<createDependencyReducedPom>false</createDependencyReducedPom>
				<relocations>
					<relocation>
						<pattern>dev.jorel.commandapi.kotlindsl</pattern>
						<shadedPattern>io.github.jorelali.commandapi.kotlindsl</shadedPattern>
					</relocation>
				</relocations>
			</configuration>
		</execution>
	</executions>
</plugin>
```