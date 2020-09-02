package dev.jorel.commandapi.arguments;

import java.util.UUID;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents a UUID
 */
public class UUIDArgument extends SafeOverrideableArgument<UUID> {
	
	/**
	 * A UUID argument. Represents an in-game entity UUID 
	 */
	public UUIDArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentUUID(), UUID::toString);
	}

	@Override
	public Class<?> getPrimitiveType() {
		return UUID.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.UUID;
	}
}
