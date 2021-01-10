# Skipping proxy senders

When the CommandAPI converts a command, it does some tweaks to the thing running the command (such as the player or console) to improve compatibility with plugins (mostly permissions). This doesn't _always_ work, and can sometimes produce an error which looks like this:

```java
[20:44:01 ERROR]: java.lang.IllegalArgumentException: object is not an instance of declaring class
[20:44:01 ERROR]:  at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
[20:44:01 ERROR]:  at sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
[20:44:01 ERROR]:  at sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
[20:44:01 ERROR]:  at java.lang.reflect.Method.invoke(Unknown Source)
[20:44:01 ERROR]:  at dev.jorel.commandapi.Converter.lambda$mergeProxySender$3(Converter.java:151)
[20:44:01 ERROR]:  at com.sun.proxy.$Proxy33.getInventory(Unknown Source)
```

To fix this, add the plugin which the command is registered from to the list of plugins under `skip-sender-proxy`.

<div class="example">

### Example - Improving compatibility with the SkinsRestorer plugin

[SkinsRestorer](https://www.spigotmc.org/resources/skinsrestorer.2124/) (not associated or sponsored by the CommandAPI in any way) is a plugin that lets you change the skin for a player. This suffers from the above issue and is not compatible with the CommandAPI's conversion compatibility tweaks. To do this, we'll add `SkinsRestorer` to the list of plugins which should be skipped:

```yaml
verbose-outputs: true
create-dispatcher-json: false
plugins-to-convert: 
  - SkinsRestorer: ~
skip-sender-proxy:
  - SkinsRestorer
```

</div>

