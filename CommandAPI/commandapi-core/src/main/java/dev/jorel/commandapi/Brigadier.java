package dev.jorel.commandapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

import org.bukkit.command.CommandSender;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.RootCommandNode;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;

/**
 * The Brigadier class is used to access some of the internals of the CommandAPI
 * so you can use the CommandAPI alongside Mojang's com.mojang.brigadier package
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class Brigadier {

	/**
	 * Returns the Brigadier CommandDispatcher tree that is used internally by the
	 * CommandAPI. Modifying this CommandDispatcher tree before the server finishes
	 * loading will still keep any changes made to it. For example, adding a new
	 * node to this tree will keep the node once the server has finished loading.
	 * 
	 * @return The CommandAPI's internal CommandDispatcher instance
	 */
	public static CommandDispatcher getCommandDispatcher() {
		return CommandAPIHandler.getInstance().DISPATCHER;
	}

	/**
	 * Returns the root node of the current CommandDispatcher. This is the
	 * equivalent of running
	 * 
	 * <code>
	 * Brigadier.getCommandDispatcher().getRoot();
	 * </code>
	 * 
	 * @return The Brigadier CommandDispatcher's root node
	 */
	public static RootCommandNode getRootNode() {
		return CommandAPIHandler.getInstance().DISPATCHER.getRoot();
	}

	/**
	 * Creates a new literal argument builder from a CommandAPI LiteralArgument
	 * 
	 * @param literalArgument the LiteralArgument to convert from
	 * @return a LiteralArgumentBuilder that represents the literal
	 */
	public static LiteralArgumentBuilder fromLiteralArgument(LiteralArgument literalArgument) {
		return CommandAPIHandler.getInstance().getLiteralArgumentBuilderArgument(literalArgument.getLiteral(),
				literalArgument.getArgumentPermission(), literalArgument.getRequirements());
	}

	/**
	 * Constructs a RedirectModifier from a predicate that uses a command sender and
	 * some arguments. RedirectModifiers can be used with Brigadier's
	 * <code>fork()</code> method to invoke other nodes in the CommandDispatcher
	 * tree. You would use this method as shown:
	 * 
	 * <pre>
	 * Brigadier.fromPredicate((sender, args) -&gt; {
	 *     ...
	 * }, arguments);
	 * </pre>
	 * 
	 * @param predicate the predicate to test
	 * @param args      the arguments that the sender has filled in
	 * @return a RedirectModifier that encapsulates the provided predicate
	 */
	public static RedirectModifier fromPredicate(BiPredicate<CommandSender, Object[]> predicate, List<Argument> args) {
		return cmdCtx -> {
			
			//CommandAPIHandler.getInstance().NMS.inject(cmdCtx);
			
			if (predicate.test(CommandAPIHandler.getInstance().NMS.getSenderForCommand(cmdCtx, false),
					CommandAPIHandler.getInstance().argsToObjectArr(cmdCtx, args))) {
				return Collections.singleton(cmdCtx.getSource());
			} else {
				return Collections.emptyList();
			}
		};
	}

	/**
	 * Converts a CommandAPICommand into a Brigadier Command
	 * 
	 * @param command the command to convert
	 * @return a Brigadier Command object that represents the provided command
	 */
	public static Command fromCommand(CommandAPICommand command) {
		try {
			return CommandAPIHandler.getInstance().generateCommand(command.getArguments(), command.getExecutor(), command.isConverted());
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}
		// This case should never happen, because the exception should be caught
		// "lower down" in execution
		return null;
	}

	/**
	 * Constructs a RequiredArgumentBuilder from a given argument within a command
	 * declaration. For example:
	 * 
	 * <pre>
	 * List&lt;Argument&gt; arguments = new ArrayList&lt;&gt;();
	 * arguments.add(new IntegerArgument("hello"));
	 * 
	 * RequiredArgumentBuilder argBuilder = Brigadier.fromArguments(arguments, "hello");
	 * </pre>
	 * 
	 * @param args     the List of arguments which you typically declare for
	 *                 commands
	 * @param nodeName the name of the argument you want to specify
	 * @return a RequiredArgumentBuilder that represents the provided argument
	 */
	public static RequiredArgumentBuilder fromArgument(List<Argument> args, String nodeName) {
		return CommandAPIHandler.getInstance().getRequiredArgumentBuilderDynamic(args, CommandAPIHandler.getArgument(args, nodeName));
	}

	/**
	 * Constructs a RequiredArgumentBuilder from a given argument
	 * 
	 * @param argument the argument to create a RequiredArgumentBuilder from
	 * @return a RequiredArgumentBuilder that represents the provided argument
	 */
	public static RequiredArgumentBuilder fromArgument(Argument argument) {
		List<Argument> arguments = new ArrayList<>();
		arguments.add(argument);
		return CommandAPIHandler.getInstance().getRequiredArgumentBuilderDynamic(arguments, argument);
	}

	/**
	 * Converts an argument name and a list of arguments to a Brigadier
	 * SuggestionProvider
	 * 
	 * @param nodeName the name (prompt) of the argument as declared by its node
	 *                 name
	 * @param args     the list of arguments
	 * @return a SuggestionProvider that suggests the overridden suggestions for the
	 *         argument declared in the List with key argumentName
	 */
	public static SuggestionProvider toSuggestions(String nodeName, List<Argument> args) {
		return CommandAPIHandler.getInstance().toSuggestions(nodeName, args);
	}
}