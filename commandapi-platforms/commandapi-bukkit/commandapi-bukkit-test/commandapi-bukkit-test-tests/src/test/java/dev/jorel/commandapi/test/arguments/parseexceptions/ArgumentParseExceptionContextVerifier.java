package dev.jorel.commandapi.test.arguments.parseexceptions;

import dev.jorel.commandapi.arguments.parseexceptions.ArgumentParseExceptionArgument;
import dev.jorel.commandapi.arguments.parseexceptions.ArgumentParseExceptionContext;
import dev.jorel.commandapi.arguments.parseexceptions.ArgumentParseExceptionHandler;
import dev.jorel.commandapi.test.TestBase;
import org.bukkit.command.CommandSender;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * A helper class for verifying generated {@link ArgumentParseExceptionContext} objects.
 *
 * @param <T> The class of the object that can be substituted instead of an exception when the Argument fails to parse.
 * @param <Raw> The class of the object returned by the initial Brigadier parse for the Argument.
 * @param <ExceptionInformation> The class that holds information about the exception.
 */
public class ArgumentParseExceptionContextVerifier<T, Raw, ExceptionInformation> {
    private Consumer<ArgumentParseExceptionContext<Raw, ExceptionInformation, CommandSender>> currentVerifier;
    private AssertionError verificationFailure;

    private final ArgumentParseExceptionHandler<T, Raw, ExceptionInformation, CommandSender> exceptionHandler = context -> {
        try {
            currentVerifier.accept(context);
        } catch (AssertionError error) {
            verificationFailure = error;
        }
        currentVerifier = null;
        throw context.exception();
    };

    private final TestBase testBase;

    /**
     * Creates a new {@link ArgumentParseExceptionContextVerifier}.
     *
     * @param testBase The {@link TestBase} object running the test.
     */
    public ArgumentParseExceptionContextVerifier(TestBase testBase) {
        this.testBase = testBase;
    }

    /**
     * Returns the {@link ArgumentParseExceptionHandler} of this context verifier. This should be passed into
     * {@link ArgumentParseExceptionArgument#withArgumentParseExceptionHandler(ArgumentParseExceptionHandler)} for the
     * Argument being tested in order to intercept its argument parse exceptions for verification.
     *
     * @return The {@link ArgumentParseExceptionHandler} used to intercept argument parse exceptions
     */
    public ArgumentParseExceptionHandler<T, Raw, ExceptionInformation, CommandSender> getExceptionHandler() {
        return exceptionHandler;
    }

    /**
     * Verifies that running a command generates the excepted {@link ArgumentParseExceptionContext}.
     *
     * @param sender           The sender of the command.
     * @param command          The command to execute.
     * @param exceptionMessage The expected exception message from running the command.
     * @param verifier         The code to run to verify the {@link ArgumentParseExceptionContext} is correct.
     */
    public void verifyGeneratedContext(
            CommandSender sender, String command, String exceptionMessage,
            Consumer<ArgumentParseExceptionContext<Raw, ExceptionInformation, CommandSender>> verifier
    ) {
        this.currentVerifier = verifier;
        testBase.assertCommandFailsWith(sender, command, exceptionMessage);

        assertNull(currentVerifier, "Expected an ArgumentParseContext to be created, but " +
                "my exceptionHandler was never called to handle an argument parse exception");
        if (verificationFailure != null) {
            AssertionError temp = verificationFailure;
            verificationFailure = null;

            throw temp;
        }
    }
}
