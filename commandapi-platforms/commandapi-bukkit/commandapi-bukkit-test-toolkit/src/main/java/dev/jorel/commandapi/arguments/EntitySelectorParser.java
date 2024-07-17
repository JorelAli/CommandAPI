package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.jorel.commandapi.UnimplementedMethodException;
import dev.jorel.commandapi.arguments.parser.ParameterGetter;
import dev.jorel.commandapi.arguments.parser.Parser;
import dev.jorel.commandapi.arguments.parser.SuggestionProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.UUID;
import java.util.function.Predicate;

public class EntitySelectorParser {
	// Errors
	public static final SimpleCommandExceptionType ERROR_INVALID_NAME_OR_UUID = new SimpleCommandExceptionType(
		() -> "Invalid name or UUID"
	);
	public static final DynamicCommandExceptionType ERROR_UNKNOWN_SELECTOR_TYPE = new DynamicCommandExceptionType(
		object -> {
			String message = "Unknown selector type '%s'".formatted(object);
			return () -> message;
		}
	);
	public static final SimpleCommandExceptionType ERROR_MISSING_SELECTOR_TYPE = new SimpleCommandExceptionType(
		() -> "Missing selector type"
	);

	// State for building a selector
	private EntitySelectorParser() {

	}

	private int maxResults;
	private boolean includesEntities;
	private EntitySelector.Order order = EntitySelector.ORDER_ARBITRARY;
	private boolean selfSelector;
	private String playerName;

	private Predicate<Entity> entityCheck = entity -> true;
	private UUID entityUUID;
	private EntityType type;

	private EntitySelector build() {
		return new EntitySelector(
			maxResults, includesEntities, order, selfSelector, playerName,
			entityCheck, entityUUID, type
		);
	}

	// Parsing
	private static final Parser.Literal isSelectorStart = reader -> {
		if (!(reader.canRead() && reader.peek() == '@')) throw Parser.NEXT_BRANCH;
	};

	private static Parser.Literal parseSelector(ParameterGetter<EntitySelectorParser> selectorBuilderGetter) {
		return reader -> {
			reader.skip(); // skip @
			if (!reader.canRead()) throw ERROR_MISSING_SELECTOR_TYPE.createWithContext(reader);
			char selectorCode = reader.read();
			EntitySelectorParser selectorBuilder = selectorBuilderGetter.get();
			switch (selectorCode) {
				case 'p' -> {
					selectorBuilder.maxResults = 1;
					selectorBuilder.includesEntities = false;
					selectorBuilder.order = EntitySelector.ORDER_NEAREST;
					selectorBuilder.type = EntityType.PLAYER;
				}
				case 'a' -> {
					selectorBuilder.maxResults = Integer.MAX_VALUE;
					selectorBuilder.includesEntities = false;
					selectorBuilder.order = EntitySelector.ORDER_ARBITRARY;
					selectorBuilder.type = EntityType.PLAYER;
				}
				case 'r' -> {
					selectorBuilder.maxResults = 1;
					selectorBuilder.includesEntities = false;
					selectorBuilder.order = EntitySelector.ORDER_RANDOM;
					selectorBuilder.type = EntityType.PLAYER;
				}
				case 's' -> {
					selectorBuilder.maxResults = 1;
					selectorBuilder.includesEntities = true;
					selectorBuilder.selfSelector = true;
				}
				case 'e' -> {
					selectorBuilder.maxResults = Integer.MAX_VALUE;
					selectorBuilder.includesEntities = true;
					selectorBuilder.order = EntitySelector.ORDER_ARBITRARY;
					// Funnily, Minecraft checks Entity::isAlive, but Bukkit only exposes the inverse of that method
					selectorBuilder.entityCheck = entity -> !entity.isDead();
				}
				case 'n' -> {
					selectorBuilder.maxResults = 1;
					selectorBuilder.includesEntities = true;
					selectorBuilder.order = EntitySelector.ORDER_NEAREST;
					selectorBuilder.entityCheck = entity -> !entity.isDead();
				}
				default -> {
					// Move under @
					reader.setCursor(reader.getCursor() - 1);
					throw ERROR_UNKNOWN_SELECTOR_TYPE.createWithContext(reader, "@" + selectorCode);
				}
			}
		};
	}

	private static final Parser.Literal isSelectorOptionsStart = reader -> {
		if (!(reader.canRead() && reader.peek() == '[')) throw Parser.NEXT_BRANCH;
	};

	private static Parser.Literal parseSelectorOptions(ParameterGetter<EntitySelectorParser> selectorBuilderGetter) {
		return reader -> {
			// TODO: Implement looping to parse these selector options
			//  I'm pretty sure it would basically reuse many other object parsers as well, so maybe do those first
			throw new UnimplementedMethodException("Entity selectors with options are not supported");
		};
	}

	private static final Parser.Literal isNameStart = reader -> {
		if (!(reader.canRead() && reader.peek() != ' ')) throw ERROR_INVALID_NAME_OR_UUID.createWithContext(reader);
	};

	private static Parser.Literal parseNameOrUUID(ParameterGetter<EntitySelectorParser> selectorBuilderGetter) {
		return reader -> {
			EntitySelectorParser selectorBuilder = selectorBuilderGetter.get();

			int start = reader.getCursor();
			String input = reader.readString();
			try {
				// Check if this is a UUID
				selectorBuilder.entityUUID = UUID.fromString(input);
				selectorBuilder.includesEntities = true;
			} catch (IllegalArgumentException ignored) {
				// Not a valid UUID string
				if (input.length() > 16) {
					// Also not a valid player name
					reader.setCursor(start);
					throw ERROR_INVALID_NAME_OR_UUID.createWithContext(reader);
				}

				selectorBuilder.includesEntities = false;
				selectorBuilder.playerName = input;
			}

			selectorBuilder.maxResults = 1;
		};
	}

	private static Parser.Argument<EntitySelector> conclude(ParameterGetter<EntitySelectorParser> selectorBuilderGetter) {
		return reader -> selectorBuilderGetter.get().build();
	}

	private static final SuggestionProvider suggestName = (context, builder) -> {
		String remaining = builder.getRemainingLowerCase();

		Bukkit.getOnlinePlayers().forEach(player -> {
			String name = player.getName().toLowerCase();
			if (name.startsWith(remaining)) {
				builder.suggest(player.getName());
			}
		});
	};
	private static final SuggestionProvider suggestSelector = (context, builder) -> {
		builder.suggest("@p", () -> "Nearest player");
		builder.suggest("@a", () -> "All players");
		builder.suggest("@r", () -> "Random player");
		builder.suggest("@s", () -> "Current entity");
		builder.suggest("@e", () -> "All entities");
	};
	private static final SuggestionProvider suggestNameOrSelector = (context, builder) -> {
		suggestSelector.addSuggestions(context, builder);
		suggestName.addSuggestions(context, builder);
	};
	private static final SuggestionProvider suggestOpenOptions = (context, builder) -> builder.suggest("[");
	private static final SuggestionProvider suggestOptionsKeyOrClose = (context, builder) -> {
		throw new UnimplementedMethodException("Entity selectors with options are not supported");
	};

	public static final Parser<EntitySelector> PARSER = Parser
		.parse(reader -> new EntitySelectorParser())
		.suggests(suggestNameOrSelector)
		.alwaysThrowException()
		.continueWith(selectorBuilder ->
			Parser.tryParse(Parser.read(isSelectorStart)
				.neverThrowException()
				.continueWith(
					Parser.read(parseSelector(selectorBuilder))
						.suggests(suggestSelector)
						.alwaysThrowException()
						.continueWith(
							Parser.tryParse(Parser.read(isSelectorOptionsStart)
								.suggests(suggestOpenOptions)
								.neverThrowException()
								.continueWith(
									Parser.read(parseSelectorOptions(selectorBuilder))
										.suggests(suggestOptionsKeyOrClose)
										.alwaysThrowException()
										// Input @?[???]
										.continueWith(conclude(selectorBuilder))
								)
							).then(conclude(selectorBuilder)) // Input @?
						)
				)
			).then(Parser.read(isNameStart)
				.alwaysThrowException()
				.continueWith(
					Parser.read(parseNameOrUUID(selectorBuilder))
						.suggests(suggestName)
						.alwaysThrowException()
						// Input name or uuid
						.continueWith(conclude(selectorBuilder))
				)
			)
		);
}
