package dev.jorel.commandapi.annotations.parser;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;

import com.google.common.collect.Streams;

import dev.jorel.commandapi.CommandPermission;
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
		CommandData topLevelCommand = parent;
		while (topLevelCommand.getParent() != null) {
			topLevelCommand = topLevelCommand.getParent();
		}
		
		// The same code from suggestion emitting in CommandElement.java
		Stack<TypeElement> typeStack = new Stack<>();
		TypeElement currentTypeElement = parent.getTypeElement();
		Types types = parent.getProcessingEnv().getTypeUtils();

		while (!types.isSameType(currentTypeElement.asType(), topLevelCommand.getTypeElement().asType())) {
			typeStack.add(currentTypeElement);

			if (currentTypeElement.getNestingKind() == NestingKind.TOP_LEVEL) {
				break;
			} else {
				// TODO: We've assumed it's a type element. It's possible that the enclosing
				// element is an executable element if we've declared this class inside a
				// function (very very improbable, but possible!)
				currentTypeElement = (TypeElement) currentTypeElement.getEnclosingElement();
			}
		}
		
		// TODO: We should probably store this variable name somewhere in a static final location

		out.print(indentation() + "command");
		for(TypeElement typeElement : typeStack) {
			out.print(".new " + typeElement.getSimpleName() + "()");
		}
		out.print("." + methodElement.getSimpleName() + "(sender");
		
		// args
		
		out.println(");");

//		
		// Actually, we don't even have to do that. (see below). All we need is to generate the full thing and provide the arguments.
		// There's actually absolutely no obligation to populate class values, they're all just placeholders anyway
		
//		// Somehow, and I have absolutely no idea how, we're supposed to generate this function:
////		HordeCommand2 command = new HordeCommand2();
////		command.byeeeeee = (int) args[0];
////		command.hiiiiii = (String) args[1];
////		HordeCommand2.HazardCommand.ModifyCommand command1 = command.new HazardCommand().new ModifyCommand();
////		command1.name = (String) args[2];
////		command1.area((Player) null, 2);
//

//		
//		// TODO: We should probably store this variable name somewhere in a static final location
//		out.print("command");
//		for(TypeElement typeElement : typeStack) {
//			out.print(".new " + typeElement.getSimpleName() + "()");
//		}
//		out.print(".get())");
//		
//		{
//
//			
//			List<ArgumentData> allArguments = parent.getInheritedArguments();
//			allArguments.addAll(arguments);
//			{
//				out.print(commandClass.getSimpleName());
//				out.print(".");
//				out.print(methodElement.getSimpleName());
//				out.print("(sender");
//				
//				for(int i = 0; i < allArguments.size(); i++) {
//					
//				}
//
////				for (int i = 0; i < argumentMapping.size(); i++) {
////					String fromArgumentMap = argumentMapping.get(i);
////					out.print(", (");
////
////					if (fromArgumentMap.contains("<")) {
////						out.print(simpleFromQualified(fromArgumentMap.substring(0, fromArgumentMap.indexOf("<"))));
////						out.print("<");
////						out.print(simpleFromQualified(
////								fromArgumentMap.substring(fromArgumentMap.indexOf("<") + 1, fromArgumentMap.indexOf(">"))));
////						out.print(">");
////					} else {
////						out.print(simpleFromQualified(fromArgumentMap));
////					}
////					out.print(") args[");
////					out.print(i);
////					out.print("]");
////				}
//			}
//		}
	}
	
}
