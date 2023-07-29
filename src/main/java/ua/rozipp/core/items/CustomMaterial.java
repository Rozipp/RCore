package ua.rozipp.core.items;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
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
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.config.ConfigMaterialCategory;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.recipes.CustomRecipe;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class CustomMaterial {
    private static final String TAGMANE = "RMID";
    private static final Map<Key, CustomMaterial> materials = new HashMap<>();

    protected Key mid;
    protected Material material;
    protected String name;
    protected List<String> lore;
    protected boolean shiny = false;
    protected int tradeValue = 0;
    protected boolean vanilla;
    protected String category = "";
    protected int tier;
    private final List<CustomRecipe> recipes = new ArrayList<>();

    protected CustomMaterial(@NotNull Key mid, @NotNull Material material, String name) {
        this.mid = mid;
        this.material = material;
        this.name = name;

        materials.put(this.getMid(), this);
        ConfigMaterialCategory.addMaterial(this);
    }

    /**
     * Возвращает билдер для создания CustomMaterial
     */
    public static CustomMaterialBuilder builder() {
        return new CustomMaterialBuilder();
    }

    /**
     * Возвращает CustomMaterial если у предмета есть NBTTag "PRIFIX.mid", иначе null
     */
    public static CustomMaterial getCustomMaterial(ItemStack stack) {
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
            if (key.asString().equalsIgnoreCase(mid) || key.value().equalsIgnoreCase(mid))
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
        if (mid == null|| mid.isEmpty()) return null;
        return NamespacedKey.fromString(mid);
    }

    /**
     * true, если у предмета есть NBTTag "PRIFIX"
     */
    public static boolean isCustomMaterial(ItemStack stack) {
        if (stack == null) return false;
        return materials.containsKey(getMid(stack));
    }

    public static Collection<String> midValues() {
        return materials.keySet().stream().map(Key::asString).collect(Collectors.toSet());
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

    public static void load(Plugin plugin, RConfig cfg) throws InvalidConfiguration {
        if (cfg == null) return;
        List<RConfig> configMaterials = cfg.getRConfigList("materials");
        if (configMaterials == null) return;
        int count = 0;
        for (RConfig args : configMaterials) {
            CustomMaterial mat = null;
            try {
                if (args.contains("components"))
                    mat = new ComponentedCustomMaterialBuilder().loadConfig(args).build(plugin);
                else
                    mat = new CustomMaterialBuilder().loadConfig(args).build(plugin);
                count++;
            } catch (InvalidConfiguration e) {
                LogHelper.warning(e.getMessage()); //TODO Дописать где возникла ошибка
            }
        }
        LogHelper.info("Loaded " + count + " Materials.");
    }

    //TODO переделать на ItemStackBuilder
    public void applyAttributes(ItemStackBuilder builder) {
    }

    /**
     * Можно ли использовать предмет с этим типом инвентаря
     */
    public boolean canUseInventoryTypes(InventoryType inv) {
        return true;
    }

    /* Events for this Material */
    public void onInteract(PlayerInteractEvent event) {
    }

    public void onInteractEntity(PlayerInteractEntityEvent event) {
    }

    public void onBlockPlaced(BlockPlaceEvent event) {
    }

    public void onHeld(PlayerItemHeldEvent event) {
    }

    public void onAttack(EntityDamageByEntityEvent event, ItemStack stack) {
    }

    public void onItemSpawn(ItemSpawnEvent event) {
    }

    /* Можно отменить использование в крафтах */
    public void onCraftItem(CraftItemEvent event) {
    }

    public void onPickupItem(EntityPickupItemEvent event) {
    }

    public boolean onDropItem(Player player, ItemStack stack) {
        return true;
    }

    /**
     * Предмет поднять из инвентаря
     */
    public boolean onInvItemPickup(Cancellable event, Player player, Inventory fromInv, ItemStack stack) {
        return true;
    }

    /**
     * Предмет брошен в инвентарь. Все изменения скопируй в onInvItemPickup()
     */
    public boolean onInvItemDrop(Cancellable event, Player player, Inventory toInv, ItemStack stack) {
        return true;
    }

    public void onPlayerDeath(EntityDeathEvent event, ItemStack stack) {
    }

    public void onDefense(EntityDamageByEntityEvent event, ItemStack stack) {
    }

    public void onDurabilityChange(PlayerItemDamageEvent event) {
        event.setCancelled(true);
    }

    public void onInventoryOpen(InventoryOpenEvent event, ItemStack stack) {
    }

    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
    }

    public void onRangedAttack(EntityDamageByEntityEvent event, ItemStack inHand) {
    }

    public ItemChangeResult onDurabilityDeath(PlayerDeathEvent event, ItemStack stack) {
        return null;
    }

    public ItemStack spawn() {
        return spawn(1);
    }

    public ItemStack spawn(int amount) {
        ItemStackBuilder builder = new ItemStackBuilder(this.material);
        builder.addTag(TAGMANE, this.getMid().asString());
        builder.setName(this.getName());
//        builder.addLore(ChatColor.ITALIC + this.category);
        if (this.getLore() != null && !this.getLore().isEmpty()) builder.setLore(this.getLore());

        this.applyAttributes(builder);
        builder.setAmount(amount);

        ItemStack stack = builder.build();
        ItemMeta meta = stack.getItemMeta();
        if (shiny) meta.addEnchant(Enchantment.LURE, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stack.setItemMeta(meta);
        return stack;
    }

    public void addRecipe(CustomRecipe recipe){
        recipes.add(recipe);
    }

    public List<CustomRecipe> getRecipes(){
        return recipes;
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

    public boolean isCraftable() {
        return !recipes.isEmpty();
    }
}