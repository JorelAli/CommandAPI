package dev.jorel.guilds;

import org.bukkit.ChatColor;

/**
 * A very simple class that contains guild information. Generated using
 * generic getters and setters
 */
public class Guild {
	
	private String name;
	private String tag;
	private ChatColor tagColor;
	
	public Guild(String name, String tag, ChatColor tagColor) {
		this.name = name;
		this.tag = tag;
		this.tagColor = tagColor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public ChatColor getTagColor() {
		return tagColor;
	}

	public void setTagColor(ChatColor tagColor) {
		this.tagColor = tagColor;
	}

}
