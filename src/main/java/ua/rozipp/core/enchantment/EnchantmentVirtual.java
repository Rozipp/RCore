package ua.rozipp.core.enchantment;

import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ua.rozipp.core.items.ItemHelper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class EnchantmentVirtual extends Enchantment {

	public EnchantmentVirtual(@NotNull NamespacedKey id) {
		super(id);
		fix(this);
	}

	static void fix(Enchantment ench) {
		try {
			if (!Arrays.asList(Enchantment.values()).contains(ench)) {
				Field f = Enchantment.class.getDeclaredField("acceptingNew");
				f.setAccessible(true);
				f.set(null, true);
				Enchantment.registerEnchantment(ench);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean canEnchantItem(@NotNull ItemStack item) {
		if (!ItemHelper.isPresent(item)) return false;
		Material material = item.getType();
		if (material == Material.BOOK || material == Material.ENCHANTED_BOOK) return true;

		CustomEnchantment ce = Enchantments.enchantmentList.get(this.getKey());
		if (ce.naturalItems.isEmpty()) return true;
		return ce.naturalItems.contains(material);
	}

	@Override
	public @NotNull Component displayName(int level) {
		return Component.empty();
	}

	@Override
	public boolean isTradeable() {
		return false;
	}

	@Override
	public boolean isDiscoverable() {
		return false;
	}

	@Override
	public @NotNull EnchantmentRarity getRarity() {
		return EnchantmentRarity.COMMON;
	}

	@Override
	public float getDamageIncrease(int level, @NotNull EntityCategory entityCategory) {
		return 0;
	}

	@Override
	public @NotNull Set<EquipmentSlot> getActiveSlots() {
		return new HashSet<>();
	}

	@Override
	public boolean conflictsWith(@NotNull Enchantment other) {
		Objects.requireNonNull(other, "Cannot check against a null item");

		CustomEnchantment cethis = Enchantments.enchantmentList.get(this.getKey());
		if (other == this) return true;

		if (cethis.group.equals("Default")) return false;

		CustomEnchantment ceother = Enchantments.enchantmentList.get(other.getKey());
		return cethis.group.equals(ceother.group);
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ALL;
	}

	@Override
	public int getMaxLevel() {
		CustomEnchantment ce = Enchantments.enchantmentList.get(this.getKey());
		return ce.maxLevel;
	}

	@Override
	public @NotNull String getName() {
		CustomEnchantment ce = Enchantments.enchantmentList.get(this.getKey());
		return ce.name;
	}

	@Override
	public int getStartLevel() {
		return 0;
	}

	@Override
	public boolean isCursed() {
		return false;
	}

	@Override
	public boolean isTreasure() {
		return false;
	}

}
