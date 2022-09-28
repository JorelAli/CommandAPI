import { StringReader, CommandSyntaxException } from "node-brigadier"

declare module "node-brigadier" {
    interface StringReader {
        readLocationLiteral(): number;
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