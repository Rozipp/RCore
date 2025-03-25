package ua.rozipp.core.itemscomponents;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.ComponentException;
import ua.rozipp.core.exception.InvalidConfiguration;
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
	public void onBuildItemStack(ItemStackBuilder builder) {
		builder.addLore(Component.translatable("<Нажми что бы отрыть>").color(NamedTextColor.GOLD));
	}

	public void onInteract(PlayerInteractEvent event) {
		if (event.useItemInHand() == Event.Result.DENY) return;
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		if (guiName == null) return;
		event.setUseInteractedBlock(Event.Result.DENY);
		event.setUseItemInHand(Event.Result.DENY);
		try {
			PluginHelper.getGuiManager().getGui(guiName, null).open(event.getPlayer());
		} catch (ComponentException e) {
			LogHelper.error(e.getComponent());
		}
	}

}
