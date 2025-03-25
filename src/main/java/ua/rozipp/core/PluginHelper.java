package ua.rozipp.core;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import ua.rozipp.core.blockdata.BlockDataManager;
import ua.rozipp.core.blocks.CustomBlockRegistry;
import ua.rozipp.core.command.CommanderRegistration;
import ua.rozipp.core.config.RConfigMultiFiles;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.gui.GuiManager;
import ua.rozipp.core.items.CustomMaterial;
import ua.rozipp.core.recipes.CustomRecipe;
import ua.rozipp.core.scheduler.Scheduler;
import ua.rozipp.core.scheduler.Schedulers;
import ua.rozipp.core.scheduler.builder.TaskBuilder;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PluginHelper {

//    @Getter
//    @Setter
//    private static World mainWorld;
    @Getter
    private static RCore rCore;
    @Getter
    private static final Random random = new Random();
    private static final Map<Class<? extends Plugin>, Plugin> plugins = new HashMap<>();
    private static final Map<Class<? extends Plugin>, RConfigMultiFiles> rConfigMultiFilesMap = new HashMap<>();
    private static final Map<Class<? extends Plugin>, Schedulers> schedulersMap = new HashMap<>();
    private static GuiManager guiManager;
    private static BlockDataManager blockDataManagers;
    private static CustomBlockRegistry customBlockRegistry;
    public static SimpleDateFormat dateFormat;

    public static void onEnable(Plugin plugin) {
        if (plugin instanceof RCore) {
            rCore = (RCore) plugin;
        }
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
        if (schedulersMap.containsKey(plugin.getClass())) getSchedulers(plugin).shutdown();
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

    public static CustomBlockRegistry getCustomBlockRegistry() {
        if (customBlockRegistry == null)
            customBlockRegistry = new CustomBlockRegistry(getBlockDataManager(), RCore.getInstance());
        return customBlockRegistry;
    }

    public static BlockDataManager getBlockDataManager() {
        if (blockDataManagers == null)
            blockDataManagers = BlockDataManager.createPDC(RCore.getInstance(), true);
        return blockDataManagers;
    }

//    //-------------- Database
//    public static DatabaseCredentials createDatabase(Plugin plugin, RConfig config) throws SQLException, InvalidConfiguration, DatabaseException {
//        Database database = new Database(plugin, config);
//        databaseMap.put(plugin.getClass(), database);
//        return database;
//    }
//
//    public static Database getDatabase() {
//        return getDatabase(RCore.getInstance());
//    }
//
//    public static Database getDatabase(Plugin plugin) {
//        if (databaseMap.containsKey(plugin.getClass())) return databaseMap.get(RCore.class);
//        try {
//            return createDatabase(plugin, ConfigHelper.getRConfigFile(plugin, "config.yml"));
//        } catch (SQLException | InvalidConfiguration | DatabaseException | IOException e) {
//            LogHelper.error(e.getMessage());
//            return null;
//        }
//    }

    //------------  RConfig  --
    public static RConfigMultiFiles getRConfigMultiFiles(Plugin plugin) {
        if (rConfigMultiFilesMap.containsKey(plugin.getClass())) return rConfigMultiFilesMap.get(plugin.getClass());
        RConfigMultiFiles rConfigMultiFiles = new RConfigMultiFiles(plugin);
        rConfigMultiFilesMap.put(plugin.getClass(), rConfigMultiFiles);
        return rConfigMultiFiles;
    }

    //------------GuiManager
    public static GuiManager getGuiManager() {
        if (guiManager == null) {
            guiManager = new GuiManager(RCore.getInstance());
        }
        return guiManager;
    }

    //--------------- Scheduler and Executors

    public static Schedulers getSchedulers() {
        return getSchedulers(RCore.getInstance());
    }

    public static Schedulers getSchedulers(Plugin plugin) {
        if (!schedulersMap.containsKey(plugin.getClass()))
            schedulersMap.put(plugin.getClass(), new Schedulers(plugin));
        return schedulersMap.get(plugin.getClass());
    }

    /**
     * Returns a "sync" scheduler, which executes tasks on the main server thread.
     *
     * @return a sync executor instance
     */
    public static Scheduler sync() {
        return getSchedulers().sync();
    }

    /**
     * Returns a "sync" scheduler, which executes tasks on the main server thread.
     *
     * @param plugin - parent plugin
     * @return a sync executor instance
     */
    public static Scheduler sync(Plugin plugin) {
        return getSchedulers(plugin).sync();
    }

    /**
     * Returns an "async" scheduler, which executes tasks asynchronously.
     *
     * @return an async executor instance
     */
    public static Scheduler async() {
        return getSchedulers().async();
    }

    /**
     * Returns an "async" scheduler, which executes tasks asynchronously.
     *
     * @param plugin - parent plugin
     * @return an async executor instance
     */
    public static Scheduler async(Plugin plugin) {
        return getSchedulers(plugin).async();
    }

    /**
     * Gets Bukkit's scheduler.
     *
     * @return bukkit's scheduler
     */
    public static BukkitScheduler bukkit() {
        return Bukkit.getServer().getScheduler();
    }

    /**
     * Gets a {@link TaskBuilder} instance
     *
     * @return a task builder
     */
    public static TaskBuilder builder() {
        return Schedulers.builder();
    }

}
