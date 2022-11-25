package dev.jorel.commandapi.executors;

import org.bukkit.command.CommandSender;

import java.util.Map;

public record ExecutionInfo<Sender extends CommandSender>(Sender sender, Object[] args, Map<String, Object> argsMap) {
}
