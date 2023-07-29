
package ua.rozipp.core.gui.action;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.gui.GuiAction;

public class Confirmation extends GuiAction {
	@Override
	public void performAction(Player player, ItemStack stack) {
//		try {
//			GuiInventory inv = new GuiInventory(player, player, null).setRow(3);
//			inv.setTitle("§a" + CivSettings.localize.translate("resident_tradeNotconfirmed"));
//			String fields = GuiItem.getActionData(stack, "passFields");
//			String action = GuiItem.getActionData(stack, "passAction");
//			String confirmText = GuiItem.getActionData(stack, "confirmText");
//			ItemStack confirm = GuiItem.guiItemBuilder(Material.EMERALD_BLOCK)
//					.setAction(action)
//					.setName("§a" + confirmText).build();
//			for (String field : fields.split(",")) {
//				confirm.setActionData(field, GuiItem.getActionData(stack, field));
//			}
//			inv.addGuiItem(11, confirm);
//			inv.addLastItem(player.getUniqueId());
//			inv.openInventory(player);
//		} catch (CivException e) {
//			CivMessage.send(player, e.getMessage());
//		}
	}
}
