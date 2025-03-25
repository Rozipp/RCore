package ua.rozipp.core.guiinventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import ua.rozipp.core.config.ConfigMaterialCategory;
import ua.rozipp.core.gui.RGui;
import ua.rozipp.core.gui.RGuiImp;
import ua.rozipp.core.gui.buttons.OpenInventoryRButton;
import ua.rozipp.core.items.ItemStackBuilder;

public class CraftingHelp extends RGuiImp {

	public CraftingHelp(Object arg) {
		super(6, Component.translatable("gui.CraftingHelp.title"));
	}

	@Override
	public void onFirstDraw() {
		int slot = 0;
		for (ConfigMaterialCategory cat : ConfigMaterialCategory.getCategories()) {
			Material identifier = cat.title.contains("Fish") ? Material.TROPICAL_FISH //
					: cat.title.contains("Gear") ? Material.IRON_SWORD //
					: cat.title.contains("Materials") ? Material.BIRCH_WOOD //
					: cat.title.contains("Tools") ? Material.IRON_SHOVEL //
					: cat.title.contains("TNT") ? Material.TNT //
					: Material.WRITTEN_BOOK;

			this.setSlot(slot++, new OpenInventoryRButton(ItemStackBuilder.of(identifier)
							.setName(cat.title)
							.addLore(Component.translatable("tutorial_lore_items").args(Component.text(cat.materials.size())),
									Component.translatable("tutorial_lore_clickToOpen", NamedTextColor.GOLD))
					).setRGuiFromClass(CraftingHelpCategory.class, cat)
			);

		}
	}
}
