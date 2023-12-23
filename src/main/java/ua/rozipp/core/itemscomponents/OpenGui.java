package ua.rozipp.core.itemscomponents;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.GuiException;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.guiinventory.PersonalCrafter;
import ua.rozipp.core.items.ItemStackBuilder;

public class OpenGui extends ItemComponent {

	private String guiName;
	private Plugin plugin;

	public OpenGui() {
		super();
	}

	public OpenGui(Plugin plugin, String guiName) {
		super();
		this.plugin = plugin;
		this.guiName = guiName;
	}

	@Override
	protected void load(RConfig rConfig) throws InvalidConfiguration {
		guiName = rConfig.getString("gui_name", null, null);
		plugin = rConfig.getPlugin();
	}

	@Override
	public void onSpawnItem(ItemStackBuilder builder) {
		builder.addLore(Component.translatable("<Нажми что бы отрыть>").color(NamedTextColor.GOLD));
	}

	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		super.onInteract(event);
		if (guiName == null) return;
		event.setCancelled(true);
		if (guiName.equals("PersonalCrafter")) {
			try {
				new PersonalCrafter(plugin, event.getPlayer()).open(event.getPlayer());
			} catch (GuiException e) {
				LogHelper.error(e.getComponent());
			}
		}
	}

}
