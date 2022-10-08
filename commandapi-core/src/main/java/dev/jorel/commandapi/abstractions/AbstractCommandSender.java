package dev.jorel.commandapi.abstractions;

public interface AbstractCommandSender {
	// TODO: figure out what features this and the subclasses of this class need
	
	// We're assuming all command senders have some form of "permission" system
	// in place to determine whether they are capable of actually running a command.
	// In the case of Bukkit, there are two types of permissions: string-based
	// permission and "op". Op permission, if set, always returns true.

	// If no permission system is present, we assume the command sender is always
	// able to run the command

	public default boolean hasPermission(String permissionNode) {
		return true;
	}
	
	public default boolean isOp() {
		return true;
	}
}
