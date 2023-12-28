# NBT Compound argument

### Using Minecraft's NBT objects

|Spigot-mapped name|Mojang-mapped name|
|------------------|------------------|
|`NBTTagCompound`|`CompoundTag`|
|`NBTBase`|`Tag`|

NBTTagCompound

The CommandAPI allows you to access the NMS `NBTTagCompound` class via `net.minecraft.nbt.NBTTagCompound` directly without requiring any additional APIs to access NBT. This is especially useful for Mojang-mapped servers.

```java
@Override
public void onLoad() {
    CommandAPI.onLoad(new CommandAPIBukkitConfig(this)
        .initializeNBTAPI(NBTTagCompound.class, o -> o)
    );
}
```

------

## Using the NBT API

-----

## Using Rtag

[Rtag](https://www.spigotmc.org/resources/100694/) is an easily shadeable API that allows you to easily interact with NBT and is highly compatible with the CommandAPI! There are multiple ways to deconstruct NBT arguments using Rtag:

### Accessing individual paths using `Rtag.INSTANCE.get()`

The [`Rtag#get`](https://javadoc.saicone.com/rtag/com/saicone/rtag/Rtag.html#get(java.lang.Object,java.lang.Object...)) method allows you to retrieve objects directly from an NMS `NBTTagCompound` object without requiring you to import `net.minecraft.net.NBTTagCompound`

```java
@Override
public void onLoad() {
    CommandAPI.onLoad(new CommandAPIBukkitConfig(this)
        .initializeNBTAPI(Object.class, o -> o)
    );
}
```

<div class="example">

For example, if you have an NBT compound tag with this data:

```json
{
    "some": {
        "path": {
            "here": "hello"
        }
    }
}
```

You can access the string value `hello` using `Rtag.INSTANCE.get()`:

```java
new CommandAPICommand("test")
    .withArguments(new NBTCompoundArgument<Object>("nbt"))
    .executes((sender, args) -> {
        String result = Rtag.INSTANCE.get(args.get("nbt"), "some", "path", "here");
    })
    .register();
```

</div>

### Converting the whole compound tag into standard Java objects

The easiest way to interact with Rtag is to deconstruct a compound tag into a `Map` of Java objects. Rtag is able to do this easily using `TagCompound#getValue`, with the `Rtag.INSTANCE` field:

```java
@Override
public void onLoad() {
    CommandAPI.onLoad(new CommandAPIBukkitConfig(this)
        .initializeNBTAPI(Map.class, o -> TagCompound.getValue(Rtag.INSTANCE, o))
    );
}
```

<div class="example">

Say you have an NBT compound tag that looks like this:

```json
{
    name: "Notch",
    exp: 20s,
    armor: ["diamond_helmet", "elytra", "diamond_leggings", "leather_boots"],
    hand_item: {
        type: "diamond_pickaxe",
        durability: 123
    }
}
```

Using Rtag's representation of an NBT compound tag using standard Java objects would convert the above NBT compound tag into the following object:

```java
Map<String, Object> {
    "name": String "Notch",
    "exp": Short 20,
    "armor": List<String> ["diamond_helmet", "elytra", "diamond_leggings", "leather_boots"],
    "hand_item": Map<String, Object> {
        "type": String "diamond_pickaxe",
        "durability": Integer 123
    }
}
```

This 

</div>
