package ua.rozipp.core.itemscomponents;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.ItemStackBuilder;

public class Health extends ItemComponent {

    private double healthValue;

    public Health() {
        super();
    }

    public Health(double healthValue) {
        super();
        this.healthValue = healthValue;
    }

    @Override
    protected void load(RConfig rConfig) throws InvalidConfiguration {
        healthValue = rConfig.getDouble("value", 1.0, null);
    }

    @Override
    public void onSpawnItem(ItemStackBuilder builder) {
        //TODO
        builder.addLore(Component.text("TODO Лечит на " + healthValue, NamedTextColor.BLUE));
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        player.setHealth(player.getHealth() + healthValue);
    }
}
