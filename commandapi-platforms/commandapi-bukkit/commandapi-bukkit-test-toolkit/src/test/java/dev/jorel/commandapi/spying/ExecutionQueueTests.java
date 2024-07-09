package dev.jorel.commandapi.spying;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTestBase;
import dev.jorel.commandapi.commandsenders.AbstractCommandSender;
import dev.jorel.commandapi.executors.ExecutionInfo;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExecutionQueueTests extends CommandTestBase {
	// Setup
	private ExecutionQueue queue;

	@BeforeEach
	public void setUp() {
		super.setUp();

		queue = getCommandAPIPlatform().getCommandAPIHandlerSpy().getExecutionQueue();

		new CommandAPICommand("test")
			.executes(DEFAULT_EXECUTOR)
			.register();
	}

	@AfterEach
	public void tearDown() {
		super.tearDown();
	}

	// Tests
	@Test
	void testQueueClear() {
		PlayerMock player = server.addPlayer();

		assertCommandSucceeds(player, "test");
		assertCommandSucceeds(player, "test");

		queue.clear();
		queue.assertNoMoreCommandsWereRun();
	}

	@Test
	void testQueueAddPoll() {
		ExecutionInfo<CommandSender, AbstractCommandSender<? extends CommandSender>> info1 = Mockito.mock();
		ExecutionInfo<CommandSender, AbstractCommandSender<? extends CommandSender>> info2 = Mockito.mock();

		queue.add(info1);
		queue.add(info2);

		// Ensure queue like first-in first-out
		assertEquals(info1, queue.poll());
		assertEquals(info2, queue.poll());

		queue.assertNoMoreCommandsWereRun();
	}

	@Test
	void testQueueAutoAdd() {
		PlayerMock player1 = server.addPlayer();
		PlayerMock player2 = server.addPlayer();

		assertCommandSucceeds(player1, "test");
		assertCommandSucceeds(player2, "test");

		// Ensure queue-like first-in first-out when automatically adding
		assertEquals(player1, queue.poll().sender());
		assertEquals(player2, queue.poll().sender());

		queue.assertNoMoreCommandsWereRun();
	}

	@Test
	void testAssertNoMoreCommandsWereRun() {
		// Successful use
		queue.assertNoMoreCommandsWereRun();

		// Unsuccessful use
		PlayerMock player = server.addPlayer();
		assertCommandSucceeds(player, "test");

		assertAssertionFails(
			() -> queue.assertNoMoreCommandsWereRun(),
			"Expected no more commands to be run, but found 1 command(s) left"
		);

		// Successful use
		queue.poll();
		queue.assertNoMoreCommandsWereRun();

		// Parse failed commands should not appear in queue
		assertCommandFails(player, "invalid", "Unknown command at position 0: <--[HERE]");
		queue.assertNoMoreCommandsWereRun();
	}
}
