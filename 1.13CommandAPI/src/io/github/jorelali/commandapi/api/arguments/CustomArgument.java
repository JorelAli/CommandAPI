package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import io.github.jorelali.commandapi.api.CommandPermission;

@SuppressWarnings("unchecked")
public class CustomArgument<S> implements Argument, OverrideableSuggestions {

	/**
	 * MessageBuilder used for generating fancy error messages for CustomArguments
	 */
	public static class MessageBuilder {
		StringBuilder builder;
		
		public MessageBuilder() {
			builder = new StringBuilder();
		}
		
		public MessageBuilder(String str) {
			builder = new StringBuilder(str);
		}
		
		public MessageBuilder appendArgInput() {
			builder.append("%input%");
			return this;
		}
		
		public MessageBuilder appendFullInput() {
			builder.append("%finput%");
			return this;
		}
		
		public MessageBuilder appendHere() {
			builder.append("%here%");
			return this;
		}
		
		public MessageBuilder append(String str) {
			builder.append(str);
			return this;
		}
		
		public MessageBuilder append(Object obj) {
			builder.append(obj);
			return this;
		}
		
		@Override
		public String toString() {
			return builder.toString();
		}
	}
	
	@SuppressWarnings("serial")
	public static class CustomArgumentException extends Exception {

		final String errorMessage;
		final MessageBuilder errorMessageBuilder;
		
		public CustomArgumentException(String errorMessage) {
			this.errorMessage = errorMessage;
			this.errorMessageBuilder = null;
		}
		
		public CustomArgumentException(MessageBuilder errorMessage) {
			this.errorMessage = null;
			this.errorMessageBuilder = errorMessage;
		}
		
		public CommandSyntaxException toCommandSyntax(String result, @SuppressWarnings("rawtypes") CommandContext cmdCtx) {
			if(errorMessage == null) {
				//Deal with MessageBuilder
				String errorMsg = errorMessageBuilder.toString().replace("%input%", result).replace("%finput%", cmdCtx.getInput()).replace("%here%", "<--[HERE]");
				return new SimpleCommandExceptionType(() -> {return errorMsg;}).create();
			} else {
				//Deal with String
				return new SimpleCommandExceptionType(new LiteralMessage(errorMessage)).create();
			}
		}
		
	}
	
	@FunctionalInterface
	public static interface CustomArgumentFunction<I, O> {
		public O apply(I i) throws CustomArgumentException;
	}
	
	public static void throwError(String errorMessage) throws CustomArgumentException {
		throw new CustomArgumentException(errorMessage);
	}
	
	public static void throwError(MessageBuilder errorMessage) throws CustomArgumentException {
		throw new CustomArgumentException(errorMessage);
	}
	
	private CustomArgumentFunction<String, S> parser;
	
	/**
	 * A Custom argument.
	 */
	public CustomArgument(CustomArgumentFunction<String, S> parser) {
		this.parser = parser;
	}
	
	@Override
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) StringArgumentType.string();
	}

	@Override
	public Class<S> getPrimitiveType() {
		return null;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
	
	public CustomArgumentFunction<String, S> getParser() {
		return parser;
	}
	
	private CommandPermission permission = CommandPermission.NONE;
	
	@Override
	public CustomArgument<S> withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}

	@Override
	public CommandPermission getArgumentPermission() {
		return permission;
	}
	
	private String[] suggestions;
	
	@Override
	public CustomArgument<S> overrideSuggestions(String... suggestions) {
		this.suggestions = suggestions;
		return this;
	}
	
	@Override
	public String[] getOverriddenSuggestions() {
		return suggestions;
	}
	
	/*
	 * TODO: 
	 * - Add DynamicArguments
	 * - Add documentation to constructor, getParser(), getPredicate etc... methods
	 * 
	 */
}
