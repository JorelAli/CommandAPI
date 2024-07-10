package dev.jorel.commandapi;

import org.bukkit.command.CommandSender;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public record MockCommandSource(CommandSender bukkitSender) {
	public MockCommandSource {
		assertNotNull(bukkitSender);
	}
}
