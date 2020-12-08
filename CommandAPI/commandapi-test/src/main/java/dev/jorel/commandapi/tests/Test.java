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
				failure();
			}
		};
	}
	
	public void success() {
		CommandAPIMain.success();
	}
	
	public void failure() {
		CommandAPIMain.failure(this.getClass().getSimpleName());
	}
	
	// index = 1: last message
	// index = 2: second to last message
	// etc.
	public void assertContains(int index, String message, Runnable operation) {
		operation.run();
		List<String> log = CommandAPIMain.getLog();
		log = log.subList(0, log.size() - 1);
		String msg = log.get(log.size() - index);
		if(!msg.contains(message)) {
			failure();
		};
	}
	
//	public boolean assertContains2(String message, Runnable operation) {
//		final boolean[] result = new boolean[] {true};
//		preLog(msg -> {
//			System.out.println("Msg: " + msg);
//			System.out.println("c.f: " + message);
//			if(!msg.contains(message)) {
//				result[0] = false;
//				postLog();
//				System.out.println("Failure! A CommandAPI test failed: " + this.getClass().getSimpleName());
//				CommandAPIMain.failure("FAILURE");
//				JavaPlugin.getPlugin(CommandAPIMain.class).getLogger().info("FAILURE!!!");
//				failure();
//			}
//		});
//		operation.run();
//		postLog();
//		return result[0];
//	}
//	
//	/////////////
//	// Logging //
//	/////////////
//	
//	Logger logger;
//	Appender appender;
//	
//	public void preLog(Consumer<String> consumer) {
//		logger = (Logger) LogManager.getRootLogger();
//		appender = new AbstractAppender("appender", null, null, false) {
//			
//			@Override
//			public void append(LogEvent record) {
//				consumer.accept(record.toImmutable().getMessage().getFormattedMessage());
//			}
//		};
//		appender.start();
//		logger.addAppender(appender);
//	}
//	
//	public void postLog() {
//		appender.stop();
//		logger.removeAppender(appender);
//	}
		
}
