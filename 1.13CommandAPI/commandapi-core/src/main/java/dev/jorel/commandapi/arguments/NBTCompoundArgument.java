package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import de.tr7zw.nbtapi.NBTContainer;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.exceptions.NBTAPINotFoundException;

/**
 * An argument that represents an NBTContainer from the NBTAPI
 */
public class NBTCompoundArgument extends Argument implements ISafeOverrideableSuggestions<NBTContainer> {

	/**
	 * An NBT Compound Argument. Represents Minecraft's NBT Compound Tag using the NBT API
	 */
	public NBTCompoundArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentNBTCompound());
		
		if(Bukkit.getPluginManager().getPlugin("NBTAPI") == null) {
			throw new NBTAPINotFoundException(this.getClass());
		}
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return NBTContainer.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.NBT_COMPOUND;
	}

	@Override
	public Argument safeOverrideSuggestions(NBTContainer... suggestions) {
		return super.overrideSuggestions(sMap0(NBTContainer::toString, suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(Function<CommandSender, NBTContainer[]> suggestions) {
		return super.overrideSuggestions(sMap1(NBTContainer::toString, suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], NBTContainer[]> suggestions) {
		return super.overrideSuggestions(sMap2(NBTContainer::toString, suggestions));
	}
}
