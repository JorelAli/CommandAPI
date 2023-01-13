# Installation

Installing the CommandAPI is easy! Just download the latest `CommandAPI-XXX.jar` file using the button below and place it in your server's `plugins/` folder!

<br>

<a href="https://github.com/JorelAli/CommandAPI/releases/latest" style="
background-color:#EB7035;
border-radius:3px;
color:#ffffff;
display:block;
line-height:44px;
text-align:center;
width:40%;
margin-top: -30px;
margin-bottom: 30px;
margin-left:auto;
margin-right: auto;">Download latest CommandAPI plugin</a>

-----

## Additional dependencies

- If you use NBT data, you'll also need the [NBT API](https://www.spigotmc.org/resources/nbt-api.7939/). (You can find out from your developers if you need this or not)
- If you are using raw JSON chat data, you'll need to be running [Spigot](https://www.spigotmc.org/wiki/about-spigot/) or a Paper-based server such as [Paper](https://papermc.io/) or [Purpur](https://purpurmc.org/). (Again, you can find out from your developers if you need this or not)

-----

## Running the CommandAPI on old versions of Minecraft

The CommandAPI is written and compiled using Java 16, and is only compatible with Minecraft servers that are compatible with Java 16 or higher. Attempting to use the CommandAPI on an old version of Minecraft (1.13 to 1.16.5) will throw an error (containing "org.bukkit.plugin.InvalidPluginException"):

```log
[11:14:40 ERROR]: Could not load 'plugins\CommandAPI-8.7.3.jar' in folder 'plugins'
org.bukkit.plugin.InvalidPluginException: java.lang.UnsupportedClassVersionError: dev/jorel/commandapi/CommandAPIMain has been compiled by a more recent version of the Java Runtime (class file version 60.0), this version of the Java Runtime only recognizes class file versions up to 52.0
        at org.bukkit.plugin.java.JavaPluginLoader.loadPlugin(JavaPluginLoader.java:138) ~[patched_1.13.2.jar:git-Paper-657]
        at org.bukkit.plugin.SimplePluginManager.loadPlugin(SimplePluginManager.java:334) ~[patched_1.13.2.jar:git-Paper-657]
        at org.bukkit.plugin.SimplePluginManager.loadPlugins(SimplePluginManager.java:255) ~[patched_1.13.2.jar:git-Paper-657]
        at org.bukkit.craftbukkit.v1_13_R2.CraftServer.loadPlugins(CraftServer.java:331) ~[patched_1.13.2.jar:git-Paper-657]
        at net.minecraft.server.v1_13_R2.DedicatedServer.init(DedicatedServer.java:235) ~[patched_1.13.2.jar:git-Paper-657]
        at net.minecraft.server.v1_13_R2.MinecraftServer.run(MinecraftServer.java:787) ~[patched_1.13.2.jar:git-Paper-657]
        at java.lang.Thread.run(Thread.java:745) [?:1.8.0_42]
Caused by: java.lang.UnsupportedClassVersionError: dev/jorel/commandapi/CommandAPIMain has been compiled by a more recent version of the Java Runtime (class file version 60.0), this version of the Java Runtime only recognizes class file versions up to 52.0
        at java.lang.ClassLoader.defineClass1(Native Method) ~[?:1.8.0_42]
        at java.lang.ClassLoader.defineClass(ClassLoader.java:760) ~[?:1.8.0_42]
        at java.security.SecureClassLoader.defineClass(SecureClassLoader.java:142) ~[?:1.8.0_42]
        at org.bukkit.plugin.java.PluginClassLoader.findClass(PluginClassLoader.java:136) ~[patched_1.13.2.jar:git-Paper-657]
        at org.bukkit.plugin.java.PluginClassLoader.findClass(PluginClassLoader.java:86) ~[patched_1.13.2.jar:git-Paper-657]
        at java.lang.ClassLoader.loadClass(ClassLoader.java:424) ~[?:1.8.0_42]
        at java.lang.ClassLoader.loadClass(ClassLoader.java:357) ~[?:1.8.0_42]
        at java.lang.Class.forName0(Native Method) ~[?:1.8.0_42]
        at java.lang.Class.forName(Class.java:348) ~[?:1.8.0_42]
        at org.bukkit.plugin.java.PluginClassLoader.<init>(PluginClassLoader.java:64) ~[patched_1.13.2.jar:git-Paper-657]
        at org.bukkit.plugin.java.JavaPluginLoader.loadPlugin(JavaPluginLoader.java:134) ~[patched_1.13.2.jar:git-Paper-657]
        ... 6 more
```

This can be mitigated by running a [Paper](https://papermc.io/) Minecraft server with the following command flag during startup:

```sh
-DPaper.IgnoreJavaVersion=true
```

<div class="example">

### Example - Running the CommandAPI using Java 17 and Minecraft 1.13.2

For example, to use the CommandAPI with Java 17 and Minecraft 1.13.2:

- Download a Paper server from the [Paper legacy downloads page](https://papermc.io/legacy) for Minecraft 1.13.2
- Run the following command in a terminal to start your Minecraft server:

  ```sh
  java -DPaper.IgnoreJavaVersion=true -jar paper-1.13.2-657.jar
  ```

</div>

Performing this will successfully load the CommandAPI, although various errors will appear in the console during startup. These errors (that start with "Fatal error trying to convert CommandAPI") can be ignored:

```log
[11:17:49 ERROR]: Fatal error trying to convert CommandAPI v8.7.3:dev/jorel/commandapi/CommandAPIMain.class
java.lang.IllegalArgumentException: Unsupported class file major version 60
        at org.objectweb.asm.ClassReader.<init>(ClassReader.java:195) ~[patched_1.13.2.jar:git-Paper-657]
        at org.objectweb.asm.ClassReader.<init>(ClassReader.java:176) ~[patched_1.13.2.jar:git-Paper-657]
        at org.objectweb.asm.ClassReader.<init>(ClassReader.java:162) ~[patched_1.13.2.jar:git-Paper-657]
        at org.bukkit.craftbukkit.v1_13_R2.util.Commodore.convert(Commodore.java:170) ~[patched_1.13.2.jar:git-Paper-657]
        at org.bukkit.craftbukkit.v1_13_R2.util.CraftMagicNumbers.processClass(CraftMagicNumbers.java:238) ~[patched_1.13.2.jar:git-Paper-657]
        at org.bukkit.plugin.java.PluginClassLoader.findClass(PluginClassLoader.java:113) ~[patched_1.13.2.jar:git-Paper-657]
        at org.bukkit.plugin.java.PluginClassLoader.findClass(PluginClassLoader.java:86) ~[patched_1.13.2.jar:git-Paper-657]
        at java.lang.ClassLoader.loadClass(ClassLoader.java:587) ~[?:?]
        at java.lang.ClassLoader.loadClass(ClassLoader.java:520) ~[?:?]
        at java.lang.Class.forName0(Native Method) ~[?:?]
        at java.lang.Class.forName(Class.java:467) ~[?:?]
        at org.bukkit.plugin.java.PluginClassLoader.<init>(PluginClassLoader.java:64) ~[patched_1.13.2.jar:git-Paper-657]
        at org.bukkit.plugin.java.JavaPluginLoader.loadPlugin(JavaPluginLoader.java:134) ~[patched_1.13.2.jar:git-Paper-657]
        at org.bukkit.plugin.SimplePluginManager.loadPlugin(SimplePluginManager.java:334) ~[patched_1.13.2.jar:git-Paper-657]
        at org.bukkit.plugin.SimplePluginManager.loadPlugins(SimplePluginManager.java:255) ~[patched_1.13.2.jar:git-Paper-657]
        at org.bukkit.craftbukkit.v1_13_R2.CraftServer.loadPlugins(CraftServer.java:331) ~[patched_1.13.2.jar:git-Paper-657]
        at net.minecraft.server.v1_13_R2.DedicatedServer.init(DedicatedServer.java:235) ~[patched_1.13.2.jar:git-Paper-657]
        at net.minecraft.server.v1_13_R2.MinecraftServer.run(MinecraftServer.java:787) ~[patched_1.13.2.jar:git-Paper-657]
        at java.lang.Thread.run(Thread.java:833) [?:?]
```
