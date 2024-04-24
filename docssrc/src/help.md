# Help

Help topics can be added to your command using the `withHelp()`, `withShortDescription()`, `withFullDescription()` or `withUsage()` methods when registering a command. Help allows users to understand what your command does and provides them with a list of usage forms to aid in writing a command.

## Parts of a help

A help topic consists of two mains parts:

- A short description which is displayed in a help list and displayed at the top of a help topic for that command
- A full description which is displayed in the "Description" section of a help topic

This can be seen with the following example, for a command `/mycmd`. This example has the short description _"Says hi"_, and a full description _"Broadcasts hi to everyone on the server"_. The short help is shown in the help list, which (in this example) is viewed using `/help 5`. The full description is shown for the help for the command on its own, which is viewed using `/help mycmd`:

![help image](./images/help.png)

## Help methods and semantics

The CommandAPI has three methods to register parts of a help. The `withShortDescription()` sets the short description for the command, the `withFullDescription()` sets the full description for the command and `withHelp()` is a simple way to set both the short and full description at the same time. The `withHelp()` method is the recommended method to use to set the help for a command.

If no short description is provided, the CommandAPI will attempt to use the full description if one is present. Note that this may be truncated automatically, so it is recommended to provide your own short description.

These are the following methods that the CommandAPI provides to set the help topic for a command:

```java
CommandAPICommand withShortDescription(String description);
```

The `withShortDescription` method simply sets the short description for the command. In the above screenshot, the short description is "Says hi".

```java
CommandAPICommand withFullDescription(String description);
```

The `withFullDescription` method sets the full description for the command. In the above screenshot, the full description is "Broadcasts hi to everyone on the server".

```java
CommandAPICommand withHelp(String shortDescription, String fullDescription);
```

The `withHelp` method sets both the short description and the full description at the same time.

<div class="example">

### Example - Adding help to a command

In this simple example, we implement the above screenshot's help topic. We register a command `/mycmd` and use the `withShortDescription` and `withFullDescription` methods to create a help topic:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:help1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:help1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:help1}}
```

</div>

We could also register this command using the `withHelp` method instead:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:help2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:help2}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:help2}}
```

</div>

</div>

## Advanced help topics

For more control over help topics, the CommandAPI offers the following method, which allows you to provide your own `HelpTopic` object:

```java
CommandAPICommand withHelp(HelpTopic helpTopic);
```

<div class="example">

### Example - Adding locale-specific help

In this example, we implement locale-specific help so players can see help in their desired language. To do this, we must make use of the Bukkit `HelpTopic` object which gives us more control over the content of help that is displayed to a player:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:help3}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:help3}}
```

</div>

We then add our new `HelpTopic` to the command using the `withHelp` method:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:help4}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:help4}}
```

</div>

</div>

## Command usage

When registering a command, there also is a command usage generated. The CommandAPI provides a way to customise this usage by providing the `withUsage()` method:

```java
CommandAPICommand withUsage(String... usage)
```

<div class="example">

### Example - Providing a command usage

In this example, we want to showcase how usage generation displays the usage vs. how a custom usage displays the usage:

```mccmd
/command <help> <admin|user|moderator|vip>
/command <reload> <commandsystem|config|server>
```

This is how it would get displayed:

```yaml
Usage:
- /command <help> <admin>
- /command <help> <user>
- /command <help> <moderator>
- /command <help> <vip>
- /command <reload> <commandsystem>
- /command <reload> <config>
- /command <reload> <server>
```

Now, we are implementing the `withUsage()` method:

```java
new CommandAPICommand("...")
    .withUsage(
        "/command <help> <section>",
        "/command <reload> <system>"
    )
```

By using `withUsage()` like that, the CommandAPI will produce this usage:

```yaml
Usage:
- /command <help> <section>
- /command <reload> <system>
```

</div>
