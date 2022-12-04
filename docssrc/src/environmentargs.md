# Environment arguments

<div class="warning">

**Developer's Note:**

The `EnvironmentArgument` is deprecated and will be removed in a future version!

If you still want some form of an "environment variable", you can implement it using a `MultiLiteralArgument` or a `CustomArgument` with the three values: `normal`, `nether`, `end`!

For reference, the old documentation is outlined below:

</div>

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
{{#include ../../commandapi-core/src/test/java/Examples.java:environmentarguments}}
```

```kotlin,Kotlin
{{#include ../../commandapi-core/src/test/kotlin/Examples.kt:environmentarguments}}
```

</div>

</div>
