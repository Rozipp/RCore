package ua.rozipp.core.items;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.MessageHelper;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.itemscomponents.BlockPlace;
import ua.rozipp.core.itemscomponents.ItemComponent;
import ua.rozipp.core.itemscomponents.OpenGui;
import ua.rozipp.core.recipes.CustomRecipe;
import ua.rozipp.core.util.TextUtils;

import java.util.*;

@Getter
public class CustomMaterial {
    private static final String TAGMANE = "RMID";
    private static final Map<Key, CustomMaterial> materials = new HashMap<>();

    protected @NotNull Key mid;
    protected @NotNull Material material;
    protected @NotNull String name;
    protected @Nullable List<String> lore;
    protected Color color;
    protected boolean glow = false;
    protected int tradeValue = 0;
    protected boolean vanilla;
    protected String category = "";
    protected int tier;
    private final List<CustomRecipe> recipes = new ArrayList<>();
    public List<ItemComponent> lowComponents = new ArrayList<>();
    public List<ItemComponent> normalComponents = new ArrayList<>();
    public List<ItemComponent> highComponents = new ArrayList<>();
    public Map<Class<? extends ItemComponent>, ItemComponent> components;

    protected CustomMaterial(@NotNull Key mid, @NotNull Material material, @NotNull String name) {
        this.mid = mid;
        this.material = material;
        this.name = name;
        materials.put(this.getMid(), this);
    }

    /**
     * Возвращает CustomMaterial если у предмета есть NBTTag "PRIFIX.mid", иначе null
     */
    public static @Nullable CustomMaterial getCustomMaterial(ItemStack stack) {
        if (stack == null || stack.getType().isAir() || stack.getAmount() == 0) return null;
        return getCustomMaterial(getMid(stack));
    }

    /**
     * Возвращает CustomMaterial по его mid
     */
    public static @Nullable CustomMaterial getCustomMaterial(Key mid) {
        if (mid == null) return null;
        return materials.get(mid);
    }

    /**
     * ищет CustomMaterial как по полному NamespacedKey, так и только по его значению, без namespace
     */
    public static @Nullable CustomMaterial getCustomMaterial(String mid) {
        if (mid == null) return null;
        for (Key key : materials.keySet()) {
            if (key.asString().equalsIgnoreCase(mid))
                return getCustomMaterial(key);
        }
        for (Key key : materials.keySet()) {
            if (key.value().equalsIgnoreCase(mid))
                return getCustomMaterial(key);
        }
        return null;
    }

    /**
     * Возвращает mid если у предмета есть NBTTag "PRIFIX", иначе пустая строка
     */
    public static @Nullable Key getMid(ItemStack stack) {
        if (stack == null || stack.getType().isAir() || stack.getAmount() == 0) return null;
        String mid = (new NBTItem(stack)).getString(TAGMANE);
        if (mid == null || mid.isEmpty()) return null;
        return NamespacedKey.fromString(mid);
    }

    /**
     * true, если у предмета есть NBTTag "PRIFIX"
     */
    public static boolean isCustomMaterial(ItemStack stack) {
        if (stack == null) return false;
        return materials.containsKey(getMid(stack));
    }

    public static Collection<Key> midValues() {
        return materials.keySet();
    }

    public static Collection<CustomMaterial> values() {
        return materials.values();
    }

    public static void removeAll(Plugin plugin) {
        List<Key> keys = new ArrayList<>();
        for (Key key : materials.keySet()) {
            if (key.namespace().equalsIgnoreCase(plugin.getName()))
                keys.add(key);
        }
        for (Key key : keys)
            materials.remove(key);
        LogHelper.fine("Unregistered " + keys.size() + " CustomMaterial");
    }

    public static CustomMaterialBuilder builder(@NotNull Key mid, Material material, String name) {
        return new CustomMaterialBuilder(mid, material, name);
    }

    //--------------- components

    public void buildComponents(List<RConfig> components) {
        if (components != null) {
            for (RConfig compInfo : components) {
                try {
                    addComponent(ItemComponent.builder(this).load(compInfo).build());
                } catch (InvalidConfiguration e) {
                    LogHelper.error(Component.text("[Mid = " + this.getMid() + "] ", NamedTextColor.DARK_PURPLE).append(e.getComponent().color(NamedTextColor.GOLD)));
                }
            }
        }
    }

    public void addComponent(ItemComponent component) {
        if (component != null) {
            if (component.getPriority() == ItemComponent.Priority.LOW)
                this.lowComponents.add(component);
            if (component.getPriority() == ItemComponent.Priority.NORMAL)
                this.normalComponents.add(component);
            if (component.getPriority() == ItemComponent.Priority.HIGH)
                this.highComponents.add(component);
        }
    }

    public boolean hasComponent(Class<? extends ItemComponent> aClass) {
        return getComponents().containsKey(aClass);
    }

    @SuppressWarnings("unchecked")
    public <T extends ItemComponent> T getComponent(Class<T> aClass) {
        return (T) this.getComponents().get(aClass);
    }

    public Map<Class<? extends ItemComponent>, ItemComponent> getComponents() {
        if (components == null) {
            components = new HashMap<>();
            lowComponents.forEach(itemComponent -> components.put(itemComponent.getClass(), itemComponent));
            normalComponents.forEach(itemComponent -> components.put(itemComponent.getClass(), itemComponent));
            highComponents.forEach(itemComponent -> components.put(itemComponent.getClass(), itemComponent));
        }
        return components;
    }

    //---------------- events -------------
    public void onInteract(PlayerInteractEvent event) {
        ItemComponent blockPlace = null;
        ItemComponent openGui = null;
        for (ItemComponent ic : getComponents().values()) {
            if (ic instanceof BlockPlace)
                blockPlace = ic;
            else if (ic instanceof OpenGui)
                openGui = ic;
            else
                ic.onInteract(event);
        }

        if (event.useInteractedBlock() != Event.Result.DENY && blockPlace != null) {
            blockPlace.onInteract(event);
        }
        if (event.useItemInHand() != Event.Result.DENY && openGui != null) {
            openGui.onInteract(event);
        }
    }

    public void onHeld(PlayerItemHeldEvent event) {
        for (ItemComponent comp : getComponents().values()) {
            if (event.isCancelled() && !comp.isCanselIgnore()) continue;
            comp.onHold(event);
        }
    }

    public boolean onDropItem(Player player, ItemStack stack) {
        return false;
    }

    public void onItemSpawn(ItemSpawnEvent event) {
        for (ItemComponent comp : getComponents().values()) {
            if (event.isCancelled() && !comp.isCanselIgnore()) continue;
            comp.onItemSpawn(event);
        }
    }

    public void onAttack(EntityDamageByEntityEvent event, ItemStack stack) {
        for (ItemComponent comp : getComponents().values()) {
            if (event.isCancelled() && !comp.isCanselIgnore()) continue;
            comp.onAttack(event, stack);
        }
    }

    public void onProjectileHit(ProjectileHitEvent event) {
        for (ItemComponent comp : getComponents().values()) {
            if (event.isCancelled() && !comp.isCanselIgnore()) continue;
            comp.onProjectileHit(event);
        }
    }

    public boolean onInvItemDrop(Cancellable event, Player player, Inventory toInv, ItemStack stack) {
        if (!this.canUseInventoryTypes(toInv.getType())) {
            MessageHelper.sendError(player, "Нельзя использовать этот предмет в инвентаре " + toInv.getType());
            return true;
        }
        return false;
    }

    public boolean onInvItemPickup(Cancellable event, Player player, Inventory fromInv, ItemStack stack) {
        return false;
    }

    public void onDefense(EntityDamageByEntityEvent event, ItemStack stack) {
        for (ItemComponent comp : getComponents().values()) {
            if (event.isCancelled() && !comp.isCanselIgnore()) continue;
            comp.onDefense(event, stack);
        }
    }

    public void onDurabilityChange(PlayerItemDamageEvent event) {
        for (ItemComponent comp : getComponents().values()) {
            if (event.isCancelled() && !comp.isCanselIgnore()) continue;
            comp.onDurabilityChange(event);
        }
    }

    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        for (ItemComponent comp : getComponents().values()) {
            if (event.isCancelled() && !comp.isCanselIgnore()) continue;
            comp.onPlayerInteractEntity(event);
        }
    }

    public ItemChangeResult onDurabilityDeath(PlayerDeathEvent event, ItemStack stack) {
        ItemChangeResult result = null;
        for (ItemComponent comp : getComponents().values()) {
            if (event.isCancelled() && !comp.isCanselIgnore()) continue;
            result = comp.onDurabilityDeath(event, result, stack);
        }
        return result;
    }

    public void onInventoryOpen(InventoryOpenEvent event, ItemStack stack) {
        for (ItemComponent comp : getComponents().values()) {
            if (event.isCancelled() && !comp.isCanselIgnore()) continue;
            comp.onInventoryOpen(event, stack);
        }
    }

    public boolean canUseInventoryTypes(InventoryType invType) {
        if (invType == null) return false;
        switch (invType) {
            case CHEST:
            case CRAFTING:
            case DROPPER:
            case ENDER_CHEST:
            case HOPPER:
            case PLAYER:
            case SHULKER_BOX:
            case WORKBENCH:
                return true;
            default:
                return false;
        }
    }

    public void onInteractEntity(PlayerInteractEntityEvent event) {
    }

    public void onBlockPlaced(BlockPlaceEvent event) {
    }

    /* Можно отменить использование в крафтах */
    public void onCraftItem(CraftItemEvent event) {
    }

    public void onPickupItem(EntityPickupItemEvent event) {
    }

    public void onPlayerDeath(EntityDeathEvent event, ItemStack stack) {
    }

    // ------------------ actions

    public ItemStack spawn() {
        return spawn(1);
    }

    public ItemStack spawn(int amount) {
        ItemStackBuilder builder = ItemStackBuilder.of(this.material);
        builder.addTag(TAGMANE, this.getMid().asString());
        builder.name(Component.text(TextUtils.translateHexColor(this.getName()), NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        builder.addLore(Component.text(this.category, NamedTextColor.DARK_PURPLE).decoration(TextDecoration.ITALIC, false));
        if (this.getLore() != null)
            this.getLore().forEach(s -> builder.addLore(Component.text(TextUtils.translateHexColor(s), NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)));

        getComponents().forEach((aClass, comp) -> comp.onBuildItemStack(builder));

        builder.setAmount(amount);

        ItemStack stack = builder.build();
        ItemMeta meta = stack.getItemMeta();
        if (color != null) {
            if (meta instanceof PotionMeta) ((PotionMeta) meta).setColor(color);
            if (meta instanceof LeatherArmorMeta) ((LeatherArmorMeta) meta).setColor(color);
        }
        if (glow) meta.addEnchant(Enchantment.LURE, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stack.setItemMeta(meta);
        return stack;
    }

    public void addRecipe(CustomRecipe recipe) {
        recipes.add(recipe);
    }

    public List<CustomRecipe> getRecipes() {
        return recipes;
    }

    public boolean isCraftable() {
        return !recipes.isEmpty();
    }

    @Override
    public int hashCode() {
        return getMid().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CustomMaterial)) return false;
        return this.getMid().equals(((CustomMaterial) o).getMid());
    }

}