package dev.jorel.commandapi.commandsenders;

import dev.jorel.commandapi.abstractions.AbstractCommandSender;
import org.bukkit.command.CommandSender;

public interface BukkitCommandSender<Source extends CommandSender> extends AbstractCommandSender<Source> {
}
