package ua.rozipp.core.itemscomponents;

import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class NoDurability extends ItemComponent {

	public NoDurability() {
		super();
	}

	@Override
	public void onInventoryOpen(InventoryOpenEvent event, ItemStack stack) {
		stack.setDurability((short) 0);		
	}

}
