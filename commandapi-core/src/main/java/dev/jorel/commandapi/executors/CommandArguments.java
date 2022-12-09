package dev.jorel.commandapi.executors;

import java.util.Map;

public class CommandArguments {

	private final Object[] args;
	private final Map<String, Object> argsMap;

	public CommandArguments(Object[] args, Map<String, Object> argsMap) {
		this.args = args;
		this.argsMap = argsMap;
	}

	public Object get(int index) {
		return args[index];
	}

	public Object get(String nodeName) {
		return argsMap.get(nodeName);
 	}
}
