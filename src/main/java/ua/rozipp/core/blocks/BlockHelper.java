package ua.rozipp.core.blocks;

import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.blockdata.DataBlock;
import ua.rozipp.core.blockscomponents.BlockHologram;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.ItemHelper;

import java.util.Collection;
import java.util.List;

public class BlockHelper {

    public static @Nullable DataBlock getDataBlock(@NotNull Block block) {
        return PluginHelper.getBlockDataManager().getDataBlock(block);
    }

    public static @Nullable CustomBlockType getType(@Nullable Key bid) {
        return PluginHelper.getCustomBlockRegistry().getByKey(bid);
    }

    public static Collection<CustomBlockType> getTypes() {
        return PluginHelper.getCustomBlockRegistry().getTypes();
    }

}
