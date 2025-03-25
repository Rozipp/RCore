package ua.rozipp.core.blockdata;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.blockdata.events.*;
import ua.rozipp.core.blockdata.events.DataBlockDestroyEvent.DestroyCause;
import ua.rozipp.core.blocks.CustomBlockRegistry;
import ua.rozipp.core.blocks.CustomBlockType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockDataListener implements Listener {

	private final BlockDataManager manager;
	private final CustomBlockRegistry registry;

	public BlockDataListener(CustomBlockRegistry registry, BlockDataManager manager) {
		this.manager = manager;
		this.registry = registry;
	}

	private void fireDestroy(DataBlock db, Event parent, DestroyCause cause) {
		if (db == null) return;
		CustomBlockType type = registry.getCustomBlockType(db);
		if (type != null) {
			CustomBlockCanDestroyEvent canDestroy = new CustomBlockCanDestroyEvent(db, type, parent, cause);
			Bukkit.getPluginManager().callEvent(canDestroy);
			if (canDestroy.isDestroyable()) {
				Bukkit.getPluginManager().callEvent(new CustomBlockDestroyEvent(db, type, parent, cause));
				manager.remove(db);
				db.getBlock().setType(Material.AIR);
			}
		} else {
			DataBlockDestroyEvent dbDestroy = new DataBlockDestroyEvent(db, parent, cause);
			Bukkit.getPluginManager().callEvent(dbDestroy);
			if (!dbDestroy.isCancelled()) {
				manager.remove(db);
				db.getBlock().setType(Material.AIR);
			}
		}
	}

	private void handleExplosion(List<Block> blocks, Cancellable e) {
		List<DataBlock> toRemove = new ArrayList<>();
		blocks.forEach(b -> {
			DataBlock db = manager.getDataBlock(b);
			if (db == null) return;
			CustomBlockType type = registry.getCustomBlockType(db);
			if (type != null) {
				CustomBlockCanDestroyEvent canDestroy = new CustomBlockCanDestroyEvent(db, type, (Event) e, DestroyCause.EXPLOSION);
				Bukkit.getPluginManager().callEvent(canDestroy);
				if (canDestroy.isDestroyable()) toRemove.add(db);
			} else {
				DataBlockDestroyEvent dbDestroy = new DataBlockDestroyEvent(db, (Event) e, DestroyCause.EXPLOSION);
				Bukkit.getPluginManager().callEvent(dbDestroy);
				if (!dbDestroy.isCancelled()) toRemove.add(db);
			}
		});
		if (e.isCancelled()) return;
		toRemove.forEach(db -> {
			CustomBlockType type = registry.getCustomBlockType(db);
			if (type != null) {
				Bukkit.getPluginManager().callEvent(new CustomBlockDestroyEvent(db, type, (Event) e, DestroyCause.EXPLOSION));
			}
			manager.remove(db);
			db.getBlock().setType(Material.AIR);
		});
	}

	private void handlePiston(List<Block> blocks, BlockPistonEvent e) {
		List<DataBlock> toMoveDB = new ArrayList<>();
		for (Block b : blocks) {
			DataBlock db = manager.getDataBlock(b);
			if (db == null) continue;

			Block destination = db.getBlock().getRelative(e.getDirection());
			DataBlockCanMoveEvent ev = new DataBlockCanMoveEvent(db, destination, e);
			Bukkit.getPluginManager().callEvent(ev);
			if (!ev.isCancelled()) {
				toMoveDB.add(db);
			} else {
				e.setCancelled(true);
				break;
			}
		}
		if (e.isCancelled()) return;

		Map<Block, DataBlock> movedDB = new HashMap<>();
		toMoveDB.forEach(db -> {
			Block destination = db.getBlock().getRelative(e.getDirection());
			movedDB.put(destination, db);
		});

		toMoveDB.forEach(manager::remove);

		PluginHelper.sync().runLater(() -> {
			movedDB.forEach((block, dbOld) -> {
				DataBlock dbNew = manager.getDataBlock(block, true);
				if (dbNew != null) {
					dbNew.setData(dbOld.data);
					Bukkit.getPluginManager().callEvent(new DataBlockMoveEvent(dbOld, dbNew.getBlock(), e));
				}
			});
		}, 3);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBreak(BlockBreakEvent e) {
		DataBlock db = manager.getDataBlock(e.getBlock(), false);
		fireDestroy(db, e, DestroyCause.PLAYER_BREAK);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onExplode(BlockExplodeEvent e) {
		handleExplosion(e.blockList(), e);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onExplode(EntityExplodeEvent e) {
		handleExplosion(e.blockList(), e);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onCombust(BlockBurnEvent e) {
		DataBlock db = manager.getDataBlock(e.getBlock(), false);
		fireDestroy(db, e, DestroyCause.COMBUST);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPistonExtend(BlockPistonExtendEvent e) {
		handlePiston(e.getBlocks(), e);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPistonRetract(BlockPistonRetractEvent e) {
		handlePiston(e.getBlocks(), e);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityChangeBlock(EntityChangeBlockEvent e) {
		DataBlock db = manager.getDataBlock(e.getBlock(), false);
		fireDestroy(db, e, DestroyCause.ENTITY);
	}

}
