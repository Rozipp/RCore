package ua.rozipp.core.itemscomponents;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import ua.rozipp.core.items.ItemStackBuilder;

public class Soulbound extends ItemComponent {

	public Soulbound() {
		super();
	}

	@Override
	public void onSpawnItem(ItemStackBuilder builder) {
		builder.addLore(Component.translatable("item.component.lore.soulbound").color(NamedTextColor.GOLD));
	}

}
