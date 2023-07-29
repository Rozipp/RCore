package ua.rozipp.core.exception;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

public class GuiException extends ComponentException {

	public GuiException(String message) {
		super(message);
	}

	public GuiException(@NotNull TextComponent component) {
		super(component);
	}

	public GuiException(@NotNull TranslatableComponent translatableComponent) {
		super(translatableComponent);
	}
}
