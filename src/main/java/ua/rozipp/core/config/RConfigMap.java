package ua.rozipp.core.config;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public class RConfigMap extends RConfigSection {
    private final Map<String, Object> map;

    public RConfigMap(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public @NotNull Set<String> getKeys(boolean deep) {
        return map.keySet();
    }

    @Override
    public @NotNull Map<String, Object> getValues(boolean deep) {
        return ImmutableMap.copyOf(map);
    }

    @Override
    public boolean contains(@NotNull String path) {
        return map.containsKey(path);
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        return map.get(path);
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        map.put(path, value);
    }

    @Override
    public <T> T getObject(@NotNull String path, @NotNull Class<T> aClass) {
        return (T) map.get(path);
    }
}
