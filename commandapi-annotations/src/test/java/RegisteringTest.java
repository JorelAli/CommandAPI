import org.junit.jupiter.api.Test;

public class RegisteringTest {
	
	/**
	 * Registers a command. Used with the CommandAPI's Annotation API.
	 * @param commandClass the class to register
	 * @param instance the instance of the class to use
	 */
	public static <T> void registerCommand(Class<T> commandClass, T instance) {
		try {
			final Class<?> commandsClass = Class.forName("Commands");
			final Object commandsClassInstance = commandsClass.getDeclaredConstructor().newInstance();
			commandsClass.getDeclaredMethod("register", commandClass).invoke(commandsClassInstance, instance);
		} catch (ReflectiveOperationException e) {
			boolean shouldPrint = true;
			if (e.getCause() instanceof IllegalStateException illegalStateException) {
				if (illegalStateException.getMessage().contains("Tried to access CommandAPIHandler instance")) {
					// Shh shh shh, there there...
					shouldPrint = false;
				}
			}
			
			if (shouldPrint) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void register() {
		registerCommand(HordeCommand2.class, new HordeCommand2(123));
	}
	
}
