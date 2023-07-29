package ua.rozipp.core.enchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import ua.rozipp.core.util.RomanNumerals;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Arrays;

/** EnchantmentAPI © 2017 com.sucy.enchant.vanilla.VanillaEnchantment */
public class VanillaEnchantment extends CustomEnchantment {

	public VanillaEnchantment(Enchantment ench, ItemSet itemSet, int maxLevel) {
		this(ench, ench.displayName(0), itemSet, maxLevel, "Default");
	}

	public VanillaEnchantment(Enchantment ench, ItemSet itemSet, int maxLevel, String group) {
		this(ench, ench.displayName(0), itemSet, maxLevel, group);
	}

	public VanillaEnchantment(Enchantment ench, Component displayName, ItemSet itemSet, int maxLevel, String group) {
		super(ench);
		this.displayName = displayName;
		enchantment = ench;
		this.naturalItems.addAll(Arrays.asList(itemSet.getItems()));
		this.maxLevel = maxLevel;
		this.group = group;
	}

	@Override
	public ItemStack addToItem(final ItemStack item, final int level) {
		if (item.getType() == Material.BOOK) {
			item.setType(Material.ENCHANTED_BOOK);
		}
		if (item.getType() == Material.ENCHANTED_BOOK) {
			final EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
			meta.addStoredEnchant(this.enchantment, level, true);
			item.setItemMeta(meta);
			return item;
		}
		item.addUnsafeEnchantment(this.enchantment, level);
		return item;
	}

	@Override
	public ItemStack removeFromItem(final ItemStack item) {
		if (item.getType() == Material.ENCHANTED_BOOK) {
			final EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
			meta.removeStoredEnchant(this.enchantment);
			item.setItemMeta(meta);
		}
		item.removeEnchantment(this.enchantment);
		return item;
	}

	@Override
	public Component getDisplayName(int level) {
		return displayName
				.append(Component.text(enchantment.getMaxLevel() > 1 ? " " + RomanNumerals.toNumerals(level) : ""))
				.color(NamedTextColor.GRAY);
	}
}
