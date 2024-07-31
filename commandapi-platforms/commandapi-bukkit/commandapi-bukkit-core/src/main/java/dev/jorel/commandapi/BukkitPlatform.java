package dev.jorel.commandapi;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.commandsenders.BukkitCommandSender;
import dev.jorel.commandapi.nms.NMS;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface BukkitPlatform<Source> extends CommandAPIPlatform<Argument<?>, CommandSender, Source>, NMS<Source> {
	// Methods implemented by CommandAPIBukkit that CommandAPISpigot and CommandAPIPaper can use
	CommandRegistrationStrategy<Source> getCommandRegistrationStrategy();

	void updateHelpForCommands(List<RegisteredCommand> commands);

	// Methods implemented by CommandAPISpigot/CommandAPIPaper that CommandAPIBukkit can use
	void platformOnLoad(CommandAPIConfig<?> config);

	void platformOnEnable();

	void platformOnDisable();

	@Override
	BukkitCommandSender<? extends CommandSender> wrapCommandSender(CommandSender sender);

	CommandMap getCommandMap();

	Platform activePlatform();

	CommandRegistrationStrategy<Source> createCommandRegistrationStrategy();
}
