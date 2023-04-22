# Handling command failures

Sometimes, you want your command to fail on purpose. This is the way to "gracefully" handle errors in your command execution. This is performed by throwing any of the following methods:

```java
throw CommandAPI.failWithString(String message);
throw CommandAPI.failWithMessage(Message message);
throw CommandAPI.failWithBaseComponents(BaseComponent... message);
throw CommandAPI.failWithAdventureComponent(Component message);
```

When the CommandAPI handles the fail method, it will cause the command to return a _success value_ of 0, to indicate failure.

<div class="example">

### Example - Command failing for element not in a list

Say we have some list containing fruit and the player can choose from it. In order to do that, we can use a `StringArgument` and suggest it to the player using `.replaceSuggestions(info -> String[])`. However, because this only lists _suggestions_ to the player, it does **not** stop the player from entering an option that isn't on the list of suggestions.

Therefore, to gracefully handle this with a proper error message, we use one of the `CommandAPI.failWithXXX()` methods above with a meaningful error message which is displayed to the user.

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:commandFailures1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:commandFailures1}}
```

</div>

</div>

> **Developer's Note:**
>
> In general, it's a good idea to handle unexpected cases with one of the `CommandAPI.failWithXXX()` methods. Most arguments used by the CommandAPI will have their own built-in failsafe system _(e.g. the `EntitySelectorArgument` will not execute the command executor if it fails to find an entity)_, so this feature is for those extra cases.
