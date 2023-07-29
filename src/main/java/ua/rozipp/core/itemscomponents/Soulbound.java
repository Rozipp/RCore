package ua.rozipp.core.itemscomponents;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.ItemStackBuilder;

public class Soulbound extends ItemComponent {

	public Soulbound(RConfig compInfo) throws InvalidConfiguration {
		super(compInfo);
	}

	@Override
	public void onPrepareCreate(ItemStackBuilder builder) {
		builder.addLore(Component.translatable("Soulbound").color(NamedTextColor.GOLD));
	}

}
