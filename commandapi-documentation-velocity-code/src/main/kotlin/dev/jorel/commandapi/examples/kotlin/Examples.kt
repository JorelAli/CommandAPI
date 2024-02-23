package dev.jorel.commandapi.examples.kotlin

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import net.kyori.adventure.text.Component
import kotlin.random.Random

class Examples {

fun velocityIntro() {
/* ANCHOR: velocityIntro1 */
CommandAPICommand("randomnumber")
    .withArguments(IntegerArgument("min"))
    .withArguments(IntegerArgument("max"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val min = args["min"] as Int
        val max = args["max"] as Int
        val random = Random
        val randomNumber = random.nextInt(min, max)
        player.sendMessage(Component.text().content("Your random number is: $randomNumber"))
    })
    .register()
/* ANCHOR_END: velocityIntro1 */
}

}