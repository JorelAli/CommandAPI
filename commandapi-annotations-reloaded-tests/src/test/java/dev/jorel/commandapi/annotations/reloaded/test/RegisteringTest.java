package dev.jorel.commandapi.annotations.reloaded.test; /*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
import dev.jorel.commandapi.annotations.reloaded.test.HordeCommand2;
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
