# NBT arguments

The CommandAPI includes support for NBT compounds by using the [NBT API by tr7zw](https://www.spigotmc.org/resources/nbt-api.7939/). To use NBT, use the `NBTCompoundArgument` and simply cast the argument to an `NBTContainer`.

<div class="example">

### Example - ???

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("nbt", new NBTCompoundArgument());

new CommandAPICommand("award")
    .withArguments(arguments)
    .executes((sender, args) -> {
        NBTContainer nbt = (NBTContainer) args[0];
        
        //Do something with "nbt" here...
    })
    .register();
```

</div>