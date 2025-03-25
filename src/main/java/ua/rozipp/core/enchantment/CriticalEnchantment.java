package ua.rozipp.core.enchantment;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.PluginHelper;

public class CriticalEnchantment extends CustomEnchantment {

	static double percentLevel = 0.05;

	public CriticalEnchantment() {
		super("Critical", Component.text("Криты"), ItemSet.SWORDS, 100, null);
	}

	public static boolean randomCriticalAttack(ItemStack weapon) {
		int level = Enchantments.getLevelEnchantment(weapon, CustomEnchantment.Critical);
		double d = percentLevel * level;
		return PluginHelper.getRandom().nextDouble() < d;
	}

	public static void run(EntityDamageByEntityEvent event, ItemStack weapon) {
		if (randomCriticalAttack(weapon)){
			if (event.getEntity() instanceof LivingEntity entity) {
				PluginHelper.sync().runLater(() -> entity.damage(event.getDamage(), event.getDamager()), 1);
			}}
	}
}