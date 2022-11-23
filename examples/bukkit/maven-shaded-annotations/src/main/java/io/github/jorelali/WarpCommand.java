package io.github.jorelali;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Help;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.Subcommand;
import dev.jorel.commandapi.annotations.arguments.APlayerArgument;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;

@Command("warp")
@Help(value = "Manages all warps on the server", shortDescription = "Manages warps")
public class WarpCommand {

	// List of warp names and their locations
	static Map<String, Location> warps = new HashMap<>();

	@Default
	public static void warp(CommandSender sender) {
		sender.sendMessage("--- Warp help ---");
		sender.sendMessage("/warp - Show this help");
		sender.sendMessage("/warp <warp> - Teleport to <warp>");
		sender.sendMessage("/warp create <warpname> - Creates a warp at your current location");
		sender.sendMessage("/warp tp <player> <warpname> - Teleports a player to a warp");
	}

	@Default
	public static void warp(Player player, @AStringArgument String warpName) {
		player.teleport(warps.get(warpName));
	}

	@Subcommand("create")
	@Permission("warps.create")
	public static void createWarp(Player player, @AStringArgument String warpName) {
		warps.put(warpName, player.getLocation());
	}

	@Subcommand("create")
	@Permission("warps.create")
	public static void tpWarp(CommandSender sender, @APlayerArgument OfflinePlayer target, @AStringArgument String warpName) {
		if (target.isOnline() && target instanceof Player onlineTarget) {
			onlineTarget.teleport(warps.get(warpName));
		}
	}

}