package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.jorel.commandapi.AbstractPlatform;
import dev.jorel.commandapi.IStringTooltip;
import dev.jorel.commandapi.StringTooltip;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * An argument that accepts a list of objects
 *
 * @param <T> the type that this list argument generates a list of.
 */
@SuppressWarnings("rawtypes")
public class ListArgumentCommon<T> extends Argument<List> {
	private final String delimiter;
	private final boolean allowDuplicates;
	private final Function<CommandSender, Collection<T>> supplier;
	private final Function<T, IStringTooltip> mapper;

	ListArgumentCommon(String nodeName, String delimiter, boolean allowDuplicates, Function<CommandSender, Collection<T>> supplier, Function<T, IStringTooltip> suggestionsMapper, StringArgumentType rawType) {
		super(nodeName, rawType);
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
			for (T object : supplier.apply(info.sender())) {
				values.add(mapper.apply(object));
			}

			String[] splitArguments = currentArg.split(Pattern.quote(delimiter));
			// If an argument is finished, suggest the delimiter
			String lastArgument = splitArguments[splitArguments.length - 1];
			if (!currentArg.endsWith(delimiter) && values.stream().map(IStringTooltip::getSuggestion).anyMatch(lastArgument::equals)) {
				values.add(StringTooltip.none(lastArgument + delimiter));
			}

			if (!allowDuplicates) {
				// Filter out values already given
				for (String str : splitArguments) {
					IStringTooltip valueToRemove = null;
					for (IStringTooltip value : values) {
						if (value.getSuggestion().equals(str)) {
							valueToRemove = value;
							break;
						}
					}
					if (valueToRemove != null) {
						values.remove(valueToRemove);
					}
				}
			}


			// Offset builder to just after the last argument
			if (currentArg.contains(delimiter)) {
				builder = builder.createOffset(builder.getStart() + currentArg.lastIndexOf(delimiter) + delimiter.length());
			}

			// 'values' is a set of all objects that need to be suggested
			for (IStringTooltip str : values) {
				if (str.getSuggestion().startsWith(builder.getRemaining())) {
					if (str.getTooltip() == null)
						builder.suggest(str.getSuggestion());
					else
						builder.suggest(str.getSuggestion(), str.getTooltip());
				}
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
	public <CommandListenerWrapper> List<T> parseArgument(AbstractPlatform<Argument<?>, CommandSender, CommandListenerWrapper> nms,
			CommandContext<CommandListenerWrapper> cmdCtx, String key, Object[] previousArgs) throws CommandSyntaxException {
		// Get the list of values which this can take
		Map<IStringTooltip, T> values = new HashMap<>();
		for (T object : supplier.apply(nms.getCommandSenderFromCommandSource(cmdCtx.getSource()).getSource())) {
			values.put(mapper.apply(object), object);
		}

		// If the argument's value is in the list of values, include it
		List<T> list = new ArrayList<>();
		String argument = cmdCtx.getArgument(key, String.class);
		String[] strArr = argument.split(Pattern.quote(delimiter));
		StringReader context = new StringReader(argument);
		int cursor = 0;
		for (String str : strArr) {
			boolean addedItem = false;
			// Yes, this isn't an instant lookup HashMap, but this is the best we can do
			for (IStringTooltip value : values.keySet()) {
				if (value.getSuggestion().equals(str)) {
					if (allowDuplicates) {
						list.add(values.get(value));
					} else {
						if (!list.contains(values.get(value))) {
							list.add(values.get(value));
						} else {
							context.setCursor(cursor);
							throw new SimpleCommandExceptionType(new LiteralMessage("Duplicate arguments are not allowed")).createWithContext(context);
						}
					}
					addedItem = true;
				}
			}
			if(!addedItem) {
				context.setCursor(cursor);
				throw new SimpleCommandExceptionType(new LiteralMessage("Item is not allowed in list")).createWithContext(context);
			}
			cursor += str.length() + delimiter.length();
		}
		return list;
	}
}
