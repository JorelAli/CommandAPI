package dev.jorel.commandapi.annotations.parser;

import java.util.List;

import dev.jorel.commandapi.executors.ExecutorType;

public class SubcommandMethod {

	// The executor types. Inferred from the first argument of the method, or explicitly declared via @Executors
	public ExecutorType[] executorTypes;
	
	public String subcommandName;
	
	public List<ArgumentData> arguments;
	
	// Whether this is a resulting executor or not. If this method returns void, it's not. If this method returns int, it is. If this method returns anything else, this should be caught by semantics (TODO: Implement in semantics)
	public boolean resulting;
	
}
