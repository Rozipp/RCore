package ua.rozipp.core.guiinventory;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.gui.RGui;
import ua.rozipp.core.gui.RGuiImp;
import ua.rozipp.core.gui.RGuiSlot;
import ua.rozipp.core.gui.buttons.OpenInventoryRButton;
import ua.rozipp.core.items.CustomMaterial;
import ua.rozipp.core.items.ItemStackBuilder;
import ua.rozipp.core.recipes.CustomRecipe;
import ua.rozipp.core.recipes.ShapedCustomRecipe;
import ua.rozipp.core.recipes.ShapelessCustomRecipe;

public class CraftingHelpRecipe extends RGuiImp {

    public static final int INV_ROW_COUNT = 9;
    public static final int START_OFFSET = INV_ROW_COUNT + 3;
    private final CustomMaterial cMat;

    public CraftingHelpRecipe(Object arg) {
        this((arg instanceof CustomMaterial) ?
                (CustomMaterial) arg :
                (arg instanceof String) ?
                        CustomMaterial.getCustomMaterial((String) arg) :
                        null);
    }

    public CraftingHelpRecipe(CustomMaterial cMat) {
        super(6,
                Component.translatable("loreGui_recipes_guiHeading").args(Component.text((cMat == null) ? "" : cMat.getName())));

        this.cMat = cMat;
    }

    public RGuiSlot getIngredItem(String umid) {
        CustomMaterial cmat = CustomMaterial.getCustomMaterial(umid);
        if (cmat == null) {
            return new RGuiSlot().setItemStack(ItemStackBuilder.of(Material.getMaterial(umid))
                    .addLore("Vanilla Item")
                    .build());
        } else {
            if (cmat.isCraftable()) {
                return new OpenInventoryRButton(ItemStackBuilder.of(cmat.spawn(1))
                        .addLore(Component.translatable("loreGui_recipes_clickForRecipe"))
                ).setRGuiFromClass(CraftingHelpRecipe.class, cmat);
            } else
                return new RGuiSlot().setItemStack(ItemStackBuilder.of(cmat.spawn(1))
                        .addLore(Component.translatable("loreGui_recipes_notCraftable"))
                        .build());
        }
    }

    public void buildCraftTableBorder() {
        int offset = 2;
        ItemStack stack = ItemStackBuilder.of(Material.CRAFTING_TABLE)
                .setName("Craft Table Border").build();
        for (int y = 0; y <= 4; y++) {
            for (int x = 0; x <= 4; x++) {
                if (x == 0 || x == 4 || y == 0 || y == 4) {
                    this.setSlot(offset + (y * INV_ROW_COUNT) + x,
                            new RGuiSlot().setItemStack(stack.clone()));
                }
            }
        }
    }

    public void buildInfoBar(CustomRecipe recipe) {
        int offset = 0;
        ItemStack stack = (recipe instanceof ShapedCustomRecipe) ? //
                ItemStackBuilder.of(Material.HOPPER).name(Component.translatable("loreGui_recipes_shaped")).build() :
                ItemStackBuilder.of(Material.COAL).name(Component.translatable("loreGui_recipes_unshaped")).build();
        offset += INV_ROW_COUNT;
        this.setSlot(offset, new RGuiSlot(stack.clone()));
    }

    @Override
    public void onFirstDraw() {
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
                    if (ingred != null) this.setSlot(i + offset, getIngredItem(ingred.umid));
                }
                offset += INV_ROW_COUNT;
            }
        } else {
            int x = 0;
            int offset = START_OFFSET;
            ShapelessCustomRecipe shapelessRecipe = (ShapelessCustomRecipe) recipe;
            for (ShapelessCustomRecipe.ShapelessIngredient ingred : shapelessRecipe.getIngredients().values()) {
                if (ingred != null) {
                    for (int i = 0; i < ingred.count; i++) {
                        this.setSlot(x + offset, getIngredItem(ingred.umid));
                        x++;
                        if (x >= 3) {
                            x = 0;
                            offset += INV_ROW_COUNT;
                        }
                    }
                }
            }
        }
        buildCraftTableBorder();
        buildInfoBar(recipe);
    }
}
