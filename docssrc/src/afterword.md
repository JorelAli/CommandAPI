# Afterword

<div class="example">

**Developer's Note:**

Congratulations on making it to the end of the documentation! I hope it was easy to understand everything ... I feel like it's a bit content heavy.

-----

This project is by far my most worked on and probably the project I'm most proud of. If you're interested in reading about my journey with the CommandAPI, I've written about it a few times on my blog:

- You can read about the creation of the CommandAPI in my blog post [here](https://www.jorel.dev/blog/Creating-the-CommandAPI/).
- You can read about the CommandAPI's first issue in my blog post [here](https://www.jorel.dev/blog/Run-commands-as-chickens/).
- You can read about my self-review of the CommandAPI, two months after its creation [here](https://www.jorel.dev/blog/Return-of-the-Command-API/).

-----

I'd like to give credit to all of the people that have opened issues on GitHub, that have truly made it what it is now. In addition to [my stargazers](https://github.com/JorelAli/1.13-Command-API/stargazers), I'd like to personally thank:
- **[Combustible](https://github.com/Combustible)**, who was my #1 supporter and motivated me to keep this project alive after its initial release. They also helped raise awareness of the CommandAPI throughout Spigot's forums.
- **[DerpNerb](https://github.com/DerpNerb)**, who gave me the idea to convert the CommandAPI to a Maven project which resulted in making the CommandAPI much more accessible for everyone.
- **[Draycia](https://github.com/Draycia)**, who suggested that I put `CommandSender` objects inside `DynamicSuggestedStringArguments`, which allow dynamic arguments to be much more powerful.
- **[HielkeMinecraft](https://github.com/HielkeMinecraft)**, who suggested various improvements for the `LocationArgument` class; suggested adding results and successes for commands and being a great bug reporter in general.
- **[i509VCB](https://github.com/i509VCB)**, who is an absolute genius and a really good bug spotter. They pointed out a severe bug that made libraries depending on the CommandAPI depend on Brigadier; created the documentation for using the CommandAPI with Gradle and suggested using the NBTAPI to include NBT support.
- **[Loapu](https://github.com/Loapu)**, who helped debug an incredibly difficult bug where command executors were not working properly. They were able to quickly communicate and explore the issue even when I was unable to replicate the issue on my machine.
- **[Minenash](https://github.com/Minenash)**, who was the driving force for the 3.0 release which adds a plethora of additional arguments. They continued to research, write documentation examples, bug test, code review and basically oversee the project throughout its development.
- **[Tinkot](https://github.com/Tinkot)**, who gave a review of the CommandAPI on its spigot page. This motivated me to fix a 6 month old bug. Somehow.
- **[ZiluckMichael](https://github.com/ZiluckMichael)**, who truly changed the CommandAPI for the better. Refactored the entire code base to make the CommandAPI much more maintainable, as well as removing a lot of unnecessary reflection.

-----

I never really expected more than 5 or so people to use this API, so it was truly an exciting time creating this for everyone - seeing everyone's responses, issues and problems helped me keep going.

In short, thank you, and everyone else that helped make the CommandAPI what it is.

</div>