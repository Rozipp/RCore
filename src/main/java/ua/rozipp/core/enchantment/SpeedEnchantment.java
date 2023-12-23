package ua.rozipp.core.enchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class SpeedEnchantment extends CustomEnchantment {

	private static Double percentPerLevel = 0.02;

	protected SpeedEnchantment() {
		super("speed", Component.translatable("item.enchantment.lore.speed_Bonus"), ItemSet.LEGGINGS, 100, null);
	}

	@Override
	public Component getDisplayName(int level) {
		return this.displayName.append(Component.text(" +" + percentPerLevel * level * 100 + "%")).color(NamedTextColor.BLUE);
	}

	public static Double getModifitedSpeed(int level) {
		return percentPerLevel * level;
	}

}
