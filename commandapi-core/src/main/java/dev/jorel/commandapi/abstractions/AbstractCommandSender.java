package dev.jorel.commandapi.abstractions;

public interface AbstractCommandSender<Source> {
	// TODO: figure out what features this and the subclasses of this class need
	
	// We're assuming all command senders have some form of "permission" system
	// in place to determine whether they are capable of actually running a command.
	// In the case of Bukkit, there are two types of permissions: string-based
	// permission and "op". Op permission, if set, always returns true.

	// If no permission system is present, we assume the command sender is always
	// able to run the command

	public boolean hasPermission(String permissionNode);
	
	public boolean isOp();
	
	// Need to be able to get the underlying command sender, NOT the underlying
	// NMS command source (CLW/CSS). If we want to access that, we can do so by
	// adding a second parameter if we so desire, but I don't think we need to
	// go that far
	public Source getSource();
}
