# LootTable argument

![A loot table argument showing a list of Minecraft loot tables as suggestions](./images/arguments/loottable.png)

The `LootTableArgument` class can be used to get a Bukkit `LootTable` object.

<div class="example">

### Example - Filling a chest with loot table contents

Say we wanted to write a command that populates a chest with some loot table contents. For this example, we'll use the following command:

```mccmd
/giveloottable <loottable> <location>
```

We ensure that the location provided is a container (such as a chest or shulkerbox) and then update the contents of that container:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentLootTable1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentLootTable1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentLootTable1}}
```

</div>

</div>
