# BlockState arguments

![A block state argument with suggestions for Minecraft items](./images/arguments/blockstate.png)

The `BlockStateArgument` is used to represent data about blocks in the world. These refer to any blocks that have data or states, such as dispensers, signs, doors and pistons. The `BlockStateArgument` creates a Bukkit `BlockData` object when used.

> **Developer's Note:**
>
> Make sure to not confuse the cast type with `BlockState`. The naming of this argument refers to the internal Minecraft vanilla argument naming convention - **this argument casts to `BlockData` and NOT `BlockState`**.

<div class="example">

### Example - Setting a block

Say we want a simple command to set the block that you're looking at. We'll use the following command syntax:

```mccmd
/set <block>
```

And then we can simply set our block using `setBlockData()`:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentBlockState1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentBlockState1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentBlockState1}}
```

</div>

</div>
