# Shading the CommandAPI in your plugins

"Shading" is the process of including the CommandAPI inside your plugin, rather than requiring the CommandAPI as an external plugin. In other words, if you shade the CommandAPI into your plugin, you don't need to include the `CommandAPI.jar` in your server's plugins folder.

-----

## Shading vs CommandAPI plugin

The CommandAPI plugin has a few slight differences with the shaded CommandAPI jar file. The CommandAPI plugin has the following extra features that are not present in the shaded version:

- Command conversion via a `config.yml` file

-----

## Shading requirements

For the CommandAPI to function as normal, you **must** call the CommandAPI's initializers in the `onLoad()` and `onEnable()` methods of your plugin:

```java
CommandAPI.onLoad(CommandAPIConfig config);
CommandAPI.onEnable();
```

If you want to handle reloading, the CommandAPI has minimal support for it with the `onDisable()` method, which can go in your plugin. This is optional and is not required if you don't plan on reloading the server.

### Loading

The `onLoad(CommandAPIConfig)` method initializes the CommandAPI's loading sequence. This must be called _before_ you start to access the CommandAPI and must be placed in your plugin's `onLoad()` method. The argument `CommandAPIConfig` is used to configure how the CommandAPI works. The `CommandAPIConfig` class has the following parameters which let you set how the CommandAPI works similar to the `config.yml`, which is described [here](./config.md).

```java
public class CommandAPIConfig {
    CommandAPIConfig verboseOutput(boolean value); // Enables verbose logging
    CommandAPIConfig silentLogs(boolean value);    // Disables ALL logging (except errors)
    CommandAPIConfig useLatestNMSVersion(boolean value); // Whether the latest NMS implementation should be used or not
    CommandAPIConfig missingExecutorImplementationMessage(String value); // Set message to display when executor implementation is missing
    CommandAPIConfig dispatcherFile(File file); // If not null, the CommandAPI will create a JSON file with Brigadier's command tree
    CommandAPIConfig setNamespace(String namespace); // The namespace to use when the CommandAPI registers a command
    CommandAPIConfig usePluginNamespace(); // Whether the CommandAPI should use the name of the plugin passed into the CommandAPIConfig implementation as the default namespace for commands

    <T> CommandAPIConfig initializeNBTAPI(Class<T> nbtContainerClass, Function<Object, T> nbtContainerConstructor); // Initializes hooks with an NBT API. See NBT arguments documentation page for more info
}
```

The `CommandAPIConfig` class follows a typical builder pattern (without you having to run `.build()` at the end), which lets you easily construct configuration instances.

However, the `CommandAPIConfig` class is abstract and cannot be used to configure the CommandAPI directly. Instead, you must use a subclass of `CommandAPIConfig` that corresponds to the platform you are developing for. For example, when developing for a Bukkit-based server, you should use the `CommandAPIPaperConfig` or the `CommandAPISpigotConfig` class.

<!-- TODO: Add tabs and explanations for other platforms -->

<div class="multi-pre">

```java,Bukkit
public abstract class CommandAPIBukkitConfig extends CommandAPIConfig {
    CommandAPIBukkitConfig skipReloadDatapacks(boolean skip); // Whether the CommandAPI should skip its initial datapack reload step
}
```

```java,Paper
public class CommandAPIPaperConfig extends CommandAPIBukkitConfig {
    CommandAPIPaperConfig(JavaPlugin plugin);
    
    CommandAPIPaperConfig shouldHookPaperReload(boolean hooked); // Whether the CommandAPI should hook into the Paper-exclusive ServerResourcesReloadedEvent
}
```

```java,Spigot
public class CommandAPISpigotConfig extends CommandAPIBukkitConfig {
    CommandAPISpigotConfig(JavaPlugin plugin);
}
```

</div>

In order to create a `CommandAPIPaperConfig` or a `CommandAPISpigotConfig` object, you must give it a reference to your `JavaPlugin` instance. The CommandAPI always uses this to registers events, so it is required when loading the CommandAPI on Bukkit. There are also platform-specific features, such as the `hook-paper-reload` configuration option on Paper, which may be configured using a `CommandAPIPaperConfig` instance.

For example, to load the CommandAPI on a Bukkit-based server with all logging disabled, you can use the following:

<div class="multi-pre">

```java,Java_(Paper)
{{#include ../../commandapi-platforms/commandapi-paper/commandapi-paper-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:setupShading1}}
```

```java,Java_(Spigot)
{{#include ../../commandapi-platforms/commandapi-spigot/commandapi-spigot-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:setupShading1}}
```

```kotlin,Kotlin_(Paper)
{{#include ../../commandapi-platforms/commandapi-paper/commandapi-paper-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:setupShading1}}
```

```kotlin,Kotlin_(Spigot)
{{#include ../../commandapi-platforms/commandapi-spigot/commandapi-spigot-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:setupShading1}}
```

</div>

### Enabling & Disabling

The `onEnable()` method initializes the CommandAPI's enabling sequence. Similar to the `onLoad(CommandAPIConfig)` method, this must be placed in your plugin's `onEnable()` method. This isn't as strict as the `onLoad(CommandAPIConfig)` method, and can be placed anywhere in your `onEnable()` method.

The `onDisable()` method disables the CommandAPI gracefully. This should be placed in your plugin's `onDisable()` method. This doesn't unregister commands, so commands may persist during reloads - this can be mitigated using the `CommandAPI.unregister()` method.

<div class="example">

### Example - Setting up the CommandAPI in your plugin

<div class="multi-pre">

```java,Java_(Paper)
public {{#include ../../commandapi-platforms/commandapi-paper/commandapi-paper-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:setupShading2}}
```

```java,Java_(Spigot)
public {{#include ../../commandapi-platforms/commandapi-spigot/commandapi-spigot-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:setupShading2}}
```

```kotlin,Kotlin_(Paper)
{{#include ../../commandapi-platforms/commandapi-paper/commandapi-paper-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:setupShading2}}
```

```kotlin,Kotlin_(Spigot)
{{#include ../../commandapi-platforms/commandapi-spigot/commandapi-spigot-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:setupShading2}}
```

</div>

</div>

-----

## A note about relocating

By default, the CommandAPI is written in the `dev.jorel.commandapi` package. It is **highly recommended** to "relocate" the shaded copy of the CommandAPI to your own package instead to prevent package clashes with other projects that shade the CommandAPI:

\begin{align}
&\qquad\texttt{dev.jorel.commandapi} \rightarrow \texttt{my.custom.package.commandapi} \\\\
\end{align}

-----

## Shading with Maven

When shading the CommandAPI, you have the option to choose between our version for Paper and our version for Spigot. The respective artifacts are the `commandapi-paper-shade` and the `commandapi-spigot-shade` modules. These are optimized for shading and don't include plugin specific files _(such as `plugin.yml`)_.
You also can choose between a Mojang-mapped and a Spigot-mapped version:

Add the CommandAPI shade dependency:

<div class="multi-pre">

```xml,Paper_(Spigot_Mappings)
<dependencies>
    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-paper-shade</artifactId>
        <version>9.6.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

```xml,Paper_(Mojang_Mappings)
<dependencies>
    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-paper-shade-mojang-mapped</artifactId>
        <version>9.6.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

```xml,Spigot_(Spigot_Mappings)
<dependencies>
    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-spigot-shade</artifactId>
        <version>9.6.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

```xml,Spigot_(Mojang_Mappings)
<dependencies>
    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-spigot-shade-mojang-mapped</artifactId>
        <version>9.6.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

</div>

You can shade the CommandAPI easily by adding the `maven-shade-plugin` to your build sequence using version `3.3.0` (compatible with Java 16):

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.3.0</version>
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
                <minimizeJar>true</minimizeJar>
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

As we're shading the CommandAPI into your plugin, you **don't** need to add `depend: [CommandAPI]` to your `plugin.yml` file.

-----

## Shading with Gradle

To shade the CommandAPI into a Gradle project, we'll use the [Goooler Gradle Shadow Plugin](https://plugins.gradle.org/plugin/io.github.goooler.shadow). This is a fork of the [Shadow Plugin](https://imperceptiblethoughts.com/shadow/) which supports Java 21. Add this to your list of plugins:

<div class="multi-pre">

```groovy,build.gradle
plugins {
    id 'java'
    id 'io.github.goooler.shadow' version '8.1.7'
}
```

```kotlin,build.gradle.kts
plugins {
    java
    id("io.github.goooler.shadow") version "8.1.7"
}
```

</div>

Add our repositories:

<div class="multi-pre">

```groovy,build.gradle
repositories {
    mavenCentral()

    // If you want to shade the NBT API as well
    maven { url = "https://repo.codemc.org/repository/maven-public/" }
}
```

```kotlin,build.gradle.kts
repositories {
    mavenCentral()

    // If you want to shade the NBT API as well
    maven(url = "https://repo.codemc.org/repository/maven-public/")
}
```

</div>

Next, we declare our dependencies:

<div class="multi-pre">

```groovy,build.gradle_(Spigot_Mappings_(Paper))
dependencies {
    implementation "dev.jorel:commandapi-paper-shade:9.6.0-SNAPSHOT"
}
```

```groovy,build.gradle_(Spigot_Mappings_(Spigot))
dependencies {
    implementation "dev.jorel:commandapi-spigot-shade:9.6.0-SNAPSHOT"
}
```

```groovy,build.gradle_(Mojang_Mappings_(Paper))
dependencies {
    implementation "dev.jorel:commandapi-paper-shade-mojang-mapped:9.6.0-SNAPSHOT"
}
```

```groovy,build.gradle_(Mojang_Mappings_(Spigot))
dependencies {
    implementation "dev.jorel:commandapi-spigot-shade-mojang-mapped:9.6.0-SNAPSHOT"
}
```

```kotlin,build.gradle.kts_(Spigot_Mappings_(Paper))
dependencies {
    implementation("dev.jorel:commandapi-paper-shade:9.6.0-SNAPSHOT")
}
```

```kotlin,build.gradle.kts_(Spigot_Mappings_(Spigot))
dependencies {
    implementation("dev.jorel:commandapi-spigot-shade:9.6.0-SNAPSHOT")
}
```

```kotlin,build.gradle.kts_(Mojang_Mappings_(Paper))
dependencies {
    implementation("dev.jorel:commandapi-paper-shade-mojang-mapped:9.6.0-SNAPSHOT")
}
```

```kotlin,build.gradle.kts_(Mojang_Mappings_(Spigot))
dependencies {
    implementation("dev.jorel:commandapi-spigot-shade-mojang-mapped:9.6.0-SNAPSHOT")
}
```

</div>

Then we add it to the `shadowJar` task configuration and relocate the CommandAPI to your desired location:

<div class="multi-pre">

```groovy,build.gradle_(Spigot_Mappings_(Paper))
shadowJar {
    dependencies {
        include dependency("dev.jorel:commandapi-paper-shade:9.6.0-SNAPSHOT")
    }

    // TODO: Change this to my own package name
    relocate("dev.jorel.commandapi", "my.custom.package.commandapi")
}
```

```groovy,build.gradle_(Spigot_Mappings_(Spigot))
shadowJar {
    dependencies {
        include dependency("dev.jorel:commandapi-spigot-shade:9.6.0-SNAPSHOT")
    }

    // TODO: Change this to my own package name
    relocate("dev.jorel.commandapi", "my.custom.package.commandapi")
}
```

```groovy,build.gradle_(Mojang_Mappings_(Paper))
shadowJar {
    dependencies {
        include dependency("dev.jorel:commandapi-paper-shade-mojang-mapped:9.6.0-SNAPSHOT")
    }

    // TODO: Change this to my own package name
    relocate("dev.jorel.commandapi", "my.custom.package.commandapi")
}
```

```groovy,build.gradle_(Mojang_Mappings_(Spigot))
shadowJar {
    dependencies {
        include dependency("dev.jorel:commandapi-spigot-shade-mojang-mapped:9.6.0-SNAPSHOT")
    }

    // TODO: Change this to my own package name
    relocate("dev.jorel.commandapi", "my.custom.package.commandapi")
}
```

```kotlin,build.gradle.kts_(Spigot_Mappings_(Paper))
tasks.withType<ShadowJar> {
    dependencies {
        include(dependency("dev.jorel:commandapi-paper-shade:9.6.0-SNAPSHOT"))
    }

    // TODO: Change this to my own package name
    relocate("dev.jorel.commandapi", "my.custom.package.commandapi")
}
```

```kotlin,build.gradle.kts_(Spigot_Mappings_(Spigot))
tasks.withType<ShadowJar> {
    dependencies {
        include(dependency("dev.jorel:commandapi-spigot-shade:9.6.0-SNAPSHOT"))
    }

    // TODO: Change this to my own package name
    relocate("dev.jorel.commandapi", "my.custom.package.commandapi")
}
```

```kotlin,build.gradle.kts_(Mojang_Mappings_(Paper))
tasks.withType<ShadowJar> {
    dependencies {
        include(dependency("dev.jorel:commandapi-paper-shade-mojang-mapped:9.6.0-SNAPSHOT"))
    }

    // TODO: Change this to my own package name
    relocate("dev.jorel.commandapi", "my.custom.package.commandapi")
}
```

```kotlin,build.gradle.kts_(Mojang_Mappings_(Spigot))
tasks.withType<ShadowJar> {
    dependencies {
        include(dependency("dev.jorel:commandapi-spigot-shade-mojang-mapped:9.6.0-SNAPSHOT"))
    }

    // TODO: Change this to my own package name
    relocate("dev.jorel.commandapi", "my.custom.package.commandapi")
}
```

</div>

Finally, we can build the shaded jar using the following command:

```bash
gradlew build shadowJar
```

As we're shading the CommandAPI into your plugin, we **don't** need to add `depend: [CommandAPI]` to your `plugin.yml` file.
