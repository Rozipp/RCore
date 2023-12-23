package ua.rozipp.core.itemscomponents;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.CustomMaterial;
import ua.rozipp.core.items.ItemChangeResult;
import ua.rozipp.core.items.ItemStackBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class ItemComponent {

    private final static Map<String, Class<? extends ItemComponent>> components = new HashMap<>();

    static {
        register(Attack.class);
        register(BlockPlace.class);
        register(ChoiceUnitComponent.class);
        register(Cooldown.class);
        register(Defense.class);
        register(DurabilityOnDeath.class);
        register(Health.class);
        register(MaxHealth.class);
        register(MoveSpeed.class);
        register(NBT.class);
        register(NoCauldronWash.class);
        register(NoDurability.class);
        register(NoRightClick.class);
        register(NoVanillaDurability.class);
        register(RangedAttack.class);
        register(RepairCost.class);
        register(Soulbound.class);
        register(Tagged.class);
        register(OpenGui.class);
    }

    public static void register(Class<? extends ItemComponent> aClass) {
        components.put(aClass.getSimpleName(), aClass);
    }

    public static ItemComponentBuilder builder(CustomMaterial customMaterial) {
        return new ItemComponentBuilder(customMaterial);
    }

    @Getter
    @Setter
    private CustomMaterial customMaterial;

    public ItemComponent() {
    }

    protected void load(RConfig rConfig) throws InvalidConfiguration {
    }

    /**
     * Вызывается при создании предмета.
     * используйте:
     * - builder.addAttribute();
     * - builder.addLore();
     * - builder.addTag()
     */
    public void onSpawnItem(ItemStackBuilder builder) {
    }

    // -------- Events
    public void onDurabilityChange(PlayerItemDamageEvent event) {
    }

    public void onDefense(EntityDamageByEntityEvent event, ItemStack stack) {
    }

    public void onInteract(PlayerInteractEvent event) {
    }

    public void onItemSpawn(ItemSpawnEvent event) {
    }

    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
    }

    public void onRangedAttack(EntityDamageByEntityEvent event, ItemStack inHand) {
    }

    public ItemChangeResult onDurabilityDeath(PlayerDeathEvent event, ItemChangeResult result, ItemStack stack) {
        return result;
    }

    public void onAttack(EntityDamageByEntityEvent event, ItemStack inHand) {
    }

    public void onInventoryOpen(InventoryOpenEvent event, ItemStack stack) {
    }

    public void onHold(PlayerItemHeldEvent event) {
    }

    public static class ItemComponentBuilder {
        private final CustomMaterial customMaterial;

        public ItemComponentBuilder(@NotNull CustomMaterial customMaterial) {
            this.customMaterial = customMaterial;
        }

        public ItemComponent build(RConfig rConfig) throws InvalidConfiguration {
            try {
                String className = rConfig.getString("name", null, "[Mid = " + customMaterial.getMid() + "] component's name not found");
                if (className.isEmpty())
                    throw new InvalidConfiguration("[Mid = " + customMaterial.getMid() + "] component's name is empty");
                Class<? extends ItemComponent> cls = components.get(className);
                ItemComponent itemComponent = cls.getConstructor().newInstance();
                itemComponent.customMaterial = customMaterial;
                itemComponent.load(rConfig);
                return itemComponent;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new InvalidConfiguration(e.getMessage());
            }
        }
    }
}
