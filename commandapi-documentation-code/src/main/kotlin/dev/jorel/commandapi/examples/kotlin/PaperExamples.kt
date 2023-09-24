package dev.jorel.commandapi.examples.kotlin

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.PlayerArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.arguments.TextArgument
import dev.jorel.commandapi.arguments.adventure.ChatArgument
import dev.jorel.commandapi.arguments.adventure.ChatColorArgument
import dev.jorel.commandapi.arguments.adventure.ChatComponentArgument
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.entity.Player

class PaperExamples {

fun argument_chatAdventure() {
/* ANCHOR: argumentChatAdventure1 */
CommandAPICommand("namecolor")
    .withArguments(ChatColorArgument("chatcolor"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val color = args["chatcolor"] as NamedTextColor
        player.displayName(Component.text().color(color).append(Component.text(player.name)).build())
    })
    .register()
/* ANCHOR_END: argumentChatAdventure1 */

/* ANCHOR: argumentChatAdventure2 */
CommandAPICommand("showbook")
    .withArguments(PlayerArgument("target"))
    .withArguments(TextArgument("title"))
    .withArguments(StringArgument("author"))
    .withArguments(ChatComponentArgument("contents"))
    .executes(CommandExecutor { _, args ->
        val target = args["target"] as Player
        val title = args["title"] as String
        val author = args["author"] as String
        val content = args["contents"] as Component

        // Create a book and show it to the user (Requires Paper)
        val mybook = Book.book(Component.text(title), Component.text(author), content)
        target.openBook(mybook)
    })
    .register()
/* ANCHOR_END: argumentChatAdventure2 */

/* ANCHOR: argumentChatAdventure3 */
CommandAPICommand("pbroadcast")
    .withArguments(ChatArgument("message"))
    .executes(CommandExecutor { _, args ->
        val message = args["message"] as Component

        // Broadcast the message to everyone with broadcast permissions.
        Bukkit.getServer().broadcast(message, Server.BROADCAST_CHANNEL_USERS)
        Bukkit.getServer().broadcast(message)
    })
    .register()
/* ANCHOR_END: argumentChatAdventure3 */
}

fun chatPreview() {
/* ANCHOR: chatPreview1 */
CommandAPICommand("broadcast")
    .withArguments(ChatArgument("message").withPreview { info ->
        // Convert parsed Component to plain text
        val plainText: String = PlainTextComponentSerializer.plainText().serialize(info.parsedInput() as Component)

        // Translate the & in plain text and generate a new Component
        LegacyComponentSerializer.legacyAmpersand().deserialize(plainText)
    } )
    .executesPlayer(PlayerCommandExecutor { _, args ->
        // The user still entered legacy text. We need to properly convert this
        // to a Component by converting to plain text then to Component
        val plainText: String = PlainTextComponentSerializer.plainText().serialize(args["message"] as Component)
        Bukkit.broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize(plainText))
    })
    .register()
/* ANCHOR_END: chatPreview1 */

/* ANCHOR: chatPreview2 */
CommandAPICommand("broadcast")
    .withArguments(ChatArgument("message").usePreview(true).withPreview { info ->
        // Convert parsed Component to plain text
        val plainText = PlainTextComponentSerializer.plainText().serialize(info.parsedInput() as Component)

        // Translate the & in plain text and generate a new Component
        LegacyComponentSerializer.legacyAmpersand().deserialize(plainText)
    } )
    .executesPlayer(PlayerCommandExecutor { _, args ->
        Bukkit.broadcast(args["message"] as Component)
    })
    .register()
/* ANCHOR_END: chatPreview2 */
}

}