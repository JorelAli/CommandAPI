import org.bukkit.entity.Player;

import dev.jorel.commandapi.annotations.annotations.Executes;
import dev.jorel.commandapi.annotations.annotations.Subcommand;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;

/**
 * Annotated subcommand. Note that this class uses {@code @Subcommand} instead
 * of {@code @Command}
 */
@Subcommand("subcommand")
public class AnnotatedExternalSubcommand extends AnnotatedCommandWithExternalSubcommand {

	// mycommand <name> <value>

	@Executes
	public void myMethod(Player player, @AIntegerArgument int value) {
		String nameArg = name; // Inherited from parent command class
	}

}
