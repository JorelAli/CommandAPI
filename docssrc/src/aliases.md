# Aliases

Aliases for commands can be added by using the `withAliases()` method when registering a command. Aliases allow you to run the same command with a different 'name' from the original registered command name.

<div class="example">

### Example - Using aliases for /getpos

In this example, we register the command `/getpos` that returns the command sender's location. We apply the aliases `/getposition`, `/getloc`, `/getlocation` and `/whereami` as well, using the `withAliases()` method.

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:aliases1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:aliases1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:aliases1}}
```

</div>

</div>
