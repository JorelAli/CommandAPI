# Kotlin-based commands

The CommandAPI also provides an alternative way of making commands when using Kotlin to develop your plugins: A DSL!

This DSL provides many methods to easily add arguments to your command structure. Examples of the DSL can be found [here](./kotlindsl.md).

-----

## Installing the DSL

To install the DSL, you need to add the `commandapi-kotlin` dependency into your `pom.xml` or your `build.gradle`, making sure to specify the server flavor you are developing for:

### Adding the dependency with Maven

<div class="linked-multi-pre">

```xml,Bukkit
<dependencies>
    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-bukkit-kotlin</artifactId>
        <version>9.0.1</version>
    </dependency>
</dependencies>
```

```xml,Velocity
<dependencies>
    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-velocity-kotlin</artifactId>
        <version>9.0.1</version>
    </dependency>
</dependencies>
```

</div>

Next, you need to add Kotlin to your project. For this, you first need to add the dependency:

```xml
<dependencies>
    <dependency>
        <groupId>org.jetbrains.kotlin</groupId>
        <artifactId>kotlin-stdlib</artifactId>
        <version>1.8.20</version>
    </dependency>
</dependencies>
```

Finally, you need to add the `kotlin-maven-plugin`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-maven-plugin</artifactId>
            <version>1.8.20</version>
            <executions>
                <execution>
                    <id>compile</id>
                    <phase>compile</phase>
                    <goals>
                        <goal>compile</goal>
                    </goals>
                </execution>
                <execution>
                    <id>test-compile</id>
                    <phase>test-compile</phase>
                    <goals>
                        <goal>test-compile</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <jvmTarget>16</jvmTarget>
            </configuration>
        </plugin>
    </plugins>
</build>
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

```groovy,Bukkit_build.gradle
dependencies {
    implementation "dev.jorel:commandapi-bukkit-kotlin:9.0.1"
}
```

```kotlin,Bukkit_build.gradle.kts
dependencies {
    implementation("dev.jorel:commandapi-bukkit-kotlin:9.0.1")
}
```

```groovy,Velocity_build.gradle
dependencies {
    implementation "dev.jorel:commandapi-velocity-kotlin:9.0.0"
}
```

```kotlin,Velocity_build.gradle.kts
dependencies {
    implementation("dev.jorel:commandapi-velocity-kotlin:9.0.0")
}
```

</div>

You also need to add Kotlin to your project. For this, you first need to add the Kotlin plugin:

<div class="multi-pre">

```groovy,build.gradle
plugins {
    id "org.jetbrains.kotlin.jvm" version "1.8.20"
}
```

```kotlin,build.gradle.kts
plugins {
    kotlin("jvm") version "1.8.20"
}
```

</div>

Next, you need to add the dependency (you should already have added the `mavenCentral()` repository to your project):

<div class="multi-pre">

```groovy,build.gradle
dependencies {
    implementation "org.jetbrains.kotlin:kotlin-stdlib"
}
```

```kotlin,build.gradle.kts
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
}
```

</div>

Then, you need to configure the Java version to build against:

<div class="multi-pre">

```groovy,build.gradle
kotlin {
    jvmToolchain(16)
}
```

```kotlin,build.gradle.kts
kotlin {
    jvmToolchain(16)
}
```

</div>
