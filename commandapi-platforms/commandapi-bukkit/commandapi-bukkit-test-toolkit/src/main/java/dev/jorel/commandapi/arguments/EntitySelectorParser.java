package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.jorel.commandapi.UnimplementedMethodException;
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
	private static Parser.Void isSelectorStart() {
		return reader -> {
			if (!(reader.canRead() && reader.peek() == '@')) throw Parser.NEXT_BRANCH;
		};
	}

	private static Parser.Void parseSelector(Parser.ParameterGetter<EntitySelectorParser> selectorBuilderGetter) {
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

	private static Parser.Void isSelectorOptionsStart() {
		return reader -> {
			if (!(reader.canRead() && reader.peek() == '[')) throw Parser.NEXT_BRANCH;
		};
	}

	private static Parser.Void parseSelectorOptions(Parser.ParameterGetter<EntitySelectorParser> selectorBuilderGetter) {
		return reader -> {
			// TODO: Implement looping to parse these selector options
			//  I'm pretty sure it would basically reuse many other object parsers as well, so maybe do those first
			throw new UnimplementedMethodException("Entity selectors with options are not supported");
		};
	}

	private static Parser.Void parseNameOrUUID(Parser.ParameterGetter<EntitySelectorParser> selectorBuilderGetter) {
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
				if (input.isEmpty() || input.length() > 16) {
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

	private static Parser<EntitySelector> conclude(Parser.ParameterGetter<EntitySelectorParser> selectorBuilderGetter) {
		return reader -> selectorBuilderGetter.get().build();
	}

	public static final Parser<EntitySelector> PARSER = Parser
		.tryParse(reader -> new EntitySelectorParser(), (selectorBuilderGetter, builder) -> builder
			.tryParse(isSelectorStart(), builder1 -> builder1
				.tryParse(parseSelector(selectorBuilderGetter), builder2 -> builder2
					.tryParse(isSelectorOptionsStart(), builder3 -> builder3
						.tryParse(parseSelectorOptions(selectorBuilderGetter), builder4 -> builder4
							// Input @?[...]
							.conclude(conclude(selectorBuilderGetter))
						).alwaysThrowException()
					).neverThrowException()
					// Input @? with no options
					.conclude(conclude(selectorBuilderGetter))
				).alwaysThrowException()
			).neverThrowException()
			.tryParse(parseNameOrUUID(selectorBuilderGetter), builder1 -> builder1
				// Input ????? as a name or UUID
				.conclude(conclude(selectorBuilderGetter))
			).alwaysThrowException()
		).alwaysThrowException();
}
