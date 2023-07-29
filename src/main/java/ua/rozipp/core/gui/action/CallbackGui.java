package ua.rozipp.core.gui.action;

import ua.rozipp.core.object.CallbackInterface;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.object.CallbackInterfaces;
import ua.rozipp.core.gui.GuiAction;
import ua.rozipp.core.gui.GuiHelper;
import ua.rozipp.core.gui.GuiInventory;

import java.util.ArrayDeque;

public class CallbackGui extends GuiAction {

	@Override
	public void performAction(Player player, ItemStack stack) {
		if (player == null) return;
		String callbackData = GuiHelper.getActionData(stack, argName1());

		CallbackInterface callback = CallbackInterfaces.poll(player);
		if (callback == null) {
			ArrayDeque<GuiInventory> gis = GuiInventory.getInventoryStack(player.getUniqueId());
			gis.pop().execute(callbackData, player.getUniqueId().toString());
		} else
			callback.execute(callbackData, player.getUniqueId().toString());
	}

	@Override
	public String argName1() {
		return "data";
	}
}
