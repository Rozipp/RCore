package ua.rozipp.core.command.taber;

import org.bukkit.command.CommandSender;

import java.util.List;

/** интерфейс для класов дополнения по клавише Tab
 * @author ua.rozipp */
public interface Taber {
	List<String> getTabList(CommandSender sender, String arg);
}
