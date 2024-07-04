package dev.jorel.commandapi;

import org.bukkit.command.CommandSender;

public record MockCommandSource(CommandSender bukkitSender) {

}
