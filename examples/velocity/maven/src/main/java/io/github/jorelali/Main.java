package io.github.jorelali;

import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import net.kyori.adventure.text.Component;

import javax.inject.Inject;
import java.util.logging.Logger;

@Plugin(id = "maven-example", description = "An example for using the CommandAPI with maven",
// Add a dependency on the CommandAPI
dependencies = {@Dependency(id = "commandapi")})
public class Main {
	@Inject
	public Main(Logger logger) {
		logger.info("Creating commands");
		// Create commands
		new CommandAPICommand("echo")
			.withArguments(new GreedyStringArgument("message"))
			.executes(((sender, args) -> {
				sender.sendMessage(Component.text((String) args.get(0)));
			}))
			.register();
	}
}
