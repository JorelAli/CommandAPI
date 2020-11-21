# Annotations

This page outlines in detail the list of all annotations that the CommandAPI's annotation-based command system includes.

---

## Annotations that go on classes

### `@Command`("commandName")

The `@Command` annotation is used to declare a command. The parameter is the name of the command that will be registered.

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/WarpCommand.java:warps_command}}
```

### `@Alias({...})`

The `@Alias` annotation is used to declare a list of aliases for a command. The parameter is a list of aliases which can be used for the command.

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/TeleportCommand.java:teleport_command}}
```

### `@Permission("permissionNode")`

The `@Permission` annotation is used to add a permission node to a command. Users that want to run this command must have this permission. The parameter is the permission node required to run the command.

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/Test2Command.java:teleport_command_perms}}
```

### `@NeedsOp`

The `@NeedsOp` annotation is used to indicate that a command needs to have operator privileges to run it. This annotation has no parameters.

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/Test2Command.java:teleport_command_needsop}}
```



-----

## Annotations that go on methods

