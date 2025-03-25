package ua.rozipp.core.guiinventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.gui.RGuiOpenable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PersonalCrafter extends RGuiOpenable {

    public PersonalCrafter(Object arg) {
        super(6, "Personal Crafter");
    }

    @Override
    public void onFirstDraw() {
        super.onFirstDraw();
        closeAllSlots();
        openSlots(1, 2, 4, 5);
        openSlot(33);
    }

    @Override
    public void onUpdate() {
        Recipe recipe = calcRecipe();
        if (recipe == null)
            setItem(9, null);
        else
            setItem(9, recipe.getResult().clone());
    }


    private Recipe calcRecipe() {
        @NotNull Iterator<Recipe> iterator = Bukkit.recipeIterator();
        while (iterator.hasNext()) {
            Recipe recipe = iterator.next();
            if (recipe instanceof ShapedRecipe shapedRecipe) {
                int height = shapedRecipe.getShape().length;
                int width = shapedRecipe.getShape()[0].length();
                for (int x = 0; x < 4 - width; x++) {
                    for (int y = 0; y < 4 - height; y++) {
                        if (foundShapedRecipe(shapedRecipe, x, y, width, height)) return shapedRecipe;
                    }
                }
            } else if (recipe instanceof ShapelessRecipe shapelessRecipe) {
                Set<Integer> foundSlots = new HashSet<>();
                boolean found = false;
                for (RecipeChoice p : shapelessRecipe.getChoiceList()) {
                    found = false;
                    for (Integer i = 0; i < getSize(); i++) {
                        if (foundSlots.contains(i)) continue;
                        if (getItem(i) != null && p.test(getItem(i))) {
                            foundSlots.add(i);
                            found = true;
                            break;
                        }
                    }
                    if (!found) break;
                }
                if (found) return shapelessRecipe;

            }
        }
        return null;
    }

    private boolean foundShapedRecipe(ShapedRecipe shapedRecipe, int x, int y, int sizeX, int sizeY) {
        for (int row = 0; row < sizeY; row++) {
            String s = shapedRecipe.getShape()[row];
            for (int col = 0; col < sizeX; col++) {
                char c = s.charAt(col);
                ItemStack stack = this.getItem((row + y) * 3 + (col + x));
                RecipeChoice recipeChoice = shapedRecipe.getChoiceMap().get(c);
                if (stack == null || recipeChoice == null || !recipeChoice.test(stack)) return false;
            }
        }
        return true;
    }

    @Override
    public boolean onPickupItem(InventoryInteractEvent event, Integer slot, ItemStack itemStack) {
//        LogHelper.debug("onPickupItem");
        if (slot == null || slot == 33) {
//            ItemStack result = this.getInventory().getItem(33);
//            if (itemStack == null || result == null || !itemStack.isSimilar(result)) return true;
//            if (itemStack.getAmount() + result.getAmount() > result.getMaxStackSize()) return true;
//            itemStack.setAmount(itemStack.getAmount() + result.getAmount());
            for (int i = 0; i < 9; i++) {
                ItemStack stack = this.getItem(i);
                if (stack != null) stack.setAmount(stack.getAmount() - 1);
            }
        }
        PluginHelper.sync().runLater(() -> {
            this.update();
            ((Player) event.getWhoClicked()).updateInventory();
        }, 1);
        return false;
    }

    @Override
    public boolean onPlaceItem(InventoryInteractEvent event, Integer slot, ItemStack itemStack) {
        if (slot == null || slot == 33) {
            ItemStack result = this.getInventory().getItem(33);
            if (itemStack != null && result != null && itemStack.isSimilar(result)) {
                if (itemStack.getAmount() + result.getAmount() > result.getMaxStackSize()) return true;
                itemStack.setAmount(itemStack.getAmount() + result.getAmount());
                for (int i = 0; i < 9; i++) {
                    ItemStack stack = this.getItem(i);
                    if (stack != null) stack.setAmount(stack.getAmount() - 1);
                }
                PluginHelper.sync().runLater(() -> {
                    this.update();
                    ((Player) event.getWhoClicked()).updateInventory();
                }, 1);
            }
            return true;
        } else {
            PluginHelper.sync().runLater(() -> {
                this.update();
                ((Player) event.getWhoClicked()).updateInventory();
            }, 1);
            return false;
        }
    }
}
