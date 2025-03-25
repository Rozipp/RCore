package ua.rozipp.core.itemscomponents;

import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ua.rozipp.core.MessageHelper;
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
        register(AddPotionEffect.class);
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
        register(NoPlace.class);
        register(NoRightClick.class);
        register(NoVanillaDurability.class);
        register(RangedAttack.class);
        register(RemovePotionEffect.class);
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
    private CustomMaterial customMaterial;
    @Getter
    private boolean canselIgnore = false;
    @Getter
    private String message = null;
    @Getter
    private Priority priority = Priority.NORMAL;

    public ItemComponent() {
    }

    protected void load(RConfig rConfig) throws InvalidConfiguration {
    }

    protected void sendMessage(Audience audience) {
        if (message != null) MessageHelper.send(audience, message);
    }

    /**
     * Вызывается при создании предмета.
     * используйте:
     * - builder.addAttribute();
     * - builder.addLore();
     * - builder.addTag()
     */
    public void onBuildItemStack(ItemStackBuilder builder) {
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

    public ItemChangeResult onDurabilityDeath(PlayerDeathEvent event, ItemChangeResult result, ItemStack stack) {
        return result;
    }

    public void onAttack(EntityDamageByEntityEvent event, ItemStack inHand) {
    }

    public void onProjectileHit(ProjectileHitEvent event) {
    }

    public void onInventoryOpen(InventoryOpenEvent event, ItemStack stack) {
    }

    public void onHold(PlayerItemHeldEvent event) {
    }

    public void action(LivingEntity entity) {
    }

    public static class ItemComponentBuilder {
        private final CustomMaterial customMaterial;
        private RConfig rConfig;
        private String className;
        private String message;
        private boolean canselIgnore = false;
        private Priority priority = Priority.NORMAL;

        public ItemComponentBuilder(@NotNull CustomMaterial customMaterial) {
            this.customMaterial = customMaterial;
        }

        public ItemComponentBuilder load(RConfig rConfig) throws InvalidConfiguration {
            className = rConfig.getString("name", null, "Component's name not found");
            message = rConfig.getString("message", message);
            canselIgnore = rConfig.getBoolean("canselIgnore", canselIgnore);
            String priorityName = rConfig.getString("priority");
            if (priorityName != null)
                try {
                    priority = Priority.valueOf(priorityName);
                } catch (IllegalArgumentException e) {
                    throw new InvalidConfiguration(e.getMessage());
                }
            this.rConfig = rConfig;
            return this;
        }

        public ItemComponent build() throws InvalidConfiguration {
            ItemComponent itemComponent = null;
            if (rConfig != null) {
                String trigger = rConfig.getString("trigger");
                if (trigger != null) {
                    itemComponent = new CompositedComponent();
                } else
                    itemComponent = build(className);
            } else
                itemComponent = build(className);

            itemComponent.customMaterial = customMaterial;
            itemComponent.message = message;
            itemComponent.canselIgnore = canselIgnore;
            itemComponent.priority = priority;
            if (rConfig != null) itemComponent.load(rConfig);
            return itemComponent;
        }

        private ItemComponent build(String className) throws InvalidConfiguration {
            try {
                ItemComponent itemComponent;
                if (className.isEmpty())
                    throw new InvalidConfiguration("Component's name is empty");
                Class<? extends ItemComponent> cls = components.get(className);
                if (cls == null) throw new InvalidConfiguration("Component \"" + className + "\" no such class");
                itemComponent = cls.getConstructor().newInstance();
                return itemComponent;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new InvalidConfiguration(e.getMessage());
            }
        }

        public ItemComponentBuilder setClassName(String className) {
            this.className = className;
            return this;
        }

        public ItemComponentBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public ItemComponentBuilder setCanselIgnore(boolean canselIgnore) {
            this.canselIgnore = canselIgnore;
            return this;
        }

        public ItemComponentBuilder setPriority(Priority priority) {
            this.priority = priority;
            return this;
        }
    }

    public enum Priority {
        LOW,
        NORMAL,
        HIGH
    }
}
