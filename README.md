<h2 align="center">
<br>
  <img src="images/cmdapi.svg" alt="CommandAPI logo" width="600">
  <br>
    <br>
  A Bukkit/Spigot API to use the command UI introduced in Minecraft 1.13
  <br>
</h2>

<div align="center">

![GitHub](https://img.shields.io/github/license/JorelAli/CommandAPI)
[![CodeFactor](https://www.codefactor.io/repository/github/jorelali/commandapi/badge)](https://www.codefactor.io/repository/github/jorelali/commandapi)
[![](https://jitpack.io/v/dev.jorel/CommandAPI.svg)](https://jitpack.io/#dev.jorel/CommandAPI)
[![Join us on Discord](https://img.shields.io/discord/745416925924032523.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.gg/G4SzSxZ)
<br>
![Spiget Download Size](https://img.shields.io/spiget/download-size/62353)
![Spiget Downloads](https://img.shields.io/spiget/downloads/62353?label=Total%20Spigot%20Downloads)
![GitHub all releases](https://img.shields.io/github/downloads/JorelAli/CommandAPI/total?label=Total%20GitHub%20Downloads)
</div>

**Support and Project Discussion**:

- <img width="20px" src="https://cdn3.iconfinder.com/data/icons/popular-services-brands-vol-2/512/discord-512.png"></img> [Discord](https://discord.gg/G4SzSxZ)
- <img width="20px" src="https://static.spigotmc.org/img/spigot.png"></img> [Spigot page](https://www.spigotmc.org/resources/commandapi.62353/) 

**Downloads & Documentation:**

- <img width="20px" src="https://icon-icons.com/icons2/2348/PNG/512/download_arrow_icon_143023.png"></img> [All downloads](https://github.com/JorelAli/CommandAPI/releases)
- <img width="20px" src="https://icon-icons.com/icons2/2348/PNG/512/books_icon_143050.png"></img> [Documentation (includes usage for server owners!)](https://www.jorel.dev/CommandAPI/)

**Announcements:**

**Compatible Minecraft versions:**

The list of what version of the CommandAPI you'll need to run on a specific version of Minecraft is as follows:

- **Minecraft 1.13.x**: CommandAPI v1.0 to 5.12
- **Minecraft 1.14.1, 1.14.2**: CommandAPI v2.0 to 5.12
- **Minecraft 1.14.3, 1.14.4**: CommandAPI v2.1 to 5.12
- **Minecraft 1.15.x**: CommandAPI v2.3a to 5.12
- **Minecraft 1.16.1**: CommandAPI v3.0 to 5.12
- **Minecraft 1.16.2**: CommandAPI v4.0 to 5.12
- **Minecraft 1.16.3**: CommandAPI v4.2 to 5.12
- **Minecraft 1.16.4**: CommandAPI v5.2 to 5.12
- **Minecraft 1.16.5**: CommandAPI v5.7 to 6.1.x
- **Minecraft 1.17**: CommandAPI 6.0.x to 6.3.x
- **Minecraft 1.17.1**: CommandAPI 6.1.x to 6.3.x

-----

## Purpose

This project provides an API to help Bukkit/Spigot developers use the Minecraft 1.13 command UI, which includes:

* **Better commands** - Prevent players from running invalid commands, making it easier for developers

  ![better commands](./images/printnumber.gif)

* **Better arguments** - Easily switch from Location arguments to raw JSON, fully supported with built-in error checking

  ![better arguments](./images/explode.gif)

* **Support for proxied command senders** - Run your command as other entities using `/execute as ... run command`

  ![proxied senders](./images/selfdestruct.gif)
  
* **Argument tooltips** - Let your users know exactly what their command will do

  ![argument tooltips](./docssrc/src/images/warps.gif)

* **Support for the `/execute` command** - Let your command to be executed by the built in `/execute` command

* **Support for Minecraft's functions** - Allow your command to be executed from Minecraft's functions and tags

* **No plugin.yml registration** - Commands don't need to be registered in the `plugin.yml` file anymore

* **You don't need Brigadier** - You don't need to import Brigadier in your projects to use the CommandAPI

* **No tracking** - The CommandAPI don't collect any stats about its plugin; what you see is what you get!

In addition to all of the above:

- **Built-in command converter** - Convert regular plugin commands into `/execute`-compatible ones - no coding experience required!
- **Optional compile-time annotation-based framework** - Don't like writing lots of code with builders? You don't have to if you don't want to!
- [**Insanely detailed documentation**](https://www.jorel.dev/CommandAPI/) - Trust me, you've never seen a plugin documentation look so good.

-----

## Code examples

_(I always like to see code examples on GitHub repos, so here's what CommandAPI code looks like):_

<details>
	<summary><b>Simple command registration</b></summary>
  
```java
new CommandAPICommand("enchantitem")
    .withArguments(new EnchantmentArgument("enchantment"))
    .withArguments(new IntegerArgument("level", 1, 5))
    .executesPlayer((player, args) -> {
        Enchantment enchantment = (Enchantment) args[0];
        int level = (int) args[1];
        
        //Add the enchantment
        player.getInventory().getItemInMainHand().addEnchantment(enchantment, level);
    })
    .register();
```
  
</details>

<details>
	<summary><b>Potion removing, suggesting potions that a player has currently</b></summary>
  
```java
List<Argument> arguments = new ArrayList<>();
arguments.add(new EntitySelectorArgument("target", EntitySelector.ONE_PLAYER));
arguments.add(new PotionEffectArgument("potioneffect").safeOverrideSuggestions(
    (sender, prevArgs) -> {
        Player target = (Player) prevArgs[0];
        
        //Convert PotionEffect[] into PotionEffectType[]
        return target.getActivePotionEffects().stream()
            .map(PotionEffect::getType)
            .toArray(PotionEffectType[]::new);
    })
);

new CommandAPICommand("removeeffect")
    .withArguments(arguments)
    .executesPlayer((player, args) -> {
        EntityType entityType = (EntityType) args[0];
        player.getWorld().spawnEntity(player.getLocation(), entityType);
    })
    .register();
```
  
</details>

<details>
	<summary><b>Subcommands</b></summary>
  
```java
new CommandAPICommand("perm")
    .withSubcommand(new CommandAPICommand("group")
        .withSubcommand(new CommandAPICommand("add")
            .withArguments(new StringArgument("permission"))
            .withArguments(new StringArgument("groupName"))
            .executes((sender, args) -> {
                //perm group add code
            })
        )
        .withSubcommand(new CommandAPICommand("remove")
            .withArguments(new StringArgument("permission"))
            .withArguments(new StringArgument("groupName"))
            .executes((sender, args) -> {
                //perm group remove code
            })
        )
    )
    .withSubcommand(new CommandAPICommand("user")
        .withSubcommand(new CommandAPICommand("add")
            .withArguments(new StringArgument("permission"))
            .withArguments(new StringArgument("userName"))
            .executes((sender, args) -> {
                //perm user add code
            })
        )
        .withSubcommand(new CommandAPICommand("remove")
            .withArguments(new StringArgument("permission"))
            .withArguments(new StringArgument("userName"))
            .executes((sender, args) -> {
                //perm user remove code
            })
        )
    )
    .register();
```
  
</details>

<details>
	<summary><b>Annotation-based commands</b></summary>
  
```java
@Command("warp")	
public class WarpCommand {
    
    // List of warp names and their locations
    static Map<String, Location> warps = new HashMap<>();
    
    @Default
    public static void warp(CommandSender sender) {
        sender.sendMessage("--- Warp help ---");
        sender.sendMessage("/warp - Show this help");
        sender.sendMessage("/warp <warp> - Teleport to <warp>");
        sender.sendMessage("/warp create <warpname> - Creates a warp at your current location");
    }
    
    @Default
    public static void warp(Player player, @AStringArgument String warpName) {
        player.teleport(warps.get(warpName));
    }
    
    @Subcommand("create")
    @Permission("warps.create")
    public static void createWarp(Player player, @AStringArgument String warpName) {
        warps.put(warpName, player.getLocation());
    }
    
}
```
  
</details>

<details>
	<summary><b>Command conversion (no compilation required)</b></summary>
  
```yml
plugins-to-convert:
  - Essentials:
    - speed <speed>[0..10]
    - speed <target>[minecraft:game_profile]
    - speed (walk|fly) <speed>[0..10]
    - speed (walk|fly) <speed>[0..10] <target>[minecraft:game_profile]
```
  
</details>

-----

## Building the CommandAPI

The CommandAPI can be built easily, but requires copies of the Spigot server jars to be present locally on your machine in order to be compatible with any Minecraft version. The CommandAPI is built using the Maven build tool - if you don't have it, you can download it [here](https://maven.apache.org/download.cgi).

- Clone the repository using your preferred method, or with the command below:

  ```
  git clone https://github.com/JorelAli/CommandAPI.git
  ```

- Go into the folder named `CommandAPI` _(Not to be confused with the folder named `CommandAPI`, which is what was just cloned)_. You want the folder which contains `pom.xml`.

- Run `mvn`

The resulting plugin `.jar` is found in `commandapi-plugin/target/CommandAPIvX.X.X_DATE.jar`

## Building the CommandAPI's documentation

The CommandAPI's documentation is built using a custom version of [mdBook](https://rust-lang.github.io/mdBook/), a command line tool to create "books" with Markdown. This custom version can be found [in my mdBook fork](https://github.com/JorelAli/mdBook/releases).

- (Optional): Build the CommandAPI first, using the instructions above. The documentation pulls information directly from tests in the source code, so it assumes that those tests compile correctly!

- Get a copy of mdbook fork executable.
  - On Windows, download `mdbook-fa5.exe` from the [mdBook FA5 support](https://github.com/JorelAli/mdBook/releases/tag/v0.4.9) release
  - On Linux:
    - Clone my [mdBook fork](https://github.com/JorelAli/mdBook) using your preferred method, or with the command below:

      ```
      git clone https://github.com/JorelAli/mdBook.git
      ```

    - Use git to checkout to the `fa5` branch, using the following command:

      ```
      git checkout fa5
      ```

    - Build the executable with `cargo` (can be installed using `sudo apt-get install cargo` on Ubuntu distros), using the following command:

      ```
      cargo build
      ```

    - Grab the executable `mdbook` from `mdBook/target/debug/`

- Navigate to the `docssrc` folder

- Run `mdbook-fa5.exe build` (or `mdbook build` on Linux)

The resulting compiled documentation is found in `docs/X.X.X`, where `X.X.X` is specified in the `book.toml` file's `build-dir` in the `docssrc` folder.

-----

## CommandAPI Project Timeline

This is the current roadmap for the CommandAPI (as of 16th May 2021):
  
- **CommandAPI 7.0.0:** Annotation improvements

  The CommandAPI's annotation system has always been a bit limited and was primarily introduced as a proof-of-concept. In this update, the CommandAPI's annotation system will be improved to be (ideally) as powerful as the non-annotation system and have slightly better type safety.

- **Future:**

  **CustomArgument improvements**

  The CustomArgument class is fairly flexible, but nowhere near flexible enough. In this update, more attention will be focused on the CustomArgument class to provide it the ability to extend from all other argument types as a base.

  **Argument conflict detection**

  The CommandAPI simply uses the Brigadier system under the hood. This system is prone to _argument conflicts_, which is where certain arguments are given priority over other arguments. (For example "hello" and "123" are both valid string arguments, but if you have a command that has a string argument or an integer argument, Brigadier may ignore the integer argument). In this update, the CommandAPI will try to spot potential conflicts and add a warning in the console. The research required for this is also required in order to implement optional arguments (which is not coming out in this release).

-----

## Changelog

<table width="100%">
    <thead>
        <tr>
            <th width="10%">Version</th>
            <th width="15%">Date</th>
            <th width="65%">Features / Changes</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td valign="top"><b>6.3.1</b></td>
            <td valign="top">September 2021</td>
            <td valign="top">
                <ul>
                    <li>Fixes issue with converted commands where executing as a player with lower permissions fails</li>
                    <li>Adds very limited support for plugin reloading by unregistering commands on disable</li>
                    <li>Fixes issue where converted commands with greedy string arguments would almost always fail</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>6.3.0</b></td>
            <td valign="top">August 2021</td>
            <td valign="top">
                <ul>
                    <li>Adds a new constructor to <code>CustomArgument</code> which takes in a record containing all inputs</li>
                    <li>Adds support for <code>CustomArgument</code>'s parser to use previously declared arguments</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>6.2.0</b></td>
            <td valign="top">July 2021</td>
            <td valign="top">
                <ul>
                    <li>Adds config option to customize messages</li>
                    <li>Adds config option to use the latest NMS version</li>
                    <li>Update documentation instructions for shading with Maven</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>6.1.0</b></td>
            <td valign="top">July 2021</td>
            <td valign="top">
                <ul>
                    <li>Adds support for 1.17.1</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>6.0.5</b></td>
            <td valign="top">June 2021</td>
            <td valign="top">
                <ul>
                    <li>Fix issue where converted commands which didn't use entity selectors would always fail</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>6.0.4</b></td>
            <td valign="top">June 2021</td>
            <td valign="top">
                <ul>
                    <li>Fix issue where some multi literal arguments would be skipped, causing a crash</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>6.0.3</b></td>
            <td valign="top">June 2021</td>
            <td valign="top">
                <ul>
                    <li>Fix issue where custom CommandSender subclasses could not run CommandAPI commands</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>6.0.2</b></td>
            <td valign="top">June 2021</td>
            <td valign="top">
                <ul>
                    <li>Fix bug where multi literal arguments would crash due to poor array preservation</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>6.0.1</b></td>
            <td valign="top">June 2021</td>
            <td valign="top">
                <ul>
                    <li>Fix bug where the CommandAPI would crash if it tries to register duplicate permissions</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>6.0.0</b></td>
            <td valign="top">June 2021</td>
            <td valign="top">
                <b>Version support changes:</b>
                <ul>
                    <li>Adds support for Minecraft 1.17</li>
                    <li>Drops support for Minecraft 1.16.4 and below</li>
                    <li>Changes build version from Java 8 to Java 16</li>
                </ul>
                <b>Development improvements:</b>
                <ul>
                    <li>Switches version convention to use Semanic Versioning</li>
                    <li>Uses CodeFactor.io for code quality checking</li>
                </ul>
                <b>New features:</b>
                <ul>
                    <li>Adds <code>OfflinePlayerArgument</code> for offline players</li>
                    <li>Adds a way to add suggestions to existing vanilla suggestions</li>
                    <li>Adds a way to access the <code>CommandSender</code> for <code>CustomArgument</code> parsing</li>
                    <li>Adds support for Paper's console tab-completion</li>
                    <li>Adds a way to completely silence all CommandAPI logs</li>
                    <li>Adds access to the current input and current argument input for argument suggestions</li>
                    <li>Improve API for setting configuration for plugins that shade the CommandAPI</li>
                </ul>
                <b>Bug fixes:</b>
                <ul>
                    <li>Fixes bug with converted commands crashing due to poor interface proxying</li>
                    <li>Adds a way to access the <code>CommandSender</code> for <code>CustomArgument</code> parsing</li>
                    <li>Adds support for Paper's console tab-completion</li>
                    <li>Adds a way to completely silence all CommandAPI logs</li>
                    <li>Fix bugs where the NBTAPI wouldn't be hooked into properly</li>
                    <li>Fixes critical issue where converted commands with entity selectors may sometimes just not run</li>
                </ul>
                <b>Other:</b>
                <ul>
                    <li>Improves overall performance</li>
                    <li>Improves performance for the <code>PotionArgument</code></li>
                    <li>Improves performance for the <code>MathOperationArgument</code></li>
                    <li>Fixes spacing issues with code blocks in the documentation</li>
                    <li>Fixes invalid code examples in the documentation</li>
                    <li>Fixes typos in the documentation</li>
                    <li>Adds syntax highlighting for command code blocks in the documentation</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>5.12</b></td>
            <td valign="top">May 2021</td>
            <td valign="top">
                <ul>
                    <li>Moves the Maven repo for 5.12 and future updates to jitpack.io</li>
                    <li>Fixes issue with sound arguments on Minecraft 1.16.4 and 1.16.5</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>5.11</b></td>
            <td valign="top">May 2021</td>
            <td valign="top">
                <ul>
                    <li>Allows converted commands to use entity selectors in plugin commands</li>
                    <li>Allows arbitrary commands to be converted with the CommandAPI's converter system</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>5.10</b></td>
            <td valign="top">May 2021</td>
            <td valign="top">
                <ul>
                    <li>Adds support for Paper's Adventure API for ChatComponent and Chat arguments.</li>
                    <li>Deprecated a few methods in favour of some slightly better ones.</li>
                    <li>Update proxied sender for Spigot 1.16.5</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>5.9</b></td>
            <td valign="top">February 2021</td>
            <td valign="top">
                <ul>
                    <li>Fixed a critical bug where plugin conversion would run the caller methods instead of callee methods, which prevented command blocks from running commands.</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>5.8</b></td>
            <td valign="top">January 2021</td>
            <td valign="top">
                <ul>
                    <li>Removed a debug <code>/test</code> command which wasn't supposed to be released!</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>5.7</b></td>
            <td valign="top">January 2021</td>
            <td valign="top">
                <ul>
                    <li>Add support for Minecraft 1.16.5</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>5.6</b></td>
            <td valign="top">January 2021</td>
            <td valign="top">
                <ul>
                    <li>Fix bug where plugins that use <a href="https://github.com/aikar/commands">Aikar's ACF</a> were incompatible with the CommandAPI</li>
                    <li>Add a new configuration option <code>skip-sender-proxy</code> which prevents certain plugins from running properly</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>5.5</b></td>
            <td valign="top">January 2021</td>
            <td valign="top">
                <ul>
                    <li>Fix bug with annotations where <code>@NeedsOp</code> didn't work if placed on a class</li>
                    <li>Fix bug where entity selector arguments with <code>@</code> selectors return empty values if the sender is not op</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>5.4</b></td>
            <td valign="top">December 2020</td>
            <td valign="top">
                <ul>
                    <li>Fix bug where the NBT-API wasn't compatible with the CommandAPI when both are shaded into a plugin</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>5.3</b></td>
            <td valign="top">November 2020</td>
            <td valign="top">
                <ul>
                    <li>Fix bug where permissions weren't being applied for subcommands and multi literal arguments</li>
                    <li>Adds detection system for command graph conflicts</li>
                    <li>Adds a way to "negate" permissions using <code>.withoutPermission</code></li>
                    <li>Adds an annotation-based command framework</li>
                    <li>Fix minor documentation inaccuracies</li>
                    <li>Fix bug where converted commands didn't apply multiple parameters</li>
					<li>The fields in <code>CommandAPICommand</code> can now be accessed via getters and setters</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>5.2</b></td>
            <td valign="top">November 2020</td>
            <td valign="top">
                <ul>
                    <li>Adds <code>CommandAPI.reloadDatapacks()</code> method to reload datapacks in the same way the CommandAPI does</li>
                    <li>Adds support for Minecraft 1.16.4</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>5.1</b></td>
            <td valign="top">October 2020</td>
            <td valign="top">
                <ul>
                    <li>Fixes bug where converted commands could not be executed by players ingame</li>
                    <li>Adds <code>withPermission(String)</code> to arguments and CommandAPICommands</li>
                    <li>Adds SimpleFunctionWrapper with helper methods to get functions and tags from ingame, as well as run them without needing to parse them via commands</li>
                    <li>Greatly improve the type-safety of the internal CommandAPI code</li>
                    <li>Move the <code>Brigadier</code> class outside of the <code>CommandAPIHandler</code> class</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>5.0</b></td>
            <td valign="top">October 2020</td>
            <td valign="top">
                <ul>
                    <li><b>Note: This version is incompatible with any plugin that used the CommandAPI version 4.3c or below! (See documentation for more information)</b></li>
                    <li>API improvements:</code>
                    <ul>
                        <li>The <code>.withArguments</code> method can now take varargs</li>
                        <li>String tooltips are now much easier to implement for custom objects using <code>IStringTooltip</code></li>
                        <li>Removes <code>LinkedHashMap</code> for argument registration in favour of <code>List</code></li>
                    </ul>
                    <li>Adds subcommands</li>
                    <li>Adds <code>AngleArgument</code></li>
                    <li>Arguments can now be omitted from the <code>Object[] args</code> using the method <code>.setListed()</code>. This means Literal arguments can now be "present" in the arguments if desired.</li>
                    <li>Remove lots of reflection calls, so start up should be a little faster</li>
                    <li>Bug fixes:</li>
                    <ul>
                        <li>Fixes bug where verbose logging of permission linking was inaccurate</li>
                        <li>Fixes bug where overriding suggestions can break when generating suggestions</li>
                        <li>Fixes bug where <code>null</code> could appear in the suggestions list of arguments</li>
                        <li>CommandAPI's non-verbose logging is now actually quiet</li>
                        <li>Fixes bug where converted commands couldn't be run from the console</li>
                        <li>Fixes bug where <code>LongArgument</code> wouldn't let you use long values as minimum or maximum</li>
                    </ul>
                    <li>Command conversion improvements:</li>
                    <ul>
                        <li>The <code>Converter.convert()</code> method can now take varargs for arguments</li>
                        <li>Command conversion code that was specific to the CommandAPI plugin is no longer included in the shaded version of the CommandAPI</li>
                        <li>Command conversion in the configuration for server owners can now let server owners apply their own command argument implementations!</li>
                    </ul>
                    <li>Documentation improvements:</li>
                    <ul>
                        <li>Documentation code examples are now guaranteed to compile</li>
                        <li>The list of CommandAPI arguments to Minecraft argument IDs is now in the documentation</li>
                    </ul>
                    <li>CommandAPI-Brigadier improvements:</li>
                    <ul>
                        <li>Adds <code>toSuggestions()</code> to the CommandAPI-Brigadier library to convert CommandAPI suggestions into Brigadier's <code>SuggestionProvider</code></li>
                        <li>CommandAPI-Brigadier library methods got renamed</li>
                        <li>Changed the way literal arguments are constructed in the CommandAPI-Brigadier library - they are no longer unnecessarily registered into the command graph</li>
                    </ul>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>4.3c</b></td>
            <td valign="top">October 2020</td>
            <td valign="top">
                <ul>
                    <li>Fixes bug where function loading would break because permissions could not be properly computed</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>4.3b</b></td>
            <td valign="top">September 2020</td>
            <td valign="top">
                <ul>
                    <li>Fixes minor command sender related bugs from 4.3a. Fixes permissions with <code>/execute ... as ...</code> from converted commands</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>4.3a</b></td>
            <td valign="top">September 2020</td>
            <td valign="top">
                <ul>
                    <li>Fixes a bug where running converted commands via <code>/execute ... as ...</code> wouldn't apply the command sender correctly</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>4.3</b></td>
            <td valign="top">September 2020</td>
            <td valign="top">
                <ul>
                    <li>Fix bug where resulting command executors with command block senders would not work</li>
                    <li>Improves the power of command conversion by letting you declare CommandAPICommand arguments for conversion</li>
                    <li>Adds support for YAML's "null" for command conversion via the config.yml file, which should be way more comprehensible rather than trailing colons</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>4.2</b></td>
            <td valign="top">September 2020</td>
            <td valign="top">
                <ul>
                    <li>Adds support for Minecraft 1.16.3</li>
                    <li>Fixes a bug where shading the CommandAPI and the NBT-API together causes the CommandAPI to incorrectly think that the NBT-API isn't present</li>
                    <li>Fixes a bug where commands with redirects (4.0+ aliases and redirects from /execute) that have two consecutive arguments with suggestions would spam the console and not provide suggestions</li>
					<li>Adds <code>NativeProxyCommandSender</code> which lets you access the location and world of a command sender via <code>/execute in|positioned|at|facing|rotated</code></li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>4.1</b></td>
            <td valign="top">September 2020</td>
            <td valign="top">
                <ul>
                    <li>Allows the CommandAPI to be shaded into plugins</li>
                    <li>Adds a way to set hover tooltips for suggestions</li>
                    <li>Adds multi-literal arguments</li>
                    <li>Adds a logo!</li>
                    <li>Adds a new method to the CommandAPI/Brigadier system to easily create Brigadier arguments from CommandAPI arguments</li>
                    <li><b>Rename maven modules</b> You can view more information about this on the <a href="https://github.com/JorelAli/CommandAPI/tree/mvn-repo">public maven repository</a></li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>4.0</b></td>
            <td valign="top">August 2020</td>
            <td valign="top">
                <ul>
                    <li>Suggestion overriding can now be populated by Bukkit objects instead of strings</li>
                    <li>Fixes a bug with the <code>FloatRangeArgument</code> where it caused a casting error</li>
                    <li>Adds support for 1.16.2
                        <ul>
                            <li><code>ChatArgument</code> now works on Minecraft 1.16.2 (still doesn't work on 1.16.1)</li>
                        </ul>
                    </li>
                    <li>Adds new arguments:
                        <ul>
                            <li><code>UUIDArgument</code></li>
                            <li><code>ItemStackPredicateArgument</code></li>
                            <li><code>BlockPredicateArgument</code></li>
                        </ul>
                    </li>
                    <li>Fix bug where <code>CustomArgument</code>s break when using the namespaced key flag</li>
                    <li>Adds a list of commands that <code>FunctionWrapper</code> executes which is now accessible</li>
                    <li>Command aliases are now much more efficient </li>
                    <li>Documentation changes (briefly):
                        <ul>
                            <li><code>BlockStateArgument</code> is now documented properly</li>
                            <li>Documentation now has pictures to show you what arguments look like</li>
                            <li>Documentation now has a page dedicated to what doesn't work on what Minecraft version</li>
                        </ul>
                    </li>
                    <li>Adds Brigadier support for developers (lets you use the CommandAPI and Brigadier code side by side!)</li>
                    <li>Fixes a bug where Java 12+ had incompatibility issues</li>
                    <li>Adds support for setting arbitrary requirements to arguments and commands</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>3.4</b></td>
            <td valign="top">July 2020</td>
            <td valign="top">
                <ul>
                    <li>Fix bug with custom recipes not registering in Minecraft 1.16+</li>
                    <li>Fix bug where command conversion didn't actually register commands</li>
                    <li>Adds command conversion as a built-in feature via the CommandAPI's <code>config.yml</code></li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>3.3</b></td>
            <td valign="top">July 2020</td>
            <td valign="top">
                <ul>
                    <li>Fixes a bug where functions didn't work in Minecraft 1.16+</li>
                    <li>Fixes a bug where spigot produces a warning about api-versions</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>3.2</b></td>
            <td valign="top">July 2020</td>
            <td valign="top">
                <ul>
                    <li>Fixes a bug with <code>.overrideSuggestions()</code> from version 3.1</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>3.1</b></td>
            <td valign="top">July 2020</td>
            <td valign="top">
                <ul>
                    <li>Fixes bug where command senders didn't work properly, causing commands to not work properly</li>
                    <li>Adds the ability to override suggestions with the information of previously declared argument</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>3.0</b></td>
            <td valign="top">June 2020</td>
            <td valign="top">
                <ul>
                    <li><b>Note: This version is incompatible with pre 3.0 versions CommandAPI plugins (See documentation for more information)</b></li>
                    <li>Complete code refactor to make command syntax slightly more intuitive and consistent</li>
                    <li>Removes lots of reflection to improve performance</li>
                    <li>Adds better documentation</li>
                    <li>Adds JavaDocs</li>
                    <li>Adds support for 1.16.1</li>
                    <li>Adds new command executors (These let you filter commands based on what type of command executor runs the command):
                        <ul>
                            <li>Player command executors</li>
                            <li>Command block command executors</li>
                            <li>Console command executors</li>
                            <li>Entity command executors</li>
                            <li>Proxied command executors</li>
                        </ul>
                    </li>
                    <li>Adds new arguments:
                        <ul>
                            <li>Axis Argument</li>
                            <li>Biome Argument</li>
                            <li>ChatColor Argument</li>
                            <li>Chat Argument</li>
                            <li>FloatRange Argument</li>
                            <li>IntegerRange Argument</li>
                            <li>Location2D Argument</li>
                            <li>MathOperation Argument</li>
                            <li>NBT Argument (NBTAPI required)</li>
                            <li>Scoreboard arguments:
                                <ul>
                                    <li>Objective Argument</li>
                                    <li>ObjectiveCriteria Argument</li>
                                    <li>ScoreboardSlot Argument</li>
                                    <li>ScoreHolder Argument</li>
                                    <li>Team Argument</li>
                                </ul>
                            </li>
                            <li>Time Argument</li>
                            <li>Rotation Argument</li>
                            <li>Environment Argument</li>
                            <li>Removes old arguments:
                                <ul>
                                    <li>SuggestedStringArgument</li>
                                    <li>DynamicSuggestedStringArgument</li>
                                    <li>DefinedCustomArguments</li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>2.3a</b></td>
            <td valign="top">December 2019</td>
            <td valign="top">
                <ul>
                    <li>Adds support for Minecraft 1.15, 1.15.1 and 1.15.2</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>2.3</b></td>
            <td valign="top">August 2019</td>
            <td valign="top">
                <ul>
                    <li>Fixes bug where permissions didn't work</li>
                    <li>Fixes bug where functions weren't working on 1.14.3 and 1.14.4</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>2.2</b></td>
            <td valign="top">July 2019</td>
            <td valign="top">
                <ul>
                    <li>Adds support for Minecraft 1.13 and 1.13.1 <i>(Funny isn't it? It's called the 1.13 CommandAPI but never supported Minecraft 1.13 until now)</i></li>
                    <li>Improves support for different versions</li>
                    <li>Adds pointless witty comments into changelog notes</li>
                    <li>Adds <a href="https://github.com/JorelAli/1.13-Command-API-SafeReflection">1.13-Command-API-SafeReflection</a> library to greatly improve reliability of reflection calls</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>2.1</b></td>
            <td valign="top">July 2019</td>
            <td valign="top">
                <ul>
                    <li>Adds RecipeArgument</li>
                    <li>Adds SoundArgument</li>
                    <li>Adds AdvancementArgument</li>
                    <li>Adds LootTableArgument</li>
                    <li>Adds support for 1.14.3 and 1.14.4</li>
                    <li>Fixes bug where aliases weren't registering properly (<a href="https://github.com/JorelAli/CommandAPI/issues/43">#43</a>)</li>
                    <li>Fix documentation for tooltips</li>
                    <li>Improve documentation for dependencies and repositories</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>2.0.1</b></td>
            <td valign="top">May 2019</td>
            <td valign="top">
                <ul>
                    <li>Fix a bug where Brigadier was required as a dependency to build plugins</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>2.0</b></td>
            <td valign="top">May 2019</td>
            <td valign="top">
                <ul>
                    <li>Compatibility for 1.14</li>
                    <li>Major overhaul of the CommandAPI's internals - greatly improves performance</li>
                    <li>Deprecates SuggestedStringArgument, adding overrideSuggestions as an alternative for any argument type </li>
                    <li>Adds CustomArguments, allowing you to create your own ... custom arguments</li>
                    <li>Excludes dependencies from final jar (<a href="https://github.com/JorelAli/CommandAPI/issues/40">#40</a>)</li>
                    <li>Adds DefinedCustomArguments - CustomArguments that have been created by yours truly</li>
                    <li>DynamicSuggestedArguments now have access to the CommandSender (<a href="https://github.com/JorelAli/CommandAPI/issues/41">#41</a>)</li>
                    <li>Adds Loot Table support</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>1.8.2</b></td>
            <td valign="top">January 2019</td>
            <td valign="top">
                <ul>
                    <li>Fix bug with PlayerArgument when player cannot be found</li>
                    <li>Adds LocationArgument options for block precision or exact precision</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>1.8.1</b></td>
            <td valign="top">December 2018</td>
            <td valign="top">
                <ul>
                    <li>Fix permissions for argument from 1.8</li>
                    <li>Neaten up logging with verbose outputs</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>1.8</b></td>
            <td valign="top">December 2018</td>
            <td valign="top">
                <ul>
                    <li>Fix bugs where DynamicSuggestedArguments don't work as the last argument</li>
                    <li>Fix support for latest spigot version</li>
                    <li>Adds permissions for arguments</li>
                    <li>Adds support to override suggestions for arguments</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>1.7.2</b></td>
            <td valign="top">December 2018</td>
            <td valign="top">
                <ul>
                    <li>Fix a bug where default return value was 0 instead of 1, causing issues with commandblocks</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>1.7.1</b></td>
            <td valign="top">December 2018</td>
            <td valign="top">
                <ul>
                    <li>Fix a bug with permission checks. Other than that, it's the same as 1.7 (in terms of documentation)</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>1.7</b></td>
            <td valign="top">December 2018</td>
            <td valign="top">
                <ul>
                    <li>Adds DynamicSuggestedStringArguments for dynamically updating suggestions</li>
                    <li>Adds support for <code>success</code> and <code>result</code> values for <code>/execute store</code></li>
                    <li>Overhaul permissions system so it works properly</li>
                    <li><b>Note: This version is incompatible with pre-1.7 version CommandAPI plugins</b></li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>1.6</b></td>
            <td valign="top">November 2018</td>
            <td valign="top">
                <ul>
                    <li>Adds FunctionArguments to handle Minecraft functions</li>
                    <li>Remove useless test code</li>
                    <li>Fix bug with ProxiedCommandSender callee and caller</li>
                    <li>Adds Converter for legacy plugin support</li>
                    <li>Improved performance by caching NMS better than in version 1.5</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>1.5</b></td>
            <td valign="top">October 2018</td>
            <td valign="top">
                <ul>
                    <li>Adds ChatComponentArgument to handle raw JSON</li>
                    <li>Adds SuggestedStringArgument to suggest strings</li>
                    <li>Adds config file</li>
                    <li>Fix bug where command errors weren't being thrown</li>
                    <li>Improved performance by caching NMS</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>1.4</b></td>
            <td valign="top">October 2018</td>
            <td valign="top">
                <ul>
                    <li>Fix critical bug where arguments weren't being handled properly</li>
                    <li>Adds GreedyStringArgument</li>
                    <li>Adds various Exception classes</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>1.3</b></td>
            <td valign="top">October 2018</td>
            <td valign="top">
                <ul>
                    <li>Migrate to Maven</li>
                    <li>Remove unnecessary reflection</li>
                    <li>Adds EntitySelectorArgument</li>
                    <li>Adds LiteralArgument</li>
                    <li>Adds support for ProxiedCommandSender</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>1.2</b></td>
            <td valign="top">August 2018</td>
            <td valign="top">
                <ul>
                    <li>Adds TextArgument</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>1.1</b></td>
            <td valign="top">August 2018</td>
            <td valign="top">
                <ul>
                    <li>Adds PlayerArgument</li>
                    <li>Adds ParticleArgument</li>
                    <li>Adds ChatColorArgument</li>
                    <li>Adds EnchantmentArgument</li>
                    <li>Adds LocationArgument</li>
                    <li>Adds EntityTypeArgument</li>
                    <li>Adds permissions support</li>
                    <li>Adds alias support</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td valign="top"><b>1.0</b></td>
            <td valign="top">August 2018</td>
            <td valign="top">
                <ul>
                    <li>Initial release</li>
                </ul>
            </td>
        </tr>
    </tbody>
</table>

