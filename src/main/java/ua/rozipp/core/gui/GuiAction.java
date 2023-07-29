package ua.rozipp.core.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.LogHelper;

public class GuiAction {

	public GuiAction(){
		LogHelper.debug("CREATED GuiAction " + getName());
	}

	public void performAction(Player player, ItemStack stack){ }

	public String getName(){
		return this.getClass().getSimpleName();
	}

	public String argName1() {
		return "data";
	}

	public String argName2() {
		return "data2";
	}

	public String argName3() {
		return "data3";
	}

	public String argName4() {
		return "data4";
	}
}
