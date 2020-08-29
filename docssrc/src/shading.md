# Shading the CommandAPI in your plugins

<p align="center"><i>After 2 years, this most requested feature is finally here...</i></p>

The CommandAPI can now be shaded into your own plugins! "Shading" is the process of including the CommandAPI inside your plugin, rather than requiring the CommandAPI as an external plugin. In other words, if you shade the CommandAPI into your plugin, you don't need to include the `CommandAPI.jar` in your server's plugins folder.

-----

## Shading with Maven

To shade the CommandAPI into a maven project, you'll need to use the `commandapi-shade` dependency, which is optimized for shading and doesn't include plugin-specific files _(such as `plugin.yml`)_:

```xml
<dependencies>
	<dependency>
		<groupId>dev.jorel</groupId>
        <artifactId>commandapi-shade</artifactId>
        <version>4.0</version>
    </dependency>
</dependencies>
```

Once you've added this this, you can shade the CommandAPI easily by adding the `maven-shade-plugin` to your build sequence:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.2.4</version>
            <executions>
                <execution>
                    <id>shade</id>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <relocations>
                    <relocation>
                        <pattern>dev.jorel.commandapi-shade</pattern>
                    </relocation>
                </relocations>
            </configuration>
        </plugin>
    </plugins>
</build>
```

Of course, if you shade the CommandAPI into your plugin, you don't need to add `depend: [CommandAPI]` to your `plugin.yml` file.