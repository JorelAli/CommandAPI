package dev.jorel.commandapi.polyfills;

import org.bukkit.NamespacedKey;

public class AdvancementPolyfill {
    private NamespacedKey key;

    public AdvancementPolyfill(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return this.key;
    }
}
