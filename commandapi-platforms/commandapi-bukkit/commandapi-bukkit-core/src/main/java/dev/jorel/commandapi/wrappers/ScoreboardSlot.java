/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.wrappers;

import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.scoreboard.DisplaySlot;

/**
 * A representation of scoreboard display slots, as well as team colors for the
 * sidebar
 */
public enum ScoreboardSlot implements Keyed {

	PLAYER_LIST("list", 0x0),
	SIDEBAR("sidebar", 0x1),
	BELOW_NAME("belowName", 0x2),
	SIDEBAR_TEAM_BLACK("sidebar.team.black", 0x0, ChatColor.BLACK),
	SIDEBAR_TEAM_DARK_BLUE("sidebar.team.dark_blue", 0x1, ChatColor.DARK_BLUE),
	SIDEBAR_TEAM_DARK_GREEN("sidebar.team.dark_green", 0x2, ChatColor.DARK_GREEN),
	SIDEBAR_TEAM_DARK_AQUA("sidebar.team.dark_aqua", 0x3, ChatColor.DARK_AQUA),
	SIDEBAR_TEAM_DARK_RED("sidebar.team.dark_red", 0x4, ChatColor.DARK_RED),
	SIDEBAR_TEAM_DARK_PURPLE("sidebar.team.dark_purple", 0x5, ChatColor.DARK_PURPLE),
	SIDEBAR_TEAM_GOLD("sidebar.team.gold", 0x6, ChatColor.GOLD),
	SIDEBAR_TEAM_GRAY("sidebar.team.gray", 0x7, ChatColor.GRAY),
	SIDEBAR_TEAM_DARK_GRAY("sidebar.team.dark_gray", 0x8, ChatColor.DARK_GRAY),
	SIDEBAR_TEAM_BLUE("sidebar.team.blue", 0x9, ChatColor.BLUE),
	SIDEBAR_TEAM_GREEN("sidebar.team.green", 0xA, ChatColor.GREEN),
	SIDEBAR_TEAM_AQUA("sidebar.team.aqua", 0xB, ChatColor.AQUA),
	SIDEBAR_TEAM_RED("sidebar.team.red", 0xC, ChatColor.RED),
	SIDEBAR_TEAM_LIGHT_PURPLE("sidebar.team.light_purple", 0xD, ChatColor.LIGHT_PURPLE),
	SIDEBAR_TEAM_YELLOW("sidebar.team.yellow", 0xE, ChatColor.YELLOW),
	SIDEBAR_TEAM_WHITE("sidebar.team.white", 0xF, ChatColor.WHITE);

	private final String key;
	private final int internal;
	private final ChatColor teamColor;

	ScoreboardSlot(String key, int internal) {
		this.key = key;
		this.internal = internal;
		this.teamColor = null;
	}

	ScoreboardSlot(String key, int internal, ChatColor color) {
		this.key = key;
		this.internal = internal;
		this.teamColor = color;
	}

	// Utility functions

	private static boolean isLegacy() {
		return DisplaySlot.values().length == 3;
	}

	private static boolean isPaper() {
		for (DisplaySlot slot : DisplaySlot.values()) {
			if (slot.name().startsWith("SIDEBAR_TEAM")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines the scoreboard slot value based on an internal Minecraft integer
	 * 
	 * @param i the scoreboard slot value
	 */
	public static ScoreboardSlot ofMinecraft(int i) {
		return switch (i) {
			case 0 -> ScoreboardSlot.PLAYER_LIST;
			case 1 -> ScoreboardSlot.SIDEBAR;
			case 2 -> ScoreboardSlot.BELOW_NAME;
			default -> {
				for (ScoreboardSlot slot : ScoreboardSlot.values()) {
					if (i - 3 == slot.internal && slot.hasTeamColor()) {
						yield slot;
					}
				}
				throw new IllegalStateException("Invalid ScoreboardSlot with internal index " + i);
			}
		};
	}

	/**
	 * Constructs a ScoreboardSlot from a DisplaySlot
	 * 
	 * @param slot the display slot to convert into a ScoreboardSlot
	 * @return a ScoreboardSlot from the provided DisplaySlot
	 */
	public static ScoreboardSlot of(DisplaySlot slot) {
		if (isLegacy()) {
			// Legacy
			return switch (slot) {
				case PLAYER_LIST -> ScoreboardSlot.PLAYER_LIST;
				case SIDEBAR -> ScoreboardSlot.SIDEBAR;
				case BELOW_NAME -> ScoreboardSlot.BELOW_NAME;
				default -> throw new IllegalArgumentException("Unexpected value: " + slot);
			};
		} else {
			// Non-legacy
			return switch (slot) {
				case PLAYER_LIST -> ScoreboardSlot.PLAYER_LIST;
				case SIDEBAR -> ScoreboardSlot.SIDEBAR;
				case BELOW_NAME -> ScoreboardSlot.BELOW_NAME;
				default -> {
					if (isPaper()) {
						// We're running on a paper server. Just handle it
						yield ScoreboardSlot.valueOf(slot.name());
					} else {
						// We're running on a Spigot server. Map the names
						yield ScoreboardSlot.valueOf("SIDEBAR_TEAM" + slot.name().substring(8));
					}
				}
			};
		}
	}

	/**
	 * Constructs a ScoreboardSlot from a ChatColor
	 * 
	 * @param color the chat color to convert into a ScoreboardSlot
	 * @return a ScoreboardSlot from the provided ChatColor
	 */
	public static ScoreboardSlot ofTeamColor(ChatColor color) {
		for(ScoreboardSlot slot : ScoreboardSlot.values()) {
			if(slot.hasTeamColor() && slot.teamColor.equals(color)) {
				return slot;
			}
		}
		return ScoreboardSlot.SIDEBAR;
	}

	/**
	 * Gets the display slot of this scoreboard slot.
	 * 
	 * @return this scoreboard slot's display slot
	 */
	public DisplaySlot getDisplaySlot() {
		if (isLegacy()) {
			return switch (this) {
				case BELOW_NAME -> DisplaySlot.BELOW_NAME;
				case PLAYER_LIST -> DisplaySlot.PLAYER_LIST;
				case SIDEBAR -> DisplaySlot.SIDEBAR;
				default -> DisplaySlot.SIDEBAR;
			};
		} else {
			if (isPaper()) {
				return DisplaySlot.valueOf(this.name());
			} else {
				return DisplaySlot.valueOf(this.name().replace("TEAM_", ""));
			}
		}
	}

	/**
	 * Gets the team color of this scoreboard slot.
	 * 
	 * @return this scoreboard slot's team color, or null if a team color is not
	 *         present
	 */
	public ChatColor getTeamColor() {
		return this.teamColor;
	}

	/**
	 * Returns whether this scoreboard slot has a team color.
	 * 
	 * @return true if this scoreboard slot has a team color
	 */
	public boolean hasTeamColor() {
		return teamColor != null;
	}

	@Override
	public NamespacedKey getKey() {
		return NamespacedKey.fromString(this.key);
	}

	/**
	 * Returns the Minecraft string representation of this scoreboard slot
	 * 
	 * @return the Minecraft string representation of this scoreboard slot
	 */
	@Override
	public String toString() {
		return this.key;
	}
}
