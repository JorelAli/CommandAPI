package dev.jorel.guilds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

public class Guilds extends JavaPlugin {

	// Map players to their guild
	private Map<UUID, Guild> playerGuilds;
	
	@Override
	public void onLoad() {
		playerGuilds = new HashMap<>();
		GuildCommands.registerCommands(this);
	}

	@Override
	public void onEnable() {
		// Register events and commands
		getServer().getPluginManager().registerEvents(new ChatListener(this), this);
	}

	/**
	 * Registers a guild to a player
	 */
	public void addGuild(UUID uuid, Guild guild) {
		this.playerGuilds.put(uuid, guild);
	}

	/**
	 * Removes a player's guild
	 */
	public void removeGuild(UUID uuid) {
		this.playerGuilds.remove(uuid);
	}

	/**
	 * Get the guild for a given player
	 */
	public Optional<Guild> getGuild(UUID uuid) {
		return Optional.ofNullable(this.playerGuilds.get(uuid));
	}

	/**
	 * Return a list of guild members for a given player's guild. This list will
	 * also include the provided uuid
	 */
	public List<UUID> getOtherGuildMembers(UUID uuid) {
		List<UUID> uuids = new ArrayList<>();
		Optional<Guild> guild = getGuild(uuid);

		if (guild.isPresent()) {
			String guildName = guild.get().getName();
			for (UUID key : playerGuilds.keySet()) {
				if (playerGuilds.get(key).getName().equals(guildName)) {
					uuids.add(key);
				}
			}
		}
		return uuids;
	}

}
