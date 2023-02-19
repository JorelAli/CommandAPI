# Objective arguments

In the CommandAPI, objectives are split into two classes:

- The `ObjectiveArgument` class, which represents objectives as a whole
- The `ObjectiveCriteriaArgument` class, which represents objective criteria

-----

## Objective argument

The objective argument refers to a single scoreboard objective.

<div class="example">

### Example - Move objective to sidebar

As an example, let's create a command to move an objective to a player's sidebar. To do this, we will use the following command syntax:

```mccmd
/sidebar <objective>
```

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentObjectives1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentObjectives1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentObjectives1}}
```

</div>

</div>

-----

## Objective criteria argument

The `ObjectiveCriteriaArgument` is fairly straight forward - it represents the criteria for an objective. Similar to Bukkit, the objective criteria is simply represented as a `String`, so it must be casted to a `String` when being used.

<div class="example">

### Example - Unregister all objectives by criteria

Say we wanted to create a command to unregister all objectives based on a given criteria. Let's create a command with the following form:

```mccmd
/unregisterall <objective critera>
```

To do this, we're going to take advantage of Bukkit's `Scoreboard.getObjectivesByCriteria(String)` method

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentObjectives2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentObjectives2}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentObjectives2}}
```

</div>

</div>
