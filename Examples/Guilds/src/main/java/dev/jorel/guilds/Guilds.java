package dev.jorel.guilds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

public class Guilds extends JavaPlugin {

	// Map players to their guild
	private Map<UUID, Guild> playerGuilds;
	
	@Override
	public void onEnable() {
		playerGuilds = new HashMap<>();
		
		getServer().getPluginManager().registerEvents(new ChatListener(this), this);
		
		GuildCommands.registerCommands(this);
	}
	
	public Guild getGuild(UUID uuid) {
		return this.playerGuilds.get(uuid);
	}
	
	public List<UUID> getOtherGuildMembers(UUID uuid) {
		List<UUID> uuids = new ArrayList<>();
		String guildName = getGuild(uuid).getName();
		
		for(UUID key : playerGuilds.keySet()) {
			if(playerGuilds.get(key).getName().equals(guildName)) {
				uuids.add(key);
			}
		}
		return uuids;
	}
	
}
