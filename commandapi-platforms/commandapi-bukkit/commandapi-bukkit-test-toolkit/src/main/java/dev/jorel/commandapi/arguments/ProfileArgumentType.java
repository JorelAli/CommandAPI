package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.jorel.commandapi.MockCommandSource;
import dev.jorel.commandapi.UnimplementedMethodException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ProfileArgumentType implements ArgumentType<ProfileArgumentType.Result> {
	// No internal state is necessary
	public static final ProfileArgumentType INSTANCE = new ProfileArgumentType();

	private ProfileArgumentType() {

	}

	// ArgumentType implementation
	@FunctionalInterface
	public interface Result {
		Collection<UUID> getProfiles(MockCommandSource source) throws CommandSyntaxException;
	}

	public static final SimpleCommandExceptionType ERROR_UNKNOWN_PLAYER = new SimpleCommandExceptionType(
		() -> "That player does not exist"
	);

	private static final Parser<Result> PARSER = Parser
		.tryParse(reader -> {
				if (!(reader.canRead() && reader.peek() == '@')) throw Parser.NEXT_BRANCH;
			}, builder -> builder
				.tryParse(EntitySelectorParser.PARSER, (entitySelectorGetter, builder2) -> builder2
					// Input entity selector
					.conclude(reader -> {
						EntitySelector entitySelector = entitySelectorGetter.get();
						if (entitySelector.includesEntities()) {
							throw EntitySelectorArgumentType.ERROR_ONLY_PLAYERS_ALLOWED.create();
						}
						return (Result) source -> {
							List<Player> players = entitySelector.findPlayers(source);
							if (players.isEmpty()) {
								throw EntitySelectorArgumentType.NO_PLAYERS_FOUND.create();
							}

							List<UUID> profiles = new ArrayList<>(players.size());
							for (Player player : players) {
								profiles.add(player.getUniqueId());
							}
							return profiles;
						};
					})
				).alwaysThrowException()
		).neverThrowException()
		.tryParse(Parser.readUntilWithoutEscapeCharacter(' '), (nameGetter, builder) -> builder
			// Input player name
			.conclude(reader -> {
				String name = nameGetter.get();
				return source -> {
					// TODO: I'm not sure if or how this should check if offline player profiles exist
					Player player = Bukkit.getPlayerExact(name);
					if (player == null) {
						throw ERROR_UNKNOWN_PLAYER.create();
					}
					return Collections.singleton(player.getUniqueId());
				};
			})
		).alwaysThrowException();

	@Override
	public Result parse(StringReader reader) throws CommandSyntaxException {
		return PARSER.parse(reader);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		// TODO: Implement suggestions
		throw new UnimplementedMethodException();
	}

	public static Collection<UUID> getProfiles(CommandContext<MockCommandSource> cmdCtx, String key) throws CommandSyntaxException {
		return cmdCtx.getArgument(key, Result.class).getProfiles(cmdCtx.getSource());
	}
}
