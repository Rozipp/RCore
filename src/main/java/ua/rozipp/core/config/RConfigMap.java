package ua.rozipp.core.config;

import com.google.common.collect.ImmutableMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class RConfigMap extends RConfig {

    protected final Map<String, Object> values;

    public RConfigMap(Plugin plugin) {
        super(plugin);
        this.values = new LinkedHashMap<>();
    }

    public RConfigMap(Plugin plugin, Map<String, Object> map) {
        this(plugin);
        this.values.putAll(map);
    }

    public RConfigMap(Plugin plugin, ConfigurationSection section) {
        this(plugin);
        for (String key : section.getKeys(false)) {
            Object o = section.get(key);
            RConfig rConfig = RConfig.createRConfig(plugin, o);
            values.put(key, (rConfig == null) ? o : rConfig);
        }
    }

    @Override
    public @NotNull Set<String> getKeys(boolean deep) {
        return values.keySet();
    }

    @Override
    public @NotNull Map<String, Object> getValues(boolean deep) {
        return ImmutableMap.copyOf(values);
    }

    @Override
    public boolean contains(@NotNull String path) {
        return values.containsKey(path);
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        values.put(path, value);
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        return getChildren(values, path);
    }

    static Object getChildren(Map<String, Object> map, String path) {
        if (path.isEmpty()) return null;
        String[] split = splitFirstAndOther(path);
        if (split.length < 2) return map.get(path);

        Object o = map.get(split[0]);
        if (o == null) return null;
        if (o instanceof RConfig) return ((RConfig) o).get(split[1]);
        if (o instanceof ConfigurationSection) return ((ConfigurationSection) o).get(split[1]);
        if (o instanceof Map) return getChildren((Map<String, Object>) o, split[1]);
        return null;
    }

    @Override
    public String toString() {
        return "RConfigMap: " + values.toString();
    }

    public void putAll(Map<String, ?> map) {
        this.values.putAll(map);
    }

    public void put(String id, Object o) {
        values.put(id, o);
    }

}
