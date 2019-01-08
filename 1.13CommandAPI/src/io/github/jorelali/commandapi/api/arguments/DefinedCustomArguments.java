package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import io.github.jorelali.commandapi.api.arguments.CustomArgument.MessageBuilder;

public class DefinedCustomArguments {
	
	public static CustomArgument<Objective> objectiveArgument() {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		return new CustomArgument<Objective>((input) -> {
			if(scoreboard.getObjective(input) == null) {
				CustomArgument.throwError(new MessageBuilder("Unknown objective: ").appendArgInput());
				return null;
			} else {
				return scoreboard.getObjective(input);
			}
		}).overrideSuggestions(scoreboard.getObjectives().stream().map(o -> o.getName()).toArray(String[]::new));
	}
	
	public static CustomArgument<Team> teamArgument() {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		return new CustomArgument<Team>((input) -> {
			if(scoreboard.getTeam(input) == null) {
				CustomArgument.throwError(new MessageBuilder("Unknown team: ").appendArgInput());
				return null;
			} else {
				return scoreboard.getTeam(input);
			}
		}).overrideSuggestions(scoreboard.getTeams().stream().map(o -> o.getName()).toArray(String[]::new));
	}
	
	/*
	 * TODO:
	 * LootTables
	 * Advancements
	 * Recipes
	 * Sounds
	 */
	
	public static CustomArgument<Sound> soundArgument() {
		
		
		return new CustomArgument<Sound>((input) -> {
			
			return null;
		}, true);
		
		//TODO: Use NMS here because it's way more reliable than the Sound enum:
		
		/*
		 * As stated in the Sound enum documentation:
		 *  
		 *   * WARNING: At any time, sounds may be added/removed from this Enum or even
			 * MineCraft itself! There is no guarantee the sounds will play. There is no
			 * guarantee values will not be removed from this Enum. As such, you should not
			 * depend on the ordinal values of this class.
		 */
	}
	
}
