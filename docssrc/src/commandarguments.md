# CommandArguments

The `CommandArguments` class was introduced in CommandAPI 9.0.0 and provides a much more powerful way of accessing arguments than just an array of arguments which existed until 9.0.0.

While the argument array just gives the possibility to access the arguments via the array notation (`args[0]`), the `CommandArguments` class offers much more, including:

- [Access the inner structure directly](#access-the-inner-structure-directly)
- [What terms are used?](#what-terms-are-used)
  - [`nodeName`](#nodename)
  - [`raw argument`](#raw-argument)
  - [`unsafe argument`](#unsafe-argument)
- [Access arguments](#access-arguments)
  - [Access arguments by node name](#access-arguments-by-node-name)
  - [Access arguments by index](#access-arguments-by-index)
- [Access raw arguments](#access-raw-arguments)
  - [Access raw arguments by node name](#access-raw-arguments-by-node-name)
  - [Access raw arguments by index](#access-raw-arguments-by-index)
- [Access unsafe arguments](#access-unsafe-arguments)
  - [Access arguments by node name](#access-arguments-by-node-name-1)
  - [Access arguments by index](#access-arguments-by-index-1)

ATTENTION!!!!! Every executable and generated documentation has to be deleted before committing anything!

-----

## Access the inner structure directly

To provide arguments, the `CommandArguments` class stores:

- a `Object[]` of parsed arguments
- a `Map<String, Object>` of parsed arguments mapped to their node names
- a `String[]` of raw arguments
- a `Map<String, String>` of raw arguments mapped to their node names
- a `String` which holds the full input

Although not recommended, it is possible to access these fields directly with methods the `CommandArguments` class provides:

```java
Object[] args();                  // Returns the argument array
Map<String, Object> argsMap();    // Returns an unmodifiable map containing the arguments mapped to their node names
String[] rawArgs();               // Returns the raw argument array
Map<String, String> rawArgsMap(); // Returns an unmodifiable map containing the raw arguments mapped to their node names
String fullInput();               // Returns the full command string (including the /)
```

Additionally, the `CommandArguments` class has one more method that isn't directly backed by a field and returns the amount of arguments for a command:

```java
int count();
```

While these methods can be used to access arguments, it may be safer to use the other methods the `CommandArguments` class provides to access arguments.

-----

## What terms are used?

Throughout this page, multiple terms are used that may need an explanation.

### `nodeName`

The `nodeName` is set when initializing an argument. For example:

```java
new StringArgument("string") 
```

The `nodeName` here would be `string`.

### `raw argument`

A "raw argument" is the `String` form of an argument as written in a command. For example:

A user defines a command `/mycommand` that accepts a `double` as the first argument and an entity selector as the second argument. It could be executed with the values `15.3` as the `double` value and `@e` as the entity selector:

```mccmd
/mycommand 15.3 @e
```

When [accessing the raw arguments](#access-raw-arguments) of this command there are `15.3` and `@e` available as `String`s.

However, when [accessing the arguments](#access-arguments) of this command there is `15.3` available as `double` and `@e` available as `Collection<Entity>`.

### `unsafe argument`

When [accessing arguments](#access-arguments) you need to cast the `Object` returned by these methods to the type the argument returns. More about casting arguments [here](./arguments.md#argument-casting).

However, the `CommandArguments` class provides a way to remove the need to cast the arguments which is referred to as `unsafe arguments` in this page.

-----

## Access arguments

The `CommandArguments` class provides its arguments in a way similar to how a `List` or `Map` let you access their contents. When using these methods, you need to cast the arguments to their respective type. The `CommandArguments` class also provides a way to [access unsafe arguments](#access-unsafe-arguments).

You can choose to access arguments by their node name or by their index.

### Access arguments by node name

Accessing arguments by their node name is the recommended way of accessing arguments.

There are two methods you can use to access arguments by their node name:

```java
Object get(String nodeName);
Optional<Object> getOptional(String nodeName);
```

There is no downside of using one method over the other but the `CommandArguments#getOptional(String)` method is especially great when you have optional arguments in your command.

### Access arguments by index

Accessing arguments by their index is the original way of accessing arguments. However, we recommend to [access arguments by node name](#access-arguments-by-node-name).

Similar to the two methods of accessing arguments by their node name, there also are two methods you can use to access arguments by their index:

```java
Object get(int index);
Optional<Object> getOptional(int index);
```

<div class="example">

### Example - Access arguments by node name and index

</div>

-----

## Access raw arguments

Raw arguments are accessed basically the same way you would [access arguments](#access-arguments). You can access them by their node name and their index in the argument array.

### Access raw arguments by node name

Accessing raw arguments by their node name is the recommended way of doing it.

To access raw arguments by their node name, you can use these methods:

```java
String getRaw(String nodeName);
Optional<String> getRawOptional(String nodeName);
```

### Access raw arguments by index

Of course, if you don't want to access raw arguments by their node name, we also provide the option to access them by index with these methods:

```java
String getRaw(int index);
Optional<String> getRawOptional(int index);
```

<div class="example">

### Example - Access raw arguments by node name and index

</div>

-----

## Access unsafe arguments

Accessing unsafe arguments is a nice way to shorten your code as you do not need to cast the argument to its corresponding type.

Here, you might notice the usage of several `getOrDefaultUnchecked` methods and not the `getOptionalUnchecked` methods you might have expected.
This is the case because Java's type inference only goes one level deep which cases issues when calling a method on the `Optional` that is returned to provide a default value.
That is not a problem when [accessing arguments](#access-arguments) because here you are expected to cast the argument anyway but if you used the `getOptionalUnchecked` method you still would have to provide a type which is not ideal when you don't want to cast arguments.

Unsafe arguments can also be accessed by their node names and their indices.

### Access arguments by node name

Unsafe arguments can also be accessed by node name which, again, is the recommended way of doing it.

In the case of unsafe arguments, the CommandAPI doesn't provide two but instead three methods to access them:

```java
T getUnchecked(String nodeName);
T getOrDefaultUnchecked(String nodeName, T defaultValue);
T getOrDefaultUnchecked(String nodeName, Supplier<T> defaultValue);
```

### Access arguments by index

If you want to access unsafe arguments by index, you can do that by using these methods:

```java
T getUnchecked(int nodeName);
T getOrDefaultUnchecked(int nodeName, T defaultValue);
T getOrDefaultUnchecked(int nodeName, Supplier<T> defaultValue);
```

<div class="example">

### Example - Access unsafe arguments by node name and index

</div>
