package ua.rozipp.core.itemscomponents.targets;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import ua.rozipp.core.config.RConfig;

import java.util.Collection;
import java.util.Collections;

public class TargetVictim extends Target {
    @Override
    protected void load(RConfig rConfig) {
    }

    @Override
    public Collection<LivingEntity> getTargetLivingEntities(LivingEntity player, LivingEntity entity, Location location) {
        return (entity != null) ?
                Collections.singleton(entity):
                Collections.emptyList();
    }
}
