package ua.rozipp.core.gui.action;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.gui.ItemButton;

public class SpawnItem extends ItemButton {

    /**
     * Create a new ItemButton with the given ItemStack as the icon
     *
     * @param item The ItemStack to be used as the icon
     */
    public SpawnItem(ItemStack item) {
        super(item);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        switch (e.getClick()) {
            case LEFT -> {
                int count = 1;
                if (e.getCursor().isSimilar(getItem())){
                    count += e.getCursor().getAmount();
                }
                ItemStack itemStack = getItem().clone();
                itemStack.setAmount(Math.min(count, getItem().getMaxStackSize()));
                e.getWhoClicked().setItemOnCursor(itemStack);
                e.setCancelled(true);
            }
            case RIGHT -> {
                ItemStack itemStack = getItem().clone();
                itemStack.setAmount(getItem().getMaxStackSize());
                e.getWhoClicked().setItemOnCursor(itemStack);
                e.setCancelled(true);
            }
        }

    }
}
