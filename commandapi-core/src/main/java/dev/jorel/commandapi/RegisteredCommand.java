package dev.jorel.commandapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Class to store a registered command which has its command name and a tree representing its arguments.
 * This class also contains the information required to construct a meaningful help topic for a command.
 * 
 * @param commandName      The name of this command, without any leading {@code /} characters
 * @param aliases          A {@link String}{@code []} of aliases for this command
 * @param namespace        The namespace for this command
 * @param permission       The {@link CommandPermission} required to run this command
 * @param shortDescription An {@link Optional} containing this command's help's short description
 * @param fullDescription  An {@link Optional} containing this command's help's full description
 * @param fullDescription  An {@link Optional} containing this command's help's usage
 * @param rootNode         The root {@link Node} in the tree structure that holds the arguments of this command
 */
public record RegisteredCommand(

	/**
	 * @return The name of this command, without any leading {@code /} characters
	 */
	String commandName,

	/**
	 * @return A {@link String}{@code []} of aliases for this command
	 */
	String[] aliases,

	/**
	 * @return The namespace for this command
	 */
	String namespace,

	/**
	 * @return The {@link CommandPermission} required to run this command
	 */
	CommandPermission permission,

	/**
	 * @return An {@link Optional} containing this command's help's short description
	 */
	Optional<String> shortDescription,

	/**
	 * @return An {@link Optional} containing this command's help's full description
	 */
	Optional<String> fullDescription,

	/**
	 * @return An {@link Optional} containing this command's help's usage
	 */
	Optional<String[]> usageDescription,

	// TODO: Bukkit specific fields probably should not be in platform agnostic classes
	//  Either make HelpTopic platform agnostic or move this field into bukkit-core
	/**
	 * @return An {@link Optional} containing this command's help topic (for Bukkit)
	 */
	Optional<Object> helpTopic,

	/**
	 * @return The root {@link Node} in the tree structure that holds the arguments of this command
	 */
	Node rootNode) {

	/**
	 * Class to store information about each argument in a command's tree.
	 * 
	 * @param nodeName   The name of this argument node
	 * @param className  The name of the CommandAPI object that this node represents
	 * @param helpString The string that should be used to represent this node when automatically generating help usage.
	 * @param executable True if this node can be executed, and false otherwise
	 * @param children   A {@link List} of nodes that are children to this node
	 */
	public static record Node(

		/**
		 * @return The name of this argument node
		 */
		String nodeName, 

		/**
		 * @return The name of the CommandAPI object that this node represents
		 */
		String className, 

		/**
		 * @return The string that should be used to represent this node when automatically generating help usage.
		 */
		String helpString, 

		/**
		 * @return True if this node can be executed, and false otherwise
		 */
		boolean executable, 

		/**
		 * @return A {@link List} of nodes that are children to this node
		 */
		List<Node> children) {

		/**
		 * @return A {@link List} of each executable path starting at this node. Each path is represented as a {@link List} of
		 * strings, where each string represents a node with the form {@code node_name:class_name}, for example {@code value:IntegerArgument}.
		 */
		public List<List<String>> argsAsStr() {
			List<List<String>> paths = new ArrayList<>();

			// Add this node
			String nodeString = nodeName + ":" + className;
			if (executable) {
				// This should be an ArrayList so parent nodes can insert themselves at the start
				paths.add(new ArrayList<>(List.of(nodeString)));
			}

			// Add children nodes
			for (Node node : children) {
				List<List<String>> subPaths = node.argsAsStr();

				for (List<String> subPath : subPaths) {
					subPath.add(0, nodeString);
				}

				paths.addAll(subPaths);
			}

			return paths;
		}

		private Node merge(Node other) {
			// Merge executable status
			boolean mergeExecutable = this.executable || other.executable;

			// Merge children
			Map<String, Node> childrenByName = new HashMap<>();
			for (Node child : this.children) {
				childrenByName.put(child.nodeName, child);
			}
			for (Node child : other.children) {
				childrenByName.compute(child.nodeName, (key, value) -> value == null ? child : value.merge(child));
			}
			List<Node> mergeChildren = new ArrayList<>(childrenByName.values());

			// Other information defaults to the node that was registered first (this)
			return new Node(nodeName, className, helpString, mergeExecutable, mergeChildren);
		}
	}

	/**
	 * Merges the given command into this command, returning the result. The result should accurately represent
	 * the command that would result from registering the commands in order. For example, registering two commands
	 * with different permissions results in the first command's permission being used, and so the result of this
	 * method will have the first command's permission.
	 * 
	 * @param other The {@link RegisteredCommand} that was registered after this one.
	 * @return The {@link RegisteredCommand} that results from merging this and the other command.
	 */
	public RegisteredCommand mergeCommandInformation(RegisteredCommand other) {
		// Merge aliases
		String[] mergedAliases = new String[this.aliases.length + other.aliases.length];
		System.arraycopy(this.aliases, 0, mergedAliases, 0, this.aliases.length);
		System.arraycopy(other.aliases, 0, mergedAliases, this.aliases.length, other.aliases.length);

		// Merge arguments
		Node mergedRootNode = this.rootNode.merge(other.rootNode);

		// Other information defaults to the command that was registered first (this)
		return new RegisteredCommand(commandName, mergedAliases, namespace, permission, shortDescription, fullDescription, usageDescription, helpTopic, mergedRootNode);
	}

	/**
	 * @return A copy of this {@link RegisteredCommand}, but with {@link RegisteredCommand#namespace()} as {@code ""}.
	 */
	public RegisteredCommand copyWithEmptyNamespace() {
		return new RegisteredCommand(commandName, aliases, "", permission, shortDescription, fullDescription, usageDescription, helpTopic, rootNode);
	}

	// The default implementation of `hashCode`, `equals`, and `toString` don't work for arrays,
	//  so we need to override and specifically use the Arrays methods for `String[] aliases`
	//  As https://stackoverflow.com/a/32083420 mentions, the same thing happens when Optionals wrap an array, like `Optional<String[]> usageDescription`

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(aliases);
		result = prime * result + Arrays.hashCode(usageDescription.orElse(null));
		result = prime * result + Objects.hash(commandName, namespace, permission, shortDescription, fullDescription, helpTopic, rootNode);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof RegisteredCommand)) {
			return false;
		}
		RegisteredCommand other = (RegisteredCommand) obj;
		return Arrays.equals(aliases, other.aliases) && Objects.equals(rootNode, other.rootNode) && Objects.equals(commandName, other.commandName)
			&& Objects.equals(namespace, other.namespace) && Arrays.equals(usageDescription.orElse(null), other.usageDescription.orElse(null))
			&& Objects.equals(fullDescription, other.fullDescription) && Objects.equals(permission, other.permission) && Objects.equals(shortDescription, other.shortDescription)
			&& Objects.equals(helpTopic, other.helpTopic);
	}

	@Override
	public String toString() {
		return "RegisteredCommand [commandName=" + commandName + ", aliases=" + Arrays.toString(aliases)
			+ ", namespace=" + namespace + ", permission=" + permission 
			+ ", shortDescription=" + shortDescription + ", fullDescription=" + fullDescription
			+ ", usageDescription=" + (usageDescription.isPresent() ? "Optional[" + Arrays.toString(usageDescription.get()) + "]" : "Optional.empty")
			+ ", helpTopic=" + helpTopic + ", rootNode=" + rootNode + "]";
	}
}
