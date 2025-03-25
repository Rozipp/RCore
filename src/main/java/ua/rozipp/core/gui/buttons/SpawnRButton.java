package ua.rozipp.core.gui.buttons;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.gui.slots.RButton;

public class SpawnRButton extends RButton {

    /**
     * Create a new ItemButton with the given ItemStack as the icon
     *
     * @param itemStack The ItemStack to be used as the icon
     */
    public SpawnRButton(ItemStack itemStack) {
        super();
        setItemStack(itemStack);
        setConsumer(e -> {
            if (e.getClick().equals(ClickType.RIGHT)) {
                if (e.getCursor() == null || e.getCursor().getType().isAir()) {
                    e.getWhoClicked().setItemOnCursor(getItemStack().clone());
                } else {
                    ItemStack stack = e.getCursor();
                    stack.setAmount(stack.getAmount() - 1);
                    e.getWhoClicked().setItemOnCursor(stack);
                }
                return;
            }
            if (e.getClick().equals(ClickType.LEFT)) {
                if (e.getCursor() == null || e.getCursor().getType().isAir()) {
                    e.getWhoClicked().setItemOnCursor(getItemStack().clone());
                } else {
                    if (e.getCursor().isSimilar(getItemStack())) {
                        ItemStack stack = e.getCursor();
                        stack.setAmount(Math.min(e.getCursor().getAmount() + 1, stack.getMaxStackSize()));
                        e.getWhoClicked().setItemOnCursor(stack);
                        e.setCancelled(true);
                    } else {
                        e.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                    }
                }
                return;
            }
            if (e.getClick().equals(ClickType.MIDDLE)) {
                ItemStack stack = getItemStack().clone();
                stack.setAmount(stack.getMaxStackSize());
                e.getWhoClicked().setItemOnCursor(stack);
                e.setCancelled(true);
                return;
            }
        });
    }

}
