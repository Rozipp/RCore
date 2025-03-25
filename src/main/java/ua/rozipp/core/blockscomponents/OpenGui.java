package ua.rozipp.core.blockscomponents;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.ComponentException;
import ua.rozipp.core.exception.InvalidConfiguration;

public class OpenGui extends BlockComponent {

    private String gui_name;
    private Plugin plugin;

    public OpenGui() {
        super();
    }

    public OpenGui(Plugin plugin, String gui_name) {
        super();
        this.gui_name = gui_name;
        this.plugin = plugin;
    }

    @Override
    protected void load(RConfig rConfig) throws InvalidConfiguration {
        this.gui_name = rConfig.getString("gui_name", null, "GuiInventory name not fount");
        this.plugin = rConfig.getPlugin();
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            try {
                PluginHelper.getGuiManager().getGui(gui_name, null).open(event.getPlayer());
                event.setCancelled(true);
            } catch (ComponentException e) {
                LogHelper.error(e.getComponent());
            }
        }
    }
}
