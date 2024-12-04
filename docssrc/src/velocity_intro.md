# Velocity

> **Developer's Note:**
>
> The CommandAPI hasn't been released for Velocity yet.
> We do, however, offer snapshot builds. This small section on Velocity will outline how to get the snapshot builds and what limitations the CommandAPI currently has on Velocity.
>
> This page focuses on outlining how to set up the CommandAPI for Velocity. It expects that you are already familiar with how to set up a Velocity plugin.

## Adding the snapshot repository with Maven or Gradle

Because we do not have an official release yet, the snapshot builds are not published in the Maven Central repository. Instead you need to add our snapshot repository:

<div class="multi-pre">

```xml,pom.xml
<repositories>
    <repository>
        <id>oss.sonatype.org-snapshot</id>
        <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
    </repository>
</repositories>
```

```gradle,build.gradle
repositories {
    maven {
        url = "https://s01.oss.sonatype.org/content/repositories/snapshots"
    }
}
```

```gradle,build.gradle.kts
repositories {
    maven {
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
}
```

</div>

## Adding the dependency

As mentioned, Velocity can only be accessed with snapshot builds. These snapshot build version are following standard semantic versioning and thus have the `-SNAPSHOT` suffix:

<div class="multi-pre">

```xml,pom.xml
<dependencies>
    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-velocity-shade</artifactId>
        <version>9.7.1-SNAPSHOT</version>
    </dependency>
</dependencies>
```

```gradle,build.gradle
dependencies {
    implementation "dev.jorel:commandapi-velocity-shade:9.7.1-SNAPSHOT"
}
```

```gradle,build.gradle.kts
dependencies {
    implementation("dev.jorel:commandapi-velocity-shade:9.7.1-SNAPSHOT")
}
```

</div>

## Setting up the CommandAPI

The CommandAPI requires two steps: loading and enabling. We will perform these steps in Velocity's loading stages, construction and initialization. These two stages are explained in [their documentation](https://docs.papermc.io/velocity/dev/api-basics#a-word-of-caution).
We will perform the CommandAPI's loading step in the construction phase first:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-velocity-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:velocityIntro1}}
```

</div>

Next, we want to utilize Velocity's `ProxyInitializeEvent` to perform the CommandAPI's enabling step:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-velocity-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:velocityIntro2}}
```

</div>

## Current limitations

The CommandAPI currently only offers support for a very limited amount of arguments on Velocity. This is because arguments are primarily implemented on the backend servers.
However, the CommandAPI offers access for the primitive type arguments:

- [`IntegerArgument`](./argument_primitives.md#numerical-arguments)
- [`LongArgument`](./argument_primitives.md#numerical-arguments)
- [`FloatArgument`](./argument_primitives.md#numerical-arguments)
- [`DoubleArgument`](./argument_primitives.md#numerical-arguments)
- [`BooleanArgument`](./argument_primitives.md#boolean-arguments)
- [`StringArgument`](./argument_strings.md#string-argument)
- [`TextArgument`](./argument_strings.md#text-argument)
- [`GreedyStringArgument`](./argument_strings.md#greedy-string-argument)
- [`LiteralArgument`](./argument_literal.md)
- [`MultiLiteralArgument`](./argument_multiliteral.md)

## Registering a simple command

Command registration works the same way as it does in Bukkit. To visualize this, we want to register a simple command that generates a random number between a chosen minimum and a chosen maximum value:

<div class="example">

### Example - Registering a simple command

We want to register the command `/randomnumber` with the following syntax:

```mccmd
/randomnumber <min> <max>
```

To accomplish that, we register the command like this:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-velocity-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:velocityIntro3}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-velocity-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:velocityIntro1}}
```

</div>

</div>
