package ua.rozipp.core.gui;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.LogHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuiHelper {

    public static final int INV_ROW_COUNT = 9;
    static Map<UUID, ArrayDeque<InventoryGUI>> playersGuiInventoryStack = new HashMap<>();

    private static InventoryGUI newGuiInventory(String id, Player player, Class<? extends InventoryGUI> cls, String arg) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?>[] partypes = {Player.class, String.class};
        Object[] arglist = {player, arg};
        InventoryGUI gi = cls.getConstructor(partypes).newInstance(arglist);
//        if (gi.getPlayer() == null) staticGuiInventory.put(id, gi);
        return gi;
    }

    public static void closeInventory(Player player) {
        player.closeInventory();
    }

    public static String buildId(String classname, String arg) {
        return arg == null ? classname : classname + "_" + arg;
    }

    public static ArrayDeque<InventoryGUI> getInventoryStack(UUID uuid) {
        if (playersGuiInventoryStack.get(uuid) == null) playersGuiInventoryStack.put(uuid, new ArrayDeque<>());
        return playersGuiInventoryStack.get(uuid);
    }

    public static InventoryGUI getActiveGuiInventory(UUID uuid) {
        if (playersGuiInventoryStack.get(uuid) == null) return null;
        if (playersGuiInventoryStack.get(uuid).isEmpty()) return null;
        return playersGuiInventoryStack.get(uuid).getFirst();
    }

    public static void setInventoryStack(UUID uuid, ArrayDeque<InventoryGUI> gis) {
        playersGuiInventoryStack.put(uuid, gis);
    }

    public static void clearInventoryStack(UUID uuid) {
        playersGuiInventoryStack.put(uuid, null);
    }
}
