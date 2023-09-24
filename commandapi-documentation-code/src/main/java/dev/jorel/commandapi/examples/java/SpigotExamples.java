package dev.jorel.commandapi.examples.java;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.spigot.ChatColorArgument;
import dev.jorel.commandapi.arguments.spigot.ChatComponentArgument;
import dev.jorel.commandapi.arguments.spigot.ChatArgument;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class SpigotExamples {

void argument_chatSpigot() {
/* ANCHOR: argumentChatSpigot1 */
new CommandAPICommand("namecolor")
    .withArguments(new ChatColorArgument("chatcolor"))
    .executesPlayer((player, args) -> {
        ChatColor color = (ChatColor) args.get("chatcolor");
        player.setDisplayName(color + player.getName());
    })
    .register();
/* ANCHOR_END: argumentChatSpigot1 */

/* ANCHOR: argumentChatSpigot2 */
new CommandAPICommand("makebook")
    .withArguments(new PlayerArgument("player"))
    .withArguments(new ChatComponentArgument("contents"))
    .executes((sender, args) -> {
        Player player = (Player) args.get("player");
        BaseComponent[] arr = (BaseComponent[]) args.get("contents");

        // Create book
        ItemStack is = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) is.getItemMeta();
        meta.setTitle("Custom Book");
        meta.setAuthor(player.getName());
        meta.spigot().setPages(arr);
        is.setItemMeta(meta);

        // Give player the book
        player.getInventory().addItem(is);
    })
    .register();
/* ANCHOR_END: argumentChatSpigot2 */

/* ANCHOR: argumentChatSpigot3 */
new CommandAPICommand("pbroadcast")
    .withArguments(new dev.jorel.commandapi.arguments.adventure.ChatArgument("message"))
    .executes((sender, args) -> {
        BaseComponent[] message = (BaseComponent[]) args.get("message");

        // Broadcast the message to everyone on the server
        Bukkit.getServer().spigot().broadcast(message);
    })
    .register();
/* ANCHOR_END: argumentChatSpigot3 */
}

void chatPreview() {
/* ANCHOR: chatPreview1 */
new CommandAPICommand("broadcast")
    .withArguments(new ChatArgument("message").withPreview(info -> {
        // Convert parsed BaseComponent[] to plain text
        String plainText = BaseComponent.toPlainText(info.parsedInput());

        // Translate the & in plain text and generate a new BaseComponent[]
        return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', plainText));
    }))
    .executesPlayer((player, args) -> {
        // The user still entered legacy text. We need to properly convert this
        // to a BaseComponent[] by converting to plain text then to BaseComponent[]
        String plainText = BaseComponent.toPlainText((BaseComponent[]) args.get("message"));
        Bukkit.spigot().broadcast(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', plainText)));
    })
    .register();
/* ANCHOR_END: chatPreview1 */

/* ANCHOR: chatPreview2 */
new CommandAPICommand("broadcast")
    .withArguments(new ChatArgument("message").usePreview(true).withPreview(info -> {
        // Convert parsed BaseComponent[] to plain text
        String plainText = BaseComponent.toPlainText(info.parsedInput());

        // Translate the & in plain text and generate a new BaseComponent[]
        return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', plainText));
    }))
    .executesPlayer((player, args) -> {
        Bukkit.spigot().broadcast((BaseComponent[]) args.get("message"));
    })
    .register();
/* ANCHOR_END: chatPreview2 */
}

}
