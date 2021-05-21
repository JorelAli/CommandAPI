# Project Structure

The CommandAPI is a relatively large project (especially from the standpoint of one guy, because the CommandAPI was written by just one guy in their spare time!) and trying to figure out what everything does is a nightmare without some guidance. I've always felt that other community project structures aren't well documented and contributing to them can be daunting. Here's the CommandAPI's project structure for you!

-----

## `CommandAPI` folder

This is where all of the code is for the CommandAPI. The CommandAPI is a Maven project with multiple modules which each serve a different purpose:

- `commandapi-preprocessor` - The CommandAPI uses a bit of reflection to perform things which could not normally be done (for example, allowing custom commands in datapacks). Reflection is inherently unsafe and can lead to runtime errors if specific fields or methods are not present. The CommandAPI preprocessor project is a source annotation processor that checks all declared reflection calls and looks up at compile-time whether those calls are possible - if not, it prevents the CommandAPI from building. In short, it's a compile-time reflection checker.

- `commandapi-x.x.x` - The CommandAPI needs to access various NMS methods in order to operate. These are implemented for the specific version given by `x.x.x`. For example, to support Minecraft `1.16.5`, the project is `commandapi-1.16.5`. The `NMS` class implementation is done in these version-specific files.

- `commandapi-core` - The main brains of the CommandAPI. This includes both the code that makes the CommandAPI run, as well as the API which developers can use.

- `commandapi-vh` - The CommandAPI version handler. This is a super tiny project which simply links up all of the NMS version-specific files into the CommandAPI. This is only used for the actual running of the CommandAPI (e.g. the CommandAPI plugin or shading the CommandAPI). This ensures proper compile-time safety of NMS implementations.

- `commandapi-plugin` - It's the CommandAPI plugin! This is the project which is used for releases to both GitHub and Spigot. It's the CommandAPI all in one neat package, with a few extra features such as config-based command conversion for server owners (or other non-developers)

- `commandapi-shade` - It's the CommandAPI, but in shade-able format. It has none of the features of the CommandAPI plugin variant and can be shaded into your own plugins. Effectively, it's `commandapi-core` + `commandapi-vh` with all of the `commandapi-x.x.x` NMS implementations included.

- `commandapi-annotations` - The CommandAPI annotations project is a small compile-time annotation processer that basically writes CommandAPI code for you. Using a compile-time annotation processor makes the server run so much faster than using a runtime-annotation processor, because annotation processing requires reflection to inspect class metadata.

## `docs` folder

This is where all of the lovely documentation, JavaDocs and the CommandAPI homepage is stored. Everything in this folder is automatically hosted on GitHub using GitHub Pages - this is the live stuff. Key things in this folder:

- `javadocs` - It's the JavaDocs! This is built using Doxygen with the configuration in the root of this project (See the `Doxyfile`). I also serve a custom version of Mojang's Brigadier library which was written by [I-Al-Istannen](https://github.com/I-Al-Istannen/brigadier) because this person spent a lot of time and effort producing JavaDocs for Brigadier when Mojang didn't bother. This person deserves a lot of respect - their work is amazing!

- `index.html` - It's the CommandAPI's homepage! It's not anything especially amazing - it's a very very simple static webpage with links - that's all that it needs to be. Every update, this file needs to be edited manually to update all of the links.

## `docssrc` folder

This is the brains behind the documentation. It's a collection of Markdown files which form the CommandAPI documentation and is built using [mdBook](https://github.com/rust-lang/mdBook). For the CommandAPI in particular, it is actually built using my own custom fork of mdBook ([here, in the fa5 branch](https://github.com/JorelAli/mdBook/tree/fa5)) which incorporates [this pull request](https://github.com/rust-lang/mdBook/pull/1225) which adds support for FontAwesome 5.