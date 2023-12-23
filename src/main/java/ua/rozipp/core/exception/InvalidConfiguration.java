package ua.rozipp.core.exception;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public class InvalidConfiguration extends ComponentException {

	@Serial
	private static final long serialVersionUID = 6603010451357647626L;

	public InvalidConfiguration(@NotNull String message) {
		super(message);
	}

	public InvalidConfiguration(@NotNull ComponentException componentException) {
		super(componentException);
	}

	public InvalidConfiguration(@NotNull Component component) {
		super(component);
	}
}
