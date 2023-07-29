package ua.rozipp.core.items;

import de.tr7zw.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import ua.rozipp.core.util.TextUtils;

import java.util.*;

public class ItemStackBuilder {

    private Material material = null;
    private ItemStack origin = null;
    private Component name;
    private Integer amount;
    private boolean clearLore;
    private boolean clearEnchantments;
    private List<Component> lore;
    private Map<Enchantment, Integer> enchantments;
    private Integer durability;
    private Color color;
    private Set<ItemFlag> addflags;
    private Map<String, String> tags;
    private Map<Attribute, List<AttributeModifier>> attributes;

    public ItemStackBuilder(ItemStack item) {
        this.origin = item;
    }

    public ItemStackBuilder(Material mat) {
        Objects.requireNonNull(mat);
        this.material = mat;
    }

    public ItemStack build() {
        ItemStack ITEM_STACK = (origin == null) ? new ItemStack(material) : origin;
        if (amount != null) ITEM_STACK.setAmount(amount);

        if (durability != null) ITEM_STACK.setDurability(durability.shortValue());
        if (clearEnchantments)
            for (Enchantment enchantment : ITEM_STACK.getEnchantments().keySet()) {
                ITEM_STACK.removeEnchantment(enchantment);
            }
        if (enchantments != null) {
            for (Enchantment enchantment : enchantments.keySet()) {
                ITEM_STACK.addUnsafeEnchantment(enchantment, enchantments.get(enchantment));
            }
        }

        ItemMeta META = ITEM_STACK.getItemMeta();
        if (name != null) META.displayName(name);
        if (lore != null) {
            if (clearLore)
                META.lore(lore);
            else {
                List<Component> l = META.lore();
                if (l == null) {
                    META.lore(lore);
                } else {
                    l.addAll(lore);
                    META.lore(l);
                }
            }
        }
        if (attributes != null) {
            for (Attribute attribute : attributes.keySet()) {
                for (AttributeModifier attributeModifier : attributes.get(attribute))
                    META.addAttributeModifier(attribute, attributeModifier);
            }
        }
        Material type = ITEM_STACK.getType();

        if (color != null && (type == Material.LEATHER_BOOTS || type == Material.LEATHER_CHESTPLATE || type == Material.LEATHER_HELMET || type == Material.LEATHER_LEGGINGS)) {
            LeatherArmorMeta leatherMeta = (LeatherArmorMeta) META;
            leatherMeta.setColor(color);
        }
        if (addflags != null) {
            for (ItemFlag flag : addflags)
                META.addItemFlags(flag);
        }

        ITEM_STACK.setItemMeta(META);
        NBTItem nbti = new NBTItem(ITEM_STACK);

        if (tags != null) {
            for (String key : tags.keySet()) {
                ItemHelper.setNBTTag(nbti, key, tags.get(key));
            }
        }

        nbti.applyNBT(ITEM_STACK);
        return ITEM_STACK;
    }

    public ItemStackBuilder addTag(String key, String value) {
        if (tags == null) tags = new HashMap<>();
        tags.put(key, value);
        return this;
    }

    public ItemStackBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    @Deprecated
    public ItemStackBuilder setName(String name) {
        this.name = Component.text(TextUtils.translateAlternateColorCodes(name));
        return this;
    }

    public ItemStackBuilder name(Component name) {
        this.name = name;
        return this;
    }

    @Deprecated
    public ItemStackBuilder addLore(String loreLine) {
        if (lore == null) lore = new ArrayList<>();
        lore.add(Component.text(TextUtils.translateAlternateColorCodes(loreLine)));
        return this;
    }

    public ItemStackBuilder addLore(Component loreLine) {
        if (lore == null) lore = new ArrayList<>();
        lore.add(loreLine);
        return this;
    }

    @Deprecated
    public ItemStackBuilder addLore(String... loreLine) {
        if (lore == null) lore = new ArrayList<>();
        for (String line : loreLine)
            lore.add(Component.text(TextUtils.translateAlternateColorCodes(line)));
        return this;
    }

    public ItemStackBuilder addLore(Component... loreLines) {
        if (lore == null)
            lore = new ArrayList<>();
        else
            Collections.addAll(lore, loreLines);
        return this;
    }

    @Deprecated
    public ItemStackBuilder setLore(String[] loreLines) {
        if (lore == null) lore = new ArrayList<>();
        for (String s : loreLines)
            lore.add(Component.text(TextUtils.translateAlternateColorCodes(s)));
        return this;
    }

    @Deprecated
    public ItemStackBuilder setLore(Collection<String> loreLines) {
        if (lore == null) lore = new ArrayList<>();
        for (String s : loreLines)
            lore.add(Component.text(TextUtils.translateAlternateColorCodes(s)));
        return this;
    }

    @Deprecated
    public ItemStackBuilder lore(List<Component> loreLines) {
        if (lore == null)
            lore = loreLines;
        else
            lore.addAll(loreLines);
        return this;
    }

    public ItemStackBuilder setDurability(int durability) {
        this.durability = durability;
        return this;
    }

    public ItemStackBuilder addEnchantment(Enchantment enchantment, final int level) {
        if (enchantments == null) enchantments = new HashMap<>();
        enchantments.put(enchantment, level);
        return this;
    }

    public ItemStackBuilder addEnchantment(Enchantment enchantment) {
        enchantments.put(enchantment, 1);
        return this;
    }

    public ItemStackBuilder addAttribute(Attribute attribute, AttributeModifier attributeModifer) {
        if (attribute == null) attributes = new HashMap<>();
        List<AttributeModifier> attributeModifierList;

        if (attributes.containsKey(attribute))
            attributeModifierList = attributes.get(attribute);
        else
            attributeModifierList = new LinkedList<>();
        attributeModifierList.add(attributeModifer);

        attributes.put(attribute, attributeModifierList);
        return this;
    }

    public ItemStackBuilder clearLore() {
        this.clearLore = true;
        return this;
    }

    public ItemStackBuilder clearEnchantments() {
        clearEnchantments = true;
        return this;
    }

    public ItemStackBuilder setColor(Color color) {
        this.color = color;
        return this;
    }

    public ItemStackBuilder addItemFlag(ItemFlag flag) {
        if (addflags == null) addflags = new LinkedHashSet<>();
        addflags.add(flag);
        return this;
    }

}