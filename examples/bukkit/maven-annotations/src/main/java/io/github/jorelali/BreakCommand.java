package io.github.jorelali;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.arguments.ALocationArgument;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Command("break")
public class BreakCommand {
	@Default
	public static void breakAt(Player player, @ALocationArgument Location location) {
		location.getBlock().breakNaturally();
	}
}
