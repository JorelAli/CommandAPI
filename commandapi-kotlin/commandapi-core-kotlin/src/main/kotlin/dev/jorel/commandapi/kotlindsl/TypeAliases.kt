package dev.jorel.commandapi.kotlindsl

import dev.jorel.commandapi.executors.CommandArguments

typealias CommandArgumentGetter<T> = (CommandArguments) -> T
