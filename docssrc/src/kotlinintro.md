# Kotlin-based commands

The CommandAPI also provides an alternative way of making commands when using Kotlin to develop your plugins: A DSL!

This DSL provides many methods to easily add arguments to your command structure. Examples of the DSL can be found [here](./kotlindsl.md).

-----

## Installing the DSL
To install the DSL, you need to add the `commandapi-kotlin` dependency into your `pom.xml` or your `build.gradle`:

### Adding the dependency with Maven

```xml
<dependencies>
    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-kotlin</artifactId>
        <version>8.6.0</version>
    </dependency>
</dependencies>
```

### Adding the dependency with Gradle

First, you need to add the repository:

<div class="multi-pre">

```groovy,build.gradle
repositories {
    mavenCentral()
}
```

```kotlin,build.gradle.kts
repositories {
    mavenCentral()
}
```

</div>

Next, you need to add the dependency:

<div class="multi-pre">

```groovy,build.gradle
dependencies {
    implementation "dev.jorel:commandapi-kotlin:8.6.0"
}
```

```kotlin,build.gradle.kts
dependencies {
    implementation("dev.jorel:commandapi-kotlin:8.6.0")
}
```

</div>

Finally, you need to add it to the `shadowJar` configuration task and relocate it to your desired location:

<div class="multi-pre">

```groovy,build.gradle
shadowJar {
    dependencies {
        include dependency("dev.jorel:commandapi-kotlin:8.6.0")
    }
    
    // TODO: Change this to my own package name
    relocate("dev.jorel.commandapi", "my.custom.package.commandapi")
}
```

```kotlin,build.gradle.kts
shadowJar {
    dependencies {
        include dependency("dev.jorel:commandapi-kotlin:8.6.0")
    }
    
    // TODO: Change this to my own package name
    relocate("dev.jorel.commandapi", "my.custom.package.commandapi")
}
```

</div>