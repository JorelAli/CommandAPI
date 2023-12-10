package dev.jorel.commandapi.test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.GreedyArgumentException;
import dev.jorel.commandapi.exceptions.InvalidCommandNameException;
import dev.jorel.commandapi.exceptions.MissingCommandExecutorException;
import org.bukkit.Bukkit;
import org.bukkit.event.server.ServerLoadEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the semantics of registering commands
 */
class CommandRegistrationTests extends TestBase {

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
	void testCommandAPICommandGreedyArgumentException() {
		// Shouldn't throw, greedy argument is at the end
		CommandAPICommand validGreedyCommand = new CommandAPICommand("test")
			.withArguments(new StringArgument("arg1"))
			.withArguments(new GreedyStringArgument("arg2"))
			.executesPlayer(P_EXEC);

		assertDoesNotThrow(() -> validGreedyCommand.register());

		// Should throw, greedy argument is not at the end
		CommandAPICommand invalidGreedyCommand = new CommandAPICommand("test")
			.withArguments(new GreedyStringArgument("arg1"))
			.withArguments(new StringArgument("arg2"))
			.executesPlayer(P_EXEC);

		assertThrowsWithMessage(
			GreedyArgumentException.class,
			"Only one GreedyStringArgument or ChatArgument can be declared, at the end of a List. Found arguments: arg1<GreedyStringArgument> arg2<StringArgument> ",
			invalidGreedyCommand::register
		);
	}

	@Test
	void testCommandTreeGreedyArgumentException() {
		// Shouldn't throw, greedy argument is at the end
		CommandTree validGreedyCommand = new CommandTree("test")
			.then(
				new StringArgument("arg1")
					.then(
						new GreedyStringArgument("arg2")
							.executesPlayer(P_EXEC)
					)
			);

		assertDoesNotThrow(() -> validGreedyCommand.register());

		// Should throw, greedy argument is not at the end
		CommandTree invalidGreedyCommand = new CommandTree("test")
			.then(
				new GreedyStringArgument("arg1")
					.then(
						new StringArgument("arg2")
							.executesPlayer(P_EXEC)
					)
			);

		assertThrowsWithMessage(
			GreedyArgumentException.class,
			"Only one GreedyStringArgument or ChatArgument can be declared, at the end of a List. Found arguments: arg1<GreedyStringArgument> arg2<StringArgument> ",
			invalidGreedyCommand::register
		);
	}

	@Test
	void testCommandAPICommandInvalidCommandNameException() {
		assertThrowsWithMessage(
			InvalidCommandNameException.class,
			"Invalid command with name 'null' cannot be registered!",
			() -> new CommandAPICommand((String) null)
		);

		assertThrowsWithMessage(
			InvalidCommandNameException.class,
			"Invalid command with name '' cannot be registered!",
			() -> new CommandAPICommand("")
		);

		assertThrowsWithMessage(
			InvalidCommandNameException.class,
			"Invalid command with name 'my command' cannot be registered!",
			() -> new CommandAPICommand("my command")
		);
	}

	@Test
	void testCommandTreeInvalidCommandNameException() {
		assertThrowsWithMessage(
			InvalidCommandNameException.class,
			"Invalid command with name 'null' cannot be registered!",
			() -> new CommandTree(null)
		);

		assertThrowsWithMessage(
			InvalidCommandNameException.class,
			"Invalid command with name '' cannot be registered!",
			() -> new CommandTree("")
		);

		assertThrowsWithMessage(
			InvalidCommandNameException.class,
			"Invalid command with name 'my command' cannot be registered!",
			() -> new CommandTree("my command")
		);
	}

	@Test
	void testCommandAPICommandMissingCommandExecutorException() {
		// This command has no executor, should complain because this isn't runnable
		CommandAPICommand commandWithNoExecutors = new CommandAPICommand("test")
			.withArguments(new StringArgument("arg1"));

		assertThrowsWithMessage(
			MissingCommandExecutorException.class,
			"/test does not declare any executors or executable subcommands!",
			commandWithNoExecutors::register
		);

		// This command has no executable subcommands, should complain because this isn't runnable
		CommandAPICommand commandWithNoRunnableSubcommands = new CommandAPICommand("test")
			.withSubcommand(new CommandAPICommand("sub"));

		assertThrowsWithMessage(
			MissingCommandExecutorException.class,
			"/test does not declare any executors or executable subcommands!",
			commandWithNoRunnableSubcommands::register
		);

		// This command is okay because it is eventually executable through a subcommand
		CommandAPICommand commandWithEventuallyRunnableSubcommand = new CommandAPICommand("test")
			.withSubcommand(new CommandAPICommand("sub")
				.withSubcommand(new CommandAPICommand("sub")
					.withSubcommand(new CommandAPICommand("sub")
						.withSubcommand(new CommandAPICommand("sub")
							.executesPlayer(P_EXEC)
						)
					)
				)
			);

		assertDoesNotThrow(() -> commandWithEventuallyRunnableSubcommand.register());
	}

	// TODO: This test does not succeed
	//  Calling `register` on CommandTree without any `executes` defined simply does not create any commands
	//  These cases should throw MissingCommandExecutorExceptions
	@Disabled
	@Test
	void testCommandTreeMissingCommandExecutorException() {
		// This command has no executor, should complain because this isn't runnable
		CommandTree commandWithNoExecutors = new CommandTree("test");

		assertThrowsWithMessage(
			MissingCommandExecutorException.class,
			"",
			commandWithNoExecutors::register
		);

		// This command has no executable arguments, should complain because this isn't runnable
		CommandTree commandWithNoRunnableSubcommands = new CommandTree("test")
			.then(new LiteralArgument("sub"));

		assertThrowsWithMessage(
			MissingCommandExecutorException.class,
			"",
			commandWithNoRunnableSubcommands::register
		);

		// This command is okay because it eventually has an executable argument
		CommandTree commandWithEventuallyRunnableSubcommand = new CommandTree("test")
			.then(new LiteralArgument("sub")
				.then(new LiteralArgument("sub")
					.then(new LiteralArgument("sub")
						.then(new LiteralArgument("sub")
							.executesPlayer(P_EXEC)
						)
					)
				)
			);

		assertDoesNotThrow(() -> commandWithEventuallyRunnableSubcommand.register());

		// This command is not okay because some paths are not executable
		CommandTree commandTreeWithSomeNotExecutablePaths = new CommandTree("test")
			.then(new LiteralArgument("executable1").then(new LiteralArgument("sub").executesPlayer(P_EXEC)))
			.then(new LiteralArgument("notExecutable1").then(new LiteralArgument("sub")))
			.then(new LiteralArgument("notExecutable2").then(new LiteralArgument("sub")))
			.then(new LiteralArgument("executable2").then(new LiteralArgument("sub").executesPlayer(P_EXEC)));

		assertThrowsWithMessage(
			MissingCommandExecutorException.class,
			"",
			commandTreeWithSomeNotExecutablePaths::register
		);
	}

	@Test
	void testCommandAPICommandDuplicateNodeNameException() {
		// Make sure dispatcher is cleared from any previous tests
		CommandAPIHandler.getInstance().writeDispatcherToFile();

		// This command is not okay because it has duplicate names for Arguments 1 and 3
		CommandAPICommand commandWithDuplicateArgumentNames = new CommandAPICommand("test")
			.withArguments(
				new StringArgument("alice"),
				new StringArgument("bob"),
				new StringArgument("alice")
			)
			.executesPlayer(P_EXEC);

		commandWithDuplicateArgumentNames.register();
		// No commands in tree
		assertEquals("""
			{
			  "type": "root"
			}""", getDispatcherString());

		// This command is okay because LiteralArguments are exempt from the duplicate name rule
		CommandAPICommand commandWithDuplicateLiteralArgumentNames = new CommandAPICommand("test")
			.withArguments(
				new LiteralArgument("alice"),
				new LiteralArgument("bob"),
				new LiteralArgument("alice")
			)
			.executesPlayer(P_EXEC);

		commandWithDuplicateLiteralArgumentNames.register();
		// Command added to tree
		assertEquals("""
				{
				  "type": "root",
				  "children": {
				    "test": {
				      "type": "literal",
				      "children": {
				        "alice": {
				          "type": "literal",
				          "children": {
				            "bob": {
				              "type": "literal",
				              "children": {
				                "alice": {
				                  "type": "literal",
				                  "executable": true
				                }
				              }
				            }
				          }
				        }
				      }
				    }
				  }
				}""",
			getDispatcherString()
		);

		// This command is okay because MultiLiteralArguments are exempt from the duplicate name rule
		CommandAPICommand commandWithDuplicateMultiLiteralArgumentNames = new CommandAPICommand("test")
			.withArguments(
				new MultiLiteralArgument("alice", "option1", "option2"),
				new MultiLiteralArgument("bob", "option1", "option2"),
				new MultiLiteralArgument("alice", "option1", "option2")
			)
			.executesPlayer(P_EXEC);

		commandWithDuplicateMultiLiteralArgumentNames.register();
		// Command added to tree
		assertEquals("""
				{
				  "type": "root",
				  "children": {
				    "test": {
				      "type": "literal",
				      "children": {
				        "alice": {
				          "type": "literal",
				          "children": {
				            "bob": {
				              "type": "literal",
				              "children": {
				                "alice": {
				                  "type": "literal",
				                  "executable": true
				                }
				              }
				            }
				          }
				        },
				        "option1": {
				          "type": "literal",
				          "children": {
				            "option1": {
				              "type": "literal",
				              "children": {
				                "option1": {
				                  "type": "literal",
				                  "executable": true
				                },
				                "option2": {
				                  "type": "literal",
				                  "executable": true
				                }
				              }
				            },
				            "option2": {
				              "type": "literal",
				              "children": {
				                "option1": {
				                  "type": "literal",
				                  "executable": true
				                },
				                "option2": {
				                  "type": "literal",
				                  "executable": true
				                }
				              }
				            }
				          }
				        },
				        "option2": {
				          "type": "literal",
				          "children": {
				            "option1": {
				              "type": "literal",
				              "children": {
				                "option1": {
				                  "type": "literal",
				                  "executable": true
				                },
				                "option2": {
				                  "type": "literal",
				                  "executable": true
				                }
				              }
				            },
				            "option2": {
				              "type": "literal",
				              "children": {
				                "option1": {
				                  "type": "literal",
				                  "executable": true
				                },
				                "option2": {
				                  "type": "literal",
				                  "executable": true
				                }
				              }
				            }
				          }
				        }
				      }
				    }
				  }
				}""",
			getDispatcherString()
		);
	}

	@Test
	void testCommandTreeDuplicateNodeNameException() {
		// Make sure dispatcher is cleared from any previous tests
		CommandAPIHandler.getInstance().writeDispatcherToFile();

		// This command is not okay because it has duplicate names for Arguments 1 and 3
		CommandTree commandWithDuplicateArgumentNames = new CommandTree("test")
			.then(
				new StringArgument("alice").then(
					new StringArgument("bob").then(
						new StringArgument("alice")
							.executesPlayer(P_EXEC)
					)
				)
			);

		commandWithDuplicateArgumentNames.register();
		// No commands in tree
		assertEquals("""
			{
			  "type": "root"
			}""", getDispatcherString());

		// This command is okay because LiteralArguments are exempt from the duplicate name rule
		CommandTree commandWithDuplicateLiteralArgumentNames = new CommandTree("test")
			.then(
				new LiteralArgument("alice").then(
					new LiteralArgument("bob").then(
						new LiteralArgument("alice")
							.executesPlayer(P_EXEC)
					)
				)
			);

		commandWithDuplicateLiteralArgumentNames.register();
		// Command added to tree
		assertEquals("""
				{
				  "type": "root",
				  "children": {
				    "test": {
				      "type": "literal",
				      "children": {
				        "alice": {
				          "type": "literal",
				          "children": {
				            "bob": {
				              "type": "literal",
				              "children": {
				                "alice": {
				                  "type": "literal",
				                  "executable": true
				                }
				              }
				            }
				          }
				        }
				      }
				    }
				  }
				}""",
			getDispatcherString()
		);

		// This command is okay because MultiLiteralArguments are exempt from the duplicate name rule
		CommandTree commandWithDuplicateMultiLiteralArgumentNames = new CommandTree("test")
			.then(
				new MultiLiteralArgument("alice", "option1", "option2").then(
					new MultiLiteralArgument("bob", "option1", "option2").then(
						new MultiLiteralArgument("alice", "option1", "option2")
							.executesPlayer(P_EXEC)
					)
				)
			);

		commandWithDuplicateMultiLiteralArgumentNames.register();
		// Command added to tree
		assertEquals("""
				{
				  "type": "root",
				  "children": {
				    "test": {
				      "type": "literal",
				      "children": {
				        "alice": {
				          "type": "literal",
				          "children": {
				            "bob": {
				              "type": "literal",
				              "children": {
				                "alice": {
				                  "type": "literal",
				                  "executable": true
				                }
				              }
				            }
				          }
				        },
				        "option1": {
				          "type": "literal",
				          "children": {
				            "option1": {
				              "type": "literal",
				              "children": {
				                "option1": {
				                  "type": "literal",
				                  "executable": true
				                },
				                "option2": {
				                  "type": "literal",
				                  "executable": true
				                }
				              }
				            },
				            "option2": {
				              "type": "literal",
				              "children": {
				                "option1": {
				                  "type": "literal",
				                  "executable": true
				                },
				                "option2": {
				                  "type": "literal",
				                  "executable": true
				                }
				              }
				            }
				          }
				        },
				        "option2": {
				          "type": "literal",
				          "children": {
				            "option1": {
				              "type": "literal",
				              "children": {
				                "option1": {
				                  "type": "literal",
				                  "executable": true
				                },
				                "option2": {
				                  "type": "literal",
				                  "executable": true
				                }
				              }
				            },
				            "option2": {
				              "type": "literal",
				              "children": {
				                "option1": {
				                  "type": "literal",
				                  "executable": true
				                },
				                "option2": {
				                  "type": "literal",
				                  "executable": true
				                }
				              }
				            }
				          }
				        }
				      }
				    }
				  }
				}""",
			getDispatcherString()
		);

		// This command is okay because the duplicate names are on different paths
		CommandTree commandWithDuplicateNamesSeparated = new CommandTree("test")
			.then(new LiteralArgument("path1").then(new StringArgument("alice").executesPlayer(P_EXEC)))
			.then(new LiteralArgument("path2").then(new StringArgument("alice").executesPlayer(P_EXEC)))
			.then(new LiteralArgument("path3").then(new StringArgument("alice").executesPlayer(P_EXEC)))
			.then(new LiteralArgument("path4").then(new StringArgument("alice").executesPlayer(P_EXEC)));

		commandWithDuplicateNamesSeparated.register();
		// Command added to tree
		assertEquals("""
				{
				  "type": "root",
				  "children": {
				    "test": {
				      "type": "literal",
				      "children": {
				        "alice": {
				          "type": "literal",
				          "children": {
				            "bob": {
				              "type": "literal",
				              "children": {
				                "alice": {
				                  "type": "literal",
				                  "executable": true
				                }
				              }
				            }
				          }
				        },
				        "option1": {
				          "type": "literal",
				          "children": {
				            "option1": {
				              "type": "literal",
				              "children": {
				                "option1": {
				                  "type": "literal",
				                  "executable": true
				                },
				                "option2": {
				                  "type": "literal",
				                  "executable": true
				                }
				              }
				            },
				            "option2": {
				              "type": "literal",
				              "children": {
				                "option1": {
				                  "type": "literal",
				                  "executable": true
				                },
				                "option2": {
				                  "type": "literal",
				                  "executable": true
				                }
				              }
				            }
				          }
				        },
				        "option2": {
				          "type": "literal",
				          "children": {
				            "option1": {
				              "type": "literal",
				              "children": {
				                "option1": {
				                  "type": "literal",
				                  "executable": true
				                },
				                "option2": {
				                  "type": "literal",
				                  "executable": true
				                }
				              }
				            },
				            "option2": {
				              "type": "literal",
				              "children": {
				                "option1": {
				                  "type": "literal",
				                  "executable": true
				                },
				                "option2": {
				                  "type": "literal",
				                  "executable": true
				                }
				              }
				            }
				          }
				        },
				        "path1": {
				          "type": "literal",
				          "children": {
				            "alice": {
				              "type": "argument",
				              "parser": "brigadier:string",
				              "properties": {
				                "type": "word"
				              },
				              "executable": true
				            }
				          }
				        },
				        "path2": {
				          "type": "literal",
				          "children": {
				            "alice": {
				              "type": "argument",
				              "parser": "brigadier:string",
				              "properties": {
				                "type": "word"
				              },
				              "executable": true
				            }
				          }
				        },
				        "path3": {
				          "type": "literal",
				          "children": {
				            "alice": {
				              "type": "argument",
				              "parser": "brigadier:string",
				              "properties": {
				                "type": "word"
				              },
				              "executable": true
				            }
				          }
				        },
				        "path4": {
				          "type": "literal",
				          "children": {
				            "alice": {
				              "type": "argument",
				              "parser": "brigadier:string",
				              "properties": {
				                "type": "word"
				              },
				              "executable": true
				            }
				          }
				        }
				      }
				    }
				  }
				}""",
			getDispatcherString()
		);
	}

	@Test
	public void registerCommandsWithOtherNamespacesTests() {
		Mut<String> results = Mut.of();

		disablePaperImplementations();
		Bukkit.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());

		assertFalse(CommandAPI.canRegister());

		PlayerMock player = server.addPlayer();

		CommandAPICommand command = new CommandAPICommand("test")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Make sure the default registration with the minecraft: namespace still works
		command.register();

		server.dispatchCommand(player, "test alpha");
		assertEquals("alpha", results.get());

		server.dispatchCommand(player, "minecraft:alpha");
		assertEquals("alpha", results.get());
		assertNoMoreResults(results);

		CommandAPI.unregister("test", true);

		// Test registering the command with a custom namespace
		command.register("commandtest");

		server.dispatchCommand(player, "test alpha");
		assertEquals("alpha", results.get());

		server.dispatchCommand(player, "commandtest:alpha");
		assertEquals("alpha", results.get());

		// Running the command with the minecraft: namespace should fail
		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "minecraft:test alpha"));
		assertNoMoreResults(results);

		CommandAPI.unregister("test", true);

		// Test registering the command with a plugin instance
		command.register(Main.getPlugin(Main.class));

		server.dispatchCommand(player, "test alpha");
		assertEquals("alpha", results.get());

		server.dispatchCommand(player, "commandapitest:alpha");
		assertEquals("alpha", results.get());

		// Running the command with the minecraft: namespace should fail
		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "minecraft:test alpha"));
		assertNoMoreResults(results);

		CommandAPI.unregister("test", true);

		command = new CommandAPICommand("test")
			.withAliases("alpha", "beta")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Test aliases with the default namespace
		command.register();

		server.dispatchCommand(player, "alpha discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "beta discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "minecraft:alpha discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "minecraft:beta discord");
		assertEquals("discord", results.get());

		assertNoMoreResults(results);

		CommandAPI.unregister("test", true);
		CommandAPI.unregister("alpha", true);
		CommandAPI.unregister("beta", true);

		// Test aliases with a custom namespace
		command.register("commandtest");

		server.dispatchCommand(player, "alpha discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "beta discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "commandtest:alpha discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "commandtest:beta discord");
		assertEquals("discord", results.get());

		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "minecraft:alpha discord"));
		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "minecraft:beta discord"));

		assertNoMoreResults(results);

		CommandAPI.unregister("test", true);
		CommandAPI.unregister("alpha", true);
		CommandAPI.unregister("beta", true);

		// Test aliases with a plugin instance
		command.register(Main.getPlugin(Main.class));

		server.dispatchCommand(player, "alpha discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "beta discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "commandapitest:alpha discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "commandapitest:beta discord");
		assertEquals("discord", results.get());

		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "minecraft:alpha discord"));
		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "minecraft:beta discord"));

		assertNoMoreResults(results);

		CommandAPI.unregister("test", true);
		CommandAPI.unregister("alpha", true);
		CommandAPI.unregister("beta", true);

		// Test same command name and argument setup but different namespace
		CommandAPICommand a = new CommandAPICommand("test")
			.withArguments(LiteralArgument.of("forced"))
			.executesPlayer(info -> {
				results.set("forced");
			});

		CommandAPICommand b = new CommandAPICommand("test")
			.withArguments(LiteralArgument.of("notforced"))
			.executesPlayer(info -> {
				results.set("notforced");
			});

		a.register("a");
		b.register("b");

		server.dispatchCommand(player, "test forced");
		assertEquals("forced", results.get());

		server.dispatchCommand(player, "a:test forced");
		assertEquals("forced", results.get());

		server.dispatchCommand(player ,"test notforced");
		assertEquals("notforced", results.get());

		server.dispatchCommand(player, "b:test notforced");
		assertEquals("notforced", results.get());

		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "a:test notforced"));
		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "b:test forced"));

		assertNoMoreResults(results);

		CommandAPI.unregister("a", true);
		CommandAPI.unregister("b", true);

		// Special case: Registering a command without a namespace
		command = new CommandAPICommand("test")
			.executesPlayer(info -> {
				results.set("success");
			});

		command.register("");

		server.dispatchCommand(player, "test");
		assertEquals("success", results.get());

		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "minecraft:test"));

		CommandAPI.unregister("test", true);

		// Registering a command using null should fail
		String namespace = null;
		assertThrows(NullPointerException.class, () -> new CommandAPICommand("test").executesPlayer(info -> {}).register(namespace));
	}

}
