# Brigadier + CommandAPI

So far, we've been using only the CommandAPI to register commands. As a result, this makes the CommandAPI's features limited by whatever the CommandAPI has implemented. To push past these limits, the CommandAPI includes some extra methods to help with invoking brigadier methods. Of course, to use these methods, brigadier is required. The brigadier dependency's installation instructions can be found [here](https://github.com/Mojang/brigadier#installation).

> **Developer's Note:**
>
> For those that are unaware, [brigadier](https://github.com/Mojang/brigadier) is Mojang's command parser and dispatching framework. This is what the CommandAPI wraps around and is the main underlying source of its functionality.

The CommandAPI has been designed in such a way that you shouldn't have to access NMS in order to make use of the more "advanced" arguments and features - if you find that NMS is required to do something, [please make a new issue](https://github.com/JorelAli/CommandAPI/issues/new/choose)!

-----

## Brigadier support functions

The CommandAPI offers the following methods in the `dev.jorel.commandapi.Brigadier` class:

```java
public static CommandDispatcher getCommandDispatcher();
public static RootCommandNode getRootNode();
public static LiteralArgumentBuilder fromLiteralArgument(LiteralArgument literalArgument);
public static RedirectModifier fromPredicate(BiPredicate<CommandSender, Object[]> predicate, List<Argument> args);
public static Command fromCommand(CommandAPICommand command);
public static RequiredArgumentBuilder fromArgument(List<Argument> args, Argument<?> argument);
public static RequiredArgumentBuilder fromArgument(Argument argument);
public static SuggestionProvider toSuggestions(Argument<?> argument, List<Argument> args);
public static Object[] parseArguments(CommandContext cmdCtx, List<Argument> args);
public static Object getBrigadierSourceFromCommandSender(CommandSender sender);
public static CommandSender getBukkitCommandSenderFromContext(CommandContext cmdCtx);
```

Briefly, here's what each of these functions do (you can view the JavaDocs for more information):

| Method                 | Description                                                  |
| ---------------------- | ------------------------------------------------------------ |
| `getCommandDispatcher` | Returns the Minecraft command dispatcher graph               |
| `getRootNode`          | Returns the root node of the command dispatcher.<br>This is equivalent to using<br />`getCommandDispatcher().getRoot();` |
| `fromLiteralArgument`  | Creates a `LiteralArgumentBuilder` from a `LiteralArgument`  |
| `fromPredicate`        | Converts a predicate and some arguments into a `RedirectModifier`. This can be used for the `fork` method in brigadier's `ArgumentBuilder` |
| `fromCommand`          | Converts a `CommandAPICommand` into a brigadier `Command` object |
| `fromArgument`         | Converts an argument, or a list of arguments, into a `RequiredArgumentBuilder` |
| `toSuggestions`        | Converts an argument's suggestions into brigadier's `SuggestionProvider`, with a list of previously declared arguments |
| `parseArguments` | Parses a list of CommandAPI arguments into their respective objects for a provided `CommandContext` |
| `getBrigadierSourceFromCommandSender` | Converts a Bukkit `CommandSender` into the NMS command sender source object |
| `getBukkitCommandSenderFromContext` | Converts a Brigadier `CommandContext` into a Bukkit `CommandSender` |

-----

## Examples

I hope these examples help understand how the CommandAPI can help with registering more "powerful" commands with the use of brigadier as well! Please bear with with it - these examples can be long, but I'm certain that they've been explained well and will be useful!

<div class="example">

### Example - Adding a predicate to the 'execute' command

Say we wanted to add a predicate to the `/execute` command. In this example, we'll create a predicate which handles random chances. To illustrate this, we want to be able to run commands such as:

```mccmd
/execute if randomchance 1 4 run say Hello!
```

In this scenario, if we ran this command, we would expect "Hello!" to appear in the chat with a \\(\frac{1}{4}\\) chance. In particular, this is what we're trying to achieve:

- We want to create a predicate (true/false value) for the following syntax:

  ```mccmd
  randomchance <numerator> <denominator>
  ```

- We also want this predicate to come _after_ `execute if`:

  \begin{gather}
  \texttt{execute}\\\\
  \downarrow\\\\
  \texttt{if}\\\\
  \downarrow\\\\
  \texttt{randomchance <numerator}\texttt{> <denominator}\texttt{>}
  \end{gather}
  
- After entering our predicate, we want to route back to `execute` (because the argument after `execute` is `run`, which is used in our example command above):

  \begin{gather}
  \texttt{execute}\\\\
  \downarrow\\\\
  \texttt{if}\\\\
  \downarrow\\\\
  \texttt{randomchance <numerator}\texttt{> <denominator}\texttt{>}\\\\
  \downarrow\\\\
  \texttt{execute}
  \end{gather}

-----

#### Writing the code

Now that we've established what we want, we can finally begin writing the code! First we want to create a literal `randomchance`. It's a literal because literal values don't change (similar to say `run` or `if` from the `/execute` command). To create a literal, we'll use the `fromLiteralArgument` method described above, and then build it using the `.build()` method:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:brigadier1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:brigadier1}}
```

</div>

With that completed, we can now create our "argument" to this predicate. To do this, we'll use the regular declaration of arguments that we would normally use for commands. In this example, because we're computing \\(\frac{numerator}{denominator}\\), we want our numerator to be 0 or greater and our denominator to be 1 or greater (we don't want any negative numbers or division by zero!):

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:brigadier2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:brigadier2}}
```

</div>

Now we're going to get into the very nitty-gritty part - the predicate declaration. First, we'll create some variables `numerator` and `denominator` to represent the brigadier instances of these arguments. This can be handled by using the `Brigadier.argBuildOf` function:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:brigadier3}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:brigadier3}}
```

</div>

Now we'll define our predicate. Since this is sort of a "meta-command" (it directly affects the outcome of the `run` command), we need to use the `ArgumentBuilder`'s `fork` method. Remember that after we run this predicate, we want to link back to `execute` again, so our first argument is the `CommandNode` for `execute`, which we can get using `Brigadier.getRootNode().getChild("execute")`. Then, we can simply use `Brigadier.fromPredicate` to finish our declaration:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:brigadier4}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:brigadier4}}
```

</div>

Finally, we can now link everything up. We know that `numerator` comes first, **then** `denominator`, so we have to have `numerator.then(denominator)`. We also know that these arguments are the **children** of the `randomChance` literal, so we use the following code to state all of this:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:brigadier5}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:brigadier5}}
```

</div>

Finally, we "register" the command. In this case, we're actually just adding the `randomChance` node under \\(\texttt{execute}\rightarrow\texttt{if}\\), which we can add using the following code:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:brigadier6}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:brigadier6}}
```

</div>

-----

#### Code summary

So, hopefully that wasn't too confusing! If you're still lost, here's the whole code that we wrote:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:brigadier7}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:brigadier7}}
```

</div>

</div>
