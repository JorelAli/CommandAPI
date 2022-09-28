import {
	SimpleCommandExceptionType,
	LiteralMessage,
	Suggestions,
	StringReader,
	CommandSyntaxException,
	CommandContext,
	SuggestionsBuilder,

	// Typing
	ArgumentType,
	ArgumentBuilder
} from "node-brigadier"

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
		const unitMultiplier: number | undefined = TimeArgument.UNITS.get(unit);
		if(unitMultiplier === undefined) {
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

	public x: number = 0;
	public y: number = 0;
	public z: number = 0;

	public parse(reader: StringReader): BlockPosArgument {
		this.x = reader.readLocationLiteral();
		reader.skip();
		this.y = reader.readLocationLiteral();
		reader.skip();
		this.z = reader.readLocationLiteral();
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

	public x: number = 0;
	public z: number = 0;

	public parse(reader: StringReader): ColumnPosArgument {
		this.x = reader.readLocationLiteral();
		reader.skip();
		this.z = reader.readLocationLiteral();
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

	public username: string = "";

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
	public selectedLiteral: string = "";

	constructor(literals: string[]) {
		this.literals = literals;
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

	public chatcolor: string = "";

	public parse(reader: StringReader) {
		let input = reader.readUnquotedString();
		let chatFormat: string | undefined = ColorArgument.ChatColor[input.toLowerCase()];
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

	public potionEffect: string = "";

	public parse(reader: StringReader): PotionEffectArgument {
		const resourceLocation: [string, string] = reader.readResourceLocation();
		const input = resourceLocation[0] + ":" + resourceLocation[1];
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
		if(matchedResults !== null && matchedResults[1] !== undefined) {
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
type OptionsType =
	"name" | "distance" | "level" | "x" | "y" | "z" | "dx" | "dy" | "dz" |
	"x_rotation" | "y_rotation" | "limit" | "sort" | "gamemode" | "team" |
	"type" | "tag" | "nbt" | "scores" | "advancements" | "predicate";

const entityTypes = [
	"area_effect_cloud", "armor_stand", "arrow", "axolotl", "bat", "bee", "blaze",
	"boat", "cat", "cave_spider", "chicken", "cod", "cow", "creeper", "dolphin",
	"donkey", "dragon_fireball", "drowned", "elder_guardian", "end_crystal", "ender_dragon",
	"enderman", "endermite", "evoker", "evoker_fangs", "experience_orb", "eye_of_ender",
	"falling_block", "firework_rocket", "fox", "ghast", "giant", "glow_item_frame",
	"glow_squid", "goat", "guardian", "hoglin", "horse", "husk", "illusioner", "iron_golem",
	"item", "item_frame", "fireball", "leash_knot", "lightning_bolt", "llama", "llama_spit",
	"magma_cube", "marker", "minecart", "chest_minecart", "command_block_minecart",
	"furnace_minecart", "hopper_minecart", "spawner_minecart", "tnt_minecart", "mule",
	"mooshroom", "ocelot", "painting", "panda", "parrot", "phantom", "pig", "piglin",
	"piglin_brute", "pillager", "polar_bear", "tnt", "pufferfish", "rabbit", "ravager",
	"salmon", "sheep", "shulker", "shulker_bullet", "silverfish", "skeleton", "skeleton_horse",
	"slime", "small_fireball", "snow_golem", "snowball", "spectral_arrow", "spider", "squid",
	"stray", "strider", "egg", "ender_pearl", "experience_bottle", "potion", "trident",
	"trader_llama", "tropical_fish", "turtle", "vex", "villager", "vindicator",
	"wandering_trader", "witch", "wither", "wither_skeleton", "wither_skull", "wolf",
	"zoglin", "zombie", "zombie_horse", "zombie_villager", "zombified_piglin", "player", "fishing_bobber"
] as const;
type EntityType = typeof entityTypes[number];

const EntityTags: readonly string[] = [
	"arrows", "axolotl_always_hostiles", "axolotl_hunt_targets", "beehive_inhabitors",
	"freeze_hurts_extra_types", "freeze_immune_entity_types", "frog_food", "impact_projectiles",
	"powder_snow_walkable_mobs", "raiders", "skeletons"
]


class SuggestionsHelper {

	public static shouldSuggest(remaining: string, suggestion: string): boolean {
		let i: number = 0;
		while(!suggestion.startsWith(remaining, i)) {
			i = suggestion.indexOf("_", i);
			if(i < 0) {
				return false;
			}
			i++;
		}
		return true;
	}

	public static suggestMatching(reader: StringReader, suggestions: string[]): string[] {
		let newSuggestions: string[] = [];
		const remaining: string = reader.getRemaining().toLocaleLowerCase();
		for(let suggestion of suggestions) {
			if(!SuggestionsHelper.shouldSuggest(remaining, suggestion.toLocaleLowerCase())) {
				continue;
			}
			newSuggestions.push(suggestion);
		}
		return newSuggestions;
	}

}

// Effectively a giant merge of EntityArgument, EntitySelectorParser and EntitySelectorOptions
export class EntitySelectorArgument implements ArgumentType<EntitySelectorArgument> {

	// EntitySelectorParser suggestions
	private suggestions: string[];
	private suggestionsModifier: ((builder: SuggestionsBuilder) => void) | null = null;

	// EntitySelectorParser + EntitySelectorOptions
	private entityUUID: string;
	private includesEntities: boolean;
	private playerName: string;
	private maxResults: number;
	private isLimited: boolean; // This might be identical to the synthetic limitedToPlayers, try using this instead

	private limitedToPlayers: boolean; // players only?
	private entityType: EntityType;

	private currentEntity: boolean;
	private worldLimited: boolean;
	private order: string; // Not BiConsumer<Vec, List<? extends Entity>> because effort
	private isSorted: boolean; // Is this entity selector sorted?

	private selectsGamemode: boolean = false;
	private excludesGamemode: boolean = false;

	private hasNameEquals: boolean;
	private hasNameNotEquals: boolean;
	private hasScores: boolean; 
	private typeInverse: boolean; // isTypeLimitedInversely AKA excludesEntityType

	// EntityArgument
	private single: boolean;
	private playersOnly: boolean;

	constructor(single: boolean, playersOnly: boolean) {
		this.single = single;
		this.playersOnly = playersOnly;
	}

	private suggestionGenerator(reader: StringReader, type: OptionsType): string[] {
		let suggestions: string[] = [];
		switch(type) {
			case "gamemode": {
				let string: string = reader.getRemaining().toLowerCase();
				let bool: boolean = !this.excludesGamemode;
				let negating: boolean = true;
				if(string.length !== 0) {
					if(string.charAt(0) === "!") {
						bool = false;
						string = string.slice(1);
					} else {
						negating = false;
					}
				}

				for(let gamemode of ["survival", "creative", "adventure", "spectator"]) {
					if(!gamemode.toLowerCase().startsWith(string)) {
						continue;
					}
					if(negating) {
						suggestions.push("!" + gamemode);
					}
					if(!bool) {
						continue;
					}
					suggestions.push(gamemode);
				}
				break;
			}
			case "sort": {
				suggestions.push(...SuggestionsHelper.suggestMatching(reader, ["nearest", "furthest", "random", "arbitrary"]));
				break;
			}
			case "type": {
				suggestions.push(...SuggestionsHelper.suggestMatching(reader, entityTypes.map(entity => `!${entity}`)));
				suggestions.push(...SuggestionsHelper.suggestMatching(reader, EntityTags.map(entity => `!#${entity}`)));
				if(!this.typeInverse) {
					suggestions.push(...SuggestionsHelper.suggestMatching(reader, [...entityTypes]));
					suggestions.push(...SuggestionsHelper.suggestMatching(reader, EntityTags.map(entity => `#${entity}`)));
				}
				break;
			}
		}
		return suggestions;
	}

	private readonly Options: { [option in OptionsType]: Modifier } = {
		name: (reader: StringReader): void => {
			const start: number = reader.getCursor();
			const shouldInvert: boolean = reader.readNegationCharacter();
			const _s: string = reader.readString();
			if(this.hasNameNotEquals && !shouldInvert) {
				reader.setCursor(start);
				throw new SimpleCommandExceptionType(new LiteralMessage(`Option 'name' isn't applicable here`)).createWithContext(reader);
			}
			if(shouldInvert) {
				this.hasNameNotEquals = true;
			} else {
				this.hasNameEquals = true;
			}
		},
		distance: (_reader: StringReader): void => {},
		level: (_reader: StringReader): void => {},
		x: (reader: StringReader): void => { reader.readFloat() },
		y: (reader: StringReader): void => { reader.readFloat() },
		z: (reader: StringReader): void => { reader.readFloat() },
		dx: (reader: StringReader): void => { reader.readFloat() },
		dy: (reader: StringReader): void => { reader.readFloat() },
		dz: (reader: StringReader): void => { reader.readFloat() },
		x_rotation: (reader: StringReader): void => { reader.readMinMaxBounds() },
		y_rotation: (reader: StringReader): void => { reader.readMinMaxBounds() },
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
			this.suggestions = this.suggestionGenerator(reader, "sort");
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
			this.suggestions = this.suggestionGenerator(reader, "gamemode");
			const start: number = reader.getCursor();
			const shouldInvert: boolean = reader.readNegationCharacter();
			if(this.excludesGamemode && !shouldInvert) {
				reader.setCursor(start);
				throw new SimpleCommandExceptionType(new LiteralMessage(`Option 'gamemode' isn't applicable here`)).createWithContext(reader);
			}
			const gamemode: string = reader.readUnquotedString();
			if(!["survival", "creative", "adventure", "spectator"].includes(gamemode)) {
				reader.setCursor(start);
				throw new SimpleCommandExceptionType(new LiteralMessage(`Invalid or unknown game mode '${gamemode}'`)).createWithContext(reader);
			} else {
				this.includesEntities = false;
				if(shouldInvert) {
					this.excludesGamemode = true;
				} else {
					this.selectsGamemode = true;
				}
			}
		},
		team: (reader: StringReader): void => {
			reader.readNegationCharacter();
			reader.readUnquotedString();
		},
		type: (reader: StringReader): void => {
			this.suggestions = this.suggestionGenerator(reader, "type");
			let start: number = reader.getCursor();
			let shouldInvert: boolean = reader.readNegationCharacter();
			if(this.typeInverse && !shouldInvert) {
				reader.setCursor(start);
				throw new SimpleCommandExceptionType(new LiteralMessage(`Option 'type' isn't applicable here`)).createWithContext(reader);
			}
			if(shouldInvert) {
				this.typeInverse = true;
			}
			if(reader.readTagCharacter()) {
				// Yay, we've got a tag. Read everything matching 0 - 9, a - z, _, :, /, . or -
				reader.readResourceLocation();
			} else {
				const [namespace, key]: [string, string] = reader.readResourceLocation();
				if(!entityTypes.includes(key as EntityType)) { // Uh... yes... this totally won't fail...
					throw new SimpleCommandExceptionType(new LiteralMessage(`${key} is not a valid entity type`)).createWithContext(reader);
				}
				if("player" === (key as EntityType) && !shouldInvert ) {
					this.includesEntities = true;
				}
				if(!shouldInvert) {
					this.entityType = key as EntityType;
				}
			}
		},
		tag: (reader: StringReader): void => {
			reader.readNegationCharacter();
			reader.readUnquotedString();
		},
		nbt: (reader: StringReader): void => {
			const start: number = reader.getCursor();
			const shouldInvert: boolean = reader.readNegationCharacter();
			try {
				reader.readNBT();
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
				reader.readMinMaxBounds();
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
			this.suggestions = SuggestionsHelper.suggestMatching(reader, [...Object.keys(this.Options).map(x => `${x}=`)]); // TODO: So this isn't exactly correct, we need to not list existing names, but that'll require a bit of a refactor
			reader.skipWhitespace();
			while(reader.canRead() && reader.peek() !== "]") {
				reader.skipWhitespace();
				let start: number = reader.getCursor();
				let optionsType: OptionsType = reader.readString() as OptionsType;

				let modifier: Modifier = this.Options[optionsType];
				if(modifier === null) {
					reader.setCursor(start);
					throw new SimpleCommandExceptionType(new LiteralMessage(`Unknown option '${optionsType}'`)).createWithContext(reader);
				}

				reader.skipWhitespace();
				if(!reader.canRead() || reader.peek() !== "=") {
					reader.setCursor(start);
					throw new SimpleCommandExceptionType(new LiteralMessage(`Expected value for option '${optionsType}'`)).createWithContext(reader);
				}
				reader.skip();
				reader.skipWhitespace();
				this.suggestions = [];
				modifier(reader);
				reader.skipWhitespace();
				this.suggestions = [",", "]"];
				if(!reader.canRead()) {
					continue;
				}
				if(reader.peek() === ",") {
					reader.skip();
					this.suggestions = SuggestionsHelper.suggestMatching(reader, [...Object.keys(this.Options)].map(x => `${x}=`)); // TODO: So this isn't exactly correct, we need to not list existing names, but that'll require a bit of a refactor
					continue;
				}
				if(reader.peek() !== "]") {
					throw new SimpleCommandExceptionType(new LiteralMessage("Expected end of options")).createWithContext(reader);
				}
			}
			if (reader.canRead()) {
				reader.skip();
				this.suggestions = [];
				return;
			}
			throw new SimpleCommandExceptionType(new LiteralMessage("Expected end of options")).createWithContext(reader);
		}

		const parseSelector: () => void = () => {
			this.suggestionsModifier = (builder: SuggestionsBuilder) => { builder.createOffset(builder.getStart() - 1) };
			this.suggestions = ["@p", "@a", "@r", "@s", "@e"];
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
			this.suggestionsModifier = null;
			this.suggestions = ["["];
			if (reader.canRead() && reader.peek() === "[") {
				reader.skip();
				this.suggestionsModifier = null;
				this.suggestions = ["]", ...Object.keys(this.Options).map(x => `${x}=`)]; // TODO: So this isn't exactly correct, we need to not list existing names, but that'll require a bit of a refactor
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
		// For some reason, setting these suggestions here triggers twice(?), so don't do that.
		// this.suggestions = ["@p", "@a", "@r", "@s", "@e", "Skepter"];// TODO: Name or selector
		if(reader.canRead() && reader.peek() === "@") {
			reader.skip();
			parseSelector();
		} else {
			parseNameOrUUID();
		}

		// Final checks...
		if(this.maxResults > 1 && this.single) {
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

	public listSuggestions(_context: CommandContext<any>, builder: SuggestionsBuilder): Promise<Suggestions> {
		try {
			this.parse(new StringReader(builder.getInput() as string))
		} catch(exception) {

		}

		if(this.suggestions.length > 0) {

			if(this.suggestionsModifier !== null) {
				this.suggestionsModifier(builder);
			}

			// We don't use HelperSuggestionProvider because we've already
			// pre-generated suggestions for the specific context, so we don't
			// need to check if it matches or anything, just suggest it!
			for (let suggestion of this.suggestions) {
				builder.suggest(suggestion);
			}
			return builder.buildPromise();
		} else {
			return Suggestions.empty();
		}
	}

	public getExamples(): string[] {
		return ["dd12be42-52a9-4a91-a8a1-11c01849e498"];
	}
}