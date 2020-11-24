# Shading the CommandAPI in your plugins

<p align="center"><i>After 2 years, this most requested feature is finally here...</i></p>

The CommandAPI can now be shaded into your own plugins! "Shading" is the process of including the CommandAPI inside your plugin, rather than requiring the CommandAPI as an external plugin. In other words, if you shade the CommandAPI into your plugin, you don't need to include the `CommandAPI.jar` in your server's plugins folder.

-----

## Shading vs CommandAPI plugin

The CommandAPI plugin has a few slight differences with the shaded CommandAPI jar file. The CommandAPI plugin has the following extra features that are not present in the shaded version:

- Command conversion via a `config.yml` file
- Creation of the `command_registration.json` file to show the Brigadier command graph

-----

## Shading requirements

For the CommandAPI to function as normal, you **must** call the CommandAPI's initializers in the `onLoad()` and `onEnable()` methods of your plugin:

```java
CommandAPI.onLoad(boolean verbose);
CommandAPI.onEnable(Plugin plugin);
```

The `onLoad(boolean)` method initializes the CommandAPI's loading sequence. This must be called _before_ you start to access the CommandAPI and must be placed in your plugin's `onLoad()` method. The argument `verbose` is used to enable verbose logging output.

The `onEnable(Plugin)` method initializes the CommandAPI's enabling sequence. As with the `onLoad(boolean)` method, this one must be placed in your plugin's `onEnable()` method. This isn't as strict as the `onLoad(boolean)` method, and can be placed anywhere in your `onEnable()` method. The argument `plugin` is your current plugin instance.

<div class="example">

### Example - Setting up the CommandAPI in your plugin

```java
public {{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:shading}}
```


</div>

-----

## Shading with Maven

To shade the CommandAPI into a maven project, you'll need to use the `commandapi-shade` dependency, which is optimized for shading and doesn't include plugin-specific files _(such as `plugin.yml`)_:

```xml
<dependencies>
	<dependency>
		<groupId>dev.jorel</groupId>
        <artifactId>commandapi-shade</artifactId>
        <version>6.0</version>
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

-----

## Shading with Gradle

To shade the CommandAPI into a Gradle project, we'll use the [Gradle Shadow Plugin](https://imperceptiblethoughts.com/shadow/). Add this to your list of plugins:

```gradle
plugins {
    id 'java'
    id 'com.github.johnrengelman.shadow' version '6.0.0'
}
```

Next, we declare our dependencies:

```gradle
dependencies {
    compile "dev.jorel:commandapi-shade:6.0"   
}
```

Then we add it to the `shadowJar` task configuration:

```gradle
shadowJar {
	dependencies {
		include dependency("dev.jorel:commandapi-shade:6.0")
	}
}
```

Finally, we can build the shaded jar using the following command:

```
gradlew build shadowJar
```

Again, as we're shading the CommandAPI into your plugin, we don't need to add `depend: [CommandAPI]` to your `plugin.yml` file.