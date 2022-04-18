package dev.jorel.commandapi.annotations.parser;

import java.util.ArrayList;
import java.util.List;

import dev.jorel.commandapi.CommandPermission;

public class CommandData {

	// The name of the command
	private String name;

	// Command name aliases. This is declared in @Command or @Subcommand and are the
	// non-first values present
	private String[] aliases;

	// Argument fields declared in this command class, in order
	private List<ArgumentData> arguments;

	// Inner subcommand classes
	private List<CommandData> subcommandClasses;

	// Subcommand methods
	private List<SubcommandMethod> subcommandMethods;

	private boolean isSubcommand;

	// Suggestion classes
	private List<SuggestionClass> suggestionClasses;

	// Permission for this command, if any. Implemented from @NeedsOp or @Permission
	private CommandPermission permission;

	// Help, if any. Retrieved from @Help
	private String help;
	private String shortDescriptionHelp;
	
	public CommandData() {
		this.arguments = new ArrayList<>();
		this.subcommandClasses = new ArrayList<>();
		this.subcommandMethods = new ArrayList<>();
		this.suggestionClasses = new ArrayList<>();
	}
	
	// GETTERS and SETTERS

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getAliases() {
		return aliases;
	}

	public void setAliases(String[] aliases) {
		this.aliases = aliases;
	}

	public List<ArgumentData> getArguments() {
		return arguments;
	}

	public void addArgument(ArgumentData argument) {
		this.arguments.add(argument);
	}

	public List<CommandData> getSubcommandClasses() {
		return subcommandClasses;
	}

	public void addSubcommandClass(CommandData subcommandClass) {
		this.subcommandClasses.add(subcommandClass);
	}

	public List<SubcommandMethod> getSubcommandMethods() {
		return subcommandMethods;
	}

	public void addSubcommandMethod(SubcommandMethod subcommandMethod) {
		this.subcommandMethods.add(subcommandMethod);
	}

	public boolean isSubcommand() {
		return isSubcommand;
	}

	public void setSubcommand(boolean isSubcommand) {
		this.isSubcommand = isSubcommand;
	}

	public List<SuggestionClass> getSuggestionClasses() {
		return suggestionClasses;
	}

	public void addSuggestionClass(SuggestionClass suggestionClass) {
		this.suggestionClasses.add(suggestionClass);
	}

	public CommandPermission getPermission() {
		return permission;
	}

	public void setPermission(CommandPermission permission) {
		this.permission = permission;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public String getShortDescriptionHelp() {
		return shortDescriptionHelp;
	}

	public void setShortDescriptionHelp(String shortDescriptionHelp) {
		this.shortDescriptionHelp = shortDescriptionHelp;
	}

}
