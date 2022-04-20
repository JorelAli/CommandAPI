package dev.jorel.commandapi.annotations.parser;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.lang.model.element.ExecutableElement;

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
	
	public SubcommandMethod(ExecutableElement methodElement, String name, String[] aliases, CommandPermission permission, List<ArgumentData> arguments, boolean isResulting) {
		this.methodElement = methodElement;
		this.subcommandName = name;
		this.aliases = aliases;
		this.permission = permission;
		this.arguments = arguments;
		this.resulting = isResulting;
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
			
			out.println(indentation() + "command." + methodElement.getSimpleName() + "(sender, args[0]);");
			
			dedent();
			out.println(indentation() + "})");
			out.println(indentation() + ".register();");
		} else {
			// TODO: Assert. This object should never have been constructed!
		}
	}
	
}
