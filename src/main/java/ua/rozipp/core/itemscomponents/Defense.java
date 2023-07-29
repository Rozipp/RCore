package ua.rozipp.core.itemscomponents;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.RCore;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.enchantment.CustomEnchantment;
import ua.rozipp.core.enchantment.DefenseEnchantment;
import ua.rozipp.core.enchantment.Enchantments;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.CustomMaterial;
import ua.rozipp.core.items.ItemStackBuilder;

public class Defense extends ItemComponent {
    private final double defValue;

    public Defense(RConfig compInfo) throws InvalidConfiguration {
        super(compInfo);
        defValue = compInfo.getDouble("value", 1.0, null);
    }

    @Override
    public void onPrepareCreate(ItemStackBuilder builder) {
        builder.addLore(Component.text(defValue)
                .append(Component.space())
                .append(Component.translatable("newItemLore_Defense"))
                .color(NamedTextColor.BLUE));
    }

    @Override
    public void onDefense(EntityDamageByEntityEvent event, ItemStack stack) {
        double def = defValue;
        /* Try to getItemComponentClass any extra defense enhancements from this item. */
        CustomMaterial craftMat = CustomMaterial.getCustomMaterial(stack);
        if (craftMat == null) return;

        double extraDef = 0;
        if (Enchantments.hasEnchantment(stack, CustomEnchantment.Defense)) {
            extraDef += DefenseEnchantment.defensePerLevel * Enchantments.getLevelEnchantment(stack, CustomEnchantment.Defense);
        }

        def += extraDef;
        double damage = event.getDamage();

        damage -= def;
        if (damage < RCore.minDamage) {
            /* Always do at least 0.5 damage. */
            damage = RCore.minDamage;
        }

        event.setDamage(damage);
    }

}
