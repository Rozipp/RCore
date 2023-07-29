package ua.rozipp.core.itemscomponents;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.ItemStackBuilder;
import ua.rozipp.core.items.ItemChangeResult;

public class DurabilityOnDeath extends ItemComponent {

    private final double percentValue;

    public DurabilityOnDeath(RConfig compInfo) throws InvalidConfiguration {
        super(compInfo);
        percentValue = compInfo.getDouble("value", 0.0, null);
    }

    @Override
    public void onPrepareCreate(ItemStackBuilder builder) {
//		attrs.addLore(CivColor.Blue+""+this.getDouble("value")+" Durability");
    }

    @Override
    public ItemChangeResult onDurabilityDeath(PlayerDeathEvent event, ItemChangeResult result, ItemStack sourceStack) {
        if (result == null) {
            result = new ItemChangeResult();
            result.stack = sourceStack;
            result.destroyItem = false;
        }

        if (result.destroyItem) {
            return result;
        }

        int reduction = (int) (result.stack.getType().getMaxDurability() * percentValue);
        int durabilityLeft = result.stack.getType().getMaxDurability() - result.stack.getDurability();

        if (durabilityLeft > reduction) {
            result.stack.setDurability((short) (result.stack.getDurability() + reduction));
        } else {
            result.destroyItem = true;
        }

        return result;
    }

}
