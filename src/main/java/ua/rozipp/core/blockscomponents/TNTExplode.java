package ua.rozipp.core.blockscomponents;

import com.destroystokyo.paper.event.block.TNTPrimeEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.EntityExplodeEvent;
import ua.rozipp.core.blocks.CustomBlockType;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.util.GeometryUtils;

public class TNTExplode extends TNTAbstract {

    private boolean glowing = true;
    private boolean fire;
    private int interval = 8;
    private int range = 20;
    private boolean gravity;
    private boolean obsidian_break;

    public TNTExplode() {
        super();
    }

    @Override
    protected void load(RConfig rConfig) throws InvalidConfiguration {
        glowing = rConfig.getBoolean("glowing", glowing);
        fire = rConfig.getBoolean("fire", false);
        interval = rConfig.getInt("interval", interval);
        range = rConfig.getInt("range", range);
        gravity = rConfig.getBoolean("gravity", true);
        obsidian_break = rConfig.getBoolean("obsidian_break", false);
    }

    public TNTPrimed onTNTPrime(TNTPrimeEvent event, TNTPrimed tNTPrimedOld) {
        TNTPrimed tNTPrimed = (tNTPrimedOld == null) ?
                super.onTNTPrime(event, null) :
                tNTPrimedOld;
        tNTPrimed.setCustomNameVisible(true);
        tNTPrimed.setYield((float) this.range);
        if (tNTPrimed.getFuseTicks() < this.interval * 20)
            tNTPrimed.setFuseTicks(this.interval * 20);
        tNTPrimed.setIsIncendiary(this.fire);
        tNTPrimed.setGlowing(this.glowing);
        tNTPrimed.setGravity(this.gravity);

        return tNTPrimed;
    }

    public void onTNTPrimedExplode(EntityExplodeEvent event, CustomBlockType cBlock) {
        if (obsidian_break) {
            Entity entity = event.getEntity();
            int radius = (int) (entity instanceof Explosive ? ((Explosive) entity).getYield() : 4.0f);
            Block centerBlock = event.getLocation().getBlock();
            for (Location location : GeometryUtils.getBallBlocks(centerBlock.getLocation(), radius)) {
                Block block = location.getBlock();
                if ((block.getType().equals(Material.OBSIDIAN) || block.getType().equals(Material.CRYING_OBSIDIAN)))
                    event.blockList().add(block);
            }
        }
    }
}
