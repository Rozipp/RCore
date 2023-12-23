package ua.rozipp.core.itemscomponents;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.MessageHelper;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.ItemStackBuilder;

public class Cooldown extends ItemComponent {
    private int cooldownValue;

    public Cooldown() {
        super();
    }

    public Cooldown(int cooldownValue) {
        super();
        this.cooldownValue = cooldownValue;
    }

    @Override
    protected void load(RConfig rConfig) throws InvalidConfiguration {
        cooldownValue = rConfig.getInt("cooldown", 0, null);
    }

    @Override
    public void onSpawnItem(ItemStackBuilder builder) {
        builder.addLore(Component.text("Cooldown")
                .append(Component.space())
                .append(Component.text(cooldownValue))
                .color(NamedTextColor.GOLD));
        builder.addLore(Component.translatable("item.component.lore.rightClickToUse").color(NamedTextColor.LIGHT_PURPLE));
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = event.getItem();
        MessageHelper.send(player, "Cooldown.onInteract в разработке");
//		ua.rozipp.rozippunit.object.Cooldown cooldown = ua.rozipp.rozippunit.object.Cooldown.getCooldown(stack);
//		if (cooldown != null) {
//			CivMessage.sendError(player, "Подождите " + cooldown.getTime() + " секунд");
//			event.setCancelled(true);
//			return;
//		}
//		ua.rozipp.rozippunit.object.Cooldown.startCooldown(player, stack, cooldownValue);
    }

}
