package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.jorel.commandapi.MockCommandSource;
import dev.jorel.commandapi.arguments.parser.Parser;
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

	private static final Parser.Literal isSelectorStart = reader -> {
		if (!(reader.canRead() && reader.peek() == '@')) throw Parser.NEXT_BRANCH;
	};

	private static final Parser<Result> PARSER = Parser
		.tryParse(Parser.read(isSelectorStart)
			.neverThrowException()
			.continueWith(
				Parser.parse(EntitySelectorParser.PARSER)
					.alwaysThrowException()
					.continueWith(selectorGetter -> Parser.parse(
						reader -> {
							EntitySelector selector = selectorGetter.get();
							if (selector.includesEntities()) {
								throw EntitySelectorArgumentType.ERROR_ONLY_PLAYERS_ALLOWED.create();
							}
							return (Result) source -> {
								List<Player> players = selector.findPlayers(source);
								if (players.isEmpty()) {
									throw EntitySelectorArgumentType.NO_PLAYERS_FOUND.create();
								}

								List<UUID> profiles = new ArrayList<>(players.size());
								for (Player player : players) {
									profiles.add(player.getUniqueId());
								}
								return profiles;
							};
						}
					))
			)
		).then(Parser.readUntilWithoutEscapeCharacter(' ')
			.alwaysThrowException()
			.continueWith(nameGetter -> Parser.parse(
				reader -> {
					String name = nameGetter.get();
					return source -> {
						// TODO: I'm not sure if or how this should check if offline player profiles exist
						Player player = Bukkit.getPlayerExact(name);
						if (player == null) {
							throw ERROR_UNKNOWN_PLAYER.create();
						}
						return Collections.singleton(player.getUniqueId());
					};
				}
			))
		);

	@Override
	public Result parse(StringReader reader) throws CommandSyntaxException {
		return PARSER.parse(reader);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return EntitySelectorParser.PARSER.listSuggestions(context, builder);
	}

	public static Collection<UUID> getProfiles(CommandContext<MockCommandSource> cmdCtx, String key) throws CommandSyntaxException {
		return cmdCtx.getArgument(key, Result.class).getProfiles(cmdCtx.getSource());
	}
}
