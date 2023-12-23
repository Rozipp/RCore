package ua.rozipp.core.guiinventory;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ua.rozipp.core.exception.GuiException;
import ua.rozipp.core.gui.GuiHelper;
import ua.rozipp.core.gui.InventoryGUI;
import ua.rozipp.core.gui.ItemButton;
import ua.rozipp.core.gui.action.OpenInventoryButton;
import ua.rozipp.core.items.CustomMaterial;
import ua.rozipp.core.items.ItemHelper;
import ua.rozipp.core.items.ItemStackBuilder;
import ua.rozipp.core.recipes.CustomRecipe;
import ua.rozipp.core.recipes.ShapedCustomRecipe;
import ua.rozipp.core.recipes.ShapelessCustomRecipe;

public class CraftingHelpRecipe extends InventoryGUI {

    public static final int START_OFFSET = GuiHelper.INV_ROW_COUNT + 3;

    public CraftingHelpRecipe(Plugin plugin, @NotNull CustomMaterial cMat) throws GuiException {
        super(plugin, Bukkit.createInventory(null, 6 * 9,
                Component.translatable("loreGui_recipes_guiHeading").args(Component.text(cMat.getName()))));
        if (!cMat.isCraftable())
            throw new GuiException("CustomMaterial " + cMat.getMid() + " have't ingredients");

        CustomRecipe recipe = cMat.getRecipes().get(0);
        if (recipe instanceof ShapedCustomRecipe) {
            ShapedCustomRecipe shapedRecipe = (ShapedCustomRecipe) recipe;
            int offset = START_OFFSET;
            for (String line : shapedRecipe.getShape()) {
                for (int i = 0; i < line.toCharArray().length; i++) {
                    ShapedCustomRecipe.ShapedIngredient ingred = null;
                    for (ShapedCustomRecipe.ShapedIngredient in : shapedRecipe.getIngredients().values()) {
                        if (in.letter.equalsIgnoreCase(String.valueOf(line.toCharArray()[i]))) {
                            ingred = in;
                            break;
                        }
                    }
                    if (ingred != null) this.addButton(i + offset, getIngredItem(ingred.umid));
                }
                offset += GuiHelper.INV_ROW_COUNT;
            }
        } else {
            int x = 0;
            int offset = START_OFFSET;
            ShapelessCustomRecipe shapelessRecipe = (ShapelessCustomRecipe) recipe;
            for (ShapelessCustomRecipe.ShapelessIngredient ingred : shapelessRecipe.getIngredients().values()) {
                if (ingred != null) {
                    for (int i = 0; i < ingred.count; i++) {
                        this.addButton(x + offset, getIngredItem(ingred.umid));
                        x++;
                        if (x >= 3) {
                            x = 0;
                            offset += GuiHelper.INV_ROW_COUNT;
                        }
                    }
                }
            }
        }
        buildCraftTableBorder();
        buildInfoBar(recipe);
    }

    public ItemButton getIngredItem(String umid) throws GuiException {
        CustomMaterial cmat = CustomMaterial.getCustomMaterial(umid);
        if (cmat == null) {
            return ItemButton.create(new ItemStackBuilder(Material.getMaterial(umid)).addLore("Vanilla Item").build());
        } else {
            if (cmat.isCraftable()) {
                return new OpenInventoryButton(cmat.spawn(1),
                        new CraftingHelpRecipe(getPlugin(), cmat),
                        Component.translatable("loreGui_recipes_clickForRecipe"));
            } else
                return ItemButton.create(new ItemStackBuilder(cmat.spawn(1))
                        .addLore(Component.translatable("loreGui_recipes_notCraftable"))
                        .build());
        }
    }

    public void buildCraftTableBorder() {
        int offset = 2;
        ItemStackBuilder builder = new ItemStackBuilder(Material.CRAFTING_TABLE)
                .setName("Craft Table Border");
        for (int y = 0; y <= 4; y++) {
            for (int x = 0; x <= 4; x++) {
                if (x == 0 || x == 4 || y == 0 || y == 4) {
                    this.addButton(offset + (y * GuiHelper.INV_ROW_COUNT) + x, ItemButton.create(builder.build()));
                }
            }
        }
    }

    public void buildInfoBar(CustomRecipe recipe) {
        int offset = 0;
        ItemStack stack = (recipe instanceof ShapedCustomRecipe) ? //
                new ItemStackBuilder(Material.HOPPER).name(Component.translatable("loreGui_recipes_shaped")).build() :
                new ItemStackBuilder(Material.COAL).name(Component.translatable("loreGui_recipes_unshaped")).build();
        offset += GuiHelper.INV_ROW_COUNT;
        this.addButton(offset, ItemButton.create(stack));
    }

}
