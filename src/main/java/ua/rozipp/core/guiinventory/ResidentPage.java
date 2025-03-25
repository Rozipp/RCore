package ua.rozipp.core.guiinventory;

import net.kyori.adventure.text.Component;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.gui.RGuiImp;

import java.text.SimpleDateFormat;

public class ResidentPage extends RGuiImp {

	public ResidentPage(Object arg) {
		super(6, Component.translatable("bookReborn_resInfoHeading"));
	}

	@Override
	public void onFirstDraw() {
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
