package ua.rozipp.core.itemscomponents.targets;

import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import ua.rozipp.core.config.RConfig;

import java.util.Collection;
import java.util.Collections;

public class TargetFocus extends Target {

    @Setter
    private int target_distance = 1;

    public TargetFocus() {
    }

    public TargetFocus(int target_distance) {
        this.target_distance = target_distance;
    }

    @Override
    protected void load(RConfig rConfig) {
        target_distance = rConfig.getInt("target_distance", target_distance);
    }

    @Override
    public Collection<LivingEntity> getTargetLivingEntities(LivingEntity player, LivingEntity entity, Location location) {
        return (player != null) ?
                Collections.singletonList((LivingEntity) player.getTargetEntity(target_distance, false)) :
                Collections.emptyList();
    }
}
