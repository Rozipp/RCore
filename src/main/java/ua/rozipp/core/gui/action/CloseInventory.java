
package ua.rozipp.core.gui.action;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.gui.GuiHelper;
import ua.rozipp.core.gui.ItemButton;

public class CloseInventory extends ItemButton {
    /**
     * Create a new ItemButton with the given ItemStack as the icon
     *
     * @param item The ItemStack to be used as the icon
     */
    public CloseInventory(ItemStack item) {
        super(item);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        GuiHelper.closeInventory((Player) e.getWhoClicked());
    }
}

