package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.bukkit.Sound;
import org.bukkit.potion.PotionEffectType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.SoundArgument;
import dev.jorel.commandapi.test.MockPlatform;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;

/**
 * Tests for the {@link SoundArgument}
 */
public class ArgumentSoundTests extends TestBase {

	/*********
	 * Setup *
	 *********/

	@BeforeEach
	public void setUp() {
		super.setUp();
	}

	@AfterEach
	public void tearDown() {
		super.tearDown();
	}

	/*********
	 * Tests *
	 *********/

	@Test
	public void executionTestWithSoundArgument() {
		Mut<Sound> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new SoundArgument("sound"))
			.executesPlayer((player, args) -> {
				results.set((Sound) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test adventure/adventuring_time
//		server.dispatchCommand(player, "test adventure/adventuring_time");
//		assertEquals(NamespacedKey.minecraft("adventure/adventuring_time"), results.get().getKey());
//		
//		// /test minecraft:adventure/adventuring_time
//		server.dispatchCommand(player, "test minecraft:adventure/adventuring_time");
//		assertEquals(NamespacedKey.minecraft("adventure/adventuring_time"), results.get().getKey());
//
//		// /test namespace:group/unknown_Sound
//		assertCommandFailsWith(player, "test namespace:group/unknown_Sound", "Unknown Sound: namespace:group/unknown_Sound");

		assertNoMoreResults(results);
	}
	
	@Test
	public void executionTestWithSoundArgumentAllSounds() {
		Mut<Sound> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new SoundArgument("sound"))
			.executesPlayer((player, args) -> {
				results.set((Sound) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();
		
		for (Sound sound : MockPlatform.getInstance().getSounds()) {
			server.dispatchCommand(player, "test " + sound.getKey());
			assertEquals(sound, results.get());
		}

		assertNoMoreResults(results);
	}

	/********************
	 * Suggestion tests *
	 ********************/

	@Test
	public void suggestionTestWithAxisArgument() {
		new CommandAPICommand("test")
			.withArguments(new SoundArgument("Sound"))
			.executesPlayer((player, args) -> {
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test
		assertEquals(
			Arrays.stream(MockPlatform.getInstance().getSounds())
				.map(s -> s.getKey().toString())
				.sorted()
				.toList(), 
			server.getSuggestions(player, "test "));

		// /test
//		assertEquals(Sounds.stream().map(NamespacedKey::toString).sorted().toList(), server.getSuggestions(player, "test "));
//		
//		// /test adventure/a
//		assertEquals(Sounds.stream().map(NamespacedKey::toString).filter(key -> key.startsWith("minecraft:adventure/a")).sorted().toList(),
//			server.getSuggestions(player, "test adventure/a"));
		
	}

}
