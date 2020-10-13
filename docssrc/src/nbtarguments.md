# NBT arguments

The CommandAPI includes support for NBT compounds by using the [NBT API by tr7zw](https://www.spigotmc.org/resources/nbt-api.7939/). To use NBT, use the `NBTCompoundArgument` and simply cast the argument to an `NBTContainer`.

Since this argument depends on the NBT API, if this is used and the NBT API is not available on the server, an `NBTAPINotFoundException` will be thrown.

<div class="example">

### Example - ???

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:nbtcompoundarguments}}
```

</div>

> **Developer's Note:**
>
> I haven't personally explored much with using this argument, so this example isn't great. If you believe you can supply a suitable example for this page, feel free to send an example [on the CommandAPI issues page](https://github.com/JorelAli/CommandAPI/issues/new/choose).