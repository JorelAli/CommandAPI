package dev.jorel.commandapi.arguments;

import org.bukkit.Sound;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit Sound object
 */
public class SoundArgument extends SafeOverrideableArgument<Sound> implements ICustomProvidedArgument {
	
	/**
	 * A Sound argument. Represents Bukkit's Sound object
	 * @param nodeName the name of the node for this argument
	 */
	public SoundArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentMinecraftKeyRegistered(), CommandAPIHandler.getInstance().getNMS()::convert);
	}

	@Override
	public Class<?> getPrimitiveType() {
		return Sound.class;
	}

	@Override
	public SuggestionProviders getSuggestionProvider() {
		return SuggestionProviders.SOUNDS;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SOUND;
	}
}
