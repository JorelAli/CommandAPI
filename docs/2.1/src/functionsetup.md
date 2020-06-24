# Setting up functions & tags

> **Developer's Note:**
>
> Most developers can completely skip this section. This is for those that are unfamiliar with functions and tags and is less about how the CommandAPI works.

This section explains how functions are declared and set up for use in a Minecraft server. This is ideal for server owners who've never set up functions, or developers _(like the Command API's creator)_ that has no idea how to set this sort of thing up.

## Creating functions

Functions are text files _(with the `.mcfunction` extension)_ which contains lists of functions which are executed one after another. Each line of the file is a valid Minecraft command. Say we have `text.mcfunction`:

```
killall
say Killed all living entities on the server
```

This will run the custom command killall _(as declared in **Chapter 5 - Functions**)_, and then broadcast a message to all players stating that all entities were killed.

## Creating tags

Tags are json files which contain a list of functions. Tags let you run multiple functions at a time. Say we have a tag called `mytag.json`:

```json
{
    "values": [
    	"mycustomnamespace:test",
    	"mycustomnamespace:test2"
    ]
}
```

This will run the function `test` and the function `test2`, which are in the namespace `mycustomnamespace`.

## Namespaces & where to place everything

The following hierarchy explains where functions and tags go. In this diagram, the two functions `test` and `test2` are in a directory called `functions`. There is also a tag called `mytag` which is placed in the `tags` directory under `functions`. These are all under the _namespace_ called `mycustomnamespace`

```
server/
├── world/
│   ├── advancements/
│   ├── data/
│   ├── datapacks/
│   │   └── bukkit/
│   │       ├── pack.mcmeta
│   │       └── data/
│   │           └── mycustomnamespace/
│   │               ├── functions/
│   │               │   ├── test.mcfunction
│   │               │   └── test2.mcfunction
│   │               └── tags/
│   │                   └── functions/
│   │                       └── mytag.json
│   └── ...
├── world_nether/
├── world_the_end/
├── ...
└── spigot.jar
```

To execute the `test` function, you would run the following command:

```
/function mycustomnamespace:test
```

To execute the `mytag` tag, you would run the following command:

```
/function #mycustomnamespace:mytag
```


