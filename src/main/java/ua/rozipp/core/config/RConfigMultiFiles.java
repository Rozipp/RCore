package ua.rozipp.core.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ua.rozipp.core.LogHelper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RConfigMultiFiles extends RConfig {

    private final Map<String, RConfigFile> files = new HashMap<>();
    private final Map<String, Object> value = new LinkedHashMap<>();

    public RConfigMultiFiles(Plugin plugin) {
        super(plugin);
    }

    private void addFile(String fileKey, RConfigFile rConfigFile) {
        files.put(fileKey, rConfigFile);
        for (String key : rConfigFile.getKeys(false)) {
            Object o = rConfigFile.get(key);
            Object dest = value.get(key);

            if (o instanceof List) {
                List<Object> result;
                if (dest == null) {
                    result = new LinkedList<>();
                    value.put(key, result);
                } else result = (List<Object>) dest;
                for (Object ob : (List) o) {
                    RConfig rConfig = RConfig.createRConfig(getPlugin(), ob);
                    result.add((rConfig == null) ? ob : rConfig);
                }
                continue;
            }

            if (dest == null) {
                RConfig rConfig = RConfig.createRConfig(getPlugin(), o);
                value.put(key, (rConfig == null) ? o : rConfig);
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
            LogHelper.error("Found duplicate key: \"" + key + "\" in files: " + foundKey(key));
        }
    }

    public void loadFile(String filepath) {
        loadFiles(filepath, false);
    }

    public void loadFiles(boolean deep) {
        loadFiles("", deep);
    }

    public void loadFiles() {
        loadFiles("", true);
    }

    public void reload() {
        value.clear();
        files.values().forEach(f -> {
            try {
                f.load();
                addFile(f.key, f);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void loadFiles(String filepath, boolean deep) {
        File file = (filepath.isEmpty()) ? getPlugin().getDataFolder() : new File(getPlugin().getDataFolder(), filepath);
        if (file.isDirectory() && deep) {
            for (String fileName : Objects.requireNonNull(file.list())) {
                if (fileName.startsWith("\\")) fileName = fileName.substring(1);
                fileName = fileName.replaceAll("\\.(\\w+)$", "");
                loadFiles(filepath + File.separator + fileName, true);
            }
        } else {
            try {
                RConfigFile rConfigFile = ConfigHelper.getRConfigFile(getPlugin(), filepath);
                addFile(rConfigFile.key, rConfigFile);
            } catch (IOException e) {
                LogHelper.error(e.getMessage());
            }
        }
    }


    @Override
    public @NotNull Set<String> getKeys(boolean deep) {
        return Collections.unmodifiableSet(files.keySet());
    }

    @Override
    public @NotNull Map<String, Object> getValues(boolean deep) {
        return Collections.unmodifiableMap(files);
    }

    @Override
    public boolean contains(@NotNull String path) {
        String[] split = RConfig.splitFirstAndOther(path);
        if (split.length == 1) return files.containsKey(split[0]);
        return files.get(split[0]).contains(split[1]);
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        if (path.isEmpty()) return this;
        String[] split = RConfig.splitFirstAndOther(path);
        if (files.containsKey(split[0])) {
            if (split.length == 1) return files.get(split[0]);
            else return files.get(split[0]).get(split[1]);
        }
        if (value.containsKey(split[0])) {
            Object o = value.get(split[0]);
            if (split.length == 1 || o == null) {
                return o;
            } else {
                if (o instanceof RConfig) return ((RConfig) o).get(split[1]);
                if (o instanceof ConfigurationSection) return ((ConfigurationSection) o).get(split[1]);
                if (o instanceof Map) return RConfigMap.getChildren((Map<String, Object>) o, split[1]);
            }
        }
        return null;
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        Exception e = new Exception("Using method Set() in RConfigMultiFiles not supported");
        e.printStackTrace();
    }

    public List<String> foundKey(String key) {
        List<String> list = new ArrayList<>();
        for (RConfigFile rConfigFile : files.values()) {
            if (rConfigFile.contains(key)) list.add(rConfigFile.key);
        }
        return list;
    }

    @Override
    public String toString() {
        return "RConfigMultiFiles:\"" + getPlugin() + "\": " + files.keySet().toString();
    }
}
