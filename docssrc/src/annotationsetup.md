# Using the annotation system

The annotation system is a separate part of the CommandAPI, and as a result it needs to be included as an additional dependency to your project. As well as that, the compiler needs to be aware that the annotation processor exists, so this needs to be added to the build cycle.

-----

## Manually (why would you do this)

-----

## Using Maven (recommended)

- If you haven't already done so, add the maven repository to your `pom.xml` file:

  ```xml
  <repositories>
      <repository>
          <id>commandapi</id>
          <url>https://raw.githubusercontent.com/JorelAli/CommandAPI/mvn-repo/</url>
      </repository>
  </repositories>
  ```

- Add the annotation dependency to your `pom.xml`:

  ```xml
  <dependencies>
      <dependency>
          <groupId>dev.jorel</groupId>
          <artifactId>commandapi-annotations</artifactId>
          <version>5.3</version>
          <scope>compile</scope>
      </dependency>
  </dependencies>
  ```

- Add the annotation processor as an annotation process to the compile task in the `pom.xml`:

  ```xml
  <build>
      <plugins>
          <plugin>
              <groupId>org.apache.maven.plugins</groupId>
              <artifactId>maven-compiler-plugin</artifactId>
              <version>3.8.1</version>
              <configuration>
                  <annotationProcessors>
                      <annotationProcessor>dev.jorel.commandapi.annotations.Annotations</annotationProcessor>
                  </annotationProcessors>
              </configuration>
          </plugin>
      </plugins>
  </build>
  ```


-----

## Using Gradle