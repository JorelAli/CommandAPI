import static org.mockito.Mockito.mock;

import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;

public class TestFile {

	private Server server;
	private Main plugin;

	@BeforeEach
	public void setUp() {
		server = MockBukkit.mock();
		server = mock(CraftServer.class);
		plugin = MockBukkit.load(Main.class);
	}

	@AfterEach
	public void tearDown() {
		MockBukkit.unmock();
	}
	
	@Test
	public void test() {
		System.out.println("Hi");
	}

}
