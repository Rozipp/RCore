package ua.rozipp.core.guiinventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import ua.rozipp.core.config.ConfigMaterialCategory;
import ua.rozipp.core.exception.GuiException;
import ua.rozipp.core.gui.InventoryGUI;
import ua.rozipp.core.gui.action.OpenInventoryButton;
import ua.rozipp.core.gui.action.SpawnItem;
import ua.rozipp.core.items.CustomMaterial;

public class ItemsSpawn extends InventoryGUI {

    public ItemsSpawn(Plugin plugin, ConfigMaterialCategory category) throws GuiException {
        super(plugin, Bukkit.createInventory(null, 6 * 9, (category == null) ?
                Component.translatable("adcmd_itemsHeader") :
                Component.text("Spawn items from category " + category.title))
        );
        if (category == null) {
            for (ConfigMaterialCategory cat : ConfigMaterialCategory.getCategories()) {
                Material identifier = cat.title.contains("Fish") ? Material.TROPICAL_FISH //
                        : cat.title.contains("Gear") ? Material.IRON_SWORD //
                        : cat.title.contains("Materials") ? Material.BIRCH_WOOD //
                        : cat.title.contains("Tools") ? Material.IRON_SHOVEL //
                        : cat.title.contains("TNT") ? Material.TNT //
                        : Material.WRITTEN_BOOK;

                this.addButton(new OpenInventoryButton(identifier,
                        new ItemsSpawn(plugin, cat),
                        cat.title,
                        Component.text(cat.materials.size() + " Items", NamedTextColor.BLUE)
                ));
            }
        } else {
            for (CustomMaterial cmat : category.materials.values()) {
                if (cmat == null) continue;
                this.addButton(new SpawnItem(cmat.spawn(1)));
            }
        }
    }

}
