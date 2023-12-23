package ua.rozipp.core.blockdata.events;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ua.rozipp.core.blockdata.DataBlock;
import ua.rozipp.core.blocks.CustomBlockType;

/**
 * Called when a DataBlock is destroyed
 *
 * @author Redempt
 */
public class CustomBlockDestroyEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private final CustomBlockType type;
	private final Event parent;
	private final DataBlock db;
	private final DataBlockDestroyEvent.DestroyCause cause;

	/**
	 * Creates a new BlockDataDestroyEvent
	 *
	 * @param db     The DataBlock that was destroyed
	 * @param parent The Event which caused this one
	 * @param cause  The cause of the DataBlock being destroyed
	 */
	public CustomBlockDestroyEvent(DataBlock db, @NotNull CustomBlockType type, Event parent, DataBlockDestroyEvent.DestroyCause cause) {
		this.db = db;
		this.parent = parent;
		this.cause = cause;
		this.type = type;
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
