package dev.jorel.commandapi;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Class to store a registered command which has its command name and a list of
 * arguments as a string. The arguments are expected to be of the form
 * {@code node_name:class_name}, for example {@code value:IntegerArgument}. This
 * class also contains the information required to construct a meaningful help
 * topic for a command
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
	 * @return a {@link String}{@code []} of aliases for this command
	 */
	String[] aliases,

	/**
	 * @return The {@link CommandPermission} required to run this command
	 */
	CommandPermission permission) {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(aliases);
		result = prime * result + Objects.hash(argsAsStr, commandName, fullDescription, permission, shortDescription);
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
		return Arrays.equals(aliases, other.aliases) && Objects.equals(argsAsStr, other.argsAsStr) && Objects.equals(commandName, other.commandName)
			&& Objects.equals(fullDescription, other.fullDescription) && Objects.equals(permission, other.permission) && Objects.equals(shortDescription, other.shortDescription);
	}

	@Override
	public String toString() {
		return "RegisteredCommand [commandName=" + commandName + ", argsAsStr=" + argsAsStr + ", shortDescription=" + shortDescription + ", fullDescription=" + fullDescription
			+ ", aliases=" + Arrays.toString(aliases) + ", permission=" + permission + "]";
	}

}