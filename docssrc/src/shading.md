# Shading the CommandAPI in your plugins

"Shading" is the process of including the CommandAPI inside your plugin, rather than requiring the CommandAPI as an external plugin. In other words, if you shade the CommandAPI into your plugin, you don't need to include the `CommandAPI.jar` in your server's plugins folder.

-----

## Shading vs CommandAPI plugin

The CommandAPI plugin has a few slight differences with the shaded CommandAPI jar file. The CommandAPI plugin has the following extra features that are not present in the shaded version:

- Command conversion via a `config.yml` file
- Creation of the `command_registration.json` file to show the Brigadier command graph

-----

## Shading requirements

For the CommandAPI to function as normal, you **must** call the CommandAPI's initializers in the `onLoad()` and `onEnable()` methods of your plugin:

```java
CommandAPI.onLoad(CommandAPIConfig config);
CommandAPI.onEnable(Plugin plugin);
```

### Loading

The `onLoad(CommandAPIConfig)` method initializes the CommandAPI's loading sequence. This must be called _before_ you start to access the CommandAPI and must be placed in your plugin's `onLoad()` method. The argument `CommandAPIConfig` is used to configure how the CommandAPI. The `CommandAPIConfig` class has the following parameters which let you set how the CommandAPI works similar to the `config.yml`, which is described [here](./config.md).

```java
public class CommandAPIConfig {
    CommandAPIConfig verboseOutput(boolean value); // Enables verbose logging
    CommandAPIConfig silentLogs(boolean value);    // Disables ALL logging (except errors)
    CommandAPIConfig useLatestNMSVersion(boolean value); // Whether the latest NMS implementation should be used or not
    CommandAPIConfig missingExecutorImplementationMessage(String value); // Set message to display when executor implementation is missing
}
```

The `CommandAPIConfig` class follows a typical builder pattern (without you having to run `.build()` at the end), which lets you easily construct configuration instances. For example, to load the CommandAPI with all logging disabled, you can use the following:

```java
{{#include ../../commandapi-core/src/test/java/Examples.java:CommandAPIConfigSilent}}
```

### Enabling

The `onEnable(Plugin)` method initializes the CommandAPI's enabling sequence. As with the `onLoad(boolean)` method, this one must be placed in your plugin's `onEnable()` method. This isn't as strict as the `onLoad(boolean)` method, and can be placed anywhere in your `onEnable()` method. The argument `plugin` is your current plugin instance.

<div class="example">

### Example - Setting up the CommandAPI in your plugin

```java
public {{#include ../../commandapi-core/src/test/java/Examples.java:shading}}
```

</div>

-----

## A note about relocating

By default, the CommandAPI is written in the `dev.jorel.commandapi` package. It is **highly recommended** to "relocate" the shaded copy of the CommandAPI to your own package instead to prevent package clashes with other projects that shade the CommandAPI:

\begin{align}
&\qquad\texttt{dev.jorel.commandapi} \rightarrow \texttt{my.custom.package.commandapi} \\\\
\end{align}

-----

## Shading with Maven

To shade the CommandAPI into a maven project, you'll need to use the `commandapi-shade` dependency, which is optimized for shading and doesn't include plugin-specific files _(such as `plugin.yml`)_. **You do not need to use `commandapi-core` if you are shading**:

Add the CommandAPI shade dependency:

```xml
<dependencies>
    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-shade</artifactId>
        <version>8.3.1</version>
    </dependency>
</dependencies>
```

As of the time of writing, the latest stable version of the `maven-shade-plugin` is not compatible with Java 16, which means certain classes such as `record` types cannot be shaded. This can be overcome using the latest snapshot build of the `maven-shade-plugin`. To use the snapshot build, add the following plugin repository to your `pom.xml` file:

```xml
<pluginRepositories>
    <pluginRepository>
        <id>maven-snapshots</id>
        <url>https://repository.apache.org/content/repositories/snapshots/</url>
    </pluginRepository>
</pluginRepositories>
```

Once you've added this this, you can shade the CommandAPI easily by adding the `maven-shade-plugin` to your build sequence using the snapshot version `3.3.1-SNAPSHOT`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.3.1-SNAPSHOT</version>
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
                        <pattern>dev.jorel.commandapi</pattern>
                        <!-- TODO: Change this to my own package name -->
                        <shadedPattern>my.custom.package.commandapi</shadedPattern>
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
    id 'com.github.johnrengelman.shadow' version '7.1.2'
}
```

Add our repositories:

```gradle
repositories {
    mavenCentral()
    maven { url = "https://repo.codemc.org/repository/maven-public/" }
}
```

Next, we declare our dependencies:

```gradle
dependencies {
    implementation "dev.jorel:commandapi-shade:8.3.1"
}
```

Then we add it to the `shadowJar` task configuration and relocate the CommandAPI to your desired location:

```gradle
shadowJar {
    dependencies {
        include dependency("dev.jorel:commandapi-shade:8.3.1")
    }

    // TODO: Change this to my own package name
    relocate("dev.jorel.commandapi", "my.custom.package.commandapi")
}
```

Finally, we can build the shaded jar using the following command:

```bash
gradlew build shadowJar
```

Again, as we're shading the CommandAPI into your plugin, we **don't** need to add `depend: [CommandAPI]` to your `plugin.yml` file.
