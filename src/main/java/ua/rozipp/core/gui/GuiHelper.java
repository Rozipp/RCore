package ua.rozipp.core.gui;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.gui.action.*;

import java.util.HashMap;

public class GuiHelper {

    public static final String TAG_GUI = "GUI";
    public static final int INV_ROW_COUNT = 9;
    public static final GuiAction CALLBACK_GUI = new CallbackGui();
    public static final GuiAction OPEN_INVENTORY = new OpenInventory();
    public static final GuiAction CLOSE_INVENTORY = new CloseInventory();
    public static final GuiAction OPEN_BACK_INVENTORY = new OpenBackInventory();
    public static final GuiAction CONFIRMATION = new Confirmation();
    public static final GuiAction SPAWN_ITEM = new SpawnItem();
    public static HashMap<String, GuiAction> guiActions = new HashMap<>();

    static {
        GuiHelper.addAction(CALLBACK_GUI);
        GuiHelper.addAction(OPEN_INVENTORY);
        GuiHelper.addAction(CLOSE_INVENTORY);
        GuiHelper.addAction(OPEN_BACK_INVENTORY);
        GuiHelper.addAction(CONFIRMATION);
        GuiHelper.addAction(SPAWN_ITEM);
        for (String key : guiActions.keySet())
            LogHelper.debug(key);
    }

    private static void addAction(GuiAction guiAction){
        guiActions.put(guiAction.getName().toLowerCase(), guiAction);
    }

    public static boolean isGUIItem(ItemStack stack) {
        return (new NBTItem(stack)).hasTag(TAG_GUI);
    }

    public static GuiAction getAction(String actionName){
        LogHelper.debug("getAction(" + actionName + ")");
        if (guiActions.containsKey(actionName.toLowerCase())) return guiActions.get(actionName.toLowerCase());
        LogHelper.warning("Not fount GuiAction " + actionName);
        return null;
    }

    public static GuiAction getAction(ItemStack stack) {
        String actionName = getActionData(stack, "action");
        if (actionName.isEmpty()) return null;
        return getAction(actionName);
    }

    public static String getActionData(ItemStack stack, String argName) {
        if (stack == null || stack.getType().isAir() || stack.getAmount() == 0) return "";
        NBTCompound compound = (new NBTItem(stack)).getCompound(TAG_GUI);
        if (compound == null) return "";
        return compound.getString(argName).replaceAll("\"","");
    }
}
