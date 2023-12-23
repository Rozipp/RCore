// 
// Decompiled by Procyon v0.5.36
// 

package ua.rozipp.core.listener;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.Plugin;

public class ChunkListener implements Listener {
    private final Plugin plugin;

    public ChunkListener(final Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldUnload(final WorldUnloadEvent event) {
        final World world = event.getWorld();
//        this.apiHologramManager.onWorldUnload(world);
//        this.v2HologramManager.onWorldUnload(world);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldLoad(final WorldLoadEvent event) {
        final World world = event.getWorld();
//        this.apiHologramManager.onWorldLoad(world);
//        this.v2HologramManager.onWordLoad(world);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkUnload(final ChunkUnloadEvent event) {
        final Chunk chunk = event.getChunk();
        if (Bukkit.isPrimaryThread())
            this.onChunkUnload(chunk);
        else
            Bukkit.getScheduler().runTask(this.plugin, () -> this.onChunkUnload(chunk));
    }

    private void onChunkUnload(final Chunk chunk) {
//        this.apiHologramManager.onChunkUnload(chunk);
//        this.v2HologramManager.onChunkUnload(chunk);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkLoad(final ChunkLoadEvent event) {
        final Chunk chunk = event.getChunk();
        if (!chunk.isLoaded()) return;
        if (Bukkit.isPrimaryThread())
            this.onChunkLoad(chunk);
        else
            Bukkit.getScheduler().runTask(this.plugin, () -> this.onChunkLoad(chunk));
    }

    private void onChunkLoad(final Chunk chunk) {
//        this.apiHologramManager.onChunkLoad(chunk);
//        this.v2HologramManager.onChunkLoad(chunk);
    }
}
