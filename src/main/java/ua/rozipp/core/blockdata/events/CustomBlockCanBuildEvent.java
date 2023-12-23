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
public class CustomBlockCanBuildEvent extends BlockEvent {

	private static final HandlerList handlers = new HandlerList();

	protected boolean buildable;
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
	public CustomBlockCanBuildEvent(Block block, ItemStack item, CustomBlockType type, Player player, boolean buildable) {
		super(block);
		this.item = item;
		this.type = type;
		this.player = player;
		this.buildable = buildable;
	}

	public CustomBlockCanBuildEvent(Block block, ItemStack itemInHand, CustomBlockType type, Player player) {
		this(block, itemInHand, type, player, true);
	}

	public CustomBlockCanBuildEvent(Block block, ItemStack itemInHand, CustomBlockType type) {
		this(block, itemInHand, type, null, true);
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

	/**
	 * Gets whether or not the block can be built here.
	 * <p>
	 * By default, returns Minecraft's answer on whether the block can be
	 * built here or not.
	 *
	 * @return boolean whether or not the block can be built
	 */
	public boolean isBuildable() {
		return buildable;
	}

	/**
	 * Sets whether the block can be built here or not.
	 *
	 * @param cancel true if you want to allow the block to be built here
	 *     despite Minecraft's default behaviour
	 */
	public void setBuildable(boolean cancel) {
		this.buildable = cancel;
	}

	/**
	 * @return The Player that placed the CustomBlock
	 */
	@Nullable
	public Player getPlayer() {
		return player;
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@NotNull
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
