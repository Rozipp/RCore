package ua.rozipp.core;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MessageHelper {

	/** Stores the player name and the hash code of the last message sent to prevent error spamming the player. */
	private static final HashMap<UUID, Integer> lastErrorMessage = new HashMap<>();

	/**Что бы не было лишнего флуда, сравнивает последнее сообщение об ошибке и если оно такое же, то ничего не сообщает.*/
	@Deprecated
	public static void sendErrorNoRepeat(Audience sender, String message) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Integer hash = lastErrorMessage.get(player.getUniqueId());
			if (hash != null && hash == message.hashCode()) return;
		}
		sendError(sender, message);
	}

	/**Что бы не было лишнего флуда, сравнивает последнее сообщение об ошибке и если оно такое же, то ничего не сообщает.*/
	public static void sendErrorNoRepeat(Audience sender, Component message) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Integer hash = lastErrorMessage.get(player.getUniqueId());
			if (hash != null && hash == message.hashCode()) return;
		}
		sendError(sender, message);
	}

	/**Сообщение об ошибке. Окрашивает сообщение в красный цвет*/
	@Deprecated
	public static void sendError(Audience sender, String message) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			lastErrorMessage.put(player.getUniqueId(), message.hashCode());
		}
		send(sender, ChatColor.RED + message);
	}

	/**Сообщение об ошибке. Окрашивает сообщение в красный цвет*/
	public static void sendError(Audience sender, Component message) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			lastErrorMessage.put(player.getUniqueId(), message.hashCode());
		}
		send(sender, message.color(NamedTextColor.RED));
	}

	/**Сообщение об удачном выполнение операции*/
	@Deprecated
	public static void sendSuccess(Audience sender, String message) {
		send(sender, ChatColor.GREEN + message);
	}
	/**Сообщение об удачном выполнение операции*/
	public static void sendSuccess(Audience sender, Component message) {
		send(sender, message.color(NamedTextColor.GREEN));
	}

	@Deprecated
	public static void sendTitle(Audience sender, String title, String subTitle) {
		sender.showTitle(Title.title(convertColorStringToComponentText(title), convertColorStringToComponentText(subTitle)));
	}

	@Deprecated
	public static void send(Audience sender, @NotNull String line) {
		if (sender instanceof Player)
			((Player) sender).sendMessage(line);
		else if (sender instanceof ConsoleCommandSender)
			((ConsoleCommandSender) sender).sendMessage(line);
		else send(sender, convertColorStringToComponentText(line));
	}

	public static void send(Audience sender, @NotNull Component line) {
		if (sender == null) return;
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage(GlobalTranslator.render(line, Locale.getDefault()));
		} else sender.sendMessage(line);
	}

	@Deprecated
	public static void sendAsList(Audience sender, List<String> lines) {
		TextComponent.Builder builder = Component.text();
		for (int i = 0; i < lines.size() - 1; i++) {
			builder.append(convertColorStringToComponentText(lines.get(i) + ", "));
		}
		builder.append(convertColorStringToComponentText(lines.get(lines.size() - 1)));
		send(sender, builder.build());
	}

	@Deprecated
	public static void sendAsList(Audience sender, String... lines) {
		sendAsList(sender, Arrays.asList(lines));
	}

	public static Component buildTitle(String title) {
		String line = "-------------------------------------------------";
		TextComponent.Builder titleBracket = Component.text()
				.append(Component.text("[ ", NamedTextColor.DARK_AQUA))
				.append(convertColorStringToComponentText(title))
				.append(Component.text(" ]", NamedTextColor.DARK_AQUA));

		int newLength = title.length() + 4;
		if (newLength > line.length()) {
			return Component.text("-").append(titleBracket).append(Component.text("-"));
		}

		int min = (line.length() / 2) - newLength / 2;
		int max = (line.length() / 2) + newLength / 2;

		String pre = line.substring(min);
		String post = line.substring(max);

		return Component.text(pre).append(titleBracket).append(Component.text(post));
	}

	public static void sendHeading(Audience sender, String title) {
		send(sender, buildTitle(title));
	}
	//TODO
	public static void sendHeading(Audience sender, Component title) {
		sender.sendMessage(title);
	}

	/* А черт его знает что оно делает. Оставил как загадку для будущих поколений */
	public static String plurals(final int count, final String... pluralForms) {
		final int i = (count % 10 == 1 && count % 100 != 11) ? 0 : ((count % 10 >= 2 && count % 10 <= 4 && (count % 100 < 10 || count % 100 >= 20)) ? 1 : 2);
		return pluralForms[i];
	}

	/**
	 * TODO Переводит строковые литералы в форматирование компонентов
	 */
	public static Component convertColorStringToComponentText(String string) {
		return Component.text(string);
	}

    public static void global(Component completed) {
		//TODO
    }
}
