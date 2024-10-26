package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.jorel.commandapi.UnimplementedMethodException;
import dev.jorel.commandapi.arguments.parser.Parser;
import dev.jorel.commandapi.arguments.parser.ParserLiteral;
import dev.jorel.commandapi.arguments.parser.Result;
import dev.jorel.commandapi.arguments.parser.SuggestionProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

public class EntitySelectorParser {
	// Errors
	public static final SimpleCommandExceptionType ERROR_INVALID_NAME_OR_UUID = new SimpleCommandExceptionType(
		ArgumentUtilities.translatedMessage("argument.entity.invalid")
	);
	public static final DynamicCommandExceptionType ERROR_UNKNOWN_SELECTOR_TYPE = new DynamicCommandExceptionType(
		object -> ArgumentUtilities.translatedMessage("argument.entity.selector.unknown", object)
	);
	public static final SimpleCommandExceptionType ERROR_MISSING_SELECTOR_TYPE = new SimpleCommandExceptionType(
		ArgumentUtilities.translatedMessage("argument.entity.selector.missing")
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
	private static ParserLiteral parseSelector(EntitySelectorParser selectorBuilder) {
		return Parser.read(reader -> {
			reader.skip(); // skip @
			if (!reader.canRead()) {
				throw ERROR_MISSING_SELECTOR_TYPE.createWithContext(reader);
			}

			char selectorCode = reader.read();
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
		}).suggests(suggestSelector);
	}

	private static ParserLiteral parseSelectorOptions(EntitySelectorParser selectorBuilder) {
		return reader -> {
			// TODO: Implement looping to parse these selector options
			//  I'm pretty sure it would basically reuse many other object parsers as well, so maybe do those first
			throw new UnimplementedMethodException("Entity selectors with options are not supported");
		};
	}

	private static ParserLiteral parseNameOrUUID(EntitySelectorParser selectorBuilder) {
		return Parser.read(reader -> {
			int start = reader.getCursor();
			String input = reader.readString();
			try {
				// Check if this is a UUID
				selectorBuilder.entityUUID = UUID.fromString(input);
				selectorBuilder.includesEntities = true;
			} catch (IllegalArgumentException ignored) {
				// Not a valid UUID string
				if (input.isEmpty() || input.length() > 16) {
					// Also not a valid player name
					reader.setCursor(start);
					throw ERROR_INVALID_NAME_OR_UUID.createWithContext(reader);
				}

				selectorBuilder.includesEntities = false;
				selectorBuilder.playerName = input;
			}

			selectorBuilder.maxResults = 1;
		}).suggests(suggestName);
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
		builder.suggest("@p", ArgumentUtilities.translatedMessage("argument.entity.selector.nearestPlayer"));
		builder.suggest("@a", ArgumentUtilities.translatedMessage("argument.entity.selector.allPlayers"));
		builder.suggest("@r", ArgumentUtilities.translatedMessage("argument.entity.selector.randomPlayer"));
		builder.suggest("@s", ArgumentUtilities.translatedMessage("argument.entity.selector.self"));
		builder.suggest("@e", ArgumentUtilities.translatedMessage("argument.entity.selector.allEntities"));
		builder.suggest("@n", ArgumentUtilities.translatedMessage("argument.entity.selector.nearestEntity"));
	};
	private static final SuggestionProvider suggestNameOrSelector = (context, builder) -> {
		suggestSelector.addSuggestions(context, builder);
		suggestName.addSuggestions(context, builder);
	};
	private static final SuggestionProvider suggestOpenOptions = (context, builder) -> builder.suggest("[");

	public static final Parser<EntitySelector> parser = reader -> {
		if (!reader.canRead()) {
			// Empty input
			return Result.withExceptionAndSuggestions(ERROR_INVALID_NAME_OR_UUID.createWithContext(reader), reader.getCursor(), suggestNameOrSelector);
		}

		// Build our selector
		EntitySelectorParser selectorBuilder = new EntitySelectorParser();
		Function<Result.Void, Result<EntitySelector>> conclude = Result.wrapFunctionResult(success -> selectorBuilder.build());

		if (reader.peek() == '@') {
			// Looks like selector
			return parseSelector(selectorBuilder).getResult(reader).continueWith(
				// Successfully read selector
				success -> {
					if (reader.canRead() && reader.peek() == '[') {
						// Looks like includes selector options
						return parseSelectorOptions(selectorBuilder).getResult(reader).continueWith(
							// If successful, build the final selector
							conclude
							// Otherwise, pass original exception
						);
					}

					// Otherwise, valid selector, but suggest opening options
					return Result.withValueAndSuggestions(selectorBuilder.build(), reader.getCursor(), suggestOpenOptions);
				}
				// Otherwise pass original exception
			);
		}

		// Looks like name/uuid
		return parseNameOrUUID(selectorBuilder).getResult(reader).continueWith(
			// If successful, build the final selector
			conclude
			// Otherwise pass original exception
		);
	};
}
