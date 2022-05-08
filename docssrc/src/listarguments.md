# List arguments

![](./images/arguments/listargument.png)

List arguments allows users to provide a list of values. This argument uses an underlying `GreedyStringArgument`, so the greedy string argument rule applies - **this argument can only be used at the end of an argument list**.

-----

## The `ListArgumentBuilder`

Unlike other arguments, because this argument can be interpreted in various different ways, this argument can only be created using a `ListArgumentBuilder`, instead of directly accessing the `ListArgument` constructor. The `ListArgumentBuilder` follows the following format:

\begin{align}
&\quad\texttt{Create } \\\\
\rightarrow&\quad\texttt{Datapacks load} \\\\
\rightarrow&\quad\texttt{Plugins load} \\\\
\rightarrow&\quad\texttt{Server finishes loading} \\\\
\rightarrow&\quad\texttt{Datapacks are reloaded} && \texttt{(by the CommandAPI)} \\\\
\rightarrow&\quad\texttt{Recipes are reloaded} && \texttt{(by the CommandAPI)}
\end{align}