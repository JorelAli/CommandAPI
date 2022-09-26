/******/ (() => { // webpackBootstrap
/******/ 	"use strict";
/******/ 	var __webpack_modules__ = ({

/***/ "./node_modules/node-brigadier/dist/index.js":
/*!***************************************************!*\
  !*** ./node_modules/node-brigadier/dist/index.js ***!
  \***************************************************/
/***/ (function(module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (Object.hasOwnProperty.call(mod, k)) result[k] = mod[k];
    result["default"] = mod;
    return result;
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const CommandDispatcher_1 = __importDefault(__webpack_require__(/*! ./lib/CommandDispatcher */ "./node_modules/node-brigadier/dist/lib/CommandDispatcher.js"));
const LiteralMessage_1 = __importDefault(__webpack_require__(/*! ./lib/LiteralMessage */ "./node_modules/node-brigadier/dist/lib/LiteralMessage.js"));
const ParseResults_1 = __importDefault(__webpack_require__(/*! ./lib/ParseResults */ "./node_modules/node-brigadier/dist/lib/ParseResults.js"));
const StringReader_1 = __importDefault(__webpack_require__(/*! ./lib/StringReader */ "./node_modules/node-brigadier/dist/lib/StringReader.js"));
const ArgumentType_1 = __webpack_require__(/*! ./lib/arguments/ArgumentType */ "./node_modules/node-brigadier/dist/lib/arguments/ArgumentType.js");
const LiteralArgumentBuilder_1 = __importStar(__webpack_require__(/*! ./lib/builder/LiteralArgumentBuilder */ "./node_modules/node-brigadier/dist/lib/builder/LiteralArgumentBuilder.js"));
const RequiredArgumentBuilder_1 = __importStar(__webpack_require__(/*! ./lib/builder/RequiredArgumentBuilder */ "./node_modules/node-brigadier/dist/lib/builder/RequiredArgumentBuilder.js"));
const CommandContext_1 = __importDefault(__webpack_require__(/*! ./lib/context/CommandContext */ "./node_modules/node-brigadier/dist/lib/context/CommandContext.js"));
const CommandContextBuilder_1 = __importDefault(__webpack_require__(/*! ./lib/context/CommandContextBuilder */ "./node_modules/node-brigadier/dist/lib/context/CommandContextBuilder.js"));
const ParsedArgument_1 = __importDefault(__webpack_require__(/*! ./lib/context/ParsedArgument */ "./node_modules/node-brigadier/dist/lib/context/ParsedArgument.js"));
const ParsedCommandNode_1 = __importDefault(__webpack_require__(/*! ./lib/context/ParsedCommandNode */ "./node_modules/node-brigadier/dist/lib/context/ParsedCommandNode.js"));
const StringRange_1 = __importDefault(__webpack_require__(/*! ./lib/context/StringRange */ "./node_modules/node-brigadier/dist/lib/context/StringRange.js"));
const SuggestionContext_1 = __importDefault(__webpack_require__(/*! ./lib/context/SuggestionContext */ "./node_modules/node-brigadier/dist/lib/context/SuggestionContext.js"));
const CommandSyntaxException_1 = __importDefault(__webpack_require__(/*! ./lib/exceptions/CommandSyntaxException */ "./node_modules/node-brigadier/dist/lib/exceptions/CommandSyntaxException.js"));
const DynamicCommandExceptionType_1 = __importDefault(__webpack_require__(/*! ./lib/exceptions/DynamicCommandExceptionType */ "./node_modules/node-brigadier/dist/lib/exceptions/DynamicCommandExceptionType.js"));
const SimpleCommandExceptionType_1 = __importDefault(__webpack_require__(/*! ./lib/exceptions/SimpleCommandExceptionType */ "./node_modules/node-brigadier/dist/lib/exceptions/SimpleCommandExceptionType.js"));
const Suggestion_1 = __importDefault(__webpack_require__(/*! ./lib/suggestion/Suggestion */ "./node_modules/node-brigadier/dist/lib/suggestion/Suggestion.js"));
const Suggestions_1 = __importDefault(__webpack_require__(/*! ./lib/suggestion/Suggestions */ "./node_modules/node-brigadier/dist/lib/suggestion/Suggestions.js"));
const SuggestionsBuilder_1 = __importDefault(__webpack_require__(/*! ./lib/suggestion/SuggestionsBuilder */ "./node_modules/node-brigadier/dist/lib/suggestion/SuggestionsBuilder.js"));
const ArgumentCommandNode_1 = __importDefault(__webpack_require__(/*! ./lib/tree/ArgumentCommandNode */ "./node_modules/node-brigadier/dist/lib/tree/ArgumentCommandNode.js"));
const LiteralCommandNode_1 = __importDefault(__webpack_require__(/*! ./lib/tree/LiteralCommandNode */ "./node_modules/node-brigadier/dist/lib/tree/LiteralCommandNode.js"));
const RootCommandNode_1 = __importDefault(__webpack_require__(/*! ./lib/tree/RootCommandNode */ "./node_modules/node-brigadier/dist/lib/tree/RootCommandNode.js"));
const { word, string, greedyString, bool, integer, float } = ArgumentType_1.DefaultType;
module.exports = {
    dispatcher: new CommandDispatcher_1.default(),
    word, string, greedyString, bool, integer, float,
    literal: LiteralArgumentBuilder_1.literal, argument: RequiredArgumentBuilder_1.argument,
    CommandDispatcher: CommandDispatcher_1.default,
    LiteralMessage: LiteralMessage_1.default,
    ParseResults: ParseResults_1.default,
    StringReader: StringReader_1.default,
    LiteralArgumentBuilder: LiteralArgumentBuilder_1.default,
    RequiredArgumentBuilder: RequiredArgumentBuilder_1.default,
    CommandContext: CommandContext_1.default,
    CommandContextBuilder: CommandContextBuilder_1.default,
    ParsedArgument: ParsedArgument_1.default,
    ParsedCommandNode: ParsedCommandNode_1.default,
    StringRange: StringRange_1.default,
    SuggestionsContext: SuggestionContext_1.default,
    CommandSyntaxException: CommandSyntaxException_1.default,
    SimpleCommandExceptionType: SimpleCommandExceptionType_1.default,
    DynamicCommandExceptionType: DynamicCommandExceptionType_1.default,
    Suggestion: Suggestion_1.default,
    Suggestions: Suggestions_1.default,
    SuggestionsBuilder: SuggestionsBuilder_1.default,
    ArgumentCommandNode: ArgumentCommandNode_1.default,
    LiteralCommandNode: LiteralCommandNode_1.default,
    RootCommandNode: RootCommandNode_1.default
};


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/CommandDispatcher.js":
/*!*******************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/CommandDispatcher.js ***!
  \*******************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const ParseResults_1 = __importDefault(__webpack_require__(/*! ./ParseResults */ "./node_modules/node-brigadier/dist/lib/ParseResults.js"));
const CommandContextBuilder_1 = __importDefault(__webpack_require__(/*! ./context/CommandContextBuilder */ "./node_modules/node-brigadier/dist/lib/context/CommandContextBuilder.js"));
const CommandSyntaxException_1 = __importDefault(__webpack_require__(/*! ./exceptions/CommandSyntaxException */ "./node_modules/node-brigadier/dist/lib/exceptions/CommandSyntaxException.js"));
const Suggestions_1 = __importDefault(__webpack_require__(/*! ./suggestion/Suggestions */ "./node_modules/node-brigadier/dist/lib/suggestion/Suggestions.js"));
const SuggestionsBuilder_1 = __importDefault(__webpack_require__(/*! ./suggestion/SuggestionsBuilder */ "./node_modules/node-brigadier/dist/lib/suggestion/SuggestionsBuilder.js"));
const RootCommandNode_1 = __importDefault(__webpack_require__(/*! ./tree/RootCommandNode */ "./node_modules/node-brigadier/dist/lib/tree/RootCommandNode.js"));
const StringReader_1 = __importDefault(__webpack_require__(/*! ./StringReader */ "./node_modules/node-brigadier/dist/lib/StringReader.js"));
const ARGUMENT_SEPARATOR = " ";
const USAGE_OPTIONAL_OPEN = "[";
const USAGE_OPTIONAL_CLOSE = "]";
const USAGE_REQUIRED_OPEN = "(";
const USAGE_REQUIRED_CLOSE = ")";
const USAGE_OR = "|";
class CommandDispatcher {
    constructor(root = null) {
        this.consumer = {
            onCommandComplete() { }
        };
        this.root = root || new RootCommandNode_1.default();
    }
    register(command) {
        let build = command.build();
        this.root.addChild(build);
        return build;
    }
    setConsumer(consumer) {
        this.consumer = consumer;
    }
    execute(input, source = null) {
        if (typeof input === "string")
            input = new StringReader_1.default(input);
        let parse;
        if (input instanceof StringReader_1.default) {
            if (!(source == null))
                parse = this.parse(input, source);
        }
        else
            parse = input;
        if (parse.getReader().canRead()) {
            if (parse.getExceptions().size === 1) {
                throw parse.getExceptions().values().next().value;
            }
            else if (parse.getContext().getRange().isEmpty()) {
                throw CommandSyntaxException_1.default.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
            }
            else {
                throw CommandSyntaxException_1.default.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(parse.getReader());
            }
        }
        let result = 0;
        let successfulForks = 0;
        let forked = false;
        let foundCommand = false;
        let command = parse.getReader().getString();
        let original = parse.getContext().build(command);
        let contexts = [];
        contexts.push(original);
        let next = null;
        while (!(contexts == null)) {
            for (let i = 0; i < contexts.length; i++) {
                let context = contexts[i];
                let child = context.getChild();
                if (!(child == null)) {
                    forked = forked || context.isForked();
                    if (child.hasNodes()) {
                        foundCommand = true;
                        let modifier = context.getRedirectModifier();
                        if (modifier == null) {
                            if (next == null)
                                next = [];
                            next.push(child.copyFor(context.getSource()));
                        }
                        else {
                            try {
                                let results = modifier.apply(context);
                                if (results.length !== 0) {
                                    if (next == null)
                                        next = [];
                                    for (let source of results) {
                                        next.push(child.copyFor(source));
                                    }
                                }
                            }
                            catch (ex) {
                                this.consumer.onCommandComplete(context, false, 0);
                                if (!forked)
                                    throw ex;
                            }
                        }
                    }
                }
                else if (context.getCommand() != null) {
                    foundCommand = true;
                    try {
                        let value = context.getCommand()(context);
                        result += value;
                        this.consumer.onCommandComplete(context, true, value);
                        successfulForks++;
                    }
                    catch (ex) {
                        this.consumer.onCommandComplete(context, false, 0);
                        if (!forked)
                            throw ex;
                    }
                }
            }
            contexts = next;
            next = null;
        }
        if (!foundCommand) {
            this.consumer.onCommandComplete(original, false, 0);
            throw CommandSyntaxException_1.default.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
        }
        return forked ? successfulForks : result;
    }
    parse(command, source) {
        if (typeof command === "string")
            command = new StringReader_1.default(command);
        let context = new CommandContextBuilder_1.default(this, source, this.root, command.getCursor());
        return this.parseNodes(this.root, command, context);
    }
    parseNodes(node, originalReader, contextSoFar) {
        let source = contextSoFar.getSource();
        let errors = null;
        let potentials = null;
        let cursor = originalReader.getCursor();
        for (let child of node.getRelevantNodes(originalReader)) {
            if (!child.canUse(source))
                continue;
            let context = contextSoFar.copy();
            let reader = new StringReader_1.default(originalReader);
            try {
                child.parse(reader, context);
                if (reader.canRead())
                    if (reader.peek() != ARGUMENT_SEPARATOR)
                        throw CommandSyntaxException_1.default.BUILT_IN_EXCEPTIONS.dispatcherExpectedArgumentSeparator().createWithContext(reader);
            }
            catch (ex) {
                if (errors == null) {
                    errors = new Map();
                }
                errors.set(child, ex);
                reader.setCursor(cursor);
                continue;
            }
            context.withCommand(child.getCommand());
            if (reader.canRead(child.getRedirect() == null ? 2 : 1)) {
                reader.skip();
                if (!(child.getRedirect() == null)) {
                    let childContext = new CommandContextBuilder_1.default(this, source, child.getRedirect(), reader.getCursor());
                    let parse = this.parseNodes(child.getRedirect(), reader, childContext);
                    context.withChild(parse.getContext());
                    return new ParseResults_1.default(context, parse.getReader(), parse.getExceptions());
                }
                else {
                    let parse = this.parseNodes(child, reader, context);
                    if (potentials == null) {
                        potentials = [];
                    }
                    potentials.push(parse);
                }
            }
            else {
                if (potentials == null) {
                    potentials = [];
                }
                potentials.push(new ParseResults_1.default(context, reader, new Map()));
            }
        }
        if (!(potentials == null)) {
            if (potentials.length > 1) {
                potentials.sort((a, b) => {
                    if (!a.getReader().canRead() && b.getReader().canRead()) {
                        return -1;
                    }
                    if (a.getReader().canRead() && !b.getReader().canRead()) {
                        return 1;
                    }
                    if (a.getExceptions().size === 0 && b.getExceptions().size !== 0) {
                        return -1;
                    }
                    if (a.getExceptions().size !== 0 && b.getExceptions().size === 0) {
                        return 1;
                    }
                    return 0;
                });
            }
            return potentials[0];
        }
        return new ParseResults_1.default(contextSoFar, originalReader, errors == null ? new Map() : errors);
    }
    getAllUsage(node, source, restricted) {
        const result = [];
        this.__getAllUsage(node, source, result, "", restricted);
        return result;
    }
    __getAllUsage(node, source, result, prefix = "", restricted) {
        if (restricted && !node.canUse(source)) {
            return;
        }
        if (node.getCommand() != null) {
            result.push(prefix);
        }
        if (node.getRedirect() != null) {
            const redirect = node.getRedirect() === this.root ? "..." : "-> " + node.getRedirect().getUsageText();
            result.push(prefix.length === 0 ? node.getUsageText() + ARGUMENT_SEPARATOR + redirect : prefix + ARGUMENT_SEPARATOR + redirect);
        }
        else if (node.getChildrenCount() > 0) {
            for (let child of node.getChildren()) {
                this.__getAllUsage(child, source, result, prefix.length === 0 ? child.getUsageText() : prefix + ARGUMENT_SEPARATOR + child.getUsageText(), restricted);
            }
        }
    }
    getSmartUsage(node, source) {
        let result = new Map();
        let optional = node.getCommand() !== null;
        for (let child of node.getChildren()) {
            let usage = this.__getSmartUsage(child, source, optional, false);
            if (!(usage == null)) {
                result.set(child, usage);
            }
        }
        return result;
    }
    __getSmartUsage(node, source, optional, deep) {
        if (!node.canUse(source)) {
            return null;
        }
        let self = optional ? USAGE_OPTIONAL_OPEN + node.getUsageText() + USAGE_OPTIONAL_CLOSE : node.getUsageText();
        let childOptional = node.getCommand() != null;
        let open = childOptional ? USAGE_OPTIONAL_OPEN : USAGE_REQUIRED_OPEN;
        let close = childOptional ? USAGE_OPTIONAL_CLOSE : USAGE_REQUIRED_CLOSE;
        if (!deep) {
            if ((node.getRedirect() != null)) {
                let redirect = node.getRedirect() == this.root ? "..." : "-> " + node.getRedirect().getUsageText();
                return self + ARGUMENT_SEPARATOR + redirect;
            }
            else {
                let children = [...node.getChildren()].filter(c => c.canUse(source));
                if ((children.length == 1)) {
                    let usage = this.__getSmartUsage(children[0], source, childOptional, childOptional);
                    if (!(usage == null)) {
                        return self + ARGUMENT_SEPARATOR + usage;
                    }
                }
                else if (children.length > 1) {
                    let childUsage = new Set();
                    for (let child of children) {
                        let usage = this.__getSmartUsage(child, source, childOptional, true);
                        if (!(usage == null)) {
                            childUsage.add(usage);
                        }
                    }
                    if (childUsage.size === 1) {
                        let usage = childUsage.values().next().value;
                        return self + ARGUMENT_SEPARATOR + (childOptional ? USAGE_OPTIONAL_OPEN + usage + USAGE_OPTIONAL_CLOSE : usage);
                    }
                    else if (childUsage.size > 1) {
                        let builder = open;
                        let count = 0;
                        for (let child of children) {
                            if (count > 0) {
                                builder += USAGE_OR;
                            }
                            builder += child.getUsageText();
                            count++;
                        }
                        if (count > 0) {
                            builder += close;
                            return self + ARGUMENT_SEPARATOR + builder;
                        }
                    }
                }
            }
        }
        return self;
    }
    getCompletionSuggestions(parse, cursor = parse.getReader().getTotalLength()) {
        return __awaiter(this, void 0, void 0, function* () {
            let context = parse.getContext();
            let nodeBeforeCursor = context.findSuggestionContext(cursor);
            let parent = nodeBeforeCursor.parent;
            let start = Math.min(nodeBeforeCursor.startPos, cursor);
            let fullInput = parse.getReader().getString();
            let truncatedInput = fullInput.substring(0, cursor);
            let futures = [];
            for (let node of parent.getChildren()) {
                let future = yield Suggestions_1.default.empty();
                try {
                    future = yield node.listSuggestions(context.build(truncatedInput), new SuggestionsBuilder_1.default(truncatedInput, start));
                }
                catch (ignored) {
                }
                futures.push(future);
            }
            return Promise.resolve(Suggestions_1.default.merge(fullInput, futures));
        });
    }
    getRoot() {
        return this.root;
    }
    getPath(target) {
        let nodes = [];
        this.addPaths(this.root, nodes, []);
        for (let list of nodes) {
            if (list[list.length - 1] === target) {
                let result = [];
                for (let node of list) {
                    if (node !== this.root) {
                        result.push(node.getName());
                    }
                }
                return result;
            }
        }
        return [];
    }
    findNode(path) {
        let node = this.root;
        for (let name of path) {
            node = node.getChild(name);
            if (node == null)
                return null;
        }
        return node;
    }
    findAmbiguities(consumer) {
        this.root.findAmbiguities(consumer);
    }
    addPaths(node, result, parents) {
        let current = [];
        current.push(...parents);
        current.push(node);
        result.push(current);
        for (let child of node.getChildren())
            this.addPaths(child, result, current);
    }
}
exports["default"] = CommandDispatcher;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/LiteralMessage.js":
/*!****************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/LiteralMessage.js ***!
  \****************************************************************/
/***/ ((__unused_webpack_module, exports) => {


Object.defineProperty(exports, "__esModule", ({ value: true }));
class LiteralMessage {
    constructor(str) {
        this.str = str;
    }
    getString() {
        return this.str;
    }
    toString() {
        return this.str;
    }
}
exports["default"] = LiteralMessage;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/ParseResults.js":
/*!**************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/ParseResults.js ***!
  \**************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const StringReader_1 = __importDefault(__webpack_require__(/*! ./StringReader */ "./node_modules/node-brigadier/dist/lib/StringReader.js"));
class ParseResults {
    constructor(context, reader, exceptions) {
        this.context = context;
        this.reader = reader || new StringReader_1.default("");
        this.exceptions = exceptions || new Map();
    }
    getContext() {
        return this.context;
    }
    getReader() {
        return this.reader;
    }
    getExceptions() {
        return this.exceptions;
    }
}
exports["default"] = ParseResults;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/StringReader.js":
/*!**************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/StringReader.js ***!
  \**************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const CommandSyntaxException_1 = __importDefault(__webpack_require__(/*! ./exceptions/CommandSyntaxException */ "./node_modules/node-brigadier/dist/lib/exceptions/CommandSyntaxException.js"));
const SYNTAX_ESCAPE = '\\';
const SYNTAX_QUOTE = '\"';
class StringReader {
    constructor(other) {
        this.cursor = 0;
        if (typeof other === "string") {
            this.string = other;
        }
        else {
            this.string = other.string;
            this.cursor = other.cursor;
        }
    }
    getString() {
        return this.string;
    }
    setCursor(cursor) {
        this.cursor = cursor;
    }
    getRemainingLength() {
        return (this.string.length - this.cursor);
    }
    getTotalLength() {
        return this.string.length;
    }
    getCursor() {
        return this.cursor;
    }
    getRead() {
        return this.string.substring(0, this.cursor);
    }
    getRemaining() {
        return this.string.substring(this.cursor);
    }
    canRead(length = 1) {
        return this.cursor + length <= this.string.length;
    }
    peek(offset = 0) {
        return this.string.charAt(this.cursor + offset);
    }
    read() {
        return this.string.charAt(this.cursor++);
    }
    skip() {
        this.cursor++;
    }
    static isAllowedNumber(c) {
        return c >= '0' && c <= '9' || c == '.' || c == '-' || c == '+' || c == 'e' || c == 'E';
    }
    skipWhitespace() {
        while ((this.canRead() && /\s/.test(this.peek()))) {
            this.skip();
        }
    }
    readInt() {
        let start = this.cursor;
        while (this.canRead() && StringReader.isAllowedNumber(this.peek())) {
            this.skip();
        }
        let number = this.string.substring(start, this.cursor);
        if (number.length === 0) {
            throw CommandSyntaxException_1.default.BUILT_IN_EXCEPTIONS.readerExpectedInt().createWithContext(this);
        }
        const result = parseInt(number);
        if (isNaN(result) || result !== parseFloat(number)) {
            this.cursor = start;
            throw CommandSyntaxException_1.default.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(this, number);
        }
        else
            return result;
    }
    readFloat() {
        let start = this.cursor;
        while ((this.canRead() && StringReader.isAllowedNumber(this.peek()))) {
            this.skip();
        }
        let number = this.string.substring(start, this.cursor);
        if (number.length === 0) {
            throw CommandSyntaxException_1.default.BUILT_IN_EXCEPTIONS.readerExpectedFloat().createWithContext(this);
        }
        const result = parseFloat(number);
        const strictParseFloatTest = parseFloat(number.substring(result.toString().length, this.cursor));
        if (isNaN(result) || (!isNaN(strictParseFloatTest) &&
            strictParseFloatTest !== 0)) {
            this.cursor = start;
            throw CommandSyntaxException_1.default.BUILT_IN_EXCEPTIONS.readerInvalidFloat().createWithContext(this, number);
        }
        else
            return result;
    }
    static isAllowedInUnquotedString(c) {
        return c >= '0' && c <= '9'
            || c >= 'A' && c <= 'Z'
            || c >= 'a' && c <= 'z'
            || c == '_' || c == '-'
            || c == '.' || c == '+';
    }
    readUnquotedString() {
        let start = this.cursor;
        while (this.canRead() && StringReader.isAllowedInUnquotedString(this.peek())) {
            this.skip();
        }
        return this.string.substring(start, this.cursor);
    }
    readQuotedString() {
        if (!this.canRead()) {
            return "";
        }
        else if ((this.peek() != SYNTAX_QUOTE)) {
            throw CommandSyntaxException_1.default.BUILT_IN_EXCEPTIONS.readerExpectedStartOfQuote().createWithContext(this);
        }
        this.skip();
        let result = "";
        let escaped = false;
        while (this.canRead()) {
            let c = this.read();
            if (escaped) {
                if (c == SYNTAX_QUOTE || c == SYNTAX_ESCAPE) {
                    result += c;
                    escaped = false;
                }
                else {
                    this.setCursor(this.getCursor() - 1);
                    throw CommandSyntaxException_1.default.BUILT_IN_EXCEPTIONS.readerInvalidEscape().createWithContext(this, c);
                }
            }
            else if (c == SYNTAX_ESCAPE) {
                escaped = true;
            }
            else if (c == SYNTAX_QUOTE) {
                return result;
            }
            else {
                result += c;
            }
        }
        throw CommandSyntaxException_1.default.BUILT_IN_EXCEPTIONS.readerExpectedEndOfQuote().createWithContext(this);
    }
    readString() {
        if (this.canRead() && (this.peek() === SYNTAX_QUOTE)) {
            return this.readQuotedString();
        }
        else {
            return this.readUnquotedString();
        }
    }
    readBoolean() {
        let start = this.cursor;
        let value = this.readString();
        if (value.length === 0) {
            throw CommandSyntaxException_1.default.BUILT_IN_EXCEPTIONS.readerExpectedBool().createWithContext(this);
        }
        if (value === "true") {
            return true;
        }
        else if (value === "false") {
            return false;
        }
        else {
            this.cursor = start;
            throw CommandSyntaxException_1.default.BUILT_IN_EXCEPTIONS.readerInvalidBool().createWithContext(this, value);
        }
    }
    expect(c) {
        if (!this.canRead() || this.peek() !== c) {
            throw CommandSyntaxException_1.default.BUILT_IN_EXCEPTIONS.readerExpectedSymbol().createWithContext(this, c);
        }
        this.skip();
    }
}
exports["default"] = StringReader;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/arguments/ArgumentType.js":
/*!************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/arguments/ArgumentType.js ***!
  \************************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const BoolArgumentType_1 = __importDefault(__webpack_require__(/*! ./BoolArgumentType */ "./node_modules/node-brigadier/dist/lib/arguments/BoolArgumentType.js"));
const IntegerArgumentType_1 = __importDefault(__webpack_require__(/*! ./IntegerArgumentType */ "./node_modules/node-brigadier/dist/lib/arguments/IntegerArgumentType.js"));
const FloatArgumentType_1 = __importDefault(__webpack_require__(/*! ./FloatArgumentType */ "./node_modules/node-brigadier/dist/lib/arguments/FloatArgumentType.js"));
const StringArgumentType_1 = __importDefault(__webpack_require__(/*! ./StringArgumentType */ "./node_modules/node-brigadier/dist/lib/arguments/StringArgumentType.js"));
exports.DefaultType = {
    bool: BoolArgumentType_1.default.bool,
    integer: IntegerArgumentType_1.default.integer,
    float: FloatArgumentType_1.default.float,
    word: StringArgumentType_1.default.word,
    string: StringArgumentType_1.default.string,
    greedyString: StringArgumentType_1.default.greedyString
};


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/arguments/BoolArgumentType.js":
/*!****************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/arguments/BoolArgumentType.js ***!
  \****************************************************************************/
/***/ ((__unused_webpack_module, exports) => {


Object.defineProperty(exports, "__esModule", ({ value: true }));
const EXAMPLES = ["true", "false"];
class BoolArgumentType {
    constructor() {
    }
    static bool() {
        return new BoolArgumentType();
    }
    static getBool(context, name) {
        return context.getArgument(name, Boolean);
    }
    parse(reader) {
        return reader.readBoolean();
    }
    listSuggestions(context, builder) {
        if ("true".startsWith(builder.getRemaining().toLowerCase())) {
            builder.suggest("true");
        }
        if ("false".startsWith(builder.getRemaining().toLowerCase())) {
            builder.suggest("false");
        }
        return builder.buildPromise();
    }
    getExamples() {
        return EXAMPLES;
    }
}
exports["default"] = BoolArgumentType;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/arguments/FloatArgumentType.js":
/*!*****************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/arguments/FloatArgumentType.js ***!
  \*****************************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const CommandSyntaxException_1 = __importDefault(__webpack_require__(/*! ../exceptions/CommandSyntaxException */ "./node_modules/node-brigadier/dist/lib/exceptions/CommandSyntaxException.js"));
const EXAMPLES = ["0", "1.2", ".5", "-1", "-.5", "-1234.56"];
class FloatArgumentType {
    constructor(minimum, maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }
    static float(min = -Infinity, max = Infinity) {
        return new FloatArgumentType(min, max);
    }
    static getFloat(context, name) {
        return context.getArgument(name, Number);
    }
    getMinimum() {
        return this.minimum;
    }
    getMaximum() {
        return this.maximum;
    }
    parse(reader) {
        let start = reader.getCursor();
        let result = reader.readFloat();
        if (result < this.minimum) {
            reader.setCursor(start);
            throw CommandSyntaxException_1.default.BUILT_IN_EXCEPTIONS.integerTooLow().createWithContext(reader, result, this.minimum);
        }
        if (result > this.maximum) {
            reader.setCursor(start);
            throw CommandSyntaxException_1.default.BUILT_IN_EXCEPTIONS.integerTooHigh().createWithContext(reader, result, this.maximum);
        }
        return result;
    }
    equals(o) {
        if (this === o)
            return true;
        if (!(o instanceof FloatArgumentType))
            return false;
        return this.maximum == o.maximum && this.minimum == o.minimum;
    }
    toString() {
        if (this.minimum === -Infinity && this.maximum === Infinity) {
            return "float()";
        }
        else if (this.maximum == Infinity) {
            return "float(" + this.minimum + ")";
        }
        else {
            return "float(" + this.minimum + ", " + this.maximum + ")";
        }
    }
    getExamples() {
        return EXAMPLES;
    }
}
exports["default"] = FloatArgumentType;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/arguments/IntegerArgumentType.js":
/*!*******************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/arguments/IntegerArgumentType.js ***!
  \*******************************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const CommandSyntaxException_1 = __importDefault(__webpack_require__(/*! ../exceptions/CommandSyntaxException */ "./node_modules/node-brigadier/dist/lib/exceptions/CommandSyntaxException.js"));
const EXAMPLES = ["0", "123", "-123"];
class IntegerArgumentType {
    constructor(minimum, maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }
    static integer(min = -Infinity, max = Infinity) {
        return new IntegerArgumentType(min, max);
    }
    static getInteger(context, name) {
        return context.getArgument(name, Number);
    }
    getMinimum() {
        return this.minimum;
    }
    getMaximum() {
        return this.maximum;
    }
    parse(reader) {
        let start = reader.getCursor();
        let result = reader.readInt();
        if (result < this.minimum) {
            reader.setCursor(start);
            throw CommandSyntaxException_1.default.BUILT_IN_EXCEPTIONS.integerTooLow().createWithContext(reader, result, this.minimum);
        }
        if (result > this.maximum) {
            reader.setCursor(start);
            throw CommandSyntaxException_1.default.BUILT_IN_EXCEPTIONS.integerTooHigh().createWithContext(reader, result, this.maximum);
        }
        return result;
    }
    equals(o) {
        if (this === o)
            return true;
        if (!(o instanceof IntegerArgumentType))
            return false;
        return this.maximum == o.maximum && this.minimum == o.minimum;
    }
    toString() {
        if (this.minimum === -Infinity && this.maximum === Infinity) {
            return "integer()";
        }
        else if (this.maximum == Infinity) {
            return "integer(" + this.minimum + ")";
        }
        else {
            return "integer(" + this.minimum + ", " + this.maximum + ")";
        }
    }
    getExamples() {
        return EXAMPLES;
    }
}
exports["default"] = IntegerArgumentType;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/arguments/StringArgumentType.js":
/*!******************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/arguments/StringArgumentType.js ***!
  \******************************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const StringReader_1 = __importDefault(__webpack_require__(/*! ../StringReader */ "./node_modules/node-brigadier/dist/lib/StringReader.js"));
var StringType;
(function (StringType) {
    StringType["SINGLE_WORD"] = "words_with_underscores";
    StringType["QUOTABLE_PHRASE"] = "\"quoted phrase\"";
    StringType["GREEDY_PHRASE"] = "words with spaces";
})(StringType = exports.StringType || (exports.StringType = {}));
class StringArgumentType {
    constructor(type) {
        this.type = type;
    }
    static word() {
        return new StringArgumentType(StringType.SINGLE_WORD);
    }
    static string() {
        return new StringArgumentType(StringType.QUOTABLE_PHRASE);
    }
    static greedyString() {
        return new StringArgumentType(StringType.GREEDY_PHRASE);
    }
    static getString(context, name) {
        return context.getArgument(name, String);
    }
    getType() {
        return this.type;
    }
    parse(reader) {
        if (this.type == StringType.GREEDY_PHRASE) {
            let text = reader.getRemaining();
            reader.setCursor(reader.getTotalLength());
            return text;
        }
        else if (this.type == StringType.SINGLE_WORD) {
            return reader.readUnquotedString();
        }
        else {
            return reader.readString();
        }
    }
    toString() {
        return "string()";
    }
    static escapeIfRequired(input) {
        for (let c of input) {
            if (!StringReader_1.default.isAllowedInUnquotedString(c)) {
                return StringArgumentType.escape(input);
            }
        }
        return input;
    }
    static escape(input) {
        let result = "\"";
        for (let i = 0; i < input.length; i++) {
            const c = input.charAt(i);
            if (c == '\\' || c == '"') {
                result += '\\';
            }
            result += c;
        }
        result += "\"";
        return result;
    }
}
exports["default"] = StringArgumentType;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/builder/ArgumentBuilder.js":
/*!*************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/builder/ArgumentBuilder.js ***!
  \*************************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const CommandNode_1 = __importDefault(__webpack_require__(/*! ../tree/CommandNode */ "./node_modules/node-brigadier/dist/lib/tree/CommandNode.js"));
const RootCommandNode_1 = __importDefault(__webpack_require__(/*! ../tree/RootCommandNode */ "./node_modules/node-brigadier/dist/lib/tree/RootCommandNode.js"));
class ArgumentBuilder {
    constructor() {
        this.args = new RootCommandNode_1.default();
        this.modifier = null;
    }
    then(arg) {
        if (!(this.target == null)) {
            throw new Error("Cannot add children to a redirected node");
        }
        if (arg instanceof CommandNode_1.default)
            this.args.addChild(arg);
        else
            this.args.addChild(arg.build());
        return this.getThis();
    }
    getArguments() {
        return this.args.getChildren();
    }
    executes(command) {
        this.command = command;
        return this.getThis();
    }
    getCommand() {
        return this.command;
    }
    requires(requirement) {
        this.requirement = requirement;
        return this.getThis();
    }
    getRequirement() {
        return this.requirement;
    }
    redirect(target, modifier) {
        return this.forward(target, modifier == null ? null : (o) => [modifier.apply(o)], false);
    }
    fork(target, modifier) {
        return this.forward(target, modifier, true);
    }
    forward(target, modifier, fork) {
        if (this.args.getChildrenCount() > 0) {
            throw new Error("Cannot forward a node with children");
        }
        this.target = target;
        this.modifier = modifier;
        this.forks = fork;
        return this.getThis();
    }
    getRedirect() {
        return this.target;
    }
    getRedirectModifier() {
        return this.modifier;
    }
    isFork() {
        return this.forks;
    }
}
exports["default"] = ArgumentBuilder;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/builder/LiteralArgumentBuilder.js":
/*!********************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/builder/LiteralArgumentBuilder.js ***!
  \********************************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const LiteralCommandNode_1 = __importDefault(__webpack_require__(/*! ../tree/LiteralCommandNode */ "./node_modules/node-brigadier/dist/lib/tree/LiteralCommandNode.js"));
const ArgumentBuilder_1 = __importDefault(__webpack_require__(/*! ./ArgumentBuilder */ "./node_modules/node-brigadier/dist/lib/builder/ArgumentBuilder.js"));
class LiteralArgumentBuilder extends ArgumentBuilder_1.default {
    constructor(literal) {
        super();
        this.literal = literal;
    }
    static literal(name) {
        return new LiteralArgumentBuilder(name);
    }
    getThis() {
        return this;
    }
    getLiteral() {
        return this.literal;
    }
    build() {
        let result = new LiteralCommandNode_1.default(this.getLiteral(), this.getCommand(), this.getRequirement(), this.getRedirect(), this.getRedirectModifier(), this.isFork());
        for (let arg of this.getArguments()) {
            result.addChild(arg);
        }
        return result;
    }
}
exports["default"] = LiteralArgumentBuilder;
exports.literal = LiteralArgumentBuilder.literal;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/builder/RequiredArgumentBuilder.js":
/*!*********************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/builder/RequiredArgumentBuilder.js ***!
  \*********************************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const ArgumentCommandNode_1 = __importDefault(__webpack_require__(/*! ../tree/ArgumentCommandNode */ "./node_modules/node-brigadier/dist/lib/tree/ArgumentCommandNode.js"));
const ArgumentBuilder_1 = __importDefault(__webpack_require__(/*! ./ArgumentBuilder */ "./node_modules/node-brigadier/dist/lib/builder/ArgumentBuilder.js"));
class RequiredArgumentBuilder extends ArgumentBuilder_1.default {
    constructor(name, type) {
        super();
        this.name = name;
        this.type = type;
    }
    static argument(name, type) {
        return new RequiredArgumentBuilder(name, type);
    }
    suggests(provider) {
        this.suggestionsProvider = provider;
        return this.getThis();
    }
    getSuggestionsProvider() {
        return this.suggestionsProvider;
    }
    getThis() {
        return this;
    }
    getType() {
        return this.type;
    }
    getName() {
        return this.name;
    }
    build() {
        let result = new ArgumentCommandNode_1.default(this.getName(), this.getType(), this.getCommand(), this.getRequirement(), this.getRedirect(), this.getRedirectModifier(), this.isFork(), this.getSuggestionsProvider());
        for (let arg of this.getArguments()) {
            result.addChild(arg);
        }
        return result;
    }
}
exports["default"] = RequiredArgumentBuilder;
exports.argument = RequiredArgumentBuilder.argument;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/context/CommandContext.js":
/*!************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/context/CommandContext.js ***!
  \************************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const isEqual_1 = __importDefault(__webpack_require__(/*! ../util/isEqual */ "./node_modules/node-brigadier/dist/lib/util/isEqual.js"));
class CommandContext {
    constructor(source, input, args, command, rootNode, nodes, range, child, modifier, forks) {
        this.source = source;
        this.input = input;
        this.args = args;
        this.command = command;
        this.rootNode = rootNode;
        this.nodes = nodes;
        this.range = range;
        this.child = child;
        this.modifier = modifier;
        this.forks = forks;
    }
    copyFor(source) {
        if (this.source === source)
            return this;
        return new CommandContext(source, this.input, this.args, this.command, this.rootNode, this.nodes, this.range, this.child, this.modifier, this.forks);
    }
    getChild() {
        return this.child;
    }
    getLastChild() {
        let result = this;
        while (!(result.getChild() == null)) {
            result = result.getChild();
        }
        return result;
    }
    getCommand() {
        return this.command;
    }
    getSource() {
        return this.source;
    }
    getArgument(name, clazz) {
        const arg = this.args.get(name);
        if (arg == null) {
            throw new Error("No such argument '" + name + "' exists on this command");
        }
        let result = arg.getResult();
        if (clazz == null) {
            return result;
        }
        else {
            return clazz(result);
        }
    }
    equals(o) {
        if (this === o)
            return true;
        if (!(o instanceof CommandContext))
            return false;
        if (!isEqual_1.default(this.args, o.args))
            return false;
        if (!this.rootNode.equals(o.rootNode))
            return false;
        if (this.nodes.length != o.nodes.length || !isEqual_1.default(this.nodes, o.nodes))
            return false;
        if (!(this.command == null) ? !isEqual_1.default(this.command, o.command) : o.command != null)
            return false;
        if (!isEqual_1.default(this.source, o.source))
            return false;
        if (!(this.child == null) ? !this.child.equals(o.child) : o.child != null)
            return false;
        return true;
    }
    getRedirectModifier() {
        return this.modifier;
    }
    getRange() {
        return this.range;
    }
    getInput() {
        return this.input;
    }
    getRootNode() {
        return this.rootNode;
    }
    getNodes() {
        return this.nodes;
    }
    hasNodes() {
        return this.nodes.length >= 0;
    }
    isForked() {
        return this.forks;
    }
}
exports["default"] = CommandContext;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/context/CommandContextBuilder.js":
/*!*******************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/context/CommandContextBuilder.js ***!
  \*******************************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const StringRange_1 = __importDefault(__webpack_require__(/*! ./StringRange */ "./node_modules/node-brigadier/dist/lib/context/StringRange.js"));
const CommandContext_1 = __importDefault(__webpack_require__(/*! ./CommandContext */ "./node_modules/node-brigadier/dist/lib/context/CommandContext.js"));
const SuggestionContext_1 = __importDefault(__webpack_require__(/*! ./SuggestionContext */ "./node_modules/node-brigadier/dist/lib/context/SuggestionContext.js"));
const ParsedCommandNode_1 = __importDefault(__webpack_require__(/*! ./ParsedCommandNode */ "./node_modules/node-brigadier/dist/lib/context/ParsedCommandNode.js"));
class CommandContextBuilder {
    constructor(dispatcher, source, rootNode, start) {
        this.args = new Map();
        this.nodes = [];
        this.modifier = null;
        this.rootNode = rootNode;
        this.dispatcher = dispatcher;
        this.source = source;
        this.range = StringRange_1.default.at(start);
    }
    withSource(source) {
        this.source = source;
        return this;
    }
    getSource() {
        return this.source;
    }
    getRootNode() {
        return this.rootNode;
    }
    withArgument(name, argument) {
        this.args.set(name, argument);
        return this;
    }
    getArguments() {
        return this.args;
    }
    withCommand(command) {
        this.command = command;
        return this;
    }
    withNode(node, range) {
        this.nodes.push(new ParsedCommandNode_1.default(node, range));
        this.range = StringRange_1.default.encompassing(this.range, range);
        this.modifier = node.getRedirectModifier();
        this.forks = node.isFork();
        return this;
    }
    copy() {
        const copy = new CommandContextBuilder(this.dispatcher, this.source, this.rootNode, this.range.getStart());
        copy.command = this.command;
        copy.args = new Map([...copy.args, ...this.args]);
        copy.nodes.push(...this.nodes);
        copy.child = this.child;
        copy.range = this.range;
        copy.forks = this.forks;
        return copy;
    }
    withChild(child) {
        this.child = child;
        return this;
    }
    getChild() {
        return this.child;
    }
    getLastChild() {
        let result = this;
        while (result.getChild() != null) {
            result = result.getChild();
        }
        return result;
    }
    getCommand() {
        return this.command;
    }
    getNodes() {
        return this.nodes;
    }
    build(input) {
        return new CommandContext_1.default(this.source, input, this.args, this.command, this.rootNode, this.nodes, this.range, this.child == null ? null : this.child.build(input), this.modifier, this.forks);
    }
    getDispatcher() {
        return this.dispatcher;
    }
    getRange() {
        return this.range;
    }
    findSuggestionContext(cursor) {
        if ((this.range.getStart() <= cursor)) {
            if ((this.range.getEnd() < cursor)) {
                if ((this.child != null)) {
                    return this.child.findSuggestionContext(cursor);
                }
                else if (this.nodes.length > 0) {
                    let last = this.nodes[this.nodes.length - 1];
                    return new SuggestionContext_1.default(last.getNode(), last.getRange().getEnd() + 1);
                }
                else {
                    return new SuggestionContext_1.default(this.rootNode, this.range.getStart());
                }
            }
            else {
                let prev = this.rootNode;
                for (let node of this.nodes) {
                    let nodeRange = node.getRange();
                    if (nodeRange.getStart() <= cursor && cursor <= nodeRange.getEnd()) {
                        return new SuggestionContext_1.default(prev, nodeRange.getStart());
                    }
                    prev = node.getNode();
                }
                if ((prev == null)) {
                    throw new Error("Can't find node before cursor");
                }
                return new SuggestionContext_1.default(prev, this.range.getStart());
            }
        }
        throw new Error("Can't find node before cursor");
    }
}
exports["default"] = CommandContextBuilder;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/context/ParsedArgument.js":
/*!************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/context/ParsedArgument.js ***!
  \************************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const StringRange_1 = __importDefault(__webpack_require__(/*! ./StringRange */ "./node_modules/node-brigadier/dist/lib/context/StringRange.js"));
class ParsedArgument {
    constructor(start, end, result) {
        this.range = StringRange_1.default.between(start, end);
        this.result = result;
    }
    getRange() {
        return this.range;
    }
    getResult() {
        return this.result;
    }
    equals(o) {
        if (this === o)
            return true;
        if (!(o instanceof ParsedArgument))
            return false;
        return this.range.equals(o.range) && this.result === o.result;
    }
}
exports["default"] = ParsedArgument;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/context/ParsedCommandNode.js":
/*!***************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/context/ParsedCommandNode.js ***!
  \***************************************************************************/
/***/ ((__unused_webpack_module, exports) => {


Object.defineProperty(exports, "__esModule", ({ value: true }));
class ParsedCommandNode {
    constructor(node, range) {
        this.node = node;
        this.range = range;
    }
    getNode() {
        return this.node;
    }
    getRange() {
        return this.range;
    }
    toString() {
        return this.node + "@" + this.range;
    }
    equals(o) {
        if (this === o)
            return true;
        if (o == null || !(o instanceof ParsedCommandNode)) {
            return false;
        }
        return this.node.equals(o.node) && this.range.equals(o.range);
    }
}
exports["default"] = ParsedCommandNode;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/context/StringRange.js":
/*!*********************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/context/StringRange.js ***!
  \*********************************************************************/
/***/ ((__unused_webpack_module, exports) => {


Object.defineProperty(exports, "__esModule", ({ value: true }));
class StringRange {
    constructor(start, end) {
        this.start = start;
        this.end = end;
    }
    static at(pos) {
        return new StringRange(pos, pos);
    }
    static between(start, end) {
        return new StringRange(start, end);
    }
    static encompassing(a, b) {
        return new StringRange(Math.min(a.getStart(), b.getStart()), Math.max(a.getEnd(), b.getEnd()));
    }
    getStart() {
        return this.start;
    }
    getEnd() {
        return this.end;
    }
    get(str) {
        if (typeof str === "string")
            return str.substring(this.start, this.end);
        else
            return str.getString().substring(this.start, this.end);
    }
    isEmpty() {
        return this.start === this.end;
    }
    getLength() {
        return this.end - this.start;
    }
    equals(o) {
        if (this === o)
            return true;
        if (!(o instanceof StringRange))
            return false;
        return this.start === o.start && this.end == o.end;
    }
    toString() {
        return "StringRange{" + "start=" + this.start + ", end=" + this.end + '}';
    }
}
exports["default"] = StringRange;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/context/SuggestionContext.js":
/*!***************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/context/SuggestionContext.js ***!
  \***************************************************************************/
/***/ ((__unused_webpack_module, exports) => {


Object.defineProperty(exports, "__esModule", ({ value: true }));
class SuggestionContext {
    constructor(parent, startPos) {
        this.parent = parent;
        this.startPos = startPos;
    }
}
exports["default"] = SuggestionContext;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/exceptions/BuiltInExceptions.js":
/*!******************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/exceptions/BuiltInExceptions.js ***!
  \******************************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const LiteralMessage_1 = __importDefault(__webpack_require__(/*! ../LiteralMessage */ "./node_modules/node-brigadier/dist/lib/LiteralMessage.js"));
const SimpleCommandExceptionType_1 = __importDefault(__webpack_require__(/*! ./SimpleCommandExceptionType */ "./node_modules/node-brigadier/dist/lib/exceptions/SimpleCommandExceptionType.js"));
const DynamicCommandExceptionType_1 = __importDefault(__webpack_require__(/*! ./DynamicCommandExceptionType */ "./node_modules/node-brigadier/dist/lib/exceptions/DynamicCommandExceptionType.js"));
class BuiltInExceptions {
    floatTooLow() {
        return BuiltInExceptions.FLOAT_TOO_SMALL;
    }
    floatTooHigh() {
        return BuiltInExceptions.FLOAT_TOO_BIG;
    }
    integerTooLow() {
        return BuiltInExceptions.INTEGER_TOO_SMALL;
    }
    integerTooHigh() {
        return BuiltInExceptions.INTEGER_TOO_BIG;
    }
    literalIncorrect() {
        return BuiltInExceptions.LITERAL_INCORRECT;
    }
    readerExpectedStartOfQuote() {
        return BuiltInExceptions.READER_EXPECTED_START_OF_QUOTE;
    }
    readerExpectedEndOfQuote() {
        return BuiltInExceptions.READER_EXPECTED_END_OF_QUOTE;
    }
    readerInvalidEscape() {
        return BuiltInExceptions.READER_INVALID_ESCAPE;
    }
    readerInvalidBool() {
        return BuiltInExceptions.READER_INVALID_BOOL;
    }
    readerInvalidInt() {
        return BuiltInExceptions.READER_INVALID_INT;
    }
    readerExpectedInt() {
        return BuiltInExceptions.READER_EXPECTED_INT;
    }
    readerInvalidFloat() {
        return BuiltInExceptions.READER_INVALID_FLOAT;
    }
    readerExpectedFloat() {
        return BuiltInExceptions.READER_EXPECTED_FLOAT;
    }
    readerExpectedBool() {
        return BuiltInExceptions.READER_EXPECTED_BOOL;
    }
    readerExpectedSymbol() {
        return BuiltInExceptions.READER_EXPECTED_SYMBOL;
    }
    dispatcherUnknownCommand() {
        return BuiltInExceptions.DISPATCHER_UNKNOWN_COMMAND;
    }
    dispatcherUnknownArgument() {
        return BuiltInExceptions.DISPATCHER_UNKNOWN_ARGUMENT;
    }
    dispatcherExpectedArgumentSeparator() {
        return BuiltInExceptions.DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR;
    }
    dispatcherParseException() {
        return BuiltInExceptions.DISPATCHER_PARSE_EXCEPTION;
    }
}
BuiltInExceptions.FLOAT_TOO_SMALL = new DynamicCommandExceptionType_1.default((found, min) => new LiteralMessage_1.default("Float must not be less than " + min + ", found " + found));
BuiltInExceptions.FLOAT_TOO_BIG = new DynamicCommandExceptionType_1.default((found, max) => new LiteralMessage_1.default("Float must not be more than " + max + ", found " + found));
BuiltInExceptions.INTEGER_TOO_SMALL = new DynamicCommandExceptionType_1.default((found, min) => new LiteralMessage_1.default("Integer must not be less than " + min + ", found " + found));
BuiltInExceptions.INTEGER_TOO_BIG = new DynamicCommandExceptionType_1.default((found, max) => new LiteralMessage_1.default("Integer must not be more than " + max + ", found " + found));
BuiltInExceptions.LITERAL_INCORRECT = new DynamicCommandExceptionType_1.default(expected => new LiteralMessage_1.default("Expected literal " + expected));
BuiltInExceptions.READER_EXPECTED_START_OF_QUOTE = new SimpleCommandExceptionType_1.default(new LiteralMessage_1.default("Expected quote to start a string"));
BuiltInExceptions.READER_EXPECTED_END_OF_QUOTE = new SimpleCommandExceptionType_1.default(new LiteralMessage_1.default("Unclosed quoted string"));
BuiltInExceptions.READER_INVALID_ESCAPE = new DynamicCommandExceptionType_1.default(character => new LiteralMessage_1.default("Invalid escape sequence '" + character + "' in quoted string"));
BuiltInExceptions.READER_INVALID_BOOL = new DynamicCommandExceptionType_1.default(value => new LiteralMessage_1.default("Invalid bool, expected true or false but found '" + value + "'"));
BuiltInExceptions.READER_INVALID_INT = new DynamicCommandExceptionType_1.default(value => new LiteralMessage_1.default("Invalid integer '" + value + "'"));
BuiltInExceptions.READER_EXPECTED_INT = new SimpleCommandExceptionType_1.default(new LiteralMessage_1.default("Expected integer"));
BuiltInExceptions.READER_INVALID_FLOAT = new DynamicCommandExceptionType_1.default(value => new LiteralMessage_1.default("Invalid float '" + value + "'"));
BuiltInExceptions.READER_EXPECTED_FLOAT = new SimpleCommandExceptionType_1.default(new LiteralMessage_1.default("Expected float"));
BuiltInExceptions.READER_EXPECTED_BOOL = new SimpleCommandExceptionType_1.default(new LiteralMessage_1.default("Expected bool"));
BuiltInExceptions.READER_EXPECTED_SYMBOL = new DynamicCommandExceptionType_1.default(symbol => new LiteralMessage_1.default("Expected '" + symbol + "'"));
BuiltInExceptions.DISPATCHER_UNKNOWN_COMMAND = new SimpleCommandExceptionType_1.default(new LiteralMessage_1.default("Unknown command"));
BuiltInExceptions.DISPATCHER_UNKNOWN_ARGUMENT = new SimpleCommandExceptionType_1.default(new LiteralMessage_1.default("Incorrect argument for command"));
BuiltInExceptions.DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR = new SimpleCommandExceptionType_1.default(new LiteralMessage_1.default("Expected whitespace to end one argument, but found trailing data"));
BuiltInExceptions.DISPATCHER_PARSE_EXCEPTION = new DynamicCommandExceptionType_1.default(message => new LiteralMessage_1.default(("Could not parse command: " + message)));
exports["default"] = BuiltInExceptions;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/exceptions/CommandSyntaxException.js":
/*!***********************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/exceptions/CommandSyntaxException.js ***!
  \***********************************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const BuiltInExceptions_1 = __importDefault(__webpack_require__(/*! ./BuiltInExceptions */ "./node_modules/node-brigadier/dist/lib/exceptions/BuiltInExceptions.js"));
class CommandSyntaxException extends Error {
    constructor(type, message, input = null, cursor = -1) {
        super(message.getString());
        Error.captureStackTrace(this, CommandSyntaxException);
        this.type = type;
        this.__message = message;
        this.input = input;
        this.cursor = cursor;
        this.message = this.getMessage();
    }
    getMessage() {
        let message = this.__message.getString();
        let context = this.getContext();
        if (context != null) {
            message += " at position " + this.cursor + ": " + context;
        }
        return message;
    }
    getRawMessage() {
        return this.__message;
    }
    getContext() {
        if (this.input == null || this.cursor < 0) {
            return null;
        }
        let builder = "";
        let cursor = Math.min(this.input.length, this.cursor);
        if (cursor > CommandSyntaxException.CONTEXT_AMOUNT) {
            builder += "...";
        }
        builder += this.input.substring(Math.max(0, cursor - CommandSyntaxException.CONTEXT_AMOUNT), cursor);
        builder += "<--[HERE]";
        return builder;
    }
    getType() {
        return this.type;
    }
    getInput() {
        return this.input;
    }
    getCursor() {
        return this.cursor;
    }
    toString() {
        return this.message;
    }
}
CommandSyntaxException.CONTEXT_AMOUNT = 10;
CommandSyntaxException.BUILT_IN_EXCEPTIONS = new BuiltInExceptions_1.default();
exports["default"] = CommandSyntaxException;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/exceptions/DynamicCommandExceptionType.js":
/*!****************************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/exceptions/DynamicCommandExceptionType.js ***!
  \****************************************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const CommandSyntaxException_1 = __importDefault(__webpack_require__(/*! ./CommandSyntaxException */ "./node_modules/node-brigadier/dist/lib/exceptions/CommandSyntaxException.js"));
class DynamicCommandExceptionType {
    constructor(fn) {
        this.fn = fn;
        Error.captureStackTrace(this, DynamicCommandExceptionType);
    }
    create(...args) {
        return new CommandSyntaxException_1.default(this, this.fn(...args));
    }
    createWithContext(reader, ...args) {
        return new CommandSyntaxException_1.default(this, this.fn(...args), reader.getString(), reader.getCursor());
    }
}
exports["default"] = DynamicCommandExceptionType;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/exceptions/SimpleCommandExceptionType.js":
/*!***************************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/exceptions/SimpleCommandExceptionType.js ***!
  \***************************************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const CommandSyntaxException_1 = __importDefault(__webpack_require__(/*! ./CommandSyntaxException */ "./node_modules/node-brigadier/dist/lib/exceptions/CommandSyntaxException.js"));
class SimpleCommandExceptionType {
    constructor(message) {
        this.message = message;
        Error.captureStackTrace(this, SimpleCommandExceptionType);
    }
    create() {
        return new CommandSyntaxException_1.default(this, this.message);
    }
    createWithContext(reader) {
        return new CommandSyntaxException_1.default(this, this.message, reader.getString(), reader.getCursor());
    }
    toString() {
        return this.message.getString();
    }
}
exports["default"] = SimpleCommandExceptionType;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/suggestion/IntegerSuggestion.js":
/*!******************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/suggestion/IntegerSuggestion.js ***!
  \******************************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const Suggestion_1 = __importDefault(__webpack_require__(/*! ./Suggestion */ "./node_modules/node-brigadier/dist/lib/suggestion/Suggestion.js"));
class IntegerSuggestion extends Suggestion_1.default {
    constructor(range, value, tooltip = null) {
        super(range, value.toString(), tooltip);
        this.value = value;
    }
    getValue() {
        return this.value;
    }
    equals(o) {
        if (this === o)
            return true;
        if (!(o instanceof IntegerSuggestion))
            return false;
        return this.value == o.value && super.equals(o);
    }
    toString() {
        return "IntegerSuggestion{" +
            "value=" + this.value +
            ", range=" + this.getRange() +
            ", text='" + this.getText() + '\'' +
            ", tooltip='" + this.getTooltip() + '\'' +
            '}';
    }
    compareTo(o) {
        if (o instanceof IntegerSuggestion) {
            return this.value < o.value ? 1 : -1;
        }
        return super.compareTo(o);
    }
    compareToIgnoreCase(b) {
        return this.compareTo(b);
    }
}
exports["default"] = IntegerSuggestion;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/suggestion/Suggestion.js":
/*!***********************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/suggestion/Suggestion.js ***!
  \***********************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const isEqual_1 = __importDefault(__webpack_require__(/*! ../util/isEqual */ "./node_modules/node-brigadier/dist/lib/util/isEqual.js"));
class Suggestion {
    constructor(range, text, tooltip = null) {
        this.range = range;
        this.text = text;
        this.tooltip = tooltip;
    }
    getRange() {
        return this.range;
    }
    getText() {
        return this.text;
    }
    getTooltip() {
        return this.tooltip;
    }
    apply(input) {
        if (this.range.getStart() === 0 && this.range.getEnd() == input.length) {
            return this.text;
        }
        let result = "";
        if (this.range.getStart() > 0) {
            result += input.substring(0, this.range.getStart());
        }
        result += this.text;
        if (this.range.getEnd() < input.length) {
            result += input.substring(this.range.getEnd());
        }
        return result;
    }
    equals(o) {
        if (this === o)
            return true;
        if (!(o instanceof Suggestion))
            return false;
        return isEqual_1.default(this.range, o.range) && (this.text === o.text) && isEqual_1.default(this.tooltip, o.tooltip);
    }
    toString() {
        return "Suggestion{" +
            "range=" + this.range +
            ", text='" + this.text + '\'' +
            ", tooltip='" + this.tooltip + '\'' +
            '}';
    }
    compareTo(o) {
        return this.text < o.text ? 1 : -1;
    }
    compareToIgnoreCase(b) {
        return this.text.toLowerCase() < b.text.toLowerCase() ? 1 : -1;
    }
    expand(command, range) {
        if (range.equals(this.range)) {
            return this;
        }
        let result = "";
        if (range.getStart() < this.range.getStart()) {
            result += command.substring(range.getStart(), this.range.getStart());
        }
        result += this.text;
        if (range.getEnd() > this.range.getEnd()) {
            result += command.substring(this.range.getEnd(), range.getEnd());
        }
        return new Suggestion(range, result, this.tooltip);
    }
}
exports["default"] = Suggestion;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/suggestion/Suggestions.js":
/*!************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/suggestion/Suggestions.js ***!
  \************************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const isEqual_1 = __importDefault(__webpack_require__(/*! ../util/isEqual */ "./node_modules/node-brigadier/dist/lib/util/isEqual.js"));
const StringRange_1 = __importDefault(__webpack_require__(/*! ../context/StringRange */ "./node_modules/node-brigadier/dist/lib/context/StringRange.js"));
class Suggestions {
    constructor(range, suggestions) {
        this.range = range;
        this.suggestions = suggestions;
    }
    getRange() {
        return this.range;
    }
    getList() {
        return this.suggestions;
    }
    isEmpty() {
        return this.suggestions.length === 0;
    }
    equals(o) {
        if (this === o)
            return true;
        if (!(o instanceof Suggestions))
            return false;
        return this.range.equals(o.range) && isEqual_1.default(this.suggestions, o.suggestions);
    }
    toString() {
        return "Suggestions{" +
            "range=" + this.range +
            ", suggestions=" + this.suggestions + '}';
    }
    static empty() {
        return Promise.resolve(this.EMPTY);
    }
    static merge(command, input) {
        if (input.length === 0) {
            return this.EMPTY;
        }
        else if (input.length === 1) {
            return input[0];
        }
        const texts = [];
        for (let suggestions of input) {
            texts.push(...suggestions.getList());
        }
        return Suggestions.create(command, texts);
    }
    static create(command, suggestions) {
        if (suggestions.length === 0) {
            return this.EMPTY;
        }
        let start = Infinity;
        let end = -Infinity;
        for (let suggestion of suggestions) {
            start = Math.min(suggestion.getRange().getStart(), start);
            end = Math.max(suggestion.getRange().getEnd(), end);
        }
        let range = new StringRange_1.default(start, end);
        const texts = [];
        for (let suggestion of suggestions) {
            texts.push(suggestion.expand(command, range));
        }
        const sorted = texts.sort((a, b) => a.compareToIgnoreCase(b));
        return new Suggestions(range, sorted);
    }
}
Suggestions.EMPTY = new Suggestions(StringRange_1.default.at(0), []);
exports["default"] = Suggestions;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/suggestion/SuggestionsBuilder.js":
/*!*******************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/suggestion/SuggestionsBuilder.js ***!
  \*******************************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const StringRange_1 = __importDefault(__webpack_require__(/*! ../context/StringRange */ "./node_modules/node-brigadier/dist/lib/context/StringRange.js"));
const Suggestion_1 = __importDefault(__webpack_require__(/*! ./Suggestion */ "./node_modules/node-brigadier/dist/lib/suggestion/Suggestion.js"));
const Suggestions_1 = __importDefault(__webpack_require__(/*! ./Suggestions */ "./node_modules/node-brigadier/dist/lib/suggestion/Suggestions.js"));
const IntegerSuggestion_1 = __importDefault(__webpack_require__(/*! ./IntegerSuggestion */ "./node_modules/node-brigadier/dist/lib/suggestion/IntegerSuggestion.js"));
class SuggestionsBuilder {
    constructor(input, start) {
        this.result = [];
        this.input = input;
        this.start = start;
        this.remaining = input.substring(start);
    }
    getInput() {
        return this.input;
    }
    getStart() {
        return this.start;
    }
    getRemaining() {
        return this.remaining;
    }
    build() {
        return Suggestions_1.default.create(this.input, this.result);
    }
    buildPromise() {
        return Promise.resolve(this.build());
    }
    suggest(text, tooltip = null) {
        if (typeof text === "number") {
            this.result.push(new IntegerSuggestion_1.default(StringRange_1.default.between(this.start, this.input.length), text, tooltip));
            return this;
        }
        if (text === this.remaining)
            return this;
        this.result.push(new Suggestion_1.default(StringRange_1.default.between(this.start, this.input.length), text, tooltip));
        return this;
    }
    add(other) {
        this.result.push(...other.result);
        return this;
    }
    createOffset(start) {
        return new SuggestionsBuilder(this.input, this.start);
    }
    restart() {
        return new SuggestionsBuilder(this.input, this.start);
    }
}
exports["default"] = SuggestionsBuilder;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/tree/ArgumentCommandNode.js":
/*!**************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/tree/ArgumentCommandNode.js ***!
  \**************************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const isEqual_1 = __importDefault(__webpack_require__(/*! ../util/isEqual */ "./node_modules/node-brigadier/dist/lib/util/isEqual.js"));
const StringReader_1 = __importDefault(__webpack_require__(/*! ../StringReader */ "./node_modules/node-brigadier/dist/lib/StringReader.js"));
const RequiredArgumentBuilder_1 = __importDefault(__webpack_require__(/*! ../builder/RequiredArgumentBuilder */ "./node_modules/node-brigadier/dist/lib/builder/RequiredArgumentBuilder.js"));
const ParsedArgument_1 = __importDefault(__webpack_require__(/*! ../context/ParsedArgument */ "./node_modules/node-brigadier/dist/lib/context/ParsedArgument.js"));
const Suggestions_1 = __importDefault(__webpack_require__(/*! ../suggestion/Suggestions */ "./node_modules/node-brigadier/dist/lib/suggestion/Suggestions.js"));
const CommandNode_1 = __importDefault(__webpack_require__(/*! ./CommandNode */ "./node_modules/node-brigadier/dist/lib/tree/CommandNode.js"));
const USAGE_ARGUMENT_OPEN = "<";
const USAGE_ARGUMENT_CLOSE = ">";
class ArgumentCommandNode extends CommandNode_1.default {
    constructor(name, type, command, requirement, redirect, modifier, forks, customSuggestions) {
        super(command, requirement, redirect, modifier, forks);
        this.name = name;
        this.type = type;
        this.customSuggestions = customSuggestions;
    }
    getNodeType() {
        return "argument";
    }
    getType() {
        return this.type;
    }
    getName() {
        return this.name;
    }
    getUsageText() {
        return USAGE_ARGUMENT_OPEN + this.name + USAGE_ARGUMENT_CLOSE;
    }
    getCustomSuggestions() {
        return this.customSuggestions;
    }
    parse(reader, contextBuilder) {
        let start = reader.getCursor();
        let result = this.type.parse(reader);
        let parsed = new ParsedArgument_1.default(start, reader.getCursor(), result);
        contextBuilder.withArgument(this.name, parsed);
        contextBuilder.withNode(this, parsed.getRange());
    }
    listSuggestions(context, builder) {
        if (this.customSuggestions == null) {
            if (typeof this.type.listSuggestions === "function")
                return this.type.listSuggestions(context, builder);
            else
                return Suggestions_1.default.empty();
        }
        else {
            return this.customSuggestions.getSuggestions(context, builder);
        }
    }
    createBuilder() {
        let builder = RequiredArgumentBuilder_1.default.argument(this.name, this.type);
        builder.requires(this.getRequirement());
        builder.forward(this.getRedirect(), this.getRedirectModifier(), this.isFork());
        builder.suggests(this.customSuggestions);
        if (this.getCommand() != null) {
            builder.executes(this.getCommand());
        }
        return builder;
    }
    isValidInput(input) {
        try {
            let reader = new StringReader_1.default(input);
            this.type.parse(reader);
            return !reader.canRead() || reader.peek() == ' ';
        }
        catch (ignored) {
        }
        return false;
    }
    equals(o) {
        if (this === o)
            return true;
        if (!(o instanceof ArgumentCommandNode))
            return false;
        if (!(this.name === o.name))
            return false;
        if (!isEqual_1.default(this.type, o.type))
            return false;
        return super.equals(o);
    }
    getSortedKey() {
        return this.name;
    }
    getExamples() {
        return typeof this.type.getExamples === "function" ? this.type.getExamples() : [];
    }
    toString() {
        return "<argument " + this.name + ":" + this.type + ">";
    }
}
exports["default"] = ArgumentCommandNode;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/tree/CommandNode.js":
/*!******************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/tree/CommandNode.js ***!
  \******************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const isEqual_1 = __importDefault(__webpack_require__(/*! ../util/isEqual */ "./node_modules/node-brigadier/dist/lib/util/isEqual.js"));
class CommandNode {
    constructor(command, requirement, redirect, modifier, forks) {
        this.children = new Map();
        this.literals = new Map();
        this.args = new Map();
        this.command = command;
        this.requirement = requirement || (() => true);
        this.redirect = redirect;
        this.modifier = modifier;
        this.forks = forks;
    }
    getCommand() {
        return this.command;
    }
    getChildren() {
        return this.children.values();
    }
    getChildrenCount() {
        return this.children.size;
    }
    getChild(name) {
        return this.children.get(name);
    }
    getRedirect() {
        return this.redirect;
    }
    getRedirectModifier() {
        return this.modifier;
    }
    canUse(source) {
        return this.requirement(source);
    }
    addChild(node) {
        if (node.getNodeType() === "root") {
            throw new Error("Cannot add a RootCommandNode as a child to any other CommandNode");
        }
        let child = this.children.get(node.getName());
        if (child != null) {
            //  We've found something to merge onto
            if ((node.getCommand() != null)) {
                child.command = node.getCommand();
            }
            for (let grandchild of node.getChildren()) {
                child.addChild(grandchild);
            }
        }
        else {
            this.children.set(node.getName(), node);
            if (node.getNodeType() === "literal") {
                this.literals.set(node.getName(), node);
            }
            else if (node.getNodeType() === "argument") {
                this.args.set(node.getName(), node);
            }
        }
        this.children = new Map([...this.children.entries()].sort((a, b) => a[1].compareTo(b[1])));
    }
    findAmbiguities(consumer) {
        let matches = [];
        for (let child of this.children.values()) {
            for (let sibling of this.children.values()) {
                if (child === sibling) {
                    continue;
                }
                for (let input of child.getExamples()) {
                    if (sibling.isValidInput(input)) {
                        matches.push(input);
                    }
                }
                if (matches.length > 0) {
                    consumer.ambiguous(this, child, sibling, matches);
                    matches = [];
                }
            }
            child.findAmbiguities(consumer);
        }
    }
    equals(o) {
        if (this === o)
            return true;
        if (!(o instanceof CommandNode))
            return false;
        if (this.children.size !== o.children.size) {
            return false;
        }
        if (!isEqual_1.default(this.children, o.children))
            return false;
        if (this.command != null ? !isEqual_1.default(this.command, o.command) : o.command != null)
            return false;
        return true;
    }
    getRequirement() {
        return this.requirement;
    }
    getRelevantNodes(input) {
        if (this.literals.size > 0) {
            let cursor = input.getCursor();
            while ((input.canRead()
                && (input.peek() != ' '))) {
                input.skip();
            }
            let text = input.getString().substring(cursor, input.getCursor());
            input.setCursor(cursor);
            let literal = this.literals.get(text);
            if (literal != null) {
                return [literal];
            }
            else {
                return this.args.values();
            }
        }
        else {
            return this.args.values();
        }
    }
    compareTo(o) {
        if (this.getNodeType() === o.getNodeType()) {
            return this.getSortedKey() > o.getSortedKey() ? 1 : -1;
        }
        return (o.getNodeType() === "literal") ? 1 : -1;
    }
    isFork() {
        return this.forks;
    }
}
exports["default"] = CommandNode;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/tree/LiteralCommandNode.js":
/*!*************************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/tree/LiteralCommandNode.js ***!
  \*************************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const CommandNode_1 = __importDefault(__webpack_require__(/*! ./CommandNode */ "./node_modules/node-brigadier/dist/lib/tree/CommandNode.js"));
const StringReader_1 = __importDefault(__webpack_require__(/*! ../StringReader */ "./node_modules/node-brigadier/dist/lib/StringReader.js"));
const LiteralArgumentBuilder_1 = __importDefault(__webpack_require__(/*! ../builder/LiteralArgumentBuilder */ "./node_modules/node-brigadier/dist/lib/builder/LiteralArgumentBuilder.js"));
const StringRange_1 = __importDefault(__webpack_require__(/*! ../context/StringRange */ "./node_modules/node-brigadier/dist/lib/context/StringRange.js"));
const CommandSyntaxException_1 = __importDefault(__webpack_require__(/*! ../exceptions/CommandSyntaxException */ "./node_modules/node-brigadier/dist/lib/exceptions/CommandSyntaxException.js"));
const Suggestions_1 = __importDefault(__webpack_require__(/*! ../suggestion/Suggestions */ "./node_modules/node-brigadier/dist/lib/suggestion/Suggestions.js"));
class LiteralCommandNode extends CommandNode_1.default {
    constructor(literal, command, requirement, redirect, modifier, forks) {
        super(command, requirement, redirect, modifier, forks);
        this.literal = literal;
    }
    getNodeType() {
        return "literal";
    }
    getLiteral() {
        return this.literal;
    }
    getName() {
        return this.literal;
    }
    parse(reader, contextBuilder) {
        let start = reader.getCursor();
        let end = this.__parse(reader);
        if (end > -1) {
            contextBuilder.withNode(this, StringRange_1.default.between(start, end));
            return;
        }
        throw CommandSyntaxException_1.default.BUILT_IN_EXCEPTIONS.literalIncorrect().createWithContext(reader, this.literal);
    }
    __parse(reader) {
        let start = reader.getCursor();
        if (reader.canRead(this.literal.length)) {
            let end = start + this.literal.length;
            if (reader.getString().substring(start, end) === this.literal) {
                reader.setCursor(end);
                if (!reader.canRead() || reader.peek() == ' ') {
                    return end;
                }
                else {
                    reader.setCursor(start);
                }
            }
        }
        return -1;
    }
    listSuggestions(context, builder) {
        if (this.literal.toLowerCase().startsWith(builder.getRemaining().toLowerCase())) {
            return builder.suggest(this.literal).buildPromise();
        }
        else {
            return Suggestions_1.default.empty();
        }
    }
    isValidInput(input) {
        return this.__parse(new StringReader_1.default(input)) > -1;
    }
    equals(o) {
        if (this === o)
            return true;
        if (!(o instanceof LiteralCommandNode))
            return false;
        if (!(this.literal === o.literal))
            return false;
        return super.equals(o);
    }
    getUsageText() {
        return this.literal;
    }
    createBuilder() {
        let builder = LiteralArgumentBuilder_1.default.literal(this.literal);
        builder.requires(this.getRequirement());
        builder.forward(this.getRedirect(), this.getRedirectModifier(), this.isFork());
        if (this.getCommand() != null)
            builder.executes(this.getCommand());
        return builder;
    }
    getSortedKey() {
        return this.literal;
    }
    getExamples() {
        return [this.literal];
    }
    toString() {
        return "<literal " + this.literal + ">";
    }
}
exports["default"] = LiteralCommandNode;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/tree/RootCommandNode.js":
/*!**********************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/tree/RootCommandNode.js ***!
  \**********************************************************************/
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {


var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const CommandNode_1 = __importDefault(__webpack_require__(/*! ./CommandNode */ "./node_modules/node-brigadier/dist/lib/tree/CommandNode.js"));
const Suggestions_1 = __importDefault(__webpack_require__(/*! ../suggestion/Suggestions */ "./node_modules/node-brigadier/dist/lib/suggestion/Suggestions.js"));
class RootCommandNode extends CommandNode_1.default {
    constructor() {
        super(null, s => true, null, (s) => s.getSource(), false);
    }
    getNodeType() {
        return "root";
    }
    getName() {
        return "";
    }
    getUsageText() {
        return "";
    }
    parse(reader, contextBuilder) {
    }
    listSuggestions(context, builder) {
        return Suggestions_1.default.empty();
    }
    isValidInput(input) {
        return false;
    }
    equals(o) {
        if (this === o)
            return true;
        if (!(o instanceof RootCommandNode))
            return false;
        return super.equals(o);
    }
    createBuilder() {
        throw new Error("Cannot convert root into a builder");
    }
    getSortedKey() {
        return "";
    }
    getExamples() {
        return [];
    }
    toString() {
        return "<root>";
    }
}
exports["default"] = RootCommandNode;


/***/ }),

/***/ "./node_modules/node-brigadier/dist/lib/util/isEqual.js":
/*!**************************************************************!*\
  !*** ./node_modules/node-brigadier/dist/lib/util/isEqual.js ***!
  \**************************************************************/
/***/ ((__unused_webpack_module, exports) => {


Object.defineProperty(exports, "__esModule", ({ value: true }));
function isEqual(a, b) {
    if (a === b)
        return true;
    if (typeof a != typeof b)
        return false;
    if (!(a instanceof Object))
        return false;
    if (typeof a === "function")
        return a.toString() === b.toString();
    if (a.constructor !== b.constructor)
        return false;
    if (a instanceof Map)
        return isMapEqual(a, b);
    if (a instanceof Set)
        return isArrayEqual([...a], [...b]);
    if (a instanceof Array)
        return isArrayEqual(a, b);
    if (typeof a === "object")
        return isObjectEqual(a, b);
    return false;
}
exports["default"] = isEqual;
function isMapEqual(a, b) {
    if (a.size != b.size)
        return false;
    for (let [key, val] of a) {
        const testVal = b.get(key);
        if (!isEqual(testVal, val))
            return false;
        if (testVal === undefined && !b.has(key))
            return false;
    }
    return true;
}
function isArrayEqual(a, b) {
    if (a.length != b.length)
        return false;
    for (let i = 0; i < a.length; i++)
        if (!isEqual(a[i], b[i]))
            return false;
    return true;
}
function isObjectEqual(a, b) {
    const aKeys = Object.keys(a);
    const bKeys = Object.keys(b);
    if (aKeys.length != bKeys.length)
        return false;
    if (!aKeys.every(key => b.hasOwnProperty(key)))
        return false;
    return aKeys.every((key) => {
        return isEqual(a[key], b[key]);
    });
}
;


/***/ }),

/***/ "./src/arguments.ts":
/*!**************************!*\
  !*** ./src/arguments.ts ***!
  \**************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   "AngleArgument": () => (/* binding */ AngleArgument),
/* harmony export */   "BlockPosArgument": () => (/* binding */ BlockPosArgument),
/* harmony export */   "ColorArgument": () => (/* binding */ ColorArgument),
/* harmony export */   "ColumnPosArgument": () => (/* binding */ ColumnPosArgument),
/* harmony export */   "EntitySelectorArgument": () => (/* binding */ EntitySelectorArgument),
/* harmony export */   "MultiLiteralArgument": () => (/* binding */ MultiLiteralArgument),
/* harmony export */   "PlayerArgument": () => (/* binding */ PlayerArgument),
/* harmony export */   "PotionEffectArgument": () => (/* binding */ PotionEffectArgument),
/* harmony export */   "TimeArgument": () => (/* binding */ TimeArgument),
/* harmony export */   "UUIDArgument": () => (/* binding */ UUIDArgument)
/* harmony export */ });
/* harmony import */ var node_brigadier__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! node-brigadier */ "./node_modules/node-brigadier/dist/index.js");
/* harmony import */ var node_brigadier__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(node_brigadier__WEBPACK_IMPORTED_MODULE_0__);

class ExtendedStringReader {
    static readLocationLiteral(reader) {
        function isAllowedLocationLiteral(c) {
            return c === '~' || c === '^';
        }
        let start = reader.getCursor();
        while (reader.canRead() && (node_brigadier__WEBPACK_IMPORTED_MODULE_0__.StringReader.isAllowedNumber(reader.peek()) || isAllowedLocationLiteral(reader.peek()))) {
            reader.skip();
        }
        let number = reader.getString().substring(start, reader.getCursor());
        if (number.length === 0) {
            throw node_brigadier__WEBPACK_IMPORTED_MODULE_0__.CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedInt().createWithContext(reader);
        }
        if (number.startsWith("~") || number.startsWith("^")) {
            if (number.length === 1) {
                // Accept.
                return 0;
            }
            else {
                number = number.slice(1);
            }
        }
        const result = parseInt(number);
        if (isNaN(result) || result !== parseFloat(number)) {
            reader.setCursor(start);
            throw node_brigadier__WEBPACK_IMPORTED_MODULE_0__.CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(reader, number);
        }
        else {
            return result;
        }
    }
    static readResourceLocation(reader) {
        function isAllowedInResourceLocation(c) {
            return ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || c === '_' || c === ':' || c === '/' || c === '.' || c === '-');
        }
        function validPathChar(c) {
            return (c === '_' || c === '-' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c === '/' || c === '.');
        }
        function validNamespaceChar(c) {
            return (c === '_' || c === '-' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c === '.');
        }
        function isValid(string, predicate) {
            for (let i = 0; i < string.length; i++) {
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
        let resourceLocation = reader.getString().substring(start, reader.getCursor());
        const resourceLocationParts = resourceLocation.split(":");
        switch (resourceLocationParts.length) {
            case 0:
                throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(resourceLocation + " is not a valid Resource")).createWithContext(reader);
            case 1:
                // Check path
                if (!isValid(resourceLocationParts[0], validPathChar)) {
                    throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Non [a-z0-9/._-] character in path of location: " + resourceLocation)).createWithContext(reader);
                }
                resourceLocation = `minecraft:${resourceLocation}`;
                break;
            case 2:
                // Check namespace
                if (!isValid(resourceLocationParts[0], validNamespaceChar)) {
                    throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Non [a-z0-9_.-] character in namespace of location: " + resourceLocation)).createWithContext(reader);
                }
                // Check path
                if (!isValid(resourceLocationParts[1], validPathChar)) {
                    throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Non [a-z0-9/._-] character in path of location: " + resourceLocation)).createWithContext(reader);
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
    static suggest(suggestions, builder) {
        let remainingLowercase = builder.getRemaining().toLowerCase();
        for (let suggestion of suggestions) {
            if (HelperSuggestionProvider.matchesSubStr(remainingLowercase, suggestion.toLowerCase())) {
                builder.suggest(suggestion);
            }
        }
        return builder.buildPromise();
    }
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
class TimeArgument {
    static UNITS = new Map([
        ["d", 24000],
        ["s", 20],
        ["t", 1],
        ["", 1]
    ]);
    ticks;
    constructor(ticks = 0) {
        this.ticks = ticks;
    }
    parse(reader) {
        const numericalValue = reader.readFloat();
        const unit = reader.readUnquotedString();
        const unitMultiplier = TimeArgument.UNITS.get(unit);
        if (unitMultiplier === 0) {
            throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`Invalid unit "${unit}"`)).createWithContext(reader);
        }
        const ticks = Math.round(numericalValue * unitMultiplier);
        if (ticks < 0) {
            throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Tick count must be non-negative")).createWithContext(reader);
        }
        this.ticks = ticks;
        return this;
    }
    listSuggestions(_context, builder) {
        let reader = new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.StringReader(builder.getRemaining());
        try {
            reader.readFloat();
        }
        catch (ex) {
            return builder.buildPromise();
        }
        return HelperSuggestionProvider.suggest([...TimeArgument.UNITS.keys()], builder.createOffset(builder.getStart() + reader.getCursor()));
    }
    getExamples() {
        return ["0d", "0s", "0t", "0"];
    }
}
class BlockPosArgument {
    x;
    y;
    z;
    constructor(x = 0, y = 0, z = 0) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    parse(reader) {
        this.x = ExtendedStringReader.readLocationLiteral(reader);
        reader.skip();
        this.y = ExtendedStringReader.readLocationLiteral(reader);
        reader.skip();
        this.z = ExtendedStringReader.readLocationLiteral(reader);
        return this;
    }
    listSuggestions(_context, builder) {
        builder.suggest("~");
        builder.suggest("~ ~");
        builder.suggest("~ ~ ~");
        return builder.buildPromise();
    }
    getExamples() {
        return ["1 2 3"];
    }
}
class ColumnPosArgument {
    x;
    z;
    constructor(x = 0, z = 0) {
        this.x = x;
        this.z = z;
    }
    parse(reader) {
        this.x = ExtendedStringReader.readLocationLiteral(reader);
        reader.skip();
        this.z = ExtendedStringReader.readLocationLiteral(reader);
        return this;
    }
    listSuggestions(_context, builder) {
        builder.suggest("~");
        builder.suggest("~ ~");
        return builder.buildPromise();
    }
    getExamples() {
        return ["1 2"];
    }
}
class PlayerArgument {
    username;
    constructor(username = "") {
        this.username = username;
    }
    parse(reader) {
        const start = reader.getCursor();
        while (reader.canRead() && reader.peek() !== " ") {
            reader.skip();
        }
        const string = reader.getString();
        const currentCursor = reader.getCursor();
        this.username = string.slice(start, currentCursor);
        if (!this.username.match(/^[A-Za-z0-9_]{2,16}$/)) {
            throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(this.username + " is not a valid username")).createWithContext(reader);
        }
        return this;
    }
    getExamples() {
        return ["Skepter"];
    }
}
class MultiLiteralArgument {
    literals;
    selectedLiteral;
    constructor(literals) {
        this.literals = literals;
        this.selectedLiteral = "";
    }
    parse(reader) {
        const start = reader.getCursor();
        while (reader.canRead() && reader.peek() !== " ") {
            reader.skip();
        }
        this.selectedLiteral = reader.getString().slice(start, reader.getCursor());
        if (this.selectedLiteral.endsWith(" ")) {
            this.selectedLiteral.trimEnd();
            reader.setCursor(reader.getCursor() - 1);
        }
        if (!this.literals.includes(this.selectedLiteral)) {
            throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(this.selectedLiteral + " is not one of " + this.literals)).createWithContext(reader);
        }
        return this;
    }
    listSuggestions(_context, builder) {
        for (let literal of this.literals) {
            builder.suggest(literal);
        }
        return builder.buildPromise();
    }
    getExamples() {
        return ["blah"];
    }
}
class ColorArgument {
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
    chatcolor;
    constructor(chatcolor = null) {
        this.chatcolor = chatcolor;
    }
    parse(reader) {
        let input = reader.readUnquotedString();
        let chatFormat = ColorArgument.ChatColor[input.toLowerCase()];
        if (chatFormat === undefined) {
            throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`Unknown colour '${input}'`)).createWithContext(reader);
        }
        this.chatcolor = chatFormat;
        return this;
    }
    listSuggestions(_context, builder) {
        return HelperSuggestionProvider.suggest(Object.keys(ColorArgument.ChatColor), builder);
    }
    getExamples() {
        return ["red", "green"];
    }
}
class PotionEffectArgument {
    static PotionEffects = [
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
    ];
    potionEffect;
    constructor(potionEffect = null) {
        this.potionEffect = potionEffect;
    }
    parse(reader) {
        const input = ExtendedStringReader.readResourceLocation(reader);
        const isValidPotionEffect = PotionEffectArgument.PotionEffects.includes(input.toLowerCase());
        if (!isValidPotionEffect) {
            throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`Unknown effect '${input}'`)).createWithContext(reader);
        }
        this.potionEffect = input;
        return this;
    }
    listSuggestions(_context, builder) {
        return HelperSuggestionProvider.suggest([...PotionEffectArgument.PotionEffects], builder);
    }
    getExamples() {
        return ["spooky", "effect"];
    }
}
class AngleArgument {
    angle;
    relative;
    parse(reader) {
        if (!reader.canRead()) {
            throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Incomplete (expected 1 angle)")).createWithContext(reader);
        }
        // Read relative ~
        if (reader.peek() === '~') {
            this.relative = true;
            reader.skip();
        }
        else {
            this.relative = false;
        }
        this.angle = (reader.canRead() && reader.peek() !== ' ') ? reader.readFloat() : 0.0;
        if (isNaN(this.angle) || !isFinite(this.angle)) {
            throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Invalid angle")).createWithContext(reader);
        }
        return this;
    }
    getExamples() {
        return ["0", "~", "~-5"];
    }
}
class UUIDArgument {
    uuid;
    parse(reader) {
        const remaining = reader.getRemaining();
        const matchedResults = remaining.match(/^([-A-Fa-f0-9]+)/);
        if (matchedResults !== null) {
            this.uuid = matchedResults[1];
            // Regex for a UUID: https://stackoverflow.com/a/13653180/4779071
            if (this.uuid.match(/^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$/i) !== null) {
                reader.setCursor(reader.getCursor() + this.uuid.length);
                return this;
            }
        }
        throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Invalid UUID")).createWithContext(reader);
    }
    getExamples() {
        return ["dd12be42-52a9-4a91-a8a1-11c01849e498"];
    }
}
class EntitySelectorArgument {
    static shouldInvertValue(reader) {
        reader.skipWhitespace();
        if (reader.canRead() && reader.peek() === '!') {
            reader.skip();
            reader.skipWhitespace();
            return true;
        }
        return false;
    }
    static Options = {
        name: (reader) => {
            const start = reader.getCursor();
            const shouldInvert = this.shouldInvertValue(reader);
            // const _s: string = reader.readString();
            if ( /* STUB: this.hasNameNotEquals() */!shouldInvert) {
                reader.setCursor(start);
                throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`Option 'name' isn't applicable here`)).createWithContext(reader);
            }
            if (shouldInvert) {
                // stub: setHasNameNotEqual(true)
            }
            else {
                // stub: setHasNameEquals(true)
            }
            // Predicate?
        },
        distance: (_reader) => { },
        level: (_reader) => { },
        x: (reader) => { reader.readFloat(); },
        y: (reader) => { reader.readFloat(); },
        z: (reader) => { reader.readFloat(); },
        dx: (reader) => { reader.readFloat(); },
        dy: (reader) => { reader.readFloat(); },
        dz: (reader) => { reader.readFloat(); },
        x_rotation: (_reader) => { },
        y_rotation: (_reader) => { },
        limit: (_reader) => { },
        sort: (_reader) => { },
        gamemode: (_reader) => { },
        team: (_reader) => { },
        type: (_reader) => { },
        tag: (_reader) => { },
        nbt: (_reader) => { },
        scores: (_reader) => { },
        advancements: (_reader) => { },
        predicate: (_reader) => { }
    };
    parse(reader) {
        // uuuuuuuuuuuugh, I so totally didn't want to implement this big chungus argument, but here we go!
        let entityUUID;
        let includesEntities;
        let playerName;
        let maxResults;
        let limitedToPlayers; // players only?
        let currentEntity;
        let single;
        function parseOptions() {
            reader.skipWhitespace();
            while (reader.canRead() && reader.peek() !== "]") {
                reader.skipWhitespace();
                let start = reader.getCursor();
                let s = reader.readString();
                let modifier = EntitySelectorArgument.Options[s];
                if (modifier === null) {
                    reader.setCursor(start);
                    throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`Unknown option '${s}'`)).createWithContext(reader);
                }
                reader.skipWhitespace();
                if (!reader.canRead() || reader.peek() !== "=") {
                    reader.setCursor(start);
                    throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`Expected value for option '${s}'`)).createWithContext(reader);
                }
                reader.skip();
                reader.skipWhitespace();
                modifier(reader);
                reader.skipWhitespace();
                if (!reader.canRead()) {
                    continue;
                }
                if (reader.peek() === ",") {
                    reader.skip();
                    continue;
                }
                if (reader.peek() !== "]") {
                    throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Expected end of options")).createWithContext(reader);
                }
            }
            if (reader.canRead()) {
                reader.skip();
                return;
            }
            throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Expected end of options")).createWithContext(reader);
        }
        function parseSelector() {
            if (!reader.canRead()) {
                throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Missing selector type")).createWithContext(reader);
            }
            let start = reader.getCursor();
            let selectorChar = reader.read();
            switch (selectorChar) {
                case "p":
                    maxResults = 1;
                    includesEntities = false;
                    limitedToPlayers = true;
                    break;
                case "a":
                    maxResults = Number.MAX_SAFE_INTEGER;
                    includesEntities = false;
                    limitedToPlayers = true;
                    break;
                case "r":
                    maxResults = 1;
                    includesEntities = false;
                    limitedToPlayers = true;
                    break;
                case "s":
                    maxResults = 1;
                    includesEntities = true;
                    currentEntity = true;
                    break;
                case "e":
                    maxResults = Number.MAX_SAFE_INTEGER;
                    includesEntities = true;
                    break;
                default:
                    reader.setCursor(start);
                    throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`Unknown selector type '${selectorChar}'`)).createWithContext(reader);
            }
            if (reader.canRead() && reader.peek() === "[") {
                reader.skip();
                parseOptions();
            }
        }
        function parseNameOrUUID() {
            let i = reader.getCursor();
            let s = reader.readString();
            // Regex for a UUID: https://stackoverflow.com/a/13653180/4779071
            if (s.match(/^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$/i) !== null) {
                entityUUID = s;
                includesEntities = true;
            }
            else if (s.length === 0 || s.length > 16) {
                reader.setCursor(i);
                throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Invalid name or UUID")).createWithContext(reader);
            }
            else {
                playerName = s;
                includesEntities = false;
            }
            // We're only allowing 1 result because we've specified a player or
            // UUID, and not an @ selector
            maxResults = 1;
        }
        let startPosition = reader.getCursor();
        if (reader.canRead() && reader.peek() === "@") {
            reader.skip();
            parseSelector();
        }
        else {
            parseNameOrUUID();
        }
        // Final checks...
        if (maxResults > 0 && single) {
            reader.setCursor(0);
            if (limitedToPlayers) {
                // throw throw ERROR_NOT_SINGLE_PLAYER.createWithContext(stringreader);
            }
            else {
                // throw ERROR_NOT_SINGLE_ENTITY.createWithContext(stringreader);
            }
        }
        if (includesEntities && limitedToPlayers /* STUB: !isSelfSelector() */) {
            reader.setCursor(0);
            // throw ERROR_ONLY_PLAYERS_ALLOWED.createWithContext(stringreader);
        }
        return this;
    }
    getExamples() {
        return ["dd12be42-52a9-4a91-a8a1-11c01849e498"];
    }
}


/***/ })

/******/ 	});
/************************************************************************/
/******/ 	// The module cache
/******/ 	var __webpack_module_cache__ = {};
/******/ 	
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/ 		// Check if module is in cache
/******/ 		var cachedModule = __webpack_module_cache__[moduleId];
/******/ 		if (cachedModule !== undefined) {
/******/ 			return cachedModule.exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = __webpack_module_cache__[moduleId] = {
/******/ 			// no module.id needed
/******/ 			// no module.loaded needed
/******/ 			exports: {}
/******/ 		};
/******/ 	
/******/ 		// Execute the module function
/******/ 		__webpack_modules__[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/ 	
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/ 	
/************************************************************************/
/******/ 	/* webpack/runtime/compat get default export */
/******/ 	(() => {
/******/ 		// getDefaultExport function for compatibility with non-harmony modules
/******/ 		__webpack_require__.n = (module) => {
/******/ 			var getter = module && module.__esModule ?
/******/ 				() => (module['default']) :
/******/ 				() => (module);
/******/ 			__webpack_require__.d(getter, { a: getter });
/******/ 			return getter;
/******/ 		};
/******/ 	})();
/******/ 	
/******/ 	/* webpack/runtime/define property getters */
/******/ 	(() => {
/******/ 		// define getter functions for harmony exports
/******/ 		__webpack_require__.d = (exports, definition) => {
/******/ 			for(var key in definition) {
/******/ 				if(__webpack_require__.o(definition, key) && !__webpack_require__.o(exports, key)) {
/******/ 					Object.defineProperty(exports, key, { enumerable: true, get: definition[key] });
/******/ 				}
/******/ 			}
/******/ 		};
/******/ 	})();
/******/ 	
/******/ 	/* webpack/runtime/hasOwnProperty shorthand */
/******/ 	(() => {
/******/ 		__webpack_require__.o = (obj, prop) => (Object.prototype.hasOwnProperty.call(obj, prop))
/******/ 	})();
/******/ 	
/******/ 	/* webpack/runtime/make namespace object */
/******/ 	(() => {
/******/ 		// define __esModule on exports
/******/ 		__webpack_require__.r = (exports) => {
/******/ 			if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 				Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 			}
/******/ 			Object.defineProperty(exports, '__esModule', { value: true });
/******/ 		};
/******/ 	})();
/******/ 	
/************************************************************************/
var __webpack_exports__ = {};
// This entry need to be wrapped in an IIFE because it need to be isolated against other modules in the chunk.
(() => {
/*!**********************!*\
  !*** ./src/index.ts ***!
  \**********************/
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var node_brigadier__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! node-brigadier */ "./node_modules/node-brigadier/dist/index.js");
/* harmony import */ var node_brigadier__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(node_brigadier__WEBPACK_IMPORTED_MODULE_0__);
/* harmony import */ var _arguments__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./arguments */ "./src/arguments.ts");
// @ts-check


/******************************************************************************
 * Classes & Interfaces                                                       *
 ******************************************************************************/
class MyCommandDispatcher extends node_brigadier__WEBPACK_IMPORTED_MODULE_0__.CommandDispatcher {
    root;
    constructor(root) {
        super(root);
        this.root = root;
    }
    deleteAll() {
        this.root = new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.RootCommandNode(undefined, undefined, undefined, undefined, undefined);
    }
    getRoot() {
        return this.root;
    }
}
const SOURCE = undefined;
/******************************************************************************
 * Constants                                                                  *
 ******************************************************************************/
const COMMAND_INPUT = document.getElementById("cmd-input");
const COMMAND_INPUT_AUTOCOMPLETE = document.getElementById("cmd-input-autocomplete");
const ERROR_MESSAGE_BOX = document.getElementById("error-box");
const SUGGESTIONS_BOX = document.getElementById("suggestions-box");
const VALID_BOX = document.getElementById("valid-box");
const COMMANDS = document.getElementById("commands");
const dispatcher = new MyCommandDispatcher();
/******************************************************************************
 * Prototypes                                                                 *
 ******************************************************************************/
// @ts-ignore
// CommandDispatcher.prototype.deleteAll = function deleteAll() { this.root = new RootCommandNode(); };
/******************************************************************************
 * Enums                                                                      *
 ******************************************************************************/
var ChatColor;
(function (ChatColor) {
    // Uses the section symbol (§), just like Minecraft
    ChatColor["BLACK"] = "\u00A70";
    ChatColor["DARK_BLUE"] = "\u00A71";
    ChatColor["DARK_GREEN"] = "\u00A72";
    ChatColor["DARK_AQUA"] = "\u00A73";
    ChatColor["DARK_RED"] = "\u00A74";
    ChatColor["DARK_PURPLE"] = "\u00A75";
    ChatColor["GOLD"] = "\u00A76";
    ChatColor["GRAY"] = "\u00A77";
    ChatColor["DARK_GRAY"] = "\u00A78";
    ChatColor["BLUE"] = "\u00A79";
    ChatColor["GREEN"] = "\u00A7a";
    ChatColor["AQUA"] = "\u00A7b";
    ChatColor["RED"] = "\u00A7c";
    ChatColor["LIGHT_PURPLE"] = "\u00A7d";
    ChatColor["YELLOW"] = "\u00A7e";
    ChatColor["WHITE"] = "\u00A7f";
})(ChatColor || (ChatColor = {}));
;
const ChatColorCSS = new Map([
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
const ChatColorCSSReversed = new Map();
for (let [key, value] of ChatColorCSS) {
    ChatColorCSSReversed.set(value, key);
}
const ArgumentColors = {
    0: ChatColor.AQUA,
    1: ChatColor.YELLOW,
    2: ChatColor.GREEN,
    3: ChatColor.LIGHT_PURPLE,
    4: ChatColor.GOLD
};
// As implemented by https://commandapi.jorel.dev/8.5.1/internal.html
const ArgumentType = new Map([
    // CommandAPI separation. These are the various EntitySelectorArgument<> types
    ["api:entity", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.EntitySelectorArgument()],
    ["api:entities", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.EntitySelectorArgument()],
    ["api:player", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.EntitySelectorArgument()],
    ["api:players", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.EntitySelectorArgument()],
    ["api:greedy_string", () => (0,node_brigadier__WEBPACK_IMPORTED_MODULE_0__.greedyString)()],
    // A note about Brigadier String types:
    // Brigadier arguments
    ["brigadier:bool", () => (0,node_brigadier__WEBPACK_IMPORTED_MODULE_0__.bool)()],
    ["brigadier:double", () => (0,node_brigadier__WEBPACK_IMPORTED_MODULE_0__.float)()],
    ["brigadier:float", () => (0,node_brigadier__WEBPACK_IMPORTED_MODULE_0__.float)()],
    ["brigadier:integer", () => (0,node_brigadier__WEBPACK_IMPORTED_MODULE_0__.integer)()],
    ["brigadier:long", () => (0,node_brigadier__WEBPACK_IMPORTED_MODULE_0__.integer)()],
    ["brigadier:string", () => (0,node_brigadier__WEBPACK_IMPORTED_MODULE_0__.string)()],
    // Minecraft arguments
    ["minecraft:angle", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.AngleArgument()],
    ["minecraft:block_pos", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.BlockPosArgument()],
    ["minecraft:block_predicate", () => null],
    ["minecraft:block_state", () => null],
    ["minecraft:color", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.ColorArgument()],
    ["minecraft:column_pos", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.ColumnPosArgument()],
    ["minecraft:component", () => null],
    ["minecraft:dimension", () => null],
    ["minecraft:entity", () => null],
    ["minecraft:entity_anchor", () => null],
    ["minecraft:entity_summon", () => null],
    ["minecraft:float_range", () => null],
    ["minecraft:function", () => null],
    ["minecraft:game_profile", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.PlayerArgument()],
    ["minecraft:int_range", () => null],
    ["minecraft:item_enchantment", () => null],
    ["minecraft:item_predicate", () => null],
    ["minecraft:item_slot", () => null],
    ["minecraft:item_stack", () => null],
    ["minecraft:message", () => (0,node_brigadier__WEBPACK_IMPORTED_MODULE_0__.greedyString)()],
    ["minecraft:mob_effect", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.PotionEffectArgument()],
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
    ["minecraft:team", () => (0,node_brigadier__WEBPACK_IMPORTED_MODULE_0__.word)()],
    ["minecraft:time", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.TimeArgument()],
    ["minecraft:uuid", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UUIDArgument()],
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
function registerCommand(configCommand) {
    // No blank commands
    if (configCommand.trim().length === 0) {
        return;
    }
    function convertArgument(argumentType) {
        if (argumentType.includes("..")) {
            let lowerBound = argumentType.split("..")[0];
            let upperBound = argumentType.split("..")[1];
            let lowerBoundNum = Number.MIN_SAFE_INTEGER;
            let upperBoundNum = Number.MAX_SAFE_INTEGER;
            if (lowerBound.length === 0) {
                lowerBoundNum = Number.MIN_SAFE_INTEGER;
            }
            else {
                lowerBoundNum = Number.parseFloat(lowerBound);
            }
            if (upperBound.length === 0) {
                upperBoundNum = Number.MAX_SAFE_INTEGER;
            }
            else {
                upperBoundNum = Number.parseFloat(upperBound);
            }
            // We've got a decimal number, use a float argument
            if (lowerBoundNum % 1 !== 0 || upperBoundNum % 1 !== 0) {
                return (0,node_brigadier__WEBPACK_IMPORTED_MODULE_0__.float)(lowerBoundNum, upperBoundNum);
            }
            else {
                // Inclusive upper bound
                upperBoundNum += 1;
                return (0,node_brigadier__WEBPACK_IMPORTED_MODULE_0__.integer)(lowerBoundNum, upperBoundNum);
            }
        }
        else {
            const argumentGeneratorFunction = ArgumentType.get(argumentType);
            if (argumentGeneratorFunction === null) {
                // TODO: Error, this argument type doesn't exist!
                console.error("Argument type " + argumentType + " doesn't exist");
            }
            if (argumentGeneratorFunction()) {
                return argumentGeneratorFunction();
            }
            else {
                console.error("Unimplemented argument: " + argumentType);
                return null;
            }
        }
    }
    const command = configCommand.split(" ")[0];
    const args = configCommand.split(" ").slice(1);
    let commandToRegister = (0,node_brigadier__WEBPACK_IMPORTED_MODULE_0__.literal)(command);
    let argumentsToRegister = [];
    // From dev/jorel/commandapi/AdvancedConverter.java
    const literalPattern = RegExp(/\((\w+(?:\|\w+)*)\)/);
    const argumentPattern = RegExp(/<(\w+)>\[([a-z:_]+|(?:[0-9\.]+)?\.\.(?:[0-9\.]+)?)\]/);
    for (let arg of args) {
        const matchedLiteral = arg.match(literalPattern);
        const matchedArgument = arg.match(argumentPattern);
        if (matchedLiteral) {
            // It's a literal argument
            const literals = matchedLiteral[1].split("|");
            if (literals.length === 1) {
                argumentsToRegister.unshift((0,node_brigadier__WEBPACK_IMPORTED_MODULE_0__.literal)(literals[0]));
            }
            else if (literals.length > 1) {
                argumentsToRegister.unshift((0,node_brigadier__WEBPACK_IMPORTED_MODULE_0__.argument)(matchedLiteral[1], new _arguments__WEBPACK_IMPORTED_MODULE_1__.MultiLiteralArgument(literals)));
            }
        }
        else if (matchedArgument) {
            // It's a regular argument
            const nodeName = matchedArgument[1];
            const argumentType = matchedArgument[2];
            let convertedArgumentType = convertArgument(argumentType);
            // We're adding arguments in reverse order (last arguments appear
            // at the beginning of the array) because it's much much easier to process
            argumentsToRegister.unshift((0,node_brigadier__WEBPACK_IMPORTED_MODULE_0__.argument)(nodeName, convertedArgumentType));
        }
    }
    if (argumentsToRegister.length > 0) {
        const lastArgument = argumentsToRegister[0].executes(_context => 0);
        // Flame on. Reduce.
        argumentsToRegister.shift();
        const reducedArguments = argumentsToRegister.reduce((prev, current) => current.then(prev), lastArgument);
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
    const sel = document.getSelection();
    sel.modify("extend", "backward", "paragraphboundary");
    const pos = sel.toString().length;
    if (sel.anchorNode !== undefined && sel.anchorNode !== null) {
        sel.collapseToEnd();
    }
    return pos;
}
;
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
function setCursorPosition(index, element) {
    if (index >= 0) {
        const createRange = (node, chars, range) => {
            if (!range) {
                range = document.createRange();
                range.selectNode(node);
                range.setStart(node, 0);
            }
            if (chars.count === 0) {
                range.setEnd(node, chars.count);
            }
            else if (node && chars.count > 0) {
                if (node.nodeType === Node.TEXT_NODE) {
                    const nodeTextContentLength = node.textContent.length;
                    if (nodeTextContentLength < chars.count) {
                        chars.count -= nodeTextContentLength;
                    }
                    else {
                        range.setEnd(node, chars.count);
                        chars.count = 0;
                    }
                }
                else {
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
}
;
function getSelectedSuggestion() {
    return document.querySelector(".yellow");
}
class TextWidth {
    static canvas;
    /**
     * Uses canvas.measureText to compute and return the width of the given text of given font in pixels.
     *
     * @param {String} text The text to be rendered.
     * @param {HTMLElement} element the element
     *
     * @see https://stackoverflow.com/questions/118241/calculate-text-width-with-javascript/21015393#21015393
     */
    static getTextWidth(text, element) {
        // re-use canvas object for better performance
        const canvas = TextWidth.canvas || (TextWidth.canvas = document.createElement("canvas"));
        const context = canvas.getContext("2d");
        context.font = element.currentFont || (element.currentFont = TextWidth.getCanvasFont(element));
        return context.measureText(text).width;
    }
    static getCssStyle(element, prop) {
        return window.getComputedStyle(element).getPropertyValue(prop);
    }
    static getCanvasFont(el = document.body) {
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
function setText(minecraftCodedText, target = null) {
    minecraftCodedText = minecraftCodedText.replaceAll(" ", "\u00A0"); // Replace normal spaces with &nbsp; for HTML
    if (!target) {
        target = COMMAND_INPUT;
    }
    // Reset the text
    target.innerHTML = "";
    if (target === COMMAND_INPUT) {
        // Command forward slash. Always present, we don't want to remove this!
        let element = document.createElement("span");
        element.innerText = "/";
        target.appendChild(element);
    }
    let buffer = "";
    let currentColor = "";
    function writeBuffer(target) {
        if (buffer.length > 0) {
            let elem = document.createElement("span");
            elem.className = currentColor;
            elem.innerText = buffer;
            target.appendChild(elem);
            buffer = "";
        }
    }
    ;
    for (let i = 0; i < minecraftCodedText.length; i++) {
        if (minecraftCodedText[i] === "\u00A7") {
            writeBuffer(target);
            currentColor = ChatColorCSS.get(minecraftCodedText[i + 1]);
            i++;
            continue;
        }
        else {
            buffer += minecraftCodedText[i];
        }
    }
    writeBuffer(target);
}
function getText(withStyling = true) {
    let buffer = "";
    for (let child of COMMAND_INPUT.children) {
        if (child.className && withStyling) {
            buffer += "\u00A7" + ChatColorCSSReversed.get(child.className);
        }
        buffer += child.innerText;
    }
    return buffer;
}
/******************************************************************************
 * Events                                                                     *
 ******************************************************************************/
COMMAND_INPUT.oninput = async function onCommandInput() {
    let cursorPos = getCursorPosition();
    let rawText = COMMAND_INPUT.innerText.replace("\n", "");
    rawText = rawText.replaceAll("\u00a0", " "); // Replace &nbsp; with normal spaces for Brigadier
    let showUsageText = false;
    let errorText = "";
    let suggestions = [];
    let commandValid = false;
    // Render colors
    if (rawText.startsWith("/")) {
        // Parse the raw text
        const rawTextNoSlash = rawText.slice(1);
        const command = rawTextNoSlash.split(" ")[0];
        // Brigadier
        const parsedCommand = dispatcher.parse(rawTextNoSlash, SOURCE);
        const parsedCommandNoTrailing = dispatcher.parse(rawTextNoSlash.trimEnd(), SOURCE);
        console.log(parsedCommand);
        let lastNode = parsedCommandNoTrailing.getContext().getRootNode();
        if (parsedCommandNoTrailing.getContext().getNodes().length > 0) {
            lastNode = parsedCommandNoTrailing.getContext().getNodes()[parsedCommandNoTrailing.getContext().getNodes().length - 1].getNode();
        }
        const usage = dispatcher.getAllUsage(lastNode, SOURCE, false).join(" ");
        // Reset text
        setText(rawTextNoSlash);
        if (parsedCommand.getExceptions().size > 0) {
            // The command is invalid (the command doesn't exist). Make the whole text red.
            setText(ChatColor.RED + rawTextNoSlash);
            const exceptions = parsedCommand.getExceptions();
            errorText = exceptions.entries().next().value[1].message;
        }
        else {
            // Brigadier is "happy" with the input. Let's run it and see!
            try {
                dispatcher.execute(parsedCommand);
            }
            catch (ex) {
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
            let newText = command;
            let parsedArgumentIndex = 0;
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
        const suggestionsResult = await dispatcher.getCompletionSuggestions(parsedCommand);
        suggestions = suggestionsResult.getList().map((x) => x.getText());
        console.log(suggestions);
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
    }
    else {
        ERROR_MESSAGE_BOX.hidden = true;
    }
    if (showUsageText) {
        ERROR_MESSAGE_BOX.style.left = TextWidth.getTextWidth(rawText, COMMAND_INPUT) + "px";
        // 8px padding, 10px margin left, 10px margin right = -28px
        // Plus an extra 10px for good luck, why not
        ERROR_MESSAGE_BOX.style.width = `calc(100% - ${ERROR_MESSAGE_BOX.style.left} - 28px + 10px)`;
    }
    else {
        ERROR_MESSAGE_BOX.style.left = "0";
        ERROR_MESSAGE_BOX.style.width = "unset";
    }
    if (commandValid) {
        setText(ChatColor.GREEN + "This command is valid ✅", VALID_BOX);
        VALID_BOX.hidden = false;
    }
    else {
        VALID_BOX.hidden = true;
    }
    const constructSuggestionsHTML = (suggestions) => {
        let nodesToAdd = [];
        for (let i = 0; i < suggestions.length; i++) {
            const suggestionElement = document.createElement("span");
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
        SUGGESTIONS_BOX.style.left = TextWidth.getTextWidth(rawText, COMMAND_INPUT) + "px";
        // 8px padding, 10px margin left, 10px margin right = -28px
        // Plus an extra 10px for good luck, why not
        SUGGESTIONS_BOX.hidden = false;
        ERROR_MESSAGE_BOX.hidden = true;
    }
    else {
        SUGGESTIONS_BOX.hidden = true;
    }
    window.dispatchEvent(new Event("suggestionsUpdated"));
};
// We really really don't want new lines in our single-lined command!
COMMAND_INPUT.addEventListener('keydown', (evt) => {
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
                            }
                            else {
                                SUGGESTIONS_BOX.children[i + 1].className = "yellow";
                            }
                        }
                        else {
                            if (i === 0) {
                                SUGGESTIONS_BOX.children[SUGGESTIONS_BOX.children.length - 1].className = "yellow";
                            }
                            else {
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
window.addEventListener("suggestionsUpdated", (_event) => {
    const rawText = COMMAND_INPUT.innerText.replaceAll("\u00a0", " "); // Replace &nbsp; with normal spaces
    if (!SUGGESTIONS_BOX.hidden) {
        const selectedSuggestionText = getSelectedSuggestion().innerText.trim();
        // TODO: This obviously needs to be specific to the current suggestions, not the whole input
        if (rawText !== selectedSuggestionText) {
            const cursorPosition = getCursorPosition();
            const lastSpaceIndex = rawText.lastIndexOf(" ") === -1 ? 1 : rawText.lastIndexOf(" ") + 1;
            setText(ChatColor.DARK_GRAY + selectedSuggestionText.slice(rawText.slice(lastSpaceIndex).length), COMMAND_INPUT_AUTOCOMPLETE);
            setCursorPosition(cursorPosition, COMMAND_INPUT);
            COMMAND_INPUT.focus();
        }
        else {
            setText("", COMMAND_INPUT_AUTOCOMPLETE);
        }
    }
    else {
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
};
/******************************************************************************
 * Entrypoint                                                                 *
 ******************************************************************************/
// Default commands
COMMANDS.value = `fill <pos1>[minecraft:block_pos] <pos2>[minecraft:block_pos] <block>[brigadier:string]
speed (walk|fly) <speed>[0..10] <target>[minecraft:game_profile]
hello <val>[1..20] <color>[minecraft:color]
myfunc <val>[minecraft:mob_effect]
entity <vala>[minecraft:entities]`;
document.getElementById("register-commands-button")?.onclick(null);
console.log("Dispatcher", dispatcher.getRoot());

})();

/******/ })()
;
//# sourceMappingURL=data:application/json;charset=utf-8;base64,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QUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLGlCQUFpQjtBQUNqQjtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFD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QUFLLEVBQUUsQ0FBQztTQUN0QjthQUFNO1lBQ04sT0FBTyxDQUFDLEVBQUUsRUFBRSwwQkFBMEIsQ0FBQyxDQUFDO1NBQ3hDO0tBQ0Q7U0FBTTtRQUNOLE9BQU8sQ0FBQyxFQUFFLEVBQUUsMEJBQTBCLENBQUMsQ0FBQztLQUN4QztBQUNGLENBQUMsQ0FBQyxDQUFDO0FBRUgsbUVBQW1FO0FBQ25FLFFBQVEsQ0FBQyxjQUFjLENBQUMsU0FBUyxDQUFDLENBQUMsT0FBTyxHQUFHLFNBQVMsZ0JBQWdCO0lBQ3JFLGFBQWEsQ0FBQyxLQUFLLEVBQUUsQ0FBQztBQUN2QixDQUFDLENBQUM7QUFFRixRQUFRLENBQUMsY0FBYyxDQUFDLDBCQUEwQixDQUFDLENBQUMsT0FBTyxHQUFHLFNBQVMsK0JBQStCO0lBQ3JHLFVBQVUsQ0FBQyxTQUFTLEVBQUUsQ0FBQztJQUN2QixRQUFRLENBQUMsS0FBSyxDQUFDLEtBQUssQ0FBQyxJQUFJLENBQUMsQ0FBQyxPQUFPLENBQUMsZUFBZSxDQUFDLENBQUM7SUFDcEQsYUFBYSxDQUFDLE9BQU8sQ0FBQyxJQUFJLENBQUMsQ0FBQyxDQUFDLHlCQUF5QjtBQUN2RCxDQUFDO0FBR0Q7O2dGQUVnRjtBQUVoRixtQkFBbUI7QUFDbkIsUUFBUSxDQUFDLEtBQUssR0FBRzs7OztrQ0FJaUIsQ0FBQztBQUVuQyxRQUFRLENBQUMsY0FBYyxDQUFDLDBCQUEwQixDQUFDLEVBQUUsT0FBTyxDQUFDLElBQUksQ0FBQyxDQUFDO0FBQ25FLE9BQU8sQ0FBQyxHQUFHLENBQUMsWUFBWSxFQUFFLFVBQVUsQ0FBQyxPQUFPLEVBQUUsQ0FBQyIsInNvdXJjZXMiOlsid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vbm9kZV9tb2R1bGVzL25vZGUtYnJpZ2FkaWVyL2Rpc3QvaW5kZXguanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvQ29tbWFuZERpc3BhdGNoZXIuanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvTGl0ZXJhbE1lc3NhZ2UuanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvUGFyc2VSZXN1bHRzLmpzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vbm9kZV9tb2R1bGVzL25vZGUtYnJpZ2FkaWVyL2Rpc3QvbGliL1N0cmluZ1JlYWRlci5qcyIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL25vZGVfbW9kdWxlcy9ub2RlLWJyaWdhZGllci9kaXN0L2xpYi9hcmd1bWVudHMvQXJndW1lbnRUeXBlLmpzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vbm9kZV9tb2R1bGVzL25vZGUtYnJpZ2FkaWVyL2Rpc3QvbGliL2FyZ3VtZW50cy9Cb29sQXJndW1lbnRUeXBlLmpzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vbm9kZV9tb2R1bGVzL25vZGUtYnJpZ2FkaWVyL2Rpc3QvbGliL2FyZ3VtZW50cy9GbG9hdEFyZ3VtZW50VHlwZS5qcyIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL25vZGVfbW9kdWxlcy9ub2RlLWJyaWdhZGllci9kaXN0L2xpYi9hcmd1bWVudHMvSW50ZWdlckFyZ3VtZW50VHlwZS5qcyIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL25vZGVfbW9kdWxlcy9ub2RlLWJyaWdhZGllci9kaXN0L2xpYi9hcmd1bWVudHMvU3RyaW5nQXJndW1lbnRUeXBlLmpzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vbm9kZV9tb2R1bGVzL25vZGUtYnJpZ2FkaWVyL2Rpc3QvbGliL2J1aWxkZXIvQXJndW1lbnRCdWlsZGVyLmpzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vbm9kZV9tb2R1bGVzL25vZGUtYnJpZ2FkaWVyL2Rpc3QvbGliL2J1aWxkZXIvTGl0ZXJhbEFyZ3VtZW50QnVpbGRlci5qcyIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL25vZGVfbW9kdWxlcy9ub2RlLWJyaWdhZGllci9kaXN0L2xpYi9idWlsZGVyL1JlcXVpcmVkQXJndW1lbnRCdWlsZGVyLmpzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vbm9kZV9tb2R1bGVzL25vZGUtYnJpZ2FkaWVyL2Rpc3QvbGliL2NvbnRleHQvQ29tbWFuZENvbnRleHQuanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvY29udGV4dC9Db21tYW5kQ29udGV4dEJ1aWxkZXIuanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvY29udGV4dC9QYXJzZWRBcmd1bWVudC5qcyIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL25vZGVfbW9kdWxlcy9ub2RlLWJyaWdhZGllci9kaXN0L2xpYi9jb250ZXh0L1BhcnNlZENvbW1hbmROb2RlLmpzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vbm9kZV9tb2R1bGVzL25vZGUtYnJpZ2FkaWVyL2Rpc3QvbGliL2NvbnRleHQvU3RyaW5nUmFuZ2UuanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvY29udGV4dC9TdWdnZXN0aW9uQ29udGV4dC5qcyIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL25vZGVfbW9kdWxlcy9ub2RlLWJyaWdhZGllci9kaXN0L2xpYi9leGNlcHRpb25zL0J1aWx0SW5FeGNlcHRpb25zLmpzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vbm9kZV9tb2R1bGVzL25vZGUtYnJpZ2FkaWVyL2Rpc3QvbGliL2V4Y2VwdGlvbnMvQ29tbWFuZFN5bnRheEV4Y2VwdGlvbi5qcyIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL25vZGVfbW9kdWxlcy9ub2RlLWJyaWdhZGllci9kaXN0L2xpYi9leGNlcHRpb25zL0R5bmFtaWNDb21tYW5kRXhjZXB0aW9uVHlwZS5qcyIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL25vZGVfbW9kdWxlcy9ub2RlLWJyaWdhZGllci9kaXN0L2xpYi9leGNlcHRpb25zL1NpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlLmpzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vbm9kZV9tb2R1bGVzL25vZGUtYnJpZ2FkaWVyL2Rpc3QvbGliL3N1Z2dlc3Rpb24vSW50ZWdlclN1Z2dlc3Rpb24uanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvc3VnZ2VzdGlvbi9TdWdnZXN0aW9uLmpzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vbm9kZV9tb2R1bGVzL25vZGUtYnJpZ2FkaWVyL2Rpc3QvbGliL3N1Z2dlc3Rpb24vU3VnZ2VzdGlvbnMuanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvc3VnZ2VzdGlvbi9TdWdnZXN0aW9uc0J1aWxkZXIuanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvdHJlZS9Bcmd1bWVudENvbW1hbmROb2RlLmpzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vbm9kZV9tb2R1bGVzL25vZGUtYnJpZ2FkaWVyL2Rpc3QvbGliL3RyZWUvQ29tbWFuZE5vZGUuanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvdHJlZS9MaXRlcmFsQ29tbWFuZE5vZGUuanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvdHJlZS9Sb290Q29tbWFuZE5vZGUuanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvdXRpbC9pc0VxdWFsLmpzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vc3JjL2FyZ3VtZW50cy50cyIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci93ZWJwYWNrL2Jvb3RzdHJhcCIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci93ZWJwYWNrL3J1bnRpbWUvY29tcGF0IGdldCBkZWZhdWx0IGV4cG9ydCIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci93ZWJwYWNrL3J1bnRpbWUvZGVmaW5lIHByb3BlcnR5IGdldHRlcnMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvd2VicGFjay9ydW50aW1lL2hhc093blByb3BlcnR5IHNob3J0aGFuZCIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci93ZWJwYWNrL3J1bnRpbWUvbWFrZSBuYW1lc3BhY2Ugb2JqZWN0Iiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vc3JjL2luZGV4LnRzIl0sInNvdXJjZXNDb250ZW50IjpbIlwidXNlIHN0cmljdFwiO1xyXG52YXIgX19pbXBvcnREZWZhdWx0ID0gKHRoaXMgJiYgdGhpcy5fX2ltcG9ydERlZmF1bHQpIHx8IGZ1bmN0aW9uIChtb2QpIHtcclxuICAgIHJldHVybiAobW9kICYmIG1vZC5fX2VzTW9kdWxlKSA/IG1vZCA6IHsgXCJkZWZhdWx0XCI6IG1vZCB9O1xyXG59O1xyXG52YXIgX19pbXBvcnRTdGFyID0gKHRoaXMgJiYgdGhpcy5fX2ltcG9ydFN0YXIpIHx8IGZ1bmN0aW9uIChtb2QpIHtcclxuICAgIGlmIChtb2QgJiYgbW9kLl9fZXNNb2R1bGUpIHJldHVybiBtb2Q7XHJcbiAgICB2YXIgcmVzdWx0ID0ge307XHJcbiAgICBpZiAobW9kICE9IG51bGwpIGZvciAodmFyIGsgaW4gbW9kKSBpZiAoT2JqZWN0Lmhhc093blByb3BlcnR5LmNhbGwobW9kLCBrKSkgcmVzdWx0W2tdID0gbW9kW2tdO1xyXG4gICAgcmVzdWx0W1wiZGVmYXVsdFwiXSA9IG1vZDtcclxuICAgIHJldHVybiByZXN1bHQ7XHJcbn07XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY29uc3QgQ29tbWFuZERpc3BhdGNoZXJfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9saWIvQ29tbWFuZERpc3BhdGNoZXJcIikpO1xyXG5jb25zdCBMaXRlcmFsTWVzc2FnZV8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL2xpYi9MaXRlcmFsTWVzc2FnZVwiKSk7XHJcbmNvbnN0IFBhcnNlUmVzdWx0c18xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL2xpYi9QYXJzZVJlc3VsdHNcIikpO1xyXG5jb25zdCBTdHJpbmdSZWFkZXJfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9saWIvU3RyaW5nUmVhZGVyXCIpKTtcclxuY29uc3QgQXJndW1lbnRUeXBlXzEgPSByZXF1aXJlKFwiLi9saWIvYXJndW1lbnRzL0FyZ3VtZW50VHlwZVwiKTtcclxuY29uc3QgTGl0ZXJhbEFyZ3VtZW50QnVpbGRlcl8xID0gX19pbXBvcnRTdGFyKHJlcXVpcmUoXCIuL2xpYi9idWlsZGVyL0xpdGVyYWxBcmd1bWVudEJ1aWxkZXJcIikpO1xyXG5jb25zdCBSZXF1aXJlZEFyZ3VtZW50QnVpbGRlcl8xID0gX19pbXBvcnRTdGFyKHJlcXVpcmUoXCIuL2xpYi9idWlsZGVyL1JlcXVpcmVkQXJndW1lbnRCdWlsZGVyXCIpKTtcclxuY29uc3QgQ29tbWFuZENvbnRleHRfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9saWIvY29udGV4dC9Db21tYW5kQ29udGV4dFwiKSk7XHJcbmNvbnN0IENvbW1hbmRDb250ZXh0QnVpbGRlcl8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL2xpYi9jb250ZXh0L0NvbW1hbmRDb250ZXh0QnVpbGRlclwiKSk7XHJcbmNvbnN0IFBhcnNlZEFyZ3VtZW50XzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vbGliL2NvbnRleHQvUGFyc2VkQXJndW1lbnRcIikpO1xyXG5jb25zdCBQYXJzZWRDb21tYW5kTm9kZV8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL2xpYi9jb250ZXh0L1BhcnNlZENvbW1hbmROb2RlXCIpKTtcclxuY29uc3QgU3RyaW5nUmFuZ2VfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9saWIvY29udGV4dC9TdHJpbmdSYW5nZVwiKSk7XHJcbmNvbnN0IFN1Z2dlc3Rpb25Db250ZXh0XzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vbGliL2NvbnRleHQvU3VnZ2VzdGlvbkNvbnRleHRcIikpO1xyXG5jb25zdCBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vbGliL2V4Y2VwdGlvbnMvQ29tbWFuZFN5bnRheEV4Y2VwdGlvblwiKSk7XHJcbmNvbnN0IER5bmFtaWNDb21tYW5kRXhjZXB0aW9uVHlwZV8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL2xpYi9leGNlcHRpb25zL0R5bmFtaWNDb21tYW5kRXhjZXB0aW9uVHlwZVwiKSk7XHJcbmNvbnN0IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vbGliL2V4Y2VwdGlvbnMvU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGVcIikpO1xyXG5jb25zdCBTdWdnZXN0aW9uXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vbGliL3N1Z2dlc3Rpb24vU3VnZ2VzdGlvblwiKSk7XHJcbmNvbnN0IFN1Z2dlc3Rpb25zXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vbGliL3N1Z2dlc3Rpb24vU3VnZ2VzdGlvbnNcIikpO1xyXG5jb25zdCBTdWdnZXN0aW9uc0J1aWxkZXJfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9saWIvc3VnZ2VzdGlvbi9TdWdnZXN0aW9uc0J1aWxkZXJcIikpO1xyXG5jb25zdCBBcmd1bWVudENvbW1hbmROb2RlXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vbGliL3RyZWUvQXJndW1lbnRDb21tYW5kTm9kZVwiKSk7XHJcbmNvbnN0IExpdGVyYWxDb21tYW5kTm9kZV8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL2xpYi90cmVlL0xpdGVyYWxDb21tYW5kTm9kZVwiKSk7XHJcbmNvbnN0IFJvb3RDb21tYW5kTm9kZV8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL2xpYi90cmVlL1Jvb3RDb21tYW5kTm9kZVwiKSk7XHJcbmNvbnN0IHsgd29yZCwgc3RyaW5nLCBncmVlZHlTdHJpbmcsIGJvb2wsIGludGVnZXIsIGZsb2F0IH0gPSBBcmd1bWVudFR5cGVfMS5EZWZhdWx0VHlwZTtcclxubW9kdWxlLmV4cG9ydHMgPSB7XHJcbiAgICBkaXNwYXRjaGVyOiBuZXcgQ29tbWFuZERpc3BhdGNoZXJfMS5kZWZhdWx0KCksXHJcbiAgICB3b3JkLCBzdHJpbmcsIGdyZWVkeVN0cmluZywgYm9vbCwgaW50ZWdlciwgZmxvYXQsXHJcbiAgICBsaXRlcmFsOiBMaXRlcmFsQXJndW1lbnRCdWlsZGVyXzEubGl0ZXJhbCwgYXJndW1lbnQ6IFJlcXVpcmVkQXJndW1lbnRCdWlsZGVyXzEuYXJndW1lbnQsXHJcbiAgICBDb21tYW5kRGlzcGF0Y2hlcjogQ29tbWFuZERpc3BhdGNoZXJfMS5kZWZhdWx0LFxyXG4gICAgTGl0ZXJhbE1lc3NhZ2U6IExpdGVyYWxNZXNzYWdlXzEuZGVmYXVsdCxcclxuICAgIFBhcnNlUmVzdWx0czogUGFyc2VSZXN1bHRzXzEuZGVmYXVsdCxcclxuICAgIFN0cmluZ1JlYWRlcjogU3RyaW5nUmVhZGVyXzEuZGVmYXVsdCxcclxuICAgIExpdGVyYWxBcmd1bWVudEJ1aWxkZXI6IExpdGVyYWxBcmd1bWVudEJ1aWxkZXJfMS5kZWZhdWx0LFxyXG4gICAgUmVxdWlyZWRBcmd1bWVudEJ1aWxkZXI6IFJlcXVpcmVkQXJndW1lbnRCdWlsZGVyXzEuZGVmYXVsdCxcclxuICAgIENvbW1hbmRDb250ZXh0OiBDb21tYW5kQ29udGV4dF8xLmRlZmF1bHQsXHJcbiAgICBDb21tYW5kQ29udGV4dEJ1aWxkZXI6IENvbW1hbmRDb250ZXh0QnVpbGRlcl8xLmRlZmF1bHQsXHJcbiAgICBQYXJzZWRBcmd1bWVudDogUGFyc2VkQXJndW1lbnRfMS5kZWZhdWx0LFxyXG4gICAgUGFyc2VkQ29tbWFuZE5vZGU6IFBhcnNlZENvbW1hbmROb2RlXzEuZGVmYXVsdCxcclxuICAgIFN0cmluZ1JhbmdlOiBTdHJpbmdSYW5nZV8xLmRlZmF1bHQsXHJcbiAgICBTdWdnZXN0aW9uc0NvbnRleHQ6IFN1Z2dlc3Rpb25Db250ZXh0XzEuZGVmYXVsdCxcclxuICAgIENvbW1hbmRTeW50YXhFeGNlcHRpb246IENvbW1hbmRTeW50YXhFeGNlcHRpb25fMS5kZWZhdWx0LFxyXG4gICAgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGU6IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlXzEuZGVmYXVsdCxcclxuICAgIER5bmFtaWNDb21tYW5kRXhjZXB0aW9uVHlwZTogRHluYW1pY0NvbW1hbmRFeGNlcHRpb25UeXBlXzEuZGVmYXVsdCxcclxuICAgIFN1Z2dlc3Rpb246IFN1Z2dlc3Rpb25fMS5kZWZhdWx0LFxyXG4gICAgU3VnZ2VzdGlvbnM6IFN1Z2dlc3Rpb25zXzEuZGVmYXVsdCxcclxuICAgIFN1Z2dlc3Rpb25zQnVpbGRlcjogU3VnZ2VzdGlvbnNCdWlsZGVyXzEuZGVmYXVsdCxcclxuICAgIEFyZ3VtZW50Q29tbWFuZE5vZGU6IEFyZ3VtZW50Q29tbWFuZE5vZGVfMS5kZWZhdWx0LFxyXG4gICAgTGl0ZXJhbENvbW1hbmROb2RlOiBMaXRlcmFsQ29tbWFuZE5vZGVfMS5kZWZhdWx0LFxyXG4gICAgUm9vdENvbW1hbmROb2RlOiBSb290Q29tbWFuZE5vZGVfMS5kZWZhdWx0XHJcbn07XHJcbiIsIlwidXNlIHN0cmljdFwiO1xyXG52YXIgX19hd2FpdGVyID0gKHRoaXMgJiYgdGhpcy5fX2F3YWl0ZXIpIHx8IGZ1bmN0aW9uICh0aGlzQXJnLCBfYXJndW1lbnRzLCBQLCBnZW5lcmF0b3IpIHtcclxuICAgIHJldHVybiBuZXcgKFAgfHwgKFAgPSBQcm9taXNlKSkoZnVuY3Rpb24gKHJlc29sdmUsIHJlamVjdCkge1xyXG4gICAgICAgIGZ1bmN0aW9uIGZ1bGZpbGxlZCh2YWx1ZSkgeyB0cnkgeyBzdGVwKGdlbmVyYXRvci5uZXh0KHZhbHVlKSk7IH0gY2F0Y2ggKGUpIHsgcmVqZWN0KGUpOyB9IH1cclxuICAgICAgICBmdW5jdGlvbiByZWplY3RlZCh2YWx1ZSkgeyB0cnkgeyBzdGVwKGdlbmVyYXRvcltcInRocm93XCJdKHZhbHVlKSk7IH0gY2F0Y2ggKGUpIHsgcmVqZWN0KGUpOyB9IH1cclxuICAgICAgICBmdW5jdGlvbiBzdGVwKHJlc3VsdCkgeyByZXN1bHQuZG9uZSA/IHJlc29sdmUocmVzdWx0LnZhbHVlKSA6IG5ldyBQKGZ1bmN0aW9uIChyZXNvbHZlKSB7IHJlc29sdmUocmVzdWx0LnZhbHVlKTsgfSkudGhlbihmdWxmaWxsZWQsIHJlamVjdGVkKTsgfVxyXG4gICAgICAgIHN0ZXAoKGdlbmVyYXRvciA9IGdlbmVyYXRvci5hcHBseSh0aGlzQXJnLCBfYXJndW1lbnRzIHx8IFtdKSkubmV4dCgpKTtcclxuICAgIH0pO1xyXG59O1xyXG52YXIgX19pbXBvcnREZWZhdWx0ID0gKHRoaXMgJiYgdGhpcy5fX2ltcG9ydERlZmF1bHQpIHx8IGZ1bmN0aW9uIChtb2QpIHtcclxuICAgIHJldHVybiAobW9kICYmIG1vZC5fX2VzTW9kdWxlKSA/IG1vZCA6IHsgXCJkZWZhdWx0XCI6IG1vZCB9O1xyXG59O1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNvbnN0IFBhcnNlUmVzdWx0c18xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL1BhcnNlUmVzdWx0c1wiKSk7XHJcbmNvbnN0IENvbW1hbmRDb250ZXh0QnVpbGRlcl8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL2NvbnRleHQvQ29tbWFuZENvbnRleHRCdWlsZGVyXCIpKTtcclxuY29uc3QgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbl8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL2V4Y2VwdGlvbnMvQ29tbWFuZFN5bnRheEV4Y2VwdGlvblwiKSk7XHJcbmNvbnN0IFN1Z2dlc3Rpb25zXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vc3VnZ2VzdGlvbi9TdWdnZXN0aW9uc1wiKSk7XHJcbmNvbnN0IFN1Z2dlc3Rpb25zQnVpbGRlcl8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL3N1Z2dlc3Rpb24vU3VnZ2VzdGlvbnNCdWlsZGVyXCIpKTtcclxuY29uc3QgUm9vdENvbW1hbmROb2RlXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vdHJlZS9Sb290Q29tbWFuZE5vZGVcIikpO1xyXG5jb25zdCBTdHJpbmdSZWFkZXJfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9TdHJpbmdSZWFkZXJcIikpO1xyXG5jb25zdCBBUkdVTUVOVF9TRVBBUkFUT1IgPSBcIiBcIjtcclxuY29uc3QgVVNBR0VfT1BUSU9OQUxfT1BFTiA9IFwiW1wiO1xyXG5jb25zdCBVU0FHRV9PUFRJT05BTF9DTE9TRSA9IFwiXVwiO1xyXG5jb25zdCBVU0FHRV9SRVFVSVJFRF9PUEVOID0gXCIoXCI7XHJcbmNvbnN0IFVTQUdFX1JFUVVJUkVEX0NMT1NFID0gXCIpXCI7XHJcbmNvbnN0IFVTQUdFX09SID0gXCJ8XCI7XHJcbmNsYXNzIENvbW1hbmREaXNwYXRjaGVyIHtcclxuICAgIGNvbnN0cnVjdG9yKHJvb3QgPSBudWxsKSB7XHJcbiAgICAgICAgdGhpcy5jb25zdW1lciA9IHtcclxuICAgICAgICAgICAgb25Db21tYW5kQ29tcGxldGUoKSB7IH1cclxuICAgICAgICB9O1xyXG4gICAgICAgIHRoaXMucm9vdCA9IHJvb3QgfHwgbmV3IFJvb3RDb21tYW5kTm9kZV8xLmRlZmF1bHQoKTtcclxuICAgIH1cclxuICAgIHJlZ2lzdGVyKGNvbW1hbmQpIHtcclxuICAgICAgICBsZXQgYnVpbGQgPSBjb21tYW5kLmJ1aWxkKCk7XHJcbiAgICAgICAgdGhpcy5yb290LmFkZENoaWxkKGJ1aWxkKTtcclxuICAgICAgICByZXR1cm4gYnVpbGQ7XHJcbiAgICB9XHJcbiAgICBzZXRDb25zdW1lcihjb25zdW1lcikge1xyXG4gICAgICAgIHRoaXMuY29uc3VtZXIgPSBjb25zdW1lcjtcclxuICAgIH1cclxuICAgIGV4ZWN1dGUoaW5wdXQsIHNvdXJjZSA9IG51bGwpIHtcclxuICAgICAgICBpZiAodHlwZW9mIGlucHV0ID09PSBcInN0cmluZ1wiKVxyXG4gICAgICAgICAgICBpbnB1dCA9IG5ldyBTdHJpbmdSZWFkZXJfMS5kZWZhdWx0KGlucHV0KTtcclxuICAgICAgICBsZXQgcGFyc2U7XHJcbiAgICAgICAgaWYgKGlucHV0IGluc3RhbmNlb2YgU3RyaW5nUmVhZGVyXzEuZGVmYXVsdCkge1xyXG4gICAgICAgICAgICBpZiAoIShzb3VyY2UgPT0gbnVsbCkpXHJcbiAgICAgICAgICAgICAgICBwYXJzZSA9IHRoaXMucGFyc2UoaW5wdXQsIHNvdXJjZSk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGVsc2VcclxuICAgICAgICAgICAgcGFyc2UgPSBpbnB1dDtcclxuICAgICAgICBpZiAocGFyc2UuZ2V0UmVhZGVyKCkuY2FuUmVhZCgpKSB7XHJcbiAgICAgICAgICAgIGlmIChwYXJzZS5nZXRFeGNlcHRpb25zKCkuc2l6ZSA9PT0gMSkge1xyXG4gICAgICAgICAgICAgICAgdGhyb3cgcGFyc2UuZ2V0RXhjZXB0aW9ucygpLnZhbHVlcygpLm5leHQoKS52YWx1ZTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICBlbHNlIGlmIChwYXJzZS5nZXRDb250ZXh0KCkuZ2V0UmFuZ2UoKS5pc0VtcHR5KCkpIHtcclxuICAgICAgICAgICAgICAgIHRocm93IENvbW1hbmRTeW50YXhFeGNlcHRpb25fMS5kZWZhdWx0LkJVSUxUX0lOX0VYQ0VQVElPTlMuZGlzcGF0Y2hlclVua25vd25Db21tYW5kKCkuY3JlYXRlV2l0aENvbnRleHQocGFyc2UuZ2V0UmVhZGVyKCkpO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIGVsc2Uge1xyXG4gICAgICAgICAgICAgICAgdGhyb3cgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbl8xLmRlZmF1bHQuQlVJTFRfSU5fRVhDRVBUSU9OUy5kaXNwYXRjaGVyVW5rbm93bkFyZ3VtZW50KCkuY3JlYXRlV2l0aENvbnRleHQocGFyc2UuZ2V0UmVhZGVyKCkpO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGxldCByZXN1bHQgPSAwO1xyXG4gICAgICAgIGxldCBzdWNjZXNzZnVsRm9ya3MgPSAwO1xyXG4gICAgICAgIGxldCBmb3JrZWQgPSBmYWxzZTtcclxuICAgICAgICBsZXQgZm91bmRDb21tYW5kID0gZmFsc2U7XHJcbiAgICAgICAgbGV0IGNvbW1hbmQgPSBwYXJzZS5nZXRSZWFkZXIoKS5nZXRTdHJpbmcoKTtcclxuICAgICAgICBsZXQgb3JpZ2luYWwgPSBwYXJzZS5nZXRDb250ZXh0KCkuYnVpbGQoY29tbWFuZCk7XHJcbiAgICAgICAgbGV0IGNvbnRleHRzID0gW107XHJcbiAgICAgICAgY29udGV4dHMucHVzaChvcmlnaW5hbCk7XHJcbiAgICAgICAgbGV0IG5leHQgPSBudWxsO1xyXG4gICAgICAgIHdoaWxlICghKGNvbnRleHRzID09IG51bGwpKSB7XHJcbiAgICAgICAgICAgIGZvciAobGV0IGkgPSAwOyBpIDwgY29udGV4dHMubGVuZ3RoOyBpKyspIHtcclxuICAgICAgICAgICAgICAgIGxldCBjb250ZXh0ID0gY29udGV4dHNbaV07XHJcbiAgICAgICAgICAgICAgICBsZXQgY2hpbGQgPSBjb250ZXh0LmdldENoaWxkKCk7XHJcbiAgICAgICAgICAgICAgICBpZiAoIShjaGlsZCA9PSBudWxsKSkge1xyXG4gICAgICAgICAgICAgICAgICAgIGZvcmtlZCA9IGZvcmtlZCB8fCBjb250ZXh0LmlzRm9ya2VkKCk7XHJcbiAgICAgICAgICAgICAgICAgICAgaWYgKGNoaWxkLmhhc05vZGVzKCkpIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgZm91bmRDb21tYW5kID0gdHJ1ZTtcclxuICAgICAgICAgICAgICAgICAgICAgICAgbGV0IG1vZGlmaWVyID0gY29udGV4dC5nZXRSZWRpcmVjdE1vZGlmaWVyKCk7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIGlmIChtb2RpZmllciA9PSBudWxsKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICBpZiAobmV4dCA9PSBudWxsKVxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIG5leHQgPSBbXTtcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIG5leHQucHVzaChjaGlsZC5jb3B5Rm9yKGNvbnRleHQuZ2V0U291cmNlKCkpKTtcclxuICAgICAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgICAgICAgICBlbHNlIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIHRyeSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgbGV0IHJlc3VsdHMgPSBtb2RpZmllci5hcHBseShjb250ZXh0KTtcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBpZiAocmVzdWx0cy5sZW5ndGggIT09IDApIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgaWYgKG5leHQgPT0gbnVsbClcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIG5leHQgPSBbXTtcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgZm9yIChsZXQgc291cmNlIG9mIHJlc3VsdHMpIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIG5leHQucHVzaChjaGlsZC5jb3B5Rm9yKHNvdXJjZSkpO1xyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgY2F0Y2ggKGV4KSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgdGhpcy5jb25zdW1lci5vbkNvbW1hbmRDb21wbGV0ZShjb250ZXh0LCBmYWxzZSwgMCk7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgaWYgKCFmb3JrZWQpXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIHRocm93IGV4O1xyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgZWxzZSBpZiAoY29udGV4dC5nZXRDb21tYW5kKCkgIT0gbnVsbCkge1xyXG4gICAgICAgICAgICAgICAgICAgIGZvdW5kQ29tbWFuZCA9IHRydWU7XHJcbiAgICAgICAgICAgICAgICAgICAgdHJ5IHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgbGV0IHZhbHVlID0gY29udGV4dC5nZXRDb21tYW5kKCkoY29udGV4dCk7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHJlc3VsdCArPSB2YWx1ZTtcclxuICAgICAgICAgICAgICAgICAgICAgICAgdGhpcy5jb25zdW1lci5vbkNvbW1hbmRDb21wbGV0ZShjb250ZXh0LCB0cnVlLCB2YWx1ZSk7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHN1Y2Nlc3NmdWxGb3JrcysrO1xyXG4gICAgICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgICAgICBjYXRjaCAoZXgpIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgdGhpcy5jb25zdW1lci5vbkNvbW1hbmRDb21wbGV0ZShjb250ZXh0LCBmYWxzZSwgMCk7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIGlmICghZm9ya2VkKVxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgdGhyb3cgZXg7XHJcbiAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIGNvbnRleHRzID0gbmV4dDtcclxuICAgICAgICAgICAgbmV4dCA9IG51bGw7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGlmICghZm91bmRDb21tYW5kKSB7XHJcbiAgICAgICAgICAgIHRoaXMuY29uc3VtZXIub25Db21tYW5kQ29tcGxldGUob3JpZ2luYWwsIGZhbHNlLCAwKTtcclxuICAgICAgICAgICAgdGhyb3cgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbl8xLmRlZmF1bHQuQlVJTFRfSU5fRVhDRVBUSU9OUy5kaXNwYXRjaGVyVW5rbm93bkNvbW1hbmQoKS5jcmVhdGVXaXRoQ29udGV4dChwYXJzZS5nZXRSZWFkZXIoKSk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHJldHVybiBmb3JrZWQgPyBzdWNjZXNzZnVsRm9ya3MgOiByZXN1bHQ7XHJcbiAgICB9XHJcbiAgICBwYXJzZShjb21tYW5kLCBzb3VyY2UpIHtcclxuICAgICAgICBpZiAodHlwZW9mIGNvbW1hbmQgPT09IFwic3RyaW5nXCIpXHJcbiAgICAgICAgICAgIGNvbW1hbmQgPSBuZXcgU3RyaW5nUmVhZGVyXzEuZGVmYXVsdChjb21tYW5kKTtcclxuICAgICAgICBsZXQgY29udGV4dCA9IG5ldyBDb21tYW5kQ29udGV4dEJ1aWxkZXJfMS5kZWZhdWx0KHRoaXMsIHNvdXJjZSwgdGhpcy5yb290LCBjb21tYW5kLmdldEN1cnNvcigpKTtcclxuICAgICAgICByZXR1cm4gdGhpcy5wYXJzZU5vZGVzKHRoaXMucm9vdCwgY29tbWFuZCwgY29udGV4dCk7XHJcbiAgICB9XHJcbiAgICBwYXJzZU5vZGVzKG5vZGUsIG9yaWdpbmFsUmVhZGVyLCBjb250ZXh0U29GYXIpIHtcclxuICAgICAgICBsZXQgc291cmNlID0gY29udGV4dFNvRmFyLmdldFNvdXJjZSgpO1xyXG4gICAgICAgIGxldCBlcnJvcnMgPSBudWxsO1xyXG4gICAgICAgIGxldCBwb3RlbnRpYWxzID0gbnVsbDtcclxuICAgICAgICBsZXQgY3Vyc29yID0gb3JpZ2luYWxSZWFkZXIuZ2V0Q3Vyc29yKCk7XHJcbiAgICAgICAgZm9yIChsZXQgY2hpbGQgb2Ygbm9kZS5nZXRSZWxldmFudE5vZGVzKG9yaWdpbmFsUmVhZGVyKSkge1xyXG4gICAgICAgICAgICBpZiAoIWNoaWxkLmNhblVzZShzb3VyY2UpKVxyXG4gICAgICAgICAgICAgICAgY29udGludWU7XHJcbiAgICAgICAgICAgIGxldCBjb250ZXh0ID0gY29udGV4dFNvRmFyLmNvcHkoKTtcclxuICAgICAgICAgICAgbGV0IHJlYWRlciA9IG5ldyBTdHJpbmdSZWFkZXJfMS5kZWZhdWx0KG9yaWdpbmFsUmVhZGVyKTtcclxuICAgICAgICAgICAgdHJ5IHtcclxuICAgICAgICAgICAgICAgIGNoaWxkLnBhcnNlKHJlYWRlciwgY29udGV4dCk7XHJcbiAgICAgICAgICAgICAgICBpZiAocmVhZGVyLmNhblJlYWQoKSlcclxuICAgICAgICAgICAgICAgICAgICBpZiAocmVhZGVyLnBlZWsoKSAhPSBBUkdVTUVOVF9TRVBBUkFUT1IpXHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHRocm93IENvbW1hbmRTeW50YXhFeGNlcHRpb25fMS5kZWZhdWx0LkJVSUxUX0lOX0VYQ0VQVElPTlMuZGlzcGF0Y2hlckV4cGVjdGVkQXJndW1lbnRTZXBhcmF0b3IoKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIGNhdGNoIChleCkge1xyXG4gICAgICAgICAgICAgICAgaWYgKGVycm9ycyA9PSBudWxsKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgZXJyb3JzID0gbmV3IE1hcCgpO1xyXG4gICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgZXJyb3JzLnNldChjaGlsZCwgZXgpO1xyXG4gICAgICAgICAgICAgICAgcmVhZGVyLnNldEN1cnNvcihjdXJzb3IpO1xyXG4gICAgICAgICAgICAgICAgY29udGludWU7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgY29udGV4dC53aXRoQ29tbWFuZChjaGlsZC5nZXRDb21tYW5kKCkpO1xyXG4gICAgICAgICAgICBpZiAocmVhZGVyLmNhblJlYWQoY2hpbGQuZ2V0UmVkaXJlY3QoKSA9PSBudWxsID8gMiA6IDEpKSB7XHJcbiAgICAgICAgICAgICAgICByZWFkZXIuc2tpcCgpO1xyXG4gICAgICAgICAgICAgICAgaWYgKCEoY2hpbGQuZ2V0UmVkaXJlY3QoKSA9PSBudWxsKSkge1xyXG4gICAgICAgICAgICAgICAgICAgIGxldCBjaGlsZENvbnRleHQgPSBuZXcgQ29tbWFuZENvbnRleHRCdWlsZGVyXzEuZGVmYXVsdCh0aGlzLCBzb3VyY2UsIGNoaWxkLmdldFJlZGlyZWN0KCksIHJlYWRlci5nZXRDdXJzb3IoKSk7XHJcbiAgICAgICAgICAgICAgICAgICAgbGV0IHBhcnNlID0gdGhpcy5wYXJzZU5vZGVzKGNoaWxkLmdldFJlZGlyZWN0KCksIHJlYWRlciwgY2hpbGRDb250ZXh0KTtcclxuICAgICAgICAgICAgICAgICAgICBjb250ZXh0LndpdGhDaGlsZChwYXJzZS5nZXRDb250ZXh0KCkpO1xyXG4gICAgICAgICAgICAgICAgICAgIHJldHVybiBuZXcgUGFyc2VSZXN1bHRzXzEuZGVmYXVsdChjb250ZXh0LCBwYXJzZS5nZXRSZWFkZXIoKSwgcGFyc2UuZ2V0RXhjZXB0aW9ucygpKTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIGVsc2Uge1xyXG4gICAgICAgICAgICAgICAgICAgIGxldCBwYXJzZSA9IHRoaXMucGFyc2VOb2RlcyhjaGlsZCwgcmVhZGVyLCBjb250ZXh0KTtcclxuICAgICAgICAgICAgICAgICAgICBpZiAocG90ZW50aWFscyA9PSBudWxsKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHBvdGVudGlhbHMgPSBbXTtcclxuICAgICAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICAgICAgcG90ZW50aWFscy5wdXNoKHBhcnNlKTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICBlbHNlIHtcclxuICAgICAgICAgICAgICAgIGlmIChwb3RlbnRpYWxzID09IG51bGwpIHtcclxuICAgICAgICAgICAgICAgICAgICBwb3RlbnRpYWxzID0gW107XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICBwb3RlbnRpYWxzLnB1c2gobmV3IFBhcnNlUmVzdWx0c18xLmRlZmF1bHQoY29udGV4dCwgcmVhZGVyLCBuZXcgTWFwKCkpKTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgIH1cclxuICAgICAgICBpZiAoIShwb3RlbnRpYWxzID09IG51bGwpKSB7XHJcbiAgICAgICAgICAgIGlmIChwb3RlbnRpYWxzLmxlbmd0aCA+IDEpIHtcclxuICAgICAgICAgICAgICAgIHBvdGVudGlhbHMuc29ydCgoYSwgYikgPT4ge1xyXG4gICAgICAgICAgICAgICAgICAgIGlmICghYS5nZXRSZWFkZXIoKS5jYW5SZWFkKCkgJiYgYi5nZXRSZWFkZXIoKS5jYW5SZWFkKCkpIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgcmV0dXJuIC0xO1xyXG4gICAgICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgICAgICBpZiAoYS5nZXRSZWFkZXIoKS5jYW5SZWFkKCkgJiYgIWIuZ2V0UmVhZGVyKCkuY2FuUmVhZCgpKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHJldHVybiAxO1xyXG4gICAgICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgICAgICBpZiAoYS5nZXRFeGNlcHRpb25zKCkuc2l6ZSA9PT0gMCAmJiBiLmdldEV4Y2VwdGlvbnMoKS5zaXplICE9PSAwKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHJldHVybiAtMTtcclxuICAgICAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICAgICAgaWYgKGEuZ2V0RXhjZXB0aW9ucygpLnNpemUgIT09IDAgJiYgYi5nZXRFeGNlcHRpb25zKCkuc2l6ZSA9PT0gMCkge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICByZXR1cm4gMTtcclxuICAgICAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICAgICAgcmV0dXJuIDA7XHJcbiAgICAgICAgICAgICAgICB9KTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICByZXR1cm4gcG90ZW50aWFsc1swXTtcclxuICAgICAgICB9XHJcbiAgICAgICAgcmV0dXJuIG5ldyBQYXJzZVJlc3VsdHNfMS5kZWZhdWx0KGNvbnRleHRTb0Zhciwgb3JpZ2luYWxSZWFkZXIsIGVycm9ycyA9PSBudWxsID8gbmV3IE1hcCgpIDogZXJyb3JzKTtcclxuICAgIH1cclxuICAgIGdldEFsbFVzYWdlKG5vZGUsIHNvdXJjZSwgcmVzdHJpY3RlZCkge1xyXG4gICAgICAgIGNvbnN0IHJlc3VsdCA9IFtdO1xyXG4gICAgICAgIHRoaXMuX19nZXRBbGxVc2FnZShub2RlLCBzb3VyY2UsIHJlc3VsdCwgXCJcIiwgcmVzdHJpY3RlZCk7XHJcbiAgICAgICAgcmV0dXJuIHJlc3VsdDtcclxuICAgIH1cclxuICAgIF9fZ2V0QWxsVXNhZ2Uobm9kZSwgc291cmNlLCByZXN1bHQsIHByZWZpeCA9IFwiXCIsIHJlc3RyaWN0ZWQpIHtcclxuICAgICAgICBpZiAocmVzdHJpY3RlZCAmJiAhbm9kZS5jYW5Vc2Uoc291cmNlKSkge1xyXG4gICAgICAgICAgICByZXR1cm47XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGlmIChub2RlLmdldENvbW1hbmQoKSAhPSBudWxsKSB7XHJcbiAgICAgICAgICAgIHJlc3VsdC5wdXNoKHByZWZpeCk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGlmIChub2RlLmdldFJlZGlyZWN0KCkgIT0gbnVsbCkge1xyXG4gICAgICAgICAgICBjb25zdCByZWRpcmVjdCA9IG5vZGUuZ2V0UmVkaXJlY3QoKSA9PT0gdGhpcy5yb290ID8gXCIuLi5cIiA6IFwiLT4gXCIgKyBub2RlLmdldFJlZGlyZWN0KCkuZ2V0VXNhZ2VUZXh0KCk7XHJcbiAgICAgICAgICAgIHJlc3VsdC5wdXNoKHByZWZpeC5sZW5ndGggPT09IDAgPyBub2RlLmdldFVzYWdlVGV4dCgpICsgQVJHVU1FTlRfU0VQQVJBVE9SICsgcmVkaXJlY3QgOiBwcmVmaXggKyBBUkdVTUVOVF9TRVBBUkFUT1IgKyByZWRpcmVjdCk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGVsc2UgaWYgKG5vZGUuZ2V0Q2hpbGRyZW5Db3VudCgpID4gMCkge1xyXG4gICAgICAgICAgICBmb3IgKGxldCBjaGlsZCBvZiBub2RlLmdldENoaWxkcmVuKCkpIHtcclxuICAgICAgICAgICAgICAgIHRoaXMuX19nZXRBbGxVc2FnZShjaGlsZCwgc291cmNlLCByZXN1bHQsIHByZWZpeC5sZW5ndGggPT09IDAgPyBjaGlsZC5nZXRVc2FnZVRleHQoKSA6IHByZWZpeCArIEFSR1VNRU5UX1NFUEFSQVRPUiArIGNoaWxkLmdldFVzYWdlVGV4dCgpLCByZXN0cmljdGVkKTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgIH1cclxuICAgIH1cclxuICAgIGdldFNtYXJ0VXNhZ2Uobm9kZSwgc291cmNlKSB7XHJcbiAgICAgICAgbGV0IHJlc3VsdCA9IG5ldyBNYXAoKTtcclxuICAgICAgICBsZXQgb3B0aW9uYWwgPSBub2RlLmdldENvbW1hbmQoKSAhPT0gbnVsbDtcclxuICAgICAgICBmb3IgKGxldCBjaGlsZCBvZiBub2RlLmdldENoaWxkcmVuKCkpIHtcclxuICAgICAgICAgICAgbGV0IHVzYWdlID0gdGhpcy5fX2dldFNtYXJ0VXNhZ2UoY2hpbGQsIHNvdXJjZSwgb3B0aW9uYWwsIGZhbHNlKTtcclxuICAgICAgICAgICAgaWYgKCEodXNhZ2UgPT0gbnVsbCkpIHtcclxuICAgICAgICAgICAgICAgIHJlc3VsdC5zZXQoY2hpbGQsIHVzYWdlKTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gcmVzdWx0O1xyXG4gICAgfVxyXG4gICAgX19nZXRTbWFydFVzYWdlKG5vZGUsIHNvdXJjZSwgb3B0aW9uYWwsIGRlZXApIHtcclxuICAgICAgICBpZiAoIW5vZGUuY2FuVXNlKHNvdXJjZSkpIHtcclxuICAgICAgICAgICAgcmV0dXJuIG51bGw7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGxldCBzZWxmID0gb3B0aW9uYWwgPyBVU0FHRV9PUFRJT05BTF9PUEVOICsgbm9kZS5nZXRVc2FnZVRleHQoKSArIFVTQUdFX09QVElPTkFMX0NMT1NFIDogbm9kZS5nZXRVc2FnZVRleHQoKTtcclxuICAgICAgICBsZXQgY2hpbGRPcHRpb25hbCA9IG5vZGUuZ2V0Q29tbWFuZCgpICE9IG51bGw7XHJcbiAgICAgICAgbGV0IG9wZW4gPSBjaGlsZE9wdGlvbmFsID8gVVNBR0VfT1BUSU9OQUxfT1BFTiA6IFVTQUdFX1JFUVVJUkVEX09QRU47XHJcbiAgICAgICAgbGV0IGNsb3NlID0gY2hpbGRPcHRpb25hbCA/IFVTQUdFX09QVElPTkFMX0NMT1NFIDogVVNBR0VfUkVRVUlSRURfQ0xPU0U7XHJcbiAgICAgICAgaWYgKCFkZWVwKSB7XHJcbiAgICAgICAgICAgIGlmICgobm9kZS5nZXRSZWRpcmVjdCgpICE9IG51bGwpKSB7XHJcbiAgICAgICAgICAgICAgICBsZXQgcmVkaXJlY3QgPSBub2RlLmdldFJlZGlyZWN0KCkgPT0gdGhpcy5yb290ID8gXCIuLi5cIiA6IFwiLT4gXCIgKyBub2RlLmdldFJlZGlyZWN0KCkuZ2V0VXNhZ2VUZXh0KCk7XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gc2VsZiArIEFSR1VNRU5UX1NFUEFSQVRPUiArIHJlZGlyZWN0O1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIGVsc2Uge1xyXG4gICAgICAgICAgICAgICAgbGV0IGNoaWxkcmVuID0gWy4uLm5vZGUuZ2V0Q2hpbGRyZW4oKV0uZmlsdGVyKGMgPT4gYy5jYW5Vc2Uoc291cmNlKSk7XHJcbiAgICAgICAgICAgICAgICBpZiAoKGNoaWxkcmVuLmxlbmd0aCA9PSAxKSkge1xyXG4gICAgICAgICAgICAgICAgICAgIGxldCB1c2FnZSA9IHRoaXMuX19nZXRTbWFydFVzYWdlKGNoaWxkcmVuWzBdLCBzb3VyY2UsIGNoaWxkT3B0aW9uYWwsIGNoaWxkT3B0aW9uYWwpO1xyXG4gICAgICAgICAgICAgICAgICAgIGlmICghKHVzYWdlID09IG51bGwpKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHJldHVybiBzZWxmICsgQVJHVU1FTlRfU0VQQVJBVE9SICsgdXNhZ2U7XHJcbiAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgZWxzZSBpZiAoY2hpbGRyZW4ubGVuZ3RoID4gMSkge1xyXG4gICAgICAgICAgICAgICAgICAgIGxldCBjaGlsZFVzYWdlID0gbmV3IFNldCgpO1xyXG4gICAgICAgICAgICAgICAgICAgIGZvciAobGV0IGNoaWxkIG9mIGNoaWxkcmVuKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIGxldCB1c2FnZSA9IHRoaXMuX19nZXRTbWFydFVzYWdlKGNoaWxkLCBzb3VyY2UsIGNoaWxkT3B0aW9uYWwsIHRydWUpO1xyXG4gICAgICAgICAgICAgICAgICAgICAgICBpZiAoISh1c2FnZSA9PSBudWxsKSkge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgY2hpbGRVc2FnZS5hZGQodXNhZ2UpO1xyXG4gICAgICAgICAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgICAgIGlmIChjaGlsZFVzYWdlLnNpemUgPT09IDEpIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgbGV0IHVzYWdlID0gY2hpbGRVc2FnZS52YWx1ZXMoKS5uZXh0KCkudmFsdWU7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHJldHVybiBzZWxmICsgQVJHVU1FTlRfU0VQQVJBVE9SICsgKGNoaWxkT3B0aW9uYWwgPyBVU0FHRV9PUFRJT05BTF9PUEVOICsgdXNhZ2UgKyBVU0FHRV9PUFRJT05BTF9DTE9TRSA6IHVzYWdlKTtcclxuICAgICAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICAgICAgZWxzZSBpZiAoY2hpbGRVc2FnZS5zaXplID4gMSkge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICBsZXQgYnVpbGRlciA9IG9wZW47XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIGxldCBjb3VudCA9IDA7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIGZvciAobGV0IGNoaWxkIG9mIGNoaWxkcmVuKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICBpZiAoY291bnQgPiAwKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgYnVpbGRlciArPSBVU0FHRV9PUjtcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIGJ1aWxkZXIgKz0gY2hpbGQuZ2V0VXNhZ2VUZXh0KCk7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICBjb3VudCsrO1xyXG4gICAgICAgICAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIGlmIChjb3VudCA+IDApIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIGJ1aWxkZXIgKz0gY2xvc2U7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICByZXR1cm4gc2VsZiArIEFSR1VNRU5UX1NFUEFSQVRPUiArIGJ1aWxkZXI7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9XHJcbiAgICAgICAgcmV0dXJuIHNlbGY7XHJcbiAgICB9XHJcbiAgICBnZXRDb21wbGV0aW9uU3VnZ2VzdGlvbnMocGFyc2UsIGN1cnNvciA9IHBhcnNlLmdldFJlYWRlcigpLmdldFRvdGFsTGVuZ3RoKCkpIHtcclxuICAgICAgICByZXR1cm4gX19hd2FpdGVyKHRoaXMsIHZvaWQgMCwgdm9pZCAwLCBmdW5jdGlvbiogKCkge1xyXG4gICAgICAgICAgICBsZXQgY29udGV4dCA9IHBhcnNlLmdldENvbnRleHQoKTtcclxuICAgICAgICAgICAgbGV0IG5vZGVCZWZvcmVDdXJzb3IgPSBjb250ZXh0LmZpbmRTdWdnZXN0aW9uQ29udGV4dChjdXJzb3IpO1xyXG4gICAgICAgICAgICBsZXQgcGFyZW50ID0gbm9kZUJlZm9yZUN1cnNvci5wYXJlbnQ7XHJcbiAgICAgICAgICAgIGxldCBzdGFydCA9IE1hdGgubWluKG5vZGVCZWZvcmVDdXJzb3Iuc3RhcnRQb3MsIGN1cnNvcik7XHJcbiAgICAgICAgICAgIGxldCBmdWxsSW5wdXQgPSBwYXJzZS5nZXRSZWFkZXIoKS5nZXRTdHJpbmcoKTtcclxuICAgICAgICAgICAgbGV0IHRydW5jYXRlZElucHV0ID0gZnVsbElucHV0LnN1YnN0cmluZygwLCBjdXJzb3IpO1xyXG4gICAgICAgICAgICBsZXQgZnV0dXJlcyA9IFtdO1xyXG4gICAgICAgICAgICBmb3IgKGxldCBub2RlIG9mIHBhcmVudC5nZXRDaGlsZHJlbigpKSB7XHJcbiAgICAgICAgICAgICAgICBsZXQgZnV0dXJlID0geWllbGQgU3VnZ2VzdGlvbnNfMS5kZWZhdWx0LmVtcHR5KCk7XHJcbiAgICAgICAgICAgICAgICB0cnkge1xyXG4gICAgICAgICAgICAgICAgICAgIGZ1dHVyZSA9IHlpZWxkIG5vZGUubGlzdFN1Z2dlc3Rpb25zKGNvbnRleHQuYnVpbGQodHJ1bmNhdGVkSW5wdXQpLCBuZXcgU3VnZ2VzdGlvbnNCdWlsZGVyXzEuZGVmYXVsdCh0cnVuY2F0ZWRJbnB1dCwgc3RhcnQpKTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIGNhdGNoIChpZ25vcmVkKSB7XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICBmdXR1cmVzLnB1c2goZnV0dXJlKTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICByZXR1cm4gUHJvbWlzZS5yZXNvbHZlKFN1Z2dlc3Rpb25zXzEuZGVmYXVsdC5tZXJnZShmdWxsSW5wdXQsIGZ1dHVyZXMpKTtcclxuICAgICAgICB9KTtcclxuICAgIH1cclxuICAgIGdldFJvb3QoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMucm9vdDtcclxuICAgIH1cclxuICAgIGdldFBhdGgodGFyZ2V0KSB7XHJcbiAgICAgICAgbGV0IG5vZGVzID0gW107XHJcbiAgICAgICAgdGhpcy5hZGRQYXRocyh0aGlzLnJvb3QsIG5vZGVzLCBbXSk7XHJcbiAgICAgICAgZm9yIChsZXQgbGlzdCBvZiBub2Rlcykge1xyXG4gICAgICAgICAgICBpZiAobGlzdFtsaXN0Lmxlbmd0aCAtIDFdID09PSB0YXJnZXQpIHtcclxuICAgICAgICAgICAgICAgIGxldCByZXN1bHQgPSBbXTtcclxuICAgICAgICAgICAgICAgIGZvciAobGV0IG5vZGUgb2YgbGlzdCkge1xyXG4gICAgICAgICAgICAgICAgICAgIGlmIChub2RlICE9PSB0aGlzLnJvb3QpIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgcmVzdWx0LnB1c2gobm9kZS5nZXROYW1lKCkpO1xyXG4gICAgICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIHJldHVybiByZXN1bHQ7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9XHJcbiAgICAgICAgcmV0dXJuIFtdO1xyXG4gICAgfVxyXG4gICAgZmluZE5vZGUocGF0aCkge1xyXG4gICAgICAgIGxldCBub2RlID0gdGhpcy5yb290O1xyXG4gICAgICAgIGZvciAobGV0IG5hbWUgb2YgcGF0aCkge1xyXG4gICAgICAgICAgICBub2RlID0gbm9kZS5nZXRDaGlsZChuYW1lKTtcclxuICAgICAgICAgICAgaWYgKG5vZGUgPT0gbnVsbClcclxuICAgICAgICAgICAgICAgIHJldHVybiBudWxsO1xyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gbm9kZTtcclxuICAgIH1cclxuICAgIGZpbmRBbWJpZ3VpdGllcyhjb25zdW1lcikge1xyXG4gICAgICAgIHRoaXMucm9vdC5maW5kQW1iaWd1aXRpZXMoY29uc3VtZXIpO1xyXG4gICAgfVxyXG4gICAgYWRkUGF0aHMobm9kZSwgcmVzdWx0LCBwYXJlbnRzKSB7XHJcbiAgICAgICAgbGV0IGN1cnJlbnQgPSBbXTtcclxuICAgICAgICBjdXJyZW50LnB1c2goLi4ucGFyZW50cyk7XHJcbiAgICAgICAgY3VycmVudC5wdXNoKG5vZGUpO1xyXG4gICAgICAgIHJlc3VsdC5wdXNoKGN1cnJlbnQpO1xyXG4gICAgICAgIGZvciAobGV0IGNoaWxkIG9mIG5vZGUuZ2V0Q2hpbGRyZW4oKSlcclxuICAgICAgICAgICAgdGhpcy5hZGRQYXRocyhjaGlsZCwgcmVzdWx0LCBjdXJyZW50KTtcclxuICAgIH1cclxufVxyXG5leHBvcnRzLmRlZmF1bHQgPSBDb21tYW5kRGlzcGF0Y2hlcjtcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY2xhc3MgTGl0ZXJhbE1lc3NhZ2Uge1xyXG4gICAgY29uc3RydWN0b3Ioc3RyKSB7XHJcbiAgICAgICAgdGhpcy5zdHIgPSBzdHI7XHJcbiAgICB9XHJcbiAgICBnZXRTdHJpbmcoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuc3RyO1xyXG4gICAgfVxyXG4gICAgdG9TdHJpbmcoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuc3RyO1xyXG4gICAgfVxyXG59XHJcbmV4cG9ydHMuZGVmYXVsdCA9IExpdGVyYWxNZXNzYWdlO1xyXG4iLCJcInVzZSBzdHJpY3RcIjtcclxudmFyIF9faW1wb3J0RGVmYXVsdCA9ICh0aGlzICYmIHRoaXMuX19pbXBvcnREZWZhdWx0KSB8fCBmdW5jdGlvbiAobW9kKSB7XHJcbiAgICByZXR1cm4gKG1vZCAmJiBtb2QuX19lc01vZHVsZSkgPyBtb2QgOiB7IFwiZGVmYXVsdFwiOiBtb2QgfTtcclxufTtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5jb25zdCBTdHJpbmdSZWFkZXJfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9TdHJpbmdSZWFkZXJcIikpO1xyXG5jbGFzcyBQYXJzZVJlc3VsdHMge1xyXG4gICAgY29uc3RydWN0b3IoY29udGV4dCwgcmVhZGVyLCBleGNlcHRpb25zKSB7XHJcbiAgICAgICAgdGhpcy5jb250ZXh0ID0gY29udGV4dDtcclxuICAgICAgICB0aGlzLnJlYWRlciA9IHJlYWRlciB8fCBuZXcgU3RyaW5nUmVhZGVyXzEuZGVmYXVsdChcIlwiKTtcclxuICAgICAgICB0aGlzLmV4Y2VwdGlvbnMgPSBleGNlcHRpb25zIHx8IG5ldyBNYXAoKTtcclxuICAgIH1cclxuICAgIGdldENvbnRleHQoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuY29udGV4dDtcclxuICAgIH1cclxuICAgIGdldFJlYWRlcigpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5yZWFkZXI7XHJcbiAgICB9XHJcbiAgICBnZXRFeGNlcHRpb25zKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmV4Y2VwdGlvbnM7XHJcbiAgICB9XHJcbn1cclxuZXhwb3J0cy5kZWZhdWx0ID0gUGFyc2VSZXN1bHRzO1xyXG4iLCJcInVzZSBzdHJpY3RcIjtcclxudmFyIF9faW1wb3J0RGVmYXVsdCA9ICh0aGlzICYmIHRoaXMuX19pbXBvcnREZWZhdWx0KSB8fCBmdW5jdGlvbiAobW9kKSB7XHJcbiAgICByZXR1cm4gKG1vZCAmJiBtb2QuX19lc01vZHVsZSkgPyBtb2QgOiB7IFwiZGVmYXVsdFwiOiBtb2QgfTtcclxufTtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5jb25zdCBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vZXhjZXB0aW9ucy9Db21tYW5kU3ludGF4RXhjZXB0aW9uXCIpKTtcclxuY29uc3QgU1lOVEFYX0VTQ0FQRSA9ICdcXFxcJztcclxuY29uc3QgU1lOVEFYX1FVT1RFID0gJ1xcXCInO1xyXG5jbGFzcyBTdHJpbmdSZWFkZXIge1xyXG4gICAgY29uc3RydWN0b3Iob3RoZXIpIHtcclxuICAgICAgICB0aGlzLmN1cnNvciA9IDA7XHJcbiAgICAgICAgaWYgKHR5cGVvZiBvdGhlciA9PT0gXCJzdHJpbmdcIikge1xyXG4gICAgICAgICAgICB0aGlzLnN0cmluZyA9IG90aGVyO1xyXG4gICAgICAgIH1cclxuICAgICAgICBlbHNlIHtcclxuICAgICAgICAgICAgdGhpcy5zdHJpbmcgPSBvdGhlci5zdHJpbmc7XHJcbiAgICAgICAgICAgIHRoaXMuY3Vyc29yID0gb3RoZXIuY3Vyc29yO1xyXG4gICAgICAgIH1cclxuICAgIH1cclxuICAgIGdldFN0cmluZygpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5zdHJpbmc7XHJcbiAgICB9XHJcbiAgICBzZXRDdXJzb3IoY3Vyc29yKSB7XHJcbiAgICAgICAgdGhpcy5jdXJzb3IgPSBjdXJzb3I7XHJcbiAgICB9XHJcbiAgICBnZXRSZW1haW5pbmdMZW5ndGgoKSB7XHJcbiAgICAgICAgcmV0dXJuICh0aGlzLnN0cmluZy5sZW5ndGggLSB0aGlzLmN1cnNvcik7XHJcbiAgICB9XHJcbiAgICBnZXRUb3RhbExlbmd0aCgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5zdHJpbmcubGVuZ3RoO1xyXG4gICAgfVxyXG4gICAgZ2V0Q3Vyc29yKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmN1cnNvcjtcclxuICAgIH1cclxuICAgIGdldFJlYWQoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuc3RyaW5nLnN1YnN0cmluZygwLCB0aGlzLmN1cnNvcik7XHJcbiAgICB9XHJcbiAgICBnZXRSZW1haW5pbmcoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuc3RyaW5nLnN1YnN0cmluZyh0aGlzLmN1cnNvcik7XHJcbiAgICB9XHJcbiAgICBjYW5SZWFkKGxlbmd0aCA9IDEpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5jdXJzb3IgKyBsZW5ndGggPD0gdGhpcy5zdHJpbmcubGVuZ3RoO1xyXG4gICAgfVxyXG4gICAgcGVlayhvZmZzZXQgPSAwKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuc3RyaW5nLmNoYXJBdCh0aGlzLmN1cnNvciArIG9mZnNldCk7XHJcbiAgICB9XHJcbiAgICByZWFkKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnN0cmluZy5jaGFyQXQodGhpcy5jdXJzb3IrKyk7XHJcbiAgICB9XHJcbiAgICBza2lwKCkge1xyXG4gICAgICAgIHRoaXMuY3Vyc29yKys7XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgaXNBbGxvd2VkTnVtYmVyKGMpIHtcclxuICAgICAgICByZXR1cm4gYyA+PSAnMCcgJiYgYyA8PSAnOScgfHwgYyA9PSAnLicgfHwgYyA9PSAnLScgfHwgYyA9PSAnKycgfHwgYyA9PSAnZScgfHwgYyA9PSAnRSc7XHJcbiAgICB9XHJcbiAgICBza2lwV2hpdGVzcGFjZSgpIHtcclxuICAgICAgICB3aGlsZSAoKHRoaXMuY2FuUmVhZCgpICYmIC9cXHMvLnRlc3QodGhpcy5wZWVrKCkpKSkge1xyXG4gICAgICAgICAgICB0aGlzLnNraXAoKTtcclxuICAgICAgICB9XHJcbiAgICB9XHJcbiAgICByZWFkSW50KCkge1xyXG4gICAgICAgIGxldCBzdGFydCA9IHRoaXMuY3Vyc29yO1xyXG4gICAgICAgIHdoaWxlICh0aGlzLmNhblJlYWQoKSAmJiBTdHJpbmdSZWFkZXIuaXNBbGxvd2VkTnVtYmVyKHRoaXMucGVlaygpKSkge1xyXG4gICAgICAgICAgICB0aGlzLnNraXAoKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgbGV0IG51bWJlciA9IHRoaXMuc3RyaW5nLnN1YnN0cmluZyhzdGFydCwgdGhpcy5jdXJzb3IpO1xyXG4gICAgICAgIGlmIChudW1iZXIubGVuZ3RoID09PSAwKSB7XHJcbiAgICAgICAgICAgIHRocm93IENvbW1hbmRTeW50YXhFeGNlcHRpb25fMS5kZWZhdWx0LkJVSUxUX0lOX0VYQ0VQVElPTlMucmVhZGVyRXhwZWN0ZWRJbnQoKS5jcmVhdGVXaXRoQ29udGV4dCh0aGlzKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgY29uc3QgcmVzdWx0ID0gcGFyc2VJbnQobnVtYmVyKTtcclxuICAgICAgICBpZiAoaXNOYU4ocmVzdWx0KSB8fCByZXN1bHQgIT09IHBhcnNlRmxvYXQobnVtYmVyKSkge1xyXG4gICAgICAgICAgICB0aGlzLmN1cnNvciA9IHN0YXJ0O1xyXG4gICAgICAgICAgICB0aHJvdyBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEuZGVmYXVsdC5CVUlMVF9JTl9FWENFUFRJT05TLnJlYWRlckludmFsaWRJbnQoKS5jcmVhdGVXaXRoQ29udGV4dCh0aGlzLCBudW1iZXIpO1xyXG4gICAgICAgIH1cclxuICAgICAgICBlbHNlXHJcbiAgICAgICAgICAgIHJldHVybiByZXN1bHQ7XHJcbiAgICB9XHJcbiAgICByZWFkRmxvYXQoKSB7XHJcbiAgICAgICAgbGV0IHN0YXJ0ID0gdGhpcy5jdXJzb3I7XHJcbiAgICAgICAgd2hpbGUgKCh0aGlzLmNhblJlYWQoKSAmJiBTdHJpbmdSZWFkZXIuaXNBbGxvd2VkTnVtYmVyKHRoaXMucGVlaygpKSkpIHtcclxuICAgICAgICAgICAgdGhpcy5za2lwKCk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGxldCBudW1iZXIgPSB0aGlzLnN0cmluZy5zdWJzdHJpbmcoc3RhcnQsIHRoaXMuY3Vyc29yKTtcclxuICAgICAgICBpZiAobnVtYmVyLmxlbmd0aCA9PT0gMCkge1xyXG4gICAgICAgICAgICB0aHJvdyBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEuZGVmYXVsdC5CVUlMVF9JTl9FWENFUFRJT05TLnJlYWRlckV4cGVjdGVkRmxvYXQoKS5jcmVhdGVXaXRoQ29udGV4dCh0aGlzKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgY29uc3QgcmVzdWx0ID0gcGFyc2VGbG9hdChudW1iZXIpO1xyXG4gICAgICAgIGNvbnN0IHN0cmljdFBhcnNlRmxvYXRUZXN0ID0gcGFyc2VGbG9hdChudW1iZXIuc3Vic3RyaW5nKHJlc3VsdC50b1N0cmluZygpLmxlbmd0aCwgdGhpcy5jdXJzb3IpKTtcclxuICAgICAgICBpZiAoaXNOYU4ocmVzdWx0KSB8fCAoIWlzTmFOKHN0cmljdFBhcnNlRmxvYXRUZXN0KSAmJlxyXG4gICAgICAgICAgICBzdHJpY3RQYXJzZUZsb2F0VGVzdCAhPT0gMCkpIHtcclxuICAgICAgICAgICAgdGhpcy5jdXJzb3IgPSBzdGFydDtcclxuICAgICAgICAgICAgdGhyb3cgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbl8xLmRlZmF1bHQuQlVJTFRfSU5fRVhDRVBUSU9OUy5yZWFkZXJJbnZhbGlkRmxvYXQoKS5jcmVhdGVXaXRoQ29udGV4dCh0aGlzLCBudW1iZXIpO1xyXG4gICAgICAgIH1cclxuICAgICAgICBlbHNlXHJcbiAgICAgICAgICAgIHJldHVybiByZXN1bHQ7XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgaXNBbGxvd2VkSW5VbnF1b3RlZFN0cmluZyhjKSB7XHJcbiAgICAgICAgcmV0dXJuIGMgPj0gJzAnICYmIGMgPD0gJzknXHJcbiAgICAgICAgICAgIHx8IGMgPj0gJ0EnICYmIGMgPD0gJ1onXHJcbiAgICAgICAgICAgIHx8IGMgPj0gJ2EnICYmIGMgPD0gJ3onXHJcbiAgICAgICAgICAgIHx8IGMgPT0gJ18nIHx8IGMgPT0gJy0nXHJcbiAgICAgICAgICAgIHx8IGMgPT0gJy4nIHx8IGMgPT0gJysnO1xyXG4gICAgfVxyXG4gICAgcmVhZFVucXVvdGVkU3RyaW5nKCkge1xyXG4gICAgICAgIGxldCBzdGFydCA9IHRoaXMuY3Vyc29yO1xyXG4gICAgICAgIHdoaWxlICh0aGlzLmNhblJlYWQoKSAmJiBTdHJpbmdSZWFkZXIuaXNBbGxvd2VkSW5VbnF1b3RlZFN0cmluZyh0aGlzLnBlZWsoKSkpIHtcclxuICAgICAgICAgICAgdGhpcy5za2lwKCk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHJldHVybiB0aGlzLnN0cmluZy5zdWJzdHJpbmcoc3RhcnQsIHRoaXMuY3Vyc29yKTtcclxuICAgIH1cclxuICAgIHJlYWRRdW90ZWRTdHJpbmcoKSB7XHJcbiAgICAgICAgaWYgKCF0aGlzLmNhblJlYWQoKSkge1xyXG4gICAgICAgICAgICByZXR1cm4gXCJcIjtcclxuICAgICAgICB9XHJcbiAgICAgICAgZWxzZSBpZiAoKHRoaXMucGVlaygpICE9IFNZTlRBWF9RVU9URSkpIHtcclxuICAgICAgICAgICAgdGhyb3cgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbl8xLmRlZmF1bHQuQlVJTFRfSU5fRVhDRVBUSU9OUy5yZWFkZXJFeHBlY3RlZFN0YXJ0T2ZRdW90ZSgpLmNyZWF0ZVdpdGhDb250ZXh0KHRoaXMpO1xyXG4gICAgICAgIH1cclxuICAgICAgICB0aGlzLnNraXAoKTtcclxuICAgICAgICBsZXQgcmVzdWx0ID0gXCJcIjtcclxuICAgICAgICBsZXQgZXNjYXBlZCA9IGZhbHNlO1xyXG4gICAgICAgIHdoaWxlICh0aGlzLmNhblJlYWQoKSkge1xyXG4gICAgICAgICAgICBsZXQgYyA9IHRoaXMucmVhZCgpO1xyXG4gICAgICAgICAgICBpZiAoZXNjYXBlZCkge1xyXG4gICAgICAgICAgICAgICAgaWYgKGMgPT0gU1lOVEFYX1FVT1RFIHx8IGMgPT0gU1lOVEFYX0VTQ0FQRSkge1xyXG4gICAgICAgICAgICAgICAgICAgIHJlc3VsdCArPSBjO1xyXG4gICAgICAgICAgICAgICAgICAgIGVzY2FwZWQgPSBmYWxzZTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIGVsc2Uge1xyXG4gICAgICAgICAgICAgICAgICAgIHRoaXMuc2V0Q3Vyc29yKHRoaXMuZ2V0Q3Vyc29yKCkgLSAxKTtcclxuICAgICAgICAgICAgICAgICAgICB0aHJvdyBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEuZGVmYXVsdC5CVUlMVF9JTl9FWENFUFRJT05TLnJlYWRlckludmFsaWRFc2NhcGUoKS5jcmVhdGVXaXRoQ29udGV4dCh0aGlzLCBjKTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICBlbHNlIGlmIChjID09IFNZTlRBWF9FU0NBUEUpIHtcclxuICAgICAgICAgICAgICAgIGVzY2FwZWQgPSB0cnVlO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIGVsc2UgaWYgKGMgPT0gU1lOVEFYX1FVT1RFKSB7XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gcmVzdWx0O1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIGVsc2Uge1xyXG4gICAgICAgICAgICAgICAgcmVzdWx0ICs9IGM7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9XHJcbiAgICAgICAgdGhyb3cgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbl8xLmRlZmF1bHQuQlVJTFRfSU5fRVhDRVBUSU9OUy5yZWFkZXJFeHBlY3RlZEVuZE9mUXVvdGUoKS5jcmVhdGVXaXRoQ29udGV4dCh0aGlzKTtcclxuICAgIH1cclxuICAgIHJlYWRTdHJpbmcoKSB7XHJcbiAgICAgICAgaWYgKHRoaXMuY2FuUmVhZCgpICYmICh0aGlzLnBlZWsoKSA9PT0gU1lOVEFYX1FVT1RFKSkge1xyXG4gICAgICAgICAgICByZXR1cm4gdGhpcy5yZWFkUXVvdGVkU3RyaW5nKCk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGVsc2Uge1xyXG4gICAgICAgICAgICByZXR1cm4gdGhpcy5yZWFkVW5xdW90ZWRTdHJpbmcoKTtcclxuICAgICAgICB9XHJcbiAgICB9XHJcbiAgICByZWFkQm9vbGVhbigpIHtcclxuICAgICAgICBsZXQgc3RhcnQgPSB0aGlzLmN1cnNvcjtcclxuICAgICAgICBsZXQgdmFsdWUgPSB0aGlzLnJlYWRTdHJpbmcoKTtcclxuICAgICAgICBpZiAodmFsdWUubGVuZ3RoID09PSAwKSB7XHJcbiAgICAgICAgICAgIHRocm93IENvbW1hbmRTeW50YXhFeGNlcHRpb25fMS5kZWZhdWx0LkJVSUxUX0lOX0VYQ0VQVElPTlMucmVhZGVyRXhwZWN0ZWRCb29sKCkuY3JlYXRlV2l0aENvbnRleHQodGhpcyk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGlmICh2YWx1ZSA9PT0gXCJ0cnVlXCIpIHtcclxuICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGVsc2UgaWYgKHZhbHVlID09PSBcImZhbHNlXCIpIHtcclxuICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgIH1cclxuICAgICAgICBlbHNlIHtcclxuICAgICAgICAgICAgdGhpcy5jdXJzb3IgPSBzdGFydDtcclxuICAgICAgICAgICAgdGhyb3cgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbl8xLmRlZmF1bHQuQlVJTFRfSU5fRVhDRVBUSU9OUy5yZWFkZXJJbnZhbGlkQm9vbCgpLmNyZWF0ZVdpdGhDb250ZXh0KHRoaXMsIHZhbHVlKTtcclxuICAgICAgICB9XHJcbiAgICB9XHJcbiAgICBleHBlY3QoYykge1xyXG4gICAgICAgIGlmICghdGhpcy5jYW5SZWFkKCkgfHwgdGhpcy5wZWVrKCkgIT09IGMpIHtcclxuICAgICAgICAgICAgdGhyb3cgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbl8xLmRlZmF1bHQuQlVJTFRfSU5fRVhDRVBUSU9OUy5yZWFkZXJFeHBlY3RlZFN5bWJvbCgpLmNyZWF0ZVdpdGhDb250ZXh0KHRoaXMsIGMpO1xyXG4gICAgICAgIH1cclxuICAgICAgICB0aGlzLnNraXAoKTtcclxuICAgIH1cclxufVxyXG5leHBvcnRzLmRlZmF1bHQgPSBTdHJpbmdSZWFkZXI7XHJcbiIsIlwidXNlIHN0cmljdFwiO1xyXG52YXIgX19pbXBvcnREZWZhdWx0ID0gKHRoaXMgJiYgdGhpcy5fX2ltcG9ydERlZmF1bHQpIHx8IGZ1bmN0aW9uIChtb2QpIHtcclxuICAgIHJldHVybiAobW9kICYmIG1vZC5fX2VzTW9kdWxlKSA/IG1vZCA6IHsgXCJkZWZhdWx0XCI6IG1vZCB9O1xyXG59O1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNvbnN0IEJvb2xBcmd1bWVudFR5cGVfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9Cb29sQXJndW1lbnRUeXBlXCIpKTtcclxuY29uc3QgSW50ZWdlckFyZ3VtZW50VHlwZV8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL0ludGVnZXJBcmd1bWVudFR5cGVcIikpO1xyXG5jb25zdCBGbG9hdEFyZ3VtZW50VHlwZV8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL0Zsb2F0QXJndW1lbnRUeXBlXCIpKTtcclxuY29uc3QgU3RyaW5nQXJndW1lbnRUeXBlXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vU3RyaW5nQXJndW1lbnRUeXBlXCIpKTtcclxuZXhwb3J0cy5EZWZhdWx0VHlwZSA9IHtcclxuICAgIGJvb2w6IEJvb2xBcmd1bWVudFR5cGVfMS5kZWZhdWx0LmJvb2wsXHJcbiAgICBpbnRlZ2VyOiBJbnRlZ2VyQXJndW1lbnRUeXBlXzEuZGVmYXVsdC5pbnRlZ2VyLFxyXG4gICAgZmxvYXQ6IEZsb2F0QXJndW1lbnRUeXBlXzEuZGVmYXVsdC5mbG9hdCxcclxuICAgIHdvcmQ6IFN0cmluZ0FyZ3VtZW50VHlwZV8xLmRlZmF1bHQud29yZCxcclxuICAgIHN0cmluZzogU3RyaW5nQXJndW1lbnRUeXBlXzEuZGVmYXVsdC5zdHJpbmcsXHJcbiAgICBncmVlZHlTdHJpbmc6IFN0cmluZ0FyZ3VtZW50VHlwZV8xLmRlZmF1bHQuZ3JlZWR5U3RyaW5nXHJcbn07XHJcbiIsIlwidXNlIHN0cmljdFwiO1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNvbnN0IEVYQU1QTEVTID0gW1widHJ1ZVwiLCBcImZhbHNlXCJdO1xyXG5jbGFzcyBCb29sQXJndW1lbnRUeXBlIHtcclxuICAgIGNvbnN0cnVjdG9yKCkge1xyXG4gICAgfVxyXG4gICAgc3RhdGljIGJvb2woKSB7XHJcbiAgICAgICAgcmV0dXJuIG5ldyBCb29sQXJndW1lbnRUeXBlKCk7XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgZ2V0Qm9vbChjb250ZXh0LCBuYW1lKSB7XHJcbiAgICAgICAgcmV0dXJuIGNvbnRleHQuZ2V0QXJndW1lbnQobmFtZSwgQm9vbGVhbik7XHJcbiAgICB9XHJcbiAgICBwYXJzZShyZWFkZXIpIHtcclxuICAgICAgICByZXR1cm4gcmVhZGVyLnJlYWRCb29sZWFuKCk7XHJcbiAgICB9XHJcbiAgICBsaXN0U3VnZ2VzdGlvbnMoY29udGV4dCwgYnVpbGRlcikge1xyXG4gICAgICAgIGlmIChcInRydWVcIi5zdGFydHNXaXRoKGJ1aWxkZXIuZ2V0UmVtYWluaW5nKCkudG9Mb3dlckNhc2UoKSkpIHtcclxuICAgICAgICAgICAgYnVpbGRlci5zdWdnZXN0KFwidHJ1ZVwiKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgaWYgKFwiZmFsc2VcIi5zdGFydHNXaXRoKGJ1aWxkZXIuZ2V0UmVtYWluaW5nKCkudG9Mb3dlckNhc2UoKSkpIHtcclxuICAgICAgICAgICAgYnVpbGRlci5zdWdnZXN0KFwiZmFsc2VcIik7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHJldHVybiBidWlsZGVyLmJ1aWxkUHJvbWlzZSgpO1xyXG4gICAgfVxyXG4gICAgZ2V0RXhhbXBsZXMoKSB7XHJcbiAgICAgICAgcmV0dXJuIEVYQU1QTEVTO1xyXG4gICAgfVxyXG59XHJcbmV4cG9ydHMuZGVmYXVsdCA9IEJvb2xBcmd1bWVudFR5cGU7XHJcbiIsIlwidXNlIHN0cmljdFwiO1xyXG52YXIgX19pbXBvcnREZWZhdWx0ID0gKHRoaXMgJiYgdGhpcy5fX2ltcG9ydERlZmF1bHQpIHx8IGZ1bmN0aW9uIChtb2QpIHtcclxuICAgIHJldHVybiAobW9kICYmIG1vZC5fX2VzTW9kdWxlKSA/IG1vZCA6IHsgXCJkZWZhdWx0XCI6IG1vZCB9O1xyXG59O1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNvbnN0IENvbW1hbmRTeW50YXhFeGNlcHRpb25fMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi4vZXhjZXB0aW9ucy9Db21tYW5kU3ludGF4RXhjZXB0aW9uXCIpKTtcclxuY29uc3QgRVhBTVBMRVMgPSBbXCIwXCIsIFwiMS4yXCIsIFwiLjVcIiwgXCItMVwiLCBcIi0uNVwiLCBcIi0xMjM0LjU2XCJdO1xyXG5jbGFzcyBGbG9hdEFyZ3VtZW50VHlwZSB7XHJcbiAgICBjb25zdHJ1Y3RvcihtaW5pbXVtLCBtYXhpbXVtKSB7XHJcbiAgICAgICAgdGhpcy5taW5pbXVtID0gbWluaW11bTtcclxuICAgICAgICB0aGlzLm1heGltdW0gPSBtYXhpbXVtO1xyXG4gICAgfVxyXG4gICAgc3RhdGljIGZsb2F0KG1pbiA9IC1JbmZpbml0eSwgbWF4ID0gSW5maW5pdHkpIHtcclxuICAgICAgICByZXR1cm4gbmV3IEZsb2F0QXJndW1lbnRUeXBlKG1pbiwgbWF4KTtcclxuICAgIH1cclxuICAgIHN0YXRpYyBnZXRGbG9hdChjb250ZXh0LCBuYW1lKSB7XHJcbiAgICAgICAgcmV0dXJuIGNvbnRleHQuZ2V0QXJndW1lbnQobmFtZSwgTnVtYmVyKTtcclxuICAgIH1cclxuICAgIGdldE1pbmltdW0oKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMubWluaW11bTtcclxuICAgIH1cclxuICAgIGdldE1heGltdW0oKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMubWF4aW11bTtcclxuICAgIH1cclxuICAgIHBhcnNlKHJlYWRlcikge1xyXG4gICAgICAgIGxldCBzdGFydCA9IHJlYWRlci5nZXRDdXJzb3IoKTtcclxuICAgICAgICBsZXQgcmVzdWx0ID0gcmVhZGVyLnJlYWRGbG9hdCgpO1xyXG4gICAgICAgIGlmIChyZXN1bHQgPCB0aGlzLm1pbmltdW0pIHtcclxuICAgICAgICAgICAgcmVhZGVyLnNldEN1cnNvcihzdGFydCk7XHJcbiAgICAgICAgICAgIHRocm93IENvbW1hbmRTeW50YXhFeGNlcHRpb25fMS5kZWZhdWx0LkJVSUxUX0lOX0VYQ0VQVElPTlMuaW50ZWdlclRvb0xvdygpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlciwgcmVzdWx0LCB0aGlzLm1pbmltdW0pO1xyXG4gICAgICAgIH1cclxuICAgICAgICBpZiAocmVzdWx0ID4gdGhpcy5tYXhpbXVtKSB7XHJcbiAgICAgICAgICAgIHJlYWRlci5zZXRDdXJzb3Ioc3RhcnQpO1xyXG4gICAgICAgICAgICB0aHJvdyBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEuZGVmYXVsdC5CVUlMVF9JTl9FWENFUFRJT05TLmludGVnZXJUb29IaWdoKCkuY3JlYXRlV2l0aENvbnRleHQocmVhZGVyLCByZXN1bHQsIHRoaXMubWF4aW11bSk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHJldHVybiByZXN1bHQ7XHJcbiAgICB9XHJcbiAgICBlcXVhbHMobykge1xyXG4gICAgICAgIGlmICh0aGlzID09PSBvKVxyXG4gICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICBpZiAoIShvIGluc3RhbmNlb2YgRmxvYXRBcmd1bWVudFR5cGUpKVxyXG4gICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMubWF4aW11bSA9PSBvLm1heGltdW0gJiYgdGhpcy5taW5pbXVtID09IG8ubWluaW11bTtcclxuICAgIH1cclxuICAgIHRvU3RyaW5nKCkge1xyXG4gICAgICAgIGlmICh0aGlzLm1pbmltdW0gPT09IC1JbmZpbml0eSAmJiB0aGlzLm1heGltdW0gPT09IEluZmluaXR5KSB7XHJcbiAgICAgICAgICAgIHJldHVybiBcImZsb2F0KClcIjtcclxuICAgICAgICB9XHJcbiAgICAgICAgZWxzZSBpZiAodGhpcy5tYXhpbXVtID09IEluZmluaXR5KSB7XHJcbiAgICAgICAgICAgIHJldHVybiBcImZsb2F0KFwiICsgdGhpcy5taW5pbXVtICsgXCIpXCI7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGVsc2Uge1xyXG4gICAgICAgICAgICByZXR1cm4gXCJmbG9hdChcIiArIHRoaXMubWluaW11bSArIFwiLCBcIiArIHRoaXMubWF4aW11bSArIFwiKVwiO1xyXG4gICAgICAgIH1cclxuICAgIH1cclxuICAgIGdldEV4YW1wbGVzKCkge1xyXG4gICAgICAgIHJldHVybiBFWEFNUExFUztcclxuICAgIH1cclxufVxyXG5leHBvcnRzLmRlZmF1bHQgPSBGbG9hdEFyZ3VtZW50VHlwZTtcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbnZhciBfX2ltcG9ydERlZmF1bHQgPSAodGhpcyAmJiB0aGlzLl9faW1wb3J0RGVmYXVsdCkgfHwgZnVuY3Rpb24gKG1vZCkge1xyXG4gICAgcmV0dXJuIChtb2QgJiYgbW9kLl9fZXNNb2R1bGUpID8gbW9kIDogeyBcImRlZmF1bHRcIjogbW9kIH07XHJcbn07XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY29uc3QgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbl8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuLi9leGNlcHRpb25zL0NvbW1hbmRTeW50YXhFeGNlcHRpb25cIikpO1xyXG5jb25zdCBFWEFNUExFUyA9IFtcIjBcIiwgXCIxMjNcIiwgXCItMTIzXCJdO1xyXG5jbGFzcyBJbnRlZ2VyQXJndW1lbnRUeXBlIHtcclxuICAgIGNvbnN0cnVjdG9yKG1pbmltdW0sIG1heGltdW0pIHtcclxuICAgICAgICB0aGlzLm1pbmltdW0gPSBtaW5pbXVtO1xyXG4gICAgICAgIHRoaXMubWF4aW11bSA9IG1heGltdW07XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgaW50ZWdlcihtaW4gPSAtSW5maW5pdHksIG1heCA9IEluZmluaXR5KSB7XHJcbiAgICAgICAgcmV0dXJuIG5ldyBJbnRlZ2VyQXJndW1lbnRUeXBlKG1pbiwgbWF4KTtcclxuICAgIH1cclxuICAgIHN0YXRpYyBnZXRJbnRlZ2VyKGNvbnRleHQsIG5hbWUpIHtcclxuICAgICAgICByZXR1cm4gY29udGV4dC5nZXRBcmd1bWVudChuYW1lLCBOdW1iZXIpO1xyXG4gICAgfVxyXG4gICAgZ2V0TWluaW11bSgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5taW5pbXVtO1xyXG4gICAgfVxyXG4gICAgZ2V0TWF4aW11bSgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5tYXhpbXVtO1xyXG4gICAgfVxyXG4gICAgcGFyc2UocmVhZGVyKSB7XHJcbiAgICAgICAgbGV0IHN0YXJ0ID0gcmVhZGVyLmdldEN1cnNvcigpO1xyXG4gICAgICAgIGxldCByZXN1bHQgPSByZWFkZXIucmVhZEludCgpO1xyXG4gICAgICAgIGlmIChyZXN1bHQgPCB0aGlzLm1pbmltdW0pIHtcclxuICAgICAgICAgICAgcmVhZGVyLnNldEN1cnNvcihzdGFydCk7XHJcbiAgICAgICAgICAgIHRocm93IENvbW1hbmRTeW50YXhFeGNlcHRpb25fMS5kZWZhdWx0LkJVSUxUX0lOX0VYQ0VQVElPTlMuaW50ZWdlclRvb0xvdygpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlciwgcmVzdWx0LCB0aGlzLm1pbmltdW0pO1xyXG4gICAgICAgIH1cclxuICAgICAgICBpZiAocmVzdWx0ID4gdGhpcy5tYXhpbXVtKSB7XHJcbiAgICAgICAgICAgIHJlYWRlci5zZXRDdXJzb3Ioc3RhcnQpO1xyXG4gICAgICAgICAgICB0aHJvdyBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEuZGVmYXVsdC5CVUlMVF9JTl9FWENFUFRJT05TLmludGVnZXJUb29IaWdoKCkuY3JlYXRlV2l0aENvbnRleHQocmVhZGVyLCByZXN1bHQsIHRoaXMubWF4aW11bSk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHJldHVybiByZXN1bHQ7XHJcbiAgICB9XHJcbiAgICBlcXVhbHMobykge1xyXG4gICAgICAgIGlmICh0aGlzID09PSBvKVxyXG4gICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICBpZiAoIShvIGluc3RhbmNlb2YgSW50ZWdlckFyZ3VtZW50VHlwZSkpXHJcbiAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICByZXR1cm4gdGhpcy5tYXhpbXVtID09IG8ubWF4aW11bSAmJiB0aGlzLm1pbmltdW0gPT0gby5taW5pbXVtO1xyXG4gICAgfVxyXG4gICAgdG9TdHJpbmcoKSB7XHJcbiAgICAgICAgaWYgKHRoaXMubWluaW11bSA9PT0gLUluZmluaXR5ICYmIHRoaXMubWF4aW11bSA9PT0gSW5maW5pdHkpIHtcclxuICAgICAgICAgICAgcmV0dXJuIFwiaW50ZWdlcigpXCI7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGVsc2UgaWYgKHRoaXMubWF4aW11bSA9PSBJbmZpbml0eSkge1xyXG4gICAgICAgICAgICByZXR1cm4gXCJpbnRlZ2VyKFwiICsgdGhpcy5taW5pbXVtICsgXCIpXCI7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGVsc2Uge1xyXG4gICAgICAgICAgICByZXR1cm4gXCJpbnRlZ2VyKFwiICsgdGhpcy5taW5pbXVtICsgXCIsIFwiICsgdGhpcy5tYXhpbXVtICsgXCIpXCI7XHJcbiAgICAgICAgfVxyXG4gICAgfVxyXG4gICAgZ2V0RXhhbXBsZXMoKSB7XHJcbiAgICAgICAgcmV0dXJuIEVYQU1QTEVTO1xyXG4gICAgfVxyXG59XHJcbmV4cG9ydHMuZGVmYXVsdCA9IEludGVnZXJBcmd1bWVudFR5cGU7XHJcbiIsIlwidXNlIHN0cmljdFwiO1xyXG52YXIgX19pbXBvcnREZWZhdWx0ID0gKHRoaXMgJiYgdGhpcy5fX2ltcG9ydERlZmF1bHQpIHx8IGZ1bmN0aW9uIChtb2QpIHtcclxuICAgIHJldHVybiAobW9kICYmIG1vZC5fX2VzTW9kdWxlKSA/IG1vZCA6IHsgXCJkZWZhdWx0XCI6IG1vZCB9O1xyXG59O1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNvbnN0IFN0cmluZ1JlYWRlcl8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuLi9TdHJpbmdSZWFkZXJcIikpO1xyXG52YXIgU3RyaW5nVHlwZTtcclxuKGZ1bmN0aW9uIChTdHJpbmdUeXBlKSB7XHJcbiAgICBTdHJpbmdUeXBlW1wiU0lOR0xFX1dPUkRcIl0gPSBcIndvcmRzX3dpdGhfdW5kZXJzY29yZXNcIjtcclxuICAgIFN0cmluZ1R5cGVbXCJRVU9UQUJMRV9QSFJBU0VcIl0gPSBcIlxcXCJxdW90ZWQgcGhyYXNlXFxcIlwiO1xyXG4gICAgU3RyaW5nVHlwZVtcIkdSRUVEWV9QSFJBU0VcIl0gPSBcIndvcmRzIHdpdGggc3BhY2VzXCI7XHJcbn0pKFN0cmluZ1R5cGUgPSBleHBvcnRzLlN0cmluZ1R5cGUgfHwgKGV4cG9ydHMuU3RyaW5nVHlwZSA9IHt9KSk7XHJcbmNsYXNzIFN0cmluZ0FyZ3VtZW50VHlwZSB7XHJcbiAgICBjb25zdHJ1Y3Rvcih0eXBlKSB7XHJcbiAgICAgICAgdGhpcy50eXBlID0gdHlwZTtcclxuICAgIH1cclxuICAgIHN0YXRpYyB3b3JkKCkge1xyXG4gICAgICAgIHJldHVybiBuZXcgU3RyaW5nQXJndW1lbnRUeXBlKFN0cmluZ1R5cGUuU0lOR0xFX1dPUkQpO1xyXG4gICAgfVxyXG4gICAgc3RhdGljIHN0cmluZygpIHtcclxuICAgICAgICByZXR1cm4gbmV3IFN0cmluZ0FyZ3VtZW50VHlwZShTdHJpbmdUeXBlLlFVT1RBQkxFX1BIUkFTRSk7XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgZ3JlZWR5U3RyaW5nKCkge1xyXG4gICAgICAgIHJldHVybiBuZXcgU3RyaW5nQXJndW1lbnRUeXBlKFN0cmluZ1R5cGUuR1JFRURZX1BIUkFTRSk7XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgZ2V0U3RyaW5nKGNvbnRleHQsIG5hbWUpIHtcclxuICAgICAgICByZXR1cm4gY29udGV4dC5nZXRBcmd1bWVudChuYW1lLCBTdHJpbmcpO1xyXG4gICAgfVxyXG4gICAgZ2V0VHlwZSgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy50eXBlO1xyXG4gICAgfVxyXG4gICAgcGFyc2UocmVhZGVyKSB7XHJcbiAgICAgICAgaWYgKHRoaXMudHlwZSA9PSBTdHJpbmdUeXBlLkdSRUVEWV9QSFJBU0UpIHtcclxuICAgICAgICAgICAgbGV0IHRleHQgPSByZWFkZXIuZ2V0UmVtYWluaW5nKCk7XHJcbiAgICAgICAgICAgIHJlYWRlci5zZXRDdXJzb3IocmVhZGVyLmdldFRvdGFsTGVuZ3RoKCkpO1xyXG4gICAgICAgICAgICByZXR1cm4gdGV4dDtcclxuICAgICAgICB9XHJcbiAgICAgICAgZWxzZSBpZiAodGhpcy50eXBlID09IFN0cmluZ1R5cGUuU0lOR0xFX1dPUkQpIHtcclxuICAgICAgICAgICAgcmV0dXJuIHJlYWRlci5yZWFkVW5xdW90ZWRTdHJpbmcoKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgZWxzZSB7XHJcbiAgICAgICAgICAgIHJldHVybiByZWFkZXIucmVhZFN0cmluZygpO1xyXG4gICAgICAgIH1cclxuICAgIH1cclxuICAgIHRvU3RyaW5nKCkge1xyXG4gICAgICAgIHJldHVybiBcInN0cmluZygpXCI7XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgZXNjYXBlSWZSZXF1aXJlZChpbnB1dCkge1xyXG4gICAgICAgIGZvciAobGV0IGMgb2YgaW5wdXQpIHtcclxuICAgICAgICAgICAgaWYgKCFTdHJpbmdSZWFkZXJfMS5kZWZhdWx0LmlzQWxsb3dlZEluVW5xdW90ZWRTdHJpbmcoYykpIHtcclxuICAgICAgICAgICAgICAgIHJldHVybiBTdHJpbmdBcmd1bWVudFR5cGUuZXNjYXBlKGlucHV0KTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gaW5wdXQ7XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgZXNjYXBlKGlucHV0KSB7XHJcbiAgICAgICAgbGV0IHJlc3VsdCA9IFwiXFxcIlwiO1xyXG4gICAgICAgIGZvciAobGV0IGkgPSAwOyBpIDwgaW5wdXQubGVuZ3RoOyBpKyspIHtcclxuICAgICAgICAgICAgY29uc3QgYyA9IGlucHV0LmNoYXJBdChpKTtcclxuICAgICAgICAgICAgaWYgKGMgPT0gJ1xcXFwnIHx8IGMgPT0gJ1wiJykge1xyXG4gICAgICAgICAgICAgICAgcmVzdWx0ICs9ICdcXFxcJztcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICByZXN1bHQgKz0gYztcclxuICAgICAgICB9XHJcbiAgICAgICAgcmVzdWx0ICs9IFwiXFxcIlwiO1xyXG4gICAgICAgIHJldHVybiByZXN1bHQ7XHJcbiAgICB9XHJcbn1cclxuZXhwb3J0cy5kZWZhdWx0ID0gU3RyaW5nQXJndW1lbnRUeXBlO1xyXG4iLCJcInVzZSBzdHJpY3RcIjtcclxudmFyIF9faW1wb3J0RGVmYXVsdCA9ICh0aGlzICYmIHRoaXMuX19pbXBvcnREZWZhdWx0KSB8fCBmdW5jdGlvbiAobW9kKSB7XHJcbiAgICByZXR1cm4gKG1vZCAmJiBtb2QuX19lc01vZHVsZSkgPyBtb2QgOiB7IFwiZGVmYXVsdFwiOiBtb2QgfTtcclxufTtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5jb25zdCBDb21tYW5kTm9kZV8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuLi90cmVlL0NvbW1hbmROb2RlXCIpKTtcclxuY29uc3QgUm9vdENvbW1hbmROb2RlXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4uL3RyZWUvUm9vdENvbW1hbmROb2RlXCIpKTtcclxuY2xhc3MgQXJndW1lbnRCdWlsZGVyIHtcclxuICAgIGNvbnN0cnVjdG9yKCkge1xyXG4gICAgICAgIHRoaXMuYXJncyA9IG5ldyBSb290Q29tbWFuZE5vZGVfMS5kZWZhdWx0KCk7XHJcbiAgICAgICAgdGhpcy5tb2RpZmllciA9IG51bGw7XHJcbiAgICB9XHJcbiAgICB0aGVuKGFyZykge1xyXG4gICAgICAgIGlmICghKHRoaXMudGFyZ2V0ID09IG51bGwpKSB7XHJcbiAgICAgICAgICAgIHRocm93IG5ldyBFcnJvcihcIkNhbm5vdCBhZGQgY2hpbGRyZW4gdG8gYSByZWRpcmVjdGVkIG5vZGVcIik7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGlmIChhcmcgaW5zdGFuY2VvZiBDb21tYW5kTm9kZV8xLmRlZmF1bHQpXHJcbiAgICAgICAgICAgIHRoaXMuYXJncy5hZGRDaGlsZChhcmcpO1xyXG4gICAgICAgIGVsc2VcclxuICAgICAgICAgICAgdGhpcy5hcmdzLmFkZENoaWxkKGFyZy5idWlsZCgpKTtcclxuICAgICAgICByZXR1cm4gdGhpcy5nZXRUaGlzKCk7XHJcbiAgICB9XHJcbiAgICBnZXRBcmd1bWVudHMoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuYXJncy5nZXRDaGlsZHJlbigpO1xyXG4gICAgfVxyXG4gICAgZXhlY3V0ZXMoY29tbWFuZCkge1xyXG4gICAgICAgIHRoaXMuY29tbWFuZCA9IGNvbW1hbmQ7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuZ2V0VGhpcygpO1xyXG4gICAgfVxyXG4gICAgZ2V0Q29tbWFuZCgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5jb21tYW5kO1xyXG4gICAgfVxyXG4gICAgcmVxdWlyZXMocmVxdWlyZW1lbnQpIHtcclxuICAgICAgICB0aGlzLnJlcXVpcmVtZW50ID0gcmVxdWlyZW1lbnQ7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuZ2V0VGhpcygpO1xyXG4gICAgfVxyXG4gICAgZ2V0UmVxdWlyZW1lbnQoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMucmVxdWlyZW1lbnQ7XHJcbiAgICB9XHJcbiAgICByZWRpcmVjdCh0YXJnZXQsIG1vZGlmaWVyKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuZm9yd2FyZCh0YXJnZXQsIG1vZGlmaWVyID09IG51bGwgPyBudWxsIDogKG8pID0+IFttb2RpZmllci5hcHBseShvKV0sIGZhbHNlKTtcclxuICAgIH1cclxuICAgIGZvcmsodGFyZ2V0LCBtb2RpZmllcikge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmZvcndhcmQodGFyZ2V0LCBtb2RpZmllciwgdHJ1ZSk7XHJcbiAgICB9XHJcbiAgICBmb3J3YXJkKHRhcmdldCwgbW9kaWZpZXIsIGZvcmspIHtcclxuICAgICAgICBpZiAodGhpcy5hcmdzLmdldENoaWxkcmVuQ291bnQoKSA+IDApIHtcclxuICAgICAgICAgICAgdGhyb3cgbmV3IEVycm9yKFwiQ2Fubm90IGZvcndhcmQgYSBub2RlIHdpdGggY2hpbGRyZW5cIik7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHRoaXMudGFyZ2V0ID0gdGFyZ2V0O1xyXG4gICAgICAgIHRoaXMubW9kaWZpZXIgPSBtb2RpZmllcjtcclxuICAgICAgICB0aGlzLmZvcmtzID0gZm9yaztcclxuICAgICAgICByZXR1cm4gdGhpcy5nZXRUaGlzKCk7XHJcbiAgICB9XHJcbiAgICBnZXRSZWRpcmVjdCgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy50YXJnZXQ7XHJcbiAgICB9XHJcbiAgICBnZXRSZWRpcmVjdE1vZGlmaWVyKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLm1vZGlmaWVyO1xyXG4gICAgfVxyXG4gICAgaXNGb3JrKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmZvcmtzO1xyXG4gICAgfVxyXG59XHJcbmV4cG9ydHMuZGVmYXVsdCA9IEFyZ3VtZW50QnVpbGRlcjtcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbnZhciBfX2ltcG9ydERlZmF1bHQgPSAodGhpcyAmJiB0aGlzLl9faW1wb3J0RGVmYXVsdCkgfHwgZnVuY3Rpb24gKG1vZCkge1xyXG4gICAgcmV0dXJuIChtb2QgJiYgbW9kLl9fZXNNb2R1bGUpID8gbW9kIDogeyBcImRlZmF1bHRcIjogbW9kIH07XHJcbn07XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY29uc3QgTGl0ZXJhbENvbW1hbmROb2RlXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4uL3RyZWUvTGl0ZXJhbENvbW1hbmROb2RlXCIpKTtcclxuY29uc3QgQXJndW1lbnRCdWlsZGVyXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vQXJndW1lbnRCdWlsZGVyXCIpKTtcclxuY2xhc3MgTGl0ZXJhbEFyZ3VtZW50QnVpbGRlciBleHRlbmRzIEFyZ3VtZW50QnVpbGRlcl8xLmRlZmF1bHQge1xyXG4gICAgY29uc3RydWN0b3IobGl0ZXJhbCkge1xyXG4gICAgICAgIHN1cGVyKCk7XHJcbiAgICAgICAgdGhpcy5saXRlcmFsID0gbGl0ZXJhbDtcclxuICAgIH1cclxuICAgIHN0YXRpYyBsaXRlcmFsKG5hbWUpIHtcclxuICAgICAgICByZXR1cm4gbmV3IExpdGVyYWxBcmd1bWVudEJ1aWxkZXIobmFtZSk7XHJcbiAgICB9XHJcbiAgICBnZXRUaGlzKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzO1xyXG4gICAgfVxyXG4gICAgZ2V0TGl0ZXJhbCgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5saXRlcmFsO1xyXG4gICAgfVxyXG4gICAgYnVpbGQoKSB7XHJcbiAgICAgICAgbGV0IHJlc3VsdCA9IG5ldyBMaXRlcmFsQ29tbWFuZE5vZGVfMS5kZWZhdWx0KHRoaXMuZ2V0TGl0ZXJhbCgpLCB0aGlzLmdldENvbW1hbmQoKSwgdGhpcy5nZXRSZXF1aXJlbWVudCgpLCB0aGlzLmdldFJlZGlyZWN0KCksIHRoaXMuZ2V0UmVkaXJlY3RNb2RpZmllcigpLCB0aGlzLmlzRm9yaygpKTtcclxuICAgICAgICBmb3IgKGxldCBhcmcgb2YgdGhpcy5nZXRBcmd1bWVudHMoKSkge1xyXG4gICAgICAgICAgICByZXN1bHQuYWRkQ2hpbGQoYXJnKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgcmV0dXJuIHJlc3VsdDtcclxuICAgIH1cclxufVxyXG5leHBvcnRzLmRlZmF1bHQgPSBMaXRlcmFsQXJndW1lbnRCdWlsZGVyO1xyXG5leHBvcnRzLmxpdGVyYWwgPSBMaXRlcmFsQXJndW1lbnRCdWlsZGVyLmxpdGVyYWw7XHJcbiIsIlwidXNlIHN0cmljdFwiO1xyXG52YXIgX19pbXBvcnREZWZhdWx0ID0gKHRoaXMgJiYgdGhpcy5fX2ltcG9ydERlZmF1bHQpIHx8IGZ1bmN0aW9uIChtb2QpIHtcclxuICAgIHJldHVybiAobW9kICYmIG1vZC5fX2VzTW9kdWxlKSA/IG1vZCA6IHsgXCJkZWZhdWx0XCI6IG1vZCB9O1xyXG59O1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNvbnN0IEFyZ3VtZW50Q29tbWFuZE5vZGVfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi4vdHJlZS9Bcmd1bWVudENvbW1hbmROb2RlXCIpKTtcclxuY29uc3QgQXJndW1lbnRCdWlsZGVyXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vQXJndW1lbnRCdWlsZGVyXCIpKTtcclxuY2xhc3MgUmVxdWlyZWRBcmd1bWVudEJ1aWxkZXIgZXh0ZW5kcyBBcmd1bWVudEJ1aWxkZXJfMS5kZWZhdWx0IHtcclxuICAgIGNvbnN0cnVjdG9yKG5hbWUsIHR5cGUpIHtcclxuICAgICAgICBzdXBlcigpO1xyXG4gICAgICAgIHRoaXMubmFtZSA9IG5hbWU7XHJcbiAgICAgICAgdGhpcy50eXBlID0gdHlwZTtcclxuICAgIH1cclxuICAgIHN0YXRpYyBhcmd1bWVudChuYW1lLCB0eXBlKSB7XHJcbiAgICAgICAgcmV0dXJuIG5ldyBSZXF1aXJlZEFyZ3VtZW50QnVpbGRlcihuYW1lLCB0eXBlKTtcclxuICAgIH1cclxuICAgIHN1Z2dlc3RzKHByb3ZpZGVyKSB7XHJcbiAgICAgICAgdGhpcy5zdWdnZXN0aW9uc1Byb3ZpZGVyID0gcHJvdmlkZXI7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuZ2V0VGhpcygpO1xyXG4gICAgfVxyXG4gICAgZ2V0U3VnZ2VzdGlvbnNQcm92aWRlcigpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5zdWdnZXN0aW9uc1Byb3ZpZGVyO1xyXG4gICAgfVxyXG4gICAgZ2V0VGhpcygpIHtcclxuICAgICAgICByZXR1cm4gdGhpcztcclxuICAgIH1cclxuICAgIGdldFR5cGUoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMudHlwZTtcclxuICAgIH1cclxuICAgIGdldE5hbWUoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMubmFtZTtcclxuICAgIH1cclxuICAgIGJ1aWxkKCkge1xyXG4gICAgICAgIGxldCByZXN1bHQgPSBuZXcgQXJndW1lbnRDb21tYW5kTm9kZV8xLmRlZmF1bHQodGhpcy5nZXROYW1lKCksIHRoaXMuZ2V0VHlwZSgpLCB0aGlzLmdldENvbW1hbmQoKSwgdGhpcy5nZXRSZXF1aXJlbWVudCgpLCB0aGlzLmdldFJlZGlyZWN0KCksIHRoaXMuZ2V0UmVkaXJlY3RNb2RpZmllcigpLCB0aGlzLmlzRm9yaygpLCB0aGlzLmdldFN1Z2dlc3Rpb25zUHJvdmlkZXIoKSk7XHJcbiAgICAgICAgZm9yIChsZXQgYXJnIG9mIHRoaXMuZ2V0QXJndW1lbnRzKCkpIHtcclxuICAgICAgICAgICAgcmVzdWx0LmFkZENoaWxkKGFyZyk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHJldHVybiByZXN1bHQ7XHJcbiAgICB9XHJcbn1cclxuZXhwb3J0cy5kZWZhdWx0ID0gUmVxdWlyZWRBcmd1bWVudEJ1aWxkZXI7XHJcbmV4cG9ydHMuYXJndW1lbnQgPSBSZXF1aXJlZEFyZ3VtZW50QnVpbGRlci5hcmd1bWVudDtcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbnZhciBfX2ltcG9ydERlZmF1bHQgPSAodGhpcyAmJiB0aGlzLl9faW1wb3J0RGVmYXVsdCkgfHwgZnVuY3Rpb24gKG1vZCkge1xyXG4gICAgcmV0dXJuIChtb2QgJiYgbW9kLl9fZXNNb2R1bGUpID8gbW9kIDogeyBcImRlZmF1bHRcIjogbW9kIH07XHJcbn07XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY29uc3QgaXNFcXVhbF8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuLi91dGlsL2lzRXF1YWxcIikpO1xyXG5jbGFzcyBDb21tYW5kQ29udGV4dCB7XHJcbiAgICBjb25zdHJ1Y3Rvcihzb3VyY2UsIGlucHV0LCBhcmdzLCBjb21tYW5kLCByb290Tm9kZSwgbm9kZXMsIHJhbmdlLCBjaGlsZCwgbW9kaWZpZXIsIGZvcmtzKSB7XHJcbiAgICAgICAgdGhpcy5zb3VyY2UgPSBzb3VyY2U7XHJcbiAgICAgICAgdGhpcy5pbnB1dCA9IGlucHV0O1xyXG4gICAgICAgIHRoaXMuYXJncyA9IGFyZ3M7XHJcbiAgICAgICAgdGhpcy5jb21tYW5kID0gY29tbWFuZDtcclxuICAgICAgICB0aGlzLnJvb3ROb2RlID0gcm9vdE5vZGU7XHJcbiAgICAgICAgdGhpcy5ub2RlcyA9IG5vZGVzO1xyXG4gICAgICAgIHRoaXMucmFuZ2UgPSByYW5nZTtcclxuICAgICAgICB0aGlzLmNoaWxkID0gY2hpbGQ7XHJcbiAgICAgICAgdGhpcy5tb2RpZmllciA9IG1vZGlmaWVyO1xyXG4gICAgICAgIHRoaXMuZm9ya3MgPSBmb3JrcztcclxuICAgIH1cclxuICAgIGNvcHlGb3Ioc291cmNlKSB7XHJcbiAgICAgICAgaWYgKHRoaXMuc291cmNlID09PSBzb3VyY2UpXHJcbiAgICAgICAgICAgIHJldHVybiB0aGlzO1xyXG4gICAgICAgIHJldHVybiBuZXcgQ29tbWFuZENvbnRleHQoc291cmNlLCB0aGlzLmlucHV0LCB0aGlzLmFyZ3MsIHRoaXMuY29tbWFuZCwgdGhpcy5yb290Tm9kZSwgdGhpcy5ub2RlcywgdGhpcy5yYW5nZSwgdGhpcy5jaGlsZCwgdGhpcy5tb2RpZmllciwgdGhpcy5mb3Jrcyk7XHJcbiAgICB9XHJcbiAgICBnZXRDaGlsZCgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5jaGlsZDtcclxuICAgIH1cclxuICAgIGdldExhc3RDaGlsZCgpIHtcclxuICAgICAgICBsZXQgcmVzdWx0ID0gdGhpcztcclxuICAgICAgICB3aGlsZSAoIShyZXN1bHQuZ2V0Q2hpbGQoKSA9PSBudWxsKSkge1xyXG4gICAgICAgICAgICByZXN1bHQgPSByZXN1bHQuZ2V0Q2hpbGQoKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgcmV0dXJuIHJlc3VsdDtcclxuICAgIH1cclxuICAgIGdldENvbW1hbmQoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuY29tbWFuZDtcclxuICAgIH1cclxuICAgIGdldFNvdXJjZSgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5zb3VyY2U7XHJcbiAgICB9XHJcbiAgICBnZXRBcmd1bWVudChuYW1lLCBjbGF6eikge1xyXG4gICAgICAgIGNvbnN0IGFyZyA9IHRoaXMuYXJncy5nZXQobmFtZSk7XHJcbiAgICAgICAgaWYgKGFyZyA9PSBudWxsKSB7XHJcbiAgICAgICAgICAgIHRocm93IG5ldyBFcnJvcihcIk5vIHN1Y2ggYXJndW1lbnQgJ1wiICsgbmFtZSArIFwiJyBleGlzdHMgb24gdGhpcyBjb21tYW5kXCIpO1xyXG4gICAgICAgIH1cclxuICAgICAgICBsZXQgcmVzdWx0ID0gYXJnLmdldFJlc3VsdCgpO1xyXG4gICAgICAgIGlmIChjbGF6eiA9PSBudWxsKSB7XHJcbiAgICAgICAgICAgIHJldHVybiByZXN1bHQ7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGVsc2Uge1xyXG4gICAgICAgICAgICByZXR1cm4gY2xhenoocmVzdWx0KTtcclxuICAgICAgICB9XHJcbiAgICB9XHJcbiAgICBlcXVhbHMobykge1xyXG4gICAgICAgIGlmICh0aGlzID09PSBvKVxyXG4gICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICBpZiAoIShvIGluc3RhbmNlb2YgQ29tbWFuZENvbnRleHQpKVxyXG4gICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICAgICAgaWYgKCFpc0VxdWFsXzEuZGVmYXVsdCh0aGlzLmFyZ3MsIG8uYXJncykpXHJcbiAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICBpZiAoIXRoaXMucm9vdE5vZGUuZXF1YWxzKG8ucm9vdE5vZGUpKVxyXG4gICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICAgICAgaWYgKHRoaXMubm9kZXMubGVuZ3RoICE9IG8ubm9kZXMubGVuZ3RoIHx8ICFpc0VxdWFsXzEuZGVmYXVsdCh0aGlzLm5vZGVzLCBvLm5vZGVzKSlcclxuICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgIGlmICghKHRoaXMuY29tbWFuZCA9PSBudWxsKSA/ICFpc0VxdWFsXzEuZGVmYXVsdCh0aGlzLmNvbW1hbmQsIG8uY29tbWFuZCkgOiBvLmNvbW1hbmQgIT0gbnVsbClcclxuICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgIGlmICghaXNFcXVhbF8xLmRlZmF1bHQodGhpcy5zb3VyY2UsIG8uc291cmNlKSlcclxuICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgIGlmICghKHRoaXMuY2hpbGQgPT0gbnVsbCkgPyAhdGhpcy5jaGlsZC5lcXVhbHMoby5jaGlsZCkgOiBvLmNoaWxkICE9IG51bGwpXHJcbiAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgIH1cclxuICAgIGdldFJlZGlyZWN0TW9kaWZpZXIoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMubW9kaWZpZXI7XHJcbiAgICB9XHJcbiAgICBnZXRSYW5nZSgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5yYW5nZTtcclxuICAgIH1cclxuICAgIGdldElucHV0KCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmlucHV0O1xyXG4gICAgfVxyXG4gICAgZ2V0Um9vdE5vZGUoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMucm9vdE5vZGU7XHJcbiAgICB9XHJcbiAgICBnZXROb2RlcygpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5ub2RlcztcclxuICAgIH1cclxuICAgIGhhc05vZGVzKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLm5vZGVzLmxlbmd0aCA+PSAwO1xyXG4gICAgfVxyXG4gICAgaXNGb3JrZWQoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuZm9ya3M7XHJcbiAgICB9XHJcbn1cclxuZXhwb3J0cy5kZWZhdWx0ID0gQ29tbWFuZENvbnRleHQ7XHJcbiIsIlwidXNlIHN0cmljdFwiO1xyXG52YXIgX19pbXBvcnREZWZhdWx0ID0gKHRoaXMgJiYgdGhpcy5fX2ltcG9ydERlZmF1bHQpIHx8IGZ1bmN0aW9uIChtb2QpIHtcclxuICAgIHJldHVybiAobW9kICYmIG1vZC5fX2VzTW9kdWxlKSA/IG1vZCA6IHsgXCJkZWZhdWx0XCI6IG1vZCB9O1xyXG59O1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNvbnN0IFN0cmluZ1JhbmdlXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vU3RyaW5nUmFuZ2VcIikpO1xyXG5jb25zdCBDb21tYW5kQ29udGV4dF8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL0NvbW1hbmRDb250ZXh0XCIpKTtcclxuY29uc3QgU3VnZ2VzdGlvbkNvbnRleHRfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9TdWdnZXN0aW9uQ29udGV4dFwiKSk7XHJcbmNvbnN0IFBhcnNlZENvbW1hbmROb2RlXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vUGFyc2VkQ29tbWFuZE5vZGVcIikpO1xyXG5jbGFzcyBDb21tYW5kQ29udGV4dEJ1aWxkZXIge1xyXG4gICAgY29uc3RydWN0b3IoZGlzcGF0Y2hlciwgc291cmNlLCByb290Tm9kZSwgc3RhcnQpIHtcclxuICAgICAgICB0aGlzLmFyZ3MgPSBuZXcgTWFwKCk7XHJcbiAgICAgICAgdGhpcy5ub2RlcyA9IFtdO1xyXG4gICAgICAgIHRoaXMubW9kaWZpZXIgPSBudWxsO1xyXG4gICAgICAgIHRoaXMucm9vdE5vZGUgPSByb290Tm9kZTtcclxuICAgICAgICB0aGlzLmRpc3BhdGNoZXIgPSBkaXNwYXRjaGVyO1xyXG4gICAgICAgIHRoaXMuc291cmNlID0gc291cmNlO1xyXG4gICAgICAgIHRoaXMucmFuZ2UgPSBTdHJpbmdSYW5nZV8xLmRlZmF1bHQuYXQoc3RhcnQpO1xyXG4gICAgfVxyXG4gICAgd2l0aFNvdXJjZShzb3VyY2UpIHtcclxuICAgICAgICB0aGlzLnNvdXJjZSA9IHNvdXJjZTtcclxuICAgICAgICByZXR1cm4gdGhpcztcclxuICAgIH1cclxuICAgIGdldFNvdXJjZSgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5zb3VyY2U7XHJcbiAgICB9XHJcbiAgICBnZXRSb290Tm9kZSgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5yb290Tm9kZTtcclxuICAgIH1cclxuICAgIHdpdGhBcmd1bWVudChuYW1lLCBhcmd1bWVudCkge1xyXG4gICAgICAgIHRoaXMuYXJncy5zZXQobmFtZSwgYXJndW1lbnQpO1xyXG4gICAgICAgIHJldHVybiB0aGlzO1xyXG4gICAgfVxyXG4gICAgZ2V0QXJndW1lbnRzKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmFyZ3M7XHJcbiAgICB9XHJcbiAgICB3aXRoQ29tbWFuZChjb21tYW5kKSB7XHJcbiAgICAgICAgdGhpcy5jb21tYW5kID0gY29tbWFuZDtcclxuICAgICAgICByZXR1cm4gdGhpcztcclxuICAgIH1cclxuICAgIHdpdGhOb2RlKG5vZGUsIHJhbmdlKSB7XHJcbiAgICAgICAgdGhpcy5ub2Rlcy5wdXNoKG5ldyBQYXJzZWRDb21tYW5kTm9kZV8xLmRlZmF1bHQobm9kZSwgcmFuZ2UpKTtcclxuICAgICAgICB0aGlzLnJhbmdlID0gU3RyaW5nUmFuZ2VfMS5kZWZhdWx0LmVuY29tcGFzc2luZyh0aGlzLnJhbmdlLCByYW5nZSk7XHJcbiAgICAgICAgdGhpcy5tb2RpZmllciA9IG5vZGUuZ2V0UmVkaXJlY3RNb2RpZmllcigpO1xyXG4gICAgICAgIHRoaXMuZm9ya3MgPSBub2RlLmlzRm9yaygpO1xyXG4gICAgICAgIHJldHVybiB0aGlzO1xyXG4gICAgfVxyXG4gICAgY29weSgpIHtcclxuICAgICAgICBjb25zdCBjb3B5ID0gbmV3IENvbW1hbmRDb250ZXh0QnVpbGRlcih0aGlzLmRpc3BhdGNoZXIsIHRoaXMuc291cmNlLCB0aGlzLnJvb3ROb2RlLCB0aGlzLnJhbmdlLmdldFN0YXJ0KCkpO1xyXG4gICAgICAgIGNvcHkuY29tbWFuZCA9IHRoaXMuY29tbWFuZDtcclxuICAgICAgICBjb3B5LmFyZ3MgPSBuZXcgTWFwKFsuLi5jb3B5LmFyZ3MsIC4uLnRoaXMuYXJnc10pO1xyXG4gICAgICAgIGNvcHkubm9kZXMucHVzaCguLi50aGlzLm5vZGVzKTtcclxuICAgICAgICBjb3B5LmNoaWxkID0gdGhpcy5jaGlsZDtcclxuICAgICAgICBjb3B5LnJhbmdlID0gdGhpcy5yYW5nZTtcclxuICAgICAgICBjb3B5LmZvcmtzID0gdGhpcy5mb3JrcztcclxuICAgICAgICByZXR1cm4gY29weTtcclxuICAgIH1cclxuICAgIHdpdGhDaGlsZChjaGlsZCkge1xyXG4gICAgICAgIHRoaXMuY2hpbGQgPSBjaGlsZDtcclxuICAgICAgICByZXR1cm4gdGhpcztcclxuICAgIH1cclxuICAgIGdldENoaWxkKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmNoaWxkO1xyXG4gICAgfVxyXG4gICAgZ2V0TGFzdENoaWxkKCkge1xyXG4gICAgICAgIGxldCByZXN1bHQgPSB0aGlzO1xyXG4gICAgICAgIHdoaWxlIChyZXN1bHQuZ2V0Q2hpbGQoKSAhPSBudWxsKSB7XHJcbiAgICAgICAgICAgIHJlc3VsdCA9IHJlc3VsdC5nZXRDaGlsZCgpO1xyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gcmVzdWx0O1xyXG4gICAgfVxyXG4gICAgZ2V0Q29tbWFuZCgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5jb21tYW5kO1xyXG4gICAgfVxyXG4gICAgZ2V0Tm9kZXMoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMubm9kZXM7XHJcbiAgICB9XHJcbiAgICBidWlsZChpbnB1dCkge1xyXG4gICAgICAgIHJldHVybiBuZXcgQ29tbWFuZENvbnRleHRfMS5kZWZhdWx0KHRoaXMuc291cmNlLCBpbnB1dCwgdGhpcy5hcmdzLCB0aGlzLmNvbW1hbmQsIHRoaXMucm9vdE5vZGUsIHRoaXMubm9kZXMsIHRoaXMucmFuZ2UsIHRoaXMuY2hpbGQgPT0gbnVsbCA/IG51bGwgOiB0aGlzLmNoaWxkLmJ1aWxkKGlucHV0KSwgdGhpcy5tb2RpZmllciwgdGhpcy5mb3Jrcyk7XHJcbiAgICB9XHJcbiAgICBnZXREaXNwYXRjaGVyKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmRpc3BhdGNoZXI7XHJcbiAgICB9XHJcbiAgICBnZXRSYW5nZSgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5yYW5nZTtcclxuICAgIH1cclxuICAgIGZpbmRTdWdnZXN0aW9uQ29udGV4dChjdXJzb3IpIHtcclxuICAgICAgICBpZiAoKHRoaXMucmFuZ2UuZ2V0U3RhcnQoKSA8PSBjdXJzb3IpKSB7XHJcbiAgICAgICAgICAgIGlmICgodGhpcy5yYW5nZS5nZXRFbmQoKSA8IGN1cnNvcikpIHtcclxuICAgICAgICAgICAgICAgIGlmICgodGhpcy5jaGlsZCAhPSBudWxsKSkge1xyXG4gICAgICAgICAgICAgICAgICAgIHJldHVybiB0aGlzLmNoaWxkLmZpbmRTdWdnZXN0aW9uQ29udGV4dChjdXJzb3IpO1xyXG4gICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgZWxzZSBpZiAodGhpcy5ub2Rlcy5sZW5ndGggPiAwKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgbGV0IGxhc3QgPSB0aGlzLm5vZGVzW3RoaXMubm9kZXMubGVuZ3RoIC0gMV07XHJcbiAgICAgICAgICAgICAgICAgICAgcmV0dXJuIG5ldyBTdWdnZXN0aW9uQ29udGV4dF8xLmRlZmF1bHQobGFzdC5nZXROb2RlKCksIGxhc3QuZ2V0UmFuZ2UoKS5nZXRFbmQoKSArIDEpO1xyXG4gICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgZWxzZSB7XHJcbiAgICAgICAgICAgICAgICAgICAgcmV0dXJuIG5ldyBTdWdnZXN0aW9uQ29udGV4dF8xLmRlZmF1bHQodGhpcy5yb290Tm9kZSwgdGhpcy5yYW5nZS5nZXRTdGFydCgpKTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICBlbHNlIHtcclxuICAgICAgICAgICAgICAgIGxldCBwcmV2ID0gdGhpcy5yb290Tm9kZTtcclxuICAgICAgICAgICAgICAgIGZvciAobGV0IG5vZGUgb2YgdGhpcy5ub2Rlcykge1xyXG4gICAgICAgICAgICAgICAgICAgIGxldCBub2RlUmFuZ2UgPSBub2RlLmdldFJhbmdlKCk7XHJcbiAgICAgICAgICAgICAgICAgICAgaWYgKG5vZGVSYW5nZS5nZXRTdGFydCgpIDw9IGN1cnNvciAmJiBjdXJzb3IgPD0gbm9kZVJhbmdlLmdldEVuZCgpKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHJldHVybiBuZXcgU3VnZ2VzdGlvbkNvbnRleHRfMS5kZWZhdWx0KHByZXYsIG5vZGVSYW5nZS5nZXRTdGFydCgpKTtcclxuICAgICAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICAgICAgcHJldiA9IG5vZGUuZ2V0Tm9kZSgpO1xyXG4gICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgaWYgKChwcmV2ID09IG51bGwpKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgdGhyb3cgbmV3IEVycm9yKFwiQ2FuJ3QgZmluZCBub2RlIGJlZm9yZSBjdXJzb3JcIik7XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gbmV3IFN1Z2dlc3Rpb25Db250ZXh0XzEuZGVmYXVsdChwcmV2LCB0aGlzLnJhbmdlLmdldFN0YXJ0KCkpO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHRocm93IG5ldyBFcnJvcihcIkNhbid0IGZpbmQgbm9kZSBiZWZvcmUgY3Vyc29yXCIpO1xyXG4gICAgfVxyXG59XHJcbmV4cG9ydHMuZGVmYXVsdCA9IENvbW1hbmRDb250ZXh0QnVpbGRlcjtcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbnZhciBfX2ltcG9ydERlZmF1bHQgPSAodGhpcyAmJiB0aGlzLl9faW1wb3J0RGVmYXVsdCkgfHwgZnVuY3Rpb24gKG1vZCkge1xyXG4gICAgcmV0dXJuIChtb2QgJiYgbW9kLl9fZXNNb2R1bGUpID8gbW9kIDogeyBcImRlZmF1bHRcIjogbW9kIH07XHJcbn07XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY29uc3QgU3RyaW5nUmFuZ2VfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9TdHJpbmdSYW5nZVwiKSk7XHJcbmNsYXNzIFBhcnNlZEFyZ3VtZW50IHtcclxuICAgIGNvbnN0cnVjdG9yKHN0YXJ0LCBlbmQsIHJlc3VsdCkge1xyXG4gICAgICAgIHRoaXMucmFuZ2UgPSBTdHJpbmdSYW5nZV8xLmRlZmF1bHQuYmV0d2VlbihzdGFydCwgZW5kKTtcclxuICAgICAgICB0aGlzLnJlc3VsdCA9IHJlc3VsdDtcclxuICAgIH1cclxuICAgIGdldFJhbmdlKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnJhbmdlO1xyXG4gICAgfVxyXG4gICAgZ2V0UmVzdWx0KCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnJlc3VsdDtcclxuICAgIH1cclxuICAgIGVxdWFscyhvKSB7XHJcbiAgICAgICAgaWYgKHRoaXMgPT09IG8pXHJcbiAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgIGlmICghKG8gaW5zdGFuY2VvZiBQYXJzZWRBcmd1bWVudCkpXHJcbiAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICByZXR1cm4gdGhpcy5yYW5nZS5lcXVhbHMoby5yYW5nZSkgJiYgdGhpcy5yZXN1bHQgPT09IG8ucmVzdWx0O1xyXG4gICAgfVxyXG59XHJcbmV4cG9ydHMuZGVmYXVsdCA9IFBhcnNlZEFyZ3VtZW50O1xyXG4iLCJcInVzZSBzdHJpY3RcIjtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5jbGFzcyBQYXJzZWRDb21tYW5kTm9kZSB7XHJcbiAgICBjb25zdHJ1Y3Rvcihub2RlLCByYW5nZSkge1xyXG4gICAgICAgIHRoaXMubm9kZSA9IG5vZGU7XHJcbiAgICAgICAgdGhpcy5yYW5nZSA9IHJhbmdlO1xyXG4gICAgfVxyXG4gICAgZ2V0Tm9kZSgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5ub2RlO1xyXG4gICAgfVxyXG4gICAgZ2V0UmFuZ2UoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMucmFuZ2U7XHJcbiAgICB9XHJcbiAgICB0b1N0cmluZygpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5ub2RlICsgXCJAXCIgKyB0aGlzLnJhbmdlO1xyXG4gICAgfVxyXG4gICAgZXF1YWxzKG8pIHtcclxuICAgICAgICBpZiAodGhpcyA9PT0gbylcclxuICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgaWYgKG8gPT0gbnVsbCB8fCAhKG8gaW5zdGFuY2VvZiBQYXJzZWRDb21tYW5kTm9kZSkpIHtcclxuICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gdGhpcy5ub2RlLmVxdWFscyhvLm5vZGUpICYmIHRoaXMucmFuZ2UuZXF1YWxzKG8ucmFuZ2UpO1xyXG4gICAgfVxyXG59XHJcbmV4cG9ydHMuZGVmYXVsdCA9IFBhcnNlZENvbW1hbmROb2RlO1xyXG4iLCJcInVzZSBzdHJpY3RcIjtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5jbGFzcyBTdHJpbmdSYW5nZSB7XHJcbiAgICBjb25zdHJ1Y3RvcihzdGFydCwgZW5kKSB7XHJcbiAgICAgICAgdGhpcy5zdGFydCA9IHN0YXJ0O1xyXG4gICAgICAgIHRoaXMuZW5kID0gZW5kO1xyXG4gICAgfVxyXG4gICAgc3RhdGljIGF0KHBvcykge1xyXG4gICAgICAgIHJldHVybiBuZXcgU3RyaW5nUmFuZ2UocG9zLCBwb3MpO1xyXG4gICAgfVxyXG4gICAgc3RhdGljIGJldHdlZW4oc3RhcnQsIGVuZCkge1xyXG4gICAgICAgIHJldHVybiBuZXcgU3RyaW5nUmFuZ2Uoc3RhcnQsIGVuZCk7XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgZW5jb21wYXNzaW5nKGEsIGIpIHtcclxuICAgICAgICByZXR1cm4gbmV3IFN0cmluZ1JhbmdlKE1hdGgubWluKGEuZ2V0U3RhcnQoKSwgYi5nZXRTdGFydCgpKSwgTWF0aC5tYXgoYS5nZXRFbmQoKSwgYi5nZXRFbmQoKSkpO1xyXG4gICAgfVxyXG4gICAgZ2V0U3RhcnQoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuc3RhcnQ7XHJcbiAgICB9XHJcbiAgICBnZXRFbmQoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuZW5kO1xyXG4gICAgfVxyXG4gICAgZ2V0KHN0cikge1xyXG4gICAgICAgIGlmICh0eXBlb2Ygc3RyID09PSBcInN0cmluZ1wiKVxyXG4gICAgICAgICAgICByZXR1cm4gc3RyLnN1YnN0cmluZyh0aGlzLnN0YXJ0LCB0aGlzLmVuZCk7XHJcbiAgICAgICAgZWxzZVxyXG4gICAgICAgICAgICByZXR1cm4gc3RyLmdldFN0cmluZygpLnN1YnN0cmluZyh0aGlzLnN0YXJ0LCB0aGlzLmVuZCk7XHJcbiAgICB9XHJcbiAgICBpc0VtcHR5KCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnN0YXJ0ID09PSB0aGlzLmVuZDtcclxuICAgIH1cclxuICAgIGdldExlbmd0aCgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5lbmQgLSB0aGlzLnN0YXJ0O1xyXG4gICAgfVxyXG4gICAgZXF1YWxzKG8pIHtcclxuICAgICAgICBpZiAodGhpcyA9PT0gbylcclxuICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgaWYgKCEobyBpbnN0YW5jZW9mIFN0cmluZ1JhbmdlKSlcclxuICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgIHJldHVybiB0aGlzLnN0YXJ0ID09PSBvLnN0YXJ0ICYmIHRoaXMuZW5kID09IG8uZW5kO1xyXG4gICAgfVxyXG4gICAgdG9TdHJpbmcoKSB7XHJcbiAgICAgICAgcmV0dXJuIFwiU3RyaW5nUmFuZ2V7XCIgKyBcInN0YXJ0PVwiICsgdGhpcy5zdGFydCArIFwiLCBlbmQ9XCIgKyB0aGlzLmVuZCArICd9JztcclxuICAgIH1cclxufVxyXG5leHBvcnRzLmRlZmF1bHQgPSBTdHJpbmdSYW5nZTtcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY2xhc3MgU3VnZ2VzdGlvbkNvbnRleHQge1xyXG4gICAgY29uc3RydWN0b3IocGFyZW50LCBzdGFydFBvcykge1xyXG4gICAgICAgIHRoaXMucGFyZW50ID0gcGFyZW50O1xyXG4gICAgICAgIHRoaXMuc3RhcnRQb3MgPSBzdGFydFBvcztcclxuICAgIH1cclxufVxyXG5leHBvcnRzLmRlZmF1bHQgPSBTdWdnZXN0aW9uQ29udGV4dDtcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbnZhciBfX2ltcG9ydERlZmF1bHQgPSAodGhpcyAmJiB0aGlzLl9faW1wb3J0RGVmYXVsdCkgfHwgZnVuY3Rpb24gKG1vZCkge1xyXG4gICAgcmV0dXJuIChtb2QgJiYgbW9kLl9fZXNNb2R1bGUpID8gbW9kIDogeyBcImRlZmF1bHRcIjogbW9kIH07XHJcbn07XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY29uc3QgTGl0ZXJhbE1lc3NhZ2VfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi4vTGl0ZXJhbE1lc3NhZ2VcIikpO1xyXG5jb25zdCBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZV8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL1NpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlXCIpKTtcclxuY29uc3QgRHluYW1pY0NvbW1hbmRFeGNlcHRpb25UeXBlXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vRHluYW1pY0NvbW1hbmRFeGNlcHRpb25UeXBlXCIpKTtcclxuY2xhc3MgQnVpbHRJbkV4Y2VwdGlvbnMge1xyXG4gICAgZmxvYXRUb29Mb3coKSB7XHJcbiAgICAgICAgcmV0dXJuIEJ1aWx0SW5FeGNlcHRpb25zLkZMT0FUX1RPT19TTUFMTDtcclxuICAgIH1cclxuICAgIGZsb2F0VG9vSGlnaCgpIHtcclxuICAgICAgICByZXR1cm4gQnVpbHRJbkV4Y2VwdGlvbnMuRkxPQVRfVE9PX0JJRztcclxuICAgIH1cclxuICAgIGludGVnZXJUb29Mb3coKSB7XHJcbiAgICAgICAgcmV0dXJuIEJ1aWx0SW5FeGNlcHRpb25zLklOVEVHRVJfVE9PX1NNQUxMO1xyXG4gICAgfVxyXG4gICAgaW50ZWdlclRvb0hpZ2goKSB7XHJcbiAgICAgICAgcmV0dXJuIEJ1aWx0SW5FeGNlcHRpb25zLklOVEVHRVJfVE9PX0JJRztcclxuICAgIH1cclxuICAgIGxpdGVyYWxJbmNvcnJlY3QoKSB7XHJcbiAgICAgICAgcmV0dXJuIEJ1aWx0SW5FeGNlcHRpb25zLkxJVEVSQUxfSU5DT1JSRUNUO1xyXG4gICAgfVxyXG4gICAgcmVhZGVyRXhwZWN0ZWRTdGFydE9mUXVvdGUoKSB7XHJcbiAgICAgICAgcmV0dXJuIEJ1aWx0SW5FeGNlcHRpb25zLlJFQURFUl9FWFBFQ1RFRF9TVEFSVF9PRl9RVU9URTtcclxuICAgIH1cclxuICAgIHJlYWRlckV4cGVjdGVkRW5kT2ZRdW90ZSgpIHtcclxuICAgICAgICByZXR1cm4gQnVpbHRJbkV4Y2VwdGlvbnMuUkVBREVSX0VYUEVDVEVEX0VORF9PRl9RVU9URTtcclxuICAgIH1cclxuICAgIHJlYWRlckludmFsaWRFc2NhcGUoKSB7XHJcbiAgICAgICAgcmV0dXJuIEJ1aWx0SW5FeGNlcHRpb25zLlJFQURFUl9JTlZBTElEX0VTQ0FQRTtcclxuICAgIH1cclxuICAgIHJlYWRlckludmFsaWRCb29sKCkge1xyXG4gICAgICAgIHJldHVybiBCdWlsdEluRXhjZXB0aW9ucy5SRUFERVJfSU5WQUxJRF9CT09MO1xyXG4gICAgfVxyXG4gICAgcmVhZGVySW52YWxpZEludCgpIHtcclxuICAgICAgICByZXR1cm4gQnVpbHRJbkV4Y2VwdGlvbnMuUkVBREVSX0lOVkFMSURfSU5UO1xyXG4gICAgfVxyXG4gICAgcmVhZGVyRXhwZWN0ZWRJbnQoKSB7XHJcbiAgICAgICAgcmV0dXJuIEJ1aWx0SW5FeGNlcHRpb25zLlJFQURFUl9FWFBFQ1RFRF9JTlQ7XHJcbiAgICB9XHJcbiAgICByZWFkZXJJbnZhbGlkRmxvYXQoKSB7XHJcbiAgICAgICAgcmV0dXJuIEJ1aWx0SW5FeGNlcHRpb25zLlJFQURFUl9JTlZBTElEX0ZMT0FUO1xyXG4gICAgfVxyXG4gICAgcmVhZGVyRXhwZWN0ZWRGbG9hdCgpIHtcclxuICAgICAgICByZXR1cm4gQnVpbHRJbkV4Y2VwdGlvbnMuUkVBREVSX0VYUEVDVEVEX0ZMT0FUO1xyXG4gICAgfVxyXG4gICAgcmVhZGVyRXhwZWN0ZWRCb29sKCkge1xyXG4gICAgICAgIHJldHVybiBCdWlsdEluRXhjZXB0aW9ucy5SRUFERVJfRVhQRUNURURfQk9PTDtcclxuICAgIH1cclxuICAgIHJlYWRlckV4cGVjdGVkU3ltYm9sKCkge1xyXG4gICAgICAgIHJldHVybiBCdWlsdEluRXhjZXB0aW9ucy5SRUFERVJfRVhQRUNURURfU1lNQk9MO1xyXG4gICAgfVxyXG4gICAgZGlzcGF0Y2hlclVua25vd25Db21tYW5kKCkge1xyXG4gICAgICAgIHJldHVybiBCdWlsdEluRXhjZXB0aW9ucy5ESVNQQVRDSEVSX1VOS05PV05fQ09NTUFORDtcclxuICAgIH1cclxuICAgIGRpc3BhdGNoZXJVbmtub3duQXJndW1lbnQoKSB7XHJcbiAgICAgICAgcmV0dXJuIEJ1aWx0SW5FeGNlcHRpb25zLkRJU1BBVENIRVJfVU5LTk9XTl9BUkdVTUVOVDtcclxuICAgIH1cclxuICAgIGRpc3BhdGNoZXJFeHBlY3RlZEFyZ3VtZW50U2VwYXJhdG9yKCkge1xyXG4gICAgICAgIHJldHVybiBCdWlsdEluRXhjZXB0aW9ucy5ESVNQQVRDSEVSX0VYUEVDVEVEX0FSR1VNRU5UX1NFUEFSQVRPUjtcclxuICAgIH1cclxuICAgIGRpc3BhdGNoZXJQYXJzZUV4Y2VwdGlvbigpIHtcclxuICAgICAgICByZXR1cm4gQnVpbHRJbkV4Y2VwdGlvbnMuRElTUEFUQ0hFUl9QQVJTRV9FWENFUFRJT047XHJcbiAgICB9XHJcbn1cclxuQnVpbHRJbkV4Y2VwdGlvbnMuRkxPQVRfVE9PX1NNQUxMID0gbmV3IER5bmFtaWNDb21tYW5kRXhjZXB0aW9uVHlwZV8xLmRlZmF1bHQoKGZvdW5kLCBtaW4pID0+IG5ldyBMaXRlcmFsTWVzc2FnZV8xLmRlZmF1bHQoXCJGbG9hdCBtdXN0IG5vdCBiZSBsZXNzIHRoYW4gXCIgKyBtaW4gKyBcIiwgZm91bmQgXCIgKyBmb3VuZCkpO1xyXG5CdWlsdEluRXhjZXB0aW9ucy5GTE9BVF9UT09fQklHID0gbmV3IER5bmFtaWNDb21tYW5kRXhjZXB0aW9uVHlwZV8xLmRlZmF1bHQoKGZvdW5kLCBtYXgpID0+IG5ldyBMaXRlcmFsTWVzc2FnZV8xLmRlZmF1bHQoXCJGbG9hdCBtdXN0IG5vdCBiZSBtb3JlIHRoYW4gXCIgKyBtYXggKyBcIiwgZm91bmQgXCIgKyBmb3VuZCkpO1xyXG5CdWlsdEluRXhjZXB0aW9ucy5JTlRFR0VSX1RPT19TTUFMTCA9IG5ldyBEeW5hbWljQ29tbWFuZEV4Y2VwdGlvblR5cGVfMS5kZWZhdWx0KChmb3VuZCwgbWluKSA9PiBuZXcgTGl0ZXJhbE1lc3NhZ2VfMS5kZWZhdWx0KFwiSW50ZWdlciBtdXN0IG5vdCBiZSBsZXNzIHRoYW4gXCIgKyBtaW4gKyBcIiwgZm91bmQgXCIgKyBmb3VuZCkpO1xyXG5CdWlsdEluRXhjZXB0aW9ucy5JTlRFR0VSX1RPT19CSUcgPSBuZXcgRHluYW1pY0NvbW1hbmRFeGNlcHRpb25UeXBlXzEuZGVmYXVsdCgoZm91bmQsIG1heCkgPT4gbmV3IExpdGVyYWxNZXNzYWdlXzEuZGVmYXVsdChcIkludGVnZXIgbXVzdCBub3QgYmUgbW9yZSB0aGFuIFwiICsgbWF4ICsgXCIsIGZvdW5kIFwiICsgZm91bmQpKTtcclxuQnVpbHRJbkV4Y2VwdGlvbnMuTElURVJBTF9JTkNPUlJFQ1QgPSBuZXcgRHluYW1pY0NvbW1hbmRFeGNlcHRpb25UeXBlXzEuZGVmYXVsdChleHBlY3RlZCA9PiBuZXcgTGl0ZXJhbE1lc3NhZ2VfMS5kZWZhdWx0KFwiRXhwZWN0ZWQgbGl0ZXJhbCBcIiArIGV4cGVjdGVkKSk7XHJcbkJ1aWx0SW5FeGNlcHRpb25zLlJFQURFUl9FWFBFQ1RFRF9TVEFSVF9PRl9RVU9URSA9IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZV8xLmRlZmF1bHQobmV3IExpdGVyYWxNZXNzYWdlXzEuZGVmYXVsdChcIkV4cGVjdGVkIHF1b3RlIHRvIHN0YXJ0IGEgc3RyaW5nXCIpKTtcclxuQnVpbHRJbkV4Y2VwdGlvbnMuUkVBREVSX0VYUEVDVEVEX0VORF9PRl9RVU9URSA9IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZV8xLmRlZmF1bHQobmV3IExpdGVyYWxNZXNzYWdlXzEuZGVmYXVsdChcIlVuY2xvc2VkIHF1b3RlZCBzdHJpbmdcIikpO1xyXG5CdWlsdEluRXhjZXB0aW9ucy5SRUFERVJfSU5WQUxJRF9FU0NBUEUgPSBuZXcgRHluYW1pY0NvbW1hbmRFeGNlcHRpb25UeXBlXzEuZGVmYXVsdChjaGFyYWN0ZXIgPT4gbmV3IExpdGVyYWxNZXNzYWdlXzEuZGVmYXVsdChcIkludmFsaWQgZXNjYXBlIHNlcXVlbmNlICdcIiArIGNoYXJhY3RlciArIFwiJyBpbiBxdW90ZWQgc3RyaW5nXCIpKTtcclxuQnVpbHRJbkV4Y2VwdGlvbnMuUkVBREVSX0lOVkFMSURfQk9PTCA9IG5ldyBEeW5hbWljQ29tbWFuZEV4Y2VwdGlvblR5cGVfMS5kZWZhdWx0KHZhbHVlID0+IG5ldyBMaXRlcmFsTWVzc2FnZV8xLmRlZmF1bHQoXCJJbnZhbGlkIGJvb2wsIGV4cGVjdGVkIHRydWUgb3IgZmFsc2UgYnV0IGZvdW5kICdcIiArIHZhbHVlICsgXCInXCIpKTtcclxuQnVpbHRJbkV4Y2VwdGlvbnMuUkVBREVSX0lOVkFMSURfSU5UID0gbmV3IER5bmFtaWNDb21tYW5kRXhjZXB0aW9uVHlwZV8xLmRlZmF1bHQodmFsdWUgPT4gbmV3IExpdGVyYWxNZXNzYWdlXzEuZGVmYXVsdChcIkludmFsaWQgaW50ZWdlciAnXCIgKyB2YWx1ZSArIFwiJ1wiKSk7XHJcbkJ1aWx0SW5FeGNlcHRpb25zLlJFQURFUl9FWFBFQ1RFRF9JTlQgPSBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGVfMS5kZWZhdWx0KG5ldyBMaXRlcmFsTWVzc2FnZV8xLmRlZmF1bHQoXCJFeHBlY3RlZCBpbnRlZ2VyXCIpKTtcclxuQnVpbHRJbkV4Y2VwdGlvbnMuUkVBREVSX0lOVkFMSURfRkxPQVQgPSBuZXcgRHluYW1pY0NvbW1hbmRFeGNlcHRpb25UeXBlXzEuZGVmYXVsdCh2YWx1ZSA9PiBuZXcgTGl0ZXJhbE1lc3NhZ2VfMS5kZWZhdWx0KFwiSW52YWxpZCBmbG9hdCAnXCIgKyB2YWx1ZSArIFwiJ1wiKSk7XHJcbkJ1aWx0SW5FeGNlcHRpb25zLlJFQURFUl9FWFBFQ1RFRF9GTE9BVCA9IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZV8xLmRlZmF1bHQobmV3IExpdGVyYWxNZXNzYWdlXzEuZGVmYXVsdChcIkV4cGVjdGVkIGZsb2F0XCIpKTtcclxuQnVpbHRJbkV4Y2VwdGlvbnMuUkVBREVSX0VYUEVDVEVEX0JPT0wgPSBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGVfMS5kZWZhdWx0KG5ldyBMaXRlcmFsTWVzc2FnZV8xLmRlZmF1bHQoXCJFeHBlY3RlZCBib29sXCIpKTtcclxuQnVpbHRJbkV4Y2VwdGlvbnMuUkVBREVSX0VYUEVDVEVEX1NZTUJPTCA9IG5ldyBEeW5hbWljQ29tbWFuZEV4Y2VwdGlvblR5cGVfMS5kZWZhdWx0KHN5bWJvbCA9PiBuZXcgTGl0ZXJhbE1lc3NhZ2VfMS5kZWZhdWx0KFwiRXhwZWN0ZWQgJ1wiICsgc3ltYm9sICsgXCInXCIpKTtcclxuQnVpbHRJbkV4Y2VwdGlvbnMuRElTUEFUQ0hFUl9VTktOT1dOX0NPTU1BTkQgPSBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGVfMS5kZWZhdWx0KG5ldyBMaXRlcmFsTWVzc2FnZV8xLmRlZmF1bHQoXCJVbmtub3duIGNvbW1hbmRcIikpO1xyXG5CdWlsdEluRXhjZXB0aW9ucy5ESVNQQVRDSEVSX1VOS05PV05fQVJHVU1FTlQgPSBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGVfMS5kZWZhdWx0KG5ldyBMaXRlcmFsTWVzc2FnZV8xLmRlZmF1bHQoXCJJbmNvcnJlY3QgYXJndW1lbnQgZm9yIGNvbW1hbmRcIikpO1xyXG5CdWlsdEluRXhjZXB0aW9ucy5ESVNQQVRDSEVSX0VYUEVDVEVEX0FSR1VNRU5UX1NFUEFSQVRPUiA9IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZV8xLmRlZmF1bHQobmV3IExpdGVyYWxNZXNzYWdlXzEuZGVmYXVsdChcIkV4cGVjdGVkIHdoaXRlc3BhY2UgdG8gZW5kIG9uZSBhcmd1bWVudCwgYnV0IGZvdW5kIHRyYWlsaW5nIGRhdGFcIikpO1xyXG5CdWlsdEluRXhjZXB0aW9ucy5ESVNQQVRDSEVSX1BBUlNFX0VYQ0VQVElPTiA9IG5ldyBEeW5hbWljQ29tbWFuZEV4Y2VwdGlvblR5cGVfMS5kZWZhdWx0KG1lc3NhZ2UgPT4gbmV3IExpdGVyYWxNZXNzYWdlXzEuZGVmYXVsdCgoXCJDb3VsZCBub3QgcGFyc2UgY29tbWFuZDogXCIgKyBtZXNzYWdlKSkpO1xyXG5leHBvcnRzLmRlZmF1bHQgPSBCdWlsdEluRXhjZXB0aW9ucztcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbnZhciBfX2ltcG9ydERlZmF1bHQgPSAodGhpcyAmJiB0aGlzLl9faW1wb3J0RGVmYXVsdCkgfHwgZnVuY3Rpb24gKG1vZCkge1xyXG4gICAgcmV0dXJuIChtb2QgJiYgbW9kLl9fZXNNb2R1bGUpID8gbW9kIDogeyBcImRlZmF1bHRcIjogbW9kIH07XHJcbn07XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY29uc3QgQnVpbHRJbkV4Y2VwdGlvbnNfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9CdWlsdEluRXhjZXB0aW9uc1wiKSk7XHJcbmNsYXNzIENvbW1hbmRTeW50YXhFeGNlcHRpb24gZXh0ZW5kcyBFcnJvciB7XHJcbiAgICBjb25zdHJ1Y3Rvcih0eXBlLCBtZXNzYWdlLCBpbnB1dCA9IG51bGwsIGN1cnNvciA9IC0xKSB7XHJcbiAgICAgICAgc3VwZXIobWVzc2FnZS5nZXRTdHJpbmcoKSk7XHJcbiAgICAgICAgRXJyb3IuY2FwdHVyZVN0YWNrVHJhY2UodGhpcywgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbik7XHJcbiAgICAgICAgdGhpcy50eXBlID0gdHlwZTtcclxuICAgICAgICB0aGlzLl9fbWVzc2FnZSA9IG1lc3NhZ2U7XHJcbiAgICAgICAgdGhpcy5pbnB1dCA9IGlucHV0O1xyXG4gICAgICAgIHRoaXMuY3Vyc29yID0gY3Vyc29yO1xyXG4gICAgICAgIHRoaXMubWVzc2FnZSA9IHRoaXMuZ2V0TWVzc2FnZSgpO1xyXG4gICAgfVxyXG4gICAgZ2V0TWVzc2FnZSgpIHtcclxuICAgICAgICBsZXQgbWVzc2FnZSA9IHRoaXMuX19tZXNzYWdlLmdldFN0cmluZygpO1xyXG4gICAgICAgIGxldCBjb250ZXh0ID0gdGhpcy5nZXRDb250ZXh0KCk7XHJcbiAgICAgICAgaWYgKGNvbnRleHQgIT0gbnVsbCkge1xyXG4gICAgICAgICAgICBtZXNzYWdlICs9IFwiIGF0IHBvc2l0aW9uIFwiICsgdGhpcy5jdXJzb3IgKyBcIjogXCIgKyBjb250ZXh0O1xyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gbWVzc2FnZTtcclxuICAgIH1cclxuICAgIGdldFJhd01lc3NhZ2UoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuX19tZXNzYWdlO1xyXG4gICAgfVxyXG4gICAgZ2V0Q29udGV4dCgpIHtcclxuICAgICAgICBpZiAodGhpcy5pbnB1dCA9PSBudWxsIHx8IHRoaXMuY3Vyc29yIDwgMCkge1xyXG4gICAgICAgICAgICByZXR1cm4gbnVsbDtcclxuICAgICAgICB9XHJcbiAgICAgICAgbGV0IGJ1aWxkZXIgPSBcIlwiO1xyXG4gICAgICAgIGxldCBjdXJzb3IgPSBNYXRoLm1pbih0aGlzLmlucHV0Lmxlbmd0aCwgdGhpcy5jdXJzb3IpO1xyXG4gICAgICAgIGlmIChjdXJzb3IgPiBDb21tYW5kU3ludGF4RXhjZXB0aW9uLkNPTlRFWFRfQU1PVU5UKSB7XHJcbiAgICAgICAgICAgIGJ1aWxkZXIgKz0gXCIuLi5cIjtcclxuICAgICAgICB9XHJcbiAgICAgICAgYnVpbGRlciArPSB0aGlzLmlucHV0LnN1YnN0cmluZyhNYXRoLm1heCgwLCBjdXJzb3IgLSBDb21tYW5kU3ludGF4RXhjZXB0aW9uLkNPTlRFWFRfQU1PVU5UKSwgY3Vyc29yKTtcclxuICAgICAgICBidWlsZGVyICs9IFwiPC0tW0hFUkVdXCI7XHJcbiAgICAgICAgcmV0dXJuIGJ1aWxkZXI7XHJcbiAgICB9XHJcbiAgICBnZXRUeXBlKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnR5cGU7XHJcbiAgICB9XHJcbiAgICBnZXRJbnB1dCgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5pbnB1dDtcclxuICAgIH1cclxuICAgIGdldEN1cnNvcigpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5jdXJzb3I7XHJcbiAgICB9XHJcbiAgICB0b1N0cmluZygpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5tZXNzYWdlO1xyXG4gICAgfVxyXG59XHJcbkNvbW1hbmRTeW50YXhFeGNlcHRpb24uQ09OVEVYVF9BTU9VTlQgPSAxMDtcclxuQ29tbWFuZFN5bnRheEV4Y2VwdGlvbi5CVUlMVF9JTl9FWENFUFRJT05TID0gbmV3IEJ1aWx0SW5FeGNlcHRpb25zXzEuZGVmYXVsdCgpO1xyXG5leHBvcnRzLmRlZmF1bHQgPSBDb21tYW5kU3ludGF4RXhjZXB0aW9uO1xyXG4iLCJcInVzZSBzdHJpY3RcIjtcclxudmFyIF9faW1wb3J0RGVmYXVsdCA9ICh0aGlzICYmIHRoaXMuX19pbXBvcnREZWZhdWx0KSB8fCBmdW5jdGlvbiAobW9kKSB7XHJcbiAgICByZXR1cm4gKG1vZCAmJiBtb2QuX19lc01vZHVsZSkgPyBtb2QgOiB7IFwiZGVmYXVsdFwiOiBtb2QgfTtcclxufTtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5jb25zdCBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vQ29tbWFuZFN5bnRheEV4Y2VwdGlvblwiKSk7XHJcbmNsYXNzIER5bmFtaWNDb21tYW5kRXhjZXB0aW9uVHlwZSB7XHJcbiAgICBjb25zdHJ1Y3Rvcihmbikge1xyXG4gICAgICAgIHRoaXMuZm4gPSBmbjtcclxuICAgICAgICBFcnJvci5jYXB0dXJlU3RhY2tUcmFjZSh0aGlzLCBEeW5hbWljQ29tbWFuZEV4Y2VwdGlvblR5cGUpO1xyXG4gICAgfVxyXG4gICAgY3JlYXRlKC4uLmFyZ3MpIHtcclxuICAgICAgICByZXR1cm4gbmV3IENvbW1hbmRTeW50YXhFeGNlcHRpb25fMS5kZWZhdWx0KHRoaXMsIHRoaXMuZm4oLi4uYXJncykpO1xyXG4gICAgfVxyXG4gICAgY3JlYXRlV2l0aENvbnRleHQocmVhZGVyLCAuLi5hcmdzKSB7XHJcbiAgICAgICAgcmV0dXJuIG5ldyBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEuZGVmYXVsdCh0aGlzLCB0aGlzLmZuKC4uLmFyZ3MpLCByZWFkZXIuZ2V0U3RyaW5nKCksIHJlYWRlci5nZXRDdXJzb3IoKSk7XHJcbiAgICB9XHJcbn1cclxuZXhwb3J0cy5kZWZhdWx0ID0gRHluYW1pY0NvbW1hbmRFeGNlcHRpb25UeXBlO1xyXG4iLCJcInVzZSBzdHJpY3RcIjtcclxudmFyIF9faW1wb3J0RGVmYXVsdCA9ICh0aGlzICYmIHRoaXMuX19pbXBvcnREZWZhdWx0KSB8fCBmdW5jdGlvbiAobW9kKSB7XHJcbiAgICByZXR1cm4gKG1vZCAmJiBtb2QuX19lc01vZHVsZSkgPyBtb2QgOiB7IFwiZGVmYXVsdFwiOiBtb2QgfTtcclxufTtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5jb25zdCBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vQ29tbWFuZFN5bnRheEV4Y2VwdGlvblwiKSk7XHJcbmNsYXNzIFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlIHtcclxuICAgIGNvbnN0cnVjdG9yKG1lc3NhZ2UpIHtcclxuICAgICAgICB0aGlzLm1lc3NhZ2UgPSBtZXNzYWdlO1xyXG4gICAgICAgIEVycm9yLmNhcHR1cmVTdGFja1RyYWNlKHRoaXMsIFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKTtcclxuICAgIH1cclxuICAgIGNyZWF0ZSgpIHtcclxuICAgICAgICByZXR1cm4gbmV3IENvbW1hbmRTeW50YXhFeGNlcHRpb25fMS5kZWZhdWx0KHRoaXMsIHRoaXMubWVzc2FnZSk7XHJcbiAgICB9XHJcbiAgICBjcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpIHtcclxuICAgICAgICByZXR1cm4gbmV3IENvbW1hbmRTeW50YXhFeGNlcHRpb25fMS5kZWZhdWx0KHRoaXMsIHRoaXMubWVzc2FnZSwgcmVhZGVyLmdldFN0cmluZygpLCByZWFkZXIuZ2V0Q3Vyc29yKCkpO1xyXG4gICAgfVxyXG4gICAgdG9TdHJpbmcoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMubWVzc2FnZS5nZXRTdHJpbmcoKTtcclxuICAgIH1cclxufVxyXG5leHBvcnRzLmRlZmF1bHQgPSBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZTtcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbnZhciBfX2ltcG9ydERlZmF1bHQgPSAodGhpcyAmJiB0aGlzLl9faW1wb3J0RGVmYXVsdCkgfHwgZnVuY3Rpb24gKG1vZCkge1xyXG4gICAgcmV0dXJuIChtb2QgJiYgbW9kLl9fZXNNb2R1bGUpID8gbW9kIDogeyBcImRlZmF1bHRcIjogbW9kIH07XHJcbn07XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY29uc3QgU3VnZ2VzdGlvbl8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL1N1Z2dlc3Rpb25cIikpO1xyXG5jbGFzcyBJbnRlZ2VyU3VnZ2VzdGlvbiBleHRlbmRzIFN1Z2dlc3Rpb25fMS5kZWZhdWx0IHtcclxuICAgIGNvbnN0cnVjdG9yKHJhbmdlLCB2YWx1ZSwgdG9vbHRpcCA9IG51bGwpIHtcclxuICAgICAgICBzdXBlcihyYW5nZSwgdmFsdWUudG9TdHJpbmcoKSwgdG9vbHRpcCk7XHJcbiAgICAgICAgdGhpcy52YWx1ZSA9IHZhbHVlO1xyXG4gICAgfVxyXG4gICAgZ2V0VmFsdWUoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMudmFsdWU7XHJcbiAgICB9XHJcbiAgICBlcXVhbHMobykge1xyXG4gICAgICAgIGlmICh0aGlzID09PSBvKVxyXG4gICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICBpZiAoIShvIGluc3RhbmNlb2YgSW50ZWdlclN1Z2dlc3Rpb24pKVxyXG4gICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMudmFsdWUgPT0gby52YWx1ZSAmJiBzdXBlci5lcXVhbHMobyk7XHJcbiAgICB9XHJcbiAgICB0b1N0cmluZygpIHtcclxuICAgICAgICByZXR1cm4gXCJJbnRlZ2VyU3VnZ2VzdGlvbntcIiArXHJcbiAgICAgICAgICAgIFwidmFsdWU9XCIgKyB0aGlzLnZhbHVlICtcclxuICAgICAgICAgICAgXCIsIHJhbmdlPVwiICsgdGhpcy5nZXRSYW5nZSgpICtcclxuICAgICAgICAgICAgXCIsIHRleHQ9J1wiICsgdGhpcy5nZXRUZXh0KCkgKyAnXFwnJyArXHJcbiAgICAgICAgICAgIFwiLCB0b29sdGlwPSdcIiArIHRoaXMuZ2V0VG9vbHRpcCgpICsgJ1xcJycgK1xyXG4gICAgICAgICAgICAnfSc7XHJcbiAgICB9XHJcbiAgICBjb21wYXJlVG8obykge1xyXG4gICAgICAgIGlmIChvIGluc3RhbmNlb2YgSW50ZWdlclN1Z2dlc3Rpb24pIHtcclxuICAgICAgICAgICAgcmV0dXJuIHRoaXMudmFsdWUgPCBvLnZhbHVlID8gMSA6IC0xO1xyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gc3VwZXIuY29tcGFyZVRvKG8pO1xyXG4gICAgfVxyXG4gICAgY29tcGFyZVRvSWdub3JlQ2FzZShiKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuY29tcGFyZVRvKGIpO1xyXG4gICAgfVxyXG59XHJcbmV4cG9ydHMuZGVmYXVsdCA9IEludGVnZXJTdWdnZXN0aW9uO1xyXG4iLCJcInVzZSBzdHJpY3RcIjtcclxudmFyIF9faW1wb3J0RGVmYXVsdCA9ICh0aGlzICYmIHRoaXMuX19pbXBvcnREZWZhdWx0KSB8fCBmdW5jdGlvbiAobW9kKSB7XHJcbiAgICByZXR1cm4gKG1vZCAmJiBtb2QuX19lc01vZHVsZSkgPyBtb2QgOiB7IFwiZGVmYXVsdFwiOiBtb2QgfTtcclxufTtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5jb25zdCBpc0VxdWFsXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4uL3V0aWwvaXNFcXVhbFwiKSk7XHJcbmNsYXNzIFN1Z2dlc3Rpb24ge1xyXG4gICAgY29uc3RydWN0b3IocmFuZ2UsIHRleHQsIHRvb2x0aXAgPSBudWxsKSB7XHJcbiAgICAgICAgdGhpcy5yYW5nZSA9IHJhbmdlO1xyXG4gICAgICAgIHRoaXMudGV4dCA9IHRleHQ7XHJcbiAgICAgICAgdGhpcy50b29sdGlwID0gdG9vbHRpcDtcclxuICAgIH1cclxuICAgIGdldFJhbmdlKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnJhbmdlO1xyXG4gICAgfVxyXG4gICAgZ2V0VGV4dCgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy50ZXh0O1xyXG4gICAgfVxyXG4gICAgZ2V0VG9vbHRpcCgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy50b29sdGlwO1xyXG4gICAgfVxyXG4gICAgYXBwbHkoaW5wdXQpIHtcclxuICAgICAgICBpZiAodGhpcy5yYW5nZS5nZXRTdGFydCgpID09PSAwICYmIHRoaXMucmFuZ2UuZ2V0RW5kKCkgPT0gaW5wdXQubGVuZ3RoKSB7XHJcbiAgICAgICAgICAgIHJldHVybiB0aGlzLnRleHQ7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGxldCByZXN1bHQgPSBcIlwiO1xyXG4gICAgICAgIGlmICh0aGlzLnJhbmdlLmdldFN0YXJ0KCkgPiAwKSB7XHJcbiAgICAgICAgICAgIHJlc3VsdCArPSBpbnB1dC5zdWJzdHJpbmcoMCwgdGhpcy5yYW5nZS5nZXRTdGFydCgpKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgcmVzdWx0ICs9IHRoaXMudGV4dDtcclxuICAgICAgICBpZiAodGhpcy5yYW5nZS5nZXRFbmQoKSA8IGlucHV0Lmxlbmd0aCkge1xyXG4gICAgICAgICAgICByZXN1bHQgKz0gaW5wdXQuc3Vic3RyaW5nKHRoaXMucmFuZ2UuZ2V0RW5kKCkpO1xyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gcmVzdWx0O1xyXG4gICAgfVxyXG4gICAgZXF1YWxzKG8pIHtcclxuICAgICAgICBpZiAodGhpcyA9PT0gbylcclxuICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgaWYgKCEobyBpbnN0YW5jZW9mIFN1Z2dlc3Rpb24pKVxyXG4gICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICAgICAgcmV0dXJuIGlzRXF1YWxfMS5kZWZhdWx0KHRoaXMucmFuZ2UsIG8ucmFuZ2UpICYmICh0aGlzLnRleHQgPT09IG8udGV4dCkgJiYgaXNFcXVhbF8xLmRlZmF1bHQodGhpcy50b29sdGlwLCBvLnRvb2x0aXApO1xyXG4gICAgfVxyXG4gICAgdG9TdHJpbmcoKSB7XHJcbiAgICAgICAgcmV0dXJuIFwiU3VnZ2VzdGlvbntcIiArXHJcbiAgICAgICAgICAgIFwicmFuZ2U9XCIgKyB0aGlzLnJhbmdlICtcclxuICAgICAgICAgICAgXCIsIHRleHQ9J1wiICsgdGhpcy50ZXh0ICsgJ1xcJycgK1xyXG4gICAgICAgICAgICBcIiwgdG9vbHRpcD0nXCIgKyB0aGlzLnRvb2x0aXAgKyAnXFwnJyArXHJcbiAgICAgICAgICAgICd9JztcclxuICAgIH1cclxuICAgIGNvbXBhcmVUbyhvKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMudGV4dCA8IG8udGV4dCA/IDEgOiAtMTtcclxuICAgIH1cclxuICAgIGNvbXBhcmVUb0lnbm9yZUNhc2UoYikge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnRleHQudG9Mb3dlckNhc2UoKSA8IGIudGV4dC50b0xvd2VyQ2FzZSgpID8gMSA6IC0xO1xyXG4gICAgfVxyXG4gICAgZXhwYW5kKGNvbW1hbmQsIHJhbmdlKSB7XHJcbiAgICAgICAgaWYgKHJhbmdlLmVxdWFscyh0aGlzLnJhbmdlKSkge1xyXG4gICAgICAgICAgICByZXR1cm4gdGhpcztcclxuICAgICAgICB9XHJcbiAgICAgICAgbGV0IHJlc3VsdCA9IFwiXCI7XHJcbiAgICAgICAgaWYgKHJhbmdlLmdldFN0YXJ0KCkgPCB0aGlzLnJhbmdlLmdldFN0YXJ0KCkpIHtcclxuICAgICAgICAgICAgcmVzdWx0ICs9IGNvbW1hbmQuc3Vic3RyaW5nKHJhbmdlLmdldFN0YXJ0KCksIHRoaXMucmFuZ2UuZ2V0U3RhcnQoKSk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHJlc3VsdCArPSB0aGlzLnRleHQ7XHJcbiAgICAgICAgaWYgKHJhbmdlLmdldEVuZCgpID4gdGhpcy5yYW5nZS5nZXRFbmQoKSkge1xyXG4gICAgICAgICAgICByZXN1bHQgKz0gY29tbWFuZC5zdWJzdHJpbmcodGhpcy5yYW5nZS5nZXRFbmQoKSwgcmFuZ2UuZ2V0RW5kKCkpO1xyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gbmV3IFN1Z2dlc3Rpb24ocmFuZ2UsIHJlc3VsdCwgdGhpcy50b29sdGlwKTtcclxuICAgIH1cclxufVxyXG5leHBvcnRzLmRlZmF1bHQgPSBTdWdnZXN0aW9uO1xyXG4iLCJcInVzZSBzdHJpY3RcIjtcclxudmFyIF9faW1wb3J0RGVmYXVsdCA9ICh0aGlzICYmIHRoaXMuX19pbXBvcnREZWZhdWx0KSB8fCBmdW5jdGlvbiAobW9kKSB7XHJcbiAgICByZXR1cm4gKG1vZCAmJiBtb2QuX19lc01vZHVsZSkgPyBtb2QgOiB7IFwiZGVmYXVsdFwiOiBtb2QgfTtcclxufTtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5jb25zdCBpc0VxdWFsXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4uL3V0aWwvaXNFcXVhbFwiKSk7XHJcbmNvbnN0IFN0cmluZ1JhbmdlXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4uL2NvbnRleHQvU3RyaW5nUmFuZ2VcIikpO1xyXG5jbGFzcyBTdWdnZXN0aW9ucyB7XHJcbiAgICBjb25zdHJ1Y3RvcihyYW5nZSwgc3VnZ2VzdGlvbnMpIHtcclxuICAgICAgICB0aGlzLnJhbmdlID0gcmFuZ2U7XHJcbiAgICAgICAgdGhpcy5zdWdnZXN0aW9ucyA9IHN1Z2dlc3Rpb25zO1xyXG4gICAgfVxyXG4gICAgZ2V0UmFuZ2UoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMucmFuZ2U7XHJcbiAgICB9XHJcbiAgICBnZXRMaXN0KCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnN1Z2dlc3Rpb25zO1xyXG4gICAgfVxyXG4gICAgaXNFbXB0eSgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5zdWdnZXN0aW9ucy5sZW5ndGggPT09IDA7XHJcbiAgICB9XHJcbiAgICBlcXVhbHMobykge1xyXG4gICAgICAgIGlmICh0aGlzID09PSBvKVxyXG4gICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICBpZiAoIShvIGluc3RhbmNlb2YgU3VnZ2VzdGlvbnMpKVxyXG4gICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMucmFuZ2UuZXF1YWxzKG8ucmFuZ2UpICYmIGlzRXF1YWxfMS5kZWZhdWx0KHRoaXMuc3VnZ2VzdGlvbnMsIG8uc3VnZ2VzdGlvbnMpO1xyXG4gICAgfVxyXG4gICAgdG9TdHJpbmcoKSB7XHJcbiAgICAgICAgcmV0dXJuIFwiU3VnZ2VzdGlvbnN7XCIgK1xyXG4gICAgICAgICAgICBcInJhbmdlPVwiICsgdGhpcy5yYW5nZSArXHJcbiAgICAgICAgICAgIFwiLCBzdWdnZXN0aW9ucz1cIiArIHRoaXMuc3VnZ2VzdGlvbnMgKyAnfSc7XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgZW1wdHkoKSB7XHJcbiAgICAgICAgcmV0dXJuIFByb21pc2UucmVzb2x2ZSh0aGlzLkVNUFRZKTtcclxuICAgIH1cclxuICAgIHN0YXRpYyBtZXJnZShjb21tYW5kLCBpbnB1dCkge1xyXG4gICAgICAgIGlmIChpbnB1dC5sZW5ndGggPT09IDApIHtcclxuICAgICAgICAgICAgcmV0dXJuIHRoaXMuRU1QVFk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGVsc2UgaWYgKGlucHV0Lmxlbmd0aCA9PT0gMSkge1xyXG4gICAgICAgICAgICByZXR1cm4gaW5wdXRbMF07XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGNvbnN0IHRleHRzID0gW107XHJcbiAgICAgICAgZm9yIChsZXQgc3VnZ2VzdGlvbnMgb2YgaW5wdXQpIHtcclxuICAgICAgICAgICAgdGV4dHMucHVzaCguLi5zdWdnZXN0aW9ucy5nZXRMaXN0KCkpO1xyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gU3VnZ2VzdGlvbnMuY3JlYXRlKGNvbW1hbmQsIHRleHRzKTtcclxuICAgIH1cclxuICAgIHN0YXRpYyBjcmVhdGUoY29tbWFuZCwgc3VnZ2VzdGlvbnMpIHtcclxuICAgICAgICBpZiAoc3VnZ2VzdGlvbnMubGVuZ3RoID09PSAwKSB7XHJcbiAgICAgICAgICAgIHJldHVybiB0aGlzLkVNUFRZO1xyXG4gICAgICAgIH1cclxuICAgICAgICBsZXQgc3RhcnQgPSBJbmZpbml0eTtcclxuICAgICAgICBsZXQgZW5kID0gLUluZmluaXR5O1xyXG4gICAgICAgIGZvciAobGV0IHN1Z2dlc3Rpb24gb2Ygc3VnZ2VzdGlvbnMpIHtcclxuICAgICAgICAgICAgc3RhcnQgPSBNYXRoLm1pbihzdWdnZXN0aW9uLmdldFJhbmdlKCkuZ2V0U3RhcnQoKSwgc3RhcnQpO1xyXG4gICAgICAgICAgICBlbmQgPSBNYXRoLm1heChzdWdnZXN0aW9uLmdldFJhbmdlKCkuZ2V0RW5kKCksIGVuZCk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGxldCByYW5nZSA9IG5ldyBTdHJpbmdSYW5nZV8xLmRlZmF1bHQoc3RhcnQsIGVuZCk7XHJcbiAgICAgICAgY29uc3QgdGV4dHMgPSBbXTtcclxuICAgICAgICBmb3IgKGxldCBzdWdnZXN0aW9uIG9mIHN1Z2dlc3Rpb25zKSB7XHJcbiAgICAgICAgICAgIHRleHRzLnB1c2goc3VnZ2VzdGlvbi5leHBhbmQoY29tbWFuZCwgcmFuZ2UpKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgY29uc3Qgc29ydGVkID0gdGV4dHMuc29ydCgoYSwgYikgPT4gYS5jb21wYXJlVG9JZ25vcmVDYXNlKGIpKTtcclxuICAgICAgICByZXR1cm4gbmV3IFN1Z2dlc3Rpb25zKHJhbmdlLCBzb3J0ZWQpO1xyXG4gICAgfVxyXG59XHJcblN1Z2dlc3Rpb25zLkVNUFRZID0gbmV3IFN1Z2dlc3Rpb25zKFN0cmluZ1JhbmdlXzEuZGVmYXVsdC5hdCgwKSwgW10pO1xyXG5leHBvcnRzLmRlZmF1bHQgPSBTdWdnZXN0aW9ucztcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbnZhciBfX2ltcG9ydERlZmF1bHQgPSAodGhpcyAmJiB0aGlzLl9faW1wb3J0RGVmYXVsdCkgfHwgZnVuY3Rpb24gKG1vZCkge1xyXG4gICAgcmV0dXJuIChtb2QgJiYgbW9kLl9fZXNNb2R1bGUpID8gbW9kIDogeyBcImRlZmF1bHRcIjogbW9kIH07XHJcbn07XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY29uc3QgU3RyaW5nUmFuZ2VfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi4vY29udGV4dC9TdHJpbmdSYW5nZVwiKSk7XHJcbmNvbnN0IFN1Z2dlc3Rpb25fMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9TdWdnZXN0aW9uXCIpKTtcclxuY29uc3QgU3VnZ2VzdGlvbnNfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9TdWdnZXN0aW9uc1wiKSk7XHJcbmNvbnN0IEludGVnZXJTdWdnZXN0aW9uXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vSW50ZWdlclN1Z2dlc3Rpb25cIikpO1xyXG5jbGFzcyBTdWdnZXN0aW9uc0J1aWxkZXIge1xyXG4gICAgY29uc3RydWN0b3IoaW5wdXQsIHN0YXJ0KSB7XHJcbiAgICAgICAgdGhpcy5yZXN1bHQgPSBbXTtcclxuICAgICAgICB0aGlzLmlucHV0ID0gaW5wdXQ7XHJcbiAgICAgICAgdGhpcy5zdGFydCA9IHN0YXJ0O1xyXG4gICAgICAgIHRoaXMucmVtYWluaW5nID0gaW5wdXQuc3Vic3RyaW5nKHN0YXJ0KTtcclxuICAgIH1cclxuICAgIGdldElucHV0KCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmlucHV0O1xyXG4gICAgfVxyXG4gICAgZ2V0U3RhcnQoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuc3RhcnQ7XHJcbiAgICB9XHJcbiAgICBnZXRSZW1haW5pbmcoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMucmVtYWluaW5nO1xyXG4gICAgfVxyXG4gICAgYnVpbGQoKSB7XHJcbiAgICAgICAgcmV0dXJuIFN1Z2dlc3Rpb25zXzEuZGVmYXVsdC5jcmVhdGUodGhpcy5pbnB1dCwgdGhpcy5yZXN1bHQpO1xyXG4gICAgfVxyXG4gICAgYnVpbGRQcm9taXNlKCkge1xyXG4gICAgICAgIHJldHVybiBQcm9taXNlLnJlc29sdmUodGhpcy5idWlsZCgpKTtcclxuICAgIH1cclxuICAgIHN1Z2dlc3QodGV4dCwgdG9vbHRpcCA9IG51bGwpIHtcclxuICAgICAgICBpZiAodHlwZW9mIHRleHQgPT09IFwibnVtYmVyXCIpIHtcclxuICAgICAgICAgICAgdGhpcy5yZXN1bHQucHVzaChuZXcgSW50ZWdlclN1Z2dlc3Rpb25fMS5kZWZhdWx0KFN0cmluZ1JhbmdlXzEuZGVmYXVsdC5iZXR3ZWVuKHRoaXMuc3RhcnQsIHRoaXMuaW5wdXQubGVuZ3RoKSwgdGV4dCwgdG9vbHRpcCkpO1xyXG4gICAgICAgICAgICByZXR1cm4gdGhpcztcclxuICAgICAgICB9XHJcbiAgICAgICAgaWYgKHRleHQgPT09IHRoaXMucmVtYWluaW5nKVxyXG4gICAgICAgICAgICByZXR1cm4gdGhpcztcclxuICAgICAgICB0aGlzLnJlc3VsdC5wdXNoKG5ldyBTdWdnZXN0aW9uXzEuZGVmYXVsdChTdHJpbmdSYW5nZV8xLmRlZmF1bHQuYmV0d2Vlbih0aGlzLnN0YXJ0LCB0aGlzLmlucHV0Lmxlbmd0aCksIHRleHQsIHRvb2x0aXApKTtcclxuICAgICAgICByZXR1cm4gdGhpcztcclxuICAgIH1cclxuICAgIGFkZChvdGhlcikge1xyXG4gICAgICAgIHRoaXMucmVzdWx0LnB1c2goLi4ub3RoZXIucmVzdWx0KTtcclxuICAgICAgICByZXR1cm4gdGhpcztcclxuICAgIH1cclxuICAgIGNyZWF0ZU9mZnNldChzdGFydCkge1xyXG4gICAgICAgIHJldHVybiBuZXcgU3VnZ2VzdGlvbnNCdWlsZGVyKHRoaXMuaW5wdXQsIHRoaXMuc3RhcnQpO1xyXG4gICAgfVxyXG4gICAgcmVzdGFydCgpIHtcclxuICAgICAgICByZXR1cm4gbmV3IFN1Z2dlc3Rpb25zQnVpbGRlcih0aGlzLmlucHV0LCB0aGlzLnN0YXJ0KTtcclxuICAgIH1cclxufVxyXG5leHBvcnRzLmRlZmF1bHQgPSBTdWdnZXN0aW9uc0J1aWxkZXI7XHJcbiIsIlwidXNlIHN0cmljdFwiO1xyXG52YXIgX19pbXBvcnREZWZhdWx0ID0gKHRoaXMgJiYgdGhpcy5fX2ltcG9ydERlZmF1bHQpIHx8IGZ1bmN0aW9uIChtb2QpIHtcclxuICAgIHJldHVybiAobW9kICYmIG1vZC5fX2VzTW9kdWxlKSA/IG1vZCA6IHsgXCJkZWZhdWx0XCI6IG1vZCB9O1xyXG59O1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNvbnN0IGlzRXF1YWxfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi4vdXRpbC9pc0VxdWFsXCIpKTtcclxuY29uc3QgU3RyaW5nUmVhZGVyXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4uL1N0cmluZ1JlYWRlclwiKSk7XHJcbmNvbnN0IFJlcXVpcmVkQXJndW1lbnRCdWlsZGVyXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4uL2J1aWxkZXIvUmVxdWlyZWRBcmd1bWVudEJ1aWxkZXJcIikpO1xyXG5jb25zdCBQYXJzZWRBcmd1bWVudF8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuLi9jb250ZXh0L1BhcnNlZEFyZ3VtZW50XCIpKTtcclxuY29uc3QgU3VnZ2VzdGlvbnNfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi4vc3VnZ2VzdGlvbi9TdWdnZXN0aW9uc1wiKSk7XHJcbmNvbnN0IENvbW1hbmROb2RlXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vQ29tbWFuZE5vZGVcIikpO1xyXG5jb25zdCBVU0FHRV9BUkdVTUVOVF9PUEVOID0gXCI8XCI7XHJcbmNvbnN0IFVTQUdFX0FSR1VNRU5UX0NMT1NFID0gXCI+XCI7XHJcbmNsYXNzIEFyZ3VtZW50Q29tbWFuZE5vZGUgZXh0ZW5kcyBDb21tYW5kTm9kZV8xLmRlZmF1bHQge1xyXG4gICAgY29uc3RydWN0b3IobmFtZSwgdHlwZSwgY29tbWFuZCwgcmVxdWlyZW1lbnQsIHJlZGlyZWN0LCBtb2RpZmllciwgZm9ya3MsIGN1c3RvbVN1Z2dlc3Rpb25zKSB7XHJcbiAgICAgICAgc3VwZXIoY29tbWFuZCwgcmVxdWlyZW1lbnQsIHJlZGlyZWN0LCBtb2RpZmllciwgZm9ya3MpO1xyXG4gICAgICAgIHRoaXMubmFtZSA9IG5hbWU7XHJcbiAgICAgICAgdGhpcy50eXBlID0gdHlwZTtcclxuICAgICAgICB0aGlzLmN1c3RvbVN1Z2dlc3Rpb25zID0gY3VzdG9tU3VnZ2VzdGlvbnM7XHJcbiAgICB9XHJcbiAgICBnZXROb2RlVHlwZSgpIHtcclxuICAgICAgICByZXR1cm4gXCJhcmd1bWVudFwiO1xyXG4gICAgfVxyXG4gICAgZ2V0VHlwZSgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy50eXBlO1xyXG4gICAgfVxyXG4gICAgZ2V0TmFtZSgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5uYW1lO1xyXG4gICAgfVxyXG4gICAgZ2V0VXNhZ2VUZXh0KCkge1xyXG4gICAgICAgIHJldHVybiBVU0FHRV9BUkdVTUVOVF9PUEVOICsgdGhpcy5uYW1lICsgVVNBR0VfQVJHVU1FTlRfQ0xPU0U7XHJcbiAgICB9XHJcbiAgICBnZXRDdXN0b21TdWdnZXN0aW9ucygpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5jdXN0b21TdWdnZXN0aW9ucztcclxuICAgIH1cclxuICAgIHBhcnNlKHJlYWRlciwgY29udGV4dEJ1aWxkZXIpIHtcclxuICAgICAgICBsZXQgc3RhcnQgPSByZWFkZXIuZ2V0Q3Vyc29yKCk7XHJcbiAgICAgICAgbGV0IHJlc3VsdCA9IHRoaXMudHlwZS5wYXJzZShyZWFkZXIpO1xyXG4gICAgICAgIGxldCBwYXJzZWQgPSBuZXcgUGFyc2VkQXJndW1lbnRfMS5kZWZhdWx0KHN0YXJ0LCByZWFkZXIuZ2V0Q3Vyc29yKCksIHJlc3VsdCk7XHJcbiAgICAgICAgY29udGV4dEJ1aWxkZXIud2l0aEFyZ3VtZW50KHRoaXMubmFtZSwgcGFyc2VkKTtcclxuICAgICAgICBjb250ZXh0QnVpbGRlci53aXRoTm9kZSh0aGlzLCBwYXJzZWQuZ2V0UmFuZ2UoKSk7XHJcbiAgICB9XHJcbiAgICBsaXN0U3VnZ2VzdGlvbnMoY29udGV4dCwgYnVpbGRlcikge1xyXG4gICAgICAgIGlmICh0aGlzLmN1c3RvbVN1Z2dlc3Rpb25zID09IG51bGwpIHtcclxuICAgICAgICAgICAgaWYgKHR5cGVvZiB0aGlzLnR5cGUubGlzdFN1Z2dlc3Rpb25zID09PSBcImZ1bmN0aW9uXCIpXHJcbiAgICAgICAgICAgICAgICByZXR1cm4gdGhpcy50eXBlLmxpc3RTdWdnZXN0aW9ucyhjb250ZXh0LCBidWlsZGVyKTtcclxuICAgICAgICAgICAgZWxzZVxyXG4gICAgICAgICAgICAgICAgcmV0dXJuIFN1Z2dlc3Rpb25zXzEuZGVmYXVsdC5lbXB0eSgpO1xyXG4gICAgICAgIH1cclxuICAgICAgICBlbHNlIHtcclxuICAgICAgICAgICAgcmV0dXJuIHRoaXMuY3VzdG9tU3VnZ2VzdGlvbnMuZ2V0U3VnZ2VzdGlvbnMoY29udGV4dCwgYnVpbGRlcik7XHJcbiAgICAgICAgfVxyXG4gICAgfVxyXG4gICAgY3JlYXRlQnVpbGRlcigpIHtcclxuICAgICAgICBsZXQgYnVpbGRlciA9IFJlcXVpcmVkQXJndW1lbnRCdWlsZGVyXzEuZGVmYXVsdC5hcmd1bWVudCh0aGlzLm5hbWUsIHRoaXMudHlwZSk7XHJcbiAgICAgICAgYnVpbGRlci5yZXF1aXJlcyh0aGlzLmdldFJlcXVpcmVtZW50KCkpO1xyXG4gICAgICAgIGJ1aWxkZXIuZm9yd2FyZCh0aGlzLmdldFJlZGlyZWN0KCksIHRoaXMuZ2V0UmVkaXJlY3RNb2RpZmllcigpLCB0aGlzLmlzRm9yaygpKTtcclxuICAgICAgICBidWlsZGVyLnN1Z2dlc3RzKHRoaXMuY3VzdG9tU3VnZ2VzdGlvbnMpO1xyXG4gICAgICAgIGlmICh0aGlzLmdldENvbW1hbmQoKSAhPSBudWxsKSB7XHJcbiAgICAgICAgICAgIGJ1aWxkZXIuZXhlY3V0ZXModGhpcy5nZXRDb21tYW5kKCkpO1xyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gYnVpbGRlcjtcclxuICAgIH1cclxuICAgIGlzVmFsaWRJbnB1dChpbnB1dCkge1xyXG4gICAgICAgIHRyeSB7XHJcbiAgICAgICAgICAgIGxldCByZWFkZXIgPSBuZXcgU3RyaW5nUmVhZGVyXzEuZGVmYXVsdChpbnB1dCk7XHJcbiAgICAgICAgICAgIHRoaXMudHlwZS5wYXJzZShyZWFkZXIpO1xyXG4gICAgICAgICAgICByZXR1cm4gIXJlYWRlci5jYW5SZWFkKCkgfHwgcmVhZGVyLnBlZWsoKSA9PSAnICc7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGNhdGNoIChpZ25vcmVkKSB7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgIH1cclxuICAgIGVxdWFscyhvKSB7XHJcbiAgICAgICAgaWYgKHRoaXMgPT09IG8pXHJcbiAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgIGlmICghKG8gaW5zdGFuY2VvZiBBcmd1bWVudENvbW1hbmROb2RlKSlcclxuICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgIGlmICghKHRoaXMubmFtZSA9PT0gby5uYW1lKSlcclxuICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgIGlmICghaXNFcXVhbF8xLmRlZmF1bHQodGhpcy50eXBlLCBvLnR5cGUpKVxyXG4gICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICAgICAgcmV0dXJuIHN1cGVyLmVxdWFscyhvKTtcclxuICAgIH1cclxuICAgIGdldFNvcnRlZEtleSgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5uYW1lO1xyXG4gICAgfVxyXG4gICAgZ2V0RXhhbXBsZXMoKSB7XHJcbiAgICAgICAgcmV0dXJuIHR5cGVvZiB0aGlzLnR5cGUuZ2V0RXhhbXBsZXMgPT09IFwiZnVuY3Rpb25cIiA/IHRoaXMudHlwZS5nZXRFeGFtcGxlcygpIDogW107XHJcbiAgICB9XHJcbiAgICB0b1N0cmluZygpIHtcclxuICAgICAgICByZXR1cm4gXCI8YXJndW1lbnQgXCIgKyB0aGlzLm5hbWUgKyBcIjpcIiArIHRoaXMudHlwZSArIFwiPlwiO1xyXG4gICAgfVxyXG59XHJcbmV4cG9ydHMuZGVmYXVsdCA9IEFyZ3VtZW50Q29tbWFuZE5vZGU7XHJcbiIsIlwidXNlIHN0cmljdFwiO1xyXG52YXIgX19pbXBvcnREZWZhdWx0ID0gKHRoaXMgJiYgdGhpcy5fX2ltcG9ydERlZmF1bHQpIHx8IGZ1bmN0aW9uIChtb2QpIHtcclxuICAgIHJldHVybiAobW9kICYmIG1vZC5fX2VzTW9kdWxlKSA/IG1vZCA6IHsgXCJkZWZhdWx0XCI6IG1vZCB9O1xyXG59O1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNvbnN0IGlzRXF1YWxfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi4vdXRpbC9pc0VxdWFsXCIpKTtcclxuY2xhc3MgQ29tbWFuZE5vZGUge1xyXG4gICAgY29uc3RydWN0b3IoY29tbWFuZCwgcmVxdWlyZW1lbnQsIHJlZGlyZWN0LCBtb2RpZmllciwgZm9ya3MpIHtcclxuICAgICAgICB0aGlzLmNoaWxkcmVuID0gbmV3IE1hcCgpO1xyXG4gICAgICAgIHRoaXMubGl0ZXJhbHMgPSBuZXcgTWFwKCk7XHJcbiAgICAgICAgdGhpcy5hcmdzID0gbmV3IE1hcCgpO1xyXG4gICAgICAgIHRoaXMuY29tbWFuZCA9IGNvbW1hbmQ7XHJcbiAgICAgICAgdGhpcy5yZXF1aXJlbWVudCA9IHJlcXVpcmVtZW50IHx8ICgoKSA9PiB0cnVlKTtcclxuICAgICAgICB0aGlzLnJlZGlyZWN0ID0gcmVkaXJlY3Q7XHJcbiAgICAgICAgdGhpcy5tb2RpZmllciA9IG1vZGlmaWVyO1xyXG4gICAgICAgIHRoaXMuZm9ya3MgPSBmb3JrcztcclxuICAgIH1cclxuICAgIGdldENvbW1hbmQoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuY29tbWFuZDtcclxuICAgIH1cclxuICAgIGdldENoaWxkcmVuKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmNoaWxkcmVuLnZhbHVlcygpO1xyXG4gICAgfVxyXG4gICAgZ2V0Q2hpbGRyZW5Db3VudCgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5jaGlsZHJlbi5zaXplO1xyXG4gICAgfVxyXG4gICAgZ2V0Q2hpbGQobmFtZSkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmNoaWxkcmVuLmdldChuYW1lKTtcclxuICAgIH1cclxuICAgIGdldFJlZGlyZWN0KCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnJlZGlyZWN0O1xyXG4gICAgfVxyXG4gICAgZ2V0UmVkaXJlY3RNb2RpZmllcigpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5tb2RpZmllcjtcclxuICAgIH1cclxuICAgIGNhblVzZShzb3VyY2UpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5yZXF1aXJlbWVudChzb3VyY2UpO1xyXG4gICAgfVxyXG4gICAgYWRkQ2hpbGQobm9kZSkge1xyXG4gICAgICAgIGlmIChub2RlLmdldE5vZGVUeXBlKCkgPT09IFwicm9vdFwiKSB7XHJcbiAgICAgICAgICAgIHRocm93IG5ldyBFcnJvcihcIkNhbm5vdCBhZGQgYSBSb290Q29tbWFuZE5vZGUgYXMgYSBjaGlsZCB0byBhbnkgb3RoZXIgQ29tbWFuZE5vZGVcIik7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGxldCBjaGlsZCA9IHRoaXMuY2hpbGRyZW4uZ2V0KG5vZGUuZ2V0TmFtZSgpKTtcclxuICAgICAgICBpZiAoY2hpbGQgIT0gbnVsbCkge1xyXG4gICAgICAgICAgICAvLyAgV2UndmUgZm91bmQgc29tZXRoaW5nIHRvIG1lcmdlIG9udG9cclxuICAgICAgICAgICAgaWYgKChub2RlLmdldENvbW1hbmQoKSAhPSBudWxsKSkge1xyXG4gICAgICAgICAgICAgICAgY2hpbGQuY29tbWFuZCA9IG5vZGUuZ2V0Q29tbWFuZCgpO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIGZvciAobGV0IGdyYW5kY2hpbGQgb2Ygbm9kZS5nZXRDaGlsZHJlbigpKSB7XHJcbiAgICAgICAgICAgICAgICBjaGlsZC5hZGRDaGlsZChncmFuZGNoaWxkKTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgIH1cclxuICAgICAgICBlbHNlIHtcclxuICAgICAgICAgICAgdGhpcy5jaGlsZHJlbi5zZXQobm9kZS5nZXROYW1lKCksIG5vZGUpO1xyXG4gICAgICAgICAgICBpZiAobm9kZS5nZXROb2RlVHlwZSgpID09PSBcImxpdGVyYWxcIikge1xyXG4gICAgICAgICAgICAgICAgdGhpcy5saXRlcmFscy5zZXQobm9kZS5nZXROYW1lKCksIG5vZGUpO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIGVsc2UgaWYgKG5vZGUuZ2V0Tm9kZVR5cGUoKSA9PT0gXCJhcmd1bWVudFwiKSB7XHJcbiAgICAgICAgICAgICAgICB0aGlzLmFyZ3Muc2V0KG5vZGUuZ2V0TmFtZSgpLCBub2RlKTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgIH1cclxuICAgICAgICB0aGlzLmNoaWxkcmVuID0gbmV3IE1hcChbLi4udGhpcy5jaGlsZHJlbi5lbnRyaWVzKCldLnNvcnQoKGEsIGIpID0+IGFbMV0uY29tcGFyZVRvKGJbMV0pKSk7XHJcbiAgICB9XHJcbiAgICBmaW5kQW1iaWd1aXRpZXMoY29uc3VtZXIpIHtcclxuICAgICAgICBsZXQgbWF0Y2hlcyA9IFtdO1xyXG4gICAgICAgIGZvciAobGV0IGNoaWxkIG9mIHRoaXMuY2hpbGRyZW4udmFsdWVzKCkpIHtcclxuICAgICAgICAgICAgZm9yIChsZXQgc2libGluZyBvZiB0aGlzLmNoaWxkcmVuLnZhbHVlcygpKSB7XHJcbiAgICAgICAgICAgICAgICBpZiAoY2hpbGQgPT09IHNpYmxpbmcpIHtcclxuICAgICAgICAgICAgICAgICAgICBjb250aW51ZTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIGZvciAobGV0IGlucHV0IG9mIGNoaWxkLmdldEV4YW1wbGVzKCkpIHtcclxuICAgICAgICAgICAgICAgICAgICBpZiAoc2libGluZy5pc1ZhbGlkSW5wdXQoaW5wdXQpKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIG1hdGNoZXMucHVzaChpbnB1dCk7XHJcbiAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgaWYgKG1hdGNoZXMubGVuZ3RoID4gMCkge1xyXG4gICAgICAgICAgICAgICAgICAgIGNvbnN1bWVyLmFtYmlndW91cyh0aGlzLCBjaGlsZCwgc2libGluZywgbWF0Y2hlcyk7XHJcbiAgICAgICAgICAgICAgICAgICAgbWF0Y2hlcyA9IFtdO1xyXG4gICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIGNoaWxkLmZpbmRBbWJpZ3VpdGllcyhjb25zdW1lcik7XHJcbiAgICAgICAgfVxyXG4gICAgfVxyXG4gICAgZXF1YWxzKG8pIHtcclxuICAgICAgICBpZiAodGhpcyA9PT0gbylcclxuICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgaWYgKCEobyBpbnN0YW5jZW9mIENvbW1hbmROb2RlKSlcclxuICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgIGlmICh0aGlzLmNoaWxkcmVuLnNpemUgIT09IG8uY2hpbGRyZW4uc2l6ZSkge1xyXG4gICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGlmICghaXNFcXVhbF8xLmRlZmF1bHQodGhpcy5jaGlsZHJlbiwgby5jaGlsZHJlbikpXHJcbiAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICBpZiAodGhpcy5jb21tYW5kICE9IG51bGwgPyAhaXNFcXVhbF8xLmRlZmF1bHQodGhpcy5jb21tYW5kLCBvLmNvbW1hbmQpIDogby5jb21tYW5kICE9IG51bGwpXHJcbiAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgIH1cclxuICAgIGdldFJlcXVpcmVtZW50KCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnJlcXVpcmVtZW50O1xyXG4gICAgfVxyXG4gICAgZ2V0UmVsZXZhbnROb2RlcyhpbnB1dCkge1xyXG4gICAgICAgIGlmICh0aGlzLmxpdGVyYWxzLnNpemUgPiAwKSB7XHJcbiAgICAgICAgICAgIGxldCBjdXJzb3IgPSBpbnB1dC5nZXRDdXJzb3IoKTtcclxuICAgICAgICAgICAgd2hpbGUgKChpbnB1dC5jYW5SZWFkKClcclxuICAgICAgICAgICAgICAgICYmIChpbnB1dC5wZWVrKCkgIT0gJyAnKSkpIHtcclxuICAgICAgICAgICAgICAgIGlucHV0LnNraXAoKTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICBsZXQgdGV4dCA9IGlucHV0LmdldFN0cmluZygpLnN1YnN0cmluZyhjdXJzb3IsIGlucHV0LmdldEN1cnNvcigpKTtcclxuICAgICAgICAgICAgaW5wdXQuc2V0Q3Vyc29yKGN1cnNvcik7XHJcbiAgICAgICAgICAgIGxldCBsaXRlcmFsID0gdGhpcy5saXRlcmFscy5nZXQodGV4dCk7XHJcbiAgICAgICAgICAgIGlmIChsaXRlcmFsICE9IG51bGwpIHtcclxuICAgICAgICAgICAgICAgIHJldHVybiBbbGl0ZXJhbF07XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgZWxzZSB7XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gdGhpcy5hcmdzLnZhbHVlcygpO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGVsc2Uge1xyXG4gICAgICAgICAgICByZXR1cm4gdGhpcy5hcmdzLnZhbHVlcygpO1xyXG4gICAgICAgIH1cclxuICAgIH1cclxuICAgIGNvbXBhcmVUbyhvKSB7XHJcbiAgICAgICAgaWYgKHRoaXMuZ2V0Tm9kZVR5cGUoKSA9PT0gby5nZXROb2RlVHlwZSgpKSB7XHJcbiAgICAgICAgICAgIHJldHVybiB0aGlzLmdldFNvcnRlZEtleSgpID4gby5nZXRTb3J0ZWRLZXkoKSA/IDEgOiAtMTtcclxuICAgICAgICB9XHJcbiAgICAgICAgcmV0dXJuIChvLmdldE5vZGVUeXBlKCkgPT09IFwibGl0ZXJhbFwiKSA/IDEgOiAtMTtcclxuICAgIH1cclxuICAgIGlzRm9yaygpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5mb3JrcztcclxuICAgIH1cclxufVxyXG5leHBvcnRzLmRlZmF1bHQgPSBDb21tYW5kTm9kZTtcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbnZhciBfX2ltcG9ydERlZmF1bHQgPSAodGhpcyAmJiB0aGlzLl9faW1wb3J0RGVmYXVsdCkgfHwgZnVuY3Rpb24gKG1vZCkge1xyXG4gICAgcmV0dXJuIChtb2QgJiYgbW9kLl9fZXNNb2R1bGUpID8gbW9kIDogeyBcImRlZmF1bHRcIjogbW9kIH07XHJcbn07XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY29uc3QgQ29tbWFuZE5vZGVfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9Db21tYW5kTm9kZVwiKSk7XHJcbmNvbnN0IFN0cmluZ1JlYWRlcl8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuLi9TdHJpbmdSZWFkZXJcIikpO1xyXG5jb25zdCBMaXRlcmFsQXJndW1lbnRCdWlsZGVyXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4uL2J1aWxkZXIvTGl0ZXJhbEFyZ3VtZW50QnVpbGRlclwiKSk7XHJcbmNvbnN0IFN0cmluZ1JhbmdlXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4uL2NvbnRleHQvU3RyaW5nUmFuZ2VcIikpO1xyXG5jb25zdCBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4uL2V4Y2VwdGlvbnMvQ29tbWFuZFN5bnRheEV4Y2VwdGlvblwiKSk7XHJcbmNvbnN0IFN1Z2dlc3Rpb25zXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4uL3N1Z2dlc3Rpb24vU3VnZ2VzdGlvbnNcIikpO1xyXG5jbGFzcyBMaXRlcmFsQ29tbWFuZE5vZGUgZXh0ZW5kcyBDb21tYW5kTm9kZV8xLmRlZmF1bHQge1xyXG4gICAgY29uc3RydWN0b3IobGl0ZXJhbCwgY29tbWFuZCwgcmVxdWlyZW1lbnQsIHJlZGlyZWN0LCBtb2RpZmllciwgZm9ya3MpIHtcclxuICAgICAgICBzdXBlcihjb21tYW5kLCByZXF1aXJlbWVudCwgcmVkaXJlY3QsIG1vZGlmaWVyLCBmb3Jrcyk7XHJcbiAgICAgICAgdGhpcy5saXRlcmFsID0gbGl0ZXJhbDtcclxuICAgIH1cclxuICAgIGdldE5vZGVUeXBlKCkge1xyXG4gICAgICAgIHJldHVybiBcImxpdGVyYWxcIjtcclxuICAgIH1cclxuICAgIGdldExpdGVyYWwoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMubGl0ZXJhbDtcclxuICAgIH1cclxuICAgIGdldE5hbWUoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMubGl0ZXJhbDtcclxuICAgIH1cclxuICAgIHBhcnNlKHJlYWRlciwgY29udGV4dEJ1aWxkZXIpIHtcclxuICAgICAgICBsZXQgc3RhcnQgPSByZWFkZXIuZ2V0Q3Vyc29yKCk7XHJcbiAgICAgICAgbGV0IGVuZCA9IHRoaXMuX19wYXJzZShyZWFkZXIpO1xyXG4gICAgICAgIGlmIChlbmQgPiAtMSkge1xyXG4gICAgICAgICAgICBjb250ZXh0QnVpbGRlci53aXRoTm9kZSh0aGlzLCBTdHJpbmdSYW5nZV8xLmRlZmF1bHQuYmV0d2VlbihzdGFydCwgZW5kKSk7XHJcbiAgICAgICAgICAgIHJldHVybjtcclxuICAgICAgICB9XHJcbiAgICAgICAgdGhyb3cgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbl8xLmRlZmF1bHQuQlVJTFRfSU5fRVhDRVBUSU9OUy5saXRlcmFsSW5jb3JyZWN0KCkuY3JlYXRlV2l0aENvbnRleHQocmVhZGVyLCB0aGlzLmxpdGVyYWwpO1xyXG4gICAgfVxyXG4gICAgX19wYXJzZShyZWFkZXIpIHtcclxuICAgICAgICBsZXQgc3RhcnQgPSByZWFkZXIuZ2V0Q3Vyc29yKCk7XHJcbiAgICAgICAgaWYgKHJlYWRlci5jYW5SZWFkKHRoaXMubGl0ZXJhbC5sZW5ndGgpKSB7XHJcbiAgICAgICAgICAgIGxldCBlbmQgPSBzdGFydCArIHRoaXMubGl0ZXJhbC5sZW5ndGg7XHJcbiAgICAgICAgICAgIGlmIChyZWFkZXIuZ2V0U3RyaW5nKCkuc3Vic3RyaW5nKHN0YXJ0LCBlbmQpID09PSB0aGlzLmxpdGVyYWwpIHtcclxuICAgICAgICAgICAgICAgIHJlYWRlci5zZXRDdXJzb3IoZW5kKTtcclxuICAgICAgICAgICAgICAgIGlmICghcmVhZGVyLmNhblJlYWQoKSB8fCByZWFkZXIucGVlaygpID09ICcgJykge1xyXG4gICAgICAgICAgICAgICAgICAgIHJldHVybiBlbmQ7XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICBlbHNlIHtcclxuICAgICAgICAgICAgICAgICAgICByZWFkZXIuc2V0Q3Vyc29yKHN0YXJ0KTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgfVxyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gLTE7XHJcbiAgICB9XHJcbiAgICBsaXN0U3VnZ2VzdGlvbnMoY29udGV4dCwgYnVpbGRlcikge1xyXG4gICAgICAgIGlmICh0aGlzLmxpdGVyYWwudG9Mb3dlckNhc2UoKS5zdGFydHNXaXRoKGJ1aWxkZXIuZ2V0UmVtYWluaW5nKCkudG9Mb3dlckNhc2UoKSkpIHtcclxuICAgICAgICAgICAgcmV0dXJuIGJ1aWxkZXIuc3VnZ2VzdCh0aGlzLmxpdGVyYWwpLmJ1aWxkUHJvbWlzZSgpO1xyXG4gICAgICAgIH1cclxuICAgICAgICBlbHNlIHtcclxuICAgICAgICAgICAgcmV0dXJuIFN1Z2dlc3Rpb25zXzEuZGVmYXVsdC5lbXB0eSgpO1xyXG4gICAgICAgIH1cclxuICAgIH1cclxuICAgIGlzVmFsaWRJbnB1dChpbnB1dCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLl9fcGFyc2UobmV3IFN0cmluZ1JlYWRlcl8xLmRlZmF1bHQoaW5wdXQpKSA+IC0xO1xyXG4gICAgfVxyXG4gICAgZXF1YWxzKG8pIHtcclxuICAgICAgICBpZiAodGhpcyA9PT0gbylcclxuICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgaWYgKCEobyBpbnN0YW5jZW9mIExpdGVyYWxDb21tYW5kTm9kZSkpXHJcbiAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICBpZiAoISh0aGlzLmxpdGVyYWwgPT09IG8ubGl0ZXJhbCkpXHJcbiAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICByZXR1cm4gc3VwZXIuZXF1YWxzKG8pO1xyXG4gICAgfVxyXG4gICAgZ2V0VXNhZ2VUZXh0KCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmxpdGVyYWw7XHJcbiAgICB9XHJcbiAgICBjcmVhdGVCdWlsZGVyKCkge1xyXG4gICAgICAgIGxldCBidWlsZGVyID0gTGl0ZXJhbEFyZ3VtZW50QnVpbGRlcl8xLmRlZmF1bHQubGl0ZXJhbCh0aGlzLmxpdGVyYWwpO1xyXG4gICAgICAgIGJ1aWxkZXIucmVxdWlyZXModGhpcy5nZXRSZXF1aXJlbWVudCgpKTtcclxuICAgICAgICBidWlsZGVyLmZvcndhcmQodGhpcy5nZXRSZWRpcmVjdCgpLCB0aGlzLmdldFJlZGlyZWN0TW9kaWZpZXIoKSwgdGhpcy5pc0ZvcmsoKSk7XHJcbiAgICAgICAgaWYgKHRoaXMuZ2V0Q29tbWFuZCgpICE9IG51bGwpXHJcbiAgICAgICAgICAgIGJ1aWxkZXIuZXhlY3V0ZXModGhpcy5nZXRDb21tYW5kKCkpO1xyXG4gICAgICAgIHJldHVybiBidWlsZGVyO1xyXG4gICAgfVxyXG4gICAgZ2V0U29ydGVkS2V5KCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmxpdGVyYWw7XHJcbiAgICB9XHJcbiAgICBnZXRFeGFtcGxlcygpIHtcclxuICAgICAgICByZXR1cm4gW3RoaXMubGl0ZXJhbF07XHJcbiAgICB9XHJcbiAgICB0b1N0cmluZygpIHtcclxuICAgICAgICByZXR1cm4gXCI8bGl0ZXJhbCBcIiArIHRoaXMubGl0ZXJhbCArIFwiPlwiO1xyXG4gICAgfVxyXG59XHJcbmV4cG9ydHMuZGVmYXVsdCA9IExpdGVyYWxDb21tYW5kTm9kZTtcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbnZhciBfX2ltcG9ydERlZmF1bHQgPSAodGhpcyAmJiB0aGlzLl9faW1wb3J0RGVmYXVsdCkgfHwgZnVuY3Rpb24gKG1vZCkge1xyXG4gICAgcmV0dXJuIChtb2QgJiYgbW9kLl9fZXNNb2R1bGUpID8gbW9kIDogeyBcImRlZmF1bHRcIjogbW9kIH07XHJcbn07XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY29uc3QgQ29tbWFuZE5vZGVfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9Db21tYW5kTm9kZVwiKSk7XHJcbmNvbnN0IFN1Z2dlc3Rpb25zXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4uL3N1Z2dlc3Rpb24vU3VnZ2VzdGlvbnNcIikpO1xyXG5jbGFzcyBSb290Q29tbWFuZE5vZGUgZXh0ZW5kcyBDb21tYW5kTm9kZV8xLmRlZmF1bHQge1xyXG4gICAgY29uc3RydWN0b3IoKSB7XHJcbiAgICAgICAgc3VwZXIobnVsbCwgcyA9PiB0cnVlLCBudWxsLCAocykgPT4gcy5nZXRTb3VyY2UoKSwgZmFsc2UpO1xyXG4gICAgfVxyXG4gICAgZ2V0Tm9kZVR5cGUoKSB7XHJcbiAgICAgICAgcmV0dXJuIFwicm9vdFwiO1xyXG4gICAgfVxyXG4gICAgZ2V0TmFtZSgpIHtcclxuICAgICAgICByZXR1cm4gXCJcIjtcclxuICAgIH1cclxuICAgIGdldFVzYWdlVGV4dCgpIHtcclxuICAgICAgICByZXR1cm4gXCJcIjtcclxuICAgIH1cclxuICAgIHBhcnNlKHJlYWRlciwgY29udGV4dEJ1aWxkZXIpIHtcclxuICAgIH1cclxuICAgIGxpc3RTdWdnZXN0aW9ucyhjb250ZXh0LCBidWlsZGVyKSB7XHJcbiAgICAgICAgcmV0dXJuIFN1Z2dlc3Rpb25zXzEuZGVmYXVsdC5lbXB0eSgpO1xyXG4gICAgfVxyXG4gICAgaXNWYWxpZElucHV0KGlucHV0KSB7XHJcbiAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgfVxyXG4gICAgZXF1YWxzKG8pIHtcclxuICAgICAgICBpZiAodGhpcyA9PT0gbylcclxuICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgaWYgKCEobyBpbnN0YW5jZW9mIFJvb3RDb21tYW5kTm9kZSkpXHJcbiAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICByZXR1cm4gc3VwZXIuZXF1YWxzKG8pO1xyXG4gICAgfVxyXG4gICAgY3JlYXRlQnVpbGRlcigpIHtcclxuICAgICAgICB0aHJvdyBuZXcgRXJyb3IoXCJDYW5ub3QgY29udmVydCByb290IGludG8gYSBidWlsZGVyXCIpO1xyXG4gICAgfVxyXG4gICAgZ2V0U29ydGVkS2V5KCkge1xyXG4gICAgICAgIHJldHVybiBcIlwiO1xyXG4gICAgfVxyXG4gICAgZ2V0RXhhbXBsZXMoKSB7XHJcbiAgICAgICAgcmV0dXJuIFtdO1xyXG4gICAgfVxyXG4gICAgdG9TdHJpbmcoKSB7XHJcbiAgICAgICAgcmV0dXJuIFwiPHJvb3Q+XCI7XHJcbiAgICB9XHJcbn1cclxuZXhwb3J0cy5kZWZhdWx0ID0gUm9vdENvbW1hbmROb2RlO1xyXG4iLCJcInVzZSBzdHJpY3RcIjtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5mdW5jdGlvbiBpc0VxdWFsKGEsIGIpIHtcclxuICAgIGlmIChhID09PSBiKVxyXG4gICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgaWYgKHR5cGVvZiBhICE9IHR5cGVvZiBiKVxyXG4gICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgIGlmICghKGEgaW5zdGFuY2VvZiBPYmplY3QpKVxyXG4gICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgIGlmICh0eXBlb2YgYSA9PT0gXCJmdW5jdGlvblwiKVxyXG4gICAgICAgIHJldHVybiBhLnRvU3RyaW5nKCkgPT09IGIudG9TdHJpbmcoKTtcclxuICAgIGlmIChhLmNvbnN0cnVjdG9yICE9PSBiLmNvbnN0cnVjdG9yKVxyXG4gICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgIGlmIChhIGluc3RhbmNlb2YgTWFwKVxyXG4gICAgICAgIHJldHVybiBpc01hcEVxdWFsKGEsIGIpO1xyXG4gICAgaWYgKGEgaW5zdGFuY2VvZiBTZXQpXHJcbiAgICAgICAgcmV0dXJuIGlzQXJyYXlFcXVhbChbLi4uYV0sIFsuLi5iXSk7XHJcbiAgICBpZiAoYSBpbnN0YW5jZW9mIEFycmF5KVxyXG4gICAgICAgIHJldHVybiBpc0FycmF5RXF1YWwoYSwgYik7XHJcbiAgICBpZiAodHlwZW9mIGEgPT09IFwib2JqZWN0XCIpXHJcbiAgICAgICAgcmV0dXJuIGlzT2JqZWN0RXF1YWwoYSwgYik7XHJcbiAgICByZXR1cm4gZmFsc2U7XHJcbn1cclxuZXhwb3J0cy5kZWZhdWx0ID0gaXNFcXVhbDtcclxuZnVuY3Rpb24gaXNNYXBFcXVhbChhLCBiKSB7XHJcbiAgICBpZiAoYS5zaXplICE9IGIuc2l6ZSlcclxuICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICBmb3IgKGxldCBba2V5LCB2YWxdIG9mIGEpIHtcclxuICAgICAgICBjb25zdCB0ZXN0VmFsID0gYi5nZXQoa2V5KTtcclxuICAgICAgICBpZiAoIWlzRXF1YWwodGVzdFZhbCwgdmFsKSlcclxuICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgIGlmICh0ZXN0VmFsID09PSB1bmRlZmluZWQgJiYgIWIuaGFzKGtleSkpXHJcbiAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgIH1cclxuICAgIHJldHVybiB0cnVlO1xyXG59XHJcbmZ1bmN0aW9uIGlzQXJyYXlFcXVhbChhLCBiKSB7XHJcbiAgICBpZiAoYS5sZW5ndGggIT0gYi5sZW5ndGgpXHJcbiAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgZm9yIChsZXQgaSA9IDA7IGkgPCBhLmxlbmd0aDsgaSsrKVxyXG4gICAgICAgIGlmICghaXNFcXVhbChhW2ldLCBiW2ldKSlcclxuICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgcmV0dXJuIHRydWU7XHJcbn1cclxuZnVuY3Rpb24gaXNPYmplY3RFcXVhbChhLCBiKSB7XHJcbiAgICBjb25zdCBhS2V5cyA9IE9iamVjdC5rZXlzKGEpO1xyXG4gICAgY29uc3QgYktleXMgPSBPYmplY3Qua2V5cyhiKTtcclxuICAgIGlmIChhS2V5cy5sZW5ndGggIT0gYktleXMubGVuZ3RoKVxyXG4gICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgIGlmICghYUtleXMuZXZlcnkoa2V5ID0+IGIuaGFzT3duUHJvcGVydHkoa2V5KSkpXHJcbiAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgcmV0dXJuIGFLZXlzLmV2ZXJ5KChrZXkpID0+IHtcclxuICAgICAgICByZXR1cm4gaXNFcXVhbChhW2tleV0sIGJba2V5XSk7XHJcbiAgICB9KTtcclxufVxyXG47XHJcbiIsImltcG9ydCB7XHJcblx0U2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGUsXHJcblx0TGl0ZXJhbE1lc3NhZ2UsXHJcblx0U3VnZ2VzdGlvbnMsXHJcblx0U3RyaW5nUmVhZGVyLFxyXG5cdENvbW1hbmRTeW50YXhFeGNlcHRpb24sXHJcblx0Q29tbWFuZENvbnRleHQsXHJcblx0U3VnZ2VzdGlvbnNCdWlsZGVyLFxyXG5cclxuXHQvLyBUeXBpbmdcclxuXHRBcmd1bWVudFR5cGVcclxufSBmcm9tIFwibm9kZS1icmlnYWRpZXJcIlxyXG5cclxuY2xhc3MgRXh0ZW5kZWRTdHJpbmdSZWFkZXIge1xyXG5cclxuXHRwdWJsaWMgc3RhdGljIHJlYWRMb2NhdGlvbkxpdGVyYWwocmVhZGVyOiBTdHJpbmdSZWFkZXIpOiBudW1iZXIge1xyXG5cclxuXHRcdGZ1bmN0aW9uIGlzQWxsb3dlZExvY2F0aW9uTGl0ZXJhbChjOiBzdHJpbmcpOiBib29sZWFuIHtcclxuXHRcdFx0cmV0dXJuIGMgPT09ICd+JyB8fCBjID09PSAnXic7XHJcblx0XHR9XHJcblxyXG5cdFx0bGV0IHN0YXJ0ID0gcmVhZGVyLmdldEN1cnNvcigpO1xyXG5cdFx0d2hpbGUgKHJlYWRlci5jYW5SZWFkKCkgJiYgKFN0cmluZ1JlYWRlci5pc0FsbG93ZWROdW1iZXIocmVhZGVyLnBlZWsoKSkgfHwgaXNBbGxvd2VkTG9jYXRpb25MaXRlcmFsKHJlYWRlci5wZWVrKCkpKSkge1xyXG5cdFx0XHRyZWFkZXIuc2tpcCgpO1xyXG5cdFx0fVxyXG5cdFx0bGV0IG51bWJlciA9IHJlYWRlci5nZXRTdHJpbmcoKS5zdWJzdHJpbmcoc3RhcnQsIHJlYWRlci5nZXRDdXJzb3IoKSk7XHJcblx0XHRpZiAobnVtYmVyLmxlbmd0aCA9PT0gMCkge1xyXG5cdFx0XHR0aHJvdyAoQ29tbWFuZFN5bnRheEV4Y2VwdGlvbiBhcyBhbnkpLkJVSUxUX0lOX0VYQ0VQVElPTlMucmVhZGVyRXhwZWN0ZWRJbnQoKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdFx0fVxyXG5cclxuXHRcdGlmIChudW1iZXIuc3RhcnRzV2l0aChcIn5cIikgfHwgbnVtYmVyLnN0YXJ0c1dpdGgoXCJeXCIpKSB7XHJcblx0XHRcdGlmIChudW1iZXIubGVuZ3RoID09PSAxKSB7XHJcblx0XHRcdFx0Ly8gQWNjZXB0LlxyXG5cdFx0XHRcdHJldHVybiAwO1xyXG5cdFx0XHR9IGVsc2Uge1xyXG5cdFx0XHRcdG51bWJlciA9IG51bWJlci5zbGljZSgxKTtcclxuXHRcdFx0fVxyXG5cdFx0fVxyXG5cdFx0Y29uc3QgcmVzdWx0ID0gcGFyc2VJbnQobnVtYmVyKTtcclxuXHRcdGlmIChpc05hTihyZXN1bHQpIHx8IHJlc3VsdCAhPT0gcGFyc2VGbG9hdChudW1iZXIpKSB7XHJcblx0XHRcdHJlYWRlci5zZXRDdXJzb3Ioc3RhcnQpO1xyXG5cdFx0XHR0aHJvdyAoQ29tbWFuZFN5bnRheEV4Y2VwdGlvbiBhcyBhbnkpLkJVSUxUX0lOX0VYQ0VQVElPTlMucmVhZGVySW52YWxpZEludCgpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlciwgbnVtYmVyKTtcclxuXHRcdH0gZWxzZSB7XHJcblx0XHRcdHJldHVybiByZXN1bHQ7XHJcblx0XHR9XHJcblx0fVxyXG5cclxuXHRwdWJsaWMgc3RhdGljIHJlYWRSZXNvdXJjZUxvY2F0aW9uKHJlYWRlcjogU3RyaW5nUmVhZGVyKTogc3RyaW5nIHtcclxuXHJcblx0XHRmdW5jdGlvbiBpc0FsbG93ZWRJblJlc291cmNlTG9jYXRpb24oYzogc3RyaW5nKTogYm9vbGVhbiB7XHJcblx0XHRcdHJldHVybiAoKGMgPj0gJzAnICYmIGMgPD0gJzknKSB8fCAoYyA+PSAnYScgJiYgYyA8PSAneicpIHx8IGMgPT09ICdfJyB8fCBjID09PSAnOicgfHwgYyA9PT0gJy8nIHx8IGMgPT09ICcuJyB8fCBjID09PSAnLScpO1xyXG5cdFx0fVxyXG5cclxuXHRcdGZ1bmN0aW9uIHZhbGlkUGF0aENoYXIoYzogc3RyaW5nKTogYm9vbGVhbiB7XHJcblx0XHRcdHJldHVybiAoYyA9PT0gJ18nIHx8IGMgPT09ICctJyB8fCAoYyA+PSAnYScgJiYgYyA8PSAneicpIHx8IChjID49ICcwJyAmJiBjIDw9ICc5JykgfHwgYyA9PT0gJy8nIHx8IGMgPT09ICcuJyk7XHJcblx0XHR9XHJcblxyXG5cdFx0ZnVuY3Rpb24gdmFsaWROYW1lc3BhY2VDaGFyKGM6IHN0cmluZyk6IGJvb2xlYW4ge1xyXG5cdFx0XHRyZXR1cm4gKGMgPT09ICdfJyB8fCBjID09PSAnLScgfHwgKGMgPj0gJ2EnICYmIGMgPD0gJ3onKSB8fCAoYyA+PSAnMCcgJiYgYyA8PSAnOScpIHx8IGMgPT09ICcuJyk7XHJcblx0XHR9XHJcblxyXG5cdFx0ZnVuY3Rpb24gaXNWYWxpZChzdHJpbmc6IHN0cmluZywgcHJlZGljYXRlOiAoYzogc3RyaW5nKSA9PiBib29sZWFuKTogYm9vbGVhbiB7XHJcblx0XHRcdGZvciAobGV0IGk6IG51bWJlciA9IDA7IGkgPCBzdHJpbmcubGVuZ3RoOyBpKyspIHtcclxuXHRcdFx0XHRpZiAoIXByZWRpY2F0ZShzdHJpbmcuY2hhckF0KGkpKSkge1xyXG5cdFx0XHRcdFx0cmV0dXJuIGZhbHNlO1xyXG5cdFx0XHRcdH1cclxuXHRcdFx0fVxyXG5cdFx0XHRyZXR1cm4gdHJ1ZTtcclxuXHRcdH1cclxuXHJcblx0XHRjb25zdCBzdGFydCA9IHJlYWRlci5nZXRDdXJzb3IoKTtcclxuXHRcdHdoaWxlIChyZWFkZXIuY2FuUmVhZCgpICYmIGlzQWxsb3dlZEluUmVzb3VyY2VMb2NhdGlvbihyZWFkZXIucGVlaygpKSkge1xyXG5cdFx0XHRyZWFkZXIuc2tpcCgpO1xyXG5cdFx0fVxyXG5cclxuXHRcdGxldCByZXNvdXJjZUxvY2F0aW9uOiBzdHJpbmcgPSByZWFkZXIuZ2V0U3RyaW5nKCkuc3Vic3RyaW5nKHN0YXJ0LCByZWFkZXIuZ2V0Q3Vyc29yKCkpO1xyXG5cclxuXHRcdGNvbnN0IHJlc291cmNlTG9jYXRpb25QYXJ0czogc3RyaW5nW10gPSByZXNvdXJjZUxvY2F0aW9uLnNwbGl0KFwiOlwiKTtcclxuXHRcdHN3aXRjaCAocmVzb3VyY2VMb2NhdGlvblBhcnRzLmxlbmd0aCkge1xyXG5cdFx0XHRjYXNlIDA6XHJcblx0XHRcdFx0dGhyb3cgbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKG5ldyBMaXRlcmFsTWVzc2FnZShyZXNvdXJjZUxvY2F0aW9uICsgXCIgaXMgbm90IGEgdmFsaWQgUmVzb3VyY2VcIikpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlcik7XHJcblx0XHRcdGNhc2UgMTpcclxuXHRcdFx0XHQvLyBDaGVjayBwYXRoXHJcblx0XHRcdFx0aWYgKCFpc1ZhbGlkKHJlc291cmNlTG9jYXRpb25QYXJ0c1swXSwgdmFsaWRQYXRoQ2hhcikpIHtcclxuXHRcdFx0XHRcdHRocm93IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZShuZXcgTGl0ZXJhbE1lc3NhZ2UoXCJOb24gW2EtejAtOS8uXy1dIGNoYXJhY3RlciBpbiBwYXRoIG9mIGxvY2F0aW9uOiBcIiArIHJlc291cmNlTG9jYXRpb24pKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdFx0XHRcdH1cclxuXHRcdFx0XHRyZXNvdXJjZUxvY2F0aW9uID0gYG1pbmVjcmFmdDoke3Jlc291cmNlTG9jYXRpb259YDtcclxuXHRcdFx0XHRicmVhaztcclxuXHRcdFx0Y2FzZSAyOlxyXG5cdFx0XHRcdC8vIENoZWNrIG5hbWVzcGFjZVxyXG5cdFx0XHRcdGlmICghaXNWYWxpZChyZXNvdXJjZUxvY2F0aW9uUGFydHNbMF0sIHZhbGlkTmFtZXNwYWNlQ2hhcikpIHtcclxuXHRcdFx0XHRcdHRocm93IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZShuZXcgTGl0ZXJhbE1lc3NhZ2UoXCJOb24gW2EtejAtOV8uLV0gY2hhcmFjdGVyIGluIG5hbWVzcGFjZSBvZiBsb2NhdGlvbjogXCIgKyByZXNvdXJjZUxvY2F0aW9uKSkuY3JlYXRlV2l0aENvbnRleHQocmVhZGVyKTtcclxuXHRcdFx0XHR9XHJcblx0XHRcdFx0Ly8gQ2hlY2sgcGF0aFxyXG5cdFx0XHRcdGlmICghaXNWYWxpZChyZXNvdXJjZUxvY2F0aW9uUGFydHNbMV0sIHZhbGlkUGF0aENoYXIpKSB7XHJcblx0XHRcdFx0XHR0aHJvdyBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGUobmV3IExpdGVyYWxNZXNzYWdlKFwiTm9uIFthLXowLTkvLl8tXSBjaGFyYWN0ZXIgaW4gcGF0aCBvZiBsb2NhdGlvbjogXCIgKyByZXNvdXJjZUxvY2F0aW9uKSkuY3JlYXRlV2l0aENvbnRleHQocmVhZGVyKTtcclxuXHRcdFx0XHR9XHJcblx0XHRcdFx0YnJlYWs7XHJcblx0XHR9XHJcblxyXG5cdFx0cmV0dXJuIHJlc291cmNlTG9jYXRpb247XHJcblx0fVxyXG5cclxufVxyXG5cclxuLyoqXHJcbiAqIEhlbHBlciBmb3IgZ2VuZXJhdGluZyBQcm9taXNlPFN1Z2dlc3Rpb25zPiwgZnJvbSBTaGFyZWRTdWdnZXN0aW9uUHJvdmlkZXIuamF2YVxyXG4gKi9cclxuY2xhc3MgSGVscGVyU3VnZ2VzdGlvblByb3ZpZGVyIHtcclxuXHJcblx0cHVibGljIHN0YXRpYyBzdWdnZXN0KHN1Z2dlc3Rpb25zOiBzdHJpbmdbXSwgYnVpbGRlcjogU3VnZ2VzdGlvbnNCdWlsZGVyKTogUHJvbWlzZTxTdWdnZXN0aW9ucz4ge1xyXG5cdFx0bGV0IHJlbWFpbmluZ0xvd2VyY2FzZTogc3RyaW5nID0gYnVpbGRlci5nZXRSZW1haW5pbmcoKS50b0xvd2VyQ2FzZSgpO1xyXG5cdFx0Zm9yIChsZXQgc3VnZ2VzdGlvbiBvZiBzdWdnZXN0aW9ucykge1xyXG5cdFx0XHRpZiAoSGVscGVyU3VnZ2VzdGlvblByb3ZpZGVyLm1hdGNoZXNTdWJTdHIocmVtYWluaW5nTG93ZXJjYXNlLCBzdWdnZXN0aW9uLnRvTG93ZXJDYXNlKCkpKSB7XHJcblx0XHRcdFx0YnVpbGRlci5zdWdnZXN0KHN1Z2dlc3Rpb24pO1xyXG5cdFx0XHR9XHJcblx0XHR9XHJcblx0XHRyZXR1cm4gYnVpbGRlci5idWlsZFByb21pc2UoKTtcclxuXHR9XHJcblxyXG5cdHB1YmxpYyBzdGF0aWMgbWF0Y2hlc1N1YlN0cihyZW1haW5pbmc6IHN0cmluZywgc3VnZ2VzdGlvbjogc3RyaW5nKTogYm9vbGVhbiB7XHJcblx0XHRsZXQgaW5kZXg6IG51bWJlciA9IDA7XHJcblx0XHR3aGlsZSAoIXN1Z2dlc3Rpb24uc3RhcnRzV2l0aChyZW1haW5pbmcsIGluZGV4KSkge1xyXG5cdFx0XHRpbmRleCA9IHN1Z2dlc3Rpb24uaW5kZXhPZignXycsIGluZGV4KTtcclxuXHRcdFx0aWYgKGluZGV4IDwgMCkge1xyXG5cdFx0XHRcdHJldHVybiBmYWxzZTtcclxuXHRcdFx0fVxyXG5cdFx0XHRpbmRleCsrO1xyXG5cdFx0fVxyXG5cdFx0cmV0dXJuIHRydWU7XHJcblx0fVxyXG59XHJcblxyXG5leHBvcnQgY2xhc3MgVGltZUFyZ3VtZW50IGltcGxlbWVudHMgQXJndW1lbnRUeXBlPFRpbWVBcmd1bWVudD4ge1xyXG5cclxuXHRzdGF0aWMgVU5JVFM6IE1hcDxzdHJpbmcsIG51bWJlcj4gPSBuZXcgTWFwKFtcclxuXHRcdFtcImRcIiwgMjQwMDBdLFxyXG5cdFx0W1wic1wiLCAyMF0sXHJcblx0XHRbXCJ0XCIsIDFdLFxyXG5cdFx0W1wiXCIsIDFdXHJcblx0XSk7XHJcblxyXG5cdHB1YmxpYyB0aWNrczogbnVtYmVyO1xyXG5cclxuXHRjb25zdHJ1Y3Rvcih0aWNrczogbnVtYmVyID0gMCkge1xyXG5cdFx0dGhpcy50aWNrcyA9IHRpY2tzO1xyXG5cdH1cclxuXHJcblx0cHVibGljIHBhcnNlKHJlYWRlcjogU3RyaW5nUmVhZGVyKTogVGltZUFyZ3VtZW50IHtcclxuXHRcdGNvbnN0IG51bWVyaWNhbFZhbHVlOiBudW1iZXIgPSByZWFkZXIucmVhZEZsb2F0KCk7XHJcblx0XHRjb25zdCB1bml0OiBzdHJpbmcgPSByZWFkZXIucmVhZFVucXVvdGVkU3RyaW5nKCk7XHJcblx0XHRjb25zdCB1bml0TXVsdGlwbGllcjogbnVtYmVyID0gVGltZUFyZ3VtZW50LlVOSVRTLmdldCh1bml0KTtcclxuXHRcdGlmICh1bml0TXVsdGlwbGllciA9PT0gMCkge1xyXG5cdFx0XHR0aHJvdyBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGUobmV3IExpdGVyYWxNZXNzYWdlKGBJbnZhbGlkIHVuaXQgXCIke3VuaXR9XCJgKSkuY3JlYXRlV2l0aENvbnRleHQocmVhZGVyKTtcclxuXHRcdH1cclxuXHRcdGNvbnN0IHRpY2tzOiBudW1iZXIgPSBNYXRoLnJvdW5kKG51bWVyaWNhbFZhbHVlICogdW5pdE11bHRpcGxpZXIpO1xyXG5cdFx0aWYgKHRpY2tzIDwgMCkge1xyXG5cdFx0XHR0aHJvdyBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGUobmV3IExpdGVyYWxNZXNzYWdlKFwiVGljayBjb3VudCBtdXN0IGJlIG5vbi1uZWdhdGl2ZVwiKSkuY3JlYXRlV2l0aENvbnRleHQocmVhZGVyKTtcclxuXHRcdH1cclxuXHRcdHRoaXMudGlja3MgPSB0aWNrcztcclxuXHRcdHJldHVybiB0aGlzO1xyXG5cdH1cclxuXHJcblx0cHVibGljIGxpc3RTdWdnZXN0aW9ucyhfY29udGV4dDogQ29tbWFuZENvbnRleHQ8YW55PiwgYnVpbGRlcjogU3VnZ2VzdGlvbnNCdWlsZGVyKTogUHJvbWlzZTxTdWdnZXN0aW9ucz4ge1xyXG5cdFx0bGV0IHJlYWRlcjogU3RyaW5nUmVhZGVyID0gbmV3IFN0cmluZ1JlYWRlcihidWlsZGVyLmdldFJlbWFpbmluZygpKTtcclxuXHRcdHRyeSB7XHJcblx0XHRcdHJlYWRlci5yZWFkRmxvYXQoKTtcclxuXHRcdH0gY2F0Y2ggKGV4KSB7XHJcblx0XHRcdHJldHVybiBidWlsZGVyLmJ1aWxkUHJvbWlzZSgpO1xyXG5cdFx0fVxyXG5cdFx0cmV0dXJuIEhlbHBlclN1Z2dlc3Rpb25Qcm92aWRlci5zdWdnZXN0KFsuLi5UaW1lQXJndW1lbnQuVU5JVFMua2V5cygpXSwgYnVpbGRlci5jcmVhdGVPZmZzZXQoYnVpbGRlci5nZXRTdGFydCgpICsgcmVhZGVyLmdldEN1cnNvcigpKSk7XHJcblx0fVxyXG5cclxuXHRwdWJsaWMgZ2V0RXhhbXBsZXMoKTogc3RyaW5nW10ge1xyXG5cdFx0cmV0dXJuIFtcIjBkXCIsIFwiMHNcIiwgXCIwdFwiLCBcIjBcIl07XHJcblx0fVxyXG59XHJcblxyXG5leHBvcnQgY2xhc3MgQmxvY2tQb3NBcmd1bWVudCBpbXBsZW1lbnRzIEFyZ3VtZW50VHlwZTxCbG9ja1Bvc0FyZ3VtZW50PiB7XHJcblxyXG5cdHB1YmxpYyB4OiBudW1iZXI7XHJcblx0cHVibGljIHk6IG51bWJlcjtcclxuXHRwdWJsaWMgejogbnVtYmVyO1xyXG5cclxuXHRjb25zdHJ1Y3Rvcih4OiBudW1iZXIgPSAwLCB5OiBudW1iZXIgPSAwLCB6OiBudW1iZXIgPSAwKSB7XHJcblx0XHR0aGlzLnggPSB4O1xyXG5cdFx0dGhpcy55ID0geTtcclxuXHRcdHRoaXMueiA9IHo7XHJcblx0fVxyXG5cclxuXHRwdWJsaWMgcGFyc2UocmVhZGVyOiBTdHJpbmdSZWFkZXIpOiBCbG9ja1Bvc0FyZ3VtZW50IHtcclxuXHRcdHRoaXMueCA9IEV4dGVuZGVkU3RyaW5nUmVhZGVyLnJlYWRMb2NhdGlvbkxpdGVyYWwocmVhZGVyKTtcclxuXHRcdHJlYWRlci5za2lwKCk7XHJcblx0XHR0aGlzLnkgPSBFeHRlbmRlZFN0cmluZ1JlYWRlci5yZWFkTG9jYXRpb25MaXRlcmFsKHJlYWRlcik7XHJcblx0XHRyZWFkZXIuc2tpcCgpO1xyXG5cdFx0dGhpcy56ID0gRXh0ZW5kZWRTdHJpbmdSZWFkZXIucmVhZExvY2F0aW9uTGl0ZXJhbChyZWFkZXIpO1xyXG5cdFx0cmV0dXJuIHRoaXM7XHJcblx0fVxyXG5cclxuXHRwdWJsaWMgbGlzdFN1Z2dlc3Rpb25zKF9jb250ZXh0OiBDb21tYW5kQ29udGV4dDxhbnk+LCBidWlsZGVyOiBTdWdnZXN0aW9uc0J1aWxkZXIpOiBQcm9taXNlPFN1Z2dlc3Rpb25zPiB7XHJcblx0XHRidWlsZGVyLnN1Z2dlc3QoXCJ+XCIpO1xyXG5cdFx0YnVpbGRlci5zdWdnZXN0KFwifiB+XCIpO1xyXG5cdFx0YnVpbGRlci5zdWdnZXN0KFwifiB+IH5cIik7XHJcblx0XHRyZXR1cm4gYnVpbGRlci5idWlsZFByb21pc2UoKTtcclxuXHR9XHJcblxyXG5cdHB1YmxpYyBnZXRFeGFtcGxlcygpOiBzdHJpbmdbXSB7XHJcblx0XHRyZXR1cm4gW1wiMSAyIDNcIl07XHJcblx0fVxyXG59XHJcblxyXG5leHBvcnQgY2xhc3MgQ29sdW1uUG9zQXJndW1lbnQgaW1wbGVtZW50cyBBcmd1bWVudFR5cGU8Q29sdW1uUG9zQXJndW1lbnQ+IHtcclxuXHJcblx0cHVibGljIHg6IG51bWJlcjtcclxuXHRwdWJsaWMgejogbnVtYmVyO1xyXG5cclxuXHRjb25zdHJ1Y3Rvcih4ID0gMCwgeiA9IDApIHtcclxuXHRcdHRoaXMueCA9IHg7XHJcblx0XHR0aGlzLnogPSB6O1xyXG5cdH1cclxuXHJcblx0cHVibGljIHBhcnNlKHJlYWRlcjogU3RyaW5nUmVhZGVyKTogQ29sdW1uUG9zQXJndW1lbnQge1xyXG5cdFx0dGhpcy54ID0gRXh0ZW5kZWRTdHJpbmdSZWFkZXIucmVhZExvY2F0aW9uTGl0ZXJhbChyZWFkZXIpO1xyXG5cdFx0cmVhZGVyLnNraXAoKTtcclxuXHRcdHRoaXMueiA9IEV4dGVuZGVkU3RyaW5nUmVhZGVyLnJlYWRMb2NhdGlvbkxpdGVyYWwocmVhZGVyKTtcclxuXHRcdHJldHVybiB0aGlzO1xyXG5cdH1cclxuXHJcblx0cHVibGljIGxpc3RTdWdnZXN0aW9ucyhfY29udGV4dDogQ29tbWFuZENvbnRleHQ8YW55PiwgYnVpbGRlcjogU3VnZ2VzdGlvbnNCdWlsZGVyKTogUHJvbWlzZTxTdWdnZXN0aW9ucz4ge1xyXG5cdFx0YnVpbGRlci5zdWdnZXN0KFwiflwiKTtcclxuXHRcdGJ1aWxkZXIuc3VnZ2VzdChcIn4gflwiKTtcclxuXHRcdHJldHVybiBidWlsZGVyLmJ1aWxkUHJvbWlzZSgpO1xyXG5cdH1cclxuXHJcblx0cHVibGljIGdldEV4YW1wbGVzKCk6IHN0cmluZ1tdIHtcclxuXHRcdHJldHVybiBbXCIxIDJcIl07XHJcblx0fVxyXG59XHJcblxyXG5leHBvcnQgY2xhc3MgUGxheWVyQXJndW1lbnQgaW1wbGVtZW50cyBBcmd1bWVudFR5cGU8UGxheWVyQXJndW1lbnQ+IHtcclxuXHJcblx0cHVibGljIHVzZXJuYW1lOiBzdHJpbmc7XHJcblxyXG5cdGNvbnN0cnVjdG9yKHVzZXJuYW1lOiBzdHJpbmcgPSBcIlwiKSB7XHJcblx0XHR0aGlzLnVzZXJuYW1lID0gdXNlcm5hbWU7XHJcblx0fVxyXG5cclxuXHRwdWJsaWMgcGFyc2UocmVhZGVyOiBTdHJpbmdSZWFkZXIpOiBQbGF5ZXJBcmd1bWVudCB7XHJcblx0XHRjb25zdCBzdGFydDogbnVtYmVyID0gcmVhZGVyLmdldEN1cnNvcigpO1xyXG5cdFx0d2hpbGUgKHJlYWRlci5jYW5SZWFkKCkgJiYgcmVhZGVyLnBlZWsoKSAhPT0gXCIgXCIpIHtcclxuXHRcdFx0cmVhZGVyLnNraXAoKTtcclxuXHRcdH1cclxuXHJcblx0XHRjb25zdCBzdHJpbmc6IHN0cmluZyA9IHJlYWRlci5nZXRTdHJpbmcoKTtcclxuXHRcdGNvbnN0IGN1cnJlbnRDdXJzb3I6IG51bWJlciA9IHJlYWRlci5nZXRDdXJzb3IoKTtcclxuXHJcblx0XHR0aGlzLnVzZXJuYW1lID0gc3RyaW5nLnNsaWNlKHN0YXJ0LCBjdXJyZW50Q3Vyc29yKTtcclxuXHJcblx0XHRpZiAoIXRoaXMudXNlcm5hbWUubWF0Y2goL15bQS1aYS16MC05X117MiwxNn0kLykpIHtcclxuXHRcdFx0dGhyb3cgbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKG5ldyBMaXRlcmFsTWVzc2FnZSh0aGlzLnVzZXJuYW1lICsgXCIgaXMgbm90IGEgdmFsaWQgdXNlcm5hbWVcIikpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlcik7XHJcblx0XHR9XHJcblxyXG5cdFx0cmV0dXJuIHRoaXM7XHJcblx0fVxyXG5cclxuXHRwdWJsaWMgZ2V0RXhhbXBsZXMoKTogc3RyaW5nW10ge1xyXG5cdFx0cmV0dXJuIFtcIlNrZXB0ZXJcIl07XHJcblx0fVxyXG59XHJcblxyXG5leHBvcnQgY2xhc3MgTXVsdGlMaXRlcmFsQXJndW1lbnQgaW1wbGVtZW50cyBBcmd1bWVudFR5cGU8TXVsdGlMaXRlcmFsQXJndW1lbnQ+IHtcclxuXHJcblx0cHJpdmF0ZSBsaXRlcmFsczogc3RyaW5nW107XHJcblx0cHVibGljIHNlbGVjdGVkTGl0ZXJhbDogc3RyaW5nO1xyXG5cclxuXHRjb25zdHJ1Y3RvcihsaXRlcmFsczogc3RyaW5nW10pIHtcclxuXHRcdHRoaXMubGl0ZXJhbHMgPSBsaXRlcmFscztcclxuXHRcdHRoaXMuc2VsZWN0ZWRMaXRlcmFsID0gXCJcIjtcclxuXHR9XHJcblxyXG5cdHB1YmxpYyBwYXJzZShyZWFkZXI6IFN0cmluZ1JlYWRlcikge1xyXG5cdFx0Y29uc3Qgc3RhcnQ6IG51bWJlciA9IHJlYWRlci5nZXRDdXJzb3IoKTtcclxuXHRcdHdoaWxlIChyZWFkZXIuY2FuUmVhZCgpICYmIHJlYWRlci5wZWVrKCkgIT09IFwiIFwiKSB7XHJcblx0XHRcdHJlYWRlci5za2lwKCk7XHJcblx0XHR9XHJcblxyXG5cdFx0dGhpcy5zZWxlY3RlZExpdGVyYWwgPSByZWFkZXIuZ2V0U3RyaW5nKCkuc2xpY2Uoc3RhcnQsIHJlYWRlci5nZXRDdXJzb3IoKSk7XHJcblxyXG5cdFx0aWYgKHRoaXMuc2VsZWN0ZWRMaXRlcmFsLmVuZHNXaXRoKFwiIFwiKSkge1xyXG5cdFx0XHR0aGlzLnNlbGVjdGVkTGl0ZXJhbC50cmltRW5kKCk7XHJcblx0XHRcdHJlYWRlci5zZXRDdXJzb3IocmVhZGVyLmdldEN1cnNvcigpIC0gMSk7XHJcblx0XHR9XHJcblxyXG5cdFx0aWYgKCF0aGlzLmxpdGVyYWxzLmluY2x1ZGVzKHRoaXMuc2VsZWN0ZWRMaXRlcmFsKSkge1xyXG5cdFx0XHR0aHJvdyBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGUobmV3IExpdGVyYWxNZXNzYWdlKHRoaXMuc2VsZWN0ZWRMaXRlcmFsICsgXCIgaXMgbm90IG9uZSBvZiBcIiArIHRoaXMubGl0ZXJhbHMpKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdFx0fVxyXG5cclxuXHRcdHJldHVybiB0aGlzO1xyXG5cdH1cclxuXHJcblx0cHVibGljIGxpc3RTdWdnZXN0aW9ucyhfY29udGV4dDogQ29tbWFuZENvbnRleHQ8YW55PiwgYnVpbGRlcjogU3VnZ2VzdGlvbnNCdWlsZGVyKTogUHJvbWlzZTxTdWdnZXN0aW9ucz4ge1xyXG5cdFx0Zm9yIChsZXQgbGl0ZXJhbCBvZiB0aGlzLmxpdGVyYWxzKSB7XHJcblx0XHRcdGJ1aWxkZXIuc3VnZ2VzdChsaXRlcmFsKTtcclxuXHRcdH1cclxuXHRcdHJldHVybiBidWlsZGVyLmJ1aWxkUHJvbWlzZSgpO1xyXG5cdH1cclxuXHJcblx0cHVibGljIGdldEV4YW1wbGVzKCk6IHN0cmluZ1tdIHtcclxuXHRcdHJldHVybiBbXCJibGFoXCJdO1xyXG5cdH1cclxufVxyXG5cclxuZXhwb3J0IGNsYXNzIENvbG9yQXJndW1lbnQgaW1wbGVtZW50cyBBcmd1bWVudFR5cGU8Q29sb3JBcmd1bWVudD4ge1xyXG5cclxuXHRzdGF0aWMgQ2hhdENvbG9yOiB7IFtjb2xvcjogc3RyaW5nXTogc3RyaW5nIH0gPSB7XHJcblx0XHQvLyBVc2VzIHRoZSBzZWN0aW9uIHN5bWJvbCAowqcpLCBqdXN0IGxpa2UgTWluZWNyYWZ0XHJcblx0XHRibGFjazogXCJcXHUwMEE3MFwiLFxyXG5cdFx0ZGFya19ibHVlOiBcIlxcdTAwQTcxXCIsXHJcblx0XHRkYXJrX2dyZWVuOiBcIlxcdTAwQTcyXCIsXHJcblx0XHRkYXJrX2FxdWE6IFwiXFx1MDBBNzNcIixcclxuXHRcdGRhcmtfcmVkOiBcIlxcdTAwQTc0XCIsXHJcblx0XHRkYXJrX3B1cnBsZTogXCJcXHUwMEE3NVwiLFxyXG5cdFx0Z29sZDogXCJcXHUwMEE3NlwiLFxyXG5cdFx0Z3JheTogXCJcXHUwMEE3N1wiLFxyXG5cdFx0ZGFya19ncmF5OiBcIlxcdTAwQTc4XCIsXHJcblx0XHRibHVlOiBcIlxcdTAwQTc5XCIsXHJcblx0XHRncmVlbjogXCJcXHUwMEE3YVwiLFxyXG5cdFx0YXF1YTogXCJcXHUwMEE3YlwiLFxyXG5cdFx0cmVkOiBcIlxcdTAwQTdjXCIsXHJcblx0XHRsaWdodF9wdXJwbGU6IFwiXFx1MDBBN2RcIixcclxuXHRcdHllbGxvdzogXCJcXHUwMEE3ZVwiLFxyXG5cdFx0d2hpdGU6IFwiXFx1MDBBN2ZcIixcclxuXHR9IGFzIGNvbnN0O1xyXG5cclxuXHRwdWJsaWMgY2hhdGNvbG9yOiBzdHJpbmc7XHJcblxyXG5cdGNvbnN0cnVjdG9yKGNoYXRjb2xvcjogc3RyaW5nID0gbnVsbCkge1xyXG5cdFx0dGhpcy5jaGF0Y29sb3IgPSBjaGF0Y29sb3I7XHJcblx0fVxyXG5cclxuXHRwdWJsaWMgcGFyc2UocmVhZGVyOiBTdHJpbmdSZWFkZXIpIHtcclxuXHRcdGxldCBpbnB1dCA9IHJlYWRlci5yZWFkVW5xdW90ZWRTdHJpbmcoKTtcclxuXHRcdGxldCBjaGF0Rm9ybWF0OiBzdHJpbmcgPSBDb2xvckFyZ3VtZW50LkNoYXRDb2xvcltpbnB1dC50b0xvd2VyQ2FzZSgpXTtcclxuXHRcdGlmIChjaGF0Rm9ybWF0ID09PSB1bmRlZmluZWQpIHtcclxuXHRcdFx0dGhyb3cgbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKG5ldyBMaXRlcmFsTWVzc2FnZShgVW5rbm93biBjb2xvdXIgJyR7aW5wdXR9J2ApKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdFx0fVxyXG5cdFx0dGhpcy5jaGF0Y29sb3IgPSBjaGF0Rm9ybWF0O1xyXG5cdFx0cmV0dXJuIHRoaXM7XHJcblx0fVxyXG5cclxuXHRwdWJsaWMgbGlzdFN1Z2dlc3Rpb25zKF9jb250ZXh0OiBDb21tYW5kQ29udGV4dDxhbnk+LCBidWlsZGVyOiBTdWdnZXN0aW9uc0J1aWxkZXIpOiBQcm9taXNlPFN1Z2dlc3Rpb25zPiB7XHJcblx0XHRyZXR1cm4gSGVscGVyU3VnZ2VzdGlvblByb3ZpZGVyLnN1Z2dlc3QoT2JqZWN0LmtleXMoQ29sb3JBcmd1bWVudC5DaGF0Q29sb3IpLCBidWlsZGVyKTtcclxuXHR9XHJcblxyXG5cdHB1YmxpYyBnZXRFeGFtcGxlcygpOiBzdHJpbmdbXSB7XHJcblx0XHRyZXR1cm4gW1wicmVkXCIsIFwiZ3JlZW5cIl07XHJcblx0fVxyXG59XHJcblxyXG5leHBvcnQgY2xhc3MgUG90aW9uRWZmZWN0QXJndW1lbnQgaW1wbGVtZW50cyBBcmd1bWVudFR5cGU8UG90aW9uRWZmZWN0QXJndW1lbnQ+IHtcclxuXHJcblx0c3RhdGljIHJlYWRvbmx5IFBvdGlvbkVmZmVjdHM6IHJlYWRvbmx5IHN0cmluZ1tdID0gW1xyXG5cdFx0XCJtaW5lY3JhZnQ6c3BlZWRcIixcclxuXHRcdFwibWluZWNyYWZ0OnNsb3duZXNzXCIsXHJcblx0XHRcIm1pbmVjcmFmdDpoYXN0ZVwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6bWluaW5nX2ZhdGlndWVcIixcclxuXHRcdFwibWluZWNyYWZ0OnN0cmVuZ3RoXCIsXHJcblx0XHRcIm1pbmVjcmFmdDppbnN0YW50X2hlYWx0aFwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6aW5zdGFudF9kYW1hZ2VcIixcclxuXHRcdFwibWluZWNyYWZ0Omp1bXBfYm9vc3RcIixcclxuXHRcdFwibWluZWNyYWZ0Om5hdXNlYVwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6cmVnZW5lcmF0aW9uXCIsXHJcblx0XHRcIm1pbmVjcmFmdDpyZXNpc3RhbmNlXCIsXHJcblx0XHRcIm1pbmVjcmFmdDpmaXJlX3Jlc2lzdGFuY2VcIixcclxuXHRcdFwibWluZWNyYWZ0OndhdGVyX2JyZWF0aGluZ1wiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6aW52aXNpYmlsaXR5XCIsXHJcblx0XHRcIm1pbmVjcmFmdDpibGluZG5lc3NcIixcclxuXHRcdFwibWluZWNyYWZ0Om5pZ2h0X3Zpc2lvblwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6aHVuZ2VyXCIsXHJcblx0XHRcIm1pbmVjcmFmdDp3ZWFrbmVzc1wiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6cG9pc29uXCIsXHJcblx0XHRcIm1pbmVjcmFmdDp3aXRoZXJcIixcclxuXHRcdFwibWluZWNyYWZ0OmhlYWx0aF9ib29zdFwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6YWJzb3JwdGlvblwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6c2F0dXJhdGlvblwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6Z2xvd2luZ1wiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6bGV2aXRhdGlvblwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6bHVja1wiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6dW5sdWNrXCIsXHJcblx0XHRcIm1pbmVjcmFmdDpzbG93X2ZhbGxpbmdcIixcclxuXHRcdFwibWluZWNyYWZ0OmNvbmR1aXRfcG93ZXJcIixcclxuXHRcdFwibWluZWNyYWZ0OmRvbHBoaW5zX2dyYWNlXCIsXHJcblx0XHRcIm1pbmVjcmFmdDpiYWRfb21lblwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6aGVyb19vZl90aGVfdmlsbGFnZVwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6ZGFya25lc3NcIixcclxuXHRdIGFzIGNvbnN0O1xyXG5cclxuXHRwdWJsaWMgcG90aW9uRWZmZWN0OiBzdHJpbmc7XHJcblxyXG5cdGNvbnN0cnVjdG9yKHBvdGlvbkVmZmVjdDogc3RyaW5nID0gbnVsbCkge1xyXG5cdFx0dGhpcy5wb3Rpb25FZmZlY3QgPSBwb3Rpb25FZmZlY3Q7XHJcblx0fVxyXG5cclxuXHRwdWJsaWMgcGFyc2UocmVhZGVyOiBTdHJpbmdSZWFkZXIpOiBQb3Rpb25FZmZlY3RBcmd1bWVudCB7XHJcblx0XHRjb25zdCBpbnB1dCA9IEV4dGVuZGVkU3RyaW5nUmVhZGVyLnJlYWRSZXNvdXJjZUxvY2F0aW9uKHJlYWRlcik7XHJcblx0XHRjb25zdCBpc1ZhbGlkUG90aW9uRWZmZWN0OiBib29sZWFuID0gUG90aW9uRWZmZWN0QXJndW1lbnQuUG90aW9uRWZmZWN0cy5pbmNsdWRlcyhpbnB1dC50b0xvd2VyQ2FzZSgpKTtcclxuXHRcdGlmICghaXNWYWxpZFBvdGlvbkVmZmVjdCkge1xyXG5cdFx0XHR0aHJvdyBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGUobmV3IExpdGVyYWxNZXNzYWdlKGBVbmtub3duIGVmZmVjdCAnJHtpbnB1dH0nYCkpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlcik7XHJcblx0XHR9XHJcblx0XHR0aGlzLnBvdGlvbkVmZmVjdCA9IGlucHV0O1xyXG5cdFx0cmV0dXJuIHRoaXM7XHJcblx0fVxyXG5cclxuXHRwdWJsaWMgbGlzdFN1Z2dlc3Rpb25zKF9jb250ZXh0OiBDb21tYW5kQ29udGV4dDxhbnk+LCBidWlsZGVyOiBTdWdnZXN0aW9uc0J1aWxkZXIpOiBQcm9taXNlPFN1Z2dlc3Rpb25zPiB7XHJcblx0XHRyZXR1cm4gSGVscGVyU3VnZ2VzdGlvblByb3ZpZGVyLnN1Z2dlc3QoWy4uLlBvdGlvbkVmZmVjdEFyZ3VtZW50LlBvdGlvbkVmZmVjdHNdLCBidWlsZGVyKTtcclxuXHR9XHJcblxyXG5cdHB1YmxpYyBnZXRFeGFtcGxlcygpOiBzdHJpbmdbXSB7XHJcblx0XHRyZXR1cm4gW1wic3Bvb2t5XCIsIFwiZWZmZWN0XCJdO1xyXG5cdH1cclxufVxyXG5cclxuZXhwb3J0IGNsYXNzIEFuZ2xlQXJndW1lbnQgaW1wbGVtZW50cyBBcmd1bWVudFR5cGU8QW5nbGVBcmd1bWVudD4ge1xyXG5cclxuXHRwdWJsaWMgYW5nbGU6IG51bWJlcjtcclxuXHRwdWJsaWMgcmVsYXRpdmU6IGJvb2xlYW47XHJcblxyXG5cdHB1YmxpYyBwYXJzZShyZWFkZXI6IFN0cmluZ1JlYWRlcik6IEFuZ2xlQXJndW1lbnQge1xyXG5cdFx0aWYoIXJlYWRlci5jYW5SZWFkKCkpIHtcclxuXHRcdFx0dGhyb3cgbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKG5ldyBMaXRlcmFsTWVzc2FnZShcIkluY29tcGxldGUgKGV4cGVjdGVkIDEgYW5nbGUpXCIpKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdFx0fVxyXG5cdFx0Ly8gUmVhZCByZWxhdGl2ZSB+XHJcblx0XHRpZiAocmVhZGVyLnBlZWsoKSA9PT0gJ34nKSB7XHJcblx0XHRcdHRoaXMucmVsYXRpdmUgPSB0cnVlO1xyXG5cdFx0XHRyZWFkZXIuc2tpcCgpO1xyXG5cdFx0fSBlbHNlIHtcclxuXHRcdFx0dGhpcy5yZWxhdGl2ZSA9IGZhbHNlO1xyXG5cdFx0fVxyXG5cdFx0dGhpcy5hbmdsZSA9IChyZWFkZXIuY2FuUmVhZCgpICYmIHJlYWRlci5wZWVrKCkgIT09ICcgJykgPyByZWFkZXIucmVhZEZsb2F0KCkgOiAwLjA7XHJcblx0XHRpZihpc05hTih0aGlzLmFuZ2xlKSB8fCAhaXNGaW5pdGUodGhpcy5hbmdsZSkpIHtcclxuXHRcdFx0dGhyb3cgbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKG5ldyBMaXRlcmFsTWVzc2FnZShcIkludmFsaWQgYW5nbGVcIikpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlcik7XHJcblx0XHR9XHJcblx0XHRyZXR1cm4gdGhpcztcclxuXHR9XHJcblxyXG5cdHB1YmxpYyBnZXRFeGFtcGxlcygpOiBzdHJpbmdbXSB7XHJcblx0XHRyZXR1cm4gW1wiMFwiLCBcIn5cIiwgXCJ+LTVcIl07XHJcblx0fVxyXG59XHJcblxyXG5leHBvcnQgY2xhc3MgVVVJREFyZ3VtZW50IGltcGxlbWVudHMgQXJndW1lbnRUeXBlPFVVSURBcmd1bWVudD4ge1xyXG5cclxuXHRwdWJsaWMgdXVpZDogc3RyaW5nO1xyXG5cclxuXHRwdWJsaWMgcGFyc2UocmVhZGVyOiBTdHJpbmdSZWFkZXIpOiBVVUlEQXJndW1lbnQge1xyXG5cdFx0Y29uc3QgcmVtYWluaW5nOiBzdHJpbmcgPSByZWFkZXIuZ2V0UmVtYWluaW5nKCk7XHJcblx0XHRjb25zdCBtYXRjaGVkUmVzdWx0cyA9IHJlbWFpbmluZy5tYXRjaCgvXihbLUEtRmEtZjAtOV0rKS8pO1xyXG5cdFx0aWYobWF0Y2hlZFJlc3VsdHMgIT09IG51bGwpIHtcclxuXHRcdFx0dGhpcy51dWlkID0gbWF0Y2hlZFJlc3VsdHNbMV07XHJcblx0XHRcdC8vIFJlZ2V4IGZvciBhIFVVSUQ6IGh0dHBzOi8vc3RhY2tvdmVyZmxvdy5jb20vYS8xMzY1MzE4MC80Nzc5MDcxXHJcblx0XHRcdGlmKHRoaXMudXVpZC5tYXRjaCgvXlswLTlhLWZdezh9LVswLTlhLWZdezR9LVswLTVdWzAtOWEtZl17M30tWzA4OWFiXVswLTlhLWZdezN9LVswLTlhLWZdezEyfSQvaSkgIT09IG51bGwpIHtcclxuXHRcdFx0XHRyZWFkZXIuc2V0Q3Vyc29yKHJlYWRlci5nZXRDdXJzb3IoKSArIHRoaXMudXVpZC5sZW5ndGgpO1xyXG5cdFx0XHRcdHJldHVybiB0aGlzO1xyXG5cdFx0XHR9XHJcblx0XHR9XHJcblx0XHR0aHJvdyBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGUobmV3IExpdGVyYWxNZXNzYWdlKFwiSW52YWxpZCBVVUlEXCIpKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdH1cclxuXHJcblx0cHVibGljIGdldEV4YW1wbGVzKCk6IHN0cmluZ1tdIHtcclxuXHRcdHJldHVybiBbXCJkZDEyYmU0Mi01MmE5LTRhOTEtYThhMS0xMWMwMTg0OWU0OThcIl07XHJcblx0fVxyXG59XHJcblxyXG50eXBlIE1vZGlmaWVyID0gKHJlYWRlcjogU3RyaW5nUmVhZGVyKSA9PiB2b2lkO1xyXG5cclxuZXhwb3J0IGNsYXNzIEVudGl0eVNlbGVjdG9yQXJndW1lbnQgaW1wbGVtZW50cyBBcmd1bWVudFR5cGU8RW50aXR5U2VsZWN0b3JBcmd1bWVudD4ge1xyXG5cclxuXHRzdGF0aWMgc2hvdWxkSW52ZXJ0VmFsdWUocmVhZGVyOiBTdHJpbmdSZWFkZXIpOiBib29sZWFuIHtcclxuXHRcdHJlYWRlci5za2lwV2hpdGVzcGFjZSgpO1xyXG5cdFx0aWYgKHJlYWRlci5jYW5SZWFkKCkgJiYgcmVhZGVyLnBlZWsoKSA9PT0gJyEnKSB7XHJcblx0XHRcdHJlYWRlci5za2lwKCk7XHJcblx0XHRcdHJlYWRlci5za2lwV2hpdGVzcGFjZSgpO1xyXG5cdFx0XHRyZXR1cm4gdHJ1ZTtcclxuXHRcdH1cclxuXHRcdHJldHVybiBmYWxzZTtcclxuXHR9XHJcblxyXG5cdHN0YXRpYyBPcHRpb25zOiB7IFtvcHRpb246IHN0cmluZ106IE1vZGlmaWVyIH0gPSB7XHJcblx0XHRuYW1lOiAocmVhZGVyOiBTdHJpbmdSZWFkZXIpOiB2b2lkID0+IHtcclxuXHRcdFx0Y29uc3Qgc3RhcnQ6IG51bWJlciA9IHJlYWRlci5nZXRDdXJzb3IoKTtcclxuXHRcdFx0Y29uc3Qgc2hvdWxkSW52ZXJ0OiBib29sZWFuID0gdGhpcy5zaG91bGRJbnZlcnRWYWx1ZShyZWFkZXIpO1xyXG5cdFx0XHQvLyBjb25zdCBfczogc3RyaW5nID0gcmVhZGVyLnJlYWRTdHJpbmcoKTtcclxuXHRcdFx0aWYoLyogU1RVQjogdGhpcy5oYXNOYW1lTm90RXF1YWxzKCkgKi8gIXNob3VsZEludmVydCkge1xyXG5cdFx0XHRcdHJlYWRlci5zZXRDdXJzb3Ioc3RhcnQpO1xyXG5cdFx0XHRcdHRocm93IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZShuZXcgTGl0ZXJhbE1lc3NhZ2UoYE9wdGlvbiAnbmFtZScgaXNuJ3QgYXBwbGljYWJsZSBoZXJlYCkpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlcik7XHJcblx0XHRcdH1cclxuXHRcdFx0aWYoc2hvdWxkSW52ZXJ0KSB7XHJcblx0XHRcdFx0Ly8gc3R1Yjogc2V0SGFzTmFtZU5vdEVxdWFsKHRydWUpXHJcblx0XHRcdH0gZWxzZSB7XHJcblx0XHRcdFx0Ly8gc3R1Yjogc2V0SGFzTmFtZUVxdWFscyh0cnVlKVxyXG5cdFx0XHR9XHJcblx0XHRcdC8vIFByZWRpY2F0ZT9cclxuXHRcdH0sXHJcblx0XHRkaXN0YW5jZTogKF9yZWFkZXI6IFN0cmluZ1JlYWRlcik6IHZvaWQgPT4ge30sXHJcblx0XHRsZXZlbDogKF9yZWFkZXI6IFN0cmluZ1JlYWRlcik6IHZvaWQgPT4ge30sXHJcblx0XHR4OiAocmVhZGVyOiBTdHJpbmdSZWFkZXIpOiB2b2lkID0+IHsgcmVhZGVyLnJlYWRGbG9hdCgpIH0sXHJcblx0XHR5OiAocmVhZGVyOiBTdHJpbmdSZWFkZXIpOiB2b2lkID0+IHsgcmVhZGVyLnJlYWRGbG9hdCgpIH0sXHJcblx0XHR6OiAocmVhZGVyOiBTdHJpbmdSZWFkZXIpOiB2b2lkID0+IHsgcmVhZGVyLnJlYWRGbG9hdCgpIH0sXHJcblx0XHRkeDogKHJlYWRlcjogU3RyaW5nUmVhZGVyKTogdm9pZCA9PiB7IHJlYWRlci5yZWFkRmxvYXQoKSB9LFxyXG5cdFx0ZHk6IChyZWFkZXI6IFN0cmluZ1JlYWRlcik6IHZvaWQgPT4geyByZWFkZXIucmVhZEZsb2F0KCkgfSxcclxuXHRcdGR6OiAocmVhZGVyOiBTdHJpbmdSZWFkZXIpOiB2b2lkID0+IHsgcmVhZGVyLnJlYWRGbG9hdCgpIH0sXHJcblx0XHR4X3JvdGF0aW9uOiAoX3JlYWRlcjogU3RyaW5nUmVhZGVyKTogdm9pZCA9PiB7fSxcclxuXHRcdHlfcm90YXRpb246IChfcmVhZGVyOiBTdHJpbmdSZWFkZXIpOiB2b2lkID0+IHt9LFxyXG5cdFx0bGltaXQ6IChfcmVhZGVyOiBTdHJpbmdSZWFkZXIpOiB2b2lkID0+IHt9LFxyXG5cdFx0c29ydDogKF9yZWFkZXI6IFN0cmluZ1JlYWRlcik6IHZvaWQgPT4ge30sXHJcblx0XHRnYW1lbW9kZTogKF9yZWFkZXI6IFN0cmluZ1JlYWRlcik6IHZvaWQgPT4ge30sXHJcblx0XHR0ZWFtOiAoX3JlYWRlcjogU3RyaW5nUmVhZGVyKTogdm9pZCA9PiB7fSxcclxuXHRcdHR5cGU6IChfcmVhZGVyOiBTdHJpbmdSZWFkZXIpOiB2b2lkID0+IHt9LFxyXG5cdFx0dGFnOiAoX3JlYWRlcjogU3RyaW5nUmVhZGVyKTogdm9pZCA9PiB7fSxcclxuXHRcdG5idDogKF9yZWFkZXI6IFN0cmluZ1JlYWRlcik6IHZvaWQgPT4ge30sXHJcblx0XHRzY29yZXM6IChfcmVhZGVyOiBTdHJpbmdSZWFkZXIpOiB2b2lkID0+IHt9LFxyXG5cdFx0YWR2YW5jZW1lbnRzOiAoX3JlYWRlcjogU3RyaW5nUmVhZGVyKTogdm9pZCA9PiB7fSxcclxuXHRcdHByZWRpY2F0ZTogKF9yZWFkZXI6IFN0cmluZ1JlYWRlcik6IHZvaWQgPT4ge31cclxuXHR9IGFzIGNvbnN0O1xyXG5cclxuXHRwdWJsaWMgcGFyc2UocmVhZGVyOiBTdHJpbmdSZWFkZXIpOiBFbnRpdHlTZWxlY3RvckFyZ3VtZW50IHtcclxuXHJcblx0XHQvLyB1dXV1dXV1dXV1dXVnaCwgSSBzbyB0b3RhbGx5IGRpZG4ndCB3YW50IHRvIGltcGxlbWVudCB0aGlzIGJpZyBjaHVuZ3VzIGFyZ3VtZW50LCBidXQgaGVyZSB3ZSBnbyFcclxuXHJcblx0XHRsZXQgZW50aXR5VVVJRDogc3RyaW5nO1xyXG5cdFx0bGV0IGluY2x1ZGVzRW50aXRpZXM6IGJvb2xlYW47XHJcblx0XHRsZXQgcGxheWVyTmFtZTogc3RyaW5nO1xyXG5cdFx0bGV0IG1heFJlc3VsdHM6IG51bWJlcjtcclxuXHRcdGxldCBsaW1pdGVkVG9QbGF5ZXJzOiBib29sZWFuOyAvLyBwbGF5ZXJzIG9ubHk/XHJcblx0XHRsZXQgY3VycmVudEVudGl0eTogYm9vbGVhbjtcclxuXHRcdGxldCBzaW5nbGU6IGJvb2xlYW47XHJcblxyXG5cdFx0ZnVuY3Rpb24gcGFyc2VPcHRpb25zKCk6IHZvaWQge1xyXG5cdFx0XHRyZWFkZXIuc2tpcFdoaXRlc3BhY2UoKTtcclxuXHRcdFx0d2hpbGUocmVhZGVyLmNhblJlYWQoKSAmJiByZWFkZXIucGVlaygpICE9PSBcIl1cIikge1xyXG5cdFx0XHRcdHJlYWRlci5za2lwV2hpdGVzcGFjZSgpO1xyXG5cdFx0XHRcdGxldCBzdGFydDogbnVtYmVyID0gcmVhZGVyLmdldEN1cnNvcigpO1xyXG5cdFx0XHRcdGxldCBzOiBzdHJpbmcgPSByZWFkZXIucmVhZFN0cmluZygpO1xyXG5cclxuXHRcdFx0XHRsZXQgbW9kaWZpZXI6IE1vZGlmaWVyID0gRW50aXR5U2VsZWN0b3JBcmd1bWVudC5PcHRpb25zW3NdO1xyXG5cdFx0XHRcdGlmKG1vZGlmaWVyID09PSBudWxsKSB7XHJcblx0XHRcdFx0XHRyZWFkZXIuc2V0Q3Vyc29yKHN0YXJ0KTtcclxuXHRcdFx0XHRcdHRocm93IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZShuZXcgTGl0ZXJhbE1lc3NhZ2UoYFVua25vd24gb3B0aW9uICcke3N9J2ApKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdFx0XHRcdH1cclxuXHJcblx0XHRcdFx0cmVhZGVyLnNraXBXaGl0ZXNwYWNlKCk7XHJcblx0XHRcdFx0aWYoIXJlYWRlci5jYW5SZWFkKCkgfHwgcmVhZGVyLnBlZWsoKSAhPT0gXCI9XCIpIHtcclxuXHRcdFx0XHRcdHJlYWRlci5zZXRDdXJzb3Ioc3RhcnQpO1xyXG5cdFx0XHRcdFx0dGhyb3cgbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKG5ldyBMaXRlcmFsTWVzc2FnZShgRXhwZWN0ZWQgdmFsdWUgZm9yIG9wdGlvbiAnJHtzfSdgKSkuY3JlYXRlV2l0aENvbnRleHQocmVhZGVyKTtcclxuXHRcdFx0XHR9XHJcblx0XHRcdFx0cmVhZGVyLnNraXAoKTtcclxuXHRcdFx0XHRyZWFkZXIuc2tpcFdoaXRlc3BhY2UoKTtcclxuXHRcdFx0XHRtb2RpZmllcihyZWFkZXIpO1xyXG5cdFx0XHRcdHJlYWRlci5za2lwV2hpdGVzcGFjZSgpO1xyXG5cdFx0XHRcdGlmKCFyZWFkZXIuY2FuUmVhZCgpKSB7XHJcblx0XHRcdFx0XHRjb250aW51ZTtcclxuXHRcdFx0XHR9XHJcblx0XHRcdFx0aWYocmVhZGVyLnBlZWsoKSA9PT0gXCIsXCIpIHtcclxuXHRcdFx0XHRcdHJlYWRlci5za2lwKCk7XHJcblx0XHRcdFx0XHRjb250aW51ZTtcclxuXHRcdFx0XHR9XHJcblx0XHRcdFx0aWYocmVhZGVyLnBlZWsoKSAhPT0gXCJdXCIpIHtcclxuXHRcdFx0XHRcdHRocm93IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZShuZXcgTGl0ZXJhbE1lc3NhZ2UoXCJFeHBlY3RlZCBlbmQgb2Ygb3B0aW9uc1wiKSkuY3JlYXRlV2l0aENvbnRleHQocmVhZGVyKTtcclxuXHRcdFx0XHR9XHJcblx0XHRcdH1cclxuXHRcdFx0aWYgKHJlYWRlci5jYW5SZWFkKCkpIHtcclxuXHRcdFx0XHRyZWFkZXIuc2tpcCgpO1xyXG5cdFx0XHRcdHJldHVybjtcclxuXHRcdFx0fVxyXG5cdFx0XHR0aHJvdyBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGUobmV3IExpdGVyYWxNZXNzYWdlKFwiRXhwZWN0ZWQgZW5kIG9mIG9wdGlvbnNcIikpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlcik7XHJcblx0XHR9XHJcblxyXG5cdFx0ZnVuY3Rpb24gcGFyc2VTZWxlY3RvcigpOiB2b2lkIHtcclxuXHRcdFx0aWYoIXJlYWRlci5jYW5SZWFkKCkpIHtcclxuXHRcdFx0XHR0aHJvdyBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGUobmV3IExpdGVyYWxNZXNzYWdlKFwiTWlzc2luZyBzZWxlY3RvciB0eXBlXCIpKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdFx0XHR9XHJcblxyXG5cdFx0XHRsZXQgc3RhcnQ6IG51bWJlciA9IHJlYWRlci5nZXRDdXJzb3IoKTtcclxuXHRcdFx0bGV0IHNlbGVjdG9yQ2hhcjogc3RyaW5nID0gcmVhZGVyLnJlYWQoKTtcclxuXHRcdFx0c3dpdGNoKHNlbGVjdG9yQ2hhcikge1xyXG5cdFx0XHRcdGNhc2UgXCJwXCI6XHJcblx0XHRcdFx0XHRtYXhSZXN1bHRzID0gMTtcclxuXHRcdFx0XHRcdGluY2x1ZGVzRW50aXRpZXMgPSBmYWxzZTtcclxuXHRcdFx0XHRcdGxpbWl0ZWRUb1BsYXllcnMgPSB0cnVlO1xyXG5cdFx0XHRcdFx0YnJlYWs7XHJcblx0XHRcdFx0Y2FzZSBcImFcIjpcclxuXHRcdFx0XHRcdG1heFJlc3VsdHMgPSBOdW1iZXIuTUFYX1NBRkVfSU5URUdFUjtcclxuXHRcdFx0XHRcdGluY2x1ZGVzRW50aXRpZXMgPSBmYWxzZTtcclxuXHRcdFx0XHRcdGxpbWl0ZWRUb1BsYXllcnMgPSB0cnVlO1xyXG5cdFx0XHRcdFx0YnJlYWs7XHJcblx0XHRcdFx0Y2FzZSBcInJcIjpcclxuXHRcdFx0XHRcdG1heFJlc3VsdHMgPSAxO1xyXG5cdFx0XHRcdFx0aW5jbHVkZXNFbnRpdGllcyA9IGZhbHNlO1xyXG5cdFx0XHRcdFx0bGltaXRlZFRvUGxheWVycyA9IHRydWU7XHJcblx0XHRcdFx0XHRicmVhaztcclxuXHRcdFx0XHRjYXNlIFwic1wiOlxyXG5cdFx0XHRcdFx0bWF4UmVzdWx0cyA9IDE7XHJcblx0XHRcdFx0XHRpbmNsdWRlc0VudGl0aWVzID0gdHJ1ZTtcclxuXHRcdFx0XHRcdGN1cnJlbnRFbnRpdHkgPSB0cnVlO1xyXG5cdFx0XHRcdFx0YnJlYWs7XHJcblx0XHRcdFx0Y2FzZSBcImVcIjpcclxuXHRcdFx0XHRcdG1heFJlc3VsdHMgPSBOdW1iZXIuTUFYX1NBRkVfSU5URUdFUjtcclxuXHRcdFx0XHRcdGluY2x1ZGVzRW50aXRpZXMgPSB0cnVlO1xyXG5cdFx0XHRcdFx0YnJlYWs7XHJcblx0XHRcdFx0ZGVmYXVsdDpcclxuXHRcdFx0XHRcdHJlYWRlci5zZXRDdXJzb3Ioc3RhcnQpO1xyXG5cdFx0XHRcdFx0dGhyb3cgbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKG5ldyBMaXRlcmFsTWVzc2FnZShgVW5rbm93biBzZWxlY3RvciB0eXBlICcke3NlbGVjdG9yQ2hhcn0nYCkpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlcik7XHJcblx0XHRcdH1cclxuXHJcblx0XHRcdGlmIChyZWFkZXIuY2FuUmVhZCgpICYmIHJlYWRlci5wZWVrKCkgPT09IFwiW1wiKSB7XHJcblx0XHRcdFx0cmVhZGVyLnNraXAoKTtcclxuXHRcdFx0XHRwYXJzZU9wdGlvbnMoKTtcclxuXHRcdFx0fVxyXG5cdFx0fVxyXG5cclxuXHRcdGZ1bmN0aW9uIHBhcnNlTmFtZU9yVVVJRCgpOiB2b2lkIHtcclxuXHRcdFx0bGV0IGk6IG51bWJlciA9IHJlYWRlci5nZXRDdXJzb3IoKTtcclxuXHRcdFx0bGV0IHM6IHN0cmluZyA9IHJlYWRlci5yZWFkU3RyaW5nKCk7XHJcblxyXG5cdFx0XHQvLyBSZWdleCBmb3IgYSBVVUlEOiBodHRwczovL3N0YWNrb3ZlcmZsb3cuY29tL2EvMTM2NTMxODAvNDc3OTA3MVxyXG5cdFx0XHRpZihzLm1hdGNoKC9eWzAtOWEtZl17OH0tWzAtOWEtZl17NH0tWzAtNV1bMC05YS1mXXszfS1bMDg5YWJdWzAtOWEtZl17M30tWzAtOWEtZl17MTJ9JC9pKSAhPT0gbnVsbCkge1xyXG5cdFx0XHRcdGVudGl0eVVVSUQgPSBzO1xyXG5cdFx0XHRcdGluY2x1ZGVzRW50aXRpZXMgPSB0cnVlO1xyXG5cdFx0XHR9IGVsc2UgaWYocy5sZW5ndGggPT09IDAgfHwgcy5sZW5ndGggPiAxNikge1xyXG5cdFx0XHRcdHJlYWRlci5zZXRDdXJzb3IoaSk7XHJcblx0XHRcdFx0dGhyb3cgbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKG5ldyBMaXRlcmFsTWVzc2FnZShcIkludmFsaWQgbmFtZSBvciBVVUlEXCIpKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdFx0XHR9IGVsc2Uge1xyXG5cdFx0XHRcdHBsYXllck5hbWUgPSBzO1xyXG5cdFx0XHRcdGluY2x1ZGVzRW50aXRpZXMgPSBmYWxzZTtcclxuXHRcdFx0fVxyXG5cclxuXHRcdFx0Ly8gV2UncmUgb25seSBhbGxvd2luZyAxIHJlc3VsdCBiZWNhdXNlIHdlJ3ZlIHNwZWNpZmllZCBhIHBsYXllciBvclxyXG5cdFx0XHQvLyBVVUlELCBhbmQgbm90IGFuIEAgc2VsZWN0b3JcclxuXHRcdFx0bWF4UmVzdWx0cyA9IDE7XHJcblx0XHR9XHJcblxyXG5cdFx0bGV0IHN0YXJ0UG9zaXRpb246IG51bWJlciA9IHJlYWRlci5nZXRDdXJzb3IoKTtcclxuXHRcdGlmKHJlYWRlci5jYW5SZWFkKCkgJiYgcmVhZGVyLnBlZWsoKSA9PT0gXCJAXCIpIHtcclxuXHRcdFx0cmVhZGVyLnNraXAoKTtcclxuXHRcdFx0cGFyc2VTZWxlY3RvcigpXHJcblx0XHR9IGVsc2Uge1xyXG5cdFx0XHRwYXJzZU5hbWVPclVVSUQoKTtcclxuXHRcdH1cclxuXHJcblx0XHQvLyBGaW5hbCBjaGVja3MuLi5cclxuXHRcdGlmKG1heFJlc3VsdHMgPiAwICYmIHNpbmdsZSkge1xyXG5cdFx0XHRyZWFkZXIuc2V0Q3Vyc29yKDApO1xyXG5cdFx0XHRpZihsaW1pdGVkVG9QbGF5ZXJzKSB7XHJcblx0XHRcdFx0Ly8gdGhyb3cgdGhyb3cgRVJST1JfTk9UX1NJTkdMRV9QTEFZRVIuY3JlYXRlV2l0aENvbnRleHQoc3RyaW5ncmVhZGVyKTtcclxuXHRcdFx0fSBlbHNlIHtcclxuXHRcdFx0XHQvLyB0aHJvdyBFUlJPUl9OT1RfU0lOR0xFX0VOVElUWS5jcmVhdGVXaXRoQ29udGV4dChzdHJpbmdyZWFkZXIpO1xyXG5cdFx0XHR9XHJcblx0XHR9XHJcblx0XHRpZihpbmNsdWRlc0VudGl0aWVzICYmIGxpbWl0ZWRUb1BsYXllcnMgLyogU1RVQjogIWlzU2VsZlNlbGVjdG9yKCkgKi8pIHtcclxuXHRcdFx0cmVhZGVyLnNldEN1cnNvcigwKTtcclxuXHRcdFx0Ly8gdGhyb3cgRVJST1JfT05MWV9QTEFZRVJTX0FMTE9XRUQuY3JlYXRlV2l0aENvbnRleHQoc3RyaW5ncmVhZGVyKTtcclxuXHRcdH1cclxuXHJcblx0XHRyZXR1cm4gdGhpcztcclxuXHR9XHJcblxyXG5cdHB1YmxpYyBnZXRFeGFtcGxlcygpOiBzdHJpbmdbXSB7XHJcblx0XHRyZXR1cm4gW1wiZGQxMmJlNDItNTJhOS00YTkxLWE4YTEtMTFjMDE4NDllNDk4XCJdO1xyXG5cdH1cclxufSIsIi8vIFRoZSBtb2R1bGUgY2FjaGVcbnZhciBfX3dlYnBhY2tfbW9kdWxlX2NhY2hlX18gPSB7fTtcblxuLy8gVGhlIHJlcXVpcmUgZnVuY3Rpb25cbmZ1bmN0aW9uIF9fd2VicGFja19yZXF1aXJlX18obW9kdWxlSWQpIHtcblx0Ly8gQ2hlY2sgaWYgbW9kdWxlIGlzIGluIGNhY2hlXG5cdHZhciBjYWNoZWRNb2R1bGUgPSBfX3dlYnBhY2tfbW9kdWxlX2NhY2hlX19bbW9kdWxlSWRdO1xuXHRpZiAoY2FjaGVkTW9kdWxlICE9PSB1bmRlZmluZWQpIHtcblx0XHRyZXR1cm4gY2FjaGVkTW9kdWxlLmV4cG9ydHM7XG5cdH1cblx0Ly8gQ3JlYXRlIGEgbmV3IG1vZHVsZSAoYW5kIHB1dCBpdCBpbnRvIHRoZSBjYWNoZSlcblx0dmFyIG1vZHVsZSA9IF9fd2VicGFja19tb2R1bGVfY2FjaGVfX1ttb2R1bGVJZF0gPSB7XG5cdFx0Ly8gbm8gbW9kdWxlLmlkIG5lZWRlZFxuXHRcdC8vIG5vIG1vZHVsZS5sb2FkZWQgbmVlZGVkXG5cdFx0ZXhwb3J0czoge31cblx0fTtcblxuXHQvLyBFeGVjdXRlIHRoZSBtb2R1bGUgZnVuY3Rpb25cblx0X193ZWJwYWNrX21vZHVsZXNfX1ttb2R1bGVJZF0uY2FsbChtb2R1bGUuZXhwb3J0cywgbW9kdWxlLCBtb2R1bGUuZXhwb3J0cywgX193ZWJwYWNrX3JlcXVpcmVfXyk7XG5cblx0Ly8gUmV0dXJuIHRoZSBleHBvcnRzIG9mIHRoZSBtb2R1bGVcblx0cmV0dXJuIG1vZHVsZS5leHBvcnRzO1xufVxuXG4iLCIvLyBnZXREZWZhdWx0RXhwb3J0IGZ1bmN0aW9uIGZvciBjb21wYXRpYmlsaXR5IHdpdGggbm9uLWhhcm1vbnkgbW9kdWxlc1xuX193ZWJwYWNrX3JlcXVpcmVfXy5uID0gKG1vZHVsZSkgPT4ge1xuXHR2YXIgZ2V0dGVyID0gbW9kdWxlICYmIG1vZHVsZS5fX2VzTW9kdWxlID9cblx0XHQoKSA9PiAobW9kdWxlWydkZWZhdWx0J10pIDpcblx0XHQoKSA9PiAobW9kdWxlKTtcblx0X193ZWJwYWNrX3JlcXVpcmVfXy5kKGdldHRlciwgeyBhOiBnZXR0ZXIgfSk7XG5cdHJldHVybiBnZXR0ZXI7XG59OyIsIi8vIGRlZmluZSBnZXR0ZXIgZnVuY3Rpb25zIGZvciBoYXJtb255IGV4cG9ydHNcbl9fd2VicGFja19yZXF1aXJlX18uZCA9IChleHBvcnRzLCBkZWZpbml0aW9uKSA9PiB7XG5cdGZvcih2YXIga2V5IGluIGRlZmluaXRpb24pIHtcblx0XHRpZihfX3dlYnBhY2tfcmVxdWlyZV9fLm8oZGVmaW5pdGlvbiwga2V5KSAmJiAhX193ZWJwYWNrX3JlcXVpcmVfXy5vKGV4cG9ydHMsIGtleSkpIHtcblx0XHRcdE9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBrZXksIHsgZW51bWVyYWJsZTogdHJ1ZSwgZ2V0OiBkZWZpbml0aW9uW2tleV0gfSk7XG5cdFx0fVxuXHR9XG59OyIsIl9fd2VicGFja19yZXF1aXJlX18ubyA9IChvYmosIHByb3ApID0+IChPYmplY3QucHJvdG90eXBlLmhhc093blByb3BlcnR5LmNhbGwob2JqLCBwcm9wKSkiLCIvLyBkZWZpbmUgX19lc01vZHVsZSBvbiBleHBvcnRzXG5fX3dlYnBhY2tfcmVxdWlyZV9fLnIgPSAoZXhwb3J0cykgPT4ge1xuXHRpZih0eXBlb2YgU3ltYm9sICE9PSAndW5kZWZpbmVkJyAmJiBTeW1ib2wudG9TdHJpbmdUYWcpIHtcblx0XHRPYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgU3ltYm9sLnRvU3RyaW5nVGFnLCB7IHZhbHVlOiAnTW9kdWxlJyB9KTtcblx0fVxuXHRPYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgJ19fZXNNb2R1bGUnLCB7IHZhbHVlOiB0cnVlIH0pO1xufTsiLCIvLyBAdHMtY2hlY2tcclxuaW1wb3J0IHtcclxuXHRDb21tYW5kRGlzcGF0Y2hlcixcclxuXHRSb290Q29tbWFuZE5vZGUsXHJcblx0bGl0ZXJhbCBhcyBsaXRlcmFsQXJndW1lbnQsXHJcblx0YXJndW1lbnQsXHJcblx0d29yZCBhcyBzaW5nbGVXb3JkQXJndW1lbnQsIC8vIEEgc2luZ2xlIHdvcmQgKGUuZy4gU3RyaW5nQXJndW1lbnQgb3IgdW5xdW90ZWQgc3RyaW5nKVxyXG5cdHN0cmluZyBhcyBzdHJpbmdBcmd1bWVudCwgLy8gQSBxdW90YWJsZSBwaHJhc2UgKGUuZy4gVGV4dEFyZ3VtZW50IG9yIFwic3RyaW5nXCIpXHJcblx0aW50ZWdlciBhcyBpbnRlZ2VyQXJndW1lbnQsXHJcblx0ZmxvYXQgYXMgZmxvYXRBcmd1bWVudCxcclxuXHRib29sIGFzIGJvb2xBcmd1bWVudCxcclxuXHRncmVlZHlTdHJpbmcgYXMgZ3JlZWR5U3RyaW5nQXJndW1lbnQsXHJcblx0TGl0ZXJhbEFyZ3VtZW50QnVpbGRlcixcclxuXHRQYXJzZVJlc3VsdHMsXHJcblx0Q29tbWFuZFN5bnRheEV4Y2VwdGlvbixcclxuXHRDb21tYW5kTm9kZSxcclxuXHRBcmd1bWVudFR5cGUgYXMgQnJpZ2FkaWVyQXJndW1lbnRUeXBlLFxyXG5cdEFyZ3VtZW50QnVpbGRlcixcclxuXHRTdWdnZXN0aW9uc1xyXG59IGZyb20gXCJub2RlLWJyaWdhZGllclwiXHJcblxyXG5pbXBvcnQge1xyXG5cdEJsb2NrUG9zQXJndW1lbnQsXHJcblx0UGxheWVyQXJndW1lbnQsXHJcblx0TXVsdGlMaXRlcmFsQXJndW1lbnQsXHJcblx0Q29sdW1uUG9zQXJndW1lbnQsXHJcblx0VGltZUFyZ3VtZW50LFxyXG5cdENvbG9yQXJndW1lbnQsXHJcblx0UG90aW9uRWZmZWN0QXJndW1lbnQsXHJcblx0QW5nbGVBcmd1bWVudCxcclxuXHRVVUlEQXJndW1lbnQsXHJcblx0RW50aXR5U2VsZWN0b3JBcmd1bWVudFxyXG59IGZyb20gXCIuL2FyZ3VtZW50c1wiXHJcblxyXG4vKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqXHJcbiAqIENsYXNzZXMgJiBJbnRlcmZhY2VzICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICpcclxuICoqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKi9cclxuXHJcbmNsYXNzIE15Q29tbWFuZERpc3BhdGNoZXI8Uz4gZXh0ZW5kcyBDb21tYW5kRGlzcGF0Y2hlcjxTPiB7XHJcblxyXG5cdHByaXZhdGUgcm9vdDogUm9vdENvbW1hbmROb2RlPFM+O1xyXG5cclxuXHRjb25zdHJ1Y3Rvcihyb290PzogUm9vdENvbW1hbmROb2RlPFM+KSB7XHJcblx0XHRzdXBlcihyb290KTtcclxuXHRcdHRoaXMucm9vdCA9IHJvb3Q7XHJcblx0fVxyXG5cclxuXHRwdWJsaWMgZGVsZXRlQWxsKCk6IHZvaWQge1xyXG5cdFx0dGhpcy5yb290ID0gbmV3IFJvb3RDb21tYW5kTm9kZSh1bmRlZmluZWQsIHVuZGVmaW5lZCwgdW5kZWZpbmVkLCB1bmRlZmluZWQsIHVuZGVmaW5lZCk7XHJcblx0fVxyXG5cclxuXHRwdWJsaWMgb3ZlcnJpZGUgZ2V0Um9vdCgpOiBSb290Q29tbWFuZE5vZGU8Uz4ge1xyXG5cdFx0cmV0dXJuIHRoaXMucm9vdDtcclxuXHR9XHJcblxyXG59XHJcblxyXG4vKipcclxuICogYFNlbGVjdGlvbi5tb2RpZnkoKWAgaXMgbm90IHBhcnQgb2YgW3RoZSBvZmZpY2lhbCBzcGVjXShodHRwczovL2RldmVsb3Blci5tb3ppbGxhLm9yZy9lbi1VUy9kb2NzL1dlYi9BUEkvU2VsZWN0aW9uL21vZGlmeSNzcGVjaWZpY2F0aW9ucyksXHJcbiAqIGJ1dCBpdCBleGlzdHMgaW4gYWxsIGJyb3dzZXJzLiBTZWUgW01pY3Jvc29mdC9UeXBlU2NyaXB0IzEyMjk2XShodHRwczovL2dpdGh1Yi5jb20vTWljcm9zb2Z0L1R5cGVTY3JpcHQvaXNzdWVzLzEyMjk2KVxyXG4gKi9cclxudHlwZSBTZWxlY3Rpb25XaXRoTW9kaWZ5ID0gU2VsZWN0aW9uICYge1xyXG5cdG1vZGlmeShzOiBzdHJpbmcsIHQ6IHN0cmluZywgdTogc3RyaW5nKTogdm9pZDtcclxufVxyXG5cclxuLy8gV2UgbmVlZCBhIFwiZmlsbGVyXCIgdHlwZSBmb3Igb3VyIGNvbW1hbmQgc291cmNlLiBTaW5jZSB3ZSdyZSBuZXZlciBhY3R1YWxseVxyXG4vLyB1c2luZyB0aGUgY29tbWFuZCBzb3VyY2UsIHdlJ2xsIGp1c3QgdXNlIGFuIEFEVCB3aXRoIG9uZSBlbnRyeVxyXG5cclxudHlwZSBTb3VyY2UgPSBuZXZlcjtcclxuY29uc3QgU09VUkNFOiBTb3VyY2UgPSB1bmRlZmluZWQgYXMgbmV2ZXI7XHJcblxyXG4vKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqXHJcbiAqIENvbnN0YW50cyAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICpcclxuICoqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKi9cclxuXHJcbmNvbnN0IENPTU1BTkRfSU5QVVQ6IEhUTUxTcGFuRWxlbWVudCA9IGRvY3VtZW50LmdldEVsZW1lbnRCeUlkKFwiY21kLWlucHV0XCIpO1xyXG5jb25zdCBDT01NQU5EX0lOUFVUX0FVVE9DT01QTEVURSA9IGRvY3VtZW50LmdldEVsZW1lbnRCeUlkKFwiY21kLWlucHV0LWF1dG9jb21wbGV0ZVwiKTtcclxuY29uc3QgRVJST1JfTUVTU0FHRV9CT1ggPSBkb2N1bWVudC5nZXRFbGVtZW50QnlJZChcImVycm9yLWJveFwiKTtcclxuY29uc3QgU1VHR0VTVElPTlNfQk9YID0gZG9jdW1lbnQuZ2V0RWxlbWVudEJ5SWQoXCJzdWdnZXN0aW9ucy1ib3hcIik7XHJcbmNvbnN0IFZBTElEX0JPWCA9IGRvY3VtZW50LmdldEVsZW1lbnRCeUlkKFwidmFsaWQtYm94XCIpO1xyXG5jb25zdCBDT01NQU5EUzogSFRNTFRleHRBcmVhRWxlbWVudCA9IGRvY3VtZW50LmdldEVsZW1lbnRCeUlkKFwiY29tbWFuZHNcIikgYXMgSFRNTFRleHRBcmVhRWxlbWVudDtcclxuXHJcbmNvbnN0IGRpc3BhdGNoZXIgPSBuZXcgTXlDb21tYW5kRGlzcGF0Y2hlcjxTb3VyY2U+KCk7XHJcblxyXG4vKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqXHJcbiAqIFByb3RvdHlwZXMgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICpcclxuICoqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKi9cclxuXHJcbi8vIEB0cy1pZ25vcmVcclxuLy8gQ29tbWFuZERpc3BhdGNoZXIucHJvdG90eXBlLmRlbGV0ZUFsbCA9IGZ1bmN0aW9uIGRlbGV0ZUFsbCgpIHsgdGhpcy5yb290ID0gbmV3IFJvb3RDb21tYW5kTm9kZSgpOyB9O1xyXG5cclxuLyoqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKlxyXG4gKiBFbnVtcyAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAqXHJcbiAqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKiovXHJcblxyXG5lbnVtIENoYXRDb2xvciB7XHJcblx0Ly8gVXNlcyB0aGUgc2VjdGlvbiBzeW1ib2wgKMKnKSwganVzdCBsaWtlIE1pbmVjcmFmdFxyXG5cdEJMQUNLID0gXCJcXHUwMEE3MFwiLFxyXG5cdERBUktfQkxVRSA9IFwiXFx1MDBBNzFcIixcclxuXHREQVJLX0dSRUVOID0gXCJcXHUwMEE3MlwiLFxyXG5cdERBUktfQVFVQSA9IFwiXFx1MDBBNzNcIixcclxuXHREQVJLX1JFRCA9IFwiXFx1MDBBNzRcIixcclxuXHREQVJLX1BVUlBMRSA9IFwiXFx1MDBBNzVcIixcclxuXHRHT0xEID0gXCJcXHUwMEE3NlwiLFxyXG5cdEdSQVkgPSBcIlxcdTAwQTc3XCIsXHJcblx0REFSS19HUkFZID0gXCJcXHUwMEE3OFwiLFxyXG5cdEJMVUUgPSBcIlxcdTAwQTc5XCIsXHJcblx0R1JFRU4gPSBcIlxcdTAwQTdhXCIsXHJcblx0QVFVQSA9IFwiXFx1MDBBN2JcIixcclxuXHRSRUQgPSBcIlxcdTAwQTdjXCIsXHJcblx0TElHSFRfUFVSUExFID0gXCJcXHUwMEE3ZFwiLFxyXG5cdFlFTExPVyA9IFwiXFx1MDBBN2VcIixcclxuXHRXSElURSA9IFwiXFx1MDBBN2ZcIlxyXG59O1xyXG5cclxuY29uc3QgQ2hhdENvbG9yQ1NTOiBNYXA8c3RyaW5nLCBzdHJpbmc+ID0gbmV3IE1hcChbXHJcblx0W1wiMFwiLCBcImJsYWNrXCJdLFxyXG5cdFtcIjFcIiwgXCJkYXJrX2JsdWVcIl0sXHJcblx0W1wiMlwiLCBcImRhcmtfZ3JlZW5cIl0sXHJcblx0W1wiM1wiLCBcImRhcmtfYXF1YVwiXSxcclxuXHRbXCI0XCIsIFwiZGFya19yZWRcIl0sXHJcblx0W1wiNVwiLCBcImRhcmtfcHVycGxlXCJdLFxyXG5cdFtcIjZcIiwgXCJnb2xkXCJdLFxyXG5cdFtcIjdcIiwgXCJncmF5XCJdLFxyXG5cdFtcIjhcIiwgXCJkYXJrX2dyYXlcIl0sXHJcblx0W1wiOVwiLCBcImJsdWVcIl0sXHJcblx0W1wiYVwiLCBcImdyZWVuXCJdLFxyXG5cdFtcImJcIiwgXCJhcXVhXCJdLFxyXG5cdFtcImNcIiwgXCJyZWRcIl0sXHJcblx0W1wiZFwiLCBcImxpZ2h0X3B1cnBsZVwiXSxcclxuXHRbXCJlXCIsIFwieWVsbG93XCJdLFxyXG5cdFtcImZcIiwgXCJ3aGl0ZVwiXVxyXG5dKTtcclxuXHJcbmNvbnN0IENoYXRDb2xvckNTU1JldmVyc2VkOiBNYXA8c3RyaW5nLCBzdHJpbmc+ID0gbmV3IE1hcCgpO1xyXG5mb3IgKGxldCBba2V5LCB2YWx1ZV0gb2YgQ2hhdENvbG9yQ1NTKSB7XHJcblx0Q2hhdENvbG9yQ1NTUmV2ZXJzZWQuc2V0KHZhbHVlLCBrZXkpO1xyXG59XHJcblxyXG5jb25zdCBBcmd1bWVudENvbG9yczogeyBbY29sb3JJbmRleDogbnVtYmVyXTogU3RyaW5nIH0gPSB7XHJcblx0MDogQ2hhdENvbG9yLkFRVUEsXHJcblx0MTogQ2hhdENvbG9yLllFTExPVyxcclxuXHQyOiBDaGF0Q29sb3IuR1JFRU4sXHJcblx0MzogQ2hhdENvbG9yLkxJR0hUX1BVUlBMRSxcclxuXHQ0OiBDaGF0Q29sb3IuR09MRFxyXG59IGFzIGNvbnN0O1xyXG5cclxuLy8gQXMgaW1wbGVtZW50ZWQgYnkgaHR0cHM6Ly9jb21tYW5kYXBpLmpvcmVsLmRldi84LjUuMS9pbnRlcm5hbC5odG1sXHJcbmNvbnN0IEFyZ3VtZW50VHlwZSA9IG5ldyBNYXA8U3RyaW5nLCAoKSA9PiBCcmlnYWRpZXJBcmd1bWVudFR5cGU8dW5rbm93bj4gfCBudWxsPihbXHJcblx0Ly8gQ29tbWFuZEFQSSBzZXBhcmF0aW9uLiBUaGVzZSBhcmUgdGhlIHZhcmlvdXMgRW50aXR5U2VsZWN0b3JBcmd1bWVudDw+IHR5cGVzXHJcblx0W1wiYXBpOmVudGl0eVwiLCAoKSA9PiBuZXcgRW50aXR5U2VsZWN0b3JBcmd1bWVudCgpXSxcclxuXHRbXCJhcGk6ZW50aXRpZXNcIiwgKCkgPT4gbmV3IEVudGl0eVNlbGVjdG9yQXJndW1lbnQoKV0sXHJcblx0W1wiYXBpOnBsYXllclwiLCAoKSA9PiBuZXcgRW50aXR5U2VsZWN0b3JBcmd1bWVudCgpXSxcclxuXHRbXCJhcGk6cGxheWVyc1wiLCAoKSA9PiBuZXcgRW50aXR5U2VsZWN0b3JBcmd1bWVudCgpXSxcclxuXHRbXCJhcGk6Z3JlZWR5X3N0cmluZ1wiLCAoKSA9PiBncmVlZHlTdHJpbmdBcmd1bWVudCgpXSxcclxuXHJcblx0Ly8gQSBub3RlIGFib3V0IEJyaWdhZGllciBTdHJpbmcgdHlwZXM6XHJcblxyXG5cdC8vIEJyaWdhZGllciBhcmd1bWVudHNcclxuXHRbXCJicmlnYWRpZXI6Ym9vbFwiLCAoKSA9PiBib29sQXJndW1lbnQoKV0sXHJcblx0W1wiYnJpZ2FkaWVyOmRvdWJsZVwiLCAoKSA9PiBmbG9hdEFyZ3VtZW50KCldLFxyXG5cdFtcImJyaWdhZGllcjpmbG9hdFwiLCAoKSA9PiBmbG9hdEFyZ3VtZW50KCldLFxyXG5cdFtcImJyaWdhZGllcjppbnRlZ2VyXCIsICgpID0+IGludGVnZXJBcmd1bWVudCgpXSxcclxuXHRbXCJicmlnYWRpZXI6bG9uZ1wiLCAoKSA9PiBpbnRlZ2VyQXJndW1lbnQoKV0sXHJcblx0W1wiYnJpZ2FkaWVyOnN0cmluZ1wiLCAoKSA9PiBzdHJpbmdBcmd1bWVudCgpXSxcclxuXHJcblx0Ly8gTWluZWNyYWZ0IGFyZ3VtZW50c1xyXG5cdFtcIm1pbmVjcmFmdDphbmdsZVwiLCAoKSA9PiBuZXcgQW5nbGVBcmd1bWVudCgpXSxcclxuXHRbXCJtaW5lY3JhZnQ6YmxvY2tfcG9zXCIsICgpID0+IG5ldyBCbG9ja1Bvc0FyZ3VtZW50KCldLFxyXG5cdFtcIm1pbmVjcmFmdDpibG9ja19wcmVkaWNhdGVcIiwgKCkgPT4gbnVsbF0sXHJcblx0W1wibWluZWNyYWZ0OmJsb2NrX3N0YXRlXCIsICgpID0+IG51bGxdLFxyXG5cdFtcIm1pbmVjcmFmdDpjb2xvclwiLCAoKSA9PiBuZXcgQ29sb3JBcmd1bWVudCgpXSxcclxuXHRbXCJtaW5lY3JhZnQ6Y29sdW1uX3Bvc1wiLCAoKSA9PiBuZXcgQ29sdW1uUG9zQXJndW1lbnQoKV0sXHJcblx0W1wibWluZWNyYWZ0OmNvbXBvbmVudFwiLCAoKSA9PiBudWxsXSxcclxuXHRbXCJtaW5lY3JhZnQ6ZGltZW5zaW9uXCIsICgpID0+IG51bGxdLFxyXG5cdFtcIm1pbmVjcmFmdDplbnRpdHlcIiwgKCkgPT4gbnVsbF0sXHJcblx0W1wibWluZWNyYWZ0OmVudGl0eV9hbmNob3JcIiwgKCkgPT4gbnVsbF0sXHJcblx0W1wibWluZWNyYWZ0OmVudGl0eV9zdW1tb25cIiwgKCkgPT4gbnVsbF0sXHJcblx0W1wibWluZWNyYWZ0OmZsb2F0X3JhbmdlXCIsICgpID0+IG51bGxdLFxyXG5cdFtcIm1pbmVjcmFmdDpmdW5jdGlvblwiLCAoKSA9PiBudWxsXSxcclxuXHRbXCJtaW5lY3JhZnQ6Z2FtZV9wcm9maWxlXCIsICgpID0+IG5ldyBQbGF5ZXJBcmd1bWVudCgpXSxcclxuXHRbXCJtaW5lY3JhZnQ6aW50X3JhbmdlXCIsICgpID0+IG51bGxdLFxyXG5cdFtcIm1pbmVjcmFmdDppdGVtX2VuY2hhbnRtZW50XCIsICgpID0+IG51bGxdLFxyXG5cdFtcIm1pbmVjcmFmdDppdGVtX3ByZWRpY2F0ZVwiLCAoKSA9PiBudWxsXSxcclxuXHRbXCJtaW5lY3JhZnQ6aXRlbV9zbG90XCIsICgpID0+IG51bGxdLFxyXG5cdFtcIm1pbmVjcmFmdDppdGVtX3N0YWNrXCIsICgpID0+IG51bGxdLFxyXG5cdFtcIm1pbmVjcmFmdDptZXNzYWdlXCIsICgpID0+IGdyZWVkeVN0cmluZ0FyZ3VtZW50KCldLCAvLyBDbG9zZSBlbm91Z2hcclxuXHRbXCJtaW5lY3JhZnQ6bW9iX2VmZmVjdFwiLCAoKSA9PiBuZXcgUG90aW9uRWZmZWN0QXJndW1lbnQoKV0sXHJcblx0W1wibWluZWNyYWZ0Om5idFwiLCAoKSA9PiBudWxsXSxcclxuXHRbXCJtaW5lY3JhZnQ6bmJ0X2NvbXBvdW5kX3RhZ1wiLCAoKSA9PiBudWxsXSxcclxuXHRbXCJtaW5lY3JhZnQ6bmJ0X3BhdGhcIiwgKCkgPT4gbnVsbF0sXHJcblx0W1wibWluZWNyYWZ0Om5idF90YWdcIiwgKCkgPT4gbnVsbF0sXHJcblx0W1wibWluZWNyYWZ0Om9iamVjdGl2ZVwiLCAoKSA9PiBudWxsXSxcclxuXHRbXCJtaW5lY3JhZnQ6b2JqZWN0aXZlX2NyaXRlcmlhXCIsICgpID0+IG51bGxdLFxyXG5cdFtcIm1pbmVjcmFmdDpvcGVyYXRpb25cIiwgKCkgPT4gbnVsbF0sXHJcblx0W1wibWluZWNyYWZ0OnBhcnRpY2xlXCIsICgpID0+IG51bGxdLFxyXG5cdFtcIm1pbmVjcmFmdDpyZXNvdXJjZV9sb2NhdGlvblwiLCAoKSA9PiBudWxsXSxcclxuXHRbXCJtaW5lY3JhZnQ6cm90YXRpb25cIiwgKCkgPT4gbnVsbF0sXHJcblx0W1wibWluZWNyYWZ0OnNjb3JlX2hvbGRlclwiLCAoKSA9PiBudWxsXSxcclxuXHRbXCJtaW5lY3JhZnQ6c2NvcmVib2FyZF9zbG90XCIsICgpID0+IG51bGxdLFxyXG5cdFtcIm1pbmVjcmFmdDpzd2l6emxlXCIsICgpID0+IG51bGxdLFxyXG5cdFtcIm1pbmVjcmFmdDp0ZWFtXCIsICgpID0+IHNpbmdsZVdvcmRBcmd1bWVudCgpXSxcclxuXHRbXCJtaW5lY3JhZnQ6dGltZVwiLCAoKSA9PiBuZXcgVGltZUFyZ3VtZW50KCldLFxyXG5cdFtcIm1pbmVjcmFmdDp1dWlkXCIsICgpID0+IG5ldyBVVUlEQXJndW1lbnQoKV0sXHJcblx0W1wibWluZWNyYWZ0OnZlYzJcIiwgKCkgPT4gbnVsbF0sXHJcblx0W1wibWluZWNyYWZ0OnZlYzNcIiwgKCkgPT4gbnVsbF0sXHJcbl0pO1xyXG5cclxuLyoqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKlxyXG4gKiBIZWxwZXJzICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAqXHJcbiAqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKiovXHJcblxyXG4vKipcclxuICogUmVnaXN0ZXJzIGEgY29tbWFuZCBpbnRvIHRoZSBnbG9iYWwgY29tbWFuZCBkaXNwYXRjaGVyXHJcbiAqIEBwYXJhbSB7c3RyaW5nfSBjb25maWdDb21tYW5kIHRoZSBjb21tYW5kIHRvIHJlZ2lzdGVyLCBhcyBkZWNsYXJlZCB1c2luZyB0aGVcclxuICogQ29tbWFuZEFQSSBjb25maWcueW1sJ3MgY29tbWFuZCBkZWNsYXJhdGlvbiBzeW50YXggKFNlZVxyXG4gKiBodHRwczovL2NvbW1hbmRhcGkuam9yZWwuZGV2LzguNS4xL2NvbnZlcnNpb25mb3Jvd25lcnNzaW5nbGVhcmdzLmh0bWwpXHJcbiAqL1xyXG5mdW5jdGlvbiByZWdpc3RlckNvbW1hbmQoY29uZmlnQ29tbWFuZDogc3RyaW5nKSB7XHJcblxyXG5cdC8vIE5vIGJsYW5rIGNvbW1hbmRzXHJcblx0aWYgKGNvbmZpZ0NvbW1hbmQudHJpbSgpLmxlbmd0aCA9PT0gMCkge1xyXG5cdFx0cmV0dXJuO1xyXG5cdH1cclxuXHJcblx0ZnVuY3Rpb24gY29udmVydEFyZ3VtZW50KGFyZ3VtZW50VHlwZTogc3RyaW5nKTogQnJpZ2FkaWVyQXJndW1lbnRUeXBlPHVua25vd24+IHtcclxuXHRcdGlmIChhcmd1bWVudFR5cGUuaW5jbHVkZXMoXCIuLlwiKSkge1xyXG5cdFx0XHRsZXQgbG93ZXJCb3VuZDogc3RyaW5nID0gYXJndW1lbnRUeXBlLnNwbGl0KFwiLi5cIilbMF07XHJcblx0XHRcdGxldCB1cHBlckJvdW5kOiBzdHJpbmcgPSBhcmd1bWVudFR5cGUuc3BsaXQoXCIuLlwiKVsxXTtcclxuXHJcblx0XHRcdGxldCBsb3dlckJvdW5kTnVtOiBudW1iZXIgPSBOdW1iZXIuTUlOX1NBRkVfSU5URUdFUjtcclxuXHRcdFx0bGV0IHVwcGVyQm91bmROdW06IG51bWJlciA9IE51bWJlci5NQVhfU0FGRV9JTlRFR0VSO1xyXG5cclxuXHRcdFx0aWYgKGxvd2VyQm91bmQubGVuZ3RoID09PSAwKSB7XHJcblx0XHRcdFx0bG93ZXJCb3VuZE51bSA9IE51bWJlci5NSU5fU0FGRV9JTlRFR0VSO1xyXG5cdFx0XHR9IGVsc2Uge1xyXG5cdFx0XHRcdGxvd2VyQm91bmROdW0gPSBOdW1iZXIucGFyc2VGbG9hdChsb3dlckJvdW5kKTtcclxuXHRcdFx0fVxyXG5cclxuXHRcdFx0aWYgKHVwcGVyQm91bmQubGVuZ3RoID09PSAwKSB7XHJcblx0XHRcdFx0dXBwZXJCb3VuZE51bSA9IE51bWJlci5NQVhfU0FGRV9JTlRFR0VSO1xyXG5cdFx0XHR9IGVsc2Uge1xyXG5cdFx0XHRcdHVwcGVyQm91bmROdW0gPSBOdW1iZXIucGFyc2VGbG9hdCh1cHBlckJvdW5kKTtcclxuXHRcdFx0fVxyXG5cclxuXHRcdFx0Ly8gV2UndmUgZ290IGEgZGVjaW1hbCBudW1iZXIsIHVzZSBhIGZsb2F0IGFyZ3VtZW50XHJcblx0XHRcdGlmIChsb3dlckJvdW5kTnVtICUgMSAhPT0gMCB8fCB1cHBlckJvdW5kTnVtICUgMSAhPT0gMCkge1xyXG5cdFx0XHRcdHJldHVybiBmbG9hdEFyZ3VtZW50KGxvd2VyQm91bmROdW0sIHVwcGVyQm91bmROdW0pO1xyXG5cdFx0XHR9IGVsc2Uge1xyXG5cdFx0XHRcdC8vIEluY2x1c2l2ZSB1cHBlciBib3VuZFxyXG5cdFx0XHRcdHVwcGVyQm91bmROdW0gKz0gMTtcclxuXHRcdFx0XHRyZXR1cm4gaW50ZWdlckFyZ3VtZW50KGxvd2VyQm91bmROdW0sIHVwcGVyQm91bmROdW0pO1xyXG5cdFx0XHR9XHJcblx0XHR9IGVsc2Uge1xyXG5cdFx0XHRjb25zdCBhcmd1bWVudEdlbmVyYXRvckZ1bmN0aW9uID0gQXJndW1lbnRUeXBlLmdldChhcmd1bWVudFR5cGUpO1xyXG5cdFx0XHRpZihhcmd1bWVudEdlbmVyYXRvckZ1bmN0aW9uID09PSBudWxsKSB7XHJcblx0XHRcdFx0Ly8gVE9ETzogRXJyb3IsIHRoaXMgYXJndW1lbnQgdHlwZSBkb2Vzbid0IGV4aXN0IVxyXG5cdFx0XHRcdGNvbnNvbGUuZXJyb3IoXCJBcmd1bWVudCB0eXBlIFwiICsgYXJndW1lbnRUeXBlICsgXCIgZG9lc24ndCBleGlzdFwiKTtcclxuXHRcdFx0fVxyXG5cdFx0XHRpZiAoYXJndW1lbnRHZW5lcmF0b3JGdW5jdGlvbigpKSB7XHJcblx0XHRcdFx0cmV0dXJuIGFyZ3VtZW50R2VuZXJhdG9yRnVuY3Rpb24oKTtcclxuXHRcdFx0fSBlbHNlIHtcclxuXHRcdFx0XHRjb25zb2xlLmVycm9yKFwiVW5pbXBsZW1lbnRlZCBhcmd1bWVudDogXCIgKyBhcmd1bWVudFR5cGUpO1xyXG5cdFx0XHRcdHJldHVybiBudWxsO1xyXG5cdFx0XHR9XHJcblx0XHR9XHJcblx0fVxyXG5cclxuXHRjb25zdCBjb21tYW5kOiBzdHJpbmcgPSBjb25maWdDb21tYW5kLnNwbGl0KFwiIFwiKVswXTtcclxuXHRjb25zdCBhcmdzOiBzdHJpbmdbXSA9IGNvbmZpZ0NvbW1hbmQuc3BsaXQoXCIgXCIpLnNsaWNlKDEpO1xyXG5cclxuXHRsZXQgY29tbWFuZFRvUmVnaXN0ZXI6IExpdGVyYWxBcmd1bWVudEJ1aWxkZXI8U291cmNlPiA9IGxpdGVyYWxBcmd1bWVudChjb21tYW5kKTtcclxuXHRsZXQgYXJndW1lbnRzVG9SZWdpc3RlcjogQXJyYXk8QXJndW1lbnRCdWlsZGVyPFNvdXJjZSwgYW55Pj4gPSBbXTtcclxuXHJcblx0Ly8gRnJvbSBkZXYvam9yZWwvY29tbWFuZGFwaS9BZHZhbmNlZENvbnZlcnRlci5qYXZhXHJcblx0Y29uc3QgbGl0ZXJhbFBhdHRlcm46IFJlZ0V4cCA9IFJlZ0V4cCgvXFwoKFxcdysoPzpcXHxcXHcrKSopXFwpLyk7XHJcblx0Y29uc3QgYXJndW1lbnRQYXR0ZXJuOiBSZWdFeHAgPSBSZWdFeHAoLzwoXFx3Kyk+XFxbKFthLXo6X10rfCg/OlswLTlcXC5dKyk/XFwuXFwuKD86WzAtOVxcLl0rKT8pXFxdLyk7XHJcblxyXG5cdGZvciAobGV0IGFyZyBvZiBhcmdzKSB7XHJcblx0XHRjb25zdCBtYXRjaGVkTGl0ZXJhbDogUmVnRXhwTWF0Y2hBcnJheSA9IGFyZy5tYXRjaChsaXRlcmFsUGF0dGVybik7XHJcblx0XHRjb25zdCBtYXRjaGVkQXJndW1lbnQ6IFJlZ0V4cE1hdGNoQXJyYXkgPSBhcmcubWF0Y2goYXJndW1lbnRQYXR0ZXJuKTtcclxuXHRcdGlmIChtYXRjaGVkTGl0ZXJhbCkge1xyXG5cdFx0XHQvLyBJdCdzIGEgbGl0ZXJhbCBhcmd1bWVudFxyXG5cdFx0XHRjb25zdCBsaXRlcmFsczogc3RyaW5nW10gPSBtYXRjaGVkTGl0ZXJhbFsxXS5zcGxpdChcInxcIik7XHJcblx0XHRcdGlmIChsaXRlcmFscy5sZW5ndGggPT09IDEpIHtcclxuXHRcdFx0XHRhcmd1bWVudHNUb1JlZ2lzdGVyLnVuc2hpZnQobGl0ZXJhbEFyZ3VtZW50KGxpdGVyYWxzWzBdKSk7XHJcblx0XHRcdH0gZWxzZSBpZiAobGl0ZXJhbHMubGVuZ3RoID4gMSkge1xyXG5cdFx0XHRcdGFyZ3VtZW50c1RvUmVnaXN0ZXIudW5zaGlmdChhcmd1bWVudChtYXRjaGVkTGl0ZXJhbFsxXSwgbmV3IE11bHRpTGl0ZXJhbEFyZ3VtZW50KGxpdGVyYWxzKSkpO1xyXG5cdFx0XHR9XHJcblx0XHR9IGVsc2UgaWYgKG1hdGNoZWRBcmd1bWVudCkge1xyXG5cdFx0XHQvLyBJdCdzIGEgcmVndWxhciBhcmd1bWVudFxyXG5cdFx0XHRjb25zdCBub2RlTmFtZTogc3RyaW5nID0gbWF0Y2hlZEFyZ3VtZW50WzFdO1xyXG5cdFx0XHRjb25zdCBhcmd1bWVudFR5cGU6IHN0cmluZyA9IG1hdGNoZWRBcmd1bWVudFsyXTtcclxuXHJcblx0XHRcdGxldCBjb252ZXJ0ZWRBcmd1bWVudFR5cGU6IEJyaWdhZGllckFyZ3VtZW50VHlwZTx1bmtub3duPiA9IGNvbnZlcnRBcmd1bWVudChhcmd1bWVudFR5cGUpO1xyXG5cclxuXHRcdFx0Ly8gV2UncmUgYWRkaW5nIGFyZ3VtZW50cyBpbiByZXZlcnNlIG9yZGVyIChsYXN0IGFyZ3VtZW50cyBhcHBlYXJcclxuXHRcdFx0Ly8gYXQgdGhlIGJlZ2lubmluZyBvZiB0aGUgYXJyYXkpIGJlY2F1c2UgaXQncyBtdWNoIG11Y2ggZWFzaWVyIHRvIHByb2Nlc3NcclxuXHRcdFx0YXJndW1lbnRzVG9SZWdpc3Rlci51bnNoaWZ0KGFyZ3VtZW50KG5vZGVOYW1lLCBjb252ZXJ0ZWRBcmd1bWVudFR5cGUpKTtcclxuXHRcdH1cclxuXHR9XHJcblxyXG5cdGlmIChhcmd1bWVudHNUb1JlZ2lzdGVyLmxlbmd0aCA+IDApIHtcclxuXHRcdGNvbnN0IGxhc3RBcmd1bWVudDogQnJpZ2FkaWVyQXJndW1lbnRUeXBlPHVua25vd24+ID0gYXJndW1lbnRzVG9SZWdpc3RlclswXS5leGVjdXRlcyhfY29udGV4dCA9PiAwKTtcclxuXHJcblx0XHQvLyBGbGFtZSBvbi4gUmVkdWNlLlxyXG5cdFx0YXJndW1lbnRzVG9SZWdpc3Rlci5zaGlmdCgpO1xyXG5cdFx0Y29uc3QgcmVkdWNlZEFyZ3VtZW50cyA9IGFyZ3VtZW50c1RvUmVnaXN0ZXIucmVkdWNlKChwcmV2OiBBcmd1bWVudEJ1aWxkZXI8U291cmNlLCBhbnk+LCBjdXJyZW50OiBBcmd1bWVudEJ1aWxkZXI8U291cmNlLCBhbnk+KSA9PiBjdXJyZW50LnRoZW4ocHJldiksIGxhc3RBcmd1bWVudCk7XHJcblx0XHRjb21tYW5kVG9SZWdpc3RlciA9IGNvbW1hbmRUb1JlZ2lzdGVyLnRoZW4ocmVkdWNlZEFyZ3VtZW50cyk7XHJcblx0fVxyXG5cclxuXHRkaXNwYXRjaGVyLnJlZ2lzdGVyKGNvbW1hbmRUb1JlZ2lzdGVyKTtcclxuXHQvLyBwbHVnaW5zLXRvLWNvbnZlcnQ6XHJcblx0Ly8gICAtIEVzc2VudGlhbHM6XHJcblx0Ly8gICAgIC0gc3BlZWQgPHNwZWVkPlswLi4xMF1cclxuXHQvLyAgICAgLSBzcGVlZCA8dGFyZ2V0PlttaW5lY3JhZnQ6Z2FtZV9wcm9maWxlXVxyXG5cdC8vICAgICAtIHNwZWVkICh3YWxrfGZseSkgPHNwZWVkPlswLi4xMF1cclxuXHQvLyAgICAgLSBzcGVlZCAod2Fsa3xmbHkpIDxzcGVlZD5bMC4uMTBdIDx0YXJnZXQ+W21pbmVjcmFmdDpnYW1lX3Byb2ZpbGVdXHJcbn1cclxuXHJcbi8qKlxyXG4gKiBHZXRzIHRoZSBjdXJyZW50IGN1cnNvciBwb3NpdGlvbi5cclxuICpcclxuICogRnJvbSBodHRwczovL3RoZXdlYmRldi5pbmZvLzIwMjIvMDQvMjQvaG93LXRvLWdldC1jb250ZW50ZWRpdGFibGUtY2FyZXQtcG9zaXRpb24td2l0aC1qYXZhc2NyaXB0L1xyXG4gKiBAcmV0dXJucyBUaGUgY3VycmVudCBjdXJzb3IgcG9zaXRpb24gZm9yIHRoZSBjdXJyZW50IGVsZW1lbnRcclxuICovXHJcbmZ1bmN0aW9uIGdldEN1cnNvclBvc2l0aW9uKCkge1xyXG5cdGNvbnN0IHNlbDogU2VsZWN0aW9uV2l0aE1vZGlmeSA9IGRvY3VtZW50LmdldFNlbGVjdGlvbigpIGFzIFNlbGVjdGlvbldpdGhNb2RpZnk7XHJcblx0c2VsLm1vZGlmeShcImV4dGVuZFwiLCBcImJhY2t3YXJkXCIsIFwicGFyYWdyYXBoYm91bmRhcnlcIik7XHJcblx0Y29uc3QgcG9zID0gc2VsLnRvU3RyaW5nKCkubGVuZ3RoO1xyXG5cdGlmIChzZWwuYW5jaG9yTm9kZSAhPT0gdW5kZWZpbmVkICYmIHNlbC5hbmNob3JOb2RlICE9PSBudWxsKSB7XHJcblx0XHRzZWwuY29sbGFwc2VUb0VuZCgpO1xyXG5cdH1cclxuXHRyZXR1cm4gcG9zO1xyXG59O1xyXG5cclxuLyoqXHJcbiAqIFNldHMgdGhlIGN1cnJlbnQgY3Vyc29yIHBvc2l0aW9uLiBUaGlzIGhhcyB0byB0YWtlIGludG8gYWNjb3VudCB0aGUgZmFjdFxyXG4gKiB0aGF0IHRoZSBjdXJyZW50IGVsZW1lbnQgaXMgbWFkZSB1cCBvZiBtYW55IEhUTUwgZWxlbWVudHMgaW5zdGVhZCBvZlxyXG4gKiBwbGFpbnRleHQsIHNvIHNlbGVjdGlvbiByYW5nZXMgaGF2ZSB0byBzcGFuIGFjcm9zcyB0aG9zZSBlbGVtZW50cyB0byBmaW5kXHJcbiAqIHRoZSBleGFjdCBsb2NhdGlvbiB5b3UncmUgbG9va2luZyBmb3IuXHJcbiAqXHJcbiAqIEZyb20gaHR0cHM6Ly9zdGFja292ZXJmbG93LmNvbS9hLzQxMDM0Njk3LzQ3NzkwNzFcclxuICogQHBhcmFtIGluZGV4IHRoZSBudW1iZXIgb2YgY2hhcmFjdGVycyBpbnRvIHRoZSBjdXJyZW50IGVsZW1lbnRcclxuICogICAgICAgICAgICAgICAgICAgICAgIHRvIHBsYWNlIHRoZSBjdXJzb3IgYXRcclxuICogQHBhcmFtIGVsZW1lbnQgdGhlIGVsZW1lbnQgdG8gc2V0IHRoZSBjdXJzb3IgZm9yXHJcbiAqL1xyXG5mdW5jdGlvbiBzZXRDdXJzb3JQb3NpdGlvbihpbmRleDogbnVtYmVyLCBlbGVtZW50OiBOb2RlKTogdm9pZCB7XHJcblx0aWYgKGluZGV4ID49IDApIHtcclxuXHRcdGNvbnN0IGNyZWF0ZVJhbmdlID0gKG5vZGU6IE5vZGUsIGNoYXJzOiB7IGNvdW50OiBudW1iZXIgfSwgcmFuZ2U/OiBSYW5nZSk6IFJhbmdlID0+IHtcclxuXHRcdFx0aWYgKCFyYW5nZSkge1xyXG5cdFx0XHRcdHJhbmdlID0gZG9jdW1lbnQuY3JlYXRlUmFuZ2UoKTtcclxuXHRcdFx0XHRyYW5nZS5zZWxlY3ROb2RlKG5vZGUpO1xyXG5cdFx0XHRcdHJhbmdlLnNldFN0YXJ0KG5vZGUsIDApO1xyXG5cdFx0XHR9XHJcblxyXG5cdFx0XHRpZiAoY2hhcnMuY291bnQgPT09IDApIHtcclxuXHRcdFx0XHRyYW5nZS5zZXRFbmQobm9kZSwgY2hhcnMuY291bnQpO1xyXG5cdFx0XHR9IGVsc2UgaWYgKG5vZGUgJiYgY2hhcnMuY291bnQgPiAwKSB7XHJcblx0XHRcdFx0aWYgKG5vZGUubm9kZVR5cGUgPT09IE5vZGUuVEVYVF9OT0RFKSB7XHJcblx0XHRcdFx0XHRjb25zdCBub2RlVGV4dENvbnRlbnRMZW5ndGg6IG51bWJlciA9IG5vZGUudGV4dENvbnRlbnQubGVuZ3RoO1xyXG5cdFx0XHRcdFx0aWYgKG5vZGVUZXh0Q29udGVudExlbmd0aCA8IGNoYXJzLmNvdW50KSB7XHJcblx0XHRcdFx0XHRcdGNoYXJzLmNvdW50IC09IG5vZGVUZXh0Q29udGVudExlbmd0aDtcclxuXHRcdFx0XHRcdH0gZWxzZSB7XHJcblx0XHRcdFx0XHRcdHJhbmdlLnNldEVuZChub2RlLCBjaGFycy5jb3VudCk7XHJcblx0XHRcdFx0XHRcdGNoYXJzLmNvdW50ID0gMDtcclxuXHRcdFx0XHRcdH1cclxuXHRcdFx0XHR9IGVsc2Uge1xyXG5cdFx0XHRcdFx0Zm9yIChsZXQgbHA6IG51bWJlciA9IDA7IGxwIDwgbm9kZS5jaGlsZE5vZGVzLmxlbmd0aDsgbHArKykge1xyXG5cdFx0XHRcdFx0XHRyYW5nZSA9IGNyZWF0ZVJhbmdlKG5vZGUuY2hpbGROb2Rlc1tscF0sIGNoYXJzLCByYW5nZSk7XHJcblxyXG5cdFx0XHRcdFx0XHRpZiAoY2hhcnMuY291bnQgPT09IDApIHtcclxuXHRcdFx0XHRcdFx0XHRicmVhaztcclxuXHRcdFx0XHRcdFx0fVxyXG5cdFx0XHRcdFx0fVxyXG5cdFx0XHRcdH1cclxuXHRcdFx0fVxyXG5cclxuXHRcdFx0cmV0dXJuIHJhbmdlO1xyXG5cdFx0fTtcclxuXHJcblx0XHQvLyBXZSB3cmFwIGluZGV4IGluIGFuIG9iamVjdCBzbyB0aGF0IHJlY3Vyc2l2ZSBjYWxscyBjYW4gdXNlIHRoZVxyXG5cdFx0Ly8gXCJuZXdcIiB2YWx1ZSB3aGljaCBpcyB1cGRhdGVkIGluc2lkZSB0aGUgb2JqZWN0IGl0c2VsZlxyXG5cdFx0bGV0IHJhbmdlOiBSYW5nZSA9IGNyZWF0ZVJhbmdlKGVsZW1lbnQsIHsgY291bnQ6IGluZGV4IH0pO1xyXG5cclxuXHRcdGlmIChyYW5nZSkge1xyXG5cdFx0XHRyYW5nZS5jb2xsYXBzZShmYWxzZSk7XHJcblx0XHRcdGxldCBzZWxlY3Rpb246IFNlbGVjdGlvbiA9IHdpbmRvdy5nZXRTZWxlY3Rpb24oKTtcclxuXHRcdFx0c2VsZWN0aW9uLnJlbW92ZUFsbFJhbmdlcygpO1xyXG5cdFx0XHRzZWxlY3Rpb24uYWRkUmFuZ2UocmFuZ2UpO1xyXG5cdFx0fVxyXG5cdH1cclxufTtcclxuXHJcbmZ1bmN0aW9uIGdldFNlbGVjdGVkU3VnZ2VzdGlvbigpOiBIVE1MRWxlbWVudCB7XHJcblx0cmV0dXJuIGRvY3VtZW50LnF1ZXJ5U2VsZWN0b3IoXCIueWVsbG93XCIpO1xyXG59XHJcblxyXG50eXBlIENhY2hlZEZvbnRIVE1MRWxlbWVudCA9IEhUTUxFbGVtZW50ICYgeyBjdXJyZW50Rm9udDogc3RyaW5nIH1cclxuXHJcbmNsYXNzIFRleHRXaWR0aCB7XHJcblxyXG5cdHByaXZhdGUgc3RhdGljIGNhbnZhczogSFRNTENhbnZhc0VsZW1lbnQ7XHJcblxyXG5cdC8qKlxyXG5cdCAqIFVzZXMgY2FudmFzLm1lYXN1cmVUZXh0IHRvIGNvbXB1dGUgYW5kIHJldHVybiB0aGUgd2lkdGggb2YgdGhlIGdpdmVuIHRleHQgb2YgZ2l2ZW4gZm9udCBpbiBwaXhlbHMuXHJcblx0ICogXHJcblx0ICogQHBhcmFtIHtTdHJpbmd9IHRleHQgVGhlIHRleHQgdG8gYmUgcmVuZGVyZWQuXHJcblx0ICogQHBhcmFtIHtIVE1MRWxlbWVudH0gZWxlbWVudCB0aGUgZWxlbWVudFxyXG5cdCAqIFxyXG5cdCAqIEBzZWUgaHR0cHM6Ly9zdGFja292ZXJmbG93LmNvbS9xdWVzdGlvbnMvMTE4MjQxL2NhbGN1bGF0ZS10ZXh0LXdpZHRoLXdpdGgtamF2YXNjcmlwdC8yMTAxNTM5MyMyMTAxNTM5M1xyXG5cdCAqL1xyXG5cdHN0YXRpYyBnZXRUZXh0V2lkdGgodGV4dDogc3RyaW5nLCBlbGVtZW50OiBDYWNoZWRGb250SFRNTEVsZW1lbnQpOiBudW1iZXIge1xyXG5cdFx0Ly8gcmUtdXNlIGNhbnZhcyBvYmplY3QgZm9yIGJldHRlciBwZXJmb3JtYW5jZVxyXG5cdFx0Y29uc3QgY2FudmFzOiBIVE1MQ2FudmFzRWxlbWVudCA9IFRleHRXaWR0aC5jYW52YXMgfHwgKFRleHRXaWR0aC5jYW52YXMgPSBkb2N1bWVudC5jcmVhdGVFbGVtZW50KFwiY2FudmFzXCIpKTtcclxuXHRcdGNvbnN0IGNvbnRleHQ6IENhbnZhc1JlbmRlcmluZ0NvbnRleHQyRCA9IGNhbnZhcy5nZXRDb250ZXh0KFwiMmRcIik7XHJcblxyXG5cdFx0Y29udGV4dC5mb250ID0gZWxlbWVudC5jdXJyZW50Rm9udCB8fCAoZWxlbWVudC5jdXJyZW50Rm9udCA9IFRleHRXaWR0aC5nZXRDYW52YXNGb250KGVsZW1lbnQpKTtcclxuXHRcdHJldHVybiBjb250ZXh0Lm1lYXN1cmVUZXh0KHRleHQpLndpZHRoO1xyXG5cdH1cclxuXHJcblx0cHJpdmF0ZSBzdGF0aWMgZ2V0Q3NzU3R5bGUoZWxlbWVudDogSFRNTEVsZW1lbnQsIHByb3A6IHN0cmluZyk6IHN0cmluZyB7XHJcblx0XHRyZXR1cm4gd2luZG93LmdldENvbXB1dGVkU3R5bGUoZWxlbWVudCkuZ2V0UHJvcGVydHlWYWx1ZShwcm9wKTtcclxuXHR9XHJcblxyXG5cdHByaXZhdGUgc3RhdGljIGdldENhbnZhc0ZvbnQoZWw6IEhUTUxFbGVtZW50ID0gZG9jdW1lbnQuYm9keSk6IHN0cmluZyB7XHJcblx0XHRjb25zdCBmb250V2VpZ2h0ID0gVGV4dFdpZHRoLmdldENzc1N0eWxlKGVsLCAnZm9udC13ZWlnaHQnKSB8fCAnbm9ybWFsJztcclxuXHRcdGNvbnN0IGZvbnRTaXplID0gVGV4dFdpZHRoLmdldENzc1N0eWxlKGVsLCAnZm9udC1zaXplJykgfHwgJzE2cHgnO1xyXG5cdFx0Y29uc3QgZm9udEZhbWlseSA9IFRleHRXaWR0aC5nZXRDc3NTdHlsZShlbCwgJ2ZvbnQtZmFtaWx5JykgfHwgJ1RpbWVzIE5ldyBSb21hbic7XHJcblxyXG5cdFx0cmV0dXJuIGAke2ZvbnRXZWlnaHR9ICR7Zm9udFNpemV9ICR7Zm9udEZhbWlseX1gO1xyXG5cdH1cclxuXHJcbn1cclxuXHJcbi8qKlxyXG4gKiBUYWtlcyBNaW5lY3JhZnQgdGV4dCBhbmQgcmVuZGVycyBpdCBpbiB0aGUgY2hhdCBib3guIFRoaXMgd2lsbCBhdXRvbWF0aWNhbGx5XHJcbiAqIGFkZCB0aGUgbGVhZGluZyAvIGNoYXJhY3Rlciwgc28geW91IGRvbid0IGhhdmUgdG8gZG8gdGhhdCB5b3Vyc2VsZiFcclxuICogQHBhcmFtIHtzdHJpbmd9IG1pbmVjcmFmdENvZGVkVGV4dFxyXG4gKiBAcGFyYW0ge0hUTUxFbGVtZW50IHwgbnVsbH0gdGFyZ2V0XHJcbiAqL1xyXG5mdW5jdGlvbiBzZXRUZXh0KG1pbmVjcmFmdENvZGVkVGV4dDogc3RyaW5nLCB0YXJnZXQ6IEhUTUxFbGVtZW50ID0gbnVsbCkge1xyXG5cdG1pbmVjcmFmdENvZGVkVGV4dCA9IG1pbmVjcmFmdENvZGVkVGV4dC5yZXBsYWNlQWxsKFwiIFwiLCBcIlxcdTAwQTBcIik7IC8vIFJlcGxhY2Ugbm9ybWFsIHNwYWNlcyB3aXRoICZuYnNwOyBmb3IgSFRNTFxyXG5cdGlmICghdGFyZ2V0KSB7XHJcblx0XHR0YXJnZXQgPSBDT01NQU5EX0lOUFVUO1xyXG5cdH1cclxuXHJcblx0Ly8gUmVzZXQgdGhlIHRleHRcclxuXHR0YXJnZXQuaW5uZXJIVE1MID0gXCJcIjtcclxuXHJcblx0aWYgKHRhcmdldCA9PT0gQ09NTUFORF9JTlBVVCkge1xyXG5cdFx0Ly8gQ29tbWFuZCBmb3J3YXJkIHNsYXNoLiBBbHdheXMgcHJlc2VudCwgd2UgZG9uJ3Qgd2FudCB0byByZW1vdmUgdGhpcyFcclxuXHRcdGxldCBlbGVtZW50OiBIVE1MU3BhbkVsZW1lbnQgPSBkb2N1bWVudC5jcmVhdGVFbGVtZW50KFwic3BhblwiKTtcclxuXHRcdGVsZW1lbnQuaW5uZXJUZXh0ID0gXCIvXCI7XHJcblx0XHR0YXJnZXQuYXBwZW5kQ2hpbGQoZWxlbWVudCk7XHJcblx0fVxyXG5cclxuXHRsZXQgYnVmZmVyOiBzdHJpbmcgPSBcIlwiO1xyXG5cdGxldCBjdXJyZW50Q29sb3I6IHN0cmluZyA9IFwiXCI7XHJcblxyXG5cdGZ1bmN0aW9uIHdyaXRlQnVmZmVyKHRhcmdldDogSFRNTEVsZW1lbnQpOiB2b2lkIHtcclxuXHRcdGlmIChidWZmZXIubGVuZ3RoID4gMCkge1xyXG5cdFx0XHRsZXQgZWxlbTogSFRNTFNwYW5FbGVtZW50ID0gZG9jdW1lbnQuY3JlYXRlRWxlbWVudChcInNwYW5cIik7XHJcblx0XHRcdGVsZW0uY2xhc3NOYW1lID0gY3VycmVudENvbG9yO1xyXG5cdFx0XHRlbGVtLmlubmVyVGV4dCA9IGJ1ZmZlcjtcclxuXHRcdFx0dGFyZ2V0LmFwcGVuZENoaWxkKGVsZW0pO1xyXG5cdFx0XHRidWZmZXIgPSBcIlwiO1xyXG5cdFx0fVxyXG5cdH07XHJcblxyXG5cdGZvciAobGV0IGk6IG51bWJlciA9IDA7IGkgPCBtaW5lY3JhZnRDb2RlZFRleHQubGVuZ3RoOyBpKyspIHtcclxuXHRcdGlmIChtaW5lY3JhZnRDb2RlZFRleHRbaV0gPT09IFwiXFx1MDBBN1wiKSB7XHJcblx0XHRcdHdyaXRlQnVmZmVyKHRhcmdldCk7XHJcblx0XHRcdGN1cnJlbnRDb2xvciA9IENoYXRDb2xvckNTUy5nZXQobWluZWNyYWZ0Q29kZWRUZXh0W2kgKyAxXSk7XHJcblx0XHRcdGkrKztcclxuXHRcdFx0Y29udGludWU7XHJcblx0XHR9IGVsc2Uge1xyXG5cdFx0XHRidWZmZXIgKz0gbWluZWNyYWZ0Q29kZWRUZXh0W2ldO1xyXG5cdFx0fVxyXG5cdH1cclxuXHJcblx0d3JpdGVCdWZmZXIodGFyZ2V0KTtcclxufVxyXG5cclxuZnVuY3Rpb24gZ2V0VGV4dCh3aXRoU3R5bGluZzogYm9vbGVhbiA9IHRydWUpOiBzdHJpbmcge1xyXG5cdGxldCBidWZmZXI6IHN0cmluZyA9IFwiXCI7XHJcblx0Zm9yIChsZXQgY2hpbGQgb2YgQ09NTUFORF9JTlBVVC5jaGlsZHJlbikge1xyXG5cdFx0aWYgKGNoaWxkLmNsYXNzTmFtZSAmJiB3aXRoU3R5bGluZykge1xyXG5cdFx0XHRidWZmZXIgKz0gXCJcXHUwMEE3XCIgKyBDaGF0Q29sb3JDU1NSZXZlcnNlZC5nZXQoY2hpbGQuY2xhc3NOYW1lKTtcclxuXHRcdH1cclxuXHRcdGJ1ZmZlciArPSAoY2hpbGQgYXMgSFRNTEVsZW1lbnQpLmlubmVyVGV4dDtcclxuXHR9XHJcblx0cmV0dXJuIGJ1ZmZlcjtcclxufVxyXG5cclxuLyoqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKlxyXG4gKiBFdmVudHMgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAqXHJcbiAqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKiovXHJcblxyXG5DT01NQU5EX0lOUFVULm9uaW5wdXQgPSBhc3luYyBmdW5jdGlvbiBvbkNvbW1hbmRJbnB1dCgpOiBQcm9taXNlPHZvaWQ+IHtcclxuXHRsZXQgY3Vyc29yUG9zOiBudW1iZXIgPSBnZXRDdXJzb3JQb3NpdGlvbigpO1xyXG5cclxuXHRsZXQgcmF3VGV4dDogc3RyaW5nID0gQ09NTUFORF9JTlBVVC5pbm5lclRleHQucmVwbGFjZShcIlxcblwiLCBcIlwiKTtcclxuXHRyYXdUZXh0ID0gcmF3VGV4dC5yZXBsYWNlQWxsKFwiXFx1MDBhMFwiLCBcIiBcIik7IC8vIFJlcGxhY2UgJm5ic3A7IHdpdGggbm9ybWFsIHNwYWNlcyBmb3IgQnJpZ2FkaWVyXHJcblxyXG5cdGxldCBzaG93VXNhZ2VUZXh0OiBib29sZWFuID0gZmFsc2U7XHJcblx0bGV0IGVycm9yVGV4dDogc3RyaW5nID0gXCJcIjtcclxuXHRsZXQgc3VnZ2VzdGlvbnM6IHN0cmluZ1tdID0gW107XHJcblx0bGV0IGNvbW1hbmRWYWxpZDogYm9vbGVhbiA9IGZhbHNlO1xyXG5cclxuXHQvLyBSZW5kZXIgY29sb3JzXHJcblx0aWYgKHJhd1RleHQuc3RhcnRzV2l0aChcIi9cIikpIHtcclxuXHRcdC8vIFBhcnNlIHRoZSByYXcgdGV4dFxyXG5cdFx0Y29uc3QgcmF3VGV4dE5vU2xhc2g6IHN0cmluZyA9IHJhd1RleHQuc2xpY2UoMSk7XHJcblx0XHRjb25zdCBjb21tYW5kOiBzdHJpbmcgPSByYXdUZXh0Tm9TbGFzaC5zcGxpdChcIiBcIilbMF07XHJcblxyXG5cdFx0Ly8gQnJpZ2FkaWVyXHJcblx0XHRjb25zdCBwYXJzZWRDb21tYW5kOiBQYXJzZVJlc3VsdHM8U291cmNlPiA9IGRpc3BhdGNoZXIucGFyc2UocmF3VGV4dE5vU2xhc2gsIFNPVVJDRSk7XHJcblx0XHRjb25zdCBwYXJzZWRDb21tYW5kTm9UcmFpbGluZzogUGFyc2VSZXN1bHRzPFNvdXJjZT4gPSBkaXNwYXRjaGVyLnBhcnNlKHJhd1RleHROb1NsYXNoLnRyaW1FbmQoKSwgU09VUkNFKTtcclxuXHRcdGNvbnNvbGUubG9nKHBhcnNlZENvbW1hbmQpO1xyXG5cclxuXHRcdGxldCBsYXN0Tm9kZTogQ29tbWFuZE5vZGU8U291cmNlPiA9IHBhcnNlZENvbW1hbmROb1RyYWlsaW5nLmdldENvbnRleHQoKS5nZXRSb290Tm9kZSgpO1xyXG5cdFx0aWYgKHBhcnNlZENvbW1hbmROb1RyYWlsaW5nLmdldENvbnRleHQoKS5nZXROb2RlcygpLmxlbmd0aCA+IDApIHtcclxuXHRcdFx0bGFzdE5vZGUgPSBwYXJzZWRDb21tYW5kTm9UcmFpbGluZy5nZXRDb250ZXh0KCkuZ2V0Tm9kZXMoKVtwYXJzZWRDb21tYW5kTm9UcmFpbGluZy5nZXRDb250ZXh0KCkuZ2V0Tm9kZXMoKS5sZW5ndGggLSAxXS5nZXROb2RlKCk7XHJcblx0XHR9XHJcblx0XHRjb25zdCB1c2FnZTogc3RyaW5nID0gZGlzcGF0Y2hlci5nZXRBbGxVc2FnZShsYXN0Tm9kZSwgU09VUkNFLCBmYWxzZSkuam9pbihcIiBcIik7XHJcblxyXG5cdFx0Ly8gUmVzZXQgdGV4dFxyXG5cdFx0c2V0VGV4dChyYXdUZXh0Tm9TbGFzaCk7XHJcblxyXG5cdFx0aWYgKHBhcnNlZENvbW1hbmQuZ2V0RXhjZXB0aW9ucygpLnNpemUgPiAwKSB7XHJcblx0XHRcdC8vIFRoZSBjb21tYW5kIGlzIGludmFsaWQgKHRoZSBjb21tYW5kIGRvZXNuJ3QgZXhpc3QpLiBNYWtlIHRoZSB3aG9sZSB0ZXh0IHJlZC5cclxuXHRcdFx0c2V0VGV4dChDaGF0Q29sb3IuUkVEICsgcmF3VGV4dE5vU2xhc2gpO1xyXG5cclxuXHRcdFx0Y29uc3QgZXhjZXB0aW9uczogTWFwPENvbW1hbmROb2RlPFNvdXJjZT4sIENvbW1hbmRTeW50YXhFeGNlcHRpb24+ID0gcGFyc2VkQ29tbWFuZC5nZXRFeGNlcHRpb25zKCk7XHJcblx0XHRcdGVycm9yVGV4dCA9IGV4Y2VwdGlvbnMuZW50cmllcygpLm5leHQoKS52YWx1ZVsxXS5tZXNzYWdlO1xyXG5cdFx0fSBlbHNlIHtcclxuXHRcdFx0Ly8gQnJpZ2FkaWVyIGlzIFwiaGFwcHlcIiB3aXRoIHRoZSBpbnB1dC4gTGV0J3MgcnVuIGl0IGFuZCBzZWUhXHJcblx0XHRcdHRyeSB7XHJcblx0XHRcdFx0ZGlzcGF0Y2hlci5leGVjdXRlKHBhcnNlZENvbW1hbmQpO1xyXG5cdFx0XHR9IGNhdGNoIChleCkge1xyXG5cdFx0XHRcdHNldFRleHQoQ2hhdENvbG9yLlJFRCArIHJhd1RleHROb1NsYXNoKTtcclxuXHRcdFx0XHRlcnJvclRleHQgPSBleC5tZXNzYWdlO1xyXG5cclxuXHRcdFx0XHQvLyBUT0RPOiBXZSBhY3R1YWxseSBuZWVkIHRvIHRha2UgaW50byBhY2NvdW50IHRoZSBjYXNlIHdoZW4gdGhlXHJcblx0XHRcdFx0Ly8gY29tbWFuZCBJUyBBQ1RVQUxMWSB1bmtub3duIVxyXG5cdFx0XHRcdGlmIChlcnJvclRleHQuc3RhcnRzV2l0aChcIlVua25vd24gY29tbWFuZCBhdCBwb3NpdGlvblwiKSkge1xyXG5cdFx0XHRcdFx0ZXJyb3JUZXh0ID0gdXNhZ2U7XHJcblx0XHRcdFx0XHRzaG93VXNhZ2VUZXh0ID0gdHJ1ZTtcclxuXHRcdFx0XHR9XHJcblx0XHRcdH1cclxuXHJcblx0XHRcdGlmIChlcnJvclRleHQgPT09IFwiXCIpIHtcclxuXHRcdFx0XHRjb21tYW5kVmFsaWQgPSB0cnVlO1xyXG5cdFx0XHR9XHJcblx0XHR9XHJcblxyXG5cdFx0Ly8gQ29sb3JpemUgZXhpc3RpbmcgYXJndW1lbnRzXHJcblx0XHRpZiAoc2hvd1VzYWdlVGV4dCB8fCBjb21tYW5kVmFsaWQpIHtcclxuXHRcdFx0bGV0IG5ld1RleHQ6IHN0cmluZyA9IGNvbW1hbmQ7XHJcblx0XHRcdGxldCBwYXJzZWRBcmd1bWVudEluZGV4OiBudW1iZXIgPSAwO1xyXG5cdFx0XHRmb3IgKGNvbnN0IFtfa2V5LCB2YWx1ZV0gb2YgcGFyc2VkQ29tbWFuZC5nZXRDb250ZXh0KCkuZ2V0QXJndW1lbnRzKCkpIHtcclxuXHRcdFx0XHRpZiAocGFyc2VkQXJndW1lbnRJbmRleCA+IE9iamVjdC5rZXlzKEFyZ3VtZW50Q29sb3JzKS5sZW5ndGgpIHtcclxuXHRcdFx0XHRcdHBhcnNlZEFyZ3VtZW50SW5kZXggPSAwO1xyXG5cdFx0XHRcdH1cclxuXHJcblx0XHRcdFx0bmV3VGV4dCArPSBcIiBcIjtcclxuXHRcdFx0XHRuZXdUZXh0ICs9IEFyZ3VtZW50Q29sb3JzW3BhcnNlZEFyZ3VtZW50SW5kZXhdO1xyXG5cdFx0XHRcdG5ld1RleHQgKz0gcmF3VGV4dE5vU2xhc2guc2xpY2UodmFsdWUuZ2V0UmFuZ2UoKS5nZXRTdGFydCgpLCB2YWx1ZS5nZXRSYW5nZSgpLmdldEVuZCgpKTtcclxuXHJcblx0XHRcdFx0cGFyc2VkQXJndW1lbnRJbmRleCsrO1xyXG5cdFx0XHR9XHJcblx0XHRcdG5ld1RleHQgKz0gXCJcIi5wYWRFbmQocmF3VGV4dE5vU2xhc2gubGVuZ3RoIC0gcmF3VGV4dE5vU2xhc2gudHJpbUVuZCgpLmxlbmd0aCk7XHJcblx0XHRcdHNldFRleHQobmV3VGV4dCk7XHJcblx0XHR9XHJcblxyXG5cdFx0Y29uc3Qgc3VnZ2VzdGlvbnNSZXN1bHQ6IFN1Z2dlc3Rpb25zID0gYXdhaXQgZGlzcGF0Y2hlci5nZXRDb21wbGV0aW9uU3VnZ2VzdGlvbnMocGFyc2VkQ29tbWFuZCk7XHJcblx0XHRzdWdnZXN0aW9ucyA9IHN1Z2dlc3Rpb25zUmVzdWx0LmdldExpc3QoKS5tYXAoKHgpID0+IHguZ2V0VGV4dCgpKTtcclxuXHRcdGNvbnNvbGUubG9nKHN1Z2dlc3Rpb25zKVxyXG5cdH1cclxuXHJcblx0Ly8gU2V0IHRoZSBjdXJzb3IgYmFjayB0byB3aGVyZSBpdCB3YXMuIFNpbmNlIGNvbW1hbmRzIGFsd2F5cyBzdGFydCB3aXRoIGFcclxuXHQvLyBmb3J3YXJkIHNsYXNoLCB0aGUgb25seSBwb3NzaWJsZSBcInN0YXJ0aW5nIGNhcmV0IHBvc2l0aW9uXCIgaXMgcG9zaXRpb24gMVxyXG5cdC8vIChpbiBmcm9udCBvZiB0aGUgc2xhc2gpXHJcblx0aWYgKGN1cnNvclBvcyA9PT0gMCAmJiByYXdUZXh0Lmxlbmd0aCA+IDApIHtcclxuXHRcdGN1cnNvclBvcyA9IDE7XHJcblx0fVxyXG5cdHNldEN1cnNvclBvc2l0aW9uKGN1cnNvclBvcywgQ09NTUFORF9JTlBVVCk7XHJcblx0Q09NTUFORF9JTlBVVC5mb2N1cygpO1xyXG5cclxuXHQvLyBJZiBhbnkgZXJyb3JzIGFwcGVhciwgZGlzcGxheSB0aGVtXHJcblx0aWYgKGVycm9yVGV4dC5sZW5ndGggIT09IDApIHtcclxuXHRcdHNldFRleHQoZXJyb3JUZXh0LCBFUlJPUl9NRVNTQUdFX0JPWCk7XHJcblx0XHRFUlJPUl9NRVNTQUdFX0JPWC5oaWRkZW4gPSBmYWxzZTtcclxuXHR9IGVsc2Uge1xyXG5cdFx0RVJST1JfTUVTU0FHRV9CT1guaGlkZGVuID0gdHJ1ZTtcclxuXHR9XHJcblxyXG5cdGlmIChzaG93VXNhZ2VUZXh0KSB7XHJcblx0XHRFUlJPUl9NRVNTQUdFX0JPWC5zdHlsZS5sZWZ0ID0gVGV4dFdpZHRoLmdldFRleHRXaWR0aChyYXdUZXh0LCBDT01NQU5EX0lOUFVUIGFzIENhY2hlZEZvbnRIVE1MRWxlbWVudCkgKyBcInB4XCI7XHJcblx0XHQvLyA4cHggcGFkZGluZywgMTBweCBtYXJnaW4gbGVmdCwgMTBweCBtYXJnaW4gcmlnaHQgPSAtMjhweFxyXG5cdFx0Ly8gUGx1cyBhbiBleHRyYSAxMHB4IGZvciBnb29kIGx1Y2ssIHdoeSBub3RcclxuXHRcdEVSUk9SX01FU1NBR0VfQk9YLnN0eWxlLndpZHRoID0gYGNhbGMoMTAwJSAtICR7RVJST1JfTUVTU0FHRV9CT1guc3R5bGUubGVmdH0gLSAyOHB4ICsgMTBweClgO1xyXG5cdH0gZWxzZSB7XHJcblx0XHRFUlJPUl9NRVNTQUdFX0JPWC5zdHlsZS5sZWZ0ID0gXCIwXCI7XHJcblx0XHRFUlJPUl9NRVNTQUdFX0JPWC5zdHlsZS53aWR0aCA9IFwidW5zZXRcIjtcclxuXHR9XHJcblxyXG5cdGlmIChjb21tYW5kVmFsaWQpIHtcclxuXHRcdHNldFRleHQoQ2hhdENvbG9yLkdSRUVOICsgXCJUaGlzIGNvbW1hbmQgaXMgdmFsaWQg4pyFXCIsIFZBTElEX0JPWCk7XHJcblx0XHRWQUxJRF9CT1guaGlkZGVuID0gZmFsc2U7XHJcblx0fSBlbHNlIHtcclxuXHRcdFZBTElEX0JPWC5oaWRkZW4gPSB0cnVlO1xyXG5cdH1cclxuXHJcblx0Y29uc3QgY29uc3RydWN0U3VnZ2VzdGlvbnNIVE1MID0gKHN1Z2dlc3Rpb25zOiBzdHJpbmdbXSk6IEhUTUxTcGFuRWxlbWVudFtdID0+IHtcclxuXHRcdGxldCBub2Rlc1RvQWRkOiBIVE1MU3BhbkVsZW1lbnRbXSA9IFtdO1xyXG5cdFx0Zm9yIChsZXQgaTogbnVtYmVyID0gMDsgaSA8IHN1Z2dlc3Rpb25zLmxlbmd0aDsgaSsrKSB7XHJcblx0XHRcdGNvbnN0IHN1Z2dlc3Rpb25FbGVtZW50OiBIVE1MU3BhbkVsZW1lbnQgPSBkb2N1bWVudC5jcmVhdGVFbGVtZW50KFwic3BhblwiKTtcclxuXHRcdFx0c3VnZ2VzdGlvbkVsZW1lbnQuaW5uZXJUZXh0ID0gc3VnZ2VzdGlvbnNbaV07XHJcblx0XHRcdGlmIChpID09PSAwKSB7XHJcblx0XHRcdFx0c3VnZ2VzdGlvbkVsZW1lbnQuY2xhc3NOYW1lID0gXCJ5ZWxsb3dcIjtcclxuXHRcdFx0fVxyXG5cdFx0XHRpZiAoaSAhPT0gc3VnZ2VzdGlvbnMubGVuZ3RoIC0gMSkge1xyXG5cdFx0XHRcdHN1Z2dlc3Rpb25FbGVtZW50LmlubmVyVGV4dCArPSBcIlxcblwiO1xyXG5cdFx0XHR9XHJcblx0XHRcdG5vZGVzVG9BZGQucHVzaChzdWdnZXN0aW9uRWxlbWVudCk7XHJcblx0XHR9XHJcblxyXG5cdFx0cmV0dXJuIG5vZGVzVG9BZGQ7XHJcblx0fTtcclxuXHJcblx0Ly8gSWYgc3VnZ2VzdGlvbnMgYXJlIHByZXNlbnQsIGRpc3BsYXkgdGhlbVxyXG5cdFNVR0dFU1RJT05TX0JPWC5zdHlsZS5sZWZ0ID0gXCIwXCI7XHJcblx0aWYgKHN1Z2dlc3Rpb25zLmxlbmd0aCAhPT0gMCkge1xyXG5cdFx0U1VHR0VTVElPTlNfQk9YLmlubmVySFRNTCA9IFwiXCI7XHJcblx0XHRmb3IgKGxldCBzdWdnZXN0aW9uRWxlbWVudCBvZiBjb25zdHJ1Y3RTdWdnZXN0aW9uc0hUTUwoc3VnZ2VzdGlvbnMpKSB7XHJcblx0XHRcdFNVR0dFU1RJT05TX0JPWC5hcHBlbmRDaGlsZChzdWdnZXN0aW9uRWxlbWVudCk7XHJcblx0XHR9XHJcblx0XHRTVUdHRVNUSU9OU19CT1guc3R5bGUubGVmdCA9IFRleHRXaWR0aC5nZXRUZXh0V2lkdGgocmF3VGV4dCwgQ09NTUFORF9JTlBVVCBhcyBDYWNoZWRGb250SFRNTEVsZW1lbnQpICsgXCJweFwiO1xyXG5cdFx0Ly8gOHB4IHBhZGRpbmcsIDEwcHggbWFyZ2luIGxlZnQsIDEwcHggbWFyZ2luIHJpZ2h0ID0gLTI4cHhcclxuXHRcdC8vIFBsdXMgYW4gZXh0cmEgMTBweCBmb3IgZ29vZCBsdWNrLCB3aHkgbm90XHJcblx0XHRTVUdHRVNUSU9OU19CT1guaGlkZGVuID0gZmFsc2U7XHJcblx0XHRFUlJPUl9NRVNTQUdFX0JPWC5oaWRkZW4gPSB0cnVlO1xyXG5cdH0gZWxzZSB7XHJcblx0XHRTVUdHRVNUSU9OU19CT1guaGlkZGVuID0gdHJ1ZTtcclxuXHR9XHJcblx0d2luZG93LmRpc3BhdGNoRXZlbnQobmV3IEV2ZW50KFwic3VnZ2VzdGlvbnNVcGRhdGVkXCIpKTtcclxufVxyXG5cclxuLy8gV2UgcmVhbGx5IHJlYWxseSBkb24ndCB3YW50IG5ldyBsaW5lcyBpbiBvdXIgc2luZ2xlLWxpbmVkIGNvbW1hbmQhXHJcbkNPTU1BTkRfSU5QVVQuYWRkRXZlbnRMaXN0ZW5lcigna2V5ZG93bicsIChldnQ6IEtleWJvYXJkRXZlbnQpID0+IHtcclxuXHRzd2l0Y2ggKGV2dC5rZXkpIHtcclxuXHRcdGNhc2UgXCJFbnRlclwiOlxyXG5cdFx0XHRldnQucHJldmVudERlZmF1bHQoKTtcclxuXHRcdFx0YnJlYWs7XHJcblx0XHRjYXNlIFwiQXJyb3dEb3duXCI6XHJcblx0XHRjYXNlIFwiQXJyb3dVcFwiOiB7XHJcblx0XHRcdGlmICghU1VHR0VTVElPTlNfQk9YLmhpZGRlbikge1xyXG5cdFx0XHRcdGZvciAobGV0IGkgPSAwOyBpIDwgU1VHR0VTVElPTlNfQk9YLmNoaWxkcmVuLmxlbmd0aDsgaSsrKSB7XHJcblx0XHRcdFx0XHRpZiAoU1VHR0VTVElPTlNfQk9YLmNoaWxkcmVuW2ldLmNsYXNzTmFtZSA9PT0gXCJ5ZWxsb3dcIikge1xyXG5cdFx0XHRcdFx0XHRTVUdHRVNUSU9OU19CT1guY2hpbGRyZW5baV0uY2xhc3NOYW1lID0gXCJcIjtcclxuXHJcblx0XHRcdFx0XHRcdGlmIChldnQua2V5ID09IFwiQXJyb3dEb3duXCIpIHtcclxuXHRcdFx0XHRcdFx0XHRpZiAoaSA9PT0gU1VHR0VTVElPTlNfQk9YLmNoaWxkcmVuLmxlbmd0aCAtIDEpIHtcclxuXHRcdFx0XHRcdFx0XHRcdFNVR0dFU1RJT05TX0JPWC5jaGlsZHJlblswXS5jbGFzc05hbWUgPSBcInllbGxvd1wiO1xyXG5cdFx0XHRcdFx0XHRcdH0gZWxzZSB7XHJcblx0XHRcdFx0XHRcdFx0XHRTVUdHRVNUSU9OU19CT1guY2hpbGRyZW5baSArIDFdLmNsYXNzTmFtZSA9IFwieWVsbG93XCI7XHJcblx0XHRcdFx0XHRcdFx0fVxyXG5cdFx0XHRcdFx0XHR9IGVsc2Uge1xyXG5cdFx0XHRcdFx0XHRcdGlmIChpID09PSAwKSB7XHJcblx0XHRcdFx0XHRcdFx0XHRTVUdHRVNUSU9OU19CT1guY2hpbGRyZW5bU1VHR0VTVElPTlNfQk9YLmNoaWxkcmVuLmxlbmd0aCAtIDFdLmNsYXNzTmFtZSA9IFwieWVsbG93XCI7XHJcblx0XHRcdFx0XHRcdFx0fSBlbHNlIHtcclxuXHRcdFx0XHRcdFx0XHRcdFNVR0dFU1RJT05TX0JPWC5jaGlsZHJlbltpIC0gMV0uY2xhc3NOYW1lID0gXCJ5ZWxsb3dcIjtcclxuXHRcdFx0XHRcdFx0XHR9XHJcblx0XHRcdFx0XHRcdH1cclxuXHJcblx0XHRcdFx0XHRcdHdpbmRvdy5kaXNwYXRjaEV2ZW50KG5ldyBFdmVudChcInN1Z2dlc3Rpb25zVXBkYXRlZFwiKSk7XHJcblxyXG5cdFx0XHRcdFx0XHRicmVhaztcclxuXHRcdFx0XHRcdH1cclxuXHRcdFx0XHR9XHJcblx0XHRcdH1cclxuXHRcdFx0YnJlYWs7XHJcblx0XHR9XHJcblx0XHRjYXNlIFwiQmFja3NwYWNlXCI6XHJcblx0XHRcdGlmIChDT01NQU5EX0lOUFVULmlubmVyVGV4dC5yZXBsYWNlKFwiXFxuXCIsIFwiXCIpLmxlbmd0aCA9PT0gMCkge1xyXG5cdFx0XHRcdGV2dC5wcmV2ZW50RGVmYXVsdCgpO1xyXG5cdFx0XHR9XHJcblx0XHRcdGJyZWFrO1xyXG5cdFx0Y2FzZSBcIlRhYlwiOlxyXG5cdFx0XHRldnQucHJldmVudERlZmF1bHQoKTtcclxuXHRcdFx0c2V0VGV4dChnZXRUZXh0KGZhbHNlKS5zbGljZSgxKSArIENPTU1BTkRfSU5QVVRfQVVUT0NPTVBMRVRFLmlubmVyVGV4dCk7XHJcblx0XHRcdENPTU1BTkRfSU5QVVQub25pbnB1dChudWxsKTtcclxuXHRcdFx0c2V0Q3Vyc29yUG9zaXRpb24oQ09NTUFORF9JTlBVVC5pbm5lclRleHQubGVuZ3RoLCBDT01NQU5EX0lOUFVUKTtcclxuXHRcdFx0YnJlYWs7XHJcblx0XHRkZWZhdWx0OlxyXG5cdFx0XHRicmVhaztcclxuXHR9XHJcbn0pO1xyXG5cclxud2luZG93LmFkZEV2ZW50TGlzdGVuZXIoXCJzdWdnZXN0aW9uc1VwZGF0ZWRcIiwgKF9ldmVudDogRXZlbnQpID0+IHtcclxuXHRjb25zdCByYXdUZXh0OiBzdHJpbmcgPSBDT01NQU5EX0lOUFVULmlubmVyVGV4dC5yZXBsYWNlQWxsKFwiXFx1MDBhMFwiLCBcIiBcIik7IC8vIFJlcGxhY2UgJm5ic3A7IHdpdGggbm9ybWFsIHNwYWNlc1xyXG5cclxuXHRpZiAoIVNVR0dFU1RJT05TX0JPWC5oaWRkZW4pIHtcclxuXHRcdGNvbnN0IHNlbGVjdGVkU3VnZ2VzdGlvblRleHQ6IHN0cmluZyA9IGdldFNlbGVjdGVkU3VnZ2VzdGlvbigpLmlubmVyVGV4dC50cmltKCk7XHJcblxyXG5cdFx0Ly8gVE9ETzogVGhpcyBvYnZpb3VzbHkgbmVlZHMgdG8gYmUgc3BlY2lmaWMgdG8gdGhlIGN1cnJlbnQgc3VnZ2VzdGlvbnMsIG5vdCB0aGUgd2hvbGUgaW5wdXRcclxuXHRcdGlmIChyYXdUZXh0ICE9PSBzZWxlY3RlZFN1Z2dlc3Rpb25UZXh0KSB7XHJcblx0XHRcdGNvbnN0IGN1cnNvclBvc2l0aW9uOiBudW1iZXIgPSBnZXRDdXJzb3JQb3NpdGlvbigpO1xyXG5cdFx0XHRjb25zdCBsYXN0U3BhY2VJbmRleDogbnVtYmVyID0gcmF3VGV4dC5sYXN0SW5kZXhPZihcIiBcIikgPT09IC0xID8gMSA6IHJhd1RleHQubGFzdEluZGV4T2YoXCIgXCIpICsgMTtcclxuXHRcdFx0c2V0VGV4dChDaGF0Q29sb3IuREFSS19HUkFZICsgc2VsZWN0ZWRTdWdnZXN0aW9uVGV4dC5zbGljZShyYXdUZXh0LnNsaWNlKGxhc3RTcGFjZUluZGV4KS5sZW5ndGgpLCBDT01NQU5EX0lOUFVUX0FVVE9DT01QTEVURSk7XHJcblx0XHRcdHNldEN1cnNvclBvc2l0aW9uKGN1cnNvclBvc2l0aW9uLCBDT01NQU5EX0lOUFVUKTtcclxuXHRcdFx0Q09NTUFORF9JTlBVVC5mb2N1cygpO1xyXG5cdFx0fSBlbHNlIHtcclxuXHRcdFx0c2V0VGV4dChcIlwiLCBDT01NQU5EX0lOUFVUX0FVVE9DT01QTEVURSk7XHJcblx0XHR9XHJcblx0fSBlbHNlIHtcclxuXHRcdHNldFRleHQoXCJcIiwgQ09NTUFORF9JTlBVVF9BVVRPQ09NUExFVEUpO1xyXG5cdH1cclxufSk7XHJcblxyXG4vLyBJZiB5b3UgY2xpY2sgb24gdGhlIGNoYXQgYm94LCBmb2N1cyB0aGUgY3VycmVudCB0ZXh0IGlucHV0IGFyZWEgXHJcbmRvY3VtZW50LmdldEVsZW1lbnRCeUlkKFwiY2hhdGJveFwiKS5vbmNsaWNrID0gZnVuY3Rpb24gb25DaGF0Qm94Q2xpY2tlZCgpIHtcclxuXHRDT01NQU5EX0lOUFVULmZvY3VzKCk7XHJcbn07XHJcblxyXG5kb2N1bWVudC5nZXRFbGVtZW50QnlJZChcInJlZ2lzdGVyLWNvbW1hbmRzLWJ1dHRvblwiKS5vbmNsaWNrID0gZnVuY3Rpb24gb25SZWdpc3RlckNvbW1hbmRzQnV0dG9uQ2xpY2tlZCgpIHtcclxuXHRkaXNwYXRjaGVyLmRlbGV0ZUFsbCgpO1xyXG5cdENPTU1BTkRTLnZhbHVlLnNwbGl0KFwiXFxuXCIpLmZvckVhY2gocmVnaXN0ZXJDb21tYW5kKTtcclxuXHRDT01NQU5EX0lOUFVULm9uaW5wdXQobnVsbCk7IC8vIFJ1biBzeW50YXggaGlnaGxpZ2h0ZXJcclxufVxyXG5cclxuXHJcbi8qKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKipcclxuICogRW50cnlwb2ludCAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgKlxyXG4gKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqL1xyXG5cclxuLy8gRGVmYXVsdCBjb21tYW5kc1xyXG5DT01NQU5EUy52YWx1ZSA9IGBmaWxsIDxwb3MxPlttaW5lY3JhZnQ6YmxvY2tfcG9zXSA8cG9zMj5bbWluZWNyYWZ0OmJsb2NrX3Bvc10gPGJsb2NrPlticmlnYWRpZXI6c3RyaW5nXVxyXG5zcGVlZCAod2Fsa3xmbHkpIDxzcGVlZD5bMC4uMTBdIDx0YXJnZXQ+W21pbmVjcmFmdDpnYW1lX3Byb2ZpbGVdXHJcbmhlbGxvIDx2YWw+WzEuLjIwXSA8Y29sb3I+W21pbmVjcmFmdDpjb2xvcl1cclxubXlmdW5jIDx2YWw+W21pbmVjcmFmdDptb2JfZWZmZWN0XVxyXG5lbnRpdHkgPHZhbGE+W21pbmVjcmFmdDplbnRpdGllc11gO1xyXG5cclxuZG9jdW1lbnQuZ2V0RWxlbWVudEJ5SWQoXCJyZWdpc3Rlci1jb21tYW5kcy1idXR0b25cIik/Lm9uY2xpY2sobnVsbCk7XHJcbmNvbnNvbGUubG9nKFwiRGlzcGF0Y2hlclwiLCBkaXNwYXRjaGVyLmdldFJvb3QoKSlcclxuXHJcbiJdLCJuYW1lcyI6W10sInNvdXJjZVJvb3QiOiIifQ==