package ua.rozipp.core.gui.action;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.gui.GuiHelper;
import ua.rozipp.core.gui.ItemButton;

import java.util.ArrayDeque;

public class OpenBackInventory extends ItemButton {

	/**
	 * Create a new ItemButton with the given ItemStack as the icon
	 *
	 * @param item The ItemStack to be used as the icon
	 */
	public OpenBackInventory(ItemStack item) {
		super(item);
	}

	@Override
	public void onClick(InventoryClickEvent e) {
//		ArrayDeque<InventoryGUI> gis = GuiHelper.getInventoryStack(player.getUniqueId());
//		gis.pop();
//
//		if (!gis.isEmpty()) {
//			player.openInventory(gis.getFirst().getInventory());
//			GuiHelper.setInventoryStack(player.getUniqueId(), gis);
//		} else
//			player.closeInventory();
	}
}
