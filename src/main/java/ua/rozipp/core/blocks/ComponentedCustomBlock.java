package ua.rozipp.core.blocks;

import net.kyori.adventure.key.Key;
import org.bukkit.event.block.*;
import org.bukkit.plugin.Plugin;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  ComponentedCustomBlock extends CustomBlock{

    protected static Map<Key, ComponentedCustomBlock> componentedCustomBlocks = new HashMap<>();

    public final Map<Class<? extends BlockComponent>, BlockComponent> components = new HashMap<>();

    public ComponentedCustomBlock(Key bid, Key mid) {
        super(bid, mid);
        componentedCustomBlocks.put(this.getBid(), this);
    }

    private void buildComponents(List<RConfig> components) {
        if (components != null) {
            for (RConfig compInfo : components) {
                BlockComponent bc = null;
                try {
                    bc = BlockComponent.buildComponent(compInfo);
                } catch (InvalidConfiguration e) {
                    LogHelper.error(e.getMessage());
                }
                if (bc != null) this.components.put(bc.getClass(), bc);
            }
        }
    }

    public <T extends BlockComponent> boolean  hasComponent(Class<T> aClass) {
        return this.components.containsKey(aClass);
    }

    @SuppressWarnings("unchecked")
    public <T extends BlockComponent> T getComponent(Class<T> aClass) {
        BlockComponent component = this.components.get(aClass);
        if (component != null) return (T) component;
        return null;
    }

    public Collection<BlockComponent> getComponents() {
        return this.components.values();
    }

    public void onBlockBreak(BlockBreakEvent event) {
        for (BlockComponent comp : getComponents()) {
            comp.onBlockBreak(event);
        }
        super.onBlockBreak(event);
    }

    public void onBlockBurnEvent(BlockBurnEvent event) {
        for (BlockComponent comp : getComponents()) {
            comp.onBlockBurnEvent(event);
        }
    }

    public void onBlockCanBuildEvent(BlockCanBuildEvent event) {
        for (BlockComponent comp : getComponents()) {
            comp.onBlockCanBuildEvent(event);
        }
    }

    public void onBlockCookEvent(BlockCookEvent event) {
        for (BlockComponent comp : getComponents()) {
            comp.onBlockCookEvent(event);
        }
    }

    public void onBlockDamageEvent(BlockDamageEvent event) {
        for (BlockComponent comp : getComponents()) {
            comp.onBlockDamageEvent(event);
        }
    }

    public void onBlockDispenseEvent(BlockDispenseEvent event) {
        for (BlockComponent comp : getComponents()) {
            comp.onBlockDispenseEvent(event);
        }
    }

    public void onBlockExplodeEvent(BlockExplodeEvent event) {
        for (BlockComponent comp : getComponents()) {
            comp.onBlockExplodeEvent(event);
        }
    }

    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        for (BlockComponent comp : getComponents()) {
            comp.onBlockPlaceEvent(event);
        }
    }

    public void onBlockRedstoneEvent(BlockRedstoneEvent event) {
        for (BlockComponent comp : getComponents()) {
            comp.onBlockRedstoneEvent(event);
        }
    }

    public void addComponents(List<RConfig> components) {
        buildComponents(components);
    }
}
