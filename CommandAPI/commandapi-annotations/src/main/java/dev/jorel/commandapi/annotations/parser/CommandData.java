package dev.jorel.commandapi.annotations.parser;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;

import dev.jorel.commandapi.CommandPermission;

public class CommandData extends CommandElement {

	private final TypeElement typeElement;
	
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
	
	public CommandData(TypeElement classElement, boolean isSubcommand) {
		this.typeElement = classElement;
		this.arguments = new ArrayList<>();
		this.subcommandClasses = new ArrayList<>();
		this.subcommandMethods = new ArrayList<>();
		this.suggestionClasses = new ArrayList<>();
		
		this.isSubcommand = isSubcommand;
	}

	public void setName(String name) {
		this.name = name;
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

	public void setHelp(String help, String shortDescription) {
		this.help = help;
		this.shortDescriptionHelp = shortDescription;
	}

	@Override
	public void emit(PrintWriter out, int currentIndentation) {
		// Here, we're just writing the command itself (new CommandAPICommand ...) and nothing else?

		out.println("public void register(" + typeElement.getSimpleName() + " command) {");
		out.println();
		indent();
		
		for(SubcommandMethod method : subcommandMethods) {
			out.println(indentation() + "new CommandAPICommand(\"" + name + "\")");
			indent();
			for(ArgumentData argument : arguments) {
				argument.emit(out, indentation);
			}
			method.emit(out, indentation);
			
			out.println();
			dedent();
		}
		
		dedent();
		out.println("}"); // End public void register()
	}

}
