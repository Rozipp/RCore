package ua.rozipp.core.itemscomponents;

import lombok.Setter;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.Action;
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
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.ItemChangeResult;
import ua.rozipp.core.items.ItemStackBuilder;
import ua.rozipp.core.itemscomponents.targets.Target;
import ua.rozipp.core.itemscomponents.triggers.Trigger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CompositedComponent extends ItemComponent {

    @Setter
    private Trigger trigger = null;
    @Setter
    private Target target = null;

    private double chance = 1.0;
    private final List<Particle> particles = new ArrayList<>();

    private final List<ItemComponent> itemComponents = new ArrayList<>();

    @Override
    protected void load(RConfig rConfig) throws InvalidConfiguration {
        super.load(rConfig);

        target = Target.createTarget(rConfig);
        String triggerName = rConfig.getString("trigger");
        if (triggerName == null) throw new InvalidConfiguration("Trigger not found");
        try {
            trigger = Trigger.valueOf(triggerName);
        } catch (IllegalArgumentException e) {
            throw new InvalidConfiguration("Trigger \"" + triggerName + "\" not found");
        }

        chance = rConfig.getDouble("chance", chance);
        if (rConfig.contains("particles"))
            for (String s : (List<String>) rConfig.getList("particles", new ArrayList<>())) {
                try {
                    particles.add(Particle.valueOf(s));
                } catch (IllegalArgumentException ignored) {
                }
            }

        @NotNull List<RConfig> list = rConfig.getRConfigList("actions");
        for (RConfig rc : list) {
            try {
                itemComponents.add(ItemComponent.builder(getCustomMaterial()).load(rc).build());
            } catch (InvalidConfiguration e) {
                LogHelper.error(e.getComponent());
            }
        }
    }

    @Override
    public void action(LivingEntity entity) {
        if (chance == 1.0 || PluginHelper.getRandom().nextDouble() < chance)
            itemComponents.forEach(itemComponent -> {
                itemComponent.action(entity);
                this.sendMessage(entity);
            });
    }

    private void action(Collection<LivingEntity> entities) {
        if (chance == 1.0 || PluginHelper.getRandom().nextDouble() < chance)
            itemComponents.forEach(itemComponent -> entities.forEach(entity -> {
                itemComponent.action(entity);
                this.sendMessage(entity);
            }));
    }

    public void onBuildItemStack(ItemStackBuilder builder) {
        for (ItemComponent itemComponent : itemComponents) {
            itemComponent.onBuildItemStack(builder);
        }
    }

    // -------- Events
    public void onDurabilityChange(PlayerItemDamageEvent event) {
    }

    public void onDefense(EntityDamageByEntityEvent event, ItemStack stack) {
    }

    public void onInteract(PlayerInteractEvent event) {
        if (trigger == Trigger.RIGHT_CLICK && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK))
            action(target.getTargetLivingEntities(event.getPlayer(), null, event.getPlayer().getLocation()));
    }

    public void onItemSpawn(ItemSpawnEvent event) {
    }

    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
    }

    public void onAttack(EntityDamageByEntityEvent event, ItemStack inHand) {
        if (trigger == Trigger.ATTACK) {
            if (event.getDamager() instanceof LivingEntity)
                action(target.getTargetLivingEntities((LivingEntity) event.getDamager(), (event.getEntity() instanceof LivingEntity) ? (LivingEntity) event.getEntity() : null, null));
        }
    }

    @Override
    public void onProjectileHit(ProjectileHitEvent event) {
        if (trigger == Trigger.RANGE_ATTACK) {
            if (event.getHitEntity() != null) {
                action(target.getTargetLivingEntities((LivingEntity) event.getEntity().getShooter(), (LivingEntity) event.getHitEntity(), event.getHitEntity().getLocation()));
            }
            if (event.getHitBlock() != null) {
                action(target.getTargetLivingEntities((LivingEntity) event.getEntity().getShooter(), null, event.getHitBlock().getLocation()));
            }

        }
    }

    public ItemChangeResult onDurabilityDeath(PlayerDeathEvent event, ItemChangeResult result, ItemStack stack) {
        return result;
    }

    public void onInventoryOpen(InventoryOpenEvent event, ItemStack stack) {
    }

    public void onHold(PlayerItemHeldEvent event) {
    }

}
