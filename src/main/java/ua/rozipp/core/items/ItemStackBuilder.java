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
    private static final ItemFlag[] ALL_FLAGS = new ItemFlag[]{
            ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES,
            ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_POTION_EFFECTS,
            ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON
    };
    private final ItemStack ITEM_STACK;
    private Component name;
    private Integer amount;
    private boolean clearLore;
    private boolean clearEnchantments;
    private final List<Component> lore = new ArrayList<>();
    private Map<Enchantment, Integer> enchantments;
    private Integer durability;
    private Color color;
    private final Set<ItemFlag> flags = new LinkedHashSet<>();
    private final Map<String, String> tags = new HashMap<>();
    private Map<Attribute, List<AttributeModifier>> attributes;

    private ItemStackBuilder(ItemStack itemStack) {
        this.ITEM_STACK = Objects.requireNonNull(itemStack, "itemStack");
    }

    public static ItemStackBuilder of(Material material) {
        return new ItemStackBuilder(new ItemStack(material)).hideAttributes();
    }

    public static ItemStackBuilder of(ItemStack itemStack) {
        return new ItemStackBuilder(itemStack.clone()).hideAttributes();
    }

    public ItemStack build() {
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
        if (name != null) META.displayName(TextUtils.translate(name));
        if (!lore.isEmpty()) {
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
        if (!flags.isEmpty()) {
            flags.forEach(META::addItemFlags);
        }

        ITEM_STACK.setItemMeta(META);
        NBTItem nbti = new NBTItem(ITEM_STACK);

        if (!tags.isEmpty()) {
            for (String key : tags.keySet()) {
                ItemHelper.setNBTTag(nbti, key, tags.get(key));
            }
        }

        nbti.applyNBT(ITEM_STACK);
        return ITEM_STACK;
    }

    public ItemStackBuilder addTag(String key, String value) {
        tags.put(key, value);
        return this;
    }

    public ItemStackBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    @Deprecated
    public ItemStackBuilder setName(String name) {
        this.name = Component.text(TextUtils.colorize(name));
        return this;
    }

    public ItemStackBuilder name(Component name) {
        this.name = name;
        return this;
    }

    @Deprecated
    public ItemStackBuilder addLore(String loreLine) {
        lore.add(Component.text(TextUtils.colorize(loreLine)));
        return this;
    }

    public ItemStackBuilder addLore(Component loreLine) {
        lore.add(loreLine);
        return this;
    }

    @Deprecated
    public ItemStackBuilder addLore(String... loreLine) {
        for (String line : loreLine)
            lore.add(Component.text(TextUtils.colorize(line)));
        return this;
    }

    public ItemStackBuilder addLore(Component... loreLines) {
        Collections.addAll(lore, loreLines);
        return this;
    }

    public ItemStackBuilder setDurability(int durability) {
        this.durability = durability;
        return this;
    }

    public ItemStackBuilder setData(int data) {
        return setDurability(data);
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

    //-------------- flags

    public ItemStackBuilder flag(ItemFlag... flags) {
        this.flags.addAll(List.of(flags));
        return this;
    }

    public ItemStackBuilder unflag(ItemFlag... flags) {
        List.of(flags).forEach(this.flags::remove);
        return this;
    }

    public ItemStackBuilder hideAttributes() {
        return flag(ALL_FLAGS);
    }

    public ItemStackBuilder showAttributes() {
        return unflag(ALL_FLAGS);
    }

}