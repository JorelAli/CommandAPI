# Predicate tips

In our [example for creating a party system](./requirements.md#example---a-party-creation-and-teleportation-system), we ended up having lots of code repetition. In our party creation command, we had the following code:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/test/dev/jorel/commandapi/examples/java/Examples.java:requirements2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/test/dev/jorel/commandapi/examples/kotlin/Examples.kt:requirements2}}
```

</div>

And for our party teleportation command, we had the following code:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/test/dev/jorel/commandapi/examples/java/Examples.java:requirementstp}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/test/dev/jorel/commandapi/examples/kotlin/Examples.kt:requirementstp}}
```

</div>

We can simplify this code by declaring the predicate:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/test/dev/jorel/commandapi/examples/java/Examples.java:predicatetips}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/test/dev/jorel/commandapi/examples/kotlin/Examples.kt:predicatetips}}
```

</div>

Now, we can use the predicate `testIfPlayerHasParty` in our code for creating a party. Since we want to apply the "not" (`!`) operator to this predicate, we can use `.negate()` to invert the result of our predicate:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/test/dev/jorel/commandapi/examples/java/Examples.java:predicatetips2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/test/dev/jorel/commandapi/examples/kotlin/Examples.kt:predicatetips2}}
```

</div>

And we can use it again for our code for teleporting to party members:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/test/dev/jorel/commandapi/examples/java/Examples.java:predicatetips3}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/test/dev/jorel/commandapi/examples/kotlin/Examples.kt:predicatetips3}}
```

</div>
