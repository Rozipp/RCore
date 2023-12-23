package ua.rozipp.core.recipes;

import lombok.Getter;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.CustomMaterial;
import ua.rozipp.core.items.ItemHelper;

import java.util.*;

public class CustomRecipe {

    private static final Map<NamespacedKey, CustomRecipe> recipes = new HashMap<>();

    @Getter
    protected String umid;
    protected NamespacedKey key;
    protected Plugin plugin;
    protected int craftAmount = 1;

    public CustomRecipe(Plugin plugin, String umid) {
        this.plugin = plugin;
        this.umid = umid;
    }

    public static void loadAll(Plugin plugin, RConfig cfg) throws InvalidConfiguration {
        if (cfg == null) return;
        List<RConfig> configRecipes = cfg.getRConfigList("recipes");
        if (configRecipes == null) return;
        int count = 0;
        for (RConfig args : configRecipes) {
            CustomRecipe recipe = null;
            try {
                if (args.contains("shape"))
                    recipe = new ShapedCustomRecipe.ShapedCustomRecipeBuilder().loadConfig(args).build(plugin);
                else
                    recipe = new ShapelessCustomRecipe.ShapelessCustomRecipeBuilder().loadConfig(args).build(plugin);
                count++;
                NamespacedKey key = getNamespacedKey(plugin, recipe.umid, recipe.getKey());
                recipe.key = key;
                if (recipes.containsKey(key)) {
                    recipes.remove(key);
                    Bukkit.getServer().removeRecipe(key);
                }
                recipes.put(recipe.key, recipe);
                CustomMaterial cmat = CustomMaterial.getCustomMaterial(recipe.umid);
                if (cmat != null) cmat.addRecipe(recipe);
                Bukkit.getServer().addRecipe(recipe.buildRecipes(plugin));
            } catch (InvalidConfiguration e) {
                LogHelper.warning(e.getMessage()); //TODO Дописать где возникла ошибка
            }
        }
        LogHelper.info("Loaded " + count + " Recipes.");
    }

    public static void removeAll(Plugin plugin) {
        List<NamespacedKey> keys = new ArrayList<>();
        for (NamespacedKey key : recipes.keySet()) {
            if (key.namespace().equalsIgnoreCase(plugin.getName()))
                keys.add(key);
        }
        for (NamespacedKey key : keys) {
            Bukkit.getServer().removeRecipe(key);
            recipes.remove(key);
        }
        LogHelper.fine("Unregistered " + keys.size() + " CustomRecipes");
    }

    protected static @NotNull NamespacedKey getNamespacedKey(Plugin plugin, String umid, String recipesKey) {
        CustomMaterial cmat = CustomMaterial.getCustomMaterial(umid);
        if (cmat != null) {
            Key key = cmat.getMid();
            return new NamespacedKey(plugin, key.value() + "-" + recipesKey);
        }
        Material material = Material.getMaterial(umid);
        if (material != null)
            return new NamespacedKey(plugin, material.name().toLowerCase(Locale.ROOT) + "-" + recipesKey);
        return new NamespacedKey(plugin, recipesKey);
    }

    public static Collection<CustomRecipe> value() {
        return recipes.values();
    }

    public Recipe buildRecipes(Plugin plugin) {
        return () -> new ItemStack(Material.AIR);
    }

    protected String getKey() {
        return umid;
    }

    public static class CustomRecipeBuilder {

        protected String umid;
        protected int craftAmount = 1;

        public CustomRecipeBuilder loadConfig(RConfig b) throws InvalidConfiguration {
            umid = b.getString("umid_result", null, "Material id_result is Empty");
            if (ItemHelper.createItemStack(umid, 1).getType().isAir())
                throw new InvalidConfiguration("Cannot create resipes for umid_result " + umid);
            craftAmount = b.getInt("amount", 1, "");
            return this;
        }

        public void setValues(CustomRecipe recipe) {
            recipe.craftAmount = craftAmount;
        }

        public CustomRecipe build(Plugin plugin) {
            CustomRecipe cmat = new CustomRecipe(plugin, umid);
            setValues(cmat);
            return cmat;
        }
    }
}
