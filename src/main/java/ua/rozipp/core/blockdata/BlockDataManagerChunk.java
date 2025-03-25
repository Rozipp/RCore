package ua.rozipp.core.blockdata;

import org.bukkit.plugin.Plugin;
import redempt.redlib.json.JSONMap;
import redempt.redlib.json.JSONParser;
import ua.rozipp.core.blockdata.backend.BlockDataBackend;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Manages persistent data attached to blocks, backed by either SQLite or chunk PersistentDataContainers
 *
 * @author Redempt
 */
public class BlockDataManagerChunk extends BlockDataManager {

    BlockDataManagerChunk(Plugin plugin, BlockDataBackend backend, boolean autoLoad) {
        super(plugin, backend, autoLoad);
    }

    @Override
    public CompletableFuture<Map<BlockPosition, DataBlock>> loadChunkAsync(ChunkPosition pos) {
        if (dataBlocks.containsKey(pos)) return CompletableFuture.completedFuture(dataBlocks.get(pos));
        CompletableFuture<Map<BlockPosition, DataBlock>> load = loading.get(pos);
        if (load != null && !load.isDone()) return load;

        dataBlocks.put(pos, new HashMap<>());

        load = backend.load(pos).thenApply(s -> {
            if (s != null) {
                JSONMap map = JSONParser.parseMap(s);
                map.keySet().forEach(key -> {
                    String[] split = key.split(",");
                    DataBlock db = new DataBlock(map.getMap(key),
                            new BlockPosition(split[0],
                                    Integer.parseInt(split[1]),
                                    Integer.parseInt(split[2]),
                                    Integer.parseInt(split[3])),
                            this);
                    dataBlocks.get(pos).put(db.getBlockPosition(), db);
                });
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
                if (blocks.size() == 0) {
                    dataBlocks.remove(pos);
                    unwrap(backend.remove(pos));
                } else {
                    JSONMap map = new JSONMap();
                    blocks.forEach((k, v) -> map.put(k.toString(), v.data));
                    String data = map.toString();
                    unwrap(backend.save(pos, data));
                }
            }
            return blocks;
        });
    }

}
