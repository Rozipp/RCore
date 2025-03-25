package ua.rozipp.core.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.gui.slots.FillerRGuiSlot;

public class RGuiSlot {

    public static RGuiSlot createFiller() {
        return new FillerRGuiSlot();
    }

    private ItemStack itemStack;

    public RGuiSlot() {
    }

    public RGuiSlot(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void onClick(InventoryClickEvent e) {
    }

    public RGuiSlot setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public RGuiSlot clone() {
        return new RGuiSlot()
                .setItemStack(this.getItemStack());
    }

}
