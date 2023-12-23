package ua.rozipp.core.blockdata;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Objects;

/**
 * Represents a world and chunk X and Z
 * @author Redempt
 */
public class ChunkPosition {

	private final int x;
	private final int z;
	private final String world;
	
	/**
	 * Creates a ChunkPosition from a chunk
	 * @param chunk The chunk to create a position for
	 */
	public ChunkPosition(Chunk chunk) {
		this(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
	}
	
	/**
	 * Creates a ChunkPosition from a Block
	 * @param block The Block to create a position for
	 */
	public ChunkPosition(Block block) {
		this(new BlockPosition(block));
	}

	/**
	 * Creates a ChunkPosition from chunk coordinates and a world name
	 *
	 * @param x     The chunk X
	 * @param z     The chunk Z
	 * @param world The world name
	 */
	public ChunkPosition(String world, int x, int z) {
		this.x = x;
		this.z = z;
		this.world = world;
	}

	public ChunkPosition(BlockPosition bPos) {
		this(bPos.getWorldName(), bPos.getX() >> 4, bPos.getZ() >> 4);
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
		return Bukkit.getWorld(world);
	}
	
	/**
	 * @return The name of the world this ChunkPosition is in
	 */
	public String getWorldName() {
		return world;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, z, world);
	}
	
	@Override
	public String toString() {
		return world + " " + x + " " + z;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ChunkPosition)) {
			return false;
		}
		ChunkPosition pos = (ChunkPosition) o;
		return pos.x == x && pos.z == z && world.equals(pos.world);
	}

	public Chunk getChunk() {
		return getWorld().getChunkAt(getX(), getZ());
    }
}
