
package ua.rozipp.core.gui.action;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.gui.ItemButton;

public class Confirmation extends ItemButton {

	/**
	 * Create a new ItemButton with the given ItemStack as the icon
	 *
	 * @param item The ItemStack to be used as the icon
	 */
	public Confirmation(ItemStack item) {
		super(item);
	}

	@Override
	public void onClick(InventoryClickEvent e) {
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
