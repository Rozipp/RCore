package ua.rozipp.core.recipes;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.ItemHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapedCustomRecipe extends CustomRecipe {

    @Getter
    protected Map<Character, ShapedIngredient> ingredients;
    @Getter
    protected String[] shape;

    public ShapedCustomRecipe(Plugin plugin, String umid) {
        super(plugin, umid);
    }

    @Override
    public Recipe buildRecipes(Plugin plugin) {
        /* Loads in materials from configuration file. */
        ItemStack resultStack = ItemHelper.createItemStack(this.umid, this.craftAmount);

        ShapedRecipe recipe = new ShapedRecipe(key, resultStack);
        recipe.shape(this.shape[0], this.shape[1], this.shape[2]);
        for (ShapedIngredient ingred : this.ingredients.values()) {
            recipe.setIngredient(ingred.letter.charAt(0), ItemHelper.createItemStack(ingred.umid, 1));
        }
        return recipe;
    }

    @Override
    protected String getKey() {
        StringBuilder sb = new StringBuilder();
        for (String s : shape) {
            for (int i = 0; i < 3; i++) {
                char c = s.charAt(i);
                ShapedIngredient ingredient = ingredients.get(c);
                if (ingredient == null) sb.append("AIR");
                else
                    sb.append(ingredient.umid);
                sb.append(".");
            }
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    public static class ShapedCustomRecipeBuilder extends CustomRecipeBuilder {

        protected Map<Character, ShapedIngredient> ingredients;
        protected String[] shape;

        @Override
        public CustomRecipeBuilder loadConfig(RConfig b) throws InvalidConfiguration {
            super.loadConfig(b);
            List<RConfig> configIngredients = b.getRConfigList("ingredients");
            if (configIngredients != null) {
                ingredients = new HashMap<>();

                for (RConfig ingred : configIngredients) {
                    ShapedIngredient ingredient = new ShapedIngredient();
                    ingredient.umid = ingred.getString("umid", null, "");
                    ingredient.letter = ingred.getString("letter", "", "");
                    ingredients.put(ingredient.letter.charAt(0), ingredient);
                }
            }

            List<String> configShape = b.getList("shape", String.class, new ArrayList<>(), null);
            if (!configShape.isEmpty()) {
                shape = new String[configShape.size()];
                int i = 0;
                for (String obj : configShape) {
                    shape[i] = obj;
                    i++;
                }
            }
            return this;
        }

        public ShapedCustomRecipeBuilder setValues(ShapedCustomRecipe recipe) {
            super.setValues(recipe);
            recipe.ingredients = ingredients;
            recipe.shape = shape;
            return this;
        }

        @Override
        public CustomRecipe build(Plugin plugin) {
            ShapedCustomRecipe cmat = new ShapedCustomRecipe(plugin, umid);
            setValues(cmat);
            return cmat;
        }

    }

    public static class ShapedIngredient {
        public String umid;
        public String letter;
    }
}
