package dev.jorel.commandapi.examples.kotlin

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.proxy.ProxyServer
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandAPIVelocity
import dev.jorel.commandapi.CommandAPIVelocityConfig
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.executors.NormalExecutor
import net.kyori.adventure.text.Component
import org.slf4j.Logger
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

class Examples {

fun velocityIntro() {
/* ANCHOR: velocityIntro1 */
CommandAPICommand("randomnumber")
    .withArguments(IntegerArgument("min"))
    .withArguments(IntegerArgument("max"))
    .executesPlayer(NormalExecutor { player, args ->
        val min = args["min"] as Int
        val max = args["max"] as Int
        val random = ThreadLocalRandom.current()
        val randomNumber = random.nextInt(min, max)
        player.sendMessage(Component.text().content("Your random number is: $randomNumber"))
    })
    .register()
/* ANCHOR_END: velocityIntro1 */
}

}