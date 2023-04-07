package io.github.jorelali

import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

	override fun onEnable() {
		SayHelloCommand().register()
	}
}