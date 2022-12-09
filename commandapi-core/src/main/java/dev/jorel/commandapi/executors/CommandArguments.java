package dev.jorel.commandapi.executors;

import java.util.Map;

public class CommandArguments {

	private final Object[] args;
	private final Map<String, Object> argsMap;

	public CommandArguments(Object[] args, Map<String, Object> argsMap) {
		this.args = args;
		this.argsMap = argsMap;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(int index) {
		return (T) args[index];
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String nodeName) {
		return (T) argsMap.get(nodeName);
 	}
}
