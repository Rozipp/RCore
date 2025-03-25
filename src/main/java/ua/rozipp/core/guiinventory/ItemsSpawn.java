package ua.rozipp.core.guiinventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.config.ConfigMaterialCategory;
import ua.rozipp.core.gui.RGuiOpenable;
import ua.rozipp.core.gui.buttons.OpenInventoryRButton;
import ua.rozipp.core.gui.buttons.SpawnRButton;
import ua.rozipp.core.items.CustomMaterial;
import ua.rozipp.core.items.ItemStackBuilder;

public class ItemsSpawn extends RGuiOpenable {

    private final ConfigMaterialCategory category;

    public ItemsSpawn(Object arg) {
        super(6, (arg instanceof ConfigMaterialCategory) ?
                Component.text("Spawn items from category " + ((ConfigMaterialCategory) arg).title) :
                Component.translatable("adcmd_itemsHeader"));
        category = (arg instanceof ConfigMaterialCategory) ? (ConfigMaterialCategory) arg : null;
    }

    @Override
    public void onFirstDraw() {
        if (category == null) {
            openAllSlots();
            for (ConfigMaterialCategory cat : ConfigMaterialCategory.getCategories()) {
                Material identifier = cat.title.contains("Fish") ? Material.TROPICAL_FISH //
                        : cat.title.contains("Gear") ? Material.IRON_SWORD //
                        : cat.title.contains("Materials") ? Material.BIRCH_WOOD //
                        : cat.title.contains("Tools") ? Material.IRON_SHOVEL //
                        : cat.title.contains("TNT") ? Material.TNT //
                        : Material.WRITTEN_BOOK;

                this.addSlot(new OpenInventoryRButton(ItemStackBuilder
                                .of(identifier)
                                .setName(cat.title)
                                .addLore(Component.text(cat.materials.size() + " Items", NamedTextColor.BLUE))
                        ).setRGuiFromClass(ItemsSpawn.class, cat)
                );
            }
        } else {
            openAllSlots();
            for (CustomMaterial cmat : category.materials.values()) {
                if (cmat == null) continue;
                this.addSlot(new SpawnRButton(cmat.spawn(1)));
            }
        }
    }

    @Override
    public boolean onPickupItem(InventoryInteractEvent event, Integer slot, ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean onPlaceItem(InventoryInteractEvent event, Integer slot, ItemStack itemStack) {
        itemStack.setAmount(0);
        if (event.getWhoClicked() instanceof Player) ((Player) event.getWhoClicked()).updateInventory();
        return false;
    }
}
