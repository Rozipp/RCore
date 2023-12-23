package ua.rozipp.core.blockdata;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import ua.rozipp.core.blockdata.events.DataBlockLoadEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BlockDataManagerEntity extends BlockDataManager {

    protected BlockDataManagerEntity(Plugin plugin, boolean autoLoad, boolean events) {
        super(plugin, null, autoLoad, events);
    }

    @Override
    public CompletableFuture<Map<BlockPosition, DataBlock>> loadChunkAsync(ChunkPosition pos) {
        if (dataBlocks.containsKey(pos)) return CompletableFuture.completedFuture(dataBlocks.get(pos));

        CompletableFuture<Map<BlockPosition, DataBlock>> load = loading.get(pos);
        if (load != null && !load.isDone()) return load;

        dataBlocks.put(pos, new HashMap<>());

        load = CompletableFuture.supplyAsync(() -> {
            Chunk chunk = pos.getChunk();
            if (!chunk.isLoaded() && !chunk.load()) return null;
            for (Entity entity : chunk.getEntities()) {
                if (entity.getType().equals(EntityType.ARMOR_STAND)) {
                    DataBlock db = DataBlock.loadFromArmorStand((ArmorStand) entity, this);
                    if (db != null) {
                        dataBlocks.get(pos).put(db.getBlockPosition(), db);
                        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new DataBlockLoadEvent(db)));
                    }
                }
            }
            loading.remove(pos);
            return dataBlocks.get(pos);
        });
        loading.put(pos, load);
        return load;
    }

    @Override
    protected CompletableFuture<Void> unloadChunkAsync(ChunkPosition pos) {
        CompletableFuture<Map<BlockPosition, DataBlock>> load = loading.remove(pos);
        if (load != null) {
            load.cancel(true);
            dataBlocks.remove(pos);
            return CompletableFuture.completedFuture(null);
        }
        return save(pos, false).thenRun(() -> dataBlocks.remove(pos));
    }

    @Override
    protected CompletableFuture<Map<BlockPosition, DataBlock>> save(ChunkPosition pos, boolean force) {
        if (!force && !modified.contains(pos)) return CompletableFuture.completedFuture(null);
        modified.remove(pos);
        return CompletableFuture.completedFuture(dataBlocks.get(pos)).thenApply(blocks -> {
            if (blocks != null) {
                if (blocks.size() == 0)
                    dataBlocks.remove(pos);
                else
                    blocks.forEach((k, v) -> v.save());
            }
            return blocks;
        });
    }

}
