package ua.rozipp.core.command.rcoremenu;

import net.kyori.adventure.text.Component;
import ua.rozipp.core.command.CustomCommand;
import ua.rozipp.core.command.CustomMenuCommand;
import ua.rozipp.core.command.Validators;

public class RCoreBlocksMenu extends CustomMenuCommand {
    public RCoreBlocksMenu() {
        super("blocks");
        withDescription(Component.text("Меню управления кастомными блоками"));
        withValidator(Validators.mustBeAdmin);
        add(new CustomCommand("placehear"));
        add(new CustomCommand("placehearlook"));
    }
}
