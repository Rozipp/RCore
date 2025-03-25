package ua.rozipp.core.recipes;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.ItemHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapelessCustomRecipe extends CustomRecipe {

    @Getter
    protected Map<String, ShapelessIngredient> ingredients;

    public ShapelessCustomRecipe(Plugin plugin, String umid) {
        super(plugin, umid);
    }

    @Override
    public Recipe buildRecipes(Plugin plugin) {
        /* Loads in materials from configuration file. */
        ItemStack resultStack = ItemHelper.createItemStack(umid, craftAmount);
//        if (resultStack.getType().isAir())
        ShapelessRecipe recipe = new ShapelessRecipe(key, resultStack);
        for (ShapelessIngredient ingred : this.ingredients.values()) {
            recipe.addIngredient(ingred.count, ItemHelper.createItemStack(ingred.umid, 1));
        }
        return recipe;
    }

    public static class ShapelessCustomRecipeBuilder extends CustomRecipeBuilder {

        protected HashMap<String, ShapelessIngredient> ingredients;

        @Override
        public CustomRecipeBuilder loadConfig(RConfig b) throws InvalidConfiguration {
            super.loadConfig(b);
            List<RConfig> configIngredients = b.getRConfigList("ingredients");
            if (configIngredients != null) {
                ingredients = new HashMap<>();

                for (RConfig ingred : configIngredients) {
                    ShapelessIngredient ingredient = new ShapelessIngredient();
                    ingredient.umid = ingred.getString("umid", null, "");
                    ingredient.count = ingred.getInt("count", 1, "");
                    ingredients.put(ingredient.umid, ingredient);
                }
            }

            return this;
        }

        public void setValues(ShapelessCustomRecipe recipe) {
            super.setValues(recipe);
            recipe.ingredients = ingredients;
        }

        @Override
        public CustomRecipe build(Plugin plugin) {
            ShapelessCustomRecipe cmat = new ShapelessCustomRecipe(plugin, umid);
            setValues(cmat);
            return cmat;
        }

    }

    public static class ShapelessIngredient {
        public String umid;
        public Integer count;
    }
}
