package dev.jorel.commandapi;

import org.bukkit.entity.EntityType;

public class Testing {

	public static void registerTestCommands() {
		new CommandAPICommand("a:b").executes((sender, args) -> {
			System.out.println("hi");
		}).register();
	}
}
