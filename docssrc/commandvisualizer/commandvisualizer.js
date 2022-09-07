import {
	CommandDispatcher,
	RootCommandNode,
	literal as literalArgument,
	argument,
	string as stringArgument,
	integer as integerArgument,
	float as floatArgument,
	bool as boolArgument,
	greedyString as greedyStringArgument
} from "./node_modules/node-brigadier/dist/index.js"

import {
	BlockPosArgument,
	PlayerArgument,
	MultiLiteralArgument,
	ColumnPosArgument,
	TimeArgument,
	ColorArgument
} from "./arguments.js"

/******************************************************************************
 * Constants                                                                  *
 ******************************************************************************/

const commandInput = document.getElementById("cmd-input");
const commandInputAutocomplete = document.getElementById("cmd-input-autocomplete");
const errorMessageBox = document.getElementById("error-box");
const suggestionsBox = document.getElementById("suggestions-box");

const dispatcher = new CommandDispatcher();

/******************************************************************************
 * Prototypes                                                                 *
 ******************************************************************************/

CommandDispatcher.prototype.deleteAll = function deleteAll() {
	this.root = new RootCommandNode();
};

/******************************************************************************
 * Enums                                                                      *
 ******************************************************************************/

const ChatColor = {
	// Uses the section symbol (§), just like Minecraft
	BLACK: "\u00A70",
	DARK_BLUE: "\u00A71",
	DARK_GREEN: "\u00A72",
	DARK_AQUA: "\u00A73",
	DARK_RED: "\u00A74",
	DARK_PURPLE: "\u00A75",
	GOLD: "\u00A76",
	GRAY: "\u00A77",
	DARK_GRAY: "\u00A78",
	BLUE: "\u00A79",
	GREEN: "\u00A7a",
	AQUA: "\u00A7b",
	RED: "\u00A7c",
	LIGHT_PURPLE: "\u00A7d",
	YELLOW: "\u00A7e",
	WHITE: "\u00A7f",
};

const ChatColorCSS = {
	"0": "black",
	"1": "dark_blue",
	"2": "dark_green",
	"3": "dark_aqua",
	"4": "dark_red",
	"5": "dark_purple",
	"6": "gold",
	"7": "gray",
	"8": "dark_gray",
	"9": "blue",
	"a": "green",
	"b": "aqua",
	"c": "red",
	"d": "light_purple",
	"e": "yellow",
	"f": "white"
};

const ChatColorCSSReversed = {};
for (let key in ChatColorCSS) {
    ChatColorCSSReversed[ChatColorCSS[key]] = key;
}

const ArgumentColors = {
	0: ChatColor.AQUA,
	1: ChatColor.YELLOW,
	2: ChatColor.GREEN,
	3: ChatColor.LIGHT_PURPLE,
	4: ChatColor.GOLD
}

// As implemented by https://commandapi.jorel.dev/8.5.1/internal.html
const ArgumentType = {
	// CommandAPI separation
	"api:entity": () => null,
	"api:entities": () => null,
	"api:player": () => null,
	"api:players": () => null,
	"api:greedy_string": () => greedyStringArgument(),

	// Brigadier arguments
	"brigadier:bool": () => boolArgument(),
	"brigadier:double": () => floatArgument(),
	"brigadier:float": () => floatArgument(),
	"brigadier:integer": () => integerArgument(),
	"brigadier:long": () => integerArgument(),
	"brigadier:string": () => stringArgument(),

	// Minecraft arguments
	"minecraft:angle": () => null,
	"minecraft:block_pos": () => new BlockPosArgument(),
	"minecraft:block_predicate": () => null,
	"minecraft:block_state": () => null,
	"minecraft:color": () => new ColorArgument(),
	"minecraft:column_pos": () => new ColumnPosArgument(),
	"minecraft:component": () => null,
	"minecraft:dimension": () => null,
	"minecraft:entity": () => null,
	"minecraft:entity_anchor": () => null,
	"minecraft:entity_summon": () => null,
	"minecraft:float_range": () => null,
	"minecraft:function": () => null,
	"minecraft:game_profile": () => new PlayerArgument(),
	"minecraft:int_range": () => null,
	"minecraft:item_enchantment": () => null,
	"minecraft:item_predicate": () => null,
	"minecraft:item_slot": () => null,
	"minecraft:item_stack": () => null,
	"minecraft:message": () => null,
	"minecraft:mob_effect": () => null,
	"minecraft:nbt": () => null,
	"minecraft:nbt_compound_tag": () => null,
	"minecraft:nbt_path": () => null,
	"minecraft:nbt_tag": () => null,
	"minecraft:objective": () => null,
	"minecraft:objective_criteria": () => null,
	"minecraft:operation": () => null,
	"minecraft:particle": () => null,
	"minecraft:resource_location": () => null,
	"minecraft:rotation": () => null,
	"minecraft:score_holder": () => null,
	"minecraft:scoreboard_slot": () => null,
	"minecraft:swizzle": () => null,
	"minecraft:team": () => null,
	"minecraft:time": () => new TimeArgument(),
	"minecraft:uuid": () => null,
	"minecraft:vec2": () => null,
	"minecraft:vec3": () => null,
};

/******************************************************************************
 * Classes                                                                    *
 ******************************************************************************/

class Argument {
	constructor(nodeName, type) {
		this.nodeName = nodeName;
	}

	/**
	 * Gets the range for this argument from a parsed command
	 * @param {ParseResults<any>} parsedCommand a parsed command from dispatcher.parse
	 */
	getRange(parsedCommand) {
		return parsedCommand.context.args.get(this.nodeName)?.range ?? {start:0, end: 0};
	}
}

/******************************************************************************
 * Helpers                                                                    *
 ******************************************************************************/

/**
 * Registers a command into the global command dispatcher
 * @param {string} configCommand the command to register, as declared using the
 * CommandAPI config.yml's command declaration syntax (See
 * https://commandapi.jorel.dev/8.5.1/conversionforownerssingleargs.html)
 */
function registerCommand(configCommand) {

	// No blank commands
	if(configCommand.trim().length === 0) {
		return;
	}

	function convertArgument(argumentType) {
		if(argumentType.includes("..")) {
			let lowerBound = argumentType.split("..")[0];
			let upperBound = argumentType.split("..")[1];

			if(lowerBound.length === 0) {
				lowerBound = Number.MIN_SAFE_INTEGER;
			} else {
				lowerBound = Number.parseFloat(lowerBound);
			}

			if(upperBound.length === 0) {
				upperBound = Number.MAX_SAFE_INTEGER;
			} else {
				upperBound = Number.parseFloat(upperBound);
			}

			// We've got a decimal number, use a float argument
			if(lowerBound % 1 !== 0 || upperBound % 1 !== 0) {
				return floatArgument(lowerBound, upperBound);
			} else {
				// Inclusive upper bound
				upperBound += 1;
				return integerArgument(lowerBound, upperBound);
			}
		} else {
			const argumentGeneratorFunction = ArgumentType[argumentType];
			if(argumentGeneratorFunction()) {
				return argumentGeneratorFunction();
			} else {
				console.error("Unimplemented argument: " + argumentType);
			}
		}
	}

	const command = configCommand.split(" ")[0];
	const args = configCommand.split(" ").slice(1);

	let commandToRegister = literalArgument(command);
	let argumentsToRegister = [];

	// From dev/jorel/commandapi/AdvancedConverter.java
	const literalPattern = RegExp(/\((\w+(?:\|\w+)*)\)/);
	const argumentPattern = RegExp(/<(\w+)>\[([a-z:_]+|(?:[0-9\.]+)?\.\.(?:[0-9\.]+)?)\]/);

	for(let arg of args) {
		const matchedLiteral = arg.match(literalPattern);
		const matchedArgument = arg.match(argumentPattern);
		if(matchedLiteral) {
			// It's a literal argument
			const literals = matchedLiteral[1].split("|");
			if(literals.length === 1) {
				argumentsToRegister.unshift(literalArgument(literals[0]));
			} else if(literals.length > 1) {
				argumentsToRegister.unshift(argument(matchedLiteral[1], new MultiLiteralArgument(literals)));
			}
		} else if(matchedArgument) {
			// It's a regular argument
			const nodeName = matchedArgument[1];
			const argumentType = matchedArgument[2];
			
			let convertedArgumentType = convertArgument(argumentType);

			// We're adding arguments in reverse order (last arguments appear
			// at the beginning of the array) because it's much much easier to process
			argumentsToRegister.unshift(argument(nodeName, convertedArgumentType));
		}
	}

	if(argumentsToRegister.length > 0) {
		const lastArgument = argumentsToRegister[0].executes(context => {
			return 0;
		});

		// Flame on. Reduce.
		argumentsToRegister.shift();
		const reducedArguments = argumentsToRegister.reduce((prev, current) => current.then(prev), lastArgument);
		commandToRegister = commandToRegister.then(reducedArguments);
	}

	dispatcher.register(commandToRegister);
	// plugins-to-convert:
	//   - Essentials:
	//     - speed <speed>[0..10]
	//     - speed <target>[minecraft:game_profile]
	//     - speed (walk|fly) <speed>[0..10]
	//     - speed (walk|fly) <speed>[0..10] <target>[minecraft:game_profile]
}

/**
 * Gets the current cursor position.
 *
 * From https://thewebdev.info/2022/04/24/how-to-get-contenteditable-caret-position-with-javascript/
 * @returns The current cursor position for the current element
 */
function getCursorPosition() {
	const sel = document.getSelection();
	sel.modify("extend", "backward", "paragraphboundary");
	const pos = sel.toString().length;
	if (sel.anchorNode !== undefined && sel.anchorNode !== null) {
		sel.collapseToEnd();
	}
	return pos;
};

/**
 * Sets the current cursor position. This has to take into account the fact
 * that the current element is made up of many HTML elements instead of
 * plaintext, so selection ranges have to span across those elements to find
 * the exact location you're looking for.
 *
 * From https://stackoverflow.com/a/41034697/4779071
 * @param {number} index the number of characters into the current element
 *                       to place the cursor at
 * @param {Node} element the element to set the cursor for
 */
function setCursorPosition(index, element) {
	if (index >= 0) {
		const createRange = (/** @type Node */ node, /** @type {{count: number}} */ chars, /** @type Range */ range) => {
			if (!range) {
				range = document.createRange();
				range.selectNode(node);
				range.setStart(node, 0);
			}
		
			if (chars.count === 0) {
				range.setEnd(node, chars.count);
			} else if (node && chars.count > 0) {
				if (node.nodeType === Node.TEXT_NODE) {
					if (node.textContent.length < chars.count) {
						chars.count -= node.textContent.length;
					} else {
						range.setEnd(node, chars.count);
						chars.count = 0;
					}
				} else {
					for (let lp = 0; lp < node.childNodes.length; lp++) {
						range = createRange(node.childNodes[lp], chars, range);
		
						if (chars.count === 0) {
							break;
						}
					}
				}
			} 
		
			return range;
		};

		// We wrap index in an object so that recursive calls can use the
		// "new" value which is updated inside the object itself
		let range = createRange(element, { count: index });

		if (range) {
			range.collapse(false);
			let selection = window.getSelection();
			selection.removeAllRanges();
			selection.addRange(range);
		}
	}
};

function getSelectedSuggestion() {
	return document.querySelector(".yellow");
}


/**
  * Uses canvas.measureText to compute and return the width of the given text of given font in pixels.
  * 
  * @param {String} text The text to be rendered.
  * @param {String} font The css font descriptor that text is to be rendered with (e.g. "bold 14px verdana").
  * 
  * @see https://stackoverflow.com/questions/118241/calculate-text-width-with-javascript/21015393#21015393
  */
function getTextWidth(text, element) {

	function getCssStyle(element, prop) {
		return window.getComputedStyle(element, null).getPropertyValue(prop);
	}

	function getCanvasFont(el = document.body) {
		const fontWeight = getCssStyle(el, 'font-weight') || 'normal';
		const fontSize = getCssStyle(el, 'font-size') || '16px';
		const fontFamily = getCssStyle(el, 'font-family') || 'Times New Roman';
		
		return `${fontWeight} ${fontSize} ${fontFamily}`;
	}

	// re-use canvas object for better performance
	const canvas = getTextWidth.canvas || (getTextWidth.canvas = document.createElement("canvas"));
	const context = canvas.getContext("2d");
	context.font = element.currentFont || (element.currentFont = getCanvasFont(element));
	const metrics = context.measureText(text);
	return metrics.width;
}

/**
 * Takes Minecraft text and renders it in the chat box. This will automatically
 * add the leading / character, so you don't have to do that yourself!
 * @param {string} minecraftCodedText
 * @param {Node | null} target
 */
function setText(minecraftCodedText, target = null) {
	minecraftCodedText = minecraftCodedText.replaceAll(" ", "\u00A0"); // Replace normal spaces with &nbsp; for HTML
	if(!target) {
		target = commandInput;
	}
	// Reset the text
	target.innerHTML = "";

	if(target === commandInput) {
		// Command forward slash. Always present, we don't want to remove this!
		let element = document.createElement("span");
		element.innerText = "/";
		target.appendChild(element);
	}

	let buffer = "";
	let currentColor = "";

	function writeBuffer() {
		if(buffer.length > 0) {
			let elem = document.createElement("span");
			elem.className = currentColor;
			elem.innerText = buffer;
			target.appendChild(elem);
			buffer = "";
		}
	};

	for(let i = 0; i < minecraftCodedText.length; i++) {
		if(minecraftCodedText[i] === "\u00A7") {
			writeBuffer();
			currentColor = ChatColorCSS[minecraftCodedText[i + 1]];
			i++;
			continue;
		} else {
			buffer += minecraftCodedText[i];
		}
	}

	writeBuffer();
}

function getText(withStyling = true) {
	let buffer = "";
	for(let child of commandInput.children) {
		if(child.className && withStyling) {
			buffer += "\u00A7" + ChatColorCSSReversed[child.className];
		}
		buffer += child.innerText;
	}
	return buffer;
}

/******************************************************************************
 * Events                                                                     *
 ******************************************************************************/

commandInput.oninput = async function() {
	let cursorPos = getCursorPosition();
	let commands = ["say", "tp", "w", "weather", "whitelist", "worldborder"];

	/** @type string */
	let rawText = commandInput.innerText.replace("\n", "");
	rawText = rawText.replaceAll("\u00A0", " "); // Replace &nbsp; with normal spaces for Brigadier

	let showUsageText = false;
	let errorText = "";
	/** @type {string[]} */ let suggestions = [];

	// Render colors
	if(rawText.startsWith("/")) {
		// Parse the raw text
		const rawTextNoSlash = rawText.slice(1);
		const command = rawTextNoSlash.split(" ")[0];
		const rawArgs = rawText.split(" ").slice(1);

		// Brigadier
		const parsedCommand = dispatcher.parse(rawTextNoSlash, {});
		const parsedCommandNoTrailing = dispatcher.parse(rawTextNoSlash.trimEnd(), {});
		console.log(parsedCommand);

		let lastNode = parsedCommandNoTrailing.context.rootNode;
		if(parsedCommandNoTrailing.context.nodes.length > 0) {
			lastNode = parsedCommandNoTrailing.context.nodes[parsedCommandNoTrailing.context.nodes.length - 1].node;
		}
		const usage = dispatcher.getAllUsage(lastNode, {}, false).join(" ");

		// Reset text
		setText(rawTextNoSlash);
		let commandValid = false;

		if(parsedCommand.exceptions.size > 0) {
			// The command is invalid (the command doesn't exist). Make the whole text red.
			setText(ChatColor.RED + rawTextNoSlash);
			
			/** @type {Map<any, String>} */
			const exceptions = parsedCommand.exceptions;
			errorText = exceptions.entries().next().value[1].message;
		} else {
			// Brigadier is "happy" with the input. Let's run it and see!
			try {
				dispatcher.execute(parsedCommand);
			} catch (ex) {
				setText(ChatColor.RED + rawTextNoSlash);
				errorText = ex.message;

				// TODO: We actually need to take into account the case when the
				// command IS ACTUALLY unknown!
				if(errorText.startsWith("Unknown command at position")) {
					errorText = usage;
					showUsageText = true;
				}
			}
			
			if(errorText === "") {
				errorText = ChatColor.GREEN + "This command is valid ✅";
				commandValid = true;
			}
		}

		// Colorize existing arguments
		if (showUsageText || commandValid) {
			let newText = command;
			let parsedArgumentIndex = 0;
			for(const [key, value] of parsedCommand.context.args) {
				if(parsedArgumentIndex > Object.keys(ArgumentColors).length) {
					parsedArgumentIndex = 0;
				}

				newText += " ";
				newText += ArgumentColors[parsedArgumentIndex];
				newText += rawTextNoSlash.slice(value.range.start, value.range.end);

				parsedArgumentIndex++;
			}
			newText += "".padEnd(rawTextNoSlash.length - rawTextNoSlash.trimEnd().length);
			setText(newText);
		}

		const suggestionsResult = await dispatcher.getCompletionSuggestions(parsedCommand);
		suggestions = suggestionsResult.getList().map((x) => x.getText());
		if(commandValid) {
			suggestions = [];
		}
	}

	// Set the cursor back to where it was. Since commands always start with a
	// forward slash, the only possible "starting caret position" is position 1
	// (in front of the slash)
	if(cursorPos === 0 && rawText.length > 0) {
		cursorPos = 1;
	}
	setCursorPosition(cursorPos, commandInput);
	commandInput.focus();

	// If any errors appear, display them
	if(errorText.length !== 0) {
		setText(errorText, errorMessageBox);
		errorMessageBox.hidden = false;
	} else {
		errorMessageBox.hidden = true;
	}

	if(showUsageText) {
		errorMessageBox.style.left = getTextWidth(rawText, commandInput) + "px";
		// 8px padding, 10px margin left, 10px margin right = -28px
		// Plus an extra 10px for good luck, why not
		errorMessageBox.style.width = `calc(100% - ${errorMessageBox.style.left} - 28px + 10px)`;
	} else {
		errorMessageBox.style.left = 0;
		errorMessageBox.style.width = "unset";
	}

	const constructSuggestionsHTML = (suggestions) => {
		let nodesToAdd = [];
		for(let i = 0; i < suggestions.length; i++) {
			const suggestionElement = document.createElement("span");
			suggestionElement.innerText = suggestions[i];
			if(i === 0) {
				suggestionElement.className = "yellow";
			}
			if(i !== suggestions.length - 1) {
				suggestionElement.innerText += "\n";
			}
			nodesToAdd.push(suggestionElement);
		}

		return nodesToAdd;
	};

	// If suggestions are present, display them
	suggestionsBox.style.left = 0;
	if(suggestions.length !== 0) {
		suggestionsBox.innerHTML = "";
		for(let suggestionElement of constructSuggestionsHTML(suggestions)) {
			suggestionsBox.appendChild(suggestionElement);
		}
		suggestionsBox.style.left = getTextWidth(rawText, commandInput) + "px";
		// 8px padding, 10px margin left, 10px margin right = -28px
		// Plus an extra 10px for good luck, why not
		suggestionsBox.hidden = false;
		errorMessageBox.hidden = true;
	} else {
		suggestionsBox.hidden = true;
	}
	window.dispatchEvent(new Event("suggestionsUpdated"));
}

// We really really don't want new lines in our single-lined command!
commandInput.addEventListener('keydown', (evt) => {
	switch(evt.key) {
		case "Enter":
			evt.preventDefault();
			break;
		case "ArrowDown":
		case "ArrowUp": {
			if(!suggestionsBox.hidden) {
				for(let i = 0; i < suggestionsBox.childNodes.length; i++) {
					if(suggestionsBox.childNodes[i].className === "yellow") {
						suggestionsBox.childNodes[i].className = "";

						if(evt.key == "ArrowDown") {
							if(i === suggestionsBox.childNodes.length - 1) {
								suggestionsBox.childNodes[0].className = "yellow";
							} else {
								suggestionsBox.childNodes[i + 1].className = "yellow";
							}
						} else {
							if(i === 0) {
								suggestionsBox.childNodes[suggestionsBox.childNodes.length - 1].className = "yellow";
							} else {
								suggestionsBox.childNodes[i - 1].className = "yellow";
							}
						}

						window.dispatchEvent(new Event("suggestionsUpdated"));
						
						break;
					}
				}
			}
			break;
		}
		case "Backspace":
			if(commandInput.innerText.replace("\n", "").length === 0) {
				evt.preventDefault();
			}
			break;
		case "Tab":
			evt.preventDefault();
			setText(getText(false).slice(1) + commandInputAutocomplete.innerText);
			document.getElementById("cmd-input").oninput();
			setCursorPosition(commandInput.innerText.length, commandInput);
			break;
		default:
			break;
	}
});

window.addEventListener("suggestionsUpdated", (event) => {
	/** @type string */
	let rawText = commandInput.innerText;

	let errorText = "";
	let suggestions = [];

	if(!suggestionsBox.hidden) {
		/** @type string */
		let selectedSuggestionText = getSelectedSuggestion().innerText.trim();
		// TODO: This obviously needs to be specific to the current suggestions, not the whole input
		if(rawText !== selectedSuggestionText) {
			let cursorPosition = getCursorPosition();
			setText(ChatColor.DARK_GRAY + selectedSuggestionText.slice(rawText.length - 1), commandInputAutocomplete);
			setCursorPosition(cursorPosition, commandInput);
			commandInput.focus();
		} else {
			setText("", commandInputAutocomplete);
		}
	} else {
		setText("", commandInputAutocomplete);
	}
});

// If you click on the chat box, focus the current text input area 
document.getElementById("chatbox").onclick = function() {
	document.getElementById("cmd-input").focus();
	setCursorPosition(commandInput.innerText.length, commandInput);
};

document.getElementById("register-commands-button").onclick = function() {
	dispatcher.deleteAll();
	document.getElementById("commands").value.split("\n").forEach(registerCommand);
	commandInput.oninput(); // Run syntax highlighter
}


/******************************************************************************
 * Entrypoint                                                                 *
 ******************************************************************************/

// Default commands
document.getElementById("commands").value = `fill <pos1>[minecraft:block_pos] <pos2>[minecraft:block_pos] <block>[brigadier:string]
speed (walk|fly) <speed>[0..10] <target>[minecraft:game_profile]
hello <val>[1..20] <color>[minecraft:color]`;

document.getElementById("register-commands-button").onclick();
console.log("Dispatcher", dispatcher.getRoot())

