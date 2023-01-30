package dev.jorel.commandapi.test.arguments;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.MapArgument;
import dev.jorel.commandapi.arguments.MapArgumentBuilder;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.GreedyArgumentException;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;
import dev.jorel.commandapi.wrappers.MapArgumentKeyType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link MapArgument}
 */
public class ArgumentMapTests extends TestBase {

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
	public void executionTestWithMapArgument() {
		Mut<HashMap<String, String>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new MapArgumentBuilder<String>("map", ':')
				.withKeyType(MapArgumentKeyType.STRING)
				.withValueMapper(s -> s)
				.build()
			)
			.executesPlayer((player, args) -> {
				results.set((HashMap<String, String>) args.get("map"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test map:"cool map"
		server.dispatchCommand(player, "test map:\"cool map\"");
		Map<String, String> testMap = new HashMap<>();
		testMap.put("map", "cool map");
		assertEquals(testMap, results.get());

		// /test map:"cool map" foo:"bar"
		server.dispatchCommand(player, "test map:\"cool map\" foo:\"bar\"");
		testMap.put("foo", "bar");
		assertEquals(testMap, results.get());

		// /test map:"cool map" foo:"bar" test:"Test value"
		server.dispatchCommand(player, "test map:\"cool map\" foo:\"bar\" test:\"Test value\"");
		testMap.put("test", "Test value");
		assertEquals(testMap, results.get());

		assertNoMoreResults(results);
	}

	@Test
	public void executionTestWithMapArgumentAndSpecialValues() {
		Mut<HashMap<String, String>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new MapArgumentBuilder<String>("map", ':')
				.withKeyType(MapArgumentKeyType.STRING)
				.withValueMapper(s -> s)
				.build()
			)
			.executesPlayer((player, args) -> {
				results.set((HashMap<String, String>) args.get("map"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test map:"\"hello\""
		server.dispatchCommand(player, "test map:\"\\\"hello\\\"\"");
		Map<String, String> testMap = new HashMap<>();
		testMap.put("map", "\"hello\"");
		assertEquals(testMap, results.get());

		// /test map:"\\hello\\"
		server.dispatchCommand(player, "test map:\"\\\\hello\\\\\"");
		testMap.clear();
		testMap.put("map", "\\hello\\");
		assertEquals(testMap, results.get());
	}

	@Test
	public void exceptionTestWithMapArgument() {
		// A MapArgument is a GreedyArgument. It is only allowed at the end of the arguments list.
		// A GreedyArgumentException should be thrown when it is not the last argument.
		assertThrows(GreedyArgumentException.class, () -> {
			new CommandAPICommand("test")
				.withArguments(new MapArgumentBuilder<Integer>("map", '=')
					.withKeyType(MapArgumentKeyType.STRING)
					.withValueMapper(Integer::valueOf)
					.build()
				)
				.withArguments(new StringArgument("string"))
				.executesPlayer(P_EXEC)
				.register();
		});

		new CommandAPICommand("test")
			.withArguments(new MapArgumentBuilder<String>("map", ':')
				.withKeyType(MapArgumentKeyType.STRING)
				.withValueMapper(s -> s)
				.build()
			)
			.executesPlayer((player, args) -> {
			})
			.register();

		PlayerMock player = server.addPlayer();

		// Test invalid key
		// /test map1:"test1"
		assertCommandFailsWith(player, "test map1:\"test1\"", "Could not parse command: A key must only contain letters from a-z and A-Z! at position 4: map1<--[HERE]");

		// Test wrong delimiter
		// /test map="test1"
		assertCommandFailsWith(player, "test map=\"test1\"", "Could not parse command: A key must only contain letters from a-z and A-Z! at position 4: map=<--[HERE]");

		// Test no delimiter
		// /test map"test1"
		assertCommandFailsWith(player, "test map\"test1\"", "Could not parse command: You have to separate a key/value pair with a ':'! at position 4: map\"<--[HERE]");

		// Test without closing quotation mark
		// /test map:"test1
		assertCommandFailsWith(player, "test map:\"test1", "Could not parse command: A value has to end with a quotation mark! at position 10: map:\"test1<--[HERE]");

		// Test without any quotation marks
		// /test map:5
		assertCommandFailsWith(player, "test map:5", "Could not parse command: A value has to start with a quotation mark! at position 4: map:<--[HERE]");
	}

	@Test
	public void executionTestWithOtherKeyValuePairs() {
		Mut<HashMap<String, Integer>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new MapArgumentBuilder<Integer>("map")
				.withKeyType(MapArgumentKeyType.STRING)
				.withValueMapper(Integer::valueOf)
				.build()
			)
			.executesPlayer((player, args) -> {
				results.set((HashMap<String, Integer>) args.get("map"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test map:"598"
		server.dispatchCommand(player, "test map:\"598\"");
		Map<String, Integer> testMap = new HashMap<>();
		testMap.put("map", 598);
		assertEquals(testMap, results.get());

		// /test map:"598" age:"18"
		server.dispatchCommand(player, "test map:\"598\" age:\"18\"");
		testMap.put("age", 18);
		assertEquals(testMap, results.get());

		// /test map:"598" age:"18" someThirdValue:"19999"
		server.dispatchCommand(player, "test map:\"598\" age:\"18\" someThirdValue:\"19999\"");
		testMap.put("someThirdValue", 19999);
		assertEquals(testMap, results.get());

		assertNoMoreResults(results);
	}

}
