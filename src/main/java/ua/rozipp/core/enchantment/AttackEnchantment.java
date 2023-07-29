package ua.rozipp.core.enchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class AttackEnchantment extends CustomEnchantment {

	public AttackEnchantment() {
		super( "attack", Component.translatable("itemLore_Attack"), ItemSet.ALLWEAPONS, 100, null);
	}

	@Override
	public Component getDisplayName(int level) {
		return this.displayName
				.append(Component.text(" +" + level))
				.color(NamedTextColor.YELLOW);
	}
	
	public static Double onAttack(int level) {
		return  1.0 *level;
	}
}
