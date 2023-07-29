package ua.rozipp.core.command.rcoremenu;

import net.kyori.adventure.text.Component;
import ua.rozipp.core.command.CustomMenuCommand;
import ua.rozipp.core.command.Validators;

public class RCoreMenu extends CustomMenuCommand {
    public RCoreMenu() {
        super("rcore");
        withDescription(Component.text("Сервисное меню плагина RCore"));
        withValidator(Validators.mustBeAdmin);
        add(new RCoreItemsMenu());
        add(new RCoreBlocksMenu());
        add(new RCoreReloadMenu());
        add(new RCoreLocaleMenu());
        add(new RCoreLoggerMenu());
    }
}
