package ua.rozipp.core.command.rcoremenu;

import org.bukkit.entity.Player;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.command.CustomCommand;
import ua.rozipp.core.command.CustomMenuCommand;
import ua.rozipp.core.command.Validators;
import ua.rozipp.core.exception.ComponentException;
import ua.rozipp.core.gui.RGui;

import java.util.ArrayList;
import java.util.List;

public class RCoreGuiMenu extends CustomMenuCommand {
    public RCoreGuiMenu() {
        super("gui");
        withValidator(Validators.mustBeAdmin);

        add(new CustomCommand("openGui").withExecutor((sender, label, args) -> {
            Player player = getPlayer(sender);
            String guiName = getNamedString(args, 0, "Enter gui name");
            String arg;
            try {
                arg = getNamedString(args, 1, "Enter arg");
            } catch (ComponentException e) {
                arg = null;
            }
            RGui gui = PluginHelper.getGuiManager().getGui(guiName, arg);
            if (gui == null) throw new ComponentException("GuiName " + guiName + " not found");
            gui.open(player);
        }).withTabCompleter((sender, arg) -> {
            List<String> list = new ArrayList<>();
            for (String s : PluginHelper.getGuiManager().inventoryGUINames()) {
                if (s.startsWith(arg)) list.add(s);
            }
            return list;
        }));

        add(new CustomCommand("openGuiTest").withExecutor((sender, label, args) -> {
            Player player = getPlayer(sender);
//            String guiName = getNamedString(args, 0, "Enter gui name");
//            String arg;
//            try {
//                arg = getNamedString(args, 1, "Enter arg");
//            } catch (ComponentException e) {
//                arg = null;
//            }
//            InventoryGUI gui = InventoryGUI.getInventoryGUI(guiName, RCore.getInstance(), arg);
//            if (gui == null) throw new ComponentException("GuiName " + guiName + " not found");
//            PaginationPanel panel = new PaginationPanel(gui);

//            List<ItemStack> list = new ArrayList<>();
//            for (CustomMaterial cMat : CustomMaterial.values()){
//                list.add(cMat.spawn());
//            }
//
//            ClaimItemsMenu t = new ClaimItemsMenu(getPlugin(), 9, list);
//            DragPage t = new DragPage(getPlugin());
//            player.openInventory(t.getInventory());
        }));
    }
}
