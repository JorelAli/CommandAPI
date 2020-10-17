package dev.jorel.commandapi.arguments;

import org.bukkit.advancement.Advancement;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit Advancement object
 */
public class AdvancementArgument extends SafeOverrideableArgument<Advancement> implements ICustomProvidedArgument {
	
	/**
	 * Constructs an AdvancementArgument with a given node name
	 * @param nodeName the name of the node for argument
	 */
	public AdvancementArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentMinecraftKeyRegistered(), fromKey(Advancement::getKey));
	}

	@Override
	public Class<?> getPrimitiveType() {
		return Advancement.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.ADVANCEMENT;
	}

	@Override
	public SuggestionProviders getSuggestionProvider() {
		return SuggestionProviders.ADVANCEMENTS;
	}
}
