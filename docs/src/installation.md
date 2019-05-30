# Installation

Installing the CommandAPI is easy! Just download the CommandAPI.jar file and place it in your server's `plugins/` folder!

<br>

<a href="https://github.com/JorelAli/1.13-Command-API/releases/latest/download/CommandAPI.jar" style="
background-color:#EB7035;
border-radius:3px;
color:#ffffff;
display:block;
line-height:44px;
text-align:center;
width:50%;
margin-left:auto;
margin-right: auto;">Download latest CommandAPI.jar</a>

## Configuring the CommandAPI

The CommandAPI can be configured in the `plugins/CommandAPI/config.yml` file.

The default `config.yml` settings are as follows:

```yaml
verbose-outputs: true
create-dispatcher-json: true
```

* `verbose-outputs` - Outputs command registration and unregistration logs in the console
* `create-dispatcher-json` - Creates a `command_registration.json` file showing the mapping of registered commands
