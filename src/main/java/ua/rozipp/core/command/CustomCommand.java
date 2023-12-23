package ua.rozipp.core.command;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.MessageHelper;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.RCore;
import ua.rozipp.core.command.taber.Taber;
import ua.rozipp.core.exception.ComponentException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
/**  <p>
 * Общий клас для команд. После создания команды, добавление параметров возможно как через сеттеры (set...()), так через билдеры (with..())
 * </p>
 * @param executor    - вызываться при выполнении команды. Обязательный параметр.
 * @param description - хранит описание, для вывода в help-е подменю - List<String> aliases - Вариванты альтернативных команд
 * @param validator   - Проверка команды на доступность для CommandSender. Обрабатываються в порядке добавления;
 * @param tabs         - Класы дополнения клавишей Tab. Обрабатываються в порядке добавления
 * @author ua.rozipp */
public class CustomCommand implements PluginIdentifiableCommand {

	private Plugin plugin;
	private final String string_cmd;
	private Component description = Component.space();
	private List<String> aliases = null;
	private String usage = null;
	private String permission = null;
	private String permissionMessage = null;
	private final List<Validator> validators = new ArrayList<>();
	private CustomExecutor executor = null;
	private List<Taber> tabs = new ArrayList<>();
	@Setter
	private CustomCommand parentCommand;

	public CustomCommand(String string_cmd) {
		this.string_cmd = string_cmd;
	}

	public static Player getPlayer(CommandSender sender) throws ComponentException {
		if (sender instanceof Player) return (Player) sender;
		throw new ComponentException(Component.translatable("cmd.error.mustBePlayer"));
	}

	public static int getNamedInteger(String[] args, int index) throws ComponentException {
		if (args.length <= index) throw new ComponentException(Component.translatable("cmd.error.enterNumber"));
		try {
			return Integer.parseInt(args[index]);
		} catch (NumberFormatException e) {
			throw new ComponentException(Component.text(args[index])
					.append(Component.space())
					.append(Component.translatable("cmd.error.enterInteger").args(Component.text(index + 1))));
		}
	}

	public static double getNamedDouble(String[] args, int index) throws ComponentException {
		if (args.length <= index) throw new ComponentException(Component.translatable("cmd.error.enterNumber"));
		try {
			return Double.parseDouble(args[index]);
		} catch (NumberFormatException e) {
			throw new ComponentException(Component.text(args[index] + " ")
					.append(Component.translatable("cmd.error.enterDouble").args(Component.text(index + 1))));
		}
	}

	public static String getNamedString(String[] args, int index, String message) throws ComponentException {
		if (args.length <= index) throw new ComponentException(message);
		return args[index];
	}

	public static String getNamedString(String[] args, int index, Component component) throws ComponentException {
		if (args.length <= index) throw new ComponentException(component);
		return args[index];
	}

	public static Plugin getNamedPlugin(String[] args, int index) throws ComponentException {
		Plugin plugin;
		try {
			String pluginName = getNamedString(args, index, Component.translatable("cmd.error.enterPluginName"));
			plugin = PluginHelper.getPlugin(pluginName);
		} catch (Exception e) {
			plugin = PluginHelper.getPlugin(RCore.class);
		}
		return plugin;
	}

	public CustomCommand withValidator(Validator validator) {
		this.addValidator(validator);
		return this;
	}

	public CustomCommand withExecutor(CustomExecutor commandExecutor) {
		this.executor = commandExecutor;
		return this;
	}

	public CustomCommand withPermissionMessage(String message) {
		this.permissionMessage = ChatColor.translateAlternateColorCodes('&', message);
		return this;
	}

	public CustomCommand withPermission(String permission) {
		this.permission = permission;
		return this;
	}

	public CustomCommand withUsage(String usage) {
		this.usage = usage;
		return this;
	}

	public CustomCommand withAliases(String... aliases) {
		this.aliases = Arrays.asList(aliases);
		return this;
	}

	public CustomCommand withDescription(Component description) {
		this.description = description;
		return this;
	}

	public CustomCommand withTabCompleter(Taber tab) {
		this.addTab(tab);
		return this;
	}

	public void addTab(Taber tab) {
		this.tabs.add(tab);
	}

	public void addValidator(Validator validator) {
		this.validators.add(validator);
	}

	public void setAliases(String... aliases) {
		this.aliases = Arrays.asList(aliases);
	}

	public void valid(CommandSender sender) throws ComponentException {
		for (Validator v : validators)
			v.isValide(sender);
		if (getPermission() != null && !getPermission().isEmpty()) {
			if (!sender.hasPermission(getPermission()))
				if (permissionMessage != null)
					throw new ComponentException(Component.text(permissionMessage));
				else
					throw new ComponentException(Component.translatable("cmd.error.noPermission"));
		}

	}

	public boolean onCommand(CommandSender sender, String label, String[] args) {
		try {
			if (executor == null) throw new ComponentException(Component.translatable("cmd.error.underDevelopment"));
			valid(sender);
			executor.run(sender, label, args);
			return true;
		} catch (ComponentException e) {
			MessageHelper.send(sender, e.getComponent());
			return false;
		}
	}

	public List<String> onTab(CommandSender sender, String label, String[] args){
		int index = args.length - 1;
		if (index >= 0 && index < getTabs().size()) {
			return getTabs().get(index).getTabList(sender, args[index].toLowerCase());
		}
		return new ArrayList<>();
	}

	public void register(JavaPlugin plugin) {
		this.plugin = plugin;
		CommanderRegistration.register(plugin, this);
	}

	@Override
	public @NotNull Plugin getPlugin() {
		return (parentCommand != null) ? getParentCommand().getPlugin() : plugin;
	}

	public void setTabs(List<Taber> tabs) {
		this.tabs = tabs;
	}

	public interface CustomExecutor {
		void run(CommandSender sender, String label, String[] args) throws ComponentException;
	}

}