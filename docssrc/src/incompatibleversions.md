# Incompatible version information

There are a few arguments that are incompatible with various versions of Minecraft. This page outlines the full list of incompatibilities that the CommandAPI has with what versions of Minecraft.

-----

## CommandAPI behavior with respect to Minecraft version

### Minecraft version 1.16 and beyond

In Minecraft version **1.16**, the way datapacks were loaded changed in such a way that the CommandAPI had to put in additional countermeasures to provide full support to it. To illustrate this, this was the previous loading sequence for Bukkit servers in Minecraft 1.15:

\\[\texttt{Server loads}\rightarrow\texttt{Plugins load}\rightarrow\texttt{Datapacks load}\rightarrow\texttt{Server finishes loading}\\]

Instead however, Minecraft 1.16 changed the loading sequence to the following:

\\[\texttt{Server loads}\rightarrow\texttt{Datapacks load}\rightarrow\texttt{Plugins load}\rightarrow\texttt{Server finishes loading}\\]

Because the CommandAPI used to register vanilla Minecraft commands _before_ datapacks (and thus, custom Minecraft functions), it was possible to register custom commands that can be used in functions. With this new loading sequence change in Minecraft 1.16, this meant that datapacks load first before the CommandAPI does, so custom commands are not registered and functions with custom commands would fail to load.

To resolve this, the CommandAPI reloads datapacks _and recipes_ at the end:

\begin{align}
&\quad\texttt{Server loads} \\\\
\rightarrow&\quad\texttt{Datapacks load} \\\\
\rightarrow&\quad\texttt{Plugins load} \\\\
\rightarrow&\quad\texttt{Server finishes loading} \\\\
\rightarrow&\quad\texttt{Datapacks are reloaded} && \texttt{(by the CommandAPI)} \\\\
\rightarrow&\quad\texttt{Recipes are reloaded} && \texttt{(by the CommandAPI)}
\end{align}

By doing this, this means:
- Custom functions from datapacks are loaded twice
- Recipes are reloaded twice, _including_ recipes defined by other plugins

Although this sounds pretty bad (since reloading these things twice can be time consuming, thus contributing to the server start-up time), it is the only way to make custom functions work in Minecraft 1.16 and beyond.