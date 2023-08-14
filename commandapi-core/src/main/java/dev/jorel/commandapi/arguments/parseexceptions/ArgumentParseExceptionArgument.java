package dev.jorel.commandapi.arguments.parseexceptions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.ChainableBuilder;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.arguments.AbstractArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandArguments;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * An interface that indicates an argument can have an {@link ArgumentParseExceptionHandler} attached to it.
 *
 * @param <T> The class of the object that can be substituted instead of an exception when the Argument fails to parse.
 * @param <Raw> The class of the object returned by the initial Brigadier parse for the Argument.
 * @param <ExceptionInformation> The class that holds information about the exception.
 * @param <Impl> The class extending this class, used as the return type in chained calls.
 * @param <CommandSender> The CommandSender class used by the class extending this class.
 */
public interface ArgumentParseExceptionArgument<T, Raw, ExceptionInformation, Impl extends AbstractArgument<?, Impl, ?, CommandSender>, CommandSender> extends ChainableBuilder<Impl> {
    /**
     * A map that links Arguments to their ExceptionHandlers. This is basically
     * equivalent to putting one instance variable in this interface, but Java
     * doesn't let you put instance variables in interfaces, so we have to do
     * this instead if we want to provide default implementations of the methods,
     * overall avoiding the code duplication that comes from implementing these
     * methods in the inheriting classes.
     */
	// TODO: Maybe this can be a WeakHashMap, so once the Argument objects aren't being used anywhere else we can forget
	//  about them and not store them anymore. I'm not entirely sure that is what WeakHashMap does though. Are Arguments
	//  ever GC'd anyway, or do they stick around somewhere?
    Map<ArgumentParseExceptionArgument<?, ?, ?, ?, ?>, ArgumentParseExceptionHandler<?, ?, ?, ?>> exceptionHandlers = new HashMap<>();

    /**
     * Sets the {@link ArgumentParseExceptionHandler} this Argument should use when it fails to parse.
     *
     * @param exceptionHandler The new {@link ArgumentParseExceptionHandler} this argument should use
     * @return this current argument
     */
    default Impl withArgumentParseExceptionHandler(
		ArgumentParseExceptionHandler<T, Raw, ExceptionInformation, CommandSender> exceptionHandler
	) {
        exceptionHandlers.put(this, exceptionHandler);
        return instance();
    }

    /**
     * Returns the {@link ArgumentParseExceptionHandler} this argument is using
     * @return The {@link ArgumentParseExceptionHandler} this argument is using
     */
    default Optional<ArgumentParseExceptionHandler<T, Raw, ExceptionInformation, CommandSender>> getArgumentParseExceptionHandler() {
        return Optional.ofNullable(
			(ArgumentParseExceptionHandler<T, Raw, ExceptionInformation, CommandSender>) exceptionHandlers.get(this)
		);
    }

	default <Source, A extends AbstractArgument<?, ?, A, CommandSender>>
	T handleArgumentParseException(
		CommandContext<Source> cmdCtx, String key, CommandArguments previousArgs,
		CommandSyntaxException original, ExceptionInformation exceptionInformation
	) throws CommandSyntaxException {
		ArgumentParseExceptionHandler<T, Raw, ExceptionInformation, CommandSender> exceptionHandler =
				getArgumentParseExceptionHandler().orElseThrow(() -> original);

		try {
			return exceptionHandler.handleException(new ArgumentParseExceptionContext<>(
					new WrapperCommandSyntaxException(original),
					exceptionInformation,
					CommandAPIHandler.<A, CommandSender, Source>getInstance().getPlatform()
							.getCommandSenderFromCommandSource(cmdCtx.getSource()).getSource(),
					(Raw) cmdCtx.getArgument(key, Object.class),
					previousArgs
			));
		} catch (WrapperCommandSyntaxException newException) {
			throw newException.getException();
		}
	}
}
