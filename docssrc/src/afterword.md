# Afterword

<div style="text-align: center;">

## A message from the CommandAPI's author

</div>

Congratulations on making it to the end of the documentation! It's really long, but I did my best to make it the best (Bukkit/Spigot plugin) documentation in existence.

-----

### Intro

My name is Jorel, commonly known by my Minecraft username _Skepter_. I started the CommandAPI in the summer holidays between my first and second year at university. On the 19th August, 2018 I made my first commit to the CommandAPI project - just a month and a day after Minecraft 1.13 was released.

At the time, I just decided to call it "The 1.13 Command API" - it wasn't the catchiest name out there, but it sort of said what I wanted it to - it's a Command API for Minecraft 1.13, which was the update when the big overhaul to the command system was introduced.

It all started as a simple idea that can be summarized in 3 bullet points:

- Create an API to use the new command UI that was introduced in Minecraft 1.13
- Make it so the developers don't have to understand/use Mojang's brigadier
- Make it similar to Bukkit's existing API

-----

### Starting out

After the release of version 1.2, two days after the initial release, I received my first GitHub issue. This was quite a shock to me - version 1.2 only had 11 total downloads so it seemed odd that someone managed to stumble upon it despite the fact that I did nothing to promote the CommandAPI. Little did I know that that one issue was the main motivation to keep this API alive after its initial release.

I would never have possible imagined in my wildest dreams that 2 years later, I would still be in contact with them and know that if I had not chosen to create this API, their server would not have survived beyond Minecraft 1.13, let alone Minecraft 1.15, two major Minecraft versions later.

-----

### Reflection on the project and personal gains

This project has been insane. Absolutely, utterly insane. At over 1000 commits and over 1,000,000 additions (that includes things such as lines of code, lines of generated HTML documentation etc.), I can say without a doubt that this is indeed my biggest project ever.

Not only have I managed to deal with hundreds of GitHub issues, I've also had the opportunity to try all sorts of technologies that I could have only dreamt of using, such as:

- Using GitHub Actions for cloud-based build checking and project status notifications
- Using CodeFactor.io to automatically identify issues with my code
- Publishing the CommandAPI to MavenCentral
- Improving my understanding of Gradle to write Gradle instructions for the documentation
- Learning Kotlin to add Kotlin examples to the CommandAPI's documentation
- Learning TypeScript for an upcoming web-based CommandAPI command visualizer

-----

### Acknowledgements (early development)

Anyway, I digress. I'd like to give credit to all of the people that have opened issues on the CommandAPI GitHub, for without these people, the CommandAPI would have only remained a shadow of what it is now. I'd like to give credit to [the people that have starred](https://github.com/JorelAli/CommandAPI/stargazers) the CommandAPI on its GitHub page, and all of the members of the [CommandAPI Discord server](https://discord.com/invite/G4SzSxZ).

I would like to personally give thanks to the following people - these are people that have made a significant contribution to the project in terms of ideas or support early in the CommandAPI's development:

- **[Combustible](https://github.com/Combustible)**, who kickstarted the project by creating the CommandAPI's first issue. From this issue, this allowed the CommandAPI to have interoperability with Minecraft commands and functions which is by far the CommandAPI's most admirable feature. Additionally, Combustible helped raise awareness of the CommandAPI via the Spigot forums and Spigot issue tracker.
- **[Draycia](https://github.com/Draycia)**, who suggested implementing lazy evaluation for argument suggestions. This has been extended to provide the CommandAPI's context-aware system for argument suggestions based on previously filled arguments.
- **[HielkeMinecraft](https://github.com/HielkeMinecraft)**, who made three outstanding contributions to the CommandAPI. They created the suggestion of setting the result and success values of commands which improves the interoperability between commands registered with the CommandAPI and vanilla Minecraft commands. They also influenced the implementation of the requirements system to have more powerful command constraints and helped start the CommandAPI Discord server.
- **[Minenash](https://github.com/Minenash)**, who was the driving force for the CommandAPI's 3.0 release, which added a plethora of new arguments to the CommandAPI. Minenash's research, code prototypes, documentation examples, bug testing and code review was a tremendous help to make the 3.0 release such a feature-rich version.
- **[Michael-Ziluck](https://github.com/Michael-Ziluck)**, who created an amazing pull request that helped greatly improve the performance of the CommandAPI as well as structure the entire CommandAPI project into a multi-module Maven project which significantly improved the maintainability of the CommandAPI for the future.
- **[portlek](https://github.com/portlek)**, who helped migrate the CommandAPI's repository to [jitpack.io](https://jitpack.io/#dev.jorel/CommandAPI). They helped debug some technical errors with remote building and tested that the repository was working throughout the process.
- **[469512345](https://github.com/469512345)**, who redesigned and implemented the suggestions feature for arguments, bringing powerful asynchronous arguments and highly-extensible suggestions to the CommandAPI.  They also implemented the CommandTree feature to bring yet another way for developers to create commands with the CommandAPI.

I'd also like to give a special mention to the following people that have helped find bugs or have supported the project in some way early in the CommandAPI's development: aianlinb, Baka-Mitai, Checkium, Comeza, DerpNerb, DogeBogey, endrdragon, EnragedRabisu, i509VCB, KieranGateley, lifespan, Loapu, Marvmallow, MatrixTunnel, Ricky12Awesome, SHsuperCM, SpaceCheetah, The_Gavster, Tinkot, vladfrangu, zedwick.

-----

### Acknowledgements (CommandAPI Discord server)

I'm well aware there are lots of users that have made significant contributions to the CommandAPI that aren't listed above! Over the past four years, hundreds of users have created GitHub issues, joined the CommandAPI Discord server and submitted pull requests to contribute to the CommandAPI.

I'd like to personally give thanks to my _✨Special Contributors_ in the CommandAPI Discord server - these are users that have made an exceptionally significant contribution to the CommandAPI project in general, from helping new users get started with the CommandAPI, to contributing to the CommandAPI repository directly, to bringing a liveliness to the CommandAPI Discord server as a whole:

- **[willkroboth](https://github.com/willkroboth)**, the first _✨Special Contributor_, recognized for their above-and-beyond contribution to helping new CommandAPI Discord members.
- **[DerEchtePilz](https://github.com/DerEchtePilz)**, recognized for their significant contribution of creating a Kotlin DSL for the CommandAPI (with the help of Sparky from the CommandAPI Discord server).
- **[Hawk](https://github.com/XHawk87)**, recognized for becoming the lead developer of the CommandAPI annotation system.

The CommandAPI would not be where it is currently without the community that has been established over the years in the CommandAPI Discord server. As such, I'd like to give a special thanks to some of the most active CommandAPI Discord server members that make the community feel lively:

- ! AkaGiant !
- Brought To You By
- MineNash
- TheGates
- Strokkur24

-----

### Acknowledgements (Donators)

I didn't really want to add a way for CommandAPI users to be able to contribute financially via donations because the CommandAPI is free for all and the cost of maintaining the CommandAPI is effectively negligible. That said, I'd like to give special thanks to those that have donated to the CommandAPI.

-----

### Final comments

I never really expected more than 5 or so people to use this API, so it was truly a pleasure to see everyone's responses, issues and suggestions that has made the CommandAPI what it is today.

Thank you so much for using the CommandAPI!
