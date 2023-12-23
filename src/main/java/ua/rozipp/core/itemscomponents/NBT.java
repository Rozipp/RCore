package ua.rozipp.core.itemscomponents;

import org.bukkit.Color;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.ItemStackBuilder;

public class NBT extends ItemComponent {

    private String value;

    public NBT() {
        super();
    }

    public NBT(String value) {
        super();
        this.value = value;
    }

    @Override
    protected void load(RConfig rConfig) throws InvalidConfiguration {
        value = rConfig.getString("color", "", null);
    }

    @Override
    public void onSpawnItem(ItemStackBuilder builder) {
        builder.setColor(Color.fromRGB(Integer.decode("0x" + value)));

    }

}
