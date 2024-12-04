# Setting up your project

In the most common simple case, tests can be added directly next to plugin code. Code that goes in your final jar file is located in the `/src/main/` directory, while tests go in `/src/test/`. You can find more information about setting up your project for tests in the [JUnit](https://junit.org/junit5/docs/current/user-guide/#overview-getting-started-example-projects) documentation.

## Dependencies

When you add the dependencies for MockBukkit and `commandapi-bukkit-test-toolkit`, make sure to place them before your main dependencies for the CommandAPI and Spigot/Paper API. This ensures that certain classes that are compatible with the testing environment override the regular classes when running tests.

<div class="multi-pre">

```xml,Maven
<dependencies>
    <!-- See https://github.com/MockBukkit/MockBukkit?tab=readme-ov-file#mag-usage for latest version -->
    <dependency>
        <groupId>com.github.seeseemelk</groupId>
        <artifactId>MockBukkit-v1.21</artifactId>
        <version>3.128.0</version>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-bukkit-test-toolkit</artifactId>
        <version>9.7.0</version>
        <scope>test</scope>
    </dependency>

    <!-- May be the shade dependency and/or mojang-mapped -->
    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-bukkit-core</artifactId>
        <version>9.7.0</version>
        <scope>provided</scope>
    </dependency>

    <!-- Can also be paper-api -->
    <dependency>
        <groupId>org.spigotmc</groupId>
        <artifactId>spigot-api</artifactId>
        <version>1.21.1-R0.1-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>

    <!-- See https://junit.org/junit5/ for latest version -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.8.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

```groovy,Gradle_(build.gradle)
dependencies {
    // See https://github.com/MockBukkit/MockBukkit?tab=readme-ov-file#mag-usage for latest version
    testImplementation 'com.github.seeseemelk:MockBukkit-v1.21:3.128.0'

    testImplementation 'dev.jorel.commandapi-bukkit-test-toolkit:9.6.2-SNAPSHOT'

    // May be the shade dependency and/or mojang-mapped
    compileOnly 'dev.jorel:commandapi-bukkit-core:9.7.0'

    // Can also be paper-api
    compileOnly 'org.spigotmc:spigot-api:1.21.1-R0.1-SNAPSHOT'

    // See https://junit.org/junit5/ for latest version
    testImplementation 'org.junit.jupiter:junit-jupiter-engine:5.8.2'
}
```

```kotlin,Kotlin_Gradle_(build.gradle.kts)
dependencies {
    // See https://github.com/MockBukkit/MockBukkit?tab=readme-ov-file#mag-usage for latest version
    testImplementation('com.github.seeseemelk:MockBukkit-v1.21:3.128.0')

    testImplementation('dev.jorel.commandapi-bukkit-test-toolkit:9.6.2-SNAPSHOT')

    // May be the shade dependency and/or mojang-mapped
    compileOnly('dev.jorel:commandapi-bukkit-core:9.7.0')

    // Can also be paper-api
    compileOnly('org.spigotmc:spigot-api:1.21.1-R0.1-SNAPSHOT')

    // See https://junit.org/junit5/ for latest version
    testImplementation('org.junit.jupiter:junit-jupiter-engine:5.8.2')
}
```

</div>
