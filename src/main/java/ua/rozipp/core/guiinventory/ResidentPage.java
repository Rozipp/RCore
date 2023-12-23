package ua.rozipp.core.guiinventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.exception.GuiException;
import ua.rozipp.core.gui.InventoryGUI;
import ua.rozipp.core.items.ItemHelper;

import java.text.SimpleDateFormat;

public class ResidentPage extends InventoryGUI {

	public ResidentPage(Plugin plugin, Player player, String arg) throws GuiException {
		super(plugin, Bukkit.createInventory(null, 6 * 9,Component.translatable("bookReborn_resInfoHeading")));

		SimpleDateFormat sdf = PluginHelper.dateFormat;

		/*this.addGuiItem(0, GuiItemBuilder.guiItemBuilder(ItemHelper.createItemStack(Material.SKELETON_SKULL,1))//
				.name(Component.translatable("bookReborn_infoMenu_name"))//
				.addLore(Component.text("Player: " + player.getName(), NamedTextColor.GRAY)) //
//				.addLore("§6" + CivSettings.CURRENCY_NAME + ": " + "§a" + player.getTreasury().getBalance()) //
				.addLore(Component.translatable("cmd_res_showRegistrationDate", NamedTextColor.DARK_GREEN)) //
//								new StringBuilder().append("§a").append(sdf.format(player.getRegistered())).toString()), //
//						"§b" + CivSettings.localize.translate("Civilization") + " " + getResident().getCivName(), //
//						"§d" + CivSettings.localize.translate("Town") + " " + getResident().getTownName(), //
//						CivColor.Red + CivSettings.localize.translate("Camp") + getResident().getCampName()
				.build());*/
	}

}
