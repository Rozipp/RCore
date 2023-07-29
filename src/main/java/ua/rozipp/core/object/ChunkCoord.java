package ua.rozipp.core.object;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Getter
@Setter
public class ChunkCoord {

    private @NotNull World world;
    private int x;
    private int z;

    public ChunkCoord(@NotNull World world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public ChunkCoord(String worldname, int x, int z) {
        this.world = Objects.requireNonNull(Bukkit.getWorld(worldname));
        this.x = x;
        this.z = z;
    }

    public ChunkCoord(Location location) {
        this.world = location.getWorld();
        this.x = castCoordToChunkCoord(location.getBlockX());
        this.z = castCoordToChunkCoord(location.getBlockZ());
    }

    public ChunkCoord(Chunk c) {
        this.world = c.getWorld();
        this.x = c.getX();
        this.z = c.getZ();
    }

    public ChunkCoord(BlockCoord coord) {
        this.world = coord.getWorld();
        this.x = castCoordToChunkCoord(coord.getX());
        this.z = castCoordToChunkCoord(coord.getZ());
    }

    public ChunkCoord(Block block) {
        this.world = block.getWorld();
        this.x = castCoordToChunkCoord(block.getX());
        this.z = castCoordToChunkCoord(block.getZ());
    }

    @Override
    public String toString() {
        return this.world.getName() + "," + x + "," + z;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other instanceof ChunkCoord) {
            ChunkCoord otherCoord = (ChunkCoord) other;
            return otherCoord.world.equals(this.world) &&
                    otherCoord.getX() == x &&
                    otherCoord.getZ() == z;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + this.x;
        hash = 31 * hash + this.z;
        hash = 31 * hash + world.hashCode();
        return hash;
    }

    public ChunkCoord getRelative(int dx, int dz) {
        return new ChunkCoord(getWorld(), getX() + dx, getZ() + dz);
    }

    public int manhattanDistance(ChunkCoord chunkCoord) {
        return Math.abs(chunkCoord.x - this.x) + Math.abs(chunkCoord.z - this.z);
    }

    public double distanceSqr(ChunkCoord chunkCoord) {
        if (!chunkCoord.getWorld().equals(this.getWorld())) return Double.MAX_VALUE;
        return Math.pow(this.getX() - chunkCoord.getX(), 2) + Math.pow(this.getZ() - chunkCoord.getZ(), 2);
    }

    public double distance(ChunkCoord chunkCoord) {
        return Math.sqrt(distanceSqr(chunkCoord));
    }

    public Chunk getChunk() {
        return this.world.getChunkAt(this.x, this.z);
    }

    public static int getCoordInChunk(int x) {
        return Math.floorMod(x, 16);
    }

    public static int castCoordToChunkCoord(int x) {
        return Math.floorDiv(x, 16);
    }

    public static int castSizeInChunkSize(int size_x) {
        return Math.floorDiv(Math.abs(size_x) - 1, 16) + 1;
    }
}
