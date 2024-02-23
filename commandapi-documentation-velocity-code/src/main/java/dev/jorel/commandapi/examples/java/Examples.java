package dev.jorel.commandapi.examples.java;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import net.kyori.adventure.text.Component;

import java.util.Random;

public class Examples {

void velocityIntro() {
/* ANCHOR: velocityIntro1 */
new CommandAPICommand("randomnumber")
    .withArguments(new IntegerArgument("min"))
    .withArguments(new IntegerArgument("max"))
    .executesPlayer((player, args) -> {
        int min = (int) args.get("min");
        int max = (int) args.get("max");
        Random random = new Random();
        int randomNumber = random.nextInt(min, max);
        player.sendMessage(Component.text().content("Your random number is: " + randomNumber));
    })
    .register();
/* ANCHOR_END: velocityIntro1 */
}

}
