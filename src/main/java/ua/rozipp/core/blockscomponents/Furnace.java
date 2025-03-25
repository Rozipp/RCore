package ua.rozipp.core.blockscomponents;

import org.bukkit.Material;
import org.bukkit.event.block.BlockCookEvent;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.blockdata.events.CustomBlockBuildEvent;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;

public class Furnace extends BlockComponent{
    private double multiplier = 1;

    public Furnace(double speed) {
        this.multiplier = speed;
    }

    public Furnace() {
    }

    @Override
    protected void load(RConfig rConfig) throws InvalidConfiguration {
        super.load(rConfig);
        multiplier = rConfig.getDouble("multiplier", multiplier);
    }

    @Override
    public void onBuild(CustomBlockBuildEvent event) {
        if (event.getBlock().getState() instanceof org.bukkit.block.Furnace){
            org.bukkit.block.Furnace furnace = (org.bukkit.block.Furnace) event.getBlock().getState();
            furnace.setCookSpeedMultiplier(multiplier);
            furnace.update();
        }
    }
}
