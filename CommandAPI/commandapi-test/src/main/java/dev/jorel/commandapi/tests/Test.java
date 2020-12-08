package dev.jorel.commandapi.tests;

import java.util.List;

import dev.jorel.commandapi.CommandAPIMain;
import dev.jorel.commandapi.executors.CommandExecutor;

public abstract class Test {

	public abstract void register();
	
	public abstract void test() throws Exception;
	
	public String cmd(String name) {
		return this.getClass().getSimpleName() + name;
	}
	
	public CommandExecutor tryExecute(CommandExecutor executor) {
		return (s, a) -> {
			try {
				executor.executeWith(s, a);
			} catch(Exception e) {
				failure("Executing a command");
			}
		};
	}
	
	public void success() {
		CommandAPIMain.success();
	}
	
	public void failure(String test) {
		CommandAPIMain.failure(this.getClass().getSimpleName() + ": " + test);
	}
	
	// index = 0: last message
	// index = 1: second to last message
	// etc.
	public void assertContains(int index, String input, Runnable operation) {
		operation.run();
		List<String> log = CommandAPIMain.getLog();
		String msg = log.get(log.size() - 1 - index);
		if(!msg.contains(input)) {
			failure("'" + msg + "' does not contain '" + input + "'");
		};
	}
	
	public void assertEquals(int index, String input, Runnable operation) {
		operation.run();
		List<String> log = CommandAPIMain.getLog();
		
		//[00:32:24] [Server thread/INFO]: ...123.12314 2<--[HERE]
		String msg = log.get(log.size() - 1 - index);
		msg = msg.substring(msg.indexOf("]") + 1);
		msg = msg.substring(msg.indexOf("]") + 3);
		if(!msg.equals(input)) {
			failure("'" + msg + "' does not equal '" + input + "'");
		}
	}
	
	public <T> void assertEquals(T object1, T object2) {
		if(!object1.equals(object2)) {
			failure("'" + object1 + "' does not equal '" + object2 + "'");
		}
	}
}
