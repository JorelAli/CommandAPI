package io.github.jorelali.commandapi.api.wrappers;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;

public class ScoreboardSlot {

	private final DisplaySlot displaySlot;
	private final ChatColor teamColor;
	
	public ScoreboardSlot(int i) {
		//Initialize displaySlot
		switch(i) {
			case 0: displaySlot = DisplaySlot.PLAYER_LIST; break;
			case 1: displaySlot = DisplaySlot.SIDEBAR; break;
			case 2: displaySlot = DisplaySlot.BELOW_NAME; break;
			default: displaySlot = DisplaySlot.SIDEBAR; break;
		}
		
		//Initialize teamColor
		switch(i) {
			case 3: teamColor = ChatColor.BLACK; break;
			case 4: teamColor = ChatColor.DARK_BLUE; break;
			case 5: teamColor = ChatColor.DARK_GREEN; break;
			case 6: teamColor = ChatColor.DARK_AQUA; break;
			case 7: teamColor = ChatColor.DARK_RED; break;
			case 8: teamColor = ChatColor.DARK_PURPLE; break;
			case 9: teamColor = ChatColor.GOLD; break;
			case 10: teamColor = ChatColor.GRAY; break;
			case 11: teamColor = ChatColor.DARK_GRAY; break;
			case 12: teamColor = ChatColor.BLUE; break;
			case 13: teamColor = ChatColor.GREEN; break;
			case 14: teamColor = ChatColor.AQUA; break;
			case 15: teamColor = ChatColor.RED; break;
			case 16: teamColor = ChatColor.LIGHT_PURPLE; break;
			case 17: teamColor = ChatColor.YELLOW; break;
			case 18: teamColor = ChatColor.WHITE; break;
			default: teamColor = null; break;
		}
	}
	
	/**
	 * Gets the display slot of this scoreboard slot.
	 * @return this scoreboard slot's display slot
	 */
	public DisplaySlot getDisplaySlot() {
		return this.displaySlot;
	}
	
	/**
	 * Gets the team color of this scoreboard slot.
	 * @return this scoreboard slot's team color, or null if a team color is not present
	 */
	public ChatColor getTeamColor() {
		return this.teamColor;
	}
	
	/**
	 * Returns whether this scoreboard slot has a team color.
	 * @return true if this scoreboard slot has a team color
	 */
	public boolean hasTeamColor() {
		return teamColor != null;
	}
}
