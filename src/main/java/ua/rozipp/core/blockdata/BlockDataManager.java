package ua.rozipp.core.blockdata;

import org.bukkit.block.Block;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redempt.redlib.json.JSONMap;
import redempt.redlib.misc.EventListener;
import ua.rozipp.core.blockdata.backend.BlockDataBackend;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Manages persistent data attached to blocks, backed by either SQLite or chunk PersistentDataContainers
 *
 * @author Redempt
 */
public abstract class BlockDataManager {

    protected final BlockDataBackend backend;
    protected Map<ChunkPosition, Map<BlockPosition, DataBlock>> dataBlocks = new ConcurrentHashMap<>();
    protected final Map<ChunkPosition, CompletableFuture<Map<BlockPosition, DataBlock>>> loading = new ConcurrentHashMap<>();
    protected final Set<ChunkPosition> modified = Collections.synchronizedSet(new HashSet<>());
    protected final Plugin plugin;

    /**
     * Creates a BlockDataManager backed by chunk PersistentDataContainers
     *
     * @param plugin   The Plugin that owns the data
     * @param autoLoad Whether to automatically load data for newly-loaded chunks asynchronously
     * @param events   Whether to listen for events to automatically move and remove DataBlocks in response to their owning blocks being moved and removed
     * @return The created BlockDataManager
     */
    public static BlockDataManager createPDC(Plugin plugin, boolean autoLoad) {
        BlockDataBackend backend = BlockDataBackend.pdc(plugin);
        return new BlockDataManagerChunk(plugin, backend, autoLoad);
    }

    protected BlockDataManager(Plugin plugin, BlockDataBackend backend, boolean autoLoad) {
        this.backend = backend;
        this.plugin = plugin;
        new EventListener<>(plugin,
                ChunkUnloadEvent.class,
                e -> unwrap(unloadChunkAsync(new ChunkPosition(e.getChunk()))));
        if (autoLoad)
            new EventListener<>(plugin,
                    ChunkLoadEvent.class,
                    e -> unwrap(loadChunkAsync(new ChunkPosition(e.getChunk()))));
    }

    /**
     * Asynchronously retrieves a DataBlock
     *
     * @param block  The Block the data is attached to
     * @param create Whether to create a new DataBlock if one does not exist for the given Block
     * @return A CompletableFuture with the DataBlock
     */
    public CompletableFuture<DataBlock> getDataBlockAsync(Block block, boolean create) {
        BlockPosition bPos = new BlockPosition(block);
        return loadChunkAsync(bPos.getChunkPosition()).thenApply(map -> {
            DataBlock db = map.get(bPos);
            if (db == null && create) {
                db = createDataBlock(new JSONMap(), bPos);
            }
            return db;
        });
    }

    public DataBlock createDataBlock(JSONMap data, BlockPosition bPos) {
        DataBlock db = new DataBlock(data, bPos, this);
        dataBlocks.get(bPos.getChunkPosition()).put(bPos, db);
        setModified(bPos.getChunkPosition());
        return db;
    }

    protected abstract CompletableFuture<Map<BlockPosition, DataBlock>> loadChunkAsync(ChunkPosition pos);

    protected abstract CompletableFuture<Void> unloadChunkAsync(ChunkPosition pos);

    protected abstract CompletableFuture<Map<BlockPosition, DataBlock>> save(ChunkPosition pos, boolean force);

    /**
     * Saves all data loaded in this BlockDataManager
     */
    public void save() {
        List<ChunkPosition> modified = new ArrayList<>(this.modified);
        modified.forEach(c -> save(c, true));
        this.modified.clear();
        if (backend != null) unwrap(backend.saveAll());
    }

    /**
     * Saves all data loaded in this BlockDataManager and closes connections where needed
     */
    public void saveAndClose() {
        save();
        if (backend != null) unwrap(backend.close());
    }

    protected void setModified(ChunkPosition pos) {
        modified.add(pos);
    }

    /**
     * Removes a DataBlock and its data from this BlockDataManager
     *
     * @param db The DataBlock to remove
     */
    public void remove(@NotNull DataBlock db) {
        BlockPosition bPos = db.getBlockPosition();
        setModified(bPos.getChunkPosition());
        Optional.ofNullable(dataBlocks.get(db.getChunkPosition())).ifPresent(m -> m.remove(bPos));
        db.remove();
    }

    /**
     * Moves a DataBlock to a new newLocation asynchronously
     *
     * @param db          The DataBlock whose data should be moved
     * @param newLocation The Block to move the data to
     * @return A CompletableFuture for the moving task
     */
    protected CompletableFuture<DataBlock> moveAsync(@NotNull DataBlock db, Block newLocation) {
        remove(db);
        setModified(new ChunkPosition(newLocation));
        return getDataBlockAsync(newLocation, true).thenApply(b -> {
            b.data = db.data;
            return b;
        });
    }

    /**
     * Moves a DataBlock to a new location
     *
     * @param db          The DataBlock whose data should be moved
     * @param newLocation The Block to move the data to
     * @return The new DataBlock
     */
    public DataBlock move(DataBlock db, Block newLocation) {
        return unwrap(moveAsync(db, newLocation));
    }

    /**
     * Gets the DataBlocks for a given chunk, if it is loaded already
     *
     * @param pos@return The DataBlocks if they are loaded, otherwise an empty collection
     */
    public Collection<DataBlock> getLoaded(ChunkPosition pos) {
        return Optional.ofNullable(dataBlocks.get(pos)).map(Map::values).orElseGet(ArrayList::new);
    }

    /**
     * @return All DataBlocks currently loaded in this BlockDataManager
     */
    public Collection<DataBlock> getAllLoaded() {
        return dataBlocks.values().stream().flatMap(m -> m.values().stream()).collect(Collectors.toSet());
    }

    /**
     * Gets a DataBlock for the given Block
     *
     * @param block  The Block data will be attached to
     * @param create Whether to create a new DataBlock if one does not exist already
     * @return The DataBlock, or null
     */
    public @Nullable DataBlock getDataBlock(Block block, boolean create) {
        return unwrap(getDataBlockAsync(block, create));
    }

    /**
     * Gets a DataBlock, creating one if it doesn't exist
     *
     * @param block The Block data will be attached to
     * @return A DataBlock
     */
    public @Nullable DataBlock getDataBlock(Block block) {
        return getDataBlock(block, false);
    }

    protected <T> T unwrap(CompletableFuture<T> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

}
