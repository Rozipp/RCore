package ua.rozipp.core.enchantment;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.Task;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.RCore;

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
			if (event.getEntity() instanceof LivingEntity) {
				LivingEntity entity = (LivingEntity) event.getEntity();
				Task.syncDelayed(RCore.getInstance(), () -> entity.damage(event.getDamage(), event.getDamager()), 1);
			}}
	}
}