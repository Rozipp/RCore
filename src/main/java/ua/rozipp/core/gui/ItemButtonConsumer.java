package ua.rozipp.core.gui;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ItemButtonConsumer extends ItemButton{

    @Setter
    @Getter
    private Consumer<InventoryClickEvent> consumer;

    /**
     * Create a new ItemButton with the given ItemStack as the icon
     *
     * @param item The ItemStack to be used as the icon
     */
    public ItemButtonConsumer(ItemStack item) {
        super(item);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        getConsumer().accept(e);
    }
}
