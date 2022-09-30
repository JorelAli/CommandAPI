/******/ (() => { // webpackBootstrap
/******/ 	"use strict";
/******/ 	var __webpack_modules__ = ({

/***/ "./node_modules/mojangson-parser/dist/es/index.js":
/*!********************************************************!*\
  !*** ./node_modules/mojangson-parser/dist/es/index.js ***!
  \********************************************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   "default": () => (/* binding */ parser)
/* harmony export */ });
class Nbt {
    static eat(length) {
        Nbt.text = Nbt.text.substr(length);
    }
    static parseObject() {
        const obj = {};
        if (Nbt.testCloseCurly()) {
            return obj;
        }
        while (true) {
            const tagName = Nbt.parseKey();
            Nbt.eatColon();
            const value = Nbt.parseValue();
            obj[tagName] = value;
            if (Nbt.testCloseCurly()) {
                break;
            }
            Nbt.eatComma();
        }
        return obj;
    }
    static testIndexArray() {
        const match = Nbt.text.match(/\s*\d+\s*:/);
        if (match) {
            if (match) {
                Nbt.eat(match[0].length);
                return true;
            }
        }
        return false;
    }
    static testTypeArray() {
        const match = Nbt.text.match(/^\s*[ILB]\s*;/);
        if (match) {
            if (match) {
                Nbt.eat(match[0].length);
                return true;
            }
        }
        return false;
    }
    static parseArray() {
        const array = [];
        if (Nbt.testCloseSquare()) {
            return array;
        }
        while (true) {
            Nbt.testIndexArray();
            Nbt.testTypeArray();
            if (Nbt.testBeginSquare()) {
                array.push(Nbt.parseObject());
            }
            else {
                array.push(Nbt.parseValue());
            }
            if (Nbt.testCloseSquare()) {
                break;
            }
            Nbt.eatComma();
        }
        return array;
    }
    static eatColon() {
        const match = Nbt.text.match(/^\s*:/);
        if (match) {
            Nbt.eat(match[0].length);
            return;
        }
        throw new Error('lack of colon');
    }
    static eatComma() {
        const match = Nbt.text.match(/^\s*,/);
        if (match) {
            Nbt.eat(match[0].length);
            return;
        }
        throw new Error('lack of comma');
    }
    static testBeginCurly() {
        const match = Nbt.text.match(/^\s*{/);
        if (match) {
            Nbt.eat(match[0].length);
            return true;
        }
        return false;
    }
    static testCloseCurly() {
        const match = Nbt.text.match(/^\s*}/);
        if (match) {
            Nbt.eat(match[0].length);
            return true;
        }
        return false;
    }
    static testBeginSquare() {
        const match = Nbt.text.match(/^\s*\[/);
        if (match) {
            Nbt.eat(match[0].length);
            return true;
        }
        return false;
    }
    static testCloseSquare() {
        const match = Nbt.text.match(/^\s*\]/);
        if (match) {
            Nbt.eat(match[0].length);
            return true;
        }
        return false;
    }
    static parseKey() {
        const match = Nbt.text.match(/^\s*([a-z][a-z0-9]*)/i);
        if (match) {
            const [_, key] = match;
            Nbt.eat(_.length);
            return key;
        }
        throw new Error(`Property name is invalid at "${Nbt.text.slice(0, 15)}"`);
    }
    static parseValue() {
        if (Nbt.testBeginCurly()) {
            return Nbt.parseObject();
        }
        if (Nbt.testBeginSquare()) {
            return Nbt.parseArray();
        }
        const match = Nbt.text.match(/^\s*([0-9a-zA-Z_\-\$\.]+)|^\s*("(?:\\\"|\\\\|[^\\\\])*?")|^\s*(\'(?:\\\"|\\\\|[^\\\\])*?')/);
        if (match) {
            const [_, bare, double, single] = match;
            Nbt.eat(_.length);
            if (bare) {
                return bare;
            }
            if (double) {
                return double.slice(1, -1).replace(/\\\\/g, '\\').replace(/\\\"/g, '"');
            }
            if (single) {
                return single.slice(1, -1);
            }
        }
        throw new Error(`value is invalid at "${Nbt.text.slice(0, 15)}"`);
    }
}
Nbt.text = '';
function parser(text) {
    Nbt.text = text;
    if (Nbt.testBeginCurly()) {
        return Nbt.parseObject();
    }
    throw new Error('expect an object');
}




/***/ }),

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
/* harmony export */   "EnchantmentArgument": () => (/* binding */ EnchantmentArgument),
/* harmony export */   "EntitySelectorArgument": () => (/* binding */ EntitySelectorArgument),
/* harmony export */   "MathOperationArgument": () => (/* binding */ MathOperationArgument),
/* harmony export */   "MultiLiteralArgument": () => (/* binding */ MultiLiteralArgument),
/* harmony export */   "NBTCompoundArgument": () => (/* binding */ NBTCompoundArgument),
/* harmony export */   "PlayerArgument": () => (/* binding */ PlayerArgument),
/* harmony export */   "PotionEffectArgument": () => (/* binding */ PotionEffectArgument),
/* harmony export */   "RangeArgument": () => (/* binding */ RangeArgument),
/* harmony export */   "TimeArgument": () => (/* binding */ TimeArgument),
/* harmony export */   "UUIDArgument": () => (/* binding */ UUIDArgument),
/* harmony export */   "UnimplementedArgument": () => (/* binding */ UnimplementedArgument)
/* harmony export */ });
/* harmony import */ var node_brigadier__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! node-brigadier */ "./node_modules/node-brigadier/dist/index.js");
/* harmony import */ var node_brigadier__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(node_brigadier__WEBPACK_IMPORTED_MODULE_0__);
/* harmony import */ var _brigadier_extensions__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./brigadier-extensions */ "./src/brigadier-extensions.ts");
/* harmony import */ var _array_extensions__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./array-extensions */ "./src/array-extensions.ts");



/******************************************************************************
 * Helper classes                                                             *
 ******************************************************************************/
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
class SuggestionsHelper {
    static shouldSuggest(remaining, suggestion) {
        let i = 0;
        while (!suggestion.startsWith(remaining, i)) {
            i = suggestion.indexOf("_", i);
            if (i < 0) {
                return false;
            }
            i++;
        }
        return true;
    }
    static suggestMatching(reader, suggestions) {
        let newSuggestions = [];
        const remaining = reader.getRemaining().toLocaleLowerCase();
        for (let suggestion of suggestions) {
            if (!SuggestionsHelper.shouldSuggest(remaining, suggestion.toLocaleLowerCase())) {
                continue;
            }
            newSuggestions.push(suggestion);
        }
        return newSuggestions;
    }
}
/******************************************************************************
 * Argument implementation classes                                            *
 ******************************************************************************/
/**
 * @deprecated Placeholder, avoid use in production
 */
class UnimplementedArgument {
    parse(_reader) {
        throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("This argument hasn't been implemented")).create();
    }
}
class TimeArgument {
    static UNITS = new Map([
        ["d", 24000],
        ["s", 20],
        ["t", 1],
        ["", 1]
    ]);
    ticks = 0;
    parse(reader) {
        const numericalValue = reader.readFloat();
        const unit = reader.readUnquotedString();
        const unitMultiplier = TimeArgument.UNITS.get(unit);
        if (unitMultiplier === undefined) {
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
    x = 0;
    y = 0;
    z = 0;
    parse(reader) {
        this.x = reader.readLocationLiteral();
        reader.skip();
        this.y = reader.readLocationLiteral();
        reader.skip();
        this.z = reader.readLocationLiteral();
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
    x = 0;
    z = 0;
    parse(reader) {
        this.x = reader.readLocationLiteral();
        reader.skip();
        this.z = reader.readLocationLiteral();
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
    parse(reader) {
        const start = reader.getCursor();
        while (reader.canRead() && reader.peek() !== " ") {
            reader.skip();
        }
        const string = reader.getString();
        const currentCursor = reader.getCursor();
        const username = string.slice(start, currentCursor);
        if (!username.match(/^[A-Za-z0-9_]{2,16}$/)) {
            throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(username + " is not a valid username")).createWithContext(reader);
        }
        return username;
    }
    getExamples() {
        return ["Skepter"];
    }
}
class MultiLiteralArgument {
    literals;
    constructor(literals) {
        this.literals = literals;
    }
    parse(reader) {
        const start = reader.getCursor();
        while (reader.canRead() && reader.peek() !== " ") {
            reader.skip();
        }
        let selectedLiteral = reader.getString().slice(start, reader.getCursor());
        if (selectedLiteral.endsWith(" ")) {
            selectedLiteral = selectedLiteral.trimEnd();
            reader.setCursor(reader.getCursor() - 1);
        }
        if (!this.literals.includes(selectedLiteral)) {
            throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(selectedLiteral + " is not one of " + this.literals)).createWithContext(reader);
        }
        return selectedLiteral;
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
    parse(reader) {
        let input = reader.readUnquotedString();
        let chatColor = ColorArgument.ChatColor[input.toLowerCase()];
        if (chatColor === undefined) {
            throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`Unknown colour '${input}'`)).createWithContext(reader);
        }
        return chatColor;
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
    parse(reader) {
        const resourceLocation = reader.readResourceLocation();
        const potionEffect = resourceLocation[0] + ":" + resourceLocation[1];
        const isValidPotionEffect = PotionEffectArgument.PotionEffects.includes(potionEffect.toLowerCase());
        if (!isValidPotionEffect) {
            throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`Unknown effect '${potionEffect}'`)).createWithContext(reader);
        }
        return potionEffect;
    }
    listSuggestions(_context, builder) {
        return HelperSuggestionProvider.suggest([...PotionEffectArgument.PotionEffects], builder);
    }
    getExamples() {
        return ["spooky", "effect"];
    }
}
class EnchantmentArgument {
    static Enchantments = [
        "minecraft:protection",
        "minecraft:fire_protection",
        "minecraft:feather_falling",
        "minecraft:blast_protection",
        "minecraft:projectile_protection",
        "minecraft:respiration",
        "minecraft:aqua_affinity",
        "minecraft:thorns",
        "minecraft:depth_strider",
        "minecraft:frost_walker",
        "minecraft:binding_curse",
        "minecraft:soul_speed",
        "minecraft:swift_sneak",
        "minecraft:sharpness",
        "minecraft:smite",
        "minecraft:bane_of_arthropods",
        "minecraft:knockback",
        "minecraft:fire_aspect",
        "minecraft:looting",
        "minecraft:sweeping",
        "minecraft:efficiency",
        "minecraft:silk_touch",
        "minecraft:unbreaking",
        "minecraft:fortune",
        "minecraft:power",
        "minecraft:punch",
        "minecraft:flame",
        "minecraft:infinity",
        "minecraft:luck_of_the_sea",
        "minecraft:lure",
        "minecraft:loyalty",
        "minecraft:impaling",
        "minecraft:riptide",
        "minecraft:channeling",
        "minecraft:multishot",
        "minecraft:quick_charge",
        "minecraft:piercing",
        "minecraft:mending",
        "minecraft:vanishing_curse",
    ];
    parse(reader) {
        const resourceLocation = reader.readResourceLocation();
        const enchantment = resourceLocation[0] + ":" + resourceLocation[1];
        const isValidEnchantment = EnchantmentArgument.Enchantments.includes(enchantment.toLowerCase());
        if (!isValidEnchantment) {
            throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`Unknown enchantment '${enchantment}'`)).createWithContext(reader);
        }
        return enchantment;
    }
    listSuggestions(_context, builder) {
        return HelperSuggestionProvider.suggest([...EnchantmentArgument.Enchantments], builder);
    }
    getExamples() {
        return ["unbreaking", "silk_touch"];
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
    parse(reader) {
        const remaining = reader.getRemaining();
        const matchedResults = remaining.match(/^([-A-Fa-f0-9]+)/);
        if (matchedResults !== null && matchedResults[1] !== undefined) {
            const uuid = matchedResults[1];
            // Regex for a UUID: https://stackoverflow.com/a/13653180/4779071
            if (uuid.match(/^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$/i) !== null) {
                reader.setCursor(reader.getCursor() + uuid.length);
                return uuid;
            }
        }
        throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Invalid UUID")).createWithContext(reader);
    }
    getExamples() {
        return ["dd12be42-52a9-4a91-a8a1-11c01849e498"];
    }
}
const MathOperations = ["=", "+=", "-=", "*=", "/=", "%=", "<", ">", "><"];
class MathOperationArgument {
    parse(reader) {
        if (reader.canRead()) {
            let start = reader.getCursor();
            while (reader.canRead() && reader.peek() != ' ') {
                reader.skip();
            }
            const mathOperation = reader.getString().substring(start, reader.getCursor());
            if (MathOperations.includes(mathOperation)) {
                return mathOperation;
            }
        }
        throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Invalid operation")).createWithContext(reader);
    }
    listSuggestions(_context, builder) {
        return HelperSuggestionProvider.suggest([...MathOperations], builder);
    }
    getExamples() {
        return ["=", ">", "<"];
    }
}
class NBTCompoundArgument {
    parse(reader) {
        return reader.readNBT();
    }
    getExamples() {
        return ["{}", "{foo=bar}"];
    }
}
class RangeArgument {
    allowFloats;
    constructor(allowFloats) {
        this.allowFloats = allowFloats;
    }
    parse(reader) {
        return reader.readMinMaxBounds(this.allowFloats);
    }
    getExamples() {
        if (this.allowFloats) {
            return ["0..5.2", "0", "-5.4", "-100.76..", "..100"];
        }
        else {
            return ["0..5", "0", "-5", "-100..", "..100"];
        }
    }
}
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
];
const EntityTags = [
    "arrows", "axolotl_always_hostiles", "axolotl_hunt_targets", "beehive_inhabitors",
    "freeze_hurts_extra_types", "freeze_immune_entity_types", "frog_food", "impact_projectiles",
    "powder_snow_walkable_mobs", "raiders", "skeletons"
];
// Effectively a giant merge of EntityArgument, EntitySelectorParser and EntitySelectorOptions
class EntitySelectorArgument {
    // EntitySelectorParser suggestions
    suggestions;
    suggestionsModifier = null;
    // EntitySelectorParser + EntitySelectorOptions
    entityUUID;
    includesEntities;
    playerName;
    maxResults;
    isLimited; // This might be identical to the synthetic limitedToPlayers, try using this instead
    limitedToPlayers; // players only?
    entityType;
    currentEntity;
    worldLimited;
    order; // Not BiConsumer<Vec, List<? extends Entity>> because effort
    isSorted; // Is this entity selector sorted?
    selectsGamemode = false;
    excludesGamemode = false;
    hasNameEquals;
    hasNameNotEquals;
    hasScores;
    typeInverse; // isTypeLimitedInversely AKA excludesEntityType
    // EntityArgument
    single;
    playersOnly;
    constructor(single, playersOnly) {
        this.single = single;
        this.playersOnly = playersOnly;
    }
    suggestionGenerator(reader, type) {
        let suggestions = [];
        switch (type) {
            case "gamemode": {
                let string = reader.getRemaining().toLowerCase();
                let bool = !this.excludesGamemode;
                let negating = true;
                if (string.length !== 0) {
                    if (string.charAt(0) === "!") {
                        bool = false;
                        string = string.slice(1);
                    }
                    else {
                        negating = false;
                    }
                }
                for (let gamemode of ["survival", "creative", "adventure", "spectator"]) {
                    if (!gamemode.toLowerCase().startsWith(string)) {
                        continue;
                    }
                    if (negating) {
                        suggestions.push("!" + gamemode);
                    }
                    if (!bool) {
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
                if (!this.typeInverse) {
                    suggestions.push(...SuggestionsHelper.suggestMatching(reader, [...entityTypes]));
                    suggestions.push(...SuggestionsHelper.suggestMatching(reader, EntityTags.map(entity => `#${entity}`)));
                }
                break;
            }
        }
        return suggestions;
    }
    Options = {
        name: (reader) => {
            const start = reader.getCursor();
            const shouldInvert = reader.readNegationCharacter();
            const _s = reader.readString();
            if (this.hasNameNotEquals && !shouldInvert) {
                reader.setCursor(start);
                throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`Option 'name' isn't applicable here`)).createWithContext(reader);
            }
            if (shouldInvert) {
                this.hasNameNotEquals = true;
            }
            else {
                this.hasNameEquals = true;
            }
        },
        distance: (_reader) => { },
        level: (_reader) => { },
        x: (reader) => { reader.readFloat(); },
        y: (reader) => { reader.readFloat(); },
        z: (reader) => { reader.readFloat(); },
        dx: (reader) => { reader.readFloat(); },
        dy: (reader) => { reader.readFloat(); },
        dz: (reader) => { reader.readFloat(); },
        x_rotation: (reader) => { reader.readMinMaxBounds(true); },
        y_rotation: (reader) => { reader.readMinMaxBounds(true); },
        limit: (reader) => {
            const start = reader.getCursor();
            const limit = reader.readInt();
            if (limit < 1) {
                reader.setCursor(start);
                throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`Limit must be at least 1`)).createWithContext(reader);
            }
            this.maxResults = limit;
            this.isLimited = true;
        },
        sort: (reader) => {
            this.suggestions = this.suggestionGenerator(reader, "sort");
            const start = reader.getCursor();
            const sortType = reader.readUnquotedString(); // word
            if (["nearest", "furthest", "random", "arbitrary"].includes(sortType)) {
                this.order = sortType;
                this.isSorted = true;
            }
            else {
                reader.setCursor(start);
                throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`Invalid or unknown sort type '${sortType}'`)).createWithContext(reader);
            }
        },
        gamemode: (reader) => {
            this.suggestions = this.suggestionGenerator(reader, "gamemode");
            const start = reader.getCursor();
            const shouldInvert = reader.readNegationCharacter();
            if (this.excludesGamemode && !shouldInvert) {
                reader.setCursor(start);
                throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`Option 'gamemode' isn't applicable here`)).createWithContext(reader);
            }
            const gamemode = reader.readUnquotedString();
            if (!["survival", "creative", "adventure", "spectator"].includes(gamemode)) {
                reader.setCursor(start);
                throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`Invalid or unknown game mode '${gamemode}'`)).createWithContext(reader);
            }
            else {
                this.includesEntities = false;
                if (shouldInvert) {
                    this.excludesGamemode = true;
                }
                else {
                    this.selectsGamemode = true;
                }
            }
        },
        team: (reader) => {
            reader.readNegationCharacter();
            reader.readUnquotedString();
        },
        type: (reader) => {
            this.suggestions = this.suggestionGenerator(reader, "type");
            let start = reader.getCursor();
            let shouldInvert = reader.readNegationCharacter();
            if (this.typeInverse && !shouldInvert) {
                reader.setCursor(start);
                throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`Option 'type' isn't applicable here`)).createWithContext(reader);
            }
            if (shouldInvert) {
                this.typeInverse = true;
            }
            if (reader.readTagCharacter()) {
                // Yay, we've got a tag. Read everything matching 0 - 9, a - z, _, :, /, . or -
                reader.readResourceLocation();
            }
            else {
                const [namespace, key] = reader.readResourceLocation();
                if (!entityTypes.includes(key)) { // Uh... yes... this totally won't fail...
                    throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`${key} is not a valid entity type`)).createWithContext(reader);
                }
                if ("player" === key && !shouldInvert) {
                    this.includesEntities = true;
                }
                if (!shouldInvert) {
                    this.entityType = key;
                }
            }
        },
        tag: (reader) => {
            reader.readNegationCharacter();
            reader.readUnquotedString();
        },
        nbt: (reader) => {
            const start = reader.getCursor();
            const shouldInvert = reader.readNegationCharacter();
            try {
                reader.readNBT();
            }
            catch (error) {
                reader.setCursor(start);
                throw error;
            }
        },
        scores: (reader) => {
            // @e[scores={foo=10,bar=1..5}]
            reader.expect("{");
            reader.skipWhitespace();
            while (reader.canRead() && reader.peek() != '}') {
                reader.skipWhitespace();
                const str = reader.readUnquotedString();
                reader.skipWhitespace();
                reader.expect('=');
                reader.skipWhitespace();
                reader.readMinMaxBounds(false);
                reader.skipWhitespace();
                if (reader.canRead() && reader.peek() == ',') {
                    reader.skip();
                }
            }
            reader.expect('}');
            this.hasScores = true;
        },
        advancements: (reader) => {
            throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("This command visualizer doesn't support 'advancements'")).createWithContext(reader);
        },
        predicate: (reader) => {
            throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("This command visualizer doesn't support 'predicate'")).createWithContext(reader);
        }
    };
    parse(reader) {
        const parseOptions = () => {
            this.suggestions = SuggestionsHelper.suggestMatching(reader, [...Object.keys(this.Options).map(x => `${x}=`)]); // TODO: So this isn't exactly correct, we need to not list existing names, but that'll require a bit of a refactor
            reader.skipWhitespace();
            while (reader.canRead() && reader.peek() !== "]") {
                reader.skipWhitespace();
                let start = reader.getCursor();
                let optionsType = reader.readString();
                let modifier = this.Options[optionsType];
                if (modifier === null) {
                    reader.setCursor(start);
                    throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`Unknown option '${optionsType}'`)).createWithContext(reader);
                }
                reader.skipWhitespace();
                if (!reader.canRead() || reader.peek() !== "=") {
                    reader.setCursor(start);
                    throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`Expected value for option '${optionsType}'`)).createWithContext(reader);
                }
                reader.skip();
                reader.skipWhitespace();
                this.suggestions = [];
                modifier(reader);
                reader.skipWhitespace();
                this.suggestions = [",", "]"];
                if (!reader.canRead()) {
                    continue;
                }
                if (reader.peek() === ",") {
                    reader.skip();
                    this.suggestions = SuggestionsHelper.suggestMatching(reader, [...Object.keys(this.Options)].map(x => `${x}=`)); // TODO: So this isn't exactly correct, we need to not list existing names, but that'll require a bit of a refactor
                    continue;
                }
                if (reader.peek() !== "]") {
                    throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Expected end of options")).createWithContext(reader);
                }
            }
            if (reader.canRead()) {
                reader.skip();
                this.suggestions = [];
                return;
            }
            throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Expected end of options")).createWithContext(reader);
        };
        const parseSelector = () => {
            this.suggestionsModifier = (builder) => { builder.createOffset(builder.getStart() - 1); };
            this.suggestions = ["@p", "@a", "@r", "@s", "@e"];
            if (!reader.canRead()) {
                throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Missing selector type")).createWithContext(reader);
            }
            let start = reader.getCursor();
            let selectorChar = reader.read();
            switch (selectorChar) {
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
                    throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`Unknown selector type '${selectorChar}'`)).createWithContext(reader);
            }
            this.suggestionsModifier = null;
            this.suggestions = ["["];
            if (reader.canRead() && reader.peek() === "[") {
                reader.skip();
                this.suggestionsModifier = null;
                this.suggestions = ["]", ...Object.keys(this.Options).map(x => `${x}=`)]; // TODO: So this isn't exactly correct, we need to not list existing names, but that'll require a bit of a refactor
                parseOptions();
            }
        };
        const parseNameOrUUID = () => {
            let i = reader.getCursor();
            let s = reader.readString();
            // Regex for a UUID: https://stackoverflow.com/a/13653180/4779071
            if (s.match(/^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$/i) !== null) {
                this.entityUUID = s;
                this.includesEntities = true;
            }
            else if (s.length === 0 || s.length > 16) {
                reader.setCursor(i);
                throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Invalid name or UUID")).createWithContext(reader);
            }
            else {
                this.playerName = s;
                this.includesEntities = false;
            }
            // We're only allowing 1 result because we've specified a player or
            // UUID, and not an @ selector
            this.maxResults = 1;
        };
        let start = reader.getCursor();
        // For some reason, setting these suggestions here triggers twice(?), so don't do that.
        // this.suggestions = ["@p", "@a", "@r", "@s", "@e", "Skepter"];// TODO: Name or selector
        if (reader.canRead() && reader.peek() === "@") {
            reader.skip();
            parseSelector();
        }
        else {
            parseNameOrUUID();
        }
        // Final checks...
        if (this.maxResults > 1 && this.single) {
            reader.setCursor(start);
            if (this.playersOnly) {
                throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Only one player is allowed, but the provided selector allows more than one")).createWithContext(reader);
            }
            else {
                throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Only one entity is allowed, but the provided selector allows more than one")).createWithContext(reader);
            }
        }
        if (this.includesEntities && this.playersOnly /* STUB: !isSelfSelector() */) {
            reader.setCursor(start);
            throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Only players may be affected by this command, but the provided selector includes entities")).createWithContext(reader);
        }
        return this;
    }
    listSuggestions(_context, builder) {
        try {
            this.parse(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.StringReader(builder.getInput()));
        }
        catch (exception) {
        }
        if (this.suggestions.length > 0) {
            if (this.suggestionsModifier !== null) {
                this.suggestionsModifier(builder);
            }
            // We don't use HelperSuggestionProvider because we've already
            // pre-generated suggestions for the specific context, so we don't
            // need to check if it matches or anything, just suggest it!
            for (let suggestion of this.suggestions) {
                builder.suggest(suggestion);
            }
            return builder.buildPromise();
        }
        else {
            return node_brigadier__WEBPACK_IMPORTED_MODULE_0__.Suggestions.empty();
        }
    }
    getExamples() {
        return ["dd12be42-52a9-4a91-a8a1-11c01849e498"];
    }
}


/***/ }),

/***/ "./src/array-extensions.ts":
/*!*********************************!*\
  !*** ./src/array-extensions.ts ***!
  \*********************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
Array.prototype.has = function x(x) {
    return this.length === x;
};



/***/ }),

/***/ "./src/brigadier-extensions.ts":
/*!*************************************!*\
  !*** ./src/brigadier-extensions.ts ***!
  \*************************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony import */ var node_brigadier__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! node-brigadier */ "./node_modules/node-brigadier/dist/index.js");
/* harmony import */ var node_brigadier__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(node_brigadier__WEBPACK_IMPORTED_MODULE_0__);
/* harmony import */ var mojangson_parser__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! mojangson-parser */ "./node_modules/mojangson-parser/dist/es/index.js");
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


node_brigadier__WEBPACK_IMPORTED_MODULE_0__.StringReader.prototype.readLocationLiteral = function readLocationLiteral() {
    function isAllowedLocationLiteral(c) {
        return c === '~' || c === '^';
    }
    let start = this.getCursor();
    while (this.canRead() && (node_brigadier__WEBPACK_IMPORTED_MODULE_0__.StringReader.isAllowedNumber(this.peek()) || isAllowedLocationLiteral(this.peek()))) {
        this.skip();
    }
    let number = this.getString().substring(start, this.getCursor());
    if (number.length === 0) {
        throw node_brigadier__WEBPACK_IMPORTED_MODULE_0__.CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedInt().createWithContext(this);
    }
    if (number.startsWith("~") || number.startsWith("^")) {
        if (number.length === 1) {
            // Accept. We don't care about returning reasonable results
            return 0;
        }
        else {
            number = number.slice(1);
        }
    }
    const result = parseInt(number);
    if (isNaN(result) || result !== parseFloat(number)) {
        this.setCursor(start);
        throw node_brigadier__WEBPACK_IMPORTED_MODULE_0__.CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(this, number);
    }
    else {
        return result;
    }
};
node_brigadier__WEBPACK_IMPORTED_MODULE_0__.StringReader.prototype.readResourceLocation = function readResourceLocation() {
    function isValid(string, predicate) {
        for (let i = 0; i < string.length; i++) {
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
    let resourceLocation = this.getString().substring(start, this.getCursor());
    const resourceLocationParts = resourceLocation.split(":");
    if (resourceLocationParts === undefined) {
        throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(resourceLocation + " is not a valid Resource")).createWithContext(this);
    }
    // Please excuse the abomination that is this Array.has(x : number between 0 and 2)
    // I'd use a switch statement, but TypeScript's type predicates don't pass through that
    // And I think this funky derpy solution is neater than using ! all over the place. It's
    // better to get the compiler to perform array indexing checks than try to tell it "yeah"
    if (resourceLocationParts.has(0)) {
        throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(resourceLocation + " is not a valid Resource")).createWithContext(this);
    }
    else if (resourceLocationParts.has(1)) {
        if (!isValid(resourceLocationParts[0], this.isValidPathChar)) {
            throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Non [a-z0-9/._-] character in path of location: " + resourceLocation)).createWithContext(this);
        }
        return ["minecraft", resourceLocation];
    }
    else if (resourceLocationParts.has(2)) {
        // Check namespace
        if (!isValid(resourceLocationParts[0], this.isValidNamespaceChar)) {
            throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Non [a-z0-9_.-] character in namespace of location: " + resourceLocation)).createWithContext(this);
        }
        // Check path
        if (!isValid(resourceLocationParts[1], this.isValidPathChar)) {
            throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage("Non [a-z0-9/._-] character in path of location: " + resourceLocation)).createWithContext(this);
        }
    }
    else {
        throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(resourceLocation + " is not a valid Resource")).createWithContext(this);
    }
    return [resourceLocationParts[0], resourceLocationParts[1]];
};
node_brigadier__WEBPACK_IMPORTED_MODULE_0__.StringReader.prototype.readMinMaxBounds = function readMinMaxBounds(allowFloats) {
    if (!this.canRead()) {
        throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`Expected value or range of values`)).createWithContext(this);
    }
    const start = this.getCursor();
    let min = null;
    let max = null;
    try {
        min = allowFloats ? this.readFloat() : this.readInt();
    }
    catch (error) {
        // ignore it
    }
    if (this.canRead(2) && this.peek() == '.' && this.peek(1) == '.') {
        this.skip();
        this.skip();
        try {
            max = allowFloats ? this.readFloat() : this.readInt();
        }
        catch (error) {
            // ignore it
        }
    }
    else {
        max = min;
    }
    if (min === null && max === null) {
        this.setCursor(start);
        throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`Expected value or range of values`)).createWithContext(this);
    }
    else {
        if (min === null) {
            min = Number.MIN_SAFE_INTEGER;
        }
        if (max === null) {
            max = Number.MAX_SAFE_INTEGER;
        }
    }
    return [min, max];
};
node_brigadier__WEBPACK_IMPORTED_MODULE_0__.StringReader.prototype.readNBT = function readNBT() {
    const start = this.getCursor();
    let nbt = "";
    while (this.canRead()) {
        nbt += this.read();
        try {
            (0,mojangson_parser__WEBPACK_IMPORTED_MODULE_1__["default"])(nbt);
            break;
        }
        catch (error) {
        }
    }
    try {
        (0,mojangson_parser__WEBPACK_IMPORTED_MODULE_1__["default"])(nbt);
    }
    catch (error) {
        this.setCursor(start);
        throw new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.SimpleCommandExceptionType(new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.LiteralMessage(`${error}`)).createWithContext(this);
    }
    return nbt;
};
node_brigadier__WEBPACK_IMPORTED_MODULE_0__.StringReader.prototype.readNegationCharacter = function readNegationCharacter() {
    this.skipWhitespace();
    if (this.canRead() && this.peek() === '!') {
        this.skip();
        this.skipWhitespace();
        return true;
    }
    return false;
};
node_brigadier__WEBPACK_IMPORTED_MODULE_0__.StringReader.prototype.readTagCharacter = function readTagCharacter() {
    this.skipWhitespace();
    if (this.canRead() && this.peek() === '#') {
        this.skip();
        this.skipWhitespace();
        return true;
    }
    return false;
};
node_brigadier__WEBPACK_IMPORTED_MODULE_0__.StringReader.prototype.isAllowedInResourceLocation = function isAllowedInResourceLocation(c) {
    return ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || c === '_' || c === ':' || c === '/' || c === '.' || c === '-');
};
node_brigadier__WEBPACK_IMPORTED_MODULE_0__.StringReader.prototype.isValidPathChar = function isValidPathChar(c) {
    return (c === '_' || c === '-' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c === '/' || c === '.');
};
node_brigadier__WEBPACK_IMPORTED_MODULE_0__.StringReader.prototype.isValidNamespaceChar = function isValidNamespaceChar(c) {
    return (c === '_' || c === '-' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c === '.');
};


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
        this.root = super.getRoot();
    }
    deleteAll() {
        // @ts-ignore - We can't access the default RootCommandNode, so just sudo do it
        this.root = new node_brigadier__WEBPACK_IMPORTED_MODULE_0__.RootCommandNode();
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
const CHAT_BOX = document.getElementById("chatbox");
const REGISTER_COMMANDS_BUTTON = document.getElementById("register-commands-button");
const APP_ERROR_BOX = document.getElementById("app-error-box");
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
    ["api:entity", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.EntitySelectorArgument(true, false)],
    ["api:entities", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.EntitySelectorArgument(false, false)],
    ["api:player", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.EntitySelectorArgument(true, true)],
    ["api:players", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.EntitySelectorArgument(false, true)],
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
    ["minecraft:block_predicate", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:block_state", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:color", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.ColorArgument()],
    ["minecraft:column_pos", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.ColumnPosArgument()],
    ["minecraft:component", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:dimension", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:entity", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:entity_anchor", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:entity_summon", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:float_range", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:function", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:game_profile", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.PlayerArgument()],
    ["minecraft:int_range", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:item_enchantment", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.EnchantmentArgument()],
    ["minecraft:item_predicate", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:item_slot", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:item_stack", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:message", () => (0,node_brigadier__WEBPACK_IMPORTED_MODULE_0__.greedyString)()],
    ["minecraft:mob_effect", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.PotionEffectArgument()],
    ["minecraft:nbt", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:nbt_compound_tag", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.NBTCompoundArgument()],
    ["minecraft:nbt_path", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:nbt_tag", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:objective", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:objective_criteria", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:operation", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.MathOperationArgument()],
    ["minecraft:particle", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:resource_location", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:rotation", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:score_holder", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:scoreboard_slot", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:swizzle", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:team", () => (0,node_brigadier__WEBPACK_IMPORTED_MODULE_0__.word)()],
    ["minecraft:time", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.TimeArgument()],
    ["minecraft:uuid", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UUIDArgument()],
    ["minecraft:vec2", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
    ["minecraft:vec3", () => new _arguments__WEBPACK_IMPORTED_MODULE_1__.UnimplementedArgument()],
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
            if (argumentGeneratorFunction === undefined) {
                throw new Error("Argument type " + argumentType + " doesn't exist");
            }
            const argumentGeneratorFunctionResult = argumentGeneratorFunction();
            if (argumentGeneratorFunctionResult !== null) {
                return argumentGeneratorFunctionResult;
            }
            else {
                throw new Error("Unimplemented argument: " + argumentType);
            }
        }
    }
    const command = configCommand.split(" ")[0];
    if (command === undefined) {
        throw new Error("Command name doesn't exist!");
    }
    const args = configCommand.split(" ").slice(1);
    let commandToRegister = (0,node_brigadier__WEBPACK_IMPORTED_MODULE_0__.literal)(command);
    let argumentsToRegister = [];
    // From dev/jorel/commandapi/AdvancedConverter.java
    const literalPattern = RegExp(/\((\w+(?:\|\w+)*)\)/);
    const argumentPattern = RegExp(/<(\w+)>\[([a-z:_]+|(?:[0-9\.]+)?\.\.(?:[0-9\.]+)?)\]/);
    for (let arg of args) {
        const matchedLiteral = arg.match(literalPattern);
        const matchedArgument = arg.match(argumentPattern);
        if (matchedLiteral !== null) {
            // It's a literal argument
            const literals = matchedLiteral[1].split("|");
            if (literals.length === 1) {
                argumentsToRegister.unshift((0,node_brigadier__WEBPACK_IMPORTED_MODULE_0__.literal)(literals[0]));
            }
            else if (literals.length > 1) {
                argumentsToRegister.unshift((0,node_brigadier__WEBPACK_IMPORTED_MODULE_0__.argument)(matchedLiteral[1], new _arguments__WEBPACK_IMPORTED_MODULE_1__.MultiLiteralArgument(literals)));
            }
        }
        else if (matchedArgument !== null) {
            // It's a regular argument
            const nodeName = matchedArgument[1];
            const argumentType = matchedArgument[2];
            let convertedArgumentType = convertArgument(argumentType);
            // We're adding arguments in reverse order (last arguments appear
            // at the beginning of the array) because it's much much easier to process
            argumentsToRegister.unshift((0,node_brigadier__WEBPACK_IMPORTED_MODULE_0__.argument)(nodeName, convertedArgumentType));
        }
        else {
            throw new Error(`${arg} has invalid syntax! Valid syntax examples: (literal|literal), <arg>[minecraft:]`);
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
    sel.modify("extend", "backward", "lineboundary");
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
    if (index > 0) {
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
                    const nodeTextContentLength = node.textContent?.length ?? 0;
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
            if (selection !== null) {
                selection.removeAllRanges();
                selection.addRange(range);
            }
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
const onCommandInput = async function () {
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
        // console.log(parsedCommand);
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
            let newText = command ?? "";
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
        // console.log(suggestions)
    }
    // Set the cursor back to where it was. Since commands always start with a
    // forward slash, the only possible "starting caret position" is position 1
    // (in front of the slash)
    if (cursorPos === 0 && rawText.length > 0) {
        cursorPos = rawText.length;
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
COMMAND_INPUT.oninput = onCommandInput;
// We really really don't want new lines in our single-lined command!
COMMAND_INPUT.addEventListener('keydown', (evt) => {
    switch (evt.key) {
        case "Enter":
            evt.preventDefault();
            break;
        case "ArrowDown":
        case "ArrowUp": {
            if (!SUGGESTIONS_BOX.hidden) {
                evt.preventDefault();
                for (let i = 0; i < SUGGESTIONS_BOX.children.length; i++) {
                    if (SUGGESTIONS_BOX.children[i].className === "yellow") {
                        SUGGESTIONS_BOX.children[i].className = "";
                        let selectedElement;
                        if (evt.key == "ArrowDown") {
                            if (i === SUGGESTIONS_BOX.children.length - 1) {
                                selectedElement = SUGGESTIONS_BOX.children[0];
                            }
                            else {
                                selectedElement = SUGGESTIONS_BOX.children[i + 1];
                            }
                        }
                        else {
                            if (i === 0) {
                                selectedElement = SUGGESTIONS_BOX.children[SUGGESTIONS_BOX.children.length - 1];
                            }
                            else {
                                selectedElement = SUGGESTIONS_BOX.children[i - 1];
                            }
                        }
                        selectedElement.className = "yellow";
                        selectedElement.scrollIntoView({ behavior: "smooth", block: "center", inline: "nearest" });
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
            onCommandInput();
            setCursorPosition(COMMAND_INPUT.innerText.length, COMMAND_INPUT);
            break;
        default:
            break;
    }
});
SUGGESTIONS_BOX.addEventListener("mouseover", (evt) => {
    if (!SUGGESTIONS_BOX.hidden) {
        if ([...SUGGESTIONS_BOX.children].includes(evt.target)) {
            for (let i = 0; i < SUGGESTIONS_BOX.children.length; i++) {
                if (SUGGESTIONS_BOX.children[i].className === "yellow") {
                    SUGGESTIONS_BOX.children[i].className = "";
                }
            }
            evt.target.className = "yellow";
            window.dispatchEvent(new Event("suggestionsUpdated"));
        }
    }
});
SUGGESTIONS_BOX.addEventListener("mousedown", (evt) => {
    if (!SUGGESTIONS_BOX.hidden && [...SUGGESTIONS_BOX.children].includes(evt.target)) {
        evt.preventDefault();
        setText(getText(false).slice(1) + COMMAND_INPUT_AUTOCOMPLETE.innerText);
        onCommandInput();
        setCursorPosition(COMMAND_INPUT.innerText.length, COMMAND_INPUT);
    }
});
window.addEventListener("suggestionsUpdated", (_event) => {
    const rawText = COMMAND_INPUT.innerText.replaceAll("\u00a0", " "); // Replace &nbsp; with normal spaces
    if (!SUGGESTIONS_BOX.hidden) {
        const selectedSuggestionText = getSelectedSuggestion().innerText.trim();
        // TODO: This obviously needs to be specific to the current suggestions, not the whole input
        if (rawText !== selectedSuggestionText) {
            const cursorPosition = getCursorPosition();
            let suggestionText = "";
            if (selectedSuggestionText.length > 0) {
                const lastIndexOfStartOfSuggestion = rawText.lastIndexOf(selectedSuggestionText[0]);
                console.log(`Last index of ${selectedSuggestionText[0]} is at index ${lastIndexOfStartOfSuggestion}`);
                // If we can start with it, just display it
                if (lastIndexOfStartOfSuggestion === -1) {
                    suggestionText = selectedSuggestionText;
                }
                else {
                    // Show whatever the rawText doesn't have that starts with whatever the suggestion text is
                    for (let i = 0; i < selectedSuggestionText.length; i++) {
                        if (rawText.endsWith(selectedSuggestionText.slice(0, i))) {
                            // suggest the end
                            suggestionText = selectedSuggestionText.slice(i);
                        }
                    }
                }
            }
            // console.log(rawText + " c.f. " + selectedSuggestionText);
            setText(ChatColor.DARK_GRAY + suggestionText, COMMAND_INPUT_AUTOCOMPLETE);
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
CHAT_BOX.onclick = function onChatBoxClicked() {
    COMMAND_INPUT.focus();
};
const onRegisterCommandsButtonClicked = function onRegisterCommandsButtonClicked() {
    dispatcher.deleteAll();
    COMMANDS.value.split("\n").forEach(registerCommand);
    onCommandInput(); // Run syntax highlighter
};
REGISTER_COMMANDS_BUTTON.onclick = onRegisterCommandsButtonClicked;
window.onerror = function onError(_event, _source, _lineno, _colno, error) {
    const errorElement = document.createElement("span");
    errorElement.className = "errorEntry";
    errorElement.innerText = `${error?.name}: ${error?.message}`;
    const closeButton = document.createElement("span");
    closeButton.innerText = "❌ ";
    closeButton.onclick = function onCloseError(mouseEvent) {
        mouseEvent.target.parentElement?.remove();
    };
    errorElement.prepend(closeButton);
    APP_ERROR_BOX.appendChild(errorElement);
};
/******************************************************************************
 * Entrypoint                                                                 *
 ******************************************************************************/
// Default commands
COMMANDS.value = `fill <pos1>[minecraft:block_pos] <pos2>[minecraft:block_pos] <block>[brigadier:string]
speed (walk|fly) <speed>[0..10] <target>[minecraft:game_profile]
hello <val>[1..20] <color>[minecraft:color]
myfunc <val>[minecraft:mob_effect]
entity <val>[api:entities]
player <val>[api:player]
test_a
test_b
test_c
test_d
test_e`;
onRegisterCommandsButtonClicked();
console.log("Dispatcher", dispatcher.getRoot());

})();

/******/ })()
;
//# sourceMappingURL=data:application/json;charset=utf-8;base64,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O0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSxpQkFBaUI7QUFDakI7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLFNBQVM7QUFDVDtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0Esa0JBQWU7Ozs7Ozs7Ozs7O0FDL1ZGO0FBQ2IsOENBQTZDLEVBQUUsYUFBYSxFQUFDO0FBQzdEO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSxrQkFBZTs7Ozs7Ozs7Ozs7QUNiRjtBQUNiO0FBQ0EsNkNBQTZDO0FBQzdDO0FBQ0EsOENBQTZDLEVBQUUsYUFBYSxFQUFDO0FBQzdELHVDQUF1QyxtQkFBTyxDQUFDLDhFQUFnQjtBQUMvRDtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLGtCQUFlOzs7Ozs7Ozs7OztBQ3RCRjtBQUNiO0FBQ0EsNkNBQTZDO0FBQzdDO0FBQ0EsOENBQTZDLEVBQUUsYUFBYSxFQUFDO0FBQzdELGlEQUFpRCxtQkFBTyxDQUFDLHdIQUFxQztBQUM5RjtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0Esa0JBQWU7Ozs7Ozs7Ozs7O0FDaExGO0FBQ2I7QUFDQSw2Q0FBNkM7QUFDN0M7QUFDQSw4Q0FBNkMsRUFBRSxhQUFhLEVBQUM7QUFDN0QsMkNBQTJDLG1CQUFPLENBQUMsZ0dBQW9CO0FBQ3ZFLDhDQUE4QyxtQkFBTyxDQUFDLHNHQUF1QjtBQUM3RSw0Q0FBNEMsbUJBQU8sQ0FBQyxrR0FBcUI7QUFDekUsNkNBQTZDLG1CQUFPLENBQUMsb0dBQXNCO0FBQzNFLG1CQUFtQjtBQUNuQjtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTs7Ozs7Ozs7Ozs7QUNoQmE7QUFDYiw4Q0FBNkMsRUFBRSxhQUFhLEVBQUM7QUFDN0Q7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLGtCQUFlOzs7Ozs7Ozs7OztBQzVCRjtBQUNiO0FBQ0EsNkNBQTZDO0FBQzdDO0FBQ0EsOENBQTZDLEVBQUUsYUFBYSxFQUFDO0FBQzdELGlEQUFpRCxtQkFBTyxDQUFDLHlIQUFzQztBQUMvRjtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0Esa0JBQWU7Ozs7Ozs7Ozs7O0FDM0RGO0FBQ2I7QUFDQSw2Q0FBNkM7QUFDN0M7QUFDQSw4Q0FBNkMsRUFBRSxhQUFhLEVBQUM7QUFDN0QsaURBQWlELG1CQUFPLENBQUMseUhBQXNDO0FBQy9GO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSxrQkFBZTs7Ozs7Ozs7Ozs7QUMzREY7QUFDYjtBQUNBLDZDQUE2QztBQUM3QztBQUNBLDhDQUE2QyxFQUFFLGFBQWEsRUFBQztBQUM3RCx1Q0FBdUMsbUJBQU8sQ0FBQywrRUFBaUI7QUFDaEU7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLENBQUMsc0NBQXNDLGtCQUFrQixLQUFLO0FBQzlEO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLHdCQUF3QixrQkFBa0I7QUFDMUM7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSxrQkFBZTs7Ozs7Ozs7Ozs7QUNwRUY7QUFDYjtBQUNBLDZDQUE2QztBQUM3QztBQUNBLDhDQUE2QyxFQUFFLGFBQWEsRUFBQztBQUM3RCxzQ0FBc0MsbUJBQU8sQ0FBQyx1RkFBcUI7QUFDbkUsMENBQTBDLG1CQUFPLENBQUMsK0ZBQXlCO0FBQzNFO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLGtCQUFlOzs7Ozs7Ozs7OztBQ2hFRjtBQUNiO0FBQ0EsNkNBQTZDO0FBQzdDO0FBQ0EsOENBQTZDLEVBQUUsYUFBYSxFQUFDO0FBQzdELDZDQUE2QyxtQkFBTyxDQUFDLHFHQUE0QjtBQUNqRiwwQ0FBMEMsbUJBQU8sQ0FBQyw0RkFBbUI7QUFDckU7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSxrQkFBZTtBQUNmLGVBQWU7Ozs7Ozs7Ozs7O0FDOUJGO0FBQ2I7QUFDQSw2Q0FBNkM7QUFDN0M7QUFDQSw4Q0FBNkMsRUFBRSxhQUFhLEVBQUM7QUFDN0QsOENBQThDLG1CQUFPLENBQUMsdUdBQTZCO0FBQ25GLDBDQUEwQyxtQkFBTyxDQUFDLDRGQUFtQjtBQUNyRTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSxrQkFBZTtBQUNmLGdCQUFnQjs7Ozs7Ozs7Ozs7QUN6Q0g7QUFDYjtBQUNBLDZDQUE2QztBQUM3QztBQUNBLDhDQUE2QyxFQUFFLGFBQWEsRUFBQztBQUM3RCxrQ0FBa0MsbUJBQU8sQ0FBQywrRUFBaUI7QUFDM0Q7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSxrQkFBZTs7Ozs7Ozs7Ozs7QUM5RkY7QUFDYjtBQUNBLDZDQUE2QztBQUM3QztBQUNBLDhDQUE2QyxFQUFFLGFBQWEsRUFBQztBQUM3RCxzQ0FBc0MsbUJBQU8sQ0FBQyxvRkFBZTtBQUM3RCx5Q0FBeUMsbUJBQU8sQ0FBQywwRkFBa0I7QUFDbkUsNENBQTRDLG1CQUFPLENBQUMsZ0dBQXFCO0FBQ3pFLDRDQUE0QyxtQkFBTyxDQUFDLGdHQUFxQjtBQUN6RTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLGtCQUFlOzs7Ozs7Ozs7OztBQ3RIRjtBQUNiO0FBQ0EsNkNBQTZDO0FBQzdDO0FBQ0EsOENBQTZDLEVBQUUsYUFBYSxFQUFDO0FBQzdELHNDQUFzQyxtQkFBTyxDQUFDLG9GQUFlO0FBQzdEO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0Esa0JBQWU7Ozs7Ozs7Ozs7O0FDekJGO0FBQ2IsOENBQTZDLEVBQUUsYUFBYSxFQUFDO0FBQzdEO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSxrQkFBZTs7Ozs7Ozs7Ozs7QUN6QkY7QUFDYiw4Q0FBNkMsRUFBRSxhQUFhLEVBQUM7QUFDN0Q7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSw0QkFBNEIsb0RBQW9EO0FBQ2hGO0FBQ0E7QUFDQSxrQkFBZTs7Ozs7Ozs7Ozs7QUM3Q0Y7QUFDYiw4Q0FBNkMsRUFBRSxhQUFhLEVBQUM7QUFDN0Q7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0Esa0JBQWU7Ozs7Ozs7Ozs7O0FDUkY7QUFDYjtBQUNBLDZDQUE2QztBQUM3QztBQUNBLDhDQUE2QyxFQUFFLGFBQWEsRUFBQztBQUM3RCx5Q0FBeUMsbUJBQU8sQ0FBQyxtRkFBbUI7QUFDcEUscURBQXFELG1CQUFPLENBQUMscUhBQThCO0FBQzNGLHNEQUFzRCxtQkFBTyxDQUFDLHVIQUErQjtBQUM3RjtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSxrQkFBZTs7Ozs7Ozs7Ozs7QUN0RkY7QUFDYjtBQUNBLDZDQUE2QztBQUM3QztBQUNBLDhDQUE2QyxFQUFFLGFBQWEsRUFBQztBQUM3RCw0Q0FBNEMsbUJBQU8sQ0FBQyxtR0FBcUI7QUFDekU7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSxrQkFBZTs7Ozs7Ozs7Ozs7QUN2REY7QUFDYjtBQUNBLDZDQUE2QztBQUM3QztBQUNBLDhDQUE2QyxFQUFFLGFBQWEsRUFBQztBQUM3RCxpREFBaUQsbUJBQU8sQ0FBQyw2R0FBMEI7QUFDbkY7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0Esa0JBQWU7Ozs7Ozs7Ozs7O0FDbEJGO0FBQ2I7QUFDQSw2Q0FBNkM7QUFDN0M7QUFDQSw4Q0FBNkMsRUFBRSxhQUFhLEVBQUM7QUFDN0QsaURBQWlELG1CQUFPLENBQUMsNkdBQTBCO0FBQ25GO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLGtCQUFlOzs7Ozs7Ozs7OztBQ3JCRjtBQUNiO0FBQ0EsNkNBQTZDO0FBQzdDO0FBQ0EsOENBQTZDLEVBQUUsYUFBYSxFQUFDO0FBQzdELHFDQUFxQyxtQkFBTyxDQUFDLHFGQUFjO0FBQzNEO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0Esa0NBQWtDO0FBQ2xDO0FBQ0E7QUFDQTtBQUNBO0FBQ0EsY0FBYztBQUNkO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSxrQkFBZTs7Ozs7Ozs7Ozs7QUN2Q0Y7QUFDYjtBQUNBLDZDQUE2QztBQUM3QztBQUNBLDhDQUE2QyxFQUFFLGFBQWEsRUFBQztBQUM3RCxrQ0FBa0MsbUJBQU8sQ0FBQywrRUFBaUI7QUFDM0Q7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSwyQkFBMkI7QUFDM0I7QUFDQTtBQUNBO0FBQ0EsY0FBYztBQUNkO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0Esa0JBQWU7Ozs7Ozs7Ozs7O0FDdEVGO0FBQ2I7QUFDQSw2Q0FBNkM7QUFDN0M7QUFDQSw4Q0FBNkMsRUFBRSxhQUFhLEVBQUM7QUFDN0Qsa0NBQWtDLG1CQUFPLENBQUMsK0VBQWlCO0FBQzNELHNDQUFzQyxtQkFBTyxDQUFDLDZGQUF3QjtBQUN0RTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLDRCQUE0QjtBQUM1QjtBQUNBLG9EQUFvRDtBQUNwRDtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLGtCQUFlOzs7Ozs7Ozs7OztBQ3JFRjtBQUNiO0FBQ0EsNkNBQTZDO0FBQzdDO0FBQ0EsOENBQTZDLEVBQUUsYUFBYSxFQUFDO0FBQzdELHNDQUFzQyxtQkFBTyxDQUFDLDZGQUF3QjtBQUN0RSxxQ0FBcUMsbUJBQU8sQ0FBQyxxRkFBYztBQUMzRCxzQ0FBc0MsbUJBQU8sQ0FBQyx1RkFBZTtBQUM3RCw0Q0FBNEMsbUJBQU8sQ0FBQyxtR0FBcUI7QUFDekU7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSxrQkFBZTs7Ozs7Ozs7Ozs7QUNwREY7QUFDYjtBQUNBLDZDQUE2QztBQUM3QztBQUNBLDhDQUE2QyxFQUFFLGFBQWEsRUFBQztBQUM3RCxrQ0FBa0MsbUJBQU8sQ0FBQywrRUFBaUI7QUFDM0QsdUNBQXVDLG1CQUFPLENBQUMsK0VBQWlCO0FBQ2hFLGtEQUFrRCxtQkFBTyxDQUFDLHFIQUFvQztBQUM5Rix5Q0FBeUMsbUJBQU8sQ0FBQyxtR0FBMkI7QUFDNUUsc0NBQXNDLG1CQUFPLENBQUMsbUdBQTJCO0FBQ3pFLHNDQUFzQyxtQkFBTyxDQUFDLGlGQUFlO0FBQzdEO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSxrQkFBZTs7Ozs7Ozs7Ozs7QUM5RkY7QUFDYjtBQUNBLDZDQUE2QztBQUM3QztBQUNBLDhDQUE2QyxFQUFFLGFBQWEsRUFBQztBQUM3RCxrQ0FBa0MsbUJBQU8sQ0FBQywrRUFBaUI7QUFDM0Q7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLGtCQUFlOzs7Ozs7Ozs7OztBQ25JRjtBQUNiO0FBQ0EsNkNBQTZDO0FBQzdDO0FBQ0EsOENBQTZDLEVBQUUsYUFBYSxFQUFDO0FBQzdELHNDQUFzQyxtQkFBTyxDQUFDLGlGQUFlO0FBQzdELHVDQUF1QyxtQkFBTyxDQUFDLCtFQUFpQjtBQUNoRSxpREFBaUQsbUJBQU8sQ0FBQyxtSEFBbUM7QUFDNUYsc0NBQXNDLG1CQUFPLENBQUMsNkZBQXdCO0FBQ3RFLGlEQUFpRCxtQkFBTyxDQUFDLHlIQUFzQztBQUMvRixzQ0FBc0MsbUJBQU8sQ0FBQyxtR0FBMkI7QUFDekU7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLGtCQUFlOzs7Ozs7Ozs7OztBQzNGRjtBQUNiO0FBQ0EsNkNBQTZDO0FBQzdDO0FBQ0EsOENBQTZDLEVBQUUsYUFBYSxFQUFDO0FBQzdELHNDQUFzQyxtQkFBTyxDQUFDLGlGQUFlO0FBQzdELHNDQUFzQyxtQkFBTyxDQUFDLG1HQUEyQjtBQUN6RTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0Esa0JBQWU7Ozs7Ozs7Ozs7O0FDaERGO0FBQ2IsOENBQTZDLEVBQUUsYUFBYSxFQUFDO0FBQzdEO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLGtCQUFlO0FBQ2Y7QUFDQTtBQUNBO0FB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QUFDO0FBRVIsK0JBQStCLEVBQUUsQ0FBQztBQUNsQyxPQUFPLENBQUMsR0FBRyxDQUFDLFlBQVksRUFBRSxVQUFVLENBQUMsT0FBTyxFQUFFLENBQUMiLCJzb3VyY2VzIjpbIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL25vZGVfbW9kdWxlcy9tb2phbmdzb24tcGFyc2VyL2Rpc3QvZXMvaW5kZXguanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9pbmRleC5qcyIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL25vZGVfbW9kdWxlcy9ub2RlLWJyaWdhZGllci9kaXN0L2xpYi9Db21tYW5kRGlzcGF0Y2hlci5qcyIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL25vZGVfbW9kdWxlcy9ub2RlLWJyaWdhZGllci9kaXN0L2xpYi9MaXRlcmFsTWVzc2FnZS5qcyIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL25vZGVfbW9kdWxlcy9ub2RlLWJyaWdhZGllci9kaXN0L2xpYi9QYXJzZVJlc3VsdHMuanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvU3RyaW5nUmVhZGVyLmpzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vbm9kZV9tb2R1bGVzL25vZGUtYnJpZ2FkaWVyL2Rpc3QvbGliL2FyZ3VtZW50cy9Bcmd1bWVudFR5cGUuanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvYXJndW1lbnRzL0Jvb2xBcmd1bWVudFR5cGUuanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvYXJndW1lbnRzL0Zsb2F0QXJndW1lbnRUeXBlLmpzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vbm9kZV9tb2R1bGVzL25vZGUtYnJpZ2FkaWVyL2Rpc3QvbGliL2FyZ3VtZW50cy9JbnRlZ2VyQXJndW1lbnRUeXBlLmpzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vbm9kZV9tb2R1bGVzL25vZGUtYnJpZ2FkaWVyL2Rpc3QvbGliL2FyZ3VtZW50cy9TdHJpbmdBcmd1bWVudFR5cGUuanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvYnVpbGRlci9Bcmd1bWVudEJ1aWxkZXIuanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvYnVpbGRlci9MaXRlcmFsQXJndW1lbnRCdWlsZGVyLmpzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vbm9kZV9tb2R1bGVzL25vZGUtYnJpZ2FkaWVyL2Rpc3QvbGliL2J1aWxkZXIvUmVxdWlyZWRBcmd1bWVudEJ1aWxkZXIuanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvY29udGV4dC9Db21tYW5kQ29udGV4dC5qcyIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL25vZGVfbW9kdWxlcy9ub2RlLWJyaWdhZGllci9kaXN0L2xpYi9jb250ZXh0L0NvbW1hbmRDb250ZXh0QnVpbGRlci5qcyIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL25vZGVfbW9kdWxlcy9ub2RlLWJyaWdhZGllci9kaXN0L2xpYi9jb250ZXh0L1BhcnNlZEFyZ3VtZW50LmpzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vbm9kZV9tb2R1bGVzL25vZGUtYnJpZ2FkaWVyL2Rpc3QvbGliL2NvbnRleHQvUGFyc2VkQ29tbWFuZE5vZGUuanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvY29udGV4dC9TdHJpbmdSYW5nZS5qcyIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL25vZGVfbW9kdWxlcy9ub2RlLWJyaWdhZGllci9kaXN0L2xpYi9jb250ZXh0L1N1Z2dlc3Rpb25Db250ZXh0LmpzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vbm9kZV9tb2R1bGVzL25vZGUtYnJpZ2FkaWVyL2Rpc3QvbGliL2V4Y2VwdGlvbnMvQnVpbHRJbkV4Y2VwdGlvbnMuanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvZXhjZXB0aW9ucy9Db21tYW5kU3ludGF4RXhjZXB0aW9uLmpzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vbm9kZV9tb2R1bGVzL25vZGUtYnJpZ2FkaWVyL2Rpc3QvbGliL2V4Y2VwdGlvbnMvRHluYW1pY0NvbW1hbmRFeGNlcHRpb25UeXBlLmpzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vbm9kZV9tb2R1bGVzL25vZGUtYnJpZ2FkaWVyL2Rpc3QvbGliL2V4Y2VwdGlvbnMvU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGUuanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvc3VnZ2VzdGlvbi9JbnRlZ2VyU3VnZ2VzdGlvbi5qcyIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL25vZGVfbW9kdWxlcy9ub2RlLWJyaWdhZGllci9kaXN0L2xpYi9zdWdnZXN0aW9uL1N1Z2dlc3Rpb24uanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvc3VnZ2VzdGlvbi9TdWdnZXN0aW9ucy5qcyIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL25vZGVfbW9kdWxlcy9ub2RlLWJyaWdhZGllci9kaXN0L2xpYi9zdWdnZXN0aW9uL1N1Z2dlc3Rpb25zQnVpbGRlci5qcyIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL25vZGVfbW9kdWxlcy9ub2RlLWJyaWdhZGllci9kaXN0L2xpYi90cmVlL0FyZ3VtZW50Q29tbWFuZE5vZGUuanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9ub2RlX21vZHVsZXMvbm9kZS1icmlnYWRpZXIvZGlzdC9saWIvdHJlZS9Db21tYW5kTm9kZS5qcyIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL25vZGVfbW9kdWxlcy9ub2RlLWJyaWdhZGllci9kaXN0L2xpYi90cmVlL0xpdGVyYWxDb21tYW5kTm9kZS5qcyIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL25vZGVfbW9kdWxlcy9ub2RlLWJyaWdhZGllci9kaXN0L2xpYi90cmVlL1Jvb3RDb21tYW5kTm9kZS5qcyIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL25vZGVfbW9kdWxlcy9ub2RlLWJyaWdhZGllci9kaXN0L2xpYi91dGlsL2lzRXF1YWwuanMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9zcmMvYXJndW1lbnRzLnRzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyLy4vc3JjL2FycmF5LWV4dGVuc2lvbnMudHMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvLi9zcmMvYnJpZ2FkaWVyLWV4dGVuc2lvbnMudHMiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvd2VicGFjay9ib290c3RyYXAiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvd2VicGFjay9ydW50aW1lL2NvbXBhdCBnZXQgZGVmYXVsdCBleHBvcnQiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvd2VicGFjay9ydW50aW1lL2RlZmluZSBwcm9wZXJ0eSBnZXR0ZXJzIiwid2VicGFjazovL2NvbW1hbmR2aXN1YWxpemVyL3dlYnBhY2svcnVudGltZS9oYXNPd25Qcm9wZXJ0eSBzaG9ydGhhbmQiLCJ3ZWJwYWNrOi8vY29tbWFuZHZpc3VhbGl6ZXIvd2VicGFjay9ydW50aW1lL21ha2UgbmFtZXNwYWNlIG9iamVjdCIsIndlYnBhY2s6Ly9jb21tYW5kdmlzdWFsaXplci8uL3NyYy9pbmRleC50cyJdLCJzb3VyY2VzQ29udGVudCI6WyJjbGFzcyBOYnQge1xyXG4gICAgc3RhdGljIGVhdChsZW5ndGgpIHtcclxuICAgICAgICBOYnQudGV4dCA9IE5idC50ZXh0LnN1YnN0cihsZW5ndGgpO1xyXG4gICAgfVxyXG4gICAgc3RhdGljIHBhcnNlT2JqZWN0KCkge1xyXG4gICAgICAgIGNvbnN0IG9iaiA9IHt9O1xyXG4gICAgICAgIGlmIChOYnQudGVzdENsb3NlQ3VybHkoKSkge1xyXG4gICAgICAgICAgICByZXR1cm4gb2JqO1xyXG4gICAgICAgIH1cclxuICAgICAgICB3aGlsZSAodHJ1ZSkge1xyXG4gICAgICAgICAgICBjb25zdCB0YWdOYW1lID0gTmJ0LnBhcnNlS2V5KCk7XHJcbiAgICAgICAgICAgIE5idC5lYXRDb2xvbigpO1xyXG4gICAgICAgICAgICBjb25zdCB2YWx1ZSA9IE5idC5wYXJzZVZhbHVlKCk7XHJcbiAgICAgICAgICAgIG9ialt0YWdOYW1lXSA9IHZhbHVlO1xyXG4gICAgICAgICAgICBpZiAoTmJ0LnRlc3RDbG9zZUN1cmx5KCkpIHtcclxuICAgICAgICAgICAgICAgIGJyZWFrO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIE5idC5lYXRDb21tYSgpO1xyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gb2JqO1xyXG4gICAgfVxyXG4gICAgc3RhdGljIHRlc3RJbmRleEFycmF5KCkge1xyXG4gICAgICAgIGNvbnN0IG1hdGNoID0gTmJ0LnRleHQubWF0Y2goL1xccypcXGQrXFxzKjovKTtcclxuICAgICAgICBpZiAobWF0Y2gpIHtcclxuICAgICAgICAgICAgaWYgKG1hdGNoKSB7XHJcbiAgICAgICAgICAgICAgICBOYnQuZWF0KG1hdGNoWzBdLmxlbmd0aCk7XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgdGVzdFR5cGVBcnJheSgpIHtcclxuICAgICAgICBjb25zdCBtYXRjaCA9IE5idC50ZXh0Lm1hdGNoKC9eXFxzKltJTEJdXFxzKjsvKTtcclxuICAgICAgICBpZiAobWF0Y2gpIHtcclxuICAgICAgICAgICAgaWYgKG1hdGNoKSB7XHJcbiAgICAgICAgICAgICAgICBOYnQuZWF0KG1hdGNoWzBdLmxlbmd0aCk7XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgcGFyc2VBcnJheSgpIHtcclxuICAgICAgICBjb25zdCBhcnJheSA9IFtdO1xyXG4gICAgICAgIGlmIChOYnQudGVzdENsb3NlU3F1YXJlKCkpIHtcclxuICAgICAgICAgICAgcmV0dXJuIGFycmF5O1xyXG4gICAgICAgIH1cclxuICAgICAgICB3aGlsZSAodHJ1ZSkge1xyXG4gICAgICAgICAgICBOYnQudGVzdEluZGV4QXJyYXkoKTtcclxuICAgICAgICAgICAgTmJ0LnRlc3RUeXBlQXJyYXkoKTtcclxuICAgICAgICAgICAgaWYgKE5idC50ZXN0QmVnaW5TcXVhcmUoKSkge1xyXG4gICAgICAgICAgICAgICAgYXJyYXkucHVzaChOYnQucGFyc2VPYmplY3QoKSk7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgZWxzZSB7XHJcbiAgICAgICAgICAgICAgICBhcnJheS5wdXNoKE5idC5wYXJzZVZhbHVlKCkpO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIGlmIChOYnQudGVzdENsb3NlU3F1YXJlKCkpIHtcclxuICAgICAgICAgICAgICAgIGJyZWFrO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIE5idC5lYXRDb21tYSgpO1xyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gYXJyYXk7XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgZWF0Q29sb24oKSB7XHJcbiAgICAgICAgY29uc3QgbWF0Y2ggPSBOYnQudGV4dC5tYXRjaCgvXlxccyo6Lyk7XHJcbiAgICAgICAgaWYgKG1hdGNoKSB7XHJcbiAgICAgICAgICAgIE5idC5lYXQobWF0Y2hbMF0ubGVuZ3RoKTtcclxuICAgICAgICAgICAgcmV0dXJuO1xyXG4gICAgICAgIH1cclxuICAgICAgICB0aHJvdyBuZXcgRXJyb3IoJ2xhY2sgb2YgY29sb24nKTtcclxuICAgIH1cclxuICAgIHN0YXRpYyBlYXRDb21tYSgpIHtcclxuICAgICAgICBjb25zdCBtYXRjaCA9IE5idC50ZXh0Lm1hdGNoKC9eXFxzKiwvKTtcclxuICAgICAgICBpZiAobWF0Y2gpIHtcclxuICAgICAgICAgICAgTmJ0LmVhdChtYXRjaFswXS5sZW5ndGgpO1xyXG4gICAgICAgICAgICByZXR1cm47XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHRocm93IG5ldyBFcnJvcignbGFjayBvZiBjb21tYScpO1xyXG4gICAgfVxyXG4gICAgc3RhdGljIHRlc3RCZWdpbkN1cmx5KCkge1xyXG4gICAgICAgIGNvbnN0IG1hdGNoID0gTmJ0LnRleHQubWF0Y2goL15cXHMqey8pO1xyXG4gICAgICAgIGlmIChtYXRjaCkge1xyXG4gICAgICAgICAgICBOYnQuZWF0KG1hdGNoWzBdLmxlbmd0aCk7XHJcbiAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgdGVzdENsb3NlQ3VybHkoKSB7XHJcbiAgICAgICAgY29uc3QgbWF0Y2ggPSBOYnQudGV4dC5tYXRjaCgvXlxccyp9Lyk7XHJcbiAgICAgICAgaWYgKG1hdGNoKSB7XHJcbiAgICAgICAgICAgIE5idC5lYXQobWF0Y2hbMF0ubGVuZ3RoKTtcclxuICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgIH1cclxuICAgIHN0YXRpYyB0ZXN0QmVnaW5TcXVhcmUoKSB7XHJcbiAgICAgICAgY29uc3QgbWF0Y2ggPSBOYnQudGV4dC5tYXRjaCgvXlxccypcXFsvKTtcclxuICAgICAgICBpZiAobWF0Y2gpIHtcclxuICAgICAgICAgICAgTmJ0LmVhdChtYXRjaFswXS5sZW5ndGgpO1xyXG4gICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICB9XHJcbiAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgfVxyXG4gICAgc3RhdGljIHRlc3RDbG9zZVNxdWFyZSgpIHtcclxuICAgICAgICBjb25zdCBtYXRjaCA9IE5idC50ZXh0Lm1hdGNoKC9eXFxzKlxcXS8pO1xyXG4gICAgICAgIGlmIChtYXRjaCkge1xyXG4gICAgICAgICAgICBOYnQuZWF0KG1hdGNoWzBdLmxlbmd0aCk7XHJcbiAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgcGFyc2VLZXkoKSB7XHJcbiAgICAgICAgY29uc3QgbWF0Y2ggPSBOYnQudGV4dC5tYXRjaCgvXlxccyooW2Etel1bYS16MC05XSopL2kpO1xyXG4gICAgICAgIGlmIChtYXRjaCkge1xyXG4gICAgICAgICAgICBjb25zdCBbXywga2V5XSA9IG1hdGNoO1xyXG4gICAgICAgICAgICBOYnQuZWF0KF8ubGVuZ3RoKTtcclxuICAgICAgICAgICAgcmV0dXJuIGtleTtcclxuICAgICAgICB9XHJcbiAgICAgICAgdGhyb3cgbmV3IEVycm9yKGBQcm9wZXJ0eSBuYW1lIGlzIGludmFsaWQgYXQgXCIke05idC50ZXh0LnNsaWNlKDAsIDE1KX1cImApO1xyXG4gICAgfVxyXG4gICAgc3RhdGljIHBhcnNlVmFsdWUoKSB7XHJcbiAgICAgICAgaWYgKE5idC50ZXN0QmVnaW5DdXJseSgpKSB7XHJcbiAgICAgICAgICAgIHJldHVybiBOYnQucGFyc2VPYmplY3QoKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgaWYgKE5idC50ZXN0QmVnaW5TcXVhcmUoKSkge1xyXG4gICAgICAgICAgICByZXR1cm4gTmJ0LnBhcnNlQXJyYXkoKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgY29uc3QgbWF0Y2ggPSBOYnQudGV4dC5tYXRjaCgvXlxccyooWzAtOWEtekEtWl9cXC1cXCRcXC5dKyl8XlxccyooXCIoPzpcXFxcXFxcInxcXFxcXFxcXHxbXlxcXFxcXFxcXSkqP1wiKXxeXFxzKihcXCcoPzpcXFxcXFxcInxcXFxcXFxcXHxbXlxcXFxcXFxcXSkqPycpLyk7XHJcbiAgICAgICAgaWYgKG1hdGNoKSB7XHJcbiAgICAgICAgICAgIGNvbnN0IFtfLCBiYXJlLCBkb3VibGUsIHNpbmdsZV0gPSBtYXRjaDtcclxuICAgICAgICAgICAgTmJ0LmVhdChfLmxlbmd0aCk7XHJcbiAgICAgICAgICAgIGlmIChiYXJlKSB7XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gYmFyZTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICBpZiAoZG91YmxlKSB7XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gZG91YmxlLnNsaWNlKDEsIC0xKS5yZXBsYWNlKC9cXFxcXFxcXC9nLCAnXFxcXCcpLnJlcGxhY2UoL1xcXFxcXFwiL2csICdcIicpO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIGlmIChzaW5nbGUpIHtcclxuICAgICAgICAgICAgICAgIHJldHVybiBzaW5nbGUuc2xpY2UoMSwgLTEpO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHRocm93IG5ldyBFcnJvcihgdmFsdWUgaXMgaW52YWxpZCBhdCBcIiR7TmJ0LnRleHQuc2xpY2UoMCwgMTUpfVwiYCk7XHJcbiAgICB9XHJcbn1cclxuTmJ0LnRleHQgPSAnJztcclxuZnVuY3Rpb24gcGFyc2VyKHRleHQpIHtcclxuICAgIE5idC50ZXh0ID0gdGV4dDtcclxuICAgIGlmIChOYnQudGVzdEJlZ2luQ3VybHkoKSkge1xyXG4gICAgICAgIHJldHVybiBOYnQucGFyc2VPYmplY3QoKTtcclxuICAgIH1cclxuICAgIHRocm93IG5ldyBFcnJvcignZXhwZWN0IGFuIG9iamVjdCcpO1xyXG59XG5cbmV4cG9ydCB7IHBhcnNlciBhcyBkZWZhdWx0IH07XG4iLCJcInVzZSBzdHJpY3RcIjtcclxudmFyIF9faW1wb3J0RGVmYXVsdCA9ICh0aGlzICYmIHRoaXMuX19pbXBvcnREZWZhdWx0KSB8fCBmdW5jdGlvbiAobW9kKSB7XHJcbiAgICByZXR1cm4gKG1vZCAmJiBtb2QuX19lc01vZHVsZSkgPyBtb2QgOiB7IFwiZGVmYXVsdFwiOiBtb2QgfTtcclxufTtcclxudmFyIF9faW1wb3J0U3RhciA9ICh0aGlzICYmIHRoaXMuX19pbXBvcnRTdGFyKSB8fCBmdW5jdGlvbiAobW9kKSB7XHJcbiAgICBpZiAobW9kICYmIG1vZC5fX2VzTW9kdWxlKSByZXR1cm4gbW9kO1xyXG4gICAgdmFyIHJlc3VsdCA9IHt9O1xyXG4gICAgaWYgKG1vZCAhPSBudWxsKSBmb3IgKHZhciBrIGluIG1vZCkgaWYgKE9iamVjdC5oYXNPd25Qcm9wZXJ0eS5jYWxsKG1vZCwgaykpIHJlc3VsdFtrXSA9IG1vZFtrXTtcclxuICAgIHJlc3VsdFtcImRlZmF1bHRcIl0gPSBtb2Q7XHJcbiAgICByZXR1cm4gcmVzdWx0O1xyXG59O1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNvbnN0IENvbW1hbmREaXNwYXRjaGVyXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vbGliL0NvbW1hbmREaXNwYXRjaGVyXCIpKTtcclxuY29uc3QgTGl0ZXJhbE1lc3NhZ2VfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9saWIvTGl0ZXJhbE1lc3NhZ2VcIikpO1xyXG5jb25zdCBQYXJzZVJlc3VsdHNfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9saWIvUGFyc2VSZXN1bHRzXCIpKTtcclxuY29uc3QgU3RyaW5nUmVhZGVyXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vbGliL1N0cmluZ1JlYWRlclwiKSk7XHJcbmNvbnN0IEFyZ3VtZW50VHlwZV8xID0gcmVxdWlyZShcIi4vbGliL2FyZ3VtZW50cy9Bcmd1bWVudFR5cGVcIik7XHJcbmNvbnN0IExpdGVyYWxBcmd1bWVudEJ1aWxkZXJfMSA9IF9faW1wb3J0U3RhcihyZXF1aXJlKFwiLi9saWIvYnVpbGRlci9MaXRlcmFsQXJndW1lbnRCdWlsZGVyXCIpKTtcclxuY29uc3QgUmVxdWlyZWRBcmd1bWVudEJ1aWxkZXJfMSA9IF9faW1wb3J0U3RhcihyZXF1aXJlKFwiLi9saWIvYnVpbGRlci9SZXF1aXJlZEFyZ3VtZW50QnVpbGRlclwiKSk7XHJcbmNvbnN0IENvbW1hbmRDb250ZXh0XzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vbGliL2NvbnRleHQvQ29tbWFuZENvbnRleHRcIikpO1xyXG5jb25zdCBDb21tYW5kQ29udGV4dEJ1aWxkZXJfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9saWIvY29udGV4dC9Db21tYW5kQ29udGV4dEJ1aWxkZXJcIikpO1xyXG5jb25zdCBQYXJzZWRBcmd1bWVudF8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL2xpYi9jb250ZXh0L1BhcnNlZEFyZ3VtZW50XCIpKTtcclxuY29uc3QgUGFyc2VkQ29tbWFuZE5vZGVfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9saWIvY29udGV4dC9QYXJzZWRDb21tYW5kTm9kZVwiKSk7XHJcbmNvbnN0IFN0cmluZ1JhbmdlXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vbGliL2NvbnRleHQvU3RyaW5nUmFuZ2VcIikpO1xyXG5jb25zdCBTdWdnZXN0aW9uQ29udGV4dF8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL2xpYi9jb250ZXh0L1N1Z2dlc3Rpb25Db250ZXh0XCIpKTtcclxuY29uc3QgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbl8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL2xpYi9leGNlcHRpb25zL0NvbW1hbmRTeW50YXhFeGNlcHRpb25cIikpO1xyXG5jb25zdCBEeW5hbWljQ29tbWFuZEV4Y2VwdGlvblR5cGVfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9saWIvZXhjZXB0aW9ucy9EeW5hbWljQ29tbWFuZEV4Y2VwdGlvblR5cGVcIikpO1xyXG5jb25zdCBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZV8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL2xpYi9leGNlcHRpb25zL1NpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlXCIpKTtcclxuY29uc3QgU3VnZ2VzdGlvbl8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL2xpYi9zdWdnZXN0aW9uL1N1Z2dlc3Rpb25cIikpO1xyXG5jb25zdCBTdWdnZXN0aW9uc18xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL2xpYi9zdWdnZXN0aW9uL1N1Z2dlc3Rpb25zXCIpKTtcclxuY29uc3QgU3VnZ2VzdGlvbnNCdWlsZGVyXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vbGliL3N1Z2dlc3Rpb24vU3VnZ2VzdGlvbnNCdWlsZGVyXCIpKTtcclxuY29uc3QgQXJndW1lbnRDb21tYW5kTm9kZV8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL2xpYi90cmVlL0FyZ3VtZW50Q29tbWFuZE5vZGVcIikpO1xyXG5jb25zdCBMaXRlcmFsQ29tbWFuZE5vZGVfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9saWIvdHJlZS9MaXRlcmFsQ29tbWFuZE5vZGVcIikpO1xyXG5jb25zdCBSb290Q29tbWFuZE5vZGVfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9saWIvdHJlZS9Sb290Q29tbWFuZE5vZGVcIikpO1xyXG5jb25zdCB7IHdvcmQsIHN0cmluZywgZ3JlZWR5U3RyaW5nLCBib29sLCBpbnRlZ2VyLCBmbG9hdCB9ID0gQXJndW1lbnRUeXBlXzEuRGVmYXVsdFR5cGU7XHJcbm1vZHVsZS5leHBvcnRzID0ge1xyXG4gICAgZGlzcGF0Y2hlcjogbmV3IENvbW1hbmREaXNwYXRjaGVyXzEuZGVmYXVsdCgpLFxyXG4gICAgd29yZCwgc3RyaW5nLCBncmVlZHlTdHJpbmcsIGJvb2wsIGludGVnZXIsIGZsb2F0LFxyXG4gICAgbGl0ZXJhbDogTGl0ZXJhbEFyZ3VtZW50QnVpbGRlcl8xLmxpdGVyYWwsIGFyZ3VtZW50OiBSZXF1aXJlZEFyZ3VtZW50QnVpbGRlcl8xLmFyZ3VtZW50LFxyXG4gICAgQ29tbWFuZERpc3BhdGNoZXI6IENvbW1hbmREaXNwYXRjaGVyXzEuZGVmYXVsdCxcclxuICAgIExpdGVyYWxNZXNzYWdlOiBMaXRlcmFsTWVzc2FnZV8xLmRlZmF1bHQsXHJcbiAgICBQYXJzZVJlc3VsdHM6IFBhcnNlUmVzdWx0c18xLmRlZmF1bHQsXHJcbiAgICBTdHJpbmdSZWFkZXI6IFN0cmluZ1JlYWRlcl8xLmRlZmF1bHQsXHJcbiAgICBMaXRlcmFsQXJndW1lbnRCdWlsZGVyOiBMaXRlcmFsQXJndW1lbnRCdWlsZGVyXzEuZGVmYXVsdCxcclxuICAgIFJlcXVpcmVkQXJndW1lbnRCdWlsZGVyOiBSZXF1aXJlZEFyZ3VtZW50QnVpbGRlcl8xLmRlZmF1bHQsXHJcbiAgICBDb21tYW5kQ29udGV4dDogQ29tbWFuZENvbnRleHRfMS5kZWZhdWx0LFxyXG4gICAgQ29tbWFuZENvbnRleHRCdWlsZGVyOiBDb21tYW5kQ29udGV4dEJ1aWxkZXJfMS5kZWZhdWx0LFxyXG4gICAgUGFyc2VkQXJndW1lbnQ6IFBhcnNlZEFyZ3VtZW50XzEuZGVmYXVsdCxcclxuICAgIFBhcnNlZENvbW1hbmROb2RlOiBQYXJzZWRDb21tYW5kTm9kZV8xLmRlZmF1bHQsXHJcbiAgICBTdHJpbmdSYW5nZTogU3RyaW5nUmFuZ2VfMS5kZWZhdWx0LFxyXG4gICAgU3VnZ2VzdGlvbnNDb250ZXh0OiBTdWdnZXN0aW9uQ29udGV4dF8xLmRlZmF1bHQsXHJcbiAgICBDb21tYW5kU3ludGF4RXhjZXB0aW9uOiBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEuZGVmYXVsdCxcclxuICAgIFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlOiBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZV8xLmRlZmF1bHQsXHJcbiAgICBEeW5hbWljQ29tbWFuZEV4Y2VwdGlvblR5cGU6IER5bmFtaWNDb21tYW5kRXhjZXB0aW9uVHlwZV8xLmRlZmF1bHQsXHJcbiAgICBTdWdnZXN0aW9uOiBTdWdnZXN0aW9uXzEuZGVmYXVsdCxcclxuICAgIFN1Z2dlc3Rpb25zOiBTdWdnZXN0aW9uc18xLmRlZmF1bHQsXHJcbiAgICBTdWdnZXN0aW9uc0J1aWxkZXI6IFN1Z2dlc3Rpb25zQnVpbGRlcl8xLmRlZmF1bHQsXHJcbiAgICBBcmd1bWVudENvbW1hbmROb2RlOiBBcmd1bWVudENvbW1hbmROb2RlXzEuZGVmYXVsdCxcclxuICAgIExpdGVyYWxDb21tYW5kTm9kZTogTGl0ZXJhbENvbW1hbmROb2RlXzEuZGVmYXVsdCxcclxuICAgIFJvb3RDb21tYW5kTm9kZTogUm9vdENvbW1hbmROb2RlXzEuZGVmYXVsdFxyXG59O1xyXG4iLCJcInVzZSBzdHJpY3RcIjtcclxudmFyIF9fYXdhaXRlciA9ICh0aGlzICYmIHRoaXMuX19hd2FpdGVyKSB8fCBmdW5jdGlvbiAodGhpc0FyZywgX2FyZ3VtZW50cywgUCwgZ2VuZXJhdG9yKSB7XHJcbiAgICByZXR1cm4gbmV3IChQIHx8IChQID0gUHJvbWlzZSkpKGZ1bmN0aW9uIChyZXNvbHZlLCByZWplY3QpIHtcclxuICAgICAgICBmdW5jdGlvbiBmdWxmaWxsZWQodmFsdWUpIHsgdHJ5IHsgc3RlcChnZW5lcmF0b3IubmV4dCh2YWx1ZSkpOyB9IGNhdGNoIChlKSB7IHJlamVjdChlKTsgfSB9XHJcbiAgICAgICAgZnVuY3Rpb24gcmVqZWN0ZWQodmFsdWUpIHsgdHJ5IHsgc3RlcChnZW5lcmF0b3JbXCJ0aHJvd1wiXSh2YWx1ZSkpOyB9IGNhdGNoIChlKSB7IHJlamVjdChlKTsgfSB9XHJcbiAgICAgICAgZnVuY3Rpb24gc3RlcChyZXN1bHQpIHsgcmVzdWx0LmRvbmUgPyByZXNvbHZlKHJlc3VsdC52YWx1ZSkgOiBuZXcgUChmdW5jdGlvbiAocmVzb2x2ZSkgeyByZXNvbHZlKHJlc3VsdC52YWx1ZSk7IH0pLnRoZW4oZnVsZmlsbGVkLCByZWplY3RlZCk7IH1cclxuICAgICAgICBzdGVwKChnZW5lcmF0b3IgPSBnZW5lcmF0b3IuYXBwbHkodGhpc0FyZywgX2FyZ3VtZW50cyB8fCBbXSkpLm5leHQoKSk7XHJcbiAgICB9KTtcclxufTtcclxudmFyIF9faW1wb3J0RGVmYXVsdCA9ICh0aGlzICYmIHRoaXMuX19pbXBvcnREZWZhdWx0KSB8fCBmdW5jdGlvbiAobW9kKSB7XHJcbiAgICByZXR1cm4gKG1vZCAmJiBtb2QuX19lc01vZHVsZSkgPyBtb2QgOiB7IFwiZGVmYXVsdFwiOiBtb2QgfTtcclxufTtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5jb25zdCBQYXJzZVJlc3VsdHNfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9QYXJzZVJlc3VsdHNcIikpO1xyXG5jb25zdCBDb21tYW5kQ29udGV4dEJ1aWxkZXJfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9jb250ZXh0L0NvbW1hbmRDb250ZXh0QnVpbGRlclwiKSk7XHJcbmNvbnN0IENvbW1hbmRTeW50YXhFeGNlcHRpb25fMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9leGNlcHRpb25zL0NvbW1hbmRTeW50YXhFeGNlcHRpb25cIikpO1xyXG5jb25zdCBTdWdnZXN0aW9uc18xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL3N1Z2dlc3Rpb24vU3VnZ2VzdGlvbnNcIikpO1xyXG5jb25zdCBTdWdnZXN0aW9uc0J1aWxkZXJfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9zdWdnZXN0aW9uL1N1Z2dlc3Rpb25zQnVpbGRlclwiKSk7XHJcbmNvbnN0IFJvb3RDb21tYW5kTm9kZV8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL3RyZWUvUm9vdENvbW1hbmROb2RlXCIpKTtcclxuY29uc3QgU3RyaW5nUmVhZGVyXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vU3RyaW5nUmVhZGVyXCIpKTtcclxuY29uc3QgQVJHVU1FTlRfU0VQQVJBVE9SID0gXCIgXCI7XHJcbmNvbnN0IFVTQUdFX09QVElPTkFMX09QRU4gPSBcIltcIjtcclxuY29uc3QgVVNBR0VfT1BUSU9OQUxfQ0xPU0UgPSBcIl1cIjtcclxuY29uc3QgVVNBR0VfUkVRVUlSRURfT1BFTiA9IFwiKFwiO1xyXG5jb25zdCBVU0FHRV9SRVFVSVJFRF9DTE9TRSA9IFwiKVwiO1xyXG5jb25zdCBVU0FHRV9PUiA9IFwifFwiO1xyXG5jbGFzcyBDb21tYW5kRGlzcGF0Y2hlciB7XHJcbiAgICBjb25zdHJ1Y3Rvcihyb290ID0gbnVsbCkge1xyXG4gICAgICAgIHRoaXMuY29uc3VtZXIgPSB7XHJcbiAgICAgICAgICAgIG9uQ29tbWFuZENvbXBsZXRlKCkgeyB9XHJcbiAgICAgICAgfTtcclxuICAgICAgICB0aGlzLnJvb3QgPSByb290IHx8IG5ldyBSb290Q29tbWFuZE5vZGVfMS5kZWZhdWx0KCk7XHJcbiAgICB9XHJcbiAgICByZWdpc3Rlcihjb21tYW5kKSB7XHJcbiAgICAgICAgbGV0IGJ1aWxkID0gY29tbWFuZC5idWlsZCgpO1xyXG4gICAgICAgIHRoaXMucm9vdC5hZGRDaGlsZChidWlsZCk7XHJcbiAgICAgICAgcmV0dXJuIGJ1aWxkO1xyXG4gICAgfVxyXG4gICAgc2V0Q29uc3VtZXIoY29uc3VtZXIpIHtcclxuICAgICAgICB0aGlzLmNvbnN1bWVyID0gY29uc3VtZXI7XHJcbiAgICB9XHJcbiAgICBleGVjdXRlKGlucHV0LCBzb3VyY2UgPSBudWxsKSB7XHJcbiAgICAgICAgaWYgKHR5cGVvZiBpbnB1dCA9PT0gXCJzdHJpbmdcIilcclxuICAgICAgICAgICAgaW5wdXQgPSBuZXcgU3RyaW5nUmVhZGVyXzEuZGVmYXVsdChpbnB1dCk7XHJcbiAgICAgICAgbGV0IHBhcnNlO1xyXG4gICAgICAgIGlmIChpbnB1dCBpbnN0YW5jZW9mIFN0cmluZ1JlYWRlcl8xLmRlZmF1bHQpIHtcclxuICAgICAgICAgICAgaWYgKCEoc291cmNlID09IG51bGwpKVxyXG4gICAgICAgICAgICAgICAgcGFyc2UgPSB0aGlzLnBhcnNlKGlucHV0LCBzb3VyY2UpO1xyXG4gICAgICAgIH1cclxuICAgICAgICBlbHNlXHJcbiAgICAgICAgICAgIHBhcnNlID0gaW5wdXQ7XHJcbiAgICAgICAgaWYgKHBhcnNlLmdldFJlYWRlcigpLmNhblJlYWQoKSkge1xyXG4gICAgICAgICAgICBpZiAocGFyc2UuZ2V0RXhjZXB0aW9ucygpLnNpemUgPT09IDEpIHtcclxuICAgICAgICAgICAgICAgIHRocm93IHBhcnNlLmdldEV4Y2VwdGlvbnMoKS52YWx1ZXMoKS5uZXh0KCkudmFsdWU7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgZWxzZSBpZiAocGFyc2UuZ2V0Q29udGV4dCgpLmdldFJhbmdlKCkuaXNFbXB0eSgpKSB7XHJcbiAgICAgICAgICAgICAgICB0aHJvdyBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEuZGVmYXVsdC5CVUlMVF9JTl9FWENFUFRJT05TLmRpc3BhdGNoZXJVbmtub3duQ29tbWFuZCgpLmNyZWF0ZVdpdGhDb250ZXh0KHBhcnNlLmdldFJlYWRlcigpKTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICBlbHNlIHtcclxuICAgICAgICAgICAgICAgIHRocm93IENvbW1hbmRTeW50YXhFeGNlcHRpb25fMS5kZWZhdWx0LkJVSUxUX0lOX0VYQ0VQVElPTlMuZGlzcGF0Y2hlclVua25vd25Bcmd1bWVudCgpLmNyZWF0ZVdpdGhDb250ZXh0KHBhcnNlLmdldFJlYWRlcigpKTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgIH1cclxuICAgICAgICBsZXQgcmVzdWx0ID0gMDtcclxuICAgICAgICBsZXQgc3VjY2Vzc2Z1bEZvcmtzID0gMDtcclxuICAgICAgICBsZXQgZm9ya2VkID0gZmFsc2U7XHJcbiAgICAgICAgbGV0IGZvdW5kQ29tbWFuZCA9IGZhbHNlO1xyXG4gICAgICAgIGxldCBjb21tYW5kID0gcGFyc2UuZ2V0UmVhZGVyKCkuZ2V0U3RyaW5nKCk7XHJcbiAgICAgICAgbGV0IG9yaWdpbmFsID0gcGFyc2UuZ2V0Q29udGV4dCgpLmJ1aWxkKGNvbW1hbmQpO1xyXG4gICAgICAgIGxldCBjb250ZXh0cyA9IFtdO1xyXG4gICAgICAgIGNvbnRleHRzLnB1c2gob3JpZ2luYWwpO1xyXG4gICAgICAgIGxldCBuZXh0ID0gbnVsbDtcclxuICAgICAgICB3aGlsZSAoIShjb250ZXh0cyA9PSBudWxsKSkge1xyXG4gICAgICAgICAgICBmb3IgKGxldCBpID0gMDsgaSA8IGNvbnRleHRzLmxlbmd0aDsgaSsrKSB7XHJcbiAgICAgICAgICAgICAgICBsZXQgY29udGV4dCA9IGNvbnRleHRzW2ldO1xyXG4gICAgICAgICAgICAgICAgbGV0IGNoaWxkID0gY29udGV4dC5nZXRDaGlsZCgpO1xyXG4gICAgICAgICAgICAgICAgaWYgKCEoY2hpbGQgPT0gbnVsbCkpIHtcclxuICAgICAgICAgICAgICAgICAgICBmb3JrZWQgPSBmb3JrZWQgfHwgY29udGV4dC5pc0ZvcmtlZCgpO1xyXG4gICAgICAgICAgICAgICAgICAgIGlmIChjaGlsZC5oYXNOb2RlcygpKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIGZvdW5kQ29tbWFuZCA9IHRydWU7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIGxldCBtb2RpZmllciA9IGNvbnRleHQuZ2V0UmVkaXJlY3RNb2RpZmllcigpO1xyXG4gICAgICAgICAgICAgICAgICAgICAgICBpZiAobW9kaWZpZXIgPT0gbnVsbCkge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgaWYgKG5leHQgPT0gbnVsbClcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBuZXh0ID0gW107XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICBuZXh0LnB1c2goY2hpbGQuY29weUZvcihjb250ZXh0LmdldFNvdXJjZSgpKSk7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgICAgICAgICAgZWxzZSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICB0cnkge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIGxldCByZXN1bHRzID0gbW9kaWZpZXIuYXBwbHkoY29udGV4dCk7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgaWYgKHJlc3VsdHMubGVuZ3RoICE9PSAwKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIGlmIChuZXh0ID09IG51bGwpXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBuZXh0ID0gW107XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIGZvciAobGV0IHNvdXJjZSBvZiByZXN1bHRzKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBuZXh0LnB1c2goY2hpbGQuY29weUZvcihzb3VyY2UpKTtcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIGNhdGNoIChleCkge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIHRoaXMuY29uc3VtZXIub25Db21tYW5kQ29tcGxldGUoY29udGV4dCwgZmFsc2UsIDApO1xyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIGlmICghZm9ya2VkKVxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICB0aHJvdyBleDtcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIGVsc2UgaWYgKGNvbnRleHQuZ2V0Q29tbWFuZCgpICE9IG51bGwpIHtcclxuICAgICAgICAgICAgICAgICAgICBmb3VuZENvbW1hbmQgPSB0cnVlO1xyXG4gICAgICAgICAgICAgICAgICAgIHRyeSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIGxldCB2YWx1ZSA9IGNvbnRleHQuZ2V0Q29tbWFuZCgpKGNvbnRleHQpO1xyXG4gICAgICAgICAgICAgICAgICAgICAgICByZXN1bHQgKz0gdmFsdWU7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHRoaXMuY29uc3VtZXIub25Db21tYW5kQ29tcGxldGUoY29udGV4dCwgdHJ1ZSwgdmFsdWUpO1xyXG4gICAgICAgICAgICAgICAgICAgICAgICBzdWNjZXNzZnVsRm9ya3MrKztcclxuICAgICAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICAgICAgY2F0Y2ggKGV4KSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHRoaXMuY29uc3VtZXIub25Db21tYW5kQ29tcGxldGUoY29udGV4dCwgZmFsc2UsIDApO1xyXG4gICAgICAgICAgICAgICAgICAgICAgICBpZiAoIWZvcmtlZClcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIHRocm93IGV4O1xyXG4gICAgICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICBjb250ZXh0cyA9IG5leHQ7XHJcbiAgICAgICAgICAgIG5leHQgPSBudWxsO1xyXG4gICAgICAgIH1cclxuICAgICAgICBpZiAoIWZvdW5kQ29tbWFuZCkge1xyXG4gICAgICAgICAgICB0aGlzLmNvbnN1bWVyLm9uQ29tbWFuZENvbXBsZXRlKG9yaWdpbmFsLCBmYWxzZSwgMCk7XHJcbiAgICAgICAgICAgIHRocm93IENvbW1hbmRTeW50YXhFeGNlcHRpb25fMS5kZWZhdWx0LkJVSUxUX0lOX0VYQ0VQVElPTlMuZGlzcGF0Y2hlclVua25vd25Db21tYW5kKCkuY3JlYXRlV2l0aENvbnRleHQocGFyc2UuZ2V0UmVhZGVyKCkpO1xyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gZm9ya2VkID8gc3VjY2Vzc2Z1bEZvcmtzIDogcmVzdWx0O1xyXG4gICAgfVxyXG4gICAgcGFyc2UoY29tbWFuZCwgc291cmNlKSB7XHJcbiAgICAgICAgaWYgKHR5cGVvZiBjb21tYW5kID09PSBcInN0cmluZ1wiKVxyXG4gICAgICAgICAgICBjb21tYW5kID0gbmV3IFN0cmluZ1JlYWRlcl8xLmRlZmF1bHQoY29tbWFuZCk7XHJcbiAgICAgICAgbGV0IGNvbnRleHQgPSBuZXcgQ29tbWFuZENvbnRleHRCdWlsZGVyXzEuZGVmYXVsdCh0aGlzLCBzb3VyY2UsIHRoaXMucm9vdCwgY29tbWFuZC5nZXRDdXJzb3IoKSk7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMucGFyc2VOb2Rlcyh0aGlzLnJvb3QsIGNvbW1hbmQsIGNvbnRleHQpO1xyXG4gICAgfVxyXG4gICAgcGFyc2VOb2Rlcyhub2RlLCBvcmlnaW5hbFJlYWRlciwgY29udGV4dFNvRmFyKSB7XHJcbiAgICAgICAgbGV0IHNvdXJjZSA9IGNvbnRleHRTb0Zhci5nZXRTb3VyY2UoKTtcclxuICAgICAgICBsZXQgZXJyb3JzID0gbnVsbDtcclxuICAgICAgICBsZXQgcG90ZW50aWFscyA9IG51bGw7XHJcbiAgICAgICAgbGV0IGN1cnNvciA9IG9yaWdpbmFsUmVhZGVyLmdldEN1cnNvcigpO1xyXG4gICAgICAgIGZvciAobGV0IGNoaWxkIG9mIG5vZGUuZ2V0UmVsZXZhbnROb2RlcyhvcmlnaW5hbFJlYWRlcikpIHtcclxuICAgICAgICAgICAgaWYgKCFjaGlsZC5jYW5Vc2Uoc291cmNlKSlcclxuICAgICAgICAgICAgICAgIGNvbnRpbnVlO1xyXG4gICAgICAgICAgICBsZXQgY29udGV4dCA9IGNvbnRleHRTb0Zhci5jb3B5KCk7XHJcbiAgICAgICAgICAgIGxldCByZWFkZXIgPSBuZXcgU3RyaW5nUmVhZGVyXzEuZGVmYXVsdChvcmlnaW5hbFJlYWRlcik7XHJcbiAgICAgICAgICAgIHRyeSB7XHJcbiAgICAgICAgICAgICAgICBjaGlsZC5wYXJzZShyZWFkZXIsIGNvbnRleHQpO1xyXG4gICAgICAgICAgICAgICAgaWYgKHJlYWRlci5jYW5SZWFkKCkpXHJcbiAgICAgICAgICAgICAgICAgICAgaWYgKHJlYWRlci5wZWVrKCkgIT0gQVJHVU1FTlRfU0VQQVJBVE9SKVxyXG4gICAgICAgICAgICAgICAgICAgICAgICB0aHJvdyBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEuZGVmYXVsdC5CVUlMVF9JTl9FWENFUFRJT05TLmRpc3BhdGNoZXJFeHBlY3RlZEFyZ3VtZW50U2VwYXJhdG9yKCkuY3JlYXRlV2l0aENvbnRleHQocmVhZGVyKTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICBjYXRjaCAoZXgpIHtcclxuICAgICAgICAgICAgICAgIGlmIChlcnJvcnMgPT0gbnVsbCkge1xyXG4gICAgICAgICAgICAgICAgICAgIGVycm9ycyA9IG5ldyBNYXAoKTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIGVycm9ycy5zZXQoY2hpbGQsIGV4KTtcclxuICAgICAgICAgICAgICAgIHJlYWRlci5zZXRDdXJzb3IoY3Vyc29yKTtcclxuICAgICAgICAgICAgICAgIGNvbnRpbnVlO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIGNvbnRleHQud2l0aENvbW1hbmQoY2hpbGQuZ2V0Q29tbWFuZCgpKTtcclxuICAgICAgICAgICAgaWYgKHJlYWRlci5jYW5SZWFkKGNoaWxkLmdldFJlZGlyZWN0KCkgPT0gbnVsbCA/IDIgOiAxKSkge1xyXG4gICAgICAgICAgICAgICAgcmVhZGVyLnNraXAoKTtcclxuICAgICAgICAgICAgICAgIGlmICghKGNoaWxkLmdldFJlZGlyZWN0KCkgPT0gbnVsbCkpIHtcclxuICAgICAgICAgICAgICAgICAgICBsZXQgY2hpbGRDb250ZXh0ID0gbmV3IENvbW1hbmRDb250ZXh0QnVpbGRlcl8xLmRlZmF1bHQodGhpcywgc291cmNlLCBjaGlsZC5nZXRSZWRpcmVjdCgpLCByZWFkZXIuZ2V0Q3Vyc29yKCkpO1xyXG4gICAgICAgICAgICAgICAgICAgIGxldCBwYXJzZSA9IHRoaXMucGFyc2VOb2RlcyhjaGlsZC5nZXRSZWRpcmVjdCgpLCByZWFkZXIsIGNoaWxkQ29udGV4dCk7XHJcbiAgICAgICAgICAgICAgICAgICAgY29udGV4dC53aXRoQ2hpbGQocGFyc2UuZ2V0Q29udGV4dCgpKTtcclxuICAgICAgICAgICAgICAgICAgICByZXR1cm4gbmV3IFBhcnNlUmVzdWx0c18xLmRlZmF1bHQoY29udGV4dCwgcGFyc2UuZ2V0UmVhZGVyKCksIHBhcnNlLmdldEV4Y2VwdGlvbnMoKSk7XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICBlbHNlIHtcclxuICAgICAgICAgICAgICAgICAgICBsZXQgcGFyc2UgPSB0aGlzLnBhcnNlTm9kZXMoY2hpbGQsIHJlYWRlciwgY29udGV4dCk7XHJcbiAgICAgICAgICAgICAgICAgICAgaWYgKHBvdGVudGlhbHMgPT0gbnVsbCkge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICBwb3RlbnRpYWxzID0gW107XHJcbiAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgICAgIHBvdGVudGlhbHMucHVzaChwYXJzZSk7XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgZWxzZSB7XHJcbiAgICAgICAgICAgICAgICBpZiAocG90ZW50aWFscyA9PSBudWxsKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgcG90ZW50aWFscyA9IFtdO1xyXG4gICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgcG90ZW50aWFscy5wdXNoKG5ldyBQYXJzZVJlc3VsdHNfMS5kZWZhdWx0KGNvbnRleHQsIHJlYWRlciwgbmV3IE1hcCgpKSk7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9XHJcbiAgICAgICAgaWYgKCEocG90ZW50aWFscyA9PSBudWxsKSkge1xyXG4gICAgICAgICAgICBpZiAocG90ZW50aWFscy5sZW5ndGggPiAxKSB7XHJcbiAgICAgICAgICAgICAgICBwb3RlbnRpYWxzLnNvcnQoKGEsIGIpID0+IHtcclxuICAgICAgICAgICAgICAgICAgICBpZiAoIWEuZ2V0UmVhZGVyKCkuY2FuUmVhZCgpICYmIGIuZ2V0UmVhZGVyKCkuY2FuUmVhZCgpKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHJldHVybiAtMTtcclxuICAgICAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICAgICAgaWYgKGEuZ2V0UmVhZGVyKCkuY2FuUmVhZCgpICYmICFiLmdldFJlYWRlcigpLmNhblJlYWQoKSkge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICByZXR1cm4gMTtcclxuICAgICAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICAgICAgaWYgKGEuZ2V0RXhjZXB0aW9ucygpLnNpemUgPT09IDAgJiYgYi5nZXRFeGNlcHRpb25zKCkuc2l6ZSAhPT0gMCkge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICByZXR1cm4gLTE7XHJcbiAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgICAgIGlmIChhLmdldEV4Y2VwdGlvbnMoKS5zaXplICE9PSAwICYmIGIuZ2V0RXhjZXB0aW9ucygpLnNpemUgPT09IDApIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgcmV0dXJuIDE7XHJcbiAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgICAgIHJldHVybiAwO1xyXG4gICAgICAgICAgICAgICAgfSk7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgcmV0dXJuIHBvdGVudGlhbHNbMF07XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHJldHVybiBuZXcgUGFyc2VSZXN1bHRzXzEuZGVmYXVsdChjb250ZXh0U29GYXIsIG9yaWdpbmFsUmVhZGVyLCBlcnJvcnMgPT0gbnVsbCA/IG5ldyBNYXAoKSA6IGVycm9ycyk7XHJcbiAgICB9XHJcbiAgICBnZXRBbGxVc2FnZShub2RlLCBzb3VyY2UsIHJlc3RyaWN0ZWQpIHtcclxuICAgICAgICBjb25zdCByZXN1bHQgPSBbXTtcclxuICAgICAgICB0aGlzLl9fZ2V0QWxsVXNhZ2Uobm9kZSwgc291cmNlLCByZXN1bHQsIFwiXCIsIHJlc3RyaWN0ZWQpO1xyXG4gICAgICAgIHJldHVybiByZXN1bHQ7XHJcbiAgICB9XHJcbiAgICBfX2dldEFsbFVzYWdlKG5vZGUsIHNvdXJjZSwgcmVzdWx0LCBwcmVmaXggPSBcIlwiLCByZXN0cmljdGVkKSB7XHJcbiAgICAgICAgaWYgKHJlc3RyaWN0ZWQgJiYgIW5vZGUuY2FuVXNlKHNvdXJjZSkpIHtcclxuICAgICAgICAgICAgcmV0dXJuO1xyXG4gICAgICAgIH1cclxuICAgICAgICBpZiAobm9kZS5nZXRDb21tYW5kKCkgIT0gbnVsbCkge1xyXG4gICAgICAgICAgICByZXN1bHQucHVzaChwcmVmaXgpO1xyXG4gICAgICAgIH1cclxuICAgICAgICBpZiAobm9kZS5nZXRSZWRpcmVjdCgpICE9IG51bGwpIHtcclxuICAgICAgICAgICAgY29uc3QgcmVkaXJlY3QgPSBub2RlLmdldFJlZGlyZWN0KCkgPT09IHRoaXMucm9vdCA/IFwiLi4uXCIgOiBcIi0+IFwiICsgbm9kZS5nZXRSZWRpcmVjdCgpLmdldFVzYWdlVGV4dCgpO1xyXG4gICAgICAgICAgICByZXN1bHQucHVzaChwcmVmaXgubGVuZ3RoID09PSAwID8gbm9kZS5nZXRVc2FnZVRleHQoKSArIEFSR1VNRU5UX1NFUEFSQVRPUiArIHJlZGlyZWN0IDogcHJlZml4ICsgQVJHVU1FTlRfU0VQQVJBVE9SICsgcmVkaXJlY3QpO1xyXG4gICAgICAgIH1cclxuICAgICAgICBlbHNlIGlmIChub2RlLmdldENoaWxkcmVuQ291bnQoKSA+IDApIHtcclxuICAgICAgICAgICAgZm9yIChsZXQgY2hpbGQgb2Ygbm9kZS5nZXRDaGlsZHJlbigpKSB7XHJcbiAgICAgICAgICAgICAgICB0aGlzLl9fZ2V0QWxsVXNhZ2UoY2hpbGQsIHNvdXJjZSwgcmVzdWx0LCBwcmVmaXgubGVuZ3RoID09PSAwID8gY2hpbGQuZ2V0VXNhZ2VUZXh0KCkgOiBwcmVmaXggKyBBUkdVTUVOVF9TRVBBUkFUT1IgKyBjaGlsZC5nZXRVc2FnZVRleHQoKSwgcmVzdHJpY3RlZCk7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9XHJcbiAgICB9XHJcbiAgICBnZXRTbWFydFVzYWdlKG5vZGUsIHNvdXJjZSkge1xyXG4gICAgICAgIGxldCByZXN1bHQgPSBuZXcgTWFwKCk7XHJcbiAgICAgICAgbGV0IG9wdGlvbmFsID0gbm9kZS5nZXRDb21tYW5kKCkgIT09IG51bGw7XHJcbiAgICAgICAgZm9yIChsZXQgY2hpbGQgb2Ygbm9kZS5nZXRDaGlsZHJlbigpKSB7XHJcbiAgICAgICAgICAgIGxldCB1c2FnZSA9IHRoaXMuX19nZXRTbWFydFVzYWdlKGNoaWxkLCBzb3VyY2UsIG9wdGlvbmFsLCBmYWxzZSk7XHJcbiAgICAgICAgICAgIGlmICghKHVzYWdlID09IG51bGwpKSB7XHJcbiAgICAgICAgICAgICAgICByZXN1bHQuc2V0KGNoaWxkLCB1c2FnZSk7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9XHJcbiAgICAgICAgcmV0dXJuIHJlc3VsdDtcclxuICAgIH1cclxuICAgIF9fZ2V0U21hcnRVc2FnZShub2RlLCBzb3VyY2UsIG9wdGlvbmFsLCBkZWVwKSB7XHJcbiAgICAgICAgaWYgKCFub2RlLmNhblVzZShzb3VyY2UpKSB7XHJcbiAgICAgICAgICAgIHJldHVybiBudWxsO1xyXG4gICAgICAgIH1cclxuICAgICAgICBsZXQgc2VsZiA9IG9wdGlvbmFsID8gVVNBR0VfT1BUSU9OQUxfT1BFTiArIG5vZGUuZ2V0VXNhZ2VUZXh0KCkgKyBVU0FHRV9PUFRJT05BTF9DTE9TRSA6IG5vZGUuZ2V0VXNhZ2VUZXh0KCk7XHJcbiAgICAgICAgbGV0IGNoaWxkT3B0aW9uYWwgPSBub2RlLmdldENvbW1hbmQoKSAhPSBudWxsO1xyXG4gICAgICAgIGxldCBvcGVuID0gY2hpbGRPcHRpb25hbCA/IFVTQUdFX09QVElPTkFMX09QRU4gOiBVU0FHRV9SRVFVSVJFRF9PUEVOO1xyXG4gICAgICAgIGxldCBjbG9zZSA9IGNoaWxkT3B0aW9uYWwgPyBVU0FHRV9PUFRJT05BTF9DTE9TRSA6IFVTQUdFX1JFUVVJUkVEX0NMT1NFO1xyXG4gICAgICAgIGlmICghZGVlcCkge1xyXG4gICAgICAgICAgICBpZiAoKG5vZGUuZ2V0UmVkaXJlY3QoKSAhPSBudWxsKSkge1xyXG4gICAgICAgICAgICAgICAgbGV0IHJlZGlyZWN0ID0gbm9kZS5nZXRSZWRpcmVjdCgpID09IHRoaXMucm9vdCA/IFwiLi4uXCIgOiBcIi0+IFwiICsgbm9kZS5nZXRSZWRpcmVjdCgpLmdldFVzYWdlVGV4dCgpO1xyXG4gICAgICAgICAgICAgICAgcmV0dXJuIHNlbGYgKyBBUkdVTUVOVF9TRVBBUkFUT1IgKyByZWRpcmVjdDtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICBlbHNlIHtcclxuICAgICAgICAgICAgICAgIGxldCBjaGlsZHJlbiA9IFsuLi5ub2RlLmdldENoaWxkcmVuKCldLmZpbHRlcihjID0+IGMuY2FuVXNlKHNvdXJjZSkpO1xyXG4gICAgICAgICAgICAgICAgaWYgKChjaGlsZHJlbi5sZW5ndGggPT0gMSkpIHtcclxuICAgICAgICAgICAgICAgICAgICBsZXQgdXNhZ2UgPSB0aGlzLl9fZ2V0U21hcnRVc2FnZShjaGlsZHJlblswXSwgc291cmNlLCBjaGlsZE9wdGlvbmFsLCBjaGlsZE9wdGlvbmFsKTtcclxuICAgICAgICAgICAgICAgICAgICBpZiAoISh1c2FnZSA9PSBudWxsKSkge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICByZXR1cm4gc2VsZiArIEFSR1VNRU5UX1NFUEFSQVRPUiArIHVzYWdlO1xyXG4gICAgICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIGVsc2UgaWYgKGNoaWxkcmVuLmxlbmd0aCA+IDEpIHtcclxuICAgICAgICAgICAgICAgICAgICBsZXQgY2hpbGRVc2FnZSA9IG5ldyBTZXQoKTtcclxuICAgICAgICAgICAgICAgICAgICBmb3IgKGxldCBjaGlsZCBvZiBjaGlsZHJlbikge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICBsZXQgdXNhZ2UgPSB0aGlzLl9fZ2V0U21hcnRVc2FnZShjaGlsZCwgc291cmNlLCBjaGlsZE9wdGlvbmFsLCB0cnVlKTtcclxuICAgICAgICAgICAgICAgICAgICAgICAgaWYgKCEodXNhZ2UgPT0gbnVsbCkpIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIGNoaWxkVXNhZ2UuYWRkKHVzYWdlKTtcclxuICAgICAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgICAgICBpZiAoY2hpbGRVc2FnZS5zaXplID09PSAxKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIGxldCB1c2FnZSA9IGNoaWxkVXNhZ2UudmFsdWVzKCkubmV4dCgpLnZhbHVlO1xyXG4gICAgICAgICAgICAgICAgICAgICAgICByZXR1cm4gc2VsZiArIEFSR1VNRU5UX1NFUEFSQVRPUiArIChjaGlsZE9wdGlvbmFsID8gVVNBR0VfT1BUSU9OQUxfT1BFTiArIHVzYWdlICsgVVNBR0VfT1BUSU9OQUxfQ0xPU0UgOiB1c2FnZSk7XHJcbiAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgICAgIGVsc2UgaWYgKGNoaWxkVXNhZ2Uuc2l6ZSA+IDEpIHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgbGV0IGJ1aWxkZXIgPSBvcGVuO1xyXG4gICAgICAgICAgICAgICAgICAgICAgICBsZXQgY291bnQgPSAwO1xyXG4gICAgICAgICAgICAgICAgICAgICAgICBmb3IgKGxldCBjaGlsZCBvZiBjaGlsZHJlbikge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgaWYgKGNvdW50ID4gMCkge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIGJ1aWxkZXIgKz0gVVNBR0VfT1I7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICBidWlsZGVyICs9IGNoaWxkLmdldFVzYWdlVGV4dCgpO1xyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgY291bnQrKztcclxuICAgICAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgICAgICAgICBpZiAoY291bnQgPiAwKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICBidWlsZGVyICs9IGNsb3NlO1xyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgcmV0dXJuIHNlbGYgKyBBUkdVTUVOVF9TRVBBUkFUT1IgKyBidWlsZGVyO1xyXG4gICAgICAgICAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHJldHVybiBzZWxmO1xyXG4gICAgfVxyXG4gICAgZ2V0Q29tcGxldGlvblN1Z2dlc3Rpb25zKHBhcnNlLCBjdXJzb3IgPSBwYXJzZS5nZXRSZWFkZXIoKS5nZXRUb3RhbExlbmd0aCgpKSB7XHJcbiAgICAgICAgcmV0dXJuIF9fYXdhaXRlcih0aGlzLCB2b2lkIDAsIHZvaWQgMCwgZnVuY3Rpb24qICgpIHtcclxuICAgICAgICAgICAgbGV0IGNvbnRleHQgPSBwYXJzZS5nZXRDb250ZXh0KCk7XHJcbiAgICAgICAgICAgIGxldCBub2RlQmVmb3JlQ3Vyc29yID0gY29udGV4dC5maW5kU3VnZ2VzdGlvbkNvbnRleHQoY3Vyc29yKTtcclxuICAgICAgICAgICAgbGV0IHBhcmVudCA9IG5vZGVCZWZvcmVDdXJzb3IucGFyZW50O1xyXG4gICAgICAgICAgICBsZXQgc3RhcnQgPSBNYXRoLm1pbihub2RlQmVmb3JlQ3Vyc29yLnN0YXJ0UG9zLCBjdXJzb3IpO1xyXG4gICAgICAgICAgICBsZXQgZnVsbElucHV0ID0gcGFyc2UuZ2V0UmVhZGVyKCkuZ2V0U3RyaW5nKCk7XHJcbiAgICAgICAgICAgIGxldCB0cnVuY2F0ZWRJbnB1dCA9IGZ1bGxJbnB1dC5zdWJzdHJpbmcoMCwgY3Vyc29yKTtcclxuICAgICAgICAgICAgbGV0IGZ1dHVyZXMgPSBbXTtcclxuICAgICAgICAgICAgZm9yIChsZXQgbm9kZSBvZiBwYXJlbnQuZ2V0Q2hpbGRyZW4oKSkge1xyXG4gICAgICAgICAgICAgICAgbGV0IGZ1dHVyZSA9IHlpZWxkIFN1Z2dlc3Rpb25zXzEuZGVmYXVsdC5lbXB0eSgpO1xyXG4gICAgICAgICAgICAgICAgdHJ5IHtcclxuICAgICAgICAgICAgICAgICAgICBmdXR1cmUgPSB5aWVsZCBub2RlLmxpc3RTdWdnZXN0aW9ucyhjb250ZXh0LmJ1aWxkKHRydW5jYXRlZElucHV0KSwgbmV3IFN1Z2dlc3Rpb25zQnVpbGRlcl8xLmRlZmF1bHQodHJ1bmNhdGVkSW5wdXQsIHN0YXJ0KSk7XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICBjYXRjaCAoaWdub3JlZCkge1xyXG4gICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgZnV0dXJlcy5wdXNoKGZ1dHVyZSk7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgcmV0dXJuIFByb21pc2UucmVzb2x2ZShTdWdnZXN0aW9uc18xLmRlZmF1bHQubWVyZ2UoZnVsbElucHV0LCBmdXR1cmVzKSk7XHJcbiAgICAgICAgfSk7XHJcbiAgICB9XHJcbiAgICBnZXRSb290KCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnJvb3Q7XHJcbiAgICB9XHJcbiAgICBnZXRQYXRoKHRhcmdldCkge1xyXG4gICAgICAgIGxldCBub2RlcyA9IFtdO1xyXG4gICAgICAgIHRoaXMuYWRkUGF0aHModGhpcy5yb290LCBub2RlcywgW10pO1xyXG4gICAgICAgIGZvciAobGV0IGxpc3Qgb2Ygbm9kZXMpIHtcclxuICAgICAgICAgICAgaWYgKGxpc3RbbGlzdC5sZW5ndGggLSAxXSA9PT0gdGFyZ2V0KSB7XHJcbiAgICAgICAgICAgICAgICBsZXQgcmVzdWx0ID0gW107XHJcbiAgICAgICAgICAgICAgICBmb3IgKGxldCBub2RlIG9mIGxpc3QpIHtcclxuICAgICAgICAgICAgICAgICAgICBpZiAobm9kZSAhPT0gdGhpcy5yb290KSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHJlc3VsdC5wdXNoKG5vZGUuZ2V0TmFtZSgpKTtcclxuICAgICAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gcmVzdWx0O1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHJldHVybiBbXTtcclxuICAgIH1cclxuICAgIGZpbmROb2RlKHBhdGgpIHtcclxuICAgICAgICBsZXQgbm9kZSA9IHRoaXMucm9vdDtcclxuICAgICAgICBmb3IgKGxldCBuYW1lIG9mIHBhdGgpIHtcclxuICAgICAgICAgICAgbm9kZSA9IG5vZGUuZ2V0Q2hpbGQobmFtZSk7XHJcbiAgICAgICAgICAgIGlmIChub2RlID09IG51bGwpXHJcbiAgICAgICAgICAgICAgICByZXR1cm4gbnVsbDtcclxuICAgICAgICB9XHJcbiAgICAgICAgcmV0dXJuIG5vZGU7XHJcbiAgICB9XHJcbiAgICBmaW5kQW1iaWd1aXRpZXMoY29uc3VtZXIpIHtcclxuICAgICAgICB0aGlzLnJvb3QuZmluZEFtYmlndWl0aWVzKGNvbnN1bWVyKTtcclxuICAgIH1cclxuICAgIGFkZFBhdGhzKG5vZGUsIHJlc3VsdCwgcGFyZW50cykge1xyXG4gICAgICAgIGxldCBjdXJyZW50ID0gW107XHJcbiAgICAgICAgY3VycmVudC5wdXNoKC4uLnBhcmVudHMpO1xyXG4gICAgICAgIGN1cnJlbnQucHVzaChub2RlKTtcclxuICAgICAgICByZXN1bHQucHVzaChjdXJyZW50KTtcclxuICAgICAgICBmb3IgKGxldCBjaGlsZCBvZiBub2RlLmdldENoaWxkcmVuKCkpXHJcbiAgICAgICAgICAgIHRoaXMuYWRkUGF0aHMoY2hpbGQsIHJlc3VsdCwgY3VycmVudCk7XHJcbiAgICB9XHJcbn1cclxuZXhwb3J0cy5kZWZhdWx0ID0gQ29tbWFuZERpc3BhdGNoZXI7XHJcbiIsIlwidXNlIHN0cmljdFwiO1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNsYXNzIExpdGVyYWxNZXNzYWdlIHtcclxuICAgIGNvbnN0cnVjdG9yKHN0cikge1xyXG4gICAgICAgIHRoaXMuc3RyID0gc3RyO1xyXG4gICAgfVxyXG4gICAgZ2V0U3RyaW5nKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnN0cjtcclxuICAgIH1cclxuICAgIHRvU3RyaW5nKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnN0cjtcclxuICAgIH1cclxufVxyXG5leHBvcnRzLmRlZmF1bHQgPSBMaXRlcmFsTWVzc2FnZTtcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbnZhciBfX2ltcG9ydERlZmF1bHQgPSAodGhpcyAmJiB0aGlzLl9faW1wb3J0RGVmYXVsdCkgfHwgZnVuY3Rpb24gKG1vZCkge1xyXG4gICAgcmV0dXJuIChtb2QgJiYgbW9kLl9fZXNNb2R1bGUpID8gbW9kIDogeyBcImRlZmF1bHRcIjogbW9kIH07XHJcbn07XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY29uc3QgU3RyaW5nUmVhZGVyXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vU3RyaW5nUmVhZGVyXCIpKTtcclxuY2xhc3MgUGFyc2VSZXN1bHRzIHtcclxuICAgIGNvbnN0cnVjdG9yKGNvbnRleHQsIHJlYWRlciwgZXhjZXB0aW9ucykge1xyXG4gICAgICAgIHRoaXMuY29udGV4dCA9IGNvbnRleHQ7XHJcbiAgICAgICAgdGhpcy5yZWFkZXIgPSByZWFkZXIgfHwgbmV3IFN0cmluZ1JlYWRlcl8xLmRlZmF1bHQoXCJcIik7XHJcbiAgICAgICAgdGhpcy5leGNlcHRpb25zID0gZXhjZXB0aW9ucyB8fCBuZXcgTWFwKCk7XHJcbiAgICB9XHJcbiAgICBnZXRDb250ZXh0KCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmNvbnRleHQ7XHJcbiAgICB9XHJcbiAgICBnZXRSZWFkZXIoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMucmVhZGVyO1xyXG4gICAgfVxyXG4gICAgZ2V0RXhjZXB0aW9ucygpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5leGNlcHRpb25zO1xyXG4gICAgfVxyXG59XHJcbmV4cG9ydHMuZGVmYXVsdCA9IFBhcnNlUmVzdWx0cztcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbnZhciBfX2ltcG9ydERlZmF1bHQgPSAodGhpcyAmJiB0aGlzLl9faW1wb3J0RGVmYXVsdCkgfHwgZnVuY3Rpb24gKG1vZCkge1xyXG4gICAgcmV0dXJuIChtb2QgJiYgbW9kLl9fZXNNb2R1bGUpID8gbW9kIDogeyBcImRlZmF1bHRcIjogbW9kIH07XHJcbn07XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY29uc3QgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbl8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL2V4Y2VwdGlvbnMvQ29tbWFuZFN5bnRheEV4Y2VwdGlvblwiKSk7XHJcbmNvbnN0IFNZTlRBWF9FU0NBUEUgPSAnXFxcXCc7XHJcbmNvbnN0IFNZTlRBWF9RVU9URSA9ICdcXFwiJztcclxuY2xhc3MgU3RyaW5nUmVhZGVyIHtcclxuICAgIGNvbnN0cnVjdG9yKG90aGVyKSB7XHJcbiAgICAgICAgdGhpcy5jdXJzb3IgPSAwO1xyXG4gICAgICAgIGlmICh0eXBlb2Ygb3RoZXIgPT09IFwic3RyaW5nXCIpIHtcclxuICAgICAgICAgICAgdGhpcy5zdHJpbmcgPSBvdGhlcjtcclxuICAgICAgICB9XHJcbiAgICAgICAgZWxzZSB7XHJcbiAgICAgICAgICAgIHRoaXMuc3RyaW5nID0gb3RoZXIuc3RyaW5nO1xyXG4gICAgICAgICAgICB0aGlzLmN1cnNvciA9IG90aGVyLmN1cnNvcjtcclxuICAgICAgICB9XHJcbiAgICB9XHJcbiAgICBnZXRTdHJpbmcoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuc3RyaW5nO1xyXG4gICAgfVxyXG4gICAgc2V0Q3Vyc29yKGN1cnNvcikge1xyXG4gICAgICAgIHRoaXMuY3Vyc29yID0gY3Vyc29yO1xyXG4gICAgfVxyXG4gICAgZ2V0UmVtYWluaW5nTGVuZ3RoKCkge1xyXG4gICAgICAgIHJldHVybiAodGhpcy5zdHJpbmcubGVuZ3RoIC0gdGhpcy5jdXJzb3IpO1xyXG4gICAgfVxyXG4gICAgZ2V0VG90YWxMZW5ndGgoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuc3RyaW5nLmxlbmd0aDtcclxuICAgIH1cclxuICAgIGdldEN1cnNvcigpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5jdXJzb3I7XHJcbiAgICB9XHJcbiAgICBnZXRSZWFkKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnN0cmluZy5zdWJzdHJpbmcoMCwgdGhpcy5jdXJzb3IpO1xyXG4gICAgfVxyXG4gICAgZ2V0UmVtYWluaW5nKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnN0cmluZy5zdWJzdHJpbmcodGhpcy5jdXJzb3IpO1xyXG4gICAgfVxyXG4gICAgY2FuUmVhZChsZW5ndGggPSAxKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuY3Vyc29yICsgbGVuZ3RoIDw9IHRoaXMuc3RyaW5nLmxlbmd0aDtcclxuICAgIH1cclxuICAgIHBlZWsob2Zmc2V0ID0gMCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnN0cmluZy5jaGFyQXQodGhpcy5jdXJzb3IgKyBvZmZzZXQpO1xyXG4gICAgfVxyXG4gICAgcmVhZCgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5zdHJpbmcuY2hhckF0KHRoaXMuY3Vyc29yKyspO1xyXG4gICAgfVxyXG4gICAgc2tpcCgpIHtcclxuICAgICAgICB0aGlzLmN1cnNvcisrO1xyXG4gICAgfVxyXG4gICAgc3RhdGljIGlzQWxsb3dlZE51bWJlcihjKSB7XHJcbiAgICAgICAgcmV0dXJuIGMgPj0gJzAnICYmIGMgPD0gJzknIHx8IGMgPT0gJy4nIHx8IGMgPT0gJy0nIHx8IGMgPT0gJysnIHx8IGMgPT0gJ2UnIHx8IGMgPT0gJ0UnO1xyXG4gICAgfVxyXG4gICAgc2tpcFdoaXRlc3BhY2UoKSB7XHJcbiAgICAgICAgd2hpbGUgKCh0aGlzLmNhblJlYWQoKSAmJiAvXFxzLy50ZXN0KHRoaXMucGVlaygpKSkpIHtcclxuICAgICAgICAgICAgdGhpcy5za2lwKCk7XHJcbiAgICAgICAgfVxyXG4gICAgfVxyXG4gICAgcmVhZEludCgpIHtcclxuICAgICAgICBsZXQgc3RhcnQgPSB0aGlzLmN1cnNvcjtcclxuICAgICAgICB3aGlsZSAodGhpcy5jYW5SZWFkKCkgJiYgU3RyaW5nUmVhZGVyLmlzQWxsb3dlZE51bWJlcih0aGlzLnBlZWsoKSkpIHtcclxuICAgICAgICAgICAgdGhpcy5za2lwKCk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGxldCBudW1iZXIgPSB0aGlzLnN0cmluZy5zdWJzdHJpbmcoc3RhcnQsIHRoaXMuY3Vyc29yKTtcclxuICAgICAgICBpZiAobnVtYmVyLmxlbmd0aCA9PT0gMCkge1xyXG4gICAgICAgICAgICB0aHJvdyBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEuZGVmYXVsdC5CVUlMVF9JTl9FWENFUFRJT05TLnJlYWRlckV4cGVjdGVkSW50KCkuY3JlYXRlV2l0aENvbnRleHQodGhpcyk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGNvbnN0IHJlc3VsdCA9IHBhcnNlSW50KG51bWJlcik7XHJcbiAgICAgICAgaWYgKGlzTmFOKHJlc3VsdCkgfHwgcmVzdWx0ICE9PSBwYXJzZUZsb2F0KG51bWJlcikpIHtcclxuICAgICAgICAgICAgdGhpcy5jdXJzb3IgPSBzdGFydDtcclxuICAgICAgICAgICAgdGhyb3cgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbl8xLmRlZmF1bHQuQlVJTFRfSU5fRVhDRVBUSU9OUy5yZWFkZXJJbnZhbGlkSW50KCkuY3JlYXRlV2l0aENvbnRleHQodGhpcywgbnVtYmVyKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgZWxzZVxyXG4gICAgICAgICAgICByZXR1cm4gcmVzdWx0O1xyXG4gICAgfVxyXG4gICAgcmVhZEZsb2F0KCkge1xyXG4gICAgICAgIGxldCBzdGFydCA9IHRoaXMuY3Vyc29yO1xyXG4gICAgICAgIHdoaWxlICgodGhpcy5jYW5SZWFkKCkgJiYgU3RyaW5nUmVhZGVyLmlzQWxsb3dlZE51bWJlcih0aGlzLnBlZWsoKSkpKSB7XHJcbiAgICAgICAgICAgIHRoaXMuc2tpcCgpO1xyXG4gICAgICAgIH1cclxuICAgICAgICBsZXQgbnVtYmVyID0gdGhpcy5zdHJpbmcuc3Vic3RyaW5nKHN0YXJ0LCB0aGlzLmN1cnNvcik7XHJcbiAgICAgICAgaWYgKG51bWJlci5sZW5ndGggPT09IDApIHtcclxuICAgICAgICAgICAgdGhyb3cgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbl8xLmRlZmF1bHQuQlVJTFRfSU5fRVhDRVBUSU9OUy5yZWFkZXJFeHBlY3RlZEZsb2F0KCkuY3JlYXRlV2l0aENvbnRleHQodGhpcyk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGNvbnN0IHJlc3VsdCA9IHBhcnNlRmxvYXQobnVtYmVyKTtcclxuICAgICAgICBjb25zdCBzdHJpY3RQYXJzZUZsb2F0VGVzdCA9IHBhcnNlRmxvYXQobnVtYmVyLnN1YnN0cmluZyhyZXN1bHQudG9TdHJpbmcoKS5sZW5ndGgsIHRoaXMuY3Vyc29yKSk7XHJcbiAgICAgICAgaWYgKGlzTmFOKHJlc3VsdCkgfHwgKCFpc05hTihzdHJpY3RQYXJzZUZsb2F0VGVzdCkgJiZcclxuICAgICAgICAgICAgc3RyaWN0UGFyc2VGbG9hdFRlc3QgIT09IDApKSB7XHJcbiAgICAgICAgICAgIHRoaXMuY3Vyc29yID0gc3RhcnQ7XHJcbiAgICAgICAgICAgIHRocm93IENvbW1hbmRTeW50YXhFeGNlcHRpb25fMS5kZWZhdWx0LkJVSUxUX0lOX0VYQ0VQVElPTlMucmVhZGVySW52YWxpZEZsb2F0KCkuY3JlYXRlV2l0aENvbnRleHQodGhpcywgbnVtYmVyKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgZWxzZVxyXG4gICAgICAgICAgICByZXR1cm4gcmVzdWx0O1xyXG4gICAgfVxyXG4gICAgc3RhdGljIGlzQWxsb3dlZEluVW5xdW90ZWRTdHJpbmcoYykge1xyXG4gICAgICAgIHJldHVybiBjID49ICcwJyAmJiBjIDw9ICc5J1xyXG4gICAgICAgICAgICB8fCBjID49ICdBJyAmJiBjIDw9ICdaJ1xyXG4gICAgICAgICAgICB8fCBjID49ICdhJyAmJiBjIDw9ICd6J1xyXG4gICAgICAgICAgICB8fCBjID09ICdfJyB8fCBjID09ICctJ1xyXG4gICAgICAgICAgICB8fCBjID09ICcuJyB8fCBjID09ICcrJztcclxuICAgIH1cclxuICAgIHJlYWRVbnF1b3RlZFN0cmluZygpIHtcclxuICAgICAgICBsZXQgc3RhcnQgPSB0aGlzLmN1cnNvcjtcclxuICAgICAgICB3aGlsZSAodGhpcy5jYW5SZWFkKCkgJiYgU3RyaW5nUmVhZGVyLmlzQWxsb3dlZEluVW5xdW90ZWRTdHJpbmcodGhpcy5wZWVrKCkpKSB7XHJcbiAgICAgICAgICAgIHRoaXMuc2tpcCgpO1xyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gdGhpcy5zdHJpbmcuc3Vic3RyaW5nKHN0YXJ0LCB0aGlzLmN1cnNvcik7XHJcbiAgICB9XHJcbiAgICByZWFkUXVvdGVkU3RyaW5nKCkge1xyXG4gICAgICAgIGlmICghdGhpcy5jYW5SZWFkKCkpIHtcclxuICAgICAgICAgICAgcmV0dXJuIFwiXCI7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGVsc2UgaWYgKCh0aGlzLnBlZWsoKSAhPSBTWU5UQVhfUVVPVEUpKSB7XHJcbiAgICAgICAgICAgIHRocm93IENvbW1hbmRTeW50YXhFeGNlcHRpb25fMS5kZWZhdWx0LkJVSUxUX0lOX0VYQ0VQVElPTlMucmVhZGVyRXhwZWN0ZWRTdGFydE9mUXVvdGUoKS5jcmVhdGVXaXRoQ29udGV4dCh0aGlzKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgdGhpcy5za2lwKCk7XHJcbiAgICAgICAgbGV0IHJlc3VsdCA9IFwiXCI7XHJcbiAgICAgICAgbGV0IGVzY2FwZWQgPSBmYWxzZTtcclxuICAgICAgICB3aGlsZSAodGhpcy5jYW5SZWFkKCkpIHtcclxuICAgICAgICAgICAgbGV0IGMgPSB0aGlzLnJlYWQoKTtcclxuICAgICAgICAgICAgaWYgKGVzY2FwZWQpIHtcclxuICAgICAgICAgICAgICAgIGlmIChjID09IFNZTlRBWF9RVU9URSB8fCBjID09IFNZTlRBWF9FU0NBUEUpIHtcclxuICAgICAgICAgICAgICAgICAgICByZXN1bHQgKz0gYztcclxuICAgICAgICAgICAgICAgICAgICBlc2NhcGVkID0gZmFsc2U7XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICBlbHNlIHtcclxuICAgICAgICAgICAgICAgICAgICB0aGlzLnNldEN1cnNvcih0aGlzLmdldEN1cnNvcigpIC0gMSk7XHJcbiAgICAgICAgICAgICAgICAgICAgdGhyb3cgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbl8xLmRlZmF1bHQuQlVJTFRfSU5fRVhDRVBUSU9OUy5yZWFkZXJJbnZhbGlkRXNjYXBlKCkuY3JlYXRlV2l0aENvbnRleHQodGhpcywgYyk7XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgZWxzZSBpZiAoYyA9PSBTWU5UQVhfRVNDQVBFKSB7XHJcbiAgICAgICAgICAgICAgICBlc2NhcGVkID0gdHJ1ZTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICBlbHNlIGlmIChjID09IFNZTlRBWF9RVU9URSkge1xyXG4gICAgICAgICAgICAgICAgcmV0dXJuIHJlc3VsdDtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICBlbHNlIHtcclxuICAgICAgICAgICAgICAgIHJlc3VsdCArPSBjO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHRocm93IENvbW1hbmRTeW50YXhFeGNlcHRpb25fMS5kZWZhdWx0LkJVSUxUX0lOX0VYQ0VQVElPTlMucmVhZGVyRXhwZWN0ZWRFbmRPZlF1b3RlKCkuY3JlYXRlV2l0aENvbnRleHQodGhpcyk7XHJcbiAgICB9XHJcbiAgICByZWFkU3RyaW5nKCkge1xyXG4gICAgICAgIGlmICh0aGlzLmNhblJlYWQoKSAmJiAodGhpcy5wZWVrKCkgPT09IFNZTlRBWF9RVU9URSkpIHtcclxuICAgICAgICAgICAgcmV0dXJuIHRoaXMucmVhZFF1b3RlZFN0cmluZygpO1xyXG4gICAgICAgIH1cclxuICAgICAgICBlbHNlIHtcclxuICAgICAgICAgICAgcmV0dXJuIHRoaXMucmVhZFVucXVvdGVkU3RyaW5nKCk7XHJcbiAgICAgICAgfVxyXG4gICAgfVxyXG4gICAgcmVhZEJvb2xlYW4oKSB7XHJcbiAgICAgICAgbGV0IHN0YXJ0ID0gdGhpcy5jdXJzb3I7XHJcbiAgICAgICAgbGV0IHZhbHVlID0gdGhpcy5yZWFkU3RyaW5nKCk7XHJcbiAgICAgICAgaWYgKHZhbHVlLmxlbmd0aCA9PT0gMCkge1xyXG4gICAgICAgICAgICB0aHJvdyBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEuZGVmYXVsdC5CVUlMVF9JTl9FWENFUFRJT05TLnJlYWRlckV4cGVjdGVkQm9vbCgpLmNyZWF0ZVdpdGhDb250ZXh0KHRoaXMpO1xyXG4gICAgICAgIH1cclxuICAgICAgICBpZiAodmFsdWUgPT09IFwidHJ1ZVwiKSB7XHJcbiAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgIH1cclxuICAgICAgICBlbHNlIGlmICh2YWx1ZSA9PT0gXCJmYWxzZVwiKSB7XHJcbiAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICB9XHJcbiAgICAgICAgZWxzZSB7XHJcbiAgICAgICAgICAgIHRoaXMuY3Vyc29yID0gc3RhcnQ7XHJcbiAgICAgICAgICAgIHRocm93IENvbW1hbmRTeW50YXhFeGNlcHRpb25fMS5kZWZhdWx0LkJVSUxUX0lOX0VYQ0VQVElPTlMucmVhZGVySW52YWxpZEJvb2woKS5jcmVhdGVXaXRoQ29udGV4dCh0aGlzLCB2YWx1ZSk7XHJcbiAgICAgICAgfVxyXG4gICAgfVxyXG4gICAgZXhwZWN0KGMpIHtcclxuICAgICAgICBpZiAoIXRoaXMuY2FuUmVhZCgpIHx8IHRoaXMucGVlaygpICE9PSBjKSB7XHJcbiAgICAgICAgICAgIHRocm93IENvbW1hbmRTeW50YXhFeGNlcHRpb25fMS5kZWZhdWx0LkJVSUxUX0lOX0VYQ0VQVElPTlMucmVhZGVyRXhwZWN0ZWRTeW1ib2woKS5jcmVhdGVXaXRoQ29udGV4dCh0aGlzLCBjKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgdGhpcy5za2lwKCk7XHJcbiAgICB9XHJcbn1cclxuZXhwb3J0cy5kZWZhdWx0ID0gU3RyaW5nUmVhZGVyO1xyXG4iLCJcInVzZSBzdHJpY3RcIjtcclxudmFyIF9faW1wb3J0RGVmYXVsdCA9ICh0aGlzICYmIHRoaXMuX19pbXBvcnREZWZhdWx0KSB8fCBmdW5jdGlvbiAobW9kKSB7XHJcbiAgICByZXR1cm4gKG1vZCAmJiBtb2QuX19lc01vZHVsZSkgPyBtb2QgOiB7IFwiZGVmYXVsdFwiOiBtb2QgfTtcclxufTtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5jb25zdCBCb29sQXJndW1lbnRUeXBlXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vQm9vbEFyZ3VtZW50VHlwZVwiKSk7XHJcbmNvbnN0IEludGVnZXJBcmd1bWVudFR5cGVfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9JbnRlZ2VyQXJndW1lbnRUeXBlXCIpKTtcclxuY29uc3QgRmxvYXRBcmd1bWVudFR5cGVfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9GbG9hdEFyZ3VtZW50VHlwZVwiKSk7XHJcbmNvbnN0IFN0cmluZ0FyZ3VtZW50VHlwZV8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL1N0cmluZ0FyZ3VtZW50VHlwZVwiKSk7XHJcbmV4cG9ydHMuRGVmYXVsdFR5cGUgPSB7XHJcbiAgICBib29sOiBCb29sQXJndW1lbnRUeXBlXzEuZGVmYXVsdC5ib29sLFxyXG4gICAgaW50ZWdlcjogSW50ZWdlckFyZ3VtZW50VHlwZV8xLmRlZmF1bHQuaW50ZWdlcixcclxuICAgIGZsb2F0OiBGbG9hdEFyZ3VtZW50VHlwZV8xLmRlZmF1bHQuZmxvYXQsXHJcbiAgICB3b3JkOiBTdHJpbmdBcmd1bWVudFR5cGVfMS5kZWZhdWx0LndvcmQsXHJcbiAgICBzdHJpbmc6IFN0cmluZ0FyZ3VtZW50VHlwZV8xLmRlZmF1bHQuc3RyaW5nLFxyXG4gICAgZ3JlZWR5U3RyaW5nOiBTdHJpbmdBcmd1bWVudFR5cGVfMS5kZWZhdWx0LmdyZWVkeVN0cmluZ1xyXG59O1xyXG4iLCJcInVzZSBzdHJpY3RcIjtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5jb25zdCBFWEFNUExFUyA9IFtcInRydWVcIiwgXCJmYWxzZVwiXTtcclxuY2xhc3MgQm9vbEFyZ3VtZW50VHlwZSB7XHJcbiAgICBjb25zdHJ1Y3RvcigpIHtcclxuICAgIH1cclxuICAgIHN0YXRpYyBib29sKCkge1xyXG4gICAgICAgIHJldHVybiBuZXcgQm9vbEFyZ3VtZW50VHlwZSgpO1xyXG4gICAgfVxyXG4gICAgc3RhdGljIGdldEJvb2woY29udGV4dCwgbmFtZSkge1xyXG4gICAgICAgIHJldHVybiBjb250ZXh0LmdldEFyZ3VtZW50KG5hbWUsIEJvb2xlYW4pO1xyXG4gICAgfVxyXG4gICAgcGFyc2UocmVhZGVyKSB7XHJcbiAgICAgICAgcmV0dXJuIHJlYWRlci5yZWFkQm9vbGVhbigpO1xyXG4gICAgfVxyXG4gICAgbGlzdFN1Z2dlc3Rpb25zKGNvbnRleHQsIGJ1aWxkZXIpIHtcclxuICAgICAgICBpZiAoXCJ0cnVlXCIuc3RhcnRzV2l0aChidWlsZGVyLmdldFJlbWFpbmluZygpLnRvTG93ZXJDYXNlKCkpKSB7XHJcbiAgICAgICAgICAgIGJ1aWxkZXIuc3VnZ2VzdChcInRydWVcIik7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGlmIChcImZhbHNlXCIuc3RhcnRzV2l0aChidWlsZGVyLmdldFJlbWFpbmluZygpLnRvTG93ZXJDYXNlKCkpKSB7XHJcbiAgICAgICAgICAgIGJ1aWxkZXIuc3VnZ2VzdChcImZhbHNlXCIpO1xyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gYnVpbGRlci5idWlsZFByb21pc2UoKTtcclxuICAgIH1cclxuICAgIGdldEV4YW1wbGVzKCkge1xyXG4gICAgICAgIHJldHVybiBFWEFNUExFUztcclxuICAgIH1cclxufVxyXG5leHBvcnRzLmRlZmF1bHQgPSBCb29sQXJndW1lbnRUeXBlO1xyXG4iLCJcInVzZSBzdHJpY3RcIjtcclxudmFyIF9faW1wb3J0RGVmYXVsdCA9ICh0aGlzICYmIHRoaXMuX19pbXBvcnREZWZhdWx0KSB8fCBmdW5jdGlvbiAobW9kKSB7XHJcbiAgICByZXR1cm4gKG1vZCAmJiBtb2QuX19lc01vZHVsZSkgPyBtb2QgOiB7IFwiZGVmYXVsdFwiOiBtb2QgfTtcclxufTtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5jb25zdCBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4uL2V4Y2VwdGlvbnMvQ29tbWFuZFN5bnRheEV4Y2VwdGlvblwiKSk7XHJcbmNvbnN0IEVYQU1QTEVTID0gW1wiMFwiLCBcIjEuMlwiLCBcIi41XCIsIFwiLTFcIiwgXCItLjVcIiwgXCItMTIzNC41NlwiXTtcclxuY2xhc3MgRmxvYXRBcmd1bWVudFR5cGUge1xyXG4gICAgY29uc3RydWN0b3IobWluaW11bSwgbWF4aW11bSkge1xyXG4gICAgICAgIHRoaXMubWluaW11bSA9IG1pbmltdW07XHJcbiAgICAgICAgdGhpcy5tYXhpbXVtID0gbWF4aW11bTtcclxuICAgIH1cclxuICAgIHN0YXRpYyBmbG9hdChtaW4gPSAtSW5maW5pdHksIG1heCA9IEluZmluaXR5KSB7XHJcbiAgICAgICAgcmV0dXJuIG5ldyBGbG9hdEFyZ3VtZW50VHlwZShtaW4sIG1heCk7XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgZ2V0RmxvYXQoY29udGV4dCwgbmFtZSkge1xyXG4gICAgICAgIHJldHVybiBjb250ZXh0LmdldEFyZ3VtZW50KG5hbWUsIE51bWJlcik7XHJcbiAgICB9XHJcbiAgICBnZXRNaW5pbXVtKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLm1pbmltdW07XHJcbiAgICB9XHJcbiAgICBnZXRNYXhpbXVtKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLm1heGltdW07XHJcbiAgICB9XHJcbiAgICBwYXJzZShyZWFkZXIpIHtcclxuICAgICAgICBsZXQgc3RhcnQgPSByZWFkZXIuZ2V0Q3Vyc29yKCk7XHJcbiAgICAgICAgbGV0IHJlc3VsdCA9IHJlYWRlci5yZWFkRmxvYXQoKTtcclxuICAgICAgICBpZiAocmVzdWx0IDwgdGhpcy5taW5pbXVtKSB7XHJcbiAgICAgICAgICAgIHJlYWRlci5zZXRDdXJzb3Ioc3RhcnQpO1xyXG4gICAgICAgICAgICB0aHJvdyBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEuZGVmYXVsdC5CVUlMVF9JTl9FWENFUFRJT05TLmludGVnZXJUb29Mb3coKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIsIHJlc3VsdCwgdGhpcy5taW5pbXVtKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgaWYgKHJlc3VsdCA+IHRoaXMubWF4aW11bSkge1xyXG4gICAgICAgICAgICByZWFkZXIuc2V0Q3Vyc29yKHN0YXJ0KTtcclxuICAgICAgICAgICAgdGhyb3cgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbl8xLmRlZmF1bHQuQlVJTFRfSU5fRVhDRVBUSU9OUy5pbnRlZ2VyVG9vSGlnaCgpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlciwgcmVzdWx0LCB0aGlzLm1heGltdW0pO1xyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gcmVzdWx0O1xyXG4gICAgfVxyXG4gICAgZXF1YWxzKG8pIHtcclxuICAgICAgICBpZiAodGhpcyA9PT0gbylcclxuICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgaWYgKCEobyBpbnN0YW5jZW9mIEZsb2F0QXJndW1lbnRUeXBlKSlcclxuICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgIHJldHVybiB0aGlzLm1heGltdW0gPT0gby5tYXhpbXVtICYmIHRoaXMubWluaW11bSA9PSBvLm1pbmltdW07XHJcbiAgICB9XHJcbiAgICB0b1N0cmluZygpIHtcclxuICAgICAgICBpZiAodGhpcy5taW5pbXVtID09PSAtSW5maW5pdHkgJiYgdGhpcy5tYXhpbXVtID09PSBJbmZpbml0eSkge1xyXG4gICAgICAgICAgICByZXR1cm4gXCJmbG9hdCgpXCI7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGVsc2UgaWYgKHRoaXMubWF4aW11bSA9PSBJbmZpbml0eSkge1xyXG4gICAgICAgICAgICByZXR1cm4gXCJmbG9hdChcIiArIHRoaXMubWluaW11bSArIFwiKVwiO1xyXG4gICAgICAgIH1cclxuICAgICAgICBlbHNlIHtcclxuICAgICAgICAgICAgcmV0dXJuIFwiZmxvYXQoXCIgKyB0aGlzLm1pbmltdW0gKyBcIiwgXCIgKyB0aGlzLm1heGltdW0gKyBcIilcIjtcclxuICAgICAgICB9XHJcbiAgICB9XHJcbiAgICBnZXRFeGFtcGxlcygpIHtcclxuICAgICAgICByZXR1cm4gRVhBTVBMRVM7XHJcbiAgICB9XHJcbn1cclxuZXhwb3J0cy5kZWZhdWx0ID0gRmxvYXRBcmd1bWVudFR5cGU7XHJcbiIsIlwidXNlIHN0cmljdFwiO1xyXG52YXIgX19pbXBvcnREZWZhdWx0ID0gKHRoaXMgJiYgdGhpcy5fX2ltcG9ydERlZmF1bHQpIHx8IGZ1bmN0aW9uIChtb2QpIHtcclxuICAgIHJldHVybiAobW9kICYmIG1vZC5fX2VzTW9kdWxlKSA/IG1vZCA6IHsgXCJkZWZhdWx0XCI6IG1vZCB9O1xyXG59O1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNvbnN0IENvbW1hbmRTeW50YXhFeGNlcHRpb25fMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi4vZXhjZXB0aW9ucy9Db21tYW5kU3ludGF4RXhjZXB0aW9uXCIpKTtcclxuY29uc3QgRVhBTVBMRVMgPSBbXCIwXCIsIFwiMTIzXCIsIFwiLTEyM1wiXTtcclxuY2xhc3MgSW50ZWdlckFyZ3VtZW50VHlwZSB7XHJcbiAgICBjb25zdHJ1Y3RvcihtaW5pbXVtLCBtYXhpbXVtKSB7XHJcbiAgICAgICAgdGhpcy5taW5pbXVtID0gbWluaW11bTtcclxuICAgICAgICB0aGlzLm1heGltdW0gPSBtYXhpbXVtO1xyXG4gICAgfVxyXG4gICAgc3RhdGljIGludGVnZXIobWluID0gLUluZmluaXR5LCBtYXggPSBJbmZpbml0eSkge1xyXG4gICAgICAgIHJldHVybiBuZXcgSW50ZWdlckFyZ3VtZW50VHlwZShtaW4sIG1heCk7XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgZ2V0SW50ZWdlcihjb250ZXh0LCBuYW1lKSB7XHJcbiAgICAgICAgcmV0dXJuIGNvbnRleHQuZ2V0QXJndW1lbnQobmFtZSwgTnVtYmVyKTtcclxuICAgIH1cclxuICAgIGdldE1pbmltdW0oKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMubWluaW11bTtcclxuICAgIH1cclxuICAgIGdldE1heGltdW0oKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMubWF4aW11bTtcclxuICAgIH1cclxuICAgIHBhcnNlKHJlYWRlcikge1xyXG4gICAgICAgIGxldCBzdGFydCA9IHJlYWRlci5nZXRDdXJzb3IoKTtcclxuICAgICAgICBsZXQgcmVzdWx0ID0gcmVhZGVyLnJlYWRJbnQoKTtcclxuICAgICAgICBpZiAocmVzdWx0IDwgdGhpcy5taW5pbXVtKSB7XHJcbiAgICAgICAgICAgIHJlYWRlci5zZXRDdXJzb3Ioc3RhcnQpO1xyXG4gICAgICAgICAgICB0aHJvdyBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEuZGVmYXVsdC5CVUlMVF9JTl9FWENFUFRJT05TLmludGVnZXJUb29Mb3coKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIsIHJlc3VsdCwgdGhpcy5taW5pbXVtKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgaWYgKHJlc3VsdCA+IHRoaXMubWF4aW11bSkge1xyXG4gICAgICAgICAgICByZWFkZXIuc2V0Q3Vyc29yKHN0YXJ0KTtcclxuICAgICAgICAgICAgdGhyb3cgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbl8xLmRlZmF1bHQuQlVJTFRfSU5fRVhDRVBUSU9OUy5pbnRlZ2VyVG9vSGlnaCgpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlciwgcmVzdWx0LCB0aGlzLm1heGltdW0pO1xyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gcmVzdWx0O1xyXG4gICAgfVxyXG4gICAgZXF1YWxzKG8pIHtcclxuICAgICAgICBpZiAodGhpcyA9PT0gbylcclxuICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgaWYgKCEobyBpbnN0YW5jZW9mIEludGVnZXJBcmd1bWVudFR5cGUpKVxyXG4gICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMubWF4aW11bSA9PSBvLm1heGltdW0gJiYgdGhpcy5taW5pbXVtID09IG8ubWluaW11bTtcclxuICAgIH1cclxuICAgIHRvU3RyaW5nKCkge1xyXG4gICAgICAgIGlmICh0aGlzLm1pbmltdW0gPT09IC1JbmZpbml0eSAmJiB0aGlzLm1heGltdW0gPT09IEluZmluaXR5KSB7XHJcbiAgICAgICAgICAgIHJldHVybiBcImludGVnZXIoKVwiO1xyXG4gICAgICAgIH1cclxuICAgICAgICBlbHNlIGlmICh0aGlzLm1heGltdW0gPT0gSW5maW5pdHkpIHtcclxuICAgICAgICAgICAgcmV0dXJuIFwiaW50ZWdlcihcIiArIHRoaXMubWluaW11bSArIFwiKVwiO1xyXG4gICAgICAgIH1cclxuICAgICAgICBlbHNlIHtcclxuICAgICAgICAgICAgcmV0dXJuIFwiaW50ZWdlcihcIiArIHRoaXMubWluaW11bSArIFwiLCBcIiArIHRoaXMubWF4aW11bSArIFwiKVwiO1xyXG4gICAgICAgIH1cclxuICAgIH1cclxuICAgIGdldEV4YW1wbGVzKCkge1xyXG4gICAgICAgIHJldHVybiBFWEFNUExFUztcclxuICAgIH1cclxufVxyXG5leHBvcnRzLmRlZmF1bHQgPSBJbnRlZ2VyQXJndW1lbnRUeXBlO1xyXG4iLCJcInVzZSBzdHJpY3RcIjtcclxudmFyIF9faW1wb3J0RGVmYXVsdCA9ICh0aGlzICYmIHRoaXMuX19pbXBvcnREZWZhdWx0KSB8fCBmdW5jdGlvbiAobW9kKSB7XHJcbiAgICByZXR1cm4gKG1vZCAmJiBtb2QuX19lc01vZHVsZSkgPyBtb2QgOiB7IFwiZGVmYXVsdFwiOiBtb2QgfTtcclxufTtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5jb25zdCBTdHJpbmdSZWFkZXJfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi4vU3RyaW5nUmVhZGVyXCIpKTtcclxudmFyIFN0cmluZ1R5cGU7XHJcbihmdW5jdGlvbiAoU3RyaW5nVHlwZSkge1xyXG4gICAgU3RyaW5nVHlwZVtcIlNJTkdMRV9XT1JEXCJdID0gXCJ3b3Jkc193aXRoX3VuZGVyc2NvcmVzXCI7XHJcbiAgICBTdHJpbmdUeXBlW1wiUVVPVEFCTEVfUEhSQVNFXCJdID0gXCJcXFwicXVvdGVkIHBocmFzZVxcXCJcIjtcclxuICAgIFN0cmluZ1R5cGVbXCJHUkVFRFlfUEhSQVNFXCJdID0gXCJ3b3JkcyB3aXRoIHNwYWNlc1wiO1xyXG59KShTdHJpbmdUeXBlID0gZXhwb3J0cy5TdHJpbmdUeXBlIHx8IChleHBvcnRzLlN0cmluZ1R5cGUgPSB7fSkpO1xyXG5jbGFzcyBTdHJpbmdBcmd1bWVudFR5cGUge1xyXG4gICAgY29uc3RydWN0b3IodHlwZSkge1xyXG4gICAgICAgIHRoaXMudHlwZSA9IHR5cGU7XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgd29yZCgpIHtcclxuICAgICAgICByZXR1cm4gbmV3IFN0cmluZ0FyZ3VtZW50VHlwZShTdHJpbmdUeXBlLlNJTkdMRV9XT1JEKTtcclxuICAgIH1cclxuICAgIHN0YXRpYyBzdHJpbmcoKSB7XHJcbiAgICAgICAgcmV0dXJuIG5ldyBTdHJpbmdBcmd1bWVudFR5cGUoU3RyaW5nVHlwZS5RVU9UQUJMRV9QSFJBU0UpO1xyXG4gICAgfVxyXG4gICAgc3RhdGljIGdyZWVkeVN0cmluZygpIHtcclxuICAgICAgICByZXR1cm4gbmV3IFN0cmluZ0FyZ3VtZW50VHlwZShTdHJpbmdUeXBlLkdSRUVEWV9QSFJBU0UpO1xyXG4gICAgfVxyXG4gICAgc3RhdGljIGdldFN0cmluZyhjb250ZXh0LCBuYW1lKSB7XHJcbiAgICAgICAgcmV0dXJuIGNvbnRleHQuZ2V0QXJndW1lbnQobmFtZSwgU3RyaW5nKTtcclxuICAgIH1cclxuICAgIGdldFR5cGUoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMudHlwZTtcclxuICAgIH1cclxuICAgIHBhcnNlKHJlYWRlcikge1xyXG4gICAgICAgIGlmICh0aGlzLnR5cGUgPT0gU3RyaW5nVHlwZS5HUkVFRFlfUEhSQVNFKSB7XHJcbiAgICAgICAgICAgIGxldCB0ZXh0ID0gcmVhZGVyLmdldFJlbWFpbmluZygpO1xyXG4gICAgICAgICAgICByZWFkZXIuc2V0Q3Vyc29yKHJlYWRlci5nZXRUb3RhbExlbmd0aCgpKTtcclxuICAgICAgICAgICAgcmV0dXJuIHRleHQ7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGVsc2UgaWYgKHRoaXMudHlwZSA9PSBTdHJpbmdUeXBlLlNJTkdMRV9XT1JEKSB7XHJcbiAgICAgICAgICAgIHJldHVybiByZWFkZXIucmVhZFVucXVvdGVkU3RyaW5nKCk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGVsc2Uge1xyXG4gICAgICAgICAgICByZXR1cm4gcmVhZGVyLnJlYWRTdHJpbmcoKTtcclxuICAgICAgICB9XHJcbiAgICB9XHJcbiAgICB0b1N0cmluZygpIHtcclxuICAgICAgICByZXR1cm4gXCJzdHJpbmcoKVwiO1xyXG4gICAgfVxyXG4gICAgc3RhdGljIGVzY2FwZUlmUmVxdWlyZWQoaW5wdXQpIHtcclxuICAgICAgICBmb3IgKGxldCBjIG9mIGlucHV0KSB7XHJcbiAgICAgICAgICAgIGlmICghU3RyaW5nUmVhZGVyXzEuZGVmYXVsdC5pc0FsbG93ZWRJblVucXVvdGVkU3RyaW5nKGMpKSB7XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gU3RyaW5nQXJndW1lbnRUeXBlLmVzY2FwZShpbnB1dCk7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9XHJcbiAgICAgICAgcmV0dXJuIGlucHV0O1xyXG4gICAgfVxyXG4gICAgc3RhdGljIGVzY2FwZShpbnB1dCkge1xyXG4gICAgICAgIGxldCByZXN1bHQgPSBcIlxcXCJcIjtcclxuICAgICAgICBmb3IgKGxldCBpID0gMDsgaSA8IGlucHV0Lmxlbmd0aDsgaSsrKSB7XHJcbiAgICAgICAgICAgIGNvbnN0IGMgPSBpbnB1dC5jaGFyQXQoaSk7XHJcbiAgICAgICAgICAgIGlmIChjID09ICdcXFxcJyB8fCBjID09ICdcIicpIHtcclxuICAgICAgICAgICAgICAgIHJlc3VsdCArPSAnXFxcXCc7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgcmVzdWx0ICs9IGM7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHJlc3VsdCArPSBcIlxcXCJcIjtcclxuICAgICAgICByZXR1cm4gcmVzdWx0O1xyXG4gICAgfVxyXG59XHJcbmV4cG9ydHMuZGVmYXVsdCA9IFN0cmluZ0FyZ3VtZW50VHlwZTtcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbnZhciBfX2ltcG9ydERlZmF1bHQgPSAodGhpcyAmJiB0aGlzLl9faW1wb3J0RGVmYXVsdCkgfHwgZnVuY3Rpb24gKG1vZCkge1xyXG4gICAgcmV0dXJuIChtb2QgJiYgbW9kLl9fZXNNb2R1bGUpID8gbW9kIDogeyBcImRlZmF1bHRcIjogbW9kIH07XHJcbn07XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY29uc3QgQ29tbWFuZE5vZGVfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi4vdHJlZS9Db21tYW5kTm9kZVwiKSk7XHJcbmNvbnN0IFJvb3RDb21tYW5kTm9kZV8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuLi90cmVlL1Jvb3RDb21tYW5kTm9kZVwiKSk7XHJcbmNsYXNzIEFyZ3VtZW50QnVpbGRlciB7XHJcbiAgICBjb25zdHJ1Y3RvcigpIHtcclxuICAgICAgICB0aGlzLmFyZ3MgPSBuZXcgUm9vdENvbW1hbmROb2RlXzEuZGVmYXVsdCgpO1xyXG4gICAgICAgIHRoaXMubW9kaWZpZXIgPSBudWxsO1xyXG4gICAgfVxyXG4gICAgdGhlbihhcmcpIHtcclxuICAgICAgICBpZiAoISh0aGlzLnRhcmdldCA9PSBudWxsKSkge1xyXG4gICAgICAgICAgICB0aHJvdyBuZXcgRXJyb3IoXCJDYW5ub3QgYWRkIGNoaWxkcmVuIHRvIGEgcmVkaXJlY3RlZCBub2RlXCIpO1xyXG4gICAgICAgIH1cclxuICAgICAgICBpZiAoYXJnIGluc3RhbmNlb2YgQ29tbWFuZE5vZGVfMS5kZWZhdWx0KVxyXG4gICAgICAgICAgICB0aGlzLmFyZ3MuYWRkQ2hpbGQoYXJnKTtcclxuICAgICAgICBlbHNlXHJcbiAgICAgICAgICAgIHRoaXMuYXJncy5hZGRDaGlsZChhcmcuYnVpbGQoKSk7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuZ2V0VGhpcygpO1xyXG4gICAgfVxyXG4gICAgZ2V0QXJndW1lbnRzKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmFyZ3MuZ2V0Q2hpbGRyZW4oKTtcclxuICAgIH1cclxuICAgIGV4ZWN1dGVzKGNvbW1hbmQpIHtcclxuICAgICAgICB0aGlzLmNvbW1hbmQgPSBjb21tYW5kO1xyXG4gICAgICAgIHJldHVybiB0aGlzLmdldFRoaXMoKTtcclxuICAgIH1cclxuICAgIGdldENvbW1hbmQoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuY29tbWFuZDtcclxuICAgIH1cclxuICAgIHJlcXVpcmVzKHJlcXVpcmVtZW50KSB7XHJcbiAgICAgICAgdGhpcy5yZXF1aXJlbWVudCA9IHJlcXVpcmVtZW50O1xyXG4gICAgICAgIHJldHVybiB0aGlzLmdldFRoaXMoKTtcclxuICAgIH1cclxuICAgIGdldFJlcXVpcmVtZW50KCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnJlcXVpcmVtZW50O1xyXG4gICAgfVxyXG4gICAgcmVkaXJlY3QodGFyZ2V0LCBtb2RpZmllcikge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmZvcndhcmQodGFyZ2V0LCBtb2RpZmllciA9PSBudWxsID8gbnVsbCA6IChvKSA9PiBbbW9kaWZpZXIuYXBwbHkobyldLCBmYWxzZSk7XHJcbiAgICB9XHJcbiAgICBmb3JrKHRhcmdldCwgbW9kaWZpZXIpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5mb3J3YXJkKHRhcmdldCwgbW9kaWZpZXIsIHRydWUpO1xyXG4gICAgfVxyXG4gICAgZm9yd2FyZCh0YXJnZXQsIG1vZGlmaWVyLCBmb3JrKSB7XHJcbiAgICAgICAgaWYgKHRoaXMuYXJncy5nZXRDaGlsZHJlbkNvdW50KCkgPiAwKSB7XHJcbiAgICAgICAgICAgIHRocm93IG5ldyBFcnJvcihcIkNhbm5vdCBmb3J3YXJkIGEgbm9kZSB3aXRoIGNoaWxkcmVuXCIpO1xyXG4gICAgICAgIH1cclxuICAgICAgICB0aGlzLnRhcmdldCA9IHRhcmdldDtcclxuICAgICAgICB0aGlzLm1vZGlmaWVyID0gbW9kaWZpZXI7XHJcbiAgICAgICAgdGhpcy5mb3JrcyA9IGZvcms7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuZ2V0VGhpcygpO1xyXG4gICAgfVxyXG4gICAgZ2V0UmVkaXJlY3QoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMudGFyZ2V0O1xyXG4gICAgfVxyXG4gICAgZ2V0UmVkaXJlY3RNb2RpZmllcigpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5tb2RpZmllcjtcclxuICAgIH1cclxuICAgIGlzRm9yaygpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5mb3JrcztcclxuICAgIH1cclxufVxyXG5leHBvcnRzLmRlZmF1bHQgPSBBcmd1bWVudEJ1aWxkZXI7XHJcbiIsIlwidXNlIHN0cmljdFwiO1xyXG52YXIgX19pbXBvcnREZWZhdWx0ID0gKHRoaXMgJiYgdGhpcy5fX2ltcG9ydERlZmF1bHQpIHx8IGZ1bmN0aW9uIChtb2QpIHtcclxuICAgIHJldHVybiAobW9kICYmIG1vZC5fX2VzTW9kdWxlKSA/IG1vZCA6IHsgXCJkZWZhdWx0XCI6IG1vZCB9O1xyXG59O1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNvbnN0IExpdGVyYWxDb21tYW5kTm9kZV8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuLi90cmVlL0xpdGVyYWxDb21tYW5kTm9kZVwiKSk7XHJcbmNvbnN0IEFyZ3VtZW50QnVpbGRlcl8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL0FyZ3VtZW50QnVpbGRlclwiKSk7XHJcbmNsYXNzIExpdGVyYWxBcmd1bWVudEJ1aWxkZXIgZXh0ZW5kcyBBcmd1bWVudEJ1aWxkZXJfMS5kZWZhdWx0IHtcclxuICAgIGNvbnN0cnVjdG9yKGxpdGVyYWwpIHtcclxuICAgICAgICBzdXBlcigpO1xyXG4gICAgICAgIHRoaXMubGl0ZXJhbCA9IGxpdGVyYWw7XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgbGl0ZXJhbChuYW1lKSB7XHJcbiAgICAgICAgcmV0dXJuIG5ldyBMaXRlcmFsQXJndW1lbnRCdWlsZGVyKG5hbWUpO1xyXG4gICAgfVxyXG4gICAgZ2V0VGhpcygpIHtcclxuICAgICAgICByZXR1cm4gdGhpcztcclxuICAgIH1cclxuICAgIGdldExpdGVyYWwoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMubGl0ZXJhbDtcclxuICAgIH1cclxuICAgIGJ1aWxkKCkge1xyXG4gICAgICAgIGxldCByZXN1bHQgPSBuZXcgTGl0ZXJhbENvbW1hbmROb2RlXzEuZGVmYXVsdCh0aGlzLmdldExpdGVyYWwoKSwgdGhpcy5nZXRDb21tYW5kKCksIHRoaXMuZ2V0UmVxdWlyZW1lbnQoKSwgdGhpcy5nZXRSZWRpcmVjdCgpLCB0aGlzLmdldFJlZGlyZWN0TW9kaWZpZXIoKSwgdGhpcy5pc0ZvcmsoKSk7XHJcbiAgICAgICAgZm9yIChsZXQgYXJnIG9mIHRoaXMuZ2V0QXJndW1lbnRzKCkpIHtcclxuICAgICAgICAgICAgcmVzdWx0LmFkZENoaWxkKGFyZyk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHJldHVybiByZXN1bHQ7XHJcbiAgICB9XHJcbn1cclxuZXhwb3J0cy5kZWZhdWx0ID0gTGl0ZXJhbEFyZ3VtZW50QnVpbGRlcjtcclxuZXhwb3J0cy5saXRlcmFsID0gTGl0ZXJhbEFyZ3VtZW50QnVpbGRlci5saXRlcmFsO1xyXG4iLCJcInVzZSBzdHJpY3RcIjtcclxudmFyIF9faW1wb3J0RGVmYXVsdCA9ICh0aGlzICYmIHRoaXMuX19pbXBvcnREZWZhdWx0KSB8fCBmdW5jdGlvbiAobW9kKSB7XHJcbiAgICByZXR1cm4gKG1vZCAmJiBtb2QuX19lc01vZHVsZSkgPyBtb2QgOiB7IFwiZGVmYXVsdFwiOiBtb2QgfTtcclxufTtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5jb25zdCBBcmd1bWVudENvbW1hbmROb2RlXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4uL3RyZWUvQXJndW1lbnRDb21tYW5kTm9kZVwiKSk7XHJcbmNvbnN0IEFyZ3VtZW50QnVpbGRlcl8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL0FyZ3VtZW50QnVpbGRlclwiKSk7XHJcbmNsYXNzIFJlcXVpcmVkQXJndW1lbnRCdWlsZGVyIGV4dGVuZHMgQXJndW1lbnRCdWlsZGVyXzEuZGVmYXVsdCB7XHJcbiAgICBjb25zdHJ1Y3RvcihuYW1lLCB0eXBlKSB7XHJcbiAgICAgICAgc3VwZXIoKTtcclxuICAgICAgICB0aGlzLm5hbWUgPSBuYW1lO1xyXG4gICAgICAgIHRoaXMudHlwZSA9IHR5cGU7XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgYXJndW1lbnQobmFtZSwgdHlwZSkge1xyXG4gICAgICAgIHJldHVybiBuZXcgUmVxdWlyZWRBcmd1bWVudEJ1aWxkZXIobmFtZSwgdHlwZSk7XHJcbiAgICB9XHJcbiAgICBzdWdnZXN0cyhwcm92aWRlcikge1xyXG4gICAgICAgIHRoaXMuc3VnZ2VzdGlvbnNQcm92aWRlciA9IHByb3ZpZGVyO1xyXG4gICAgICAgIHJldHVybiB0aGlzLmdldFRoaXMoKTtcclxuICAgIH1cclxuICAgIGdldFN1Z2dlc3Rpb25zUHJvdmlkZXIoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuc3VnZ2VzdGlvbnNQcm92aWRlcjtcclxuICAgIH1cclxuICAgIGdldFRoaXMoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXM7XHJcbiAgICB9XHJcbiAgICBnZXRUeXBlKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnR5cGU7XHJcbiAgICB9XHJcbiAgICBnZXROYW1lKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLm5hbWU7XHJcbiAgICB9XHJcbiAgICBidWlsZCgpIHtcclxuICAgICAgICBsZXQgcmVzdWx0ID0gbmV3IEFyZ3VtZW50Q29tbWFuZE5vZGVfMS5kZWZhdWx0KHRoaXMuZ2V0TmFtZSgpLCB0aGlzLmdldFR5cGUoKSwgdGhpcy5nZXRDb21tYW5kKCksIHRoaXMuZ2V0UmVxdWlyZW1lbnQoKSwgdGhpcy5nZXRSZWRpcmVjdCgpLCB0aGlzLmdldFJlZGlyZWN0TW9kaWZpZXIoKSwgdGhpcy5pc0ZvcmsoKSwgdGhpcy5nZXRTdWdnZXN0aW9uc1Byb3ZpZGVyKCkpO1xyXG4gICAgICAgIGZvciAobGV0IGFyZyBvZiB0aGlzLmdldEFyZ3VtZW50cygpKSB7XHJcbiAgICAgICAgICAgIHJlc3VsdC5hZGRDaGlsZChhcmcpO1xyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gcmVzdWx0O1xyXG4gICAgfVxyXG59XHJcbmV4cG9ydHMuZGVmYXVsdCA9IFJlcXVpcmVkQXJndW1lbnRCdWlsZGVyO1xyXG5leHBvcnRzLmFyZ3VtZW50ID0gUmVxdWlyZWRBcmd1bWVudEJ1aWxkZXIuYXJndW1lbnQ7XHJcbiIsIlwidXNlIHN0cmljdFwiO1xyXG52YXIgX19pbXBvcnREZWZhdWx0ID0gKHRoaXMgJiYgdGhpcy5fX2ltcG9ydERlZmF1bHQpIHx8IGZ1bmN0aW9uIChtb2QpIHtcclxuICAgIHJldHVybiAobW9kICYmIG1vZC5fX2VzTW9kdWxlKSA/IG1vZCA6IHsgXCJkZWZhdWx0XCI6IG1vZCB9O1xyXG59O1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNvbnN0IGlzRXF1YWxfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi4vdXRpbC9pc0VxdWFsXCIpKTtcclxuY2xhc3MgQ29tbWFuZENvbnRleHQge1xyXG4gICAgY29uc3RydWN0b3Ioc291cmNlLCBpbnB1dCwgYXJncywgY29tbWFuZCwgcm9vdE5vZGUsIG5vZGVzLCByYW5nZSwgY2hpbGQsIG1vZGlmaWVyLCBmb3Jrcykge1xyXG4gICAgICAgIHRoaXMuc291cmNlID0gc291cmNlO1xyXG4gICAgICAgIHRoaXMuaW5wdXQgPSBpbnB1dDtcclxuICAgICAgICB0aGlzLmFyZ3MgPSBhcmdzO1xyXG4gICAgICAgIHRoaXMuY29tbWFuZCA9IGNvbW1hbmQ7XHJcbiAgICAgICAgdGhpcy5yb290Tm9kZSA9IHJvb3ROb2RlO1xyXG4gICAgICAgIHRoaXMubm9kZXMgPSBub2RlcztcclxuICAgICAgICB0aGlzLnJhbmdlID0gcmFuZ2U7XHJcbiAgICAgICAgdGhpcy5jaGlsZCA9IGNoaWxkO1xyXG4gICAgICAgIHRoaXMubW9kaWZpZXIgPSBtb2RpZmllcjtcclxuICAgICAgICB0aGlzLmZvcmtzID0gZm9ya3M7XHJcbiAgICB9XHJcbiAgICBjb3B5Rm9yKHNvdXJjZSkge1xyXG4gICAgICAgIGlmICh0aGlzLnNvdXJjZSA9PT0gc291cmNlKVxyXG4gICAgICAgICAgICByZXR1cm4gdGhpcztcclxuICAgICAgICByZXR1cm4gbmV3IENvbW1hbmRDb250ZXh0KHNvdXJjZSwgdGhpcy5pbnB1dCwgdGhpcy5hcmdzLCB0aGlzLmNvbW1hbmQsIHRoaXMucm9vdE5vZGUsIHRoaXMubm9kZXMsIHRoaXMucmFuZ2UsIHRoaXMuY2hpbGQsIHRoaXMubW9kaWZpZXIsIHRoaXMuZm9ya3MpO1xyXG4gICAgfVxyXG4gICAgZ2V0Q2hpbGQoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuY2hpbGQ7XHJcbiAgICB9XHJcbiAgICBnZXRMYXN0Q2hpbGQoKSB7XHJcbiAgICAgICAgbGV0IHJlc3VsdCA9IHRoaXM7XHJcbiAgICAgICAgd2hpbGUgKCEocmVzdWx0LmdldENoaWxkKCkgPT0gbnVsbCkpIHtcclxuICAgICAgICAgICAgcmVzdWx0ID0gcmVzdWx0LmdldENoaWxkKCk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHJldHVybiByZXN1bHQ7XHJcbiAgICB9XHJcbiAgICBnZXRDb21tYW5kKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmNvbW1hbmQ7XHJcbiAgICB9XHJcbiAgICBnZXRTb3VyY2UoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuc291cmNlO1xyXG4gICAgfVxyXG4gICAgZ2V0QXJndW1lbnQobmFtZSwgY2xhenopIHtcclxuICAgICAgICBjb25zdCBhcmcgPSB0aGlzLmFyZ3MuZ2V0KG5hbWUpO1xyXG4gICAgICAgIGlmIChhcmcgPT0gbnVsbCkge1xyXG4gICAgICAgICAgICB0aHJvdyBuZXcgRXJyb3IoXCJObyBzdWNoIGFyZ3VtZW50ICdcIiArIG5hbWUgKyBcIicgZXhpc3RzIG9uIHRoaXMgY29tbWFuZFwiKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgbGV0IHJlc3VsdCA9IGFyZy5nZXRSZXN1bHQoKTtcclxuICAgICAgICBpZiAoY2xhenogPT0gbnVsbCkge1xyXG4gICAgICAgICAgICByZXR1cm4gcmVzdWx0O1xyXG4gICAgICAgIH1cclxuICAgICAgICBlbHNlIHtcclxuICAgICAgICAgICAgcmV0dXJuIGNsYXp6KHJlc3VsdCk7XHJcbiAgICAgICAgfVxyXG4gICAgfVxyXG4gICAgZXF1YWxzKG8pIHtcclxuICAgICAgICBpZiAodGhpcyA9PT0gbylcclxuICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgaWYgKCEobyBpbnN0YW5jZW9mIENvbW1hbmRDb250ZXh0KSlcclxuICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgIGlmICghaXNFcXVhbF8xLmRlZmF1bHQodGhpcy5hcmdzLCBvLmFyZ3MpKVxyXG4gICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICAgICAgaWYgKCF0aGlzLnJvb3ROb2RlLmVxdWFscyhvLnJvb3ROb2RlKSlcclxuICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgIGlmICh0aGlzLm5vZGVzLmxlbmd0aCAhPSBvLm5vZGVzLmxlbmd0aCB8fCAhaXNFcXVhbF8xLmRlZmF1bHQodGhpcy5ub2Rlcywgby5ub2RlcykpXHJcbiAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICBpZiAoISh0aGlzLmNvbW1hbmQgPT0gbnVsbCkgPyAhaXNFcXVhbF8xLmRlZmF1bHQodGhpcy5jb21tYW5kLCBvLmNvbW1hbmQpIDogby5jb21tYW5kICE9IG51bGwpXHJcbiAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICBpZiAoIWlzRXF1YWxfMS5kZWZhdWx0KHRoaXMuc291cmNlLCBvLnNvdXJjZSkpXHJcbiAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICBpZiAoISh0aGlzLmNoaWxkID09IG51bGwpID8gIXRoaXMuY2hpbGQuZXF1YWxzKG8uY2hpbGQpIDogby5jaGlsZCAhPSBudWxsKVxyXG4gICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICB9XHJcbiAgICBnZXRSZWRpcmVjdE1vZGlmaWVyKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLm1vZGlmaWVyO1xyXG4gICAgfVxyXG4gICAgZ2V0UmFuZ2UoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMucmFuZ2U7XHJcbiAgICB9XHJcbiAgICBnZXRJbnB1dCgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5pbnB1dDtcclxuICAgIH1cclxuICAgIGdldFJvb3ROb2RlKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnJvb3ROb2RlO1xyXG4gICAgfVxyXG4gICAgZ2V0Tm9kZXMoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMubm9kZXM7XHJcbiAgICB9XHJcbiAgICBoYXNOb2RlcygpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5ub2Rlcy5sZW5ndGggPj0gMDtcclxuICAgIH1cclxuICAgIGlzRm9ya2VkKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmZvcmtzO1xyXG4gICAgfVxyXG59XHJcbmV4cG9ydHMuZGVmYXVsdCA9IENvbW1hbmRDb250ZXh0O1xyXG4iLCJcInVzZSBzdHJpY3RcIjtcclxudmFyIF9faW1wb3J0RGVmYXVsdCA9ICh0aGlzICYmIHRoaXMuX19pbXBvcnREZWZhdWx0KSB8fCBmdW5jdGlvbiAobW9kKSB7XHJcbiAgICByZXR1cm4gKG1vZCAmJiBtb2QuX19lc01vZHVsZSkgPyBtb2QgOiB7IFwiZGVmYXVsdFwiOiBtb2QgfTtcclxufTtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5jb25zdCBTdHJpbmdSYW5nZV8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL1N0cmluZ1JhbmdlXCIpKTtcclxuY29uc3QgQ29tbWFuZENvbnRleHRfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9Db21tYW5kQ29udGV4dFwiKSk7XHJcbmNvbnN0IFN1Z2dlc3Rpb25Db250ZXh0XzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vU3VnZ2VzdGlvbkNvbnRleHRcIikpO1xyXG5jb25zdCBQYXJzZWRDb21tYW5kTm9kZV8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL1BhcnNlZENvbW1hbmROb2RlXCIpKTtcclxuY2xhc3MgQ29tbWFuZENvbnRleHRCdWlsZGVyIHtcclxuICAgIGNvbnN0cnVjdG9yKGRpc3BhdGNoZXIsIHNvdXJjZSwgcm9vdE5vZGUsIHN0YXJ0KSB7XHJcbiAgICAgICAgdGhpcy5hcmdzID0gbmV3IE1hcCgpO1xyXG4gICAgICAgIHRoaXMubm9kZXMgPSBbXTtcclxuICAgICAgICB0aGlzLm1vZGlmaWVyID0gbnVsbDtcclxuICAgICAgICB0aGlzLnJvb3ROb2RlID0gcm9vdE5vZGU7XHJcbiAgICAgICAgdGhpcy5kaXNwYXRjaGVyID0gZGlzcGF0Y2hlcjtcclxuICAgICAgICB0aGlzLnNvdXJjZSA9IHNvdXJjZTtcclxuICAgICAgICB0aGlzLnJhbmdlID0gU3RyaW5nUmFuZ2VfMS5kZWZhdWx0LmF0KHN0YXJ0KTtcclxuICAgIH1cclxuICAgIHdpdGhTb3VyY2Uoc291cmNlKSB7XHJcbiAgICAgICAgdGhpcy5zb3VyY2UgPSBzb3VyY2U7XHJcbiAgICAgICAgcmV0dXJuIHRoaXM7XHJcbiAgICB9XHJcbiAgICBnZXRTb3VyY2UoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuc291cmNlO1xyXG4gICAgfVxyXG4gICAgZ2V0Um9vdE5vZGUoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMucm9vdE5vZGU7XHJcbiAgICB9XHJcbiAgICB3aXRoQXJndW1lbnQobmFtZSwgYXJndW1lbnQpIHtcclxuICAgICAgICB0aGlzLmFyZ3Muc2V0KG5hbWUsIGFyZ3VtZW50KTtcclxuICAgICAgICByZXR1cm4gdGhpcztcclxuICAgIH1cclxuICAgIGdldEFyZ3VtZW50cygpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5hcmdzO1xyXG4gICAgfVxyXG4gICAgd2l0aENvbW1hbmQoY29tbWFuZCkge1xyXG4gICAgICAgIHRoaXMuY29tbWFuZCA9IGNvbW1hbmQ7XHJcbiAgICAgICAgcmV0dXJuIHRoaXM7XHJcbiAgICB9XHJcbiAgICB3aXRoTm9kZShub2RlLCByYW5nZSkge1xyXG4gICAgICAgIHRoaXMubm9kZXMucHVzaChuZXcgUGFyc2VkQ29tbWFuZE5vZGVfMS5kZWZhdWx0KG5vZGUsIHJhbmdlKSk7XHJcbiAgICAgICAgdGhpcy5yYW5nZSA9IFN0cmluZ1JhbmdlXzEuZGVmYXVsdC5lbmNvbXBhc3NpbmcodGhpcy5yYW5nZSwgcmFuZ2UpO1xyXG4gICAgICAgIHRoaXMubW9kaWZpZXIgPSBub2RlLmdldFJlZGlyZWN0TW9kaWZpZXIoKTtcclxuICAgICAgICB0aGlzLmZvcmtzID0gbm9kZS5pc0ZvcmsoKTtcclxuICAgICAgICByZXR1cm4gdGhpcztcclxuICAgIH1cclxuICAgIGNvcHkoKSB7XHJcbiAgICAgICAgY29uc3QgY29weSA9IG5ldyBDb21tYW5kQ29udGV4dEJ1aWxkZXIodGhpcy5kaXNwYXRjaGVyLCB0aGlzLnNvdXJjZSwgdGhpcy5yb290Tm9kZSwgdGhpcy5yYW5nZS5nZXRTdGFydCgpKTtcclxuICAgICAgICBjb3B5LmNvbW1hbmQgPSB0aGlzLmNvbW1hbmQ7XHJcbiAgICAgICAgY29weS5hcmdzID0gbmV3IE1hcChbLi4uY29weS5hcmdzLCAuLi50aGlzLmFyZ3NdKTtcclxuICAgICAgICBjb3B5Lm5vZGVzLnB1c2goLi4udGhpcy5ub2Rlcyk7XHJcbiAgICAgICAgY29weS5jaGlsZCA9IHRoaXMuY2hpbGQ7XHJcbiAgICAgICAgY29weS5yYW5nZSA9IHRoaXMucmFuZ2U7XHJcbiAgICAgICAgY29weS5mb3JrcyA9IHRoaXMuZm9ya3M7XHJcbiAgICAgICAgcmV0dXJuIGNvcHk7XHJcbiAgICB9XHJcbiAgICB3aXRoQ2hpbGQoY2hpbGQpIHtcclxuICAgICAgICB0aGlzLmNoaWxkID0gY2hpbGQ7XHJcbiAgICAgICAgcmV0dXJuIHRoaXM7XHJcbiAgICB9XHJcbiAgICBnZXRDaGlsZCgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5jaGlsZDtcclxuICAgIH1cclxuICAgIGdldExhc3RDaGlsZCgpIHtcclxuICAgICAgICBsZXQgcmVzdWx0ID0gdGhpcztcclxuICAgICAgICB3aGlsZSAocmVzdWx0LmdldENoaWxkKCkgIT0gbnVsbCkge1xyXG4gICAgICAgICAgICByZXN1bHQgPSByZXN1bHQuZ2V0Q2hpbGQoKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgcmV0dXJuIHJlc3VsdDtcclxuICAgIH1cclxuICAgIGdldENvbW1hbmQoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuY29tbWFuZDtcclxuICAgIH1cclxuICAgIGdldE5vZGVzKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLm5vZGVzO1xyXG4gICAgfVxyXG4gICAgYnVpbGQoaW5wdXQpIHtcclxuICAgICAgICByZXR1cm4gbmV3IENvbW1hbmRDb250ZXh0XzEuZGVmYXVsdCh0aGlzLnNvdXJjZSwgaW5wdXQsIHRoaXMuYXJncywgdGhpcy5jb21tYW5kLCB0aGlzLnJvb3ROb2RlLCB0aGlzLm5vZGVzLCB0aGlzLnJhbmdlLCB0aGlzLmNoaWxkID09IG51bGwgPyBudWxsIDogdGhpcy5jaGlsZC5idWlsZChpbnB1dCksIHRoaXMubW9kaWZpZXIsIHRoaXMuZm9ya3MpO1xyXG4gICAgfVxyXG4gICAgZ2V0RGlzcGF0Y2hlcigpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5kaXNwYXRjaGVyO1xyXG4gICAgfVxyXG4gICAgZ2V0UmFuZ2UoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMucmFuZ2U7XHJcbiAgICB9XHJcbiAgICBmaW5kU3VnZ2VzdGlvbkNvbnRleHQoY3Vyc29yKSB7XHJcbiAgICAgICAgaWYgKCh0aGlzLnJhbmdlLmdldFN0YXJ0KCkgPD0gY3Vyc29yKSkge1xyXG4gICAgICAgICAgICBpZiAoKHRoaXMucmFuZ2UuZ2V0RW5kKCkgPCBjdXJzb3IpKSB7XHJcbiAgICAgICAgICAgICAgICBpZiAoKHRoaXMuY2hpbGQgIT0gbnVsbCkpIHtcclxuICAgICAgICAgICAgICAgICAgICByZXR1cm4gdGhpcy5jaGlsZC5maW5kU3VnZ2VzdGlvbkNvbnRleHQoY3Vyc29yKTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIGVsc2UgaWYgKHRoaXMubm9kZXMubGVuZ3RoID4gMCkge1xyXG4gICAgICAgICAgICAgICAgICAgIGxldCBsYXN0ID0gdGhpcy5ub2Rlc1t0aGlzLm5vZGVzLmxlbmd0aCAtIDFdO1xyXG4gICAgICAgICAgICAgICAgICAgIHJldHVybiBuZXcgU3VnZ2VzdGlvbkNvbnRleHRfMS5kZWZhdWx0KGxhc3QuZ2V0Tm9kZSgpLCBsYXN0LmdldFJhbmdlKCkuZ2V0RW5kKCkgKyAxKTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIGVsc2Uge1xyXG4gICAgICAgICAgICAgICAgICAgIHJldHVybiBuZXcgU3VnZ2VzdGlvbkNvbnRleHRfMS5kZWZhdWx0KHRoaXMucm9vdE5vZGUsIHRoaXMucmFuZ2UuZ2V0U3RhcnQoKSk7XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgZWxzZSB7XHJcbiAgICAgICAgICAgICAgICBsZXQgcHJldiA9IHRoaXMucm9vdE5vZGU7XHJcbiAgICAgICAgICAgICAgICBmb3IgKGxldCBub2RlIG9mIHRoaXMubm9kZXMpIHtcclxuICAgICAgICAgICAgICAgICAgICBsZXQgbm9kZVJhbmdlID0gbm9kZS5nZXRSYW5nZSgpO1xyXG4gICAgICAgICAgICAgICAgICAgIGlmIChub2RlUmFuZ2UuZ2V0U3RhcnQoKSA8PSBjdXJzb3IgJiYgY3Vyc29yIDw9IG5vZGVSYW5nZS5nZXRFbmQoKSkge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICByZXR1cm4gbmV3IFN1Z2dlc3Rpb25Db250ZXh0XzEuZGVmYXVsdChwcmV2LCBub2RlUmFuZ2UuZ2V0U3RhcnQoKSk7XHJcbiAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgICAgIHByZXYgPSBub2RlLmdldE5vZGUoKTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIGlmICgocHJldiA9PSBudWxsKSkge1xyXG4gICAgICAgICAgICAgICAgICAgIHRocm93IG5ldyBFcnJvcihcIkNhbid0IGZpbmQgbm9kZSBiZWZvcmUgY3Vyc29yXCIpO1xyXG4gICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgcmV0dXJuIG5ldyBTdWdnZXN0aW9uQ29udGV4dF8xLmRlZmF1bHQocHJldiwgdGhpcy5yYW5nZS5nZXRTdGFydCgpKTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgIH1cclxuICAgICAgICB0aHJvdyBuZXcgRXJyb3IoXCJDYW4ndCBmaW5kIG5vZGUgYmVmb3JlIGN1cnNvclwiKTtcclxuICAgIH1cclxufVxyXG5leHBvcnRzLmRlZmF1bHQgPSBDb21tYW5kQ29udGV4dEJ1aWxkZXI7XHJcbiIsIlwidXNlIHN0cmljdFwiO1xyXG52YXIgX19pbXBvcnREZWZhdWx0ID0gKHRoaXMgJiYgdGhpcy5fX2ltcG9ydERlZmF1bHQpIHx8IGZ1bmN0aW9uIChtb2QpIHtcclxuICAgIHJldHVybiAobW9kICYmIG1vZC5fX2VzTW9kdWxlKSA/IG1vZCA6IHsgXCJkZWZhdWx0XCI6IG1vZCB9O1xyXG59O1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNvbnN0IFN0cmluZ1JhbmdlXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vU3RyaW5nUmFuZ2VcIikpO1xyXG5jbGFzcyBQYXJzZWRBcmd1bWVudCB7XHJcbiAgICBjb25zdHJ1Y3RvcihzdGFydCwgZW5kLCByZXN1bHQpIHtcclxuICAgICAgICB0aGlzLnJhbmdlID0gU3RyaW5nUmFuZ2VfMS5kZWZhdWx0LmJldHdlZW4oc3RhcnQsIGVuZCk7XHJcbiAgICAgICAgdGhpcy5yZXN1bHQgPSByZXN1bHQ7XHJcbiAgICB9XHJcbiAgICBnZXRSYW5nZSgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5yYW5nZTtcclxuICAgIH1cclxuICAgIGdldFJlc3VsdCgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5yZXN1bHQ7XHJcbiAgICB9XHJcbiAgICBlcXVhbHMobykge1xyXG4gICAgICAgIGlmICh0aGlzID09PSBvKVxyXG4gICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICBpZiAoIShvIGluc3RhbmNlb2YgUGFyc2VkQXJndW1lbnQpKVxyXG4gICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMucmFuZ2UuZXF1YWxzKG8ucmFuZ2UpICYmIHRoaXMucmVzdWx0ID09PSBvLnJlc3VsdDtcclxuICAgIH1cclxufVxyXG5leHBvcnRzLmRlZmF1bHQgPSBQYXJzZWRBcmd1bWVudDtcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY2xhc3MgUGFyc2VkQ29tbWFuZE5vZGUge1xyXG4gICAgY29uc3RydWN0b3Iobm9kZSwgcmFuZ2UpIHtcclxuICAgICAgICB0aGlzLm5vZGUgPSBub2RlO1xyXG4gICAgICAgIHRoaXMucmFuZ2UgPSByYW5nZTtcclxuICAgIH1cclxuICAgIGdldE5vZGUoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMubm9kZTtcclxuICAgIH1cclxuICAgIGdldFJhbmdlKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnJhbmdlO1xyXG4gICAgfVxyXG4gICAgdG9TdHJpbmcoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMubm9kZSArIFwiQFwiICsgdGhpcy5yYW5nZTtcclxuICAgIH1cclxuICAgIGVxdWFscyhvKSB7XHJcbiAgICAgICAgaWYgKHRoaXMgPT09IG8pXHJcbiAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgIGlmIChvID09IG51bGwgfHwgIShvIGluc3RhbmNlb2YgUGFyc2VkQ29tbWFuZE5vZGUpKSB7XHJcbiAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICB9XHJcbiAgICAgICAgcmV0dXJuIHRoaXMubm9kZS5lcXVhbHMoby5ub2RlKSAmJiB0aGlzLnJhbmdlLmVxdWFscyhvLnJhbmdlKTtcclxuICAgIH1cclxufVxyXG5leHBvcnRzLmRlZmF1bHQgPSBQYXJzZWRDb21tYW5kTm9kZTtcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY2xhc3MgU3RyaW5nUmFuZ2Uge1xyXG4gICAgY29uc3RydWN0b3Ioc3RhcnQsIGVuZCkge1xyXG4gICAgICAgIHRoaXMuc3RhcnQgPSBzdGFydDtcclxuICAgICAgICB0aGlzLmVuZCA9IGVuZDtcclxuICAgIH1cclxuICAgIHN0YXRpYyBhdChwb3MpIHtcclxuICAgICAgICByZXR1cm4gbmV3IFN0cmluZ1JhbmdlKHBvcywgcG9zKTtcclxuICAgIH1cclxuICAgIHN0YXRpYyBiZXR3ZWVuKHN0YXJ0LCBlbmQpIHtcclxuICAgICAgICByZXR1cm4gbmV3IFN0cmluZ1JhbmdlKHN0YXJ0LCBlbmQpO1xyXG4gICAgfVxyXG4gICAgc3RhdGljIGVuY29tcGFzc2luZyhhLCBiKSB7XHJcbiAgICAgICAgcmV0dXJuIG5ldyBTdHJpbmdSYW5nZShNYXRoLm1pbihhLmdldFN0YXJ0KCksIGIuZ2V0U3RhcnQoKSksIE1hdGgubWF4KGEuZ2V0RW5kKCksIGIuZ2V0RW5kKCkpKTtcclxuICAgIH1cclxuICAgIGdldFN0YXJ0KCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnN0YXJ0O1xyXG4gICAgfVxyXG4gICAgZ2V0RW5kKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmVuZDtcclxuICAgIH1cclxuICAgIGdldChzdHIpIHtcclxuICAgICAgICBpZiAodHlwZW9mIHN0ciA9PT0gXCJzdHJpbmdcIilcclxuICAgICAgICAgICAgcmV0dXJuIHN0ci5zdWJzdHJpbmcodGhpcy5zdGFydCwgdGhpcy5lbmQpO1xyXG4gICAgICAgIGVsc2VcclxuICAgICAgICAgICAgcmV0dXJuIHN0ci5nZXRTdHJpbmcoKS5zdWJzdHJpbmcodGhpcy5zdGFydCwgdGhpcy5lbmQpO1xyXG4gICAgfVxyXG4gICAgaXNFbXB0eSgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5zdGFydCA9PT0gdGhpcy5lbmQ7XHJcbiAgICB9XHJcbiAgICBnZXRMZW5ndGgoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuZW5kIC0gdGhpcy5zdGFydDtcclxuICAgIH1cclxuICAgIGVxdWFscyhvKSB7XHJcbiAgICAgICAgaWYgKHRoaXMgPT09IG8pXHJcbiAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgIGlmICghKG8gaW5zdGFuY2VvZiBTdHJpbmdSYW5nZSkpXHJcbiAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICByZXR1cm4gdGhpcy5zdGFydCA9PT0gby5zdGFydCAmJiB0aGlzLmVuZCA9PSBvLmVuZDtcclxuICAgIH1cclxuICAgIHRvU3RyaW5nKCkge1xyXG4gICAgICAgIHJldHVybiBcIlN0cmluZ1Jhbmdle1wiICsgXCJzdGFydD1cIiArIHRoaXMuc3RhcnQgKyBcIiwgZW5kPVwiICsgdGhpcy5lbmQgKyAnfSc7XHJcbiAgICB9XHJcbn1cclxuZXhwb3J0cy5kZWZhdWx0ID0gU3RyaW5nUmFuZ2U7XHJcbiIsIlwidXNlIHN0cmljdFwiO1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNsYXNzIFN1Z2dlc3Rpb25Db250ZXh0IHtcclxuICAgIGNvbnN0cnVjdG9yKHBhcmVudCwgc3RhcnRQb3MpIHtcclxuICAgICAgICB0aGlzLnBhcmVudCA9IHBhcmVudDtcclxuICAgICAgICB0aGlzLnN0YXJ0UG9zID0gc3RhcnRQb3M7XHJcbiAgICB9XHJcbn1cclxuZXhwb3J0cy5kZWZhdWx0ID0gU3VnZ2VzdGlvbkNvbnRleHQ7XHJcbiIsIlwidXNlIHN0cmljdFwiO1xyXG52YXIgX19pbXBvcnREZWZhdWx0ID0gKHRoaXMgJiYgdGhpcy5fX2ltcG9ydERlZmF1bHQpIHx8IGZ1bmN0aW9uIChtb2QpIHtcclxuICAgIHJldHVybiAobW9kICYmIG1vZC5fX2VzTW9kdWxlKSA/IG1vZCA6IHsgXCJkZWZhdWx0XCI6IG1vZCB9O1xyXG59O1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNvbnN0IExpdGVyYWxNZXNzYWdlXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4uL0xpdGVyYWxNZXNzYWdlXCIpKTtcclxuY29uc3QgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGVfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9TaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZVwiKSk7XHJcbmNvbnN0IER5bmFtaWNDb21tYW5kRXhjZXB0aW9uVHlwZV8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL0R5bmFtaWNDb21tYW5kRXhjZXB0aW9uVHlwZVwiKSk7XHJcbmNsYXNzIEJ1aWx0SW5FeGNlcHRpb25zIHtcclxuICAgIGZsb2F0VG9vTG93KCkge1xyXG4gICAgICAgIHJldHVybiBCdWlsdEluRXhjZXB0aW9ucy5GTE9BVF9UT09fU01BTEw7XHJcbiAgICB9XHJcbiAgICBmbG9hdFRvb0hpZ2goKSB7XHJcbiAgICAgICAgcmV0dXJuIEJ1aWx0SW5FeGNlcHRpb25zLkZMT0FUX1RPT19CSUc7XHJcbiAgICB9XHJcbiAgICBpbnRlZ2VyVG9vTG93KCkge1xyXG4gICAgICAgIHJldHVybiBCdWlsdEluRXhjZXB0aW9ucy5JTlRFR0VSX1RPT19TTUFMTDtcclxuICAgIH1cclxuICAgIGludGVnZXJUb29IaWdoKCkge1xyXG4gICAgICAgIHJldHVybiBCdWlsdEluRXhjZXB0aW9ucy5JTlRFR0VSX1RPT19CSUc7XHJcbiAgICB9XHJcbiAgICBsaXRlcmFsSW5jb3JyZWN0KCkge1xyXG4gICAgICAgIHJldHVybiBCdWlsdEluRXhjZXB0aW9ucy5MSVRFUkFMX0lOQ09SUkVDVDtcclxuICAgIH1cclxuICAgIHJlYWRlckV4cGVjdGVkU3RhcnRPZlF1b3RlKCkge1xyXG4gICAgICAgIHJldHVybiBCdWlsdEluRXhjZXB0aW9ucy5SRUFERVJfRVhQRUNURURfU1RBUlRfT0ZfUVVPVEU7XHJcbiAgICB9XHJcbiAgICByZWFkZXJFeHBlY3RlZEVuZE9mUXVvdGUoKSB7XHJcbiAgICAgICAgcmV0dXJuIEJ1aWx0SW5FeGNlcHRpb25zLlJFQURFUl9FWFBFQ1RFRF9FTkRfT0ZfUVVPVEU7XHJcbiAgICB9XHJcbiAgICByZWFkZXJJbnZhbGlkRXNjYXBlKCkge1xyXG4gICAgICAgIHJldHVybiBCdWlsdEluRXhjZXB0aW9ucy5SRUFERVJfSU5WQUxJRF9FU0NBUEU7XHJcbiAgICB9XHJcbiAgICByZWFkZXJJbnZhbGlkQm9vbCgpIHtcclxuICAgICAgICByZXR1cm4gQnVpbHRJbkV4Y2VwdGlvbnMuUkVBREVSX0lOVkFMSURfQk9PTDtcclxuICAgIH1cclxuICAgIHJlYWRlckludmFsaWRJbnQoKSB7XHJcbiAgICAgICAgcmV0dXJuIEJ1aWx0SW5FeGNlcHRpb25zLlJFQURFUl9JTlZBTElEX0lOVDtcclxuICAgIH1cclxuICAgIHJlYWRlckV4cGVjdGVkSW50KCkge1xyXG4gICAgICAgIHJldHVybiBCdWlsdEluRXhjZXB0aW9ucy5SRUFERVJfRVhQRUNURURfSU5UO1xyXG4gICAgfVxyXG4gICAgcmVhZGVySW52YWxpZEZsb2F0KCkge1xyXG4gICAgICAgIHJldHVybiBCdWlsdEluRXhjZXB0aW9ucy5SRUFERVJfSU5WQUxJRF9GTE9BVDtcclxuICAgIH1cclxuICAgIHJlYWRlckV4cGVjdGVkRmxvYXQoKSB7XHJcbiAgICAgICAgcmV0dXJuIEJ1aWx0SW5FeGNlcHRpb25zLlJFQURFUl9FWFBFQ1RFRF9GTE9BVDtcclxuICAgIH1cclxuICAgIHJlYWRlckV4cGVjdGVkQm9vbCgpIHtcclxuICAgICAgICByZXR1cm4gQnVpbHRJbkV4Y2VwdGlvbnMuUkVBREVSX0VYUEVDVEVEX0JPT0w7XHJcbiAgICB9XHJcbiAgICByZWFkZXJFeHBlY3RlZFN5bWJvbCgpIHtcclxuICAgICAgICByZXR1cm4gQnVpbHRJbkV4Y2VwdGlvbnMuUkVBREVSX0VYUEVDVEVEX1NZTUJPTDtcclxuICAgIH1cclxuICAgIGRpc3BhdGNoZXJVbmtub3duQ29tbWFuZCgpIHtcclxuICAgICAgICByZXR1cm4gQnVpbHRJbkV4Y2VwdGlvbnMuRElTUEFUQ0hFUl9VTktOT1dOX0NPTU1BTkQ7XHJcbiAgICB9XHJcbiAgICBkaXNwYXRjaGVyVW5rbm93bkFyZ3VtZW50KCkge1xyXG4gICAgICAgIHJldHVybiBCdWlsdEluRXhjZXB0aW9ucy5ESVNQQVRDSEVSX1VOS05PV05fQVJHVU1FTlQ7XHJcbiAgICB9XHJcbiAgICBkaXNwYXRjaGVyRXhwZWN0ZWRBcmd1bWVudFNlcGFyYXRvcigpIHtcclxuICAgICAgICByZXR1cm4gQnVpbHRJbkV4Y2VwdGlvbnMuRElTUEFUQ0hFUl9FWFBFQ1RFRF9BUkdVTUVOVF9TRVBBUkFUT1I7XHJcbiAgICB9XHJcbiAgICBkaXNwYXRjaGVyUGFyc2VFeGNlcHRpb24oKSB7XHJcbiAgICAgICAgcmV0dXJuIEJ1aWx0SW5FeGNlcHRpb25zLkRJU1BBVENIRVJfUEFSU0VfRVhDRVBUSU9OO1xyXG4gICAgfVxyXG59XHJcbkJ1aWx0SW5FeGNlcHRpb25zLkZMT0FUX1RPT19TTUFMTCA9IG5ldyBEeW5hbWljQ29tbWFuZEV4Y2VwdGlvblR5cGVfMS5kZWZhdWx0KChmb3VuZCwgbWluKSA9PiBuZXcgTGl0ZXJhbE1lc3NhZ2VfMS5kZWZhdWx0KFwiRmxvYXQgbXVzdCBub3QgYmUgbGVzcyB0aGFuIFwiICsgbWluICsgXCIsIGZvdW5kIFwiICsgZm91bmQpKTtcclxuQnVpbHRJbkV4Y2VwdGlvbnMuRkxPQVRfVE9PX0JJRyA9IG5ldyBEeW5hbWljQ29tbWFuZEV4Y2VwdGlvblR5cGVfMS5kZWZhdWx0KChmb3VuZCwgbWF4KSA9PiBuZXcgTGl0ZXJhbE1lc3NhZ2VfMS5kZWZhdWx0KFwiRmxvYXQgbXVzdCBub3QgYmUgbW9yZSB0aGFuIFwiICsgbWF4ICsgXCIsIGZvdW5kIFwiICsgZm91bmQpKTtcclxuQnVpbHRJbkV4Y2VwdGlvbnMuSU5URUdFUl9UT09fU01BTEwgPSBuZXcgRHluYW1pY0NvbW1hbmRFeGNlcHRpb25UeXBlXzEuZGVmYXVsdCgoZm91bmQsIG1pbikgPT4gbmV3IExpdGVyYWxNZXNzYWdlXzEuZGVmYXVsdChcIkludGVnZXIgbXVzdCBub3QgYmUgbGVzcyB0aGFuIFwiICsgbWluICsgXCIsIGZvdW5kIFwiICsgZm91bmQpKTtcclxuQnVpbHRJbkV4Y2VwdGlvbnMuSU5URUdFUl9UT09fQklHID0gbmV3IER5bmFtaWNDb21tYW5kRXhjZXB0aW9uVHlwZV8xLmRlZmF1bHQoKGZvdW5kLCBtYXgpID0+IG5ldyBMaXRlcmFsTWVzc2FnZV8xLmRlZmF1bHQoXCJJbnRlZ2VyIG11c3Qgbm90IGJlIG1vcmUgdGhhbiBcIiArIG1heCArIFwiLCBmb3VuZCBcIiArIGZvdW5kKSk7XHJcbkJ1aWx0SW5FeGNlcHRpb25zLkxJVEVSQUxfSU5DT1JSRUNUID0gbmV3IER5bmFtaWNDb21tYW5kRXhjZXB0aW9uVHlwZV8xLmRlZmF1bHQoZXhwZWN0ZWQgPT4gbmV3IExpdGVyYWxNZXNzYWdlXzEuZGVmYXVsdChcIkV4cGVjdGVkIGxpdGVyYWwgXCIgKyBleHBlY3RlZCkpO1xyXG5CdWlsdEluRXhjZXB0aW9ucy5SRUFERVJfRVhQRUNURURfU1RBUlRfT0ZfUVVPVEUgPSBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGVfMS5kZWZhdWx0KG5ldyBMaXRlcmFsTWVzc2FnZV8xLmRlZmF1bHQoXCJFeHBlY3RlZCBxdW90ZSB0byBzdGFydCBhIHN0cmluZ1wiKSk7XHJcbkJ1aWx0SW5FeGNlcHRpb25zLlJFQURFUl9FWFBFQ1RFRF9FTkRfT0ZfUVVPVEUgPSBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGVfMS5kZWZhdWx0KG5ldyBMaXRlcmFsTWVzc2FnZV8xLmRlZmF1bHQoXCJVbmNsb3NlZCBxdW90ZWQgc3RyaW5nXCIpKTtcclxuQnVpbHRJbkV4Y2VwdGlvbnMuUkVBREVSX0lOVkFMSURfRVNDQVBFID0gbmV3IER5bmFtaWNDb21tYW5kRXhjZXB0aW9uVHlwZV8xLmRlZmF1bHQoY2hhcmFjdGVyID0+IG5ldyBMaXRlcmFsTWVzc2FnZV8xLmRlZmF1bHQoXCJJbnZhbGlkIGVzY2FwZSBzZXF1ZW5jZSAnXCIgKyBjaGFyYWN0ZXIgKyBcIicgaW4gcXVvdGVkIHN0cmluZ1wiKSk7XHJcbkJ1aWx0SW5FeGNlcHRpb25zLlJFQURFUl9JTlZBTElEX0JPT0wgPSBuZXcgRHluYW1pY0NvbW1hbmRFeGNlcHRpb25UeXBlXzEuZGVmYXVsdCh2YWx1ZSA9PiBuZXcgTGl0ZXJhbE1lc3NhZ2VfMS5kZWZhdWx0KFwiSW52YWxpZCBib29sLCBleHBlY3RlZCB0cnVlIG9yIGZhbHNlIGJ1dCBmb3VuZCAnXCIgKyB2YWx1ZSArIFwiJ1wiKSk7XHJcbkJ1aWx0SW5FeGNlcHRpb25zLlJFQURFUl9JTlZBTElEX0lOVCA9IG5ldyBEeW5hbWljQ29tbWFuZEV4Y2VwdGlvblR5cGVfMS5kZWZhdWx0KHZhbHVlID0+IG5ldyBMaXRlcmFsTWVzc2FnZV8xLmRlZmF1bHQoXCJJbnZhbGlkIGludGVnZXIgJ1wiICsgdmFsdWUgKyBcIidcIikpO1xyXG5CdWlsdEluRXhjZXB0aW9ucy5SRUFERVJfRVhQRUNURURfSU5UID0gbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlXzEuZGVmYXVsdChuZXcgTGl0ZXJhbE1lc3NhZ2VfMS5kZWZhdWx0KFwiRXhwZWN0ZWQgaW50ZWdlclwiKSk7XHJcbkJ1aWx0SW5FeGNlcHRpb25zLlJFQURFUl9JTlZBTElEX0ZMT0FUID0gbmV3IER5bmFtaWNDb21tYW5kRXhjZXB0aW9uVHlwZV8xLmRlZmF1bHQodmFsdWUgPT4gbmV3IExpdGVyYWxNZXNzYWdlXzEuZGVmYXVsdChcIkludmFsaWQgZmxvYXQgJ1wiICsgdmFsdWUgKyBcIidcIikpO1xyXG5CdWlsdEluRXhjZXB0aW9ucy5SRUFERVJfRVhQRUNURURfRkxPQVQgPSBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGVfMS5kZWZhdWx0KG5ldyBMaXRlcmFsTWVzc2FnZV8xLmRlZmF1bHQoXCJFeHBlY3RlZCBmbG9hdFwiKSk7XHJcbkJ1aWx0SW5FeGNlcHRpb25zLlJFQURFUl9FWFBFQ1RFRF9CT09MID0gbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlXzEuZGVmYXVsdChuZXcgTGl0ZXJhbE1lc3NhZ2VfMS5kZWZhdWx0KFwiRXhwZWN0ZWQgYm9vbFwiKSk7XHJcbkJ1aWx0SW5FeGNlcHRpb25zLlJFQURFUl9FWFBFQ1RFRF9TWU1CT0wgPSBuZXcgRHluYW1pY0NvbW1hbmRFeGNlcHRpb25UeXBlXzEuZGVmYXVsdChzeW1ib2wgPT4gbmV3IExpdGVyYWxNZXNzYWdlXzEuZGVmYXVsdChcIkV4cGVjdGVkICdcIiArIHN5bWJvbCArIFwiJ1wiKSk7XHJcbkJ1aWx0SW5FeGNlcHRpb25zLkRJU1BBVENIRVJfVU5LTk9XTl9DT01NQU5EID0gbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlXzEuZGVmYXVsdChuZXcgTGl0ZXJhbE1lc3NhZ2VfMS5kZWZhdWx0KFwiVW5rbm93biBjb21tYW5kXCIpKTtcclxuQnVpbHRJbkV4Y2VwdGlvbnMuRElTUEFUQ0hFUl9VTktOT1dOX0FSR1VNRU5UID0gbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlXzEuZGVmYXVsdChuZXcgTGl0ZXJhbE1lc3NhZ2VfMS5kZWZhdWx0KFwiSW5jb3JyZWN0IGFyZ3VtZW50IGZvciBjb21tYW5kXCIpKTtcclxuQnVpbHRJbkV4Y2VwdGlvbnMuRElTUEFUQ0hFUl9FWFBFQ1RFRF9BUkdVTUVOVF9TRVBBUkFUT1IgPSBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGVfMS5kZWZhdWx0KG5ldyBMaXRlcmFsTWVzc2FnZV8xLmRlZmF1bHQoXCJFeHBlY3RlZCB3aGl0ZXNwYWNlIHRvIGVuZCBvbmUgYXJndW1lbnQsIGJ1dCBmb3VuZCB0cmFpbGluZyBkYXRhXCIpKTtcclxuQnVpbHRJbkV4Y2VwdGlvbnMuRElTUEFUQ0hFUl9QQVJTRV9FWENFUFRJT04gPSBuZXcgRHluYW1pY0NvbW1hbmRFeGNlcHRpb25UeXBlXzEuZGVmYXVsdChtZXNzYWdlID0+IG5ldyBMaXRlcmFsTWVzc2FnZV8xLmRlZmF1bHQoKFwiQ291bGQgbm90IHBhcnNlIGNvbW1hbmQ6IFwiICsgbWVzc2FnZSkpKTtcclxuZXhwb3J0cy5kZWZhdWx0ID0gQnVpbHRJbkV4Y2VwdGlvbnM7XHJcbiIsIlwidXNlIHN0cmljdFwiO1xyXG52YXIgX19pbXBvcnREZWZhdWx0ID0gKHRoaXMgJiYgdGhpcy5fX2ltcG9ydERlZmF1bHQpIHx8IGZ1bmN0aW9uIChtb2QpIHtcclxuICAgIHJldHVybiAobW9kICYmIG1vZC5fX2VzTW9kdWxlKSA/IG1vZCA6IHsgXCJkZWZhdWx0XCI6IG1vZCB9O1xyXG59O1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNvbnN0IEJ1aWx0SW5FeGNlcHRpb25zXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vQnVpbHRJbkV4Y2VwdGlvbnNcIikpO1xyXG5jbGFzcyBDb21tYW5kU3ludGF4RXhjZXB0aW9uIGV4dGVuZHMgRXJyb3Ige1xyXG4gICAgY29uc3RydWN0b3IodHlwZSwgbWVzc2FnZSwgaW5wdXQgPSBudWxsLCBjdXJzb3IgPSAtMSkge1xyXG4gICAgICAgIHN1cGVyKG1lc3NhZ2UuZ2V0U3RyaW5nKCkpO1xyXG4gICAgICAgIEVycm9yLmNhcHR1cmVTdGFja1RyYWNlKHRoaXMsIENvbW1hbmRTeW50YXhFeGNlcHRpb24pO1xyXG4gICAgICAgIHRoaXMudHlwZSA9IHR5cGU7XHJcbiAgICAgICAgdGhpcy5fX21lc3NhZ2UgPSBtZXNzYWdlO1xyXG4gICAgICAgIHRoaXMuaW5wdXQgPSBpbnB1dDtcclxuICAgICAgICB0aGlzLmN1cnNvciA9IGN1cnNvcjtcclxuICAgICAgICB0aGlzLm1lc3NhZ2UgPSB0aGlzLmdldE1lc3NhZ2UoKTtcclxuICAgIH1cclxuICAgIGdldE1lc3NhZ2UoKSB7XHJcbiAgICAgICAgbGV0IG1lc3NhZ2UgPSB0aGlzLl9fbWVzc2FnZS5nZXRTdHJpbmcoKTtcclxuICAgICAgICBsZXQgY29udGV4dCA9IHRoaXMuZ2V0Q29udGV4dCgpO1xyXG4gICAgICAgIGlmIChjb250ZXh0ICE9IG51bGwpIHtcclxuICAgICAgICAgICAgbWVzc2FnZSArPSBcIiBhdCBwb3NpdGlvbiBcIiArIHRoaXMuY3Vyc29yICsgXCI6IFwiICsgY29udGV4dDtcclxuICAgICAgICB9XHJcbiAgICAgICAgcmV0dXJuIG1lc3NhZ2U7XHJcbiAgICB9XHJcbiAgICBnZXRSYXdNZXNzYWdlKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLl9fbWVzc2FnZTtcclxuICAgIH1cclxuICAgIGdldENvbnRleHQoKSB7XHJcbiAgICAgICAgaWYgKHRoaXMuaW5wdXQgPT0gbnVsbCB8fCB0aGlzLmN1cnNvciA8IDApIHtcclxuICAgICAgICAgICAgcmV0dXJuIG51bGw7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGxldCBidWlsZGVyID0gXCJcIjtcclxuICAgICAgICBsZXQgY3Vyc29yID0gTWF0aC5taW4odGhpcy5pbnB1dC5sZW5ndGgsIHRoaXMuY3Vyc29yKTtcclxuICAgICAgICBpZiAoY3Vyc29yID4gQ29tbWFuZFN5bnRheEV4Y2VwdGlvbi5DT05URVhUX0FNT1VOVCkge1xyXG4gICAgICAgICAgICBidWlsZGVyICs9IFwiLi4uXCI7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGJ1aWxkZXIgKz0gdGhpcy5pbnB1dC5zdWJzdHJpbmcoTWF0aC5tYXgoMCwgY3Vyc29yIC0gQ29tbWFuZFN5bnRheEV4Y2VwdGlvbi5DT05URVhUX0FNT1VOVCksIGN1cnNvcik7XHJcbiAgICAgICAgYnVpbGRlciArPSBcIjwtLVtIRVJFXVwiO1xyXG4gICAgICAgIHJldHVybiBidWlsZGVyO1xyXG4gICAgfVxyXG4gICAgZ2V0VHlwZSgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy50eXBlO1xyXG4gICAgfVxyXG4gICAgZ2V0SW5wdXQoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuaW5wdXQ7XHJcbiAgICB9XHJcbiAgICBnZXRDdXJzb3IoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuY3Vyc29yO1xyXG4gICAgfVxyXG4gICAgdG9TdHJpbmcoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMubWVzc2FnZTtcclxuICAgIH1cclxufVxyXG5Db21tYW5kU3ludGF4RXhjZXB0aW9uLkNPTlRFWFRfQU1PVU5UID0gMTA7XHJcbkNvbW1hbmRTeW50YXhFeGNlcHRpb24uQlVJTFRfSU5fRVhDRVBUSU9OUyA9IG5ldyBCdWlsdEluRXhjZXB0aW9uc18xLmRlZmF1bHQoKTtcclxuZXhwb3J0cy5kZWZhdWx0ID0gQ29tbWFuZFN5bnRheEV4Y2VwdGlvbjtcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbnZhciBfX2ltcG9ydERlZmF1bHQgPSAodGhpcyAmJiB0aGlzLl9faW1wb3J0RGVmYXVsdCkgfHwgZnVuY3Rpb24gKG1vZCkge1xyXG4gICAgcmV0dXJuIChtb2QgJiYgbW9kLl9fZXNNb2R1bGUpID8gbW9kIDogeyBcImRlZmF1bHRcIjogbW9kIH07XHJcbn07XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY29uc3QgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbl8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL0NvbW1hbmRTeW50YXhFeGNlcHRpb25cIikpO1xyXG5jbGFzcyBEeW5hbWljQ29tbWFuZEV4Y2VwdGlvblR5cGUge1xyXG4gICAgY29uc3RydWN0b3IoZm4pIHtcclxuICAgICAgICB0aGlzLmZuID0gZm47XHJcbiAgICAgICAgRXJyb3IuY2FwdHVyZVN0YWNrVHJhY2UodGhpcywgRHluYW1pY0NvbW1hbmRFeGNlcHRpb25UeXBlKTtcclxuICAgIH1cclxuICAgIGNyZWF0ZSguLi5hcmdzKSB7XHJcbiAgICAgICAgcmV0dXJuIG5ldyBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEuZGVmYXVsdCh0aGlzLCB0aGlzLmZuKC4uLmFyZ3MpKTtcclxuICAgIH1cclxuICAgIGNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlciwgLi4uYXJncykge1xyXG4gICAgICAgIHJldHVybiBuZXcgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbl8xLmRlZmF1bHQodGhpcywgdGhpcy5mbiguLi5hcmdzKSwgcmVhZGVyLmdldFN0cmluZygpLCByZWFkZXIuZ2V0Q3Vyc29yKCkpO1xyXG4gICAgfVxyXG59XHJcbmV4cG9ydHMuZGVmYXVsdCA9IER5bmFtaWNDb21tYW5kRXhjZXB0aW9uVHlwZTtcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbnZhciBfX2ltcG9ydERlZmF1bHQgPSAodGhpcyAmJiB0aGlzLl9faW1wb3J0RGVmYXVsdCkgfHwgZnVuY3Rpb24gKG1vZCkge1xyXG4gICAgcmV0dXJuIChtb2QgJiYgbW9kLl9fZXNNb2R1bGUpID8gbW9kIDogeyBcImRlZmF1bHRcIjogbW9kIH07XHJcbn07XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY29uc3QgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbl8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL0NvbW1hbmRTeW50YXhFeGNlcHRpb25cIikpO1xyXG5jbGFzcyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZSB7XHJcbiAgICBjb25zdHJ1Y3RvcihtZXNzYWdlKSB7XHJcbiAgICAgICAgdGhpcy5tZXNzYWdlID0gbWVzc2FnZTtcclxuICAgICAgICBFcnJvci5jYXB0dXJlU3RhY2tUcmFjZSh0aGlzLCBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZSk7XHJcbiAgICB9XHJcbiAgICBjcmVhdGUoKSB7XHJcbiAgICAgICAgcmV0dXJuIG5ldyBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEuZGVmYXVsdCh0aGlzLCB0aGlzLm1lc3NhZ2UpO1xyXG4gICAgfVxyXG4gICAgY3JlYXRlV2l0aENvbnRleHQocmVhZGVyKSB7XHJcbiAgICAgICAgcmV0dXJuIG5ldyBDb21tYW5kU3ludGF4RXhjZXB0aW9uXzEuZGVmYXVsdCh0aGlzLCB0aGlzLm1lc3NhZ2UsIHJlYWRlci5nZXRTdHJpbmcoKSwgcmVhZGVyLmdldEN1cnNvcigpKTtcclxuICAgIH1cclxuICAgIHRvU3RyaW5nKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLm1lc3NhZ2UuZ2V0U3RyaW5nKCk7XHJcbiAgICB9XHJcbn1cclxuZXhwb3J0cy5kZWZhdWx0ID0gU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGU7XHJcbiIsIlwidXNlIHN0cmljdFwiO1xyXG52YXIgX19pbXBvcnREZWZhdWx0ID0gKHRoaXMgJiYgdGhpcy5fX2ltcG9ydERlZmF1bHQpIHx8IGZ1bmN0aW9uIChtb2QpIHtcclxuICAgIHJldHVybiAobW9kICYmIG1vZC5fX2VzTW9kdWxlKSA/IG1vZCA6IHsgXCJkZWZhdWx0XCI6IG1vZCB9O1xyXG59O1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNvbnN0IFN1Z2dlc3Rpb25fMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi9TdWdnZXN0aW9uXCIpKTtcclxuY2xhc3MgSW50ZWdlclN1Z2dlc3Rpb24gZXh0ZW5kcyBTdWdnZXN0aW9uXzEuZGVmYXVsdCB7XHJcbiAgICBjb25zdHJ1Y3RvcihyYW5nZSwgdmFsdWUsIHRvb2x0aXAgPSBudWxsKSB7XHJcbiAgICAgICAgc3VwZXIocmFuZ2UsIHZhbHVlLnRvU3RyaW5nKCksIHRvb2x0aXApO1xyXG4gICAgICAgIHRoaXMudmFsdWUgPSB2YWx1ZTtcclxuICAgIH1cclxuICAgIGdldFZhbHVlKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnZhbHVlO1xyXG4gICAgfVxyXG4gICAgZXF1YWxzKG8pIHtcclxuICAgICAgICBpZiAodGhpcyA9PT0gbylcclxuICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgaWYgKCEobyBpbnN0YW5jZW9mIEludGVnZXJTdWdnZXN0aW9uKSlcclxuICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgIHJldHVybiB0aGlzLnZhbHVlID09IG8udmFsdWUgJiYgc3VwZXIuZXF1YWxzKG8pO1xyXG4gICAgfVxyXG4gICAgdG9TdHJpbmcoKSB7XHJcbiAgICAgICAgcmV0dXJuIFwiSW50ZWdlclN1Z2dlc3Rpb257XCIgK1xyXG4gICAgICAgICAgICBcInZhbHVlPVwiICsgdGhpcy52YWx1ZSArXHJcbiAgICAgICAgICAgIFwiLCByYW5nZT1cIiArIHRoaXMuZ2V0UmFuZ2UoKSArXHJcbiAgICAgICAgICAgIFwiLCB0ZXh0PSdcIiArIHRoaXMuZ2V0VGV4dCgpICsgJ1xcJycgK1xyXG4gICAgICAgICAgICBcIiwgdG9vbHRpcD0nXCIgKyB0aGlzLmdldFRvb2x0aXAoKSArICdcXCcnICtcclxuICAgICAgICAgICAgJ30nO1xyXG4gICAgfVxyXG4gICAgY29tcGFyZVRvKG8pIHtcclxuICAgICAgICBpZiAobyBpbnN0YW5jZW9mIEludGVnZXJTdWdnZXN0aW9uKSB7XHJcbiAgICAgICAgICAgIHJldHVybiB0aGlzLnZhbHVlIDwgby52YWx1ZSA/IDEgOiAtMTtcclxuICAgICAgICB9XHJcbiAgICAgICAgcmV0dXJuIHN1cGVyLmNvbXBhcmVUbyhvKTtcclxuICAgIH1cclxuICAgIGNvbXBhcmVUb0lnbm9yZUNhc2UoYikge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmNvbXBhcmVUbyhiKTtcclxuICAgIH1cclxufVxyXG5leHBvcnRzLmRlZmF1bHQgPSBJbnRlZ2VyU3VnZ2VzdGlvbjtcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbnZhciBfX2ltcG9ydERlZmF1bHQgPSAodGhpcyAmJiB0aGlzLl9faW1wb3J0RGVmYXVsdCkgfHwgZnVuY3Rpb24gKG1vZCkge1xyXG4gICAgcmV0dXJuIChtb2QgJiYgbW9kLl9fZXNNb2R1bGUpID8gbW9kIDogeyBcImRlZmF1bHRcIjogbW9kIH07XHJcbn07XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY29uc3QgaXNFcXVhbF8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuLi91dGlsL2lzRXF1YWxcIikpO1xyXG5jbGFzcyBTdWdnZXN0aW9uIHtcclxuICAgIGNvbnN0cnVjdG9yKHJhbmdlLCB0ZXh0LCB0b29sdGlwID0gbnVsbCkge1xyXG4gICAgICAgIHRoaXMucmFuZ2UgPSByYW5nZTtcclxuICAgICAgICB0aGlzLnRleHQgPSB0ZXh0O1xyXG4gICAgICAgIHRoaXMudG9vbHRpcCA9IHRvb2x0aXA7XHJcbiAgICB9XHJcbiAgICBnZXRSYW5nZSgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5yYW5nZTtcclxuICAgIH1cclxuICAgIGdldFRleHQoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMudGV4dDtcclxuICAgIH1cclxuICAgIGdldFRvb2x0aXAoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMudG9vbHRpcDtcclxuICAgIH1cclxuICAgIGFwcGx5KGlucHV0KSB7XHJcbiAgICAgICAgaWYgKHRoaXMucmFuZ2UuZ2V0U3RhcnQoKSA9PT0gMCAmJiB0aGlzLnJhbmdlLmdldEVuZCgpID09IGlucHV0Lmxlbmd0aCkge1xyXG4gICAgICAgICAgICByZXR1cm4gdGhpcy50ZXh0O1xyXG4gICAgICAgIH1cclxuICAgICAgICBsZXQgcmVzdWx0ID0gXCJcIjtcclxuICAgICAgICBpZiAodGhpcy5yYW5nZS5nZXRTdGFydCgpID4gMCkge1xyXG4gICAgICAgICAgICByZXN1bHQgKz0gaW5wdXQuc3Vic3RyaW5nKDAsIHRoaXMucmFuZ2UuZ2V0U3RhcnQoKSk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHJlc3VsdCArPSB0aGlzLnRleHQ7XHJcbiAgICAgICAgaWYgKHRoaXMucmFuZ2UuZ2V0RW5kKCkgPCBpbnB1dC5sZW5ndGgpIHtcclxuICAgICAgICAgICAgcmVzdWx0ICs9IGlucHV0LnN1YnN0cmluZyh0aGlzLnJhbmdlLmdldEVuZCgpKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgcmV0dXJuIHJlc3VsdDtcclxuICAgIH1cclxuICAgIGVxdWFscyhvKSB7XHJcbiAgICAgICAgaWYgKHRoaXMgPT09IG8pXHJcbiAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgIGlmICghKG8gaW5zdGFuY2VvZiBTdWdnZXN0aW9uKSlcclxuICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgIHJldHVybiBpc0VxdWFsXzEuZGVmYXVsdCh0aGlzLnJhbmdlLCBvLnJhbmdlKSAmJiAodGhpcy50ZXh0ID09PSBvLnRleHQpICYmIGlzRXF1YWxfMS5kZWZhdWx0KHRoaXMudG9vbHRpcCwgby50b29sdGlwKTtcclxuICAgIH1cclxuICAgIHRvU3RyaW5nKCkge1xyXG4gICAgICAgIHJldHVybiBcIlN1Z2dlc3Rpb257XCIgK1xyXG4gICAgICAgICAgICBcInJhbmdlPVwiICsgdGhpcy5yYW5nZSArXHJcbiAgICAgICAgICAgIFwiLCB0ZXh0PSdcIiArIHRoaXMudGV4dCArICdcXCcnICtcclxuICAgICAgICAgICAgXCIsIHRvb2x0aXA9J1wiICsgdGhpcy50b29sdGlwICsgJ1xcJycgK1xyXG4gICAgICAgICAgICAnfSc7XHJcbiAgICB9XHJcbiAgICBjb21wYXJlVG8obykge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnRleHQgPCBvLnRleHQgPyAxIDogLTE7XHJcbiAgICB9XHJcbiAgICBjb21wYXJlVG9JZ25vcmVDYXNlKGIpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy50ZXh0LnRvTG93ZXJDYXNlKCkgPCBiLnRleHQudG9Mb3dlckNhc2UoKSA/IDEgOiAtMTtcclxuICAgIH1cclxuICAgIGV4cGFuZChjb21tYW5kLCByYW5nZSkge1xyXG4gICAgICAgIGlmIChyYW5nZS5lcXVhbHModGhpcy5yYW5nZSkpIHtcclxuICAgICAgICAgICAgcmV0dXJuIHRoaXM7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGxldCByZXN1bHQgPSBcIlwiO1xyXG4gICAgICAgIGlmIChyYW5nZS5nZXRTdGFydCgpIDwgdGhpcy5yYW5nZS5nZXRTdGFydCgpKSB7XHJcbiAgICAgICAgICAgIHJlc3VsdCArPSBjb21tYW5kLnN1YnN0cmluZyhyYW5nZS5nZXRTdGFydCgpLCB0aGlzLnJhbmdlLmdldFN0YXJ0KCkpO1xyXG4gICAgICAgIH1cclxuICAgICAgICByZXN1bHQgKz0gdGhpcy50ZXh0O1xyXG4gICAgICAgIGlmIChyYW5nZS5nZXRFbmQoKSA+IHRoaXMucmFuZ2UuZ2V0RW5kKCkpIHtcclxuICAgICAgICAgICAgcmVzdWx0ICs9IGNvbW1hbmQuc3Vic3RyaW5nKHRoaXMucmFuZ2UuZ2V0RW5kKCksIHJhbmdlLmdldEVuZCgpKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgcmV0dXJuIG5ldyBTdWdnZXN0aW9uKHJhbmdlLCByZXN1bHQsIHRoaXMudG9vbHRpcCk7XHJcbiAgICB9XHJcbn1cclxuZXhwb3J0cy5kZWZhdWx0ID0gU3VnZ2VzdGlvbjtcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbnZhciBfX2ltcG9ydERlZmF1bHQgPSAodGhpcyAmJiB0aGlzLl9faW1wb3J0RGVmYXVsdCkgfHwgZnVuY3Rpb24gKG1vZCkge1xyXG4gICAgcmV0dXJuIChtb2QgJiYgbW9kLl9fZXNNb2R1bGUpID8gbW9kIDogeyBcImRlZmF1bHRcIjogbW9kIH07XHJcbn07XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuY29uc3QgaXNFcXVhbF8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuLi91dGlsL2lzRXF1YWxcIikpO1xyXG5jb25zdCBTdHJpbmdSYW5nZV8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuLi9jb250ZXh0L1N0cmluZ1JhbmdlXCIpKTtcclxuY2xhc3MgU3VnZ2VzdGlvbnMge1xyXG4gICAgY29uc3RydWN0b3IocmFuZ2UsIHN1Z2dlc3Rpb25zKSB7XHJcbiAgICAgICAgdGhpcy5yYW5nZSA9IHJhbmdlO1xyXG4gICAgICAgIHRoaXMuc3VnZ2VzdGlvbnMgPSBzdWdnZXN0aW9ucztcclxuICAgIH1cclxuICAgIGdldFJhbmdlKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnJhbmdlO1xyXG4gICAgfVxyXG4gICAgZ2V0TGlzdCgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5zdWdnZXN0aW9ucztcclxuICAgIH1cclxuICAgIGlzRW1wdHkoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuc3VnZ2VzdGlvbnMubGVuZ3RoID09PSAwO1xyXG4gICAgfVxyXG4gICAgZXF1YWxzKG8pIHtcclxuICAgICAgICBpZiAodGhpcyA9PT0gbylcclxuICAgICAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICAgICAgaWYgKCEobyBpbnN0YW5jZW9mIFN1Z2dlc3Rpb25zKSlcclxuICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgIHJldHVybiB0aGlzLnJhbmdlLmVxdWFscyhvLnJhbmdlKSAmJiBpc0VxdWFsXzEuZGVmYXVsdCh0aGlzLnN1Z2dlc3Rpb25zLCBvLnN1Z2dlc3Rpb25zKTtcclxuICAgIH1cclxuICAgIHRvU3RyaW5nKCkge1xyXG4gICAgICAgIHJldHVybiBcIlN1Z2dlc3Rpb25ze1wiICtcclxuICAgICAgICAgICAgXCJyYW5nZT1cIiArIHRoaXMucmFuZ2UgK1xyXG4gICAgICAgICAgICBcIiwgc3VnZ2VzdGlvbnM9XCIgKyB0aGlzLnN1Z2dlc3Rpb25zICsgJ30nO1xyXG4gICAgfVxyXG4gICAgc3RhdGljIGVtcHR5KCkge1xyXG4gICAgICAgIHJldHVybiBQcm9taXNlLnJlc29sdmUodGhpcy5FTVBUWSk7XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgbWVyZ2UoY29tbWFuZCwgaW5wdXQpIHtcclxuICAgICAgICBpZiAoaW5wdXQubGVuZ3RoID09PSAwKSB7XHJcbiAgICAgICAgICAgIHJldHVybiB0aGlzLkVNUFRZO1xyXG4gICAgICAgIH1cclxuICAgICAgICBlbHNlIGlmIChpbnB1dC5sZW5ndGggPT09IDEpIHtcclxuICAgICAgICAgICAgcmV0dXJuIGlucHV0WzBdO1xyXG4gICAgICAgIH1cclxuICAgICAgICBjb25zdCB0ZXh0cyA9IFtdO1xyXG4gICAgICAgIGZvciAobGV0IHN1Z2dlc3Rpb25zIG9mIGlucHV0KSB7XHJcbiAgICAgICAgICAgIHRleHRzLnB1c2goLi4uc3VnZ2VzdGlvbnMuZ2V0TGlzdCgpKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgcmV0dXJuIFN1Z2dlc3Rpb25zLmNyZWF0ZShjb21tYW5kLCB0ZXh0cyk7XHJcbiAgICB9XHJcbiAgICBzdGF0aWMgY3JlYXRlKGNvbW1hbmQsIHN1Z2dlc3Rpb25zKSB7XHJcbiAgICAgICAgaWYgKHN1Z2dlc3Rpb25zLmxlbmd0aCA9PT0gMCkge1xyXG4gICAgICAgICAgICByZXR1cm4gdGhpcy5FTVBUWTtcclxuICAgICAgICB9XHJcbiAgICAgICAgbGV0IHN0YXJ0ID0gSW5maW5pdHk7XHJcbiAgICAgICAgbGV0IGVuZCA9IC1JbmZpbml0eTtcclxuICAgICAgICBmb3IgKGxldCBzdWdnZXN0aW9uIG9mIHN1Z2dlc3Rpb25zKSB7XHJcbiAgICAgICAgICAgIHN0YXJ0ID0gTWF0aC5taW4oc3VnZ2VzdGlvbi5nZXRSYW5nZSgpLmdldFN0YXJ0KCksIHN0YXJ0KTtcclxuICAgICAgICAgICAgZW5kID0gTWF0aC5tYXgoc3VnZ2VzdGlvbi5nZXRSYW5nZSgpLmdldEVuZCgpLCBlbmQpO1xyXG4gICAgICAgIH1cclxuICAgICAgICBsZXQgcmFuZ2UgPSBuZXcgU3RyaW5nUmFuZ2VfMS5kZWZhdWx0KHN0YXJ0LCBlbmQpO1xyXG4gICAgICAgIGNvbnN0IHRleHRzID0gW107XHJcbiAgICAgICAgZm9yIChsZXQgc3VnZ2VzdGlvbiBvZiBzdWdnZXN0aW9ucykge1xyXG4gICAgICAgICAgICB0ZXh0cy5wdXNoKHN1Z2dlc3Rpb24uZXhwYW5kKGNvbW1hbmQsIHJhbmdlKSk7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGNvbnN0IHNvcnRlZCA9IHRleHRzLnNvcnQoKGEsIGIpID0+IGEuY29tcGFyZVRvSWdub3JlQ2FzZShiKSk7XHJcbiAgICAgICAgcmV0dXJuIG5ldyBTdWdnZXN0aW9ucyhyYW5nZSwgc29ydGVkKTtcclxuICAgIH1cclxufVxyXG5TdWdnZXN0aW9ucy5FTVBUWSA9IG5ldyBTdWdnZXN0aW9ucyhTdHJpbmdSYW5nZV8xLmRlZmF1bHQuYXQoMCksIFtdKTtcclxuZXhwb3J0cy5kZWZhdWx0ID0gU3VnZ2VzdGlvbnM7XHJcbiIsIlwidXNlIHN0cmljdFwiO1xyXG52YXIgX19pbXBvcnREZWZhdWx0ID0gKHRoaXMgJiYgdGhpcy5fX2ltcG9ydERlZmF1bHQpIHx8IGZ1bmN0aW9uIChtb2QpIHtcclxuICAgIHJldHVybiAobW9kICYmIG1vZC5fX2VzTW9kdWxlKSA/IG1vZCA6IHsgXCJkZWZhdWx0XCI6IG1vZCB9O1xyXG59O1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNvbnN0IFN0cmluZ1JhbmdlXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4uL2NvbnRleHQvU3RyaW5nUmFuZ2VcIikpO1xyXG5jb25zdCBTdWdnZXN0aW9uXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vU3VnZ2VzdGlvblwiKSk7XHJcbmNvbnN0IFN1Z2dlc3Rpb25zXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vU3VnZ2VzdGlvbnNcIikpO1xyXG5jb25zdCBJbnRlZ2VyU3VnZ2VzdGlvbl8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL0ludGVnZXJTdWdnZXN0aW9uXCIpKTtcclxuY2xhc3MgU3VnZ2VzdGlvbnNCdWlsZGVyIHtcclxuICAgIGNvbnN0cnVjdG9yKGlucHV0LCBzdGFydCkge1xyXG4gICAgICAgIHRoaXMucmVzdWx0ID0gW107XHJcbiAgICAgICAgdGhpcy5pbnB1dCA9IGlucHV0O1xyXG4gICAgICAgIHRoaXMuc3RhcnQgPSBzdGFydDtcclxuICAgICAgICB0aGlzLnJlbWFpbmluZyA9IGlucHV0LnN1YnN0cmluZyhzdGFydCk7XHJcbiAgICB9XHJcbiAgICBnZXRJbnB1dCgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5pbnB1dDtcclxuICAgIH1cclxuICAgIGdldFN0YXJ0KCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnN0YXJ0O1xyXG4gICAgfVxyXG4gICAgZ2V0UmVtYWluaW5nKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLnJlbWFpbmluZztcclxuICAgIH1cclxuICAgIGJ1aWxkKCkge1xyXG4gICAgICAgIHJldHVybiBTdWdnZXN0aW9uc18xLmRlZmF1bHQuY3JlYXRlKHRoaXMuaW5wdXQsIHRoaXMucmVzdWx0KTtcclxuICAgIH1cclxuICAgIGJ1aWxkUHJvbWlzZSgpIHtcclxuICAgICAgICByZXR1cm4gUHJvbWlzZS5yZXNvbHZlKHRoaXMuYnVpbGQoKSk7XHJcbiAgICB9XHJcbiAgICBzdWdnZXN0KHRleHQsIHRvb2x0aXAgPSBudWxsKSB7XHJcbiAgICAgICAgaWYgKHR5cGVvZiB0ZXh0ID09PSBcIm51bWJlclwiKSB7XHJcbiAgICAgICAgICAgIHRoaXMucmVzdWx0LnB1c2gobmV3IEludGVnZXJTdWdnZXN0aW9uXzEuZGVmYXVsdChTdHJpbmdSYW5nZV8xLmRlZmF1bHQuYmV0d2Vlbih0aGlzLnN0YXJ0LCB0aGlzLmlucHV0Lmxlbmd0aCksIHRleHQsIHRvb2x0aXApKTtcclxuICAgICAgICAgICAgcmV0dXJuIHRoaXM7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIGlmICh0ZXh0ID09PSB0aGlzLnJlbWFpbmluZylcclxuICAgICAgICAgICAgcmV0dXJuIHRoaXM7XHJcbiAgICAgICAgdGhpcy5yZXN1bHQucHVzaChuZXcgU3VnZ2VzdGlvbl8xLmRlZmF1bHQoU3RyaW5nUmFuZ2VfMS5kZWZhdWx0LmJldHdlZW4odGhpcy5zdGFydCwgdGhpcy5pbnB1dC5sZW5ndGgpLCB0ZXh0LCB0b29sdGlwKSk7XHJcbiAgICAgICAgcmV0dXJuIHRoaXM7XHJcbiAgICB9XHJcbiAgICBhZGQob3RoZXIpIHtcclxuICAgICAgICB0aGlzLnJlc3VsdC5wdXNoKC4uLm90aGVyLnJlc3VsdCk7XHJcbiAgICAgICAgcmV0dXJuIHRoaXM7XHJcbiAgICB9XHJcbiAgICBjcmVhdGVPZmZzZXQoc3RhcnQpIHtcclxuICAgICAgICByZXR1cm4gbmV3IFN1Z2dlc3Rpb25zQnVpbGRlcih0aGlzLmlucHV0LCB0aGlzLnN0YXJ0KTtcclxuICAgIH1cclxuICAgIHJlc3RhcnQoKSB7XHJcbiAgICAgICAgcmV0dXJuIG5ldyBTdWdnZXN0aW9uc0J1aWxkZXIodGhpcy5pbnB1dCwgdGhpcy5zdGFydCk7XHJcbiAgICB9XHJcbn1cclxuZXhwb3J0cy5kZWZhdWx0ID0gU3VnZ2VzdGlvbnNCdWlsZGVyO1xyXG4iLCJcInVzZSBzdHJpY3RcIjtcclxudmFyIF9faW1wb3J0RGVmYXVsdCA9ICh0aGlzICYmIHRoaXMuX19pbXBvcnREZWZhdWx0KSB8fCBmdW5jdGlvbiAobW9kKSB7XHJcbiAgICByZXR1cm4gKG1vZCAmJiBtb2QuX19lc01vZHVsZSkgPyBtb2QgOiB7IFwiZGVmYXVsdFwiOiBtb2QgfTtcclxufTtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5jb25zdCBpc0VxdWFsXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4uL3V0aWwvaXNFcXVhbFwiKSk7XHJcbmNvbnN0IFN0cmluZ1JlYWRlcl8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuLi9TdHJpbmdSZWFkZXJcIikpO1xyXG5jb25zdCBSZXF1aXJlZEFyZ3VtZW50QnVpbGRlcl8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuLi9idWlsZGVyL1JlcXVpcmVkQXJndW1lbnRCdWlsZGVyXCIpKTtcclxuY29uc3QgUGFyc2VkQXJndW1lbnRfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi4vY29udGV4dC9QYXJzZWRBcmd1bWVudFwiKSk7XHJcbmNvbnN0IFN1Z2dlc3Rpb25zXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4uL3N1Z2dlc3Rpb24vU3VnZ2VzdGlvbnNcIikpO1xyXG5jb25zdCBDb21tYW5kTm9kZV8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuL0NvbW1hbmROb2RlXCIpKTtcclxuY29uc3QgVVNBR0VfQVJHVU1FTlRfT1BFTiA9IFwiPFwiO1xyXG5jb25zdCBVU0FHRV9BUkdVTUVOVF9DTE9TRSA9IFwiPlwiO1xyXG5jbGFzcyBBcmd1bWVudENvbW1hbmROb2RlIGV4dGVuZHMgQ29tbWFuZE5vZGVfMS5kZWZhdWx0IHtcclxuICAgIGNvbnN0cnVjdG9yKG5hbWUsIHR5cGUsIGNvbW1hbmQsIHJlcXVpcmVtZW50LCByZWRpcmVjdCwgbW9kaWZpZXIsIGZvcmtzLCBjdXN0b21TdWdnZXN0aW9ucykge1xyXG4gICAgICAgIHN1cGVyKGNvbW1hbmQsIHJlcXVpcmVtZW50LCByZWRpcmVjdCwgbW9kaWZpZXIsIGZvcmtzKTtcclxuICAgICAgICB0aGlzLm5hbWUgPSBuYW1lO1xyXG4gICAgICAgIHRoaXMudHlwZSA9IHR5cGU7XHJcbiAgICAgICAgdGhpcy5jdXN0b21TdWdnZXN0aW9ucyA9IGN1c3RvbVN1Z2dlc3Rpb25zO1xyXG4gICAgfVxyXG4gICAgZ2V0Tm9kZVR5cGUoKSB7XHJcbiAgICAgICAgcmV0dXJuIFwiYXJndW1lbnRcIjtcclxuICAgIH1cclxuICAgIGdldFR5cGUoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMudHlwZTtcclxuICAgIH1cclxuICAgIGdldE5hbWUoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMubmFtZTtcclxuICAgIH1cclxuICAgIGdldFVzYWdlVGV4dCgpIHtcclxuICAgICAgICByZXR1cm4gVVNBR0VfQVJHVU1FTlRfT1BFTiArIHRoaXMubmFtZSArIFVTQUdFX0FSR1VNRU5UX0NMT1NFO1xyXG4gICAgfVxyXG4gICAgZ2V0Q3VzdG9tU3VnZ2VzdGlvbnMoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuY3VzdG9tU3VnZ2VzdGlvbnM7XHJcbiAgICB9XHJcbiAgICBwYXJzZShyZWFkZXIsIGNvbnRleHRCdWlsZGVyKSB7XHJcbiAgICAgICAgbGV0IHN0YXJ0ID0gcmVhZGVyLmdldEN1cnNvcigpO1xyXG4gICAgICAgIGxldCByZXN1bHQgPSB0aGlzLnR5cGUucGFyc2UocmVhZGVyKTtcclxuICAgICAgICBsZXQgcGFyc2VkID0gbmV3IFBhcnNlZEFyZ3VtZW50XzEuZGVmYXVsdChzdGFydCwgcmVhZGVyLmdldEN1cnNvcigpLCByZXN1bHQpO1xyXG4gICAgICAgIGNvbnRleHRCdWlsZGVyLndpdGhBcmd1bWVudCh0aGlzLm5hbWUsIHBhcnNlZCk7XHJcbiAgICAgICAgY29udGV4dEJ1aWxkZXIud2l0aE5vZGUodGhpcywgcGFyc2VkLmdldFJhbmdlKCkpO1xyXG4gICAgfVxyXG4gICAgbGlzdFN1Z2dlc3Rpb25zKGNvbnRleHQsIGJ1aWxkZXIpIHtcclxuICAgICAgICBpZiAodGhpcy5jdXN0b21TdWdnZXN0aW9ucyA9PSBudWxsKSB7XHJcbiAgICAgICAgICAgIGlmICh0eXBlb2YgdGhpcy50eXBlLmxpc3RTdWdnZXN0aW9ucyA9PT0gXCJmdW5jdGlvblwiKVxyXG4gICAgICAgICAgICAgICAgcmV0dXJuIHRoaXMudHlwZS5saXN0U3VnZ2VzdGlvbnMoY29udGV4dCwgYnVpbGRlcik7XHJcbiAgICAgICAgICAgIGVsc2VcclxuICAgICAgICAgICAgICAgIHJldHVybiBTdWdnZXN0aW9uc18xLmRlZmF1bHQuZW1wdHkoKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgZWxzZSB7XHJcbiAgICAgICAgICAgIHJldHVybiB0aGlzLmN1c3RvbVN1Z2dlc3Rpb25zLmdldFN1Z2dlc3Rpb25zKGNvbnRleHQsIGJ1aWxkZXIpO1xyXG4gICAgICAgIH1cclxuICAgIH1cclxuICAgIGNyZWF0ZUJ1aWxkZXIoKSB7XHJcbiAgICAgICAgbGV0IGJ1aWxkZXIgPSBSZXF1aXJlZEFyZ3VtZW50QnVpbGRlcl8xLmRlZmF1bHQuYXJndW1lbnQodGhpcy5uYW1lLCB0aGlzLnR5cGUpO1xyXG4gICAgICAgIGJ1aWxkZXIucmVxdWlyZXModGhpcy5nZXRSZXF1aXJlbWVudCgpKTtcclxuICAgICAgICBidWlsZGVyLmZvcndhcmQodGhpcy5nZXRSZWRpcmVjdCgpLCB0aGlzLmdldFJlZGlyZWN0TW9kaWZpZXIoKSwgdGhpcy5pc0ZvcmsoKSk7XHJcbiAgICAgICAgYnVpbGRlci5zdWdnZXN0cyh0aGlzLmN1c3RvbVN1Z2dlc3Rpb25zKTtcclxuICAgICAgICBpZiAodGhpcy5nZXRDb21tYW5kKCkgIT0gbnVsbCkge1xyXG4gICAgICAgICAgICBidWlsZGVyLmV4ZWN1dGVzKHRoaXMuZ2V0Q29tbWFuZCgpKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgcmV0dXJuIGJ1aWxkZXI7XHJcbiAgICB9XHJcbiAgICBpc1ZhbGlkSW5wdXQoaW5wdXQpIHtcclxuICAgICAgICB0cnkge1xyXG4gICAgICAgICAgICBsZXQgcmVhZGVyID0gbmV3IFN0cmluZ1JlYWRlcl8xLmRlZmF1bHQoaW5wdXQpO1xyXG4gICAgICAgICAgICB0aGlzLnR5cGUucGFyc2UocmVhZGVyKTtcclxuICAgICAgICAgICAgcmV0dXJuICFyZWFkZXIuY2FuUmVhZCgpIHx8IHJlYWRlci5wZWVrKCkgPT0gJyAnO1xyXG4gICAgICAgIH1cclxuICAgICAgICBjYXRjaCAoaWdub3JlZCkge1xyXG4gICAgICAgIH1cclxuICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICB9XHJcbiAgICBlcXVhbHMobykge1xyXG4gICAgICAgIGlmICh0aGlzID09PSBvKVxyXG4gICAgICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgICAgICBpZiAoIShvIGluc3RhbmNlb2YgQXJndW1lbnRDb21tYW5kTm9kZSkpXHJcbiAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICBpZiAoISh0aGlzLm5hbWUgPT09IG8ubmFtZSkpXHJcbiAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICBpZiAoIWlzRXF1YWxfMS5kZWZhdWx0KHRoaXMudHlwZSwgby50eXBlKSlcclxuICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgIHJldHVybiBzdXBlci5lcXVhbHMobyk7XHJcbiAgICB9XHJcbiAgICBnZXRTb3J0ZWRLZXkoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMubmFtZTtcclxuICAgIH1cclxuICAgIGdldEV4YW1wbGVzKCkge1xyXG4gICAgICAgIHJldHVybiB0eXBlb2YgdGhpcy50eXBlLmdldEV4YW1wbGVzID09PSBcImZ1bmN0aW9uXCIgPyB0aGlzLnR5cGUuZ2V0RXhhbXBsZXMoKSA6IFtdO1xyXG4gICAgfVxyXG4gICAgdG9TdHJpbmcoKSB7XHJcbiAgICAgICAgcmV0dXJuIFwiPGFyZ3VtZW50IFwiICsgdGhpcy5uYW1lICsgXCI6XCIgKyB0aGlzLnR5cGUgKyBcIj5cIjtcclxuICAgIH1cclxufVxyXG5leHBvcnRzLmRlZmF1bHQgPSBBcmd1bWVudENvbW1hbmROb2RlO1xyXG4iLCJcInVzZSBzdHJpY3RcIjtcclxudmFyIF9faW1wb3J0RGVmYXVsdCA9ICh0aGlzICYmIHRoaXMuX19pbXBvcnREZWZhdWx0KSB8fCBmdW5jdGlvbiAobW9kKSB7XHJcbiAgICByZXR1cm4gKG1vZCAmJiBtb2QuX19lc01vZHVsZSkgPyBtb2QgOiB7IFwiZGVmYXVsdFwiOiBtb2QgfTtcclxufTtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5jb25zdCBpc0VxdWFsXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4uL3V0aWwvaXNFcXVhbFwiKSk7XHJcbmNsYXNzIENvbW1hbmROb2RlIHtcclxuICAgIGNvbnN0cnVjdG9yKGNvbW1hbmQsIHJlcXVpcmVtZW50LCByZWRpcmVjdCwgbW9kaWZpZXIsIGZvcmtzKSB7XHJcbiAgICAgICAgdGhpcy5jaGlsZHJlbiA9IG5ldyBNYXAoKTtcclxuICAgICAgICB0aGlzLmxpdGVyYWxzID0gbmV3IE1hcCgpO1xyXG4gICAgICAgIHRoaXMuYXJncyA9IG5ldyBNYXAoKTtcclxuICAgICAgICB0aGlzLmNvbW1hbmQgPSBjb21tYW5kO1xyXG4gICAgICAgIHRoaXMucmVxdWlyZW1lbnQgPSByZXF1aXJlbWVudCB8fCAoKCkgPT4gdHJ1ZSk7XHJcbiAgICAgICAgdGhpcy5yZWRpcmVjdCA9IHJlZGlyZWN0O1xyXG4gICAgICAgIHRoaXMubW9kaWZpZXIgPSBtb2RpZmllcjtcclxuICAgICAgICB0aGlzLmZvcmtzID0gZm9ya3M7XHJcbiAgICB9XHJcbiAgICBnZXRDb21tYW5kKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmNvbW1hbmQ7XHJcbiAgICB9XHJcbiAgICBnZXRDaGlsZHJlbigpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5jaGlsZHJlbi52YWx1ZXMoKTtcclxuICAgIH1cclxuICAgIGdldENoaWxkcmVuQ291bnQoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuY2hpbGRyZW4uc2l6ZTtcclxuICAgIH1cclxuICAgIGdldENoaWxkKG5hbWUpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5jaGlsZHJlbi5nZXQobmFtZSk7XHJcbiAgICB9XHJcbiAgICBnZXRSZWRpcmVjdCgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5yZWRpcmVjdDtcclxuICAgIH1cclxuICAgIGdldFJlZGlyZWN0TW9kaWZpZXIoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMubW9kaWZpZXI7XHJcbiAgICB9XHJcbiAgICBjYW5Vc2Uoc291cmNlKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMucmVxdWlyZW1lbnQoc291cmNlKTtcclxuICAgIH1cclxuICAgIGFkZENoaWxkKG5vZGUpIHtcclxuICAgICAgICBpZiAobm9kZS5nZXROb2RlVHlwZSgpID09PSBcInJvb3RcIikge1xyXG4gICAgICAgICAgICB0aHJvdyBuZXcgRXJyb3IoXCJDYW5ub3QgYWRkIGEgUm9vdENvbW1hbmROb2RlIGFzIGEgY2hpbGQgdG8gYW55IG90aGVyIENvbW1hbmROb2RlXCIpO1xyXG4gICAgICAgIH1cclxuICAgICAgICBsZXQgY2hpbGQgPSB0aGlzLmNoaWxkcmVuLmdldChub2RlLmdldE5hbWUoKSk7XHJcbiAgICAgICAgaWYgKGNoaWxkICE9IG51bGwpIHtcclxuICAgICAgICAgICAgLy8gIFdlJ3ZlIGZvdW5kIHNvbWV0aGluZyB0byBtZXJnZSBvbnRvXHJcbiAgICAgICAgICAgIGlmICgobm9kZS5nZXRDb21tYW5kKCkgIT0gbnVsbCkpIHtcclxuICAgICAgICAgICAgICAgIGNoaWxkLmNvbW1hbmQgPSBub2RlLmdldENvbW1hbmQoKTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICBmb3IgKGxldCBncmFuZGNoaWxkIG9mIG5vZGUuZ2V0Q2hpbGRyZW4oKSkge1xyXG4gICAgICAgICAgICAgICAgY2hpbGQuYWRkQ2hpbGQoZ3JhbmRjaGlsZCk7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9XHJcbiAgICAgICAgZWxzZSB7XHJcbiAgICAgICAgICAgIHRoaXMuY2hpbGRyZW4uc2V0KG5vZGUuZ2V0TmFtZSgpLCBub2RlKTtcclxuICAgICAgICAgICAgaWYgKG5vZGUuZ2V0Tm9kZVR5cGUoKSA9PT0gXCJsaXRlcmFsXCIpIHtcclxuICAgICAgICAgICAgICAgIHRoaXMubGl0ZXJhbHMuc2V0KG5vZGUuZ2V0TmFtZSgpLCBub2RlKTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICBlbHNlIGlmIChub2RlLmdldE5vZGVUeXBlKCkgPT09IFwiYXJndW1lbnRcIikge1xyXG4gICAgICAgICAgICAgICAgdGhpcy5hcmdzLnNldChub2RlLmdldE5hbWUoKSwgbm9kZSk7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9XHJcbiAgICAgICAgdGhpcy5jaGlsZHJlbiA9IG5ldyBNYXAoWy4uLnRoaXMuY2hpbGRyZW4uZW50cmllcygpXS5zb3J0KChhLCBiKSA9PiBhWzFdLmNvbXBhcmVUbyhiWzFdKSkpO1xyXG4gICAgfVxyXG4gICAgZmluZEFtYmlndWl0aWVzKGNvbnN1bWVyKSB7XHJcbiAgICAgICAgbGV0IG1hdGNoZXMgPSBbXTtcclxuICAgICAgICBmb3IgKGxldCBjaGlsZCBvZiB0aGlzLmNoaWxkcmVuLnZhbHVlcygpKSB7XHJcbiAgICAgICAgICAgIGZvciAobGV0IHNpYmxpbmcgb2YgdGhpcy5jaGlsZHJlbi52YWx1ZXMoKSkge1xyXG4gICAgICAgICAgICAgICAgaWYgKGNoaWxkID09PSBzaWJsaW5nKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgY29udGludWU7XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICBmb3IgKGxldCBpbnB1dCBvZiBjaGlsZC5nZXRFeGFtcGxlcygpKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgaWYgKHNpYmxpbmcuaXNWYWxpZElucHV0KGlucHV0KSkge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICBtYXRjaGVzLnB1c2goaW5wdXQpO1xyXG4gICAgICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIGlmIChtYXRjaGVzLmxlbmd0aCA+IDApIHtcclxuICAgICAgICAgICAgICAgICAgICBjb25zdW1lci5hbWJpZ3VvdXModGhpcywgY2hpbGQsIHNpYmxpbmcsIG1hdGNoZXMpO1xyXG4gICAgICAgICAgICAgICAgICAgIG1hdGNoZXMgPSBbXTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICBjaGlsZC5maW5kQW1iaWd1aXRpZXMoY29uc3VtZXIpO1xyXG4gICAgICAgIH1cclxuICAgIH1cclxuICAgIGVxdWFscyhvKSB7XHJcbiAgICAgICAgaWYgKHRoaXMgPT09IG8pXHJcbiAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgIGlmICghKG8gaW5zdGFuY2VvZiBDb21tYW5kTm9kZSkpXHJcbiAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICBpZiAodGhpcy5jaGlsZHJlbi5zaXplICE9PSBvLmNoaWxkcmVuLnNpemUpIHtcclxuICAgICAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgICAgIH1cclxuICAgICAgICBpZiAoIWlzRXF1YWxfMS5kZWZhdWx0KHRoaXMuY2hpbGRyZW4sIG8uY2hpbGRyZW4pKVxyXG4gICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICAgICAgaWYgKHRoaXMuY29tbWFuZCAhPSBudWxsID8gIWlzRXF1YWxfMS5kZWZhdWx0KHRoaXMuY29tbWFuZCwgby5jb21tYW5kKSA6IG8uY29tbWFuZCAhPSBudWxsKVxyXG4gICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICAgICAgcmV0dXJuIHRydWU7XHJcbiAgICB9XHJcbiAgICBnZXRSZXF1aXJlbWVudCgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5yZXF1aXJlbWVudDtcclxuICAgIH1cclxuICAgIGdldFJlbGV2YW50Tm9kZXMoaW5wdXQpIHtcclxuICAgICAgICBpZiAodGhpcy5saXRlcmFscy5zaXplID4gMCkge1xyXG4gICAgICAgICAgICBsZXQgY3Vyc29yID0gaW5wdXQuZ2V0Q3Vyc29yKCk7XHJcbiAgICAgICAgICAgIHdoaWxlICgoaW5wdXQuY2FuUmVhZCgpXHJcbiAgICAgICAgICAgICAgICAmJiAoaW5wdXQucGVlaygpICE9ICcgJykpKSB7XHJcbiAgICAgICAgICAgICAgICBpbnB1dC5za2lwKCk7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgbGV0IHRleHQgPSBpbnB1dC5nZXRTdHJpbmcoKS5zdWJzdHJpbmcoY3Vyc29yLCBpbnB1dC5nZXRDdXJzb3IoKSk7XHJcbiAgICAgICAgICAgIGlucHV0LnNldEN1cnNvcihjdXJzb3IpO1xyXG4gICAgICAgICAgICBsZXQgbGl0ZXJhbCA9IHRoaXMubGl0ZXJhbHMuZ2V0KHRleHQpO1xyXG4gICAgICAgICAgICBpZiAobGl0ZXJhbCAhPSBudWxsKSB7XHJcbiAgICAgICAgICAgICAgICByZXR1cm4gW2xpdGVyYWxdO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIGVsc2Uge1xyXG4gICAgICAgICAgICAgICAgcmV0dXJuIHRoaXMuYXJncy52YWx1ZXMoKTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgIH1cclxuICAgICAgICBlbHNlIHtcclxuICAgICAgICAgICAgcmV0dXJuIHRoaXMuYXJncy52YWx1ZXMoKTtcclxuICAgICAgICB9XHJcbiAgICB9XHJcbiAgICBjb21wYXJlVG8obykge1xyXG4gICAgICAgIGlmICh0aGlzLmdldE5vZGVUeXBlKCkgPT09IG8uZ2V0Tm9kZVR5cGUoKSkge1xyXG4gICAgICAgICAgICByZXR1cm4gdGhpcy5nZXRTb3J0ZWRLZXkoKSA+IG8uZ2V0U29ydGVkS2V5KCkgPyAxIDogLTE7XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHJldHVybiAoby5nZXROb2RlVHlwZSgpID09PSBcImxpdGVyYWxcIikgPyAxIDogLTE7XHJcbiAgICB9XHJcbiAgICBpc0ZvcmsoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuZm9ya3M7XHJcbiAgICB9XHJcbn1cclxuZXhwb3J0cy5kZWZhdWx0ID0gQ29tbWFuZE5vZGU7XHJcbiIsIlwidXNlIHN0cmljdFwiO1xyXG52YXIgX19pbXBvcnREZWZhdWx0ID0gKHRoaXMgJiYgdGhpcy5fX2ltcG9ydERlZmF1bHQpIHx8IGZ1bmN0aW9uIChtb2QpIHtcclxuICAgIHJldHVybiAobW9kICYmIG1vZC5fX2VzTW9kdWxlKSA/IG1vZCA6IHsgXCJkZWZhdWx0XCI6IG1vZCB9O1xyXG59O1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNvbnN0IENvbW1hbmROb2RlXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vQ29tbWFuZE5vZGVcIikpO1xyXG5jb25zdCBTdHJpbmdSZWFkZXJfMSA9IF9faW1wb3J0RGVmYXVsdChyZXF1aXJlKFwiLi4vU3RyaW5nUmVhZGVyXCIpKTtcclxuY29uc3QgTGl0ZXJhbEFyZ3VtZW50QnVpbGRlcl8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuLi9idWlsZGVyL0xpdGVyYWxBcmd1bWVudEJ1aWxkZXJcIikpO1xyXG5jb25zdCBTdHJpbmdSYW5nZV8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuLi9jb250ZXh0L1N0cmluZ1JhbmdlXCIpKTtcclxuY29uc3QgQ29tbWFuZFN5bnRheEV4Y2VwdGlvbl8xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuLi9leGNlcHRpb25zL0NvbW1hbmRTeW50YXhFeGNlcHRpb25cIikpO1xyXG5jb25zdCBTdWdnZXN0aW9uc18xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuLi9zdWdnZXN0aW9uL1N1Z2dlc3Rpb25zXCIpKTtcclxuY2xhc3MgTGl0ZXJhbENvbW1hbmROb2RlIGV4dGVuZHMgQ29tbWFuZE5vZGVfMS5kZWZhdWx0IHtcclxuICAgIGNvbnN0cnVjdG9yKGxpdGVyYWwsIGNvbW1hbmQsIHJlcXVpcmVtZW50LCByZWRpcmVjdCwgbW9kaWZpZXIsIGZvcmtzKSB7XHJcbiAgICAgICAgc3VwZXIoY29tbWFuZCwgcmVxdWlyZW1lbnQsIHJlZGlyZWN0LCBtb2RpZmllciwgZm9ya3MpO1xyXG4gICAgICAgIHRoaXMubGl0ZXJhbCA9IGxpdGVyYWw7XHJcbiAgICB9XHJcbiAgICBnZXROb2RlVHlwZSgpIHtcclxuICAgICAgICByZXR1cm4gXCJsaXRlcmFsXCI7XHJcbiAgICB9XHJcbiAgICBnZXRMaXRlcmFsKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmxpdGVyYWw7XHJcbiAgICB9XHJcbiAgICBnZXROYW1lKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLmxpdGVyYWw7XHJcbiAgICB9XHJcbiAgICBwYXJzZShyZWFkZXIsIGNvbnRleHRCdWlsZGVyKSB7XHJcbiAgICAgICAgbGV0IHN0YXJ0ID0gcmVhZGVyLmdldEN1cnNvcigpO1xyXG4gICAgICAgIGxldCBlbmQgPSB0aGlzLl9fcGFyc2UocmVhZGVyKTtcclxuICAgICAgICBpZiAoZW5kID4gLTEpIHtcclxuICAgICAgICAgICAgY29udGV4dEJ1aWxkZXIud2l0aE5vZGUodGhpcywgU3RyaW5nUmFuZ2VfMS5kZWZhdWx0LmJldHdlZW4oc3RhcnQsIGVuZCkpO1xyXG4gICAgICAgICAgICByZXR1cm47XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHRocm93IENvbW1hbmRTeW50YXhFeGNlcHRpb25fMS5kZWZhdWx0LkJVSUxUX0lOX0VYQ0VQVElPTlMubGl0ZXJhbEluY29ycmVjdCgpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlciwgdGhpcy5saXRlcmFsKTtcclxuICAgIH1cclxuICAgIF9fcGFyc2UocmVhZGVyKSB7XHJcbiAgICAgICAgbGV0IHN0YXJ0ID0gcmVhZGVyLmdldEN1cnNvcigpO1xyXG4gICAgICAgIGlmIChyZWFkZXIuY2FuUmVhZCh0aGlzLmxpdGVyYWwubGVuZ3RoKSkge1xyXG4gICAgICAgICAgICBsZXQgZW5kID0gc3RhcnQgKyB0aGlzLmxpdGVyYWwubGVuZ3RoO1xyXG4gICAgICAgICAgICBpZiAocmVhZGVyLmdldFN0cmluZygpLnN1YnN0cmluZyhzdGFydCwgZW5kKSA9PT0gdGhpcy5saXRlcmFsKSB7XHJcbiAgICAgICAgICAgICAgICByZWFkZXIuc2V0Q3Vyc29yKGVuZCk7XHJcbiAgICAgICAgICAgICAgICBpZiAoIXJlYWRlci5jYW5SZWFkKCkgfHwgcmVhZGVyLnBlZWsoKSA9PSAnICcpIHtcclxuICAgICAgICAgICAgICAgICAgICByZXR1cm4gZW5kO1xyXG4gICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgZWxzZSB7XHJcbiAgICAgICAgICAgICAgICAgICAgcmVhZGVyLnNldEN1cnNvcihzdGFydCk7XHJcbiAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9XHJcbiAgICAgICAgcmV0dXJuIC0xO1xyXG4gICAgfVxyXG4gICAgbGlzdFN1Z2dlc3Rpb25zKGNvbnRleHQsIGJ1aWxkZXIpIHtcclxuICAgICAgICBpZiAodGhpcy5saXRlcmFsLnRvTG93ZXJDYXNlKCkuc3RhcnRzV2l0aChidWlsZGVyLmdldFJlbWFpbmluZygpLnRvTG93ZXJDYXNlKCkpKSB7XHJcbiAgICAgICAgICAgIHJldHVybiBidWlsZGVyLnN1Z2dlc3QodGhpcy5saXRlcmFsKS5idWlsZFByb21pc2UoKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgZWxzZSB7XHJcbiAgICAgICAgICAgIHJldHVybiBTdWdnZXN0aW9uc18xLmRlZmF1bHQuZW1wdHkoKTtcclxuICAgICAgICB9XHJcbiAgICB9XHJcbiAgICBpc1ZhbGlkSW5wdXQoaW5wdXQpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5fX3BhcnNlKG5ldyBTdHJpbmdSZWFkZXJfMS5kZWZhdWx0KGlucHV0KSkgPiAtMTtcclxuICAgIH1cclxuICAgIGVxdWFscyhvKSB7XHJcbiAgICAgICAgaWYgKHRoaXMgPT09IG8pXHJcbiAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgIGlmICghKG8gaW5zdGFuY2VvZiBMaXRlcmFsQ29tbWFuZE5vZGUpKVxyXG4gICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICAgICAgaWYgKCEodGhpcy5saXRlcmFsID09PSBvLmxpdGVyYWwpKVxyXG4gICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICAgICAgcmV0dXJuIHN1cGVyLmVxdWFscyhvKTtcclxuICAgIH1cclxuICAgIGdldFVzYWdlVGV4dCgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5saXRlcmFsO1xyXG4gICAgfVxyXG4gICAgY3JlYXRlQnVpbGRlcigpIHtcclxuICAgICAgICBsZXQgYnVpbGRlciA9IExpdGVyYWxBcmd1bWVudEJ1aWxkZXJfMS5kZWZhdWx0LmxpdGVyYWwodGhpcy5saXRlcmFsKTtcclxuICAgICAgICBidWlsZGVyLnJlcXVpcmVzKHRoaXMuZ2V0UmVxdWlyZW1lbnQoKSk7XHJcbiAgICAgICAgYnVpbGRlci5mb3J3YXJkKHRoaXMuZ2V0UmVkaXJlY3QoKSwgdGhpcy5nZXRSZWRpcmVjdE1vZGlmaWVyKCksIHRoaXMuaXNGb3JrKCkpO1xyXG4gICAgICAgIGlmICh0aGlzLmdldENvbW1hbmQoKSAhPSBudWxsKVxyXG4gICAgICAgICAgICBidWlsZGVyLmV4ZWN1dGVzKHRoaXMuZ2V0Q29tbWFuZCgpKTtcclxuICAgICAgICByZXR1cm4gYnVpbGRlcjtcclxuICAgIH1cclxuICAgIGdldFNvcnRlZEtleSgpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5saXRlcmFsO1xyXG4gICAgfVxyXG4gICAgZ2V0RXhhbXBsZXMoKSB7XHJcbiAgICAgICAgcmV0dXJuIFt0aGlzLmxpdGVyYWxdO1xyXG4gICAgfVxyXG4gICAgdG9TdHJpbmcoKSB7XHJcbiAgICAgICAgcmV0dXJuIFwiPGxpdGVyYWwgXCIgKyB0aGlzLmxpdGVyYWwgKyBcIj5cIjtcclxuICAgIH1cclxufVxyXG5leHBvcnRzLmRlZmF1bHQgPSBMaXRlcmFsQ29tbWFuZE5vZGU7XHJcbiIsIlwidXNlIHN0cmljdFwiO1xyXG52YXIgX19pbXBvcnREZWZhdWx0ID0gKHRoaXMgJiYgdGhpcy5fX2ltcG9ydERlZmF1bHQpIHx8IGZ1bmN0aW9uIChtb2QpIHtcclxuICAgIHJldHVybiAobW9kICYmIG1vZC5fX2VzTW9kdWxlKSA/IG1vZCA6IHsgXCJkZWZhdWx0XCI6IG1vZCB9O1xyXG59O1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmNvbnN0IENvbW1hbmROb2RlXzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcIi4vQ29tbWFuZE5vZGVcIikpO1xyXG5jb25zdCBTdWdnZXN0aW9uc18xID0gX19pbXBvcnREZWZhdWx0KHJlcXVpcmUoXCIuLi9zdWdnZXN0aW9uL1N1Z2dlc3Rpb25zXCIpKTtcclxuY2xhc3MgUm9vdENvbW1hbmROb2RlIGV4dGVuZHMgQ29tbWFuZE5vZGVfMS5kZWZhdWx0IHtcclxuICAgIGNvbnN0cnVjdG9yKCkge1xyXG4gICAgICAgIHN1cGVyKG51bGwsIHMgPT4gdHJ1ZSwgbnVsbCwgKHMpID0+IHMuZ2V0U291cmNlKCksIGZhbHNlKTtcclxuICAgIH1cclxuICAgIGdldE5vZGVUeXBlKCkge1xyXG4gICAgICAgIHJldHVybiBcInJvb3RcIjtcclxuICAgIH1cclxuICAgIGdldE5hbWUoKSB7XHJcbiAgICAgICAgcmV0dXJuIFwiXCI7XHJcbiAgICB9XHJcbiAgICBnZXRVc2FnZVRleHQoKSB7XHJcbiAgICAgICAgcmV0dXJuIFwiXCI7XHJcbiAgICB9XHJcbiAgICBwYXJzZShyZWFkZXIsIGNvbnRleHRCdWlsZGVyKSB7XHJcbiAgICB9XHJcbiAgICBsaXN0U3VnZ2VzdGlvbnMoY29udGV4dCwgYnVpbGRlcikge1xyXG4gICAgICAgIHJldHVybiBTdWdnZXN0aW9uc18xLmRlZmF1bHQuZW1wdHkoKTtcclxuICAgIH1cclxuICAgIGlzVmFsaWRJbnB1dChpbnB1dCkge1xyXG4gICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgIH1cclxuICAgIGVxdWFscyhvKSB7XHJcbiAgICAgICAgaWYgKHRoaXMgPT09IG8pXHJcbiAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgIGlmICghKG8gaW5zdGFuY2VvZiBSb290Q29tbWFuZE5vZGUpKVxyXG4gICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICAgICAgcmV0dXJuIHN1cGVyLmVxdWFscyhvKTtcclxuICAgIH1cclxuICAgIGNyZWF0ZUJ1aWxkZXIoKSB7XHJcbiAgICAgICAgdGhyb3cgbmV3IEVycm9yKFwiQ2Fubm90IGNvbnZlcnQgcm9vdCBpbnRvIGEgYnVpbGRlclwiKTtcclxuICAgIH1cclxuICAgIGdldFNvcnRlZEtleSgpIHtcclxuICAgICAgICByZXR1cm4gXCJcIjtcclxuICAgIH1cclxuICAgIGdldEV4YW1wbGVzKCkge1xyXG4gICAgICAgIHJldHVybiBbXTtcclxuICAgIH1cclxuICAgIHRvU3RyaW5nKCkge1xyXG4gICAgICAgIHJldHVybiBcIjxyb290PlwiO1xyXG4gICAgfVxyXG59XHJcbmV4cG9ydHMuZGVmYXVsdCA9IFJvb3RDb21tYW5kTm9kZTtcclxuIiwiXCJ1c2Ugc3RyaWN0XCI7XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIl9fZXNNb2R1bGVcIiwgeyB2YWx1ZTogdHJ1ZSB9KTtcclxuZnVuY3Rpb24gaXNFcXVhbChhLCBiKSB7XHJcbiAgICBpZiAoYSA9PT0gYilcclxuICAgICAgICByZXR1cm4gdHJ1ZTtcclxuICAgIGlmICh0eXBlb2YgYSAhPSB0eXBlb2YgYilcclxuICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICBpZiAoIShhIGluc3RhbmNlb2YgT2JqZWN0KSlcclxuICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICBpZiAodHlwZW9mIGEgPT09IFwiZnVuY3Rpb25cIilcclxuICAgICAgICByZXR1cm4gYS50b1N0cmluZygpID09PSBiLnRvU3RyaW5nKCk7XHJcbiAgICBpZiAoYS5jb25zdHJ1Y3RvciAhPT0gYi5jb25zdHJ1Y3RvcilcclxuICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICBpZiAoYSBpbnN0YW5jZW9mIE1hcClcclxuICAgICAgICByZXR1cm4gaXNNYXBFcXVhbChhLCBiKTtcclxuICAgIGlmIChhIGluc3RhbmNlb2YgU2V0KVxyXG4gICAgICAgIHJldHVybiBpc0FycmF5RXF1YWwoWy4uLmFdLCBbLi4uYl0pO1xyXG4gICAgaWYgKGEgaW5zdGFuY2VvZiBBcnJheSlcclxuICAgICAgICByZXR1cm4gaXNBcnJheUVxdWFsKGEsIGIpO1xyXG4gICAgaWYgKHR5cGVvZiBhID09PSBcIm9iamVjdFwiKVxyXG4gICAgICAgIHJldHVybiBpc09iamVjdEVxdWFsKGEsIGIpO1xyXG4gICAgcmV0dXJuIGZhbHNlO1xyXG59XHJcbmV4cG9ydHMuZGVmYXVsdCA9IGlzRXF1YWw7XHJcbmZ1bmN0aW9uIGlzTWFwRXF1YWwoYSwgYikge1xyXG4gICAgaWYgKGEuc2l6ZSAhPSBiLnNpemUpXHJcbiAgICAgICAgcmV0dXJuIGZhbHNlO1xyXG4gICAgZm9yIChsZXQgW2tleSwgdmFsXSBvZiBhKSB7XHJcbiAgICAgICAgY29uc3QgdGVzdFZhbCA9IGIuZ2V0KGtleSk7XHJcbiAgICAgICAgaWYgKCFpc0VxdWFsKHRlc3RWYWwsIHZhbCkpXHJcbiAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgICAgICBpZiAodGVzdFZhbCA9PT0gdW5kZWZpbmVkICYmICFiLmhhcyhrZXkpKVxyXG4gICAgICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICB9XHJcbiAgICByZXR1cm4gdHJ1ZTtcclxufVxyXG5mdW5jdGlvbiBpc0FycmF5RXF1YWwoYSwgYikge1xyXG4gICAgaWYgKGEubGVuZ3RoICE9IGIubGVuZ3RoKVxyXG4gICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgIGZvciAobGV0IGkgPSAwOyBpIDwgYS5sZW5ndGg7IGkrKylcclxuICAgICAgICBpZiAoIWlzRXF1YWwoYVtpXSwgYltpXSkpXHJcbiAgICAgICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgIHJldHVybiB0cnVlO1xyXG59XHJcbmZ1bmN0aW9uIGlzT2JqZWN0RXF1YWwoYSwgYikge1xyXG4gICAgY29uc3QgYUtleXMgPSBPYmplY3Qua2V5cyhhKTtcclxuICAgIGNvbnN0IGJLZXlzID0gT2JqZWN0LmtleXMoYik7XHJcbiAgICBpZiAoYUtleXMubGVuZ3RoICE9IGJLZXlzLmxlbmd0aClcclxuICAgICAgICByZXR1cm4gZmFsc2U7XHJcbiAgICBpZiAoIWFLZXlzLmV2ZXJ5KGtleSA9PiBiLmhhc093blByb3BlcnR5KGtleSkpKVxyXG4gICAgICAgIHJldHVybiBmYWxzZTtcclxuICAgIHJldHVybiBhS2V5cy5ldmVyeSgoa2V5KSA9PiB7XHJcbiAgICAgICAgcmV0dXJuIGlzRXF1YWwoYVtrZXldLCBiW2tleV0pO1xyXG4gICAgfSk7XHJcbn1cclxuO1xyXG4iLCJpbXBvcnQge1xyXG5cdFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlLFxyXG5cdExpdGVyYWxNZXNzYWdlLFxyXG5cdFN1Z2dlc3Rpb25zLFxyXG5cdFN0cmluZ1JlYWRlcixcclxuXHRDb21tYW5kU3ludGF4RXhjZXB0aW9uLFxyXG5cdENvbW1hbmRDb250ZXh0LFxyXG5cdFN1Z2dlc3Rpb25zQnVpbGRlcixcclxuXHJcblx0Ly8gVHlwaW5nXHJcblx0QXJndW1lbnRUeXBlLFxyXG5cdEFyZ3VtZW50QnVpbGRlclxyXG59IGZyb20gXCJub2RlLWJyaWdhZGllclwiXHJcblxyXG5pbXBvcnQgXCIuL2JyaWdhZGllci1leHRlbnNpb25zXCJcclxuaW1wb3J0IFwiLi9hcnJheS1leHRlbnNpb25zXCJcclxuXHJcbi8qKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKipcclxuICogSGVscGVyIGNsYXNzZXMgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgKlxyXG4gKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqL1xyXG5cclxuLyoqXHJcbiAqIEhlbHBlciBmb3IgZ2VuZXJhdGluZyBQcm9taXNlPFN1Z2dlc3Rpb25zPiwgZnJvbSBTaGFyZWRTdWdnZXN0aW9uUHJvdmlkZXIuamF2YVxyXG4gKi9cclxuY2xhc3MgSGVscGVyU3VnZ2VzdGlvblByb3ZpZGVyIHtcclxuXHRwdWJsaWMgc3RhdGljIHN1Z2dlc3Qoc3VnZ2VzdGlvbnM6IHN0cmluZ1tdLCBidWlsZGVyOiBTdWdnZXN0aW9uc0J1aWxkZXIpOiBQcm9taXNlPFN1Z2dlc3Rpb25zPiB7XHJcblx0XHRsZXQgcmVtYWluaW5nTG93ZXJjYXNlOiBzdHJpbmcgPSBidWlsZGVyLmdldFJlbWFpbmluZygpLnRvTG93ZXJDYXNlKCk7XHJcblx0XHRmb3IgKGxldCBzdWdnZXN0aW9uIG9mIHN1Z2dlc3Rpb25zKSB7XHJcblx0XHRcdGlmIChIZWxwZXJTdWdnZXN0aW9uUHJvdmlkZXIubWF0Y2hlc1N1YlN0cihyZW1haW5pbmdMb3dlcmNhc2UsIHN1Z2dlc3Rpb24udG9Mb3dlckNhc2UoKSkpIHtcclxuXHRcdFx0XHRidWlsZGVyLnN1Z2dlc3Qoc3VnZ2VzdGlvbik7XHJcblx0XHRcdH1cclxuXHRcdH1cclxuXHRcdHJldHVybiBidWlsZGVyLmJ1aWxkUHJvbWlzZSgpO1xyXG5cdH1cclxuXHJcblx0cHVibGljIHN0YXRpYyBtYXRjaGVzU3ViU3RyKHJlbWFpbmluZzogc3RyaW5nLCBzdWdnZXN0aW9uOiBzdHJpbmcpOiBib29sZWFuIHtcclxuXHRcdGxldCBpbmRleDogbnVtYmVyID0gMDtcclxuXHRcdHdoaWxlICghc3VnZ2VzdGlvbi5zdGFydHNXaXRoKHJlbWFpbmluZywgaW5kZXgpKSB7XHJcblx0XHRcdGluZGV4ID0gc3VnZ2VzdGlvbi5pbmRleE9mKCdfJywgaW5kZXgpO1xyXG5cdFx0XHRpZiAoaW5kZXggPCAwKSB7XHJcblx0XHRcdFx0cmV0dXJuIGZhbHNlO1xyXG5cdFx0XHR9XHJcblx0XHRcdGluZGV4Kys7XHJcblx0XHR9XHJcblx0XHRyZXR1cm4gdHJ1ZTtcclxuXHR9XHJcbn1cclxuXHJcbmNsYXNzIFN1Z2dlc3Rpb25zSGVscGVyIHtcclxuXHRwdWJsaWMgc3RhdGljIHNob3VsZFN1Z2dlc3QocmVtYWluaW5nOiBzdHJpbmcsIHN1Z2dlc3Rpb246IHN0cmluZyk6IGJvb2xlYW4ge1xyXG5cdFx0bGV0IGk6IG51bWJlciA9IDA7XHJcblx0XHR3aGlsZSghc3VnZ2VzdGlvbi5zdGFydHNXaXRoKHJlbWFpbmluZywgaSkpIHtcclxuXHRcdFx0aSA9IHN1Z2dlc3Rpb24uaW5kZXhPZihcIl9cIiwgaSk7XHJcblx0XHRcdGlmKGkgPCAwKSB7XHJcblx0XHRcdFx0cmV0dXJuIGZhbHNlO1xyXG5cdFx0XHR9XHJcblx0XHRcdGkrKztcclxuXHRcdH1cclxuXHRcdHJldHVybiB0cnVlO1xyXG5cdH1cclxuXHJcblx0cHVibGljIHN0YXRpYyBzdWdnZXN0TWF0Y2hpbmcocmVhZGVyOiBTdHJpbmdSZWFkZXIsIHN1Z2dlc3Rpb25zOiBzdHJpbmdbXSk6IHN0cmluZ1tdIHtcclxuXHRcdGxldCBuZXdTdWdnZXN0aW9uczogc3RyaW5nW10gPSBbXTtcclxuXHRcdGNvbnN0IHJlbWFpbmluZzogc3RyaW5nID0gcmVhZGVyLmdldFJlbWFpbmluZygpLnRvTG9jYWxlTG93ZXJDYXNlKCk7XHJcblx0XHRmb3IobGV0IHN1Z2dlc3Rpb24gb2Ygc3VnZ2VzdGlvbnMpIHtcclxuXHRcdFx0aWYoIVN1Z2dlc3Rpb25zSGVscGVyLnNob3VsZFN1Z2dlc3QocmVtYWluaW5nLCBzdWdnZXN0aW9uLnRvTG9jYWxlTG93ZXJDYXNlKCkpKSB7XHJcblx0XHRcdFx0Y29udGludWU7XHJcblx0XHRcdH1cclxuXHRcdFx0bmV3U3VnZ2VzdGlvbnMucHVzaChzdWdnZXN0aW9uKTtcclxuXHRcdH1cclxuXHRcdHJldHVybiBuZXdTdWdnZXN0aW9ucztcclxuXHR9XHJcbn1cclxuXHJcbi8qKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKipcclxuICogQXJndW1lbnQgaW1wbGVtZW50YXRpb24gY2xhc3NlcyAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgKlxyXG4gKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqL1xyXG5cclxuLyoqXHJcbiAqIEBkZXByZWNhdGVkIFBsYWNlaG9sZGVyLCBhdm9pZCB1c2UgaW4gcHJvZHVjdGlvblxyXG4gKi9cclxuZXhwb3J0IGNsYXNzIFVuaW1wbGVtZW50ZWRBcmd1bWVudCBpbXBsZW1lbnRzIEFyZ3VtZW50VHlwZTxVbmltcGxlbWVudGVkQXJndW1lbnQ+IHtcclxuXHJcblx0cHVibGljIHBhcnNlKF9yZWFkZXI6IFN0cmluZ1JlYWRlcik6IFVuaW1wbGVtZW50ZWRBcmd1bWVudCB7XHJcblx0XHR0aHJvdyBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGUobmV3IExpdGVyYWxNZXNzYWdlKFwiVGhpcyBhcmd1bWVudCBoYXNuJ3QgYmVlbiBpbXBsZW1lbnRlZFwiKSkuY3JlYXRlKCk7XHJcblx0fVxyXG5cclxufVxyXG5cclxuZXhwb3J0IGNsYXNzIFRpbWVBcmd1bWVudCBpbXBsZW1lbnRzIEFyZ3VtZW50VHlwZTxUaW1lQXJndW1lbnQ+IHtcclxuXHJcblx0c3RhdGljIFVOSVRTOiBNYXA8c3RyaW5nLCBudW1iZXI+ID0gbmV3IE1hcChbXHJcblx0XHRbXCJkXCIsIDI0MDAwXSxcclxuXHRcdFtcInNcIiwgMjBdLFxyXG5cdFx0W1widFwiLCAxXSxcclxuXHRcdFtcIlwiLCAxXVxyXG5cdF0pO1xyXG5cclxuXHRwdWJsaWMgdGlja3M6IG51bWJlciA9IDA7XHJcblxyXG5cdHB1YmxpYyBwYXJzZShyZWFkZXI6IFN0cmluZ1JlYWRlcik6IFRpbWVBcmd1bWVudCB7XHJcblx0XHRjb25zdCBudW1lcmljYWxWYWx1ZTogbnVtYmVyID0gcmVhZGVyLnJlYWRGbG9hdCgpO1xyXG5cdFx0Y29uc3QgdW5pdDogc3RyaW5nID0gcmVhZGVyLnJlYWRVbnF1b3RlZFN0cmluZygpO1xyXG5cdFx0Y29uc3QgdW5pdE11bHRpcGxpZXI6IG51bWJlciB8IHVuZGVmaW5lZCA9IFRpbWVBcmd1bWVudC5VTklUUy5nZXQodW5pdCk7XHJcblx0XHRpZih1bml0TXVsdGlwbGllciA9PT0gdW5kZWZpbmVkKSB7XHJcblx0XHRcdHRocm93IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZShuZXcgTGl0ZXJhbE1lc3NhZ2UoYEludmFsaWQgdW5pdCBcIiR7dW5pdH1cImApKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdFx0fVxyXG5cdFx0Y29uc3QgdGlja3M6IG51bWJlciA9IE1hdGgucm91bmQobnVtZXJpY2FsVmFsdWUgKiB1bml0TXVsdGlwbGllcik7XHJcblx0XHRpZiAodGlja3MgPCAwKSB7XHJcblx0XHRcdHRocm93IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZShuZXcgTGl0ZXJhbE1lc3NhZ2UoXCJUaWNrIGNvdW50IG11c3QgYmUgbm9uLW5lZ2F0aXZlXCIpKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdFx0fVxyXG5cdFx0dGhpcy50aWNrcyA9IHRpY2tzO1xyXG5cdFx0cmV0dXJuIHRoaXM7XHJcblx0fVxyXG5cclxuXHRwdWJsaWMgbGlzdFN1Z2dlc3Rpb25zKF9jb250ZXh0OiBDb21tYW5kQ29udGV4dDxhbnk+LCBidWlsZGVyOiBTdWdnZXN0aW9uc0J1aWxkZXIpOiBQcm9taXNlPFN1Z2dlc3Rpb25zPiB7XHJcblx0XHRsZXQgcmVhZGVyOiBTdHJpbmdSZWFkZXIgPSBuZXcgU3RyaW5nUmVhZGVyKGJ1aWxkZXIuZ2V0UmVtYWluaW5nKCkpO1xyXG5cdFx0dHJ5IHtcclxuXHRcdFx0cmVhZGVyLnJlYWRGbG9hdCgpO1xyXG5cdFx0fSBjYXRjaCAoZXgpIHtcclxuXHRcdFx0cmV0dXJuIGJ1aWxkZXIuYnVpbGRQcm9taXNlKCk7XHJcblx0XHR9XHJcblx0XHRyZXR1cm4gSGVscGVyU3VnZ2VzdGlvblByb3ZpZGVyLnN1Z2dlc3QoWy4uLlRpbWVBcmd1bWVudC5VTklUUy5rZXlzKCldLCBidWlsZGVyLmNyZWF0ZU9mZnNldChidWlsZGVyLmdldFN0YXJ0KCkgKyByZWFkZXIuZ2V0Q3Vyc29yKCkpKTtcclxuXHR9XHJcblxyXG5cdHB1YmxpYyBnZXRFeGFtcGxlcygpOiBzdHJpbmdbXSB7XHJcblx0XHRyZXR1cm4gW1wiMGRcIiwgXCIwc1wiLCBcIjB0XCIsIFwiMFwiXTtcclxuXHR9XHJcbn1cclxuXHJcbmV4cG9ydCBjbGFzcyBCbG9ja1Bvc0FyZ3VtZW50IGltcGxlbWVudHMgQXJndW1lbnRUeXBlPEJsb2NrUG9zQXJndW1lbnQ+IHtcclxuXHJcblx0cHVibGljIHg6IG51bWJlciA9IDA7XHJcblx0cHVibGljIHk6IG51bWJlciA9IDA7XHJcblx0cHVibGljIHo6IG51bWJlciA9IDA7XHJcblxyXG5cdHB1YmxpYyBwYXJzZShyZWFkZXI6IFN0cmluZ1JlYWRlcik6IEJsb2NrUG9zQXJndW1lbnQge1xyXG5cdFx0dGhpcy54ID0gcmVhZGVyLnJlYWRMb2NhdGlvbkxpdGVyYWwoKTtcclxuXHRcdHJlYWRlci5za2lwKCk7XHJcblx0XHR0aGlzLnkgPSByZWFkZXIucmVhZExvY2F0aW9uTGl0ZXJhbCgpO1xyXG5cdFx0cmVhZGVyLnNraXAoKTtcclxuXHRcdHRoaXMueiA9IHJlYWRlci5yZWFkTG9jYXRpb25MaXRlcmFsKCk7XHJcblx0XHRyZXR1cm4gdGhpcztcclxuXHR9XHJcblxyXG5cdHB1YmxpYyBsaXN0U3VnZ2VzdGlvbnMoX2NvbnRleHQ6IENvbW1hbmRDb250ZXh0PGFueT4sIGJ1aWxkZXI6IFN1Z2dlc3Rpb25zQnVpbGRlcik6IFByb21pc2U8U3VnZ2VzdGlvbnM+IHtcclxuXHRcdGJ1aWxkZXIuc3VnZ2VzdChcIn5cIik7XHJcblx0XHRidWlsZGVyLnN1Z2dlc3QoXCJ+IH5cIik7XHJcblx0XHRidWlsZGVyLnN1Z2dlc3QoXCJ+IH4gflwiKTtcclxuXHRcdHJldHVybiBidWlsZGVyLmJ1aWxkUHJvbWlzZSgpO1xyXG5cdH1cclxuXHJcblx0cHVibGljIGdldEV4YW1wbGVzKCk6IHN0cmluZ1tdIHtcclxuXHRcdHJldHVybiBbXCIxIDIgM1wiXTtcclxuXHR9XHJcbn1cclxuXHJcbmV4cG9ydCBjbGFzcyBDb2x1bW5Qb3NBcmd1bWVudCBpbXBsZW1lbnRzIEFyZ3VtZW50VHlwZTxDb2x1bW5Qb3NBcmd1bWVudD4ge1xyXG5cclxuXHRwdWJsaWMgeDogbnVtYmVyID0gMDtcclxuXHRwdWJsaWMgejogbnVtYmVyID0gMDtcclxuXHJcblx0cHVibGljIHBhcnNlKHJlYWRlcjogU3RyaW5nUmVhZGVyKTogQ29sdW1uUG9zQXJndW1lbnQge1xyXG5cdFx0dGhpcy54ID0gcmVhZGVyLnJlYWRMb2NhdGlvbkxpdGVyYWwoKTtcclxuXHRcdHJlYWRlci5za2lwKCk7XHJcblx0XHR0aGlzLnogPSByZWFkZXIucmVhZExvY2F0aW9uTGl0ZXJhbCgpO1xyXG5cdFx0cmV0dXJuIHRoaXM7XHJcblx0fVxyXG5cclxuXHRwdWJsaWMgbGlzdFN1Z2dlc3Rpb25zKF9jb250ZXh0OiBDb21tYW5kQ29udGV4dDxhbnk+LCBidWlsZGVyOiBTdWdnZXN0aW9uc0J1aWxkZXIpOiBQcm9taXNlPFN1Z2dlc3Rpb25zPiB7XHJcblx0XHRidWlsZGVyLnN1Z2dlc3QoXCJ+XCIpO1xyXG5cdFx0YnVpbGRlci5zdWdnZXN0KFwifiB+XCIpO1xyXG5cdFx0cmV0dXJuIGJ1aWxkZXIuYnVpbGRQcm9taXNlKCk7XHJcblx0fVxyXG5cclxuXHRwdWJsaWMgZ2V0RXhhbXBsZXMoKTogc3RyaW5nW10ge1xyXG5cdFx0cmV0dXJuIFtcIjEgMlwiXTtcclxuXHR9XHJcbn1cclxuXHJcbmV4cG9ydCBjbGFzcyBQbGF5ZXJBcmd1bWVudCBpbXBsZW1lbnRzIEFyZ3VtZW50VHlwZTxzdHJpbmc+IHtcclxuXHJcblx0cHVibGljIHBhcnNlKHJlYWRlcjogU3RyaW5nUmVhZGVyKTogc3RyaW5nIHtcclxuXHRcdGNvbnN0IHN0YXJ0OiBudW1iZXIgPSByZWFkZXIuZ2V0Q3Vyc29yKCk7XHJcblx0XHR3aGlsZSAocmVhZGVyLmNhblJlYWQoKSAmJiByZWFkZXIucGVlaygpICE9PSBcIiBcIikge1xyXG5cdFx0XHRyZWFkZXIuc2tpcCgpO1xyXG5cdFx0fVxyXG5cclxuXHRcdGNvbnN0IHN0cmluZzogc3RyaW5nID0gcmVhZGVyLmdldFN0cmluZygpO1xyXG5cdFx0Y29uc3QgY3VycmVudEN1cnNvcjogbnVtYmVyID0gcmVhZGVyLmdldEN1cnNvcigpO1xyXG5cclxuXHRcdGNvbnN0IHVzZXJuYW1lOiBzdHJpbmcgPSBzdHJpbmcuc2xpY2Uoc3RhcnQsIGN1cnJlbnRDdXJzb3IpO1xyXG5cclxuXHRcdGlmICghdXNlcm5hbWUubWF0Y2goL15bQS1aYS16MC05X117MiwxNn0kLykpIHtcclxuXHRcdFx0dGhyb3cgbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKG5ldyBMaXRlcmFsTWVzc2FnZSh1c2VybmFtZSArIFwiIGlzIG5vdCBhIHZhbGlkIHVzZXJuYW1lXCIpKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdFx0fVxyXG5cclxuXHRcdHJldHVybiB1c2VybmFtZTtcclxuXHR9XHJcblxyXG5cdHB1YmxpYyBnZXRFeGFtcGxlcygpOiBzdHJpbmdbXSB7XHJcblx0XHRyZXR1cm4gW1wiU2tlcHRlclwiXTtcclxuXHR9XHJcbn1cclxuXHJcbmV4cG9ydCBjbGFzcyBNdWx0aUxpdGVyYWxBcmd1bWVudCBpbXBsZW1lbnRzIEFyZ3VtZW50VHlwZTxzdHJpbmc+IHtcclxuXHJcblx0cHJpdmF0ZSBsaXRlcmFsczogc3RyaW5nW107XHJcblxyXG5cdGNvbnN0cnVjdG9yKGxpdGVyYWxzOiBzdHJpbmdbXSkge1xyXG5cdFx0dGhpcy5saXRlcmFscyA9IGxpdGVyYWxzO1xyXG5cdH1cclxuXHJcblx0cHVibGljIHBhcnNlKHJlYWRlcjogU3RyaW5nUmVhZGVyKTogc3RyaW5nIHtcclxuXHRcdGNvbnN0IHN0YXJ0OiBudW1iZXIgPSByZWFkZXIuZ2V0Q3Vyc29yKCk7XHJcblx0XHR3aGlsZSAocmVhZGVyLmNhblJlYWQoKSAmJiByZWFkZXIucGVlaygpICE9PSBcIiBcIikge1xyXG5cdFx0XHRyZWFkZXIuc2tpcCgpO1xyXG5cdFx0fVxyXG5cclxuXHRcdGxldCBzZWxlY3RlZExpdGVyYWwgPSByZWFkZXIuZ2V0U3RyaW5nKCkuc2xpY2Uoc3RhcnQsIHJlYWRlci5nZXRDdXJzb3IoKSk7XHJcblxyXG5cdFx0aWYgKHNlbGVjdGVkTGl0ZXJhbC5lbmRzV2l0aChcIiBcIikpIHtcclxuXHRcdFx0c2VsZWN0ZWRMaXRlcmFsID0gc2VsZWN0ZWRMaXRlcmFsLnRyaW1FbmQoKTtcclxuXHRcdFx0cmVhZGVyLnNldEN1cnNvcihyZWFkZXIuZ2V0Q3Vyc29yKCkgLSAxKTtcclxuXHRcdH1cclxuXHJcblx0XHRpZiAoIXRoaXMubGl0ZXJhbHMuaW5jbHVkZXMoc2VsZWN0ZWRMaXRlcmFsKSkge1xyXG5cdFx0XHR0aHJvdyBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGUobmV3IExpdGVyYWxNZXNzYWdlKHNlbGVjdGVkTGl0ZXJhbCArIFwiIGlzIG5vdCBvbmUgb2YgXCIgKyB0aGlzLmxpdGVyYWxzKSkuY3JlYXRlV2l0aENvbnRleHQocmVhZGVyKTtcclxuXHRcdH1cclxuXHJcblx0XHRyZXR1cm4gc2VsZWN0ZWRMaXRlcmFsO1xyXG5cdH1cclxuXHJcblx0cHVibGljIGxpc3RTdWdnZXN0aW9ucyhfY29udGV4dDogQ29tbWFuZENvbnRleHQ8YW55PiwgYnVpbGRlcjogU3VnZ2VzdGlvbnNCdWlsZGVyKTogUHJvbWlzZTxTdWdnZXN0aW9ucz4ge1xyXG5cdFx0Zm9yIChsZXQgbGl0ZXJhbCBvZiB0aGlzLmxpdGVyYWxzKSB7XHJcblx0XHRcdGJ1aWxkZXIuc3VnZ2VzdChsaXRlcmFsKTtcclxuXHRcdH1cclxuXHRcdHJldHVybiBidWlsZGVyLmJ1aWxkUHJvbWlzZSgpO1xyXG5cdH1cclxuXHJcblx0cHVibGljIGdldEV4YW1wbGVzKCk6IHN0cmluZ1tdIHtcclxuXHRcdHJldHVybiBbXCJibGFoXCJdO1xyXG5cdH1cclxufVxyXG5cclxuZXhwb3J0IGNsYXNzIENvbG9yQXJndW1lbnQgaW1wbGVtZW50cyBBcmd1bWVudFR5cGU8c3RyaW5nPiB7XHJcblxyXG5cdHN0YXRpYyBDaGF0Q29sb3I6IHsgW2NvbG9yOiBzdHJpbmddOiBzdHJpbmcgfSA9IHtcclxuXHRcdC8vIFVzZXMgdGhlIHNlY3Rpb24gc3ltYm9sICjCpyksIGp1c3QgbGlrZSBNaW5lY3JhZnRcclxuXHRcdGJsYWNrOiBcIlxcdTAwQTcwXCIsXHJcblx0XHRkYXJrX2JsdWU6IFwiXFx1MDBBNzFcIixcclxuXHRcdGRhcmtfZ3JlZW46IFwiXFx1MDBBNzJcIixcclxuXHRcdGRhcmtfYXF1YTogXCJcXHUwMEE3M1wiLFxyXG5cdFx0ZGFya19yZWQ6IFwiXFx1MDBBNzRcIixcclxuXHRcdGRhcmtfcHVycGxlOiBcIlxcdTAwQTc1XCIsXHJcblx0XHRnb2xkOiBcIlxcdTAwQTc2XCIsXHJcblx0XHRncmF5OiBcIlxcdTAwQTc3XCIsXHJcblx0XHRkYXJrX2dyYXk6IFwiXFx1MDBBNzhcIixcclxuXHRcdGJsdWU6IFwiXFx1MDBBNzlcIixcclxuXHRcdGdyZWVuOiBcIlxcdTAwQTdhXCIsXHJcblx0XHRhcXVhOiBcIlxcdTAwQTdiXCIsXHJcblx0XHRyZWQ6IFwiXFx1MDBBN2NcIixcclxuXHRcdGxpZ2h0X3B1cnBsZTogXCJcXHUwMEE3ZFwiLFxyXG5cdFx0eWVsbG93OiBcIlxcdTAwQTdlXCIsXHJcblx0XHR3aGl0ZTogXCJcXHUwMEE3ZlwiLFxyXG5cdH07XHJcblxyXG5cdHB1YmxpYyBwYXJzZShyZWFkZXI6IFN0cmluZ1JlYWRlcik6IHN0cmluZyB7XHJcblx0XHRsZXQgaW5wdXQgPSByZWFkZXIucmVhZFVucXVvdGVkU3RyaW5nKCk7XHJcblx0XHRsZXQgY2hhdENvbG9yOiBzdHJpbmcgfCB1bmRlZmluZWQgPSBDb2xvckFyZ3VtZW50LkNoYXRDb2xvcltpbnB1dC50b0xvd2VyQ2FzZSgpXTtcclxuXHRcdGlmIChjaGF0Q29sb3IgPT09IHVuZGVmaW5lZCkge1xyXG5cdFx0XHR0aHJvdyBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGUobmV3IExpdGVyYWxNZXNzYWdlKGBVbmtub3duIGNvbG91ciAnJHtpbnB1dH0nYCkpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlcik7XHJcblx0XHR9XHJcblx0XHRyZXR1cm4gY2hhdENvbG9yO1xyXG5cdH1cclxuXHJcblx0cHVibGljIGxpc3RTdWdnZXN0aW9ucyhfY29udGV4dDogQ29tbWFuZENvbnRleHQ8YW55PiwgYnVpbGRlcjogU3VnZ2VzdGlvbnNCdWlsZGVyKTogUHJvbWlzZTxTdWdnZXN0aW9ucz4ge1xyXG5cdFx0cmV0dXJuIEhlbHBlclN1Z2dlc3Rpb25Qcm92aWRlci5zdWdnZXN0KE9iamVjdC5rZXlzKENvbG9yQXJndW1lbnQuQ2hhdENvbG9yKSwgYnVpbGRlcik7XHJcblx0fVxyXG5cclxuXHRwdWJsaWMgZ2V0RXhhbXBsZXMoKTogc3RyaW5nW10ge1xyXG5cdFx0cmV0dXJuIFtcInJlZFwiLCBcImdyZWVuXCJdO1xyXG5cdH1cclxufVxyXG5cclxuZXhwb3J0IGNsYXNzIFBvdGlvbkVmZmVjdEFyZ3VtZW50IGltcGxlbWVudHMgQXJndW1lbnRUeXBlPHN0cmluZz4ge1xyXG5cclxuXHRzdGF0aWMgcmVhZG9ubHkgUG90aW9uRWZmZWN0czogcmVhZG9ubHkgc3RyaW5nW10gPSBbXHJcblx0XHRcIm1pbmVjcmFmdDpzcGVlZFwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6c2xvd25lc3NcIixcclxuXHRcdFwibWluZWNyYWZ0Omhhc3RlXCIsXHJcblx0XHRcIm1pbmVjcmFmdDptaW5pbmdfZmF0aWd1ZVwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6c3RyZW5ndGhcIixcclxuXHRcdFwibWluZWNyYWZ0Omluc3RhbnRfaGVhbHRoXCIsXHJcblx0XHRcIm1pbmVjcmFmdDppbnN0YW50X2RhbWFnZVwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6anVtcF9ib29zdFwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6bmF1c2VhXCIsXHJcblx0XHRcIm1pbmVjcmFmdDpyZWdlbmVyYXRpb25cIixcclxuXHRcdFwibWluZWNyYWZ0OnJlc2lzdGFuY2VcIixcclxuXHRcdFwibWluZWNyYWZ0OmZpcmVfcmVzaXN0YW5jZVwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6d2F0ZXJfYnJlYXRoaW5nXCIsXHJcblx0XHRcIm1pbmVjcmFmdDppbnZpc2liaWxpdHlcIixcclxuXHRcdFwibWluZWNyYWZ0OmJsaW5kbmVzc1wiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6bmlnaHRfdmlzaW9uXCIsXHJcblx0XHRcIm1pbmVjcmFmdDpodW5nZXJcIixcclxuXHRcdFwibWluZWNyYWZ0OndlYWtuZXNzXCIsXHJcblx0XHRcIm1pbmVjcmFmdDpwb2lzb25cIixcclxuXHRcdFwibWluZWNyYWZ0OndpdGhlclwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6aGVhbHRoX2Jvb3N0XCIsXHJcblx0XHRcIm1pbmVjcmFmdDphYnNvcnB0aW9uXCIsXHJcblx0XHRcIm1pbmVjcmFmdDpzYXR1cmF0aW9uXCIsXHJcblx0XHRcIm1pbmVjcmFmdDpnbG93aW5nXCIsXHJcblx0XHRcIm1pbmVjcmFmdDpsZXZpdGF0aW9uXCIsXHJcblx0XHRcIm1pbmVjcmFmdDpsdWNrXCIsXHJcblx0XHRcIm1pbmVjcmFmdDp1bmx1Y2tcIixcclxuXHRcdFwibWluZWNyYWZ0OnNsb3dfZmFsbGluZ1wiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6Y29uZHVpdF9wb3dlclwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6ZG9scGhpbnNfZ3JhY2VcIixcclxuXHRcdFwibWluZWNyYWZ0OmJhZF9vbWVuXCIsXHJcblx0XHRcIm1pbmVjcmFmdDpoZXJvX29mX3RoZV92aWxsYWdlXCIsXHJcblx0XHRcIm1pbmVjcmFmdDpkYXJrbmVzc1wiLFxyXG5cdF07XHJcblxyXG5cdHB1YmxpYyBwYXJzZShyZWFkZXI6IFN0cmluZ1JlYWRlcik6IHN0cmluZyB7XHJcblx0XHRjb25zdCByZXNvdXJjZUxvY2F0aW9uOiBbc3RyaW5nLCBzdHJpbmddID0gcmVhZGVyLnJlYWRSZXNvdXJjZUxvY2F0aW9uKCk7XHJcblx0XHRjb25zdCBwb3Rpb25FZmZlY3QgPSByZXNvdXJjZUxvY2F0aW9uWzBdICsgXCI6XCIgKyByZXNvdXJjZUxvY2F0aW9uWzFdO1xyXG5cdFx0Y29uc3QgaXNWYWxpZFBvdGlvbkVmZmVjdDogYm9vbGVhbiA9IFBvdGlvbkVmZmVjdEFyZ3VtZW50LlBvdGlvbkVmZmVjdHMuaW5jbHVkZXMocG90aW9uRWZmZWN0LnRvTG93ZXJDYXNlKCkpO1xyXG5cdFx0aWYgKCFpc1ZhbGlkUG90aW9uRWZmZWN0KSB7XHJcblx0XHRcdHRocm93IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZShuZXcgTGl0ZXJhbE1lc3NhZ2UoYFVua25vd24gZWZmZWN0ICcke3BvdGlvbkVmZmVjdH0nYCkpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlcik7XHJcblx0XHR9XHJcblx0XHRyZXR1cm4gcG90aW9uRWZmZWN0O1xyXG5cdH1cclxuXHJcblx0cHVibGljIGxpc3RTdWdnZXN0aW9ucyhfY29udGV4dDogQ29tbWFuZENvbnRleHQ8YW55PiwgYnVpbGRlcjogU3VnZ2VzdGlvbnNCdWlsZGVyKTogUHJvbWlzZTxTdWdnZXN0aW9ucz4ge1xyXG5cdFx0cmV0dXJuIEhlbHBlclN1Z2dlc3Rpb25Qcm92aWRlci5zdWdnZXN0KFsuLi5Qb3Rpb25FZmZlY3RBcmd1bWVudC5Qb3Rpb25FZmZlY3RzXSwgYnVpbGRlcik7XHJcblx0fVxyXG5cclxuXHRwdWJsaWMgZ2V0RXhhbXBsZXMoKTogc3RyaW5nW10ge1xyXG5cdFx0cmV0dXJuIFtcInNwb29reVwiLCBcImVmZmVjdFwiXTtcclxuXHR9XHJcbn1cclxuXHJcbmV4cG9ydCBjbGFzcyBFbmNoYW50bWVudEFyZ3VtZW50IGltcGxlbWVudHMgQXJndW1lbnRUeXBlPHN0cmluZz4ge1xyXG5cclxuXHRzdGF0aWMgcmVhZG9ubHkgRW5jaGFudG1lbnRzOiByZWFkb25seSBzdHJpbmdbXSA9IFtcclxuXHRcdFwibWluZWNyYWZ0OnByb3RlY3Rpb25cIixcclxuXHRcdFwibWluZWNyYWZ0OmZpcmVfcHJvdGVjdGlvblwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6ZmVhdGhlcl9mYWxsaW5nXCIsXHJcblx0XHRcIm1pbmVjcmFmdDpibGFzdF9wcm90ZWN0aW9uXCIsXHJcblx0XHRcIm1pbmVjcmFmdDpwcm9qZWN0aWxlX3Byb3RlY3Rpb25cIixcclxuXHRcdFwibWluZWNyYWZ0OnJlc3BpcmF0aW9uXCIsXHJcblx0XHRcIm1pbmVjcmFmdDphcXVhX2FmZmluaXR5XCIsXHJcblx0XHRcIm1pbmVjcmFmdDp0aG9ybnNcIixcclxuXHRcdFwibWluZWNyYWZ0OmRlcHRoX3N0cmlkZXJcIixcclxuXHRcdFwibWluZWNyYWZ0OmZyb3N0X3dhbGtlclwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6YmluZGluZ19jdXJzZVwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6c291bF9zcGVlZFwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6c3dpZnRfc25lYWtcIixcclxuXHRcdFwibWluZWNyYWZ0OnNoYXJwbmVzc1wiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6c21pdGVcIixcclxuXHRcdFwibWluZWNyYWZ0OmJhbmVfb2ZfYXJ0aHJvcG9kc1wiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6a25vY2tiYWNrXCIsXHJcblx0XHRcIm1pbmVjcmFmdDpmaXJlX2FzcGVjdFwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6bG9vdGluZ1wiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6c3dlZXBpbmdcIixcclxuXHRcdFwibWluZWNyYWZ0OmVmZmljaWVuY3lcIixcclxuXHRcdFwibWluZWNyYWZ0OnNpbGtfdG91Y2hcIixcclxuXHRcdFwibWluZWNyYWZ0OnVuYnJlYWtpbmdcIixcclxuXHRcdFwibWluZWNyYWZ0OmZvcnR1bmVcIixcclxuXHRcdFwibWluZWNyYWZ0OnBvd2VyXCIsXHJcblx0XHRcIm1pbmVjcmFmdDpwdW5jaFwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6ZmxhbWVcIixcclxuXHRcdFwibWluZWNyYWZ0OmluZmluaXR5XCIsXHJcblx0XHRcIm1pbmVjcmFmdDpsdWNrX29mX3RoZV9zZWFcIixcclxuXHRcdFwibWluZWNyYWZ0Omx1cmVcIixcclxuXHRcdFwibWluZWNyYWZ0OmxveWFsdHlcIixcclxuXHRcdFwibWluZWNyYWZ0OmltcGFsaW5nXCIsXHJcblx0XHRcIm1pbmVjcmFmdDpyaXB0aWRlXCIsXHJcblx0XHRcIm1pbmVjcmFmdDpjaGFubmVsaW5nXCIsXHJcblx0XHRcIm1pbmVjcmFmdDptdWx0aXNob3RcIixcclxuXHRcdFwibWluZWNyYWZ0OnF1aWNrX2NoYXJnZVwiLFxyXG5cdFx0XCJtaW5lY3JhZnQ6cGllcmNpbmdcIixcclxuXHRcdFwibWluZWNyYWZ0Om1lbmRpbmdcIixcclxuXHRcdFwibWluZWNyYWZ0OnZhbmlzaGluZ19jdXJzZVwiLFxyXG5cdF07XHJcblxyXG5cdHB1YmxpYyBwYXJzZShyZWFkZXI6IFN0cmluZ1JlYWRlcik6IHN0cmluZyB7XHJcblx0XHRjb25zdCByZXNvdXJjZUxvY2F0aW9uOiBbc3RyaW5nLCBzdHJpbmddID0gcmVhZGVyLnJlYWRSZXNvdXJjZUxvY2F0aW9uKCk7XHJcblx0XHRjb25zdCBlbmNoYW50bWVudCA9IHJlc291cmNlTG9jYXRpb25bMF0gKyBcIjpcIiArIHJlc291cmNlTG9jYXRpb25bMV07XHJcblx0XHRjb25zdCBpc1ZhbGlkRW5jaGFudG1lbnQ6IGJvb2xlYW4gPSBFbmNoYW50bWVudEFyZ3VtZW50LkVuY2hhbnRtZW50cy5pbmNsdWRlcyhlbmNoYW50bWVudC50b0xvd2VyQ2FzZSgpKTtcclxuXHRcdGlmICghaXNWYWxpZEVuY2hhbnRtZW50KSB7XHJcblx0XHRcdHRocm93IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZShuZXcgTGl0ZXJhbE1lc3NhZ2UoYFVua25vd24gZW5jaGFudG1lbnQgJyR7ZW5jaGFudG1lbnR9J2ApKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdFx0fVxyXG5cdFx0cmV0dXJuIGVuY2hhbnRtZW50O1xyXG5cdH1cclxuXHJcblx0cHVibGljIGxpc3RTdWdnZXN0aW9ucyhfY29udGV4dDogQ29tbWFuZENvbnRleHQ8YW55PiwgYnVpbGRlcjogU3VnZ2VzdGlvbnNCdWlsZGVyKTogUHJvbWlzZTxTdWdnZXN0aW9ucz4ge1xyXG5cdFx0cmV0dXJuIEhlbHBlclN1Z2dlc3Rpb25Qcm92aWRlci5zdWdnZXN0KFsuLi5FbmNoYW50bWVudEFyZ3VtZW50LkVuY2hhbnRtZW50c10sIGJ1aWxkZXIpO1xyXG5cdH1cclxuXHJcblx0cHVibGljIGdldEV4YW1wbGVzKCk6IHN0cmluZ1tdIHtcclxuXHRcdHJldHVybiBbXCJ1bmJyZWFraW5nXCIsIFwic2lsa190b3VjaFwiXTtcclxuXHR9XHJcbn1cclxuXHJcbmV4cG9ydCBjbGFzcyBBbmdsZUFyZ3VtZW50IGltcGxlbWVudHMgQXJndW1lbnRUeXBlPHsgYW5nbGU6IG51bWJlciwgcmVsYXRpdmU6IGJvb2xlYW4gfT4ge1xyXG5cclxuXHRwdWJsaWMgYW5nbGU6IG51bWJlcjtcclxuXHRwdWJsaWMgcmVsYXRpdmU6IGJvb2xlYW47XHJcblxyXG5cdHB1YmxpYyBwYXJzZShyZWFkZXI6IFN0cmluZ1JlYWRlcik6IEFuZ2xlQXJndW1lbnQge1xyXG5cdFx0aWYoIXJlYWRlci5jYW5SZWFkKCkpIHtcclxuXHRcdFx0dGhyb3cgbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKG5ldyBMaXRlcmFsTWVzc2FnZShcIkluY29tcGxldGUgKGV4cGVjdGVkIDEgYW5nbGUpXCIpKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdFx0fVxyXG5cdFx0Ly8gUmVhZCByZWxhdGl2ZSB+XHJcblx0XHRpZiAocmVhZGVyLnBlZWsoKSA9PT0gJ34nKSB7XHJcblx0XHRcdHRoaXMucmVsYXRpdmUgPSB0cnVlO1xyXG5cdFx0XHRyZWFkZXIuc2tpcCgpO1xyXG5cdFx0fSBlbHNlIHtcclxuXHRcdFx0dGhpcy5yZWxhdGl2ZSA9IGZhbHNlO1xyXG5cdFx0fVxyXG5cdFx0dGhpcy5hbmdsZSA9IChyZWFkZXIuY2FuUmVhZCgpICYmIHJlYWRlci5wZWVrKCkgIT09ICcgJykgPyByZWFkZXIucmVhZEZsb2F0KCkgOiAwLjA7XHJcblx0XHRpZihpc05hTih0aGlzLmFuZ2xlKSB8fCAhaXNGaW5pdGUodGhpcy5hbmdsZSkpIHtcclxuXHRcdFx0dGhyb3cgbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKG5ldyBMaXRlcmFsTWVzc2FnZShcIkludmFsaWQgYW5nbGVcIikpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlcik7XHJcblx0XHR9XHJcblx0XHRyZXR1cm4gdGhpcztcclxuXHR9XHJcblxyXG5cdHB1YmxpYyBnZXRFeGFtcGxlcygpOiBzdHJpbmdbXSB7XHJcblx0XHRyZXR1cm4gW1wiMFwiLCBcIn5cIiwgXCJ+LTVcIl07XHJcblx0fVxyXG59XHJcblxyXG5leHBvcnQgY2xhc3MgVVVJREFyZ3VtZW50IGltcGxlbWVudHMgQXJndW1lbnRUeXBlPHN0cmluZz4ge1xyXG5cclxuXHRwdWJsaWMgcGFyc2UocmVhZGVyOiBTdHJpbmdSZWFkZXIpOiBzdHJpbmcge1xyXG5cdFx0Y29uc3QgcmVtYWluaW5nOiBzdHJpbmcgPSByZWFkZXIuZ2V0UmVtYWluaW5nKCk7XHJcblx0XHRjb25zdCBtYXRjaGVkUmVzdWx0cyA9IHJlbWFpbmluZy5tYXRjaCgvXihbLUEtRmEtZjAtOV0rKS8pO1xyXG5cdFx0aWYobWF0Y2hlZFJlc3VsdHMgIT09IG51bGwgJiYgbWF0Y2hlZFJlc3VsdHNbMV0gIT09IHVuZGVmaW5lZCkge1xyXG5cdFx0XHRjb25zdCB1dWlkID0gbWF0Y2hlZFJlc3VsdHNbMV07XHJcblx0XHRcdC8vIFJlZ2V4IGZvciBhIFVVSUQ6IGh0dHBzOi8vc3RhY2tvdmVyZmxvdy5jb20vYS8xMzY1MzE4MC80Nzc5MDcxXHJcblx0XHRcdGlmKHV1aWQubWF0Y2goL15bMC05YS1mXXs4fS1bMC05YS1mXXs0fS1bMC01XVswLTlhLWZdezN9LVswODlhYl1bMC05YS1mXXszfS1bMC05YS1mXXsxMn0kL2kpICE9PSBudWxsKSB7XHJcblx0XHRcdFx0cmVhZGVyLnNldEN1cnNvcihyZWFkZXIuZ2V0Q3Vyc29yKCkgKyB1dWlkLmxlbmd0aCk7XHJcblx0XHRcdFx0cmV0dXJuIHV1aWQ7XHJcblx0XHRcdH1cclxuXHRcdH1cclxuXHRcdHRocm93IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZShuZXcgTGl0ZXJhbE1lc3NhZ2UoXCJJbnZhbGlkIFVVSURcIikpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlcik7XHJcblx0fVxyXG5cclxuXHRwdWJsaWMgZ2V0RXhhbXBsZXMoKTogc3RyaW5nW10ge1xyXG5cdFx0cmV0dXJuIFtcImRkMTJiZTQyLTUyYTktNGE5MS1hOGExLTExYzAxODQ5ZTQ5OFwiXTtcclxuXHR9XHJcbn1cclxuXHJcbmNvbnN0IE1hdGhPcGVyYXRpb25zID0gW1wiPVwiLCBcIis9XCIsIFwiLT1cIiwgXCIqPVwiLCBcIi89XCIsIFwiJT1cIiwgXCI8XCIsIFwiPlwiLCBcIj48XCJdIGFzIGNvbnN0O1xyXG5leHBvcnQgdHlwZSBNYXRoT3BlcmF0aW9uID0gdHlwZW9mIE1hdGhPcGVyYXRpb25zW251bWJlcl07XHJcblxyXG5leHBvcnQgY2xhc3MgTWF0aE9wZXJhdGlvbkFyZ3VtZW50IGltcGxlbWVudHMgQXJndW1lbnRUeXBlPE1hdGhPcGVyYXRpb24+IHtcclxuXHJcblx0cHVibGljIHBhcnNlKHJlYWRlcjogU3RyaW5nUmVhZGVyKTogTWF0aE9wZXJhdGlvbiB7XHJcblx0XHRpZiAocmVhZGVyLmNhblJlYWQoKSkge1xyXG5cdFx0XHRsZXQgc3RhcnQ6IG51bWJlciA9IHJlYWRlci5nZXRDdXJzb3IoKTtcclxuXHRcdFx0d2hpbGUgKHJlYWRlci5jYW5SZWFkKCkgJiYgcmVhZGVyLnBlZWsoKSAhPSAnICcpIHtcclxuXHRcdFx0XHRyZWFkZXIuc2tpcCgpO1xyXG5cdFx0XHR9XHJcblx0XHRcdGNvbnN0IG1hdGhPcGVyYXRpb24gPSByZWFkZXIuZ2V0U3RyaW5nKCkuc3Vic3RyaW5nKHN0YXJ0LCByZWFkZXIuZ2V0Q3Vyc29yKCkpO1xyXG5cdFx0XHRpZihNYXRoT3BlcmF0aW9ucy5pbmNsdWRlcyhtYXRoT3BlcmF0aW9uIGFzIE1hdGhPcGVyYXRpb24pKSB7XHJcblx0XHRcdFx0cmV0dXJuIG1hdGhPcGVyYXRpb24gYXMgTWF0aE9wZXJhdGlvbjtcclxuXHRcdFx0fVxyXG5cdFx0fVxyXG5cdFx0dGhyb3cgbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKG5ldyBMaXRlcmFsTWVzc2FnZShcIkludmFsaWQgb3BlcmF0aW9uXCIpKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdH1cclxuXHJcblx0cHVibGljIGxpc3RTdWdnZXN0aW9ucyhfY29udGV4dDogQ29tbWFuZENvbnRleHQ8YW55PiwgYnVpbGRlcjogU3VnZ2VzdGlvbnNCdWlsZGVyKTogUHJvbWlzZTxTdWdnZXN0aW9ucz4ge1xyXG5cdFx0cmV0dXJuIEhlbHBlclN1Z2dlc3Rpb25Qcm92aWRlci5zdWdnZXN0KFsuLi5NYXRoT3BlcmF0aW9uc10sIGJ1aWxkZXIpO1xyXG5cdH1cclxuXHJcblx0cHVibGljIGdldEV4YW1wbGVzKCk6IHN0cmluZ1tdIHtcclxuXHRcdHJldHVybiBbXCI9XCIsIFwiPlwiLCBcIjxcIl07XHJcblx0fVxyXG59XHJcblxyXG5leHBvcnQgY2xhc3MgTkJUQ29tcG91bmRBcmd1bWVudCBpbXBsZW1lbnRzIEFyZ3VtZW50VHlwZTxzdHJpbmc+IHtcclxuXHJcblx0cHVibGljIHBhcnNlKHJlYWRlcjogU3RyaW5nUmVhZGVyKTogc3RyaW5nIHtcclxuXHRcdHJldHVybiByZWFkZXIucmVhZE5CVCgpO1xyXG5cdH1cclxuXHJcblx0cHVibGljIGdldEV4YW1wbGVzKCk6IHN0cmluZ1tdIHtcclxuXHRcdHJldHVybiBbXCJ7fVwiLCBcIntmb289YmFyfVwiXTtcclxuXHR9XHJcbn1cclxuXHJcbmV4cG9ydCBjbGFzcyBSYW5nZUFyZ3VtZW50IGltcGxlbWVudHMgQXJndW1lbnRUeXBlPFtudW1iZXIsIG51bWJlcl0+IHtcclxuXHJcblx0cHJpdmF0ZSBhbGxvd0Zsb2F0czogYm9vbGVhbjtcclxuXHJcblx0Y29uc3RydWN0b3IoYWxsb3dGbG9hdHM6IGJvb2xlYW4pIHtcclxuXHRcdHRoaXMuYWxsb3dGbG9hdHMgPSBhbGxvd0Zsb2F0cztcclxuXHR9XHJcblxyXG5cdHB1YmxpYyBwYXJzZShyZWFkZXI6IFN0cmluZ1JlYWRlcik6IFtudW1iZXIsIG51bWJlcl0ge1xyXG5cdFx0cmV0dXJuIHJlYWRlci5yZWFkTWluTWF4Qm91bmRzKHRoaXMuYWxsb3dGbG9hdHMpO1xyXG5cdH1cclxuXHJcblx0cHVibGljIGdldEV4YW1wbGVzKCk6IHN0cmluZ1tdIHtcclxuXHRcdGlmKHRoaXMuYWxsb3dGbG9hdHMpIHtcclxuXHRcdFx0cmV0dXJuIFtcIjAuLjUuMlwiLCBcIjBcIiwgXCItNS40XCIsIFwiLTEwMC43Ni4uXCIsIFwiLi4xMDBcIl07XHJcblx0XHR9IGVsc2Uge1xyXG5cdFx0XHRyZXR1cm4gW1wiMC4uNVwiLCBcIjBcIiwgXCItNVwiLCBcIi0xMDAuLlwiLCBcIi4uMTAwXCJdO1xyXG5cdFx0fVxyXG5cdH1cclxufVxyXG5cclxudHlwZSBNb2RpZmllciA9IChyZWFkZXI6IFN0cmluZ1JlYWRlcikgPT4gdm9pZDtcclxudHlwZSBPcHRpb25zVHlwZSA9XHJcblx0XCJuYW1lXCIgfCBcImRpc3RhbmNlXCIgfCBcImxldmVsXCIgfCBcInhcIiB8IFwieVwiIHwgXCJ6XCIgfCBcImR4XCIgfCBcImR5XCIgfCBcImR6XCIgfFxyXG5cdFwieF9yb3RhdGlvblwiIHwgXCJ5X3JvdGF0aW9uXCIgfCBcImxpbWl0XCIgfCBcInNvcnRcIiB8IFwiZ2FtZW1vZGVcIiB8IFwidGVhbVwiIHxcclxuXHRcInR5cGVcIiB8IFwidGFnXCIgfCBcIm5idFwiIHwgXCJzY29yZXNcIiB8IFwiYWR2YW5jZW1lbnRzXCIgfCBcInByZWRpY2F0ZVwiO1xyXG5cclxuY29uc3QgZW50aXR5VHlwZXMgPSBbXHJcblx0XCJhcmVhX2VmZmVjdF9jbG91ZFwiLCBcImFybW9yX3N0YW5kXCIsIFwiYXJyb3dcIiwgXCJheG9sb3RsXCIsIFwiYmF0XCIsIFwiYmVlXCIsIFwiYmxhemVcIixcclxuXHRcImJvYXRcIiwgXCJjYXRcIiwgXCJjYXZlX3NwaWRlclwiLCBcImNoaWNrZW5cIiwgXCJjb2RcIiwgXCJjb3dcIiwgXCJjcmVlcGVyXCIsIFwiZG9scGhpblwiLFxyXG5cdFwiZG9ua2V5XCIsIFwiZHJhZ29uX2ZpcmViYWxsXCIsIFwiZHJvd25lZFwiLCBcImVsZGVyX2d1YXJkaWFuXCIsIFwiZW5kX2NyeXN0YWxcIiwgXCJlbmRlcl9kcmFnb25cIixcclxuXHRcImVuZGVybWFuXCIsIFwiZW5kZXJtaXRlXCIsIFwiZXZva2VyXCIsIFwiZXZva2VyX2ZhbmdzXCIsIFwiZXhwZXJpZW5jZV9vcmJcIiwgXCJleWVfb2ZfZW5kZXJcIixcclxuXHRcImZhbGxpbmdfYmxvY2tcIiwgXCJmaXJld29ya19yb2NrZXRcIiwgXCJmb3hcIiwgXCJnaGFzdFwiLCBcImdpYW50XCIsIFwiZ2xvd19pdGVtX2ZyYW1lXCIsXHJcblx0XCJnbG93X3NxdWlkXCIsIFwiZ29hdFwiLCBcImd1YXJkaWFuXCIsIFwiaG9nbGluXCIsIFwiaG9yc2VcIiwgXCJodXNrXCIsIFwiaWxsdXNpb25lclwiLCBcImlyb25fZ29sZW1cIixcclxuXHRcIml0ZW1cIiwgXCJpdGVtX2ZyYW1lXCIsIFwiZmlyZWJhbGxcIiwgXCJsZWFzaF9rbm90XCIsIFwibGlnaHRuaW5nX2JvbHRcIiwgXCJsbGFtYVwiLCBcImxsYW1hX3NwaXRcIixcclxuXHRcIm1hZ21hX2N1YmVcIiwgXCJtYXJrZXJcIiwgXCJtaW5lY2FydFwiLCBcImNoZXN0X21pbmVjYXJ0XCIsIFwiY29tbWFuZF9ibG9ja19taW5lY2FydFwiLFxyXG5cdFwiZnVybmFjZV9taW5lY2FydFwiLCBcImhvcHBlcl9taW5lY2FydFwiLCBcInNwYXduZXJfbWluZWNhcnRcIiwgXCJ0bnRfbWluZWNhcnRcIiwgXCJtdWxlXCIsXHJcblx0XCJtb29zaHJvb21cIiwgXCJvY2Vsb3RcIiwgXCJwYWludGluZ1wiLCBcInBhbmRhXCIsIFwicGFycm90XCIsIFwicGhhbnRvbVwiLCBcInBpZ1wiLCBcInBpZ2xpblwiLFxyXG5cdFwicGlnbGluX2JydXRlXCIsIFwicGlsbGFnZXJcIiwgXCJwb2xhcl9iZWFyXCIsIFwidG50XCIsIFwicHVmZmVyZmlzaFwiLCBcInJhYmJpdFwiLCBcInJhdmFnZXJcIixcclxuXHRcInNhbG1vblwiLCBcInNoZWVwXCIsIFwic2h1bGtlclwiLCBcInNodWxrZXJfYnVsbGV0XCIsIFwic2lsdmVyZmlzaFwiLCBcInNrZWxldG9uXCIsIFwic2tlbGV0b25faG9yc2VcIixcclxuXHRcInNsaW1lXCIsIFwic21hbGxfZmlyZWJhbGxcIiwgXCJzbm93X2dvbGVtXCIsIFwic25vd2JhbGxcIiwgXCJzcGVjdHJhbF9hcnJvd1wiLCBcInNwaWRlclwiLCBcInNxdWlkXCIsXHJcblx0XCJzdHJheVwiLCBcInN0cmlkZXJcIiwgXCJlZ2dcIiwgXCJlbmRlcl9wZWFybFwiLCBcImV4cGVyaWVuY2VfYm90dGxlXCIsIFwicG90aW9uXCIsIFwidHJpZGVudFwiLFxyXG5cdFwidHJhZGVyX2xsYW1hXCIsIFwidHJvcGljYWxfZmlzaFwiLCBcInR1cnRsZVwiLCBcInZleFwiLCBcInZpbGxhZ2VyXCIsIFwidmluZGljYXRvclwiLFxyXG5cdFwid2FuZGVyaW5nX3RyYWRlclwiLCBcIndpdGNoXCIsIFwid2l0aGVyXCIsIFwid2l0aGVyX3NrZWxldG9uXCIsIFwid2l0aGVyX3NrdWxsXCIsIFwid29sZlwiLFxyXG5cdFwiem9nbGluXCIsIFwiem9tYmllXCIsIFwiem9tYmllX2hvcnNlXCIsIFwiem9tYmllX3ZpbGxhZ2VyXCIsIFwiem9tYmlmaWVkX3BpZ2xpblwiLCBcInBsYXllclwiLCBcImZpc2hpbmdfYm9iYmVyXCJcclxuXSBhcyBjb25zdDtcclxudHlwZSBFbnRpdHlUeXBlID0gdHlwZW9mIGVudGl0eVR5cGVzW251bWJlcl07XHJcblxyXG5jb25zdCBFbnRpdHlUYWdzID0gW1xyXG5cdFwiYXJyb3dzXCIsIFwiYXhvbG90bF9hbHdheXNfaG9zdGlsZXNcIiwgXCJheG9sb3RsX2h1bnRfdGFyZ2V0c1wiLCBcImJlZWhpdmVfaW5oYWJpdG9yc1wiLFxyXG5cdFwiZnJlZXplX2h1cnRzX2V4dHJhX3R5cGVzXCIsIFwiZnJlZXplX2ltbXVuZV9lbnRpdHlfdHlwZXNcIiwgXCJmcm9nX2Zvb2RcIiwgXCJpbXBhY3RfcHJvamVjdGlsZXNcIixcclxuXHRcInBvd2Rlcl9zbm93X3dhbGthYmxlX21vYnNcIiwgXCJyYWlkZXJzXCIsIFwic2tlbGV0b25zXCJcclxuXSBhcyBjb25zdDtcclxuXHJcbi8vIEVmZmVjdGl2ZWx5IGEgZ2lhbnQgbWVyZ2Ugb2YgRW50aXR5QXJndW1lbnQsIEVudGl0eVNlbGVjdG9yUGFyc2VyIGFuZCBFbnRpdHlTZWxlY3Rvck9wdGlvbnNcclxuZXhwb3J0IGNsYXNzIEVudGl0eVNlbGVjdG9yQXJndW1lbnQgaW1wbGVtZW50cyBBcmd1bWVudFR5cGU8RW50aXR5U2VsZWN0b3JBcmd1bWVudD4ge1xyXG5cclxuXHQvLyBFbnRpdHlTZWxlY3RvclBhcnNlciBzdWdnZXN0aW9uc1xyXG5cdHByaXZhdGUgc3VnZ2VzdGlvbnM6IHN0cmluZ1tdO1xyXG5cdHByaXZhdGUgc3VnZ2VzdGlvbnNNb2RpZmllcjogKChidWlsZGVyOiBTdWdnZXN0aW9uc0J1aWxkZXIpID0+IHZvaWQpIHwgbnVsbCA9IG51bGw7XHJcblxyXG5cdC8vIEVudGl0eVNlbGVjdG9yUGFyc2VyICsgRW50aXR5U2VsZWN0b3JPcHRpb25zXHJcblx0cHJpdmF0ZSBlbnRpdHlVVUlEOiBzdHJpbmc7XHJcblx0cHJpdmF0ZSBpbmNsdWRlc0VudGl0aWVzOiBib29sZWFuO1xyXG5cdHByaXZhdGUgcGxheWVyTmFtZTogc3RyaW5nO1xyXG5cdHByaXZhdGUgbWF4UmVzdWx0czogbnVtYmVyO1xyXG5cdHByaXZhdGUgaXNMaW1pdGVkOiBib29sZWFuOyAvLyBUaGlzIG1pZ2h0IGJlIGlkZW50aWNhbCB0byB0aGUgc3ludGhldGljIGxpbWl0ZWRUb1BsYXllcnMsIHRyeSB1c2luZyB0aGlzIGluc3RlYWRcclxuXHJcblx0cHJpdmF0ZSBsaW1pdGVkVG9QbGF5ZXJzOiBib29sZWFuOyAvLyBwbGF5ZXJzIG9ubHk/XHJcblx0cHJpdmF0ZSBlbnRpdHlUeXBlOiBFbnRpdHlUeXBlO1xyXG5cclxuXHRwcml2YXRlIGN1cnJlbnRFbnRpdHk6IGJvb2xlYW47XHJcblx0cHJpdmF0ZSB3b3JsZExpbWl0ZWQ6IGJvb2xlYW47XHJcblx0cHJpdmF0ZSBvcmRlcjogc3RyaW5nOyAvLyBOb3QgQmlDb25zdW1lcjxWZWMsIExpc3Q8PyBleHRlbmRzIEVudGl0eT4+IGJlY2F1c2UgZWZmb3J0XHJcblx0cHJpdmF0ZSBpc1NvcnRlZDogYm9vbGVhbjsgLy8gSXMgdGhpcyBlbnRpdHkgc2VsZWN0b3Igc29ydGVkP1xyXG5cclxuXHRwcml2YXRlIHNlbGVjdHNHYW1lbW9kZTogYm9vbGVhbiA9IGZhbHNlO1xyXG5cdHByaXZhdGUgZXhjbHVkZXNHYW1lbW9kZTogYm9vbGVhbiA9IGZhbHNlO1xyXG5cclxuXHRwcml2YXRlIGhhc05hbWVFcXVhbHM6IGJvb2xlYW47XHJcblx0cHJpdmF0ZSBoYXNOYW1lTm90RXF1YWxzOiBib29sZWFuO1xyXG5cdHByaXZhdGUgaGFzU2NvcmVzOiBib29sZWFuOyBcclxuXHRwcml2YXRlIHR5cGVJbnZlcnNlOiBib29sZWFuOyAvLyBpc1R5cGVMaW1pdGVkSW52ZXJzZWx5IEFLQSBleGNsdWRlc0VudGl0eVR5cGVcclxuXHJcblx0Ly8gRW50aXR5QXJndW1lbnRcclxuXHRwcml2YXRlIHNpbmdsZTogYm9vbGVhbjtcclxuXHRwcml2YXRlIHBsYXllcnNPbmx5OiBib29sZWFuO1xyXG5cclxuXHRjb25zdHJ1Y3RvcihzaW5nbGU6IGJvb2xlYW4sIHBsYXllcnNPbmx5OiBib29sZWFuKSB7XHJcblx0XHR0aGlzLnNpbmdsZSA9IHNpbmdsZTtcclxuXHRcdHRoaXMucGxheWVyc09ubHkgPSBwbGF5ZXJzT25seTtcclxuXHR9XHJcblxyXG5cdHByaXZhdGUgc3VnZ2VzdGlvbkdlbmVyYXRvcihyZWFkZXI6IFN0cmluZ1JlYWRlciwgdHlwZTogT3B0aW9uc1R5cGUpOiBzdHJpbmdbXSB7XHJcblx0XHRsZXQgc3VnZ2VzdGlvbnM6IHN0cmluZ1tdID0gW107XHJcblx0XHRzd2l0Y2godHlwZSkge1xyXG5cdFx0XHRjYXNlIFwiZ2FtZW1vZGVcIjoge1xyXG5cdFx0XHRcdGxldCBzdHJpbmc6IHN0cmluZyA9IHJlYWRlci5nZXRSZW1haW5pbmcoKS50b0xvd2VyQ2FzZSgpO1xyXG5cdFx0XHRcdGxldCBib29sOiBib29sZWFuID0gIXRoaXMuZXhjbHVkZXNHYW1lbW9kZTtcclxuXHRcdFx0XHRsZXQgbmVnYXRpbmc6IGJvb2xlYW4gPSB0cnVlO1xyXG5cdFx0XHRcdGlmKHN0cmluZy5sZW5ndGggIT09IDApIHtcclxuXHRcdFx0XHRcdGlmKHN0cmluZy5jaGFyQXQoMCkgPT09IFwiIVwiKSB7XHJcblx0XHRcdFx0XHRcdGJvb2wgPSBmYWxzZTtcclxuXHRcdFx0XHRcdFx0c3RyaW5nID0gc3RyaW5nLnNsaWNlKDEpO1xyXG5cdFx0XHRcdFx0fSBlbHNlIHtcclxuXHRcdFx0XHRcdFx0bmVnYXRpbmcgPSBmYWxzZTtcclxuXHRcdFx0XHRcdH1cclxuXHRcdFx0XHR9XHJcblxyXG5cdFx0XHRcdGZvcihsZXQgZ2FtZW1vZGUgb2YgW1wic3Vydml2YWxcIiwgXCJjcmVhdGl2ZVwiLCBcImFkdmVudHVyZVwiLCBcInNwZWN0YXRvclwiXSkge1xyXG5cdFx0XHRcdFx0aWYoIWdhbWVtb2RlLnRvTG93ZXJDYXNlKCkuc3RhcnRzV2l0aChzdHJpbmcpKSB7XHJcblx0XHRcdFx0XHRcdGNvbnRpbnVlO1xyXG5cdFx0XHRcdFx0fVxyXG5cdFx0XHRcdFx0aWYobmVnYXRpbmcpIHtcclxuXHRcdFx0XHRcdFx0c3VnZ2VzdGlvbnMucHVzaChcIiFcIiArIGdhbWVtb2RlKTtcclxuXHRcdFx0XHRcdH1cclxuXHRcdFx0XHRcdGlmKCFib29sKSB7XHJcblx0XHRcdFx0XHRcdGNvbnRpbnVlO1xyXG5cdFx0XHRcdFx0fVxyXG5cdFx0XHRcdFx0c3VnZ2VzdGlvbnMucHVzaChnYW1lbW9kZSk7XHJcblx0XHRcdFx0fVxyXG5cdFx0XHRcdGJyZWFrO1xyXG5cdFx0XHR9XHJcblx0XHRcdGNhc2UgXCJzb3J0XCI6IHtcclxuXHRcdFx0XHRzdWdnZXN0aW9ucy5wdXNoKC4uLlN1Z2dlc3Rpb25zSGVscGVyLnN1Z2dlc3RNYXRjaGluZyhyZWFkZXIsIFtcIm5lYXJlc3RcIiwgXCJmdXJ0aGVzdFwiLCBcInJhbmRvbVwiLCBcImFyYml0cmFyeVwiXSkpO1xyXG5cdFx0XHRcdGJyZWFrO1xyXG5cdFx0XHR9XHJcblx0XHRcdGNhc2UgXCJ0eXBlXCI6IHtcclxuXHRcdFx0XHRzdWdnZXN0aW9ucy5wdXNoKC4uLlN1Z2dlc3Rpb25zSGVscGVyLnN1Z2dlc3RNYXRjaGluZyhyZWFkZXIsIGVudGl0eVR5cGVzLm1hcChlbnRpdHkgPT4gYCEke2VudGl0eX1gKSkpO1xyXG5cdFx0XHRcdHN1Z2dlc3Rpb25zLnB1c2goLi4uU3VnZ2VzdGlvbnNIZWxwZXIuc3VnZ2VzdE1hdGNoaW5nKHJlYWRlciwgRW50aXR5VGFncy5tYXAoZW50aXR5ID0+IGAhIyR7ZW50aXR5fWApKSk7XHJcblx0XHRcdFx0aWYoIXRoaXMudHlwZUludmVyc2UpIHtcclxuXHRcdFx0XHRcdHN1Z2dlc3Rpb25zLnB1c2goLi4uU3VnZ2VzdGlvbnNIZWxwZXIuc3VnZ2VzdE1hdGNoaW5nKHJlYWRlciwgWy4uLmVudGl0eVR5cGVzXSkpO1xyXG5cdFx0XHRcdFx0c3VnZ2VzdGlvbnMucHVzaCguLi5TdWdnZXN0aW9uc0hlbHBlci5zdWdnZXN0TWF0Y2hpbmcocmVhZGVyLCBFbnRpdHlUYWdzLm1hcChlbnRpdHkgPT4gYCMke2VudGl0eX1gKSkpO1xyXG5cdFx0XHRcdH1cclxuXHRcdFx0XHRicmVhaztcclxuXHRcdFx0fVxyXG5cdFx0fVxyXG5cdFx0cmV0dXJuIHN1Z2dlc3Rpb25zO1xyXG5cdH1cclxuXHJcblx0cHJpdmF0ZSByZWFkb25seSBPcHRpb25zOiB7IFtvcHRpb24gaW4gT3B0aW9uc1R5cGVdOiBNb2RpZmllciB9ID0ge1xyXG5cdFx0bmFtZTogKHJlYWRlcjogU3RyaW5nUmVhZGVyKTogdm9pZCA9PiB7XHJcblx0XHRcdGNvbnN0IHN0YXJ0OiBudW1iZXIgPSByZWFkZXIuZ2V0Q3Vyc29yKCk7XHJcblx0XHRcdGNvbnN0IHNob3VsZEludmVydDogYm9vbGVhbiA9IHJlYWRlci5yZWFkTmVnYXRpb25DaGFyYWN0ZXIoKTtcclxuXHRcdFx0Y29uc3QgX3M6IHN0cmluZyA9IHJlYWRlci5yZWFkU3RyaW5nKCk7XHJcblx0XHRcdGlmKHRoaXMuaGFzTmFtZU5vdEVxdWFscyAmJiAhc2hvdWxkSW52ZXJ0KSB7XHJcblx0XHRcdFx0cmVhZGVyLnNldEN1cnNvcihzdGFydCk7XHJcblx0XHRcdFx0dGhyb3cgbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKG5ldyBMaXRlcmFsTWVzc2FnZShgT3B0aW9uICduYW1lJyBpc24ndCBhcHBsaWNhYmxlIGhlcmVgKSkuY3JlYXRlV2l0aENvbnRleHQocmVhZGVyKTtcclxuXHRcdFx0fVxyXG5cdFx0XHRpZihzaG91bGRJbnZlcnQpIHtcclxuXHRcdFx0XHR0aGlzLmhhc05hbWVOb3RFcXVhbHMgPSB0cnVlO1xyXG5cdFx0XHR9IGVsc2Uge1xyXG5cdFx0XHRcdHRoaXMuaGFzTmFtZUVxdWFscyA9IHRydWU7XHJcblx0XHRcdH1cclxuXHRcdH0sXHJcblx0XHRkaXN0YW5jZTogKF9yZWFkZXI6IFN0cmluZ1JlYWRlcik6IHZvaWQgPT4ge30sXHJcblx0XHRsZXZlbDogKF9yZWFkZXI6IFN0cmluZ1JlYWRlcik6IHZvaWQgPT4ge30sXHJcblx0XHR4OiAocmVhZGVyOiBTdHJpbmdSZWFkZXIpOiB2b2lkID0+IHsgcmVhZGVyLnJlYWRGbG9hdCgpIH0sXHJcblx0XHR5OiAocmVhZGVyOiBTdHJpbmdSZWFkZXIpOiB2b2lkID0+IHsgcmVhZGVyLnJlYWRGbG9hdCgpIH0sXHJcblx0XHR6OiAocmVhZGVyOiBTdHJpbmdSZWFkZXIpOiB2b2lkID0+IHsgcmVhZGVyLnJlYWRGbG9hdCgpIH0sXHJcblx0XHRkeDogKHJlYWRlcjogU3RyaW5nUmVhZGVyKTogdm9pZCA9PiB7IHJlYWRlci5yZWFkRmxvYXQoKSB9LFxyXG5cdFx0ZHk6IChyZWFkZXI6IFN0cmluZ1JlYWRlcik6IHZvaWQgPT4geyByZWFkZXIucmVhZEZsb2F0KCkgfSxcclxuXHRcdGR6OiAocmVhZGVyOiBTdHJpbmdSZWFkZXIpOiB2b2lkID0+IHsgcmVhZGVyLnJlYWRGbG9hdCgpIH0sXHJcblx0XHR4X3JvdGF0aW9uOiAocmVhZGVyOiBTdHJpbmdSZWFkZXIpOiB2b2lkID0+IHsgcmVhZGVyLnJlYWRNaW5NYXhCb3VuZHModHJ1ZSkgfSxcclxuXHRcdHlfcm90YXRpb246IChyZWFkZXI6IFN0cmluZ1JlYWRlcik6IHZvaWQgPT4geyByZWFkZXIucmVhZE1pbk1heEJvdW5kcyh0cnVlKSB9LFxyXG5cdFx0bGltaXQ6IChyZWFkZXI6IFN0cmluZ1JlYWRlcik6IHZvaWQgPT4ge1xyXG5cdFx0XHRjb25zdCBzdGFydDogbnVtYmVyID0gcmVhZGVyLmdldEN1cnNvcigpO1xyXG5cdFx0XHRjb25zdCBsaW1pdDogbnVtYmVyID0gcmVhZGVyLnJlYWRJbnQoKTtcclxuXHRcdFx0aWYobGltaXQgPCAxKSB7XHJcblx0XHRcdFx0cmVhZGVyLnNldEN1cnNvcihzdGFydCk7XHJcblx0XHRcdFx0dGhyb3cgbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKG5ldyBMaXRlcmFsTWVzc2FnZShgTGltaXQgbXVzdCBiZSBhdCBsZWFzdCAxYCkpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlcik7XHJcblx0XHRcdH1cclxuXHRcdFx0dGhpcy5tYXhSZXN1bHRzID0gbGltaXQ7XHJcblx0XHRcdHRoaXMuaXNMaW1pdGVkID0gdHJ1ZTtcclxuXHRcdH0sXHJcblx0XHRzb3J0OiAocmVhZGVyOiBTdHJpbmdSZWFkZXIpOiB2b2lkID0+IHtcclxuXHRcdFx0dGhpcy5zdWdnZXN0aW9ucyA9IHRoaXMuc3VnZ2VzdGlvbkdlbmVyYXRvcihyZWFkZXIsIFwic29ydFwiKTtcclxuXHRcdFx0Y29uc3Qgc3RhcnQ6IG51bWJlciA9IHJlYWRlci5nZXRDdXJzb3IoKTtcclxuXHRcdFx0Y29uc3Qgc29ydFR5cGU6IHN0cmluZyA9IHJlYWRlci5yZWFkVW5xdW90ZWRTdHJpbmcoKTsgLy8gd29yZFxyXG5cdFx0XHRpZihbXCJuZWFyZXN0XCIsIFwiZnVydGhlc3RcIiwgXCJyYW5kb21cIiwgXCJhcmJpdHJhcnlcIl0uaW5jbHVkZXMoc29ydFR5cGUpKSB7XHJcblx0XHRcdFx0dGhpcy5vcmRlciA9IHNvcnRUeXBlO1xyXG5cdFx0XHRcdHRoaXMuaXNTb3J0ZWQgPSB0cnVlO1xyXG5cdFx0XHR9IGVsc2Uge1xyXG5cdFx0XHRcdHJlYWRlci5zZXRDdXJzb3Ioc3RhcnQpO1xyXG5cdFx0XHRcdHRocm93IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZShuZXcgTGl0ZXJhbE1lc3NhZ2UoYEludmFsaWQgb3IgdW5rbm93biBzb3J0IHR5cGUgJyR7c29ydFR5cGV9J2ApKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdFx0XHR9XHJcblx0XHR9LFxyXG5cdFx0Z2FtZW1vZGU6IChyZWFkZXI6IFN0cmluZ1JlYWRlcik6IHZvaWQgPT4ge1xyXG5cdFx0XHR0aGlzLnN1Z2dlc3Rpb25zID0gdGhpcy5zdWdnZXN0aW9uR2VuZXJhdG9yKHJlYWRlciwgXCJnYW1lbW9kZVwiKTtcclxuXHRcdFx0Y29uc3Qgc3RhcnQ6IG51bWJlciA9IHJlYWRlci5nZXRDdXJzb3IoKTtcclxuXHRcdFx0Y29uc3Qgc2hvdWxkSW52ZXJ0OiBib29sZWFuID0gcmVhZGVyLnJlYWROZWdhdGlvbkNoYXJhY3RlcigpO1xyXG5cdFx0XHRpZih0aGlzLmV4Y2x1ZGVzR2FtZW1vZGUgJiYgIXNob3VsZEludmVydCkge1xyXG5cdFx0XHRcdHJlYWRlci5zZXRDdXJzb3Ioc3RhcnQpO1xyXG5cdFx0XHRcdHRocm93IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZShuZXcgTGl0ZXJhbE1lc3NhZ2UoYE9wdGlvbiAnZ2FtZW1vZGUnIGlzbid0IGFwcGxpY2FibGUgaGVyZWApKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdFx0XHR9XHJcblx0XHRcdGNvbnN0IGdhbWVtb2RlOiBzdHJpbmcgPSByZWFkZXIucmVhZFVucXVvdGVkU3RyaW5nKCk7XHJcblx0XHRcdGlmKCFbXCJzdXJ2aXZhbFwiLCBcImNyZWF0aXZlXCIsIFwiYWR2ZW50dXJlXCIsIFwic3BlY3RhdG9yXCJdLmluY2x1ZGVzKGdhbWVtb2RlKSkge1xyXG5cdFx0XHRcdHJlYWRlci5zZXRDdXJzb3Ioc3RhcnQpO1xyXG5cdFx0XHRcdHRocm93IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZShuZXcgTGl0ZXJhbE1lc3NhZ2UoYEludmFsaWQgb3IgdW5rbm93biBnYW1lIG1vZGUgJyR7Z2FtZW1vZGV9J2ApKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdFx0XHR9IGVsc2Uge1xyXG5cdFx0XHRcdHRoaXMuaW5jbHVkZXNFbnRpdGllcyA9IGZhbHNlO1xyXG5cdFx0XHRcdGlmKHNob3VsZEludmVydCkge1xyXG5cdFx0XHRcdFx0dGhpcy5leGNsdWRlc0dhbWVtb2RlID0gdHJ1ZTtcclxuXHRcdFx0XHR9IGVsc2Uge1xyXG5cdFx0XHRcdFx0dGhpcy5zZWxlY3RzR2FtZW1vZGUgPSB0cnVlO1xyXG5cdFx0XHRcdH1cclxuXHRcdFx0fVxyXG5cdFx0fSxcclxuXHRcdHRlYW06IChyZWFkZXI6IFN0cmluZ1JlYWRlcik6IHZvaWQgPT4ge1xyXG5cdFx0XHRyZWFkZXIucmVhZE5lZ2F0aW9uQ2hhcmFjdGVyKCk7XHJcblx0XHRcdHJlYWRlci5yZWFkVW5xdW90ZWRTdHJpbmcoKTtcclxuXHRcdH0sXHJcblx0XHR0eXBlOiAocmVhZGVyOiBTdHJpbmdSZWFkZXIpOiB2b2lkID0+IHtcclxuXHRcdFx0dGhpcy5zdWdnZXN0aW9ucyA9IHRoaXMuc3VnZ2VzdGlvbkdlbmVyYXRvcihyZWFkZXIsIFwidHlwZVwiKTtcclxuXHRcdFx0bGV0IHN0YXJ0OiBudW1iZXIgPSByZWFkZXIuZ2V0Q3Vyc29yKCk7XHJcblx0XHRcdGxldCBzaG91bGRJbnZlcnQ6IGJvb2xlYW4gPSByZWFkZXIucmVhZE5lZ2F0aW9uQ2hhcmFjdGVyKCk7XHJcblx0XHRcdGlmKHRoaXMudHlwZUludmVyc2UgJiYgIXNob3VsZEludmVydCkge1xyXG5cdFx0XHRcdHJlYWRlci5zZXRDdXJzb3Ioc3RhcnQpO1xyXG5cdFx0XHRcdHRocm93IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZShuZXcgTGl0ZXJhbE1lc3NhZ2UoYE9wdGlvbiAndHlwZScgaXNuJ3QgYXBwbGljYWJsZSBoZXJlYCkpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlcik7XHJcblx0XHRcdH1cclxuXHRcdFx0aWYoc2hvdWxkSW52ZXJ0KSB7XHJcblx0XHRcdFx0dGhpcy50eXBlSW52ZXJzZSA9IHRydWU7XHJcblx0XHRcdH1cclxuXHRcdFx0aWYocmVhZGVyLnJlYWRUYWdDaGFyYWN0ZXIoKSkge1xyXG5cdFx0XHRcdC8vIFlheSwgd2UndmUgZ290IGEgdGFnLiBSZWFkIGV2ZXJ5dGhpbmcgbWF0Y2hpbmcgMCAtIDksIGEgLSB6LCBfLCA6LCAvLCAuIG9yIC1cclxuXHRcdFx0XHRyZWFkZXIucmVhZFJlc291cmNlTG9jYXRpb24oKTtcclxuXHRcdFx0fSBlbHNlIHtcclxuXHRcdFx0XHRjb25zdCBbbmFtZXNwYWNlLCBrZXldOiBbc3RyaW5nLCBzdHJpbmddID0gcmVhZGVyLnJlYWRSZXNvdXJjZUxvY2F0aW9uKCk7XHJcblx0XHRcdFx0aWYoIWVudGl0eVR5cGVzLmluY2x1ZGVzKGtleSBhcyBFbnRpdHlUeXBlKSkgeyAvLyBVaC4uLiB5ZXMuLi4gdGhpcyB0b3RhbGx5IHdvbid0IGZhaWwuLi5cclxuXHRcdFx0XHRcdHRocm93IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZShuZXcgTGl0ZXJhbE1lc3NhZ2UoYCR7a2V5fSBpcyBub3QgYSB2YWxpZCBlbnRpdHkgdHlwZWApKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdFx0XHRcdH1cclxuXHRcdFx0XHRpZihcInBsYXllclwiID09PSAoa2V5IGFzIEVudGl0eVR5cGUpICYmICFzaG91bGRJbnZlcnQgKSB7XHJcblx0XHRcdFx0XHR0aGlzLmluY2x1ZGVzRW50aXRpZXMgPSB0cnVlO1xyXG5cdFx0XHRcdH1cclxuXHRcdFx0XHRpZighc2hvdWxkSW52ZXJ0KSB7XHJcblx0XHRcdFx0XHR0aGlzLmVudGl0eVR5cGUgPSBrZXkgYXMgRW50aXR5VHlwZTtcclxuXHRcdFx0XHR9XHJcblx0XHRcdH1cclxuXHRcdH0sXHJcblx0XHR0YWc6IChyZWFkZXI6IFN0cmluZ1JlYWRlcik6IHZvaWQgPT4ge1xyXG5cdFx0XHRyZWFkZXIucmVhZE5lZ2F0aW9uQ2hhcmFjdGVyKCk7XHJcblx0XHRcdHJlYWRlci5yZWFkVW5xdW90ZWRTdHJpbmcoKTtcclxuXHRcdH0sXHJcblx0XHRuYnQ6IChyZWFkZXI6IFN0cmluZ1JlYWRlcik6IHZvaWQgPT4ge1xyXG5cdFx0XHRjb25zdCBzdGFydDogbnVtYmVyID0gcmVhZGVyLmdldEN1cnNvcigpO1xyXG5cdFx0XHRjb25zdCBzaG91bGRJbnZlcnQ6IGJvb2xlYW4gPSByZWFkZXIucmVhZE5lZ2F0aW9uQ2hhcmFjdGVyKCk7XHJcblx0XHRcdHRyeSB7XHJcblx0XHRcdFx0cmVhZGVyLnJlYWROQlQoKTtcclxuXHRcdFx0fSBjYXRjaChlcnJvcikge1xyXG5cdFx0XHRcdHJlYWRlci5zZXRDdXJzb3Ioc3RhcnQpO1xyXG5cdFx0XHRcdHRocm93IGVycm9yO1xyXG5cdFx0XHR9XHJcblx0XHR9LFxyXG5cdFx0c2NvcmVzOiAocmVhZGVyOiBTdHJpbmdSZWFkZXIpOiB2b2lkID0+IHtcclxuXHRcdFx0Ly8gQGVbc2NvcmVzPXtmb289MTAsYmFyPTEuLjV9XVxyXG5cdFx0XHRyZWFkZXIuZXhwZWN0KFwie1wiKTtcclxuXHRcdFx0cmVhZGVyLnNraXBXaGl0ZXNwYWNlKCk7XHJcblx0XHRcdHdoaWxlIChyZWFkZXIuY2FuUmVhZCgpICYmIHJlYWRlci5wZWVrKCkgIT0gJ30nKSB7XHJcblx0XHRcdFx0cmVhZGVyLnNraXBXaGl0ZXNwYWNlKCk7XHJcblx0XHRcdFx0Y29uc3Qgc3RyOiBzdHJpbmcgPSByZWFkZXIucmVhZFVucXVvdGVkU3RyaW5nKCk7XHJcblx0XHRcdFx0cmVhZGVyLnNraXBXaGl0ZXNwYWNlKCk7XHJcblx0XHRcdFx0cmVhZGVyLmV4cGVjdCgnPScpO1xyXG5cdFx0XHRcdHJlYWRlci5za2lwV2hpdGVzcGFjZSgpO1xyXG5cdFx0XHRcdHJlYWRlci5yZWFkTWluTWF4Qm91bmRzKGZhbHNlKTtcclxuXHRcdFx0XHRyZWFkZXIuc2tpcFdoaXRlc3BhY2UoKTtcclxuXHRcdFx0XHRpZiAocmVhZGVyLmNhblJlYWQoKSAmJiByZWFkZXIucGVlaygpID09ICcsJykge1xyXG5cdFx0XHRcdFx0cmVhZGVyLnNraXAoKTsgXHJcblx0XHRcdFx0fVxyXG5cdFx0XHR9XHJcblx0XHRcdHJlYWRlci5leHBlY3QoJ30nKTtcclxuXHRcdFx0dGhpcy5oYXNTY29yZXMgPSB0cnVlO1xyXG5cdFx0fSxcclxuXHRcdGFkdmFuY2VtZW50czogKHJlYWRlcjogU3RyaW5nUmVhZGVyKTogdm9pZCA9PiB7XHJcblx0XHRcdHRocm93IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZShuZXcgTGl0ZXJhbE1lc3NhZ2UoXCJUaGlzIGNvbW1hbmQgdmlzdWFsaXplciBkb2Vzbid0IHN1cHBvcnQgJ2FkdmFuY2VtZW50cydcIikpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlcik7IFxyXG5cdFx0fSxcclxuXHRcdHByZWRpY2F0ZTogKHJlYWRlcjogU3RyaW5nUmVhZGVyKTogdm9pZCA9PiB7XHJcblx0XHRcdHRocm93IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZShuZXcgTGl0ZXJhbE1lc3NhZ2UoXCJUaGlzIGNvbW1hbmQgdmlzdWFsaXplciBkb2Vzbid0IHN1cHBvcnQgJ3ByZWRpY2F0ZSdcIikpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlcik7XHJcblx0XHR9XHJcblx0fSBhcyBjb25zdDtcclxuXHJcblx0cHVibGljIHBhcnNlKHJlYWRlcjogU3RyaW5nUmVhZGVyKTogRW50aXR5U2VsZWN0b3JBcmd1bWVudCB7XHJcblxyXG5cdFx0Y29uc3QgcGFyc2VPcHRpb25zOiAoKSA9PiB2b2lkID0gKCkgPT4ge1xyXG5cdFx0XHR0aGlzLnN1Z2dlc3Rpb25zID0gU3VnZ2VzdGlvbnNIZWxwZXIuc3VnZ2VzdE1hdGNoaW5nKHJlYWRlciwgWy4uLk9iamVjdC5rZXlzKHRoaXMuT3B0aW9ucykubWFwKHggPT4gYCR7eH09YCldKTsgLy8gVE9ETzogU28gdGhpcyBpc24ndCBleGFjdGx5IGNvcnJlY3QsIHdlIG5lZWQgdG8gbm90IGxpc3QgZXhpc3RpbmcgbmFtZXMsIGJ1dCB0aGF0J2xsIHJlcXVpcmUgYSBiaXQgb2YgYSByZWZhY3RvclxyXG5cdFx0XHRyZWFkZXIuc2tpcFdoaXRlc3BhY2UoKTtcclxuXHRcdFx0d2hpbGUocmVhZGVyLmNhblJlYWQoKSAmJiByZWFkZXIucGVlaygpICE9PSBcIl1cIikge1xyXG5cdFx0XHRcdHJlYWRlci5za2lwV2hpdGVzcGFjZSgpO1xyXG5cdFx0XHRcdGxldCBzdGFydDogbnVtYmVyID0gcmVhZGVyLmdldEN1cnNvcigpO1xyXG5cdFx0XHRcdGxldCBvcHRpb25zVHlwZTogT3B0aW9uc1R5cGUgPSByZWFkZXIucmVhZFN0cmluZygpIGFzIE9wdGlvbnNUeXBlO1xyXG5cclxuXHRcdFx0XHRsZXQgbW9kaWZpZXI6IE1vZGlmaWVyID0gdGhpcy5PcHRpb25zW29wdGlvbnNUeXBlXTtcclxuXHRcdFx0XHRpZihtb2RpZmllciA9PT0gbnVsbCkge1xyXG5cdFx0XHRcdFx0cmVhZGVyLnNldEN1cnNvcihzdGFydCk7XHJcblx0XHRcdFx0XHR0aHJvdyBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGUobmV3IExpdGVyYWxNZXNzYWdlKGBVbmtub3duIG9wdGlvbiAnJHtvcHRpb25zVHlwZX0nYCkpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlcik7XHJcblx0XHRcdFx0fVxyXG5cclxuXHRcdFx0XHRyZWFkZXIuc2tpcFdoaXRlc3BhY2UoKTtcclxuXHRcdFx0XHRpZighcmVhZGVyLmNhblJlYWQoKSB8fCByZWFkZXIucGVlaygpICE9PSBcIj1cIikge1xyXG5cdFx0XHRcdFx0cmVhZGVyLnNldEN1cnNvcihzdGFydCk7XHJcblx0XHRcdFx0XHR0aHJvdyBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGUobmV3IExpdGVyYWxNZXNzYWdlKGBFeHBlY3RlZCB2YWx1ZSBmb3Igb3B0aW9uICcke29wdGlvbnNUeXBlfSdgKSkuY3JlYXRlV2l0aENvbnRleHQocmVhZGVyKTtcclxuXHRcdFx0XHR9XHJcblx0XHRcdFx0cmVhZGVyLnNraXAoKTtcclxuXHRcdFx0XHRyZWFkZXIuc2tpcFdoaXRlc3BhY2UoKTtcclxuXHRcdFx0XHR0aGlzLnN1Z2dlc3Rpb25zID0gW107XHJcblx0XHRcdFx0bW9kaWZpZXIocmVhZGVyKTtcclxuXHRcdFx0XHRyZWFkZXIuc2tpcFdoaXRlc3BhY2UoKTtcclxuXHRcdFx0XHR0aGlzLnN1Z2dlc3Rpb25zID0gW1wiLFwiLCBcIl1cIl07XHJcblx0XHRcdFx0aWYoIXJlYWRlci5jYW5SZWFkKCkpIHtcclxuXHRcdFx0XHRcdGNvbnRpbnVlO1xyXG5cdFx0XHRcdH1cclxuXHRcdFx0XHRpZihyZWFkZXIucGVlaygpID09PSBcIixcIikge1xyXG5cdFx0XHRcdFx0cmVhZGVyLnNraXAoKTtcclxuXHRcdFx0XHRcdHRoaXMuc3VnZ2VzdGlvbnMgPSBTdWdnZXN0aW9uc0hlbHBlci5zdWdnZXN0TWF0Y2hpbmcocmVhZGVyLCBbLi4uT2JqZWN0LmtleXModGhpcy5PcHRpb25zKV0ubWFwKHggPT4gYCR7eH09YCkpOyAvLyBUT0RPOiBTbyB0aGlzIGlzbid0IGV4YWN0bHkgY29ycmVjdCwgd2UgbmVlZCB0byBub3QgbGlzdCBleGlzdGluZyBuYW1lcywgYnV0IHRoYXQnbGwgcmVxdWlyZSBhIGJpdCBvZiBhIHJlZmFjdG9yXHJcblx0XHRcdFx0XHRjb250aW51ZTtcclxuXHRcdFx0XHR9XHJcblx0XHRcdFx0aWYocmVhZGVyLnBlZWsoKSAhPT0gXCJdXCIpIHtcclxuXHRcdFx0XHRcdHRocm93IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZShuZXcgTGl0ZXJhbE1lc3NhZ2UoXCJFeHBlY3RlZCBlbmQgb2Ygb3B0aW9uc1wiKSkuY3JlYXRlV2l0aENvbnRleHQocmVhZGVyKTtcclxuXHRcdFx0XHR9XHJcblx0XHRcdH1cclxuXHRcdFx0aWYgKHJlYWRlci5jYW5SZWFkKCkpIHtcclxuXHRcdFx0XHRyZWFkZXIuc2tpcCgpO1xyXG5cdFx0XHRcdHRoaXMuc3VnZ2VzdGlvbnMgPSBbXTtcclxuXHRcdFx0XHRyZXR1cm47XHJcblx0XHRcdH1cclxuXHRcdFx0dGhyb3cgbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKG5ldyBMaXRlcmFsTWVzc2FnZShcIkV4cGVjdGVkIGVuZCBvZiBvcHRpb25zXCIpKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdFx0fVxyXG5cclxuXHRcdGNvbnN0IHBhcnNlU2VsZWN0b3I6ICgpID0+IHZvaWQgPSAoKSA9PiB7XHJcblx0XHRcdHRoaXMuc3VnZ2VzdGlvbnNNb2RpZmllciA9IChidWlsZGVyOiBTdWdnZXN0aW9uc0J1aWxkZXIpID0+IHsgYnVpbGRlci5jcmVhdGVPZmZzZXQoYnVpbGRlci5nZXRTdGFydCgpIC0gMSkgfTtcclxuXHRcdFx0dGhpcy5zdWdnZXN0aW9ucyA9IFtcIkBwXCIsIFwiQGFcIiwgXCJAclwiLCBcIkBzXCIsIFwiQGVcIl07XHJcblx0XHRcdGlmKCFyZWFkZXIuY2FuUmVhZCgpKSB7XHJcblx0XHRcdFx0dGhyb3cgbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKG5ldyBMaXRlcmFsTWVzc2FnZShcIk1pc3Npbmcgc2VsZWN0b3IgdHlwZVwiKSkuY3JlYXRlV2l0aENvbnRleHQocmVhZGVyKTtcclxuXHRcdFx0fVxyXG5cclxuXHRcdFx0bGV0IHN0YXJ0OiBudW1iZXIgPSByZWFkZXIuZ2V0Q3Vyc29yKCk7XHJcblx0XHRcdGxldCBzZWxlY3RvckNoYXI6IHN0cmluZyA9IHJlYWRlci5yZWFkKCk7XHJcblx0XHRcdHN3aXRjaChzZWxlY3RvckNoYXIpIHtcclxuXHRcdFx0XHRjYXNlIFwicFwiOlxyXG5cdFx0XHRcdFx0dGhpcy5tYXhSZXN1bHRzID0gMTtcclxuXHRcdFx0XHRcdHRoaXMuaW5jbHVkZXNFbnRpdGllcyA9IGZhbHNlO1xyXG5cdFx0XHRcdFx0dGhpcy5saW1pdGVkVG9QbGF5ZXJzID0gdHJ1ZTtcclxuXHRcdFx0XHRcdGJyZWFrO1xyXG5cdFx0XHRcdGNhc2UgXCJhXCI6XHJcblx0XHRcdFx0XHR0aGlzLm1heFJlc3VsdHMgPSBOdW1iZXIuTUFYX1NBRkVfSU5URUdFUjtcclxuXHRcdFx0XHRcdHRoaXMuaW5jbHVkZXNFbnRpdGllcyA9IGZhbHNlO1xyXG5cdFx0XHRcdFx0dGhpcy5saW1pdGVkVG9QbGF5ZXJzID0gdHJ1ZTtcclxuXHRcdFx0XHRcdGJyZWFrO1xyXG5cdFx0XHRcdGNhc2UgXCJyXCI6XHJcblx0XHRcdFx0XHR0aGlzLm1heFJlc3VsdHMgPSAxO1xyXG5cdFx0XHRcdFx0dGhpcy5pbmNsdWRlc0VudGl0aWVzID0gZmFsc2U7XHJcblx0XHRcdFx0XHR0aGlzLmxpbWl0ZWRUb1BsYXllcnMgPSB0cnVlO1xyXG5cdFx0XHRcdFx0YnJlYWs7XHJcblx0XHRcdFx0Y2FzZSBcInNcIjpcclxuXHRcdFx0XHRcdHRoaXMubWF4UmVzdWx0cyA9IDE7XHJcblx0XHRcdFx0XHR0aGlzLmluY2x1ZGVzRW50aXRpZXMgPSB0cnVlO1xyXG5cdFx0XHRcdFx0dGhpcy5jdXJyZW50RW50aXR5ID0gdHJ1ZTtcclxuXHRcdFx0XHRcdGJyZWFrO1xyXG5cdFx0XHRcdGNhc2UgXCJlXCI6XHJcblx0XHRcdFx0XHR0aGlzLm1heFJlc3VsdHMgPSBOdW1iZXIuTUFYX1NBRkVfSU5URUdFUjtcclxuXHRcdFx0XHRcdHRoaXMuaW5jbHVkZXNFbnRpdGllcyA9IHRydWU7XHJcblx0XHRcdFx0XHRicmVhaztcclxuXHRcdFx0XHRkZWZhdWx0OlxyXG5cdFx0XHRcdFx0cmVhZGVyLnNldEN1cnNvcihzdGFydCk7XHJcblx0XHRcdFx0XHR0aHJvdyBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGUobmV3IExpdGVyYWxNZXNzYWdlKGBVbmtub3duIHNlbGVjdG9yIHR5cGUgJyR7c2VsZWN0b3JDaGFyfSdgKSkuY3JlYXRlV2l0aENvbnRleHQocmVhZGVyKTtcclxuXHRcdFx0fVxyXG5cdFx0XHR0aGlzLnN1Z2dlc3Rpb25zTW9kaWZpZXIgPSBudWxsO1xyXG5cdFx0XHR0aGlzLnN1Z2dlc3Rpb25zID0gW1wiW1wiXTtcclxuXHRcdFx0aWYgKHJlYWRlci5jYW5SZWFkKCkgJiYgcmVhZGVyLnBlZWsoKSA9PT0gXCJbXCIpIHtcclxuXHRcdFx0XHRyZWFkZXIuc2tpcCgpO1xyXG5cdFx0XHRcdHRoaXMuc3VnZ2VzdGlvbnNNb2RpZmllciA9IG51bGw7XHJcblx0XHRcdFx0dGhpcy5zdWdnZXN0aW9ucyA9IFtcIl1cIiwgLi4uT2JqZWN0LmtleXModGhpcy5PcHRpb25zKS5tYXAoeCA9PiBgJHt4fT1gKV07IC8vIFRPRE86IFNvIHRoaXMgaXNuJ3QgZXhhY3RseSBjb3JyZWN0LCB3ZSBuZWVkIHRvIG5vdCBsaXN0IGV4aXN0aW5nIG5hbWVzLCBidXQgdGhhdCdsbCByZXF1aXJlIGEgYml0IG9mIGEgcmVmYWN0b3JcclxuXHRcdFx0XHRwYXJzZU9wdGlvbnMoKTtcclxuXHRcdFx0fVxyXG5cdFx0fVxyXG5cclxuXHRcdGNvbnN0IHBhcnNlTmFtZU9yVVVJRDogKCkgPT4gdm9pZCA9ICgpID0+IHtcclxuXHRcdFx0bGV0IGk6IG51bWJlciA9IHJlYWRlci5nZXRDdXJzb3IoKTtcclxuXHRcdFx0bGV0IHM6IHN0cmluZyA9IHJlYWRlci5yZWFkU3RyaW5nKCk7XHJcblxyXG5cdFx0XHQvLyBSZWdleCBmb3IgYSBVVUlEOiBodHRwczovL3N0YWNrb3ZlcmZsb3cuY29tL2EvMTM2NTMxODAvNDc3OTA3MVxyXG5cdFx0XHRpZihzLm1hdGNoKC9eWzAtOWEtZl17OH0tWzAtOWEtZl17NH0tWzAtNV1bMC05YS1mXXszfS1bMDg5YWJdWzAtOWEtZl17M30tWzAtOWEtZl17MTJ9JC9pKSAhPT0gbnVsbCkge1xyXG5cdFx0XHRcdHRoaXMuZW50aXR5VVVJRCA9IHM7XHJcblx0XHRcdFx0dGhpcy5pbmNsdWRlc0VudGl0aWVzID0gdHJ1ZTtcclxuXHRcdFx0fSBlbHNlIGlmKHMubGVuZ3RoID09PSAwIHx8IHMubGVuZ3RoID4gMTYpIHtcclxuXHRcdFx0XHRyZWFkZXIuc2V0Q3Vyc29yKGkpO1xyXG5cdFx0XHRcdHRocm93IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZShuZXcgTGl0ZXJhbE1lc3NhZ2UoXCJJbnZhbGlkIG5hbWUgb3IgVVVJRFwiKSkuY3JlYXRlV2l0aENvbnRleHQocmVhZGVyKTtcclxuXHRcdFx0fSBlbHNlIHtcclxuXHRcdFx0XHR0aGlzLnBsYXllck5hbWUgPSBzO1xyXG5cdFx0XHRcdHRoaXMuaW5jbHVkZXNFbnRpdGllcyA9IGZhbHNlO1xyXG5cdFx0XHR9XHJcblxyXG5cdFx0XHQvLyBXZSdyZSBvbmx5IGFsbG93aW5nIDEgcmVzdWx0IGJlY2F1c2Ugd2UndmUgc3BlY2lmaWVkIGEgcGxheWVyIG9yXHJcblx0XHRcdC8vIFVVSUQsIGFuZCBub3QgYW4gQCBzZWxlY3RvclxyXG5cdFx0XHR0aGlzLm1heFJlc3VsdHMgPSAxO1xyXG5cdFx0fVxyXG5cclxuXHRcdGxldCBzdGFydDogbnVtYmVyID0gcmVhZGVyLmdldEN1cnNvcigpO1xyXG5cdFx0Ly8gRm9yIHNvbWUgcmVhc29uLCBzZXR0aW5nIHRoZXNlIHN1Z2dlc3Rpb25zIGhlcmUgdHJpZ2dlcnMgdHdpY2UoPyksIHNvIGRvbid0IGRvIHRoYXQuXHJcblx0XHQvLyB0aGlzLnN1Z2dlc3Rpb25zID0gW1wiQHBcIiwgXCJAYVwiLCBcIkByXCIsIFwiQHNcIiwgXCJAZVwiLCBcIlNrZXB0ZXJcIl07Ly8gVE9ETzogTmFtZSBvciBzZWxlY3RvclxyXG5cdFx0aWYocmVhZGVyLmNhblJlYWQoKSAmJiByZWFkZXIucGVlaygpID09PSBcIkBcIikge1xyXG5cdFx0XHRyZWFkZXIuc2tpcCgpO1xyXG5cdFx0XHRwYXJzZVNlbGVjdG9yKCk7XHJcblx0XHR9IGVsc2Uge1xyXG5cdFx0XHRwYXJzZU5hbWVPclVVSUQoKTtcclxuXHRcdH1cclxuXHJcblx0XHQvLyBGaW5hbCBjaGVja3MuLi5cclxuXHRcdGlmKHRoaXMubWF4UmVzdWx0cyA+IDEgJiYgdGhpcy5zaW5nbGUpIHtcclxuXHRcdFx0cmVhZGVyLnNldEN1cnNvcihzdGFydCk7XHJcblx0XHRcdGlmKHRoaXMucGxheWVyc09ubHkpIHtcclxuXHRcdFx0XHR0aHJvdyBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGUobmV3IExpdGVyYWxNZXNzYWdlKFwiT25seSBvbmUgcGxheWVyIGlzIGFsbG93ZWQsIGJ1dCB0aGUgcHJvdmlkZWQgc2VsZWN0b3IgYWxsb3dzIG1vcmUgdGhhbiBvbmVcIikpLmNyZWF0ZVdpdGhDb250ZXh0KHJlYWRlcik7XHJcblx0XHRcdH0gZWxzZSB7XHJcblx0XHRcdFx0dGhyb3cgbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKG5ldyBMaXRlcmFsTWVzc2FnZShcIk9ubHkgb25lIGVudGl0eSBpcyBhbGxvd2VkLCBidXQgdGhlIHByb3ZpZGVkIHNlbGVjdG9yIGFsbG93cyBtb3JlIHRoYW4gb25lXCIpKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdFx0XHR9XHJcblx0XHR9XHJcblx0XHRpZih0aGlzLmluY2x1ZGVzRW50aXRpZXMgJiYgdGhpcy5wbGF5ZXJzT25seSAvKiBTVFVCOiAhaXNTZWxmU2VsZWN0b3IoKSAqLykge1xyXG5cdFx0XHRyZWFkZXIuc2V0Q3Vyc29yKHN0YXJ0KTtcclxuXHRcdFx0dGhyb3cgbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKG5ldyBMaXRlcmFsTWVzc2FnZShcIk9ubHkgcGxheWVycyBtYXkgYmUgYWZmZWN0ZWQgYnkgdGhpcyBjb21tYW5kLCBidXQgdGhlIHByb3ZpZGVkIHNlbGVjdG9yIGluY2x1ZGVzIGVudGl0aWVzXCIpKS5jcmVhdGVXaXRoQ29udGV4dChyZWFkZXIpO1xyXG5cdFx0fVxyXG5cclxuXHRcdHJldHVybiB0aGlzO1xyXG5cdH1cclxuXHJcblx0cHVibGljIGxpc3RTdWdnZXN0aW9ucyhfY29udGV4dDogQ29tbWFuZENvbnRleHQ8YW55PiwgYnVpbGRlcjogU3VnZ2VzdGlvbnNCdWlsZGVyKTogUHJvbWlzZTxTdWdnZXN0aW9ucz4ge1xyXG5cdFx0dHJ5IHtcclxuXHRcdFx0dGhpcy5wYXJzZShuZXcgU3RyaW5nUmVhZGVyKGJ1aWxkZXIuZ2V0SW5wdXQoKSBhcyBzdHJpbmcpKVxyXG5cdFx0fSBjYXRjaChleGNlcHRpb24pIHtcclxuXHJcblx0XHR9XHJcblxyXG5cdFx0aWYodGhpcy5zdWdnZXN0aW9ucy5sZW5ndGggPiAwKSB7XHJcblxyXG5cdFx0XHRpZih0aGlzLnN1Z2dlc3Rpb25zTW9kaWZpZXIgIT09IG51bGwpIHtcclxuXHRcdFx0XHR0aGlzLnN1Z2dlc3Rpb25zTW9kaWZpZXIoYnVpbGRlcik7XHJcblx0XHRcdH1cclxuXHJcblx0XHRcdC8vIFdlIGRvbid0IHVzZSBIZWxwZXJTdWdnZXN0aW9uUHJvdmlkZXIgYmVjYXVzZSB3ZSd2ZSBhbHJlYWR5XHJcblx0XHRcdC8vIHByZS1nZW5lcmF0ZWQgc3VnZ2VzdGlvbnMgZm9yIHRoZSBzcGVjaWZpYyBjb250ZXh0LCBzbyB3ZSBkb24ndFxyXG5cdFx0XHQvLyBuZWVkIHRvIGNoZWNrIGlmIGl0IG1hdGNoZXMgb3IgYW55dGhpbmcsIGp1c3Qgc3VnZ2VzdCBpdCFcclxuXHRcdFx0Zm9yIChsZXQgc3VnZ2VzdGlvbiBvZiB0aGlzLnN1Z2dlc3Rpb25zKSB7XHJcblx0XHRcdFx0YnVpbGRlci5zdWdnZXN0KHN1Z2dlc3Rpb24pO1xyXG5cdFx0XHR9XHJcblx0XHRcdHJldHVybiBidWlsZGVyLmJ1aWxkUHJvbWlzZSgpO1xyXG5cdFx0fSBlbHNlIHtcclxuXHRcdFx0cmV0dXJuIFN1Z2dlc3Rpb25zLmVtcHR5KCk7XHJcblx0XHR9XHJcblx0fVxyXG5cclxuXHRwdWJsaWMgZ2V0RXhhbXBsZXMoKTogc3RyaW5nW10ge1xyXG5cdFx0cmV0dXJuIFtcImRkMTJiZTQyLTUyYTktNGE5MS1hOGExLTExYzAxODQ5ZTQ5OFwiXTtcclxuXHR9XHJcbn0iLCJleHBvcnQge307XHJcblxyXG5kZWNsYXJlIGdsb2JhbCB7XHJcbiAgICBpbnRlcmZhY2UgQXJyYXk8VD4ge1xyXG4gICAgICAgIGhhcyh4OiAwKTogdGhpcyBpcyBbXTtcclxuICAgICAgICBoYXMoeDogMSk6IHRoaXMgaXMgW1RdO1xyXG4gICAgICAgIGhhcyh4OiAyKTogdGhpcyBpcyBbVCwgVF07XHJcbiAgICB9XHJcbn1cclxuXHJcbkFycmF5LnByb3RvdHlwZS5oYXMgPSBmdW5jdGlvbiB4KHg6IG51bWJlcikge1xyXG4gICAgcmV0dXJuIHRoaXMubGVuZ3RoID09PSB4O1xyXG59OyIsIi8qKlxyXG4gKiBodHRwczovL3d3dy50eXBlc2NyaXB0bGFuZy5vcmcvZG9jcy9oYW5kYm9vay9kZWNsYXJhdGlvbi1tZXJnaW5nLmh0bWwjbW9kdWxlLWF1Z21lbnRhdGlvblxyXG4gKlxyXG4gKiBUaGlzIGlzIGFuIGV4YW1wbGUgb2YgbW9kdWxlIGF1Z21lbnRhdGlvbi4gV2Ugd2FudCB0byBtb2RpZnkgdGhlIGV4aXNpbmdcclxuICogbW9kdWxlIG5vZGUtYnJpZ2FkaWVyIHdpdGggb3VyIG93biBleHRlbnNpb25zLiBXZSBkbyB0aGF0IGJ5IGRlY2xhcmluZyB0aGVcclxuICogbW9kdWxlIFwibm9kZS1icmlnYWRpZXJcIiwgb3VyIHR5cGVzIHRoYXQgd2Ugd2FudCB0byBhdWdtZW50IGFuZCBvdXIgbmV3XHJcbiAqIG1ldGhvZHMuIFR5cGVTY3JpcHQgd2lsbCBhdXRvbWF0aWNhbGx5IHVwZGF0ZSB0aGVtLiBUaGlzIGxldHMgeW91IHVzZSB0aGUgbmV3XHJcbiAqIG1ldGhvZHMgd2l0aG91dCBUeXBlU2NyaXB0IGNvbXBsYWluaW5nIHRoYXQgdGhleSBkb24ndCBleGlzdC4gVGhlc2UgbmV3XHJcbiAqIG1ldGhvZHMgY2FuIGJlIHVzZWQgYnkgaW1wb3J0aW5nIHRoaXMgZmlsZSAob3IgaWYgaXQncyBpbiB0aGUgc2FtZSBmb2xkZXIsXHJcbiAqIHlvdSBkb24ndCBoYXZlIHRvIGRvIGFueXRoaW5nLCB1bmxlc3MgeW91J3JlIHVzaW5nIHdlYnBhY2sgYmVjYXVzZSB3ZWJwYWNrXHJcbiAqIG5lZWRzIHRvIGtub3cgaXQgZXhpc3RzKSwgZS5nLjpcclxuICogXHJcbiAqICAgaW1wb3J0IFwiLi9icmlnYWRpZXJfZXh0ZW5zaW9uc1wiXHJcbiAqL1xyXG5cclxuaW1wb3J0IHtcclxuXHRTdHJpbmdSZWFkZXIsXHJcblx0Q29tbWFuZFN5bnRheEV4Y2VwdGlvbixcclxuXHRTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZSxcclxuXHRMaXRlcmFsTWVzc2FnZVxyXG59IGZyb20gXCJub2RlLWJyaWdhZGllclwiXHJcblxyXG5pbXBvcnQgbW9qYW5nc29uIGZyb20gXCJtb2phbmdzb24tcGFyc2VyXCJcclxuXHJcbmRlY2xhcmUgbW9kdWxlIFwibm9kZS1icmlnYWRpZXJcIiB7XHJcblx0aW50ZXJmYWNlIFN0cmluZ1JlYWRlciB7XHJcblx0XHQvLyBOZXcgcmVhZGluZyBtZXRob2RzXHJcblx0XHRyZWFkTG9jYXRpb25MaXRlcmFsKCk6IG51bWJlcjtcclxuXHRcdHJlYWRSZXNvdXJjZUxvY2F0aW9uKCk6IFtzdHJpbmcsIHN0cmluZ107XHJcblx0XHRyZWFkTWluTWF4Qm91bmRzKGFsbG93RmxvYXRzOiBib29sZWFuKTogW251bWJlciwgbnVtYmVyXTtcclxuXHRcdHJlYWROQlQoKTogc3RyaW5nO1xyXG5cclxuXHRcdC8qKiBAcmV0dXJucyB0cnVlIGlmIGEgbmVnYXRpb24gY2hhcmFjdGVyIGAhYCB3YXMgcmVhZCAqL1xyXG5cdFx0cmVhZE5lZ2F0aW9uQ2hhcmFjdGVyKCk6IGJvb2xlYW47XHJcblx0XHQvKiogQHJldHVybnMgdHJ1ZSBpZiBhIG5lZ2F0aW9uIGNoYXJhY3RlciBgI2Agd2FzIHJlYWQgKi9cclxuXHRcdHJlYWRUYWdDaGFyYWN0ZXIoKTogYm9vbGVhbjtcclxuXHJcblx0XHQvLyBOZXcgY2hhciBtZXRob2RzXHJcblx0XHRpc0FsbG93ZWRJblJlc291cmNlTG9jYXRpb24oYzogc3RyaW5nKTogYm9vbGVhbjtcclxuXHRcdGlzVmFsaWRQYXRoQ2hhcihjOiBzdHJpbmcpOiBib29sZWFuO1xyXG5cdFx0aXNWYWxpZE5hbWVzcGFjZUNoYXIoYzogc3RyaW5nKTogYm9vbGVhbjtcclxuXHR9XHJcbn1cclxuXHJcblN0cmluZ1JlYWRlci5wcm90b3R5cGUucmVhZExvY2F0aW9uTGl0ZXJhbCA9IGZ1bmN0aW9uIHJlYWRMb2NhdGlvbkxpdGVyYWwoKTogbnVtYmVyIHtcclxuXHJcblx0ZnVuY3Rpb24gaXNBbGxvd2VkTG9jYXRpb25MaXRlcmFsKGM6IHN0cmluZyk6IGJvb2xlYW4ge1xyXG5cdFx0cmV0dXJuIGMgPT09ICd+JyB8fCBjID09PSAnXic7XHJcblx0fVxyXG5cclxuXHRsZXQgc3RhcnQgPSB0aGlzLmdldEN1cnNvcigpO1xyXG5cdHdoaWxlICh0aGlzLmNhblJlYWQoKSAmJiAoU3RyaW5nUmVhZGVyLmlzQWxsb3dlZE51bWJlcih0aGlzLnBlZWsoKSkgfHwgaXNBbGxvd2VkTG9jYXRpb25MaXRlcmFsKHRoaXMucGVlaygpKSkpIHtcclxuXHRcdHRoaXMuc2tpcCgpO1xyXG5cdH1cclxuXHRsZXQgbnVtYmVyID0gdGhpcy5nZXRTdHJpbmcoKS5zdWJzdHJpbmcoc3RhcnQsIHRoaXMuZ2V0Q3Vyc29yKCkpO1xyXG5cdGlmIChudW1iZXIubGVuZ3RoID09PSAwKSB7XHJcblx0XHR0aHJvdyAoQ29tbWFuZFN5bnRheEV4Y2VwdGlvbiBhcyBhbnkpLkJVSUxUX0lOX0VYQ0VQVElPTlMucmVhZGVyRXhwZWN0ZWRJbnQoKS5jcmVhdGVXaXRoQ29udGV4dCh0aGlzKTtcclxuXHR9XHJcblxyXG5cdGlmIChudW1iZXIuc3RhcnRzV2l0aChcIn5cIikgfHwgbnVtYmVyLnN0YXJ0c1dpdGgoXCJeXCIpKSB7XHJcblx0XHRpZiAobnVtYmVyLmxlbmd0aCA9PT0gMSkge1xyXG5cdFx0XHQvLyBBY2NlcHQuIFdlIGRvbid0IGNhcmUgYWJvdXQgcmV0dXJuaW5nIHJlYXNvbmFibGUgcmVzdWx0c1xyXG5cdFx0XHRyZXR1cm4gMDtcclxuXHRcdH0gZWxzZSB7XHJcblx0XHRcdG51bWJlciA9IG51bWJlci5zbGljZSgxKTtcclxuXHRcdH1cclxuXHR9XHJcblx0Y29uc3QgcmVzdWx0ID0gcGFyc2VJbnQobnVtYmVyKTtcclxuXHRpZiAoaXNOYU4ocmVzdWx0KSB8fCByZXN1bHQgIT09IHBhcnNlRmxvYXQobnVtYmVyKSkge1xyXG5cdFx0dGhpcy5zZXRDdXJzb3Ioc3RhcnQpO1xyXG5cdFx0dGhyb3cgKENvbW1hbmRTeW50YXhFeGNlcHRpb24gYXMgYW55KS5CVUlMVF9JTl9FWENFUFRJT05TLnJlYWRlckludmFsaWRJbnQoKS5jcmVhdGVXaXRoQ29udGV4dCh0aGlzLCBudW1iZXIpO1xyXG5cdH0gZWxzZSB7XHJcblx0XHRyZXR1cm4gcmVzdWx0O1xyXG5cdH1cclxuXHJcbn07XHJcblxyXG5TdHJpbmdSZWFkZXIucHJvdG90eXBlLnJlYWRSZXNvdXJjZUxvY2F0aW9uID0gZnVuY3Rpb24gcmVhZFJlc291cmNlTG9jYXRpb24oKTogW3N0cmluZywgc3RyaW5nXSB7XHJcblxyXG5cdGZ1bmN0aW9uIGlzVmFsaWQoc3RyaW5nOiBzdHJpbmcsIHByZWRpY2F0ZTogKGM6IHN0cmluZykgPT4gYm9vbGVhbik6IGJvb2xlYW4ge1xyXG5cdFx0Zm9yIChsZXQgaTogbnVtYmVyID0gMDsgaSA8IHN0cmluZy5sZW5ndGg7IGkrKykge1xyXG5cdFx0XHRpZiAoIXByZWRpY2F0ZShzdHJpbmcuY2hhckF0KGkpKSkge1xyXG5cdFx0XHRcdHJldHVybiBmYWxzZTtcclxuXHRcdFx0fVxyXG5cdFx0fVxyXG5cdFx0cmV0dXJuIHRydWU7XHJcblx0fVxyXG5cclxuXHRjb25zdCBzdGFydCA9IHRoaXMuZ2V0Q3Vyc29yKCk7XHJcblx0d2hpbGUgKHRoaXMuY2FuUmVhZCgpICYmIHRoaXMuaXNBbGxvd2VkSW5SZXNvdXJjZUxvY2F0aW9uKHRoaXMucGVlaygpKSkge1xyXG5cdFx0dGhpcy5za2lwKCk7XHJcblx0fVxyXG5cclxuXHRsZXQgcmVzb3VyY2VMb2NhdGlvbjogc3RyaW5nID0gdGhpcy5nZXRTdHJpbmcoKS5zdWJzdHJpbmcoc3RhcnQsIHRoaXMuZ2V0Q3Vyc29yKCkpO1xyXG5cclxuXHRjb25zdCByZXNvdXJjZUxvY2F0aW9uUGFydHM6IHN0cmluZ1tdIHwgdW5kZWZpbmVkID0gcmVzb3VyY2VMb2NhdGlvbi5zcGxpdChcIjpcIik7XHJcblx0aWYgKHJlc291cmNlTG9jYXRpb25QYXJ0cyA9PT0gdW5kZWZpbmVkKSB7XHJcblx0XHR0aHJvdyBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGUobmV3IExpdGVyYWxNZXNzYWdlKHJlc291cmNlTG9jYXRpb24gKyBcIiBpcyBub3QgYSB2YWxpZCBSZXNvdXJjZVwiKSkuY3JlYXRlV2l0aENvbnRleHQodGhpcyk7XHJcblx0fVxyXG5cclxuXHQvLyBQbGVhc2UgZXhjdXNlIHRoZSBhYm9taW5hdGlvbiB0aGF0IGlzIHRoaXMgQXJyYXkuaGFzKHggOiBudW1iZXIgYmV0d2VlbiAwIGFuZCAyKVxyXG5cdC8vIEknZCB1c2UgYSBzd2l0Y2ggc3RhdGVtZW50LCBidXQgVHlwZVNjcmlwdCdzIHR5cGUgcHJlZGljYXRlcyBkb24ndCBwYXNzIHRocm91Z2ggdGhhdFxyXG5cdC8vIEFuZCBJIHRoaW5rIHRoaXMgZnVua3kgZGVycHkgc29sdXRpb24gaXMgbmVhdGVyIHRoYW4gdXNpbmcgISBhbGwgb3ZlciB0aGUgcGxhY2UuIEl0J3NcclxuXHQvLyBiZXR0ZXIgdG8gZ2V0IHRoZSBjb21waWxlciB0byBwZXJmb3JtIGFycmF5IGluZGV4aW5nIGNoZWNrcyB0aGFuIHRyeSB0byB0ZWxsIGl0IFwieWVhaFwiXHJcblx0aWYgKHJlc291cmNlTG9jYXRpb25QYXJ0cy5oYXMoMCkpIHtcclxuXHRcdHRocm93IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZShuZXcgTGl0ZXJhbE1lc3NhZ2UocmVzb3VyY2VMb2NhdGlvbiArIFwiIGlzIG5vdCBhIHZhbGlkIFJlc291cmNlXCIpKS5jcmVhdGVXaXRoQ29udGV4dCh0aGlzKTtcclxuXHR9XHJcblx0ZWxzZSBpZiAocmVzb3VyY2VMb2NhdGlvblBhcnRzLmhhcygxKSkge1xyXG5cdFx0aWYgKCFpc1ZhbGlkKHJlc291cmNlTG9jYXRpb25QYXJ0c1swXSwgdGhpcy5pc1ZhbGlkUGF0aENoYXIpKSB7XHJcblx0XHRcdHRocm93IG5ldyBTaW1wbGVDb21tYW5kRXhjZXB0aW9uVHlwZShuZXcgTGl0ZXJhbE1lc3NhZ2UoXCJOb24gW2EtejAtOS8uXy1dIGNoYXJhY3RlciBpbiBwYXRoIG9mIGxvY2F0aW9uOiBcIiArIHJlc291cmNlTG9jYXRpb24pKS5jcmVhdGVXaXRoQ29udGV4dCh0aGlzKTtcclxuXHRcdH1cclxuXHRcdHJldHVybiBbXCJtaW5lY3JhZnRcIiwgcmVzb3VyY2VMb2NhdGlvbl07XHJcblx0fVxyXG5cdGVsc2UgaWYgKHJlc291cmNlTG9jYXRpb25QYXJ0cy5oYXMoMikpIHtcclxuXHRcdC8vIENoZWNrIG5hbWVzcGFjZVxyXG5cdFx0aWYgKCFpc1ZhbGlkKHJlc291cmNlTG9jYXRpb25QYXJ0c1swXSwgdGhpcy5pc1ZhbGlkTmFtZXNwYWNlQ2hhcikpIHtcclxuXHRcdFx0dGhyb3cgbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKG5ldyBMaXRlcmFsTWVzc2FnZShcIk5vbiBbYS16MC05Xy4tXSBjaGFyYWN0ZXIgaW4gbmFtZXNwYWNlIG9mIGxvY2F0aW9uOiBcIiArIHJlc291cmNlTG9jYXRpb24pKS5jcmVhdGVXaXRoQ29udGV4dCh0aGlzKTtcclxuXHRcdH1cclxuXHRcdC8vIENoZWNrIHBhdGhcclxuXHRcdGlmICghaXNWYWxpZChyZXNvdXJjZUxvY2F0aW9uUGFydHNbMV0sIHRoaXMuaXNWYWxpZFBhdGhDaGFyKSkge1xyXG5cdFx0XHR0aHJvdyBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGUobmV3IExpdGVyYWxNZXNzYWdlKFwiTm9uIFthLXowLTkvLl8tXSBjaGFyYWN0ZXIgaW4gcGF0aCBvZiBsb2NhdGlvbjogXCIgKyByZXNvdXJjZUxvY2F0aW9uKSkuY3JlYXRlV2l0aENvbnRleHQodGhpcyk7XHJcblx0XHR9XHJcblx0fVxyXG5cdGVsc2Uge1xyXG5cdFx0dGhyb3cgbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKG5ldyBMaXRlcmFsTWVzc2FnZShyZXNvdXJjZUxvY2F0aW9uICsgXCIgaXMgbm90IGEgdmFsaWQgUmVzb3VyY2VcIikpLmNyZWF0ZVdpdGhDb250ZXh0KHRoaXMpO1xyXG5cdH1cclxuXHRyZXR1cm4gW3Jlc291cmNlTG9jYXRpb25QYXJ0c1swXSwgcmVzb3VyY2VMb2NhdGlvblBhcnRzWzFdXTtcclxufTtcclxuXHJcblN0cmluZ1JlYWRlci5wcm90b3R5cGUucmVhZE1pbk1heEJvdW5kcyA9IGZ1bmN0aW9uIHJlYWRNaW5NYXhCb3VuZHMoYWxsb3dGbG9hdHM6IGJvb2xlYW4pOiBbbnVtYmVyLCBudW1iZXJdIHtcclxuXHRpZiAoIXRoaXMuY2FuUmVhZCgpKSB7XHJcblx0XHR0aHJvdyBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGUobmV3IExpdGVyYWxNZXNzYWdlKGBFeHBlY3RlZCB2YWx1ZSBvciByYW5nZSBvZiB2YWx1ZXNgKSkuY3JlYXRlV2l0aENvbnRleHQodGhpcyk7XHJcblx0fVxyXG5cclxuXHRjb25zdCBzdGFydCA9IHRoaXMuZ2V0Q3Vyc29yKCk7XHJcblx0bGV0IG1pbjogbnVtYmVyIHwgbnVsbCA9IG51bGw7XHJcblx0bGV0IG1heDogbnVtYmVyIHwgbnVsbCA9IG51bGw7XHJcblxyXG5cdHRyeSB7XHJcblx0XHRtaW4gPSBhbGxvd0Zsb2F0cyA/IHRoaXMucmVhZEZsb2F0KCkgOiB0aGlzLnJlYWRJbnQoKTtcclxuXHR9IGNhdGNoIChlcnJvcikge1xyXG5cdFx0Ly8gaWdub3JlIGl0XHJcblx0fVxyXG5cclxuXHRpZiAodGhpcy5jYW5SZWFkKDIpICYmIHRoaXMucGVlaygpID09ICcuJyAmJiB0aGlzLnBlZWsoMSkgPT0gJy4nKSB7XHJcblx0XHR0aGlzLnNraXAoKTtcclxuXHRcdHRoaXMuc2tpcCgpO1xyXG5cclxuXHRcdHRyeSB7XHJcblx0XHRcdG1heCA9IGFsbG93RmxvYXRzID8gdGhpcy5yZWFkRmxvYXQoKSA6IHRoaXMucmVhZEludCgpO1xyXG5cdFx0fSBjYXRjaCAoZXJyb3IpIHtcclxuXHRcdFx0Ly8gaWdub3JlIGl0XHJcblx0XHR9XHJcblx0fSBlbHNlIHtcclxuXHRcdG1heCA9IG1pbjtcclxuXHR9XHJcblxyXG5cdGlmIChtaW4gPT09IG51bGwgJiYgbWF4ID09PSBudWxsKSB7XHJcblx0XHR0aGlzLnNldEN1cnNvcihzdGFydCk7XHJcblx0XHR0aHJvdyBuZXcgU2ltcGxlQ29tbWFuZEV4Y2VwdGlvblR5cGUobmV3IExpdGVyYWxNZXNzYWdlKGBFeHBlY3RlZCB2YWx1ZSBvciByYW5nZSBvZiB2YWx1ZXNgKSkuY3JlYXRlV2l0aENvbnRleHQodGhpcyk7XHJcblx0fSBlbHNlIHtcclxuXHRcdGlmIChtaW4gPT09IG51bGwpIHtcclxuXHRcdFx0bWluID0gTnVtYmVyLk1JTl9TQUZFX0lOVEVHRVI7XHJcblx0XHR9XHJcblx0XHRpZiAobWF4ID09PSBudWxsKSB7XHJcblx0XHRcdG1heCA9IE51bWJlci5NQVhfU0FGRV9JTlRFR0VSO1xyXG5cdFx0fVxyXG5cdH1cclxuXHJcblx0cmV0dXJuIFttaW4sIG1heF07XHJcbn07XHJcblxyXG5TdHJpbmdSZWFkZXIucHJvdG90eXBlLnJlYWROQlQgPSBmdW5jdGlvbiByZWFkTkJUKCk6IHN0cmluZyB7XHJcblx0Y29uc3Qgc3RhcnQ6IG51bWJlciA9IHRoaXMuZ2V0Q3Vyc29yKCk7XHJcblx0bGV0IG5idDogc3RyaW5nID0gXCJcIjtcclxuXHR3aGlsZSAodGhpcy5jYW5SZWFkKCkpIHtcclxuXHRcdG5idCArPSB0aGlzLnJlYWQoKTtcclxuXHRcdHRyeSB7XHJcblx0XHRcdG1vamFuZ3NvbihuYnQpO1xyXG5cdFx0XHRicmVhaztcclxuXHRcdH0gY2F0Y2ggKGVycm9yKSB7XHJcblx0XHR9XHJcblx0fVxyXG5cdHRyeSB7XHJcblx0XHRtb2phbmdzb24obmJ0KTtcclxuXHR9IGNhdGNoIChlcnJvcikge1xyXG5cdFx0dGhpcy5zZXRDdXJzb3Ioc3RhcnQpO1xyXG5cdFx0dGhyb3cgbmV3IFNpbXBsZUNvbW1hbmRFeGNlcHRpb25UeXBlKG5ldyBMaXRlcmFsTWVzc2FnZShgJHtlcnJvcn1gKSkuY3JlYXRlV2l0aENvbnRleHQodGhpcyk7XHJcblx0fVxyXG5cdHJldHVybiBuYnQ7XHJcbn07XHJcblxyXG5TdHJpbmdSZWFkZXIucHJvdG90eXBlLnJlYWROZWdhdGlvbkNoYXJhY3RlciA9IGZ1bmN0aW9uIHJlYWROZWdhdGlvbkNoYXJhY3RlcigpOiBib29sZWFuIHtcclxuXHR0aGlzLnNraXBXaGl0ZXNwYWNlKCk7XHJcblx0aWYgKHRoaXMuY2FuUmVhZCgpICYmIHRoaXMucGVlaygpID09PSAnIScpIHtcclxuXHRcdHRoaXMuc2tpcCgpO1xyXG5cdFx0dGhpcy5za2lwV2hpdGVzcGFjZSgpO1xyXG5cdFx0cmV0dXJuIHRydWU7XHJcblx0fVxyXG5cdHJldHVybiBmYWxzZTtcclxufTtcclxuXHJcblN0cmluZ1JlYWRlci5wcm90b3R5cGUucmVhZFRhZ0NoYXJhY3RlciA9IGZ1bmN0aW9uIHJlYWRUYWdDaGFyYWN0ZXIoKTogYm9vbGVhbiB7XHJcblx0dGhpcy5za2lwV2hpdGVzcGFjZSgpO1xyXG5cdGlmICh0aGlzLmNhblJlYWQoKSAmJiB0aGlzLnBlZWsoKSA9PT0gJyMnKSB7XHJcblx0XHR0aGlzLnNraXAoKTtcclxuXHRcdHRoaXMuc2tpcFdoaXRlc3BhY2UoKTtcclxuXHRcdHJldHVybiB0cnVlO1xyXG5cdH1cclxuXHRyZXR1cm4gZmFsc2U7XHJcbn07XHJcblxyXG5TdHJpbmdSZWFkZXIucHJvdG90eXBlLmlzQWxsb3dlZEluUmVzb3VyY2VMb2NhdGlvbiA9IGZ1bmN0aW9uIGlzQWxsb3dlZEluUmVzb3VyY2VMb2NhdGlvbihjOiBzdHJpbmcpOiBib29sZWFuIHtcclxuXHRyZXR1cm4gKChjID49ICcwJyAmJiBjIDw9ICc5JykgfHwgKGMgPj0gJ2EnICYmIGMgPD0gJ3onKSB8fCBjID09PSAnXycgfHwgYyA9PT0gJzonIHx8IGMgPT09ICcvJyB8fCBjID09PSAnLicgfHwgYyA9PT0gJy0nKTtcclxufTtcclxuXHJcblN0cmluZ1JlYWRlci5wcm90b3R5cGUuaXNWYWxpZFBhdGhDaGFyID0gZnVuY3Rpb24gaXNWYWxpZFBhdGhDaGFyKGM6IHN0cmluZyk6IGJvb2xlYW4ge1xyXG5cdHJldHVybiAoYyA9PT0gJ18nIHx8IGMgPT09ICctJyB8fCAoYyA+PSAnYScgJiYgYyA8PSAneicpIHx8IChjID49ICcwJyAmJiBjIDw9ICc5JykgfHwgYyA9PT0gJy8nIHx8IGMgPT09ICcuJyk7XHJcbn07XHJcblxyXG5TdHJpbmdSZWFkZXIucHJvdG90eXBlLmlzVmFsaWROYW1lc3BhY2VDaGFyID0gZnVuY3Rpb24gaXNWYWxpZE5hbWVzcGFjZUNoYXIoYzogc3RyaW5nKTogYm9vbGVhbiB7XHJcblx0cmV0dXJuIChjID09PSAnXycgfHwgYyA9PT0gJy0nIHx8IChjID49ICdhJyAmJiBjIDw9ICd6JykgfHwgKGMgPj0gJzAnICYmIGMgPD0gJzknKSB8fCBjID09PSAnLicpO1xyXG59OyIsIi8vIFRoZSBtb2R1bGUgY2FjaGVcbnZhciBfX3dlYnBhY2tfbW9kdWxlX2NhY2hlX18gPSB7fTtcblxuLy8gVGhlIHJlcXVpcmUgZnVuY3Rpb25cbmZ1bmN0aW9uIF9fd2VicGFja19yZXF1aXJlX18obW9kdWxlSWQpIHtcblx0Ly8gQ2hlY2sgaWYgbW9kdWxlIGlzIGluIGNhY2hlXG5cdHZhciBjYWNoZWRNb2R1bGUgPSBfX3dlYnBhY2tfbW9kdWxlX2NhY2hlX19bbW9kdWxlSWRdO1xuXHRpZiAoY2FjaGVkTW9kdWxlICE9PSB1bmRlZmluZWQpIHtcblx0XHRyZXR1cm4gY2FjaGVkTW9kdWxlLmV4cG9ydHM7XG5cdH1cblx0Ly8gQ3JlYXRlIGEgbmV3IG1vZHVsZSAoYW5kIHB1dCBpdCBpbnRvIHRoZSBjYWNoZSlcblx0dmFyIG1vZHVsZSA9IF9fd2VicGFja19tb2R1bGVfY2FjaGVfX1ttb2R1bGVJZF0gPSB7XG5cdFx0Ly8gbm8gbW9kdWxlLmlkIG5lZWRlZFxuXHRcdC8vIG5vIG1vZHVsZS5sb2FkZWQgbmVlZGVkXG5cdFx0ZXhwb3J0czoge31cblx0fTtcblxuXHQvLyBFeGVjdXRlIHRoZSBtb2R1bGUgZnVuY3Rpb25cblx0X193ZWJwYWNrX21vZHVsZXNfX1ttb2R1bGVJZF0uY2FsbChtb2R1bGUuZXhwb3J0cywgbW9kdWxlLCBtb2R1bGUuZXhwb3J0cywgX193ZWJwYWNrX3JlcXVpcmVfXyk7XG5cblx0Ly8gUmV0dXJuIHRoZSBleHBvcnRzIG9mIHRoZSBtb2R1bGVcblx0cmV0dXJuIG1vZHVsZS5leHBvcnRzO1xufVxuXG4iLCIvLyBnZXREZWZhdWx0RXhwb3J0IGZ1bmN0aW9uIGZvciBjb21wYXRpYmlsaXR5IHdpdGggbm9uLWhhcm1vbnkgbW9kdWxlc1xuX193ZWJwYWNrX3JlcXVpcmVfXy5uID0gKG1vZHVsZSkgPT4ge1xuXHR2YXIgZ2V0dGVyID0gbW9kdWxlICYmIG1vZHVsZS5fX2VzTW9kdWxlID9cblx0XHQoKSA9PiAobW9kdWxlWydkZWZhdWx0J10pIDpcblx0XHQoKSA9PiAobW9kdWxlKTtcblx0X193ZWJwYWNrX3JlcXVpcmVfXy5kKGdldHRlciwgeyBhOiBnZXR0ZXIgfSk7XG5cdHJldHVybiBnZXR0ZXI7XG59OyIsIi8vIGRlZmluZSBnZXR0ZXIgZnVuY3Rpb25zIGZvciBoYXJtb255IGV4cG9ydHNcbl9fd2VicGFja19yZXF1aXJlX18uZCA9IChleHBvcnRzLCBkZWZpbml0aW9uKSA9PiB7XG5cdGZvcih2YXIga2V5IGluIGRlZmluaXRpb24pIHtcblx0XHRpZihfX3dlYnBhY2tfcmVxdWlyZV9fLm8oZGVmaW5pdGlvbiwga2V5KSAmJiAhX193ZWJwYWNrX3JlcXVpcmVfXy5vKGV4cG9ydHMsIGtleSkpIHtcblx0XHRcdE9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBrZXksIHsgZW51bWVyYWJsZTogdHJ1ZSwgZ2V0OiBkZWZpbml0aW9uW2tleV0gfSk7XG5cdFx0fVxuXHR9XG59OyIsIl9fd2VicGFja19yZXF1aXJlX18ubyA9IChvYmosIHByb3ApID0+IChPYmplY3QucHJvdG90eXBlLmhhc093blByb3BlcnR5LmNhbGwob2JqLCBwcm9wKSkiLCIvLyBkZWZpbmUgX19lc01vZHVsZSBvbiBleHBvcnRzXG5fX3dlYnBhY2tfcmVxdWlyZV9fLnIgPSAoZXhwb3J0cykgPT4ge1xuXHRpZih0eXBlb2YgU3ltYm9sICE9PSAndW5kZWZpbmVkJyAmJiBTeW1ib2wudG9TdHJpbmdUYWcpIHtcblx0XHRPYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgU3ltYm9sLnRvU3RyaW5nVGFnLCB7IHZhbHVlOiAnTW9kdWxlJyB9KTtcblx0fVxuXHRPYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgJ19fZXNNb2R1bGUnLCB7IHZhbHVlOiB0cnVlIH0pO1xufTsiLCIvLyBAdHMtY2hlY2tcclxuaW1wb3J0IHtcclxuXHRDb21tYW5kRGlzcGF0Y2hlcixcclxuXHRSb290Q29tbWFuZE5vZGUsXHJcblx0bGl0ZXJhbCBhcyBsaXRlcmFsQXJndW1lbnQsXHJcblx0YXJndW1lbnQsXHJcblx0d29yZCBhcyBzaW5nbGVXb3JkQXJndW1lbnQsIC8vIEEgc2luZ2xlIHdvcmQgKGUuZy4gU3RyaW5nQXJndW1lbnQgb3IgdW5xdW90ZWQgc3RyaW5nKVxyXG5cdHN0cmluZyBhcyBzdHJpbmdBcmd1bWVudCwgLy8gQSBxdW90YWJsZSBwaHJhc2UgKGUuZy4gVGV4dEFyZ3VtZW50IG9yIFwic3RyaW5nXCIpXHJcblx0aW50ZWdlciBhcyBpbnRlZ2VyQXJndW1lbnQsXHJcblx0ZmxvYXQgYXMgZmxvYXRBcmd1bWVudCxcclxuXHRib29sIGFzIGJvb2xBcmd1bWVudCxcclxuXHRncmVlZHlTdHJpbmcgYXMgZ3JlZWR5U3RyaW5nQXJndW1lbnQsXHJcblx0TGl0ZXJhbEFyZ3VtZW50QnVpbGRlcixcclxuXHRQYXJzZVJlc3VsdHMsXHJcblx0Q29tbWFuZFN5bnRheEV4Y2VwdGlvbixcclxuXHRDb21tYW5kTm9kZSxcclxuXHRBcmd1bWVudFR5cGUgYXMgQnJpZ2FkaWVyQXJndW1lbnRUeXBlLFxyXG5cdEFyZ3VtZW50QnVpbGRlcixcclxuXHRTdWdnZXN0aW9uc1xyXG59IGZyb20gXCJub2RlLWJyaWdhZGllclwiXHJcblxyXG5pbXBvcnQge1xyXG5cdEJsb2NrUG9zQXJndW1lbnQsXHJcblx0UGxheWVyQXJndW1lbnQsXHJcblx0TXVsdGlMaXRlcmFsQXJndW1lbnQsXHJcblx0Q29sdW1uUG9zQXJndW1lbnQsXHJcblx0VGltZUFyZ3VtZW50LFxyXG5cdENvbG9yQXJndW1lbnQsXHJcblx0UG90aW9uRWZmZWN0QXJndW1lbnQsXHJcblx0QW5nbGVBcmd1bWVudCxcclxuXHRVVUlEQXJndW1lbnQsXHJcblx0RW50aXR5U2VsZWN0b3JBcmd1bWVudCxcclxuXHRVbmltcGxlbWVudGVkQXJndW1lbnQsXHJcblx0TWF0aE9wZXJhdGlvbkFyZ3VtZW50LFxyXG5cdE5CVENvbXBvdW5kQXJndW1lbnQsXHJcblx0RW5jaGFudG1lbnRBcmd1bWVudFxyXG59IGZyb20gXCIuL2FyZ3VtZW50c1wiXHJcblxyXG4vKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqXHJcbiAqIENsYXNzZXMgJiBJbnRlcmZhY2VzICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICpcclxuICoqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKi9cclxuXHJcbmNsYXNzIE15Q29tbWFuZERpc3BhdGNoZXI8Uz4gZXh0ZW5kcyBDb21tYW5kRGlzcGF0Y2hlcjxTPiB7XHJcblxyXG5cdHByaXZhdGUgcm9vdDogUm9vdENvbW1hbmROb2RlPFM+O1xyXG5cclxuXHRjb25zdHJ1Y3Rvcihyb290PzogUm9vdENvbW1hbmROb2RlPFM+KSB7XHJcblx0XHRzdXBlcihyb290KTtcclxuXHRcdHRoaXMucm9vdCA9IHN1cGVyLmdldFJvb3QoKTtcclxuXHR9XHJcblxyXG5cdHB1YmxpYyBkZWxldGVBbGwoKTogdm9pZCB7XHJcblx0XHQvLyBAdHMtaWdub3JlIC0gV2UgY2FuJ3QgYWNjZXNzIHRoZSBkZWZhdWx0IFJvb3RDb21tYW5kTm9kZSwgc28ganVzdCBzdWRvIGRvIGl0XHJcblx0XHR0aGlzLnJvb3QgPSBuZXcgUm9vdENvbW1hbmROb2RlKCk7XHJcblx0fVxyXG5cclxuXHRwdWJsaWMgb3ZlcnJpZGUgZ2V0Um9vdCgpOiBSb290Q29tbWFuZE5vZGU8Uz4ge1xyXG5cdFx0cmV0dXJuIHRoaXMucm9vdDtcclxuXHR9XHJcblxyXG59XHJcblxyXG4vKipcclxuICogYFNlbGVjdGlvbi5tb2RpZnkoKWAgaXMgbm90IHBhcnQgb2YgW3RoZSBvZmZpY2lhbCBzcGVjXShodHRwczovL2RldmVsb3Blci5tb3ppbGxhLm9yZy9lbi1VUy9kb2NzL1dlYi9BUEkvU2VsZWN0aW9uL21vZGlmeSNzcGVjaWZpY2F0aW9ucyksXHJcbiAqIGJ1dCBpdCBleGlzdHMgaW4gYWxsIGJyb3dzZXJzLiBTZWUgW01pY3Jvc29mdC9UeXBlU2NyaXB0IzEyMjk2XShodHRwczovL2dpdGh1Yi5jb20vTWljcm9zb2Z0L1R5cGVTY3JpcHQvaXNzdWVzLzEyMjk2KVxyXG4gKi9cclxudHlwZSBTZWxlY3Rpb25XaXRoTW9kaWZ5ID0gU2VsZWN0aW9uICYge1xyXG5cdG1vZGlmeShzOiBzdHJpbmcsIHQ6IHN0cmluZywgdTogc3RyaW5nKTogdm9pZDtcclxufVxyXG5cclxuLy8gV2UgbmVlZCBhIFwiZmlsbGVyXCIgdHlwZSBmb3Igb3VyIGNvbW1hbmQgc291cmNlLiBTaW5jZSB3ZSdyZSBuZXZlciBhY3R1YWxseVxyXG4vLyB1c2luZyB0aGUgY29tbWFuZCBzb3VyY2UsIHdlJ2xsIGp1c3QgdXNlIGFuIEFEVCB3aXRoIG9uZSBlbnRyeVxyXG5cclxudHlwZSBTb3VyY2UgPSBuZXZlcjtcclxuY29uc3QgU09VUkNFOiBTb3VyY2UgPSB1bmRlZmluZWQgYXMgbmV2ZXI7XHJcblxyXG4vKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqXHJcbiAqIENvbnN0YW50cyAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICpcclxuICoqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKi9cclxuXHJcbmNvbnN0IENPTU1BTkRfSU5QVVQ6IEhUTUxTcGFuRWxlbWVudCA9IGRvY3VtZW50LmdldEVsZW1lbnRCeUlkKFwiY21kLWlucHV0XCIpIGFzIEhUTUxTcGFuRWxlbWVudDtcclxuY29uc3QgQ09NTUFORF9JTlBVVF9BVVRPQ09NUExFVEU6IEhUTUxTcGFuRWxlbWVudCA9IGRvY3VtZW50LmdldEVsZW1lbnRCeUlkKFwiY21kLWlucHV0LWF1dG9jb21wbGV0ZVwiKSBhcyBIVE1MU3BhbkVsZW1lbnQ7XHJcbmNvbnN0IEVSUk9SX01FU1NBR0VfQk9YOiBIVE1MRGl2RWxlbWVudCA9IGRvY3VtZW50LmdldEVsZW1lbnRCeUlkKFwiZXJyb3ItYm94XCIpIGFzIEhUTUxEaXZFbGVtZW50O1xyXG5jb25zdCBTVUdHRVNUSU9OU19CT1g6IEhUTUxEaXZFbGVtZW50ID0gZG9jdW1lbnQuZ2V0RWxlbWVudEJ5SWQoXCJzdWdnZXN0aW9ucy1ib3hcIikgYXMgSFRNTERpdkVsZW1lbnQ7XHJcbmNvbnN0IFZBTElEX0JPWDogSFRNTERpdkVsZW1lbnQgPSBkb2N1bWVudC5nZXRFbGVtZW50QnlJZChcInZhbGlkLWJveFwiKSBhcyBIVE1MRGl2RWxlbWVudDtcclxuY29uc3QgQ09NTUFORFM6IEhUTUxUZXh0QXJlYUVsZW1lbnQgPSBkb2N1bWVudC5nZXRFbGVtZW50QnlJZChcImNvbW1hbmRzXCIpIGFzIEhUTUxUZXh0QXJlYUVsZW1lbnQ7XHJcbmNvbnN0IENIQVRfQk9YOiBIVE1MRGl2RWxlbWVudCA9IGRvY3VtZW50LmdldEVsZW1lbnRCeUlkKFwiY2hhdGJveFwiKSBhcyBIVE1MRGl2RWxlbWVudDtcclxuY29uc3QgUkVHSVNURVJfQ09NTUFORFNfQlVUVE9OOiBIVE1MQnV0dG9uRWxlbWVudCA9IGRvY3VtZW50LmdldEVsZW1lbnRCeUlkKFwicmVnaXN0ZXItY29tbWFuZHMtYnV0dG9uXCIpIGFzIEhUTUxCdXR0b25FbGVtZW50O1xyXG5jb25zdCBBUFBfRVJST1JfQk9YOiBIVE1MRGl2RWxlbWVudCA9IGRvY3VtZW50LmdldEVsZW1lbnRCeUlkKFwiYXBwLWVycm9yLWJveFwiKSBhcyBIVE1MRGl2RWxlbWVudDtcclxuXHJcbmNvbnN0IGRpc3BhdGNoZXIgPSBuZXcgTXlDb21tYW5kRGlzcGF0Y2hlcjxTb3VyY2U+KCk7XHJcblxyXG4vKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqXHJcbiAqIFByb3RvdHlwZXMgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICpcclxuICoqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKi9cclxuXHJcbi8vIEB0cy1pZ25vcmVcclxuLy8gQ29tbWFuZERpc3BhdGNoZXIucHJvdG90eXBlLmRlbGV0ZUFsbCA9IGZ1bmN0aW9uIGRlbGV0ZUFsbCgpIHsgdGhpcy5yb290ID0gbmV3IFJvb3RDb21tYW5kTm9kZSgpOyB9O1xyXG5cclxuLyoqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKlxyXG4gKiBFbnVtcyAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAqXHJcbiAqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKiovXHJcblxyXG5lbnVtIENoYXRDb2xvciB7XHJcblx0Ly8gVXNlcyB0aGUgc2VjdGlvbiBzeW1ib2wgKMKnKSwganVzdCBsaWtlIE1pbmVjcmFmdFxyXG5cdEJMQUNLID0gXCJcXHUwMEE3MFwiLFxyXG5cdERBUktfQkxVRSA9IFwiXFx1MDBBNzFcIixcclxuXHREQVJLX0dSRUVOID0gXCJcXHUwMEE3MlwiLFxyXG5cdERBUktfQVFVQSA9IFwiXFx1MDBBNzNcIixcclxuXHREQVJLX1JFRCA9IFwiXFx1MDBBNzRcIixcclxuXHREQVJLX1BVUlBMRSA9IFwiXFx1MDBBNzVcIixcclxuXHRHT0xEID0gXCJcXHUwMEE3NlwiLFxyXG5cdEdSQVkgPSBcIlxcdTAwQTc3XCIsXHJcblx0REFSS19HUkFZID0gXCJcXHUwMEE3OFwiLFxyXG5cdEJMVUUgPSBcIlxcdTAwQTc5XCIsXHJcblx0R1JFRU4gPSBcIlxcdTAwQTdhXCIsXHJcblx0QVFVQSA9IFwiXFx1MDBBN2JcIixcclxuXHRSRUQgPSBcIlxcdTAwQTdjXCIsXHJcblx0TElHSFRfUFVSUExFID0gXCJcXHUwMEE3ZFwiLFxyXG5cdFlFTExPVyA9IFwiXFx1MDBBN2VcIixcclxuXHRXSElURSA9IFwiXFx1MDBBN2ZcIlxyXG59O1xyXG5cclxuY29uc3QgQ2hhdENvbG9yQ1NTOiBNYXA8c3RyaW5nLCBzdHJpbmc+ID0gbmV3IE1hcChbXHJcblx0W1wiMFwiLCBcImJsYWNrXCJdLFxyXG5cdFtcIjFcIiwgXCJkYXJrX2JsdWVcIl0sXHJcblx0W1wiMlwiLCBcImRhcmtfZ3JlZW5cIl0sXHJcblx0W1wiM1wiLCBcImRhcmtfYXF1YVwiXSxcclxuXHRbXCI0XCIsIFwiZGFya19yZWRcIl0sXHJcblx0W1wiNVwiLCBcImRhcmtfcHVycGxlXCJdLFxyXG5cdFtcIjZcIiwgXCJnb2xkXCJdLFxyXG5cdFtcIjdcIiwgXCJncmF5XCJdLFxyXG5cdFtcIjhcIiwgXCJkYXJrX2dyYXlcIl0sXHJcblx0W1wiOVwiLCBcImJsdWVcIl0sXHJcblx0W1wiYVwiLCBcImdyZWVuXCJdLFxyXG5cdFtcImJcIiwgXCJhcXVhXCJdLFxyXG5cdFtcImNcIiwgXCJyZWRcIl0sXHJcblx0W1wiZFwiLCBcImxpZ2h0X3B1cnBsZVwiXSxcclxuXHRbXCJlXCIsIFwieWVsbG93XCJdLFxyXG5cdFtcImZcIiwgXCJ3aGl0ZVwiXVxyXG5dKTtcclxuXHJcbmNvbnN0IENoYXRDb2xvckNTU1JldmVyc2VkOiBNYXA8c3RyaW5nLCBzdHJpbmc+ID0gbmV3IE1hcCgpO1xyXG5mb3IgKGxldCBba2V5LCB2YWx1ZV0gb2YgQ2hhdENvbG9yQ1NTKSB7XHJcblx0Q2hhdENvbG9yQ1NTUmV2ZXJzZWQuc2V0KHZhbHVlLCBrZXkpO1xyXG59XHJcblxyXG5jb25zdCBBcmd1bWVudENvbG9yczogeyBbY29sb3JJbmRleDogbnVtYmVyXTogU3RyaW5nIH0gPSB7XHJcblx0MDogQ2hhdENvbG9yLkFRVUEsXHJcblx0MTogQ2hhdENvbG9yLllFTExPVyxcclxuXHQyOiBDaGF0Q29sb3IuR1JFRU4sXHJcblx0MzogQ2hhdENvbG9yLkxJR0hUX1BVUlBMRSxcclxuXHQ0OiBDaGF0Q29sb3IuR09MRFxyXG59IGFzIGNvbnN0O1xyXG5cclxuLy8gQXMgaW1wbGVtZW50ZWQgYnkgaHR0cHM6Ly9jb21tYW5kYXBpLmpvcmVsLmRldi84LjUuMS9pbnRlcm5hbC5odG1sXHJcbmNvbnN0IEFyZ3VtZW50VHlwZSA9IG5ldyBNYXA8c3RyaW5nLCAoKSA9PiBCcmlnYWRpZXJBcmd1bWVudFR5cGU8dW5rbm93bj4+KFtcclxuXHQvLyBDb21tYW5kQVBJIHNlcGFyYXRpb24uIFRoZXNlIGFyZSB0aGUgdmFyaW91cyBFbnRpdHlTZWxlY3RvckFyZ3VtZW50PD4gdHlwZXNcclxuXHRbXCJhcGk6ZW50aXR5XCIsICgpID0+IG5ldyBFbnRpdHlTZWxlY3RvckFyZ3VtZW50KHRydWUsIGZhbHNlKV0sXHJcblx0W1wiYXBpOmVudGl0aWVzXCIsICgpID0+IG5ldyBFbnRpdHlTZWxlY3RvckFyZ3VtZW50KGZhbHNlLCBmYWxzZSldLFxyXG5cdFtcImFwaTpwbGF5ZXJcIiwgKCkgPT4gbmV3IEVudGl0eVNlbGVjdG9yQXJndW1lbnQodHJ1ZSwgdHJ1ZSldLFxyXG5cdFtcImFwaTpwbGF5ZXJzXCIsICgpID0+IG5ldyBFbnRpdHlTZWxlY3RvckFyZ3VtZW50KGZhbHNlLCB0cnVlKV0sXHJcblx0W1wiYXBpOmdyZWVkeV9zdHJpbmdcIiwgKCkgPT4gZ3JlZWR5U3RyaW5nQXJndW1lbnQoKV0sXHJcblxyXG5cdC8vIEEgbm90ZSBhYm91dCBCcmlnYWRpZXIgU3RyaW5nIHR5cGVzOlxyXG5cclxuXHQvLyBCcmlnYWRpZXIgYXJndW1lbnRzXHJcblx0W1wiYnJpZ2FkaWVyOmJvb2xcIiwgKCkgPT4gYm9vbEFyZ3VtZW50KCldLFxyXG5cdFtcImJyaWdhZGllcjpkb3VibGVcIiwgKCkgPT4gZmxvYXRBcmd1bWVudCgpXSxcclxuXHRbXCJicmlnYWRpZXI6ZmxvYXRcIiwgKCkgPT4gZmxvYXRBcmd1bWVudCgpXSxcclxuXHRbXCJicmlnYWRpZXI6aW50ZWdlclwiLCAoKSA9PiBpbnRlZ2VyQXJndW1lbnQoKV0sXHJcblx0W1wiYnJpZ2FkaWVyOmxvbmdcIiwgKCkgPT4gaW50ZWdlckFyZ3VtZW50KCldLFxyXG5cdFtcImJyaWdhZGllcjpzdHJpbmdcIiwgKCkgPT4gc3RyaW5nQXJndW1lbnQoKV0sXHJcblxyXG5cdC8vIE1pbmVjcmFmdCBhcmd1bWVudHNcclxuXHRbXCJtaW5lY3JhZnQ6YW5nbGVcIiwgKCkgPT4gbmV3IEFuZ2xlQXJndW1lbnQoKV0sXHJcblx0W1wibWluZWNyYWZ0OmJsb2NrX3Bvc1wiLCAoKSA9PiBuZXcgQmxvY2tQb3NBcmd1bWVudCgpXSxcclxuXHRbXCJtaW5lY3JhZnQ6YmxvY2tfcHJlZGljYXRlXCIsICgpID0+IG5ldyBVbmltcGxlbWVudGVkQXJndW1lbnQoKV0sXHJcblx0W1wibWluZWNyYWZ0OmJsb2NrX3N0YXRlXCIsICgpID0+IG5ldyBVbmltcGxlbWVudGVkQXJndW1lbnQoKV0sXHJcblx0W1wibWluZWNyYWZ0OmNvbG9yXCIsICgpID0+IG5ldyBDb2xvckFyZ3VtZW50KCldLFxyXG5cdFtcIm1pbmVjcmFmdDpjb2x1bW5fcG9zXCIsICgpID0+IG5ldyBDb2x1bW5Qb3NBcmd1bWVudCgpXSxcclxuXHRbXCJtaW5lY3JhZnQ6Y29tcG9uZW50XCIsICgpID0+IG5ldyBVbmltcGxlbWVudGVkQXJndW1lbnQoKV0sXHJcblx0W1wibWluZWNyYWZ0OmRpbWVuc2lvblwiLCAoKSA9PiBuZXcgVW5pbXBsZW1lbnRlZEFyZ3VtZW50KCldLFxyXG5cdFtcIm1pbmVjcmFmdDplbnRpdHlcIiwgKCkgPT4gbmV3IFVuaW1wbGVtZW50ZWRBcmd1bWVudCgpXSxcclxuXHRbXCJtaW5lY3JhZnQ6ZW50aXR5X2FuY2hvclwiLCAoKSA9PiBuZXcgVW5pbXBsZW1lbnRlZEFyZ3VtZW50KCldLFxyXG5cdFtcIm1pbmVjcmFmdDplbnRpdHlfc3VtbW9uXCIsICgpID0+IG5ldyBVbmltcGxlbWVudGVkQXJndW1lbnQoKV0sXHJcblx0W1wibWluZWNyYWZ0OmZsb2F0X3JhbmdlXCIsICgpID0+IG5ldyBVbmltcGxlbWVudGVkQXJndW1lbnQoKV0sXHJcblx0W1wibWluZWNyYWZ0OmZ1bmN0aW9uXCIsICgpID0+IG5ldyBVbmltcGxlbWVudGVkQXJndW1lbnQoKV0sXHJcblx0W1wibWluZWNyYWZ0OmdhbWVfcHJvZmlsZVwiLCAoKSA9PiBuZXcgUGxheWVyQXJndW1lbnQoKV0sXHJcblx0W1wibWluZWNyYWZ0OmludF9yYW5nZVwiLCAoKSA9PiBuZXcgVW5pbXBsZW1lbnRlZEFyZ3VtZW50KCldLFxyXG5cdFtcIm1pbmVjcmFmdDppdGVtX2VuY2hhbnRtZW50XCIsICgpID0+IG5ldyBFbmNoYW50bWVudEFyZ3VtZW50KCldLFxyXG5cdFtcIm1pbmVjcmFmdDppdGVtX3ByZWRpY2F0ZVwiLCAoKSA9PiBuZXcgVW5pbXBsZW1lbnRlZEFyZ3VtZW50KCldLFxyXG5cdFtcIm1pbmVjcmFmdDppdGVtX3Nsb3RcIiwgKCkgPT4gbmV3IFVuaW1wbGVtZW50ZWRBcmd1bWVudCgpXSxcclxuXHRbXCJtaW5lY3JhZnQ6aXRlbV9zdGFja1wiLCAoKSA9PiBuZXcgVW5pbXBsZW1lbnRlZEFyZ3VtZW50KCldLFxyXG5cdFtcIm1pbmVjcmFmdDptZXNzYWdlXCIsICgpID0+IGdyZWVkeVN0cmluZ0FyZ3VtZW50KCldLCAvLyBDbG9zZSBlbm91Z2hcclxuXHRbXCJtaW5lY3JhZnQ6bW9iX2VmZmVjdFwiLCAoKSA9PiBuZXcgUG90aW9uRWZmZWN0QXJndW1lbnQoKV0sXHJcblx0W1wibWluZWNyYWZ0Om5idFwiLCAoKSA9PiBuZXcgVW5pbXBsZW1lbnRlZEFyZ3VtZW50KCldLFxyXG5cdFtcIm1pbmVjcmFmdDpuYnRfY29tcG91bmRfdGFnXCIsICgpID0+IG5ldyBOQlRDb21wb3VuZEFyZ3VtZW50KCldLFxyXG5cdFtcIm1pbmVjcmFmdDpuYnRfcGF0aFwiLCAoKSA9PiBuZXcgVW5pbXBsZW1lbnRlZEFyZ3VtZW50KCldLFxyXG5cdFtcIm1pbmVjcmFmdDpuYnRfdGFnXCIsICgpID0+IG5ldyBVbmltcGxlbWVudGVkQXJndW1lbnQoKV0sXHJcblx0W1wibWluZWNyYWZ0Om9iamVjdGl2ZVwiLCAoKSA9PiBuZXcgVW5pbXBsZW1lbnRlZEFyZ3VtZW50KCldLFxyXG5cdFtcIm1pbmVjcmFmdDpvYmplY3RpdmVfY3JpdGVyaWFcIiwgKCkgPT4gbmV3IFVuaW1wbGVtZW50ZWRBcmd1bWVudCgpXSxcclxuXHRbXCJtaW5lY3JhZnQ6b3BlcmF0aW9uXCIsICgpID0+IG5ldyBNYXRoT3BlcmF0aW9uQXJndW1lbnQoKV0sXHJcblx0W1wibWluZWNyYWZ0OnBhcnRpY2xlXCIsICgpID0+IG5ldyBVbmltcGxlbWVudGVkQXJndW1lbnQoKV0sXHJcblx0W1wibWluZWNyYWZ0OnJlc291cmNlX2xvY2F0aW9uXCIsICgpID0+IG5ldyBVbmltcGxlbWVudGVkQXJndW1lbnQoKV0sXHJcblx0W1wibWluZWNyYWZ0OnJvdGF0aW9uXCIsICgpID0+IG5ldyBVbmltcGxlbWVudGVkQXJndW1lbnQoKV0sXHJcblx0W1wibWluZWNyYWZ0OnNjb3JlX2hvbGRlclwiLCAoKSA9PiBuZXcgVW5pbXBsZW1lbnRlZEFyZ3VtZW50KCldLFxyXG5cdFtcIm1pbmVjcmFmdDpzY29yZWJvYXJkX3Nsb3RcIiwgKCkgPT4gbmV3IFVuaW1wbGVtZW50ZWRBcmd1bWVudCgpXSxcclxuXHRbXCJtaW5lY3JhZnQ6c3dpenpsZVwiLCAoKSA9PiBuZXcgVW5pbXBsZW1lbnRlZEFyZ3VtZW50KCldLFxyXG5cdFtcIm1pbmVjcmFmdDp0ZWFtXCIsICgpID0+IHNpbmdsZVdvcmRBcmd1bWVudCgpXSxcclxuXHRbXCJtaW5lY3JhZnQ6dGltZVwiLCAoKSA9PiBuZXcgVGltZUFyZ3VtZW50KCldLFxyXG5cdFtcIm1pbmVjcmFmdDp1dWlkXCIsICgpID0+IG5ldyBVVUlEQXJndW1lbnQoKV0sXHJcblx0W1wibWluZWNyYWZ0OnZlYzJcIiwgKCkgPT4gbmV3IFVuaW1wbGVtZW50ZWRBcmd1bWVudCgpXSxcclxuXHRbXCJtaW5lY3JhZnQ6dmVjM1wiLCAoKSA9PiBuZXcgVW5pbXBsZW1lbnRlZEFyZ3VtZW50KCldLFxyXG5dKTtcclxuXHJcbi8qKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKipcclxuICogSGVscGVycyAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgKlxyXG4gKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqL1xyXG5cclxuLyoqXHJcbiAqIFJlZ2lzdGVycyBhIGNvbW1hbmQgaW50byB0aGUgZ2xvYmFsIGNvbW1hbmQgZGlzcGF0Y2hlclxyXG4gKiBAcGFyYW0ge3N0cmluZ30gY29uZmlnQ29tbWFuZCB0aGUgY29tbWFuZCB0byByZWdpc3RlciwgYXMgZGVjbGFyZWQgdXNpbmcgdGhlXHJcbiAqIENvbW1hbmRBUEkgY29uZmlnLnltbCdzIGNvbW1hbmQgZGVjbGFyYXRpb24gc3ludGF4IChTZWVcclxuICogaHR0cHM6Ly9jb21tYW5kYXBpLmpvcmVsLmRldi84LjUuMS9jb252ZXJzaW9uZm9yb3duZXJzc2luZ2xlYXJncy5odG1sKVxyXG4gKi9cclxuZnVuY3Rpb24gcmVnaXN0ZXJDb21tYW5kKGNvbmZpZ0NvbW1hbmQ6IHN0cmluZykge1xyXG5cclxuXHQvLyBObyBibGFuayBjb21tYW5kc1xyXG5cdGlmIChjb25maWdDb21tYW5kLnRyaW0oKS5sZW5ndGggPT09IDApIHtcclxuXHRcdHJldHVybjtcclxuXHR9XHJcblxyXG5cdGZ1bmN0aW9uIGNvbnZlcnRBcmd1bWVudChhcmd1bWVudFR5cGU6IHN0cmluZyk6IEJyaWdhZGllckFyZ3VtZW50VHlwZTx1bmtub3duPiB7XHJcblx0XHRpZiAoYXJndW1lbnRUeXBlLmluY2x1ZGVzKFwiLi5cIikpIHtcclxuXHRcdFx0bGV0IGxvd2VyQm91bmQ6IHN0cmluZyA9IGFyZ3VtZW50VHlwZS5zcGxpdChcIi4uXCIpWzBdITtcclxuXHRcdFx0bGV0IHVwcGVyQm91bmQ6IHN0cmluZyA9IGFyZ3VtZW50VHlwZS5zcGxpdChcIi4uXCIpWzFdITtcclxuXHJcblx0XHRcdGxldCBsb3dlckJvdW5kTnVtOiBudW1iZXIgPSBOdW1iZXIuTUlOX1NBRkVfSU5URUdFUjtcclxuXHRcdFx0bGV0IHVwcGVyQm91bmROdW06IG51bWJlciA9IE51bWJlci5NQVhfU0FGRV9JTlRFR0VSO1xyXG5cclxuXHRcdFx0aWYgKGxvd2VyQm91bmQubGVuZ3RoID09PSAwKSB7XHJcblx0XHRcdFx0bG93ZXJCb3VuZE51bSA9IE51bWJlci5NSU5fU0FGRV9JTlRFR0VSO1xyXG5cdFx0XHR9IGVsc2Uge1xyXG5cdFx0XHRcdGxvd2VyQm91bmROdW0gPSBOdW1iZXIucGFyc2VGbG9hdChsb3dlckJvdW5kKTtcclxuXHRcdFx0fVxyXG5cclxuXHRcdFx0aWYgKHVwcGVyQm91bmQubGVuZ3RoID09PSAwKSB7XHJcblx0XHRcdFx0dXBwZXJCb3VuZE51bSA9IE51bWJlci5NQVhfU0FGRV9JTlRFR0VSO1xyXG5cdFx0XHR9IGVsc2Uge1xyXG5cdFx0XHRcdHVwcGVyQm91bmROdW0gPSBOdW1iZXIucGFyc2VGbG9hdCh1cHBlckJvdW5kKTtcclxuXHRcdFx0fVxyXG5cclxuXHRcdFx0Ly8gV2UndmUgZ290IGEgZGVjaW1hbCBudW1iZXIsIHVzZSBhIGZsb2F0IGFyZ3VtZW50XHJcblx0XHRcdGlmIChsb3dlckJvdW5kTnVtICUgMSAhPT0gMCB8fCB1cHBlckJvdW5kTnVtICUgMSAhPT0gMCkge1xyXG5cdFx0XHRcdHJldHVybiBmbG9hdEFyZ3VtZW50KGxvd2VyQm91bmROdW0sIHVwcGVyQm91bmROdW0pO1xyXG5cdFx0XHR9IGVsc2Uge1xyXG5cdFx0XHRcdC8vIEluY2x1c2l2ZSB1cHBlciBib3VuZFxyXG5cdFx0XHRcdHVwcGVyQm91bmROdW0gKz0gMTtcclxuXHRcdFx0XHRyZXR1cm4gaW50ZWdlckFyZ3VtZW50KGxvd2VyQm91bmROdW0sIHVwcGVyQm91bmROdW0pO1xyXG5cdFx0XHR9XHJcblx0XHR9IGVsc2Uge1xyXG5cdFx0XHRjb25zdCBhcmd1bWVudEdlbmVyYXRvckZ1bmN0aW9uID0gQXJndW1lbnRUeXBlLmdldChhcmd1bWVudFR5cGUpO1xyXG5cdFx0XHRpZihhcmd1bWVudEdlbmVyYXRvckZ1bmN0aW9uID09PSB1bmRlZmluZWQpIHtcclxuXHRcdFx0XHR0aHJvdyBuZXcgRXJyb3IoXCJBcmd1bWVudCB0eXBlIFwiICsgYXJndW1lbnRUeXBlICsgXCIgZG9lc24ndCBleGlzdFwiKTtcclxuXHRcdFx0fVxyXG5cdFx0XHRjb25zdCBhcmd1bWVudEdlbmVyYXRvckZ1bmN0aW9uUmVzdWx0OiBCcmlnYWRpZXJBcmd1bWVudFR5cGU8dW5rbm93bj4gfCBudWxsID0gYXJndW1lbnRHZW5lcmF0b3JGdW5jdGlvbigpO1xyXG5cdFx0XHRpZiAoYXJndW1lbnRHZW5lcmF0b3JGdW5jdGlvblJlc3VsdCAhPT0gbnVsbCkge1xyXG5cdFx0XHRcdHJldHVybiBhcmd1bWVudEdlbmVyYXRvckZ1bmN0aW9uUmVzdWx0O1xyXG5cdFx0XHR9IGVsc2Uge1xyXG5cdFx0XHRcdHRocm93IG5ldyBFcnJvcihcIlVuaW1wbGVtZW50ZWQgYXJndW1lbnQ6IFwiICsgYXJndW1lbnRUeXBlKTtcclxuXHRcdFx0fVxyXG5cdFx0fVxyXG5cdH1cclxuXHJcblx0Y29uc3QgY29tbWFuZDogc3RyaW5nIHwgdW5kZWZpbmVkID0gY29uZmlnQ29tbWFuZC5zcGxpdChcIiBcIilbMF07XHJcblx0aWYoY29tbWFuZCA9PT0gdW5kZWZpbmVkKSB7XHJcblx0XHR0aHJvdyBuZXcgRXJyb3IoXCJDb21tYW5kIG5hbWUgZG9lc24ndCBleGlzdCFcIilcclxuXHR9XHJcblx0Y29uc3QgYXJnczogc3RyaW5nW10gPSBjb25maWdDb21tYW5kLnNwbGl0KFwiIFwiKS5zbGljZSgxKTtcclxuXHJcblx0bGV0IGNvbW1hbmRUb1JlZ2lzdGVyOiBMaXRlcmFsQXJndW1lbnRCdWlsZGVyPFNvdXJjZT4gPSBsaXRlcmFsQXJndW1lbnQoY29tbWFuZCk7XHJcblx0bGV0IGFyZ3VtZW50c1RvUmVnaXN0ZXI6IEFycmF5PEFyZ3VtZW50QnVpbGRlcjxTb3VyY2UsIGFueT4+ID0gW107XHJcblxyXG5cdC8vIEZyb20gZGV2L2pvcmVsL2NvbW1hbmRhcGkvQWR2YW5jZWRDb252ZXJ0ZXIuamF2YVxyXG5cdGNvbnN0IGxpdGVyYWxQYXR0ZXJuOiBSZWdFeHAgPSBSZWdFeHAoL1xcKChcXHcrKD86XFx8XFx3KykqKVxcKS8pO1xyXG5cdGNvbnN0IGFyZ3VtZW50UGF0dGVybjogUmVnRXhwID0gUmVnRXhwKC88KFxcdyspPlxcWyhbYS16Ol9dK3woPzpbMC05XFwuXSspP1xcLlxcLig/OlswLTlcXC5dKyk/KVxcXS8pO1xyXG5cclxuXHRmb3IgKGxldCBhcmcgb2YgYXJncykge1xyXG5cdFx0Y29uc3QgbWF0Y2hlZExpdGVyYWw6IFJlZ0V4cE1hdGNoQXJyYXkgfCBudWxsID0gYXJnLm1hdGNoKGxpdGVyYWxQYXR0ZXJuKTtcclxuXHRcdGNvbnN0IG1hdGNoZWRBcmd1bWVudDogUmVnRXhwTWF0Y2hBcnJheSB8IG51bGwgPSBhcmcubWF0Y2goYXJndW1lbnRQYXR0ZXJuKTtcclxuXHRcdGlmIChtYXRjaGVkTGl0ZXJhbCAhPT0gbnVsbCkge1xyXG5cdFx0XHQvLyBJdCdzIGEgbGl0ZXJhbCBhcmd1bWVudFxyXG5cdFx0XHRjb25zdCBsaXRlcmFsczogc3RyaW5nW10gPSBtYXRjaGVkTGl0ZXJhbFsxXSEuc3BsaXQoXCJ8XCIpO1xyXG5cdFx0XHRpZiAobGl0ZXJhbHMubGVuZ3RoID09PSAxKSB7XHJcblx0XHRcdFx0YXJndW1lbnRzVG9SZWdpc3Rlci51bnNoaWZ0KGxpdGVyYWxBcmd1bWVudChsaXRlcmFsc1swXSEpKTtcclxuXHRcdFx0fSBlbHNlIGlmIChsaXRlcmFscy5sZW5ndGggPiAxKSB7XHJcblx0XHRcdFx0YXJndW1lbnRzVG9SZWdpc3Rlci51bnNoaWZ0KGFyZ3VtZW50KG1hdGNoZWRMaXRlcmFsWzFdISwgbmV3IE11bHRpTGl0ZXJhbEFyZ3VtZW50KGxpdGVyYWxzKSkpO1xyXG5cdFx0XHR9XHJcblx0XHR9IGVsc2UgaWYgKG1hdGNoZWRBcmd1bWVudCAhPT0gbnVsbCkge1xyXG5cdFx0XHQvLyBJdCdzIGEgcmVndWxhciBhcmd1bWVudFxyXG5cdFx0XHRjb25zdCBub2RlTmFtZTogc3RyaW5nID0gbWF0Y2hlZEFyZ3VtZW50WzFdITtcclxuXHRcdFx0Y29uc3QgYXJndW1lbnRUeXBlOiBzdHJpbmcgPSBtYXRjaGVkQXJndW1lbnRbMl0hO1xyXG5cclxuXHRcdFx0bGV0IGNvbnZlcnRlZEFyZ3VtZW50VHlwZTogQnJpZ2FkaWVyQXJndW1lbnRUeXBlPHVua25vd24+ID0gY29udmVydEFyZ3VtZW50KGFyZ3VtZW50VHlwZSk7XHJcblxyXG5cdFx0XHQvLyBXZSdyZSBhZGRpbmcgYXJndW1lbnRzIGluIHJldmVyc2Ugb3JkZXIgKGxhc3QgYXJndW1lbnRzIGFwcGVhclxyXG5cdFx0XHQvLyBhdCB0aGUgYmVnaW5uaW5nIG9mIHRoZSBhcnJheSkgYmVjYXVzZSBpdCdzIG11Y2ggbXVjaCBlYXNpZXIgdG8gcHJvY2Vzc1xyXG5cdFx0XHRhcmd1bWVudHNUb1JlZ2lzdGVyLnVuc2hpZnQoYXJndW1lbnQobm9kZU5hbWUsIGNvbnZlcnRlZEFyZ3VtZW50VHlwZSkpO1xyXG5cdFx0fSBlbHNlIHtcclxuXHRcdFx0dGhyb3cgbmV3IEVycm9yKGAke2FyZ30gaGFzIGludmFsaWQgc3ludGF4ISBWYWxpZCBzeW50YXggZXhhbXBsZXM6IChsaXRlcmFsfGxpdGVyYWwpLCA8YXJnPlttaW5lY3JhZnQ6XWApO1xyXG5cdFx0fVxyXG5cdH1cclxuXHJcblx0aWYgKGFyZ3VtZW50c1RvUmVnaXN0ZXIubGVuZ3RoID4gMCkge1xyXG5cdFx0Y29uc3QgbGFzdEFyZ3VtZW50OiBCcmlnYWRpZXJBcmd1bWVudFR5cGU8dW5rbm93bj4gPSBhcmd1bWVudHNUb1JlZ2lzdGVyWzBdIS5leGVjdXRlcyhfY29udGV4dCA9PiAwKTtcclxuXHJcblx0XHQvLyBGbGFtZSBvbi4gUmVkdWNlLlxyXG5cdFx0YXJndW1lbnRzVG9SZWdpc3Rlci5zaGlmdCgpO1xyXG5cdFx0Y29uc3QgcmVkdWNlZEFyZ3VtZW50cyA9IGFyZ3VtZW50c1RvUmVnaXN0ZXIucmVkdWNlKChwcmV2OiBBcmd1bWVudEJ1aWxkZXI8U291cmNlLCBhbnk+LCBjdXJyZW50OiBBcmd1bWVudEJ1aWxkZXI8U291cmNlLCBhbnk+KSA9PiBjdXJyZW50LnRoZW4ocHJldiksIGxhc3RBcmd1bWVudCk7XHJcblx0XHRjb21tYW5kVG9SZWdpc3RlciA9IGNvbW1hbmRUb1JlZ2lzdGVyLnRoZW4ocmVkdWNlZEFyZ3VtZW50cyk7XHJcblx0fVxyXG5cclxuXHRkaXNwYXRjaGVyLnJlZ2lzdGVyKGNvbW1hbmRUb1JlZ2lzdGVyKTtcclxuXHQvLyBwbHVnaW5zLXRvLWNvbnZlcnQ6XHJcblx0Ly8gICAtIEVzc2VudGlhbHM6XHJcblx0Ly8gICAgIC0gc3BlZWQgPHNwZWVkPlswLi4xMF1cclxuXHQvLyAgICAgLSBzcGVlZCA8dGFyZ2V0PlttaW5lY3JhZnQ6Z2FtZV9wcm9maWxlXVxyXG5cdC8vICAgICAtIHNwZWVkICh3YWxrfGZseSkgPHNwZWVkPlswLi4xMF1cclxuXHQvLyAgICAgLSBzcGVlZCAod2Fsa3xmbHkpIDxzcGVlZD5bMC4uMTBdIDx0YXJnZXQ+W21pbmVjcmFmdDpnYW1lX3Byb2ZpbGVdXHJcbn1cclxuXHJcbi8qKlxyXG4gKiBHZXRzIHRoZSBjdXJyZW50IGN1cnNvciBwb3NpdGlvbi5cclxuICpcclxuICogRnJvbSBodHRwczovL3RoZXdlYmRldi5pbmZvLzIwMjIvMDQvMjQvaG93LXRvLWdldC1jb250ZW50ZWRpdGFibGUtY2FyZXQtcG9zaXRpb24td2l0aC1qYXZhc2NyaXB0L1xyXG4gKiBAcmV0dXJucyBUaGUgY3VycmVudCBjdXJzb3IgcG9zaXRpb24gZm9yIHRoZSBjdXJyZW50IGVsZW1lbnRcclxuICovXHJcbmZ1bmN0aW9uIGdldEN1cnNvclBvc2l0aW9uKCkge1xyXG5cdGNvbnN0IHNlbDogU2VsZWN0aW9uV2l0aE1vZGlmeSA9IGRvY3VtZW50LmdldFNlbGVjdGlvbigpIGFzIFNlbGVjdGlvbldpdGhNb2RpZnk7XHJcblx0c2VsLm1vZGlmeShcImV4dGVuZFwiLCBcImJhY2t3YXJkXCIsIFwibGluZWJvdW5kYXJ5XCIpO1xyXG5cdGNvbnN0IHBvcyA9IHNlbC50b1N0cmluZygpLmxlbmd0aDtcclxuXHRpZiAoc2VsLmFuY2hvck5vZGUgIT09IHVuZGVmaW5lZCAmJiBzZWwuYW5jaG9yTm9kZSAhPT0gbnVsbCkge1xyXG5cdFx0c2VsLmNvbGxhcHNlVG9FbmQoKTtcclxuXHR9XHJcblx0cmV0dXJuIHBvcztcclxufTtcclxuXHJcbi8qKlxyXG4gKiBTZXRzIHRoZSBjdXJyZW50IGN1cnNvciBwb3NpdGlvbi4gVGhpcyBoYXMgdG8gdGFrZSBpbnRvIGFjY291bnQgdGhlIGZhY3RcclxuICogdGhhdCB0aGUgY3VycmVudCBlbGVtZW50IGlzIG1hZGUgdXAgb2YgbWFueSBIVE1MIGVsZW1lbnRzIGluc3RlYWQgb2ZcclxuICogcGxhaW50ZXh0LCBzbyBzZWxlY3Rpb24gcmFuZ2VzIGhhdmUgdG8gc3BhbiBhY3Jvc3MgdGhvc2UgZWxlbWVudHMgdG8gZmluZFxyXG4gKiB0aGUgZXhhY3QgbG9jYXRpb24geW91J3JlIGxvb2tpbmcgZm9yLlxyXG4gKlxyXG4gKiBGcm9tIGh0dHBzOi8vc3RhY2tvdmVyZmxvdy5jb20vYS80MTAzNDY5Ny80Nzc5MDcxXHJcbiAqIEBwYXJhbSBpbmRleCB0aGUgbnVtYmVyIG9mIGNoYXJhY3RlcnMgaW50byB0aGUgY3VycmVudCBlbGVtZW50XHJcbiAqICAgICAgICAgICAgICAgICAgICAgICB0byBwbGFjZSB0aGUgY3Vyc29yIGF0XHJcbiAqIEBwYXJhbSBlbGVtZW50IHRoZSBlbGVtZW50IHRvIHNldCB0aGUgY3Vyc29yIGZvclxyXG4gKi9cclxuZnVuY3Rpb24gc2V0Q3Vyc29yUG9zaXRpb24oaW5kZXg6IG51bWJlciwgZWxlbWVudDogTm9kZSk6IHZvaWQge1xyXG5cdGlmIChpbmRleCA+IDApIHtcclxuXHRcdGNvbnN0IGNyZWF0ZVJhbmdlID0gKG5vZGU6IE5vZGUsIGNoYXJzOiB7IGNvdW50OiBudW1iZXIgfSwgcmFuZ2U/OiBSYW5nZSk6IFJhbmdlID0+IHtcclxuXHRcdFx0aWYgKCFyYW5nZSkge1xyXG5cdFx0XHRcdHJhbmdlID0gZG9jdW1lbnQuY3JlYXRlUmFuZ2UoKTtcclxuXHRcdFx0XHRyYW5nZS5zZWxlY3ROb2RlKG5vZGUpO1xyXG5cdFx0XHRcdHJhbmdlLnNldFN0YXJ0KG5vZGUsIDApO1xyXG5cdFx0XHR9XHJcblxyXG5cdFx0XHRpZiAoY2hhcnMuY291bnQgPT09IDApIHtcclxuXHRcdFx0XHRyYW5nZS5zZXRFbmQobm9kZSwgY2hhcnMuY291bnQpO1xyXG5cdFx0XHR9IGVsc2UgaWYgKG5vZGUgJiYgY2hhcnMuY291bnQgPiAwKSB7XHJcblx0XHRcdFx0aWYgKG5vZGUubm9kZVR5cGUgPT09IE5vZGUuVEVYVF9OT0RFKSB7XHJcblx0XHRcdFx0XHRjb25zdCBub2RlVGV4dENvbnRlbnRMZW5ndGg6IG51bWJlciA9IG5vZGUudGV4dENvbnRlbnQ/Lmxlbmd0aCA/PyAwO1xyXG5cdFx0XHRcdFx0aWYgKG5vZGVUZXh0Q29udGVudExlbmd0aCA8IGNoYXJzLmNvdW50KSB7XHJcblx0XHRcdFx0XHRcdGNoYXJzLmNvdW50IC09IG5vZGVUZXh0Q29udGVudExlbmd0aDtcclxuXHRcdFx0XHRcdH0gZWxzZSB7XHJcblx0XHRcdFx0XHRcdHJhbmdlLnNldEVuZChub2RlLCBjaGFycy5jb3VudCk7XHJcblx0XHRcdFx0XHRcdGNoYXJzLmNvdW50ID0gMDtcclxuXHRcdFx0XHRcdH1cclxuXHRcdFx0XHR9IGVsc2Uge1xyXG5cdFx0XHRcdFx0Zm9yIChsZXQgbHA6IG51bWJlciA9IDA7IGxwIDwgbm9kZS5jaGlsZE5vZGVzLmxlbmd0aDsgbHArKykge1xyXG5cdFx0XHRcdFx0XHRyYW5nZSA9IGNyZWF0ZVJhbmdlKG5vZGUuY2hpbGROb2Rlc1tscF0hLCBjaGFycywgcmFuZ2UpO1xyXG5cclxuXHRcdFx0XHRcdFx0aWYgKGNoYXJzLmNvdW50ID09PSAwKSB7XHJcblx0XHRcdFx0XHRcdFx0YnJlYWs7XHJcblx0XHRcdFx0XHRcdH1cclxuXHRcdFx0XHRcdH1cclxuXHRcdFx0XHR9XHJcblx0XHRcdH1cclxuXHJcblx0XHRcdHJldHVybiByYW5nZTtcclxuXHRcdH07XHJcblxyXG5cdFx0Ly8gV2Ugd3JhcCBpbmRleCBpbiBhbiBvYmplY3Qgc28gdGhhdCByZWN1cnNpdmUgY2FsbHMgY2FuIHVzZSB0aGVcclxuXHRcdC8vIFwibmV3XCIgdmFsdWUgd2hpY2ggaXMgdXBkYXRlZCBpbnNpZGUgdGhlIG9iamVjdCBpdHNlbGZcclxuXHRcdGxldCByYW5nZTogUmFuZ2UgPSBjcmVhdGVSYW5nZShlbGVtZW50LCB7IGNvdW50OiBpbmRleCB9KTtcclxuXHJcblx0XHRpZiAocmFuZ2UpIHtcclxuXHRcdFx0cmFuZ2UuY29sbGFwc2UoZmFsc2UpO1xyXG5cdFx0XHRsZXQgc2VsZWN0aW9uOiBTZWxlY3Rpb24gfCBudWxsID0gd2luZG93LmdldFNlbGVjdGlvbigpO1xyXG5cdFx0XHRpZihzZWxlY3Rpb24gIT09IG51bGwpIHtcclxuXHRcdFx0XHRzZWxlY3Rpb24ucmVtb3ZlQWxsUmFuZ2VzKCk7XHJcblx0XHRcdFx0c2VsZWN0aW9uLmFkZFJhbmdlKHJhbmdlKTtcclxuXHRcdFx0fVxyXG5cdFx0fVxyXG5cdH1cclxufTtcclxuXHJcbmZ1bmN0aW9uIGdldFNlbGVjdGVkU3VnZ2VzdGlvbigpOiBIVE1MRWxlbWVudCB7XHJcblx0cmV0dXJuIGRvY3VtZW50LnF1ZXJ5U2VsZWN0b3IoXCIueWVsbG93XCIpITtcclxufVxyXG5cclxudHlwZSBDYWNoZWRGb250SFRNTEVsZW1lbnQgPSBIVE1MRWxlbWVudCAmIHsgY3VycmVudEZvbnQ6IHN0cmluZyB9XHJcblxyXG5jbGFzcyBUZXh0V2lkdGgge1xyXG5cclxuXHRwcml2YXRlIHN0YXRpYyBjYW52YXM6IEhUTUxDYW52YXNFbGVtZW50O1xyXG5cclxuXHQvKipcclxuXHQgKiBVc2VzIGNhbnZhcy5tZWFzdXJlVGV4dCB0byBjb21wdXRlIGFuZCByZXR1cm4gdGhlIHdpZHRoIG9mIHRoZSBnaXZlbiB0ZXh0IG9mIGdpdmVuIGZvbnQgaW4gcGl4ZWxzLlxyXG5cdCAqIFxyXG5cdCAqIEBwYXJhbSB7U3RyaW5nfSB0ZXh0IFRoZSB0ZXh0IHRvIGJlIHJlbmRlcmVkLlxyXG5cdCAqIEBwYXJhbSB7SFRNTEVsZW1lbnR9IGVsZW1lbnQgdGhlIGVsZW1lbnRcclxuXHQgKiBcclxuXHQgKiBAc2VlIGh0dHBzOi8vc3RhY2tvdmVyZmxvdy5jb20vcXVlc3Rpb25zLzExODI0MS9jYWxjdWxhdGUtdGV4dC13aWR0aC13aXRoLWphdmFzY3JpcHQvMjEwMTUzOTMjMjEwMTUzOTNcclxuXHQgKi9cclxuXHRzdGF0aWMgZ2V0VGV4dFdpZHRoKHRleHQ6IHN0cmluZywgZWxlbWVudDogQ2FjaGVkRm9udEhUTUxFbGVtZW50KTogbnVtYmVyIHtcclxuXHRcdC8vIHJlLXVzZSBjYW52YXMgb2JqZWN0IGZvciBiZXR0ZXIgcGVyZm9ybWFuY2VcclxuXHRcdGNvbnN0IGNhbnZhczogSFRNTENhbnZhc0VsZW1lbnQgPSBUZXh0V2lkdGguY2FudmFzIHx8IChUZXh0V2lkdGguY2FudmFzID0gZG9jdW1lbnQuY3JlYXRlRWxlbWVudChcImNhbnZhc1wiKSk7XHJcblx0XHRjb25zdCBjb250ZXh0OiBDYW52YXNSZW5kZXJpbmdDb250ZXh0MkQgPSBjYW52YXMuZ2V0Q29udGV4dChcIjJkXCIpITtcclxuXHJcblx0XHRjb250ZXh0LmZvbnQgPSBlbGVtZW50LmN1cnJlbnRGb250IHx8IChlbGVtZW50LmN1cnJlbnRGb250ID0gVGV4dFdpZHRoLmdldENhbnZhc0ZvbnQoZWxlbWVudCkpO1xyXG5cdFx0cmV0dXJuIGNvbnRleHQubWVhc3VyZVRleHQodGV4dCkud2lkdGg7XHJcblx0fVxyXG5cclxuXHRwcml2YXRlIHN0YXRpYyBnZXRDc3NTdHlsZShlbGVtZW50OiBIVE1MRWxlbWVudCwgcHJvcDogc3RyaW5nKTogc3RyaW5nIHtcclxuXHRcdHJldHVybiB3aW5kb3cuZ2V0Q29tcHV0ZWRTdHlsZShlbGVtZW50KS5nZXRQcm9wZXJ0eVZhbHVlKHByb3ApO1xyXG5cdH1cclxuXHJcblx0cHJpdmF0ZSBzdGF0aWMgZ2V0Q2FudmFzRm9udChlbDogSFRNTEVsZW1lbnQgPSBkb2N1bWVudC5ib2R5KTogc3RyaW5nIHtcclxuXHRcdGNvbnN0IGZvbnRXZWlnaHQgPSBUZXh0V2lkdGguZ2V0Q3NzU3R5bGUoZWwsICdmb250LXdlaWdodCcpIHx8ICdub3JtYWwnO1xyXG5cdFx0Y29uc3QgZm9udFNpemUgPSBUZXh0V2lkdGguZ2V0Q3NzU3R5bGUoZWwsICdmb250LXNpemUnKSB8fCAnMTZweCc7XHJcblx0XHRjb25zdCBmb250RmFtaWx5ID0gVGV4dFdpZHRoLmdldENzc1N0eWxlKGVsLCAnZm9udC1mYW1pbHknKSB8fCAnVGltZXMgTmV3IFJvbWFuJztcclxuXHJcblx0XHRyZXR1cm4gYCR7Zm9udFdlaWdodH0gJHtmb250U2l6ZX0gJHtmb250RmFtaWx5fWA7XHJcblx0fVxyXG5cclxufVxyXG5cclxuLyoqXHJcbiAqIFRha2VzIE1pbmVjcmFmdCB0ZXh0IGFuZCByZW5kZXJzIGl0IGluIHRoZSBjaGF0IGJveC4gVGhpcyB3aWxsIGF1dG9tYXRpY2FsbHlcclxuICogYWRkIHRoZSBsZWFkaW5nIC8gY2hhcmFjdGVyLCBzbyB5b3UgZG9uJ3QgaGF2ZSB0byBkbyB0aGF0IHlvdXJzZWxmIVxyXG4gKiBAcGFyYW0ge3N0cmluZ30gbWluZWNyYWZ0Q29kZWRUZXh0XHJcbiAqIEBwYXJhbSB7SFRNTEVsZW1lbnQgfCBudWxsfSB0YXJnZXRcclxuICovXHJcbmZ1bmN0aW9uIHNldFRleHQobWluZWNyYWZ0Q29kZWRUZXh0OiBzdHJpbmcsIHRhcmdldDogSFRNTEVsZW1lbnQgfCBudWxsID0gbnVsbCkge1xyXG5cdG1pbmVjcmFmdENvZGVkVGV4dCA9IG1pbmVjcmFmdENvZGVkVGV4dC5yZXBsYWNlQWxsKFwiIFwiLCBcIlxcdTAwQTBcIik7IC8vIFJlcGxhY2Ugbm9ybWFsIHNwYWNlcyB3aXRoICZuYnNwOyBmb3IgSFRNTFxyXG5cdGlmICghdGFyZ2V0KSB7XHJcblx0XHR0YXJnZXQgPSBDT01NQU5EX0lOUFVUO1xyXG5cdH1cclxuXHJcblx0Ly8gUmVzZXQgdGhlIHRleHRcclxuXHR0YXJnZXQuaW5uZXJIVE1MID0gXCJcIjtcclxuXHJcblx0aWYgKHRhcmdldCA9PT0gQ09NTUFORF9JTlBVVCkge1xyXG5cdFx0Ly8gQ29tbWFuZCBmb3J3YXJkIHNsYXNoLiBBbHdheXMgcHJlc2VudCwgd2UgZG9uJ3Qgd2FudCB0byByZW1vdmUgdGhpcyFcclxuXHRcdGxldCBlbGVtZW50OiBIVE1MU3BhbkVsZW1lbnQgPSBkb2N1bWVudC5jcmVhdGVFbGVtZW50KFwic3BhblwiKTtcclxuXHRcdGVsZW1lbnQuaW5uZXJUZXh0ID0gXCIvXCI7XHJcblx0XHR0YXJnZXQuYXBwZW5kQ2hpbGQoZWxlbWVudCk7XHJcblx0fVxyXG5cclxuXHRsZXQgYnVmZmVyOiBzdHJpbmcgPSBcIlwiO1xyXG5cdGxldCBjdXJyZW50Q29sb3I6IHN0cmluZyA9IFwiXCI7XHJcblxyXG5cdGZ1bmN0aW9uIHdyaXRlQnVmZmVyKHRhcmdldDogSFRNTEVsZW1lbnQpOiB2b2lkIHtcclxuXHRcdGlmIChidWZmZXIubGVuZ3RoID4gMCkge1xyXG5cdFx0XHRsZXQgZWxlbTogSFRNTFNwYW5FbGVtZW50ID0gZG9jdW1lbnQuY3JlYXRlRWxlbWVudChcInNwYW5cIik7XHJcblx0XHRcdGVsZW0uY2xhc3NOYW1lID0gY3VycmVudENvbG9yO1xyXG5cdFx0XHRlbGVtLmlubmVyVGV4dCA9IGJ1ZmZlcjtcclxuXHRcdFx0dGFyZ2V0LmFwcGVuZENoaWxkKGVsZW0pO1xyXG5cdFx0XHRidWZmZXIgPSBcIlwiO1xyXG5cdFx0fVxyXG5cdH07XHJcblxyXG5cdGZvciAobGV0IGk6IG51bWJlciA9IDA7IGkgPCBtaW5lY3JhZnRDb2RlZFRleHQubGVuZ3RoOyBpKyspIHtcclxuXHRcdGlmIChtaW5lY3JhZnRDb2RlZFRleHRbaV0gPT09IFwiXFx1MDBBN1wiKSB7XHJcblx0XHRcdHdyaXRlQnVmZmVyKHRhcmdldCk7XHJcblx0XHRcdGN1cnJlbnRDb2xvciA9IENoYXRDb2xvckNTUy5nZXQobWluZWNyYWZ0Q29kZWRUZXh0W2kgKyAxXSEpITtcclxuXHRcdFx0aSsrO1xyXG5cdFx0XHRjb250aW51ZTtcclxuXHRcdH0gZWxzZSB7XHJcblx0XHRcdGJ1ZmZlciArPSBtaW5lY3JhZnRDb2RlZFRleHRbaV07XHJcblx0XHR9XHJcblx0fVxyXG5cclxuXHR3cml0ZUJ1ZmZlcih0YXJnZXQpO1xyXG59XHJcblxyXG5mdW5jdGlvbiBnZXRUZXh0KHdpdGhTdHlsaW5nOiBib29sZWFuID0gdHJ1ZSk6IHN0cmluZyB7XHJcblx0bGV0IGJ1ZmZlcjogc3RyaW5nID0gXCJcIjtcclxuXHRmb3IgKGxldCBjaGlsZCBvZiBDT01NQU5EX0lOUFVULmNoaWxkcmVuKSB7XHJcblx0XHRpZiAoY2hpbGQuY2xhc3NOYW1lICYmIHdpdGhTdHlsaW5nKSB7XHJcblx0XHRcdGJ1ZmZlciArPSBcIlxcdTAwQTdcIiArIENoYXRDb2xvckNTU1JldmVyc2VkLmdldChjaGlsZC5jbGFzc05hbWUpO1xyXG5cdFx0fVxyXG5cdFx0YnVmZmVyICs9IChjaGlsZCBhcyBIVE1MRWxlbWVudCkuaW5uZXJUZXh0O1xyXG5cdH1cclxuXHRyZXR1cm4gYnVmZmVyO1xyXG59XHJcblxyXG4vKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqXHJcbiAqIEV2ZW50cyAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICpcclxuICoqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKi9cclxuXHJcbmNvbnN0IG9uQ29tbWFuZElucHV0ID0gYXN5bmMgZnVuY3Rpb24gKCk6IFByb21pc2U8dm9pZD4ge1xyXG5cdGxldCBjdXJzb3JQb3M6IG51bWJlciA9IGdldEN1cnNvclBvc2l0aW9uKCk7XHJcblxyXG5cdGxldCByYXdUZXh0OiBzdHJpbmcgPSBDT01NQU5EX0lOUFVULmlubmVyVGV4dC5yZXBsYWNlKFwiXFxuXCIsIFwiXCIpO1xyXG5cdHJhd1RleHQgPSByYXdUZXh0LnJlcGxhY2VBbGwoXCJcXHUwMGEwXCIsIFwiIFwiKTsgLy8gUmVwbGFjZSAmbmJzcDsgd2l0aCBub3JtYWwgc3BhY2VzIGZvciBCcmlnYWRpZXJcclxuXHJcblx0bGV0IHNob3dVc2FnZVRleHQ6IGJvb2xlYW4gPSBmYWxzZTtcclxuXHRsZXQgZXJyb3JUZXh0OiBzdHJpbmcgPSBcIlwiO1xyXG5cdGxldCBzdWdnZXN0aW9uczogc3RyaW5nW10gPSBbXTtcclxuXHRsZXQgY29tbWFuZFZhbGlkOiBib29sZWFuID0gZmFsc2U7XHJcblxyXG5cdC8vIFJlbmRlciBjb2xvcnNcclxuXHRpZiAocmF3VGV4dC5zdGFydHNXaXRoKFwiL1wiKSkge1xyXG5cdFx0Ly8gUGFyc2UgdGhlIHJhdyB0ZXh0XHJcblx0XHRjb25zdCByYXdUZXh0Tm9TbGFzaDogc3RyaW5nID0gcmF3VGV4dC5zbGljZSgxKTtcclxuXHRcdGNvbnN0IGNvbW1hbmQ6IHN0cmluZyB8IHVuZGVmaW5lZCA9IHJhd1RleHROb1NsYXNoLnNwbGl0KFwiIFwiKVswXTtcclxuXHJcblx0XHQvLyBCcmlnYWRpZXJcclxuXHRcdGNvbnN0IHBhcnNlZENvbW1hbmQ6IFBhcnNlUmVzdWx0czxTb3VyY2U+ID0gZGlzcGF0Y2hlci5wYXJzZShyYXdUZXh0Tm9TbGFzaCwgU09VUkNFKTtcclxuXHRcdGNvbnN0IHBhcnNlZENvbW1hbmROb1RyYWlsaW5nOiBQYXJzZVJlc3VsdHM8U291cmNlPiA9IGRpc3BhdGNoZXIucGFyc2UocmF3VGV4dE5vU2xhc2gudHJpbUVuZCgpLCBTT1VSQ0UpO1xyXG5cdFx0Ly8gY29uc29sZS5sb2cocGFyc2VkQ29tbWFuZCk7XHJcblxyXG5cdFx0bGV0IGxhc3ROb2RlOiBDb21tYW5kTm9kZTxTb3VyY2U+ID0gcGFyc2VkQ29tbWFuZE5vVHJhaWxpbmcuZ2V0Q29udGV4dCgpLmdldFJvb3ROb2RlKCk7XHJcblx0XHRpZiAocGFyc2VkQ29tbWFuZE5vVHJhaWxpbmcuZ2V0Q29udGV4dCgpLmdldE5vZGVzKCkubGVuZ3RoID4gMCkge1xyXG5cdFx0XHRsYXN0Tm9kZSA9IHBhcnNlZENvbW1hbmROb1RyYWlsaW5nLmdldENvbnRleHQoKS5nZXROb2RlcygpW3BhcnNlZENvbW1hbmROb1RyYWlsaW5nLmdldENvbnRleHQoKS5nZXROb2RlcygpLmxlbmd0aCAtIDFdIS5nZXROb2RlKCk7XHJcblx0XHR9XHJcblx0XHRjb25zdCB1c2FnZTogc3RyaW5nID0gZGlzcGF0Y2hlci5nZXRBbGxVc2FnZShsYXN0Tm9kZSwgU09VUkNFLCBmYWxzZSkuam9pbihcIiBcIik7XHJcblxyXG5cdFx0Ly8gUmVzZXQgdGV4dFxyXG5cdFx0c2V0VGV4dChyYXdUZXh0Tm9TbGFzaCk7XHJcblxyXG5cdFx0aWYgKHBhcnNlZENvbW1hbmQuZ2V0RXhjZXB0aW9ucygpLnNpemUgPiAwKSB7XHJcblx0XHRcdC8vIFRoZSBjb21tYW5kIGlzIGludmFsaWQgKHRoZSBjb21tYW5kIGRvZXNuJ3QgZXhpc3QpLiBNYWtlIHRoZSB3aG9sZSB0ZXh0IHJlZC5cclxuXHRcdFx0c2V0VGV4dChDaGF0Q29sb3IuUkVEICsgcmF3VGV4dE5vU2xhc2gpO1xyXG5cclxuXHRcdFx0Y29uc3QgZXhjZXB0aW9uczogTWFwPENvbW1hbmROb2RlPFNvdXJjZT4sIENvbW1hbmRTeW50YXhFeGNlcHRpb24+ID0gcGFyc2VkQ29tbWFuZC5nZXRFeGNlcHRpb25zKCk7XHJcblx0XHRcdGVycm9yVGV4dCA9IGV4Y2VwdGlvbnMuZW50cmllcygpLm5leHQoKS52YWx1ZVsxXS5tZXNzYWdlO1xyXG5cdFx0fSBlbHNlIHtcclxuXHRcdFx0Ly8gQnJpZ2FkaWVyIGlzIFwiaGFwcHlcIiB3aXRoIHRoZSBpbnB1dC4gTGV0J3MgcnVuIGl0IGFuZCBzZWUhXHJcblx0XHRcdHRyeSB7XHJcblx0XHRcdFx0ZGlzcGF0Y2hlci5leGVjdXRlKHBhcnNlZENvbW1hbmQpO1xyXG5cdFx0XHR9IGNhdGNoIChleCkge1xyXG5cdFx0XHRcdHNldFRleHQoQ2hhdENvbG9yLlJFRCArIHJhd1RleHROb1NsYXNoKTtcclxuXHRcdFx0XHRlcnJvclRleHQgPSBleC5tZXNzYWdlO1xyXG5cclxuXHRcdFx0XHQvLyBUT0RPOiBXZSBhY3R1YWxseSBuZWVkIHRvIHRha2UgaW50byBhY2NvdW50IHRoZSBjYXNlIHdoZW4gdGhlXHJcblx0XHRcdFx0Ly8gY29tbWFuZCBJUyBBQ1RVQUxMWSB1bmtub3duIVxyXG5cdFx0XHRcdGlmIChlcnJvclRleHQuc3RhcnRzV2l0aChcIlVua25vd24gY29tbWFuZCBhdCBwb3NpdGlvblwiKSkge1xyXG5cdFx0XHRcdFx0ZXJyb3JUZXh0ID0gdXNhZ2U7XHJcblx0XHRcdFx0XHRzaG93VXNhZ2VUZXh0ID0gdHJ1ZTtcclxuXHRcdFx0XHR9XHJcblx0XHRcdH1cclxuXHJcblx0XHRcdGlmIChlcnJvclRleHQgPT09IFwiXCIpIHtcclxuXHRcdFx0XHRjb21tYW5kVmFsaWQgPSB0cnVlO1xyXG5cdFx0XHR9XHJcblx0XHR9XHJcblxyXG5cdFx0Ly8gQ29sb3JpemUgZXhpc3RpbmcgYXJndW1lbnRzXHJcblx0XHRpZiAoc2hvd1VzYWdlVGV4dCB8fCBjb21tYW5kVmFsaWQpIHtcclxuXHRcdFx0bGV0IG5ld1RleHQ6IHN0cmluZyA9IGNvbW1hbmQgPz8gXCJcIjtcclxuXHRcdFx0bGV0IHBhcnNlZEFyZ3VtZW50SW5kZXg6IG51bWJlciA9IDA7XHJcblx0XHRcdGZvciAoY29uc3QgW19rZXksIHZhbHVlXSBvZiBwYXJzZWRDb21tYW5kLmdldENvbnRleHQoKS5nZXRBcmd1bWVudHMoKSkge1xyXG5cdFx0XHRcdGlmIChwYXJzZWRBcmd1bWVudEluZGV4ID4gT2JqZWN0LmtleXMoQXJndW1lbnRDb2xvcnMpLmxlbmd0aCkge1xyXG5cdFx0XHRcdFx0cGFyc2VkQXJndW1lbnRJbmRleCA9IDA7XHJcblx0XHRcdFx0fVxyXG5cclxuXHRcdFx0XHRuZXdUZXh0ICs9IFwiIFwiO1xyXG5cdFx0XHRcdG5ld1RleHQgKz0gQXJndW1lbnRDb2xvcnNbcGFyc2VkQXJndW1lbnRJbmRleF07XHJcblx0XHRcdFx0bmV3VGV4dCArPSByYXdUZXh0Tm9TbGFzaC5zbGljZSh2YWx1ZS5nZXRSYW5nZSgpLmdldFN0YXJ0KCksIHZhbHVlLmdldFJhbmdlKCkuZ2V0RW5kKCkpO1xyXG5cclxuXHRcdFx0XHRwYXJzZWRBcmd1bWVudEluZGV4Kys7XHJcblx0XHRcdH1cclxuXHRcdFx0bmV3VGV4dCArPSBcIlwiLnBhZEVuZChyYXdUZXh0Tm9TbGFzaC5sZW5ndGggLSByYXdUZXh0Tm9TbGFzaC50cmltRW5kKCkubGVuZ3RoKTtcclxuXHRcdFx0c2V0VGV4dChuZXdUZXh0KTtcclxuXHRcdH1cclxuXHJcblx0XHRjb25zdCBzdWdnZXN0aW9uc1Jlc3VsdDogU3VnZ2VzdGlvbnMgPSBhd2FpdCBkaXNwYXRjaGVyLmdldENvbXBsZXRpb25TdWdnZXN0aW9ucyhwYXJzZWRDb21tYW5kKTtcclxuXHRcdHN1Z2dlc3Rpb25zID0gc3VnZ2VzdGlvbnNSZXN1bHQuZ2V0TGlzdCgpLm1hcCgoeCkgPT4geC5nZXRUZXh0KCkpO1xyXG5cdFx0Ly8gY29uc29sZS5sb2coc3VnZ2VzdGlvbnMpXHJcblx0fVxyXG5cclxuXHQvLyBTZXQgdGhlIGN1cnNvciBiYWNrIHRvIHdoZXJlIGl0IHdhcy4gU2luY2UgY29tbWFuZHMgYWx3YXlzIHN0YXJ0IHdpdGggYVxyXG5cdC8vIGZvcndhcmQgc2xhc2gsIHRoZSBvbmx5IHBvc3NpYmxlIFwic3RhcnRpbmcgY2FyZXQgcG9zaXRpb25cIiBpcyBwb3NpdGlvbiAxXHJcblx0Ly8gKGluIGZyb250IG9mIHRoZSBzbGFzaClcclxuXHRpZiAoY3Vyc29yUG9zID09PSAwICYmIHJhd1RleHQubGVuZ3RoID4gMCkge1xyXG5cdFx0Y3Vyc29yUG9zID0gcmF3VGV4dC5sZW5ndGg7XHJcblx0fVxyXG5cdHNldEN1cnNvclBvc2l0aW9uKGN1cnNvclBvcywgQ09NTUFORF9JTlBVVCk7XHJcblx0Q09NTUFORF9JTlBVVC5mb2N1cygpO1xyXG5cclxuXHQvLyBJZiBhbnkgZXJyb3JzIGFwcGVhciwgZGlzcGxheSB0aGVtXHJcblx0aWYgKGVycm9yVGV4dC5sZW5ndGggIT09IDApIHtcclxuXHRcdHNldFRleHQoZXJyb3JUZXh0LCBFUlJPUl9NRVNTQUdFX0JPWCk7XHJcblx0XHRFUlJPUl9NRVNTQUdFX0JPWC5oaWRkZW4gPSBmYWxzZTtcclxuXHR9IGVsc2Uge1xyXG5cdFx0RVJST1JfTUVTU0FHRV9CT1guaGlkZGVuID0gdHJ1ZTtcclxuXHR9XHJcblxyXG5cdGlmIChzaG93VXNhZ2VUZXh0KSB7XHJcblx0XHRFUlJPUl9NRVNTQUdFX0JPWC5zdHlsZS5sZWZ0ID0gVGV4dFdpZHRoLmdldFRleHRXaWR0aChyYXdUZXh0LCBDT01NQU5EX0lOUFVUIGFzIENhY2hlZEZvbnRIVE1MRWxlbWVudCkgKyBcInB4XCI7XHJcblx0XHQvLyA4cHggcGFkZGluZywgMTBweCBtYXJnaW4gbGVmdCwgMTBweCBtYXJnaW4gcmlnaHQgPSAtMjhweFxyXG5cdFx0Ly8gUGx1cyBhbiBleHRyYSAxMHB4IGZvciBnb29kIGx1Y2ssIHdoeSBub3RcclxuXHRcdEVSUk9SX01FU1NBR0VfQk9YLnN0eWxlLndpZHRoID0gYGNhbGMoMTAwJSAtICR7RVJST1JfTUVTU0FHRV9CT1guc3R5bGUubGVmdH0gLSAyOHB4ICsgMTBweClgO1xyXG5cdH0gZWxzZSB7XHJcblx0XHRFUlJPUl9NRVNTQUdFX0JPWC5zdHlsZS5sZWZ0ID0gXCIwXCI7XHJcblx0XHRFUlJPUl9NRVNTQUdFX0JPWC5zdHlsZS53aWR0aCA9IFwidW5zZXRcIjtcclxuXHR9XHJcblxyXG5cdGlmIChjb21tYW5kVmFsaWQpIHtcclxuXHRcdHNldFRleHQoQ2hhdENvbG9yLkdSRUVOICsgXCJUaGlzIGNvbW1hbmQgaXMgdmFsaWQg4pyFXCIsIFZBTElEX0JPWCk7XHJcblx0XHRWQUxJRF9CT1guaGlkZGVuID0gZmFsc2U7XHJcblx0fSBlbHNlIHtcclxuXHRcdFZBTElEX0JPWC5oaWRkZW4gPSB0cnVlO1xyXG5cdH1cclxuXHJcblx0Y29uc3QgY29uc3RydWN0U3VnZ2VzdGlvbnNIVE1MID0gKHN1Z2dlc3Rpb25zOiBzdHJpbmdbXSk6IEhUTUxTcGFuRWxlbWVudFtdID0+IHtcclxuXHRcdGxldCBub2Rlc1RvQWRkOiBIVE1MU3BhbkVsZW1lbnRbXSA9IFtdO1xyXG5cdFx0Zm9yIChsZXQgaTogbnVtYmVyID0gMDsgaSA8IHN1Z2dlc3Rpb25zLmxlbmd0aDsgaSsrKSB7XHJcblx0XHRcdGNvbnN0IHN1Z2dlc3Rpb25FbGVtZW50OiBIVE1MU3BhbkVsZW1lbnQgPSBkb2N1bWVudC5jcmVhdGVFbGVtZW50KFwic3BhblwiKTtcclxuXHRcdFx0c3VnZ2VzdGlvbkVsZW1lbnQuaW5uZXJUZXh0ID0gc3VnZ2VzdGlvbnNbaV0hO1xyXG5cdFx0XHRpZiAoaSA9PT0gMCkge1xyXG5cdFx0XHRcdHN1Z2dlc3Rpb25FbGVtZW50LmNsYXNzTmFtZSA9IFwieWVsbG93XCI7XHJcblx0XHRcdH1cclxuXHRcdFx0aWYgKGkgIT09IHN1Z2dlc3Rpb25zLmxlbmd0aCAtIDEpIHtcclxuXHRcdFx0XHRzdWdnZXN0aW9uRWxlbWVudC5pbm5lclRleHQgKz0gXCJcXG5cIjtcclxuXHRcdFx0fVxyXG5cdFx0XHRub2Rlc1RvQWRkLnB1c2goc3VnZ2VzdGlvbkVsZW1lbnQpO1xyXG5cdFx0fVxyXG5cclxuXHRcdHJldHVybiBub2Rlc1RvQWRkO1xyXG5cdH07XHJcblxyXG5cdC8vIElmIHN1Z2dlc3Rpb25zIGFyZSBwcmVzZW50LCBkaXNwbGF5IHRoZW1cclxuXHRTVUdHRVNUSU9OU19CT1guc3R5bGUubGVmdCA9IFwiMFwiO1xyXG5cdGlmIChzdWdnZXN0aW9ucy5sZW5ndGggIT09IDApIHtcclxuXHRcdFNVR0dFU1RJT05TX0JPWC5pbm5lckhUTUwgPSBcIlwiO1xyXG5cdFx0Zm9yIChsZXQgc3VnZ2VzdGlvbkVsZW1lbnQgb2YgY29uc3RydWN0U3VnZ2VzdGlvbnNIVE1MKHN1Z2dlc3Rpb25zKSkge1xyXG5cdFx0XHRTVUdHRVNUSU9OU19CT1guYXBwZW5kQ2hpbGQoc3VnZ2VzdGlvbkVsZW1lbnQpO1xyXG5cdFx0fVxyXG5cdFx0U1VHR0VTVElPTlNfQk9YLnN0eWxlLmxlZnQgPSBUZXh0V2lkdGguZ2V0VGV4dFdpZHRoKHJhd1RleHQsIENPTU1BTkRfSU5QVVQgYXMgQ2FjaGVkRm9udEhUTUxFbGVtZW50KSArIFwicHhcIjtcclxuXHRcdC8vIDhweCBwYWRkaW5nLCAxMHB4IG1hcmdpbiBsZWZ0LCAxMHB4IG1hcmdpbiByaWdodCA9IC0yOHB4XHJcblx0XHQvLyBQbHVzIGFuIGV4dHJhIDEwcHggZm9yIGdvb2QgbHVjaywgd2h5IG5vdFxyXG5cdFx0U1VHR0VTVElPTlNfQk9YLmhpZGRlbiA9IGZhbHNlO1xyXG5cdFx0RVJST1JfTUVTU0FHRV9CT1guaGlkZGVuID0gdHJ1ZTtcclxuXHR9IGVsc2Uge1xyXG5cdFx0U1VHR0VTVElPTlNfQk9YLmhpZGRlbiA9IHRydWU7XHJcblx0fVxyXG5cdHdpbmRvdy5kaXNwYXRjaEV2ZW50KG5ldyBFdmVudChcInN1Z2dlc3Rpb25zVXBkYXRlZFwiKSk7XHJcbn07XHJcblxyXG5DT01NQU5EX0lOUFVULm9uaW5wdXQgPSBvbkNvbW1hbmRJbnB1dDtcclxuXHJcbi8vIFdlIHJlYWxseSByZWFsbHkgZG9uJ3Qgd2FudCBuZXcgbGluZXMgaW4gb3VyIHNpbmdsZS1saW5lZCBjb21tYW5kIVxyXG5DT01NQU5EX0lOUFVULmFkZEV2ZW50TGlzdGVuZXIoJ2tleWRvd24nLCAoZXZ0OiBLZXlib2FyZEV2ZW50KSA9PiB7XHJcblx0c3dpdGNoIChldnQua2V5KSB7XHJcblx0XHRjYXNlIFwiRW50ZXJcIjpcclxuXHRcdFx0ZXZ0LnByZXZlbnREZWZhdWx0KCk7XHJcblx0XHRcdGJyZWFrO1xyXG5cdFx0Y2FzZSBcIkFycm93RG93blwiOlxyXG5cdFx0Y2FzZSBcIkFycm93VXBcIjoge1xyXG5cdFx0XHRpZiAoIVNVR0dFU1RJT05TX0JPWC5oaWRkZW4pIHtcclxuXHRcdFx0XHRldnQucHJldmVudERlZmF1bHQoKTtcclxuXHRcdFx0XHRmb3IgKGxldCBpID0gMDsgaSA8IFNVR0dFU1RJT05TX0JPWC5jaGlsZHJlbi5sZW5ndGg7IGkrKykge1xyXG5cdFx0XHRcdFx0aWYgKFNVR0dFU1RJT05TX0JPWC5jaGlsZHJlbltpXSEuY2xhc3NOYW1lID09PSBcInllbGxvd1wiKSB7XHJcblx0XHRcdFx0XHRcdFNVR0dFU1RJT05TX0JPWC5jaGlsZHJlbltpXSEuY2xhc3NOYW1lID0gXCJcIjtcclxuXHJcblx0XHRcdFx0XHRcdGxldCBzZWxlY3RlZEVsZW1lbnQ6IEVsZW1lbnQ7XHJcblx0XHRcdFx0XHRcdGlmIChldnQua2V5ID09IFwiQXJyb3dEb3duXCIpIHtcclxuXHRcdFx0XHRcdFx0XHRpZiAoaSA9PT0gU1VHR0VTVElPTlNfQk9YLmNoaWxkcmVuLmxlbmd0aCAtIDEpIHtcclxuXHRcdFx0XHRcdFx0XHRcdHNlbGVjdGVkRWxlbWVudCA9IFNVR0dFU1RJT05TX0JPWC5jaGlsZHJlblswXSE7XHJcblx0XHRcdFx0XHRcdFx0fSBlbHNlIHtcclxuXHRcdFx0XHRcdFx0XHRcdHNlbGVjdGVkRWxlbWVudCA9IFNVR0dFU1RJT05TX0JPWC5jaGlsZHJlbltpICsgMV0hO1xyXG5cdFx0XHRcdFx0XHRcdH1cclxuXHRcdFx0XHRcdFx0fSBlbHNlIHtcclxuXHRcdFx0XHRcdFx0XHRpZiAoaSA9PT0gMCkge1xyXG5cdFx0XHRcdFx0XHRcdFx0c2VsZWN0ZWRFbGVtZW50ID0gU1VHR0VTVElPTlNfQk9YLmNoaWxkcmVuW1NVR0dFU1RJT05TX0JPWC5jaGlsZHJlbi5sZW5ndGggLSAxXSE7XHJcblx0XHRcdFx0XHRcdFx0fSBlbHNlIHtcclxuXHRcdFx0XHRcdFx0XHRcdHNlbGVjdGVkRWxlbWVudCA9IFNVR0dFU1RJT05TX0JPWC5jaGlsZHJlbltpIC0gMV0hO1xyXG5cdFx0XHRcdFx0XHRcdH1cclxuXHRcdFx0XHRcdFx0fVxyXG5cdFx0XHRcdFx0XHRzZWxlY3RlZEVsZW1lbnQuY2xhc3NOYW1lID0gXCJ5ZWxsb3dcIjtcclxuXHRcdFx0XHRcdFx0c2VsZWN0ZWRFbGVtZW50LnNjcm9sbEludG9WaWV3KHtiZWhhdmlvcjogXCJzbW9vdGhcIiwgYmxvY2s6IFwiY2VudGVyXCIsIGlubGluZTogXCJuZWFyZXN0XCJ9KTtcclxuXHJcblxyXG5cdFx0XHRcdFx0XHR3aW5kb3cuZGlzcGF0Y2hFdmVudChuZXcgRXZlbnQoXCJzdWdnZXN0aW9uc1VwZGF0ZWRcIikpO1xyXG5cclxuXHRcdFx0XHRcdFx0YnJlYWs7XHJcblx0XHRcdFx0XHR9XHJcblx0XHRcdFx0fVxyXG5cdFx0XHR9XHJcblx0XHRcdGJyZWFrO1xyXG5cdFx0fVxyXG5cdFx0Y2FzZSBcIkJhY2tzcGFjZVwiOlxyXG5cdFx0XHRpZiAoQ09NTUFORF9JTlBVVC5pbm5lclRleHQucmVwbGFjZShcIlxcblwiLCBcIlwiKS5sZW5ndGggPT09IDApIHtcclxuXHRcdFx0XHRldnQucHJldmVudERlZmF1bHQoKTtcclxuXHRcdFx0fVxyXG5cdFx0XHRicmVhaztcclxuXHRcdGNhc2UgXCJUYWJcIjpcclxuXHRcdFx0ZXZ0LnByZXZlbnREZWZhdWx0KCk7XHJcblx0XHRcdHNldFRleHQoZ2V0VGV4dChmYWxzZSkuc2xpY2UoMSkgKyBDT01NQU5EX0lOUFVUX0FVVE9DT01QTEVURS5pbm5lclRleHQpO1xyXG5cdFx0XHRvbkNvbW1hbmRJbnB1dCgpO1xyXG5cdFx0XHRzZXRDdXJzb3JQb3NpdGlvbihDT01NQU5EX0lOUFVULmlubmVyVGV4dC5sZW5ndGgsIENPTU1BTkRfSU5QVVQpO1xyXG5cdFx0XHRicmVhaztcclxuXHRcdGRlZmF1bHQ6XHJcblx0XHRcdGJyZWFrO1xyXG5cdH1cclxufSk7XHJcblxyXG5TVUdHRVNUSU9OU19CT1guYWRkRXZlbnRMaXN0ZW5lcihcIm1vdXNlb3ZlclwiLCAoZXZ0OiBNb3VzZUV2ZW50KSA9PiB7XHJcblx0aWYoIVNVR0dFU1RJT05TX0JPWC5oaWRkZW4pIHtcclxuXHRcdGlmKFsuLi5TVUdHRVNUSU9OU19CT1guY2hpbGRyZW5dLmluY2x1ZGVzKGV2dC50YXJnZXQgYXMgRWxlbWVudCkpIHtcclxuXHRcdFx0Zm9yIChsZXQgaSA9IDA7IGkgPCBTVUdHRVNUSU9OU19CT1guY2hpbGRyZW4ubGVuZ3RoOyBpKyspIHtcclxuXHRcdFx0XHRpZiAoU1VHR0VTVElPTlNfQk9YLmNoaWxkcmVuW2ldIS5jbGFzc05hbWUgPT09IFwieWVsbG93XCIpIHtcclxuXHRcdFx0XHRcdFNVR0dFU1RJT05TX0JPWC5jaGlsZHJlbltpXSEuY2xhc3NOYW1lID0gXCJcIjtcclxuXHRcdFx0XHR9XHJcblx0XHRcdH1cclxuXHRcdFx0KGV2dC50YXJnZXQgYXMgRWxlbWVudCkuY2xhc3NOYW1lID0gXCJ5ZWxsb3dcIjtcclxuXHRcdFx0d2luZG93LmRpc3BhdGNoRXZlbnQobmV3IEV2ZW50KFwic3VnZ2VzdGlvbnNVcGRhdGVkXCIpKTtcclxuXHRcdH1cclxuXHR9XHJcbn0pO1xyXG5cclxuU1VHR0VTVElPTlNfQk9YLmFkZEV2ZW50TGlzdGVuZXIoXCJtb3VzZWRvd25cIiwgKGV2dDogTW91c2VFdmVudCkgPT4ge1xyXG5cdGlmKCFTVUdHRVNUSU9OU19CT1guaGlkZGVuICYmIFsuLi5TVUdHRVNUSU9OU19CT1guY2hpbGRyZW5dLmluY2x1ZGVzKGV2dC50YXJnZXQgYXMgRWxlbWVudCkpIHtcclxuXHRcdGV2dC5wcmV2ZW50RGVmYXVsdCgpO1xyXG5cdFx0c2V0VGV4dChnZXRUZXh0KGZhbHNlKS5zbGljZSgxKSArIENPTU1BTkRfSU5QVVRfQVVUT0NPTVBMRVRFLmlubmVyVGV4dCk7XHJcblx0XHRvbkNvbW1hbmRJbnB1dCgpO1xyXG5cdFx0c2V0Q3Vyc29yUG9zaXRpb24oQ09NTUFORF9JTlBVVC5pbm5lclRleHQubGVuZ3RoLCBDT01NQU5EX0lOUFVUKTtcclxuXHR9XHJcbn0pO1xyXG5cclxud2luZG93LmFkZEV2ZW50TGlzdGVuZXIoXCJzdWdnZXN0aW9uc1VwZGF0ZWRcIiwgKF9ldmVudDogRXZlbnQpID0+IHtcclxuXHRjb25zdCByYXdUZXh0OiBzdHJpbmcgPSBDT01NQU5EX0lOUFVULmlubmVyVGV4dC5yZXBsYWNlQWxsKFwiXFx1MDBhMFwiLCBcIiBcIik7IC8vIFJlcGxhY2UgJm5ic3A7IHdpdGggbm9ybWFsIHNwYWNlc1xyXG5cclxuXHRpZiAoIVNVR0dFU1RJT05TX0JPWC5oaWRkZW4pIHtcclxuXHRcdGNvbnN0IHNlbGVjdGVkU3VnZ2VzdGlvblRleHQ6IHN0cmluZyA9IGdldFNlbGVjdGVkU3VnZ2VzdGlvbigpLmlubmVyVGV4dC50cmltKCk7XHJcblxyXG5cdFx0Ly8gVE9ETzogVGhpcyBvYnZpb3VzbHkgbmVlZHMgdG8gYmUgc3BlY2lmaWMgdG8gdGhlIGN1cnJlbnQgc3VnZ2VzdGlvbnMsIG5vdCB0aGUgd2hvbGUgaW5wdXRcclxuXHRcdGlmIChyYXdUZXh0ICE9PSBzZWxlY3RlZFN1Z2dlc3Rpb25UZXh0KSB7XHJcblx0XHRcdGNvbnN0IGN1cnNvclBvc2l0aW9uOiBudW1iZXIgPSBnZXRDdXJzb3JQb3NpdGlvbigpO1xyXG5cclxuXHRcdFx0bGV0IHN1Z2dlc3Rpb25UZXh0ID0gXCJcIjtcclxuXHRcdFx0aWYoc2VsZWN0ZWRTdWdnZXN0aW9uVGV4dC5sZW5ndGggPiAwKSB7XHJcblx0XHRcdFx0Y29uc3QgbGFzdEluZGV4T2ZTdGFydE9mU3VnZ2VzdGlvbjogbnVtYmVyID0gcmF3VGV4dC5sYXN0SW5kZXhPZihzZWxlY3RlZFN1Z2dlc3Rpb25UZXh0WzBdISk7XHJcblx0XHRcdFx0Y29uc29sZS5sb2coYExhc3QgaW5kZXggb2YgJHtzZWxlY3RlZFN1Z2dlc3Rpb25UZXh0WzBdfSBpcyBhdCBpbmRleCAke2xhc3RJbmRleE9mU3RhcnRPZlN1Z2dlc3Rpb259YCk7XHJcblxyXG5cdFx0XHRcdC8vIElmIHdlIGNhbiBzdGFydCB3aXRoIGl0LCBqdXN0IGRpc3BsYXkgaXRcclxuXHRcdFx0XHRpZihsYXN0SW5kZXhPZlN0YXJ0T2ZTdWdnZXN0aW9uID09PSAtMSkge1xyXG5cdFx0XHRcdFx0c3VnZ2VzdGlvblRleHQgPSBzZWxlY3RlZFN1Z2dlc3Rpb25UZXh0O1xyXG5cdFx0XHRcdH0gZWxzZSB7XHJcblx0XHRcdFx0XHQvLyBTaG93IHdoYXRldmVyIHRoZSByYXdUZXh0IGRvZXNuJ3QgaGF2ZSB0aGF0IHN0YXJ0cyB3aXRoIHdoYXRldmVyIHRoZSBzdWdnZXN0aW9uIHRleHQgaXNcclxuXHRcdFx0XHRcdGZvcihsZXQgaSA9IDA7IGkgPCBzZWxlY3RlZFN1Z2dlc3Rpb25UZXh0Lmxlbmd0aDsgaSsrKSB7XHJcblx0XHRcdFx0XHRcdGlmKHJhd1RleHQuZW5kc1dpdGgoc2VsZWN0ZWRTdWdnZXN0aW9uVGV4dC5zbGljZSgwLCBpKSkpIHtcclxuXHRcdFx0XHRcdFx0XHQvLyBzdWdnZXN0IHRoZSBlbmRcclxuXHRcdFx0XHRcdFx0XHRzdWdnZXN0aW9uVGV4dCA9IHNlbGVjdGVkU3VnZ2VzdGlvblRleHQuc2xpY2UoaSk7XHJcblx0XHRcdFx0XHRcdH1cclxuXHRcdFx0XHRcdH1cclxuXHRcdFx0XHR9XHJcblx0XHRcdH1cclxuXHRcdFx0Ly8gY29uc29sZS5sb2cocmF3VGV4dCArIFwiIGMuZi4gXCIgKyBzZWxlY3RlZFN1Z2dlc3Rpb25UZXh0KTtcclxuXHRcdFx0c2V0VGV4dChDaGF0Q29sb3IuREFSS19HUkFZICsgc3VnZ2VzdGlvblRleHQsIENPTU1BTkRfSU5QVVRfQVVUT0NPTVBMRVRFKTtcclxuXHRcdFx0c2V0Q3Vyc29yUG9zaXRpb24oY3Vyc29yUG9zaXRpb24sIENPTU1BTkRfSU5QVVQpO1xyXG5cdFx0XHRDT01NQU5EX0lOUFVULmZvY3VzKCk7XHJcblx0XHR9IGVsc2Uge1xyXG5cdFx0XHRzZXRUZXh0KFwiXCIsIENPTU1BTkRfSU5QVVRfQVVUT0NPTVBMRVRFKTtcclxuXHRcdH1cclxuXHR9IGVsc2Uge1xyXG5cdFx0c2V0VGV4dChcIlwiLCBDT01NQU5EX0lOUFVUX0FVVE9DT01QTEVURSk7XHJcblx0fVxyXG59KTtcclxuXHJcbi8vIElmIHlvdSBjbGljayBvbiB0aGUgY2hhdCBib3gsIGZvY3VzIHRoZSBjdXJyZW50IHRleHQgaW5wdXQgYXJlYSBcclxuQ0hBVF9CT1gub25jbGljayA9IGZ1bmN0aW9uIG9uQ2hhdEJveENsaWNrZWQoKSB7XHJcblx0Q09NTUFORF9JTlBVVC5mb2N1cygpO1xyXG59O1xyXG5cclxuY29uc3Qgb25SZWdpc3RlckNvbW1hbmRzQnV0dG9uQ2xpY2tlZCA9IGZ1bmN0aW9uIG9uUmVnaXN0ZXJDb21tYW5kc0J1dHRvbkNsaWNrZWQoKSB7XHJcblx0ZGlzcGF0Y2hlci5kZWxldGVBbGwoKTtcclxuXHRDT01NQU5EUy52YWx1ZS5zcGxpdChcIlxcblwiKS5mb3JFYWNoKHJlZ2lzdGVyQ29tbWFuZCk7XHJcblx0b25Db21tYW5kSW5wdXQoKTsgLy8gUnVuIHN5bnRheCBoaWdobGlnaHRlclxyXG59XHJcblxyXG5SRUdJU1RFUl9DT01NQU5EU19CVVRUT04ub25jbGljayA9IG9uUmVnaXN0ZXJDb21tYW5kc0J1dHRvbkNsaWNrZWQ7XHJcblxyXG53aW5kb3cub25lcnJvciA9IGZ1bmN0aW9uIG9uRXJyb3IoX2V2ZW50LCBfc291cmNlLCBfbGluZW5vLCBfY29sbm8sIGVycm9yKSB7XHJcblx0Y29uc3QgZXJyb3JFbGVtZW50OiBIVE1MU3BhbkVsZW1lbnQgPSBkb2N1bWVudC5jcmVhdGVFbGVtZW50KFwic3BhblwiKTtcclxuXHRlcnJvckVsZW1lbnQuY2xhc3NOYW1lID0gXCJlcnJvckVudHJ5XCI7XHJcblx0ZXJyb3JFbGVtZW50LmlubmVyVGV4dCA9IGAke2Vycm9yPy5uYW1lfTogJHtlcnJvcj8ubWVzc2FnZX1gO1xyXG5cclxuXHRjb25zdCBjbG9zZUJ1dHRvbjogSFRNTFNwYW5FbGVtZW50ID0gZG9jdW1lbnQuY3JlYXRlRWxlbWVudChcInNwYW5cIik7XHJcblx0Y2xvc2VCdXR0b24uaW5uZXJUZXh0ID0gXCLinYwgXCI7XHJcblx0Y2xvc2VCdXR0b24ub25jbGljayA9IGZ1bmN0aW9uIG9uQ2xvc2VFcnJvcihtb3VzZUV2ZW50OiBNb3VzZUV2ZW50KSB7XHJcblx0XHQobW91c2VFdmVudC50YXJnZXQgYXMgRWxlbWVudCkucGFyZW50RWxlbWVudD8ucmVtb3ZlKCk7XHJcblx0fTtcclxuXHJcblx0ZXJyb3JFbGVtZW50LnByZXBlbmQoY2xvc2VCdXR0b24pO1xyXG5cdEFQUF9FUlJPUl9CT1guYXBwZW5kQ2hpbGQoZXJyb3JFbGVtZW50KTtcclxufVxyXG5cclxuXHJcbi8qKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKipcclxuICogRW50cnlwb2ludCAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgKlxyXG4gKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqL1xyXG5cclxuLy8gRGVmYXVsdCBjb21tYW5kc1xyXG5DT01NQU5EUy52YWx1ZSA9IGBmaWxsIDxwb3MxPlttaW5lY3JhZnQ6YmxvY2tfcG9zXSA8cG9zMj5bbWluZWNyYWZ0OmJsb2NrX3Bvc10gPGJsb2NrPlticmlnYWRpZXI6c3RyaW5nXVxyXG5zcGVlZCAod2Fsa3xmbHkpIDxzcGVlZD5bMC4uMTBdIDx0YXJnZXQ+W21pbmVjcmFmdDpnYW1lX3Byb2ZpbGVdXHJcbmhlbGxvIDx2YWw+WzEuLjIwXSA8Y29sb3I+W21pbmVjcmFmdDpjb2xvcl1cclxubXlmdW5jIDx2YWw+W21pbmVjcmFmdDptb2JfZWZmZWN0XVxyXG5lbnRpdHkgPHZhbD5bYXBpOmVudGl0aWVzXVxyXG5wbGF5ZXIgPHZhbD5bYXBpOnBsYXllcl1cclxudGVzdF9hXHJcbnRlc3RfYlxyXG50ZXN0X2NcclxudGVzdF9kXHJcbnRlc3RfZWA7XHJcblxyXG5vblJlZ2lzdGVyQ29tbWFuZHNCdXR0b25DbGlja2VkKCk7XHJcbmNvbnNvbGUubG9nKFwiRGlzcGF0Y2hlclwiLCBkaXNwYXRjaGVyLmdldFJvb3QoKSlcclxuXHJcbiJdLCJuYW1lcyI6W10sInNvdXJjZVJvb3QiOiIifQ==