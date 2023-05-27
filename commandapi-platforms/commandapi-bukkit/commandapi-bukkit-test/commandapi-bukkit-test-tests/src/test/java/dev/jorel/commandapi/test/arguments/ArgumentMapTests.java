package dev.jorel.commandapi.test.arguments;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.arguments.StringParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.MapArgument;
import dev.jorel.commandapi.arguments.MapArgumentBuilder;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.GreedyArgumentException;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;
import io.netty.util.internal.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

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

	// All ways of constructing the MapArgument:
	//  Default separators | define delimiter | define separator
	//  Key mapper | map to non-string | map and throw errors
	//  Value mapper | map to non-string | map and throw errors
	//  Without key list | with key list | keys containing delimiter
	//  Without value list | with value list | values containing delimiter
	//  No duplicates | allow duplicate values

	@Test
	public void exceptionTestWithMapArgument() {
		// A MapArgument is a GreedyArgument. It is only allowed at the end of the arguments list.
		// A GreedyArgumentException should be thrown when it is not the last argument.
		CommandAPICommand command =
			new CommandAPICommand("test")
				.withArguments(
					// 'Default' MapArgument
					new MapArgumentBuilder<String, String>("map")
						.withKeyMapper(s -> s)
						.withValueMapper(s -> s)
						.withoutKeyList()
						.withoutValueList()
						.build()
				)
				.executesPlayer(P_EXEC);
		assertDoesNotThrow(command::register);

		command.withArguments(new StringArgument("string"));
		assertThrows(GreedyArgumentException.class, command::register);
	}

	@Test
	public void executionTestWithMapArgument() {
		Mut<Map<String, String>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(
				// 'Default' MapArgument
				new MapArgumentBuilder<String, String>("map")
					.withKeyMapper(s -> s)
					.withValueMapper(s -> s)
					.withoutKeyList()
					.withoutValueList()
					.build()
			)
			.executesPlayer((player, args) -> {
				results.set(args.getUnchecked("map"));
			})
			.register();

		PlayerMock player = server.addPlayer();


		Map<String, String> expectedMap = new LinkedHashMap<>();

		expectedMap.put("key1", "value1");
		// /test key1:value1
		// Input map with 1 unquoted key-value pair
		assertStoresResult(player, "test key1:value1", results, expectedMap);
		// /test "key1":"value1"
		// Input map with 1 quoted key-value pair
		assertStoresResult(player, "test \"key1\":\"value1\"", results, expectedMap);

		expectedMap.put("key2", "value2");
		// /test key1:value1 key2:value2
		// Input map with 2 unquoted key-value pairs
		assertStoresResult(player, "test key1:value1 key2:value2", results, expectedMap);
		// /test "key1":"value1" "key2":"value2"
		// Input map with 2 quoted key-value pairs
		assertStoresResult(player, "test \"key1\":\"value1\" \"key2\":\"value2\"", results, expectedMap);

		expectedMap.put("key3", "value3");
		// /test key1:value1 key2:value2 key3:value3
		// Input map with 3 unquoted key-value pairs
		assertStoresResult(player, "test key1:value1 key2:value2 key3:value3", results, expectedMap);
		// /test "key1":"value1" "key2":"value2" "key3":"value3"
		// Input map with 3 quoted key-value pairs
		assertStoresResult(player, "test \"key1\":\"value1\" \"key2\":\"value2\" \"key3\":\"value3\"", results, expectedMap);


		// /test key1:value1 key1:value2
		// Error for having duplicate unquoted key1
		assertCommandFailsWith(player, "test key1:value1 key1:value2", "Could not parse command: Duplicate keys are not allowed! at position 12: ...y1:value1 <--[HERE]");
		// /test key1:value1 "key1":value2
		// Error for having duplicate quoted key1
		assertCommandFailsWith(player, "test key1:value1 \"key1\":value2", "Could not parse command: Duplicate keys are not allowed! at position 12: ...y1:value1 <--[HERE]");
		// /test key1:value1 key1
		// Error for having duplicate unquoted key1 without delimiter
		assertCommandFailsWith(player, "test key1:value1 key1", "Could not parse command: Duplicate keys are not allowed! at position 12: ...y1:value1 <--[HERE]");

		// /test key1:value1 key2:value1
		// Error for having duplicate unquoted value1
		assertCommandFailsWith(player, "test key1:value1 key2:value1", "Could not parse command: Duplicate values are not allowed! at position 17: ...lue1 key2:<--[HERE]");
		// /test key1:value1 key2:"value1"
		// Error for having duplicate quoted value1
		assertCommandFailsWith(player, "test key1:value1 key2:\"value1\"", "Could not parse command: Duplicate values are not allowed! at position 17: ...lue1 key2:<--[HERE]");


		// /test key1:value1 key2:
		// Error for missing value after unquoted key
		assertCommandFailsWith(player, "test key1:value1 key2:", "Could not parse command: Expected a value after the delimiter at position 17: ...lue1 key2:<--[HERE]");
		// /test key1:value1 "key2":
		// Error for missing value after quoted key
		assertCommandFailsWith(player, "test key1:value1 \"key2\":", "Could not parse command: Expected a value after the delimiter at position 19: ...e1 \"key2\":<--[HERE]");

		// /test key1:value1
		// Error for missing key after unquoted value
		assertCommandFailsWith(player, "test key1:value1 ", "Could not parse command: Expected a key after the separator at position 12: ...y1:value1 <--[HERE]");
		// /test key1:"value1"
		// Error for missing key after quoted value
		assertCommandFailsWith(player, "test key1:\"value1\" ", "Could not parse command: Expected a key after the separator at position 14: ...:\"value1\" <--[HERE]");


		// /test key1:value1 key2
		// Error for missing delimiter after unquoted key
		assertCommandFailsWith(player, "test key1:value1 key2", "Could not parse command: Delimiter \":\" required after writing a key at position 12: ...y1:value1 <--[HERE]");
		// /test key1:value1 "key2"
		// Error for missing delimiter after quoted key
		assertCommandFailsWith(player, "test key1:value1 \"key2\"", "Could not parse command: Delimiter \":\" required after writing a key at position 18: ...ue1 \"key2\"<--[HERE]");


		// /test key1:value1 "key2"=value2
		// Error for incorrect delimiter
		assertCommandFailsWith(player, "test key1:value1 \"key2\"=value2", "Could not parse command: Delimiter \":\" required after writing a key at position 18: ...ue1 \"key2\"<--[HERE]");
		// /test key1:"value1",key2=value2
		// Error for incorrect separator
		assertCommandFailsWith(player, "test key1:\"value1\",key2=value2", "Could not parse command: Separator \" \" required after writing a value at position 13: ...1:\"value1\"<--[HERE]");


		// /test key1:value1 "key2
		// Error for quoted key not closing
		assertCommandFailsWith(player, "test key1:value1 \"key2", "Could not parse command: A quoted key must end with a quotation mark at position 13: ...1:value1 \"<--[HERE]");
		// /test key1:value1 key2:"value2
		// Error for quoted value not closing
		assertCommandFailsWith(player, "test key1:value1 key2:\"value2", "Could not parse command: A quoted value must end with a quotation mark at position 18: ...ue1 key2:\"<--[HERE]");


		assertNoMoreResults(results);
	}

	@Test
	public void executionTestWithSpecialCharacters() {
		// Special characters are \ " and the separator and delimiter
		Mut<Map<String, String>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(
				// 'Default' MapArgument
				new MapArgumentBuilder<String, String>("map")
					.withKeyMapper(s -> s)
					.withValueMapper(s -> s)
					.withoutKeyList()
					.withoutValueList()
					.build()
			)
			.executesPlayer((player, args) -> {
				results.set(args.getUnchecked("map"));
			})
			.register();

		PlayerMock player = server.addPlayer();


		// /test \"HelloWorld":value
		// Input quotes into unquoted key
		assertStoresResult(player, "test \\\"HelloWorld\":value", results, Map.of("\"HelloWorld\"", "value"));
		// /test "\"HelloWorld\"":value
		// Input quotes into quoted key
		assertStoresResult(player, "test \"\\\"HelloWorld\\\"\":value", results, Map.of("\"HelloWorld\"", "value"));

		// /test key:\"HelloWorld"
		// Input quotes into unquoted value
		assertStoresResult(player, "test key:\\\"HelloWorld\"", results, Map.of("key", "\"HelloWorld\""));
		// /test key:"\"HelloWorld\""
		// Input quotes into quoted value
		assertStoresResult(player, "test key:\"\\\"HelloWorld\\\"\"", results, Map.of("key", "\"HelloWorld\""));

		// /test "key1\":value1 "key2":value2
		// Error from escaping instead of closing quoted key
		assertCommandFailsWith(player, "test \"key1\\\":value1 \"key2\":value2", "Could not parse command: Delimiter \":\" required after writing a key at position 16: ...\":value1 \"<--[HERE]");
		// /test key1:"value\" key2:"value2"
		// Error from escaping instead of closing quoted value
		assertCommandFailsWith(player, "test key1:\"value1\\\" key2:\"value2\"", "Could not parse command: Separator \" \" required after writing a value at position 21: ...1\\\" key2:\"<--[HERE]");


		// /test ke\\y:value
		// Input backslash into unquoted key
		assertStoresResult(player, "test ke\\\\y:value", results, Map.of("ke\\y", "value"));
		// /test "ke\\y":value
		// Input backslash into quoted key
		assertStoresResult(player, "test \"ke\\\\y\":value", results, Map.of("ke\\y", "value"));

		// /test key:val\\ue
		// Input backslash into unquoted value
		assertStoresResult(player, "test key:val\\\\ue", results, Map.of("key", "val\\ue"));
		// /test key:"val\\ue"
		// Input backslash into quoted value
		assertStoresResult(player, "test key:val\\\\ue", results, Map.of("key", "val\\ue"));


		// /test ke\:y1:value1 key2:value2
		// Input delimiter into unquoted key
		assertStoresResult(player, "test ke\\:y1:value1 key2:value2", results, Map.of("ke:y1", "value1", "key2", "value2"));
		// /test "ke:y1":value1 key2:value2
		// Input delimiter into quoted key
		assertStoresResult(player, "test \"ke:y1\":value1 key2:value2", results, Map.of("ke:y1", "value1", "key2", "value2"));

		// /test key1:val\ ue1 key2:value2
		// Input separator into unquoted value
		assertStoresResult(player, "test key1:val\\ ue1 key2:value2", results, Map.of("key1", "val ue1", "key2", "value2"));
		// /test key1:"val ue1" key2:value2
		// Input separator into quoted value
		assertStoresResult(player, "test key1:\"val ue1\" key2:value2", results, Map.of("key1", "val ue1", "key2", "value2"));


		assertNoMoreResults(results);
	}

	@Test
	public void executionTestWithNonSpecialCharacters() {
		// I believe the characters that may appear in chat messages are defined by the
		//  net.minecraft.server.SharedConstants#isAllowedChatCharacter method
		// We'll only bother testing ASCII characters that fit that method
		// https://bukkit.org/threads/printing-special-characters-%E2%99%A0-%E2%99%A3-%E2%99%A5-%E2%99%A6-in-chat.72293/
		//  - Thanks for helping me find that
		Mut<Map<String, String>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(
				// 'Default' MapArgument
				new MapArgumentBuilder<String, String>("map")
					.withKeyMapper(s -> s)
					.withValueMapper(s -> s)
					.withoutKeyList()
					.withoutValueList()
					.build()
			)
			.executesPlayer((player, args) -> {
				results.set(args.getUnchecked("map"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		for (char c = 32; c < 127; c++) {
			if(c == '\"' || c == '\\' || c == ':'|| c == ' ') continue; // Skip special characters

			// Use c as the key and value, unquoted
			assertStoresResult(player, "test " + c + ":" + c, results, Map.of(String.valueOf(c), String.valueOf(c)));

			// Use c as the key and value, quoted
			assertStoresResult(player, "test \"" + c + "\":\"" + c + "\"", results, Map.of(String.valueOf(c), String.valueOf(c)));
		}


		assertNoMoreResults(results);
	}

	@RepeatedTest(10)
	public void executionTestWithPlayerNameKeys() {
		Mut<Map<String, String>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(
				// 'Default' MapArgument
				new MapArgumentBuilder<String, String>("map")
					.withKeyMapper(s -> s)
					.withValueMapper(s -> s)
					.withoutKeyList()
					.withoutValueList()
					.build()
			)
			.executesPlayer((player, args) -> {
				results.set(args.getUnchecked("map"));
			})
			.register();

		PlayerMock player = server.addPlayer();
		
		// A username is 3 - 16 characters long, has no spaces and only consist
		// of a-z, A-Z, 0-9 and underscore (_)
		
		String possibleChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_";
		StringBuilder playerNameBuilder = new StringBuilder();
		for(int i = 0; i < ThreadLocalRandom.current().nextInt(3, 17); i++) {
			playerNameBuilder.append(possibleChars.charAt(ThreadLocalRandom.current().nextInt(0, possibleChars.length())));
		}
		final String playerName = playerNameBuilder.toString();

		// /test <random player name, derived above>:value
		assertStoresResult(player, "test " + playerName + ":\"value\"", results, Map.of(playerName, "value"));
		// /test "<random player name, derived above>":"value"
		assertStoresResult(player, "test \"" + playerName + "\":\"value\"", results, Map.of(playerName, "value"));


		assertNoMoreResults(results);
	}

	@Test
	public void executionTestWithLongTerminators() {
		Mut<Map<String, String>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(
				// Taking advantage of String terminators
				new MapArgumentBuilder<String, String>("map", "-->", ",  ")
					.withKeyMapper(s -> s)
					.withValueMapper(s -> s)
					.withoutKeyList()
					.withoutValueList()
					.build()
			)
			.executesPlayer((player, args) -> {
				results.set(args.getUnchecked("map"));
			})
			.register();

		PlayerMock player = server.addPlayer();


		Map<String, String> expectedMap = new LinkedHashMap<>();

		expectedMap.put("key1", "value1");
		// /test key1-->value1
		// Input map with 1 unquoted key-value pair
		assertStoresResult(player, "test key1-->value1", results, expectedMap);
		// /test "key1"-->"value1"
		// Input map with 1 quoted key-value pair
		assertStoresResult(player, "test \"key1\"-->\"value1\"", results, expectedMap);

		expectedMap.put("key2", "value2");
		// /test key1-->value1,  key2-->value2
		// Input map with 2 unquoted key-value pairs
		assertStoresResult(player, "test key1-->value1,  key2-->value2", results, expectedMap);
		// /test "key1"-->"value1",  "key2"-->"value2"
		// Input map with 2 quoted key-value pairs
		assertStoresResult(player, "test \"key1\"-->\"value1\",  \"key2\"-->\"value2\"", results, expectedMap);

		expectedMap.put("key3", "value3");
		// /test key1-->value1,  key2-->value2,  key3-->value3
		// Input map with 3 unquoted key-value pairs
		assertStoresResult(player, "test key1-->value1,  key2-->value2,  key3-->value3", results, expectedMap);
		// /test "key1"-->"value1",  "key2"-->"value2",  "key3"-->"value3"
		// Input map with 3 quoted key-value pairs
		assertStoresResult(player, "test \"key1\"-->\"value1\",  \"key2\"-->\"value2\",  \"key3\"-->\"value3\"", results, expectedMap);


		// /test key1-->value1,  key2
		// Error for missing delimiter after unquoted key
		assertCommandFailsWith(player, "test key1-->value1,  key2", "Could not parse command: Delimiter \"-->\" required after writing a key at position 16: ...>value1,  <--[HERE]");
		// /test key1-->value1,  "key2"
		// Error for missing delimiter after quoted key
		assertCommandFailsWith(player, "test key1-->value1,  \"key2\"", "Could not parse command: Delimiter \"-->\" required after writing a key at position 22: ...1,  \"key2\"<--[HERE]");


		// /test key1-->value1,  "key2"=value2
		// Error for incorrect delimiter
		assertCommandFailsWith(player, "test key1-->value1,  \"key2\"=value2", "Could not parse command: Delimiter \"-->\" required after writing a key at position 22: ...1,  \"key2\"<--[HERE]");
		// /test key1-->"value1",key2-->value2
		// Error for incorrect separator
		assertCommandFailsWith(player, "test key1-->\"value1\",key2-->value2", "Could not parse command: Separator \",  \" required after writing a value at position 15: ...->\"value1\"<--[HERE]");


		// /test a-b-c-->a, b, and c,  key2-->value2
		// Input key/values that initially look like terminators
		assertStoresResult(player, "test a-b-c-->a, b, and c,  key2-->value2", results, Map.of("a-b-c", "a, b, and c", "key2", "value2"));


		// /test key1-->value1,  key2--
		// Error for incomplete delimiter after unquoted key
		assertCommandFailsWith(player, "test key1-->value1,  key2--", "Could not parse command: Delimiter \"-->\" required after writing a key at position 16: ...>value1,  <--[HERE]");
		// /test key1-->value1,  "key2"--
		// Error for incomplete delimiter after quoted key
		assertCommandFailsWith(player, "test key1-->value1,  \"key2\"--", "Could not parse command: Delimiter \"-->\" required after writing a key at position 22: ...1,  \"key2\"<--[HERE]");

		// /test key1-->value",
		// Input incomplete delimiter after unquoted value
		// Since we're not using a value list, "value1," is a valid value
		assertStoresResult(player, "test key1-->value1,", results, Map.of("key1", "value1,"));
		// /test key1-->"value1",
		// Error for incomplete delimiter after quoted value
		assertCommandFailsWith(player, "test key1-->\"value1\",", "Could not parse command: Separator \",  \" required after writing a value at position 15: ...->\"value1\"<--[HERE]");


		assertNoMoreResults(results);
	}

	@Test
	public void executionTestWithCustomMappers() {
		Mut<Map<Integer, Integer>> results = Mut.of();

		StringParser<Integer> customMapper = (s) -> {
			int i = Integer.parseInt(s);
			if(i < 0 || i > 255) throw CommandAPI.failWithString("Must be between 0 and 255");
			return i;
		};

		new CommandAPICommand("test")
			.withArguments(
				new MapArgumentBuilder<Integer, Integer>("map")
					// Using a custom Integer key and value mapper
					.withKeyMapper(customMapper)
					.withValueMapper(customMapper)
					.withoutKeyList()
					.withoutValueList()
					.build()
			)
			.executesPlayer((player, args) -> {
				results.set(args.getUnchecked("map"));
			})
			.register();

		PlayerMock player = server.addPlayer();


		Map<Integer, Integer> expectedMap = new LinkedHashMap<>();

		expectedMap.put(0, 10);
		// /test 0:10
		// Input map with 1 unquoted key-value pair
		assertStoresResult(player, "test 0:10", results, expectedMap);
		// /test "0":"10"
		// Input map with 1 quoted key-value pair
		assertStoresResult(player, "test \"0\":\"10\"", results, expectedMap);

		expectedMap.put(20, 100);
		// /test 0:10 20:100
		// Input map with 2 unquoted key-value pairs
		assertStoresResult(player, "test 0:10 20:100", results, expectedMap);
		// /test "0":"10" "20":"100"
		// Input map with 2 quoted key-value pairs
		assertStoresResult(player, "test \"0\":\"10\" \"20\":\"100\"", results, expectedMap);


		// /test key:100
		// Error from unquoted key string not converting to int
		assertCommandFailsWith(player, "test key:100", "Could not parse command: Invalid key (key): cannot be converted to a key at position 0: <--[HERE]");
		// /test "key":100
		// Error from quoted key string not converting to int
		assertCommandFailsWith(player, "test \"key\":100", "Could not parse command: Invalid key (key): cannot be converted to a key at position 0: <--[HERE]");

		// /test 100:value
		// Error from unquoted value string not converting to int
		assertCommandFailsWith(player, "test 100:value", "Could not parse command: Invalid value (value): cannot be converted to a value at position 4: 100:<--[HERE]");
		// /test 100:"value"
		// Error from quoted key value not converting to int
		assertCommandFailsWith(player, "test 100:\"value\"", "Could not parse command: Invalid value (value): cannot be converted to a value at position 4: 100:<--[HERE]");


		// /test 1000:0
		// Error from unquoted key being too large
		assertCommandFailsWith(player, "test 1000:0", "Could not parse command: Must be between 0 and 255 at position 0: <--[HERE]");
		// /test -10:0
		// Error from unquoted key being too small
		assertCommandFailsWith(player, "test -10:0", "Could not parse command: Must be between 0 and 255 at position 0: <--[HERE]");
		// /test "1000":0
		// Error from quoted key being too large
		assertCommandFailsWith(player, "test \"1000\":0", "Could not parse command: Must be between 0 and 255 at position 0: <--[HERE]");
		// /test "-10":0
		// Error from quoted key being too small
		assertCommandFailsWith(player, "test \"-10\":0", "Could not parse command: Must be between 0 and 255 at position 0: <--[HERE]");

		// /test 0:1000
		// Error from unquoted value being too large
		assertCommandFailsWith(player, "test 0:1000", "Could not parse command: Must be between 0 and 255 at position 2: 0:<--[HERE]");
		// /test 0:-10
		// Error from unquoted value being too small
		assertCommandFailsWith(player, "test 0:-10", "Could not parse command: Must be between 0 and 255 at position 2: 0:<--[HERE]");
		// /test 0:"1000"
		// Error from quoted value being too large
		assertCommandFailsWith(player, "test 0:\"1000\"", "Could not parse command: Must be between 0 and 255 at position 2: 0:<--[HERE]");
		// /test 0:"-10"
		// Error from quoted value being too small
		assertCommandFailsWith(player, "test 0:\"-10\"", "Could not parse command: Must be between 0 and 255 at position 2: 0:<--[HERE]");
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
				results.set(args.getUnchecked(0));
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
		assertCommandFailsWith(player, "test 3,5:\"Hello world!\"", "Could not parse command: Invalid key (3,5): cannot be converted to a key at position 0: <--[HERE]");


		assertNoMoreResults(results);
	}

	@Test
	public void executionTestWithLists() {
		new CommandAPICommand("test")
			.withArguments(
				new MapArgumentBuilder<String, String>("map")
					.withKeyMapper(s -> s)
					.withValueMapper(s -> s)
					// Give key and value list
					.withKeyList(List.of("key1", "key2", "key3"))
					.withValueList(List.of("value1", "value2", "value3"))
					.build()
			)
			.executesPlayer(P_EXEC)
			.register();

		PlayerMock player = server.addPlayer();

		// From previous test we know everything works so here only relevant exceptions will be tested

		// /test key1:value1 key1:value2
		// Error for having duplicate unquoted key1
		assertCommandFailsWith(player, "test key1:value1 key1:value2", "Could not parse command: Duplicate keys are not allowed! at position 12: ...y1:value1 <--[HERE]");
		// /test key1:value1 "key1":value2
		// Error for having duplicate quoted key1
		assertCommandFailsWith(player, "test key1:value1 \"key1\":value2", "Could not parse command: Duplicate keys are not allowed! at position 12: ...y1:value1 <--[HERE]");
		// /test key1:value1 key1
		// Error for having duplicate unquoted key1 without delimiter
		assertCommandFailsWith(player, "test key1:value1 key1", "Could not parse command: Duplicate keys are not allowed! at position 12: ...y1:value1 <--[HERE]");

		// /test key1:value1 key2:value1
		// Error for having duplicate value1
		assertCommandFailsWith(player, "test key1:value1 key2:value1", "Could not parse command: Duplicate values are not allowed! at position 17: ...lue1 key2:<--[HERE]");
		// /test key1:value1 key2:"value1"
		// Error for having duplicate quoted value1
		assertCommandFailsWith(player, "test key1:value1 key2:\"value1\"", "Could not parse command: Duplicate values are not allowed! at position 17: ...lue1 key2:<--[HERE]");


		// /test key1:value1 key4:value2
		// Error for having invalid unquoted key4
		assertCommandFailsWith(player, "test key1:value1 key4:value2", "Could not parse command: Invalid key: key4 at position 12: ...y1:value1 <--[HERE]");
		// /test key1:value1 "key4":value2
		// Error for having invalid quoted key1
		assertCommandFailsWith(player, "test key1:value1 \"key4\":value2", "Could not parse command: Invalid key: key4 at position 12: ...y1:value1 <--[HERE]");
		// /test key1:value1 key4
		// Error for having invalid unquoted key4 without delimiter
		assertCommandFailsWith(player, "test key1:value1 key4", "Could not parse command: Invalid key: key4 at position 12: ...y1:value1 <--[HERE]");

		// /test key1:value1 key2:value4
		// Error for having invalid value4
		assertCommandFailsWith(player, "test key1:value1 key2:value4", "Could not parse command: Invalid value: value4 at position 17: ...lue1 key2:<--[HERE]");
		// /test key1:value1 key2:"value4"
		// Error for having invalid quoted value4
		assertCommandFailsWith(player, "test key1:value1 key2:\"value4\"", "Could not parse command: Invalid value: value4 at position 17: ...lue1 key2:<--[HERE]");


		// /test key1:value1 key2
		// Check this still gives missing delimiter message in special case b/c key is valid
		assertCommandFailsWith(player, "test key1:value1 key2", "Could not parse command: Delimiter \":\" required after writing a key at position 12: ...y1:value1 <--[HERE]");
	}

	@Test
	public void executionTestWithDuplicateValues() {
		Mut<Map<String, String>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(
				new MapArgumentBuilder<String, String>("map")
					.withKeyMapper(s -> s)
					.withValueMapper(s -> s)
					.withoutKeyList()
					// Allowing duplicate values
					.withoutValueList(true)
					.build()
			)
			.executesPlayer((player, args) -> {
				results.set(args.getUnchecked("map"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// Allowing duplicate values doesn't do much else beyond that, so there is not much extra to test

		// /test key1:value key2:value
		// Input a duplicate value
		assertStoresResult(player, "test key1:value key2:value", results, Map.of("key1", "value", "key2", "value"));


		assertNoMoreResults(results);
	}

	@Test
	public void executionTestWithDuplicateValuesAndList() {
		Mut<Map<String, String>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(
				new MapArgumentBuilder<String, String>("map")
					.withKeyMapper(s -> s)
					.withValueMapper(s -> s)
					.withoutKeyList()
					// Giving list and allowing duplicate values
					.withValueList(List.of("value1", "value2", "value3"), true)
					.build()
			)
			.executesPlayer((player, args) -> {
				results.set(args.getUnchecked("map"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// Allowing duplicate values doesn't do much else beyond that, so there is not much extra to test

		// /test key1:value1 key2:value1
		// Input a duplicate value
		assertStoresResult(player, "test key1:value1 key2:value1", results, Map.of("key1", "value1", "key2", "value1"));


		// /test key1:value1 key2:value4
		// Error for having invalid value4
		assertCommandFailsWith(player, "test key1:value1 key2:value4", "Could not parse command: Invalid value: value4 at position 17: ...lue1 key2:<--[HERE]");


		assertNoMoreResults(results);
	}

	/********************
	 * Suggestion tests *
	 ********************/
	// TODO: Update suggestions tests to include new MapArgument features
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
		assertEquals(List.of("beautiful:"), server.getSuggestions(player, "test beautiful"));

		// /test beautiful:
		assertEquals(List.of("chaotic", "majestic", "sunny", "sweet", "weird"), server.getSuggestions(player, "test beautiful:"));

		// /test beautiful:"
		assertEquals(List.of("\"chaotic\"", "\"majestic\"", "\"sunny\"", "\"sweet\"", "\"weird\""), server.getSuggestions(player, "test beautiful:\""));

		// /test beautiful:"s
		assertEquals(List.of("\"sunny\"", "\"sweet\""), server.getSuggestions(player, "test beautiful:\"s"));

		// /test beautiful:"c
		assertEquals(List.of("\"chaotic\""), server.getSuggestions(player, "test beautiful:\"c"));

		// /test beautiful:"m
		assertEquals(List.of("\"majestic\""), server.getSuggestions(player, "test beautiful:\"m"));

		// /test beautiful:"w
		assertEquals(List.of("\"weird\""), server.getSuggestions(player, "test beautiful:\"w"));

		// Use "weird" for all following tests as the first value

		// /test beautiful:"weird
		assertEquals(List.of("\"weird\" "), server.getSuggestions(player, "test beautiful:\"weird"));

		// Key-value pair completed, check if value removal works

		// /test beautiful:"weird"
		assertEquals(List.of("bold", "crazy", "mighty", "wonderful"), server.getSuggestions(player, "test beautiful:\"weird\" "));

		// /test beautiful:"weird" bold:"
		assertEquals(List.of("\"chaotic\"", "\"majestic\"", "\"sunny\"", "\"sweet\""), server.getSuggestions(player, "test beautiful:\"weird\" bold:\""));
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
		assertEquals(List.of("bold", "crazy", "mighty", "wonderful"), server.getSuggestions(player, "test beautiful:\"weird\" "));

		// /test beautiful:"weird" bold:"
		assertEquals(List.of("\"chaotic\"", "\"majestic\"", "\"sunny\"", "\"sweet\"", "\"weird\""), server.getSuggestions(player, "test beautiful:\"weird\" bold:\""));
	}

}
