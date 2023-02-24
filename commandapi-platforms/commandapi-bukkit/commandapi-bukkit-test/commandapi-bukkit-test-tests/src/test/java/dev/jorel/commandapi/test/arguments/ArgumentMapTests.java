package dev.jorel.commandapi.test.arguments;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.MapArgument;
import dev.jorel.commandapi.arguments.MapArgumentBuilder;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.GreedyArgumentException;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
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

	// TODO: Suggestion test!!!

	@Test
	public void executionTestWithMapArgument() {
		Mut<LinkedHashMap<String, String>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new MapArgumentBuilder<String, String>("map", ':')
				.withKeyMapper(s -> s)
				.withValueMapper(s -> s)
				.withoutKeyList()
				.withoutValueList()
				.build()
			)
			.executesPlayer((player, args) -> {
				results.set((LinkedHashMap<String, String>) args.get("map"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test map:"cool map"
		server.dispatchCommand(player, "test map:\"cool map\"");
		Map<String, String> testMap = new LinkedHashMap<>();
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

		// /test map:"cool map" map:"bar"
		assertCommandFailsWith(player, "test map:\"cool map\" map:\"bar\"", "Could not parse command: Duplicate keys are not allowed at position 19: ...l map\" map<--[HERE]");

		// /test map:
		assertCommandFailsWith(player, "test map:", "Could not parse command: Quotation mark required after writing the delimiter at position 4: map:<--[HERE]");

		// /test map
		assertCommandFailsWith(player, "test map", "Could not parse command: Delimiter required after writing a key at position 3: map<--[HERE]");

		// /test map:"
		assertCommandFailsWith(player, "test map:\"", "Could not parse command: Value required after opening quotation mark at position 5: map:\"<--[HERE]");

		// /test map:"this" otherMap:"this"
		assertCommandFailsWith(player, "test map:\"this\" otherMap:\"this\"", "Could not parse command: Duplicate values are not allowed here at position 26: ...rMap:\"this<--[HERE]");

		assertNoMoreResults(results);
	}

	@Test
	public void executionTestWithMapArgumentAndSpecialValues() {
		Mut<LinkedHashMap<String, String>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new MapArgumentBuilder<String, String>("map", ':')
				.withKeyMapper(s -> s)
				.withValueMapper(s -> s)
				.withoutKeyList()
				.withoutValueList()
				.build()
			)
			.executesPlayer((player, args) -> {
				results.set((LinkedHashMap<String, String>) args.get("map"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test map:"\"hello\""
		server.dispatchCommand(player, "test map:\"\\\"hello\\\"\"");
		Map<String, String> testMap = new LinkedHashMap<>();
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
				.withArguments(new MapArgumentBuilder<String, Integer>("map", '=')
					.withKeyMapper(s -> s)
					.withValueMapper(Integer::valueOf)
					.withoutKeyList()
					.withoutValueList()
					.build()
				)
				.withArguments(new StringArgument("string"))
				.executesPlayer(P_EXEC)
				.register();
		});

		new CommandAPICommand("test")
			.withArguments(new MapArgumentBuilder<String, String>("map", ':')
				.withKeyMapper(s -> s)
				.withValueMapper(s -> s)
				.withoutKeyList()
				.withoutValueList()
				.build()
			)
			.executesPlayer((player, args) -> {
			})
			.register();

		PlayerMock player = server.addPlayer();

		// Test wrong delimiter
		// /test map="test1"
		assertCommandFailsWith(player, "test map=\"test1\"", "Could not parse command: A key must only contain letters from a-z and A-Z, numbers and periods at position 4: map=<--[HERE]");

		// Test no delimiter
		// /test map"test1"
		assertCommandFailsWith(player, "test map\"test1\"", "Could not parse command: You must separate a key/value pair with a ':' at position 4: map\"<--[HERE]");

		// Test without closing quotation mark
		// /test map:"test1
		assertCommandFailsWith(player, "test map:\"test1", "Could not parse command: A value must end with a quotation mark at position 10: map:\"test1<--[HERE]");

		// Test without any quotation marks
		// /test map:5
		assertCommandFailsWith(player, "test map:5", "Could not parse command: A value must start with a quotation mark at position 4: map:<--[HERE]");
	}

	@Test
	public void executionTestWithOtherKeyValuePairs() {
		Mut<LinkedHashMap<String, Integer>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new MapArgumentBuilder<String, Integer>("map")
				.withKeyMapper(s -> s)
				.withValueMapper(Integer::valueOf)
				.withoutKeyList()
				.withoutValueList()
				.build()
			)
			.executesPlayer((player, args) -> {
				results.set((LinkedHashMap<String, Integer>) args.get("map"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test map:"598"
		server.dispatchCommand(player, "test map:\"598\"");
		Map<String, Integer> testMap = new LinkedHashMap<>();
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

	@Test
	public void executionTestWithFloatKey() {
		Mut<LinkedHashMap<Float, String>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new MapArgumentBuilder<Float, String>("map")
				.withKeyMapper(Float::valueOf)
				.withValueMapper(s -> s)
				.withoutKeyList()
				.withoutValueList()
				.build()
			)
			.executesPlayer((player, args) -> {
				results.set((LinkedHashMap<Float, String>) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test 3.5:"Hello World" 12.25:"This is a test!"
		server.dispatchCommand(player, "test 3.5:\"Hello World\" 12.25:\"This is a test!\"");
		LinkedHashMap<Float, String> testMap = new LinkedHashMap<>();
		testMap.put(3.5F, "Hello World");
		testMap.put(12.25F, "This is a test!");
		assertEquals(testMap, results.get());

		// /test 3.5:"Hello World" 12.25:"This is a test!" 6.25:"And this is a third value!"
		server.dispatchCommand(player, "test 3.5:\"Hello World\" 12.25:\"This is a test!\" 6.25:\"And this is a third value!\"");
		testMap.put(6.25F, "And this is a third value!");
		assertEquals(testMap, results.get());

		// /test 3,5:"Hello world!"
		assertCommandFailsWith(player, "test 3,5:\"Hello world!\"", "Could not parse command: A key must only contain letters from a-z and A-Z, numbers and periods at position 2: 3,<--[HERE]");

		assertNoMoreResults(results);
	}

	@Test
	public void executionTestWithIntegerKey() {
		Mut<LinkedHashMap<Integer, String>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new MapArgumentBuilder<Integer, String>("map")
				.withKeyMapper(Integer::valueOf)
				.withValueMapper(s -> s)
				.withoutKeyList()
				.withoutValueList()
				.build()
			)
			.executesPlayer((player, args) -> {
				results.set((LinkedHashMap<Integer, String>) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test 3:"Hello World" 12:"This is a test!"
		server.dispatchCommand(player, "test 3:\"Hello World\" 12:\"This is a test!\"");
		LinkedHashMap<Integer, String> testMap = new LinkedHashMap<>();
		testMap.put(3, "Hello World");
		testMap.put(12, "This is a test!");
		assertEquals(testMap, results.get());

		// /test 3:"Hello World" 12:"This is a test!" 6:"And this is a third value!"
		server.dispatchCommand(player, "test 3:\"Hello World\" 12:\"This is a test!\" 6:\"And this is a third value!\"");
		testMap.put(6, "And this is a third value!");
		assertEquals(testMap, results.get());

		// /test 3.5:"Hello world!"
		assertThrows(NumberFormatException.class, () -> server.dispatchCommand(player, "test 3.5:\"Hello world!\""));

		assertNoMoreResults(results);
	}

	@Test
	public void executionTestWithSuggestionsWithContents() {

		new CommandAPICommand("test")
			.withArguments(new MapArgumentBuilder<String, String>("map")
				.withKeyMapper(s -> s)
				.withValueMapper(s -> s)
				.withKeyList(List.of("optionOne", "optionTwo", "optionThree"))
				.withValueList(List.of("solutionOne", "solutionTwo", "solutionThree"))
				.build()
			)
			.executesPlayer(P_EXEC)
			.register();

		PlayerMock player = server.addPlayer();

		// From previous test we know everything works so here only exceptions will be tested

		// Test invalid key
		// /test optionOne:"solutionTwo" optionFour:"solutionOne"
		assertCommandFailsWith(player, "test optionOne:\"solutionTwo\" optionFour:\"solutionOne\"", "Could not parse command: Invalid key: optionFour at position 35: ...optionFour<--[HERE]");

		// Test invalid value
		// /test optionOne:"solutionOne" optionTwo:"solutionFour"
		assertCommandFailsWith(player, "test optionOne:\"solutionOne\" optionTwo:\"solutionFour\"", "Could not parse command: Invalid value: solutionFour at position 48: ...lutionFour<--[HERE]");
 	}

	@Test
	public void executionTestWithoutSuggestions() {
		Mut<LinkedHashMap<String, String>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new MapArgumentBuilder<String, String>("map")
				.withKeyMapper(s -> s)
				.withValueMapper(s -> s)
				.withoutKeyList()
				.withoutValueList()
				.build()
			)
			.executesPlayer((player, args) -> {
				results.set((LinkedHashMap<String, String>) args.get("map"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// Theoretically with this command, every key/value pair should be possible
		LinkedHashMap<String, String> testMap = new LinkedHashMap<>();
		testMap.put("key1", "value1");
		testMap.put("key2", "value2");

		// /test key1:"value1" key2:"value2"
		server.dispatchCommand(player, "test key1:\"value1\" key2:\"value2\"");
		assertEquals(testMap, results.get());

		assertNoMoreResults(results);
	}

	@Test
	public void suggestionTestWithMapArgumentAndNoValueDuplicates() {
		new CommandAPICommand("test")
			.withArguments(new MapArgumentBuilder<String, String>("map")
				.withKeyMapper(s -> s)
				.withValueMapper(s -> s)
				.withKeyList(List.of("beautiful", "bold", "crazy", "mighty", "wonderful"))
				.withValueList(List.of("chaotic", "majestic", "sunny", "sweet", "weird"))
				.build()
			)
			.executesPlayer(P_EXEC)
			.register();

		PlayerMock player = server.addPlayer();

		// Key tests

		// /test
		assertEquals(List.of("beautiful", "bold", "crazy", "mighty", "wonderful"), server.getSuggestions(player, "test "));

		// /test b
		assertEquals(List.of("beautiful", "bold"), server.getSuggestions(player, "test b"));

		// /test c
		assertEquals(List.of("crazy"), server.getSuggestions(player, "test c"));

		// /test m
		assertEquals(List.of("mighty"), server.getSuggestions(player, "test m"));

		// /test w
		assertEquals(List.of("wonderful"), server.getSuggestions(player, "test w"));

		// Use "beautiful" for all following tests as the first key

		// /test beautiful
		assertEquals(List.of(":"), server.getSuggestions(player, "test beautiful"));

		// /test beautiful:
		assertEquals(List.of("\""), server.getSuggestions(player, "test beautiful:"));

		// /test beautiful:"
		assertEquals(List.of("chaotic", "majestic", "sunny", "sweet", "weird"), server.getSuggestions(player, "test beautiful:\""));

		// /test beautiful:"s
		assertEquals(List.of("sunny", "sweet"), server.getSuggestions(player, "test beautiful:\"s"));

		// /test beautiful:"c
		assertEquals(List.of("chaotic"), server.getSuggestions(player, "test beautiful:\"c"));

		// /test beautiful:"m
		assertEquals(List.of("majestic"), server.getSuggestions(player, "test beautiful:\"m"));

		// /test beautiful:"w
		assertEquals(List.of("weird"), server.getSuggestions(player, "test beautiful:\"w"));

		// Use "weird" for all following tests as the first value

		// /test beautiful:"weird
		assertEquals(List.of("\""), server.getSuggestions(player, "test beautiful:\"weird"));

		// Key-value pair completed, check if value removal works

		// /test beautiful:"weird"
		assertEquals(List.of("bold", "crazy", "mighty", "wonderful"), server.getSuggestions(player, "test beautiful:\"weird\""));

		// /test beautiful:"weird" bold:"
		assertEquals(List.of("chaotic", "majestic", "sunny", "sweet"), server.getSuggestions(player, "test beautiful:\"weird\" bold:\""));
	}

	@Test
	public void suggestionTestWithMapArgumentAndValueDuplicates() {
		new CommandAPICommand("test")
			.withArguments(new MapArgumentBuilder<String, String>("map")
				.withKeyMapper(s -> s)
				.withValueMapper(s -> s)
				.withKeyList(List.of("beautiful", "bold", "crazy", "mighty", "wonderful"))
				.withValueList(List.of("chaotic", "majestic", "sunny", "sweet", "weird"), true)
				.build()
			)
			.executesPlayer(P_EXEC)
			.register();

		PlayerMock player = server.addPlayer();

		// Check if value removal works

		// /test beautiful:"weird"
		assertEquals(List.of("bold", "crazy", "mighty", "wonderful"), server.getSuggestions(player, "test beautiful:\"weird\""));

		// /test beautiful:"weird" bold:"
		assertEquals(List.of("chaotic", "majestic", "sunny", "sweet", "weird"), server.getSuggestions(player, "test beautiful:\"weird\" bold:\""));
	}

}
