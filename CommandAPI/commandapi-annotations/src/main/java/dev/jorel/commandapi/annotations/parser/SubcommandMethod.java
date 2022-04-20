package dev.jorel.commandapi.annotations.parser;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.lang.model.element.ExecutableElement;

import com.google.common.collect.Streams;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.annotations.Utils;
import dev.jorel.commandapi.annotations.annotations.Subcommand;
import dev.jorel.commandapi.executors.ExecutorType;

public class SubcommandMethod extends CommandElement {

	private final ExecutableElement methodElement;

	// The executor types. Inferred from the first argument of the method, or explicitly declared via @Executors
	private ExecutorType[] executorTypes;

	private final String subcommandName;

	private final String[] aliases;

	private CommandPermission permission;

	/**
	 * Arguments, including arguments "inherited" from the current (and parent(s)) class(es)?
	 */
	private List<ArgumentData> arguments;

	// Whether this is a resulting executor or not. If this method returns void, it's not. If this method returns int, it is. If this method returns anything else, this should be caught by semantics (TODO: Implement in semantics)
	private boolean resulting;

	private final CommandData parent;

	public SubcommandMethod(ExecutableElement methodElement, String name, String[] aliases, CommandPermission permission, List<ArgumentData> arguments, boolean isResulting, CommandData parent) {
		this.methodElement = methodElement;
		this.subcommandName = name;
		this.aliases = aliases;
		this.permission = permission;
		this.arguments = arguments;
		this.resulting = isResulting;
		this.parent = parent;
	}

	@Override
	public void emit(PrintWriter out, int currentIndentation) {
		this.indentation = currentIndentation;

		if (methodElement.getAnnotation(Subcommand.class) != null) {

			// MultiLiteralArgument representing this command
			out.println(indentation() + ".withArguments(");
			indent();
			out.print(indentation() + "new MultiLiteralArgument(");
			out.print(Streams.concat(Stream.of(subcommandName), Arrays.stream(aliases))
					.map(x -> "\"" + x + "\"").collect(Collectors.joining(", ")));
			out.println(")");
			indent();
			out.println(indentation() + ".setListed(false)");

			// Permissions
			emitPermission(out, permission);

			dedent();
			out.println(indentation() + ")");

			dedent();
			// TODO: executor type
			out.println(indentation() + ".executes((sender, args) -> {");
			indent();

			if(resulting) {
				out.print("return ");
			}

			emitMethodCallArguments(out);

			dedent();
			out.println(indentation() + "})");
			out.println(indentation() + ".register();");
		} else {
			// TODO: Assert. This object should never have been constructed!
		}
	}

	private void emitMethodCallArguments(PrintWriter out) {
		List<ArgumentData> allArguments = parent.getInheritedArguments();
		allArguments.addAll(arguments);

		// We need to derive the path of classes required to get to this suggestion
		// class, from the top-level @Command class
		
		List<CommandData> commandTree = new ArrayList<>();

		CommandData topLevelCommand = parent;
		while (topLevelCommand.getParent() != null) {
			commandTree.add(topLevelCommand);
			topLevelCommand = topLevelCommand.getParent();
		}
		commandTree.add(topLevelCommand);

		// TODO: Please, use some data structure (stack?), don't use Collections.reverse
		Collections.reverse(commandTree);
		
		// TODO: We can be smart here and reduce unused redundancies. Take for example:
		
	//        command.hiiiiii = (java.lang.String) args[0];
	//        command.byeeeeee = (int) args[1];
	//        HordeCommand2.HazardCommand command1 = command.new HazardCommand();
	//        HordeCommand2.HazardCommand.CreateCommand command2 = command1.new CreateCommand();
		// In this example ^, we don't use command1 and command2 because we know that
		// they have no arguments. We can reverse from the tree, if we have an argument
		// with no thingies, remove it

		System.out.println("PRINTING TREE");
		commandTree.stream().forEach(x -> System.out.println(x.getTypeElement().toString()));
		
		int argumentIndex = 0;

		// TODO: Initialize top level command arguments
		
		int commandDataIndex = 0;
		for(CommandData commandData : commandTree) {
			
			// If we're not the root level, we have to create the object first
			if(commandDataIndex != 0) {
				out.print(indentation() + commandData.getTypeElement().toString() + " ");
				out.print(Utils.COMMAND_VAR_NAME + commandDataIndex + " = ");
				if(commandDataIndex == 1) {
					out.print(Utils.COMMAND_VAR_NAME);
				} else {
					out.print(Utils.COMMAND_VAR_NAME + (commandDataIndex - 1));
				}
				out.println(".new " + commandData.getTypeElement().getSimpleName() + "();");
			}
			
			// Generate the arguments for the class
			for(int i = 0; i < commandData.getArguments().size(); i++) {
				ArgumentData currentArgument = commandData.getArguments().get(i);
				
				// If we're the root level, we have no prefixed index (e.g. 'command' instead of 'command0')
				out.print(indentation());
				if(commandDataIndex == 0) {
					out.print(Utils.COMMAND_VAR_NAME + ".");
				} else {
					out.print(Utils.COMMAND_VAR_NAME + commandDataIndex + ".");
				}
				
				// Generate the argument assignment
				out.println(currentArgument.getArgumentVariableName() + " = (" + currentArgument.getTypeMirror().toString() + ") args[" + argumentIndex + "];");
				argumentIndex++;
			}
			commandDataIndex++;
		}

		// The same code from suggestion emitting in CommandElement.java
//		Stack<TypeElement> typeStack = new Stack<>();
//		TypeElement currentTypeElement = parent.getTypeElement();
//		Types types = parent.getProcessingEnv().getTypeUtils();
//
//		while (!types.isSameType(currentTypeElement.asType(), topLevelCommand.getTypeElement().asType())) {
//			typeStack.add(currentTypeElement);
//
//			if (currentTypeElement.getNestingKind() == NestingKind.TOP_LEVEL) {
//				break;
//			} else {
//				// TODO: We've assumed it's a type element. It's possible that the enclosing
//				// element is an executable element if we've declared this class inside a
//				// function (very very improbable, but possible!)
//				currentTypeElement = (TypeElement) currentTypeElement.getEnclosingElement();
//			}
//		}
//
//		for(TypeElement )
//			for(ArgumentData topLevelClassArgument = 


			// TODO: We should probably store this variable name somewhere in a static final location
			//		out.print(indentation() + "command");
			//		for(TypeElement typeElement : typeStack) {
			//			out.print(".new " + typeElement.getSimpleName() + "()");
			//		}
			//		out.print("." + methodElement.getSimpleName() + "(sender");

			//		for (int i = 0, len = allArguments.size(); i < len; i++) {
			//			ArgumentData argument = allArguments.get(i);
			//			
			//			out.print(", (" + argument.getTypeMirror() + ") args[" + i + "]");
			//		}
			//		
			//		out.println(");");

			// Yeah, we have to bind these variables somehow:

			//		// Somehow, and I have absolutely no idea how, we're supposed to generate this function:
			////		HordeCommand2 command = new HordeCommand2();
			////		command.byeeeeee = (int) args[0];
			////		command.hiiiiii = (String) args[1];
			////		HordeCommand2.HazardCommand.ModifyCommand command1 = command.new HazardCommand().new ModifyCommand();
			////		command1.name = (String) args[2];
			////		command1.area((Player) null, 2);
			//
	}

}
