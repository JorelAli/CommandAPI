# Chat arguments

The CommandAPI provides a number of ways to interact with chat formatting in Minecraft. These are the following:

- **ChatColor**: The color of text rendered in Minecraft
- **Chat**: Text which is said in chat. This also includes entity selectors such as `@a` and `@r`
- **ChatComponent**: Minecraft's [Raw JSON text format](https://minecraft.wiki/w/Raw_JSON_text_format)

The CommandAPI implements **ChatColor**, **Chat** and **ChatComponent** in two separate ways: [Spigot-compatible](./argument_chat_spigot.md) and [Adventure-compatible](./argument_chat_adventure.md). The differences between these and how to use them are described in their own relevant pages.

The CommandAPI also supports Minecraft 1.19's chat preview feature. To use Minecraft 1.19's chat preview feature, information on that can be found in [Chat preview](./chatpreview.md).
