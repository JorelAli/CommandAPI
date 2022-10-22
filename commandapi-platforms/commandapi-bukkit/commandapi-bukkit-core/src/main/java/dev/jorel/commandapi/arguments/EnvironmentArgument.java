/*******************************************************************************
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
package dev.jorel.commandapi.arguments;

import java.util.function.Function;

import dev.jorel.commandapi.BukkitExecutable;
import org.bukkit.World.Environment;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.jorel.commandapi.BukkitPlatform;
import dev.jorel.commandapi.abstractions.AbstractPlatform;
import org.bukkit.command.CommandSender;

/**
 * An argument that represents the Bukkit Environment object
 */
public class EnvironmentArgument extends SafeOverrideableArgument<Environment, Environment, EnvironmentArgument, CommandSender> implements BukkitExecutable<EnvironmentArgument> {
	
	/**
	 * An Environment argument. Represents Bukkit's Environment object
	 * @param nodeName the name of the node for this argument
	 */
	public EnvironmentArgument(String nodeName) {
		super(nodeName, BukkitPlatform.get()._ArgumentDimension(), ((Function<Environment, String>) Environment::name).andThen(String::toLowerCase));
	}
	
	@Override
	public Class<Environment> getPrimitiveType() {
		return Environment.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.ENVIRONMENT;
	}
	
	@Override
	public <CommandSourceStack> Environment parseArgument(AbstractPlatform<CommandSender, CommandSourceStack> platform,
			CommandContext<CommandSourceStack> cmdCtx, String key, Object[] previousArgs) throws CommandSyntaxException {
		return ((BukkitPlatform<CommandSourceStack>) platform).getDimension(cmdCtx, key);
	}
}
