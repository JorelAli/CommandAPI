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
import java.util.function.Predicate;

import org.bukkit.inventory.ItemStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.nms.NMS;

/**
 * An argument that represents a <code>Predicate&lt;ItemStack&gt;</code>
 */
@SuppressWarnings("rawtypes")
public class ItemStackPredicateArgument extends Argument<Predicate> implements InitialParseExceptionArgument<Predicate, Argument<Predicate>>{
	
	/**
	 * A ItemStack Predicate argument. Represents a predicate for itemstacks
	 * @param nodeName the name of the node for this argument 
	 */
	public ItemStackPredicateArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentItemPredicate());
	}

	@Override
	public Class<Predicate> getPrimitiveType() {
		return Predicate.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.ITEMSTACK_PREDICATE;
	}
	
	@Override
	public <CommandListenerWrapper> Predicate<ItemStack> parseArgument(NMS<CommandListenerWrapper> nms,
			CommandContext<CommandListenerWrapper> cmdCtx, String key, Object[] previousArgs) throws CommandSyntaxException {
		return nms.getItemStackPredicate(cmdCtx, key);
	}

	private InitialParseExceptionHandler<Predicate> exceptionHandler;

	@Override
	public Argument<Predicate> withInitialParseExceptionHandler(InitialParseExceptionHandler<Predicate> exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
		return this;
	}

	@Override
	public Optional<InitialParseExceptionHandler<Predicate>> getInitialParseExceptionHandler() {
		return Optional.ofNullable(exceptionHandler);
	}
}
