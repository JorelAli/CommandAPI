# commandapi-trees

A simple example showcasing creating a command using a `CommandTree` with the CommandAPI.

Key points:

- It's much easier to declare the _command structure_ using the command tree, then use method reference lambdas to implement the relevant executor methods than it is to use inline lambdas.

In other words, it's much simpler to use the following:

```java
public register() {
    new CommandTree("command")
        .then(new LiteralArgument("something")
            .executes(this::someMethod)
        )
        .register();
}

public someMethod(CommandSender sender, Object[] args) {
    // Implementation here
}
```

Compared to using the following:

```java
public register() {
    new CommandTree("command")
        .then(new LiteralArgument("something")
            .executes((sender, args) -> {
                // Implementation here
            })
        )
        .register();
}
```
