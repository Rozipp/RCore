package ua.rozipp.core.itemscomponents;

import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;

public class NoDurability extends ItemComponent {

	public NoDurability(RConfig compInfo) throws InvalidConfiguration {
		super(compInfo);
	}

	@Override
	public void onInventoryOpen(InventoryOpenEvent event, ItemStack stack) {
		stack.setDurability((short) 0);		
	}

}
