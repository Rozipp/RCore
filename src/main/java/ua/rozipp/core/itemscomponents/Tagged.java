package ua.rozipp.core.itemscomponents;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.items.CustomMaterial;
import ua.rozipp.core.items.ItemHelper;
import ua.rozipp.core.items.ItemStackBuilder;

public class Tagged extends ItemComponent {

	public Tagged() {
		super();
	}

	public ItemStack addTag(ItemStack src, String tag) {
		ItemStackBuilder builder = ItemStackBuilder.of(src);
		builder.addTag("tag", tag);
		builder.addLore(Component.text(tag, NamedTextColor.GRAY));
		return builder.build();
	}

	public String getTag(ItemStack src) {
		return ItemHelper.getNBTTag(src, "tag");
	}

	public static String matrixHasSameTag(ItemStack[] matrix) {
		String tag = null;

		for (ItemStack stack : matrix) {
			if (!ItemHelper.isPresent(stack)) continue;

			CustomMaterial cMat = CustomMaterial.getCustomMaterial(stack);
			if (cMat == null) return null;

			Tagged tagged = cMat.getComponent(Tagged.class);
			if (tagged == null) return null;

			if (tag == null) {
				tag = tagged.getTag(stack);
				continue;
			}
			if (!tagged.getTag(stack).equals(tag)) return null;
		}
		return tag;
	}
}
