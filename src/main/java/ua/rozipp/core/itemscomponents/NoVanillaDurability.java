package ua.rozipp.core.itemscomponents;

import org.bukkit.event.player.PlayerItemDamageEvent;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;

public class NoVanillaDurability extends ItemComponent {

	public NoVanillaDurability(RConfig compInfo) throws InvalidConfiguration {
		super(compInfo);
	}

	@Override
	public void onDurabilityChange(PlayerItemDamageEvent event) {
		event.setDamage(0);
		event.getPlayer().updateInventory();
	}

}
