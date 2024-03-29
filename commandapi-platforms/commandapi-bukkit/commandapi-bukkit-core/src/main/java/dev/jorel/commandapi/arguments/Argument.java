package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.jorel.commandapi.BukkitExecutable;
import org.bukkit.command.CommandSender;

/**
 * The core abstract class for Command API arguments
 *
 * @param <T> The type of the underlying object that this argument casts to
 */
public abstract class Argument<T> extends AbstractArgument<T, Argument<T>, Argument<?>, CommandSender> implements BukkitExecutable<Argument<T>> {
	/**
	 * Constructs an argument with a given NMS/brigadier type.
	 *
	 * @param nodeName the name to assign to this argument node
	 * @param rawType  the NMS or brigadier type to be used for this argument
	 */
	protected Argument(String nodeName, ArgumentType<?> rawType) {
		super(nodeName, rawType);
	}

	@Override
	public Argument<T> instance() {
		return this;
	}
}
