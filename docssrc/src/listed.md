# Listed arguments

Arguments have a setting which determine whether or not they are present in the `Object[] args` that is populated when executing a command.

By default, the `LiteralArgument` has this setting set to `false`, hence the literal values are _not_ present in the `Object[] args`.

This flag is set using the following function:

```java
Argument setListed(boolean listed);
```

<div class="example">

### Example - Setting listed arguments

Say we have the following command:

```mccmd
/mycommand <player> <value> <message>
```

Let's also say that in our implementation of this command, we don't actually perform any processing for `<value>`. Hence, listing it in the `Object args[]` is unnecessary.

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:listed1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:listed1}}
```

</div>

In this scenario, the argument `<value>` is not present in the `Object args[]` for the executor.

</div>
