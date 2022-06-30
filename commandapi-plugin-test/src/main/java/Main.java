import java.io.File;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;

public class Main extends JavaPlugin {

	@Override
	public void onLoad() {
		CommandAPI.onLoad(new CommandAPIConfig().useLatestNMSVersion(true).setCustomNMS(new CustomNMS()));
	}

	@Override
	public void onEnable() {
		CommandAPI.onEnable(this);
	}

	// Additional constructors required for MockBukkit
	public Main() {
		super();
	}

	protected Main(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
		super(loader, description, dataFolder, file);
	}

}
