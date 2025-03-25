package ua.rozipp.core.guiinventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import ua.rozipp.core.gui.RGui;
import ua.rozipp.core.gui.RGuiImp;

public class BookGUI extends RGuiImp {

	public BookGUI(Object arg) {
		super(3, Component.translatable("bookReborn_heading", NamedTextColor.GREEN));
	}

	@Override
    public void onUpdate() {
		/*
		this.addGuiItem(0, GuiItemBuilder.guiItemBuilder(Material.SKELETON_SKULL)//
				.setOpenInventory(ResidentPage.class, null)//
				.name(Component.translatable("bookReborn_infoMenu"))//
				.addLore(Component.translatable("bookReborn_clickToView", NamedTextColor.GOLD))
				.build());
		this.addGuiItem(1, GuiItemBuilder.guiItemBuilder(Material.NAME_TAG)
						.setOpenInventory(InventoryGUI.class, null)
				.name(Component.translatable("bookReborn_diplomaticMenu"))//
				.addLore(Component.translatable("bookReborn_clickToView", NamedTextColor.GOLD))//
				.build());
//		this.addGuiItem(2, GuiItem.guiItemBuilder(Material.BLAZE_POWDER)//
//						.setAction(GuiAction.CONFIRMATION)
//						.setName(CivSettings.localize.translate("bookReborn_civSpaceMenu"))//
//				.addLore("§6" + CivSettings.localize.translate("bookReborn_clickToView"))//
//				.build());

		this.addGuiItem(4, GuiItemBuilder.guiItemBuilder(Material.LEGACY_WORKBENCH)
				.setOpenInventory(CraftingHelp.class, null)
				.name(Component.translatable("bookReborn_craftMenu"))//
				.addLore(Component.translatable("bookReborn_clickToView", NamedTextColor.GOLD))//
				.build());
//		this.addGuiItem(5, GuiItem.guiItemBuilder()//
//				.setName(CivSettings.localize.translate("bookReborn_newsMenu"))//
//				.setMaterial(Material.PAPER)//
//				.setLore("§6" + CivSettings.localize.translate("bookReborn_clickToView"))//
//				.setOpenInventory("NewsPaper", null));
//
//		this.addGuiItem(7, GuiItem.guiItemBuilder()//
//				.setName(CivSettings.localize.translate("bookReborn_dynmapMenu"))//
//				.setMaterial(Material.LADDER)//
//				.setLore("§6" + CivSettings.localize.translate("bookReborn_clickToView"))//
//				.setAction("BookLinksAction"));
//		this.addGuiItem(8, GuiItem.guiItemBuilder()//
//				.setName(CivSettings.localize.translate("bookReborn_gameInfoMenu"))//
//				.setMaterial(Material.WRITTEN_BOOK)//
//				.setLore("§6" + CivSettings.localize.translate("bookReborn_clickToView"))//
//				.setOpenInventory("Tutorial", null));
//		this.addGuiItem(9, GuiItem.guiItemBuilder()//
//				.setName(CivSettings.localize.translate("bookReborn_buildMenu"))//
//				.setMaterial(Material.SLIME_BLOCK)//
//				.setLore("§6" + CivSettings.localize.translate("bookReborn_clickToView"))//
//				.setOpenInventory("Structure", "false"));
//
//		this.addGuiItem(17, GuiItem.guiItemBuilder()//
//				.setName(CivSettings.localize.translate("bookReborn_donateMenu"))//
//				.setMaterial(Material.GOLD_INGOT)//
//				.setLore("§6" + CivSettings.localize.translate("bookReborn_clickToView"))//
//				.setAction("BookShowDonateMenu"));
//		this.addGuiItem(18, GuiItem.guiItemBuilder()//
//				.setName(CivSettings.localize.translate("bookReborn_techMenu"))//
//				.setMaterial(Material.POTION)//
//				.setAction("BookTechsGui"));
//		this.addGuiItem(19, GuiItem.guiItemBuilder()//
//				.setName(CivSettings.localize.translate("bookReborn_upgradeMenu"))//
//				.setMaterial(Material.ANVIL)//
//				.setLore("§6" + CivSettings.localize.translate("bookReborn_clickToView"))//
//				.setOpenInventory("UpgradeBuy", "true"));
//
//		this.addGuiItem(26, GuiItem.guiItemBuilder()//
//				.setName(CivSettings.localize.translate("bookReborn_perkMenu"))//
//				.setMaterial(Material.ENCHANTED_BOOK)//
//				.setLore("§6" + CivSettings.localize.translate("bookReborn_clickToView"))//
//				.setOpenInventory("PerkPage", null));*/
	}

}
