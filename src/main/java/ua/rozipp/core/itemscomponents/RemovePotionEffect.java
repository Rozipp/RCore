package ua.rozipp.core.itemscomponents;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;

public class RemovePotionEffect extends ItemComponent {

    private PotionEffectType potionEffectType;

    public RemovePotionEffect() {
    }

    @Override
    protected void load(RConfig rConfig) throws InvalidConfiguration {
        super.load(rConfig);
        String effectName = rConfig.getString("effect");
        if (effectName == null || effectName.isEmpty())
            throw new InvalidConfiguration("Effect in component \"" + this.getClass().getSimpleName() + "\"  not found.");
        potionEffectType = PotionEffectType.getByName(effectName);
        if (potionEffectType == null)
            throw new InvalidConfiguration("Effect \"" + effectName + "\" in component \"" + this.getClass().getSimpleName() + "\" not found.");
    }

    @Override
    public void action(LivingEntity entity) {
        super.action(entity);
        if (potionEffectType != null) entity.removePotionEffect(potionEffectType);

    }
}
