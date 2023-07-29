package ua.rozipp.core.itemscomponents;

import org.bukkit.attribute.Attribute;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.ItemStackBuilder;
import ua.rozipp.core.items.AttributeModifierBuilder;

public class MaxHealth extends ItemComponent {
    private final double healthValue;

    public MaxHealth(RConfig compInfo) throws InvalidConfiguration {
        super(compInfo);
        healthValue = compInfo.getDouble("value", 1.0, null);
    }

    @Override
    public void onPrepareCreate(ItemStackBuilder builder) {
        builder.addAttribute(Attribute.GENERIC_MAX_HEALTH,
                AttributeModifierBuilder.newBuilder().name("Health").
                        amount(healthValue).
                        build());
        //TODO
        builder.addLore("TODO Добавляет " + healthValue + " к максимальному ХП");
    }

}
