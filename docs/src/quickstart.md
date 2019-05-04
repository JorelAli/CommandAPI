# Quickstart 

## Adding the CommandAPI to your project

### Manually using the .jar

- Download the v1.8.2 CommandAPI.jar from the download page [here](https://github.com/JorelAli/1.13-Command-API/releases/tag/v1.8.2)
- Add the CommandAPI.jar file to your project/environment's build path
- Add the plugin as a dependent in the plugin.yml (`depend: [CommandAPI]`)

### Using Maven

* Add the maven repository:

  ```
  <repository>
      <id>mccommandapi</id>
      <url>https://raw.githubusercontent.com/JorelAli/1.13-Command-API/mvn-repo/1.13CommandAPI/</url>
  </repository>
  ```

* Add the dependency:

  ```
  <dependency>
      <groupId>io.github.jorelali</groupId>
      <artifactId>commandapi</artifactId>
      <version>1.8.2</version>
  </dependency>
  ```

* Add the plugin as a dependent in the plugin.yml (`depend: [CommandAPI]`)

## Starting template

* Generate a `LinkedHashMap<String, Argument>` to store your arguments for your command. **The insertion order is important.**

  ```java
  LinkedHashMap<String, Argument> args = new LinkedHashMap<>();
  args.put("time", new IntegerArgument());
  ```

* Register your command using the CommandAPI instance

  ```java
  CommandAPI.getInstance().register("mycommand", arguments, (sender, args) -> {
      if(sender instanceof Player) {
       	Player player = (Player) sender;
          player.getWorld().setTime((int) args[0]);
      }
  });
  ```
