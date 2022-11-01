import dev.jorel.commandapi.*
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.LiteralArgument.of
import dev.jorel.commandapi.arguments.StringArgument
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun sendMessageToCommand() {
/* ANCHOR: dslSendMessageToCommand */
command("sendmessageto") {
    playerArgument("player") { // Defines a new PlayerArgument("player")
        greedyArgument("msg") { // Defines a new GreedyStringArgument("msg)
            anyExecutor { _, args -> // Command can be executed by anyone and anything (such as entities, the console, etc.)
                val player: Player = args[0] as Player
                val message: String = args[1] as String
                player.sendMessage(message)
            }
        }
    }
}
/* ANCHOR_END: dslSendMessageToCommand */

/* ANCHOR: dslSendMessageToCommandRequirement */
command("sendMessageTo") {
    playerArgument("player") {
        greedyArgument("msg") {
            playerExecutor { _, args ->
                val player: Player = args[0] as Player
                val message: String = args[1] as String
                player.sendMessage(message)
            }
        }
    }
    requirement(of("broadcast"), { sender: CommandSender -> sender.isOp }) { // Define a new LiteralArgument("broadcast") that requires the CommandSender to be a player who is a server operator
        greedyArgument("msg") {
            playerExecutor { _, args ->
                val message: String = args[0] as String
                Bukkit.broadcastMessage(message)
            }
        }
    }
}
/* ANCHOR_END: dslSendMessageToCommandRequirement */

/* ANCHOR: dslCommandRequirements */
command("commandRequirement", {sender: CommandSender -> sender.isOp}) {
    playerExecutor { player, _ ->
        player.sendMessage("This command can only be executed by players who are server operators.")
    }
}
/* ANCHOR_END: dslCommandRequirements */
}

fun moreExamples() {
/* ANCHOR: optionalArgument */
command("optionalArgument") {
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

/* ANCHOR: replaceSuggestions */
command("replaceSuggestions") {
    argument(StringArgument("strings").replaceSuggestions(ArgumentSuggestions.strings("one", "two", "three"))) { // Implement an argument that has suggestions
        playerExecutor { player, args ->
            player.sendMessage("You chose option ${args[0] as String}!")
        }
    }
}
/* ANCHOR_END: replaceSuggestions */
}