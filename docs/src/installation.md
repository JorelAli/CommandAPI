# Installation (For server owners)

* Download the v1.8.2 CommandAPI.jar from the download page [here](https://github.com/JorelAli/1.13-Command-API/releases/tag/v1.8.2)
* Place the CommandAPI.jar file in your server's `plugins/` folder
* That's it!

## Configuring the CommandAPI

The CommandAPI can be configured in the `plugins/CommandAPI/config.yml` file.

The default `config.yml` settings are as follows:
 
```yaml
verbose-outputs: true
create-dispatcher-json: true
```

* `verbose-outputs` - Outputs command registration and unregistration logs in the console
* `create-dispatcher-json` - Creates a `command_registration.json` file showing the mapping of registered commands
