package dev.jorel.commandapi.executors;

import org.bukkit.command.CommandSender;

public record ExecutionInfo<Sender extends CommandSender>(Sender sender, CommandArguments args) {
}
