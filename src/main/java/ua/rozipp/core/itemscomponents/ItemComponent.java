package ua.rozipp.core.itemscomponents;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.ItemStackBuilder;
import ua.rozipp.core.items.ItemChangeResult;

import java.util.HashMap;
import java.util.Map;

public abstract class ItemComponent {

    private final static Map<String, Class<? extends ItemComponent>> components = new HashMap<>();

    static {
        register(Attack.class);
        register(BlockPlace.class);
        register(AllowBlockPlace.class);
        register(ChoiceUnitComponent.class);
        register(Cooldown.class);
        register(Defense.class);
        register(DurabilityOnDeath.class);
        register(Health.class);
        register(MaxHealth.class);
        register(MoveSpeed.class);
        register(NBT.class);
        register(NoCauldronWash.class);
        register(NoDurability.class);
        register(NoRightClick.class);
        register(NoVanillaDurability.class);
        register(RangedAttack.class);
        register(RepairCost.class);
        register(Soulbound.class);
        register(Tagged.class);
        register(OpenGui.class);
    }

    public static Class<? extends ItemComponent> getItemComponentClass(String name) {
        return components.get(name);
    }

    private static void register(Class<? extends ItemComponent> aClass){
        components.put(aClass.getSimpleName(), aClass);
    }

    @Getter
    private final String name;

    public ItemComponent(RConfig compInfo) throws InvalidConfiguration {
        this.name = compInfo.getString("name", null, null);
    }

    public Component getDisplayName(){
        return Component.text(getName());
    }

    /*
    Вызываеться при создании предмета с данными компонентами.
    используйте:
    - builder.addAttribute();
    - builder.addLore();
    - builder.addTag()
     */
    public void onPrepareCreate(ItemStackBuilder builder) {
    }

    // -------- Events
    public void onDurabilityChange(PlayerItemDamageEvent event) {
    }

    public void onDefense(EntityDamageByEntityEvent event, ItemStack stack) {
    }

    public void onInteract(PlayerInteractEvent event) {
    }

    public void onItemSpawn(ItemSpawnEvent event) {
    }

    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
    }

    public void onRangedAttack(EntityDamageByEntityEvent event, ItemStack inHand) {
    }

    public ItemChangeResult onDurabilityDeath(PlayerDeathEvent event, ItemChangeResult result, ItemStack stack) {
        return result;
    }

    public void onAttack(EntityDamageByEntityEvent event, ItemStack inHand) {
    }

    public boolean onBlockPlaced(BlockPlaceEvent event) {
        return false;
    }

    public void onInventoryOpen(InventoryOpenEvent event, ItemStack stack) {
    }

    public void onHold(PlayerItemHeldEvent event) {
    }

}
