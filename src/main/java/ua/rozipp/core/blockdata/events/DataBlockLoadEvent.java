package ua.rozipp.core.blockdata.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ua.rozipp.core.blockdata.DataBlock;

public class DataBlockLoadEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final DataBlock db;

    public DataBlockLoadEvent(DataBlock db) {
        this.db = db;
    }

    /**
     * @return The DataBlock being moved
     */
    public DataBlock getDataBlock() {
        return db;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
