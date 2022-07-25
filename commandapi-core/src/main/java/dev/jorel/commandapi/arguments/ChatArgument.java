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

import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.exceptions.SpigotNotFoundException;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.wrappers.PreviewableFunction;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * An argument that represents chat with entity selectors
 * 
 * @apiNote Returns a {@link BaseComponent}{@code []} object
 */
public class ChatArgument extends Argument<BaseComponent[]> implements IGreedyArgument, IPreviewable<ChatArgument, BaseComponent[]> {

	private PreviewableFunction<BaseComponent[]> preview;
	
	/**
	 * Constructs a Chat argument with a given node name. Represents fancy greedy
	 * strings that can parse entity selectors
	 * 
	 * @param nodeName the name of the node for argument
	 */
	public ChatArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentChat());
		
		try {
			Class.forName("org.spigotmc.SpigotConfig");
		} catch(ClassNotFoundException e) {
			throw new SpigotNotFoundException(this.getClass());
		}
	}

	@Override
	public Class<BaseComponent[]> getPrimitiveType() {
		return BaseComponent[].class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.CHAT;
	}
	
	@Override
	public <CommandListenerWrapper> BaseComponent[] parseArgument(NMS<CommandListenerWrapper> nms,
			CommandContext<CommandListenerWrapper> cmdCtx, String key, Object[] previousArgs) throws CommandSyntaxException {
		final CommandSender sender = nms.getCommandSenderFromCSS(cmdCtx.getSource());
		if(getPreview().isPresent() && sender instanceof Player player) {
			try {
				getPreview().get().generatePreview(new PreviewInfo(player, CommandAPIHandler.getRawArgumentInput(cmdCtx, key), cmdCtx.getInput()));
			} catch (WrapperCommandSyntaxException e) {
				throw e.getException();
			}
		}
		return nms.getChat(cmdCtx, key);
	}

	@Override
	public ChatArgument withPreview(PreviewableFunction<BaseComponent[]> preview) {
		this.preview = preview;
		return this;
	}

	@Override
	public Optional<PreviewableFunction<BaseComponent[]>> getPreview() {
		return Optional.ofNullable(preview);
	}
}
