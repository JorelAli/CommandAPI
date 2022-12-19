package dev.jorel.commandapi.commandsenders;

/**
 * An interface that represents an object sends commands on some platform
 *
 * @param <Source> The class of the CommandSender being represented
 */
public interface AbstractCommandSender<Source> {

	/**
	 * Tests if this CommandSender has permission to use the given permission node
	 *
	 * @param permissionNode The node to check for
	 * @return True if this CommandSender holds the permission node, and false otherwise
	 */
	boolean hasPermission(String permissionNode);

	/**
	 * Tests if this CommandSender has `operator` status, or the ability to run any command
	 *
	 * @return True if this CommandSender is an operator, and false otherwise
	 */
	boolean isOp();

	/**
	 * @return The underlying CommandSender object
	 */
	Source getSource();
}
