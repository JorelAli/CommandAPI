# Optional Arguments

Sometimes, you want to implement a command that has arguments that do not need to be entered. Take a `/sayhi` command for example. You may want to say "Hi" to yourself, or to another player. For that, we want this command syntax:

```mccmd
/sayhi          - Says "Hi!" to yourself
/sayhi <target> - Says "Hi!" to a target player
```

To implement these commands, the CommandAPI provides two methods to help you with that:

```java
Argument withOptionalArguments(List<Argument<?>> args);
Argument withOptionalArguments(Argument<?>... args);
```

<div class="example">

### Example - /sayhi command with an optional argument

For example, say we're registering a command `/sayhi`:

```mccmd
/sayhi          - Says "Hi!" to yourself
/sayhi <target> - Says "Hi!" to a target player
```

For that, we are going to register a command `/sayhi`. To add optional arguments, we are going to use the `withOptionalArguments(Argument... args)` method:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:optionalArguments1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:optionalArguments1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:optionalArguments1}}
```

</div>

This gives us the ability to run both `/sayhi` and `/sayhi <target>` with the same command name "sayhi", but have different results based on the arguments used.

You can notice two things:

- We use the `withOptionalArguments` method to add an optional argument to a command
- We use `args.get("target")` to get our player out of the arguments

With optional arguments, there is a possibility of them being not present in the arguments of a command. The reason we use `args.get("target")` is that this will just return `null` and you can handle what should happen.

</div>

## Setting existing arguments as optional arguments

In order to set arguments as optional the CommandAPI has the method `setOptional(boolean)`:

```java
Argument setOptional(boolean optional);
```

When using the `withOptionalArguments` method it might be interesting to know that it calls the `setOptional()` method internally. This means that the following two examples are identical:

```java
new CommandAPICommand("optional")
    .withOptionalArguments(new PlayerArgument("target"))
```

```java
new CommandAPICommand("optional")
    .withArguments(new PlayerArgument("target").setOptional(true))
```

However, calling `withOptionalArguments` is safer because it makes sure that the argument is optional because of that internal call.

## Avoiding null values

Previously, we've looked at how to handle null values. To make all of this easier, the CommandAPI implements multiple additional methods for [`CommandArguments`](./commandarguments.md):

```java
Object getOrDefault(int index, Object defaultValue);
Object getOrDefault(int index, Supplier<?> defaultValue);
Object getOrDefault(String nodeName, Object defaultValue);
Object getOrDefault(String nodeName, Supplier<?> defaultValue);
Optional<Object> getOptional(int index)
Optional<Object> getOptional(String nodeName)
```

The examples will be using the `getOptional` methods but there is no downside of using the `getOrDefault` methods.

<div class="example">

### Example - /sayhi command while using the getOptional method

Let's register the `/sayhi` command from above a second time - this time using a `getOptional` method. We are using the exact same command syntax:

```mccmd
/sayhi          - Says "Hi!" to yourself
/sayhi <target> - Says "Hi!" to a target player
```

This is how the `getOptional` method is being implemented:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:optionalArguments2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:optionalArguments2}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:optionalArguments2}}
```

</div>

</div>

## Implementing required arguments after optional arguments

We've now talked about how to implement optional arguments and how to avoid null values returned by optional arguments when they aren't provided when executing the command.

Now we also want to talk about how to implement required arguments after optional arguments. For this, the CommandAPI implements a `combineWith` method for arguments:

```java
AbstractArgument combineWith(Argument<?>... combinedArguments);
```

You will need to use this method if you want to have a required argument after an optional argument. In general, this is which pattern the CommandAPI follows while dealing with optional arguments:

1. You have a `CommandAPICommand` and you add arguments to it.
2. After your required arguments, you can provide optional arguments.

At this point your command is basically done. Any attempt to add a required argument will result in an `OptionalArgumentException`. However, this is where the `combineWith` method comes in.
This method allows you to combine arguments. Let's say you have an optional `StringArgument` (here simplified to `A`) and you want a required `PlayerArgument` (here simplified to `B`).
Argument `B` should only be required if argument `A` is given. To implement that logic, we are going to use the `combineWith` method so that we have this syntax:

```java
A.combineWith(B)
```

This does two things:

1. When checking optional argument constraints the argument `B` will be ignored so the `OptionalArgumentException` will not be thrown
2. It allows you to define additional optional arguments afterwards which can only be entered if argument `B` has been entered

This is how you would add another optional `PlayerArgument` (here simplified to `C`):

```java
new CommandAPICommand("mycommand")
    .withOptionalArguments(A.combineWith(B))
    .withOptionalArguments(C)
```

Let's say you declare your arguments like this:

```java
new CommandAPICommand("mycommand")
    .withOptionalArguments(A.combineWith(B))
    .withArguments(C)
```

This would result in an `OptionalArgumentException` because you are declaring a required argument after an optional argument without creating that exception for argument `C` like you do for argument `B`.

<div class="example">

### Example - Required arguments after optional arguments

We want to register a command `/rate` with the following syntax:

```mccmd
/rate                           - Sends an information message
/rate <topic> <rating>          - Rates a topic with a rating and sends a message to the command sender
/rate <topic> <rating> <target> - Rates a topic with a rating and sends a message to the target
```

To implement that structure we make use of the `combineWith` method to make the argument after the optional argument \<topic> required:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:optionalArguments3}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:optionalArguments3}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:optionalArguments3}}
```

</div>

</div>
