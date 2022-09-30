/**
 * https://www.typescriptlang.org/docs/handbook/declaration-merging.html#module-augmentation
 *
 * This is an example of module augmentation. We want to modify the exising
 * module node-brigadier with our own extensions. We do that by declaring the
 * module "node-brigadier", our types that we want to augment and our new
 * methods. TypeScript will automatically update them. This lets you use the new
 * methods without TypeScript complaining that they don't exist. These new
 * methods can be used by importing this file (or if it's in the same folder,
 * you don't have to do anything, unless you're using webpack because webpack
 * needs to know it exists), e.g.:
 * 
 *   import "./brigadier_extensions"
 */

import {
	StringReader,
	CommandSyntaxException,
	SimpleCommandExceptionType,
	LiteralMessage
} from "node-brigadier"

import mojangson from "mojangson-parser"

declare module "node-brigadier" {
	interface StringReader {
		// New reading methods
		readLocationLiteral(): number;
		readResourceLocation(): [string, string];
		readMinMaxBounds(allowFloats: boolean): [number, number];
		readNBT(): string;

		/** @returns true if a negation character `!` was read */
		readNegationCharacter(): boolean;
		/** @returns true if a negation character `#` was read */
		readTagCharacter(): boolean;

		// New char methods
		isAllowedInResourceLocation(c: string): boolean;
		isValidPathChar(c: string): boolean;
		isValidNamespaceChar(c: string): boolean;
	}
}

StringReader.prototype.readLocationLiteral = function readLocationLiteral(): number {

	function isAllowedLocationLiteral(c: string): boolean {
		return c === '~' || c === '^';
	}

	let start = this.getCursor();
	while (this.canRead() && (StringReader.isAllowedNumber(this.peek()) || isAllowedLocationLiteral(this.peek()))) {
		this.skip();
	}
	let number = this.getString().substring(start, this.getCursor());
	if (number.length === 0) {
		throw (CommandSyntaxException as any).BUILT_IN_EXCEPTIONS.readerExpectedInt().createWithContext(this);
	}

	if (number.startsWith("~") || number.startsWith("^")) {
		if (number.length === 1) {
			// Accept. We don't care about returning reasonable results
			return 0;
		} else {
			number = number.slice(1);
		}
	}
	const result = parseInt(number);
	if (isNaN(result) || result !== parseFloat(number)) {
		this.setCursor(start);
		throw (CommandSyntaxException as any).BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(this, number);
	} else {
		return result;
	}

};

StringReader.prototype.readResourceLocation = function readResourceLocation(): [string, string] {

	function isValid(string: string, predicate: (c: string) => boolean): boolean {
		for (let i: number = 0; i < string.length; i++) {
			if (!predicate(string.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	const start = this.getCursor();
	while (this.canRead() && this.isAllowedInResourceLocation(this.peek())) {
		this.skip();
	}

	let resourceLocation: string = this.getString().substring(start, this.getCursor());

	const resourceLocationParts: string[] | undefined = resourceLocation.split(":");
	if (resourceLocationParts === undefined) {
		throw new SimpleCommandExceptionType(new LiteralMessage(resourceLocation + " is not a valid Resource")).createWithContext(this);
	}

	// Please excuse the abomination that is this Array.has(x : number between 0 and 2)
	// I'd use a switch statement, but TypeScript's type predicates don't pass through that
	// And I think this funky derpy solution is neater than using ! all over the place. It's
	// better to get the compiler to perform array indexing checks than try to tell it "yeah"
	if (resourceLocationParts.has(0)) {
		throw new SimpleCommandExceptionType(new LiteralMessage(resourceLocation + " is not a valid Resource")).createWithContext(this);
	}
	else if (resourceLocationParts.has(1)) {
		if (!isValid(resourceLocationParts[0], this.isValidPathChar)) {
			throw new SimpleCommandExceptionType(new LiteralMessage("Non [a-z0-9/._-] character in path of location: " + resourceLocation)).createWithContext(this);
		}
		return ["minecraft", resourceLocation];
	}
	else if (resourceLocationParts.has(2)) {
		// Check namespace
		if (!isValid(resourceLocationParts[0], this.isValidNamespaceChar)) {
			throw new SimpleCommandExceptionType(new LiteralMessage("Non [a-z0-9_.-] character in namespace of location: " + resourceLocation)).createWithContext(this);
		}
		// Check path
		if (!isValid(resourceLocationParts[1], this.isValidPathChar)) {
			throw new SimpleCommandExceptionType(new LiteralMessage("Non [a-z0-9/._-] character in path of location: " + resourceLocation)).createWithContext(this);
		}
	}
	else {
		throw new SimpleCommandExceptionType(new LiteralMessage(resourceLocation + " is not a valid Resource")).createWithContext(this);
	}
	return [resourceLocationParts[0], resourceLocationParts[1]];
};

StringReader.prototype.readMinMaxBounds = function readMinMaxBounds(allowFloats: boolean): [number, number] {
	if (!this.canRead()) {
		throw new SimpleCommandExceptionType(new LiteralMessage(`Expected value or range of values`)).createWithContext(this);
	}

	const start = this.getCursor();
	let min: number | null = null;
	let max: number | null = null;

	try {
		min = allowFloats ? this.readFloat() : this.readInt();
	} catch (error) {
		// ignore it
	}

	if (this.canRead(2) && this.peek() == '.' && this.peek(1) == '.') {
		this.skip();
		this.skip();

		try {
			max = allowFloats ? this.readFloat() : this.readInt();
		} catch (error) {
			// ignore it
		}
	} else {
		max = min;
	}

	if (min === null && max === null) {
		this.setCursor(start);
		throw new SimpleCommandExceptionType(new LiteralMessage(`Expected value or range of values`)).createWithContext(this);
	} else {
		if (min === null) {
			min = Number.MIN_SAFE_INTEGER;
		}
		if (max === null) {
			max = Number.MAX_SAFE_INTEGER;
		}
	}

	return [min, max];
};

StringReader.prototype.readNBT = function readNBT(): string {
	const start: number = this.getCursor();
	let nbt: string = "";
	while (this.canRead()) {
		nbt += this.read();
		try {
			mojangson(nbt);
			break;
		} catch (error) {
		}
	}
	try {
		mojangson(nbt);
	} catch (error) {
		this.setCursor(start);
		throw new SimpleCommandExceptionType(new LiteralMessage(`${error}`)).createWithContext(this);
	}
	return nbt;
};

StringReader.prototype.readNegationCharacter = function readNegationCharacter(): boolean {
	this.skipWhitespace();
	if (this.canRead() && this.peek() === '!') {
		this.skip();
		this.skipWhitespace();
		return true;
	}
	return false;
};

StringReader.prototype.readTagCharacter = function readTagCharacter(): boolean {
	this.skipWhitespace();
	if (this.canRead() && this.peek() === '#') {
		this.skip();
		this.skipWhitespace();
		return true;
	}
	return false;
};

StringReader.prototype.isAllowedInResourceLocation = function isAllowedInResourceLocation(c: string): boolean {
	return ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || c === '_' || c === ':' || c === '/' || c === '.' || c === '-');
};

StringReader.prototype.isValidPathChar = function isValidPathChar(c: string): boolean {
	return (c === '_' || c === '-' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c === '/' || c === '.');
};

StringReader.prototype.isValidNamespaceChar = function isValidNamespaceChar(c: string): boolean {
	return (c === '_' || c === '-' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c === '.');
};