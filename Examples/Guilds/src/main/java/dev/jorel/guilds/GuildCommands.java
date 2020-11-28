package dev.jorel.guilds;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ChatColorArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;

public class GuildCommands {

	// Commands are:
	// /guild - Displays help
	// /guild create <name> <tag> <tagColor> - Create a guild
	//
	// /guild info - List current guild information
	// /guild add <player> - Adds a player to your guild
	// /guild kick <player> - Kick a player from your guild
	// /guild leave - Leave your current guild

	public static void registerCommands(Guilds plugin) {
		
		new CommandAPICommand("guild")
			.executes((sender, args) -> {
				//Print help
			})
			.register();
		
		// A predicate that checks if a player is in a guild or not. Returns
		// true if they are in a guild. If they are not a player, return false
		final Predicate<CommandSender> isInGuildPredicate = sender -> {
			if(sender instanceof Player) {
				return plugin.getGuild(((Player) sender).getUniqueId()) != null;
			} else {
				return false;
			}
		};
		
		new CommandAPICommand("guild")
		    .withArguments(new LiteralArgument("create"))
		    .withArguments(new StringArgument("name"))
		    .withArguments(new StringArgument("tag"))
		    .withArguments(new ChatColorArgument("tagcolor"))
		    .withRequirement(isInGuildPredicate.negate())
		    .executesPlayer((player, args) -> {
		    	// Get the arguments
		    	String guildName = (String) args[0];
		    	String guildTag = (String) args[1];
		    	ChatColor guildTagColor = (ChatColor) args[2];
		    	
		    	// Add them to the guild and update the requirements
		    	// because they went from not being in a guild to being
		    	// in a guild
		    	Guild guild = new Guild(guildName, guildTag, guildTagColor);
		    	plugin.addGuild(player.getUniqueId(), guild);
		    	CommandAPI.updateRequirements(player);
		    })
		    .register();
		
		new CommandAPICommand("guild")
			.withRequirement(isInGuildPredicate)
			.withArguments(new LiteralArgument("info"))
			.executesPlayer((player, args) -> {
				Guild guild = plugin.getGuild(player.getUniqueId());
				
				List<UUID> guildMembers = plugin.getOtherGuildMembers(player.getUniqueId());
				List<String> guildMemberNames = guildMembers.stream()
					.map(Bukkit::getOfflinePlayer)
					.map(OfflinePlayer::getName)
					.sorted()
					.collect(Collectors.toList());
				
				player.sendMessage("Guild: " + guild.getName());
				player.sendMessage("Guild tag: [" + guild.getTagColor() + guild.getTag() + ChatColor.RESET + "]");
				player.sendMessage("Guild members:");
				for(String guildMember : guildMemberNames) {
					player.sendMessage("- " + guildMember);
				}
			})
			.register();
	}
	
}
