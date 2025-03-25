package ua.rozipp.core.gui;

import de.tr7zw.nbtapi.utils.MinecraftVersion;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.util.TextUtils;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RGuiImp implements RGui {

    @Getter
    private final Inventory inventory;
    private final int inventorySize;
    private final Map<Integer, RGuiSlot> elements = new HashMap<>();
    @Nullable
    Function<Player, RGui> fallbackGui = null;
    @Getter
    @Setter
    boolean valid = false;

    public RGuiImp(int lines, Component title) {
        this(lines, TextUtils.toLegacy(title));
    }

    public RGuiImp(int lines, String title) {
        inventorySize = lines * 9;
        if (title == null)
            this.inventory = Bukkit.createInventory(this, inventorySize);
        else
            this.inventory = Bukkit.createInventory(this, inventorySize, TextUtils.colorize(title));
    }

    public void onInventoryClickEvent(InventoryClickEvent e) {
        int slotId = e.getRawSlot();
        RGuiSlot slot = this.getSlot(slotId);
        if (slot == null) return;
        slot.onClick(e);
    }

    @Nullable
    public Function<Player, RGui> getFallbackGui() {
        return this.fallbackGui;
    }

    public void setFallbackGui(@Nullable Function<Player, RGui> fallbackGui) {
        this.fallbackGui = fallbackGui;
    }

    public @Nullable Integer getFirstEmptySlot() {
        for (int i = 0; i < inventorySize; i++) {
            if (getSlot(i) == null) return i;
        }
        return null;
    }

    public Integer addSlot(RGuiSlot element) {
        Integer slot = getFirstEmptySlot();
        setSlot(slot, element);
        return slot;
    }

    public void setSlot(Integer slot, RGuiSlot element) {
        if (slot != null && slot >= 0 && slot < inventorySize)
            elements.put(slot, element);
    }

    public RGuiSlot getSlot(int slot) {
        return elements.get(slot);
    }

    public Map<Integer, RGuiSlot> getSlots() {
        return elements;
    }

    public void open(Player player) {
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_16_R1)) {
            // delay by a tick in 1.16+ to prevent an unwanted PlayerInteractEvent interfering with inventory clicks
            PluginHelper.sync().runLater(() -> {
                if (!player.isOnline()) {
                    return;
                }
                handleOpen(player);
            }, 1);
        } else {
            handleOpen(player);
        }
    }

    private void handleOpen(Player player) {
        try {
            if (isValid()) {
                update();
            } else {
                onFirstDraw();
                update();
            }
        } catch (Exception e) {
            e.printStackTrace();
            close(player);
            return;
        }

        player.openInventory(this.getInventory());
        PluginHelper.getGuiManager().putOpenedGui(player, this);
        this.setValid(true);
    }

    @Override
    public void onFirstDraw() {
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onClose(Player player) {
    }

}
