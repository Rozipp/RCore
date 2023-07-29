package ua.rozipp.core.guiinventory;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.exception.GuiException;
import ua.rozipp.core.gui.GuiHelper;
import ua.rozipp.core.gui.GuiInventory;
import ua.rozipp.core.gui.GuiItemBuilder;
import ua.rozipp.core.items.CustomMaterial;
import ua.rozipp.core.items.ItemHelper;
import ua.rozipp.core.items.ItemStackBuilder;
import ua.rozipp.core.recipes.CustomRecipe;
import ua.rozipp.core.recipes.ShapedCustomRecipe;
import ua.rozipp.core.recipes.ShapelessCustomRecipe;

public class CraftingHelpRecipe extends GuiInventory {

    public static final int START_OFFSET = GuiHelper.INV_ROW_COUNT + 3;

    public CraftingHelpRecipe(Player player, String mid) throws GuiException {
        super(player, null, mid);
        CustomMaterial cMat = CustomMaterial.getCustomMaterial(mid);
        if (cMat == null || !cMat.isCraftable())
            throw new GuiException("CustomMaterial " + mid + " have't ingredients");

//		for (CustomRecipe recipe : cMat.getRecipes()){
        CustomRecipe recipe = cMat.getRecipes().get(0);
        this.setTitle(Component.translatable("loreGui_recipes_guiHeading").args(Component.text(cMat.getName())));
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
                    if (ingred != null) this.addGuiItem(i + offset, getIngredItem(ingred.umid));
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
                        this.addGuiItem(x + offset, getIngredItem(ingred.umid));
                        x++;
                        if (x >= 3) {
                            x = 0;
                            offset += GuiHelper.INV_ROW_COUNT;
                        }
                    }
                }
            }
        }
        buildCraftTableBorder(this);
        buildInfoBar(recipe, this);
    }

    public ItemStack getIngredItem(String umid) {
        GuiItemBuilder builder = GuiItemBuilder.guiItemBuilder(ItemHelper.createItemStack(umid, 1));
        CustomMaterial cmat = CustomMaterial.getCustomMaterial(umid);
        if (cmat == null) {
            builder.setName(umid).addLore("Vanilla Item").build();
        } else {
            builder.setName(cmat.getName());
            if (cmat.isCraftable()) {
                builder.setOpenInventory(CraftingHelpRecipe.class, cmat.getMid().asString())
                        .addLore(Component.translatable("loreGui_recipes_clickForRecipe"));
            } else
                builder.addLore(Component.translatable("loreGui_recipes_notCraftable"));
        }
        return builder.build();
    }

    public void buildCraftTableBorder(GuiInventory recInv) {
        int offset = 2;
        GuiItemBuilder builder = GuiItemBuilder.guiItemBuilder(Material.LEGACY_WORKBENCH)
                .setName("Craft Table Border");
        for (int y = 0; y <= 4; y++) {
            for (int x = 0; x <= 4; x++) {
                if (x == 0 || x == 4 || y == 0 || y == 4) {
                    recInv.addGuiItem(offset + (y * GuiHelper.INV_ROW_COUNT) + x, builder.build());
                }
            }
        }
    }

    public void buildInfoBar(CustomRecipe recipe, GuiInventory recInv) {
        int offset = 0;
        ItemStack stack;

        stack = (recipe instanceof ShapedCustomRecipe) ? //
                GuiItemBuilder.guiItemBuilder(Material.HOPPER).name(Component.translatable("loreGui_recipes_shaped")).build() :
                GuiItemBuilder.guiItemBuilder(Material.COAL).name(Component.translatable("loreGui_recipes_unshaped")).build();
        offset += GuiHelper.INV_ROW_COUNT;
        recInv.addGuiItem(offset, stack);
    }

    public static ItemStack getInfoBookForItem(Key mid) {
        CustomMaterial cMat = CustomMaterial.getCustomMaterial(mid);
        if (!cMat.isCraftable()) return null;

        ItemStack stack = cMat.spawn();

        ItemStackBuilder builder = new ItemStackBuilder(stack);
        builder.addLore(Component.translatable("tutorial_clickForRecipe", Style.style(NamedTextColor.GOLD, TextDecoration.BOLD)));
        return builder.build();
    }

}
