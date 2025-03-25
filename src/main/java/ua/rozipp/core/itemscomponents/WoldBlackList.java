package ua.rozipp.core.itemscomponents;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;

import java.util.ArrayList;
import java.util.List;

public class WoldBlackList extends ItemComponent {

    private final List<String> worlds = new ArrayList<>();

    @Override
    protected void load(RConfig rConfig) throws InvalidConfiguration {
        super.load(rConfig);
        String worldName = rConfig.getString("world");
        if (worldName != null) worlds.add(worldName);
        if (rConfig.contains("worlds"))
            for (String name : (List<String>) rConfig.getList("worlds"))
                if (name != null) worlds.add(name);
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        super.onInteract(event);
        if (worlds.contains(event.getPlayer().getWorld().getName()))
            event.setCancelled(true);
    }

    @Override
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        super.onPlayerInteractEntity(event);
        if (worlds.contains(event.getPlayer().getWorld().getName()))
            event.setCancelled(true);
    }

    @Override
    public void onAttack(EntityDamageByEntityEvent event, ItemStack inHand) {
        super.onAttack(event, inHand);
        if (worlds.contains(event.getEntity().getWorld().getName()))
            event.setCancelled(true);
    }

    @Override
    public void onProjectileHit(ProjectileHitEvent event) {
        super.onProjectileHit(event);
        if (worlds.contains(event.getEntity().getWorld().getName()))
            event.setCancelled(true);
    }

    @Override
    public void onInventoryOpen(InventoryOpenEvent event, ItemStack stack) {
        super.onInventoryOpen(event, stack);
        if (worlds.contains(event.getPlayer().getWorld().getName()))
            event.setCancelled(true);
    }

    @Override
    public void onDefense(EntityDamageByEntityEvent event, ItemStack stack) {
        super.onDefense(event, stack);
        if (worlds.contains(event.getEntity().getWorld().getName()))
            event.setCancelled(true);
    }

    @Override
    public void onHold(PlayerItemHeldEvent event) {
        super.onHold(event);
        if (worlds.contains(event.getPlayer().getWorld().getName()))
            event.setCancelled(true);
    }

    
}
