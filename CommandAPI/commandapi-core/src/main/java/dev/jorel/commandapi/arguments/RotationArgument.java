package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.wrappers.Rotation;

/**
 * An argument that represents rotation as pitch and yaw
 */
public class RotationArgument extends Argument implements ISafeOverrideableSuggestions<Rotation> {

	/**
	 * A Rotation argument. Represents pitch and yaw
	 */
	public RotationArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentRotation());
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return Rotation.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.ROTATION;
	}

	@Override
	public Argument safeOverrideSuggestions(Rotation... suggestions) {
		return super.overrideSuggestions(sMap0(Rotation::toString, suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(Function<CommandSender, Rotation[]> suggestions) {
		return super.overrideSuggestions(sMap1(Rotation::toString, suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], Rotation[]> suggestions) {
		return super.overrideSuggestions(sMap2(Rotation::toString, suggestions));
	}
}
