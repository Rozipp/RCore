package ua.rozipp.core.blockdata.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ua.rozipp.core.blocks.CustomBlockType;

/**
 * Called when a CustomBlock is placed by a Player
 */
public class CustomBlockBuildEvent extends BlockEvent {

	private static HandlerList handlers = new HandlerList();

	private final CustomBlockType type;
	private final Player player;
	private final ItemStack item;

	/**
	 * Constructs a new CustomBlockPlaceEvent
	 *
	 * @param block  The block that was placed
	 * @param item   The item used to break the block
	 * @param type   The type of CustomBlock that is being placed
	 * @param player The Player that placed the block
	 */
	public CustomBlockBuildEvent(Block block, CustomBlockType type, ItemStack item, Player player) {
		super(block);
		this.item = item;
		this.type = type;
		this.player = player;
	}

	/**
	 * @return The Player that placed the CustomBlock
	 */
	@Nullable
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return The item that was in the player's hand when this block was placed
	 */
	public ItemStack getItem() {
		return item;
	}

	/**
	 * @return The CustomBlockType that is being placed
	 */
	public CustomBlockType getCustomBlockType() {
		return type;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
