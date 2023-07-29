package ua.rozipp.core.command;

import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

/** Клас псевдо меню. Отличаеться от меню тем, что у него не подкоманды, а набор статических аргументов
 * @author ua.rozipp */
public abstract class SelectorAbstractCommand extends CustomCommand {
	private final List<String> selectorCommands = new ArrayList<>();
	private final List<Component> selectorDescriptions = new ArrayList<>();

	public SelectorAbstractCommand(String string_cmd, CustomExecutor executor) {
		super(string_cmd);
		initSubCommands();
		this.withTabCompleter((sender, arg) -> {
			List<String> l = new ArrayList<>();
			for (String s : selectorCommands) {
				if (s.startsWith(arg)) l.add(s);
			}
			return l;
		});
		this.withExecutor((sender, label, args) -> {
			if (args.length < 1) {
				for (int i = 0; i < selectorCommands.size(); i++) {
					sender.sendMessage(Component.text("(" + i + ") ")
							.append(Component.text(selectorCommands.get(i)))
							.append(Component.space())
							.append(Component.space())
							.append(Component.space())
							.append(selectorDescriptions.get(i)));
				}
				return;
			}
			executor.run(sender, label, args);
		});
	}

	public abstract void initSubCommands();

	public void add(String string_cmd, Component description) {
		selectorCommands.add(string_cmd);
		selectorDescriptions.add(description);
	}

}