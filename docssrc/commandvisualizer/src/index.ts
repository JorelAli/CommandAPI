// @ts-check
import {
	CommandDispatcher,
	RootCommandNode,
	literal as literalArgument,
	argument,
	string as stringArgument,
	integer as integerArgument,
	float as floatArgument,
	bool as boolArgument,
	greedyString as greedyStringArgument,
	LiteralArgumentBuilder,
	ParseResults,
	CommandSyntaxException,
	CommandNode,
	ArgumentType as BrigadierArgumentType,
	ArgumentBuilder,
	Suggestions
} from "node-brigadier"

import {
	BlockPosArgument,
	PlayerArgument,
	MultiLiteralArgument,
	ColumnPosArgument,
	TimeArgument,
	ColorArgument
} from "./arguments"

/******************************************************************************
 * Classes & Interfaces                                                       *
 ******************************************************************************/

class MyCommandDispatcher<S> extends CommandDispatcher<S> {

	private root: RootCommandNode<S>;

	constructor(root?: RootCommandNode<S>) {
		super(root);
		this.root = root;
	}

	public deleteAll(): void {
		this.root = new RootCommandNode(undefined, undefined, undefined, undefined, undefined);
	}

	public override getRoot(): RootCommandNode<S> {
		return this.root;
	}

}

/**
 * `Selection.modify()` is not part of [the official spec](https://developer.mozilla.org/en-US/docs/Web/API/Selection/modify#specifications),
 * but it exists in all browsers. See [Microsoft/TypeScript#12296](https://github.com/Microsoft/TypeScript/issues/12296)
 */
type SelectionWithModify = Selection & {
	modify(s: string, t: string, u: string): void;
}

// We need a "filler" type for our command source. Since we're never actually
// using the command source, we'll just use an ADT with one entry

type Source = never;
const SOURCE: Source = undefined as never;

/******************************************************************************
 * Constants                                                                  *
 ******************************************************************************/

const COMMAND_INPUT: HTMLSpanElement = document.getElementById("cmd-input");
const COMMAND_INPUT_AUTOCOMPLETE = document.getElementById("cmd-input-autocomplete");
const ERROR_MESSAGE_BOX = document.getElementById("error-box");
const SUGGESTIONS_BOX = document.getElementById("suggestions-box");
const VALID_BOX = document.getElementById("valid-box");
const COMMANDS: HTMLTextAreaElement = document.getElementById("commands") as HTMLTextAreaElement;

const dispatcher = new MyCommandDispatcher<Source>();

/******************************************************************************
 * Prototypes                                                                 *
 ******************************************************************************/

// @ts-ignore
// CommandDispatcher.prototype.deleteAll = function deleteAll() { this.root = new RootCommandNode(); };

/******************************************************************************
 * Enums                                                                      *
 ******************************************************************************/

enum ChatColor {
	// Uses the section symbol (§), just like Minecraft
	BLACK = "\u00A70",
	DARK_BLUE = "\u00A71",
	DARK_GREEN = "\u00A72",
	DARK_AQUA = "\u00A73",
	DARK_RED = "\u00A74",
	DARK_PURPLE = "\u00A75",
	GOLD = "\u00A76",
	GRAY = "\u00A77",
	DARK_GRAY = "\u00A78",
	BLUE = "\u00A79",
	GREEN = "\u00A7a",
	AQUA = "\u00A7b",
	RED = "\u00A7c",
	LIGHT_PURPLE = "\u00A7d",
	YELLOW = "\u00A7e",
	WHITE = "\u00A7f"
};

const ChatColorCSS: Map<string, string> = new Map([
	["0", "black"],
	["1", "dark_blue"],
	["2", "dark_green"],
	["3", "dark_aqua"],
	["4", "dark_red"],
	["5", "dark_purple"],
	["6", "gold"],
	["7", "gray"],
	["8", "dark_gray"],
	["9", "blue"],
	["a", "green"],
	["b", "aqua"],
	["c", "red"],
	["d", "light_purple"],
	["e", "yellow"],
	["f", "white"]
]);

const ChatColorCSSReversed: Map<string, string> = new Map();
for (let [key, value] of ChatColorCSS) {
	ChatColorCSSReversed.set(value, key);
}

const ArgumentColors: { [colorIndex: number]: String } = {
	0: ChatColor.AQUA,
	1: ChatColor.YELLOW,
	2: ChatColor.GREEN,
	3: ChatColor.LIGHT_PURPLE,
	4: ChatColor.GOLD
} as const;

// As implemented by https://commandapi.jorel.dev/8.5.1/internal.html
const ArgumentType = new Map<String, () => BrigadierArgumentType<unknown> | null>([
	// CommandAPI separation
	["api:entity", () => null],
	["api:entities", () => null],
	["api:player", () => null],
	["api:players", () => null],
	["api:greedy_string", () => greedyStringArgument()],

	// Brigadier arguments
	["brigadier:bool", () => boolArgument()],
	["brigadier:double", () => floatArgument()],
	["brigadier:float", () => floatArgument()],
	["brigadier:integer", () => integerArgument()],
	["brigadier:long", () => integerArgument()],
	["brigadier:string", () => stringArgument()],

	// Minecraft arguments
	["minecraft:angle", () => null],
	["minecraft:block_pos", () => new BlockPosArgument()],
	["minecraft:block_predicate", () => null],
	["minecraft:block_state", () => null],
	["minecraft:color", () => new ColorArgument()],
	["minecraft:column_pos", () => new ColumnPosArgument()],
	["minecraft:component", () => null],
	["minecraft:dimension", () => null],
	["minecraft:entity", () => null],
	["minecraft:entity_anchor", () => null],
	["minecraft:entity_summon", () => null],
	["minecraft:float_range", () => null],
	["minecraft:function", () => null],
	["minecraft:game_profile", () => new PlayerArgument()],
	["minecraft:int_range", () => null],
	["minecraft:item_enchantment", () => null],
	["minecraft:item_predicate", () => null],
	["minecraft:item_slot", () => null],
	["minecraft:item_stack", () => null],
	["minecraft:message", () => null],
	["minecraft:mob_effect", () => null],
	["minecraft:nbt", () => null],
	["minecraft:nbt_compound_tag", () => null],
	["minecraft:nbt_path", () => null],
	["minecraft:nbt_tag", () => null],
	["minecraft:objective", () => null],
	["minecraft:objective_criteria", () => null],
	["minecraft:operation", () => null],
	["minecraft:particle", () => null],
	["minecraft:resource_location", () => null],
	["minecraft:rotation", () => null],
	["minecraft:score_holder", () => null],
	["minecraft:scoreboard_slot", () => null],
	["minecraft:swizzle", () => null],
	["minecraft:team", () => null],
	["minecraft:time", () => new TimeArgument()],
	["minecraft:uuid", () => null],
	["minecraft:vec2", () => null],
	["minecraft:vec3", () => null],
]);

/******************************************************************************
 * Helpers                                                                    *
 ******************************************************************************/

/**
 * Registers a command into the global command dispatcher
 * @param {string} configCommand the command to register, as declared using the
 * CommandAPI config.yml's command declaration syntax (See
 * https://commandapi.jorel.dev/8.5.1/conversionforownerssingleargs.html)
 */
function registerCommand(configCommand: string) {

	// No blank commands
	if (configCommand.trim().length === 0) {
		return;
	}

	function convertArgument(argumentType: string): BrigadierArgumentType<unknown> {
		if (argumentType.includes("..")) {
			let lowerBound: string = argumentType.split("..")[0];
			let upperBound: string = argumentType.split("..")[1];

			let lowerBoundNum: number = Number.MIN_SAFE_INTEGER;
			let upperBoundNum: number = Number.MAX_SAFE_INTEGER;

			if (lowerBound.length === 0) {
				lowerBoundNum = Number.MIN_SAFE_INTEGER;
			} else {
				lowerBoundNum = Number.parseFloat(lowerBound);
			}

			if (upperBound.length === 0) {
				upperBoundNum = Number.MAX_SAFE_INTEGER;
			} else {
				upperBoundNum = Number.parseFloat(upperBound);
			}

			// We've got a decimal number, use a float argument
			if (lowerBoundNum % 1 !== 0 || upperBoundNum % 1 !== 0) {
				return floatArgument(lowerBoundNum, upperBoundNum);
			} else {
				// Inclusive upper bound
				upperBoundNum += 1;
				return integerArgument(lowerBoundNum, upperBoundNum);
			}
		} else {
			const argumentGeneratorFunction = ArgumentType.get(argumentType);
			if (argumentGeneratorFunction()) {
				return argumentGeneratorFunction();
			} else {
				console.error("Unimplemented argument: " + argumentType);
				return null;
			}
		}
	}

	const command: string = configCommand.split(" ")[0];
	const args: string[] = configCommand.split(" ").slice(1);

	let commandToRegister: LiteralArgumentBuilder<Source> = literalArgument(command);
	let argumentsToRegister: Array<ArgumentBuilder<Source, any>> = [];

	// From dev/jorel/commandapi/AdvancedConverter.java
	const literalPattern: RegExp = RegExp(/\((\w+(?:\|\w+)*)\)/);
	const argumentPattern: RegExp = RegExp(/<(\w+)>\[([a-z:_]+|(?:[0-9\.]+)?\.\.(?:[0-9\.]+)?)\]/);

	for (let arg of args) {
		const matchedLiteral: RegExpMatchArray = arg.match(literalPattern);
		const matchedArgument: RegExpMatchArray = arg.match(argumentPattern);
		if (matchedLiteral) {
			// It's a literal argument
			const literals: string[] = matchedLiteral[1].split("|");
			if (literals.length === 1) {
				argumentsToRegister.unshift(literalArgument(literals[0]));
			} else if (literals.length > 1) {
				argumentsToRegister.unshift(argument(matchedLiteral[1], new MultiLiteralArgument(literals)));
			}
		} else if (matchedArgument) {
			// It's a regular argument
			const nodeName: string = matchedArgument[1];
			const argumentType: string = matchedArgument[2];

			let convertedArgumentType: BrigadierArgumentType<unknown> = convertArgument(argumentType);

			// We're adding arguments in reverse order (last arguments appear
			// at the beginning of the array) because it's much much easier to process
			argumentsToRegister.unshift(argument(nodeName, convertedArgumentType));
		}
	}

	if (argumentsToRegister.length > 0) {
		const lastArgument: BrigadierArgumentType<unknown> = argumentsToRegister[0].executes(_context => 0);

		// Flame on. Reduce.
		argumentsToRegister.shift();
		const reducedArguments = argumentsToRegister.reduce((prev: ArgumentBuilder<Source, any>, current: ArgumentBuilder<Source, any>) => current.then(prev), lastArgument);
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
	const sel: SelectionWithModify = document.getSelection() as SelectionWithModify;
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
 * @param index the number of characters into the current element
 *                       to place the cursor at
 * @param element the element to set the cursor for
 */
function setCursorPosition(index: number, element: Node): void {
	if (index >= 0) {
		const createRange = (node: Node, chars: { count: number }, range?: Range): Range => {
			if (!range) {
				range = document.createRange();
				range.selectNode(node);
				range.setStart(node, 0);
			}

			if (chars.count === 0) {
				range.setEnd(node, chars.count);
			} else if (node && chars.count > 0) {
				if (node.nodeType === Node.TEXT_NODE) {
					const nodeTextContentLength: number = node.textContent.length;
					if (nodeTextContentLength < chars.count) {
						chars.count -= nodeTextContentLength;
					} else {
						range.setEnd(node, chars.count);
						chars.count = 0;
					}
				} else {
					for (let lp: number = 0; lp < node.childNodes.length; lp++) {
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
		let range: Range = createRange(element, { count: index });

		if (range) {
			range.collapse(false);
			let selection: Selection = window.getSelection();
			selection.removeAllRanges();
			selection.addRange(range);
		}
	}
};

function getSelectedSuggestion(): HTMLElement {
	return document.querySelector(".yellow");
}

type CachedFontHTMLElement = HTMLElement & { currentFont: string }

class TextWidth {

	private static canvas: HTMLCanvasElement;

	/**
	 * Uses canvas.measureText to compute and return the width of the given text of given font in pixels.
	 * 
	 * @param {String} text The text to be rendered.
	 * @param {HTMLElement} element the element
	 * 
	 * @see https://stackoverflow.com/questions/118241/calculate-text-width-with-javascript/21015393#21015393
	 */
	static getTextWidth(text: string, element: CachedFontHTMLElement): number {
		// re-use canvas object for better performance
		const canvas: HTMLCanvasElement = TextWidth.canvas || (TextWidth.canvas = document.createElement("canvas"));
		const context: CanvasRenderingContext2D = canvas.getContext("2d");

		context.font = element.currentFont || (element.currentFont = TextWidth.getCanvasFont(element));
		return context.measureText(text).width;
	}

	private static getCssStyle(element: HTMLElement, prop: string): string {
		return window.getComputedStyle(element).getPropertyValue(prop);
	}

	private static getCanvasFont(el: HTMLElement = document.body): string {
		const fontWeight = TextWidth.getCssStyle(el, 'font-weight') || 'normal';
		const fontSize = TextWidth.getCssStyle(el, 'font-size') || '16px';
		const fontFamily = TextWidth.getCssStyle(el, 'font-family') || 'Times New Roman';

		return `${fontWeight} ${fontSize} ${fontFamily}`;
	}

}

/**
 * Takes Minecraft text and renders it in the chat box. This will automatically
 * add the leading / character, so you don't have to do that yourself!
 * @param {string} minecraftCodedText
 * @param {HTMLElement | null} target
 */
function setText(minecraftCodedText: string, target: HTMLElement = null) {
	minecraftCodedText = minecraftCodedText.replaceAll(" ", "\u00A0"); // Replace normal spaces with &nbsp; for HTML
	if (!target) {
		target = COMMAND_INPUT;
	}

	// Reset the text
	target.innerHTML = "";

	if (target === COMMAND_INPUT) {
		// Command forward slash. Always present, we don't want to remove this!
		let element: HTMLSpanElement = document.createElement("span");
		element.innerText = "/";
		target.appendChild(element);
	}

	let buffer: string = "";
	let currentColor: string = "";

	function writeBuffer(target: HTMLElement): void {
		if (buffer.length > 0) {
			let elem: HTMLSpanElement = document.createElement("span");
			elem.className = currentColor;
			elem.innerText = buffer;
			target.appendChild(elem);
			buffer = "";
		}
	};

	for (let i: number = 0; i < minecraftCodedText.length; i++) {
		if (minecraftCodedText[i] === "\u00A7") {
			writeBuffer(target);
			currentColor = ChatColorCSS.get(minecraftCodedText[i + 1]);
			i++;
			continue;
		} else {
			buffer += minecraftCodedText[i];
		}
	}

	writeBuffer(target);
}

function getText(withStyling: boolean = true): string {
	let buffer: string = "";
	for (let child of COMMAND_INPUT.children) {
		if (child.className && withStyling) {
			buffer += "\u00A7" + ChatColorCSSReversed.get(child.className);
		}
		buffer += (child as HTMLElement).innerText;
	}
	return buffer;
}

/******************************************************************************
 * Events                                                                     *
 ******************************************************************************/

COMMAND_INPUT.oninput = async function onCommandInput(): Promise<void> {
	let cursorPos: number = getCursorPosition();

	let rawText: string = COMMAND_INPUT.innerText.replace("\n", "");
	rawText = rawText.replaceAll("\u00a0", " "); // Replace &nbsp; with normal spaces for Brigadier

	let showUsageText: boolean = false;
	let errorText: string = "";
	let suggestions: string[] = [];
	let commandValid: boolean = false;

	// Render colors
	if (rawText.startsWith("/")) {
		// Parse the raw text
		const rawTextNoSlash: string = rawText.slice(1);
		const command: string = rawTextNoSlash.split(" ")[0];

		// Brigadier
		const parsedCommand: ParseResults<Source> = dispatcher.parse(rawTextNoSlash, SOURCE);
		const parsedCommandNoTrailing: ParseResults<Source> = dispatcher.parse(rawTextNoSlash.trimEnd(), SOURCE);
		console.log(parsedCommand);

		let lastNode: CommandNode<Source> = parsedCommandNoTrailing.getContext().getRootNode();
		if (parsedCommandNoTrailing.getContext().getNodes().length > 0) {
			lastNode = parsedCommandNoTrailing.getContext().getNodes()[parsedCommandNoTrailing.getContext().getNodes().length - 1].getNode();
		}
		const usage: string = dispatcher.getAllUsage(lastNode, SOURCE, false).join(" ");

		// Reset text
		setText(rawTextNoSlash);

		if (parsedCommand.getExceptions().size > 0) {
			// The command is invalid (the command doesn't exist). Make the whole text red.
			setText(ChatColor.RED + rawTextNoSlash);

			const exceptions: Map<CommandNode<Source>, CommandSyntaxException> = parsedCommand.getExceptions();
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
				if (errorText.startsWith("Unknown command at position")) {
					errorText = usage;
					showUsageText = true;
				}
			}

			if (errorText === "") {
				commandValid = true;
			}
		}

		// Colorize existing arguments
		if (showUsageText || commandValid) {
			let newText: string = command;
			let parsedArgumentIndex: number = 0;
			for (const [_key, value] of parsedCommand.getContext().getArguments()) {
				if (parsedArgumentIndex > Object.keys(ArgumentColors).length) {
					parsedArgumentIndex = 0;
				}

				newText += " ";
				newText += ArgumentColors[parsedArgumentIndex];
				newText += rawTextNoSlash.slice(value.getRange().getStart(), value.getRange().getEnd());

				parsedArgumentIndex++;
			}
			newText += "".padEnd(rawTextNoSlash.length - rawTextNoSlash.trimEnd().length);
			setText(newText);
		}

		const suggestionsResult: Suggestions = await dispatcher.getCompletionSuggestions(parsedCommand);
		suggestions = suggestionsResult.getList().map((x) => x.getText());
		console.log(suggestions)
	}

	// Set the cursor back to where it was. Since commands always start with a
	// forward slash, the only possible "starting caret position" is position 1
	// (in front of the slash)
	if (cursorPos === 0 && rawText.length > 0) {
		cursorPos = 1;
	}
	setCursorPosition(cursorPos, COMMAND_INPUT);
	COMMAND_INPUT.focus();

	// If any errors appear, display them
	if (errorText.length !== 0) {
		setText(errorText, ERROR_MESSAGE_BOX);
		ERROR_MESSAGE_BOX.hidden = false;
	} else {
		ERROR_MESSAGE_BOX.hidden = true;
	}

	if (showUsageText) {
		ERROR_MESSAGE_BOX.style.left = TextWidth.getTextWidth(rawText, COMMAND_INPUT as CachedFontHTMLElement) + "px";
		// 8px padding, 10px margin left, 10px margin right = -28px
		// Plus an extra 10px for good luck, why not
		ERROR_MESSAGE_BOX.style.width = `calc(100% - ${ERROR_MESSAGE_BOX.style.left} - 28px + 10px)`;
	} else {
		ERROR_MESSAGE_BOX.style.left = "0";
		ERROR_MESSAGE_BOX.style.width = "unset";
	}

	if (commandValid) {
		setText(ChatColor.GREEN + "This command is valid ✅", VALID_BOX);
		VALID_BOX.hidden = false;
	} else {
		VALID_BOX.hidden = true;
	}

	const constructSuggestionsHTML = (suggestions: string[]): HTMLSpanElement[] => {
		let nodesToAdd: HTMLSpanElement[] = [];
		for (let i: number = 0; i < suggestions.length; i++) {
			const suggestionElement: HTMLSpanElement = document.createElement("span");
			suggestionElement.innerText = suggestions[i];
			if (i === 0) {
				suggestionElement.className = "yellow";
			}
			if (i !== suggestions.length - 1) {
				suggestionElement.innerText += "\n";
			}
			nodesToAdd.push(suggestionElement);
		}

		return nodesToAdd;
	};

	// If suggestions are present, display them
	SUGGESTIONS_BOX.style.left = "0";
	if (suggestions.length !== 0) {
		SUGGESTIONS_BOX.innerHTML = "";
		for (let suggestionElement of constructSuggestionsHTML(suggestions)) {
			SUGGESTIONS_BOX.appendChild(suggestionElement);
		}
		SUGGESTIONS_BOX.style.left = TextWidth.getTextWidth(rawText, COMMAND_INPUT as CachedFontHTMLElement) + "px";
		// 8px padding, 10px margin left, 10px margin right = -28px
		// Plus an extra 10px for good luck, why not
		SUGGESTIONS_BOX.hidden = false;
		ERROR_MESSAGE_BOX.hidden = true;
	} else {
		SUGGESTIONS_BOX.hidden = true;
	}
	window.dispatchEvent(new Event("suggestionsUpdated"));
}

// We really really don't want new lines in our single-lined command!
COMMAND_INPUT.addEventListener('keydown', (evt: KeyboardEvent) => {
	switch (evt.key) {
		case "Enter":
			evt.preventDefault();
			break;
		case "ArrowDown":
		case "ArrowUp": {
			if (!SUGGESTIONS_BOX.hidden) {
				for (let i = 0; i < SUGGESTIONS_BOX.children.length; i++) {
					if (SUGGESTIONS_BOX.children[i].className === "yellow") {
						SUGGESTIONS_BOX.children[i].className = "";

						if (evt.key == "ArrowDown") {
							if (i === SUGGESTIONS_BOX.children.length - 1) {
								SUGGESTIONS_BOX.children[0].className = "yellow";
							} else {
								SUGGESTIONS_BOX.children[i + 1].className = "yellow";
							}
						} else {
							if (i === 0) {
								SUGGESTIONS_BOX.children[SUGGESTIONS_BOX.children.length - 1].className = "yellow";
							} else {
								SUGGESTIONS_BOX.children[i - 1].className = "yellow";
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
			if (COMMAND_INPUT.innerText.replace("\n", "").length === 0) {
				evt.preventDefault();
			}
			break;
		case "Tab":
			evt.preventDefault();
			setText(getText(false).slice(1) + COMMAND_INPUT_AUTOCOMPLETE.innerText);
			COMMAND_INPUT.oninput(null);
			setCursorPosition(COMMAND_INPUT.innerText.length, COMMAND_INPUT);
			break;
		default:
			break;
	}
});

window.addEventListener("suggestionsUpdated", (_event: Event) => {
	let rawText: string = COMMAND_INPUT.innerText;

	if (!SUGGESTIONS_BOX.hidden) {
		let selectedSuggestionText: string = getSelectedSuggestion().innerText.trim();

		// TODO: This obviously needs to be specific to the current suggestions, not the whole input
		if (rawText !== selectedSuggestionText) {
			let cursorPosition = getCursorPosition();
			setText(ChatColor.DARK_GRAY + selectedSuggestionText.slice(rawText.length - 1), COMMAND_INPUT_AUTOCOMPLETE);
			setCursorPosition(cursorPosition, COMMAND_INPUT);
			COMMAND_INPUT.focus();
		} else {
			setText("", COMMAND_INPUT_AUTOCOMPLETE);
		}
	} else {
		setText("", COMMAND_INPUT_AUTOCOMPLETE);
	}
});

// If you click on the chat box, focus the current text input area 
document.getElementById("chatbox").onclick = function onChatBoxClicked() {
	COMMAND_INPUT.focus();
};

document.getElementById("register-commands-button").onclick = function onRegisterCommandsButtonClicked() {
	dispatcher.deleteAll();
	COMMANDS.value.split("\n").forEach(registerCommand);
	COMMAND_INPUT.oninput(null); // Run syntax highlighter
}


/******************************************************************************
 * Entrypoint                                                                 *
 ******************************************************************************/

// Default commands
COMMANDS.value = `fill <pos1>[minecraft:block_pos] <pos2>[minecraft:block_pos] <block>[brigadier:string]
speed (walk|fly) <speed>[0..10] <target>[minecraft:game_profile]
hello <val>[1..20] <color>[minecraft:color]`;

document.getElementById("register-commands-button")?.onclick(null);
console.log("Dispatcher", dispatcher.getRoot())

