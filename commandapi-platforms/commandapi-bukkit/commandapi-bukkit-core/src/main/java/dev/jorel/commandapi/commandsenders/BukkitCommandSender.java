package dev.jorel.commandapi.commandsenders;

import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface BukkitCommandSender<Source extends CommandSender> extends AbstractCommandSender<Source> {

	/**
	 * This method will attempt to return the relevant wrapped command sender for a given bukkit command sender. If not
	 * appropriate wrapper is found it will return null.
	 *
	 * @param source bukkit command sender
	 *
	 * @return wrapper command sender
	 */
	static @Nullable BukkitCommandSender<?> fromUnknownCommandSender(CommandSender source) {
		if(source instanceof BlockCommandSender blockCommandSender) {
			return new BukkitBlockCommandSender(blockCommandSender);
		}
		if(source instanceof ConsoleCommandSender consoleCommandSender) {
			return new BukkitConsoleCommandSender(consoleCommandSender);
		}
		//Player needs to go before entity as player extends entity
		if(source instanceof Player player) {
			return new BukkitPlayer(player);
		}
		if(source instanceof Entity entity) {
			return new BukkitEntity(entity);
		}
		if(source instanceof NativeProxyCommandSender nativeProxyCommandSender) {
			return new BukkitNativeProxyCommandSender(nativeProxyCommandSender);
		}
		if(source instanceof ProxiedCommandSender proxiedCommandSender) {
			return new BukkitProxiedCommandSender(proxiedCommandSender);
		}
		return null;
	}

}
