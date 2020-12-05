package dev.jorel.commandapi.arguments;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import org.bukkit.command.CommandSender;

import com.mojang.brigadier.arguments.ArgumentType;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.IStringTooltip;
import dev.jorel.commandapi.StringTooltip;
import dev.jorel.commandapi.wrappers.InputParser;

/**
 * The core abstract class for Command API arguments
 */
public abstract class Argument implements IOverrideableSuggestions {

	/**
	 * Returns the primitive type of the current Argument. After executing a
	 * command, this argument should yield an object of this returned class.
	 * 
	 * @return the type that this argument yields when the command is run
	 */
	public abstract Class<?> getPrimitiveType();

	/**
	 * Returns the argument type for this argument.
	 * 
	 * @return the argument type for this argument
	 */
	public abstract CommandAPIArgumentType getArgumentType();

	////////////////////////
	// Raw Argument Types //
	////////////////////////

	private final String nodeName;
	private final ArgumentType<?> rawType;

	/**
	 * Constructs an argument with a given NMS/brigadier type.
	 * 
	 * @param nodeName the name to assign to this argument node 
	 * @param rawType the NMS or brigadier type to be used for this argument
	 */
	protected Argument(String nodeName, ArgumentType<?> rawType) {
		this.nodeName = nodeName;
		this.rawType = rawType;
	}

	/**
	 * Returns the NMS or brigadier type for this argument.
	 * 
	 * @return the NMS or brigadier type for this argument
	 */
	public final ArgumentType<?> getRawType() {
		return this.rawType;
	}
	
	/**
	 * Returns the name of this argument's node
	 * @return the name of this argument's node
	 */
	public final String getNodeName() {
		return this.nodeName;
	}

	/////////////////
	// Suggestions //
	/////////////////

	private Optional<BiFunction<CommandSender, Object[], IStringTooltip[]>> suggestions = Optional.empty();
		
	/**
	 * Maps a String[] of suggestions to a StringTooltip[], using StringTooltip.none.
	 * This is used internally by the CommandAPI.
	 * 
	 * @param suggestions the array of suggestions to convert
	 * @return a StringTooltip[] representation of the provided suggestions
	 */
	private final StringTooltip[] fromSuggestions(String[] suggestions) {
		return Arrays.stream(suggestions).map(StringTooltip::none).toArray(StringTooltip[]::new);
	}

	/**
	 * Override the suggestions of this argument with a String array.
	 * 
	 * @param suggestions the string array to override suggestions with
	 * @return the current argument
	 */
	@Override
	public final Argument overrideSuggestions(String... suggestions) {
		this.suggestions = Optional.of((c, m) -> fromSuggestions(suggestions));
		return this;
	}
	
	/**
	 * Override the suggestions of this argument with a <code>Collection&lt;String&gt;</code>.
	 * 
	 * @param suggestions the collection of suggestions to override suggestions with
	 * @return the current argument
	 */
	@Override
	public final Argument overrideSuggestions(Collection<String> suggestions) {
		this.suggestions = Optional.of((c, m) -> fromSuggestions(suggestions.toArray(new String[0])));
		return this;
	}

	/**
	 * Override the suggestions of this argument with a function that maps the
	 * command sender to a String array.
	 * 
	 * @param suggestions the function to override suggestions with
	 * @return the current argument
	 */
	@Override
	public final Argument overrideSuggestions(Function<CommandSender, String[]> suggestions) {
		this.suggestions =  Optional.of((c, m) -> fromSuggestions(suggestions.apply(c)));
		return this;
	}
	
	/**
	 * Override the suggestions of this argument with a function that maps the
	 * command sender and a data set of previously declared arguments to a String array.
	 * 
	 * @param suggestions the function to override suggestions with
	 * @return the current argument
	 */
	@Override
	public final Argument overrideSuggestions(BiFunction<CommandSender, Object[], String[]> suggestions) {
		this.suggestions =  Optional.of((c, m) -> fromSuggestions(suggestions.apply(c, m)));
		return this;
	}
	
	/**
	 * Override the suggestions of this argument with a <code>Collection&lt;IStringTooltip&gt;</code>.
	 * 
	 * @param suggestions the collection of IStringTooltip to override suggestions with
	 * @return the current argument
	 */
	@Override
	public final Argument overrideSuggestionsT(Collection<IStringTooltip> suggestions) {
		this.suggestions = Optional.of((c, m) -> suggestions.toArray(new IStringTooltip[0]));
		return this;
	}
	
	/**
	 * Override the suggestions of this argument with an IStringTooltip array.
	 * 
	 * @param suggestions the IStringTooltip array to override suggestions with
	 * @return the current argument
	 */
	@Override
	public final Argument overrideSuggestionsT(IStringTooltip... suggestions) {
		this.suggestions = Optional.of((c, m) -> suggestions);
		return this;
	}

	/**
	 * Override the suggestions of this argument with a function that maps the
	 * command sender to an IStringTooltip array.
	 * 
	 * @param suggestions the function to override suggestions with
	 * @return the current argument
	 */
	@Override
	public final Argument overrideSuggestionsT(Function<CommandSender, IStringTooltip[]> suggestions) {
		this.suggestions =  Optional.of((c, m) -> suggestions.apply(c));
		return this;
	}
	
	/**
	 * Override the suggestions of this argument with a function that maps the
	 * command sender and a data set of previously declared arguments to an IStringTooltip array.
	 * 
	 * @param suggestions the function to override suggestions with
	 * @return the current argument
	 */
	@Override
	public final Argument overrideSuggestionsT(BiFunction<CommandSender, Object[], IStringTooltip[]> suggestions) {
		this.suggestions =  Optional.of(suggestions);
		return this;
	}

	/**
	 * Returns an optional function that maps the command sender to an IStringTooltip array of
	 * suggestions for the current command
	 * 
	 * @return a function that provides suggestions, or <code>Optional.empty()</code> if there
	 *         are no overridden suggestions.
	 */
	@Override
	public final Optional<BiFunction<CommandSender, Object[], IStringTooltip[]>> getOverriddenSuggestions() {
		return suggestions;
	}

	/////////////////
	// Permissions //
	/////////////////

	private CommandPermission permission = CommandPermission.NONE;

	/**
	 * Assigns the given permission as a requirement to execute this command.
	 * 
	 * @param permission the permission required to execute this command
	 * @return this current argument
	 */
	public final Argument withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}
	
	/**
	 * Assigns the given permission as a requirement to execute this command.
	 * 
	 * @param permission the permission required to execute this command
	 * @return this current argument
	 */
	public final Argument withPermission(String permission) {
		this.permission = CommandPermission.fromString(permission);
		return this;
	}

	/**
	 * Returns the permission required to run this command
	 * @return the permission required to run this command
	 */
	public final CommandPermission getArgumentPermission() {
		return this.permission;
	}
	
	//////////////////
	// Requirements //
	//////////////////
	
	private Predicate<CommandSender> requirements = s -> true;
		
	/**
	 * Returns the requirements required to run this command
	 * @return the requirements required to run this command
	 */
	public final Predicate<CommandSender> getRequirements() {
		return this.requirements;
	}
	
	/**
	 * Adds a requirement that has to be satisfied to use this argument. This method
	 * can be used multiple times and each use of this method will AND its
	 * requirement with the previously declared ones
	 * 
	 * @param requirement the predicate that must be satisfied to use this argument
	 * @return this current argument
	 */
	public final Argument withRequirement(Predicate<CommandSender> requirement) {
		this.requirements = this.requirements.and(requirement);
		return this;
	}
	
	/////////////////
	// Listability //
	/////////////////
	
	private boolean isListed = true;
	
	/**
	 * Returns true if this argument will be listed in the Object args[] of the command executor
	 * @return true if this argument will be listed in the Object args[] of the command executor
	 */
	public boolean isListed() {
		return this.isListed;
	}
	
	/**
	 * Sets whether this argument will be listed in the Object args[] of the command executor
	 * @param listed if true, this argument will be included in the Object args[] of the command executor
	 * @return this current argument
	 */
	public Argument setListed(boolean listed) {
		this.isListed = listed;
		return this;
	}
	
	/////////////
	// Parsing //
	/////////////
	
	private InputParser parser = (s, r) -> {};
	
	public final InputParser getParser() {
		return this.parser;
	}
	
	public final Argument withParser(InputParser parser) {
		this.parser = parser;
		return this;
	}
	
	////////////////////////
	// Optional arguments //
	////////////////////////
	
	private boolean optional = false;
	
	public final Argument setOptional() {
		return setOptional(true);
	}
	
	public final Argument setOptional(boolean optional) {
		this.optional = optional;
		return this;
	}
	
	public final boolean isOptional() {
		return this.optional;
	}
	
}