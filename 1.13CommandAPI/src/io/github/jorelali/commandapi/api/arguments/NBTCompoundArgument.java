package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.Bukkit;

import com.mojang.brigadier.arguments.ArgumentType;

import de.tr7zw.nbtapi.NBTContainer;
import io.github.jorelali.commandapi.api.CommandAPIHandler;
import io.github.jorelali.commandapi.api.CommandPermission;
import io.github.jorelali.commandapi.api.exceptions.NBTNotFoundException;


@SuppressWarnings("unchecked")
public class NBTCompoundArgument implements Argument, OverrideableSuggestions {

	ArgumentType<?> rawType;
	
	/**
	 * An NBT Compound Argument. Represents Minecraft's NBT Compound Tag using the NBT API
	 */
	public NBTCompoundArgument() {

		if(Bukkit.getPluginManager().getPlugin("NBTAPI") == null) {
			throw new NBTNotFoundException(this.getClass());
		}
		
		rawType = CommandAPIHandler.getNMS()._ArgumentNBTCompound();
	}
	
	@Override
	public <T> ArgumentType<T> getRawType() {
		return (ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) NBTContainer.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
	
	private String[] suggestions;
	
	@Override
	public NBTCompoundArgument overrideSuggestions(String... suggestions) {
		this.suggestions = suggestions;
		return this;
	}
	
	@Override
	public String[] getOverriddenSuggestions() {
		return suggestions;
	}

	private CommandPermission permission = null;
	
	@Override
	public NBTCompoundArgument withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}

	@Override
	public CommandPermission getArgumentPermission() {
		return permission;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.NBT_COMPOUND;
	}
}
