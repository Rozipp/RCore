package ua.rozipp.core.blockdata;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Objects;

/**
 * Represents a world and chunk X and Z
 *
 * @author Redempt
 */
public class ChunkPosition implements Cloneable {

	private final int x;
	private final int z;
	private final String worldName;
	private World world;

	/**
	 * Creates a ChunkPosition from a chunk
	 *
	 * @param chunk The chunk to create a position for
	 */
	public ChunkPosition(Chunk chunk) {
		this(chunk.getWorld(), chunk.getX(), chunk.getZ());
	}

	/**
	 * Creates a ChunkPosition from a Block
	 *
	 * @param block The Block to create a position for
	 */
	public ChunkPosition(Block block) {
		this(block.getWorld(), block.getX() >> 4, block.getZ() >> 4);
	}

	public ChunkPosition(BlockPosition bPos) {
		this(bPos.getWorldName(), bPos.getX() >> 4, bPos.getZ() >> 4);
	}

	/**
	 * Creates a ChunkPosition from chunk coordinates and a world name
	 *
	 * @param x     The chunk X
	 * @param z     The chunk Z
	 * @param world The world
	 */
	public ChunkPosition(World world, int x, int z) {
		this.x = x;
		this.z = z;
		this.world = world;
		this.worldName = world.getName();
	}

	/**
	 * Creates a ChunkPosition from chunk coordinates and a world name
	 *
	 * @param x         The chunk X
	 * @param z         The chunk Z
	 * @param worldName The world name
	 */
	public ChunkPosition(String worldName, int x, int z) {
		this.x = x;
		this.z = z;
		this.worldName = worldName;
	}

	/**
	 * @return The chunk X
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return The chunk Z
	 */
	public int getZ() {
		return z;
	}

	/**
	 * @return The world this ChunkPosition is in
	 */
	public World getWorld() {
		if (world == null)
			world = Bukkit.getWorld(worldName);
		return world;
	}
	
	/**
	 * @return The name of the world this ChunkPosition is in
	 */
	public String getWorldName() {
		return worldName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, z, worldName);
	}

	@Override
	public String toString() {
		return worldName + " " + x + " " + z;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ChunkPosition pos)) {
			return false;
		}
		return pos.x == x && pos.z == z && worldName.equals(pos.worldName);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new ChunkPosition(this.getWorldName(), this.getX(), this.getZ());
	}

	public Chunk getChunk() {
		return getWorld().getChunkAt(getX(), getZ());
	}
}
