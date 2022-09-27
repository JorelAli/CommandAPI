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

import mojangson from "mojangson-parser"

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

	public static readMinMaxBounds(reader: StringReader): [number, number] {
		if(!reader.canRead()) {
			throw new SimpleCommandExceptionType(new LiteralMessage(`Expected value or range of values`)).createWithContext(reader);
		}

		const start = reader.getCursor();
		let min: number | null = null;
		let max: number | null = null;

		try {
			min = reader.readFloat();
		} catch(error) {
			// ignore it
		}

		if (reader.canRead(2) && reader.peek() == '.' && reader.peek(1) == '.') {
			reader.skip();
			reader.skip();

			try {
				max = reader.readFloat();
			} catch(error) {
				// ignore it
			}
		} else {
			max = min;
		}

		if(min === null && max === null) {
			reader.setCursor(start);
			throw new SimpleCommandExceptionType(new LiteralMessage(`Expected value or range of values`)).createWithContext(reader);
		} else {
			if(min === null) {
				min = Number.MIN_SAFE_INTEGER;
			}
			if(max === null) {
				max = Number.MAX_SAFE_INTEGER;
			}
		}

		return [min, max];
	}

	public static readNBT(reader: StringReader): string {
		const start: number = reader.getCursor();
		let nbt: string = "";
		while(reader.canRead()) {
			nbt += reader.read();
			try {
				mojangson(nbt);
				break;
			} catch(error) {
			}
		}
		try {
			mojangson(nbt);
		} catch(error) {
			reader.setCursor(start);
			throw new SimpleCommandExceptionType(new LiteralMessage(`${error}`)).createWithContext(reader);
		}
		return nbt;
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

export class AngleArgument implements ArgumentType<AngleArgument> {

	public angle: number;
	public relative: boolean;

	public parse(reader: StringReader): AngleArgument {
		if(!reader.canRead()) {
			throw new SimpleCommandExceptionType(new LiteralMessage("Incomplete (expected 1 angle)")).createWithContext(reader);
		}
		// Read relative ~
		if (reader.peek() === '~') {
			this.relative = true;
			reader.skip();
		} else {
			this.relative = false;
		}
		this.angle = (reader.canRead() && reader.peek() !== ' ') ? reader.readFloat() : 0.0;
		if(isNaN(this.angle) || !isFinite(this.angle)) {
			throw new SimpleCommandExceptionType(new LiteralMessage("Invalid angle")).createWithContext(reader);
		}
		return this;
	}

	public getExamples(): string[] {
		return ["0", "~", "~-5"];
	}
}

export class UUIDArgument implements ArgumentType<UUIDArgument> {

	public uuid: string;

	public parse(reader: StringReader): UUIDArgument {
		const remaining: string = reader.getRemaining();
		const matchedResults = remaining.match(/^([-A-Fa-f0-9]+)/);
		if(matchedResults !== null) {
			this.uuid = matchedResults[1];
			// Regex for a UUID: https://stackoverflow.com/a/13653180/4779071
			if(this.uuid.match(/^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$/i) !== null) {
				reader.setCursor(reader.getCursor() + this.uuid.length);
				return this;
			}
		}
		throw new SimpleCommandExceptionType(new LiteralMessage("Invalid UUID")).createWithContext(reader);
	}

	public getExamples(): string[] {
		return ["dd12be42-52a9-4a91-a8a1-11c01849e498"];
	}
}

type Modifier = (reader: StringReader) => void;

// Effectively a giant merge of EntityArgument, EntitySelectorParser and EntitySelectorOptions
export class EntitySelectorArgument implements ArgumentType<EntitySelectorArgument> {

	static shouldInvertValue(reader: StringReader): boolean {
		reader.skipWhitespace();
		if (reader.canRead() && reader.peek() === '!') {
			reader.skip();
			reader.skipWhitespace();
			return true;
		}
		return false;
	}

	// EntitySelectorParser suggestions
	private suggestions: string[];

	// EntitySelectorParser + EntitySelectorOptions
	private entityUUID: string;
	private includesEntities: boolean;
	private playerName: string;
	private maxResults: number;
	private isLimited: boolean; // This might be identical to the synthetic limitedToPlayers, try using this instead
	private limitedToPlayers: boolean; // players only?
	private currentEntity: boolean;
	private worldLimited: boolean;
	private order: string; // Not BiConsumer<Vec, List<? extends Entity>> because effort
	private isSorted: boolean; // Is this entity selector sorted?
	private hasGamemodeEquals: boolean; // Has gamemode ... equals?
	private hasGamemodeNotEquals: boolean; // uhh...
	private hasScores: boolean; 

	// EntityArgument
	private single: boolean;
	private playersOnly: boolean;

	private readonly Options: { [option: string]: Modifier } = {
		name: (reader: StringReader): void => {
			const start: number = reader.getCursor();
			const shouldInvert: boolean = EntitySelectorArgument.shouldInvertValue(reader);
			// const _s: string = reader.readString();
			if(/* STUB: this.hasNameNotEquals() */ !shouldInvert) {
				reader.setCursor(start);
				throw new SimpleCommandExceptionType(new LiteralMessage(`Option 'name' isn't applicable here`)).createWithContext(reader);
			}
			if(shouldInvert) {
				// stub: setHasNameNotEqual(true)
			} else {
				// stub: setHasNameEquals(true)
			}
			// Predicate?
		},
		distance: (_reader: StringReader): void => {},
		level: (_reader: StringReader): void => {},
		x: (reader: StringReader): void => { reader.readFloat() },
		y: (reader: StringReader): void => { reader.readFloat() },
		z: (reader: StringReader): void => { reader.readFloat() },
		dx: (reader: StringReader): void => { reader.readFloat() },
		dy: (reader: StringReader): void => { reader.readFloat() },
		dz: (reader: StringReader): void => { reader.readFloat() },
		x_rotation: (reader: StringReader): void => { ExtendedStringReader.readMinMaxBounds(reader) },
		y_rotation: (reader: StringReader): void => { ExtendedStringReader.readMinMaxBounds(reader) },
		limit: (reader: StringReader): void => {
			const start: number = reader.getCursor();
			const limit: number = reader.readInt();
			if(limit < 1) {
				reader.setCursor(start);
				throw new SimpleCommandExceptionType(new LiteralMessage(`Limit must be at least 1`)).createWithContext(reader);
			}
			this.maxResults = limit;
			this.isLimited = true;
		},
		sort: (reader: StringReader): void => {
			const start: number = reader.getCursor();
			const sortType: string = reader.readUnquotedString(); // word
			if(["nearest", "furthest", "random", "arbitrary"].includes(sortType)) {
				this.order = sortType;
				this.isSorted = true;
			} else {
				reader.setCursor(start);
				throw new SimpleCommandExceptionType(new LiteralMessage(`Invalid or unknown sort type '${sortType}'`)).createWithContext(reader);
			}
		},
		gamemode: (reader: StringReader): void => {
			const start: number = reader.getCursor();
			const shouldInvert: boolean = EntitySelectorArgument.shouldInvertValue(reader);
			// stub: if this.hasGamemodeNotEquals() && !shouldInvert {
			//     reset to start, ERROR_INAPPLICABLE_OPTION
			// }
			const gamemode: string = reader.readUnquotedString();
			if(!["survival", "creative", "adventure", "spectator"].includes(gamemode)) {
				reader.setCursor(start);
				throw new SimpleCommandExceptionType(new LiteralMessage(`Invalid or unknown game mode '${gamemode}'`)).createWithContext(reader);
			} else {
				this.includesEntities = false;
				if(shouldInvert) {
					this.hasGamemodeNotEquals = true;
				} else {
					this.hasGamemodeEquals = true;
				}
			}
		},
		team: (_reader: StringReader): void => {},
		type: (_reader: StringReader): void => {},
		tag: (reader: StringReader): void => {
			EntitySelectorArgument.shouldInvertValue(reader);
			reader.readUnquotedString();
		},
		nbt: (reader: StringReader): void => {
			const start: number = reader.getCursor();
			const shouldInvert: boolean = EntitySelectorArgument.shouldInvertValue(reader);
			try {
				ExtendedStringReader.readNBT(reader);
			} catch(error) {
				reader.setCursor(start);
				throw error;
			}
		},
		scores: (reader: StringReader): void => {
			// @e[scores={foo=10,bar=1..5}]
			reader.expect("{");
			reader.skipWhitespace();
			while (reader.canRead() && reader.peek() != '}') {
				reader.skipWhitespace();
				const str: string = reader.readUnquotedString();
				reader.skipWhitespace();
				reader.expect('=');
				reader.skipWhitespace();
				ExtendedStringReader.readMinMaxBounds(reader);
				reader.skipWhitespace();
				if (reader.canRead() && reader.peek() == ',') {
					reader.skip(); 
				}
			}
			reader.expect('}');
			this.hasScores = true;
		},
		advancements: (reader: StringReader): void => {
			throw new SimpleCommandExceptionType(new LiteralMessage("This command visualizer doesn't support 'advancements'")).createWithContext(reader);
		},
		predicate: (reader: StringReader): void => {
			throw new SimpleCommandExceptionType(new LiteralMessage("This command visualizer doesn't support 'predicate'")).createWithContext(reader);
		}
	} as const;

	public parse(reader: StringReader): EntitySelectorArgument {

		const parseOptions: () => void = () => {
			reader.skipWhitespace();
			while(reader.canRead() && reader.peek() !== "]") {
				reader.skipWhitespace();
				let start: number = reader.getCursor();
				let s: string = reader.readString();

				let modifier: Modifier = this.Options[s];
				if(modifier === null) {
					reader.setCursor(start);
					throw new SimpleCommandExceptionType(new LiteralMessage(`Unknown option '${s}'`)).createWithContext(reader);
				}

				reader.skipWhitespace();
				if(!reader.canRead() || reader.peek() !== "=") {
					reader.setCursor(start);
					throw new SimpleCommandExceptionType(new LiteralMessage(`Expected value for option '${s}'`)).createWithContext(reader);
				}
				reader.skip();
				reader.skipWhitespace();
				modifier(reader);
				reader.skipWhitespace();
				if(!reader.canRead()) {
					continue;
				}
				if(reader.peek() === ",") {
					reader.skip();
					continue;
				}
				if(reader.peek() !== "]") {
					throw new SimpleCommandExceptionType(new LiteralMessage("Expected end of options")).createWithContext(reader);
				}
			}
			if (reader.canRead()) {
				reader.skip();
				return;
			}
			throw new SimpleCommandExceptionType(new LiteralMessage("Expected end of options")).createWithContext(reader);
		}

		const parseSelector: () => void = () => {
			if(!reader.canRead()) {
				throw new SimpleCommandExceptionType(new LiteralMessage("Missing selector type")).createWithContext(reader);
			}

			let start: number = reader.getCursor();
			let selectorChar: string = reader.read();
			switch(selectorChar) {
				case "p":
					this.maxResults = 1;
					this.includesEntities = false;
					this.limitedToPlayers = true;
					break;
				case "a":
					this.maxResults = Number.MAX_SAFE_INTEGER;
					this.includesEntities = false;
					this.limitedToPlayers = true;
					break;
				case "r":
					this.maxResults = 1;
					this.includesEntities = false;
					this.limitedToPlayers = true;
					break;
				case "s":
					this.maxResults = 1;
					this.includesEntities = true;
					this.currentEntity = true;
					break;
				case "e":
					this.maxResults = Number.MAX_SAFE_INTEGER;
					this.includesEntities = true;
					break;
				default:
					reader.setCursor(start);
					throw new SimpleCommandExceptionType(new LiteralMessage(`Unknown selector type '${selectorChar}'`)).createWithContext(reader);
			}

			if (reader.canRead() && reader.peek() === "[") {
				reader.skip();
				parseOptions();
			}
		}

		const parseNameOrUUID: () => void = () => {
			let i: number = reader.getCursor();
			let s: string = reader.readString();

			// Regex for a UUID: https://stackoverflow.com/a/13653180/4779071
			if(s.match(/^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$/i) !== null) {
				this.entityUUID = s;
				this.includesEntities = true;
			} else if(s.length === 0 || s.length > 16) {
				reader.setCursor(i);
				throw new SimpleCommandExceptionType(new LiteralMessage("Invalid name or UUID")).createWithContext(reader);
			} else {
				this.playerName = s;
				this.includesEntities = false;
			}

			// We're only allowing 1 result because we've specified a player or
			// UUID, and not an @ selector
			this.maxResults = 1;
		}

		let start: number = reader.getCursor();
		if(reader.canRead() && reader.peek() === "@") {
			reader.skip();
			parseSelector()
		} else {
			parseNameOrUUID();
		}

		// Final checks...
		if(this.maxResults > 0 && this.single) {
			reader.setCursor(start);
			if(this.playersOnly) {
				throw new SimpleCommandExceptionType(new LiteralMessage("Only one player is allowed, but the provided selector allows more than one")).createWithContext(reader);
			} else {
				throw new SimpleCommandExceptionType(new LiteralMessage("Only one entity is allowed, but the provided selector allows more than one")).createWithContext(reader);
			}
		}
		if(this.includesEntities && this.playersOnly /* STUB: !isSelfSelector() */) {
			reader.setCursor(start);
			throw new SimpleCommandExceptionType(new LiteralMessage("Only players may be affected by this command, but the provided selector includes entities")).createWithContext(reader);
		}

		return this;
	}

	public listSuggestions(context: CommandContext<any>, builder: SuggestionsBuilder): Promise<Suggestions> {
		try {
			this.parse(new StringReader(builder.getInput() as string))
		} catch(exception) {}
		return Suggestions.empty();
	}

	public getExamples(): string[] {
		return ["dd12be42-52a9-4a91-a8a1-11c01849e498"];
	}
}