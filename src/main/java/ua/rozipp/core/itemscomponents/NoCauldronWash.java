package ua.rozipp.core.itemscomponents;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.object.BlockCoord;

public class NoCauldronWash extends ItemComponent {

	public NoCauldronWash(RConfig compInfo) throws InvalidConfiguration {
		super(compInfo);
	}

	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (!event.hasBlock()) {
				return;
			}
						
			BlockCoord bcoord = new BlockCoord(event.getClickedBlock());
						
			if (bcoord.getBlock().getType().equals(Material.CAULDRON)) {
				event.getPlayer().updateInventory();
				event.setCancelled(true);
			}
		}
	}
}