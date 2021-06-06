# Objective arguments

In the CommandAPI, objectives are split into two classes: 

- The `ObjectiveArgument` class, which represents objectives as a whole
- The `ObjectiveCriteriaArgument` class, which represents objective criteria

-----

## Objective argument

The objective argument refers to a single scoreboard objective. Unconventionally, the `ObjectiveArgument` must be cast to `String` due to implementation limitations.

> **Developer's Note:**
>
> The two classes `ObjectiveArgument` and `TeamArgument` must both be cast to `String`, as opposed to `Objective` and `Team` respectively. This is due to the fact that commands are typically registered in the `onLoad()` method during a plugin's initialization. At this point in the server start-up sequence, the main server scoreboard is not initialized, so it cannot be used.

<div class="example">

### Example - Move objective to sidebar

As an example, let's create a command to move an objective to a player's sidebar. To do this, we will use the following command syntax:

```mccmd
/sidebar <objective>
```

Given that an objective has to be casted to a String, we have to find a way to convert it from its name to a Bukkit `Objective` object. We can do that by using the `getObjective(String)` method from a Bukkit `Scoreboard`:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:objectiveargument}}
```

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

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:objectivecriteriaarguments}}
```

</div>