package dev.jorel.guilds;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.md_5.bungee.api.ChatColor;

public class ChatListener implements Listener {

	private Guilds plugin;
	
	public ChatListener(Guilds plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) { 
		UUID uuid = event.getPlayer().getUniqueId();
		Optional<Guild> guild = plugin.getGuild(uuid);
		if(guild.isPresent()) {
			event.setFormat("[" + guild.get().getTagColor() + guild.get().getTag() + ChatColor.RESET + "] " + "%s: %s");
		}
	}
	
}
