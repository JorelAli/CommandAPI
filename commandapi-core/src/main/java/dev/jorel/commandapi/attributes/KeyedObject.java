package dev.jorel.commandapi.attributes;

import org.bukkit.NamespacedKey;

public class KeyedObject {
    private NamespacedKey key;

    public KeyedObject(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return this.key;
    }
}
