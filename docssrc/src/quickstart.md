# Setting up your development environment

To use the CommandAPI in your plugins, there are a few methods of adding it to your development environment.

-----

## Manually using the .jar

- Download the latest CommandAPI.jar from the download page [here](https://github.com/JorelAli/CommandAPI/releases/latest)

- Add the CommandAPI.jar file to your project/environment's build path:

  ![](images/eclipse.jpg)
  
- Add the CommandAPI as a dependent in the plugin.yml (`depend: [CommandAPI]`)

-----

## Using Maven (recommended)

> **Developer's Note:**
>
> If you've never used maven before, I highly recommend it! It makes it easier to keep your code updated with the latest dependency updates. For information on how to set up a plugin using maven, you can read [Bukkit's plugin tutorial](https://bukkit.gamepedia.com/Plugin_Tutorial).

* Add the maven repository to your `pom.xml` file:

  ```xml
  <repositories>
      <repository>
          <id>commandapi</id>
          <url>https://raw.githubusercontent.com/JorelAli/CommandAPI/mvn-repo/</url>
      </repository>
  </repositories>
  ```

* Add the dependency to your `pom.xml`:

  ```xml
  <dependencies>
      <dependency>
          <groupId>dev.jorel</groupId>
          <artifactId>commandapi-core</artifactId>
          <version>6.0</version>
          <scope>provided</scope>
      </dependency>
  </dependencies>
  ```
* Add the CommandAPI as a dependent in the `plugin.yml` (`depend: [CommandAPI]`)

-----

## Using Gradle

* Add the repositories to your `build.gradle` file (the second repository is required because the CommandAPI depends on the NBT-API):

  ```gradle
  repositories {
      maven { url = "https://raw.githubusercontent.com/JorelAli/CommandAPI/mvn-repo/" }
      maven { url = "https://repo.codemc.org/repository/maven-public/" }
  }
  ```
  
* Add the dependency to your list of dependencies in your `build.gradle` file:

  ```gradle
  dependencies {
      compileOnly "dev.jorel:commandapi-core:6.0"
  }
  ```


- Add the CommandAPI as a dependent in the `plugin.yml` (`depend: [CommandAPI]`)