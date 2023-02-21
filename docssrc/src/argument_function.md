# Function arguments

![An image of a function argument showing a list of Minecraft functions and tags that are available to run](./images/arguments/functions.png)

The `FunctionArgument` class is used to represent a function or a tag in Minecraft. When retrieving an instance of the argument, it will return a `FunctionWrapper[]`, where each `FunctionWrapper` consists of a Minecraft function.

Therefore, if a user supplies a single function, the `FunctionWrapper[]` will be of size 1, and if the user supplies a tag which can consist of multiple functions, the `FunctionWrapper[]` will consist of the array of functions as declared by that tag.

<div class="example">

### Example - Minecraft's /function command

Since it's a little difficult to demonstrate a custom use for the `FunctionArgument`, we will show how you can implement Vanilla Minecraft's `/function` command. In this example, we want a command that uses the following syntax:

```mccmd
/runfunction <function>
```

When provided with a function, it will execute that function. If instead a tag is provided, it will execute that tag (i.e. execute all functions declared in that tag).

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentFunction1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentFunction1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentFunction1}}
```

</div>

</div>
