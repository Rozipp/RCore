package ua.rozipp.core.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.items.CustomMaterial;

public class CraftableCustomMaterialListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void OnCraftItemEvent(CraftItemEvent event) {
        if (event.getWhoClicked() instanceof Player) {
//			CraftableCustomMaterial craftMat = CraftableCustomMaterial.getCraftableCustomMaterial(event.getInventory().getResult());
//			if (craftMat != null) {
//			Если нужно изменить чтото в скрафченом предмете. Например установить хозяина или время крафта
//			}
        }
    }

    private boolean matrixContainsCustom(ItemStack[] matrix) {
        for (ItemStack stack : matrix) {
            if (CustomMaterial.isCustomMaterial(stack)) {
                return true;
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void OnPrepareItemCraftEvent(PrepareItemCraftEvent event) {
        if (event.getRecipe() == null) return;
        if (!CustomMaterial.isCustomMaterial(event.getRecipe().getResult())) {
            if (matrixContainsCustom(event.getInventory().getMatrix())) {
                event.getInventory().setResult(new ItemStack(Material.AIR));
                return;
            }
        }
    }
}