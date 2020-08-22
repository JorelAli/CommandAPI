# Predicate tips

In our [example for creating a party system](./requirements.md#example---a-party-creation-and-teleportation-system), we ended up having lots of code repetition. In our party creation command, we had the following code:

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("createParty", new LiteralArgument("create")
	.withRequirement(sender -> {
		
		return !partyMembers.containsKey(((Player) sender).getUniqueId());
        
	}));
arguments.put("partyName", new StringArgument());
```

And for our party teleportation command, we had the following code:

```java
arguments = new LinkedHashMap<>();
arguments.put("teleport", new LiteralArgument("tp")
	.withRequirement(sender -> {
		
		return partyMembers.containsKey(((Player) sender).getUniqueId());
        
	}));
```

We can simplify this code by declaring the predicate:

```java
Predicate<CommandSender> testIfPlayerHasParty = sender -> {
    return partyMembers.containsKey(((Player) sender).getUniqueId());
};
```

Now, we can use the predicate `testIfPlayerHasParty` in our code for creating a party. Since we want to apply the "not" (`!`) operator to this predicate, we can use `.negate()` to invert the result of our predicate:

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("createParty", new LiteralArgument("create")
	.withRequirement(testIfPlayerHasParty.negate());
arguments.put("partyName", new StringArgument());
```

And we can use it again for our code for teleporting to party members:

```java
arguments = new LinkedHashMap<>();
arguments.put("teleport", new LiteralArgument("tp")
	.withRequirement(testIfPlayerHasParty));
```

