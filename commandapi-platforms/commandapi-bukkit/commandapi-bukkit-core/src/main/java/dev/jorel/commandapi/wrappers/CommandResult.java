package dev.jorel.commandapi.wrappers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Objects;

/**
 * CommandResult represents an executable command. It contains the following
 * methods:
 * <ul>
 * <li>{@link CommandResult#command} - the command which this
 * {@code CommandResult} contains</li>
 * <li>{@link CommandResult#args} - the arguments that were provided to this
 * command</li>
 * <li>{@link CommandResult#execute(CommandSender)} - Run the command using the
 * given CommandSender directly</li>
 * </ul>
 */
public record CommandResult(
	/**
	 * The executable Bukkit {@link Command} that this {@link CommandResult}
	 * contains.
	 */
	Command command,

	/**
	 * The arguments provided to this command.
	 */
	String[] args) {

	/**
	 * Executes this command with a provided {@link CommandSender}. This is
	 * equivalent to running
	 * {@code result.command().execute(sender, result.command().getLabel(), result.args());}
	 *
	 * @param sender the command sender that will be used to execute this command
	 */
	public boolean execute(CommandSender sender) {
		return command.execute(sender, command.getLabel(), args);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		CommandResult that = (CommandResult) o;
		return command.equals(that.command) && Arrays.equals(args, that.args);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(command);
		result = 31 * result + Arrays.hashCode(args);
		return result;
	}

	@Override
	public String toString() {
		return "CommandResult [command=" + command + ", args=" + Arrays.toString(args) + "]";
	}
}
