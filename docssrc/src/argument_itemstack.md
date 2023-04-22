# Itemstack arguments

![An item stack argument with suggestions for Minecraft items](./images/arguments/itemstack.png)

The `ItemStackArgument` class represents in-game items. As expected, this should be casted to Bukkit's `ItemStack` object. The `ItemStack` which is returned by the `ItemStackArgument` always has a size of 1.

<div class="example">

### Example - Giving a player an itemstack

Say we want to create a command that gives you items. For this command, we will use the following syntax:

```mccmd
/item <itemstack>
```

With this syntax, we can easily create our command:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentItemStack1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentItemStack1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentItemStack1}}
```

</div>

</div>
