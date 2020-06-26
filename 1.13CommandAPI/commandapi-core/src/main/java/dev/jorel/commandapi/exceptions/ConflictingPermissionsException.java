package dev.jorel.commandapi.exceptions;

import dev.jorel.commandapi.CommandPermission;

/**
 * An exception caused when the same permission is registered to a command
 */
@SuppressWarnings("serial")
public class ConflictingPermissionsException extends RuntimeException {
	
	public ConflictingPermissionsException(String command, CommandPermission currentPermission, CommandPermission conflictingPermission) {
		super("The command " + command + " already has a permission assigned to it! Tried to assign "
				+ conflictingPermission.toString() + ", but " + currentPermission + " already exists!");
	}
	
}
