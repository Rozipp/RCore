package ua.rozipp.core.guiinventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ua.rozipp.core.exception.GuiException;
import ua.rozipp.core.gui.InventoryGUI;

public class PersonalCrafter extends InventoryGUI {
    public PersonalCrafter(Plugin plugin, Player player) throws GuiException {
        super(plugin, Bukkit.createInventory(player, 54, "Personal Crafter"));
        fill(0, 53, FILLER);
        openSlots(1, 1, 3, 4);
        fill(1, 1, 3, 4, new ItemStack(Material.AIR));
    }
}
