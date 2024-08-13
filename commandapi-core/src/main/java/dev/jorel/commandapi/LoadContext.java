package dev.jorel.commandapi;

public record LoadContext(CommandAPIPlatform<?, ?, ?> platform, Runnable context) {
}
