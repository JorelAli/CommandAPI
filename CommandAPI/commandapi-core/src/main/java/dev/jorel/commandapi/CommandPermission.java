package dev.jorel.commandapi;

/**
 * A representation of permission nodes for commands. Represents permission nodes, being op and having all permissions
 */
public class CommandPermission {

	/**
	 * Determines if this CommandPermission is equal to another CommandPermission
	 * @param obj the other CommandPermission to check against
	 * @return true if this CommandPermission is equal to the provided object
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommandPermission other = (CommandPermission) obj;
		if (permission == null) {
			if (other.permission != null)
				return false;
		} else if (!permission.equals(other.permission))
			return false;
		if (permissionNode != other.permissionNode)
			return false;
		return true;
	}

	/**
	 * A player that has to be an operator to run a command
	 */
	public static CommandPermission OP = new CommandPermission(PermissionNode.OP);
	
	/**
	 * Command can be run with no permissions
	 */
	public static CommandPermission NONE = new CommandPermission(PermissionNode.NONE);
	
	/**
	 * Generates a new CommandPermission from a permission node
	 * @param permission the permission node
	 * @return a new CommandPermission
	 */
	public static CommandPermission fromString(String permission) {
		return new CommandPermission(permission);
	}

	private String permission;
	private PermissionNode permissionNode;
	private boolean negated = false;
	
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
	
	String getPermission() {
		return this.permission;
	}
	
	PermissionNode getPermissionNode() {
		return this.permissionNode;
	}
	
	boolean isNegated() {
		return this.negated;
	}
	
	CommandPermission negate() {
		this.negated = true;
		return this;
	}
	
	/**
	 * Returns a human-readable string of this CommandPermission
	 * @return a human-readable string of this CommandPermission
	 */
	@Override
	public String toString() {
		final String result;
		if(permissionNode != null) {
			if(permissionNode == PermissionNode.OP) {
				result = "OP";
			} else {
				result =  "NONE";
			}
		} else {
			result = permission;
		}
		return (negated ? "not " : "") + result;
	}
	
	private enum PermissionNode {
		/**
		 * A player that has to be an operator to run a command
		 */
		OP, 
		
		/**
		 * Command can be run with no permissions
		 */
		NONE;
	}
	
}
