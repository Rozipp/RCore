package ua.rozipp.core.itemscomponents;

import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;

public class RepairCost extends ItemComponent {

    private final double repairCostValue;

    public RepairCost(RConfig compInfo) throws InvalidConfiguration {
        super(compInfo);
        repairCostValue = compInfo.getDouble("value", 1.0, null);
    }

}
