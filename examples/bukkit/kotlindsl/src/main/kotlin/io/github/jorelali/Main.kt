package io.github.jorelali

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

	override fun onEnable() {
		CommandAPI.onLoad(CommandAPIBukkitConfig(this))
		CommandAPI.onEnable()
		SayHelloCommand().register()
	}

}