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

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.entity.Player;

/**
 * An argument that represents the Bukkit Player object.
 * <p>
 * Note that this Argument internally references Mojang's authentication servers to resolve
 * usernames, which gives it a slight performance overhead compared to {@link EntitySelectorArgument.OnePlayer}.
 * 
 * @since 1.1
 */
public class PlayerArgument extends SafeOverrideableArgument<Player, Player> {

	/**
	 * A Player argument. Produces a single player, regardless of whether
	 * <code>@a</code>, <code>@p</code>, <code>@r</code> or <code>@e</code> is used.
	 * 
	 * @param nodeName the name of the node for this argument
	 */
	public PlayerArgument(String nodeName) {
		super(nodeName, CommandAPIBukkit.get().getNMS()._ArgumentProfile(), Player::getName);
	}

	@Override
	public Class<Player> getPrimitiveType() {
		return Player.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.PLAYER;
	}
	
	@Override
	public <CommandSourceStack> Player parseArgument(CommandContext<CommandSourceStack> cmdCtx, String key, CommandArguments previousArgs) throws CommandSyntaxException {
		return CommandAPIBukkit.<CommandSourceStack>get().getNMS().getPlayer(cmdCtx, key);
	}
}
