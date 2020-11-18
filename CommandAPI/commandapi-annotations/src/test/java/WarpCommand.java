import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.annotations.Arg;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Subcommand;
import dev.jorel.commandapi.annotations.Suggest;
import dev.jorel.commandapi.annotations.Suggestion;
import dev.jorel.commandapi.arguments.StringArgument;

@Command("warp")	
public class WarpCommand {
	
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
	@Arg(name = "warp", type = StringArgument.class)
	public static void warp(Player player, @Suggest("warpNames") String warpName) {
		player.teleport(warps.get(warpName));
	}
	
	@Suggestion
	public static String[] warpNames() {
		return warps.keySet().toArray(new String[0]);
	}
	
	@Subcommand("create")
	@Arg(name = "warpname", type = StringArgument.class)
	public static void createWarp(Player player, String warpName) {
		warps.put(warpName, player.getLocation());
	}
	
}
	