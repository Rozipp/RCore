package ua.rozipp.core.itemscomponents;

import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;

public class RepairCost extends ItemComponent {

    private double repairCostValue;

    public RepairCost() {
        super();
    }

    public RepairCost(double repairCostValue) {
        super();
        this.repairCostValue = repairCostValue;
    }

    @Override
    protected void load(RConfig rConfig) throws InvalidConfiguration {
        repairCostValue = rConfig.getDouble("value", 1.0, null);
    }

}
