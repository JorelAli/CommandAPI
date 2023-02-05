package io.github.jorelali;

import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.SafeSuggestions;
import org.bukkit.entity.Player;

import java.util.Map;

public class RatedPlayerArgument extends CustomArgument<RatedPlayer, Player> {
	public RatedPlayerArgument(String nodeName) {
		this(nodeName, 0);
	}

	public RatedPlayerArgument(String nodeName, int minimumRank) {
		super(
			// PlayerArgument is the base for our argument
			new PlayerArgument(nodeName)
				// Replacing suggestions with Players who are rated and have appropriate level
				.replaceSafeSuggestions(SafeSuggestions.suggest(
					info ->
						// Get all rated players
						RatedPlayer.getPlayers().entrySet().stream()
							// Filter out players who don't have the appropriate level
							.filter((entry) -> {
								int rank = entry.getValue().getRank();
								return minimumRank <= rank;
							})
							// Get the Players for our suggestions
							.map(Map.Entry::getKey)
							// Collect into an array
							.toArray(Player[]::new)
				)),
			// The parser that inputs CustomArgumentInfo and returns the proper RatedPlayer
			info -> {
				Player player = info.currentInput();
				RatedPlayer ratedPlayer = RatedPlayer.get(player);
				// If a Player that is not a RatedPlayer was entered, throw a CustomArgumentException
				if (ratedPlayer == null)
					throw new CustomArgumentException(new MessageBuilder("This player is not rated! /").appendFullInput().appendHere());

				int rank = ratedPlayer.getRank();
				if(!(minimumRank <= rank))
					throw new CustomArgumentException(
						new MessageBuilder("This player dose not have a high enough rank. Expected at least ")
							.append(minimumRank)
							.append(" but found ")
							.append(rank)
							.append("/")
							.appendFullInput().appendHere()
					);

				return ratedPlayer;
			});
	}
}
