/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi;

/**
 * A representation of permission nodes for commands. Represents permission nodes, being op and having all permissions
 */
public class CommandPermission {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (negated ? 1231 : 1237);
		result = prime * result + ((permission == null) ? 0 : permission.hashCode());
		result = prime * result + ((permissionNode == null) ? 0 : permissionNode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommandPermission other = (CommandPermission) obj;
		if (negated != other.negated)
			return false;
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
