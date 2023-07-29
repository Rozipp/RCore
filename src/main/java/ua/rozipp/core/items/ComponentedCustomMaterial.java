package ua.rozipp.core.items;

import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.MessageHelper;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.itemscomponents.ItemComponent;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentedCustomMaterial extends CustomMaterial {

    public Map<Class<? extends ItemComponent>, ItemComponent> components = new HashMap<>();

    protected ComponentedCustomMaterial(Key mid, Material baseMat, String name) {
        super(mid, baseMat, name);
    }

    private void buildComponents(List<RConfig> components) {
        if (components != null) {
            for (RConfig compInfo : components) {
                String name = (String) compInfo.get("name");
                Class<? extends ItemComponent> cls = ItemComponent.getItemComponentClass(name);
                if (cls != null)
                    try {
                        Class<?>[] partypes = {RConfig.class};
                        Object[] arglist = {compInfo};
                        this.components.put(cls, cls.getConstructor(partypes).newInstance(arglist));
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    public boolean hasComponent(Class<? extends ItemComponent> aClass) {
        return this.components.containsKey(aClass);
    }

    @SuppressWarnings("unchecked")
    public <T extends ItemComponent> T getComponent(Class<T> aClass) {
        return (T) this.components.get(aClass);
    }

    public Collection<ItemComponent> getComponents() {
        return this.components.values();
    }

    //---------------- events -------------
    @Override
    public void onInteract(PlayerInteractEvent event) {
        for (ItemComponent ic : getComponents()) {
            ic.onInteract(event);
        }
    }

    @Override
    public void onBlockPlaced(BlockPlaceEvent event) {
        for (ItemComponent ic : getComponents()) {
            if (ic.onBlockPlaced(event)) return;
        }
        event.setCancelled(true);
    }

    @Override
    public void onHeld(PlayerItemHeldEvent event) {
        for (ItemComponent comp : getComponents()) {
            comp.onHold(event);
        }
    }

    @Override
    public boolean onDropItem(Player player, ItemStack stack) {
        return false;
    }


    @Override
    public void onItemSpawn(ItemSpawnEvent event) {
        for (ItemComponent comp : getComponents()) {
            comp.onItemSpawn(event);
        }
    }

    @Override
    public void onAttack(EntityDamageByEntityEvent event, ItemStack stack) {
        for (ItemComponent comp : getComponents()) {
            comp.onAttack(event, stack);
        }
    }

    @Override
    public boolean onInvItemDrop(Cancellable event, Player player, Inventory toInv, ItemStack stack) {
        if (!this.canUseInventoryTypes(toInv.getType())) {
            MessageHelper.sendError(player, "Нельзя использовать этот предмет в инвентаре " + toInv.getType());
            return true;
        }
        return false;
    }

    @Override
    public boolean onInvItemPickup(Cancellable event, Player player, Inventory fromInv, ItemStack stack) {
        return false;
    }

    @Override
    public void applyAttributes(ItemStackBuilder builder) {
        for (ItemComponent comp : getComponents()) {
            comp.onPrepareCreate(builder);
        }
    }

    @Override
    public void onDefense(EntityDamageByEntityEvent event, ItemStack stack) {
        for (ItemComponent comp : getComponents()) {
            comp.onDefense(event, stack);
        }
    }

    @Override
    public void onDurabilityChange(PlayerItemDamageEvent event) {
        for (ItemComponent comp : getComponents()) {
            comp.onDurabilityChange(event);
        }
    }

    @Override
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        for (ItemComponent comp : getComponents()) {
            comp.onPlayerInteractEntity(event);
        }
    }

    @Override
    public void onRangedAttack(EntityDamageByEntityEvent event, ItemStack inHand) {
        for (ItemComponent comp : getComponents()) {
            comp.onRangedAttack(event, inHand);
        }
    }
    @Override
    public ItemChangeResult onDurabilityDeath(PlayerDeathEvent event, ItemStack stack) {
        ItemChangeResult result = null;
        for (ItemComponent comp : getComponents()) {
            result = comp.onDurabilityDeath(event, result, stack);
        }
        return result;
    }

    @Override
    public void onInventoryOpen(InventoryOpenEvent event, ItemStack stack) {
        for (ItemComponent comp : getComponents()) {
            comp.onInventoryOpen(event, stack);
        }
    }

    @Override
    public boolean canUseInventoryTypes(InventoryType invType) {
        if (invType == null) return false;
        switch (invType) {
            case CHEST:
            case CRAFTING:
            case DROPPER:
            case ENDER_CHEST:
            case HOPPER:
            case PLAYER:
            case SHULKER_BOX:
            case WORKBENCH:
                return true;
            default:
                return false;
        }
    }

    public void addComponents(List<RConfig> components) {
        buildComponents(components);
    }
}
