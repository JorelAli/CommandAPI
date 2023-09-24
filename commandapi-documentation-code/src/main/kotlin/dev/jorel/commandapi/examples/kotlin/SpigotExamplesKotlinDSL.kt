package dev.jorel.commandapi.examples.kotlin

import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.spigot.chatArgument
import dev.jorel.commandapi.kotlindsl.spigot.chatColorArgument
import dev.jorel.commandapi.kotlindsl.spigot.chatComponentArgument
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta

class SpigotExamplesKotlinDSL {

fun argument_chatSpigot() {
/* ANCHOR: argumentChatSpigot1 */
commandAPICommand("namecolor") {
    chatColorArgument("chatcolor")
    playerExecutor { player, args ->
        val color = args["chatcolor"] as ChatColor
        player.setDisplayName("$color${player.name}")
    }
}
/* ANCHOR_END: argumentChatSpigot1 */

/* ANCHOR: argumentChatSpigot2 */
commandAPICommand("makebook") {
    playerArgument("player")
    chatComponentArgument("contents")
    anyExecutor { _, args ->
        val player = args["player"] as Player
        val array = args["contents"] as Array<BaseComponent>

        // Create book
        val item = ItemStack(Material.WRITTEN_BOOK)
        val meta = item.itemMeta as BookMeta
        meta.title = "Custom Book"
        meta.author = player.name
        meta.spigot().setPages(array)
        item.itemMeta = meta

        // Give player the book
        player.inventory.addItem(item)
    }
}
/* ANCHOR_END: argumentChatSpigot2 */

/* ANCHOR: argumentChatSpigot3 */
commandAPICommand("pbroadcast") {
    chatArgument("message")
    anyExecutor { _, args ->
        val message = args["message"] as Array<BaseComponent>

        // Broadcast the message to everyone on the server
        Bukkit.getServer().spigot().broadcast(*message)
    }
}
/* ANCHOR_END: argumentChatSpigot3 */
}

}