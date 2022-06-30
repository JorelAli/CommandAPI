import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandAPIConfig;
import dev.jorel.commandapi.arguments.StringArgument;

public class Main extends JavaPlugin {

	private Runnable onEnabled;
	
	@Override
	public void onLoad() {
		CommandAPI.onLoad(new CommandAPIConfig()
			.useLatestNMSVersion(true)
			.verboseOutput(true)
			.setCustomNMS(new CustomNMS())
		);
	}

	@Override
	public void onEnable() {
		CommandAPI.onEnable(this);

		Bukkit.getScheduler().runTaskLater(this, () -> {
			getLogger().info("Running task");
			onEnabled.run();
		}, 0);
		
		// Once we start registering commands, we lose all control of the
		// test system. We have to schedule the test to end!
		
		new CommandAPICommand("hello")
			.withArguments(new StringArgument("node"))
			.executesPlayer((player, args) -> {
				getLogger().info(("HJi"));
				System.out.println("AAAA");
			})
			.register();
	}

	// Additional constructors required for MockBukkit
	public Main() {
		super();
	}

	public Main(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file, Runnable onEnabled) {
		super(loader, description, dataFolder, file);
		this.onEnabled = onEnabled;
	}

}
