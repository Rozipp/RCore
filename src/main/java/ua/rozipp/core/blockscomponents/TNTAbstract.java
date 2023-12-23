package ua.rozipp.core.blockscomponents;

import com.destroystokyo.paper.event.block.TNTPrimeEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.RCore;
import ua.rozipp.core.blocks.CustomBlockType;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;

public abstract class TNTAbstract extends BlockComponent {

    public static NamespacedKey KEY = new NamespacedKey(RCore.getInstance(), "TNTBid");;

    public TNTAbstract() {
        super();
    }

    @Override
    public TNTPrimed onTNTPrime(TNTPrimeEvent event, TNTPrimed tNTPrimed) {
        if (tNTPrimed == null) {
            event.setCancelled(true);
            Block block = event.getBlock();
            CustomBlockType type = PluginHelper.getCustomBlockRegistry().getCustomBlockType(block);

//            block.setBlockData(Material.AIR.createBlockData());

            TNTPrimed tNTPrimedNew = (TNTPrimed) block.getWorld().spawnEntity(block.getLocation().toCenterLocation(), EntityType.PRIMED_TNT);
            tNTPrimedNew.getPersistentDataContainer().set(KEY, PersistentDataType.STRING, type.getId().asString());
            return tNTPrimedNew;
        } else
            return tNTPrimed;
    }

    public void onTNTPrimedExplode(EntityExplodeEvent event, CustomBlockType cBlock) {

    }

    @Override
    public void onBlockIgniteEvent(BlockIgniteEvent event, @NotNull Block block) {
        super.onBlockIgniteEvent(event, block);
        if (event.isCancelled()) return;
        event.setCancelled(true);
        if (!event.getBlock().getType().equals(Material.TNT))
            Bukkit.getPluginManager().callEvent(new TNTPrimeEvent(block, TNTPrimeEvent.PrimeReason.ITEM, event.getIgnitingEntity()));
    }

    @Override
    public void onBlockRedstoneEvent(BlockRedstoneEvent event) {
        super.onBlockRedstoneEvent(event);
        if (event.getNewCurrent() > event.getOldCurrent()) {
            Bukkit.getPluginManager().callEvent(new TNTPrimeEvent(event.getBlock(), TNTPrimeEvent.PrimeReason.REDSTONE, null));
            event.setNewCurrent(0);
        }
    }

}
