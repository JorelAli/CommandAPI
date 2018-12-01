package io.github.jorelali.commandapi.api.exceptions;

import io.github.jorelali.commandapi.api.CommandPermission;

@SuppressWarnings("serial")
public class ConflictingPermissionsException extends RuntimeException {
	
	private String command;
	private CommandPermission currentPermission;
	private CommandPermission conflictingPermission;
	
	public ConflictingPermissionsException(String command, CommandPermission currentPermission, CommandPermission conflictingPermission) {
		this.command = command;
		this.currentPermission = currentPermission;
		this.conflictingPermission = conflictingPermission;
	}
	
	@Override
    public String getMessage() {
		return "The command " + command + " already has a permission assigned to it! Tried to assign "
				+ conflictingPermission.toString() + ", but " + currentPermission + " already exists!";
    }
	
}
