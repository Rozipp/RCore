
package ua.rozipp.core.gui.action;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.gui.GuiAction;
import ua.rozipp.core.gui.GuiInventory;

public class CloseInventory extends GuiAction {
    @Override
    public void performAction(Player player, ItemStack stack) {
        GuiInventory.closeInventory(player);
    }
}

