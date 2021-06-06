# Argument suggestions with tooltips

![](./images/warps.gif)

The CommandAPI can also display tooltips for specific argument suggestions. These are shown to the user when they hover over a given suggestion and can be used to provide more context to a user about the suggestions that are shown to them. In this section, we'll outline the two ways of creating suggestions with tooltips: 

- Normal (String) suggestions with tooltips
- Safely typed suggestions with tooltips

Tooltips _can_ have formatting to change how the text is displayed by using the `ChatColor` class.

-----

## Tooltips with normal (String) suggestions

To use these features, the CommandAPI includes the `replaceSuggestionsT` methods for arguments, that accept `IStringTooltip` objects instead of `String` objects:

```java
Argument replaceSuggestionsT(Function<SuggestionInfo, IStringTooltip[]> suggestions);
```

The `StringTooltip` class is the CommandAPI's default implementation of `IStringTooltip`, which has two static methods to construct it easily:

```java
StringTooltip none(String suggestion);
StringTooltip of(String suggestion, String tooltip);
```

The first method, `StringTooltip.none(String)` creates a normal suggestion entry with no tooltip, whereas the `StringTooltip.of(String, String)` method creates a suggestion with the provided tooltip text.

<div class="example">

### Example - An emotes command with string suggestion tooltips

Say we want to create a simple command to provide in-game emotes between players. For example, if you did `/emote wave Bob`, you'll "wave" to the player _Bob_. For this example, we'll use the following command syntax:

```mccmd
/emote <emote> <target>
```

First, we'll declare our arguments. Here, we'll use the `replaceSuggestionsT` method, along with the `StringTooltip.of(String, String)` method to create emote suggestions and include suitable descriptions:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:Tooltips1}}
```

Finally, we declare our command as normal:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:Tooltips2}}
```

</div>

The `IStringTooltip` interface can be implemented by any other class to provide tooltips for custom objects. The `IStringTooltip` interface has the following methods:

```java
public interface IStringTooltip {
    public String getSuggestion();
    public String getTooltip();
}
```

This is incredibly useful if you are using suggestions with custom objects, such as a plugin that has custom items.

<div class="example">

### Example - Using `IStringTooltip` for custom items

Let's say we've created a simple plugin which has custom items. For a custom item, we'll have a super simple class `CustomItem` that sets its name, lore and attached itemstack:

```java
public {{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:Tooltips3}}
```

Let's also say that our plugin has registered lots of `CustomItem`s and has this stored in a `CustomItem[]` in our plugin. We could then use this as our input for suggestions:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:Tooltips4}}
```

</div>

-----

## Tooltips with safe suggestions

Using tooltips with safe suggestions is almost identical to the method described above for normal suggestions, except for two things. Firstly, you must use `replaceWithSafeSuggestionsT` method instead of the `replaceSuggestionsT` method and secondly, instead of using `StringTooltip`, you must use `Tooltip<S>`. Let's look at these differences in more detail.

The `replaceWithSafeSuggestionsT` methods are fairly similar to the `replaceSuggestionsT` methods, except instead of using `StringTooltip`, it simply uses `Tooltip<S>`:

```java
Argument replaceWithSafeSuggestionsT(Function<SuggestionInfo, Tooltip<S>[]> suggestions);
```

The `Tooltip<S>` class represents a tooltip for a given object `S`. For example, a tooltip for a `LocationArgument` would be a `Tooltip<Location>` and a tooltip for an `EnchantmentArgument` would be a `Tooltip<Enchantment>`.

Just like the `StringTooltip` class, the `Tooltip<S>` class provides the following static methods, which operate exactly the same as the ones in the `StringTooltip` class:

```java
Tooltip<S> none(S object);
Tooltip<S> of(S object, String tooltip);
Tooltip<S>[] arrayOf(Tooltip<S>... tooltips);
```

The use of `arrayOf` is heavily recommended as it provides the necessary type safety for Java code to ensure that the correct types are being passed to the `replaceWithSafeSuggestionsT` method.

<div class="example">

### Example - Teleportation command with suggestion descriptions

Say we wanted to create a custom teleport command which suggestions a few key locations. In this example, we'll use the following command syntax:

```mccmd
/warp <location>
```

First, we'll declare our arguments. Here, we use a `LocationArgument` and use the `replaceWithSafeSuggestionsT` method, with a parameter for the command sender, so we can get information about the world. We populate the suggestions with tooltips using `Tooltip.of(Location, String)` and collate them together with `Tooltip.arrayOf(Tooltip<Location>...)`:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:SafeTooltips}}
```

In the arguments declaration, we've casted the command sender to a player. To ensure that the command sender is definitely a player, we'll use the `executesPlayer` command execution method in our command declaration:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:SafeTooltips2}}
```

</div>

