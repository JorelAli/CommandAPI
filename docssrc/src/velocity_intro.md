# Velocity

> **Developer's Note:**
> 
> The CommandAPI hasn't been released for Velocity yet.
> We do, however, offer snapshot builds. This small section on Velocity will outline how to get the snapshot builds and what limitations the CommandAPI currently has on Velocity.

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
        <version>9.4.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

```gradle,build.gradle
dependencies {
    compile "dev.jorel:commandapi-velocity-shade:9.4.0-SNAPSHOT"
}
```

```gradle,build.gradle.kts
dependencies {
    compileOnly("dev.jorel:commandapi-velocity-shade:9.4.0-SNAPSHOT")
}
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