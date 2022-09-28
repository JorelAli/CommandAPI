export {};

declare global {
    interface Array<T> {
        has(x: 0): this is [];
        has(x: 1): this is [T];
        has(x: 2): this is [T, T];
    }
}

Array.prototype.has = function x(x: number) {
    return this.length === x;
};