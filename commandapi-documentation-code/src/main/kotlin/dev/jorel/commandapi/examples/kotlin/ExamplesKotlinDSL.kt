import dev.jorel.commandapi.*
import dev.jorel.commandapi.kotlindsl.*
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.LiteralArgument.of
import dev.jorel.commandapi.arguments.StringArgument
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun sendMessageToCommand() {
/* ANCHOR: dslSendMessageToCommand */
commandTree("sendmessageto") {
    playerArgument("player") { // Defines a new PlayerArgument("player")
        greedyStringArgument("msg") { // Defines a new GreedyStringArgument("msg)
            anyExecutor { _, args -> // Command can be executed by anyone and anything (such as entities, the console, etc.)
                val player: Player = args[0] as Player
                val message: String = args[1] as String
                player.sendMessage(message)
            }
        }
    }
}
/* ANCHOR_END: dslSendMessageToCommand */

/* ANCHOR: dslSendMessageToCommand2 */
commandAPICommand("sendmessageto") {
    playerArgument("player") // Defines a new PlayerArgument("player")
    greedyStringArgument("msg") // Defines a new GreedyStringArgument("msg)
    anyExecutor { _, args -> // Command can be executed by anyone and anything (such as entities, the console, etc.)
        val player: Player = args[0] as Player
        val message: String = args[1] as String
        player.sendMessage(message)
    }
}
/* ANCHOR_END: dslSendMessageToCommand2 */

/* ANCHOR: dslSendMessageToCommandRequirement */
commandTree("sendMessageTo") {
    playerArgument("player") {
        greedyStringArgument("msg") {
            playerExecutor { _, args ->
                val player: Player = args[0] as Player
                val message: String = args[1] as String
                player.sendMessage(message)
            }
        }
    }
    requirement(of("broadcast"), { sender: CommandSender -> sender.isOp }) { // Define a new LiteralArgument("broadcast") that requires the CommandSender to be a player who is a server operator
        greedyStringArgument("msg") {
            playerExecutor { _, args ->
                val message: String = args[0] as String
                Bukkit.broadcastMessage(message)
            }
        }
    }
}
/* ANCHOR_END: dslSendMessageToCommandRequirement */

/* ANCHOR: dslSendMessageToCommandRequirement2 */
commandAPICommand("sendMessageTo") {
    playerArgument("player")
    greedyStringArgument("msg")
    playerExecutor { _, args ->
        val player: Player = args[0] as Player
        val message: String = args[1] as String
        player.sendMessage(message)
    }
}

commandAPICommand("sendMessageTo") {
    requirement(of("broadcast"), { sender: CommandSender -> sender.isOp }) // Define a new LiteralArgument("broadcast") that requires the CommandSender to be a player who is a server operator
    greedyStringArgument("msg")
    playerExecutor { _, args ->
        val message: String = args[0] as String
        Bukkit.broadcastMessage(message)
    }
}
/* ANCHOR_END: dslSendMessageToCommandRequirement2 */

/* ANCHOR: dslCommandRequirements */
commandTree("commandRequirement", {sender: CommandSender -> sender.isOp}) {
    playerExecutor { player, _ ->
        player.sendMessage("This command can only be executed by players who are server operators.")
    }
}
/* ANCHOR_END: dslCommandRequirements */

/* ANCHOR: dslCommandRequirements2 */
commandAPICommand("commandRequirement", {sender: CommandSender -> sender.isOp}) {
    playerExecutor { player, _ ->
        player.sendMessage("This command can only be executed by players who are server operators.")
    }
}
/* ANCHOR_END: dslCommandRequirements2 */
}

fun moreExamples() {
/* ANCHOR: optionalArgument */
commandTree("optionalArgument") {
    literalArgument("give") {
        itemStackArgument("item") {
            playerExecutor { player, args -> // This will let you execute "/optionalArgument give minecraft:stick"
                val itemStack: ItemStack = args[0] as ItemStack
                player.inventory.addItem(itemStack)
            }
            integerArgument("amount") {
                playerExecutor { player, args -> // This will let you execute "/optionalArgument give minecraft:stick 5"
                    val itemStack: ItemStack = args[0] as ItemStack
                    val amount: Int = args[1] as Int
                    itemStack.amount = amount
                    player.inventory.addItem(itemStack)
                }
            }
        }
    }
}
/* ANCHOR_END: optionalArgument */

/* ANCHOR: optionalArgument2 */
commandAPICommand("optionalArgument") {
    literalArgument("give")
    itemStackArgument("item")
    playerExecutor { player, args -> // This will let you execute "/optionalArgument give minecraft:stick"
        val itemStack: ItemStack = args[0] as ItemStack
        player.inventory.addItem(itemStack)
    }
}

commandAPICommand("optionalArgument") {
    literalArgument("give")
    itemStackArgument("item")
    integerArgument("amount")
    playerExecutor { player, args -> // This will let you execute "/optionalArgument give minecraft:stick 5"
        val itemStack: ItemStack = args[0] as ItemStack
        val amount: Int = args[1] as Int
        itemStack.amount = amount
        player.inventory.addItem(itemStack)
    }
}
/* ANCHOR_END: optionalArgument2 */

/* ANCHOR: replaceSuggestions */
commandTree("replaceSuggestions") {
    argument(StringArgument("strings").replaceSuggestions(ArgumentSuggestions.strings("one", "two", "three"))) { // Implement an argument that has suggestions
        playerExecutor { player, args ->
            player.sendMessage("You chose option ${args[0] as String}!")
        }
    }
}
/* ANCHOR_END: replaceSuggestions */

/* ANCHOR: replaceSuggestions2 */
commandAPICommand("replaceSuggestions") {
    argument(StringArgument("strings").replaceSuggestions(ArgumentSuggestions.strings("one", "two", "three"))) // Implement an argument that has suggestions
    playerExecutor { player, args ->
        player.sendMessage("You chose option ${args[0] as String}!")
    }
}
/* ANCHOR_END: replaceSuggestions2 */
}