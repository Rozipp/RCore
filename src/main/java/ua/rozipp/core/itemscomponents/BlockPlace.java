package ua.rozipp.core.itemscomponents;

import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ua.rozipp.core.blocks.CustomBlockType;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.ItemStackBuilder;

public class BlockPlace extends ItemComponent {

    private CustomBlockType customBlockType;

    public BlockPlace() {
        super();
    }

    public BlockPlace(CustomBlockType customBlockType) {
        super();
        this.customBlockType = customBlockType;
    }

    @Override
    protected void load(RConfig rConfig) throws InvalidConfiguration {
        Key mid = getCustomMaterial().getMid();
        Plugin plugin = Bukkit.getPluginManager().getPlugin(mid.namespace());
        if (plugin == null) throw new InvalidConfiguration("Not Found plugin '" + mid.namespace() + "'");
        Key key = mid.namespace().equals(plugin.getName()) ? mid : new NamespacedKey(plugin, mid.value());
        customBlockType = CustomBlockType.builder(key, mid).load(rConfig);
    }

    private CustomBlockType getCustomBlock() {
        return customBlockType;
    }

    @Override
    public void onBuildItemStack(ItemStackBuilder builder) {
        super.onBuildItemStack(builder);
    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && e.getClickedBlock() != null
                && getCustomBlock() != null
                && e.useInteractedBlock() == Event.Result.ALLOW
                && !e.getItem().getType().isBlock()
                && e.getHand() != null) {
            Player player = e.getPlayer();
            ItemStack item = (e.getHand() == EquipmentSlot.HAND)
                    ? player.getInventory().getItemInMainHand()
                    : player.getInventory().getItemInOffHand();

            Block placed = e.getClickedBlock().getRelative(e.getBlockFace());
            BlockState state = placed.getState();
            Block against = e.getClickedBlock();

            BlockPlaceEvent event = new BlockPlaceEvent(placed, state, against, item, player, true, e.getHand());
            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                if (player.getGameMode() != GameMode.CREATIVE)
                    item.setAmount(item.getAmount() - 1);
            }
            e.setUseInteractedBlock(Event.Result.DENY);
            e.setUseItemInHand(Event.Result.DENY);
        }
    }

}
