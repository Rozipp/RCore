package ua.rozipp.core.blocks;

import com.destroystokyo.paper.event.block.TNTPrimeEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import redempt.redlib.misc.EventListener;
import redempt.redlib.misc.Path;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.blockdata.BlockDataManager;
import ua.rozipp.core.blockdata.DataBlock;
import ua.rozipp.core.blockdata.events.*;
import ua.rozipp.core.blockscomponents.BlockComponent;
import ua.rozipp.core.blockscomponents.TNTAbstract;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomBlockListener implements Listener {

    private final BlockDataManager manager;
    private final CustomBlockRegistry registry;
    private final Plugin plugin;

    public CustomBlockListener(CustomBlockRegistry registry, BlockDataManager manager, Plugin plugin) {
        this.manager = manager;
        this.plugin = plugin;
        this.registry = registry;

        onEventCancel(BlockFadeEvent.class, EventPriority.LOWEST);
        onEventCancel(BlockFertilizeEvent.class, EventPriority.LOWEST);
        onEventCancel(BlockFromToEvent.class, EventPriority.LOWEST);
        onEventCancel(BlockGrowEvent.class, EventPriority.LOWEST);
        onEventCancel(LeavesDecayEvent.class, EventPriority.LOWEST);
        onEventCancel(MoistureChangeEvent.class, EventPriority.LOWEST);
        onEventCancel(SignChangeEvent.class, EventPriority.LOWEST);
        onEventCancel(SpongeAbsorbEvent.class, EventPriority.LOWEST);

        onEvent(BlockBurnEvent.class, EventPriority.HIGHEST);
        onEvent(BlockCookEvent.class, EventPriority.HIGHEST);
        onEvent(BlockDamageEvent.class, EventPriority.HIGHEST);
        onEvent(BlockDispenseEvent.class, EventPriority.HIGHEST);
        onEvent(BlockExplodeEvent.class, EventPriority.HIGHEST);
        onEvent(BlockRedstoneEvent.class, EventPriority.HIGHEST);
    }

    public <T extends BlockEvent> void onEventCancel(Class<T> event, EventPriority priority) {
        new EventListener<>(plugin, event, priority, e -> {
            if (e instanceof Cancellable && registry.getCustomBlockType(e.getBlock()) != null) {
                LogHelper.debug("Event \"" + event.getSimpleName() + "\" is cancelled");
                ((Cancellable) e).setCancelled(true);
            }
        });
    }

    public <T extends BlockEvent> void onEvent(Class<T> event, EventPriority priority) {
        new EventListener<>(plugin, event, priority, e -> {
            if (e instanceof Cancellable && ((Cancellable) e).isCancelled()) return;
            CustomBlockType type = registry.getCustomBlockType(e.getBlock());
            if (type != null) {
                type.getComponents().forEach(comp -> {
                    try {
                        BlockComponent.class.getMethod(event.getSimpleName(), event).invoke(comp, e);
                        LogHelper.debug("onEvent \"" + event.getSimpleName() + "\" is run");
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException error) {
                        LogHelper.error("Event Method error \"" + event.getSimpleName() + "\"");
                    }
                });
            }
        });
    }

    @EventHandler
    public void onInventoryCreative(InventoryCreativeEvent e) {
        if (e.getCursor().getType() == Material.AIR
                || e.getSlot() > 8 || e.getView().getTopInventory().getType() != InventoryType.CRAFTING
                || e.getCursor().getAmount() != 1 || e.getAction() != InventoryAction.PLACE_ALL) {
            return;
        }
        List<Location> path = Path.getPath(e.getWhoClicked().getEyeLocation(), e.getWhoClicked().getLocation().getDirection(), 5);
        Block block = null;
        for (Location loc : path) {
            if (loc.getBlock().getType() != Material.AIR) {
                block = loc.getBlock();
                break;
            }
        }
        if (block == null) return;
        if (block.getType() == e.getCursor().getType()) {
            CustomBlockType type = registry.getCustomBlockType(block);
            if (type != null) {
                e.setCancelled(true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    ItemStack item = type.getItem();
                    for (int i = 0; i < 9; i++) {
                        if (item.isSimilar(e.getWhoClicked().getInventory().getItem(i))) {
                            e.getWhoClicked().getInventory().setHeldItemSlot(i);
                            return;
                        }
                    }
                    e.getWhoClicked().getInventory().setItem(e.getSlot(), item);
                });
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            CustomBlockType type = registry.getCustomBlockType(event.getClickedBlock());
            if (type != null)
                for (BlockComponent comp : type.getComponents()) {
                    comp.onInteract(event);
                }
        }
    }

    @EventHandler
    public void onTNTPrime(TNTPrimeEvent event) {
        if (event.isCancelled()) return;
        DataBlock db = manager.getDataBlock(event.getBlock());
        CustomBlockType type = registry.getCustomBlockType(db);
        if (db != null && type != null) {
            TNTPrimed tntPrimed = null;
            LogHelper.debug("onTNTPrime type.getComponents(): " + type.getComponents().size());
            for (BlockComponent comp : type.getComponents()) {
                tntPrimed = comp.onTNTPrime(event, tntPrimed);
            }
            if (tntPrimed != null) {
                DataBlockDestroyEvent e = new DataBlockDestroyEvent(db, event, DataBlockDestroyEvent.DestroyCause.COMBUST);
                Bukkit.getPluginManager().callEvent(e);
                if (e.isCancelled()) {
                    tntPrimed.remove();
                } else {
                    event.getBlock().setType(Material.AIR);
                    manager.remove(db);
                }
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity().getType() == EntityType.PRIMED_TNT && event.getEntity().getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM)) {
            String list = event.getEntity().getPersistentDataContainer().get(TNTAbstract.KEY, PersistentDataType.STRING);
            if (list == null || list.isEmpty()) return;
            CustomBlockType cBlock = registry.getByKey(NamespacedKey.fromString(list));
            if (cBlock != null)
                for (BlockComponent comp : cBlock.getComponents()) {
                    if (comp instanceof TNTAbstract) {
                        ((TNTAbstract) comp).onTNTPrimedExplode(event, cBlock);
                    }
                }
        }
    }

    private static final int[][] offset = {{-1, 0, 0}, {1, 0, 0}, {0, 0, -1}, {0, 0, 1}, {0, -1, 0}};

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockIgniteEvent(BlockIgniteEvent event) {
        if (event.isCancelled()) return;
        Block origin = event.getBlock();
        for (int[] set : offset) {
            int x = set[0];
            int y = set[1];
            int z = set[2];
            CustomBlockType type = registry.getCustomBlockType(origin.getRelative(x, y, z));
            if (type != null) {
                Block block = origin.getRelative(x, y, z);
                for (BlockComponent comp : type.getComponents()) {
                    comp.onBlockIgniteEvent(event, block);
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCanBuild(CustomBlockCanBuildEvent e) {
        e.getCustomBlockType().getComponents().forEach(comp -> comp.onCanBuild(e));
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBuild(CustomBlockBuildEvent e) {
        e.getCustomBlockType().getComponents().forEach(comp -> comp.onBuild(e));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCanDestroy(CustomBlockCanDestroyEvent e) {
        e.getCustomBlockType().getComponents().forEach(comp -> comp.onCanDestroy(e));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDestroy(CustomBlockDestroyEvent e) {
        CustomBlockType type = e.getCustomBlockType();

        if (e.getCause() == DataBlockDestroyEvent.DestroyCause.PLAYER_BREAK) {
            BlockBreakEvent blockBreakEvent = (BlockBreakEvent) e.getParent();
            Player player = blockBreakEvent.getPlayer();

            if (player.getGameMode() != GameMode.CREATIVE) {
                Block block = e.getBlock();
                if (blockBreakEvent.isDropItems()) {
                    blockBreakEvent.setDropItems(false);
                    Collection<ItemStack> items = type.getDrops();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        List<Item> drops = new ArrayList<>();
                        items.forEach(itemStack -> drops.add(block.getWorld().dropItemNaturally(e.getBlock().getLocation(), itemStack)));
                        BlockDropItemEvent event = new BlockDropItemEvent(block, block.getState(), player, drops);
                        Bukkit.getPluginManager().callEvent(event);
                        if (event.isCancelled()) drops.forEach(Item::remove);
                    });
                }
            }
        }

        for (BlockComponent comp : e.getCustomBlockType().getComponents())
            comp.onDestroy(e);
    }

    @EventHandler
    public void onDataBlockLoadEvent(DataBlockLoadEvent event) {
        CustomBlockType type = registry.getCustomBlockType(event.getDataBlock());
        if (type != null)
            type.getComponents().forEach(comp -> comp.onLoad(event));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDataBlockCanMoveEvent(DataBlockCanMoveEvent event) {
        CustomBlockType type = registry.getCustomBlockType(event.getDataBlock());
        if (type != null)
            type.getComponents().forEach(comp -> comp.onCanMove(event));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDataBlockMoveEvent(DataBlockMoveEvent event) {
        CustomBlockType type = registry.getCustomBlockType(event.getDataBlock());
        if (type != null)
            type.getComponents().forEach(comp -> comp.onMove(event));
    }
}
