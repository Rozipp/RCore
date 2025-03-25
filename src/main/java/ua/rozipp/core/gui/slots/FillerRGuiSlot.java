package ua.rozipp.core.gui.slots;

import de.tr7zw.nbtapi.utils.MinecraftVersion;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.gui.RGuiSlot;
import ua.rozipp.core.items.ItemStackBuilder;

public class FillerRGuiSlot extends RGuiSlot {

    private static final ItemStack GLASS_PANE;
    public static final RGuiSlot FILLER;

    static {
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_13_R1)) {
            GLASS_PANE = ItemStackBuilder.of(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build();
        } else {
            GLASS_PANE = ItemStackBuilder.of(Material.valueOf("STAINED_GLASS_PANE")).setDurability(7).setName(" ").build();
        }
        FILLER = new FillerRGuiSlot();
    }

    public FillerRGuiSlot() {
        this(GLASS_PANE.clone());
    }

    public FillerRGuiSlot(ItemStack itemStack) {
        this.setItemStack(itemStack);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);
        e.setCancelled(true);
        e.setResult(Event.Result.DENY);
    }

    @Override
    public RGuiSlot clone() {
        return new FillerRGuiSlot(this.getItemStack());
    }
}
