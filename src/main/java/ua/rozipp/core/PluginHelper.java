package ua.rozipp.core;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import redempt.redlib.RedLib;
import redempt.redlib.blockdata.BlockDataManager;
import ru.easydonate.easypayments.database.Database;
import ua.rozipp.core.blocks.CustomBlock;
import ua.rozipp.core.command.CommanderRegistration;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.config.RConfigFile;
import ua.rozipp.core.exception.DatabaseException;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.CustomMaterial;
import ua.rozipp.core.recipes.CustomRecipe;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PluginHelper {

    @Getter
    @Setter
    private static World mainWorld;
    @Getter
    private static final Random random = new Random();
    private static final Map<Class<? extends Plugin>, Plugin> plugins = new HashMap<>();
    static BlockDataManager blockDataManager;
    public static SimpleDateFormat dateFormat;
    private static final Map<Class<? extends Plugin>, Database> databaseMap = new HashMap<>();

    public static void onEnable(Plugin plugin) {
        plugins.put(plugin.getClass(), plugin);

        LocaleHelper.registerPluginsLocaleFiles(plugin);

        LogHelper.fine("PluginHelper: Added Plugin " + plugin.getName());
    }

    public static void onDisable(Plugin plugin) {
        CustomBlock.removeAll(plugin);
        CustomRecipe.removeAll(plugin);
        CustomMaterial.removeAll(plugin);
        CommanderRegistration.unregisterAllCommands(plugin);
        plugins.remove(plugin.getClass());
    }

    public static boolean hasPlugin(String name) {
        return getPlugin(name) != null;
    }

    public static Plugin getPlugin(String name) {
        return Bukkit.getPluginManager().getPlugin(name);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Plugin> T getPlugin(Class<T> aClass) {
        return (T) plugins.get(aClass);
    }

    public static Collection<Plugin> getRegistryPlugins() {
        return plugins.values();
    }

    public static Server getServer() {
        return Bukkit.getServer();
    }

    /**
     * Gets the plugin that called the calling method of this method
     *
     * @return The plugin which called the method
     */
    public static Plugin getCallingPlugin() {
        return RedLib.getCallingPlugin();
    }

    public static BlockDataManager getBlockDataManager() {
        return blockDataManager;
    }

    public static Database getDatabase() {
        return getDatabase(getPlugin(RCore.class));
    }

    public static Database createDatabase(Plugin plugin, RConfig config) throws SQLException, InvalidConfiguration, DatabaseException {
        Database database = new Database(plugin, config);
        databaseMap.put(plugin.getClass(), database);
        return database;
    }

    public static Database getDatabase(Plugin plugin) {
        if (databaseMap.containsKey(plugin.getClass())) return databaseMap.get(RCore.class);
        try {
            return createDatabase(plugin, new RConfigFile(plugin, "config.yml"));
        } catch (SQLException | InvalidConfiguration | DatabaseException e) {
            LogHelper.error(e.getMessage());
            return null;
        }
    }
}
