package dev.jorel.commandapi.commandsenders;

import org.bukkit.command.CommandSender;

public interface BukkitCommandSender<Source extends CommandSender> extends AbstractCommandSender<Source> {
}
