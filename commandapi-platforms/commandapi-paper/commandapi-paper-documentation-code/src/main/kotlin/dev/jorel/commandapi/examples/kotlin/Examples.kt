package dev.jorel.commandapi.examples.kotlin

import de.tr7zw.changeme.nbtapi.NBTContainer
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandAPIPaperConfig
import dev.jorel.commandapi.arguments.*
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
import org.bukkit.plugin.java.JavaPlugin

class Examples {

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

class argument_nbt : JavaPlugin() {

/* ANCHOR: argumentNBT1 */
override fun onLoad() {
    CommandAPI.onLoad(CommandAPIPaperConfig(this)
        .initializeNBTAPI(NBTContainer::class.java, ::NBTContainer)
    )
}
/* ANCHOR_END: argumentNBT1 */

fun argument_nbt2() {
    /* ANCHOR: argumentNBT2 */
    CommandAPICommand("award")
        .withArguments(NBTCompoundArgument<NBTContainer>("nbt"))
        .executes(CommandExecutor { _, args ->
            val nbt = args["nbt"] as NBTContainer

            // Do something with "nbt" here...
        })
        .register()
    /* ANCHOR_END: argumentNBT2 */
}

}

fun chatPreview() {
/* ANCHOR: chatPreview1 */
CommandAPICommand("broadcast")
    .withArguments(ChatArgument("message").withPreview { info ->
        // Convert parsed Component to plain text
        val plainText: String = PlainTextComponentSerializer.plainText().serialize(info.parsedInput() as Component)

        // Translate the & in plain text and generate a new Component
        LegacyComponentSerializer.legacyAmpersand().deserialize(plainText)
    })
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
    })
    .executesPlayer(PlayerCommandExecutor { _, args ->
        Bukkit.broadcast(args["message"] as Component)
    })
    .register()
/* ANCHOR_END: chatPreview2 */
}

class setupShading {
val plugin: JavaPlugin = object : JavaPlugin() {}

fun setupShading1() {
    /* ANCHOR: setupShading1 */
    CommandAPI.onLoad(CommandAPIPaperConfig(plugin).silentLogs(true))
    /* ANCHOR_END: setupShading1 */
}

/* ANCHOR: setupShading2 */
class MyPlugin : JavaPlugin() {

    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIPaperConfig(this).verboseOutput(true)) // Load with verbose output

        CommandAPICommand("ping")
            .executes(CommandExecutor { sender, _ ->
                sender.sendMessage("pong!")
            })
            .register()
    }

    override fun onEnable() {
        CommandAPI.onEnable()

        // Register commands, listeners etc.
    }

    override fun onDisable() {
        CommandAPI.onDisable()
    }

}
/* ANCHOR_END: setupShading2 */
}

}