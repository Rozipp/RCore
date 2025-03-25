package ua.rozipp.core.itemscomponents;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;

public class AddPotionEffect extends ItemComponent {

    private PotionEffect potionEffect;

    @Override
    protected void load(RConfig rConfig) throws InvalidConfiguration {
        super.load(rConfig);
        String effectName = rConfig.getString("effect");
        if (effectName == null || effectName.isEmpty())
            throw new InvalidConfiguration("Effect in component \"" + this.getClass().getSimpleName() + "\"  not found.");
        PotionEffectType potionEffectType = PotionEffectType.getByName(effectName);
        if (potionEffectType == null)
            throw new InvalidConfiguration("Effect \"" + effectName + "\" in component \"" + this.getClass().getSimpleName() + "\" not found.");
        int duration = rConfig.getInt("effect_duration", 20);
        int amplifier = rConfig.getInt("effect_amplifier", 1);
        potionEffect = new PotionEffect(potionEffectType, duration, amplifier);
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        super.onInteract(event);
        action(event.getPlayer());
    }

    public void action(LivingEntity entity) {
        entity.addPotionEffect(potionEffect);
//            LogHelper.debug("AddPotionEffect " + potionEffect.getType() + ":" + potionEffect.getDuration() + ":" + potionEffect.getAmplifier());
    }

}
