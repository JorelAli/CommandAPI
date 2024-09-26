package dev.jorel.commandapi.config;

public record CommentedConfigOption<T>(String[] comment, T option) {
}
