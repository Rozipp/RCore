package ua.rozipp.core.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.MessageHelper;
import ua.rozipp.core.util.TextUtils;

import java.util.*;

/**
 * Регистрирует класы CustomCommand как команды плагина
 *
 * @author ua.rozipp
 */
public class CommanderRegistration {

    private static final Map<String, CustomCommand> registeredCommands = new HashMap<>();
    protected static CommandMap cmap;

    public static void register(JavaPlugin plugin, CustomCommand customCommand) {
        if (customCommand.getString_cmd() == null || customCommand.getString_cmd().isEmpty())
            throw new CommandNotPreparedException("Command does not have a name.");
        PluginCommand c = plugin.getCommand(customCommand.getString_cmd());
        if (c != null) {
            c.setExecutor((sender, cmd, label, args) -> customCommand.onCommand(sender, label, args));
            c.setTabCompleter((sender, cmd, label, args) -> customCommand.onTab(sender, label, args));
            if (customCommand.getAliases() != null) c.setAliases(customCommand.getAliases());
            if (customCommand.getDescription() != null)
                c.setDescription(TextUtils.componentToString(customCommand.getDescription()));
            if (customCommand.getPermission() != null) c.setPermission(customCommand.getPermission());
            if (customCommand.getPermissionMessage() != null)
                c.setPermissionMessage(customCommand.getPermissionMessage());
            if (customCommand.getUsage() != null) c.setUsage(customCommand.getUsage());
        } else {
            Command command = new ReflectCommand(customCommand);
            getCommandMap().register(plugin.getName(), command);
        }
        registeredCommands.put(customCommand.getString_cmd(), customCommand);
    }

    public static void unregisterAllCommands(Plugin plugin) {
        List<CustomCommand> unregisterCommand = new ArrayList<>();
        for (CustomCommand command : registeredCommands.values()) {
            if (command.getPlugin().equals(plugin)) {
                unregisterCommand.add(command);
            }
        }
        int count = 0;
        for (CustomCommand customCommand : unregisterCommand) {
            Command command = getCommandMap().getCommand(customCommand.getString_cmd());
            if (command != null)
                command.unregister(getCommandMap());
            customCommand.withExecutor((sender, label, args) -> MessageHelper.sendError(sender, "This Command is not active"));
            count++;
            Bukkit.getServer().getCommandMap().getKnownCommands().remove(customCommand.getString_cmd().toLowerCase(Locale.ENGLISH));
            Bukkit.getServer().getCommandMap().getKnownCommands().remove((plugin.getName() + ":" + customCommand.getString_cmd()).toLowerCase(Locale.ENGLISH));
            registeredCommands.remove(customCommand.getString_cmd());
        }
        LogHelper.fine("Unregistered " + count + "\\" + unregisterCommand.size() + " CustomCommand");
    }

    private static CommandMap getCommandMap() {
        if (cmap == null) {
            cmap = Bukkit.getCommandMap();
//only Paper
//            try {
//                final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
//                f.setAccessible(true);
//                cmap = (CommandMap) f.get(Bukkit.getServer());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
        return cmap;
    }

    public static class CommandNotPreparedException extends RuntimeException {
        public CommandNotPreparedException(String message) {
            super(message);
        }
    }

}
