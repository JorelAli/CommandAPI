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
        <version>9.0.0</version>
    </dependency>
</dependencies>
```

```xml,Velocity
<dependencies>
    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-velocity-kotlin</artifactId>
        <version>9.0.0</version>
    </dependency>
</dependencies>
```

</div>

Next, to shade it into your project easily, you need to add the `maven-shade-plugin`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.3.0</version>
            <executions>
                <execution>
                    <id>shade</id>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <relocations>
                    <relocation>
                        <pattern>dev.jorel.commandapi.kotlindsl</pattern>
                        <!-- TODO: Change this to my own package name -->
                        <shadedPattern>my.custom.package.commandapi.kotlindsl</shadedPattern>
                    </relocation>
                </relocations>
            </configuration>
        </plugin>
    </plugins>
</build>
```

Next, you need to add Kotlin to your project. For this, you first need to add the dependency:

```xml
<dependencies>
    <dependency>
        <groupId>org.jetbrains.kotlin</groupId>
        <artifactId>kotlin-stdlib</artifactId>
        <version>1.8.0</version>
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
            <version>1.8.0</version>
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
    implementation "dev.jorel:commandapi-bukkit-kotlin:9.0.0"
}
```

```kotlin,Bukkit_build.gradle.kts
dependencies {
    implementation("dev.jorel:commandapi-bukkit-kotlin:9.0.0")
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

Finally, you need to add it to the `shadowJar` configuration task and relocate it to your desired location:

<div class="multi-pre">

```groovy,Bukkit_build.gradle
shadowJar {
    dependencies {
        include dependency("dev.jorel:commandapi-bukkit-kotlin:9.0.0")
    }
    
    // TODO: Change this to my own package name
    relocate("dev.jorel.commandapi", "my.custom.package.commandapi")
}
```

```kotlin,Bukkit_build.gradle.kts
shadowJar {
    dependencies {
        include dependency("dev.jorel:commandapi-bukkit-kotlin:9.0.0")
    }
    
    // TODO: Change this to my own package name
    relocate("dev.jorel.commandapi", "my.custom.package.commandapi")
}
```

```groovy,Velocity_build.gradle
shadowJar {
    dependencies {
        include dependency("dev.jorel:commandapi-velocity-kotlin:9.0.0")
    }
    
    // TODO: Change this to my own package name
    relocate("dev.jorel.commandapi", "my.custom.package.commandapi")
}
```

```kotlin,Velocity_build.gradle.kts
shadowJar {
    dependencies {
        include dependency("dev.jorel:commandapi-velocity-kotlin:9.0.0")
    }
    
    // TODO: Change this to my own package name
    relocate("dev.jorel.commandapi", "my.custom.package.commandapi")
}
```

</div>

You also need to add Kotlin to your project. For this, you first need to add the Kotlin plugin:

<div class="multi-pre">

```groovy,build.gradle
plugins {
    id "org.jetbrains.kotlin.jvm" version "1.8.0"
}
```

```kotlin,build.gradle.kts
plugins {
    kotlin("jvm") version "1.8.0"
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

Then, you need to add the `compileKotlin` task:

<div class="multi-pre">

```groovy,build.gradle
compileKotlin {
    kotlinOptions {
        jvmTarget = "16"
    }
}
```

```kotlin,build.gradle.kts
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}
```

</div>

Finally, you need to add it to the `shadowJar` configuration task:

<div class="multi-pre">

```groovy,build.gradle
shadowJar {
    dependencies {
        include dependency("org.jetbrains.kotlin:kotlin-stdlib")
    }
}
```

```kotlin,build.gradle.kts
shadowJar {
    dependencies {
        include dependency("org.jetbrains.kotlin:kotlin-stdlib")
    }
}
```

</div>
