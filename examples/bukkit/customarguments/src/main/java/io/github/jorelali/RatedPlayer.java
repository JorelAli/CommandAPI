package io.github.jorelali;

import dev.jorel.commandapi.CommandAPI;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RatedPlayer {
	private static final Map<Player, RatedPlayer> players = new HashMap<>();

	public static RatedPlayer get(Player player) {
		return players.get(player);
	}

	public static void unrank(RatedPlayer player) {
		players.remove(player.getPlayer());

		// May not be able to access some commands since they are no longer rated
		CommandAPI.updateRequirements(player.getPlayer());
	}

	public static Map<Player, RatedPlayer> getPlayers() {
		return Collections.unmodifiableMap(players);
	}

	private final Player player;
	private int rank;

	public RatedPlayer(Player player, int rank) {
		this.player = player;
		this.rank = rank;

		players.put(player, this);

		// May be able to access new commands since they are now rated
		CommandAPI.updateRequirements(player);
	}

	public Player getPlayer() {
		return player;
	}

	public void setRank(int rank) {
		if (rank != this.rank) {
			this.rank = rank;

			// May have different command access with a different rank
			CommandAPI.updateRequirements(player);
		}
	}

	public int getRank() {
		return rank;
	}
}
