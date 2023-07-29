package ua.rozipp.core.itemscomponents;

import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import org.bukkit.event.block.BlockPlaceEvent;

public class AllowBlockPlace extends ItemComponent {

	public AllowBlockPlace(RConfig compInfo) throws InvalidConfiguration {
		super(compInfo);
	}

	@Override
	public boolean onBlockPlaced(BlockPlaceEvent event) { 
		return true; 
	}

}
