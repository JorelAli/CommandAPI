import {
	SimpleCommandExceptionType,
	LiteralMessage,
	Suggestions,
	StringReader,
	CommandSyntaxException,
	CommandContext,
	SuggestionsBuilder,

	// Typing
	ArgumentType
} from "node-brigadier"

StringReader.prototype.readLocationLiteral = function readLocationLiteral(reader) {

	function isAllowedLocationLiteral(c) {
		return c == '~' || c == '^';
	}

	let start = this.cursor;
	while (this.canRead() && (StringReader.isAllowedNumber(this.peek()) || isAllowedLocationLiteral(this.peek()))) {
		this.skip();
	}
	let number = this.string.substring(start, this.cursor);
	if (number.length === 0) {
		throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedInt().createWithContext(this);
	}

	if(number.startsWith("~") || number.startsWith("^")) {
		if(number.length === 1) {
			// Accept.
			return number;
		} else {
			number = number.slice(1);
		}
	}
	const result = parseInt(number);
	if (isNaN(result) || result !== parseFloat(number)) {
		this.cursor = start;
		throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(this, number);
	} else {
		return result;
	}
}

/**
 * Helper for generating Promise<Suggestions>, from SharedSuggestionProvider.java
 */
class HelperSuggestionProvider {
	/**
	 * 
	 * @param {String[]} suggestions
	 * @param {SuggestionsBuilder} builder
	 * @return {Promise<Suggestions>}
	 */
	static suggest(suggestions, builder) {
		let remainingLowercase = builder.getRemaining().toLowerCase();
		for(let suggestion of suggestions) {
			if(HelperSuggestionProvider.matchesSubStr(remainingLowercase, suggestion.toLowerCase())) {
				builder.suggest(suggestion);
			}
		}
		return builder.buildPromise();
	}

	/**
	 * 
	 * @param {String} remaining 
	 * @param {String} suggestion 
	 * @returns {boolean}
	 */
	static matchesSubStr(remaining, suggestion) {
		let index = 0;
		while (!suggestion.startsWith(remaining, index)) {
			index = suggestion.indexOf('_', index);
			if (index < 0) {
				return false; 
			}
			index++;
		} 
		return true;
	}
}

/**
 * @extends {ArgumentType}
 */
export class TimeArgument {
	
	/** @type {Map<String, Number} */
	static UNITS = new Map();

	static {
		TimeArgument.UNITS.set("d", 24000);
		TimeArgument.UNITS.set("s", 20);
		TimeArgument.UNITS.set("t", 1);
		TimeArgument.UNITS.set("", 1);
	}

	constructor(ticks = 0) {
		this.ticks = ticks;
	}

	parse(/** @type {StringReader} */ reader) {
		const numericalValue = reader.readFloat();
		const unit = reader.readUnquotedString();
		const unitMultiplier = TimeArgument.UNITS.get(unit) ?? 0;
		if(unitMultiplier === 0) {
			throw new SimpleCommandExceptionType(new LiteralMessage(`Invalid unit "${unit}"`)).createWithContext(reader);
		}
		const ticks = Math.round(numericalValue * unitMultiplier);
		if(ticks < 0) {
			throw new SimpleCommandExceptionType(new LiteralMessage("Tick count must be non-negative")).createWithContext(reader);
		}
		this.ticks = ticks;
		return this;
	}

	/**
	 * 
	 * @param {CommandContext} context 
	 * @param {SuggestionsBuilder} builder 
	 * @returns {Promise<Suggestions>}
	 */
	listSuggestions(context, builder) {
		let reader = new StringReader(builder.getRemaining());
		try {
			reader.readFloat();
		} catch(ex) {
			return reader.buildPromise();
		}
		return HelperSuggestionProvider.suggest([...TimeArgument.UNITS.keys()], builder.createOffset(builder.getStart() + reader.getCursor()));
	}

	getExamples() {
		return ["0d", "0s", "0t", "0"];
	}
}

/**
 * @extends {ArgumentType}
 */
export class BlockPosArgument {

	constructor(x = 0, y = 0, z = 0) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	parse(/** @type {StringReader} */ reader) {
		this.x = reader.readLocationLiteral();
		reader.skip();
		this.y = reader.readLocationLiteral();
		reader.skip();
		this.z = reader.readLocationLiteral();
		return this;
	}

	/**
	 * 
	 * @param {CommandContext} context 
	 * @param {SuggestionsBuilder} builder 
	 * @returns {Promise<Suggestions>}
	 */
	listSuggestions(context, builder) {
		builder.suggest("~");
		builder.suggest("~ ~");
		builder.suggest("~ ~ ~");
		return builder.buildPromise();
	}

	getExamples() {
		return ["1 2 3"];
	}
}

/**
 * @extends {ArgumentType}
 */
export class ColumnPosArgument {

	constructor(x = 0, z = 0) {
		this.x = x;
		this.z = z;
	}

	parse(/** @type {StringReader} */ reader) {
		this.x = reader.readLocationLiteral();
		reader.skip();
		this.z = reader.readLocationLiteral();
		return this;
	}

	/**
	 * 
	 * @param {CommandContext} context 
	 * @param {SuggestionsBuilder} builder 
	 * @returns {Promise<Suggestions>}
	 */
	listSuggestions(context, builder) {
		builder.suggest("~");
		builder.suggest("~ ~");
		return builder.buildPromise();
	}

	getExamples() {
		return ["1 2"];
	}
}

/**
 * @extends {ArgumentType}
 */
export class PlayerArgument {
	/**
	 * 
	 * @param {string} username 
	 */
	constructor(username = "") {
		/** @type string */
		this.username = username;
	}

	parse(/** @type {StringReader} */ reader) {
		const start = reader.getCursor();
		while(reader.canRead() && reader.peek() !== " ") {
			reader.skip();
		}

		const string = reader.getString();
		const currentCursor = reader.getCursor();

		this.username = string.slice(start, currentCursor);

		if(!this.username.match(/^[A-Za-z0-9_]{2,16}$/)) {
			throw new SimpleCommandExceptionType(new LiteralMessage(this.username + " is not a valid username")).createWithContext(reader);
		}

		return this;
	}

	/**
	 * 
	 * @param {CommandContext} context 
	 * @param {SuggestionsBuilder} builder 
	 * @returns {Promise<Suggestions>}
	 */
	listSuggestions(context, builder) {
		return Suggestions.empty();
	}

	getExamples() {
		return ["Skepter"];
	}
}

/**
 * @extends {ArgumentType}
 */
export class MultiLiteralArgument {
	/**
	 * @param {Array<String>} literals 
	 */
	 constructor(literals) {
		/** @type {Array<String>} */
		this.literals = literals;
		/** @type {string} */
		this.selectedLiteral = "";
	}

	/** @override */
	parse(/** @type {StringReader} */ reader) {
		const start = reader.getCursor();
		while(reader.canRead() && reader.peek() !== " ") {
			reader.skip();
		}

		this.selectedLiteral = reader.getString().slice(start, reader.getCursor());

		if(this.selectedLiteral.endsWith(" ")) {
			this.selectedLiteral.trimEnd();
			reader.setCursor(reader.getCursor() - 1);
		}

		if(!this.literals.includes(this.selectedLiteral)) {
			throw new SimpleCommandExceptionType(new LiteralMessage(this.selectedLiteral + " is not one of " + this.literals)).createWithContext(reader);
		}

		return this;
	}

	/**
	 * 
	 * @param {CommandContext} context 
	 * @param {SuggestionsBuilder} builder 
	 * @returns {Promise<Suggestions>}
	 */
	/** @override */
	listSuggestions(context, builder) {
		for(let literal of this.literals) {
			builder.suggest(literal);
		}
		return builder.buildPromise();
	}

	/** @override */
	getExamples() {
		return ["blah"];
	}
}

/**
 * @extends {ArgumentType}
 */
export class ColorArgument {

	static ChatColor = {
		// Uses the section symbol (§), just like Minecraft
		black: "\u00A70",
		dark_blue: "\u00A71",
		dark_green: "\u00A72",
		dark_aqua: "\u00A73",
		dark_red: "\u00A74",
		dark_purple: "\u00A75",
		gold: "\u00A76",
		gray: "\u00A77",
		dark_gray: "\u00A78",
		blue: "\u00A79",
		green: "\u00A7a",
		aqua: "\u00A7b",
		red: "\u00A7c",
		light_purple: "\u00A7d",
		yellow: "\u00A7e",
		white: "\u00A7f",
	};

	/**
	 * @param {Array<String>} literals 
	 */
	constructor(chatcolor = null) {
		this.chatcolor = chatcolor;
	}

	parse(/** @type {StringReader} */ reader) {
		let input = reader.readUnquotedString();
		let chatFormat = ColorArgument.ChatColor[input.toLowerCase()];
		if(chatFormat === undefined) {
			throw new SimpleCommandExceptionType(new LiteralMessage(`Unknown colour '${input}'`)).createWithContext(reader);
		}
		this.chatcolor = chatFormat;
		return this;
	}

	/**
	 * 
	 * @param {CommandContext} context 
	 * @param {SuggestionsBuilder} builder 
	 * @returns {Promise<Suggestions>}
	 */
	listSuggestions(context, builder) {
		return HelperSuggestionProvider.suggest(Object.keys(ColorArgument.ChatColor), builder);
	}

	getExamples() {
		return ["red", "green"];
	}
}