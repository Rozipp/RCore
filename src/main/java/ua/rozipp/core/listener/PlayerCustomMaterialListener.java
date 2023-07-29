package ua.rozipp.core.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.items.CustomMaterial;

import java.util.ArrayList;

public class PlayerCustomMaterialListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerPickup(EntityPickupItemEvent event) {
        if (event.isCancelled()) return;
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Component name;
            ItemStack item = event.getItem().getItemStack();
            if (item.getItemMeta().hasDisplayName()) {
                name = item.getItemMeta().displayName();
            } else {
                name = Component.text(item.getType().name().replace("_", " ").toLowerCase());
            }
            if (name == null) name = Component.space();

            player.sendMessage(Component.translatable("var_customItem_Pickup", NamedTextColor.GREEN)
                    .args(Component.text(event.getItem().getItemStack().getAmount()).color(NamedTextColor.LIGHT_PURPLE),
                            name));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        boolean keepInventory = Boolean.parseBoolean(event.getEntity().getWorld().getGameRuleValue("keepInventory"));
        if (!keepInventory) {
            ArrayList<ItemStack> stacksToRemove = new ArrayList<>();
            for (ItemStack stack : event.getDrops()) {
                if (stack != null) {
                    final CustomMaterial material = CustomMaterial.getCustomMaterial(stack);
                    if (material == null) continue;
                    material.onPlayerDeath(event, stack);
//                    if (material instanceof UnitMaterial) {
//                        stacksToRemove.add(stack);
//                    } else {
//                        if (material instanceof UnitCustomMaterial) stacksToRemove.add(stack);
//                    }
                }
            }

            for (ItemStack stack : stacksToRemove) {
                event.getDrops().remove(stack);
            }
        }

    }
}
