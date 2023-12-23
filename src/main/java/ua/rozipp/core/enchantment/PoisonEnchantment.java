package ua.rozipp.core.enchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonEnchantment extends CustomEnchantment {

	public PoisonEnchantment() {
		super("poision", Component.translatable("item.enchantment.lore.poision").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD), ItemSet.WEAPONS, 1, null);
	}

	public static void onAttack(EntityDamageByEntityEvent event) {
		Player player = null;

		if (!event.isCancelled() && event.getEntity() instanceof LivingEntity && !(event.getEntity() instanceof ArmorStand) && !(event.getEntity() instanceof Slime)) {
			if (!(event.getEntity() instanceof Player)) {
				((LivingEntity) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 50, 1));
			} else {
				player = (Player) event.getEntity();
				player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 0));
			}
		}
	}
}
