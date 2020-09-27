# Predicate tips

In our [example for creating a party system](./requirements.md#example---a-party-creation-and-teleportation-system), we ended up having lots of code repetition. In our party creation command, we had the following code:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:requirements2}}
```

And for our party teleportation command, we had the following code:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:requirementstp}}
```

We can simplify this code by declaring the predicate:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:predicatetips}}
```

Now, we can use the predicate `testIfPlayerHasParty` in our code for creating a party. Since we want to apply the "not" (`!`) operator to this predicate, we can use `.negate()` to invert the result of our predicate:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:predicatetips2}}
```

And we can use it again for our code for teleporting to party members:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:predicatetips3}}
```

