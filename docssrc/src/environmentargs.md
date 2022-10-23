# Environment arguments

![An environment argument with the suggestions minecraft:overworld, minecraft:the_end and minecraft:the_nether](./images/arguments/environment.png)

The `EnvironmentArgument` class allows a command sender to refer to a specific world environment, declared in Bukkit's `World.Environment` class. This includes the following three environments: `NORMAL`, `NETHER` and `THE_END`.

<div class="example">

### Example - Creating a new world

Say we want to create a new world on our Minecraft server. To do this, we need to know the name of the world, and the type (i.e. overworld, nether or the end). As such, we want to create a command with the following syntax:

```mccmd
/createworld <worldname> <type>
```

Using the world name and the environment of the world, we can use Bukkit's `WorldCreator` to create a new world that matches our provided specifications:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-examples/src/test/dev/jorel/commandapi/examples/java/Examples.java:environmentarguments}}
```

```kotlin,Kotlin
{{#include ../../commandapi-examples/src/test/dev/jorel/commandapi/examples/kotlin/Examples.kt:environmentarguments}}
```

</div>

</div>
