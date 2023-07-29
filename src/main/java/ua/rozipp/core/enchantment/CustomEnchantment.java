package ua.rozipp.core.enchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ua.rozipp.core.RCore;

import java.util.*;

public class CustomEnchantment {
	
	public static Map<String, CustomEnchantment> allEnchantmentCustom = new HashMap<>();
	
	// ---- Armor ----
	public static CustomEnchantment BINDING_CURSE = new VanillaEnchantment(Enchantment.BINDING_CURSE, ItemSet.ALL, 1);
	public static CustomEnchantment DEPTH_STRIDER = new VanillaEnchantment(Enchantment.DEPTH_STRIDER, ItemSet.BOOTS, 3, "stride");
	public static CustomEnchantment FROST_WALKER = new VanillaEnchantment(Enchantment.FROST_WALKER, ItemSet.BOOTS, 2, "stride");
	public static CustomEnchantment OXYGEN = new VanillaEnchantment(Enchantment.OXYGEN, ItemSet.HELMETS, 3);
	public static CustomEnchantment PROTECTION_ENVIRONMENTAL = new VanillaEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, ItemSet.ARMOR, 4, "protection");
	public static CustomEnchantment PROTECTION_EXPLOSIONS = new VanillaEnchantment(Enchantment.PROTECTION_EXPLOSIONS, ItemSet.ARMOR, 4, "protection");
	public static CustomEnchantment PROTECTION_FALL = new VanillaEnchantment(Enchantment.PROTECTION_FALL, ItemSet.BOOTS, 4, "protection");
	public static CustomEnchantment PROTECTION_FIRE = new VanillaEnchantment(Enchantment.PROTECTION_FIRE, ItemSet.ARMOR, 4, "protection");
	public static CustomEnchantment PROTECTION_PROJECTILE = new VanillaEnchantment(Enchantment.PROTECTION_PROJECTILE, ItemSet.ARMOR, 4, "protection");
	public static CustomEnchantment THORNS = new VanillaEnchantment(Enchantment.THORNS, ItemSet.CHESTPLATES, 3);
	public static CustomEnchantment WATER_WORKER = new VanillaEnchantment(Enchantment.WATER_WORKER, ItemSet.HELMETS, 1);
	// ---- Swords ----
	public static CustomEnchantment DAMAGE_ALL = new VanillaEnchantment(Enchantment.DAMAGE_ALL, ItemSet.WEAPONS, 5, "damage");
	public static CustomEnchantment DAMAGE_ARTHROPODS = new VanillaEnchantment(Enchantment.DAMAGE_ARTHROPODS, ItemSet.WEAPONS, 5, "damage");
	public static CustomEnchantment DAMAGE_UNDEAD = new VanillaEnchantment(Enchantment.DAMAGE_UNDEAD, ItemSet.WEAPONS, 5, "damage");
	public static CustomEnchantment FIRE_ASPECT = new VanillaEnchantment(Enchantment.FIRE_ASPECT, ItemSet.SWORDS, 2);
	public static CustomEnchantment KNOCKBACK = new VanillaEnchantment(Enchantment.KNOCKBACK, ItemSet.SWORDS, 2);
	public static CustomEnchantment LOOT_BONUS_MOBS = new VanillaEnchantment(Enchantment.LOOT_BONUS_MOBS, ItemSet.SWORDS, 3);
	public static CustomEnchantment SWEEPING_EDGE = new VanillaEnchantment(Enchantment.SWEEPING_EDGE, ItemSet.SWORDS, 3);
	// --- Tools ---
	public static CustomEnchantment DIG_SPEED = new VanillaEnchantment(Enchantment.DIG_SPEED, ItemSet.TOOLS, 5);
	public static CustomEnchantment LOOT_BONUS_BLOCKS = new VanillaEnchantment(Enchantment.LOOT_BONUS_BLOCKS, ItemSet.TOOLS, 3, "blocks");
	public static CustomEnchantment SILK_TOUCH = new VanillaEnchantment(Enchantment.SILK_TOUCH, ItemSet.TOOLS, 1, "blocks");
	// ---- Bows ----
	public static CustomEnchantment ARROW_DAMAGE = new VanillaEnchantment(Enchantment.ARROW_DAMAGE, ItemSet.BOWS, 5);
	public static CustomEnchantment ARROW_FIRE = new VanillaEnchantment(Enchantment.ARROW_FIRE, ItemSet.BOWS, 1);
	public static CustomEnchantment ARROW_INFINITE = new VanillaEnchantment(Enchantment.ARROW_INFINITE, ItemSet.BOWS, 1, "infinite");
	public static CustomEnchantment ARROW_KNOCKBACK = new VanillaEnchantment(Enchantment.ARROW_KNOCKBACK, ItemSet.BOWS, 2);
	// ---- Fishing ----
	public static CustomEnchantment LUCK = new VanillaEnchantment(Enchantment.LUCK, ItemSet.FISHING, 3);
	public static CustomEnchantment LURE = new VanillaEnchantment(Enchantment.LURE, ItemSet.FISHING, 3);
	// ---- Trident ---- 1.13
	// public static CustomEnchantment LOYALTY = new VanillaEnchantment(Enchantment.LOYALTY, ItemSet.TRIDENT, 3, 5, 5, 7, 43, 2, true);
	// public static CustomEnchantment IMPALING = new VanillaEnchantment(Enchantment.IMPALING, TRIDENT, 5, 2, 1, 8, 12, 4, true);
	// public static CustomEnchantment RIPTIDE = new VanillaEnchantment(Enchantment.RIPTIDE, TRIDENT, 3, 2, 10, 7, 43, 4, true);
	// public static CustomEnchantment CHANNELING = new VanillaEnchantment(Enchantment.CHANNELING, TRIDENT, 1, 1, 25, 0, 50, 8, true);
	// ---- All ----
	public static CustomEnchantment DURABILITY = new VanillaEnchantment(Enchantment.DURABILITY, ItemSet.DURABILITY, 3);
	public static CustomEnchantment MENDING = new VanillaEnchantment(Enchantment.MENDING, ItemSet.DURABILITY_ALL, 1, "infinite");
	public static CustomEnchantment VANISHING_CURSE = new VanillaEnchantment(Enchantment.VANISHING_CURSE, ItemSet.ALL, 1);

	public static CustomEnchantment Attack = new AttackEnchantment();
	public static CustomEnchantment BuyItem = new CustomEnchantment("buy_item", Component.translatable("itemLore_Buy"), ItemSet.NONE, 1, null);
	public static CustomEnchantment Critical = new CriticalEnchantment();
	public static CustomEnchantment Defense = new DefenseEnchantment();
	public static CustomEnchantment Evrei = new CustomEnchantment("evrei", Component.translatable("itemLore_evrei"), ItemSet.NONE, 1, null);
	public static CustomEnchantment Jumping = new CustomEnchantment("jumping", Component.text("Прыгучесть"), ItemSet.LEGGINGS, 100, null);
	public static CustomEnchantment NoRepair = new CustomEnchantment("norepair", Component.translatable("itemLore_noRepair"), ItemSet.NONE, 1, null);
	public static CustomEnchantment NoTech = new CustomEnchantment("notech", Component.translatable("itemLore_noTech"), ItemSet.NONE, 1, null);
	public static CustomEnchantment Poison = new PoisonEnchantment();
	public static CustomEnchantment SoulBound = new CustomEnchantment("soulbound", Component.translatable("itemLore_Soulbound"), ItemSet.ALL, 1, null);
	public static CustomEnchantment Speed = new SpeedEnchantment();
	public static CustomEnchantment TechOnly = new CustomEnchantment("techonly", Component.translatable("itemLore_techOnly").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD), ItemSet.NONE, 1, null);
	public static CustomEnchantment Thorns = new CustomEnchantment( "recoil", Component.text("Отдача").color(NamedTextColor.BLUE), ItemSet.CHESTPLATES, 5, null);
	public static CustomEnchantment UnitItem = new CustomEnchantment( "unit_item", Component.text("Предмет юнита").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD), ItemSet.NONE, 1, null);

	protected Enchantment enchantment;
	protected Component displayName;

	public final Set<Material> naturalItems = new HashSet<>();

	public @NotNull NamespacedKey id;
	public String name;
	public String group;
	public int maxLevel;

	@SuppressWarnings("deprecation")
	protected CustomEnchantment(Enchantment enchant) {
		this.id = enchant.getKey();
		this.name = enchant.getName().trim();
		if (!Enchantments.enchantmentList.containsKey(id)) Enchantments.enchantmentList.put(id, this);
		allEnchantmentCustom.put(this.name, this);
	}

	protected CustomEnchantment(@NotNull NamespacedKey id, String name, Component displayName, ItemSet itemSet, int maxLevel, String group) {
		Validate.notEmpty(name, "The name must be present and not empty");
		this.id = id;
		this.displayName = displayName;
		this.addNaturalItems(itemSet);
		this.name = name.trim();
		this.group = (group == null) ? "Default" : group;
		this.maxLevel = maxLevel;

		if (!Enchantments.enchantmentList.containsKey(id)) Enchantments.enchantmentList.put(id, this);
		enchantment = new EnchantmentVirtual(id);
		allEnchantmentCustom.put(this.name, this);
	}

	protected CustomEnchantment(Plugin plugin, final String name, Component displyName, ItemSet itemSet, int maxLevel, String group) {
		this(new NamespacedKey(plugin, name), name, displyName, itemSet, maxLevel, group);
	}

	protected CustomEnchantment(final String name, Component displyName, ItemSet itemSet, int maxLevel, String group) {
		this(new NamespacedKey(RCore.getInstance(), name), name, displyName, itemSet, maxLevel, group);
	}

	public void addNaturalItems(final Material... materials) {
		for (Material material : materials) {
			Objects.requireNonNull(material, "Cannot add a null natural material");
			naturalItems.add(material);
		}
	}

	public void addNaturalItems(final ItemSet... items) {
		for (ItemSet item : items) {
			if (item.equals(ItemSet.NONE)) {
				naturalItems.clear();
				break;
			}
			for (Material material : item.getItems()) {
				Objects.requireNonNull(material, "Cannot add a null natural material");
				naturalItems.add(material);
			}
		}
	}

	/** @param item item to add to
	 * @param level enchantment level
	 * @return item with the enchantment */
	public ItemStack addToItem(final ItemStack item, final int level) {
		Objects.requireNonNull(item, "Item cannot be null");
		Validate.isTrue(level > 0, "Level must be at least 1");
		if (item.getType() == Material.BOOK) {
			item.setType(Material.ENCHANTED_BOOK);
		}
		final ItemMeta meta = item.getItemMeta();
		final List<Component> lore = meta.hasLore() ? meta.lore() : new ArrayList<>();
		assert lore != null;
		final int lvl = Enchantments.getCustomEnchantments(item).getOrDefault(this, 0);
		if (lvl > 0) {
			lore.remove(this.getDisplayName(lvl));
		}
		lore.add(0, this.getDisplayName(level));
		meta.lore(lore);
		item.setItemMeta(meta);
		return item;
	}

	/** @param item item to remove from
	 * @return item without this enchantment */
	public ItemStack removeFromItem(final ItemStack item) {
		Objects.requireNonNull(item, "Item cannot be null");
		final int lvl = Enchantments.getCustomEnchantments(item).getOrDefault(this, 0);
		if (lvl > 0) {
			final ItemMeta meta = item.getItemMeta();
			final List<Component> lore = meta.hasLore() ? meta.lore() : new ArrayList<>();
			assert lore != null;
			lore.remove(this.getDisplayName(lvl));
			meta.lore(lore);
			item.setItemMeta(meta);
		}
		return item;
	}

	public String getName() {
		return name;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public boolean canEnchantItem(ItemStack item) {
		return enchantment.canEnchantItem(item);
	}

	public Component getDisplayName(int level) {
		if (enchantment.getName().startsWith("§"))
			return displayName
					.append(Component.text(enchantment.getMaxLevel() > 1 ? " " + level : ""));
		return displayName
				.append(Component.text(enchantment.getMaxLevel() > 1 ? " " + level : ""))
				.color(NamedTextColor.GRAY);
	}

}
