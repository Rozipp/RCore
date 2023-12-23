package ua.rozipp.core.enchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class DefenseEnchantment extends CustomEnchantment {

	public static Double defensePerLevel = 1.0;

	public DefenseEnchantment() {
		super("defense", Component.translatable("item.enchantment.lore.defense"), ItemSet.ARMOR, 100, null);
	}

	public Component getDisplayName(int level) {
		return this.displayName.append(Component.text(" +" + defensePerLevel * level)).color(NamedTextColor.RED);
	}

}
