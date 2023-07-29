package ua.rozipp.core.guiinventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.config.ConfigMaterialCategory;
import ua.rozipp.core.exception.GuiException;
import ua.rozipp.core.gui.GuiInventory;
import ua.rozipp.core.gui.GuiItemBuilder;
import ua.rozipp.core.items.CustomMaterial;

public class CraftingHelp extends GuiInventory {

	public CraftingHelp(Player player, String arg) throws GuiException {
		super(player, null, arg);
		if (arg == null)
			createPerent();
		else
			createCategory();
	}

	private void createPerent() {
		this.setTitle(Component.translatable("tutorial_customRecipesHeading"));

		/* Build the Category Inventory. */
		for (ConfigMaterialCategory cat : ConfigMaterialCategory.getCategories()) {
			if (cat.craftableCount == 0) continue;
			Material identifier = cat.title.contains("Fish") ? Material.TROPICAL_FISH //
					: cat.title.contains("Gear") ? Material.IRON_SWORD //
							: cat.title.contains("Materials") ? Material.BIRCH_WOOD //
									: cat.title.contains("Tools") ? Material.IRON_SHOVEL : //
											Material.WRITTEN_BOOK;
			this.addGuiItem(GuiItemBuilder.guiItemBuilder(identifier)//
							.setOpenInventory(CraftingHelp.class, cat.id)
							.setName(cat.title)//
					.addLore(Component.text(cat.materials.size())
							.append(Component.space())
							.append(Component.translatable("tutorial_lore_items"))
							.color(NamedTextColor.AQUA))//
					.addLore(Component.translatable("tutorial_lore_clickToOpen", NamedTextColor.GOLD))//
					.build());
		}
	}

	private void createCategory() {
		/* Build a new GUI Inventory. */
		ConfigMaterialCategory cat = ConfigMaterialCategory.getCategory(this.getArg());
		this.setTitle(Component.translatable("tutorial_lore_recipes").args(Component.text(cat.title)));
		for (CustomMaterial cmat : cat.materials.values()) {
			ItemStack stack = CraftingHelpRecipe.getInfoBookForItem(cmat.getMid());
			if (stack == null) continue;
			this.addGuiItem(GuiItemBuilder.guiItemBuilder(stack).setOpenInventory(CraftingHelpRecipe.class, cmat.getMid().asString()).build());
		}
	}

}
