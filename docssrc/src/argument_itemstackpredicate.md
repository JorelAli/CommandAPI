# ItemStack predicate arguments

Similar to the `BlockPredicateArgument`, the `ItemStackPredicateArgument` is a way of performing predicate checks on `ItemStack` objects. These can represent tags, such as the ones declared [here on the MinecraftWiki](https://minecraft.gamepedia.com/Tag#Items), or individual items. The cast type for this argument is `Predicate<ItemStack>`.

<div class="example">

### Example - Removing items in inventories based on predicates

Say we wanted to remove items in your inventory _(I know, the `/clear` command does this, but this is the only example I could come up with)_. To do this, we'll use the following command syntax:

```mccmd
/rem <item>
```

We implement this with a simple for loop over the player's inventory and remove items that satisfy the predicate.

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentItemStackPredicate1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentItemStackPredicate1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentItemStackPredicate1}}
```

</div>

</div>
