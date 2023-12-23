package ua.rozipp.core.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ua.rozipp.core.LogHelper;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RConfigFile extends RConfigMap {

    private final File file;
    private YamlConfiguration yamlConfiguration;
    final String key;

    RConfigFile(@NotNull Plugin plugin, @NotNull String filePath) throws IOException {
        super(plugin);
        file = new File(plugin.getDataFolder(), filePath + ".yml");
        if (!file.exists()) throw new IOException("File: \"" + filePath + ".yml \" was missing.");
        key = getFileKey(plugin, filePath);
        load();
        ConfigHelper.rConfigFiles.put(key, this);
    }

    static String getFileKey(Plugin plugin, String filePath) {
        return plugin.getName() + ":" + filePath;
    }

    public void setAndSave(@NotNull String path, @Nullable Object value) {
        set(path, value);
        try {
            save();
        } catch (IOException e) {
            LogHelper.error(e.getMessage());
        }
    }

    public void load() throws IOException {
        try {
            yamlConfiguration = new YamlConfiguration();
            yamlConfiguration.load(file);
        } catch (InvalidConfigurationException e) {
            throw new IOException("filepath=" + file.getAbsolutePath() + ": " + e.getMessage());
        }
        values.clear();
        LogHelper.fine("Configuration file: \"" + key + "\" loaded");

        for (String key : yamlConfiguration.getKeys(false)) {
            Object o = yamlConfiguration.get(key);
            Object dest = get(key);

            if (o instanceof List) {
                List<Object> result;
                if (dest == null) {
                    result = new LinkedList<>();
                    values.put(key, result);
                } else result = (List<Object>) dest;
                for (Object ob : (List) o) {
                    RConfig rConfig = RConfig.createRConfig(getPlugin(), ob);
                    result.add((rConfig == null) ? ob : rConfig);
                }
                continue;
            }

            if (dest == null) {
                RConfig rConfig = RConfig.createRConfig(getPlugin(), o);
                values.put(key, (rConfig == null) ? o : rConfig);
                continue;
            }

            if (o instanceof Map) {
                if (dest instanceof RConfigMap) {
                    ((RConfigMap) dest).putAll((Map<String, ?>) o);
                    continue;
                }
                if (dest instanceof Map) {
                    ((Map<String, Object>) dest).putAll((Map<String, ?>) o);
                    continue;
                }
            }
        }
    }

    public void save() throws IOException {
        yamlConfiguration.save(file);
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        super.set(path, value);
        yamlConfiguration.set(path, value);
    }

    @Override
    public String toString() {
        return "RConfigFile:\"" + key + "\": " + getKeys(false).toString();
    }
}
