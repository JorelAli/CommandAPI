# Setting up your development environment

To use the CommandAPI in your plugins, there are two methods of adding it to your development environment:

## Manually using the .jar

- Download the latest CommandAPI.jar from the download page [here](https://github.com/JorelAli/1.13-Command-API/releases/latest)
- Add the CommandAPI.jar file to your project/environment's build path
- Add the plugin as a dependent in the plugin.yml (`depend: [CommandAPI]`)

## Using Maven (recommended)

> **Developer's Note:**
>
> If you've never used maven before, I highly recommend it! It makes it easier to keep your code updated with the latest dependency updates. For information on how to set up a plugin using maven, you can read [Bukkit's plugin tutorial](https://bukkit.gamepedia.com/Plugin_Tutorial).

* Add the maven repository to your `pom.xml` file:

  ```
  <repository>
      <id>mccommandapi</id>
      <url>https://raw.githubusercontent.com/JorelAli/1.13-Command-API/mvn-repo/1.13CommandAPI/</url>
  </repository>
  ```

* Add the dependency to your `pom.xml`:

  ```
  <dependency>
      <groupId>io.github.jorelali</groupId>
      <artifactId>commandapi</artifactId>
      <version>VERSION</version>
  </dependency>
  ```
  A list of version numbers can be found [here](https://github.com/JorelAli/1.13-Command-API/tree/mvn-repo/1.13CommandAPI/io/github/jorelali/commandapi).
  For example, if you wanted to use version 2.0, you would use `<version>2.0</version>`

* Add the plugin as a dependent in the plugin.yml (`depend: [CommandAPI]`)
