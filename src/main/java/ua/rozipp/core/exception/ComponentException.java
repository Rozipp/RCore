package ua.rozipp.core.exception;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import ua.rozipp.core.util.TextUtils;

import java.io.Serial;

public class ComponentException extends Exception {

    @Serial
    private static final long serialVersionUID = -945041178494359650L;

    private final Component component;

    public ComponentException(@NotNull String message) {
        super(message);
        this.component = Component.text(message);
    }

    public ComponentException(@NotNull ComponentException componentException) {
        super(componentException.getMessage());
        this.component = componentException.getComponent();
    }

    public ComponentException(@NotNull Component component) {
        super(TextUtils.componentToString(component));
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }

    @Override
    public String getMessage() {
        return TextUtils.componentToString(getComponent());
    }

}