package ua.rozipp.core.enchantment;

import net.kyori.adventure.text.Component;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ua.rozipp.core.items.ItemHelper;

import java.util.*;

public class Enchantments {
	public static Map<NamespacedKey, CustomEnchantment> enchantmentList = new HashMap<>();

	public static void addEnchantment(ItemStack item, CustomEnchantment enchantment, Integer level) {
		Objects.requireNonNull(item, "Item cannot be null");
		Validate.isTrue(level > 0, "Level must be at least 1");
		if (item.getType() == Material.BOOK) item.setType(Material.ENCHANTED_BOOK);

		ItemMeta meta = item.getItemMeta();
		List<Component> lore = meta.lore();
		if (lore == null) lore = new ArrayList<>();
		int lvl = Enchantments.getLevelEnchantment(item, enchantment);
		if (lvl > 0) lore.remove(enchantment.getDisplayName(lvl));
		lore.add(0, enchantment.getDisplayName(level));
		meta.lore(lore);
		meta.addEnchant(enchantment.enchantment, level, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
	}

//TODO	public static void addEnchantment(ItemStackBuilder builder, CustomEnchantment enchantment, Integer level) {
//
//		Objects.requireNonNull(builder, "Item cannot be null");
//		Validate.isTrue(level > 0, "Level must be at least 1");
//
//		final List<String> lore = builder.getLore();
//		if (lore == null) new ArrayList<>();
//		int lvl = builder.getEnchantLevel(enchantment.enchantment);
//		if (lvl > 0) {
//			lore.remove(enchantment.getDisplayName(lvl));
//		}
//		lore.add(0, enchantment.getDisplayName(level));
//		builder.setLore(lore);
//		builder.addEnchantment(enchantment.enchantment, level);
//	}

	public static void removeEnchantment(ItemStack item, CustomEnchantment enchantment) {
		Objects.requireNonNull(item, "Item cannot be null");
		final int lvl = Enchantments.getLevelEnchantment(item, enchantment);
		if (lvl > 0) {
			final ItemMeta meta = item.getItemMeta();
			meta.removeEnchant(enchantment.enchantment);
			final List<Component> lore = meta.lore();
			if (lore != null) {
				lore.remove(enchantment.getDisplayName(lvl));
				meta.lore(lore);
			}
			item.setItemMeta(meta);
		}
	}

	public static Integer getLevelEnchantment(final ItemStack item, CustomEnchantment enchantment) {
		if (item == null) return 0;
		return item.getItemMeta().getEnchantLevel(enchantment.enchantment);
	}

	public static CustomEnchantment getCustomEnchantment(NamespacedKey id) {
		return enchantmentList.get(id);
	}

	public static Map<CustomEnchantment, Integer> getCustomEnchantments(final ItemStack item) {
		final HashMap<CustomEnchantment, Integer> list = new HashMap<>();
		if (!ItemHelper.isPresent(item)) return list;

		final ItemMeta meta = item.getItemMeta();
		if (meta == null || !meta.hasLore()) return list;

		for (Enchantment ench : meta.getEnchants().keySet()) {
			NamespacedKey id = ench.getKey();
			if (enchantmentList.containsKey(id)) {
				CustomEnchantment enchant = enchantmentList.get(id);
				if (enchant == null) continue;
				int level = meta.getEnchants().get(ench);
				if (level > 0) list.put(enchant, level);
			}
		}
		return list;
	}

	public static Map<CustomEnchantment, Integer> getAllEnchantments(final ItemStack item) {
		final Map<CustomEnchantment, Integer> result = getCustomEnchantments(item);
		return result;
	}

	public static boolean hasEnchantment(final ItemStack item, final CustomEnchantment enchantment) {
		if (item == null || !item.hasItemMeta()) return false;
		return item.getItemMeta().hasEnchant(enchantment.enchantment);
	}

	public static ItemStack removeAllEnchantments(final ItemStack item) {
		item.getEnchantments().forEach((enchant, level) -> item.removeEnchantment(enchant));
		return item;
	}

}
