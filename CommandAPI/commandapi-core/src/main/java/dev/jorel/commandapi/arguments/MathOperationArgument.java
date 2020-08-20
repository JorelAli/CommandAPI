package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.wrappers.MathOperation;

/**
 * An argument that represents Minecraft scoreboard math operations
 */
public class MathOperationArgument extends Argument implements ISafeOverrideableSuggestions<MathOperation> {

	/**
	 * A MathOperation argument. Represents a math operation (e.g. addition, subtraction etc.)
	 */
	public MathOperationArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentMathOperation());
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return MathOperation.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.MATH_OPERATION;
	}

	@Override
	public Argument safeOverrideSuggestions(MathOperation... suggestions) {
		return super.overrideSuggestions(sMap0(MathOperation::toString, suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(Function<CommandSender, MathOperation[]> suggestions) {
		return super.overrideSuggestions(sMap1(MathOperation::toString, suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], MathOperation[]> suggestions) {
		return super.overrideSuggestions(sMap2(MathOperation::toString, suggestions));
	}
}
