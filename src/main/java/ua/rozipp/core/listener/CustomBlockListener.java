package ua.rozipp.core.listener;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import ua.rozipp.core.blocks.CustomBlock;

public class CustomBlockListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        Block block = event.getBlock();
        CustomBlock cblock = CustomBlock.getCustomBlock(event.getBlock());
        if (cblock != null) cblock.onBlockBreak(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBurnEvent(BlockBurnEvent event) {
        if (event.isCancelled()) return;
        CustomBlock cblock = CustomBlock.getCustomBlock(event.getBlock());
        if (cblock != null) cblock.onBlockBurnEvent(event);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockCanBuildEvent(BlockCanBuildEvent event) {
        CustomBlock cblock = CustomBlock.getCustomBlock(event.getBlock());
        if (cblock != null) cblock.onBlockCanBuildEvent(event);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockCookEvent(BlockCookEvent event) {
        if (event.isCancelled()) return;
        CustomBlock cblock = CustomBlock.getCustomBlock(event.getBlock());
        if (cblock != null) cblock.onBlockCookEvent(event);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockDamageEvent(BlockDamageEvent event) {
        if (event.isCancelled()) return;
        Block block = event.getBlock();
        CustomBlock cblock = CustomBlock.getCustomBlock(event.getBlock());
        if (cblock != null) cblock.onBlockDamageEvent(event);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockDispenseEvent(BlockDispenseEvent event) {
        if (event.isCancelled()) return;
        CustomBlock cblock = CustomBlock.getCustomBlock(event.getBlock());
        if (cblock != null) cblock.onBlockDispenseEvent(event);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockExplodeEvent(BlockExplodeEvent event) {
        if (event.isCancelled()) return;
        CustomBlock cblock = CustomBlock.getCustomBlock(event.getBlock());
        if (cblock != null) cblock.onBlockExplodeEvent(event);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockFadeEvent(BlockFadeEvent event) {
        if (event.isCancelled()) return;
        CustomBlock cblock = CustomBlock.getCustomBlock(event.getBlock());
        if (cblock != null) event.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockFertilizeEvent(BlockFertilizeEvent event) {
        if (event.isCancelled()) return;
        CustomBlock cblock = CustomBlock.getCustomBlock(event.getBlock());
        if (cblock != null) event.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockFromToEvent(BlockFromToEvent event) {
        if (event.isCancelled()) return;
        CustomBlock cblock = CustomBlock.getCustomBlock(event.getBlock());
        if (cblock != null) event.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockGrowEvent(BlockGrowEvent event) {
        if (event.isCancelled()) return;
        CustomBlock cblock = CustomBlock.getCustomBlock(event.getBlock());
        if (cblock != null) event.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockIgniteEvent(BlockIgniteEvent event) {
        if (event.isCancelled()) return;
        CustomBlock cblock = CustomBlock.getCustomBlock(event.getBlock());
        if (cblock != null) event.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        if (event.isCancelled()) return;
        CustomBlock cblock = CustomBlock.getCustomBlock(event.getBlock());
        if (cblock != null) cblock.onBlockPlaceEvent(event);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockRedstoneEvent(BlockRedstoneEvent event) {
        CustomBlock cblock = CustomBlock.getCustomBlock(event.getBlock());
        if (cblock != null) cblock.onBlockRedstoneEvent(event);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.isCancelled() || event.getClickedBlock() == null) return;
        CustomBlock cblock = CustomBlock.getCustomBlock(event.getClickedBlock());
        if (cblock != null) cblock.onPlayerInteractEvent(event);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onLeavesDecayEvent(LeavesDecayEvent event) {
        if (event.isCancelled()) return;
        CustomBlock cblock = CustomBlock.getCustomBlock(event.getBlock());
        if (cblock != null) event.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onMoistureChangeEvent(MoistureChangeEvent event) {
        if (event.isCancelled()) return;
        CustomBlock cblock = CustomBlock.getCustomBlock(event.getBlock());
        if (cblock != null) event.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSignChangeEvent(SignChangeEvent event) {
        if (event.isCancelled()) return;
        CustomBlock cblock = CustomBlock.getCustomBlock(event.getBlock());
        if (cblock != null) event.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSpongeAbsorbEvent(SpongeAbsorbEvent event) {
        if (event.isCancelled()) return;
        CustomBlock cblock = CustomBlock.getCustomBlock(event.getBlock());
        if (cblock != null) event.setCancelled(true);
    }



}
