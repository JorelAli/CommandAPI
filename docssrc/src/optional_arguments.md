# Optional Arguments

Sometimes, you want to implement a command that has arguments that do not need to be entered. Take a `/sayhi` command for example. You may want to say "Hi", or to another player. For that, we want this command syntax:

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

Previously, we've looked at how to handle null values. To make all of this easier, the CommandAPI implements multiple `getOrDefault()` methods for `CommandArguments`:

```java
Object getOrDefault(int index, Object defaultValue)
Object getOrDefault(String nodeName, Object defaultValue)
Object getOrDefault(int index, Supplier<?> defaultValue)
Object getOrDefault(String nodeName, Supplier<?> defaultValue)
```

<div class="example">

### Example - /sayhi command while using the getOrDefault method

Let's register the `/sayhi` command from above a second time - this time using a `getOrDefault` method. We are using the exact same command syntax:

```mccmd
/sayhi          - Says "Hi!" to yourself
/sayhi <target> - Says "Hi!" to a target player
```

This is how the `getOrDefault` method is being implemented:

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
