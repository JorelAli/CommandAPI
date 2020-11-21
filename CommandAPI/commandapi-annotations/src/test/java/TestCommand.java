import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.Subcommand;
import dev.jorel.commandapi.annotations.arguments.StringArgumentA;

@Command("warp")	
public class TestCommand {
	
	// List of warp names and their locations
	static Map<String, Location> warps = new HashMap<>();
	
	@Default
	public static void warp(CommandSender sender) {
		sender.sendMessage("--- Warp help ---");
		sender.sendMessage("/warp - Show this help");
		sender.sendMessage("/warp <warp> - Teleport to <warp>");
		sender.sendMessage("/warp create <warpname> - Creates a warp at your current location");
	}
	
	@Default
	public static void warp(Player player, @StringArgumentA String warpName) {
		player.teleport(warps.get(warpName));
	}
	
	@Subcommand("create")
	@Permission("warps.create")
	public static void createWarp(Player player, @StringArgumentA String warpName) {
//		warps.put(warpName, player.getLocation());
//		new IntegerArgument("");
	}
	
	
}