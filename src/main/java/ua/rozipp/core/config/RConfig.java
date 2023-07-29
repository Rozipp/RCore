package ua.rozipp.core.config;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ua.rozipp.core.exception.InvalidConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RConfig {

    @NotNull Set<String> getKeys(boolean deep);

    @NotNull Map<String, Object> getValues(boolean deep);

    boolean contains(@NotNull String path);

    @Nullable Object get(@NotNull String path);

    void set(@NotNull String path, @Nullable Object value);

    <T> T getObject(@NotNull String path, @NotNull Class<T> aClass);

    String getString(String path, String defVal, String message) throws InvalidConfiguration;

    Integer getInt(String path, Integer defVal, String message) throws InvalidConfiguration;

    Boolean getBoolean(String path, Boolean defVal, String message) throws InvalidConfiguration;

    Double getDouble(String path, Double defVal, String message) throws InvalidConfiguration;

    @SuppressWarnings("unchecked")
    default List<RConfig> getRConfigList(String path) throws InvalidConfiguration {
        List<?> list = (List<?>) get(path);
        if (list != null && !list.isEmpty()) {
            List<RConfig> result = new ArrayList<>();
            for (Object o : list) {
                try {
                    if (o instanceof ConfigurationSection) {
                        RConfig rConfigSection = new RConfigConfigurationSection((ConfigurationSection) o);
                        result.add(rConfigSection);
                    }
                    if (o instanceof Map) {
                        RConfig rConfigSection = new RConfigMap((Map<String, Object>) o);
                        result.add(rConfigSection);
                    }
                } catch (Exception ignored) {
                }
            }
            return result;
        }
        throw new InvalidConfiguration("This configSection does not contain the data block \"" + path + "\"");
    }

    <T> List<T> getList(String path, Class<T> aClass, List<T> defVal, String message) throws InvalidConfiguration;

    default RConfig getRConfig(String path, String message) throws InvalidConfiguration {
        Object o = get(path);
        RConfig result = null;
        try {
            if (o instanceof ConfigurationSection) {
                result = new RConfigConfigurationSection((ConfigurationSection) o);
            }
            if (o instanceof Map) {
                result = new RConfigMap((Map<String, Object>) o);
            }
        } catch (Exception ignored) {
        }
        if (result != null)
            return result;
        throw new InvalidConfiguration("This configSection does not contain the data block \"" + path + "\"");
    }

}
