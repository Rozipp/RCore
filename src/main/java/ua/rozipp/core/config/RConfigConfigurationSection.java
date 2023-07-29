package ua.rozipp.core.config;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public class RConfigConfigurationSection extends RConfigSection {
    private final ConfigurationSection configurationSection;

    public RConfigConfigurationSection(ConfigurationSection configurationSection) {
        this.configurationSection = configurationSection;
    }

    @Override
    public @NotNull Set<String> getKeys(boolean deep) {
        return configurationSection.getKeys(deep);
    }

    @Override
    public @NotNull Map<String, Object> getValues(boolean deep) {
        return configurationSection.getValues(deep);
    }

    @Override
    public boolean contains(@NotNull String path) {
        return configurationSection.contains(path);
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        return configurationSection.get(path);
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        configurationSection.set(path, value);
    }

    @Override
    public <T> T getObject(@NotNull String path, @NotNull Class<T> aClass) {
        return configurationSection.getObject(path, aClass);
    }
}
