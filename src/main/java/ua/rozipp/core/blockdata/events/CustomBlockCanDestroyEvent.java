package ua.rozipp.core.blockdata.events;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;
import ua.rozipp.core.blockdata.DataBlock;
import ua.rozipp.core.blocks.CustomBlockType;

/**
 * Called when a CustomBlock is placed by a Player
 */
public class CustomBlockCanDestroyEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private final Event parent;
	private final DataBlock db;
	private final CustomBlockType type;
	private final DataBlockDestroyEvent.DestroyCause cause;
	private boolean destroyable;

	/**
	 * Creates a new BlockDataDestroyEvent
	 *
	 * @param db     The DataBlock that was destroyed
	 * @param parent The Event which caused this one
	 * @param cause  The cause of the DataBlock being destroyed
	 */
	public CustomBlockCanDestroyEvent(DataBlock db, CustomBlockType type, Event parent, DataBlockDestroyEvent.DestroyCause cause) {
		this(db, type, parent, cause, true);
	}

	/**
	 * Creates a new BlockDataDestroyEvent
	 *
	 * @param db     The DataBlock that was destroyed
	 * @param parent The Event which caused this one
	 * @param cause  The cause of the DataBlock being destroyed
	 */
	public CustomBlockCanDestroyEvent(DataBlock db, CustomBlockType type, Event parent, DataBlockDestroyEvent.DestroyCause cause, boolean destroyable) {
		this.db = db;
		this.parent = parent;
		this.cause = cause;
		this.destroyable = destroyable;
		this.type = type;
	}

	/**
	 * Sets whether the data should be removed from the block
	 *
	 * @param cancelled True to cancel removal of data from the block, false otherwise
	 */
	public void setDestroyable(boolean cancelled) {
		this.destroyable = cancelled;
	}

	/**
	 * Cancels the event which caused this one - meaning the block will not be destroyed
	 */
	public void cancelParent() {
		setDestroyable(false);
		if (parent instanceof Cancellable) {
			((Cancellable) parent).setCancelled(true);
		}
		if (parent instanceof BlockExplodeEvent) {
			BlockExplodeEvent e = (BlockExplodeEvent) parent;
			Block block = db.getBlock();
			e.blockList().remove(db.getBlock());
			if (destroyable) {
				e.blockList().add(block);
			}
		}
		if (parent instanceof EntityExplodeEvent) {
			EntityExplodeEvent e = (EntityExplodeEvent) parent;
			Block block = db.getBlock();
			e.blockList().remove(db.getBlock());
			if (destroyable) {
				e.blockList().add(block);
			}
		}
	}

	/**
	 * @return The reason the DataBlock was destroyed
	 */
	public DataBlockDestroyEvent.DestroyCause getCause() {
		return cause;
	}

	/**
	 * @return The event which caused this one
	 */
	public Event getParent() {
		return parent;
	}

	/**
	 * @return The CustomBlockType that is being placed
	 */
	public CustomBlockType getCustomBlockType() {
		return type;
	}

	/**
	 * @return Whether this event is cancelled
	 */
	public boolean isDestroyable() {
		return destroyable;
	}

	/**
	 * @return The DataBlock being removed
	 */
	public DataBlock getDataBlock() {
		return db;
	}

	/**
	 * @return The Block being destroyed
	 */
	public Block getBlock() {
		return db.getBlock();
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
