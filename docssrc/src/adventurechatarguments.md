# Adventure chat arguments

> **Developer's Note:**
>
> The two following classes, `AdventureChatComponentArgument` and `AdventureChatArgument` depend on a Paper based server which has the Adventure library. If you use this class on a server without the Adventure library, it will throw a `PaperAdventureNotFoundException`

From Paper 1.16.5 build #473, it now includes [Kyori's Adventure API](https://github.com/KyoriPowered/adventure-platform). This library is a replacement of the BungeeCord chat API and has all of the same functionality as the BungeeCord chat API (and more!). The documentation for this API can be found [here](https://docs.adventure.kyori.net/index.html).

Since this functions very similar to the Spigot chat arguments, this page won't reiterate everything about how it works, we'll just outline some examples of how to use these arguments instead.

-----

## Adventure chat component argument

The `AdventureChatComponentArgument` class accepts raw chat-based JSON as valid input, as declared [here](https://minecraft.gamepedia.com/Raw_JSON_text_format). This is converted into Adventure's `Component` class.

<div class="example">

### Example - Opening a book with raw JSON content

In this example, we'll create a simple command which lets you show a book to a user. The syntax for our command is as follows:

```
/showbook <target> <title> <author> <contents>
```

We can construct a book using the Adventure API's `Book.book(Component, Component, Component...)` method. In order to convert our strings into `Component` objects, we use the `Component.text(String)` method. Since Paper supports the Adventure API natively, we can then send this book to a player using the `openBook(Book)` method:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:ArgumentAdventureChatComponent}}
```

</div>