package ua.rozipp.core.gui;

import de.tr7zw.nbtapi.utils.MinecraftVersion;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import ua.rozipp.core.PluginHelper;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;

public interface RGui extends InventoryHolder {

    /**
     * Places items on the GUI. Called when the GUI is first opened.
     */
    void onFirstDraw();

    /**
     * Replaces items on the GUI when inventory update. Also called after {@link #onFirstDraw()}
     */
    void onUpdate();

    void onClose(Player player);

    void onInventoryClickEvent(InventoryClickEvent e);

    @Nullable
    Function<Player, RGui> getFallbackGui();

    void setFallbackGui(@Nullable Function<Player, RGui> fallbackGui);

    /**
     * Returns true unless this GUI has been invalidated, through being closed, or the player leaving.
     *
     * @return true unless this GUI has been invalidated.
     */
    boolean isValid();

    void setValid(boolean b);

    @NotNull Inventory getInventory();

    @Nullable
    Integer getFirstEmptySlot();

    Integer addSlot(RGuiSlot element);

    void setSlot(Integer slot, RGuiSlot element);

    RGuiSlot getSlot(int slot);

    Map<Integer, RGuiSlot> getSlots();

    default void fillSlot(int slot, RGuiSlot filler) {
        setSlot(slot, filler.clone());
    }

    default void fillAllSlots(int start, int end, RGuiSlot filler) {
        for (int i = start; i < end; i++) {
            fillSlot(i, filler);
        }
    }

    default void fillAllSlots(int x1, int y1, int x2, int y2, RGuiSlot filler) {
        for (int x = x1; x < x2; x++) {
            for (int y = y1; y < y2; y++) {
                fillSlot(x + (y * 9), filler);
            }
        }
    }

    default void clearSlot(Integer slot) {
        getSlots().remove(slot);
        getInventory().setItem(slot, null);
    }

    default void clearInventory() {
        this.getInventory().clear();
        this.getSlots().clear();
    }

    default void update() {
        onUpdate();
        for (Integer slot : getSlots().keySet()) {
            getInventory().setItem(slot, getSlots().get(slot).getItemStack());
        }
    }

    void open(Player player);

    default void close(Player player) {
        PluginHelper.getGuiManager().removeOpenedGui(player);
        // clear all items from the GUI, just in case the menu didn't close properly.
        if (this.getInventory().getViewers().size() <= 1) {
            this.setValid(false);
            onClose(player);
            clearInventory();
            player.updateInventory();
        }
        player.closeInventory();
    }

}
