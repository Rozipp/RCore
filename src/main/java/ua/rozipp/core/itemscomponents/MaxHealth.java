package ua.rozipp.core.itemscomponents;

import org.bukkit.attribute.Attribute;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.AttributeModifierBuilder;
import ua.rozipp.core.items.ItemStackBuilder;

public class MaxHealth extends ItemComponent {
    private double healthValue;

    public MaxHealth() {
        super();
    }

    public MaxHealth(double healthValue) {
        super();
        this.healthValue = healthValue;
    }

    @Override
    protected void load(RConfig rConfig) throws InvalidConfiguration {
        healthValue = rConfig.getDouble("value", 1.0, null);
    }

    @Override
    public void onBuildItemStack(ItemStackBuilder builder) {
        builder.addAttribute(Attribute.GENERIC_MAX_HEALTH,
                AttributeModifierBuilder.newBuilder().name("Health").
                        amount(healthValue).
                        build());
        //TODO
        builder.addLore("TODO Добавляет " + healthValue + " к максимальному ХП");
    }

}
