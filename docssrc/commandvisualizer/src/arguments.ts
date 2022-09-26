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
import { Argument } from "webpack";

class ExtendedStringReader extends StringReader {

    public readLocationLiteral(): number {

        function isAllowedLocationLiteral(c: string) {
            return c == '~' || c == '^';
        }
    
        let start = this.getCursor();
        while (this.canRead() && (StringReader.isAllowedNumber(this.peek()) || isAllowedLocationLiteral(this.peek()))) {
            this.skip();
        }
        let number = this.getString().substring(start, this.getCursor());
        if (number.length === 0) {
            throw (CommandSyntaxException as any).BUILT_IN_EXCEPTIONS.readerExpectedInt().createWithContext(this);
        }
    
        if(number.startsWith("~") || number.startsWith("^")) {
            if(number.length === 1) {
                // Accept.
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
    }

}

/**
 * Helper for generating Promise<Suggestions>, from SharedSuggestionProvider.java
 */
class HelperSuggestionProvider {

	public static suggest(suggestions: string[], builder: SuggestionsBuilder): Promise<Suggestions> {
		let remainingLowercase: string = builder.getRemaining().toLowerCase();
		for(let suggestion of suggestions) {
			if(HelperSuggestionProvider.matchesSubStr(remainingLowercase, suggestion.toLowerCase())) {
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

/**
 * @extends {ArgumentType}
 */
export class TimeArgument implements ArgumentType<TimeArgument> {

	static UNITS: Map<string, number> = new Map([
		["d", 24000],
		["s", 20],
		["t", 1],
		["", 1]
	]);

	private ticks: number;

	constructor(ticks: number = 0) {
		this.ticks = ticks;
	}

	public /* override */ parse(reader: StringReader): TimeArgument {
		const numericalValue: number = reader.readFloat();
		const unit: string = reader.readUnquotedString();
		const unitMultiplier: number = TimeArgument.UNITS.get(unit);
		if(unitMultiplier === 0) {
			throw new SimpleCommandExceptionType(new LiteralMessage(`Invalid unit "${unit}"`)).createWithContext(reader);
		}
		const ticks: number = Math.round(numericalValue * unitMultiplier);
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
	public listSuggestions(context: CommandContext<any>, builder: SuggestionsBuilder): Promise<Suggestions> {
		let reader: StringReader = new StringReader(builder.getRemaining());
		try {
			reader.readFloat();
		} catch(ex) {
			return builder.buildPromise();
		}
		return HelperSuggestionProvider.suggest([...TimeArgument.UNITS.keys()], builder.createOffset(builder.getStart() + reader.getCursor()));
	}

	public getExamples(): string[] {
		return ["0d", "0s", "0t", "0"];
	}
}

export class BlockPosArgument implements ArgumentType<BlockPosArgument> {

	private x: number;
	private y: number;
	private z: number;

	constructor(x: number = 0, y: number = 0, z: number = 0) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public parse(reader: ExtendedStringReader): BlockPosArgument {
		this.x = reader.readLocationLiteral();
		reader.skip();
		this.y = reader.readLocationLiteral();
		reader.skip();
		this.z = reader.readLocationLiteral();
		return this;
	}

	public listSuggestions(context: CommandContext<any>, builder: SuggestionsBuilder): Promise<Suggestions> {
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

	private x: number;
	private z: number;

	constructor(x = 0, z = 0) {
		this.x = x;
		this.z = z;
	}

	public parse(reader: ExtendedStringReader): ColumnPosArgument {
		this.x = reader.readLocationLiteral();
		reader.skip();
		this.z = reader.readLocationLiteral();
		return this;
	}

	public listSuggestions(context: CommandContext<any>, builder: SuggestionsBuilder): Promise<Suggestions> {
		builder.suggest("~");
		builder.suggest("~ ~");
		return builder.buildPromise();
	}

	public getExamples(): string[] {
		return ["1 2"];
	}
}

export class PlayerArgument implements ArgumentType<PlayerArgument> {

	private username: string;

	constructor(username: string = "") {
		this.username = username;
	}

	public parse(reader: StringReader): PlayerArgument {
		const start: number = reader.getCursor();
		while(reader.canRead() && reader.peek() !== " ") {
			reader.skip();
		}

		const string: string = reader.getString();
		const currentCursor: number = reader.getCursor();

		this.username = string.slice(start, currentCursor);

		if(!this.username.match(/^[A-Za-z0-9_]{2,16}$/)) {
			throw new SimpleCommandExceptionType(new LiteralMessage(this.username + " is not a valid username")).createWithContext(reader);
		}

		return this;
	}

	public listSuggestions(context: CommandContext<any>, builder: SuggestionsBuilder): Promise<Suggestions> {
		return Suggestions.empty();
	}

	public getExamples(): string[] {
		return ["Skepter"];
	}
}

export class MultiLiteralArgument implements ArgumentType<MultiLiteralArgument> {

	private literals: string[];
	private selectedLiteral: string;

	 constructor(literals: string[]) {
		this.literals = literals;
		this.selectedLiteral = "";
	}

	public parse(reader: StringReader) {
		const start: number = reader.getCursor();
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

	public listSuggestions(context: CommandContext<any>, builder: SuggestionsBuilder): Promise<Suggestions> {
		for(let literal of this.literals) {
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

	private chatcolor: string;

	constructor(chatcolor: string = null) {
		this.chatcolor = chatcolor;
	}

	public parse(reader: StringReader) {
		let input = reader.readUnquotedString();
		let chatFormat: string = ColorArgument.ChatColor[input.toLowerCase()];
		if(chatFormat === undefined) {
			throw new SimpleCommandExceptionType(new LiteralMessage(`Unknown colour '${input}'`)).createWithContext(reader);
		}
		this.chatcolor = chatFormat;
		return this;
	}

	 public listSuggestions(context: CommandContext<any>, builder: SuggestionsBuilder): Promise<Suggestions> {
		return HelperSuggestionProvider.suggest(Object.keys(ColorArgument.ChatColor), builder);
	}

	public getExamples(): string[] {
		return ["red", "green"];
	}
}