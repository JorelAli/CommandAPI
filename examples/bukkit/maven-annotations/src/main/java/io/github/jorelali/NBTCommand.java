package io.github.jorelali;

import org.bukkit.command.CommandSender;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.arguments.ANBTCompoundArgument;

@Command("nbt")
public class NBTCommand {
	@Default
	public static void nbt(CommandSender sender, @ANBTCompoundArgument NBTContainer nbt) {
		sender.sendMessage(nbt.toString());
	}
}
