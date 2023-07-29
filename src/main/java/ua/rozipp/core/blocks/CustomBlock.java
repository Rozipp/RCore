package ua.rozipp.core.blocks;

import lombok.Getter;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redempt.redlib.blockdata.DataBlock;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.CustomMaterial;

import java.util.*;

public class CustomBlock {
    protected static final String TAGNAME = "RBID";
    private static final Map<Key, CustomBlock> blocks = new HashMap<>();

    @Getter
    private final Key bid;
    @Getter
    private final Key mid;
    private Material material;

    public CustomBlock(@NotNull Key bid, Key mid) {
        this(bid, mid, null);
    }

    public CustomBlock(@NotNull Key bid, Key mid, Material material) {
        this.bid = bid;
        this.mid = mid;
        this.material = material;
        blocks.put(bid, this);
    }

    /**
     * Возвращает билдер для создания CustomBlock
     */
    public static CustomBlockBuilder builder() {
        return new CustomBlockBuilder();
    }

    /**
     * Возвращает CustomBlock если у блока есть кастомное ID, иначе null
     */
    public static @Nullable CustomBlock getCustomBlock(Block block) {
        return getCustomBlock(getBid(block));
    }

    /**
     * Возвращает CustomBlock по его bid
     */
    public static @Nullable CustomBlock getCustomBlock(@Nullable Key bid) {
        if (bid == null) return null;
        return blocks.get(bid);
    }

    /**
     * ищет CustomBlock как по полному NamespacedKey, так и только по его значению, без namespace
     */
    public static @Nullable CustomBlock getCustomBlock(String bid) {
        for (Key key : blocks.keySet()) {
            if (key.asString().equalsIgnoreCase(bid) || key.value().equalsIgnoreCase(bid))
                return CustomBlock.getCustomBlock(key);
        }
        return null;
    }

    /**
     * Возвращает bid если у предмета есть кастомное ID, иначе пустая строка
     */
    public static @Nullable Key getBid(Block block) {
        DataBlock blockData = PluginHelper.getBlockDataManager().getDataBlock(block, false);
        if (blockData == null || blockData.getString(TAGNAME) == null) return null;
        return NamespacedKey.fromString(blockData.getString(TAGNAME));
    }

    /**
     * true, если у блока есть кастомное ID"
     */
    public static boolean isCustomBlock(Block block) {
        if (block == null) return false;
        return blocks.containsKey(getBid(block));
    }

    public static Collection<CustomBlock> values() {
        return blocks.values();
    }

    public static void removeAll(Plugin plugin) {
        List<Key> keys = new ArrayList<>();
        for (Key key : blocks.keySet()) {
            if (key.namespace().equalsIgnoreCase(plugin.getName()))
                keys.add(key);
        }
        for (Key key : keys)
            blocks.remove(key);
        LogHelper.fine("Unregistered " + keys.size() + " CustomBlock");
    }

    public static void load(Plugin plugin, RConfig cfg) throws InvalidConfiguration {
        if (cfg == null) return;
        List<RConfig> configBlocks = cfg.getRConfigList("blocks");
        if (configBlocks == null) return;
        int count = 0;
        for (RConfig rConfig : configBlocks) {
            try {
                new ComponentedCustomBlockBuilder().loadConfig(rConfig).build(plugin);
                count++;
            } catch (InvalidConfiguration e) {
                LogHelper.warning(e.getMessage()); //TODO Дописать где возникла ошибка
            }
        }
        LogHelper.info("Loaded " + count + " CustomBlocks.");
    }

    public Material getMaterial() {
        if (material == null)
            if (mid == null) material = Material.WHITE_WOOL;
            else
                try {
                    material = CustomMaterial.getCustomMaterial(mid).getMaterial();
                } catch (Exception e) {
                    material = Material.WHITE_WOOL;
                }
        return material;
    }

    /* --------- Events for this CustomBlock --------- */
    public void blockPlace(Block block) {
        block.setType(this.getMaterial());
        block.setBlockData(this.getMaterial().createBlockData());
        DataBlock dataBlock = PluginHelper.getBlockDataManager().getDataBlock(block);
        dataBlock.set(TAGNAME, bid.asString());
    }

    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        if (mid != null) {
            CustomMaterial customMaterial = CustomMaterial.getCustomMaterial(mid);
            if (customMaterial != null) {
                Block block = event.getBlock();
                event.setDropItems(false);
                block.getWorld().dropItem(block.getLocation().toCenterLocation(), customMaterial.spawn(1));
            }
        }
    }

    public void onBlockBurnEvent(BlockBurnEvent event) {
    }

    public void onBlockCanBuildEvent(BlockCanBuildEvent event) {
    }

    public void onBlockCookEvent(BlockCookEvent event) {
    }

    public void onBlockDamageEvent(BlockDamageEvent event) {
    }

    public void onBlockDispenseEvent(BlockDispenseEvent event) {
    }

    public void onBlockExplodeEvent(BlockExplodeEvent event) {
    }

    public void onBlockPlaceEvent(BlockPlaceEvent event) {
    }

    public void onBlockRedstoneEvent(BlockRedstoneEvent event) {
    }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
    }

    @Override
    public int hashCode() {
        return getBid().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof CustomBlock)) return false;
        return ((CustomBlock) o).getBid().equals(this.getBid());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + bid.asString() + "]";
    }

}
