# How to contribute

Thanks for considering contributing to the CommandAPI! We're always happy to receive new improvements from developers to create a powerful and sustainable Minecraft API.

Useful resources:

- If you haven't joined [the CommandAPI Discord](https://discord.com/invite/G4SzSxZ), feel free to come find us! We can assure you that we don't bite.
- Found a bug or have a suggestion and aren't quite sure how to tackle it? Open a [GitHub issue](https://github.com/JorelAli/CommandAPI/issues) and we can work it out together.

## Where should I start?

The CommandAPI repository consists of a multi-module Maven project and some documentation. Here's where everything is situated:

### Main logic

- [`commandapi-core`](https://github.com/JorelAli/CommandAPI/tree/master/CommandAPI/commandapi-core) - the main bulk of the CommandAPI. Houses the developer-facing API and the main backend logic.
- `commandapi-x.x.x` - the NMS implementation for various versions of Minecraft

### Outputs

- [`commandapi-plugin`](https://github.com/JorelAli/CommandAPI/tree/master/CommandAPI/commandapi-plugin) - the CommandAPI as a standalone plugin with the `JavaPlugin` entrypoint and `config.yml` command converter.
- [`commandapi-shade`](https://github.com/JorelAli/CommandAPI/tree/master/CommandAPI/commandapi-shade) - the dependency required to shade the CommandAPI.
- [`commandapi-annotations`](https://github.com/JorelAli/CommandAPI/tree/master/CommandAPI/commandapi-annotations) - a compile-time annotation processor for commands declared using Java's annotations.

### Helpers

- [`commandapi-vh`](https://github.com/JorelAli/CommandAPI/tree/master/CommandAPI/commandapi-vh) - a Maven module that links the various NMS modules together for `commandapi-shade` and `commandapi-plugin`.
- [`commandapi-preprocessor`](https://github.com/JorelAli/CommandAPI/tree/master/CommandAPI/commandapi-preprocessor) - an annotation processor used internally in the CommandAPI to perform compile-time field reflection checks.

### Documentation

- [`docssrc`](https://github.com/JorelAli/CommandAPI/tree/master/docssrc) - the [mdBook](https://rust-lang.github.io/mdBook/) source for the CommandAPI's documentation website
- [`docs`](https://github.com/JorelAli/CommandAPI/tree/master/docs)
  - `docs/x.x.x` - the compiled mdBook website for the CommandAPI's documentation
  - [`docs/javadocs/html`](https://github.com/JorelAli/CommandAPI/tree/master/docs/javadocs/html) - Generated [Doxygen](https://www.doxygen.nl/index.html) JavaDocs for the CommandAPI
  - [`docs/index.html`](https://github.com/JorelAli/CommandAPI/blob/master/docs/index.html) - the frontend documentation website for [commandapi.jorel.dev](https://commandapi.jorel.dev/)

## Submitting changes

To submit new changes, open a new [pull request](https://github.com/JorelAli/CommandAPI/pulls) to the `dev/dev` branch with a list of what you've done - please _do not_ open a pull request to the `master` branch!

Pull requests should:

- Have suitable commit messages that describes the changes in the commit.
- Compile successfully (this will be checked automatically when a pull request is made).
- Have been tested in a Minecraft server enviroment if necessary (e.g. the latest compatible version of Minecraft and using Spigot or Paper).

If the pull request is a new feature, some documentation for the feature should be provided in a new Markdown file in `docssrc/src` if the feature is relatively complex, requiring various steps to use the feature. If the feature is simple enough to explain with a few examples, please include these in your pull request description instead of creating a Markdown documentation file.

## Coding conventions

We don't have any overly strict conventions, but these are a must:

- Use tabs instead of spaces, except for documentation-facing example code (such as `commandapi-core/src/main/test/Examples.java`).
- Avoiding Java's Streams API, in favour of for loops or while loops.
- Avoid the use of `null` wherever possible. If `null` must be used, consider using optionals.
- If using reflection or otherwise, all required fields must be declared at the top of the file with `@RequireField`.
- If you are adding a new developer-facing API method, suitable JavaDocs must be provided.

Additionally, the following are recommended but not overly enforced:

- In general, stick to the formatting of Java code in surrounding code blocks or contexts. By default, we use the built-in Eclipse IDE Java formatter, except for cases where this sacrifices readability of the code, such as builder function calls.
- Avoiding the use of reflection, in favour of `VarHandles`.
- If a field in a method will be used a lot and isn't going to be modified, make it `final`.

## Common pull request pitfalls

**Did you fix whitespace, formatting or a purely cosmetic change?**

Cosmetic changes don't add anything of value to the CommandAPI's functionality or stability and will generally not be accepted.
