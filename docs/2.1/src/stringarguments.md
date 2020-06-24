# String arguments

## String argument

The `StringArgument` class is used to represent a single word. These words **can only contain alphanumeric characters (A-Z, a-z and 0-9), and the underscore character**.

Accepted `StringArgument` values:

```
Hello
123
hello123
Hello_world
```

Rejected `StringArgument` values:

```
hello@email.com
yesn't
```

### Potential uses for string arguments

* Entering Strings to identify offline players

## Text argument

The `TextArgument` acts similar to any String in Java. These can be single words, like to the `StringArgument`, or have additional characters (e.g. spaces, symbols) **if surrounded by quotes**. To type quotation marks, you can use `\"` (as similar to Java) to escape these special characters.

Accepted `TextArgument` values:

```
hello
"hello world!"
"hello@gmail.com"
"this has \" <<-- speech marks! "
```

Rejected `TextArgument` values:

```
hello world
私
"speech marks: ""
```

### Potential uses for text arguments

- A command to edit the contents on a sign
- Any command that may require multiple text arguments

## Greedy string argument

The `GreedyStringArgument` takes the `TextArgument` a step further. **Any characters and symbols are allowed** and quotation marks are not required. **However, the `GreedyStringArgument` uses the entirety of the argument array from its position**.

### Example - Messaging command

Say we have a command `/msg <target> <message>`

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("target", new PlayerArgument());
arguments.put("message", new GreedyStringArgument());

CommandAPI.getInstance().register("msg", arguments, (sender, args) -> {
	((Player) args[0]).sendMessage((String) args[1]);
});
```

Any text entered after the `<target>` argument would be sent to the player. For example, the command could be used as follows:

```
/msg Skepter This is some incredibly long string with "symbols" and $p3c!aL characters~
```


Due to the fact that **the `GreedyStringArgument` has no terminator** (it has infinite length), **a `GreedyStringArgument` must be defined at the end of the `LinkedHashMap`** (otherwise the CommandAPI will throw a `GreedyStringException`)

For example, if the syntax was`/msg <message> <target>`, it would not be able to determine where the message ends and the `<target>` argument begins.

### Potential uses for greedy strings

- A messaging/whisper command
- A mailing command
- Any command involving lots of text, such as a book writing command
- Any command which involves an unreasonable/unknown amount of arguments
- Any command where you want to parse arguments similar to how regular Bukkit would
