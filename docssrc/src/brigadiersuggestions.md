# Brigadier Suggestions

As described in [The ArgumentSuggestions interface](./argumentsuggestions.md#the-argumentsuggestions-interface), the `ArgumentSuggestions` interface has the following default method:

```java
{{#include ../../commandapi-core/src/main/java/dev/jorel/commandapi/arguments/ArgumentSuggestions.java:Declaration}}

}
```

This allows you to use Brigadier's `SuggestionsBuilder` and `Suggestions` classes to create more powerful suggestions beyond the basic capabilities of the CommandAPI.

In order to use this, you will need the Brigadier dependency, which you can find under the [Brigadier installation instructions](https://github.com/Mojang/brigadier#installation).

<div class="example">

### Example - Making an emoji broadcasting message

Say we want to let users broadcast a message, but also allow them to enter emojis into the message they're typing:

![A gif showcasing a command where emojis are suggested when typing a message](./images/emojimsg.gif)

For this command, we'll use a `GreedyStringArgument` as if we were making a generic broadcasted message. We create a map of emojis to their descriptions to use as tooltips and then we use Brigadier to display the suggestions at the end of the message where the cursor is.

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:brigadierSuggestions1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:brigadierSuggestions1}}
```

</div>

In this example, we simply create the `GreedyStringArgument` and use `replaceSuggestions()` to specify our suggestion rules. We create an offset using the current builder to make suggestions start at the last character (the current builder start `builder.getStart()` and the current length of what the user has already typed `info.currentArg().length()`). Finally, we build the suggestions with `builder.buildFuture()` and then register our command as normal.

</div>

<div class="example">

### Example - Using a Minecraft command as an argument

> **Developer's Note:**
>
> This example has been superseded by the [Command argument](./argument_command.md). This example is still present as it gives an example of much more complicated brigadier suggestions which may be useful for readers!

Courtesy of [469512345](https://github.com/469512345), the following example shows how using Brigadier's suggestions and parser can be combined with the CommandAPI to create an argument which suggests valid Minecraft commands. This could be used for example as a `sudo` command, to run a command as another player.

![A gif showcasing a command suggestion for the /give command](./images/commandargument.gif)

For this command, we'll use a `GreedyStringArgument` because that allows users to enter any combination of characters (which therefore, allows users to enter any command). First, we start by defining the suggestions that we'll use for the `GreedyStringArgument`. We'll use the `ArgumentSuggestions` functional interface described above:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:brigadierSuggestions2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:brigadierSuggestions2}}
```

</div>

There's a lot to unpack there, but it's generally split up into 4 key sections:

- **Finding the start of the argument**. We find the start of the argument so we know where the beginning of our command suggestion is. This is done easily using `builder.getStart()`, but we also have to take into account any spaces if our command argument contains spaces.

- **Parsing the command argument**. We make use of Brigadier's `parse()` method to parse the argument and generate some `ParseResults`.

- **Reporting parsing errors**. This is actually an optional step, but in general it's good practice to handle exceptions stored in `ParseResults`. While [Brigadier doesn't actually handle suggestion exceptions](https://github.com/Mojang/brigadier/blob/master/src/main/java/com/mojang/brigadier/CommandDispatcher.java#L599), this has been included in this example to showcase exception handling.

- **Generating suggestions from parse results**. We use our parse results with Brigadier's `getCompletionSuggestions()` method to generate some suggestions based on the parse results and the suggestion string range.

Now that we've declared our arguments suggestions, we can then create our simple command with the following syntax:

```mccmd
/commandargument <command>
```

We use the command suggestions declared above by using the `replaceSuggestions` method in our `GreedyStringArgument`, and write a simple executor which runs the command that the user provided:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:brigadierSuggestions3}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:brigadierSuggestions3}}
```

</div>

</div>
