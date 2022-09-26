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

class ExtendedStringReader {

	public static readLocationLiteral(reader: StringReader): number {

		function isAllowedLocationLiteral(c: string): boolean {
			return c === '~' || c === '^';
		}

		let start = reader.getCursor();
		while (reader.canRead() && (StringReader.isAllowedNumber(reader.peek()) || isAllowedLocationLiteral(reader.peek()))) {
			reader.skip();
		}
		let number = reader.getString().substring(start, reader.getCursor());
		if (number.length === 0) {
			throw (CommandSyntaxException as any).BUILT_IN_EXCEPTIONS.readerExpectedInt().createWithContext(reader);
		}

		if (number.startsWith("~") || number.startsWith("^")) {
			if (number.length === 1) {
				// Accept.
				return 0;
			} else {
				number = number.slice(1);
			}
		}
		const result = parseInt(number);
		if (isNaN(result) || result !== parseFloat(number)) {
			reader.setCursor(start);
			throw (CommandSyntaxException as any).BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(reader, number);
		} else {
			return result;
		}
	}

	public static readResourceLocation(reader: StringReader): string {

		function isAllowedInResourceLocation(c: string): boolean {
			return ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || c === '_' || c === ':' || c === '/' || c === '.' || c === '-');
		}

		function validPathChar(c: string): boolean {
			return (c === '_' || c === '-' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c === '/' || c === '.');
		}

		function validNamespaceChar(c: string): boolean {
			return (c === '_' || c === '-' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c === '.');
		}

		function isValid(string: string, predicate: (c: string) => boolean): boolean {
			for (let i: number = 0; i < string.length; i++) {
				if (!predicate(string.charAt(i))) {
					return false;
				}
			}
			return true;
		}

		const start = reader.getCursor();
		while (reader.canRead() && isAllowedInResourceLocation(reader.peek())) {
			reader.skip();
		}

		let resourceLocation: string = reader.getString().substring(start, reader.getCursor());

		const resourceLocationParts: string[] = resourceLocation.split(":");
		switch (resourceLocationParts.length) {
			case 0:
				throw new SimpleCommandExceptionType(new LiteralMessage(resourceLocation + " is not a valid Resource")).createWithContext(reader);
			case 1:
				// Check path
				if (!isValid(resourceLocationParts[0], validPathChar)) {
					throw new SimpleCommandExceptionType(new LiteralMessage("Non [a-z0-9/._-] character in path of location: " + resourceLocation)).createWithContext(reader);
				}
				resourceLocation = `minecraft:${resourceLocation}`;
				break;
			case 2:
				// Check namespace
				if (!isValid(resourceLocationParts[0], validNamespaceChar)) {
					throw new SimpleCommandExceptionType(new LiteralMessage("Non [a-z0-9_.-] character in namespace of location: " + resourceLocation)).createWithContext(reader);
				}
				// Check path
				if (!isValid(resourceLocationParts[1], validPathChar)) {
					throw new SimpleCommandExceptionType(new LiteralMessage("Non [a-z0-9/._-] character in path of location: " + resourceLocation)).createWithContext(reader);
				}
				break;
		}

		return resourceLocation;
	}

}

/**
 * Helper for generating Promise<Suggestions>, from SharedSuggestionProvider.java
 */
class HelperSuggestionProvider {

	public static suggest(suggestions: string[], builder: SuggestionsBuilder): Promise<Suggestions> {
		let remainingLowercase: string = builder.getRemaining().toLowerCase();
		for (let suggestion of suggestions) {
			if (HelperSuggestionProvider.matchesSubStr(remainingLowercase, suggestion.toLowerCase())) {
				builder.suggest(suggestion);
			}
		}
		return builder.buildPromise();
	}

	public static matchesSubStr(remaining: string, suggestion: string): boolean {
		let index: number = 0;
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

export class TimeArgument implements ArgumentType<TimeArgument> {

	static UNITS: Map<string, number> = new Map([
		["d", 24000],
		["s", 20],
		["t", 1],
		["", 1]
	]);

	public ticks: number;

	constructor(ticks: number = 0) {
		this.ticks = ticks;
	}

	public parse(reader: StringReader): TimeArgument {
		const numericalValue: number = reader.readFloat();
		const unit: string = reader.readUnquotedString();
		const unitMultiplier: number = TimeArgument.UNITS.get(unit);
		if (unitMultiplier === 0) {
			throw new SimpleCommandExceptionType(new LiteralMessage(`Invalid unit "${unit}"`)).createWithContext(reader);
		}
		const ticks: number = Math.round(numericalValue * unitMultiplier);
		if (ticks < 0) {
			throw new SimpleCommandExceptionType(new LiteralMessage("Tick count must be non-negative")).createWithContext(reader);
		}
		this.ticks = ticks;
		return this;
	}

	public listSuggestions(_context: CommandContext<any>, builder: SuggestionsBuilder): Promise<Suggestions> {
		let reader: StringReader = new StringReader(builder.getRemaining());
		try {
			reader.readFloat();
		} catch (ex) {
			return builder.buildPromise();
		}
		return HelperSuggestionProvider.suggest([...TimeArgument.UNITS.keys()], builder.createOffset(builder.getStart() + reader.getCursor()));
	}

	public getExamples(): string[] {
		return ["0d", "0s", "0t", "0"];
	}
}

export class BlockPosArgument implements ArgumentType<BlockPosArgument> {

	public x: number;
	public y: number;
	public z: number;

	constructor(x: number = 0, y: number = 0, z: number = 0) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public parse(reader: StringReader): BlockPosArgument {
		this.x = ExtendedStringReader.readLocationLiteral(reader);
		reader.skip();
		this.y = ExtendedStringReader.readLocationLiteral(reader);
		reader.skip();
		this.z = ExtendedStringReader.readLocationLiteral(reader);
		return this;
	}

	public listSuggestions(_context: CommandContext<any>, builder: SuggestionsBuilder): Promise<Suggestions> {
		builder.suggest("~");
		builder.suggest("~ ~");
		builder.suggest("~ ~ ~");
		return builder.buildPromise();
	}

	public getExamples(): string[] {
		return ["1 2 3"];
	}
}

export class ColumnPosArgument implements ArgumentType<ColumnPosArgument> {

	public x: number;
	public z: number;

	constructor(x = 0, z = 0) {
		this.x = x;
		this.z = z;
	}

	public parse(reader: StringReader): ColumnPosArgument {
		this.x = ExtendedStringReader.readLocationLiteral(reader);
		reader.skip();
		this.z = ExtendedStringReader.readLocationLiteral(reader);
		return this;
	}

	public listSuggestions(_context: CommandContext<any>, builder: SuggestionsBuilder): Promise<Suggestions> {
		builder.suggest("~");
		builder.suggest("~ ~");
		return builder.buildPromise();
	}

	public getExamples(): string[] {
		return ["1 2"];
	}
}

export class PlayerArgument implements ArgumentType<PlayerArgument> {

	public username: string;

	constructor(username: string = "") {
		this.username = username;
	}

	public parse(reader: StringReader): PlayerArgument {
		const start: number = reader.getCursor();
		while (reader.canRead() && reader.peek() !== " ") {
			reader.skip();
		}

		const string: string = reader.getString();
		const currentCursor: number = reader.getCursor();

		this.username = string.slice(start, currentCursor);

		if (!this.username.match(/^[A-Za-z0-9_]{2,16}$/)) {
			throw new SimpleCommandExceptionType(new LiteralMessage(this.username + " is not a valid username")).createWithContext(reader);
		}

		return this;
	}

	public listSuggestions(_context: CommandContext<any>, _builder: SuggestionsBuilder): Promise<Suggestions> {
		return Suggestions.empty();
	}

	public getExamples(): string[] {
		return ["Skepter"];
	}
}

export class MultiLiteralArgument implements ArgumentType<MultiLiteralArgument> {

	private literals: string[];
	public selectedLiteral: string;

	constructor(literals: string[]) {
		this.literals = literals;
		this.selectedLiteral = "";
	}

	public parse(reader: StringReader) {
		const start: number = reader.getCursor();
		while (reader.canRead() && reader.peek() !== " ") {
			reader.skip();
		}

		this.selectedLiteral = reader.getString().slice(start, reader.getCursor());

		if (this.selectedLiteral.endsWith(" ")) {
			this.selectedLiteral.trimEnd();
			reader.setCursor(reader.getCursor() - 1);
		}

		if (!this.literals.includes(this.selectedLiteral)) {
			throw new SimpleCommandExceptionType(new LiteralMessage(this.selectedLiteral + " is not one of " + this.literals)).createWithContext(reader);
		}

		return this;
	}

	public listSuggestions(_context: CommandContext<any>, builder: SuggestionsBuilder): Promise<Suggestions> {
		for (let literal of this.literals) {
			builder.suggest(literal);
		}
		return builder.buildPromise();
	}

	public getExamples(): string[] {
		return ["blah"];
	}
}

export class ColorArgument implements ArgumentType<ColorArgument> {

	static ChatColor: { [color: string]: string } = {
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
	} as const;

	public chatcolor: string;

	constructor(chatcolor: string = null) {
		this.chatcolor = chatcolor;
	}

	public parse(reader: StringReader) {
		let input = reader.readUnquotedString();
		let chatFormat: string = ColorArgument.ChatColor[input.toLowerCase()];
		if (chatFormat === undefined) {
			throw new SimpleCommandExceptionType(new LiteralMessage(`Unknown colour '${input}'`)).createWithContext(reader);
		}
		this.chatcolor = chatFormat;
		return this;
	}

	public listSuggestions(_context: CommandContext<any>, builder: SuggestionsBuilder): Promise<Suggestions> {
		return HelperSuggestionProvider.suggest(Object.keys(ColorArgument.ChatColor), builder);
	}

	public getExamples(): string[] {
		return ["red", "green"];
	}
}

export class PotionEffectArgument implements ArgumentType<PotionEffectArgument> {

	static readonly PotionEffects: readonly string[] = [
		"minecraft:speed",
		"minecraft:slowness",
		"minecraft:haste",
		"minecraft:mining_fatigue",
		"minecraft:strength",
		"minecraft:instant_health",
		"minecraft:instant_damage",
		"minecraft:jump_boost",
		"minecraft:nausea",
		"minecraft:regeneration",
		"minecraft:resistance",
		"minecraft:fire_resistance",
		"minecraft:water_breathing",
		"minecraft:invisibility",
		"minecraft:blindness",
		"minecraft:night_vision",
		"minecraft:hunger",
		"minecraft:weakness",
		"minecraft:poison",
		"minecraft:wither",
		"minecraft:health_boost",
		"minecraft:absorption",
		"minecraft:saturation",
		"minecraft:glowing",
		"minecraft:levitation",
		"minecraft:luck",
		"minecraft:unluck",
		"minecraft:slow_falling",
		"minecraft:conduit_power",
		"minecraft:dolphins_grace",
		"minecraft:bad_omen",
		"minecraft:hero_of_the_village",
		"minecraft:darkness",
	] as const;

	public potionEffect: string;

	constructor(potionEffect: string = null) {
		this.potionEffect = potionEffect;
	}

	public parse(reader: StringReader): PotionEffectArgument {
		const input = ExtendedStringReader.readResourceLocation(reader);
		const isValidPotionEffect: boolean = PotionEffectArgument.PotionEffects.includes(input.toLowerCase());
		if (!isValidPotionEffect) {
			throw new SimpleCommandExceptionType(new LiteralMessage(`Unknown effect '${input}'`)).createWithContext(reader);
		}
		this.potionEffect = input;
		return this;
	}

	public listSuggestions(_context: CommandContext<any>, builder: SuggestionsBuilder): Promise<Suggestions> {
		return HelperSuggestionProvider.suggest([...PotionEffectArgument.PotionEffects], builder);
	}

	public getExamples(): string[] {
		return ["spooky", "effect"];
	}
}