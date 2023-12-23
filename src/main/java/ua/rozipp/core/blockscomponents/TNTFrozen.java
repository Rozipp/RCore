package ua.rozipp.core.blockscomponents;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import ua.rozipp.core.blocks.CustomBlockType;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.util.GeometryUtils;

import java.util.Collection;

public class TNTFrozen extends TNTAbstract {

    public int range = 20;

    public TNTFrozen() {
        super();
    }

    @Override
    protected void load(RConfig compInfo) throws InvalidConfiguration {
        range = compInfo.getInt("range", range, "");
    }

    @Override
    public void onTNTPrimedExplode(EntityExplodeEvent event, CustomBlockType cBlock) {
        event.setCancelled(true);
        Location centerBlock = event.getLocation();
        Collection<Location> c = GeometryUtils.getBallBlocks(centerBlock, range);
        for (Location location : c) {
            Block block = location.getBlock();
            if (block.getType() == Material.WATER)
                block.setType(Material.ICE);
        }
    }

}
