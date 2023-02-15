package dev.jorel.commandapi.test.wrappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.bukkit.scoreboard.DisplaySlot;
import org.junit.jupiter.api.Test;

import dev.jorel.commandapi.wrappers.ScoreboardSlot;

class ScoreboardSlotWrapperTests {

	@Test
	void testDisplaySlotMapping() {
		// Values 0, 1, 2
		assertEquals(DisplaySlot.PLAYER_LIST, new ScoreboardSlot(0).getDisplaySlot());
		assertEquals(DisplaySlot.SIDEBAR, new ScoreboardSlot(1).getDisplaySlot());
		assertEquals(DisplaySlot.BELOW_NAME, new ScoreboardSlot(2).getDisplaySlot());

		// Everything else. These values should never occur, but we default to
		// DisplaySlot.SIDEBAR
//		for (int i = -10; i < 0; i++) {
//			assertEquals(DisplaySlot.PLAYER_LIST, new ScoreboardSlot(i).getDisplaySlot());
//		}
//		for (int i = 3; i <= 10; i++) {
//			assertEquals(DisplaySlot.PLAYER_LIST, new ScoreboardSlot(i).getDisplaySlot());
//		}
	}

	@Test
	void testScoreboardSlotOf() {
		// TODO: Need to rewrite ScoreboardSlot before I can come back to this...
	}

}
