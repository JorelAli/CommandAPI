package dev.jorel.commandapi.config;

record CommentedConfigOption<T>(String[] comment, T option) {
}
