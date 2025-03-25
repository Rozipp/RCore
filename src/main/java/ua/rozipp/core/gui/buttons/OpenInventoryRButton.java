package ua.rozipp.core.gui.buttons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ua.rozipp.core.MessageHelper;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.exception.ComponentException;
import ua.rozipp.core.gui.RGui;
import ua.rozipp.core.gui.slots.RButton;
import ua.rozipp.core.items.ItemStackBuilder;

public class OpenInventoryRButton extends RButton {

	private RGui rGui;
	private Class<? extends RGui> rGuiClass;
	private String rGuiName;
	private Object arg;

	public OpenInventoryRButton(@NotNull ItemStackBuilder itemStackBuilder) {
		super();
		setItemStack(itemStackBuilder
				.addLore(
//								Component.translatable("gui.lore.clickToOpen")
						Component.text("<Click To Open>", NamedTextColor.GOLD)
				)
				.build());
		setConsumer(event -> {
			RGui rGui = null;
			if (this.rGui != null)
				rGui = this.rGui;
			else
				try {
					if (rGuiClass != null)
						rGui = PluginHelper.getGuiManager().getGui(rGuiClass, arg);
					else if (rGuiName != null)
						rGui = PluginHelper.getGuiManager().getGui(rGuiName, arg);
				} catch (ComponentException e) {
					MessageHelper.sendError(event.getWhoClicked(), e.getComponent());
					return;
				}
			if (rGui != null) rGui.open((Player) event.getWhoClicked());
		});
	}

	public OpenInventoryRButton setRGui(RGui rGui) {
		this.rGui = rGui;
		return this;
	}

	public OpenInventoryRButton setRGuiFromClass(Class<? extends RGui> rGuiClass, Object arg) {
		this.rGuiClass = rGuiClass;
		this.arg = arg;
		return this;
	}

	public OpenInventoryRButton setRGuiFromName(String rGuiName, Object arg) {
		this.rGuiName = rGuiName;
		this.arg = arg;
		return this;
	}
}
