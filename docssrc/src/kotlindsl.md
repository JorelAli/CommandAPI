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

There are two arguments not having a method which directly corresponds to their respective argument.

These two arguments are the `CustomArgument` and the `ListArgument` as they require further implementation by the user.

To use these arguments, the DSL also provides the `argument()` method. This takes in any argument as parameter which is why you can also use this method when you need to replace suggestions of any argument or do something else with arguments.

</div>

-----

## Requirements

When using the DSL, you might want to use requirements to restrict the use of certain arguments to specific players. However, implementing requirements with this DSL works a bit different from when using the "normal" command system.

Below, the `sendMessageTo` command is adding a broadcast option which should only be executed by server operators.

<div class="multi-pre">

```kotlin,CommandTree
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:kotlindsl3}}
```

```kotlin,CommandAPICommand
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:kotlindsl4}}
```

</div>

### Adding requirements to commands

Previously, we've taken a look at how to restrict arguments to certain players by using requirements.

You can also restrict the use of a whole command by using requirements:

<div class="multi-pre">

```kotlin,CommandTree
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:kotlindsl5}}
```

```kotlin,CommandAPICommand
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:kotlindsl6}}
```

</div>

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

As with the Java method of registering commands, with a CommandTree you can declare the command tree to include the additional argument `<amount>`, and with the CommandAPICommand you need to set the `optional` value to `true`:

<div class="multi-pre">

```kotlin,CommandTree
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:kotlindsl7}}
```

```kotlin,CommandAPICommand
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:kotlindsl8}}
```

</div>

</div>

<div class="example">

### Example - Replacing suggestions using the Kotlin DSL

We want to create a command with the following syntax to demonstrate replacing suggestions using the Kotlin DSL:

```mccmd
/replaceSuggestions <strings>
```

We make use of the `argument()` method to provide an argument implementation and use the `replaceSuggestions` method as normal to provide suggestions for the argument:

<div class="multi-pre">

```kotlin,CommandTree
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:kotlindsl9}}
```

```kotlin,CommandAPICommand
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:kotlindsl10}}
```

</div>

</div>
