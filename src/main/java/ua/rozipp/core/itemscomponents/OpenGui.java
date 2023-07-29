package ua.rozipp.core.itemscomponents;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.GuiException;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.gui.GuiInventory;
import ua.rozipp.core.items.ItemStackBuilder;

public class OpenGui extends ItemComponent {

	private final String guiName;

	public OpenGui(RConfig compInfo) throws InvalidConfiguration {
		super(compInfo);
		this.guiName = compInfo.getString("gui_name", null, null);
	}

	@Override
	public void onPrepareCreate(ItemStackBuilder builder) {
		builder.addLore(Component.translatable("tutorialBook_lore1").color(NamedTextColor.GOLD));
		builder.addLore(Component.translatable("tutorialBook_lore2").color(NamedTextColor.LIGHT_PURPLE));
	}

	public void onInteract(PlayerInteractEvent event){
		if (guiName == null) return;
		event.setCancelled(true);
		if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		try {
			GuiInventory.getGuiInventory(event.getPlayer(), guiName, null).openInventory(event.getPlayer());
		} catch (GuiException e) {
			event.getPlayer().sendMessage(e.getComponent());
		}
	}

	public void onItemSpawn(ItemSpawnEvent event) {
		event.setCancelled(true);
	}

}
