package ua.rozipp.core.guiinventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import ua.rozipp.core.config.ConfigMaterialCategory;
import ua.rozipp.core.gui.RGui;
import ua.rozipp.core.gui.RGuiImp;
import ua.rozipp.core.gui.buttons.OpenInventoryRButton;
import ua.rozipp.core.items.CustomMaterial;
import ua.rozipp.core.items.ItemStackBuilder;

public class CraftingHelpCategory extends RGuiImp {
	ConfigMaterialCategory category;

	public CraftingHelpCategory(Object arg) {
		this((arg instanceof ConfigMaterialCategory) ?
				(ConfigMaterialCategory) arg :
				(arg instanceof String) ?
						ConfigMaterialCategory.getCategory((String) arg) :
						null);
	}

	public CraftingHelpCategory(ConfigMaterialCategory category) {
		super(6,
				(category == null) ?
						Component.translatable("tutorial_customRecipesHeading") :
						Component.translatable("tutorial_lore_recipes").args(Component.text(category.title)));
		this.category = category;
	}

	@Override
	public void onFirstDraw() {
		int slot = 0;
		for (CustomMaterial cmat : category.materials.values()) {
			if (!cmat.isCraftable()) continue;
			this.setSlot(slot++, new OpenInventoryRButton(ItemStackBuilder.of(cmat.spawn())
							.addLore(Component.translatable("gui.CraftingHelpCategory.clickForRecipe", Style.style(NamedTextColor.GOLD, TextDecoration.BOLD)))
					).setRGuiFromClass(CraftingHelpRecipe.class, cmat)
			);
		}
	}
}
