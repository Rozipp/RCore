package ua.rozipp.core.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.exception.InvalidConfiguration;

import java.util.*;

public abstract class RConfig {

    static @Nullable RConfig createRConfig(Plugin plugin, Object o) {
        try {
            if (o instanceof RConfig) return (RConfig) o;
            if (o instanceof ConfigurationSection)
                return new RConfigMap(plugin, (ConfigurationSection) o);
            if (o instanceof Map)
                return new RConfigMap(plugin, (Map<String, Object>) o);
        } catch (Exception ignored) {
        }
        return null;
    }

    protected static String[] splitFirstAndOther(String path) {
        String[] split = path.split("\\.");
        if (split.length == 0) return new String[]{path};
        if (split.length == 1) return new String[]{split[0]};
        return new String[]{split[0], String.join(".", Arrays.copyOfRange(split, 1, split.length))};
    }

    private final Plugin plugin;

    protected RConfig(Plugin plugin) {
        this.plugin = plugin;
    }

    public @NotNull Plugin getPlugin() {
        return plugin;
    }

    public abstract @NotNull Set<String> getKeys(boolean deep);

    public abstract @NotNull Map<String, Object> getValues(boolean deep);

    public abstract boolean contains(@NotNull String path);

    public abstract void set(@NotNull String path, @Nullable Object value);

    public abstract @Nullable Object get(@NotNull String path);

    public @Nullable Object get(@NotNull String path, @Nullable Object def) {
        return contains(path) ? get(path) : def;
    }

    public @NotNull Object get(@NotNull String path, @Nullable Object def, @Nullable String message) throws InvalidConfiguration {
        Object o = get(path);
        if (o != null) return o;
        if (def != null) return def;
        throw new InvalidConfiguration((message != null) ? message : "This RConfig does not contain the data block \"" + path + "\"");
    }

    public <T> @Nullable T getObject(@NotNull String path, @NotNull Class<T> clazz) {
        Object o = get(path);
        if (clazz.isInstance(o)) return clazz.cast(o);
        return null;
    }

    public <T> @Nullable T getObject(@NotNull String path, @NotNull Class<T> clazz, @Nullable T def) {
        T o = getObject(path, clazz);
        if (o != null) return o;
        return def;
    }

    public <T> @NotNull T getObject(@NotNull String path, @NotNull Class<T> clazz, @Nullable T def, @Nullable String message) throws InvalidConfiguration {
        T o = getObject(path, clazz);
        if (o != null) return o;
        if (def != null) return def;
        throw new InvalidConfiguration((message != null) ? message : "This RConfig does not contain the data block \"" + path + "\", or it is not " + clazz.getName());
    }

    public @Nullable String getString(@NotNull String path) {
        return getObject(path, String.class);
    }

    public @Nullable String getString(@NotNull String path, @Nullable String def) {
        return getObject(path, String.class, def);
    }

    public @NotNull String getString(@NotNull String path, @Nullable String def, @Nullable String message) throws InvalidConfiguration {
        return getObject(path, String.class, def, message);
    }

    public int getInt(@NotNull String path) {
        Object o = get(path);
        return (o != null) ? (int) o : 0;
    }

    public int getInt(@NotNull String path, int def) {
        Object o = get(path);
        return (o != null) ? (int) o : def;
    }

    public int getInt(@NotNull String path, Integer def, String message) throws InvalidConfiguration {
        return getObject(path, Integer.class, def, message);
    }

    public boolean isInt(@NotNull String path) {
        return (get(path) instanceof Integer);
    }

    public boolean getBoolean(@NotNull String path) {
        Object o = get(path);
        return (o != null) ? (Boolean) o : false;
    }

    public boolean getBoolean(@NotNull String path, boolean def) {
        Object o = get(path);
        return (o != null) ? (boolean) o : def;
    }

    public boolean getBoolean(@NotNull String path, @Nullable Boolean def, @Nullable String message) throws InvalidConfiguration {
        return getObject(path, Boolean.class, def, message);
    }

    public boolean isBoolean(@NotNull String path) {
        return (get(path) instanceof Boolean);
    }

    public double getDouble(@NotNull String path) {
        Object o = get(path);
        return (o != null) ? (Double) o : 0;
    }

    public double getDouble(@NotNull String path, double def) {
        Object o = get(path);
        return (o != null) ? (Double) o : def;
    }

    public double getDouble(@NotNull String path, @Nullable Double def, @Nullable String message) throws InvalidConfiguration {
        return getObject(path, Double.class, def, message);
    }

    public boolean isDouble(@NotNull String path) {
        return (get(path) instanceof Double);
    }

    public long getLong(@NotNull String path) {
        Object o = get(path);
        return (o != null) ? (Long) o : 0;
    }

    public long getLong(@NotNull String path, long def) {
        Object o = get(path);
        return (o != null) ? (Long) o : def;
    }

    public long getLong(@NotNull String path, @Nullable Long def, @Nullable String message) throws InvalidConfiguration {
        return getObject(path, Long.class, def, message);
    }

    public boolean isLong(@NotNull String path) {
        return (get(path) instanceof Long);
    }

    public @Nullable List<?> getList(@NotNull String path) {
        return (List<?>) get(path);
    }

    public @Nullable List<?> getList(@NotNull String path, @Nullable List<?> def) {
        Object o = get(path);
        return (o != null) ? (List<?>) o : def;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String path, Class<T> aClass, List<T> def, String message) throws InvalidConfiguration {
        List<?> list = getList(path);
        if (list != null) {
            List<T> result = new ArrayList<>();
            for (Object o : list)
                try {
                    result.add((T) o);
                } catch (Exception ignored) {
                }
            return result;
        }
        if (def != null) return def;
        throw new InvalidConfiguration((message != null) ? message : "This RConfig does not contain the data block \"" + path + "\"");
    }

    public boolean isList(@NotNull String path) {
        return (get(path) instanceof List);
    }

    public @NotNull List<RConfig> getRConfigList(@NotNull String path) throws InvalidConfiguration {
        List<?> list = getList(path);
        if (list != null && !list.isEmpty()) {
            List<RConfig> result = new ArrayList<>();
            for (Object o : list) {
                RConfig rConfig = RConfig.createRConfig(getPlugin(), o);
                if (rConfig != null) result.add(rConfig);
            }
            return result;
        }
        throw new InvalidConfiguration("This RConfig does not contain the data block \"" + path + "\"");
    }

    public @Nullable RConfig getRConfig(@NotNull String path) {
        return RConfig.createRConfig(getPlugin(), get(path));
    }

    public Map<String, ?> getMap(String path) throws InvalidConfiguration {
        Object o = get(path);
        if (o instanceof Map) return (Map<String, ?>) o;
        if (o instanceof ConfigurationSection) {
            return ((ConfigurationSection) o).getValues(false);
        }
        if (o instanceof RConfig) {
            return ((RConfig) o).getValues(false);
        }
        throw new InvalidConfiguration("This RConfig does not contain the data block \"" + path + "\"");
    }

    @SuppressWarnings("unchecked")
    public <T extends ConfigurationSerializable> @Nullable T getSerializable(@NotNull String path, @NotNull Class<T> clazz) {
        try {
            return (T) ConfigurationSerialization.deserializeObject(getMap(path), clazz);
        } catch (InvalidConfiguration e) {
            LogHelper.error(e.getMessage());
            return null;
        }
    }

    public <T extends ConfigurationSerializable> @Nullable T getSerializable(@NotNull String path, @NotNull Class<T> clazz, @Nullable T def) {
        T t = getSerializable(path, clazz);
        return (t != null) ? t : def;
    }

    public <T extends ConfigurationSerializable> @Nullable T getSerializable(@NotNull String path, @NotNull Class<T> clazz, @Nullable T def, String message) throws InvalidConfiguration {
        T t = getSerializable(path, clazz);
        if (t != null) return t;
        if (def != null) return def;
        throw new InvalidConfiguration((message != null) ? message : "This RConfig does not contain the data block \"" + path + "\", or it is not " + clazz.getName());
    }

}
