package ua.rozipp.core.command.rcoremenu;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.MessageHelper;
import ua.rozipp.core.command.CustomCommand;
import ua.rozipp.core.command.CustomMenuCommand;
import ua.rozipp.core.command.taber.Taber;

import java.util.ArrayList;
import java.util.List;

public class RCoreLoggerMenu extends CustomMenuCommand {

    private static List<String> loglevels;

    static {
        loglevels = new ArrayList<>();
        loglevels.add("OFF");
        loglevels.add("SEVERE");
        loglevels.add("WARNING");
        loglevels.add("INFO");
        loglevels.add("CONFIG");
        loglevels.add("FINE");
        loglevels.add("FINER");
        loglevels.add("FINEST");
        loglevels.add("ALL");
    }

    public RCoreLoggerMenu() {
        super("logger");

        add(new CustomCommand("getloggerlevel").withExecutor((sender, label, args) ->
                MessageHelper.sendSuccess(sender, Bukkit.getLogger().getLevel().getName())
        ));
        add(new CustomCommand("setloggerlevel").withExecutor((sender, label, args) -> {
                    LogHelper.setLevel(getNamedString(args, 0, ""));
                    MessageHelper.sendSuccess(sender, "Logging level changed to " + Bukkit.getLogger().getLevel().getName());
                }
        ).withTabCompleter((sender, arg) -> {
            List<String> list = new ArrayList<>();
            for (String l : loglevels) {
                if (l.toLowerCase().startsWith(arg.toLowerCase())) list.add(l);
            }
            return list;
        }));
    }
}
