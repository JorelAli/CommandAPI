package dev.jorel.commandapi.examples.java;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandAPIPaperConfig;
import dev.jorel.commandapi.arguments.ChatArgument;
import dev.jorel.commandapi.arguments.ChatColorArgument;
import dev.jorel.commandapi.arguments.ChatComponentArgument;
import dev.jorel.commandapi.arguments.NBTCompoundArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Examples {

void argument_chatAdventure() {
/* ANCHOR: argumentChatAdventure1 */
new CommandAPICommand("namecolor")
    .withArguments(new ChatColorArgument("chatcolor"))
    .executesPlayer((player, args) -> {
        NamedTextColor color = (NamedTextColor) args.get("chatcolor");
        player.displayName(Component.text().color(color).append(Component.text(player.getName())).build());
    })
    .register();
/* ANCHOR_END: argumentChatAdventure1 */

/* ANCHOR: argumentChatAdventure2 */
new CommandAPICommand("showbook")
    .withArguments(new PlayerArgument("target"))
    .withArguments(new TextArgument("title"))
    .withArguments(new StringArgument("author"))
    .withArguments(new ChatComponentArgument("contents"))
    .executes((sender, args) -> {
        Player target = (Player) args.get("target");
        String title = (String) args.get("title");
        String author = (String) args.get("author");
        Component content = (Component) args.get("contents");

        // Create a book and show it to the user (Requires Paper)
        Book mybook = Book.book(Component.text(title), Component.text(author), content);
        target.openBook(mybook);
    })
    .register();
/* ANCHOR_END: argumentChatAdventure2 */

/* ANCHOR: argumentChatAdventure3 */
new CommandAPICommand("pbroadcast")
    .withArguments(new ChatArgument("message"))
    .executes((sender, args) -> {
        Component message = (Component) args.get("message");

        // Broadcast the message to everyone with broadcast permissions.
        Bukkit.getServer().broadcast(message, Server.BROADCAST_CHANNEL_USERS);
        Bukkit.getServer().broadcast(message);
    })
    .register();
/* ANCHOR_END: argumentChatAdventure3 */
}

class argument_nbt extends JavaPlugin {
/* ANCHOR: argumentNBT1 */
@Override
public void onLoad() {
    CommandAPI.onLoad(new CommandAPIPaperConfig(this)
        .initializeNBTAPI(NBTContainer.class, NBTContainer::new)
    );
}
/* ANCHOR_END: argumentNBT1 */

void argument_nbt2() {
/* ANCHOR: argumentNBT2 */
new CommandAPICommand("award")
    .withArguments(new NBTCompoundArgument<NBTContainer>("nbt"))
    .executes((sender, args) -> {
        NBTContainer nbt = (NBTContainer) args.get("nbt");

        // Do something with "nbt" here...
    })
    .register();
/* ANCHOR_END: argumentNBT2 */
}
}

void chatPreview() {
/* ANCHOR: chatPreview1 */
new CommandAPICommand("broadcast")
    .withArguments(new ChatArgument("message").withPreview(info -> {
        // Convert parsed Component to plain text
        String plainText = PlainTextComponentSerializer.plainText().serialize(info.parsedInput());

        // Translate the & in plain text and generate a new Component
        return LegacyComponentSerializer.legacyAmpersand().deserialize(plainText);
    }))
    .executesPlayer((player, args) -> {
        // The user still entered legacy text. We need to properly convert this
        // to a Component by converting to plain text then to Component
        String plainText = PlainTextComponentSerializer.plainText().serialize((Component) args.get("broadcast"));
        Bukkit.broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize(plainText));
    })
    .register();
/* ANCHOR_END: chatPreview1 */

/* ANCHOR: chatPreview2 */
new CommandAPICommand("broadcast")
    .withArguments(new ChatArgument("message").usePreview(true).withPreview(info -> {
        // Convert parsed Component to plain text
        String plainText = PlainTextComponentSerializer.plainText().serialize(info.parsedInput());

        // Translate the & in plain text and generate a new Component
        return LegacyComponentSerializer.legacyAmpersand().deserialize(plainText);
    }))
    .executesPlayer((player, args) -> {
        Bukkit.broadcast((Component) args.get("message"));
    })
    .register();
/* ANCHOR_END: chatPreview2 */
}

class setupShading {
JavaPlugin plugin = new JavaPlugin() {};

{
/* ANCHOR: setupShading1 */
CommandAPI.onLoad(new CommandAPIPaperConfig(plugin).silentLogs(true));
/* ANCHOR_END: setupShading1 */
}

/* ANCHOR: setupShading2 */
class MyPlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIPaperConfig(this).verboseOutput(true)); // Load with verbose output

        new CommandAPICommand("ping")
            .executes((sender, args) -> {
                sender.sendMessage("pong!");
            })
            .register();
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();

        // Register commands, listeners etc.
    }

    @Override
    public void onDisable() {
                CommandAPI.onDisable();
            }

}
/* ANCHOR_END: setupShading2 */
}

}
