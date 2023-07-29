package ua.rozipp.core.command;

import org.bukkit.command.CommandSender;
import ua.rozipp.core.exception.ComponentException;

public interface Validator {
    void isValide(CommandSender sender) throws ComponentException;
}
