package io.github.jorelali.commandapi.api.arguments;

import java.util.function.Function;
import java.util.function.Predicate;

import com.mojang.brigadier.arguments.StringArgumentType;

import io.github.jorelali.commandapi.api.CommandPermission;

@SuppressWarnings("unchecked")
public class CustomArgument<S> implements Argument, OverrideableSuggestions {

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
	
	private Class<S> clazz;
	private Function<String, S> parser;
	private Predicate<String> predicate;
	private MessageBuilder builder;
	
	/**
	 * A Custom argument.
	 */
	public CustomArgument(Class<S> clazz, Function<String, S> parser, Predicate<String> predicate, MessageBuilder builder) {
		this.clazz = clazz;
		this.parser = parser;
		this.predicate = predicate;
		this.builder = builder;
	}
	
	@Override
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) StringArgumentType.string();
	}

	@Override
	public Class<S> getPrimitiveType() {
		return clazz;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
	
	public Function<String, S> getParser() {
		return parser;
	}
	
	public Predicate<String> getPredicate() {
		return predicate;
	}
	
	public MessageBuilder getErrorMessage() {
		return builder;
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
}
