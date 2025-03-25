package ua.rozipp.core.itemscomponents;

import org.bukkit.event.player.PlayerInteractEvent;

public class NoPlace extends ItemComponent{

    @Override
    public void onInteract(PlayerInteractEvent event) {
        super.onInteract(event);
        this.sendMessage(event.getPlayer());
        event.setCancelled(true);
        event.getPlayer().updateInventory();
    }
}
