package ua.rozipp.core.blockdata.backend;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import ua.rozipp.core.blockdata.BlockDataManager;
import ua.rozipp.core.blockdata.ChunkPosition;
import ua.rozipp.core.blockdata.DataBlock;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

class PDCBackend implements BlockDataBackend {
	
	private final NamespacedKey key;
	
	public PDCBackend(Plugin plugin) {
		key = new NamespacedKey(plugin, "blockData");
	}
	
	@Override
	public CompletableFuture<String> load(ChunkPosition pos) {
		PersistentDataContainer pdc = pos.getChunk().getPersistentDataContainer();
		return CompletableFuture.completedFuture(pdc.get(key, PersistentDataType.STRING));
	}
	
	@Override
	public CompletableFuture<Void> save(ChunkPosition pos, String data) {
		PersistentDataContainer pdc = pos.getChunk().getPersistentDataContainer();
		pdc.set(key, PersistentDataType.STRING, data);
		return CompletableFuture.completedFuture(null);
	}
	
	@Override
	public CompletableFuture<Void> remove(ChunkPosition pos) {
		PersistentDataContainer pdc = pos.getChunk().getPersistentDataContainer();
		pdc.remove(key);
		return CompletableFuture.completedFuture(null);
	}
	
	@Override
	public CompletableFuture<Collection<DataBlock>> saveAll() {
		return CompletableFuture.completedFuture(null);
	}
	
	@Override
	public CompletableFuture<Void> close() {
		return CompletableFuture.completedFuture(null);
	}
	
	@Override
	public CompletableFuture<Map<ChunkPosition, String>> loadAll() {
		throw new UnsupportedOperationException("PDC backend cannot access all data blocks");
	}
	
	@Override
	public boolean attemptMigration(BlockDataManager manager) {
		return false;
	}
	
}
