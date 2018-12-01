package io.github.jorelali.commandapi.api;

public class CommandPermission {

	public static CommandPermission OP = new CommandPermission(PermissionNode.OP);
	
	public static CommandPermission NONE = new CommandPermission(PermissionNode.NONE);
	
	public static CommandPermission fromString(String permission) {
		return new CommandPermission(permission);
	}

	private String permission;
	private PermissionNode permissionNode;
	
	protected enum PermissionNode {
		/**
		 * A player that has to be an operator to run a command
		 */
		OP, 
		
		/**
		 * Command can be run with no permissions
		 */
		NONE;
	}
	
	/**
	 * Represents a single permission required to execute a command
	 * @param permission The permission node the sender requires to run this command
	 */
	private CommandPermission(String permission) {
		this.permission = permission;
		this.permissionNode = null;
	}
	
	/**
	 * Represents either no permission or OP status in order to run a command
	 * @param permissionNode The enumerated type of what permission is required to run this command
	 */
	private CommandPermission(PermissionNode permissionNode) {
		this.permission = null;
		this.permissionNode = permissionNode;
	}
	
	protected String getPermission() {
		return this.permission;
	}
	
	protected PermissionNode getPermissionNode() {
		return this.permissionNode;
	}
	
	@Override
	public String toString() {
		if(permissionNode != null) {
			if(permissionNode == PermissionNode.OP)
				return "OP";
			else
				return "NONE";
		} else {
			return permission;
		}
	}
	
}
