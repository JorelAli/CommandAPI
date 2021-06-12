# Argument suggestions

Sometimes, you want to replace the list of suggestions that are provided by an argument. To handle this, CommandAPI arguments have two methods:

```java
Argument replaceSuggestions(Function<SuggestionInfo, String[]> suggestions);
Argument includeSuggestions(Function<SuggestionInfo, String[]> suggestions);
```

The `replaceSuggestions` method replaces all suggestions with the provided list of suggestions, whereas the `includeSuggestions` method will include the provided suggestions with the suggestions already present by the argument.

-----

## The `SuggestionsInfo` record

The two methods above require a function which takes in `SuggestionInfo` and returns a `String[]` which are the suggestions to provide. The `SuggestionInfo` class is a record which contains the following methods:

```java
public record SuggestionInfo {
    CommandSender sender();
    Object[] previousArgs();
    String currentInput();
    String currentArg();
}
```

These methods can be used to aid with providing context-aware suggestions for users. The fields are as follows:

```java
CommandSender sender();
```

`sender()` represents the command sender which is typing this command and requesting these suggestions. This is normally a `Player`, but can also be a console command sender if using a Paper server.

```java
Object[] previousArgs();
```

`previousArgs()` represents a list of previously declared arguments, which are parsed and interpreted as if they were being used to execute the command. See [this example below](./argumentsuggestions.md#suggestions-depending-on-previous-arguments) for an idea of how to utilize this field.

```java
String currentInput();
```

`currentInput()` represents the current input that the command sender has entered. This is effectively everything that they have typed, including the leading `/` symbol which is required to start a command. If a user is typing `/mycommand hellowor¦`, the result of `currentInput()` would be `"/mycommand hellowor"`.

```java
String currentArg();
```

`currentArg()` represents the current text which the command sender has entered for the argument which you're trying to provide suggestions for. If a user is typing `/mycommand hellowor¦`, the result of `currentArg()` would be `"hellowor"`.

-----

## Suggestions with a String Array

The first method, `replaceSuggestions(Function<SuggestionInfo, String[]>)`, allows you to *replace* the suggestions normally associated with that argument with an array of strings.

<div class="example">

### Example - Teleport to worlds by replacing suggestions

Say we're creating a plugin with the ability to teleport to different warps on the server. If we were to retrieve a list of warps, we would be able to replace the suggestions of a typical `StringArgument` to teleport to that warp. Let's create a command with the following syntax:

```mccmd
/warp <warp>
```

We then implement our warp teleporting command using `replaceSuggestions()` on the `StringArgument` to provide a list of warps to teleport to:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:ArgumentSuggestions1}}
```

</div>

The second method, `includeSuggestions(Function<SuggestionInfo, String[]>)`, allows you to _include_ additional suggestions in combination with the list of existing suggestions for a command.

-----

## Suggestions depending on a command sender

The `replaceSuggestions(Function<SuggestionInfo, String[]>)` method allows you to replace the suggestions normally associated with that argument with an array of strings that are evaluated dynamically using information about the command sender, using the `sender()` method.

<div class="example">


### Example - Friend list by replacing suggestions

Say you have a plugin which has a "friend list" for players. If you want to teleport to a friend in that list, you could use a `PlayerArgument`, which has the list of suggestions replaced with the list of friends that that player has. Since the list of friends *depends on the sender*, we can use the function to determine what our suggestions should be. Let's use the following command to teleport to a friend from our friend list:

```mccmd
/friendtp <friend>
```

Let's say we have a simple class to get the friends of a command sender:

```java
public {{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:ArgumentSuggestions2_1}}
```

We can then use this to generate our suggested list of friends:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:ArgumentSuggestions2_2}}
```

</div>

-----

## Suggestions depending on previous arguments

The `replaceSuggestions(Function<SuggestionInfo, String[]>)` method also has the capability to suggest arguments based on the values of previously inputted arguments, using the `previousArgs()` method. This `previousArgs()` method returns a **list of previous arguments** which are parsed exactly like any regular `CommandAPI` command argument.

<div class="warning">

**Note:**

The ability to use previously declared arguments _does not work via redirects_. This means that any command that comes before it that leads into a command that uses suggestions depending on previous arguments will not work. For example, if we had a command `/mycommand <arg1> <arg2> <arg3>` and ran it as normal, it would work as normal:

```mccmd
/mycommand arg1 arg2 arg3
```

However, if we redirect execution via the `/execute` command to have the following:

```mccmd
/execute run mycommand <suggestions>
```

This won't work, because we make use of a redirect:

\\[\texttt{/execute run} \xrightarrow{redirect} \texttt{mycommand arg1 arg2 arg3}\\]

To clarify, by "does not work", I mean that it is not possible to access the `Object[]` of previously declared arguments. **If a command occurs via a redirect, the `Object[]` of previously declared arguments will be null**.

</div>




<div class="example">

### Example - Sending a message to a nearby player

Say we wanted to create a command that lets you send a message to a specific player in a given radius. _(This is a bit of a contrived example, but let's roll with it)_. To do this, we'll use the following command syntax:

```mccmd
/localmsg <radius> <target> <message>
```

When run, this command will send a message to a target player within the provided radius. To help identify which players are within a radius, we can replace the suggestions on the `<target>` argument to include a list of players within the provided radius. We do this with the following code:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:ArgumentSuggestionsPrevious}}
```

As shown in this code, we use the `previousArgs()` method access the previously declared arguments. In this example, `info.previousArgs()` will be `{ int }`, where this `int` refers to the radius. Note how this object array only has the previously declared arguments (and not for example `{ int, Player, String }`).

</div>

