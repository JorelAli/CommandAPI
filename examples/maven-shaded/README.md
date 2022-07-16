# Maven-shaded

A simple example of shading the CommandAPI with Maven.

Key points:

- In `Main.java`, we call `CommandAPI.onLoad()` and `CommandAPI.onEnable()` to set up the CommandAPI
- In `pom.xml`, we use relocation to relocate the CommandAPI to the `io.github.jorelali.commandapi` package. This gives us the following directory structure in our plugin's `.jar` file:

```text
.
├── io
│   └── github
│       └── jorelali
│           ├── commandapi
│           │   ├── arguments
│           │   │   ├── AdvancementArgument.class
│           │   │   └── ...
│           │   ├── CommandAPI.class
│           │   ├── CommandAPICommand.class
│           │   └── ...
│           ├── Main.class
│           └── MyCommands.class
├── META-INF
│   └── MANIFEST.MF
└── plugin.yml
```
