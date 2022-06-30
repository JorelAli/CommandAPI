import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;

public class TestFile {

	private ServerMock server;
	private Main plugin;

	@BeforeEach
	public void setUp() {
		server = MockBukkit.mock();
		plugin = MockBukkit.load(Main.class, (Runnable) () -> {
			System.out.println("Run");
//			PlayerMock pMock = server.addPlayer();
//			pMock.updateCommands();
//			pMock.performCommand("hello blah");
//			System.out.println("Command ran, exiting...");
//			System.exit(0);
		});
	}

	@AfterEach
	public void tearDown() {
		//System.exit(0);
		//MockBukkit.unmock();
	}
	
	@Test
	public void test() {
		System.out.println("start test");
		PlayerMock pMock = server.addPlayer();
		System.out.println("Added player");
		// pMock.updateCommands();
		System.out.println("Commands updated. Running...");
		boolean result1 = server.dispatchCommand(pMock, "hello blah");
		System.out.println("Result1:@ " + result1);
		boolean result = pMock.performCommand("hello blah");
		System.out.println("result: " + result);
		System.out.println("Command ran, exiting...");
//		System.exit(0);
	}

}
