# Argument suggestions

Sometimes, you want to modify the list of suggestions that are provided by an argument. To handle this, CommandAPI arguments have two methods:

```java
Argument replaceSuggestions(ArgumentSuggestions suggestions);
Argument includeSuggestions(ArgumentSuggestions suggestions);
```

The `replaceSuggestions` method replaces all suggestions with the provided list of suggestions, whereas the `includeSuggestions` method will include the provided suggestions with the suggestions already present by the argument.

Because argument suggestions are arguably the most powerful feature that the CommandAPI offers, I've split this section into a number of subsections. To give an overview on what CommandAPI argument suggestions can do:

- Provide a list of suggestions
- Provide a list of suggestions depending on the command sender
- Provide a list of suggestions based on what the player has already typed in the command
- Provide a list of suggestions asynchronously
- Provide a list of suggestions with tooltips to guide users on what certain suggestions do
- Append suggestions to an existing list of suggestions

-----

## The `ArgumentSuggestions` interface

The two methods above require an `ArgumentSuggestions` object, which is a functional interface that takes in a `SuggestionInfo` record and the current Brigadier `SuggestionsBuilder` and returns a `CompletableFuture<Suggestions>` object. This may sound a bit complicated, but this allows you to implement very powerful suggestions using a combination of the CommandAPI and raw Brigadier API methods. More information about using Brigadier-level suggestions can be found in the [brigadier suggestions](./brigadiersuggestions.md) section.

To simplify this, the CommandAPI provides a number of methods to generate suggestions:

```java
ArgumentSuggestions strings(String... suggestions);
ArgumentSuggestions strings(Collection<String>);
ArgumentSuggestions strings(Function<SuggestionInfo, String[]> suggestions);
ArgumentSuggestions stringCollection(Function<SuggestionInfo<CommandSender>, Collection<String>>);
ArgumentSuggestions stringsAsync(Function<SuggestionInfo, CompletableFuture<String[]>> suggestions);
ArgumentSuggestions stringCollectionAsync(Function<SuggestionInfo<CommandSender>, CompletableFuture<Collection<String>>>);

ArgumentSuggestions stringsWithTooltips(IStringTooltip... suggestions);
ArgumentSuggestions stringsWithTooltips(Collection<IStringTooltip>);
ArgumentSuggestions stringsWithTooltips(Function<SuggestionInfo, IStringTooltip[]> suggestions);
ArgumentSuggestions stringsWithTooltipsCollection(Function<SuggestionInfo<CommandSender>, Collection<IStringTooltip>>);
ArgumentSuggestions stringsWithTooltipsAsync(Function<SuggestionInfo, CompletableFuture<IStringTooltip[]>> suggestions);
ArgumentSuggestions stringsWithTooltipsCollectionAsync(Function<SuggestionInfo<CommandSender>, CompletableFuture<Collection<IStringTooltip>>>);
```
