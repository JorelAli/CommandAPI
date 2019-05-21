# Primitive arguments

## Boolean arguments

The `BooleanArgument` class represents boolean values `true` and `false`.

### Example - Config editing plugin

```java
String[] configKeys = getConfig().getKeys(true).toArray(new String[getConfig().getKeys(true).size()]);

LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("config-key", new TextArgument().overrideSuggestions(configKeys));
arguments.put("value", new BooleanArgument());

CommandAPI.getInstance().register("editconfig", arguments, (sender, args) -> {
	getConfig().set((String) args[0], (boolean) args[1]);
});
```

## Numerical arguments

Numbers are represented using the designated number classes:

| Class | Description |
| ----- | ----------- |
| `IntegerArgument` | Whole numbers |
| `DoubleArgument` | Double precision floating point numbers |
| `FloatArgument` | Single precision floating point numbers |

Each numerical argument can have ranges applied to them, which restricts the user to only entering numbers from within a certain range. This is done using the constructor, and the range specified:

| Constructor | Description |
| ----------- | ----------- |
| `new IntegerArgument()` | Any range |
| `new IntegerArgument(2)` | Values greater than _or equal to_ 2 |
| `new IntegerArgument(2, 10)` | Values greater than or equal to 2 and less than or equal to 10 |

Each range is _inclusive_, so it includes the number given to it.


