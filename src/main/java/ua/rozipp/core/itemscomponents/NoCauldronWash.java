package ua.rozipp.core.itemscomponents;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class NoCauldronWash extends ItemComponent {

    public NoCauldronWash() {
        super();
    }

    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (!event.hasBlock()) {
                return;
            }

            Block block = event.getClickedBlock();

            if (block != null && block.getType().equals(Material.CAULDRON)) {
                event.getPlayer().updateInventory();
                event.setCancelled(true);
            }
        }
    }
}