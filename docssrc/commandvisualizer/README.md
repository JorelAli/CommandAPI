# Command Visualizer

A JavaScript-based embedded Minecraft command viewing interface.

## Building

- Get [Node.js](https://nodejs.org/en/) if you don't already have it
- Run `npm install`
- Run `npx webpack ./commandvisualizer.js -o .`

## Testing (e.g. how to use in your local browser)

- Run `npm install http-server`
- Run `npx http-server .`
- Run `npx webpack ./commandvisualizer.js -o . -d source-map` command above when necessary to update local JavaScript changes

## Acknowledgements

- [Node-Brigadier](https://github.com/remtori/brigadier) - a Node,js version of Mojang's Brigadier library.

## Notes

The command visualizer is a _static webpage_ that uses webpack to bundle the node-brigadier module. Because ECMAScript6 modules cannot access the file system, they must be run via a web server (hence `http-server` for testing). This doesn't have issues when run via GitHub Pages or otherwise.
