package dev.jorel.commandapi.commandsenders;

import org.spongepowered.api.command.CommandCause;

public interface SpongeCommandSender<Source extends CommandCause> extends AbstractCommandSender<Source> {
}
