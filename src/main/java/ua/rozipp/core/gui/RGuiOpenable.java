package ua.rozipp.core.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.gui.slots.RButton;

import javax.annotation.Nullable;
import java.util.*;

public class RGuiOpenable extends RGuiImp {

    private List<Integer> openSlots = new LinkedList<>();

    public RGuiOpenable(int lines, Component title) {
        super(lines, title);
    }

    public RGuiOpenable(int lines, String title) {
        super(lines, title);
    }

    public boolean onPickupItem(InventoryInteractEvent event, Integer slot, ItemStack itemStack) {
        return false;
    }

    public boolean onPlaceItem(InventoryInteractEvent event, Integer slot, ItemStack itemStack) {
        return false;
    }

    @Override
    public void onInventoryClickEvent(InventoryClickEvent e) {
        super.onInventoryClickEvent(e);
        if (e.isCancelled()) return;

        int slotId = e.getRawSlot();
        boolean topClick = (slotId == e.getSlot());

        ItemStack currentItem = (e.getCurrentItem() != null && e.getCurrentItem().getType().isAir()) ? null : e.getCurrentItem();
        ItemStack cursorItem = (e.getCursor() != null && e.getCursor().getType().isAir()) ? null : e.getCursor();

//        LogHelper.debug("e.getAction() = " + e.getAction() + "    "
//                + (currentItem != null ? "currentItem = " + currentItem.getType() : "") + "    "
//                + (cursorItem != null ? "cursorItem = " + cursorItem.getType() : "")
//        );
        boolean canceled = false;
        LogHelper.debug(String.valueOf(e.getAction()));
        switch (e.getAction()) {
            case PICKUP_ALL, PICKUP_SOME, PICKUP_HALF, PICKUP_ONE, DROP_ALL_SLOT, DROP_ONE_SLOT -> {
                if (topClick)
                    canceled = this.onPickupItem(e, slotId, currentItem);
            }
            case PLACE_ALL, PLACE_SOME, PLACE_ONE -> {
                if (topClick)
                    canceled = this.onPlaceItem(e, slotId, cursorItem);
            }
            case SWAP_WITH_CURSOR -> {
                if (topClick) {
                    canceled = this.onPickupItem(e, slotId, currentItem);
                    if (!canceled) this.onPlaceItem(e, slotId, cursorItem);
                }
            }
            case MOVE_TO_OTHER_INVENTORY -> {
                if (topClick)
                    canceled = this.onPickupItem(e, slotId, currentItem);
                else
                    canceled = this.onPlaceItem(e, null, currentItem);
            }
            case HOTBAR_MOVE_AND_READD -> {
                if (topClick) {
                    canceled = this.onPickupItem(e, slotId, currentItem);
                    if (!canceled) this.onPlaceItem(e,
                            slotId,
                            e.getWhoClicked().getInventory().getItem(e.getHotbarButton())
                    );
                }
            }
            case HOTBAR_SWAP -> {
                if (topClick) {
                    canceled = this.onPlaceItem(e,
                            slotId,
                            e.getWhoClicked().getInventory().getItem(e.getHotbarButton()));
                }
            }
            case DROP_ALL_CURSOR, DROP_ONE_CURSOR, CLONE_STACK, COLLECT_TO_CURSOR, UNKNOWN, NOTHING -> {
            }
        }
        if (canceled) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            ((Player) e.getWhoClicked()).updateInventory();
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    public void onClose(Player player) {
        super.onClose(player);
        returnAllItems(player);
    }

    public int getSize() {
        return this.openSlots.size();
    }

    @Override
    public Integer addSlot(RGuiSlot element) {
        Integer slot = super.addSlot(element);
        if (slot != null)
            openSlots.remove(slot);
        return slot;
    }

    public void openSlot(Integer slot) {
        openSlots.add(slot);
        super.clearSlot(slot);
    }

    /**
     * Opens slots so that items can be placed in them
     *
     * @param start The start of the open slot section, inclusive
     * @param end   The end of the open slot section, exclusive
     */
    public void openSlots(int start, int end) {
        for (int i = start; i < end; i++) {
            openSlot(i);
        }
    }

    /**
     * Opens slots so that items can be placed in them
     *
     * @param x1 The x position to open from, inclusive
     * @param y1 The y position to open from, inclusive
     * @param x2 The x position to open to, exclusive
     * @param y2 The y position to open to, exclusive
     */
    public void openSlots(int x1, int y1, int x2, int y2) {
        for (int y = y1; y < y2; y++) {
            for (int x = x1; x < x2; x++) {
                openSlot(y * 9 + x);
            }
        }
    }

    public void openAllSlots() {
        openSlots(0, this.getInventory().getSize());
    }

    /**
     * Closes a slot so that items can't be placed in it
     *
     * @param slot The slot to open
     */
    public void closeSlot(Integer slot) {
        setSlot(slot, RGuiSlot.createFiller());
    }

    /**
     * Closes slots so that items can't be placed in them
     *
     * @param start The start of the closed slot section, inclusive
     * @param end   The end of the open closed section, exclusive
     */
    public void closeSlots(int start, int end) {
        for (int i = start; i < end; i++) {
            closeSlot(i);
        }
    }

    /**
     * Closes slots so that items can't be placed in them
     *
     * @param x1 The x position to close from, inclusive
     * @param y1 The y position to close from, inclusive
     * @param x2 The x position to close to, exclusive
     * @param y2 The y position to close to, exclusive
     */
    public void closeSlots(int x1, int y1, int x2, int y2) {
        for (int y = y1; y < y2; y++) {
            for (int x = x1; x < x2; x++) {
                closeSlot(y * 9 + x);
            }
        }
    }

    public void closeAllSlots() {
        closeSlots(0, this.getInventory().getSize());
    }

    /**
     * Gets the open slots
     *
     * @return The set of open slots
     */
    public List<Integer> getOpenSlots() {
        return openSlots;
    }

    public Integer getFirstEmptySlot() {
        for (Integer i : openSlots) {
            ItemStack item = this.getInventory().getItem(i);
            if (item == null || item.getType().isAir()) return i;
        }
        return null;
    }

    public void addItem(ItemStack itemStack) {
        Objects.requireNonNull(itemStack, "item");
        try {
            int slot = getFirstEmptySlot();
            this.setItem(slot, itemStack);
        } catch (IndexOutOfBoundsException e) {
            LogHelper.error(e.getMessage());
        }
    }

    public void addItems(Iterable<ItemStack> items) {
        Objects.requireNonNull(items, "items");
        for (ItemStack itemStack : items) {
            addItem(itemStack);
        }
    }

    public void setItem(Integer slot, ItemStack itemStack) {
        if (slot >= 0 && slot < getSize()) {
            this.getInventory().setItem(openSlots.get(slot), itemStack);
        }
    }

    public ItemStack getItem(int slot) {
        if (slot < 0 || slot >= getSize()) return null;
        return this.getInventory().getItem(openSlots.get(slot));
    }

    public ItemStack removeItem(int slot) {
        ItemStack itemStack = null;
        if (slot >= 0 && slot < getSize()) {
            itemStack = this.getInventory().getItem(openSlots.get(slot));
            this.getInventory().setItem(openSlots.get(slot), new ItemStack(Material.AIR));
        }
        return itemStack;
    }

    public Collection<ItemStack> removeItems(int... slots) {
        ArrayList<ItemStack> removed = new ArrayList<>();
        for (int slot : slots) {
            removed.add(removeItem(slot));
        }
        return removed;
    }

    public Collection<ItemStack> removeItems(Iterable<Integer> slots) {
        Objects.requireNonNull(slots, "slots");
        ArrayList<ItemStack> removed = new ArrayList<>();
        for (Integer slot : slots) {
            removed.add(removeItem(slot));
        }
        return removed;
    }

    public void returnAllItems(Player player) {
        HashMap<Integer, ItemStack> drop = new HashMap<>();
        for (Integer i : openSlots) {
            ItemStack itemStack = this.getInventory().getItem(i);
            if (itemStack != null)
                drop.putAll(player.getInventory().addItem(itemStack));
            this.getInventory().setItem(i, new ItemStack(Material.AIR));
        }
        drop.values().forEach(itemStack -> player.getLocation().getWorld().dropItem(player.getLocation(), itemStack));
    }

    public @Nullable Integer convertSlot(Integer slot) {
        for (int i = 0; i < openSlots.size(); i++)
            if (slot.equals(openSlots.get(i))) return i;
        return null;
    }
}
