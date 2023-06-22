package dev.jorel.commandapi.test;

import dev.jorel.commandapi.CommandAPICommand;

public class CommandAPITestCommand extends CommandAPICommand {

	@SuppressWarnings("rawtypes")
	private final ArgumentInspector listener;

	public CommandAPITestCommand(String commandName) {
		this(commandName, null);
	}

	public CommandAPITestCommand(String commandName, @SuppressWarnings("rawtypes") ArgumentInspector listener) {
		super(commandName);
		this.listener = listener;
	}
	
//	// Replace all executors with our 'testing' executor
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	private void mapExecutor(CustomCommandExecutor customCommandExecutor) {
//		// Do normal executors
//		{
//			ListIterator it = customCommandExecutor.getNormalExecutors().listIterator();
//			while(it.hasNext()) {
//				IExecutorNormal executor = (IExecutorNormal) it.next();
//				IExecutorNormal newExecutor = (sender, args) -> {
//					for(Object arg : args) {
//						listener.set(arg);
//					}
//					executor.executeWith(sender, args);
//				};
//				it.set(newExecutor);
//			}
//		}
//
//		// And now do resulting executors
//		{
//			ListIterator it = customCommandExecutor.getResultingExecutors().listIterator();
//			while(it.hasNext()) {
//				IExecutorResulting executor = (IExecutorResulting) it.next();
//				IExecutorResulting newExecutor = (sender, args) -> {
//					for(Object arg : args) {
//						listener.set(arg);
//					}
//					return executor.executeWith(sender, args);
//				};
//				it.set(newExecutor);
//			}
//		}
//	}
//
//	@Override
//	public void register() {
//		if(this.listener != null) {
//			mapExecutor(this.executor);
//			for (CommandAPICommand subcommand : this.getSubcommands()) {
//				mapExecutor(subcommand.getExecutor());
//			}
//		}
//		super.register();
//	}

}
