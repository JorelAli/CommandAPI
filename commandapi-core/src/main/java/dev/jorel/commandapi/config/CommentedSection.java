package dev.jorel.commandapi.config;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public record CommentedSection(String[] comment) {
}
