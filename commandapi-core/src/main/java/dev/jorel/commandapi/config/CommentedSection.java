package dev.jorel.commandapi.config;

import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
record CommentedSection(List<String> comment) {
}
