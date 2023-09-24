package dev.jorel.commandapi.examples.kotlin

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.PlayerArgument
import dev.jorel.commandapi.arguments.spigot.ChatColorArgument
import dev.jorel.commandapi.arguments.spigot.ChatComponentArgument
import dev.jorel.commandapi.arguments.spigot.ChatArgument
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta

class SpigotExamples {

fun argument_chatSpigot() {
/* ANCHOR: argumentChatSpigot1 */
CommandAPICommand("namecolor")
    .withArguments(ChatColorArgument("chatColor"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val color = args["chatColor"] as ChatColor
        player.setDisplayName("$color${player.name}")
    })
    .register()
/* ANCHOR_END: argumentChatSpigot1 */

/* ANCHOR: argumentChatSpigot2 */
CommandAPICommand("makebook")
    .withArguments(PlayerArgument("player"))
    .withArguments(ChatComponentArgument("contents"))
    .executes(CommandExecutor { _, args ->
        val player = args["player"] as Player
        val arr = args["contents"] as Array<BaseComponent>

        // Create book
        val item = ItemStack(Material.WRITTEN_BOOK)
        val meta = item.itemMeta as BookMeta
        meta.title = "Custom Book"
        meta.author = player.name
        meta.spigot().setPages(arr)
        item.itemMeta = meta

        // Give player the book
        player.inventory.addItem(item)
    })
    .register()
/* ANCHOR_END: argumentChatSpigot2 */

/* ANCHOR: argumentChatSpigot3 */
CommandAPICommand("pbroadcast")
    .withArguments(dev.jorel.commandapi.arguments.adventure.ChatArgument("message"))
    .executes(CommandExecutor { _, args ->
        val message = args["message"] as Array<BaseComponent>

        // Broadcast the message to everyone on the server
        Bukkit.getServer().spigot().broadcast(*message)
    })
    .register()
/* ANCHOR_END: argumentChatSpigot3 */
}

fun chatPreview() {
/* ANCHOR: chatPreview1 */
CommandAPICommand("broadcast")
    .withArguments(ChatArgument("message").withPreview { info ->
        // Convert parsed BaseComponent[] to plain text
        val plainText: String = BaseComponent.toPlainText(*info.parsedInput() as Array<BaseComponent>)

        // Translate the & in plain text and generate a new BaseComponent[]
        TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', plainText))
    } )
    .executesPlayer(PlayerCommandExecutor { _, args ->
        // The user still entered legacy text. We need to properly convert this
        // to a BaseComponent[] by converting to plain text then to BaseComponent[]
        val plainText: String = BaseComponent.toPlainText(*args["message"] as Array<BaseComponent>)
        val baseComponents: Array<BaseComponent> = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', plainText))
        Bukkit.spigot().broadcast(*baseComponents)
    })
    .register()
/* ANCHOR_END: chatPreview1 */

/* ANCHOR: chatPreview2 */
CommandAPICommand("broadcast")
    .withArguments(ChatArgument("message")
        .usePreview(true).withPreview { info ->
            // Convert parsed BaseComponent[] to plain text
            val plainText = BaseComponent.toPlainText(*info.parsedInput() as Array<BaseComponent>)

            // Translate the & in plain text and generate a new BaseComponent[]
            TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', plainText))
        } )
    .executesPlayer(PlayerCommandExecutor { _, args ->
        Bukkit.spigot().broadcast(*args["message"] as Array<BaseComponent>)
    })
    .register()
/* ANCHOR_END: chatPreview2 */
}

}