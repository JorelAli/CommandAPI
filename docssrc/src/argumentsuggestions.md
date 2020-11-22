# Argument suggestions

Sometimes, you want to override the list of suggestions that are provided by an argument. To handle this, CommandAPI arguments contain three methods to override suggestions:

```java
Argument overrideSuggestions(String... suggestions);
Argument overrideSuggestions(Function<CommandSender, String[]> suggestions);
Argument overrideSuggestions(BiFunction<CommandSender, Object[], String[]> suggestions);
```

-----

## Argument suggestion deferral

Before we go into detail about what the above methods do, we must first describe _suggestion deferral_. When the server loads, arguments that are provided with a String array are fixed, and do not change. However, arguments that are provided using the function and bifunction parameters are evaluated when they are needed - their execution is deferred until requested by the user. As such, if you wanted to retrieve something that isn't available during server load but is available during normal server running, it is recommended to use either of those functions instead of the String array.

For example, instead of doing the following, which retrieves a list of worlds on the server:

```java
overrideSuggestions(Bukkit.getWorlds().stream().map(World::getName).toArray(String[]::new))
```

You should defer it using the function parameter:

```java
overrideSuggestions(sender -> Bukkit.getWorlds().stream().map(World::getName).toArray(String[]::new))
```



-----

## Suggestions with a String Array

The first method, `overrideSuggestions(String... suggestions)`, allows you to *replace* the suggestions normally associated with that argument with an array of strings. As described above, this doesn't use suggestion deferral, _so this list will not update automatically_.

<div class="example">

### Example - Teleport to worlds by overriding suggestions

Say we're creating a plugin with the ability to teleport to different warps on the server. If we were to retrieve a list of warps, we would be able to override the suggestions of a typical `StringArgument` to teleport to that warp. Let's create a command with the following structure:

```
/warp <warp>
```

We then implement our warp teleporting command using `overrideSuggestions()` on the `StringArgument` to provide a list of warps to teleport to:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:ArgumentSuggestions1}}
```

</div>

-----

## Suggestions depending on a command sender

The `overrideSuggestions(Function<CommandSender, String[]> suggestions)` method allows you to replace the suggestions normally associated with that argument with an array of strings that are evaluated dynamically using information about the command sender.

<div class="example">


### Example - Friend list by overriding suggestions

Say you have a plugin which has a "friend list" for players. If you want to teleport to a friend in that list, you could use a `PlayerArgument`, which has the list of suggestions overridden with the list of friends that that player has. Since the list of friends *depends on the sender*, we can use the function to determine what our suggestions should be. Let's use the following command to teleport to a friend from our friend list:

```
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

> **Developer's Note:**
>
> The syntax of inlining the `.overrideSuggestions()` method has been designed to work well with Java's lambdas. For example, we could write the above code more consisely, such as:
>
> ```java
> List<Argument> arguments = new ArrayList<>();
> arguments.add(new PlayerArgument("friend").overrideSuggestions(Friends::getFriends));
> ```
>
> 

</div>

-----

## Suggestions depending on previous arguments

The `overrideSuggestions(BiFunction<CommandSender, Object[], String[]> suggestions)` method is the most powerful suggestion overriding function that the CommandAPI offers. It has the capability to suggest arguments based on the values of previously inputted arguments.

This method requires a function that takes in a command sender and the **list of previous arguments** and must return a `String[]` of suggestions. The arguments are parsed exactly like any regular `CommandAPI` command argument.

<div class="warning">

**Note:**

The ability to use previously declared arguments _does not work via redirects_. This means that any command that comes before it that leads into a command that uses suggestions depending on previous arguments will not work. For example, if we had a command `/mycommand <arg1> <arg2> <arg3>` and ran it as normal, it would work as normal:

```
/mycommand arg1 arg2 arg3
```

However, if we redirect execution via the `/execute` command to have the following:

```
/execute run mycommand <suggestions>
```

This won't work, because we make use of a redirect:

\\[\texttt{/execute run} \xrightarrow{redirect} \texttt{mycommand arg1 arg2 arg3}\\]

To clarify, by "does not work", I mean that it is not possible to access the `Object[]` of previously declared arguments. **If a command occurs via a redirect, the `Object[]` of previously declared arguments will be null**.

</div>




<div class="example">

### Example - Sending a message to a nearby player

Say we wanted to create a command that lets you send a message to a specific player in a given radius. _(This is a bit of a contrived example, but let's roll with it)_. To do this, we'll use the following command structure:

```
/localmsg <radius> <target> <message>
```

When run, this command will send a message to a target player within the provided radius. To help identify which players are within a radius, we can override the suggestions on the `<target>` argument to include a list of players within the provided radius. We do this with the following code:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:ArgumentSuggestionsPrevious}}
```

As shown in this code, we use the `(sender, args) -> ...` lambda to override suggestions with previously declared arguments. In this example, our variable `args` will be `{ int }`, where this `int` refers to the radius. Note how this object array only has the previously declared arguments (and not for example `{ int, Player, String }`).

</div>

