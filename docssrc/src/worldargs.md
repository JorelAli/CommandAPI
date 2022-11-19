# World arguments

The `WorldArgument` allows you to select a loaded world on the server.

<div class="example">

### Example - Teleporting a player to another world

We want to create a command that teleports a player into a world. For this we register this command:

```mccmd
/teleportto <player> <world> <location>
```

We do this registering a command like this:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-core/src/test/java/Examples.java:worldarguments}}
```

```kotlin,Kotlin
{{#include ../../commandapi-core/src/test/kotlin/Examples.kt:worldarguments}}
```

</div>

</div>