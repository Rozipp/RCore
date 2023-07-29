package ua.rozipp.core.itemscomponents;

import org.bukkit.Color;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.ItemStackBuilder;

public class NBT extends ItemComponent {

    private final String value;

    public NBT(RConfig compInfo) throws InvalidConfiguration {
        super(compInfo);
        value = compInfo.getString("color", "", null);
    }

    @Override
    public void onPrepareCreate(ItemStackBuilder builder) {
        builder.setColor(Color.fromRGB(Integer.decode("0x" + value)));

    }

}
