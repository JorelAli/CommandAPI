package dev.jorel.commandapi;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.jorel.commandapi.arguments.AbstractArgument;
import dev.jorel.commandapi.exceptions.MissingCommandExecutorException;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the root node for creating a command as a tree
 * @param <Impl> The class extending this class, used as the return type for chain calls
 * @param <Argument> The implementation of AbstractArgument being used by this class
 * @param <CommandSender> The CommandSender class used by the class extending this class
 */
public abstract class AbstractCommandTree<Impl
/// @cond DOX
extends AbstractCommandTree<Impl, Argument, CommandSender>
/// @endcond
, Argument
/// @cond DOX
extends AbstractArgument<?, ?, Argument, CommandSender>
/// @endcond
, CommandSender> extends ExecutableCommand<Impl, CommandSender> {

	private final List<AbstractArgumentTree<?, Argument, CommandSender>> arguments = new ArrayList<>();

	/**
	 * Creates a main root node for a command tree with a given command name
	 *
	 * @param commandName The name of the command to create
	 */
	protected AbstractCommandTree(final String commandName) {
		super(commandName);
	}

	/**
	 * Create a child branch on the tree
	 *
	 * @param tree the child node
	 * @return this root node
	 */
	public Impl then(final AbstractArgumentTree<?, Argument, CommandSender> tree) {
		this.arguments.add(tree);
		return instance();
	}

	public List<AbstractArgumentTree<?, Argument, CommandSender>> getArguments() {
		return arguments;
	}

	@Override
	public List<List<String>> getArgumentsAsStrings() {
		if (arguments.isEmpty()) return List.of(List.of());

		List<List<String>> argumentStrings = new ArrayList<>();
		argumentStrings.add(new ArrayList<>());
		for (AbstractArgumentTree<?, Argument, CommandSender> argument : arguments) {
			argumentStrings.addAll(argument.getBranchesAsStrings());
		}

		return argumentStrings;
	}

	@Override
	<Source> Nodes<Source> createCommandNodes() {
		CommandAPIHandler<Argument, CommandSender, Source> handler = CommandAPIHandler.getInstance();

		// Check preconditions
		if (!executor.hasAnyExecutors() && arguments.isEmpty()) {
			throw new MissingCommandExecutorException(name);
		}

		// Create node
		LiteralArgumentBuilder<Source> rootBuilder = LiteralArgumentBuilder.literal(name);

		// Add permission and requirements
		rootBuilder.requires(handler.generateBrigadierRequirements(permission, requirements));

		// Add our executor
		if (executor.hasAnyExecutors()) {
			rootBuilder.executes(handler.generateBrigadierCommand(List.of(), executor));
		}

		// Register main node
		LiteralCommandNode<Source> rootNode = rootBuilder.build();

		// Add our arguments as children to the node
		// The previous arguments include an unlisted MultiLiteral representing the command name and aliases
		//  This doesn't affect how the command acts, but it helps represent the command path in exceptions
		String[] literals = new String[aliases.length + 1];
		literals[0] = name;
		System.arraycopy(aliases, 0, literals, 1, aliases.length);
		Argument commandNames = handler.getPlatform().newConcreteMultiLiteralArgument(name, literals);
		commandNames.setListed(false);

		for (AbstractArgumentTree<?, Argument, CommandSender> argument : arguments) {
			// We need new previousArguments lists for each branch
			List<Argument> previousArguments = new ArrayList<>();
			List<String> previousArgumentNames = new ArrayList<>();
			previousArguments.add(commandNames);

			argument.buildBrigadierNode(rootNode, previousArguments, previousArgumentNames);
		}

		// Generate alias nodes
		List<LiteralCommandNode<Source>> aliasNodes = new ArrayList<>();
		for (String alias : aliases) {
			// Create node
			LiteralArgumentBuilder<Source> aliasBuilder = LiteralArgumentBuilder.literal(alias);

			// Add permission and requirements
			aliasBuilder.requires(handler.generateBrigadierRequirements(permission, requirements));

			// Add our executor
			if (executor.hasAnyExecutors()) {
				aliasBuilder.executes(handler.generateBrigadierCommand(List.of(), executor));
			}

			// Redirect to rootNode so all its arguments come after this node
			aliasBuilder.redirect(rootNode);

			// Register alias node
			aliasNodes.add(aliasBuilder.build());
		}

		return new Nodes<>(rootNode, aliasNodes);
	}
}
