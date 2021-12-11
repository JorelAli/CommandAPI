# Upgrading guide

## From version 6.5.2 to 7.0.0

### Suggestions

Suggestions have been overhauled and no longer take in a `Function<SuggestionsInfo, String[]>` anymore. Instead, they now take in a `ArgumentSuggestions` object which represents argument suggestions (and whether they are executed asynchronously or have tooltips).

#### Normal (string) suggestions

These normal suggestions methods have been replaced with an `ArgumentSuggestions` parameter instead of a function:

```java
Argument replaceSuggestions(Function<SuggestionInfo, String[]> suggestions);
Argument includeSuggestions(Function<SuggestionInfo, String[]> suggestions);
```

\\[\downarrow\\]

```java
Argument replaceSuggestions(ArgumentSuggestions suggestions);
Argument includeSuggestions(ArgumentSuggestions suggestions);
```

The same functionality can be reproduced by wrapping your existing functions in `ArgumentSuggestions.strings`:

```java
List<Argument> arguments = new ArrayList<>();
arguments.add(new StringArgument("world").replaceSuggestions(info -> 
    new String[] {"northland", "eastland", "southland", "westland" }
));
```

\\[\downarrow\\]

```java
List<Argument> arguments = new ArrayList<>();
arguments.add(new StringArgument("world").replaceSuggestions(ArgumentSuggestions.strings(info -> 
    new String[] {"northland", "eastland", "southland", "westland" }
)));
```

#### Normal (strings with tooltips) suggestions

The `...T()` methods have been replaced with the normal methods above, and can use the `ArgumentSuggestions.stringsWithTooltips` method:

```java
Argument replaceSuggestionsT(Function<SuggestionInfo, IStringTooltip[]> suggestions);
Argument includeSuggestionsT(Function<SuggestionInfo, IStringTooltip[]> suggestions);
```

\\[\downarrow\\]

```java
Argument replaceSuggestions(ArgumentSuggestions suggestions);
Argument includeSuggestions(ArgumentSuggestions suggestions);
```

For example:

```java
List<Argument> arguments = new ArrayList<>();
arguments.add(new StringArgument("emote")
    .replaceSuggestionsT( info -> new IStringTooltip[] {
            StringTooltip.of("wave", "Waves at a player"),
            StringTooltip.of("hug", "Gives a player a hug"),
            StringTooltip.of("glare", "Gives a player the death glare")
        }
    )
);
```

\\[\downarrow\\]

```java
List<Argument> arguments = new ArrayList<>();
arguments.add(new StringArgument("emote")
    .replaceSuggestions(ArgumentSuggestions.stringsWithTooltips(info -> new IStringTooltip[] {
            StringTooltip.of("wave", "Waves at a player"),
            StringTooltip.of("hug", "Gives a player a hug"),
            StringTooltip.of("glare", "Gives a player the death glare")
        }
    ))
);
```

#### Safe suggestions

Similar to above with normal suggestions, safe suggestions have been replaced with `replaceSafeSuggestions` and `includeSafeSuggestions` respectively:

```java
Argument replaceWithSafeSuggestions(Function<SuggestionInfo, S[]> suggestions);
Argument includeWithSafeSuggestions(Function<SuggestionInfo, S[]> suggestions);
```

\\[\downarrow\\]

```java
Argument replaceSafeSuggestions(SafeSuggestions<T> suggestions);
Argument includeSafeSuggestions(SafeSuggestions<T> suggestions);
```

These can be used with the `SafeSuggestions.suggest` and `SafeSuggestions.tooltips` methods to wrap existing functions. For example:

```java
List<Argument> arguments = new ArrayList<>();
arguments.add(new RecipeArgument("recipe").replaceWithSafeSuggestions(info -> 
    new Recipe[] { emeraldSwordRecipe, /* Other recipes here */ }
));
```

\\[\downarrow\\]

```java
List<Argument> arguments = new ArrayList<>();
arguments.add(new RecipeArgument("recipe").replaceSafeSuggestions(SafeSuggestions.suggest(info -> 
    new Recipe[] { emeraldSwordRecipe, /* Other recipes here */ }
)));
```

-----

## From version 6.2.0 or earlier to 6.3.0

Please refer to an older version of the documentation. This has been omitted to save space and reduce confusion in this upgrading section.