package dev.jorel.commandapi.spying;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.CommandAPIExecutor;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.MockCommandSource;
import dev.jorel.commandapi.arguments.AbstractArgument;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.commandsenders.AbstractCommandSender;
import org.bukkit.command.CommandSender;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

public class CommandAPIHandlerSpy {
	// Handler instances
	private final CommandAPIHandler<Argument<?>, CommandSender, MockCommandSource> handler;
	private final CommandAPIHandler<Argument<?>, CommandSender, MockCommandSource> spyHandler;

	public CommandAPIHandler<Argument<?>, CommandSender, MockCommandSource> spyHandler() {
		return spyHandler;
	}

	// Methods for handling intercepts
	ExecutionQueue executionQueue = new ExecutionQueue();

	public ExecutionQueue getExecutionQueue() {
		return executionQueue;
	}

	// Setup
	public CommandAPIHandlerSpy(CommandAPIHandler<?, ?, ?> commandAPIHandler) {
		handler = (CommandAPIHandler<Argument<?>, CommandSender, MockCommandSource>) commandAPIHandler;
		spyHandler = Mockito.spy(handler);

		Mockito.when(spyHandler.generateCommand(any(), any(), any(Boolean.class) /* Class gives non-null un-boxable default */))
			.thenAnswer(i -> generateCommand(i.getArgument(0), i.getArgument(1), i.getArgument(2)));
	}

	// Intercepted methods
	private Command<MockCommandSource> generateCommand(
		AbstractArgument<?, ?, ?, ?>[] args, // Using AbstractArgument[] because that's the actual runtime type of the array
		CommandAPIExecutor<CommandSender, AbstractCommandSender<? extends CommandSender>> executor,
		boolean converted
	) {
		CommandAPIExecutor<CommandSender, AbstractCommandSender<? extends CommandSender>> spyExecutor = Mockito.spy(executor);

		try {
			// Not using Mockito.when to avoid calling real executes method
			Mockito.doAnswer(i -> {
				executionQueue.add(i.getArgument(0));
				return i.callRealMethod();
			}).when(spyExecutor).execute(any());
		} catch (CommandSyntaxException ignored) {
			// `spyExecutor#execute` will never actually throw an exception
		}

		// Convert array to Argument<?>[], which is what we actually want
		Argument<?>[] arguments = new Argument[args.length];
		System.arraycopy(args, 0, arguments, 0, args.length);
		return handler.generateCommand(arguments, spyExecutor, converted);
	}
}
