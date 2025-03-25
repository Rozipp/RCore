package ua.rozipp.core.itemscomponents;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import ua.rozipp.core.RCore;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.ItemStackBuilder;

public class RangedAttack extends ItemComponent {

    private double damageValue;

    public RangedAttack() {
        super();
    }

    public RangedAttack(double damageValue) {
        super();
        this.damageValue = damageValue;
    }

    @Override
    protected void load(RConfig rConfig) throws InvalidConfiguration {
        damageValue = rConfig.getDouble("value", 1.0, null);
    }

    @Override
    public void onBuildItemStack(ItemStackBuilder builder) {
        builder.addLore(Component.text(damageValue)
                .append(Component.space())
                .append(Component.translatable("item.component.lore.rangedAttack"))
                .color(NamedTextColor.LIGHT_PURPLE));
    }

    private static double ARROW_MAX_VEL = 6.0;

    @Override
    public void onInteract(PlayerInteractEvent event) {
//		if (UnitStatic.isWearingAnyMetal(event.getPlayer())) {
//			event.setCancelled(true);
//			CivMessage.sendError(event.getPlayer(), CivSettings.localize.translate("itemLore_RangedAttack_errorMetal"));
//			return;
//		}
    }


    @Override
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getHitEntity() == null) return;
        double dmg = damageValue;
        double extraAtt = 0.0;
//        if (Enchantments.hasEnchantment(inHand, CustomEnchantment.Attack))
//            dmg += AttackEnchantment.onAttack(Enchantments.getLevelEnchantment(inHand, CustomEnchantment.Attack));
        ProjectileSource shooter = event.getEntity().getShooter();
        if (shooter instanceof LivingEntity livingShooter) {
            Vector vel = livingShooter.getVelocity();
            double magnitudeSquared = Math.pow(vel.getX(), 2) + Math.pow(vel.getY(), 2) + Math.pow(vel.getZ(), 2);
            double percentage = magnitudeSquared / ARROW_MAX_VEL;
            if (percentage > 1) percentage = 1;
            dmg = percentage * dmg;

            dmg += extraAtt;
            if (dmg < RCore.minDamage) dmg = RCore.minDamage;
            EntityDamageByEntityEvent event1 = new EntityDamageByEntityEvent(event.getEntity(), event.getHitEntity(), EntityDamageEvent.DamageCause.PROJECTILE, dmg);
            event1.callEvent();
        }
    }

    @Override
    public void action(LivingEntity entity) {
    }
}
