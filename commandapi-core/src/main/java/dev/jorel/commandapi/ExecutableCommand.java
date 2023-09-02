package dev.jorel.commandapi;

import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.jorel.commandapi.commandsenders.AbstractCommandSender;
import dev.jorel.commandapi.exceptions.InvalidCommandNameException;

import java.util.List;
import java.util.function.Predicate;

/**
 * This is a base class for {@link AbstractCommandAPICommand} and {@link AbstractCommandTree} command definitions
 *
 * @param <Impl> The class extending this class, used as the return type for chain calls
 * @param <CommandSender> The CommandSender class used by the class extending this class
 */
public abstract class ExecutableCommand<Impl
/// @cond DOX
extends ExecutableCommand<Impl, CommandSender>
/// @endcond
, CommandSender> extends Executable<Impl, CommandSender> {
	// Command names
	/**
	 * The command's name
	 */
	protected final String name;
	/**
	 * The command's aliases
	 */
	protected String[] aliases = new String[0];

	// Command requirements
	/**
	 * The command's permission
	 */
	protected CommandPermission permission = CommandPermission.NONE;
	/**
	 * A predicate that a {@link AbstractCommandSender} must pass in order to execute the command
	 */
	protected Predicate<CommandSender> requirements = s -> true;

	// Command help
	/**
	 * An optional short description for the command
	 */
	protected String shortDescription = null;
	/**
	 * An optional full description for the command
	 */
	protected String fullDescription = null;
	/**
	 * An optional usage description for the command
	 */
	protected String[] usageDescription = null;
	// TODO: Bukkit specific fields probably should not be in platform agnostic classes
	//  Either make HelpTopic platform agnostic or move this field into bukkit-core
	/**
	 * An optional HelpTopic object for the command (for Bukkit)
	 */
	protected Object helpTopic = null;

	ExecutableCommand(final String name) {
		if (name == null || name.isEmpty() || name.contains(" ")) {
			throw new InvalidCommandNameException(name);
		}

		this.name = name;
	}

	/////////////////////
	// Builder methods //
	/////////////////////

	/**
	 * Adds an array of aliases to the current command builder
	 *
	 * @param aliases An array of aliases which can be used to execute this command
	 * @return this command builder
	 */
	public Impl withAliases(String... aliases) {
		this.aliases = aliases;
		return instance();
	}

	/**
	 * Applies a permission to the current command builder
	 *
	 * @param permission The permission node required to execute this command
	 * @return this command builder
	 */
	public Impl withPermission(CommandPermission permission) {
		this.permission = permission;
		return instance();
	}

	/**
	 * Applies a permission to the current command builder
	 *
	 * @param permission The permission node required to execute this command
	 * @return this command builder
	 */
	public Impl withPermission(String permission) {
		this.permission = CommandPermission.fromString(permission);
		return instance();
	}

	/**
	 * Applies a permission to the current command builder
	 *
	 * @param permission The permission node required to execute this command
	 * @return this command builder
	 */
	public Impl withoutPermission(CommandPermission permission) {
		this.permission = permission.negate();
		return instance();
	}

	/**
	 * Applies a permission to the current command builder
	 *
	 * @param permission The permission node required to execute this command
	 * @return this command builder
	 */
	public Impl withoutPermission(String permission) {
		this.permission = CommandPermission.fromString(permission).negate();
		return instance();
	}

	/**
	 * Adds a requirement that has to be satisfied to use this command. This method
	 * can be used multiple times and each use of this method will AND its
	 * requirement with the previously declared ones
	 *
	 * @param requirement the predicate that must be satisfied to use this command
	 * @return this command builder
	 */
	public Impl withRequirement(Predicate<CommandSender> requirement) {
		this.requirements = this.requirements.and(requirement);
		return instance();
	}

	/**
	 * Sets the short description for this command. This is the help which is
	 * shown in the main /help menu.
	 *
	 * @param description the short description for this command
	 * @return this command builder
	 */
	public Impl withShortDescription(String description) {
		this.shortDescription = description;
		return instance();
	}

	/**
	 * Sets the full description for this command. This is the help which is
	 * shown in the specific /help page for this command (e.g. /help mycommand).
	 *
	 * @param description the full description for this command
	 * @return this command builder
	 */
	public Impl withFullDescription(String description) {
		this.fullDescription = description;
		return instance();
	}

	/**
	 * Sets the short and full description for this command. This is a short-hand
	 * for the {@link ExecutableCommand#withShortDescription} and
	 * {@link ExecutableCommand#withFullDescription} methods.
	 *
	 * @param shortDescription the short description for this command
	 * @param fullDescription  the full description for this command
	 * @return this command builder
	 */
	public Impl withHelp(String shortDescription, String fullDescription) {
		this.shortDescription = shortDescription;
		this.fullDescription = fullDescription;
		return instance();
	}

	/**
	 * Sets the full usage for this command. This is the usage which is
	 * shown in the specific /help page for this command (e.g. /help mycommand).
	 *
	 * @param usage the full usage for this command
	 * @return this command builder
	 */
	public Impl withUsage(String... usage) {
		this.usageDescription = usage;
		return instance();
	}

	/////////////////////////
	// Getters and setters //
	/////////////////////////

	/**
	 * Returns the name of this command
	 *
	 * @return the name of this command
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the aliases for this command
	 *
	 * @param aliases the aliases for this command
	 */
	public void setAliases(String[] aliases) {
		this.aliases = aliases;
	}

	/**
	 * Returns an array of aliases that can be used to run this command
	 *
	 * @return an array of aliases that can be used to run this command
	 */
	public String[] getAliases() {
		return aliases;
	}

	/**
	 * Returns the permission associated with this command
	 *
	 * @return the permission associated with this command
	 */
	public CommandPermission getPermission() {
		return this.permission;
	}

	/**
	 * Sets the permission required to run this command
	 *
	 * @param permission the permission required to run this command
	 */
	public void setPermission(CommandPermission permission) {
		this.permission = permission;
	}

	/**
	 * Returns the requirements that must be satisfied to run this command
	 *
	 * @return the requirements that must be satisfied to run this command
	 */
	public Predicate<CommandSender> getRequirements() {
		return this.requirements;
	}

	/**
	 * Sets the requirements that must be satisfied to run this command
	 *
	 * @param requirements the requirements that must be satisfied to run this command
	 */
	public void setRequirements(Predicate<CommandSender> requirements) {
		this.requirements = requirements;
	}

	/**
	 * Returns the short description for this command
	 *
	 * @return the short description for this command
	 */
	public String getShortDescription() {
		return this.shortDescription;
	}

	/**
	 * Returns the full description for this command
	 *
	 * @return the full description for this command
	 */
	public String getFullDescription() {
		return this.fullDescription;
	}

	/**
	 * Returns the usage for this command
	 *
	 * @return the usage for this command
	 */
	public String[] getUsage() {
		return this.usageDescription;
	}

	/**
	 * Returns the {@code HelpTopic} object assigned to this command (For Bukkit)
	 * 
	 * @return the {@code HelpTopic} object assigned to this command (For Bukkit)
	 */
	public Object getHelpTopic() {
		return helpTopic;
	}

	//////////////////
	// Registration //
	//////////////////

	public abstract List<List<String>> getArgumentsAsStrings();

	/**
	 * Overrides a command. Effectively the same as unregistering the command using
	 * CommandAPI.unregister() and then registering the command using .register()
	 */
	public void override() {
		CommandAPI.unregister(this.name, true);
		register();
	}

	/**
	 * Registers this command with the default namespace
	 */
	public void register() {
		register(CommandAPI.getConfiguration().getNamespace());
	}

	/**
	 * Registers the command with a given namespace.
	 *
	 * @param namespace The namespace of this command. This cannot be null
	 * @throws NullPointerException if the namespace is null
	 */
	public void register(String namespace) {
		if (namespace == null) {
			throw new NullPointerException("Parameter 'namespace' was null when registering command /" + this.name + "!");
		}
		((CommandAPIHandler<?, CommandSender, ?>) CommandAPIHandler.getInstance()).registerCommand(this, namespace);
	}

	abstract <Source> Nodes<Source> createCommandNodes();

	record Nodes<Source>(LiteralCommandNode<Source> rootNode, List<LiteralCommandNode<Source>> aliasNodes) {
	}
}
