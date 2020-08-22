package dev.jorel.commandapi.wrappers;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;

/**
 * A representation of scoreboard display slots, as well as team colors for the sidebar
 */
public class ScoreboardSlot {

	private final DisplaySlot displaySlot;
	private final ChatColor teamColor;
	
	/**
	 * Determines the scoreboard slot value based on an internal Minecraft integer
	 * @param i the scoreboard slot value
	 */
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
	 * Constructs a ScoreboardSlot from a DisplaySlot
	 * @param slot the display slot to convert into a ScoreboardSlot
	 * @return a ScoreboardSlot from the provided DisplaySlot
	 */
	public static ScoreboardSlot of(DisplaySlot slot) {
		switch(slot) {
		case PLAYER_LIST:
			return new ScoreboardSlot(0);
		case SIDEBAR:
			return new ScoreboardSlot(1);
		case BELOW_NAME:
			return new ScoreboardSlot(2);
		default:
			return new ScoreboardSlot(1);
		}
	}
	
	/**
	 * Constructs a ScoreboardSlot from a ChatColor
	 * @param color the chat color to convert into a ScoreboardSlot
	 * @return a ScoreboardSlot from the provided ChatColor
	 */
	public static ScoreboardSlot ofTeamColor(ChatColor color) {
		switch (color) {
		case BLACK:
			return new ScoreboardSlot(3);
		case DARK_BLUE:
			return new ScoreboardSlot(4);
		case DARK_GREEN:
			return new ScoreboardSlot(5);
		case DARK_AQUA:
			return new ScoreboardSlot(6);
		case DARK_RED:
			return new ScoreboardSlot(7);
		case DARK_PURPLE:
			return new ScoreboardSlot(8);
		case GOLD:
			return new ScoreboardSlot(9);
		case GRAY:
			return new ScoreboardSlot(10);
		case DARK_GRAY:
			return new ScoreboardSlot(11);
		case BLUE:
			return new ScoreboardSlot(12);
		case GREEN:
			return new ScoreboardSlot(13);
		case AQUA:
			return new ScoreboardSlot(14);
		case RED:
			return new ScoreboardSlot(15);
		case LIGHT_PURPLE:
			return new ScoreboardSlot(16);
		case YELLOW:
			return new ScoreboardSlot(17);
		case WHITE:
			return new ScoreboardSlot(18);
		default:
			return new ScoreboardSlot(1);
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
	
	/**
	 * Returns the Minecraft string representation of this scoreboard slot
	 * @return the Minecraft string representation of this scoreboard slot
	 */
	@Override
	public String toString() {
		if(teamColor != null) {
			return "sidebar.team." + teamColor.name().toLowerCase();
		} else {
			switch(displaySlot) {
			case PLAYER_LIST:
				return "list";
			case SIDEBAR:
				return "sidebar";
			case BELOW_NAME:
				return "belowName";
			default:
				return "sidebar";
			}
		}
	}
}
