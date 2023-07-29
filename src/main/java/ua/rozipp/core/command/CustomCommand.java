package ua.rozipp.core.command;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
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
	private final List<Taber> tabs = new ArrayList<>();

	public CustomCommand(String string_cmd) {
		this.string_cmd = string_cmd;
	}

	public static Player getPlayer(CommandSender sender) throws ComponentException {
		if (sender instanceof Player) return (Player) sender;
		throw new ComponentException(Component.translatable("cmd_MustBePlayer"));
	}

	public static Integer getNamedInteger(String[] args, int index) throws ComponentException {
		if (args.length < (index + 1)) throw new ComponentException(Component.translatable("cmd_enterNumber"));
		try {
			return Integer.valueOf(args[index]);
		} catch (NumberFormatException e) {
			throw new ComponentException(Component.text(args[index])
					.append(Component.space())
					.append(Component.translatable("cmd_enterNumerError2").args(Component.text(index + 1))));
		}
	}

	public static Double getNamedDouble(String[] args, int index) throws ComponentException {
		if (args.length < (index + 1)) throw new ComponentException(Component.translatable("cmd_enterNumber"));
		try {
			return Double.valueOf(args[index]);
		} catch (NumberFormatException e) {
			throw new ComponentException(Component.text(args[index] + " ")
					.append(Component.translatable("cmd_enterNumerError").args(Component.text(index + 1))));
		}
	}

	public static String getNamedString(String[] args, int index, String message) throws ComponentException {
		if (args.length < (index + 1)) throw new ComponentException(message);
		return args[index];
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

	public void valide(CommandSender sender) throws ComponentException {
		if (validators != null)
			for (Validator v : validators)
				v.isValide(sender);
		if (getPermission() != null && !getPermission().isEmpty()){
			if (!sender.hasPermission(getPermission()))
				throw new ComponentException(Component.translatable("cmd_noPermission"));
		}

	}

	public boolean onCommand(CommandSender sender, String label, String[] args) {
		try {
			if (executor == null) throw new ComponentException("Команда в разработке");
			valide(sender);
			executor.run(sender, label, args);
			return true;
		} catch (ComponentException e) {
			sender.sendMessage(e.getComponent());
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
		return plugin;
	}

	public interface CustomExecutor {
		void run(CommandSender sender, String label, String[] args) throws ComponentException;
	}


}