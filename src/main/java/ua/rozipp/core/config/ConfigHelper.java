package ua.rozipp.core.config;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigHelper {

    static final Map<String, RConfigFile> rConfigFiles = new HashMap<>();

    /**
     * Если filePath уже был загружен, то возвращаем его, иначе создаём новый RConfigFile
     * @param plugin - ваш плагин
     * @param filePath - пусть к файлу относительно папки конфигурационных файлов
     * @return - RConfigFile ссылающийся на нужный конфигурационный файл
     * @throws IOException - ошибка конфигурационных файлов
     */
    public static RConfigFile getRConfigFile(Plugin plugin, String filePath) throws IOException {
        String key = RConfigFile.getFileKey(plugin, filePath);
        if (rConfigFiles.containsKey(key)) return rConfigFiles.get(key);
        return new RConfigFile(plugin, filePath);
    }

    /**
     * Добавить значение в файл конфигурации
     * @param plugin - плагин
     * @param filepath - путь к файлу конфигурации
     * @param path - ключ конфига
     * @param value - новое значение конфига
     * @throws IOException
     */
    public static void saveToConfigFile(Plugin plugin, String filepath, @NotNull String path, @Nullable Object value) throws IOException {
        RConfigFile rConfig = getRConfigFile(plugin, filepath);
        rConfig.setAndSave(path, value);
    }
}
