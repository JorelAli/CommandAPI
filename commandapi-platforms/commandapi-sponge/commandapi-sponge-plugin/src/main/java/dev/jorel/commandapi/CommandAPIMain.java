package dev.jorel.commandapi;

import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import com.google.inject.Inject;
import dev.jorel.commandapi.CommandAPISpongePluginWrapper;

@Plugin("CommandAPI")
public class CommandAPIMain implements CommandAPISpongePluginWrapper {

	@Inject
	private Logger logger;

	@Listener
	public void onServerStart(final StartedEngineEvent<Server> event) {
		CommandAPI.onEnable(this);
	}

	@Override
	public Server getServer() {
		// TODO Auto-generated method stub
		return null;
	}
}
