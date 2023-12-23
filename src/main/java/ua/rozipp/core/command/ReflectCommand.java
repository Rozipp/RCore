package ua.rozipp.core.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.PluginsCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Клас-обертка для регистрации класов CustomCommand как команды плагина
 *
 * @author ua.rozipp
 */
class ReflectCommand extends PluginsCommand {

    private CustomCommand customCommand;

    ReflectCommand(CustomCommand customCommand) {
        super(customCommand.getString_cmd());
        setCustomCommand(customCommand);
    }

    public void setCustomCommand(CustomCommand customCommand) {
        this.customCommand = customCommand;
        if (customCommand.getAliases() != null) this.setAliases(customCommand.getAliases());
//			if (custonCommand.getDescription() != null) this.setDescription(custonCommand.getDescription().insertion());
        if (customCommand.getPermission() != null) this.setPermission(customCommand.getPermission());
        if (customCommand.getPermissionMessage() != null)
            this.setPermissionMessage(customCommand.getPermissionMessage());
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
