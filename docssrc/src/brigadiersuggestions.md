# Brigadier Suggestions

As described in [The ArgumentSuggestions interface](./argumentsuggestions.md#the-argumentsuggestions-interface), the `ArgumentSuggestions` interface has the following default method:

```java
{{#include ../../CommandAPI/commandapi-core/src/main/java/dev/jorel/commandapi/arguments/ArgumentSuggestions.java:Declaration}}

}
```

This allows you to use Brigadier's `SuggestionsBuilder` and `Suggestions` classes to create more powerful suggestions beyond the basic capabilities of the CommandAPI.

In order to use this, you will need the Brigadier dependency, which you can find under the [Brigadier installation instructions](https://github.com/Mojang/brigadier#installation).

<div class="example">

### Example - Using a Minecraft command as an argument

Courtesy of [469512345](https://github.com/469512345), the following example shows how using Brigadier's suggestions and parser can be combined with the CommandAPI to create an argument which suggests valid Minecraft commands. This could be used for example as a `sudo` command, to run a command as another player.

For this command, we'll use a `GreedyStringArgument` because that allows users to enter any combination of characters (which therefore, allows users to enter any command). First, we start by defining the suggestions that we'll use for the `GreedyStringArgument`. We'll use the `ArgumentSuggestions` functional interface described above:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:BrigadierSuggestions1}}
```

There's a lot to unpack there, but it's generally split up into 4 key sections:

- **Finding the start of the argument**. We find the start of the argument so we know where the beginning of our command suggestion is. This is done easily using `builder.getStart()`, but we also have to take into account any spaces if our command argument contains spaces.

- **Parsing the command argument**. We make use of Brigadier's `parse()` method to parse the argument and generate some `ParseResults`.

- **Reporting parsing errors**. This is actually an optional step, but in general it's good practice to handle exceptions stored in `ParseResults`. While [Brigadier doesn't actually handle suggestion exceptions](https://github.com/Mojang/brigadier/blob/master/src/main/java/com/mojang/brigadier/CommandDispatcher.java#L599), this has been included in this example to showcase exception handling.

- **Generating suggestions from parse results**. We use our parse results with Brigadier's `getCompletionSuggestions()` method to generate some suggestions based on the parse results and the suggestion string range.

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:BrigadierSuggestions2}}
```

</div>