package ua.rozipp.core.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.exception.ComponentException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class GuiManager implements Listener {
    private final Map<Player, RGui> openedRGui = new HashMap<>();

    private final Map<String, Class<? extends RGui>> registeredGuis = new HashMap<>();

    public GuiManager(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void putOpenedGui(Player player, RGui rGui) {
        openedRGui.put(player, rGui);
    }

    public RGui getOpenedGui(Player player) {
        if (openedRGui.containsKey(player)) {
            return openedRGui.get(player);
        }
        return null;
    }

    public void removeOpenedGui(Player player) {
        openedRGui.remove(player);
    }

    public void register(String name, Class<? extends RGui> aClass) {
        registeredGuis.put(name, aClass);
    }

    public Collection<String> inventoryGUINames() {
        return registeredGuis.keySet();
    }

    public RGui getGui(String name, Object arg) throws ComponentException {
        Class<? extends RGui> cls = registeredGuis.get(name);
        if (cls == null) throw new ComponentException("InventoryGui '" + name + "' not found");
        return getGui(cls, arg);
    }

    public RGui getGui(@NotNull Class<? extends RGui> cls, Object arg) throws ComponentException {
        try {
            Class<?>[] partypes = {Object.class};
            Object[] arglist = {arg};
            return cls.getConstructor(partypes).newInstance(arglist);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ComponentException(e.getMessage());
        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent e) {
        onInvalidate(e.getEntity());
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        onInvalidate(e.getPlayer());
    }

    @EventHandler
    public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent e) {
        onInvalidate(e.getPlayer());
    }

    @EventHandler
    public void onPlayerTeleportEvent(PlayerTeleportEvent e) {
        onInvalidate(e.getPlayer());
    }

    private void onInvalidate(Player player) {
        RGui rGui = getOpenedGui(player);
        if (rGui == null) return;
        if (!player.isValid()) return;
        rGui.close(player);
    }

    @EventHandler
    public void onInventoryDragEvent(InventoryDragEvent e) {
        Player player = (Player) e.getWhoClicked();
        RGui rGui = getOpenedGui(player);
        if (rGui == null) return;
        if (!rGui.isValid()) player.closeInventory();

        boolean canceled = true;
//        for (Integer slot : e.getRawSlots()) {
//            if (rGui.convertSlot(slot) != null)
//                canceled = rGui.onPlaceItem(null, slot, rGui.getInventory().getItem(slot));
//            if (canceled) break;
//        }
        if (canceled) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            player.updateInventory();
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        RGui rGui = getOpenedGui(player);
        if (rGui == null) return;
        if (!rGui.isValid()) {
            rGui.close(player);
            return;
        }

        if (!e.getInventory().equals(rGui.getInventory())) {
            rGui.close(player);
            return;
        }

        rGui.onInventoryClickEvent(e);


    }

    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent e) {
        Player player = (Player) e.getPlayer();
        RGui rGui = getOpenedGui(player);
        if (rGui == null) return;
        if (!e.getInventory().equals(rGui.getInventory())) return;
        if (rGui.isValid()) return;
        rGui.close(player);
    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        RGui rGui = getOpenedGui(player);
        if (rGui == null) return;
        rGui.close(player);

        if (e.getInventory().equals(rGui.getInventory())) {
            // Check for a fallback GUI
            Function<Player, RGui> fallback = rGui.getFallbackGui();
            if (fallback == null) return;

            // Open at a delay
            PluginHelper.sync().runLater(() -> {
                if (!player.isOnline()) {
                    return;
                }
                RGui fallbackGui = fallback.apply(player);
                if (fallbackGui == null)
                    throw new IllegalStateException("Fallback function " + fallback + " returned null");
                if (fallbackGui.isValid())
                    throw new IllegalStateException("Fallback function " + fallback + " produced a GUI " + fallbackGui + " which is already open");
                fallbackGui.open(player);

            }, 1L);
        }
    }
}
