package io.github.jorelali;

import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.LocationArgument;

public class BaseLocations extends LocationArgument {
    public BaseLocations(String nodeName) {
        super(nodeName);
		replaceSuggestions(ArgumentSuggestions.strings("0 0 0", "1 1 1"));
    }
}
