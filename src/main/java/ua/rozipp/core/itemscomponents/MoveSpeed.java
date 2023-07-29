package ua.rozipp.core.itemscomponents;

import org.bukkit.attribute.Attribute;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.AttributeModifierBuilder;
import ua.rozipp.core.items.ItemStackBuilder;

public class MoveSpeed extends ItemComponent {

    private final double speedValue;

    public MoveSpeed(RConfig compInfo) throws InvalidConfiguration {
        super(compInfo);
        speedValue = compInfo.getDouble("value", 1.0, null);
    }

    @Override
    public void onPrepareCreate(ItemStackBuilder builder) {
        builder.addAttribute(Attribute.GENERIC_MOVEMENT_SPEED,
                AttributeModifierBuilder.newBuilder().name("Speed").
                        amount(speedValue).
                        build());
        //TODo
        builder.addLore("TODO Добавляет " + speedValue + " к скорости");
    }

}
