import { SimpleCommandExceptionType, LiteralMessage, Suggestions, StringReader, CommandSyntaxException } from "./node_modules/node-brigadier/dist/index.js"

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

export class BlockPosArgument {

	constructor(x = 0, y = 0, z = 0) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	parse(reader) {
		this.x = reader.readLocationLiteral();
		reader.skip();
		this.y = reader.readLocationLiteral();
		reader.skip();
		this.z = reader.readLocationLiteral();
		return this;
	}

	listSuggestions(context, builder) {
		return Suggestions.empty();
	}

	getExamples() {
		return ["1 2 3"];
	}
}

export class PlayerArgument {
	/**
	 * 
	 * @param {string} username 
	 */
	constructor(username) {
		/** @type string */
		this.username = username;
	}

	parse(reader) {
		const start = reader.getCursor();
		while(reader.canRead() && reader.peek() !== " ") {
			reader.skip();
		}

		this.username = reader.getString().slice(start, reader.getCursor());

		if(!this.username.match(/[A-Za-z_]+/)) {
			throw new SimpleCommandExceptionType(new LiteralMessage(this.username + " is not a valid username")).createWithContext(reader);
		}

		return this;
	}

	listSuggestions(context, builder) {
		return Suggestions.empty();
	}

	getExamples() {
		return ["Skepter"];
	}
}

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

	parse(reader) {
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

	listSuggestions(context, builder) {
		for(let literal of this.literals) {
			builder.suggest(literal);
		}
		let result = builder.buildPromise();
		return result;
	}

	getExamples() {
		return ["blah"];
	}
}