package io.github.jorelali.commandapi.api.arguments;

import java.lang.reflect.InvocationTargetException;

import com.mojang.brigadier.arguments.ArgumentType;

@SuppressWarnings("unchecked")
public class LiteralArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	String literal;
	
	/**
	 * A literal argument. Only takes one string value which cannot be modified 
	 */
	public LiteralArgument(final String literal) {
		try {
			this.literal = literal;
			//rawType = LiteralArgumentBuilder.literal(literal);
			//LiteralArgumentBuilder.literal(s)
			rawType = (ArgumentType<?>) ArgumentUtil.getNMS("ArgumentEnchantment").getDeclaredMethod("a").invoke(null);
		} catch (IllegalAccessException | ClassNotFoundException | IllegalArgumentException | SecurityException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) String.class;
	}

	public String getLiteral() {
		return literal;
	}
	
	@Override
	public boolean isSimple() {
		return false;
	}
}
