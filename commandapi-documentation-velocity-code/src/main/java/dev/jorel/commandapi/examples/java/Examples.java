package dev.jorel.commandapi.examples.java;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandAPIVelocityConfig;
import dev.jorel.commandapi.arguments.IntegerArgument;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("InnerClassMayBeStatic")
public class Examples {

class velocityIntro {
public class ExamplePlugin {

    private final ProxyServer server;
    private final Logger logger;

/* ANCHOR: velocityIntro1 */
@Inject
public ExamplePlugin(ProxyServer server, Logger logger) {
    this.server = server;
    this.logger = logger;

    CommandAPI.onLoad(new CommandAPIVelocityConfig(server, this));
}
/* ANCHOR_END: velocityIntro1 */

/* ANCHOR: velocityIntro2 */
@Subscribe
public void onProxyInitialization(ProxyInitializeEvent event) {
    // Any CommandAPI command registrations...
    CommandAPI.onEnable();
}
/* ANCHOR_END: velocityIntro2 */
}

void velocityIntro() {
/* ANCHOR: velocityIntro3 */
new CommandAPICommand("randomnumber")
    .withArguments(new IntegerArgument("min"))
    .withArguments(new IntegerArgument("max"))
    .executesPlayer((player, args) -> {
        int min = (int) args.get("min");
        int max = (int) args.get("max");
        Random random = ThreadLocalRandom.current();
        int randomNumber = random.nextInt(min, max);
        player.sendMessage(Component.text().content("Your random number is: " + randomNumber));
    })
    .register();
/* ANCHOR_END: velocityIntro3 */
}
}
}
