package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

public record ArgumentParseExceptionContext(WrapperCommandSyntaxException exception) {
}
