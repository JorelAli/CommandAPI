# Sound arguments

![](./images/arguments/sound.png)

The `SoundArgument` class allows a command sender to retrieve the Bukkit `Sound` object to represent in-game sound effects (such as mob sounds or ambient sound effects), as well as in-game music.

<div class="example">

### Example - Playing sound to yourself

Say we want a simple command that plays a specific sound at your location. To do this, we will make the following command:

```mccmd
/sound <sound>
```

This command simply plays the provided sound to the current player:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:soundarguments}}
```

</div>