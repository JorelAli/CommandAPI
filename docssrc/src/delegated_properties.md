# Delegated properties

The CommandAPI offers an additional way to access arguments when using Kotlin: [delegated properties](https://kotlinlang.org/docs/delegated-properties.html). With delegated properties, there are two possible dependencies you can use:

1. `commandapi-core-kotlin`

   Support for delegated properties has been added to the `commandapi-core-kotlin` module. If you want to use delegated properties, you need to add this dependency.

2. `commandapi-bukkit-kotlin`

   If you are already using the Kotlin DSL to create your commands, you can already use delegated properties. `commandapi-core-kotlin` is included in `commandapi-bukkit-kotlin`.

### Access arguments using delegated properties

To be able to access arguments by using delegated properties, your variable name needs to match the node name of the argument. This could look like this:

<div class="multi-pre">

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:delegatedProperties1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:delegatedProperties1}}
```

</div>
