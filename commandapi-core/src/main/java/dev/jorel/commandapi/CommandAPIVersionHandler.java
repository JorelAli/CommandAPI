package dev.jorel.commandapi;

/**
 * This file handles loading the correct platform implementation. The CommandAPIVersionHandler
 * file within the commandapi-core module is NOT used at run time. Instead, the platform modules
 * replace this class with their own version that handles loads the correct class for thier version
 */
public interface CommandAPIVersionHandler {

	/**
	 * Returns an instance of the version's implementation of AbstractPlatform.
	 *
	 * @return an instance of AbstractPlatform which can run on the currently active server
	 */
	static CommandAPIPlatform<?, ?, ?> getPlatform() {
		throw new RuntimeException("You have the wrong copy of the CommandAPI! If you're shading, did you use commandapi-core instead of commandapi-{platform}-shade?");
	}
}
