# Help

Help topics can be added to your command using the `withHelp()`, `withShortDescription()` or `withFullDescription()` methods when registering a command. Help allows users to understand what your command does and provides them with a list of usage forms to aid in writing a command.

## Parts of a help

A help topic consists of two mains parts:

- A short description which is displayed in a help list and displayed at the top of a help topic for that command
- A full description which is displayed in the "Description" section of a help topic

This can be seen with the following example, for a command `/mycmd`. This example has the short description _"Says hi"_, and a full description _"Broadcasts hi to everyone on the server"_. The short help is shown in the help list, which (in this example) is viewed using `/help 5`. The full description is shown for the help for the command on its own, which is viewed using `/help mycmd`:

![help image](./images/help.png)

## Help methods and semantics

The CommandAPI has three methods to register parts of a help. The `withShortDescription()` sets the short description for the command, the `withFullDescription()` sets the full description for the command and `withHelp()` is a simple way to set both the short and full description at the same time. The `withHelp()` method is the recommended method to use to set the help for a command.

If no short description is provided, the CommandAPI will attempt to use the full description if one is present. Note that this may be truncated automatically, so it is recommended to provide your own short description.