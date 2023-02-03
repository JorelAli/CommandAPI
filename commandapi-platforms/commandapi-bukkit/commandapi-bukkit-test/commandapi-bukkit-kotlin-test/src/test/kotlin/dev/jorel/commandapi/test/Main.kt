package dev.jorel.commandapi.test

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import dev.jorel.commandapi.CommandAPILogger
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader
import java.io.File

class Main : JavaPlugin {
	override fun onLoad() {
		dataFolder.mkdirs()
		CommandAPI.setLogger(CommandAPILogger.fromJavaLogger(logger))
		CommandAPI.onLoad(
			CommandAPIBukkitConfig(this).useLatestNMSVersion(true) // Doesn't matter because we implement CommandAPIVersionHandler here
				.silentLogs(true).dispatcherFile(File(dataFolder, "command_registration.json"))
		)
	}

	override fun onEnable() {
		CommandAPI.onEnable()
	}

	override fun onDisable() {
		CommandAPI.onDisable()
	}

	// Additional constructors required for MockBukkit
	constructor() : super() {}
	constructor(loader: JavaPluginLoader?, description: PluginDescriptionFile?, dataFolder: File?, file: File?) : super(loader!!, description!!, dataFolder!!, file!!) {}
}