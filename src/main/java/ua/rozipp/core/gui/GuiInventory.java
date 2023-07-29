package ua.rozipp.core.gui;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.object.CallbackInterface;
import ua.rozipp.core.exception.GuiException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Setter
@Getter
public class GuiInventory implements CallbackInterface {
    public static final int MAX_INV_SIZE = 54;
    public static final int INV_COLUM_COUNT = 9;
    protected static Map<String, GuiInventory> staticGuiInventory = new HashMap<>();
    private static Map<UUID, ArrayDeque<GuiInventory>> playersGuiInventoryStack = new HashMap<>();
    private InventoryHolder holder;
    private Player player;
    private String arg;
    protected Inventory inventory = null;
    private HashMap<Integer, ItemStack> items = new HashMap<>();
    private Component title = Component.space();
    private Integer row = 6;

    public GuiInventory(InventoryHolder holder, Player player, String arg) throws GuiException {
        this.holder = holder;
        this.player = player;
        this.arg = arg;
    }

    // ------------- builders

    private static GuiInventory newGuiInventory(String id, Player player, Class<? extends GuiInventory> cls, String arg) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?>[] partypes = {Player.class, String.class};
        Object[] arglist = {player, arg};
        GuiInventory gi = cls.getConstructor(partypes).newInstance(arglist);
        if (gi.getPlayer() == null) staticGuiInventory.put(id, gi);
        return gi;
    }

    public static GuiInventory getGuiInventory(Player player, String className, String arg) throws GuiException {
        String id = GuiInventory.buildId(className, arg);
        GuiInventory gi = staticGuiInventory.get(id);
        if (gi == null) try {
            gi = newGuiInventory(id, player, (Class<? extends GuiInventory>) Class.forName("ua.rozipp.core.gui.guiinventory." + className), arg);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                 ClassNotFoundException e) {
            throw new GuiException(e.getMessage());
        }
        return gi;
    }

    public static GuiInventory getGuiInventory(Player player, Class<? extends GuiInventory> cls, String arg) throws GuiException {
        String id = GuiInventory.buildId(cls.getName(), arg);
        GuiInventory gi = staticGuiInventory.get(id);
        if (gi == null) try {
            gi = newGuiInventory(id, player, cls, arg);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new GuiException(e.getMessage());
        }
        return gi;
    }

    public static void openGuiInventory(Player player, String className, String arg) {
    }

    // -------------- items

    public static void closeInventory(Player player) {
        player.closeInventory();
    }

    public static String buildId(String classname, String arg) {
        return arg == null ? classname : classname + "_" + arg;
    }

    public static ArrayDeque<GuiInventory> getInventoryStack(UUID uuid) {
        if (playersGuiInventoryStack.get(uuid) == null) playersGuiInventoryStack.put(uuid, new ArrayDeque<>());
        return playersGuiInventoryStack.get(uuid);
    }

    public static GuiInventory getActiveGuiInventory(UUID uuid) {
        if (playersGuiInventoryStack.get(uuid) == null) return null;
        if (playersGuiInventoryStack.get(uuid).isEmpty()) return null;
        return playersGuiInventoryStack.get(uuid).getFirst();
    }

    public static void setInventoryStack(UUID uuid, ArrayDeque<GuiInventory> gis) {
        GuiInventory.playersGuiInventoryStack.put(uuid, gis);
    }

    public static void clearInventoryStack(UUID uuid) {
        GuiInventory.playersGuiInventoryStack.put(uuid, null);
    }

    // --------------- getters

    public GuiInventory setRow(int row) {
        this.row = row;
        if (this.row < 1) this.row = 1;
        if (this.row > 6) this.row = 6;
        return this;
    }

    // -------------------- Inventory
    public Integer addGuiItem(ItemStack item) {
        return addGuiItem(0, item);
    }

    public Integer addGuiItem(Integer slot, ItemStack item) {
        if (slot < 0 || slot >= size()) {
            int i;
            for (i = 0; i < size(); i++) {
                if (items.get(i) == null) break;
            }
            items.put(i, item);
            return i;
        }
        if (items.get(slot) == null) {
            items.put(slot, item);
            return slot;
        }
        if (items.size() >= size()) {
            items.put(slot, item);
            return slot;
        }
        int i;
        for (i = 0; i < size(); i++) {
            if (items.get(i) == null) break;
        }
        items.put(i, items.get(slot));
        items.put(slot, item);
        return slot;
    }

    public void addLastItem(UUID uuid) {
        ArrayDeque<GuiInventory> gis;
        gis = GuiInventory.getInventoryStack(uuid);
        if (gis.isEmpty()) {
            items.put(size(), GuiItemBuilder.guiItemBuilder(Material.MAP)//
                    .setAction(GuiHelper.CLOSE_INVENTORY)
                    .setName("§c" + "Закрыть меню").build()
            );
        } else {
            items.put(size(), GuiItemBuilder.guiItemBuilder(Material.MAP)
                    .setAction(GuiHelper.OPEN_BACK_INVENTORY)
                    .name(Component.translatable("loreGui_recipes_back"))//
                    .addLore(Component.translatable("bookReborn_backTo")
                            .args(Component.text(gis.getFirst().getName(), NamedTextColor.WHITE)))
                    .build()
            );
        }
    }

    // ------------------- static

    public ItemStack getGuiItem(Integer i) {
        return items.get(i);
    }

    public Integer size() {
        return row * INV_COLUM_COUNT - 1;
    }

    public String getName() {
        return inventory.getType().name();
    }

    public Component getTitle() {
        return title;
    }

    public GuiInventory name(Component title) {
        this.title = title;
        return this;
    }

    public void openInventory(Player player) {
        ArrayDeque<GuiInventory> gis = GuiInventory.getInventoryStack(player.getUniqueId());
        player.openInventory(getInventory(player));
        gis.push(this);

    }

    private Inventory getInventory(Player player) {
        if (inventory == null) try {
            inventory = Bukkit.createInventory(holder, size() + 1, title);
            addLastItem(player.getUniqueId());
            for (int slot = 0; slot <= size(); slot++) {
                ItemStack item = items.get(slot);
                if (item == null) continue;
                inventory.setItem(slot, item);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return inventory;
    }

    public boolean onItemToInventory(Cancellable event, Player player, Inventory inv, ItemStack stack) {
        return true;
    }

    public boolean onItemFromInventory(Cancellable event, Player player, Inventory inv, ItemStack stack) {
        if (event.isCancelled()) return true;
        if (GuiHelper.isGUIItem(stack)) {
            GuiAction action = GuiHelper.getAction(stack);
            if (action != null) action.performAction(player, stack);
            return true;
        }
        return true;
    }

    @Override
    public void execute(String... strings) {
        // XXX Children Override
        UUID uuid = (getPlayer() == null) ? UUID.fromString(strings[1]) : getPlayer().getUniqueId();

        ArrayDeque<GuiInventory> gis = getInventoryStack(uuid);
        if (!gis.isEmpty()) gis.getFirst().execute(strings);
    }

}
