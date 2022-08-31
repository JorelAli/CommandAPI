import { SimpleCommandExceptionType } from "./node_modules/node-brigadier/dist/index.js"

export class LocationArgument {

	constructor(x = 0, y = 0, z = 0) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	parse(reader) {
		this.x = reader.readInt();
		reader.skip();
		this.y = reader.readInt();
		reader.skip();
		this.z = reader.readInt();
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

		if(!this.username.match(/A-Za-z_/)) {
			throw new SimpleCommandExceptionType("AAAAA").createWithContext(reader);
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