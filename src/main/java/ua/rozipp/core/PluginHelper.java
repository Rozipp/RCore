package ua.rozipp.core;

import lombok.Getter;
import lombok.Setter;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import ru.easydonate.easypayments.database.Database;
import ua.rozipp.core.blockdata.BlockDataManager;
import ua.rozipp.core.blocks.CustomBlockRegistry;
import ua.rozipp.core.command.CommanderRegistration;
import ua.rozipp.core.config.ConfigHelper;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.config.RConfigMultiFiles;
import ua.rozipp.core.exception.DatabaseException;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.CustomMaterial;
import ua.rozipp.core.recipes.CustomRecipe;

import java.io.IOException;
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
    private static final Map<Class<? extends Plugin>, Database> databaseMap = new HashMap<>();
    private static final Map<Class<? extends Plugin>, RConfigMultiFiles> rConfigMultiFilesMap = new HashMap<>();
    private static BlockDataManager blockDataManagers;
    private static CustomBlockRegistry customBlockRegistry;
    public static SimpleDateFormat dateFormat;

    public static void onEnable(Plugin plugin) {
        plugins.put(plugin.getClass(), plugin);
        LocaleHelper.initServerLocale(plugin);
        try {
            LocaleHelper.loadLocaleFile(plugin);
        } catch (InvalidConfiguration e) {
            LogHelper.error(e.getComponent());
        }
        LogHelper.fine("[PluginHelper]: Plugin added \"" + plugin.getName() + "\"");
    }

    public static void onDisable(Plugin plugin) {
        if (customBlockRegistry != null) customBlockRegistry.unregisterAll(plugin);
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

    public static HolographicDisplaysAPI getHolographicDisplaysAPI(Plugin plugin) {
        if (plugin != null) return HolographicDisplaysAPI.get(plugin);
        return HolographicDisplaysAPI.get(RCore.getInstance());
    }

    public static CustomBlockRegistry getCustomBlockRegistry() {
        if (customBlockRegistry == null) customBlockRegistry = new CustomBlockRegistry(getBlockDataManager(), RCore.getInstance());
        return customBlockRegistry;
    }

    public static BlockDataManager getBlockDataManager() {
        if (blockDataManagers == null)
            blockDataManagers = BlockDataManager.createPDC(RCore.getInstance(), true, true);
        return blockDataManagers;
    }

    //-------------- Database
    public static Database createDatabase(Plugin plugin, RConfig config) throws SQLException, InvalidConfiguration, DatabaseException {
        Database database = new Database(plugin, config);
        databaseMap.put(plugin.getClass(), database);
        return database;
    }

    public static Database getDatabase() {
        return getDatabase(RCore.getInstance());
    }

    public static Database getDatabase(Plugin plugin) {
        if (databaseMap.containsKey(plugin.getClass())) return databaseMap.get(RCore.class);
        try {
            return createDatabase(plugin, ConfigHelper.getRConfigFile(plugin, "config.yml"));
        } catch (SQLException | InvalidConfiguration | DatabaseException | IOException e) {
            LogHelper.error(e.getMessage());
            return null;
        }
    }

    //------------  RConfig  --
    public static RConfigMultiFiles getRConfigMultiFiles(Plugin plugin) {
        if (rConfigMultiFilesMap.containsKey(plugin.getClass())) return rConfigMultiFilesMap.get(plugin.getClass());
        RConfigMultiFiles rConfigMultiFiles = new RConfigMultiFiles(plugin);
        rConfigMultiFilesMap.put(plugin.getClass(), rConfigMultiFiles);
        return rConfigMultiFiles;
    }

}
