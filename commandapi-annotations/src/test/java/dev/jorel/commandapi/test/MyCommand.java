package dev.jorel.commandapi.test;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.annotations.annotations.Command;
import dev.jorel.commandapi.annotations.annotations.Subcommand;
import dev.jorel.commandapi.annotations.arguments.APlayerArgument;

// mycommand hi - says hi to everyone
// mycommand nested <Player> - says hi to a player
// mycommand nested hello - says hello to everyone

@Command("mycommand")
public class MyCommand {

	@Subcommand("hi")
	public class Hi {

		@Subcommand
		public void hi(CommandSender sender) {
			System.out.println("Hi");
		}

	}

	// TODO: This should be a valid case which does the same thing as the thing
	// above it
//	@Subcommand("hi")
//	public void hi(CommandSender sender) {
//		System.out.println("Hi");
//	}

	@Subcommand("nested")
	public class Nested {

		@Subcommand
		public void toPlayer(CommandSender sender, @APlayerArgument Player player) {
			System.out.println("Hi " + player.getName());
		}

		@Subcommand("hello")
		public class Hello {

			@Subcommand
			public void hello(CommandSender sender) {
				System.out.println("Hello");
			}

		}

	}

}
