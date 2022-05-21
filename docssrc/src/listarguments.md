# List arguments

![A list argument with the command "/multigive @p stone grass_block dirt" and Minecraft suggestions with a list of Minecraft items](./images/arguments/listargument.png)

List arguments allows users to provide a list of values. This argument uses an underlying `GreedyStringArgument`, so the greedy string argument rule applies - **this argument can only be used at the end of an argument list**.

-----

## The `ListArgumentBuilder`

Unlike other arguments, because this argument can be interpreted in various different ways, this argument can only be created using a `ListArgumentBuilder`, instead of directly accessing the `ListArgument` constructor. The `ListArgumentBuilder` loosely follows the following format:

\begin{align}
&\quad\texttt{Create a ListArgumentBuilder} \\\\
\rightarrow&\quad\texttt{\(Provide the list delimiter\)} \\\\
\rightarrow&\quad\texttt{Provide the list to pull suggestions from} \\\\
\rightarrow&\quad\texttt{Provide the mapper of the list items to a string} \\\\
\rightarrow&\quad\texttt{Build the ListArgument}
\end{align}

### Construction

First, you have to create a `ListArgumentBuilder` parameterized over the type that the list will generate. For example, if you want to create a list of Strings, you would use `new ListArgumentBuilder<String>`.

- The `nodeName` parameter represents the name of the node to use for the argument.
- The optional `delimiter` argument specifies the delimiter (separator) to use between entries. If a delimiter is not provided, a space `" "` will be used as the delimiter.

```java
public ListArgumentBuilder<T>(String nodeName);
public ListArgumentBuilder<T>(String nodeName, String delimiter);
```

\\[\downarrow\\]

> ### Allowing duplicates (Optional)
>
> If you want your users to enter duplicate entries in your list, you can use the `allowDuplicates` method to set whether duplicates are allowed. By default, duplicates are disabled.
>
> When duplicates are enabled, items that have been entered before can be displayed again in the list of suggestions:
>
> ```java
> ListArgumentBuilder.allowDuplicates(true);
> ```
>
> ![List arguments with duplicates enabled](./images/arguments/listargument_with_duplicates.gif)
>
> When duplicates are disabled, items that have already been entered will not appear in the list of suggestions:
>
> ```java
> ListArgumentBuilder.allowDuplicates(false);
> ```
>
> ![List arguments with duplicates disabled](./images/arguments/listargument_without_duplicates.gif)

### Providing the list

The `ListArgument` requires a list that is displayed as suggestions to the user.
