/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import dev.jorel.commandapi.CommandAPIExecutor;
import dev.jorel.commandapi.commandsenders.AbstractCommandSender;
import dev.jorel.commandapi.exceptions.BadLiteralException;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * An argument that represents multiple LiteralArguments
 * 
 * @since 4.1
 */
public class MultiLiteralArgument extends Argument<String> implements MultiLiteral<Argument<?>, CommandSender> {

	private final String[] literals;

	/**
	 * A multiliteral argument. Takes in string literals which cannot be modified
	 *
	 * @param nodeName the node name for this argument
	 * @param literals the literals that this argument represents
	 */
	public MultiLiteralArgument(String nodeName, String... literals) {
		super(nodeName, null);
		if(literals == null) {
			throw new BadLiteralException(true);
		}
		if(literals.length == 0) {
			throw new BadLiteralException(false);
		}
		this.literals = literals;
	}

	/**
	 * A multiliteral argument. Takes in string literals which cannot be modified
	 * @param literals the literals that this argument represents
	 * @deprecated Use {@link MultiLiteralArgument#MultiLiteralArgument(String, String...)} instead
	 */
	@Deprecated(since = "9.0.2", forRemoval = true)
	public MultiLiteralArgument(final String[] literals) {
		this(null, literals);
	}

	/**
	 * A multiliteral argument. Takes in string literals which cannot be modified
	 *
	 * @param nodeName the node name for this argument
	 * @param literals the literals that this argument represents
	 * @deprecated Use {@link MultiLiteralArgument#MultiLiteralArgument(String, String...)} instead
	 */
	@Deprecated(since = "9.1.0", forRemoval = true)
	public MultiLiteralArgument(String nodeName, final List<String> literals) {
		this(nodeName, literals.toArray(new String[0]));
	}

	@Override
	public Class<String> getPrimitiveType() {
		return String.class;
	}

	@Override
	public String[] getLiterals() {
		return literals;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.MULTI_LITERAL;
	}

	@Override
	public <Source> String parseArgument(CommandContext<Source> cmdCtx, String key, CommandArguments previousArgs) throws CommandSyntaxException {
		return cmdCtx.getArgument(key, String.class);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// MultiLiteral interface overrides                                                                               //
	// When a method in a parent class and interface have the same signature, Java will call the class version of the //
	//  method by default. However, we want to use the implementations found in the MultiLiteral interface.           //
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void checkPreconditions(List<Argument<?>> previousArguments, List<String> previousNonLiteralArgumentNames) {
		MultiLiteral.super.checkPreconditions(previousArguments, previousNonLiteralArgumentNames);
	}

	@Override
	public void appendToCommandPaths(List<List<String>> argumentStrings) {
		MultiLiteral.super.appendToCommandPaths(argumentStrings);
	}

	@Override
	public <Source> ArgumentBuilder<Source, ?> createArgumentBuilder(List<Argument<?>> previousArguments, List<String> previousNonLiteralArgumentNames) {
		return MultiLiteral.super.createArgumentBuilder(previousArguments, previousNonLiteralArgumentNames);
	}

	@Override
	public <Source> CommandNode<Source> linkNode(CommandNode<Source> previousNode, CommandNode<Source> rootNode, List<Argument<?>> previousArguments, List<String> previousNonLiteralArgumentNames, CommandAPIExecutor<CommandSender, AbstractCommandSender<? extends CommandSender>> terminalExecutor) {
		return MultiLiteral.super.linkNode(previousNode, rootNode, previousArguments, previousNonLiteralArgumentNames, terminalExecutor);
	}
}
