package ua.rozipp.core.itemscomponents;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import ua.rozipp.core.items.ItemStackBuilder;

public class ChoiceUnitComponent extends ItemComponent {
	public ChoiceUnitComponent() {
	}

	@Override
	public void onSpawnItem(ItemStackBuilder builder) {
		builder.addLore("<Нажми для выбора>");
	}

	@Override
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
//		Resident resident = CivGlobal.getResident(player);
//		int unitid = resident.getUnitObjectId();
//		if (unitid <= 0) {
//			AMessage.send(player, "Юнит не найден");
//			event.setCancelled(true);
//			return;
//		}
//TODO		try {
//			GuiInventory.getGuiInventory(player, ua.rozipp.core.guiinventory.ChoiceUnitComponent.class, null).openInventory(player);
//		} catch (CivException e) {
//			CivMessage.sendError(player, e.getMessage());
//		}
	}

	@Override
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		event.setCancelled(true);
	}

}
