package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import io.github.jorelali.commandapi.api.CommandAPIHandler;
import io.github.jorelali.commandapi.api.CommandPermission;

@SuppressWarnings("unchecked")
public class CustomArgument<S> implements Argument, OverrideableSuggestions {
	
	/**
	 * Throws an error to be handled by the command execution handler. When this error
	 * is thrown, the original command is <i>not</i> executed and the provided error message
	 * is displayed to the CommandSender
	 * @param errorMessage The error message to display to the CommandSender
	 * @throws CustomArgumentException An exception that is handled internally
	 */
	public static void throwError(String errorMessage) throws CustomArgumentException {
		throw new CustomArgumentException(errorMessage);
	}
	
	/**
	 * Throws an error to be handled by the command execution handler. When this error
	 * is thrown, the original command is <i>not</i> executed and the provided error message
	 * is displayed to the CommandSender
	 * @param errorMessage The error message to display to the CommandSender
	 * @throws CustomArgumentException An exception that is handled internally
	 */
	public static void throwError(MessageBuilder errorMessage) throws CustomArgumentException {
		throw new CustomArgumentException(errorMessage);
	}
	
	private CustomArgumentFunction<S> parser;
	private boolean keyed;
	
	/**
	 * Creates a CustomArgument with a valid parser, defaults to non-keyed argument
	 * 
	 * @param parser
	 *            A CustomArgumentFunction that maps a String to the object of your choice.
	 *            The String input is the text that the CommandSender inputs for
	 *            this argument
	 *            
	 * @see #CustomArgument(CustomArgumentFunction, boolean)
	 */
	public CustomArgument(CustomArgumentFunction<S> parser) {
		this(parser, false);
	}
	
	/**
	 * Creates a CustomArgument with a valid parser
	 * 
	 * @param parser
	 *            A CustomArgumentFunction that maps a String to the object of your choice.
	 *            The String input is the text that the CommandSender inputs for
	 *            this argument
	 * @param keyed Whether this argument can accept Minecraft's <code>NamespacedKey</code> as
	 * valid arguments
	 */
	public CustomArgument(CustomArgumentFunction<S> parser, boolean keyed) {
		this.parser = parser;
		this.keyed = keyed;
	}
	
	@Override
	public <T> ArgumentType<T> getRawType() {
		if(keyed) {
			return (ArgumentType<T>) CommandAPIHandler.getNMS()._ArgumentMinecraftKeyRegistered();
		} else {
			return (ArgumentType<T>) StringArgumentType.string();
		}
		
	}

	@Override
	public Class<S> getPrimitiveType() {
		return null;
	}
	
	public CustomArgumentFunction<S> getParser() {
		return parser;
	}
	
	private CommandPermission permission = null;
	
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

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.CUSTOM;
	}
	
	/**
	 * MessageBuilder is used to create error messages for invalid argument inputs
	 */
	public static class MessageBuilder {
		StringBuilder builder;
		
		/**
		 * Create a blank message
		 */
		public MessageBuilder() {
			builder = new StringBuilder();
		}
		
		/**
		 * Create a message with an input string
		 * @param str The string to start the message with
		 */
		public MessageBuilder(String str) {
			builder = new StringBuilder(str);
		}
		
		/**
		 * Appends the argument input that the CommandSender used in this command.<br>
		 * For example, if <code>/foo bar</code> was executed and an error occurs
		 * with the CustomArgument <code>bar</code>, then the arg input will append
		 * <code>bar</code> to the end of the message.<br><br> This input is determined
		 * at runtime, and is stored as <code>%input%</code> until executed 
		 * @return A reference to this object
		 */
		public MessageBuilder appendArgInput() {
			builder.append("%input%");
			return this;
		}
		
		/**
		 * Appends the whole input that the CommandSender used in this command.<br>
		 * For example, if <code>/foo bar</code> was executed, then <code>foo bar</code> will be
		 * appended to the end of the message.<br><br> This input is determined
		 * at runtime, and is stored as <code>%finput%</code> until executed 
		 * @return A reference to this object
		 */
		public MessageBuilder appendFullInput() {
			builder.append("%finput%");
			return this;
		}
		
		/**
		 * Appends <code><--[HERE]</code> to the end of the message
		 * @return A reference to this object
		 */
		public MessageBuilder appendHere() {
			builder.append("<--[HERE]");
			return this;
		}
		
		/**
		 * Appends a string to the end of this message
		 * @param str The string to append to the end of this message
		 * @return A reference to this object
		 */
		public MessageBuilder append(String str) {
			builder.append(str);
			return this;
		}
		
		/**
		 * Appends an object to the end of this message
		 * @param obj The object to append to the end of this message
		 * @return A reference to this object
		 */
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
		
		private CustomArgumentException(String errorMessage) {
			this.errorMessage = errorMessage;
			this.errorMessageBuilder = null;
		}
		
		private CustomArgumentException(MessageBuilder errorMessage) {
			this.errorMessage = null;
			this.errorMessageBuilder = errorMessage;
		}
		
		public CommandSyntaxException toCommandSyntax(String result, CommandContext<?> cmdCtx) {
			if(errorMessage == null) {
				//Deal with MessageBuilder
				String errorMsg = errorMessageBuilder.toString().replace("%input%", result).replace("%finput%", cmdCtx.getInput());
				return new SimpleCommandExceptionType(() -> {return errorMsg;}).create();
			} else {
				//Deal with String
				return new SimpleCommandExceptionType(new LiteralMessage(errorMessage)).create();
			}
		}
		
	}
	
	@FunctionalInterface
	public static interface CustomArgumentFunction<S> {
		public S apply(String input) throws CustomArgumentException;
	}
}
