package io.github.jorelali;

import static org.junit.Assert.assertNotNull;

import org.bukkit.potion.PotionEffectType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.test.TestBase;

class MyCommandTests extends TestBase {

	@BeforeEach
	public void setUp() {
		super.setUp(MyMain.class);
	}

	@AfterEach
	public void tearDown() {
		super.tearDown();
	}

	@Test
	void testMyEffectCommand() {
		PlayerMock player = server.addPlayer();

		server.dispatchCommand(player, "myeffect " + player.getName() + " speed");
		assertNotNull(player.getPotionEffect(PotionEffectType.SPEED));
	}
}
