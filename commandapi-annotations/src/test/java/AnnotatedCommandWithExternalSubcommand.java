import dev.jorel.commandapi.annotations.annotations.Command;
import dev.jorel.commandapi.annotations.annotations.ExternalSubcommand;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;

/**
 * Annotated command that uses an external subcommand
 */
@Command("mycommand")
public class AnnotatedCommandWithExternalSubcommand {

	// /mycommand <name>

	@AStringArgument
	String name;

	@ExternalSubcommand(AnnotatedExternalSubcommand.class)
	AnnotatedExternalSubcommand subcommand;

}
