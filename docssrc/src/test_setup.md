# Setting up your project

In the most common simple case, tests can be added directly next to plugin code. Code that goes in your final jar file is located in the `/src/main/` directory, while tests go in `/src/test/`. You can find more information about setting up your project for tests in the [JUnit](https://junit.org/junit5/docs/current/user-guide/#overview-getting-started-example-projects) documentation.

## Dependencies

When you add the dependencies for MockBukkit and `commandapi-bukkit-test-toolkit`, make sure to place them before your main dependencies for the CommandAPI and Spigot/Paper API. This ensures that certain classes that are compatible with the testing environment override the regular classes when running tests.

<div class="multi-pre">

```xml,Plugin_Dependency
<dependencies>
    <dependency>
        <groupId>com.github.seeseemelk</groupId>
        <artifactId>MockBukkit-v1.20</artifactId>
        <version>3.9.0</version>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-bukkit-test-toolkit</artifactId>
        <version>9.6.0-SNAPSHOT</version>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-bukkit-core</artifactId>
        <version>9.6.0-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>

    <dependency>
        <groupId>org.spigotmc</groupId>
        <artifactId>spigot-api</artifactId>
        <version>1.20.6-R0.1-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>

    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.8.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

```xml,Shading_CommandAPI
<dependencies>
    <dependency>
        <groupId>com.github.seeseemelk</groupId>
        <artifactId>MockBukkit-v1.20</artifactId>
        <version>3.9.0</version>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-bukkit-test-toolkit</artifactId>
        <version>9.6.0-SNAPSHOT</version>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-bukkit-shade</artifactId>
        <version>9.6.0-SNAPSHOT</version>
        <scope>compile</scope>
    </dependency>

    <dependency>
        <groupId>org.spigotmc</groupId>
        <artifactId>spigot-api</artifactId>
        <version>1.20.6-R0.1-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>

    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.8.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

</div>
