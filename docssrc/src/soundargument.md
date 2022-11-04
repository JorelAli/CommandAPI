# Sound arguments

![A sound argument command with a list of Minecraft sounds as suggestions](./images/arguments/sound.png)

The `SoundArgument` class allows a command sender to retrieve the Bukkit `Sound` object to represent in-game sound effects (such as mob sounds or ambient sound effects), as well as in-game music.

<div class="example">

### Example - Playing sound to yourself

Say we want a simple command that plays a specific sound at your location. To do this, we will make the following command:

```mccmd
/sound <sound>
```

This command simply plays the provided sound to the current player:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/test/dev/jorel/commandapi/examples/java/Examples.java:soundarguments}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/test/dev/jorel/commandapi/examples/kotlin/Examples.kt:soundarguments}}
```

</div>

</div>
