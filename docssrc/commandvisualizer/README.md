# Command Visualizer

A TypeScript-based embedded Minecraft command viewing interface.

## Building

- Get [Node.js](https://nodejs.org/en/) if you don't already have it
- Run `npm install`
- Run `npx webpack`

## Testing (e.g. how to use in your local browser)

- Run `npx webpack serve`
- Navigate to `http://localhost:8080`

## Acknowledgements

- [Node-Brigadier](https://github.com/remtori/brigadier) - a Node,js version of Mojang's Brigadier library.

## Notes

The command visualizer is a _static webpage_ that uses webpack to bundle the node-brigadier module. Because ECMAScript6 modules cannot access the file system, they must be run via a web server (hence `http-server` for testing). This doesn't have issues when run via GitHub Pages or otherwise.
