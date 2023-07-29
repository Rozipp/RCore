package ua.rozipp.core.command.rcoremenu;

import org.bukkit.plugin.Plugin;
import ua.rozipp.core.LocaleHelper;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.RCore;
import ua.rozipp.core.command.CustomCommand;
import ua.rozipp.core.command.CustomMenuCommand;
import ua.rozipp.core.command.Validators;
import ua.rozipp.core.command.taber.Taber;
import ua.rozipp.core.exception.ComponentException;

import java.util.ArrayList;
import java.util.List;

public class RCoreReloadMenu extends CustomMenuCommand {

    public static Taber pluginTaber = (sender, arg) -> {
        List<String> list = new ArrayList<>();
        for (Plugin plugin : PluginHelper.getRegistryPlugins()) {
            if (plugin.getName().startsWith(arg)) list.add(plugin.getName());
        }
        return list;
    };

    public static Plugin getNamedPlugin(String[] args, int index) throws ComponentException {
        Plugin plugin;
        try {
            String pluginName = getNamedString(args, index, "Enter plugin name");
            plugin = PluginHelper.getPlugin(pluginName);
        } catch (Exception e) {
            plugin = PluginHelper.getPlugin(RCore.class);
        }
        return plugin;
    }

    public RCoreReloadMenu() {
        super("reload");
        withValidator(Validators.mustBeAdmin);

        add(new CustomCommand("locale").withExecutor((sender, label, args) ->
                LocaleHelper.reloadLocaleFile(getNamedPlugin(args, 0))
        ).withTabCompleter(pluginTaber));

        add(new CustomCommand("materials").withExecutor((sender, label, args) -> {
//            CustomMaterial.load(getNamedPlugin(args, 0), );
        }).withTabCompleter(pluginTaber));
    }
}
