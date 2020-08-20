package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.Keyed;
import org.bukkit.command.CommandSender;
import org.bukkit.loot.LootTable;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit LootTable object
 */
public class LootTableArgument extends Argument implements ICustomProvidedArgument, ISafeOverrideableSuggestions<LootTable> {
	
	public LootTableArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentMinecraftKeyRegistered());
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return LootTable.class;
	}
	
	@Override
	public SuggestionProviders getSuggestionProvider() {
		return SuggestionProviders.LOOT_TABLES;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.LOOT_TABLE;
	}

	@Override
	public Argument safeOverrideSuggestions(LootTable... suggestions) {
		return super.overrideSuggestions(sMap0(fromKey(Keyed::getKey), suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(Function<CommandSender, LootTable[]> suggestions) {
		return super.overrideSuggestions(sMap1(fromKey(Keyed::getKey), suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], LootTable[]> suggestions) {
		return super.overrideSuggestions(sMap2(fromKey(Keyed::getKey), suggestions));
	}
}
