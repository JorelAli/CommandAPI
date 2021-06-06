# The SimpleFunctionWrapper class

To represent Minecraft functions and tags, the CommandAPI uses the `SimpleFunctionWrapper` class. Simply put, this class represents _one_ Minecraft function, which are defined in `.mcfunction` files.

> **Developer's Note**
>
> The `SimpleFunctionWrapper` class represents a Minecraft function. In order to represent a Minecraft "tag", which is a collection of Minecraft functions, the CommandAPI simply uses a `SimpleFunctionWrapper[]`.

## SimpleFunctionWrapper methods

The `SimpleFunctionWrapper` class has the following methods:

```java
class SimpleFunctionWrapper implements Keyed {
    
    // Methods that creates SimpleFunctionWrapper instances
    static SimpleFunctionWrapper getFunction(NamespacedKey key);
    static SimpleFunctionWrapper[] getTag(NamespacedKey key);

    // Methods that query the Minecraft server
    static Set<NamespacedKey> getFunctions();
    static Set<NamespacedKey> getTags();
    
    // Methods for using the SimpleFunctionWrapper
    int run(CommandSender sender);
    
    // Utility functions
    String[] getCommands();
    NamespacedKey getKey();
}
```

### getTag(NamespacedKey) and getFunction(NamespacedKey)

The `getFunction(NamespacedKey)` function is used to get a function that has been declared in a datapack and is loaded on the server.

The `getTag(NamespacedKey)` function is used to get a Tag that has been declared in a datapack and is loaded on the server. This returns a `SimpleFunctionWrapper[]`, since a tag is simply an ordered collection of functions. When using this method, the `#` symbol which is typically used at the start of the tag's name _is not needed_.

### getFunctions() and getTags()

The methods `getFunctions()` and `getTags()` simply return a set of `NamespacedKey` objects which are the names of functions or tags that have been declared by all datapacks on the server.

### run(CommandSender)

This method simply runs the current `SimpleFunctionWrapper` as the provided command sender. The method will return a numerical result value, stating whether it succeeds or returns a result. This is documented in more detail [here](./normalexecutors.md) and [here](./resultingcommandexecutors.md). For example:

### getCommands()

The `getCommands()` method returns a `String[]` that contains the list of commands that the Minecraft function "holds". In other words, running this Minecraft function is basically as simple as iterating through its commands and running them in order. The commands that this `String[]` holds are the raw strings that this function represents - in other words, it can include things such as `@p` and `~ ~ ~` instead of "filled in" values.