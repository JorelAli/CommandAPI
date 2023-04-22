package io.github.jorelali;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.arguments.ANBTCompoundArgument;
import dev.jorel.commandapi.nbtapi.NBTContainer;
import org.bukkit.command.CommandSender;

@Command("nbt")
public class NBTCommand {
	@Default
	public static void nbt(CommandSender sender, @ANBTCompoundArgument NBTContainer nbt) {
		sender.sendMessage(nbt.toString());
	}
}
