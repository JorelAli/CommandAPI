package dev.jorel.commandapi.test;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Dispatcher tests for the semantics of registering commands
 */
class CommandRegistrationDispatcherTests extends TestBase {

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

		commandWithDuplicateLiteralArgumentNames.register("minecraft");
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

		commandWithDuplicateMultiLiteralArgumentNames.register("minecraft");
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

		commandWithDuplicateLiteralArgumentNames.register("minecraft");
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

		commandWithDuplicateMultiLiteralArgumentNames.register("minecraft");
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

		commandWithDuplicateNamesSeparated.register("minecraft");
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
	void testCommandTreeThenNested() {
		// Make sure dispatcher is cleared from any previous tests
		CommandAPIHandler.getInstance().writeDispatcherToFile();

		// Register a command using the new `thenNested` method
		new CommandTree("test").thenNested(
			new LiteralArgument("a"),
			new LiteralArgument("b"),
			new LiteralArgument("c")
				.executesPlayer(P_EXEC)
		).register("minecraft");

		// Command added to tree
		assertEquals("""
			{
			  "type": "root",
			  "children": {
			    "test": {
			      "type": "literal",
			      "children": {
			        "a": {
			          "type": "literal",
			          "children": {
			            "b": {
			              "type": "literal",
			              "children": {
			                "c": {
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
			}""", getDispatcherString());
	}
}