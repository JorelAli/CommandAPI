package dev.jorel.commandapi.test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.GreedyArgumentException;
import dev.jorel.commandapi.exceptions.InvalidCommandNameException;

/**
 * Tests for command semantics
 */
@SuppressWarnings("unused")
public class CommandTests extends TestBase {
	
	/*********
	 * Setup *
	 *********/
	
	@BeforeEach
	public void setUp() {
		super.setUp();
	}

	@AfterEach
	public void tearDown() {
		super.tearDown();
	}
	
	/*********
	 * Tests *
	 *********/

	@Test
	public void testGreedyStringArgumentNotAtEnd() {
		assertDoesNotThrow(() -> {
			new CommandAPICommand("test")
				.withArguments(new StringArgument("arg1"))
				.withArguments(new GreedyStringArgument("arg2"))
				.executes((sender, args) -> {
					String arg1 = (String) args.get(0);
					String arg2 = (String) args.get(0);
				})
				.register();
		});

		assertThrows(GreedyArgumentException.class, () -> {
			new CommandAPICommand("test")
				.withArguments(new GreedyStringArgument("arg1"))
				.withArguments(new StringArgument("arg2"))
				.executes((sender, args) -> {
					String arg1 = (String) args.get(0);
					String arg2 = (String) args.get(0);
				})
				.register();
		});
	}

	@Test
	public void testInvalidCommandName() {
		assertThrows(InvalidCommandNameException.class, () -> {
			new CommandAPICommand((String) null)
				.withArguments(new StringArgument("arg1"))
				.executes((sender, args) -> {
					String arg1 = (String) args.get(0);
				})
				.register();
		});
		
		assertThrows(InvalidCommandNameException.class, () -> {
			new CommandAPICommand("")
				.withArguments(new StringArgument("arg1"))
				.executes((sender, args) -> {
					String arg1 = (String) args.get(0);
				})
				.register();
		});
		
		assertThrows(InvalidCommandNameException.class, () -> {
			new CommandAPICommand("my command")
				.withArguments(new StringArgument("arg1"))
				.executes((sender, args) -> {
					String arg1 = (String) args.get(0);
				})
				.register();
		});
	}
	
	@Test
	public void testNoExecutor() {
		// TODO: Catch this case. Need to check if has no executor AND has no
		//  subcommand or otherwise when .register() called
		new CommandAPICommand("test")
			.withArguments(new StringArgument("arg1"))
			.register();
	}

}
