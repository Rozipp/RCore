package ua.rozipp.core.gui.action;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.exception.GuiException;
import ua.rozipp.core.gui.GuiAction;
import ua.rozipp.core.gui.GuiHelper;
import ua.rozipp.core.gui.GuiInventory;
import ua.rozipp.core.MessageHelper;

public class OpenInventory extends GuiAction {

	@Override
	public void performAction(Player player, ItemStack stack) {
		try {
			GuiInventory.getGuiInventory(player,
					GuiHelper.getActionData(stack, argName1()),
					GuiHelper.getActionData(stack, argName2())
			).openInventory(player);
		} catch (GuiException e) {
			MessageHelper.sendError(player, e.getMessage());
		}
	}

	@Override
	public String argName1() {
		return "className";
	}

	@Override
	public String argName2() {
		return "arg";
	}
}
