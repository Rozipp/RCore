package ua.rozipp.core.gui.action;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.gui.InventoryGUI;
import ua.rozipp.core.gui.ItemButton;
import ua.rozipp.core.items.CustomMaterial;
import ua.rozipp.core.items.ItemStackBuilder;

public class OpenInventoryButton extends ItemButton {

	private final InventoryGUI inventoryGUI;

	public OpenInventoryButton(Material material, InventoryGUI inventoryGUI, String name, Component... lore) {
		super(new ItemStackBuilder(material)
				.name(Component.translatable("gui.button.openInventory.name").args(Component.text(name)))
				.addLore(lore)
				.addLore(Component.text("<Click To Open>", NamedTextColor.GOLD))//
				.build());
		this.inventoryGUI = inventoryGUI;
	}

	public OpenInventoryButton(ItemStack itemStack, InventoryGUI inventoryGUI, Component... lore) {
		super(new ItemStackBuilder(itemStack)
				.addLore(lore)
				.addLore(Component.text("<Click To Open>", NamedTextColor.GOLD))//
				.build());
		this.inventoryGUI = inventoryGUI;
	}

	@Override
	public void onClick(InventoryClickEvent e) {
		inventoryGUI.open((Player) e.getWhoClicked());
	}
}
