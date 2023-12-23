package ua.rozipp.core.itemscomponents;

import org.bukkit.event.player.PlayerItemDamageEvent;

public class NoVanillaDurability extends ItemComponent {

	public NoVanillaDurability() {
		super();
	}

	@Override
	public void onDurabilityChange(PlayerItemDamageEvent event) {
		event.setDamage(0);
		event.getPlayer().updateInventory();
	}

}
