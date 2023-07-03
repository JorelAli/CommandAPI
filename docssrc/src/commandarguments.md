# CommandArguments

The `CommandArguments` class was introduced in CommandAPI 9.0.0 and provides a much more powerful way of accessing arguments than just an array of arguments which existed until 9.0.0.

While the argument array just gives the possibility to access the arguments via the array notation (`args[0]`), the `CommandArguments` class offers much more, including:

- [Access the inner structure directly](#access-the-inner-structure-directly)
- [Access arguments](#access-arguments)
- [Access raw arguments](#access-raw-arguments)
- [Access unsafe arguments](#access-unsafe-arguments)

ATTENTION!!!!! Every executable and generated documentation has to be deleted before committing anything!

-----

## Access the inner structure directly

To access the inner structure of the `CommandArguments` class directly, it provides various methods which you can learn about below:

**Get the argument array**

```java
Object[] args();
```

This returns the array of arguments as defined when creating your command.

**Get the arguments mapped to their node name**

```java
Map<String, Object> argsMap();
```

This returns an unmodifiable map which contains the arguments mapped to their node names.

**Get the raw argument array**

```java
String[] rawArgs();
 ```

This returns the array of raw arguments. An explanation of what raw arguments are can be found in the section about [accessing raw arguments](#access-raw-arguments).

**Get the raw arguments mapped to their node name**

```java
Map<String, String> rawArgsMap();
```

This returns an unmodifiable map which contains the raw arguments mapped to their node names. An explanation of what raw arguments are can be found in the section about [accessing raw arguments](#access-raw-arguments).

**Other useful methods**

```java
String getFullInput(); // Returns the full command input (including the / character)
int count();           // Returns the amount of arguments
```

-----

## Access arguments

The `CommandArguments` class provides its arguments in a way similar to how a `List` or `Map` let you access their contents. When using these methods, you need to cast the arguments to their respective type. The `CommandArguments` class also provides a way to [access unsafe arguments](#access-unsafe-arguments).

You can choose to access arguments by their node name or by their index.

### Access arguments by node name

Accessing arguments by their node name is the recommended way of accessing arguments.

There are four methods you can use to access arguments by their node name:

```java
Object get(String nodeName);
Object getOrDefault(String nodeName, Object defaultValue);
Object getOrDefault(String nodeName, Supplier<?> defaultValue);
Optional<Object> getOptional(String nodeName);
```

### Access arguments by index

Accessing arguments by their index is the original way of accessing arguments. However, we recommend to [access arguments by node name](#access-arguments-by-node-name).

Similar to the four methods of accessing arguments by their node name, there also are four methods you can use to access arguments by their index:

```java
Object get(int index);
Object getOrDefault(int index, Object defaultValue);
Object getOrDefault(int index, Supplier<?> defaultValue);
Optional<Object> getOptional(int index);
```

<div class="example">

### Example - Access arguments by node name and index

</div>

-----

## Access raw arguments

A "raw argument" is the `String` form of an argument as written in a command. For example:

A user defines a command `/mycommand` that accepts a `double` as the first argument and an entity selector as the second argument. It could be executed with the values `15.3` as the `double` value and `@e` as the entity selector:

```mccmd
/mycommand 15.3 @e
```

When [accessing the raw arguments](#access-raw-arguments) of this command there are `15.3` and `@e` available as `String`s.

However, when [accessing the arguments](#access-arguments) of this command there is `15.3` available as `double` and `@e` available as `Collection<Entity>`.


Raw arguments are accessed basically the same way you would [access arguments](#access-arguments). You can access them by their node name and their index in the argument array.

### Access raw arguments by node name

Accessing raw arguments by their node name is the recommended way of doing it.

To access raw arguments by their node name, you can use these methods:

```java
String getRaw(String nodeName);
String getOrDefaultRaw(String nodeName, String defaultValue);
String getOrDefaultRaw(String nodeName, Supplier<String> defaultValue);
Optional<String> getRawOptional(String nodeName);
```

### Access raw arguments by index

Of course, if you don't want to access raw arguments by their node name, we also provide the option to access them by index with these methods:

```java
String getRaw(int index);
String getOrDefaultRaw(int index, String defaultValue);
String getOrDefaultRaw(int index, Supplier<String> defaultValue);
Optional<String> getRawOptional(int index);
```

<div class="example">

### Example - Access raw arguments by node name and index

</div>

-----

## Access unsafe arguments

When [accessing arguments](#access-arguments) you need to cast the `Object` returned by these methods to the type the argument returns. More about casting arguments [here](./arguments.md#argument-casting).

However, the `CommandArguments` class provides a way to remove the need to cast the arguments which is referred to as `unsafe arguments` in this page.

Accessing unsafe arguments is a nice way to shorten your code as you do not need to cast the argument to its corresponding type.

_insert example of what an unsafe argument is_

Unsafe arguments can also be accessed by their node names and their indices.

### Access arguments by node name

Unsafe arguments can also be accessed by node name which, again, is the recommended way of doing it.

Use these methods when accessing unsafe arguments by their node name:

```java
T getUnchecked(String nodeName);
T getOrDefaultUnchecked(String nodeName, T defaultValue);
T getOrDefaultUnchecked(String nodeName, Supplier<T> defaultValue);
Optional<T> getOptionalUnchecked(String nodeName);
```

### Access arguments by index

If you want to access unsafe arguments by index, you can do that by using these methods:

```java
T getUnchecked(int index);
T getOrDefaultUnchecked(int index, T defaultValue);
T getOrDefaultUnchecked(int index, Supplier<T> defaultValue);
Optional<T> getOptionalUnchecked(int index);
```

<div class="example">

### Example - Access unsafe arguments by node name and index

</div>
