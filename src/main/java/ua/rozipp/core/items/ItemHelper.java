package ua.rozipp.core.items;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.SkullMeta;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;

import java.util.*;

public class ItemHelper {

    public static ItemStack createItemStack(String umid, int amount) {
        if (umid != null && !umid.isEmpty()) {
            if (umid.contains(":")) {
                NamespacedKey mid = NamespacedKey.fromString(umid);
                CustomMaterial cm = CustomMaterial.getCustomMaterial(mid);
                if (cm != null) return cm.spawn(amount);
            }
            CustomMaterial cm = CustomMaterial.getCustomMaterial(umid);
            if (cm != null) return cm.spawn(amount);
            Material material = Material.getMaterial(umid);
            if (material != null) return new ItemStack(material, amount);
        }
        LogHelper.error("createItemStack() \"" + umid + "\" is not found material");
        return new ItemStack(Material.AIR);
    }

    public static ItemStack createItemStack(Key id, int amount) {
        if (id.namespace().equals(Key.MINECRAFT_NAMESPACE)) {
            Material material = Material.getMaterial(id.value());
            if (material != null) return new ItemStack(material, amount);
        } else {
            CustomMaterial cMat = CustomMaterial.getCustomMaterial(id);
            if (cMat != null) return cMat.spawn(amount);
        }
        return new ItemStack(Material.AIR);
    }

    @SuppressWarnings("deprecation")
    public static void setDurability(ItemStack stack, double percent) {
        short demage = (short) ((1 - percent) * stack.getType().getMaxDurability());
        stack.setDurability(demage);
    }

    @SuppressWarnings("deprecation")
    public static short getDurability(ItemStack stack) {
        return stack.getDurability();
    }

    @SuppressWarnings("deprecation")
    public static ItemStack spawnPlayerHead(String playerName, String itemDisplayName) {
        ItemStack skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(playerName);
        meta.setDisplayName(itemDisplayName);
        skull.setItemMeta(meta);
        return skull;
    }

    public static boolean removeItemFromPlayer(Player player, Material mat, int amount) {
        ItemStack m = new ItemStack(mat, amount);
        if (player.getInventory().contains(mat)) {
            player.getInventory().removeItem(m);
            player.updateInventory();
            return true;
        }
        return false;
    }

    /**
     * Возвращает информацию из NBTTag'а под ключем key
     */
    public static String getNBTTag(ItemStack stack, String key) {
        return getNBTTag(new NBTItem(stack), key);
    }

    /**
     * Возвращает информацию из NBTTag'а под ключем key
     */
    private static String getNBTTag(NBTItem nbti, String key) {
        NBTCompound compound = nbti;
        String k = key;
        if (key.contains(".")) {
            String[] splits = key.split("\\.");
            for (int i = 0; i < splits.length - 1; i++) {
                compound = compound.getCompound(splits[i]);
                if (compound == null) return "";
            }
            k = splits[splits.length - 1];
        }
        String r = compound.getString(k);
        if (r == null)
            return "";
        else
            return r.replace("\"", "");
    }

    /**
     * Возвращает все NBTTag'ы предмета
     */
    public static String getAllProperty(ItemStack stack) {
        if (stack == null || stack.getType().isAir()) return "AIR";
        NBTItem nbti = new NBTItem(stack);
        return nbti.toString();
    }

    /**
     * Добавляет в NBTTag предмета stack строку value под ключом key
     *
     * @return - создает новый предмет скопированный из начального stack
     * @key - может быть задан как путь к NBT-тегу разделенный точкой (.). Например display.name
     */
    public static ItemStack setNBTTag(ItemStack stack, String key, String value) {
        return setNBTTag(new NBTItem(stack), key, value).getItem();
    }

    /**
     * Добавляет в NBTTag строку value под ключом key
     * key может быть задан как путь к NBT-тегу разделенный точкой (.). Например display.name
     *
     * @nbti - изменяется при работе метода
     */
    public static NBTItem setNBTTag(NBTItem nbti, String key, String value) {
        NBTCompound compound = nbti;
        String k = key;
        if (key.contains(".")) {
            String[] splits = key.split("\\.");
            for (int i = 0; i < splits.length - 1; i++) {
                compound = compound.getOrCreateCompound(splits[i]);
            }
            k = splits[splits.length - 1];
        }
        compound.setString(k, value);
        return nbti;
    }


    /**
     * Сравнивает ItemStack с umid предмета umid может быть:
     * 1) mid CustomMaterial
     * 2) название материала (например Material.AIR)
     */
    public static boolean itemStackEqualsUmid(ItemStack stack, String umid) {
        Key mid = CustomMaterial.getMid(stack);
        if (mid != null) return umid.equalsIgnoreCase(mid.asString());
        else
            try {
                Material vanilaMat = Material.valueOf(umid.toUpperCase());
                return vanilaMat.equals(stack.getType());
            } catch (IllegalArgumentException e1) {
                LogHelper.warning("itemStackEqualsUmid() \"" + umid + "\" is not material");
                return false;
            }
    }

    /**
     * Ложить предмет в заданный слот. Если тот не пустой выбрасывает содержимое слота на пол и ложить предмет.
     * Moves an item stack off of this slot by trying to re-add it to the inventory, if it fails, then we drop it on the ground.
     */
    public static void putItemToSlot(Player player, Inventory inv, int slot, ItemStack stack) {
        ItemStack oldStack = inv.getItem(slot);
        inv.setItem(slot, stack);

        if (oldStack != null) {
            if (oldStack.equals(stack)) return;
            HashMap<Integer, ItemStack> leftovers = inv.addItem(oldStack);
            for (ItemStack s : leftovers.values()) {
                player.getWorld().dropItem(player.getLocation(), s);
            }
        }
    }

    public static void removeCustomItemFromInventory(Inventory inv, String mat, Integer amount) {
        for (int i = inv.getSize() - 1; i >= 0; i--) {
            ItemStack stack = inv.getItem(i);
            if (stack == null) continue;
            if (itemStackEqualsUmid(stack, mat)) {
                Integer count = stack.getAmount();
                stack.setAmount(count - amount);
                inv.setItem(i, stack);
            }
        }
    }

    public static boolean isPresent(final ItemStack item) {
        return item != null && item.getType() != Material.AIR;
    }

    public static void removeRecipes(RConfig cfg, HashSet<Material> removedRecipies) throws InvalidConfiguration {
        List<RConfig> configMaterials = cfg.getRConfigList("removed_recipes");
        for (RConfig b : configMaterials) {
            try {
                Material material = Material.getMaterial(b.getString("material", null, "removeRecipes ERROR. Material not found"));
                removedRecipies.add(material);
            } catch (InvalidConfiguration e) {
                LogHelper.error(e.getMessage());
            }

        }

        // Idk why you change scope, but why not
        List<Recipe> backup = new ArrayList<>();
        Iterator<Recipe> a = Bukkit.getServer().recipeIterator();
        while (a.hasNext()) {
            Recipe recipe = a.next();
            ItemStack result = recipe.getResult();
            Material mat = result.getType();
            if (!removedRecipies.contains(mat)) {
                backup.add(recipe);
            }
        }

        Bukkit.getServer().clearRecipes();
        for (Recipe r : backup) {
            Bukkit.getServer().addRecipe(r);
        }
    }

    public static Key getKey(ItemStack item) {
        CustomMaterial customMaterial = CustomMaterial.getCustomMaterial(item);
        return (customMaterial != null) ? customMaterial.getMid() : item.getType().getKey();
    }

    public static Material getMaterial(Key key) {
        if (key.namespace().equals(NamespacedKey.MINECRAFT_NAMESPACE))
            return Material.getMaterial(key.value().toUpperCase());
        CustomMaterial customMaterial = CustomMaterial.getCustomMaterial(key);
        return (customMaterial != null) ? customMaterial.getMaterial() : Material.WHITE_WOOL;
    }
}
