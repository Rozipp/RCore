package ua.rozipp.core.command;

import net.kyori.adventure.text.Component;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import ua.rozipp.core.exception.ComponentException;

public class Validators {

    /** Разрешает выполнение админских команд связынных.*/
    public static Validator mustBeAdmin = sender -> {
        if (!(sender instanceof ConsoleCommandSender)) {
            Player player = CustomCommand.getPlayer(sender);
            if (!player.isOp()) {
                throw new ComponentException(Component.translatable("cmd_mustBeOnlyAdmin"));
            }
        }
    };
}
