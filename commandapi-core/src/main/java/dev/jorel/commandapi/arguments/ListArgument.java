/*******************************************************************************
 * Copyright 2022 Jorel Ali (Skepter) - MIT License
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

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.IStringTooltip;
import dev.jorel.commandapi.nms.NMS;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * An argument that accepts a list of objects
 * @param <T> the type that this list argument generates a list of.
 */
@SuppressWarnings("rawtypes")
public class ListArgument<T> extends Argument<List> implements IGreedyArgument {

	private final String delimiter;
	private final boolean allowDuplicates;
	private final Function<CommandSender, Collection<T>> supplier;
	private final Function<T, IStringTooltip> mapper;

	ListArgument(String nodeName, String delimiter, boolean allowDuplicates, Function<CommandSender, Collection<T>> supplier, Function<T, IStringTooltip> suggestionsMapper) {
		super(nodeName, StringArgumentType.greedyString());
		this.delimiter = delimiter;
		this.allowDuplicates = allowDuplicates;
		this.supplier = supplier;
		this.mapper = suggestionsMapper;

		applySuggestions();
	}

	private void applySuggestions() {
		this.replaceSuggestions((info, builder) -> {
			String currentArg = info.currentArg();

			// This need not be a sorted map because entries in suggestions are
			// automatically sorted anyway
			Set<IStringTooltip> values = new HashSet<>();
			for(T object : supplier.apply(info.sender())) {
				values.add(mapper.apply(object));
			}

			if(!allowDuplicates) {
				// filter out values already given
				for(String str : currentArg.split(Pattern.quote(delimiter))) {
					IStringTooltip valueToRemove = null;
					for(IStringTooltip value : values) {
						if(value.getSuggestion().equals(str)) {
							valueToRemove = value;
							break;
						}
					}
					if(valueToRemove != null) {
						values.remove(valueToRemove);
					}
				}
			}

			// offset builder to just after the last argument
			if(currentArg.contains(delimiter))
				builder = builder.createOffset(builder.getStart() + currentArg.lastIndexOf(delimiter) + delimiter.length());

			// 'values' is a set of all objects that need to be suggested
			for(IStringTooltip str: values) {
				if(str.getTooltip() == null)
					builder.suggest(str.getSuggestion());
				else
					builder.suggest(str.getSuggestion(), new LiteralMessage(str.getTooltip()));
			}

			return builder.buildFuture();
		});
	}

	@Override
	public Class<List> getPrimitiveType() {
		return List.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.LIST;
	}

	@Override
	public <CommandListenerWrapper> List<T> parseArgument(NMS<CommandListenerWrapper> nms,
			CommandContext<CommandListenerWrapper> cmdCtx, String key, Object[] previousArgs) throws CommandSyntaxException {
		// Get the list of values which this can take
		Map<IStringTooltip, T> values = new HashMap<>();
		for(T object : supplier.apply(nms.getCommandSenderFromCSS(cmdCtx.getSource()))) {
			values.put(mapper.apply(object), object);
		}

		// If the argument argument's value is in the list of values, include it
		List<T> list = new ArrayList<>();
		String[] strArr = cmdCtx.getArgument(key, String.class).split(Pattern.quote(delimiter));
		for(String str : strArr) {
			// Yes, this isn't an instant lookup HashMap, but this is the best we can do
			for(IStringTooltip value : values.keySet()) {
				if(value.getSuggestion().equals(str)) {
					if(allowDuplicates) {
						list.add(values.get(value));
					} else if(!list.contains(values.get(value))) {
						list.add(values.get(value));
					}
				}
			}
		}
		return list;
	}
}
