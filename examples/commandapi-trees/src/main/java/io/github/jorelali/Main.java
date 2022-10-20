package io.github.jorelali;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		new BetterBossBarsCommand().registerBetterBossBarCommand();
	}

}
