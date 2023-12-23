package ua.rozipp.core.blockdata.events;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ua.rozipp.core.blockdata.DataBlock;

/**
 * Called when a DataBlock is moved by pistons
 *
 * @author Redempt
 */
public class DataBlockMoveEvent extends Event {

	private static HandlerList handlers = new HandlerList();

	private final DataBlock db;
	private final Block destination;
	private final Event parent;

	/**
	 * Creates a DataBlockMoveEvent
	 * @param db The DataBlock being moved
	 * @param destination The Block it is being moved to
	 * @param parent The event which caused this one
	 */
	public DataBlockMoveEvent(DataBlock db, Block destination, Event parent) {
		this.db = db;
		this.parent = parent;
		this.destination = destination;
	}
	
	/**
	 * @return The event which caused this one
	 */
	public Event getParent() {
		return parent;
	}
	
	/**
	 * @return The Block the data is being moved to
	 */
	public Block getDestination() {
		return destination;
	}
	
	/**
	 * @return The DataBlock being moved
	 */
	public DataBlock getDataBlock() {
		return db;
	}
	
	public Block getBlock() {
		return destination;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
