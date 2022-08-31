import * as brigadier from "./node_modules/node-brigadier/dist/index.js"

console.log(brigadier);

/******************************************************************************
 * Constants                                                                  *
 ******************************************************************************/

const commandInput = document.getElementById("cmd-input");
const commandInputAutocomplete = document.getElementById("cmd-input-autocomplete");
const errorMessageBox = document.getElementById("error-box");
const suggestionsBox = document.getElementById("suggestions-box");

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

// Argument types, with the argument and a matching function that returns if the
// input provided is a valid match
const ArgumentType = {
	AdvancementArgument: ["AdvancementArgument", (input) => {}],
	AdventureChatArgument: ["AdventureChatArgument", (input) => {}],
	AdventureChatComponentArgument: ["AdventureChatComponentArgument", (input) => {}],
	AngleArgument: ["AngleArgument", (input) => {}],
	AxisArgument: ["AxisArgument", (input) => {}],
	BiomeArgument: ["BiomeArgument", (input) => {}],
	BlockPredicateArgument: ["BlockPredicateArgument", (input) => {}],
	BlockStateArgument: ["BlockStateArgument", (input) => {}],
	BooleanArgument: ["BooleanArgument", (input) => {}],
	ChatArgument: ["ChatArgument", (input) => {}],
	ChatColorArgument: ["ChatColorArgument", (input) => {}],
	ChatComponentArgument: ["ChatComponentArgument", (input) => {}],
	CustomArgument: ["CustomArgument", (input) => {}],
	DoubleArgument: ["DoubleArgument", (input) => {}],
	EnchantmentArgument: ["EnchantmentArgument", (input) => {}],
	EntitySelectorArgument: ["EntitySelectorArgument", (input) => {}],
	EntityTypeArgument: ["EntityTypeArgument", (input) => {}],
	EnvironmentArgument: ["EnvironmentArgument", (input) => {}],
	FloatArgument: ["FloatArgument", (input) => {}],
	FloatRangeArgument: ["FloatRangeArgument", (input) => {}],
	FunctionArgument: ["FunctionArgument", (input) => {}],
	GreedyStringArgument: ["GreedyStringArgument", (input) => {}],
	IntegerArgument: ["IntegerArgument", (input) => {}],
	IntegerRangeArgument: ["IntegerRangeArgument", (input) => {}],
	IPreviewable: ["IPreviewable", (input) => {}],
	ItemStackArgument: ["ItemStackArgument", (input) => {}],
	ItemStackPredicateArgument: ["ItemStackPredicateArgument", (input) => {}],
	LiteralArgument: ["LiteralArgument", (input) => {}],
	Location2DArgument: ["Location2DArgument", (input) => {}],
	LocationArgument: ["LocationArgument", (input) => {}],
	LongArgument: ["LongArgument", (input) => {}],
	LootTableArgument: ["LootTableArgument", (input) => {}],
	MathOperationArgument: ["MathOperationArgument", (input) => {}],
	MultiLiteralArgument: ["MultiLiteralArgument", (input) => {}],
	NamespacedKeyArgument: ["NamespacedKeyArgument", (input) => {}],
	NBTCompoundArgument: ["NBTCompoundArgument", (input) => {}],
	ObjectiveArgument: ["ObjectiveArgument", (input) => {}],
	ObjectiveCriteriaArgument: ["ObjectiveCriteriaArgument", (input) => {}],
	OfflinePlayerArgument: ["OfflinePlayerArgument", (input) => {}],
	ParticleArgument: ["ParticleArgument", (input) => {}],
	PlayerArgument: ["PlayerArgument", (input) => {}],
	PotionEffectArgument: ["PotionEffectArgument", (input) => {}],
	RecipeArgument: ["RecipeArgument", (input) => {}],
	RotationArgument: ["RotationArgument", (input) => {}],
	ScoreboardSlotArgument: ["ScoreboardSlotArgument", (input) => {}],
	ScoreHolderArgument: ["ScoreHolderArgument", (input) => {}],
	SoundArgument: ["SoundArgument", (input) => {}],
	StringArgument: ["StringArgument", (input) => {}],
	TeamArgument: ["TeamArgument", (input) => {}],
	TextArgument: ["TextArgument", (input) => {}],
	TimeArgument: ["TimeArgument", (input) => {}],
	UUIDArgument: ["UUIDArgument", (input) => {}],
};

/******************************************************************************
 * Classes                                                                    *
 ******************************************************************************/

class Argument {
	constructor(argumentType) {
		this.argumentType = argumentType;
	}
}

/******************************************************************************
 * Helpers                                                                    *
 ******************************************************************************/

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
 * Takes Minecraft text and renders it in the chat box
 */
function setText(minecraftCodedText, target = null) {
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

commandInput.oninput = function() {
	let cursorPos = getCursorPosition();
	let commands = ["say", "tp", "w", "weather", "whitelist", "worldborder"];

	/** @type string */
	let rawText = commandInput.innerText.replace("\n", "");

	let errorText = "";
	let suggestions = [];

	// Render colors
	if(rawText.startsWith("/")) {
		// Parse the raw text
		const rawTextNoSlash = rawText.slice(1);
		const command = rawTextNoSlash.split(" ")[0];
		const rawArgs = rawText.split(" ").slice(1);

		if(!(commands.some((x) => x === command))) {
			// The command is invalid (the command doesn't exist). Make the whole text red.
			setText(ChatColor.RED + rawTextNoSlash);
			errorText = "Unknown or incomplete command, see below for error at position 1: /<--[HERE]";
		} else {
			setText(command + (rawArgs.length > 0 ? " " + ChatColor.AQUA + rawArgs.join(" ") : ""));
		}

		suggestions = commands.filter((x) => x.startsWith(rawTextNoSlash) && x !== rawTextNoSlash);
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
		errorMessageBox.innerText = errorText;
		errorMessageBox.hidden = false;
	} else {
		errorMessageBox.hidden = true;
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
	if(suggestions.length !== 0) {
		suggestionsBox.innerHTML = "";
		for(let suggestionElement of constructSuggestionsHTML(suggestions)) {
			suggestionsBox.appendChild(suggestionElement);
		}
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
};

// Run syntax highlighter
commandInput.oninput();