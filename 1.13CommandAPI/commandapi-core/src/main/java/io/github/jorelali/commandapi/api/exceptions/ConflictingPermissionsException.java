package io.github.jorelali.commandapi.api.exceptions;

import io.github.jorelali.commandapi.api.CommandPermission;

@SuppressWarnings("serial")
public class ConflictingPermissionsException extends RuntimeException {
	
	public ConflictingPermissionsException(String command, CommandPermission currentPermission, CommandPermission conflictingPermission) {
		super("The command " + command + " already has a permission assigned to it! Tried to assign "
				+ conflictingPermission.toString() + ", but " + currentPermission + " already exists!");
	}
	
}
