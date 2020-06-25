package dev.jorel.commandapi;

public class Testing {
	public static void registerTestCommands() {
		new CommandAPICommand("a:b").executes((sender, args) -> {
			System.out.println("hi");
		}).register();
	}
}
