package dev.jorel.commandapi.exceptions;

import dev.jorel.commandapi.CommandPermission;

/**
 * An exception caused when the same permission is registered to a command
 */
@SuppressWarnings("serial")
public class ConflictingPermissionsException extends RuntimeException {
	
	/**
	 * Creates a ConflictingPermissionsException
	 * @param command the command that has conflicting permissions
	 * @param currentPermission the permission that already exists on this command
	 * @param conflictingPermission the permission that was tried to be assigned to the command
	 */
	public ConflictingPermissionsException(String command, CommandPermission currentPermission, CommandPermission conflictingPermission) {
		super("The command " + command + " already has a permission assigned to it! Tried to assign "
				+ conflictingPermission.toString() + ", but " + currentPermission + " already exists!");
	}
	
}
