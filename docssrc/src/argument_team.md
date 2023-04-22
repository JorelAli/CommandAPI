# Team arguments

The `TeamArgument` class interacts with the Minecraft scoreboard and represents a team.

<div class="example">

### Example - Toggling friendly fire in a team

Let's say we want to create a command to toggle the state of friendly fire in a team. We want a command of the following form

```mccmd
/togglepvp <team>
```

To do this, given a team we want to use the `setAllowFriendlyFire(boolean)` function.

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentTeam1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentTeam1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentTeam1}}
```

</div>

</div>
