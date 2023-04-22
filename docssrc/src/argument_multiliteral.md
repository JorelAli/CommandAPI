# Multi literal arguments

So far, we've described normal arguments and literal arguments. We've described the nuances with literal arguments and how they're not really "arguments", so they don't appear in the `args[]` for commands.

Now forget all of that. Multi literal arguments are the same as literal arguments but they _do_ appear in the `args[]` for commands (i.e. they are [listed](./listed.md)). Multi literal arguments are just a way better alternative to literal arguments. The multi literal argument constructor allows you to provide a `String[]` of possible values which you can use for your command declaration.

The multi literal argument has all of the same benefits of a regular literal argument - they are hardcoded options that the user must enter - they don't allow other values.

<div class="example">

### Example - Using multi literals to make the gamemode command

In this example, we'll show how to use multi literals to declare Minecraft's `/gamemode` command. As you can see from the example code below, the argument declaration and command declaration is the same as if you were declaring any normal argument or command.

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentMultiLiteral1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentMultiLiteral1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentMultiLiteral1}}
```

</div>

An important thing to note is that we don't have to implement a `default` case for the above `switch` statements, because the CommandAPI will only permit valid options of a `MultiLiteralArgument` to reach the executor. If the user enters an invalid option, the command doesn't run.

</div>
