# Ranged arguments

![A float range argument command with the argument "0.5.3.5" entered](./images/arguments/floatrange.png)

Ranged arguments allow players to provide a range between two numbers, all within a single argument. The CommandAPI provides two ranged arguments, `IntegerRangeArgument` for ranges with only integer values, and `FloatRangeArgument` for ranged with potential floating point values.

These consist of values such as:

| Input   | What it means                                                |
| ------- | ------------------------------------------------------------ |
| `5`     | The number 5                                                 |
| `5..10` | Numbers between 5 and 10, including 5 and 10                 |
| `5..`   | Numbers greater than or equal to 5 (bounded by Java's max number size) |
| `..5`   | Numbers less than or equal to 5 (bounded by Java's min number size) |

This allows you to let users define a range of values, which can be used to limit a value, such as the number of players in a region or for a random number generator.

-----

## The IntegerRange & FloatRange class

The CommandAPI returns an `IntegerRange` from the `IntegerRangeArgument`, and a `FloatRange` from the `FloatRangeArgument`, which represents the upper and lower bounds of the numbers provided by the command sender, as well as a method to check if a number is within that range.

The `IntegerRange` class has the following methods:

```java
class IntegerRange {
    public int getLowerBound();
    public int getUpperBound();
    public boolean isInRange(int);
}
```

The `FloatRange` class has the following methods:

```java
class FloatRange {
    public float getLowerBound();
    public float getUpperBound();
    public boolean isInRange(float);
}
```

<div class="example">

## Example - Searching chests for certain items

Say you're working on a plugin for server administrators to help them find restricted items. A method of doing so would be to search chests in a given radius for certain items. As such, we can use the following syntax:

```mccmd
/searchchests <range> <item>
```

Now, we simply create our arguments using `IntegerRangeArgument` for our range and `ItemStackArgument` as the item to search for. We can then find all chests in a given area and determine if it is within the range provided by the command sender by using `range.isInRange(distance)`:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentRange1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentRange1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentRange1}}
```

</div>

</div>
