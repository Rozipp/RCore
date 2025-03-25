package ua.rozipp.core.itemscomponents.targets;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import ua.rozipp.core.config.RConfig;

import java.util.Collection;
import java.util.Collections;

public class TargetHimself extends Target {
    public TargetHimself() {
    }

    @Override
    protected void load(RConfig rConfig) {

    }

    @Override
    public Collection<LivingEntity> getTargetLivingEntities(LivingEntity player, LivingEntity entity, Location location) {
        return (player != null) ?
                Collections.singleton(player) :
                Collections.emptyList();
    }
}
