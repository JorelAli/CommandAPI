package dev.jorel.commandapi.test.arguments.parseexceptions;

import dev.jorel.commandapi.arguments.parseexceptions.InitialParseExceptionArgument;
import dev.jorel.commandapi.arguments.parseexceptions.InitialParseExceptionContext;
import dev.jorel.commandapi.arguments.parseexceptions.InitialParseExceptionHandler;
import dev.jorel.commandapi.test.TestBase;
import org.bukkit.command.CommandSender;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * A helper class for verifying generated {@link InitialParseExceptionContext} objects.
 *
 * @param <T> The class of the object returned by the ArgumentType this object is handling.
 * @param <ExceptionInformation> The class that holds information about the exception.
 */
public class InitialParseExceptionContextVerifier<T, ExceptionInformation> {
    private Consumer<InitialParseExceptionContext<ExceptionInformation>> currentVerifier;
    private AssertionError verificationFailure;

    private final InitialParseExceptionHandler<T, ExceptionInformation> exceptionHandler = context -> {
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
     * Creates a new {@link InitialParseExceptionContextVerifier}.
     *
     * @param testBase The {@link TestBase} object running the test.
     */
    public InitialParseExceptionContextVerifier(TestBase testBase) {
        this.testBase = testBase;
    }

    /**
     * Returns the {@link InitialParseExceptionHandler} of this context verifier. This should be passed into
     * {@link InitialParseExceptionArgument#withInitialParseExceptionHandler(InitialParseExceptionHandler)} for the
     * Argument being tested in order to intercept its initial parse exceptions for verification.
     *
     * @return The {@link InitialParseExceptionHandler} used to intercept initial parse exceptions
     */
    public InitialParseExceptionHandler<T, ExceptionInformation> getExceptionHandler() {
        return exceptionHandler;
    }

    /**
     * Verifies that running a command generates the excepted {@link InitialParseExceptionContext}.
     *
     * @param sender           The sender of the command.
     * @param command          The command to execute.
     * @param exceptionMessage The expected exception message from running the command.
     * @param verifier         The code to run to verify the {@link InitialParseExceptionContext} is correct.
     */
    public void verifyGeneratedContext(
            CommandSender sender, String command, String exceptionMessage,
            Consumer<InitialParseExceptionContext<ExceptionInformation>> verifier
    ) {
        this.currentVerifier = verifier;
        testBase.assertCommandFailsWith(sender, command, exceptionMessage);

        assertNull(currentVerifier, "Expected an InitialParseContext to be created, but " +
                "my exceptionHandler was never called to handle an initial parse exception");
        if (verificationFailure != null) {
            AssertionError temp = verificationFailure;
            verificationFailure = null;

            throw temp;
        }
    }

    /**
     * Asserts that the given {@code actual} context has all the given expected attributes.
     *
     * @param exceptionMessage The message of the initial parse exception.
     * @param readerString     The command string being parsed.
     * @param cursorStart      The place where the StringReader started parsing the ArgumentType.
     * @param readerCursor     The place where the StringReader ended when the exception was handled.
     * @param actual           The actual {@link InitialParseExceptionContext} generated.
     */
    public void assertCorrectContext(
            String exceptionMessage, String readerString, int cursorStart, int readerCursor,
            InitialParseExceptionContext<ExceptionInformation> actual
    ) {
        assertEquals(exceptionMessage, actual.exception().getMessage());

        assertEquals(readerString, actual.stringReader().getString());
        assertEquals(readerCursor, actual.stringReader().getCursor());

        assertEquals(cursorStart, actual.cursorStart());
    }
}
