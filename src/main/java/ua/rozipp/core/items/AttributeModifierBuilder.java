package ua.rozipp.core.items;

import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;

import java.util.UUID;

// Makes it easier to construct an attribute
public class AttributeModifierBuilder {
    private double amount;
    private Operation operation = Operation.ADD_NUMBER;
    private String name;
    private UUID uuid;

    AttributeModifierBuilder() {
        // Don't make this accessible
    }

    /* Construct a new attribute builder with a random UUID and default operation of adding numbers.
     * @return The attribute builder.
     */
    public static AttributeModifierBuilder newBuilder() {
        return new AttributeModifierBuilder().uuid(UUID.randomUUID()).operation(Operation.ADD_NUMBER);
    }

    public AttributeModifierBuilder amount(double amount) {
        this.amount = amount;
        return this;
    }

    public AttributeModifierBuilder operation(Operation operation) {
        this.operation = operation;
        return this;
    }

    public AttributeModifierBuilder name(String name) {
        this.name = name;
        return this;
    }

    public AttributeModifierBuilder uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public AttributeModifier build() {
        if (uuid == null)
            return new AttributeModifier(name, amount, operation);
        else
            return new AttributeModifier(uuid, name, amount, operation);
    }
}
