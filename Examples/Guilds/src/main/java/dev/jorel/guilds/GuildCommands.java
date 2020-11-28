package dev.jorel.guilds;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ChatColorArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import net.md_5.bungee.api.ChatColor;

public class GuildCommands {

	public static void registerCommands(Guilds plugin) {
		
		// Commands are:
		// /guild - Displays help
		// /guild create <name> <tag> <tagColor> - Create a guild
		//
		// /guild info - List current guild information
		// /guild add <player> - Adds a player to your guild
		// /guild kick <player> - Kick a player from your guild
		// /guild leave - Leave your current guild
		
		new CommandAPICommand("guild")
			.executes((sender, args) -> {
				//Print help
			})
			.register();
		
		new CommandAPICommand("guild")
		    .withArguments(new LiteralArgument("create"))
		    .withArguments(new StringArgument("name"))
		    .withArguments(new StringArgument("tag"))
		    .withArguments(new ChatColorArgument("tagcolor"))
		    .executesPlayer((player, args) -> {
		    	
		    })
		    .register();
		
		Predicate<CommandSender> isInGuildPredicate = sender -> {
			if(sender instanceof Player) {
				return plugin.getGuild(((Player) sender).getUniqueId()) != null;
			} else {
				return false;
			}
		};
		
		// Require the player to be in a guild to use this command
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
