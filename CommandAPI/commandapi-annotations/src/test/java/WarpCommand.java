import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.annotations.Arg;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.Subcommand;
import dev.jorel.commandapi.arguments.StringArgument;

/* ANCHOR: warps */
/* ANCHOR: warps_command */
@Command("warp")	
public class WarpCommand {
/* ANCHOR_END: warps_command */
	
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
	public static void warp(Player player, String warpName) {
		player.teleport(warps.get(warpName));
	}
	
	@Subcommand("create")
	@Permission("warps.create")
	@Arg(name = "warpname", type = StringArgument.class)
	public static void createWarp(Player player, String warpName) {
		warps.put(warpName, player.getLocation());
	}
	
}
/* ANCHOR_END: warps */

class  A {
	{ 
/* ANCHOR: warps_register */
CommandAPI.registerCommand(WarpCommand.class);
/* ANCHOR_END: warps_register */
	}
	
	static Map<String, Location> warps = new HashMap<>();
	
/* ANCHOR: warps_help */
@Default
public static void warp(CommandSender sender) {
	sender.sendMessage("--- Warp help ---");
	sender.sendMessage("/warp - Show this help");
	sender.sendMessage("/warp <warp> - Teleport to <warp>");
	sender.sendMessage("/warp create <warpname> - Creates a warp at your current location");
}
/* ANCHOR_END: warps_help */
	
/* ANCHOR: warps_warp */
@Default
@Arg(name = "warp", type = StringArgument.class)
public static void warp(Player player, String warpName) {
	player.teleport(warps.get(warpName));
}
/* ANCHOR_END: warps_warp */
	
/* ANCHOR: warps_create */
@Subcommand("create")
@Permission("warps.create")
@Arg(name = "warpname", type = StringArgument.class)
public static void createWarp(Player player, String warpName) {
	warps.put(warpName, player.getLocation());
}
/* ANCHOR_END: warps_create */

}

class Examples {
{
/* ANCHOR: old_warps */
Map<String, Location> warps = new HashMap<>();

// /warp
new CommandAPICommand("warp")
	.executes((sender, args) -> {
		sender.sendMessage("--- Warp help ---");
		sender.sendMessage("/warp - Show this help");
		sender.sendMessage("/warp <warp> - Teleport to <warp>");
		sender.sendMessage("/warp create <warpname> - Creates a warp at your current location");
	})
	.register();

// /warp <warp>
new CommandAPICommand("warp")
	.withArguments(new StringArgument("warp").overrideSuggestions(sender -> {
		return warps.keySet().toArray(new String[0]);
	}))
	.executesPlayer((player, args) -> {
		player.teleport(warps.get((String) args[0]));
	})
	.register();

// /warp create <warpname>
new CommandAPICommand("warp")
    .withSubcommand(
		new CommandAPICommand("create")
			.withPermission("warps.create")
			.withArguments(new StringArgument("warpname"))
			.executesPlayer((player, args) -> {
				warps.put((String) args[0], player.getLocation());
			})
	)
    .register();
/* ANCHOR_END: old_warps */
}
	
}