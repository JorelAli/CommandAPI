# Installation

Installing the CommandAPI is easy! Just download the latest `CommandAPI.jar` file using the button below and place it in your server's `plugins` folder!

<br>

<a href="https://github.com/JorelAli/1.13-Command-API/releases/latest/download/CommandAPI.jar" style="
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
margin-right: auto;">Download latest CommandAPI.jar</a>

-----

## Additional dependencies

- If you use NBT data, you'll also need the [NBT API](https://www.spigotmc.org/resources/nbt-api.7939/). (you can find out from your developers if you need this or not)
- If you are using raw JSON chat data, you'll need to be running [Spigot](https://www.spigotmc.org/wiki/about-spigot/) or another spigot-related server such as [Paper Spigot](https://papermc.io/) or [Taco Spigot](https://tacospigot.github.io/). (Again, you can find out from your developers if you need this or not)

-----

## Configuring the CommandAPI

The CommandAPI can be configured in the `plugins/CommandAPI/config.yml` file.

The default `config.yml` settings are as follows:

```yaml
verbose-outputs: true
create-dispatcher-json: false
```

* `verbose-outputs` - Outputs command registration and unregistration logs in the console
* `create-dispatcher-json` - Creates a `command_registration.json` file showing the mapping of registered commands
