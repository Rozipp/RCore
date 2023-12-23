package ua.rozipp.core.blocks;

import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import redempt.redlib.json.JSONMap;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.blockdata.BlockDataListener;
import ua.rozipp.core.blockdata.BlockDataManager;
import ua.rozipp.core.blockdata.BlockPosition;
import ua.rozipp.core.blockdata.DataBlock;
import ua.rozipp.core.blockdata.events.CustomBlockBuildEvent;
import ua.rozipp.core.blockdata.events.CustomBlockCanBuildEvent;
import ua.rozipp.core.items.ItemHelper;

import java.util.*;

/**
 * Loads and registers CBlockTs
 *
 * @author Redempt
 */
public class CustomBlockRegistry implements Listener {

	public static final String TAGNAME = "RBID";
	private final BlockDataManager manager;
	private final Map<Key, CustomBlockType> types = new HashMap<>();
	private final Map<Key, CustomBlockType> byItemName = new HashMap<>();

	/**
	 * Construct a CustomBlockRegistry, passing a plugin. Use this constructor if you plan to use
	 * {@link CustomBlockRegistry#register(CustomBlockType)}
	 *
	 * @param manager The BlockDataManager to use for managing block data
	 * @param plugin  The Plugin to register events with
	 */
	public CustomBlockRegistry(BlockDataManager manager, Plugin plugin) {
		this.manager = manager;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		Bukkit.getPluginManager().registerEvents(new CustomBlockListener(this, manager, plugin), plugin);
		Bukkit.getPluginManager().registerEvents(new BlockDataListener(this, manager), plugin);
	}

	public void createCustomBlock(Block block, BlockFace face, CustomBlockType type, ItemStack item, Player player) {
		if (type == null || block == null) return;

		DataBlock db = manager.getDataBlock(block);
		if (db != null) manager.remove(db);

		if (!block.getType().equals(type.getBlockMaterial())) {
			BlockData blockData = type.getBlockMaterial().createBlockData();
			if (face != null && blockData instanceof Directional) ((Directional) blockData).setFacing(face);
			block.setBlockData(blockData);
		}

		JSONMap data = new JSONMap();
		data.put(TAGNAME, type.getId().asString());
		manager.createDataBlock(data, new BlockPosition(block));
		Bukkit.getPluginManager().callEvent(new CustomBlockBuildEvent(block, type, item, player));
	}

	/**
	 * Gets a CustomBlock instance with the correct CBlockT
	 *
	 * @param block The Block to check
	 * @return The CustomBlock, or null if it was not a custom block
	 */
	public @Nullable CustomBlockType getCustomBlockType(Block block) {
		return getCustomBlockType(manager.getDataBlock(block, false));
	}

	/**
	 * Gets a CustomBlock instance with the correct CBlockT
	 *
	 * @param db The DataBlock to check
	 * @return The CustomBlock, or null if it was not a custom block
	 */
	public @Nullable CustomBlockType getCustomBlockType(DataBlock db) {
		if (db != null && db.contains(TAGNAME)) {
			Key key = NamespacedKey.fromString(db.getString(TAGNAME));
			if (key != null && types.containsKey(key))
				return types.get(key);
		}
		return null;
	}

	/**
	 * @return The BlockDataManager managing block data for this CustomBlockRegistry
	 */
	public BlockDataManager getManager() {
		return manager;
	}

	/**
	 * Registers a single CBlockT into this CustomBlockRegistry
	 *
	 * @param type The CBlockT to register
	 */
	public void register(CustomBlockType type) {
		byItemName.put(type.getId(), type);
		types.put(type.getItemId(), type);
	}

	public void unregisterAll(Plugin plugin) {
		List<CustomBlockType> customBlockTypes = new ArrayList<>();
		for (CustomBlockType customBlockType : types.values()) {
			if (customBlockType.getId().namespace().equalsIgnoreCase(plugin.getName()))
				customBlockTypes.add(customBlockType);
		}
		for (CustomBlockType customBlockType : customBlockTypes) {
			types.remove(customBlockType.getId());
			byItemName.remove(customBlockType.getItemId());
		}
		LogHelper.fine("Unregistered " + customBlockTypes.size() + " CustomBlock");
	}

	/**
	 * Gets a CBlockT by key
	 *
	 * @param key The key of the CBlockT
	 * @return The CBlockT with the given key
	 */
	public @Nullable CustomBlockType getByKey(@Nullable Key key) {
		if (key == null) return null;
		return types.get(key);
	}

	/**
	 * @return A collection of all CBlockTs in this registry
	 */
	public Collection<CustomBlockType> getTypes() {
		return types.values();
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockPlaceEvent(BlockPlaceEvent e) {
		CustomBlockType type = byItemName.get(ItemHelper.getKey(e.getItemInHand()));
		if (type == null) return;

		CustomBlockCanBuildEvent place = new CustomBlockCanBuildEvent(e.getBlock(), e.getItemInHand(), type, e.getPlayer());
		Bukkit.getPluginManager().callEvent(place);
		if (!place.isBuildable()) {
			e.setCancelled(true);
			return;
		}

		//TODO Благодаря этому фиксу не будет на месте одного блока сразу две DataBlock, но блок удаляется неправильно
		DataBlock db = manager.getDataBlock(e.getBlock());
		if (db != null) manager.remove(db);

		createCustomBlock(e.getBlockPlaced(), e.getBlockAgainst().getFace(e.getBlockPlaced()), type, e.getItemInHand(), e.getPlayer());
	}

}
