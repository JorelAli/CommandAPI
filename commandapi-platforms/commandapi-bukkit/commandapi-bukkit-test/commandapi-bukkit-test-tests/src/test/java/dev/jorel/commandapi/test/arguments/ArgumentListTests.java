package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import dev.jorel.commandapi.arguments.ListArgumentCommon;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.arguments.parseexceptions.ArgumentParseExceptionContext;
import dev.jorel.commandapi.test.arguments.parseexceptions.ArgumentParseExceptionContextVerifier;
import dev.jorel.commandapi.test.arguments.parseexceptions.InitialParseExceptionTextArgumentVerifier;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ListArgument;
import dev.jorel.commandapi.arguments.ListArgumentBuilder;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;

/**
 * Tests for the {@link ListArgument}
 */
@SuppressWarnings("unchecked")
class ArgumentListTests extends TestBase {

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
	void executionTestWithListArgument() {
		Mut<List<String>> results = Mut.of();

		new CommandAPICommand("list")
			.withArguments(new ListArgumentBuilder<>("values", ", ")
				.withList(() -> List.of("cat", "wolf", "axolotl"))
				.withStringMapper()
				.buildGreedy())
			.executesPlayer((player, args) -> {
				results.set((List<String>) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer("APlayer");
		
		// /list cat, wolf, axolotl
		server.dispatchCommand(player, "list cat, wolf, axolotl");
		assertEquals(List.of("cat", "wolf", "axolotl"), results.get());
		
		// /list cat, wolf, axolotl, wolf
		// No duplicates allowed
		assertCommandFailsWith(player, "list cat, wolf, axolotl, wolf", "Duplicate arguments are not allowed at position 20: ... axolotl, <--[HERE]");
		
		// /list axolotl, wolf, chicken, cat
		// Can't have unspecified "chicken" in the list
		assertCommandFailsWith(player, "list axolotl, wolf, chicken, cat", "Item is not allowed in list at position 15: ...tl, wolf, <--[HERE]");
		
		assertNoMoreResults(results);
	}
	
	@Test
	void executionTestWithListArgumentWithDuplicates() {
		Mut<List<String>> results = Mut.of();

		new CommandAPICommand("list")
			.withArguments(new ListArgumentBuilder<>("values", ", ")
				.allowDuplicates(true)
				.withList(() -> List.of("cat", "wolf", "axolotl"))
				.withStringMapper()
				.buildGreedy())
			.executesPlayer((player, args) -> {
				results.set((List<String>) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer("APlayer");
		
		// /list cat, wolf, axolotl
		server.dispatchCommand(player, "list cat, wolf, axolotl");
		assertEquals(List.of("cat", "wolf", "axolotl"), results.get());
		
		// /list cat, wolf, axolotl, cat, wolf
		server.dispatchCommand(player, "list cat, wolf, axolotl, cat, wolf");
		assertEquals(List.of("cat", "wolf", "axolotl", "cat", "wolf"), results.get());
		
		// /list cat, wolf, axolotl, chicken, cat
		// Can't have unspecified "chicken" in the list
		assertCommandFailsWith(player, "list cat, wolf, axolotl, chicken, cat", "Item is not allowed in list at position 20: ... axolotl, <--[HERE]");
		
		assertNoMoreResults(results);
	}
	
	@Test
	void executionTestWithListArgumentWithConstantList() {
		Mut<List<String>> results = Mut.of();

		new CommandAPICommand("list")
			.withArguments(new ListArgumentBuilder<>("values", ", ")
				.withList(List.of("cat", "wolf", "axolotl"))
				.withStringMapper()
				.buildGreedy())
			.executesPlayer((player, args) -> {
				results.set((List<String>) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer("APlayer");
		
		// /list cat, wolf, axolotl
		server.dispatchCommand(player, "list cat, wolf, axolotl");
		assertEquals(List.of("cat", "wolf", "axolotl"), results.get());
		
		// /list cat, wolf, axolotl, wolf
		// No duplicates allowed
		assertCommandFailsWith(player, "list cat, wolf, axolotl, wolf", "Duplicate arguments are not allowed at position 20: ... axolotl, <--[HERE]");
		
		// /list axolotl, wolf, chicken, cat
		// Can't have unspecified "chicken" in the list
		assertCommandFailsWith(player, "list axolotl, wolf, chicken, cat", "Item is not allowed in list at position 15: ...tl, wolf, <--[HERE]");
		
		assertNoMoreResults(results);
	}
	
	@Test
	void executionTestWithListArgumentWithConstantListArray() {
		Mut<List<String>> results = Mut.of();

		new CommandAPICommand("list")
			.withArguments(new ListArgumentBuilder<>("values", ", ")
				.withList("cat", "wolf", "axolotl")
				.withStringMapper()
				.buildGreedy())
			.executesPlayer((player, args) -> {
				results.set((List<String>) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer("APlayer");
		
		// /list cat, wolf, axolotl
		server.dispatchCommand(player, "list cat, wolf, axolotl");
		assertEquals(List.of("cat", "wolf", "axolotl"), results.get());
		
		// /list cat, wolf, axolotl, wolf
		// No duplicates allowed
		assertCommandFailsWith(player, "list cat, wolf, axolotl, wolf", "Duplicate arguments are not allowed at position 20: ... axolotl, <--[HERE]");
		
		// /list axolotl, wolf, chicken, cat
		// Can't have unspecified "chicken" in the list
		assertCommandFailsWith(player, "list axolotl, wolf, chicken, cat", "Item is not allowed in list at position 15: ...tl, wolf, <--[HERE]");
		
		assertNoMoreResults(results);
	}
	
	@Test
	void executionTestWithListArgumentWithFunctionList() {
		Mut<List<String>> results = Mut.of();

		new CommandAPICommand("list")
			.withArguments(new ListArgumentBuilder<>("values", ", ")
				.withList(info -> List.of("cat", "wolf", "axolotl", info.sender().getName()))
				.withStringMapper()
				.buildGreedy())
			.executesPlayer((player, args) -> {
				results.set((List<String>) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer("APlayer");
		
		// /list cat, wolf, axolotl
		server.dispatchCommand(player, "list cat, wolf, axolotl");
		assertEquals(List.of("cat", "wolf", "axolotl"), results.get());
		
		// /list axolotl, wolf, APlayer
		server.dispatchCommand(player, "list axolotl, wolf, APlayer");
		assertEquals(List.of("axolotl", "wolf", "APlayer"), results.get());

		// /list cat, wolf, axolotl, wolf
		// No duplicates allowed
		assertCommandFailsWith(player, "list cat, wolf, axolotl, wolf", "Duplicate arguments are not allowed at position 20: ... axolotl, <--[HERE]");
		
		// /list axolotl, wolf, chicken, cat
		// Can't have unspecified "chicken" in the list
		assertCommandFailsWith(player, "list axolotl, wolf, chicken, cat", "Item is not allowed in list at position 15: ...tl, wolf, <--[HERE]");
		
		assertNoMoreResults(results);
	}

	/***************************************
	 * Test ListArgument with .buildText() *
	 ***************************************/

	@Test
	void executionTestWithListTextArgument() {
		Mut<List<String>> results = Mut.of();

		new CommandAPICommand("list")
			.withArguments(new ListArgumentBuilder<>("values", ", ")
				.withList(() -> List.of("cat", "wolf", "axolotl"))
				.withStringMapper()
				.buildText())
			.executesPlayer((player, args) -> {
				results.set((List<String>) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer("APlayer");
		
		// /list "cat, wolf, axolotl"
		server.dispatchCommand(player, "list \"cat, wolf, axolotl\"");
		assertEquals(List.of("cat", "wolf", "axolotl"), results.get());
		
		// /list "cat, wolf, axolotl, wolf"
		// No duplicates allowed
		assertCommandFailsWith(player, "list \"cat, wolf, axolotl, wolf\"", "Duplicate arguments are not allowed at position 20: ... axolotl, <--[HERE]");
		
		// /list "axolotl, wolf, chicken, cat"
		// Can't have unspecified "chicken" in the list
		assertCommandFailsWith(player, "list \"axolotl, wolf, chicken, cat\"", "Item is not allowed in list at position 15: ...tl, wolf, <--[HERE]");
		
		assertNoMoreResults(results);
	}
	
	@Test
	void executionTestWithListTextArgumentWithDuplicates() {
		Mut<List<String>> results = Mut.of();

		new CommandAPICommand("list")
			.withArguments(new ListArgumentBuilder<>("values", ", ")
				.allowDuplicates(true)
				.withList(() -> List.of("cat", "wolf", "axolotl"))
				.withStringMapper()
				.buildText())
			.executesPlayer((player, args) -> {
				results.set((List<String>) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer("APlayer");
		
		// /list "cat, wolf, axolotl"
		server.dispatchCommand(player, "list \"cat, wolf, axolotl\"");
		assertEquals(List.of("cat", "wolf", "axolotl"), results.get());
		
		// /list "cat, wolf, axolotl, cat, wolf"
		server.dispatchCommand(player, "list \"cat, wolf, axolotl, cat, wolf\"");
		assertEquals(List.of("cat", "wolf", "axolotl", "cat", "wolf"), results.get());
		
		// /list "cat, wolf, axolotl, chicken, cat"
		// Can't have unspecified "chicken" in the list
		assertCommandFailsWith(player, "list \"cat, wolf, axolotl, chicken, cat\"", "Item is not allowed in list at position 20: ... axolotl, <--[HERE]");
		
		assertNoMoreResults(results);
	}
	
	@Test
	void executionTestWithListTextArgumentWithConstantList() {
		Mut<List<String>> results = Mut.of();

		new CommandAPICommand("list")
			.withArguments(new ListArgumentBuilder<>("values", ", ")
				.withList(List.of("cat", "wolf", "axolotl"))
				.withStringMapper()
				.buildText())
			.executesPlayer((player, args) -> {
				results.set((List<String>) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer("APlayer");
		
		// /list "cat, wolf, axolotl"
		server.dispatchCommand(player, "list \"cat, wolf, axolotl\"");
		assertEquals(List.of("cat", "wolf", "axolotl"), results.get());
		
		// /list "cat, wolf, axolotl, wolf"
		// No duplicates allowed
		assertCommandFailsWith(player, "list \"cat, wolf, axolotl, wolf\"", "Duplicate arguments are not allowed at position 20: ... axolotl, <--[HERE]");
		
		// /list "axolotl, wolf, chicken, cat"
		// Can't have unspecified "chicken" in the list
		assertCommandFailsWith(player, "list \"axolotl, wolf, chicken, cat\"", "Item is not allowed in list at position 15: ...tl, wolf, <--[HERE]");
		
		assertNoMoreResults(results);
	}
	
	@Test
	void executionTestWithListTextArgumentWithFunctionList() {
		Mut<List<String>> results = Mut.of();

		new CommandAPICommand("list")
			.withArguments(new ListArgumentBuilder<>("values", ", ")
				.withList(info -> List.of("cat", "wolf", "axolotl", info.sender().getName()))
				.withStringMapper()
				.buildText())
			.executesPlayer((player, args) -> {
				results.set((List<String>) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer("APlayer");
		
		// /list "cat, wolf, axolotl"
		server.dispatchCommand(player, "list \"cat, wolf, axolotl\"");
		assertEquals(List.of("cat", "wolf", "axolotl"), results.get());
		
		// /list "axolotl, wolf, APlayer"
		server.dispatchCommand(player, "list \"axolotl, wolf, APlayer\"");
		assertEquals(List.of("axolotl", "wolf", "APlayer"), results.get());

		// /list "cat, wolf, axolotl, wolf"
		// No duplicates allowed
		assertCommandFailsWith(player, "list \"cat, wolf, axolotl, wolf\"", "Duplicate arguments are not allowed at position 20: ... axolotl, <--[HERE]");
		
		// /list "axolotl, wolf, chicken, cat"
		// Can't have unspecified "chicken" in the list
		assertCommandFailsWith(player, "list \"axolotl, wolf, chicken, cat\"", "Item is not allowed in list at position 15: ...tl, wolf, <--[HERE]");
		
		assertNoMoreResults(results);
	}

	@Test
	void executionTestWithListTextArgumentAfterListTextArgument() {
		Mut<List<String>> results = Mut.of();

		new CommandAPICommand("list")
			.withArguments(new ListArgumentBuilder<>("values", ", ")
				.withList(player -> List.of("cat", "wolf", "axolotl"))
				.withStringMapper()
				.buildText())
			.withArguments(new ListArgumentBuilder<>("morevalues", ", ")
				.withList(player -> List.of("pumpkin", "melon", "cake"))
				.withStringMapper()
				.buildText())
			.executesPlayer((player, args) -> {
				results.set((List<String>) args.get(0));
				results.set((List<String>) args.get(1));
			})
			.register();

		PlayerMock player = server.addPlayer("APlayer");
		
		// /list "cat, wolf, axolotl" "pumpkin melon"
		server.dispatchCommand(player, "list \"cat, wolf, axolotl\" \"pumpkin, melon\"");
		assertEquals(List.of("cat", "wolf", "axolotl"), results.get());
		assertEquals(List.of("pumpkin", "melon"), results.get());
		
		assertNoMoreResults(results);
	}

	/****************************
	 * Other ListArgument tests *
	 ****************************/

	@Test
	void executionTestWithListArgumentWithNonStringMapper() {
		Mut<List<Material>> results = Mut.of();

		new CommandAPICommand("list")
			.withArguments(new ListArgumentBuilder<Material>("values", ", ")
				.withList(() -> List.of(Material.DIRT, Material.GRASS_BLOCK, Material.DIAMOND))
				.withMapper(material -> material.getKey().getKey())
				.buildGreedy())
			.executesPlayer((player, args) -> {
				results.set((List<Material>) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer("APlayer");
		
		// /list dirt, grass_block
		server.dispatchCommand(player, "list dirt, grass_block");
		assertEquals(List.of(Material.DIRT, Material.GRASS_BLOCK), results.get());
		
		// /list dirt, dirt
		// No duplicates allowed
		assertCommandFailsWith(player, "list dirt, dirt", "Duplicate arguments are not allowed at position 6: dirt, <--[HERE]");
		
		// /list iron_ingot
		// Can't have unspecified "iron_ingot" in the list
		assertCommandFailsWith(player, "list iron_ingot", "Item is not allowed in list at position 0: <--[HERE]");
		
		assertNoMoreResults(results);
	}
	
	@Test
	void executionTestWithListArgumentWithDefaultDelimiter() {
		Mut<List<String>> results = Mut.of();

		new CommandAPICommand("list")
			.withArguments(new ListArgumentBuilder<>("values")
				.withList(() -> List.of("cat", "wolf", "axolotl"))
				.withStringMapper()
				.buildGreedy())
			.executesPlayer((player, args) -> {
				results.set((List<String>) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer("APlayer");
		
		// /list cat wolf axolotl
		server.dispatchCommand(player, "list cat wolf axolotl");
		assertEquals(List.of("cat", "wolf", "axolotl"), results.get());
		
		// /list cat wolf axolotl wolf
		// No duplicates allowed
		assertCommandFailsWith(player, "list cat wolf axolotl wolf", "Duplicate arguments are not allowed at position 17: ...f axolotl <--[HERE]");
		
		// /list axolotl wolf chicken, cat
		// Can't have unspecified "chicken" in the list
		assertCommandFailsWith(player, "list axolotl wolf chicken cat", "Item is not allowed in list at position 13: ...lotl wolf <--[HERE]");
		
		assertNoMoreResults(results);
	}

	/*************************
	 * Parse exception tests *
	 *************************/

	public static class ArgumentParseExceptionListArgumentVerifier<T>
			extends ArgumentParseExceptionContextVerifier<T, String, ListArgumentCommon.ArgumentParseExceptionInformation<T>> {
		public ArgumentParseExceptionListArgumentVerifier(TestBase testBase) {
			super(testBase);
		}

		public void assertCorrectContext(
			String exceptionMessage, CommandSender sender, String input, Map<String, Object> previousArgsMap,
			ListArgumentCommon.ArgumentParseExceptionInformation.Exceptions type,
			List<T> listSoFar, String rawItem, T currentItem,
			ArgumentParseExceptionContext<String, ListArgumentCommon.ArgumentParseExceptionInformation<T>, CommandSender> actual
		) {
			super.assertCorrectContext(exceptionMessage, sender, input, previousArgsMap, actual);

			ListArgumentCommon.ArgumentParseExceptionInformation<T> exceptionInformation = actual.exceptionInformation();
			assertEquals(type, exceptionInformation.type());
			assertEquals(listSoFar, exceptionInformation.listSoFar());
			assertEquals(rawItem, exceptionInformation.rawItem());
			assertEquals(currentItem, exceptionInformation.currentItem());
		}

		// Tests a case where the exception type is DUPLICATES_NOT_ALLOWED, when a duplicate item shows up and allow
		//  duplicates is false.
		void testDuplicatesNotAllowedCase(
			CommandSender sender, String command, String exceptionMessage,
			String input, Map<String, Object> previousArgsMap,
			List<T> listSoFar, String rawItem, T currentItem
		) {
			verifyGeneratedContext(
				sender, command, exceptionMessage,
				context -> assertCorrectContext(
					exceptionMessage, sender, input, previousArgsMap,
					ListArgumentCommon.ArgumentParseExceptionInformation.Exceptions.DUPLICATES_NOT_ALLOWED,
					listSoFar, rawItem, currentItem,
					context
				)
			);
		}

		// Tests a case where the exception type is INVALID_ITEM, where an item is given that is not found in the list.
		// currentItem should always be null here.
		void testInvalidItemCase(
			CommandSender sender, String command, String exceptionMessage,
			String input, Map<String, Object> previousArgsMap,
			List<T> listSoFar, String rawItem
		) {
			verifyGeneratedContext(
				sender, command, exceptionMessage,
				context -> assertCorrectContext(
					exceptionMessage, sender, input, previousArgsMap,
					ListArgumentCommon.ArgumentParseExceptionInformation.Exceptions.INVALID_ITEM,
					listSoFar, rawItem, null,
					context
				)
			);
		}
	}

	@Test
	void argumentParseExceptionTestWithListArgument() {
		PlayerMock player = server.addPlayer();
		ArgumentParseExceptionListArgumentVerifier<Integer> verifier = new ArgumentParseExceptionListArgumentVerifier<>(this);

		new CommandAPICommand("test")
			.withArguments(
				new StringArgument("buffer"),
				new ListArgumentBuilder<Integer>("list")
					.withList(1, 2, 3, 4, 5)
					.withStringMapper()
					.buildGreedy()
					.withArgumentParseExceptionHandler(verifier.getExceptionHandler())
			)
			.executesPlayer(P_EXEC)
			.register();

		// Test DUPLICATES_NOT_ALLOWED cases: Duplicate item given when that isn't allowed
		verifier.testDuplicatesNotAllowedCase(
			player, "test b123 1 1",
			"Duplicate arguments are not allowed at position 2: 1 <--[HERE]",
			"1 1", Map.of("buffer", "b123"), List.of(1), "1", 1
		);
		verifier.testDuplicatesNotAllowedCase(
			player, "test b123 1 2 3 3 5",
			"Duplicate arguments are not allowed at position 6: 1 2 3 <--[HERE]",
			"1 2 3 3 5", Map.of("buffer", "b123"), List.of(1, 2, 3), "3", 3
		);


		// Test INVALID_ITEM cases: Item given that is not in the defined list
		verifier.testInvalidItemCase(
			player, "test b123 1 10",
			"Item is not allowed in list at position 2: 1 <--[HERE]",
			"1 10", Map.of("buffer", "b123"), List.of(1), "10"
		);
		verifier.testInvalidItemCase(
			player, "test b123 1 2 3 a 5",
			"Item is not allowed in list at position 6: 1 2 3 <--[HERE]",
			"1 2 3 a 5", Map.of("buffer", "b123"), List.of(1, 2, 3), "a"
		);


		// Changing contents of buffer argument should update the previousArgs map
		verifier.testDuplicatesNotAllowedCase(
			player, "test b12345 1 1",
			"Duplicate arguments are not allowed at position 2: 1 <--[HERE]",
			"1 1", Map.of("buffer", "b12345"), List.of(1), "1", 1
		);
		verifier.testDuplicatesNotAllowedCase(
			player, "test b123456789012345 1 1",
			"Duplicate arguments are not allowed at position 2: 1 <--[HERE]",
			"1 1", Map.of("buffer", "b123456789012345"), List.of(1), "1", 1
		);

		verifier.testInvalidItemCase(
			player, "test b12345 1 2 3 a 5",
			"Item is not allowed in list at position 6: 1 2 3 <--[HERE]",
			"1 2 3 a 5", Map.of("buffer", "b12345"), List.of(1, 2, 3), "a"
		);
		verifier.testInvalidItemCase(
			player, "test b123456789012345 1 2 3 a 5",
			"Item is not allowed in list at position 6: 1 2 3 <--[HERE]",
			"1 2 3 a 5", Map.of("buffer", "b123456789012345"), List.of(1, 2, 3), "a"
		);
	}

	@Test
	void initialParseExceptionTestWithListTextArgument() {
		PlayerMock player = server.addPlayer();
		InitialParseExceptionTextArgumentVerifier verifier = new InitialParseExceptionTextArgumentVerifier(this);

		new CommandAPICommand("test")
			.withArguments(
				new StringArgument("buffer"),
				new ListArgumentBuilder<Integer>("list")
					.withList(1, 2, 3, 4, 5)
					.withStringMapper()
					.buildText()
					.withInitialParseExceptionHandler(verifier.getExceptionHandler())
			)
			.executesPlayer(P_EXEC)
			.register();

		// Test INVALID_ESCAPE cases: Backslash not followed by backslash or the same quote that started argument
		verifier.testInvalidEscapeCase(
			player, "test b123 \"\\abc\"",
			"Invalid escape sequence '\\a' in quoted string at position 12: ...st b123 \"\\<--[HERE]",
			10, 12
		);
		verifier.testInvalidEscapeCase(
			player, "test b123 \"ab\\c\"",
			"Invalid escape sequence '\\c' in quoted string at position 14: ... b123 \"ab\\<--[HERE]",
			10, 14
		);

		verifier.testInvalidEscapeCase(
			player, "test b123 \"\\'bc\"",
			"Invalid escape sequence '\\'' in quoted string at position 12: ...st b123 \"\\<--[HERE]",
			10, 12
		);
		verifier.testInvalidEscapeCase(
			player, "test b123 '\\\"bc'",
			"Invalid escape sequence '\\\"' in quoted string at position 12: ...st b123 '\\<--[HERE]",
			10, 12
		);


		// Test EXPECTED_QUOTE_END cases: Quoted string never ended by same quote that started argument
		verifier.testExpectedQuoteEndCase(
			player, "test b123 \"abc",
			"Unclosed quoted string at position 14: ... b123 \"abc<--[HERE]",
			10
		);
		verifier.testExpectedQuoteEndCase(
			player, "test b123 'abcde",
			"Unclosed quoted string at position 16: ...123 'abcde<--[HERE]",
			10
		);

		verifier.testExpectedQuoteEndCase(
			player, "test b123 \"abc'",
			"Unclosed quoted string at position 15: ...b123 \"abc'<--[HERE]",
			10
		);
		verifier.testExpectedQuoteEndCase(
			player, "test b123 'abcde\"",
			"Unclosed quoted string at position 17: ...23 'abcde\"<--[HERE]",
			10
		);


		// Increasing characters in buffer argument increases cursor start
		verifier.testInvalidEscapeCase(
			player, "test b12345 '\\\"bc'",
			"Invalid escape sequence '\\\"' in quoted string at position 14: ... b12345 '\\<--[HERE]",
			12, 14
		);
		verifier.testInvalidEscapeCase(
			player, "test b123456789012345 '\\\"bc'",
			"Invalid escape sequence '\\\"' in quoted string at position 24: ...9012345 '\\<--[HERE]",
			22, 24
		);

		verifier.testExpectedQuoteEndCase(
			player, "test b12345 \"abc'",
			"Unclosed quoted string at position 17: ...2345 \"abc'<--[HERE]",
			12
		);

		verifier.testExpectedQuoteEndCase(
			player, "test b123456789012345 \"abc'",
			"Unclosed quoted string at position 27: ...2345 \"abc'<--[HERE]",
			22
		);
	}

	/********************
	 * Suggestion tests *
	 ********************/

	@Test
	void suggestionTestWithListArgument() {
		new CommandAPICommand("list")
			.withArguments(new ListArgumentBuilder<>("values")
				.withList(() -> List.of("cat", "wolf", "axolotl"))
				.withStringMapper()
				.buildGreedy())
			.executesPlayer(P_EXEC)
		.register();

		PlayerMock player = server.addPlayer();

		// /list
		// All values should be suggested
		assertEquals(List.of("axolotl", "cat", "wolf"), server.getSuggestions(player, "list "));
		
		// /list cat 
		// All values should be suggested except for "cat"
		assertEquals(List.of("axolotl", "wolf"), server.getSuggestions(player, "list cat "));
	}

	@Test
	void suggestionTestWithListArgumentWithDuplicates() {
		new CommandAPICommand("list")
			.withArguments(new ListArgumentBuilder<>("values")
				.allowDuplicates(true)
				.withList(() -> List.of("cat", "wolf", "axolotl"))
				.withStringMapper()
				.buildGreedy())
			.executesPlayer(P_EXEC)
		.register();

		PlayerMock player = server.addPlayer();

		// /list
		// All values should be suggested
		assertEquals(List.of("axolotl", "cat", "wolf"), server.getSuggestions(player, "list "));
		
		// /list cat 
		// All values should also be suggested
		assertEquals(List.of("axolotl", "cat", "wolf"), server.getSuggestions(player, "list cat "));
	}
	
	@Test
	void suggestionTestWithListArgumentDelimiter() {
		new CommandAPICommand("list")
			.withArguments(new ListArgumentBuilder<>("values", ", ")
				.withList(() -> List.of("cat", "wolf", "axolotl"))
				.withStringMapper()
				.buildGreedy())
			.executesPlayer(P_EXEC)
		.register();

		PlayerMock player = server.addPlayer();

		// /list
		// The delimiter should be suggested
		assertEquals(List.of("cat, "), server.getSuggestions(player, "list cat"));
	}
	
	@Test
	void suggestionTestWithListArgumentText() {
		new CommandAPICommand("list")
			.withArguments(new ListArgumentBuilder<>("values")
				.withList(() -> List.of("cat", "wolf", "axolotl"))
				.withStringMapper()
				.buildText())
			.executesPlayer(P_EXEC)
		.register();

		PlayerMock player = server.addPlayer();

		// /list "cat
		// All values should be suggested except for "cat"
		assertEquals(List.of("axolotl", "wolf"), server.getSuggestions(player, "list \"cat "));
	}
}
