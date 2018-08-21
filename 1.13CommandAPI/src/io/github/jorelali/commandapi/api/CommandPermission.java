package io.github.jorelali.commandapi.api;

public class CommandPermission {

	private String[] permissions;
	private PermissionNode permissionNode;
	
	public enum PermissionNode {
		OP, NONE;
	}
	
	/**
	 * Represents a single permission required to execute a command
	 * @param permission The permission node the sender requires to run this command
	 */
	public CommandPermission(String permission) {
		this.permissions = new String[] {permission};
	}
	
	/**
	 * Represents multiple permission nodes that are required to execute a command 
	 * @param permissions The array of permission nodes the sender requires to run this command
	 */
	public CommandPermission(String... permissions) {
		this.permissions = permissions;
	}
	
	/**
	 * Represents either no permission or OP status in order to run a command
	 * @param permission The enumerated type of what permission is required to run this command
	 */
	public CommandPermission(PermissionNode permission) {
		this.permissions = null;
		this.permissionNode = permission;
	}
	
	protected String[] getPermissions() {
		return this.permissions;
	}
	
	protected PermissionNode getPermissionNode() {
		return permissionNode;
	}
	
}
