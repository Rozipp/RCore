package ua.rozipp.core.guiinventory;

import net.kyori.adventure.text.Component;
import ua.rozipp.core.gui.RGui;
import ua.rozipp.core.gui.RGuiImp;

public class Tutorial extends RGuiImp {

	public Tutorial(Object arg) {
		super(3, Component.translatable("tutorial_gui_heading"));
	}

	@Override
    public void onFirstDraw() {
//		this.addButton(0, new GuiItemBuilder(Material.LEGACY_WORKBENCH)//
//				.name(Component.translatable("tutorial_workbench_heading", NamedTextColor.BLUE, TextDecoration.BOLD))//
//				.addLore(Component.translatable("tutorial_workbench_Line1"), //
//						Component.translatable("tutorial_workbench_Line2"), //
//						Component.translatable("tutorial_workbench_Line3"), //
//						Component.translatable("tutorial_workbench_Line4"), //
//						Component.translatable("tutorial_workbench_Line5"), //
//						Component.translatable("tutorial_workbench_Line6", NamedTextColor.GREEN))
//				.build());
//
//		this.addButton(0, GuiItemBuilder.guiItemBuilder(Material.COMPASS)//
//				.name(Component.translatable("tutorial_compass_heading", NamedTextColor.BLUE, TextDecoration.BOLD))//
//				.addLore(Component.translatable("tutorial_compass_Line1"), //
//						Component.translatable("tutorial_compass_Line2"), //
//						Component.translatable("tutorial_compass_Line3"), //
//						Component.translatable("tutorial_compass_Line4"), //
//						Component.translatable("tutorial_compass_Line5"))
//				.build());
//
//		this.addButton(0, GuiItemBuilder.guiItemBuilder(Material.DIAMOND_ORE)//
//				.name(Component.translatable("tutorial_diamondOre_heading", NamedTextColor.AQUA, TextDecoration.BOLD))//
//				.addLore(Component.translatable("tutorial_diamondOre_Line1"), //
//						Component.translatable("tutorial_diamondOre_Line2"), //
//						Component.translatable("tutorial_diamondOre_Line3"), //
//						Component.translatable("tutorial_diamondOre_Line4"), //
//						Component.translatable("var_tutorial_diamondOre_Line5"), //
//						Component.translatable("var_tutorial_diamondOre_Line6"))
//				.build());
//
//		this.addButton(0, GuiItemBuilder.guiItemBuilder(Material.FEATHER)//
//				.name(Component.translatable("tutorial_Fence_heading", NamedTextColor.AQUA, TextDecoration.BOLD))//
//				.addLore(Component.translatable("tutorial_Fence_Line1"), //
//						Component.translatable("tutorial_Fence_Line2"), //
//						Component.translatable("tutorial_Fence_Line3"), //
//						Component.translatable("var_tutorial_Fence_Line4"), //
//						Component.translatable("tutorial_Fence_Line5"), //
//						Component.translatable("tutorial_Fence_Line6"), //
//						Component.translatable("tutorial_Fence_Line7"))
//				.build());
//
//		this.addButton(0, GuiItemBuilder.guiItemBuilder(Material.GOLDEN_HELMET)//
//				.name(Component.translatable("tutorial_goldHelmet_heading", NamedTextColor.AQUA, TextDecoration.BOLD))//
//				.addLore(Component.translatable("tutorial_goldHelmet_Line1"), //
//						Component.translatable("tutorial_goldHelmet_Line2"), //
//						Component.translatable("tutorial_goldHelmet_Line3"), //
//						Component.translatable("tutorial_goldHelmet_Line4"), //
//						Component.translatable("tutorial_goldHelmet_Line5"), //
//						Component.translatable("tutorial_goldHelmet_Line6"), //
//						Component.translatable("tutorial_goldHelmet_Line7"))
//				.build());
//
//
//		this.addButton(0, GuiItemBuilder.guiItemBuilder(Material.IRON_SWORD)//
//				.name(Component.translatable("tutorial_ironSword_heading", NamedTextColor.AQUA, TextDecoration.BOLD))//
//				.addLore(Component.translatable("tutorial_ironSword_Line1"), //
//						Component.translatable("tutorial_ironSword_Line2"), //
//						Component.translatable("tutorial_ironSword_Line3"), //
//						Component.translatable("tutorial_ironSword_Line4"), //
//						Component.translatable("tutorial_ironSword_Line5"), //
//						Component.translatable("tutorial_ironSword_Line6"))
//				.build());
//
//		this.addButton(8, GuiItemBuilder.guiItemBuilder(Material.LEGACY_BOOK_AND_QUILL)//
//				.name(Component.translatable("tutorial_bookAndQuill_heading", NamedTextColor.AQUA, TextDecoration.BOLD))//
//				.addLore(Component.translatable("tutorial_bookAndQuill_Line1"), //
//						Component.translatable("tutorial_bookAndQuill_Line2"), //
//						Component.translatable("tutorial_bookAndQuill_Line3", NamedTextColor.GREEN, TextDecoration.BOLD), //
//						Component.translatable("tutorial_bookAndQuill_Line4"))
//				.build());
//
//		this.addButton(9, GuiItemBuilder.guiItemBuilder(Material.LEGACY_BOOK_AND_QUILL)//
//				.name(Component.translatable("tutorial_campQuest_heading", NamedTextColor.AQUA, TextDecoration.BOLD))//
//				.addLore(Component.translatable("tutorial_campQuest_Line1"), //
//						Component.translatable("tutorial_campQuest_Line2"), //
//						Component.translatable("tutorial_campQuest_Line3"), //
//						Component.translatable("tutorial_campQuest_Line4"))
//				.build());
//
//		this.addButton(10, GuiItemBuilder.guiItemBuilder(Material.LEGACY_BOOK_AND_QUILL)//
//				.name(Component.translatable("tutorial_civQuest_heading", NamedTextColor.AQUA, TextDecoration.BOLD))//
//				.addLore(Component.translatable("tutorial_civQuest_Line1"), //
//						Component.translatable("tutorial_civQuest_Line2"), //
//						Component.translatable("tutorial_civQuest_Line3"), //
//						Component.translatable("tutorial_civQuest_Line4"), //
//						Component.translatable("tutorial_civQuest_Line5"))
//				.build());
//
//		this.addButton(11, GuiItemBuilder.guiItemBuilder(Material.LEGACY_WORKBENCH)//
//				.name(Component.translatable("tutorial_needRecipe_heading", NamedTextColor.AQUA, TextDecoration.BOLD))//
//				.addLore(Component.translatable("tutorial_needRecipe_Line1"), //
//						Component.translatable("tutorial_needRecipe_Line2"), //
//						Component.translatable("tutorial_needRecipe_Line3"), //
//						Component.translatable("tutorial_needRecipe_Line4"), //
//						Component.translatable("tutorial_needRecipe_Line5"))
//				.build());
//		CustomMaterial customMaterial = CustomMaterial.getCustomMaterial("mat_super_tnt");
//		this.addButton(19, GuiItemBuilder.guiItemBuilder(customMaterial.getMaterial())//
//				.setOpenInventory(new CraftingHelpRecipe(getPlugin(), customMaterial))
//				.build());
//		this.addButton(18, GuiItemBuilder.guiItemBuilder(CraftingHelpRecipe.getInfoBookForItem("mat_found_camp"))//
//				.setOpenInventory(CraftingHelpRecipe.class, "mat_found_camp").build());

	}
}
