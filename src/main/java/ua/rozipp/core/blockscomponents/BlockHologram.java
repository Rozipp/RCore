package ua.rozipp.core.blockscomponents;

import org.bukkit.block.Block;
import ua.rozipp.core.RCore;
import ua.rozipp.core.blockdata.events.CustomBlockBuildEvent;
import ua.rozipp.core.blockdata.events.CustomBlockDestroyEvent;
import ua.rozipp.core.blockdata.events.DataBlockLoadEvent;
import ua.rozipp.core.blockdata.events.DataBlockMoveEvent;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;

import java.util.HashMap;
import java.util.Map;

public class BlockHologram extends BlockComponent {

    private String text1;
    private String text2;

    public BlockHologram(){
        super();
    }

    public BlockHologram(String text1, String text2) {
        super();
        this.text1 = text1;
        this.text2 = text2;
    }

    @Override
    protected void load(RConfig rConfig) throws InvalidConfiguration {
        this.text1 = rConfig.getString("text1", "", null);
        this.text2 = rConfig.getString("text2", "", null);
    }

    @Override
    public void onBuild(CustomBlockBuildEvent event) {
//        Block block = event.getBlock();
//        Hologram hologram = holograms.get(block);
//        if (hologram == null) {
//            hologram = HD_API.createHologram(event.getBlock().getLocation().add(0.5, 1.2, 0.5));
//            hologram.getLines().appendText(text1);
//            hologram.getLines().appendText(text2);
//            holograms.put(block, hologram);
//        }
    }

    @Override
    public void onLoad(DataBlockLoadEvent event) {
//        Block block = event.getDataBlock().getBlock();
//        Hologram hologram = holograms.get(block);
//        if (hologram == null) {
//            hologram = HD_API.createHologram(event.getDataBlock().getBlock().getLocation().add(0.5, 1.2, 0.5));
//            hologram.getLines().appendText(text1);
//            hologram.getLines().appendText(text2);
//            holograms.put(block, hologram);
//        }
    }

    @Override
    public void onDestroy(CustomBlockDestroyEvent event) {
//        Block block = event.getDataBlock().getBlock();
//        Hologram hologram = holograms.get(block);
//        if (hologram != null) {
//            hologram.delete();
//            holograms.remove(block);
//        }
    }

    @Override
    public void onMove(DataBlockMoveEvent event) {
//        Block block = event.getDataBlock().getBlock();
//        Hologram hologram = holograms.get(block);
//        if (hologram != null) hologram.setPosition(event.getBlock().getLocation().add(0.5, 1.2, 0.5));
    }
}
