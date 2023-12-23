package ua.rozipp.core.guiinventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.config.ConfigMaterialCategory;
import ua.rozipp.core.exception.GuiException;
import ua.rozipp.core.gui.InventoryGUI;
import ua.rozipp.core.gui.action.OpenInventoryButton;
import ua.rozipp.core.items.CustomMaterial;

public class CraftingHelp extends InventoryGUI {

	public CraftingHelp(Plugin plugin, ConfigMaterialCategory category) throws GuiException {
		super(plugin, Bukkit.createInventory(null, 6 * 9,
				(category == null) ?
						Component.translatable("tutorial_customRecipesHeading") :
						Component.translatable("tutorial_lore_recipes").args(Component.text(category.title))));
		if (category == null)
			createParent();
		else
			createCategory(category);
	}

	private void createParent() {
		/* Build the Category Inventory. */
		for (ConfigMaterialCategory cat : ConfigMaterialCategory.getCategories()) {
			if (cat.craftableCount == 0) continue;
			Material identifier = cat.title.contains("Fish") ? Material.TROPICAL_FISH //
					: cat.title.contains("Gear") ? Material.IRON_SWORD //
					: cat.title.contains("Materials") ? Material.BIRCH_WOOD //
					: cat.title.contains("Tools") ? Material.IRON_SHOVEL //
					: cat.title.contains("TNT") ? Material.TNT //
					: Material.WRITTEN_BOOK;
			try {
				this.addButton(new OpenInventoryButton(identifier,
						new CraftingHelp(getPlugin(), cat),
						cat.title,
						Component.text(cat.materials.size())
								.append(Component.space())
								.append(Component.translatable("tutorial_lore_items"))
								.color(NamedTextColor.AQUA),
						Component.translatable("tutorial_lore_clickToOpen", NamedTextColor.GOLD))
				);
			} catch (GuiException e) {
				LogHelper.error(e.getComponent());
				e.printStackTrace();
			}
		}
	}

	private void createCategory(ConfigMaterialCategory category) {
		/* Build a new GUI Inventory. */
		for (CustomMaterial cmat : category.materials.values()) {
			if (!cmat.isCraftable()) continue;
			try {
				this.addButton(new OpenInventoryButton(cmat.spawn(),
						new CraftingHelpRecipe(getPlugin(), cmat),
						Component.translatable("tutorial_clickForRecipe", Style.style(NamedTextColor.GOLD, TextDecoration.BOLD))
				));
			} catch (GuiException e) {
				LogHelper.error(e.getComponent());
				e.printStackTrace();
			}
		}
	}

}
