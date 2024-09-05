package dev.jorel.commandapi.config;

import java.util.List;

record CommentedConfigOption<T>(List<String> comment, T option) {
}
