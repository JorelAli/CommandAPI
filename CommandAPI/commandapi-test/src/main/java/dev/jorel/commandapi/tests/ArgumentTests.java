package dev.jorel.commandapi.tests;

import org.bukkit.Bukkit;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.FloatArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LongArgument;

public class ArgumentTests extends Test {

	public void register() {
		new CommandAPICommand(cmd("primitives"))
		.withArguments(new IntegerArgument("int"))
		.withArguments(new LongArgument("long"))
		.withArguments(new FloatArgument("float"))
		.withArguments(new DoubleArgument("double"))
		.withArguments(new BooleanArgument("boolean"))
		.executes(tryExecute((sender, args) -> {
			int arg1 = (int) args[0];
			long arg2 = (long) args[1];
			float arg3 = (float) args[2];
			double arg4 = (double) args[3];
			boolean arg5 = (boolean) args[4];
			
			assertEquals(arg1, 1);
			assertEquals(arg2, 8589934592L);
			assertEquals(arg3, 2.3F);
			assertEquals(arg4, 123.12314D);
			assertEquals(arg5, false);
		}))
		.register();
	}
	
	public void test() throws Exception {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd("primitives 1 8589934592 2.3 123.12314 false"));
		
		assertContains(1, "Invalid boolean, expected 'true' or 'false' but found '2'", () -> Bukkit
				.dispatchCommand(Bukkit.getConsoleSender(), cmd("primitives 1 8589934592 2.3 123.12314 2")));
	}
	
}
