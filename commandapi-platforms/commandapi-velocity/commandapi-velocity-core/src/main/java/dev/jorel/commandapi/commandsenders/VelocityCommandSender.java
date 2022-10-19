package dev.jorel.commandapi.commandsenders;

import com.velocitypowered.api.command.CommandSource;
import dev.jorel.commandapi.abstractions.AbstractCommandSender;

public interface VelocityCommandSender<Source extends CommandSource> extends AbstractCommandSender<Source> {
}
