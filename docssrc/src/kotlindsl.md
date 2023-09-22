# Using the DSL

## Defining a simple message command

As a first example and to take a first look at the Kotlin DSL syntax, we will first create a simple command to send messages to a player.

<div class="example">

### Example - Sending a message to a player using the Kotlin DSL

We want to create a command that lets us send a message to a player. To do this, we want to register a command with the following syntax:

```mccmd
/sendmessageto <player> <msg>
```

We can then use the following command registration:

<div class="multi-pre">

```kotlin,CommandTree
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:kotlindsl1}}
```

```kotlin,CommandAPICommand
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:kotlindsl2}}
```

</div>

Here you can see some interesting things:

- You do not need to call the `.register()` method when using the DSL
- You do not need to initialise any arguments

</div>

-----

## Executors

The Kotlin DSL also provides executors to execute your command. You've seen the `anyExecutor` in the example above.

To find out, which DSL executor corresponds to "normal" executors, you can refer to the table below:

| DSL Executor             | "normal" Executor        |
|--------------------------|--------------------------|
| `anyExecutor()`          | `executes()`             |
| `playerExecutor()`       | `executesPlayer()`       |
| `entityExecutor()`       | `executesEntity()`       |
| `consoleExecutor()`      | `executesConsole()`      |
| `commandBlockExecutor()` | `executesCommandBlock()` |
| `proxyExecutor()`        | `executesProxy()`        |
| `nativeExecutor()`       | `executesNative()`       |

-----

## Arguments

The DSL implements almost every argument with a method. You've seen the `playerArgument()` and the `greedyStringArgument()` method in the example at the top of this page.

The way arguments are implemented is pretty straight forward: It's basically the argument class' name, but as a method. So if you wanted to use a `ItemStackArgument` in your command, you would use the `itemStackArgument()` method of the DSL.

One thing to note is that the DSL also features every existing constructor. This means if you want to use an `IntegerArgument` with a minimum of `0` and a maximum of `10`, you normally would implement it like this:

```java
new IntegerArgument("integer", 0, 10)
```

However, when using this DSL it is implemented like this:

```kotlin
integerArgument("integer", 0, 10)
```

<div class="warning">

**Developer's Note:**

There are a few arguments not having a method which directly corresponds to their respective argument.

These arguments most likely use a builder pattern and because of that require further implementation by the user.

To use these arguments, the DSL also provides the `argument()` method which takes in any argument as a parameter.

</div>

-----

## Editing arguments

When using the DSL, you might want to modify the behaviour of certain arguments by adding requirements or suggestions to them.

To give you a general idea how you could accomplish that, the `sendMessageTo` command is adding a broadcast option which should only be executed by server operators.

<div class="multi-pre">

```kotlin,CommandTree
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:kotlindsl3}}
```

```kotlin,CommandAPICommand
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:kotlindsl4}}
```

</div>

Notice how you can just add the requirement in a CommandTree by adding it to the argument block where you also define the next arguments and the executor.

However, when modifying the behaviour of an argument in a CommandAPICommand you have to add an extra block where you can implement the additional behaviour.

### Adding requirements to commands

Expanding on the previous example where we added a requirement to a single argument, we now also want to add a requirement to a whole command.

This works similar to how argument behaviour is modified in a CommandTree:

<div class="multi-pre">

```kotlin,CommandTree
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:kotlindsl5}}
```

```kotlin,CommandAPICommand
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:kotlindsl6}}
```

</div>

## Delegations

The Kotlin DSL offers an additional way to access arguments: [delegated properties](https://kotlinlang.org/docs/delegated-properties.html). With delegated properties, there are two possible dependencies you can use:

1. `commandapi-core-kotlin`

   Support for delegated properties in the Kotlin DSL has been added to the `commandapi-core-kotlin` module. If you want to use delegated properties, you need to add this dependency.

2. `commandapi-bukkit-kotlin`

   If you are already using the Kotlin DSL to create your commands, you can already use delegated properties. `commandapi-core-kotlin` is included in `commandapi-bukkit-kotlin`.

### Access arguments using delegated properties

To be able to access arguments by using delegated properties, your variable name needs to match the node name of the argument. This could look like this:

<div class="multi-pre">

```kotlin,Delegated_properties_example
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:kotlindsl7}}
```

</div>

> **Developer's Note:**
>
> Because delegated properties target the way you can access arguments, you can also use delegated properties when using Kotlin, but not the Kotlin DSL to create your commands.
>
> Just keep in mind that you need to at least add `commandapi-core-kotlin` as a dependency.

-----

## More examples

Now, a few more examples are shown to demonstrate the use of this DSL a little more:

<div class="example">

### Example - Implementing optional arguments with the Kotlin DSL

We want to create a `/give` command with the following syntax:

```mccmd
/optionalArgument give <item>
/optionalArgument give <item> <amount>
```

To declare an argument as optional you need to set the `optional` value to `true`:

<div class="multi-pre">

```kotlin,CommandTree
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:kotlindsl8}}
```

```kotlin,CommandAPICommand
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:kotlindsl9}}
```

</div>

</div>

<div class="example">

### Example - Replacing suggestions using the Kotlin DSL

We want to create a command with the following syntax to demonstrate replacing suggestions using the Kotlin DSL:

```mccmd
/replaceSuggestions <strings>
```

Replacing suggestions works similar to how you would add a requirement to an argument as shown in [Editing arguments](#editing-arguments).

You just have to use the `replaceSuggestions` method this time:

<div class="multi-pre">

```kotlin,CommandTree
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:kotlindsl10}}
```

```kotlin,CommandAPICommand
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:kotlindsl11}}
```

</div>

</div>
