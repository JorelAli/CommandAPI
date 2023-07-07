package dev.jorel.commandapi.annotations.parser;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.annotations.Logging;
import dev.jorel.commandapi.annotations.Utils;
import dev.jorel.commandapi.annotations.arguments.AMultiLiteralArgument;

public class CommandData extends CommandElement {

	private final TypeElement typeElement;
	
	public TypeElement getTypeElement() {
		return typeElement;
	}

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
	
	private final CommandData parent;
	
	private final ProcessingEnvironment processingEnv;
	
	public CommandData getParent() {
		return parent;
	}

	public CommandData(Logging logging, TypeElement classElement, boolean isSubcommand, ProcessingEnvironment processingEnv, CommandData parent) {
		super(logging);
		this.typeElement = classElement;
		this.arguments = new ArrayList<>();
		this.subcommandClasses = new ArrayList<>();
		this.subcommandMethods = new ArrayList<>();
		this.suggestionClasses = new ArrayList<>();
		
		this.isSubcommand = isSubcommand;
		this.processingEnv = processingEnv;
		this.parent = parent;
	}

	public ProcessingEnvironment getProcessingEnv() {
		return this.processingEnv;
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

	public void addSubcommandClass(CommandData subcommandClass) {
		this.subcommandClasses.add(subcommandClass);
	}

	public void addSubcommandMethod(SubcommandMethod subcommandMethod) {
		this.subcommandMethods.add(subcommandMethod);
	}

	public void setPermission(CommandPermission permission) {
		this.permission = permission;
	}

	public void setHelp(String help, String shortDescription) {
		this.help = help;
		this.shortDescriptionHelp = shortDescription;
	}
	
	private static Annotation asMultiLiteralAnnotation(CommandData data) {
		return new AMultiLiteralArgument() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				return AMultiLiteralArgument.class;
			}
			
			@Override
			public String[] value() {
				return Utils.strCons(data.name, data.aliases);
			}
		};
	}
	
	private List<ArgumentData> getInheritedArguments() {
		List<ArgumentData> inheritedArguments = new ArrayList<>();
		CommandData current = this;
		while(current != null) {
			

			//System.out.println("Current: " + current.name + " Args: " + current.arguments.stream().map(x -> x.getArgumentVariableName()).toList());
			inheritedArguments.addAll(0, current.arguments);
			
			// Convert @Subcommand into a multiliteral argument
			if(current.isSubcommand) {
				Annotation multiLiteralArgumentAnnotation = asMultiLiteralAnnotation(current);
				inheritedArguments.add(0, new ArgumentData(super.logging, current.typeElement, multiLiteralArgumentAnnotation, current.permission, current.name, Optional.empty(), Optional.empty(), current, false, false));
			}
			
			current = current.parent;
		}
		return inheritedArguments;
	}

	/**
	 * Here, we're just writing the command itself, as well as any nested subcommands
	 */
	@Override
	public void emit(PrintWriter out, int currentIndentation) {
		this.indentation = currentIndentation;

		// Check that we're going to emit some commands. If not, throw an error 
		if(!isSubcommand) {
			if(subcommandMethods.isEmpty() && subcommandClasses.isEmpty()) {
				logging.warn(typeElement, "Command has no command executors");
			}
		}

		if(!isSubcommand) {
			out.println(indentation() + "/**");
//			out.println(indentation() + " * Registers the following commands:");
			// TODO:
			// /blah <blah> <blah>
			// And so on...
			out.println(indentation() + " * @param command an instance of {@link " + typeElement.getSimpleName() + "} for command execution");
			out.println(indentation() + " */");
			out.println(indentation() + "public static void register(" + typeElement.getSimpleName() + " command) {");
			out.println();
			indent();
		}
		
		// Declare a temporary output stream. We want to compute everything first,
		// then we can construct meaningful JavaDocs!
		ByteArrayOutputStream tempOutStream = new ByteArrayOutputStream();
		PrintWriter tempOut = new PrintWriter(tempOutStream);
		
		// TODO: Don't forget aliases, permissions, help
		// TODO: Are we going to support requirements?
		
		Deque<CommandData> commandTree = new ArrayDeque<>();
		CommandData topLevelCommand = this;
		do {
			commandTree.push(topLevelCommand);
			topLevelCommand = topLevelCommand.getParent();
		} while(topLevelCommand != null);
		
		// Subcommand methods
		for(SubcommandMethod method : subcommandMethods) {
			tempOut.println(indentation() + "new CommandAPICommand(\"" + commandTree.peek().name + "\")");
//			out.print("// /" + commandTree.peek().name);
			indent();
			
			// TODO: Handle formatting of this properly
			emitPermission(tempOut, permission);
			
			for(ArgumentData argument : getInheritedArguments()) {
				argument.emit(tempOut, indentation);
//				out.print(" " + argument.getArgumentVariableName());
			}
			method.emit(tempOut, indentation);
//			out.println();
			tempOut.println();
			dedent();
		}
		
		// Do all subcommand classes and their stuff
		for(CommandData subcommand : subcommandClasses) {
			subcommand.emit(tempOut, indentation);
		}
		
		if(!isSubcommand) {
			dedent();
			tempOut.println(indentation() + "}"); // End public void register()
		}
		
		tempOut.flush();
		out.print(tempOutStream.toString());
	}

}
