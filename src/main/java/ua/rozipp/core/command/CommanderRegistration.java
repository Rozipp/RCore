package ua.rozipp.core.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ua.rozipp.core.LogHelper;

import java.lang.reflect.Field;
import java.util.*;

/** Регистрирует класы CustomCommand как команды плагина
 * @author ua.rozipp */
public class CommanderRegistration {

	private static final Map<String, CustomCommand> registeredCommands = new HashMap<>();
	protected static CommandMap cmap;
	public static int count = 0;

	public static void register(JavaPlugin plugin, CustomCommand customCommand) {
		String fromPlugin = plugin.getName();
		PluginCommand c = plugin.getCommand(customCommand.getString_cmd());
		if (c != null){
			c.setExecutor((sender1, cmd1, label1, args1) -> customCommand.onCommand(sender1, label1, args1));
			c.setTabCompleter((sender, cmd, label, args) -> customCommand.onTab(sender, label, args));
		} else {
			ReflectCommand command;
			if (customCommand.getString_cmd() != null && !customCommand.getString_cmd().isEmpty())
				command = new ReflectCommand(customCommand);
			else
				throw new CommandNotPreparedException("Command does not have a name.");
			getCommandMap().register(fromPlugin, command);
		}
		registeredCommands.put(customCommand.getString_cmd(), customCommand);
		count++;
	}

	public static void unregister(CustomCommand customCommand) {
		Command command = getCommandMap().getCommand(customCommand.getString_cmd());
		if (command != null)
			command.unregister(getCommandMap());
		registeredCommands.remove(customCommand.getString_cmd());
	}

	public static void unregisterAllCommands(Plugin plugin) {
		List<CustomCommand> unregisterCommand = new ArrayList<>();
		for (CustomCommand command : registeredCommands.values()) {
			if (command.getPlugin().equals(plugin)) {
				unregisterCommand.add(command);
			}
		}
		for (CustomCommand command: unregisterCommand){
			unregister(command);
		}
		LogHelper.fine("Unregistered " + unregisterCommand.size() + " CustomCommand");
	}

	private static CommandMap getCommandMap() {
		if (cmap == null) {
			try {
				final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
				f.setAccessible(true);
				cmap = (CommandMap) f.get(Bukkit.getServer());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			return cmap;
		return getCommandMap();
	}

	/** Клас-обертка для регистрации класов CustomCommand как команды плагина
	 * @author ua.rozipp */
	private static final class ReflectCommand extends Command {

		private final CustomCommand customCommand;

		private ReflectCommand(CustomCommand customCommand) {
			super(customCommand.getString_cmd());
			this.customCommand = customCommand;
			if (customCommand.getAliases() != null) this.setAliases(customCommand.getAliases());
//			if (custonCommand.getDescription() != null) this.setDescription(custonCommand.getDescription().insertion());
			if (customCommand.getPermission() != null) this.setPermission(customCommand.getPermission());
			if (customCommand.getPermissionMessage() != null) this.setPermissionMessage(customCommand.getPermissionMessage());
			if (customCommand.getUsage() != null) this.setUsage(customCommand.getUsage());
		}

		@Override
		public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
			return customCommand.onCommand(sender, label, args);
		}

		@Override
		public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, String[] args) {
			return customCommand.onTab(sender, label, args);
		}
	}

	@SuppressWarnings("serial")
	public static class CommandNotPreparedException extends RuntimeException {
		public CommandNotPreparedException(String message) {
			super(message);
		}
	}

}
