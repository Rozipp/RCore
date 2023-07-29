package ua.rozipp.core.itemscomponents;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import ua.rozipp.core.RCore;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.enchantment.AttackEnchantment;
import ua.rozipp.core.enchantment.CustomEnchantment;
import ua.rozipp.core.enchantment.Enchantments;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.ItemStackBuilder;

public class RangedAttack extends ItemComponent {

    private final double damageValue;

    public RangedAttack(RConfig compInfo) throws InvalidConfiguration {
        super(compInfo);
        damageValue = compInfo.getDouble("value", 1.0, null);
    }

    @Override
    public void onPrepareCreate(ItemStackBuilder builder) {
        builder.addLore(Component.text(damageValue)
                .append(Component.space())
                .append(Component.translatable("itemLore_RangedAttack"))
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

    public void onRangedAttack(EntityDamageByEntityEvent event, ItemStack inHand) {
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player attacker = (Player) arrow.getShooter();
//				if (UnitStatic.isWearingAnyMetal(attacker)) {
//					event.setCancelled(true);
//					CivMessage.sendError(attacker, CivSettings.localize.translate("itemLore_RangedAttack_errorMetal"));
//					return;
//				}
            }
        }
        double dmg = damageValue;
        double extraAtt = 0.0;
        if (Enchantments.hasEnchantment(inHand, CustomEnchantment.Attack))
            dmg += AttackEnchantment.onAttack(Enchantments.getLevelEnchantment(inHand, CustomEnchantment.Attack));

        Vector vel = event.getDamager().getVelocity();
        double magnitudeSquared = Math.pow(vel.getX(), 2) + Math.pow(vel.getY(), 2) + Math.pow(vel.getZ(), 2);
        double percentage = magnitudeSquared / ARROW_MAX_VEL;
        if (percentage > 1) percentage = 1;
        dmg = percentage * dmg;

        dmg += extraAtt;
        if (dmg < RCore.minDamage) dmg = RCore.minDamage;
        event.setDamage(dmg);
    }
}
