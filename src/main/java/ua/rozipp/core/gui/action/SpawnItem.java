package ua.rozipp.core.gui.action;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.gui.GuiAction;
import ua.rozipp.core.items.CustomMaterial;

public class SpawnItem extends GuiAction {

    @Override
    public void performAction(Player player, ItemStack stack) {
        CustomMaterial cmat = CustomMaterial.getCustomMaterial(stack);
        player.setItemOnCursor(cmat.spawn(1));

    }
}
