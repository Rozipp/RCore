package ua.rozipp.core.itemscomponents.targets;

import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import ua.rozipp.core.config.RConfig;

import java.util.Collection;
import java.util.Collections;

public class TargetRange extends Target {

    @Setter
    private double target_range = 1;

    public TargetRange() {
    }

    public TargetRange(double target_range) {
        this.target_range = target_range;
    }

    @Override
    protected void load(RConfig rConfig) {
        target_range = rConfig.getDouble("target_range", target_range);
    }

    @Override
    public Collection<LivingEntity> getTargetLivingEntities(LivingEntity player, LivingEntity entity, Location location) {
        return (location != null) ?
                location.getNearbyLivingEntities(target_range) :
                Collections.emptyList();
    }
}
