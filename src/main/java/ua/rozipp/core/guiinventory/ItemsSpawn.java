package ua.rozipp.core.guiinventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.items.CustomMaterial;
import ua.rozipp.core.threading.tasks.DelayMoveInventoryItem;
import ua.rozipp.core.config.ConfigMaterialCategory;
import ua.rozipp.core.exception.GuiException;
import ua.rozipp.core.gui.GuiAction;
import ua.rozipp.core.gui.GuiHelper;
import ua.rozipp.core.gui.GuiInventory;
import ua.rozipp.core.gui.GuiItemBuilder;

public class ItemsSpawn extends GuiInventory {

    public ItemsSpawn(Player player, String arg) throws GuiException {
        super(player, null, arg);
        if (arg == null)
            createPerent();
        else
            createCategory();
    }

    private void createPerent() {
        this.setTitle(Component.translatable("adcmd_itemsHeader"));
        /* Build the Category Inventory. */
        for (ConfigMaterialCategory cat : ConfigMaterialCategory.getCategories()) {
            Material identifier = cat.title.contains("Fish") ? Material.TROPICAL_FISH //
                    : cat.title.contains("Gear") ? Material.IRON_SWORD //
                    : cat.title.contains("Materials") ? Material.BIRCH_WOOD //
                    : cat.title.contains("Tools") ? Material.IRON_SHOVEL : //
                    Material.WRITTEN_BOOK;
            this.addGuiItem(0, GuiItemBuilder.guiItemBuilder(identifier)
                    .setOpenInventory(ItemsSpawn.class, cat.id)
                    .setName(cat.title)
                    .addLore(Component.text(cat.materials.size() + " Items", NamedTextColor.BLUE))
                    .addLore(Component.text("<Click To Open>", NamedTextColor.GOLD))//
                    .build());
        }
    }

    private void createCategory() {
        /* Build a new GUI Inventory. */
        ConfigMaterialCategory cat = ConfigMaterialCategory.getCategory(getArg());
        this.setTitle(Component.text("Spawn items from category " + cat.title));//
        for (CustomMaterial cmat : cat.materials.values()) {
            if (cmat == null) continue;
            this.addGuiItem(GuiItemBuilder.guiItemBuilder(cmat.spawn(64))
					.setAction(GuiHelper.SPAWN_ITEM).build());
        }
    }

    @Override
    public boolean onItemToInventory(Cancellable event, Player player, Inventory inv, ItemStack stack) {
        stack.setAmount(0);
        player.updateInventory();
        return false;
    }

    @Override
    public boolean onItemFromInventory(Cancellable event, Player player, Inventory inv, ItemStack stack) {
        if (!GuiHelper.isGUIItem(stack)) {
            DelayMoveInventoryItem.beginTaskRespawn(event, player, inv, stack, inv.first(stack));
            return false;
        } else {
            GuiAction action = GuiHelper.getAction(stack);
            if (action != null) {
//                if (action instanceof SpawnItem) {
//                    DelayMoveInventoryItem.beginTaskRespawn(event, player, inv, stack, inv.first(stack));
//                    CustomMaterial cMat = CustomMaterials.getCustomMaterial(stack);
//                    stack.setItemMeta(CustomMaterials.spawn(cMat).getItemMeta());
//                    return false;
//                }
                action.performAction(player, stack);
                return true;
            }
        }
        return true;
    }
}
