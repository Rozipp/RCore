package ua.rozipp.core.config;

import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public class RConfigMapS extends RConfigMap {
    public RConfigMapS(Plugin plugin, Consumer<RConfigMapS> consumer) {
        super(plugin);
        consumer.accept(this);
    }

}
