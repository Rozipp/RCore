package ua.rozipp.core;

import lombok.Getter;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ua.rozipp.core.blocks.CustomBlockType;
import ua.rozipp.core.blockscomponents.BlockHologram;
import ua.rozipp.core.command.rcoremenu.RCoreMenu;
import ua.rozipp.core.config.RConfigMultiFiles;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.CustomMaterial;
import ua.rozipp.core.items.CustomMaterialBuilder;
import ua.rozipp.core.itemscomponents.BlockPlace;
import ua.rozipp.core.listener.CraftableCustomMaterialListener;
import ua.rozipp.core.listener.CustomItemListener;
import ua.rozipp.core.listener.armor.ArmorListener;
import ua.rozipp.core.recipes.CustomRecipe;

import java.text.SimpleDateFormat;
import java.util.List;

public class RCore extends JavaPlugin {
    private static Plugin instance;
    public static double minDamage = 0.2; //FIXME Сделать константой в файле civ.yml
    @Getter
    public static RConfigMultiFiles configManager;

    public static Plugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        LogHelper.setLevel(getConfig().getString("loger_level"));
        configManager = PluginHelper.getRConfigMultiFiles(this);
        configManager.loadFiles();

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


        PluginHelper.getCustomBlockRegistry();

        if (!PluginHelper.hasPlugin("HolographicDisplays"))
            LogHelper.warning("I can not find find the Holodisply plugin in the plugins. I can not integrate us with holo.");

        try {
            CustomMaterialBuilder.loadAll(configManager);
        } catch (InvalidConfiguration e) {
            LogHelper.error(e.getMessage());
        }

        try {
            CustomRecipe.loadAll(this, configManager);
        } catch (InvalidConfiguration e) {
            LogHelper.error(e.getMessage());
        }

        Key key = new NamespacedKey(this, "m_crafter");
        CustomMaterial.builder(key, Material.CRAFTING_TABLE, "Персональный крафтер2")
                .category("Tools")
                .lore(List.of("Крафтер тестовый", "Line2"))
                .addComponent(new ua.rozipp.core.itemscomponents.OpenGui(this, "PersonalCrafter"))
                .addComponent(new BlockPlace(CustomBlockType.builder(key)
                        .blockMaterial(Material.CRAFTING_TABLE)
                        .addComponent(new BlockHologram("Крафтер2", ""))
                        .addComponent(new ua.rozipp.core.blockscomponents.OpenGui(this, "PersonalCrafter"))
                        .build()))
                .build();

        // ------------------registerAllListener
        ListenerHelper.register(new CustomItemListener(), this);
        ListenerHelper.register(new CraftableCustomMaterialListener(), this);
        ArmorListener.blockedMaterials = getConfig().getStringList("blocked");
        ListenerHelper.register(new ArmorListener(), this);


        // -------------------- commandRegister
        (new RCoreMenu()).register(this);

    }

    @Override
    public void onDisable() {
        PluginHelper.onDisable(this);
        PluginHelper.getBlockDataManager().saveAndClose();
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