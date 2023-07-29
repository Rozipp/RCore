package ua.rozipp.core.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;
import ua.rozipp.core.MessageHelper;
import ua.rozipp.core.command.taber.Taber;
import ua.rozipp.core.exception.ComponentException;
import ua.rozipp.core.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Абстрактный клас служит оболочкой для создания подменю
 * </p>
 *
 * @author ua.rozipp
 */
public abstract class CustomMenuCommand extends CustomCommand {

    protected Component displayName = Component.text("FIXME");
    private final List<CustomCommand> subCommands = new ArrayList<>();

    public CustomMenuCommand(String perentCommand) {
        super(perentCommand);
        displayName = Component.text("/" + perentCommand);
        CustomMenuCommand parent = this;
        this.withExecutor((sender, label, args) -> {
            boolean finished = false;
            if (args.length == 0) {
                parent.doDefaultAction(sender);
            } else if (args[0].equalsIgnoreCase("help")) {
                parent.showBasicHelp(sender);
            } else {
                String newcomm = args[0];
                String[] newargs = stripArgs(args, 1);
                try {
                    int index = Integer.parseInt(newcomm);
                    if (index < 0 || index >= subCommands.size())
                        throw new ComponentException("Недопустимый индекс команды");
                    CustomCommand cc = subCommands.get(index);
                    cc.onCommand(sender, label + " " + cc.getString_cmd(), newargs);
                    finished = true;
                } catch (NumberFormatException ignored) {
                }
                if (!finished) {
                    for (CustomCommand cc : parent.getSubCommands()) {
                        if (cc.getString_cmd().equalsIgnoreCase(newcomm)) {
                            cc.onCommand(sender, label + " " + cc.getString_cmd(), newargs);
                            finished = true;
                            break;
                        }
                        if (cc.getAliases() != null && cc.getAliases().contains(newcomm.toLowerCase())) {
                            cc.onCommand(sender, label + " " + cc.getString_cmd(), newargs);
                            finished = true;
                            break;
                        }
                    }
                    if (!finished)
                        throw new ComponentException("Command not found");
                }
            }
        });
        this.addTab(new MenuCustomTab(this, perentCommand));
    }

    public static String[] stripArgs(String[] someArgs, int amount) {
        if (amount >= someArgs.length) return new String[0];
        String[] argsLeft = new String[someArgs.length - amount];
        System.arraycopy(someArgs, amount, argsLeft, 0, argsLeft.length);
        return argsLeft;
    }

    public void add(CustomCommand menu) {
        this.subCommands.add(menu);
    }

    /* Called when no arguments are passed. */
    public void doDefaultAction(CommandSender sender) {
        showBasicHelp(sender);
    }

    public void showBasicHelp(CommandSender sender) {
        MessageHelper.send(sender, Component.translatable("cmd_CommandHelpTitle", NamedTextColor.LIGHT_PURPLE).args(displayName.color(NamedTextColor.GOLD)));
        MessageHelper.send(sender, Component.text("------- /", NamedTextColor.LIGHT_PURPLE)
                .append(Component.text(getString_cmd(), NamedTextColor.GREEN, TextDecoration.BOLD))
                .append(Component.text(" help -------", NamedTextColor.LIGHT_PURPLE)));

        for (int i = 0; i < subCommands.size(); i++) {
            CustomCommand command = subCommands.get(i);
            try {
                command.valide(sender);
            } catch (ComponentException e) {
                continue;
            }

            String nomer = "(" + i + ")";

            StringBuilder altComms = new StringBuilder();
            if (command.getAliases() != null) for (String s : command.getAliases())
                altComms.append(" (").append(s).append(")");

            sender.sendMessage(Component.text(StringUtils.addTabToString(nomer, 3), NamedTextColor.GREEN)
                    .append(Component.text(StringUtils.addTabToString(command.getString_cmd() + altComms, 12), NamedTextColor.LIGHT_PURPLE))
                    .append(command.getDescription()));
        }
    }

    public List<CustomCommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public List<String> onTab(CommandSender sender, String label, String[] args) {
        if (args.length == 1) {
            return getTabs().get(0).getTabList(sender, args[0].toLowerCase());
        }
        String arg = args[0];
        String[] newargs = stripArgs(args, 1);
        try {
            int index = Integer.parseInt(arg);
            CustomCommand cc = this.getSubCommands().get(index);
            return cc.onTab(sender, label + " " + arg, newargs);
        } catch (Exception ignored) {
        }

        for (CustomCommand cc : this.getSubCommands()) {
            if (cc.getString_cmd().equalsIgnoreCase(arg))
                return cc.onTab(sender, label + " " + arg, newargs);
            if (cc.getAliases() != null) {
                for (String al : cc.getAliases()) {
                    if (al.equalsIgnoreCase(arg))
                        return cc.onTab(sender, label + " " + arg, newargs);
                }
            }
        }
        return new ArrayList<>();
    }

    public static class MenuCustomTab implements Taber {
        CustomMenuCommand perent;
        String cmdName;

        public MenuCustomTab(CustomMenuCommand perent, String cmdName) {
            this.cmdName  = cmdName;
            this.perent = perent;
        }

        @Override
        public List<String> getTabList(CommandSender sender, String arg) {
            List<String> tabList = new ArrayList<>();
            for (CustomCommand command : perent.getSubCommands()) {
                try {
                    command.valide(sender);
                } catch (ComponentException e) {
                    continue;
                }
                if (command.getString_cmd().startsWith(arg)) tabList.add(command.getString_cmd());
            }
            return tabList;
        }

    }

}
