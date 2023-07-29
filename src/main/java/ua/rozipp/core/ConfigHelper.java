package ua.rozipp.core;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ua.rozipp.core.config.RConfigFile;

public class ConfigHelper {

    public static RConfigFile loadConfigFile(Plugin plugin, String filepath) {
        return RConfigFile.loadConfigFile(plugin, filepath);
    }

    public static void saveToConfigFile(Plugin plugin, String fileName, @NotNull String path, @Nullable Object value) {
        RConfigFile.saveToConfigFile(plugin, fileName, path, value);
    }

}
