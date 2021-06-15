# Registering annotation-based commands

Registering annotation-based commands is _really_ simple. To do this, we use the following method, where `className` is the name of a class with a `@Command` annotation:

```java
CommandAPI.registerCommand(className)
```

<div class="example">

### Example: Registering a Warp command

Say we have a simple command `/warp` that is defined as follows:

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/WarpCommand.java:warps}}
```

We can register this in our `onLoad()` method so we can use this command from within Minecraft functions:

```java
public {{#include ../../CommandAPI/commandapi-annotations/src/test/java/WarpCommand.java:warp_register2}}
```

</div>

