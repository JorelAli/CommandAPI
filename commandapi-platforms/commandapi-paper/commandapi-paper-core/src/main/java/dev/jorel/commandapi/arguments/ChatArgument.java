/*******************************************************************************
 * Copyright 2018, 2021 Jorel Ali (Skepter) - MIT License
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
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPIPaper;
import dev.jorel.commandapi.commandsenders.BukkitPlayer;
import dev.jorel.commandapi.exceptions.PaperAdventureNotFoundException;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandArguments;
import dev.jorel.commandapi.wrappers.PreviewableFunction;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * An argument that represents chat with entity selectors
 * 
 * @since 5.10
 * 
 * @apiNote Returns a {@link Component} object
 */
public class ChatArgument extends Argument<Component> implements GreedyArgument, Previewable<ChatArgument, Component> {

	private PreviewableFunction<Component> preview;
	private boolean usePreview;

	/**
	 * Constructs a Chat argument with a given node name. Represents fancy greedy
	 * strings that can parse entity selectors
	 * 
	 * @param nodeName the name of the node for argument
	 */
	public ChatArgument(String nodeName) {
		super(nodeName, CommandAPIBukkit.get()._ArgumentChat());

		try {
			Class.forName("net.kyori.adventure.text.Component");
		} catch (ClassNotFoundException e) {
			throw new PaperAdventureNotFoundException(this.getClass());
		}
	}

	@Override
	public Class<Component> getPrimitiveType() {
		return Component.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.ADVENTURE_CHAT;
	}

	@Override
	public <CommandSourceStack> Component parseArgument(CommandContext<CommandSourceStack> cmdCtx, String key, CommandArguments previousArgs) throws CommandSyntaxException {
		final CommandSender sender = CommandAPIPaper.<CommandSourceStack>getBukkit().getCommandSenderFromCommandSource(cmdCtx.getSource()).getSource();
		Component component = CommandAPIPaper.<CommandSourceStack>getPaper().getChat(cmdCtx, key);

		Optional<PreviewableFunction<Component>> previewOptional = getPreview();
		if (this.usePreview && previewOptional.isPresent() && sender instanceof Player player) {
			try {
				Component previewComponent = previewOptional.get()
					.generatePreview(new PreviewInfo<>(new BukkitPlayer(player), CommandAPIHandler.getRawArgumentInput(cmdCtx, key), cmdCtx.getInput(), component));

				component = previewComponent;
			} catch (WrapperCommandSyntaxException e) {
				throw e.getException();
			}
		}
		
		return component;
	}

	@Override
	public ChatArgument withPreview(PreviewableFunction<Component> preview) {
		this.preview = preview;
		return this;
	}

	@Override
	public Optional<PreviewableFunction<Component>> getPreview() {
		return Optional.ofNullable(preview);
	}

	@Override
	public ChatArgument usePreview(boolean usePreview) {
		this.usePreview = usePreview;
		return this;
	}
}
