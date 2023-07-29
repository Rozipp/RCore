package ua.rozipp.core.itemscomponents;

import org.bukkit.event.block.BlockPlaceEvent;
import ua.rozipp.core.blocks.CustomBlock;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.ItemStackBuilder;

public class BlockPlace extends ItemComponent {

    private CustomBlock customBlock;
    private final String bid;

    public BlockPlace(RConfig compInfo) throws InvalidConfiguration {
        super(compInfo);
        bid = compInfo.getString("bid", null, null);
    }

    private CustomBlock getCustomBlock() {
        if (customBlock == null) {
            customBlock = CustomBlock.getCustomBlock(bid);
        }
        return customBlock;
    }

    @Override
    public void onPrepareCreate(ItemStackBuilder builder) {
        super.onPrepareCreate(builder);
    }

    @Override
    public boolean onBlockPlaced(BlockPlaceEvent event) {
        if (getCustomBlock() != null) {
            getCustomBlock().blockPlace(event.getBlockPlaced());
        }
        return true;
    }
}
