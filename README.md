# CommandAPI Maven Repository
## Repository Usage
- Add the maven repository to your project:

  ```
  <repository>
      <id>commandapi</id>
      <url>https://raw.githubusercontent.com/JorelAli/CommandAPI/mvn-repo/</url>
  </repository>
  ```

- Add the dependency with a version number:

  ```
  <dependency>
      <groupId>dev.jorel</groupId>
      <artifactId>commandapi-core</artifactId>
      <version>5.3</version>
  </dependency>
  ```

-----

## Maven Modules

| Module name               | What it's for                                                |
| ------------------------- | ------------------------------------------------------------ |
| `commandapi-core`         | The main CommandAPI API                                      |
| `commandapi-vh`           | The CommandAPI's version handler system. This is available in both the shaded and plugin releases of the CommandAPI |
| `commandapi-plugin`       | The CommandAPI plugin. Includes plugin-specific files and code |
| `commandapi-shade`        | The CommandAPI, for shading purposes. Doesn't include plugin-specific files or code |
| `commandapi-annotations`  | The CommandAPI's annotation-based command framework's annotation processor |
| `commandapi-preprocessor` | A reflection checking annotation processor for NMS           |
| `commandapi-x.y.z`        | The NMS specific code for Minecraft version `x.y.z`          |

