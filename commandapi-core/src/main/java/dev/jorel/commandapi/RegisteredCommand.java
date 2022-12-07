package dev.jorel.commandapi;

import java.util.List;
import java.util.Optional;

import dev.jorel.commandapi.arguments.IntegerArgument;

/**
 * Class to store a registered command which has its command name and a list of
 * arguments as a string. The arguments are expected to be of the form
 * {@code node_name:class_name}, for example
 * {@code value:}{@link IntegerArgument}. This class also contains the
 * information required to construct a meaningful help topic for a command
 */
public record RegisteredCommand(

		/**
		 * @return The name of this command, without any leading {@code /} characters
		 */
		String commandName,

		/**
		 * @return The list of node names and argument class simple names in the form
		 *         {@code node_name:class_name}, for example
		 *         {@code value:}{@link IntegerArgument}
		 */
		List<String> argsAsStr,

		/**
		 * @return An {@link Optional} containing this command's help's short
		 *         descriptions
		 */
		Optional<String> shortDescription,

		/**
		 * @return An {@link Optional} containing this command's help's full
		 *         descriptions
		 */
		Optional<String> fullDescription,

		/**
		 * @return An {@link Optional} containing this command's usage text
		 */
		Optional<String[]> usage,

		/**
		 * @return a {@link String}{@code []} of aliases for this command
		 */
		String[] aliases,

		/**
		 * @return The {@link CommandPermission} required to run this command
		 */
		CommandPermission permission) {
}