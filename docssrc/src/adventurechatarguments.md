# Adventure chat arguments

> **Developer's Note:**
>
> The two following classes, `AdventureChatComponentArgument` and `AdventureChatArgument` depend on a PaperSpigot based server which has the Adventure library. If you use this class on a server without the Adventure library, it will throw a `PaperAdventureNotFoundException`

From PaperSpigot 1.16.5 build #473, it now includes [Kyori's Adventure API](https://github.com/KyoriPowered/adventure-platform). This library is a replacement of the BungeeCord chat API and has all of the same functionality as the BungeeCord chat API (and more!). The documentation for this API can be found [here](https://docs.adventure.kyori.net/index.html).

Since this functions very similar to the Spigot chat arguments, this page won't reiterate everything about how it works, we'll just outline some examples of how to use these arguments instead.

-----

## Adventure chat component argument

The `AdventureChatComponentArgument` class accepts raw chat-based JSON as valid input, as declared [here](https://minecraft.gamepedia.com/Raw_JSON_text_format). This is converted into Adventure's `Component` class.

TODO: An example here!

```java
new CommandAPICommand("ccpadventure").withArguments(new ChatArgument("test2"))
    .executes((sender, args) -> {
        Component message = (Component) args[0];
        
        //Broadcast the message to everyone on the server
        Bukkit.broadcast(message, "someperm");
    }).register();
```