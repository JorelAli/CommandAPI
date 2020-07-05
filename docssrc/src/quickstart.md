# Setting up your development environment

To use the CommandAPI in your plugins, there are a few methods of adding it to your development environment.

-----

## Manually using the .jar

- Download the latest CommandAPI.jar from the download page [here](https://github.com/JorelAli/1.13-Command-API/releases/latest)

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
          <id>mccommandapi</id>
          <url>https://raw.githubusercontent.com/JorelAli/1.13-Command-API/mvn-repo/1.13CommandAPI/</url>
      </repository>
  </repositories>
  ```

* Add the dependency to your `pom.xml`:

  ```xml
  <dependencies>
      <dependency>
          <groupId>dev.jorel</groupId>
          <artifactId>commandapi-core</artifactId>
          <version>VERSION</version>
      </dependency>
  </dependencies>
  ```
  A list of version numbers can be found [here](https://github.com/JorelAli/1.13-Command-API/tree/mvn-repo/1.13CommandAPI/io/github/jorelali/commandapi)
  For example, if you wanted to use version 3.0, you would use `<version>3.0</version>`

* Add the CommandAPI as a dependent in the plugin.yml (`depend: [CommandAPI]`)

> **Please Note:**
>
> In version 3.0 onwards, the group ID is no longer `io.github.jorelali`. Instead, the group ID is `dev.jorel`, as shown above. If you would like to use a version of the CommandAPI that is less than 3.0, you must make sure the group ID is `io.github.jorelali`.

-----

## Using Gradle

* Add the repository to your `build.gradle` file:

  ```gradle
  repositories {
      maven {
          name = 'mccommandapi'
          url = 'https://raw.githubusercontent.com/JorelAli/1.13-Command-API/mvn-repo/1.13CommandAPI/'
      }
  }
  ```

* Add the dependency to your list of dependencies in your `build.gradle` file:

  ```gradle
  dependencies {
      compile "dev.jorel:commandapi-core:VERSION"
  }
  ```

  A list of version numbers can be found [here](https://github.com/JorelAli/1.13-Command-API/tree/mvn-repo/1.13CommandAPI/io/github/jorelali/commandapi).
  For example, if you wanted to use version 3.0, you would use `compile "dev.jorel:commandapi-core:3.0"`

* Add the CommandAPI as a dependent in the plugin.yml (`depend: [CommandAPI]`)
