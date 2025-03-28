package ua.rozipp.core.itemscomponents;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.RCore;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.enchantment.AttackEnchantment;
import ua.rozipp.core.enchantment.CustomEnchantment;
import ua.rozipp.core.enchantment.Enchantments;
import ua.rozipp.core.enchantment.PoisonEnchantment;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.AttributeModifierBuilder;
import ua.rozipp.core.items.ItemStackBuilder;

import java.util.Collection;

public class Attack extends ItemComponent {

	private double damageValue;

	public Attack() {
		super();
	}

	public Attack(double damageValue) {
		super();
		this.damageValue = damageValue;
	}

	@Override
	protected void load(RConfig rConfig) throws InvalidConfiguration {
		this.damageValue = rConfig.getDouble("value", 1.0, null);
	}

	@Override
	public void onBuildItemStack(ItemStackBuilder builder) {
		// Add generic attack damage to 0 to clear the default lore on item.
		builder.addAttribute(Attribute.GENERIC_ATTACK_DAMAGE,
				AttributeModifierBuilder.newBuilder()
						.name("Attack").amount(0).build());
		builder.addLore(Component.text(damageValue)
				.append(Component.space())
				.append(Component.translatable("item.component.lore.attack"))
				.color(NamedTextColor.BLUE));
	}

	@Override
	@Deprecated
	public void onAttack(EntityDamageByEntityEvent event, ItemStack inHand) {
		double dmg = damageValue;
		double extraAtt = 0.0;
		if (Enchantments.hasEnchantment(inHand, CustomEnchantment.Attack)) extraAtt += AttackEnchantment.onAttack(Enchantments.getLevelEnchantment(inHand, CustomEnchantment.Attack));
		if (Enchantments.hasEnchantment(inHand, CustomEnchantment.Poison)) PoisonEnchantment.onAttack(event);

		dmg *= event.getOriginalDamage(DamageModifier.BASE);
		
		dmg += extraAtt;

		if (dmg < RCore.minDamage) dmg = RCore.minDamage;
		event.setDamage(dmg);
	}

}
