package ua.rozipp.core.itemscomponents;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;

public class NoRightClick extends ItemComponent {

    public NoRightClick(RConfig compInfo) throws InvalidConfiguration {
        super(compInfo);
    }

    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            event.getPlayer().updateInventory();
            event.setCancelled(true);
        } else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getClickedBlock() != null && event.getClickedBlock().getType() != Material.CHEST) {
                event.getPlayer().updateInventory();
                event.setCancelled(true);
            }
        }
    }

    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        event.setCancelled(true);
    }

}
