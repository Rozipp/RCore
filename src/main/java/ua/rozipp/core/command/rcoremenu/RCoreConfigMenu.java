package ua.rozipp.core.command.rcoremenu;

import net.kyori.adventure.text.Component;
import org.bukkit.plugin.Plugin;
import ua.rozipp.core.LocaleHelper;
import ua.rozipp.core.MessageHelper;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.RCore;
import ua.rozipp.core.command.CustomCommand;
import ua.rozipp.core.command.CustomMenuCommand;
import ua.rozipp.core.command.Validators;
import ua.rozipp.core.command.taber.Taber;
import ua.rozipp.core.config.RConfigMultiFiles;
import ua.rozipp.core.exception.ComponentException;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.CustomMaterialBuilder;
import ua.rozipp.core.recipes.CustomRecipe;

import java.util.ArrayList;
import java.util.List;

public class RCoreConfigMenu extends CustomMenuCommand {

    public static Taber pluginTaber = (sender, arg) -> {
        List<String> list = new ArrayList<>();
        for (Plugin plugin : PluginHelper.getRegistryPlugins()) {
            if (plugin.getName().startsWith(arg)) list.add(plugin.getName());
        }
        return list;
    };

    public RCoreConfigMenu() {
        super("config");
        withValidator(Validators.mustBeAdmin);

        add(new CustomCommand("reloadAll").withExecutor((sender, label, args) -> {
                    Plugin plugin;
                    try {
                        plugin = getNamedPlugin(args, 0);
                    } catch (ComponentException e) {
                        plugin = RCore.getInstance();
                    }
                    PluginHelper.getRConfigMultiFiles(plugin).reload();
                }
        ).withTabCompleter(pluginTaber));

        add(new CustomCommand("reloadLocale").withExecutor((sender, label, args) -> {
            Plugin plugin = getNamedPlugin(args, 0);
            PluginHelper.getRConfigMultiFiles(plugin).reload();
            LocaleHelper.reloadLocaleFile(plugin);
            MessageHelper.sendSuccess(sender, Component.translatable("cmd.rcore.config.reloadLocaleSuccess").args(Component.text(plugin.getName())));
        }).withTabCompleter(pluginTaber));

        add(new CustomCommand("reloadMaterials").withExecutor((sender, label, args) -> {
            Plugin plugin = getNamedPlugin(args, 0);
            PluginHelper.getRConfigMultiFiles(plugin).reload();
            try {
                CustomMaterialBuilder.loadAll(PluginHelper.getRConfigMultiFiles(plugin));
            } catch (InvalidConfiguration e) {
                e.printStackTrace();
                throw new ComponentException(e.getMessage());
            }
        }).withTabCompleter(pluginTaber));

        add(new CustomCommand("reloadRecipes").withExecutor((sender, label, args) -> {
            Plugin plugin = getNamedPlugin(args, 0);
            try {
                CustomRecipe.loadAll(plugin, PluginHelper.getRConfigMultiFiles(plugin));
            } catch (InvalidConfiguration e) {
                e.printStackTrace();
                throw new ComponentException(e.getMessage());
            }
        }).withTabCompleter(pluginTaber));

        add(new CustomCommand("getConfig").withExecutor((sender, label, args) -> {
            RConfigMultiFiles cmf = PluginHelper.getRConfigMultiFiles(RCore.getInstance());

            if (args.length == 0) {
                for (String key : cmf.getKeys(true)) {
                    sender.sendMessage(key);
                }
                return;
            }

            String key = getNamedString(args, 0, "Введите имя ключа");
            Object o = cmf.get(key);
            if (o instanceof List) {
                List<?> list = ((List<?>) o);
                try {
                    int index = getNamedInteger(args, 1);
                    if (index < 0 || index >= list.size())
                        throw new ComponentException("Введите число между 0 і " + list.size());
                    sender.sendMessage("Node " + index + ": " + list.get(index));
                } catch (ComponentException e) {
                    sender.sendMessage("List size=" + list.size());
                }
            } else
                sender.sendMessage((o == null) ? "null" : o.toString());
        }));
    }
}
