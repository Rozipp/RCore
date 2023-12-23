package ua.rozipp.core.blockdata;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Objects;

public class BlockPosition {

	private final int x;
	private final int y;
	private final int z;
	private final String worldName;
	private ChunkPosition chunkPosition;

	public BlockPosition(String worldName, int x, int y, int z) {
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public BlockPosition(Block block) {
		this(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
	}

	public static BlockPosition deserialize(String data) {
		if (data == null || data.isEmpty()) return null;
		String[] split = data.split(",");
		if (split.length != 4) return null;
		return new BlockPosition(split[0],
				Integer.parseInt(split[1]),
				Integer.parseInt(split[2]),
				Integer.parseInt(split[3]));
	}

	public World getWorld() {
		return Bukkit.getWorld(worldName);
	}

	public String getWorldName() {
		return worldName;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public Block getBlock() {
		return getWorld().getBlockAt(x, y, z);
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof BlockPosition)) {
			return false;
		}
		BlockPosition pos = (BlockPosition) o;
		return pos.x == x && pos.y == y && pos.z == z && pos.worldName.equals(worldName);
	}

	@Override
	public String toString() {
		return worldName + "," + x + "," + y + "," + z;
	}

	public ChunkPosition getChunkPosition() {
		if (chunkPosition == null)
			chunkPosition = new ChunkPosition(this);
		return chunkPosition;
	}
}
