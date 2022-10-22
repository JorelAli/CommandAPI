package dev.jorel.commandapi;

import dev.jorel.commandapi.abstractions.AbstractPlatform;

/**
 * This file handles loadeding the correct platform implementation. The CommandAPIVersionHandler
 * file within the commandapi-core module is NOT used at run time. Instead, the platform modules
 * replace this class with their own version that handles loads the correct class for thier version
 */
public interface CommandAPIVersionHandler {

	/**
	 * Returns an instance of the version's implementation of AbstractPlatform.
	 * @param <Source> the command source type
	 * @return an instance of AbstractPlatform which can run on the specified server version
	 */
	static <CommandSender, Source> AbstractPlatform<CommandSender, Source> getPlatform() {
		throw new RuntimeException("You have the wrong copy of the CommandAPI! If you're shading, did you use commandapi-core instead of commandapi-{platform}-shade?");
	}
}
