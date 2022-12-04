# Sound arguments

![A sound argument command with a list of Minecraft sounds as suggestions](./images/arguments/sound.png)

The `SoundArgument` class allows a command sender to retrieve the Bukkit `Sound` or `NamespacedKey` object to represent in-game sound effects (such as mob sounds or ambient sound effects), as well as in-game music.

The `SoundArgument` must be parameterized over `Sound` or `NamespacedKey` to indicate whether it will return a `Sound` object or a `NamespacedKey` object. A `SoundType` can be provided to specify whether the `SoundArgument` will return a `Sound` or `NamespacedKey`. If no `SoundType` is provided, the `SoundArgument` will default to returning a `Sound` object:

```java
// Makes a SoundArgument that returns a Sound
new SoundArgument<Sound>("sound");
new SoundArgument<Sound>("sound", SoundType.SOUND);

// Makes a SoundArgument that returns a NamespacedKey
new SoundArgument<NamespacedKey>("sound", SoundType.NAMESPACED_KEY);
```

<div class="example">

### Example - Playing sound to yourself

Say we want a simple command that plays a specific sound at your location. To do this, we will make the following command:

```mccmd
/sound <sound>
```

This command simply plays the provided sound to the current player:
TODO: Make sure all docs reference Examples in the right place
```
From {{#include ../../commandapi-core/src/test/java/Examples.java
To {{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java

From {{#include ../../commandapi-core/src/test/kotlin/Examples.kt
To {{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt
```
<div class="multi-pre">

```java,Java_(Sound)
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:soundarguments}}
```

```java,Java_(NamespacedKey)
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:soundarguments2}}
```

```kotlin,Kotlin_(Sound)
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:soundarguments}}
```

```kotlin,Kotlin_(NamespacedKey)
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:soundarguments2}}
```

</div>

</div>
