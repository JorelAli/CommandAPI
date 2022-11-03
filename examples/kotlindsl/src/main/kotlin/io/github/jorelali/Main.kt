package io.github.jorelali

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIConfig
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

	override fun onEnable() {
		CommandAPI.onLoad(CommandAPIConfig())
		CommandAPI.onEnable(this)
		SayHelloCommand().register()
	}

}