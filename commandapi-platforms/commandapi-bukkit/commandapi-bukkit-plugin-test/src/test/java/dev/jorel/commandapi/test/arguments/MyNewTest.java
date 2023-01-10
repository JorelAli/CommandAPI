package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.function.Supplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ListArgument;
import dev.jorel.commandapi.arguments.ListArgumentBuilder;
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.NMSProvider;
import dev.jorel.commandapi.test.TestBase;

/**
 * Tests for the {@link ListArgument}
 */
@SuppressWarnings("unchecked")
//@ExtendWith(TestNMSVersions.class)
public class MyNewTest extends TestBase {

	/*********
	 * Setup *
	 *********/

//	@BeforeEach
//	public void setUp(NMS<?> nms) {
//		super.setUp(nms);
//	}

	@AfterEach
	public void tearDown() {
		super.tearDown();
	}

	/*********
	 * Tests *
	 *********/

//	@Test
	@ParameterizedTest
	@ArgumentsSource(NMSProvider.class)
	public void executionTestWithListArgument(Supplier<NMS<?>> nms) {
		super.setUp(nms);
		System.out.println("AAA\n\n\nAAA");
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
	public void a() {
		System.out.println("hi");
	}
}
