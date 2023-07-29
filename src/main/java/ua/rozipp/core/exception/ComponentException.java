package ua.rozipp.core.exception;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import ua.rozipp.core.util.ComponentUtils;

public class ComponentException extends Exception {

    private static final long serialVersionUID = -945041178494359650L;

    private final Component component;

    public ComponentException(@NotNull String message) {
        super(message);
        this.component = Component.text(message, NamedTextColor.RED);
    }

    public ComponentException(@NotNull ComponentException componentException) {
        super(componentException.getMessage());
        this.component = componentException.getComponent();
    }

    public ComponentException(@NotNull TextComponent component) {
        super(component.content());
        this.component = component.color(NamedTextColor.RED);
    }

    public ComponentException(@NotNull TranslatableComponent translatableComponent) {
        super(ComponentUtils.componentedToString(translatableComponent));
        this.component = translatableComponent.color(NamedTextColor.RED);
    }

    public Component getComponent() {
        return component;
    }

    @Override
    public String getMessage() {
        return ComponentUtils.componentedToString(getComponent());
    }

}