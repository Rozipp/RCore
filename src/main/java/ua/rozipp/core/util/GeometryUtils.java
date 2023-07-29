package ua.rozipp.core.util;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GeometryUtils {

    public static Collection<Location> getBallBlocks(Location centerBlock, double radius) {
        int range = (int) Math.ceil(Math.abs(radius));
        double radiusS = radius * radius;
        List<Location> list = new ArrayList<>();

        for (int x = centerBlock.getBlockX() - range; x <= centerBlock.getBlockX() + range; x++)
            for (int z = centerBlock.getBlockZ() - range; z <= centerBlock.getBlockZ() + range; z++)
                for (int y = centerBlock.getBlockY() - range; y <= centerBlock.getBlockY() + range; y++) {
                    Location location = new Location(centerBlock.getWorld(), x, y, z);
                    if (location.distanceSquared(centerBlock) <= radiusS)
                        list.add(location);
                }
        return list;
    }

    public static Collection<Location> getSphereBlocks(Location centerBlock, double radius) {
        int range = (int) Math.ceil(Math.abs(radius));
        double radiusS = radius * radius;
        List<Location> list = new ArrayList<>();

        for (int x = centerBlock.getBlockX() - range; x <= centerBlock.getBlockX() + range; x++)
            for (int z = centerBlock.getBlockZ() - range; z <= centerBlock.getBlockZ() + range; z++)
                for (int y = centerBlock.getBlockY() - range; y <= centerBlock.getBlockY() + range; y++) {
                    Location location = new Location(centerBlock.getWorld(), x, y, z);
                    if (Math.abs(location.distanceSquared(centerBlock) - radiusS) < 9)
                        list.add(location);
                }
        return list;
    }

}
