package dev.jorel.commandapi.config;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
record CommentedConfigOption<T>(String[] comment, T option) {
}
