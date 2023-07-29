package ua.rozipp.core.gui.action;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.gui.GuiAction;
import ua.rozipp.core.gui.GuiInventory;

import java.util.ArrayDeque;

public class OpenBackInventory extends GuiAction {

	@Override
	public void performAction(Player player, ItemStack stack) {
		ArrayDeque<GuiInventory> gis = GuiInventory.getInventoryStack(player.getUniqueId());
		gis.pop();
		
		if (!gis.isEmpty()) {
			player.openInventory(gis.getFirst().getInventory());
			GuiInventory.setInventoryStack(player.getUniqueId(), gis);
		} else
			player.closeInventory();
	}

}
