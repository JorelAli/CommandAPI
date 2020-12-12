package dev.jorel.commandapi.arguments;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;

import com.mojang.brigadier.arguments.ArgumentType;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.IStringTooltip;
import dev.jorel.commandapi.Tooltip;
import dev.jorel.commandapi.wrappers.InputParser;

/**
 * An interface declaring methods required to override argument suggestions
 */
public abstract class SafeOverrideableArgument<S> extends Argument {
	
	private final Function<S, String> mapper;
	private Function<CommandSender, S> defaultValue;

	protected SafeOverrideableArgument(String nodeName, ArgumentType<?> rawType, Function<S, String> mapper) {
		super(nodeName, rawType);
		this.mapper = mapper;
	}
	
	public final void setDefaultValue(Function<CommandSender, S> defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public final Function<CommandSender, S> getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * Override the suggestions of this argument with a custom array.
	 * 
	 * @param suggestions the S array to override suggestions with
	 * @return the current argument
	 */
	@SuppressWarnings("unchecked")
	public final SafeOverrideableArgument<S> safeOverrideSuggestions(S... suggestions) {
		return this.overrideSuggestions(sMap0(mapper, suggestions));
	}
	
	/**
	 * Override the suggestions of this argument with a custom <code>Collection&lt;S&gt;</code>.
	 * 
	 * @param suggestions the <code>Collection&lt;S&gt;</code> to override suggestions with
	 * @return the current argument
	 */
	@SuppressWarnings("unchecked")
	public final SafeOverrideableArgument<S> safeOverrideSuggestions(Collection<S> suggestions) {
		return this.overrideSuggestions(sMap0(mapper, suggestions.toArray((S[]) new Object[0])));
	} 

	/**
	 * Override the suggestions of this argument with a function that maps the
	 * command sender to a custom array.
	 * 
	 * @param suggestions the function to override suggestions with
	 * @return the current argument
	 */
	public final SafeOverrideableArgument<S> safeOverrideSuggestions(Function<CommandSender, S[]> suggestions) {
		return this.overrideSuggestions(sMap1(mapper, suggestions));
	}
	
	/**
	 * Override the suggestions of this argument with a function that maps the
	 * command sender to a custom array.
	 * 
	 * @param suggestions the function to override suggestions with
	 * @return the current argument
	 */
	public final SafeOverrideableArgument<S> safeOverrideSuggestions(BiFunction<CommandSender, Object[], S[]> suggestions) {
		return this.overrideSuggestions(sMap2(mapper, suggestions));
	}
	
	/**
	 * Override the suggestions of this argument with an array of <code>Tooltip&lt;S&gt;</code>,
	 * that represents a safe suggestion and a hover tooltip
	 * 
	 * @param suggestions the suggestions and tooltips to override suggestions with 
	 * @return the current argument
	 */
	@SafeVarargs
	public final SafeOverrideableArgument<S> safeOverrideSuggestionsT(Tooltip<S>... suggestions) {
		return this.overrideSuggestionsT(tMap0(mapper, suggestions));
	};
	
	/**
	 * Override the suggestions of this argument with a <code>Collection&lt;Tooltip&lt;S&gt;&gt;</code>,
	 * that represents a safe suggestion and a hover tooltip
	 * 
	 * @param suggestions the suggestions and tooltips to override suggestions with 
	 * @return the current argument
	 */
	@SuppressWarnings("unchecked")
	public final SafeOverrideableArgument<S> safeOverrideSuggestionsT(Collection<Tooltip<S>> suggestions) {
		return this.overrideSuggestionsT(tMap0(mapper, suggestions.toArray(new Tooltip[0])));
	};
	
	/**
	 * Override the suggestions of this argument with a function mapping the command
	 * sender to an array of <code>Tooltip&lt;S&gt;</code>, that represents a safe suggestion and a
	 * hover tooltip
	 * 
	 * @param suggestions the suggestions and tooltips to override suggestions with
	 * @return the current argument
	 */
	public final SafeOverrideableArgument<S> safeOverrideSuggestionsT(Function<CommandSender, Tooltip<S>[]> suggestions) {
		return this.overrideSuggestionsT(tMap1(mapper, suggestions));
	}
	
	/**
	 * Override the suggestions of this argument with a function mapping the command
	 * sender an previously declared arguments to an array of <code>Tooltip&lt;S&gt;</code>, that
	 * represents a safe suggestion and a hover tooltip
	 * 
	 * @param suggestions the suggestions and tooltips to override suggestions with
	 * @return the current argument
	 */
	public final SafeOverrideableArgument<S> safeOverrideSuggestionsT(BiFunction<CommandSender, Object[], Tooltip<S>[]> suggestions) {
		return this.overrideSuggestionsT(tMap2(mapper, suggestions));
	}
	
	/**
	 * Composes a <code>S</code> to a <code>NamespacedKey</code> mapping function to convert <code>S</code> to a <code>String</code>
	 * @param mapper the mapping function from <code>S</code> to <code>NamespacedKey</code>
	 * @return a composed function that converts <code>S</code> to <code>String</code>
	 */
	static <S> Function<S, String> fromKey(Function<S, NamespacedKey> mapper) {
		return mapper.andThen(NamespacedKey::toString);
	}
	
	////////////////////
	// Implementation //
	////////////////////
	
	/**
	 * Safely overrides the suggestions of this argument with a custom array.
	 * Arguments of type S are guaranteed to succeed in commands if and only if the
	 * mapping function does not fail.
	 * 
	 * @param mapper      the mapping function from S to a String
	 * @param suggestions a S[] of objects to suggest to the command sender
	 * @return the current argument
	 */
	@SafeVarargs
	private final BiFunction<CommandSender, Object[], String[]> sMap0(Function<S, String> mapper, S... suggestions) {
		return (c, m) -> Arrays.stream(suggestions).map(mapper).toArray(String[]::new);
	}
	
	/**
	 * Safely overrides the suggestions of this argument with a custom array.
	 * Arguments of type S are guaranteed to succeed in commands if and only if the
	 * mapping function does not fail.
	 * 
	 * @param mapper      the mapping function from S to a String
	 * @param suggestions a <code>(sender, args) -&gt; S[]</code> of objects to suggest to the command sender where <code>sender</code> is the command sender
	 * @return the current argument
	 */
	private final BiFunction<CommandSender, Object[], String[]> sMap1(Function<S, String> mapper, Function<CommandSender, S[]> suggestions) {
		return (c, m) -> Arrays.stream(suggestions.apply(c)).map(mapper).toArray(String[]::new);
	}
	
	/**
	 * Safely overrides the suggestions of this argument with a custom array.
	 * Arguments of type S are guaranteed to succeed in commands if and only if the
	 * mapping function does not fail.
	 * 
	 * @param mapper      the mapping function from S to a String
	 * @param suggestions a <code>(sender, args) -&gt; S[]</code> of objects to suggest to the command sender where <code>sender</code> is the command sender and <code>args</code> is the array of previously defined arguments 
	 * @return the current argument
	 */
	private final BiFunction<CommandSender, Object[], String[]> sMap2(Function<S, String> mapper, BiFunction<CommandSender, Object[], S[]> suggestions) {
		return (c, m) -> Arrays.stream(suggestions.apply(c, m)).map(mapper).toArray(String[]::new);
	}
	
	/**
	 * sMap0, but for <code>Tooltip&lt;S&gt;</code> instead of Strings
	 * @param mapper the mapping function from S to a String
	 * @param suggestions a <code>Tooltip<S>[]</code> of tooltips and suggestions to send to the user
	 * @return the current argument
	 * @see SafeOverrideableArgument#sMap0(Function, Object...)
	 */
	@SafeVarargs
	private final BiFunction<CommandSender, Object[], IStringTooltip[]> tMap0(Function<S, String> mapper, Tooltip<S>... suggestions) {
		return (c, m) -> Arrays.stream(suggestions).map(Tooltip.build(mapper)).toArray(IStringTooltip[]::new);
	}
	
	/**
	 * sMap1, but for <code>Tooltip&lt;S&gt;</code> instead of Strings
	 * @param mapper the mapping function from S to a String
	 * @param suggestions a <code>(sender) -&gt; Tooltip&lt;S&gt;[]</code> of tooltips and suggestions to send to the user
	 * @return the current argument
	 * @see SafeOverrideableArgument#sMap1(Function, Function)
	 */
	private final BiFunction<CommandSender, Object[], IStringTooltip[]> tMap1(Function<S, String> mapper, Function<CommandSender, Tooltip<S>[]> suggestions) {
		return (c, m) -> Arrays.stream(suggestions.apply(c)).map(Tooltip.build(mapper)).toArray(IStringTooltip[]::new);
	}
	
	/**
	 * sMap2, but for <code>Tooltip&lt;S&gt;</code> instead of Strings
	 * @param mapper the mapping function from S to a String
	 * @param suggestions a <code>(sender, args) -&gt; Tooltip&lt;S&gt;[]</code> of tooltips and suggestions to send to the user
	 * @return the current argument
	 * @see SafeOverrideableArgument#sMap2(Function, BiFunction)
	 */
	private final BiFunction<CommandSender, Object[], IStringTooltip[]> tMap2(Function<S, String> mapper, BiFunction<CommandSender, Object[], Tooltip<S>[]> suggestions) {
		return (c, m) -> Arrays.stream(suggestions.apply(c, m)).map(Tooltip.build(mapper)).toArray(IStringTooltip[]::new);
	}
	
	/////////////////////////////////////////////////////////////////////
	// Overrides (fix type-level issues with SafeOverrideableArgument) //
	/////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("unchecked")
	@Override
	public SafeOverrideableArgument<S> overrideSuggestions(BiFunction<CommandSender, Object[], String[]> suggestions) {
		return (SafeOverrideableArgument<S>) super.overrideSuggestions(suggestions);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public SafeOverrideableArgument<S> overrideSuggestions(Collection<String> suggestions) {
		return (SafeOverrideableArgument<S>) super.overrideSuggestions(suggestions);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public SafeOverrideableArgument<S> overrideSuggestions(Function<CommandSender, String[]> suggestions) {
		return (SafeOverrideableArgument<S>) super.overrideSuggestions(suggestions);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public SafeOverrideableArgument<S> overrideSuggestions(String... suggestions) {
		return (SafeOverrideableArgument<S>) super.overrideSuggestions(suggestions);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public SafeOverrideableArgument<S> overrideSuggestionsT(BiFunction<CommandSender, Object[], IStringTooltip[]> suggestions) {
		return (SafeOverrideableArgument<S>) super.overrideSuggestionsT(suggestions);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public SafeOverrideableArgument<S> overrideSuggestionsT(Collection<IStringTooltip> suggestions) {
		return (SafeOverrideableArgument<S>) super.overrideSuggestionsT(suggestions);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public SafeOverrideableArgument<S> overrideSuggestionsT(Function<CommandSender, IStringTooltip[]> suggestions) {
		return (SafeOverrideableArgument<S>) super.overrideSuggestionsT(suggestions);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public SafeOverrideableArgument<S> overrideSuggestionsT(IStringTooltip... suggestions) {
		return (SafeOverrideableArgument<S>) super.overrideSuggestionsT(suggestions);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public SafeOverrideableArgument<S> withPermission(CommandPermission permission) {
		return (SafeOverrideableArgument<S>) super.withPermission(permission);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public SafeOverrideableArgument<S> withPermission(String permission) {
		return (SafeOverrideableArgument<S>) super.withPermission(permission);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public SafeOverrideableArgument<S> withRequirement(Predicate<CommandSender> requirement) {
		return (SafeOverrideableArgument<S>) super.withRequirement(requirement);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public SafeOverrideableArgument<S> setListed(boolean listed) {
		return (SafeOverrideableArgument<S>) super.setListed(listed);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public SafeOverrideableArgument<S> withParser(InputParser parser) {
		return (SafeOverrideableArgument<S>) super.withParser(parser);
	}
	
	////////////////////////
	// Optional arguments //
	////////////////////////
	
	public final PlaceholderArgument asOptional() {
		PlaceholderArgument result = new PlaceholderArgument(this);
		result.setOptional(false);
		result.setListed(this.isListed());
		result.withPermission(this.getArgumentPermission());
		result.withRequirement(this.getRequirements());
		result.setDefaultValue(this.defaultValue);
		result.withParser(this.getParser());
		//TODO: Add suggestions... etc.
	
		return result;
	}
	
	public class PlaceholderArgument extends SafeOverrideableArgument<S> {
				
		protected PlaceholderArgument(SafeOverrideableArgument<S> base) {
			super(base.getNodeName(), base.getRawType(), base.mapper);
		}

		@Override
		public Class<?> getPrimitiveType() {
			return null; // Not needed
		}

		@Override
		public CommandAPIArgumentType getArgumentType() {
			return null; // Not needed
		}
		
	}
	
}
