# List arguments

![A list argument with the command "/multigive @p stone grass_block dirt" and Minecraft suggestions with a list of Minecraft items](./images/arguments/listargument.png)

List arguments allows users to provide a list of values. This argument uses an underlying `GreedyStringArgument`, so the greedy string argument rule applies - **this argument can only be used at the end of an argument list**.

-----

## The `ListArgumentBuilder`

Unlike other arguments, because this argument can be interpreted in various different ways, this argument can only be created using a `ListArgumentBuilder`, instead of directly accessing the `ListArgument` constructor. The `ListArgumentBuilder` loosely follows the following format:

\begin{align}
&\quad\texttt{Create a ListArgumentBuilder} \\\\
\rightarrow&\quad\texttt{Provide the list to pull suggestions from} \\\\
\rightarrow&\quad\texttt{Provide the mapper of the list items to a string} \\\\
\rightarrow&\quad\texttt{Build the ListArgument}
\end{align}
