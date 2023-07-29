package ua.rozipp.core.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.exception.InvalidConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RConfigFile extends YamlConfiguration implements RConfig {

    private static final Map<String, RConfigFile> rConfigFiles = new HashMap<>();

    public static RConfigFile loadConfigFile(Plugin plugin, String filepath) {
        RConfigFile rConfigFile = new RConfigFile(plugin, filepath);
        rConfigFiles.put(getFileKey(plugin, filepath), rConfigFile);
        return rConfigFile;
    }

    public static void saveToConfigFile(Plugin plugin, String fileName, @NotNull String path, @Nullable Object value) {
        RConfigFile rConfig = rConfigFiles.get(getFileKey(plugin, fileName));
        if (rConfig == null) {
            rConfig = loadConfigFile(plugin, fileName);
        }
        rConfig.set(path, value);
    }

    private static String getFileKey(Plugin plugin, String filepath) {
        return ((plugin != null) ? plugin.getName() + ":" : "") + filepath;
    }

    private File file;
    private boolean loaded = false;
    private final String filepath;

    public RConfigFile(@NotNull Plugin plugin, @NotNull String filepath) {
        super();
        this.filepath = filepath;
        this.file = new File(plugin.getDataFolder(), filepath);
        if (file.isDirectory()) {
            file = null;
            return;
        }
        if (!file.exists()) {
            LogHelper.warning("Configuration file:" + filepath + " was missing. Streaming to disk from Jar.");
            try {
                plugin.saveResource(filepath, true);
            } catch (Exception e) {
                LogHelper.error("Could not get file \"" + filepath + "\" from plugin file " + plugin.getName() + ".jar");
                file = null;
                return;
            }
        }

        try {
            load();
        } catch (IOException | InvalidConfigurationException e) {
            LogHelper.warning(e.getMessage());
        }
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void set(@NotNull String path, @Nullable Object value, boolean save) {
        if (!loaded) return;
        super.set(path, value);
        if (save) {
            try {
                save();
            } catch (IOException e) {
                LogHelper.error(e.getMessage());
            }
        }
    }

    public void reload() throws IOException, InvalidConfigurationException {
        this.map.clear();
        load();
    }

    public void load() throws IOException, InvalidConfigurationException {
        if (file == null) return;
        try {
            this.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            LogHelper.error("filepath=" + filepath + ": " + e.getMessage());
            file = null;
            return;
        }

        loaded = true;
        LogHelper.fine("Configuration file: \"" + filepath + "\" loaded");
    }

    public void save() throws IOException {
        if (!loaded) return;
        if (file != null) save(file);
    }

    @Override
    public String getString(String path, @Nullable String defVal, String message) throws InvalidConfiguration {
        String result = getString(path, defVal);
        if (result == null)
            throw new InvalidConfiguration((message != null) ? message : "This configSection does not contain the data block \"" + path + "\"");
        return result;
    }

    @Override
    public Integer getInt(String path, @Nullable Integer defVal, String message) throws InvalidConfiguration {
        try {
            Object o = get(path);
            if (o instanceof Number)
                return ((Number) o).intValue();
            if (o instanceof String)
                return Integer.parseInt((String) o);
        } catch (Exception ignored) {
        }
        if (defVal == null)
            throw new InvalidConfiguration((message != null) ? message : "This configSection does not contain the data block \"" + path + "\"");
        return defVal;
    }

    @Override
    public Boolean getBoolean(String path, @Nullable Boolean defVal, String message) throws InvalidConfiguration {
        try {
            return getBoolean(path, defVal);
        } catch (NullPointerException e) {
            throw new InvalidConfiguration((message != null) ? message : "This configSection does not contain the data block \"" + path + "\"");
        }
    }

    @Override
    public Double getDouble(String path, @Nullable Double defVal, String message) throws InvalidConfiguration {
        try {
            Object o = get(path);
            if (o instanceof Number)
                return getDouble(path, defVal);
            if (o instanceof String)
                return Double.parseDouble((String) o);
        } catch (NullPointerException e) {
            throw new InvalidConfiguration((message != null) ? message : "This configSection does not contain the data block \"" + path + "\"");
        }
        throw new InvalidConfiguration((message != null) ? message : "This configSection does not contain the data block \"" + path + "\"");
    }

    @Override
    public <T> List<T> getList(String path, Class<T> aClass, List<T> defVal, String message) throws InvalidConfiguration {
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
        if (defVal == null)
            throw new InvalidConfiguration((message != null) ? message : "This is not List or path not found. Key=" + path);
        return defVal;
    }

    @Override
    public @NotNull Set<String> getKeys(boolean deep) {
        return super.getKeys(deep);
    }

}
