package ua.rozipp.core;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.blockdata.BlockDataManager;
import ua.rozipp.core.command.rcoremenu.RCoreMenu;
import ua.rozipp.core.listener.CraftableCustomMaterialListener;
import ua.rozipp.core.listener.CustomBlockListener;
import ua.rozipp.core.listener.CustomItemListener;
import ua.rozipp.core.listener.armor.ArmorListener;

import java.text.SimpleDateFormat;

public class RCore extends JavaPlugin {
    private static Plugin instance;
    public static double minDamage = 0.2; //FIXME Сделать константой в файле civ.yml

    public static Plugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        PluginHelper.onEnable(this);

        String mainWorldName = this.getConfig().getString("main_world");
        PluginHelper.dateFormat = new SimpleDateFormat(this.getConfig().getString("simple_date_format", "M/dd/yy h:mm:ss a z"));

        if (mainWorldName != null)
            PluginHelper.setMainWorld(getServer().getWorld(mainWorldName));
        if (PluginHelper.getMainWorld() == null)
            try {
                PluginHelper.setMainWorld(getServer().getWorlds().get(0));
            } catch (Exception ignored) { //ONLY_FOR_TEST
            }

        LogHelper.setLevel(getConfig().getString("loger_level"));

        PluginHelper.blockDataManager = BlockDataManager.createPDC(this, true, true);

//        if (!PluginHelper.hasPlugin("HolographicDisplays"))
//            LogHelper.warning("I can not find find the Holodisply plugin in the plugins. I can not integrate us with holo.");

        // ------------------registerAllListener
        ListenerHelper.register(new CustomItemListener(), this);
        ListenerHelper.register(new CraftableCustomMaterialListener(), this);
        ListenerHelper.register(new CustomBlockListener(), this);
        ArmorListener.blockedMaterials = getConfig().getStringList("blocked");
        ListenerHelper.register(new ArmorListener(), this);

        // -------------------- commandRegister
        (new RCoreMenu()).register(this);

    }

    @Override
    public void onDisable() {
        PluginHelper.onDisable(this);
    }

//    public static WorldEditPlugin getWorldEdit() {
//        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
//
//        // WorldGuard may not be loaded
//        if (plugin == null || !(plugin instanceof WorldEditPlugin)) {
//            return null; // Maybe you want throw an exception instead
//        }
//
//        return (WorldEditPlugin) plugin;
//    }
}