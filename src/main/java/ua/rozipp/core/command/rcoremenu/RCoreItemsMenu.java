package ua.rozipp.core.command.rcoremenu;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ua.rozipp.core.MessageHelper;
import ua.rozipp.core.command.CustomCommand;
import ua.rozipp.core.command.CustomMenuCommand;
import ua.rozipp.core.command.Validators;
import ua.rozipp.core.command.taber.CashedTaber;
import ua.rozipp.core.command.taber.Tabers;
import ua.rozipp.core.exception.ComponentException;
import ua.rozipp.core.guiinventory.CraftingHelp;
import ua.rozipp.core.guiinventory.ItemsSpawn;
import ua.rozipp.core.items.CustomMaterial;
import ua.rozipp.core.items.ItemHelper;

import java.util.ArrayList;
import java.util.List;

public class RCoreItemsMenu extends CustomMenuCommand {
    public RCoreItemsMenu() {
        super("items");
        withDescription(Component.translatable("cmd.rcore.items.description"));
        withValidator(Validators.mustBeAdmin);

        add(new CustomCommand("itemsgui").withDescription(Component.translatable("cmd.rcore.items.itemsgui.description")).withExecutor((sender, label, args) -> {
            new ItemsSpawn(getPlugin(), null).open(getPlayer(sender));
        }));

        add(new CustomCommand("recipesgui").withDescription(Component.translatable("cmd.rcore.items.recipesgui.description")).withExecutor((sender, label, args) -> {
            new CraftingHelp(getPlugin(), null).open(getPlayer(sender));
        }));

        add(new CustomCommand("give").withExecutor((sender, label, args) -> {
            Player player = getPlayer(sender);
            String arg1 = getNamedString(args, 0, "Недостаточно аргументов give <player> <custom_material_id> <amount>");
            Player player2 = Bukkit.getPlayer(arg1);
            String mid;
            int amaunt = 1;
            if (player2 == null) {
                mid = arg1;
                try {
                    amaunt = getNamedInteger(args, 1);
                } catch (ComponentException ignored) {
                }
            } else {
                mid = getNamedString(args, 1, "Введите айди кастомного предмета");
                try {
                    amaunt = getNamedInteger(args, 2);
                } catch (ComponentException ignored) {
                }
            }

            CustomMaterial cmat = CustomMaterial.getCustomMaterial(mid);
            if (cmat == null) throw new ComponentException(Component.text("Предмет " + mid + " не найден"));
            player.getInventory().addItem(cmat.spawn(amaunt));
            MessageHelper.send(player, Component.translatable("cmd_tnt_targetGive", NamedTextColor.GREEN).args(
                    Component.text(cmat.getName()),
                    Component.text(amaunt)));
            if (!player.equals(sender)) {
                MessageHelper.send(sender, Component.translatable("cmd_tnt_senderGive", NamedTextColor.GREEN).args(
                        Component.text(cmat.getName()),
                        Component.text(amaunt),
                        Component.text(player.getName())));
            }
        }).withTabCompleter(Tabers.onlinePlayer).withTabCompleter(new CashedTaber("give") {
            @Override
            protected List<String> newTabList(String arg) {
                List<String> list = new ArrayList<>();
                for (Key mid : CustomMaterial.midValues()) {
                    String s = mid.asString();
                    if (s.startsWith(arg)) list.add(s);
                }
                return list;
            }
        }));
        add(new CustomCommand("showNBT").withExecutor((sender, label, args) ->
                sender.sendMessage(ItemHelper.getAllProperty(getPlayer(sender).getInventory().getItemInMainHand()))));
    }
}
