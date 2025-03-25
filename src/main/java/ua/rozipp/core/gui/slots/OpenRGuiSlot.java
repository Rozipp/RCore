package ua.rozipp.core.gui.slots;

import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.gui.RGuiSlot;

public class OpenRGuiSlot extends RGuiSlot {

    public OpenRGuiSlot() {
    }

    @Override
    public RGuiSlot setItemStack(ItemStack itemStack) {
        return this;
    }

    @Override
    public ItemStack getItemStack() {
        return null;
    }

    @Override
    public RGuiSlot clone() {
        return new OpenRGuiSlot();
    }
}
