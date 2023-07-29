package ua.rozipp.core.config;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ua.rozipp.core.exception.InvalidConfiguration;

import java.io.File;
import java.util.*;

public class RConfigMultiFiles implements RConfig {

    private final List<RConfigFile> files = new ArrayList<>();

    public RConfigMultiFiles() {
    }

    public RConfigMultiFiles(Plugin plugin) {
        {
            File dir = plugin.getDataFolder();
            for (String fileName : Objects.requireNonNull(dir.list())) {
                RConfigFile file = new RConfigFile(plugin, fileName);
                if (file.isLoaded()) files.add(file);
            }
        }
        {
            File dir = new File(plugin.getDataFolder(), "data");
            for (String fileName : Objects.requireNonNull(dir.list())) {
                RConfigFile file = new RConfigFile(plugin, "data" + File.separator + fileName);
                if (file.isLoaded()) files.add(file);
            }
        }
    }

    public void addFile(RConfigFile rConfigFile) {
        files.add(rConfigFile);
    }

    public void addAllFile(Collection<RConfigFile> rConfigFiles) {
        files.addAll(rConfigFiles);
    }

    @Override
    public @NotNull Set<String> getKeys(boolean deep) {
        Set<String> result = new HashSet<>();
        for (RConfigFile f : files) {
            result.addAll(f.getKeys(deep));
        }
        return result;
    }

    @Override
    public @NotNull Map<String, Object> getValues(boolean deep) {
        Map<String, Object> result = new HashMap<>();
        for (RConfigFile f : files) {
            result.putAll(f.getValues(deep));
        }
        return result;
    }

    @Override
    public boolean contains(@NotNull String path) {
        for (RConfigFile f : files) {
            if (f.contains(path)) return true;
        }
        return false;
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        for (RConfigFile f : files) {
            if (f.contains(path))
                return f.get(path);
        }
        return null;
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        Exception e = new Exception("Using method Set() is RConfigMultiFiles not supported");
        e.printStackTrace();
    }

    @Override
    public <T> T getObject(@NotNull String path, @NotNull Class<T> aClass) {
        for (RConfigFile f : files) {
            Object o = f.get(path);
            if (aClass.isInstance(o)) return aClass.cast(o);
        }
        return null;
    }

    @Override
    public String getString(String path, String defVal, String message) throws InvalidConfiguration {
        for (RConfigFile f : files) {
            Object o = f.get(path);
            if (o instanceof String) return (String) o;
        }
        return null;
    }

    @Override
    public Integer getInt(String path, Integer defVal, String message) throws InvalidConfiguration {
        for (RConfigFile f : files) {
            if (f.contains(path))
                return f.getInt(path);
        }
        if (defVal == null)
            throw new InvalidConfiguration((message != null) ? message : "This configSection does not contain the data block \"" + path + "\"");
        return defVal;
    }

    @Override
    public Boolean getBoolean(String path, Boolean defVal, String message) throws InvalidConfiguration {
        for (RConfigFile f : files) {
            if (f.contains(path))
                return f.getBoolean(path);
        }
        if (defVal == null)
            throw new InvalidConfiguration((message != null) ? message : "This configSection does not contain the data block \"" + path + "\"");
        return defVal;
    }

    @Override
    public Double getDouble(String path, Double defVal, String message) throws InvalidConfiguration {
        for (RConfigFile f : files) {
            if (f.contains(path))
                return f.getDouble(path);
        }
        if (defVal == null)
            throw new InvalidConfiguration((message != null) ? message : "This configSection does not contain the data block \"" + path + "\"");
        return defVal;
    }

    @Override
    public <T> List<T> getList(String path, Class<T> aClass, List<T> defVal, String message) throws InvalidConfiguration {
        List<T> result = new ArrayList<>();
        for (RConfigFile f : files) {
            if (f.contains(path)) {
                T o = f.getObject(path, aClass);
                if (o != null) result.add(o);
            }
        }
        if (result.isEmpty())
            if (defVal == null)
                throw new InvalidConfiguration((message != null) ? message : "This configSection does not contain the data block \"" + path + "\"");
            else
                return defVal;
        return result;
    }

    @Override
    public List<RConfig> getRConfigList(String path) throws InvalidConfiguration {
        List<RConfig> result = new ArrayList<>();
        for (RConfigFile f : files) {
            if (f.contains(path)) {
                List<RConfig> list = f.getRConfigList(path);
                if (list != null) result.addAll(list);
            }
        }
        return result;
    }
}
