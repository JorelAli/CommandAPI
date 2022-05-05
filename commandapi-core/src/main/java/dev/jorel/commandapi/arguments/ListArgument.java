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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.jorel.commandapi.IStringTooltip;
import dev.jorel.commandapi.nms.NMS;

@SuppressWarnings("rawtypes")
public class ListArgument<T> extends SafeOverrideableArgument<Collection, Collection<T>> {

	private final String delimiter;
	private final boolean allowDuplicates;
	private final Supplier<Collection<T>> supplier;
	private final Function<T, String> mapper;

	private ListArgument(String nodeName, String delimiter, boolean allowDuplicates, /* TODO: This could be a standard collection, we'll use a builder for this*/Supplier<Collection<T>> supplier, Function<T, String> suggestionsMapper) {
		super(nodeName, StringArgumentType.greedyString(), c -> c.stream().map(suggestionsMapper::apply).collect(Collectors.joining(delimiter)));
		this.delimiter = delimiter;
		this.allowDuplicates = allowDuplicates;
		this.supplier = supplier;
		this.mapper = suggestionsMapper;

		applySuggestions();
	}

	private void applySuggestions() {
		// We use normal suggestions instead of sage suggestions because we're assuming
		// that this code is valid (and thus, don't have to do additional mapping checks)
		this.replaceSuggestions(ArgumentSuggestions.strings(info -> {
			String currentArg = info.currentArg();

			Map<String, T> values = new HashMap<>();
			for(T object : supplier.get()) {
				values.put(mapper.apply(object), object);
			}

			List<String> currentArgList = Arrays.asList(currentArg.split(delimiter));

			// If we end with the specified delimiter, we prompt for the next entry
			if(currentArg.endsWith(delimiter)) {

				if(!allowDuplicates) {
					for(String str : currentArgList) {
						values.remove(str);
					}
				}

				// 'values' now contains a set of all objects that are NOT in
				// the current list that the user is typing. We want to return
				// a list of the current argument + each value that isn't
				// in the list (i.e. each key in 'values')
				String[] returnValues = new String[values.size()];
				int i = 0;
				for(String str : values.keySet()) {
					returnValues[i] = currentArg + str;
					i++;
				}
				return returnValues;
			} else {

				// Auto-complete the current player that the user is typing
				// Remove the last argument and turn it into a string as the base for suggestions
				String nameStart = currentArgList.remove(currentArgList.size() - 1);
				String suggestionBase = currentArgList.isEmpty() ? "" : String.join(" ", currentArgList) + " ";

				return Bukkit.getOnlinePlayers().stream()
						.filter(player -> player.getName().startsWith(nameStart))
						.map(player -> suggestionBase + player.getName())
						.toArray(String[]::new);
			}
		}));
	}

	@Override
	public Class<Collection> getPrimitiveType() {
		return Collection.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.LIST;
	}

	@Override
	public <CommandListenerWrapper> Collection<T> parseArgument(NMS<CommandListenerWrapper> nms,
			CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		// Get the list of values which this can take
		Map<String, T> values = new HashMap<>();
		for(T object : supplier.get()) {
			values.put(mapper.apply(object), object);
		}

		// If the argument argument's value is in the list of values, include it
		List<T> list = new ArrayList<>();
		String[] strArr = cmdCtx.getArgument(key, String.class).split(delimiter);
		for(String str : strArr) {
			if(values.containsKey(str)) {
				list.add(values.get(str));
			}
		}
		return list;
	}

	public static class ListArgumentBuilder<T> {

		private final String nodeName;
		private final String delimiter;
		private boolean allowDuplicates = false;

		public ListArgumentBuilder(String nodeName) {
			this(nodeName, " ");
		}

		public ListArgumentBuilder(String nodeName, String delimiter) {
			this.nodeName = nodeName;
			this.delimiter = delimiter;
		}

		public ListArgumentBuilder allowDuplicates(boolean allowDuplicates) {
			this.allowDuplicates = allowDuplicates;
			return this;
		}

		public ListArgumentBuilderSuggests withList(Supplier<Collection<T>> list) {
			return new ListArgumentBuilderSuggests(list);
		}

		public ListArgumentBuilderSuggests withList(Collection<T> list) {
			return new ListArgumentBuilderSuggests(() -> list);
		}

		public class ListArgumentBuilderSuggests {

			private final Supplier<Collection<T>> supplier;

			private ListArgumentBuilderSuggests(Supplier<Collection<T>> list) {
				this.supplier = list;
			}
			
			public ListArgumentBuilderFinished withStringMapper() {
				return new ListArgumentBuilderFinished(x -> String.valueOf(x));
			}

			public ListArgumentBuilderFinished withMapper(Function<T, String> mapper) {
				return new ListArgumentBuilderFinished(mapper);
			}
			
			public ListArgumentBuilderFinished withTooltipMapper(Function<T, IStringTooltip> mapper) {
				return new ListArgumentBuilderFinished(null);
			}

			public class ListArgumentBuilderFinished {

				private final Function<T, String> mapper;

				private ListArgumentBuilderFinished(Function<T, String> mapper) {
					this.mapper = mapper;
				}

				public ListArgument build() {
					return new ListArgument<>(nodeName, delimiter, allowDuplicates, supplier, mapper);
				}
			}
		}

	}
}
