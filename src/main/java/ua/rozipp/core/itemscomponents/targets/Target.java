package ua.rozipp.core.itemscomponents.targets;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Target {

    static Map<String, Class<? extends Target>> map = new HashMap<>();

    static {
        register("HIMSELF", TargetHimself.class);
        register("VICTIM", TargetVictim.class);
        register("RANGE", TargetRange.class);
        register("FOCUS", TargetFocus.class);
    }

    public static void register(String name, Class<? extends Target> aClass) {
        map.put(name, aClass);
    }

    public static Target createTarget(RConfig rConfig) throws InvalidConfiguration {
        String targetName = rConfig.getString("target");
        if (targetName == null) throw new InvalidConfiguration("Target not found");
        try {
            if (targetName.isEmpty())
                throw new InvalidConfiguration("Component's name is empty");
            Class<? extends Target> cls = map.get(targetName);
            if (cls == null) throw new InvalidConfiguration("Target \"" + targetName + "\" no such class");
            Target target = cls.getConstructor().newInstance();
            target.load(rConfig);
            return target;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new InvalidConfiguration("Target \"" + targetName + "\" not found");
        }
    }

    protected Target() {
    }

    protected abstract void load(RConfig rConfig);

    public abstract Collection<LivingEntity> getTargetLivingEntities(LivingEntity player, LivingEntity entity, Location location);

}
