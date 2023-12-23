package ua.rozipp.core.blockscomponents;

import com.destroystokyo.paper.event.block.TNTPrimeEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Block;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import ua.rozipp.core.blockdata.events.*;
import ua.rozipp.core.blocks.CustomBlockType;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class BlockComponent {
    private final static Map<String, Class<? extends BlockComponent>> components = new HashMap<>();

    static {
        register(TNTExplode.class);
        register(TNTFrozen.class);
        register(OpenGui.class);
    }

    public static Class<? extends BlockComponent> getBlockComponentClass(String name) {
        return components.get(name);
    }

    public static void register(Class<? extends BlockComponent> aClass) {
        components.put(aClass.getSimpleName(), aClass);
    }

    public static BlockComponentBuilder builder(CustomBlockType type) {
        return new BlockComponentBuilder(type);
    }

    @Getter
    @Setter
    private CustomBlockType type;

    public BlockComponent() {
    }

    protected void load(RConfig rConfig) throws InvalidConfiguration {
    }

    public void onInteract(PlayerInteractEvent event) {
    }

    public void onBlockBurnEvent(BlockBurnEvent event) {
    }

    public void onBlockCookEvent(BlockCookEvent event) {
    }

    public void onBlockDamageEvent(BlockDamageEvent event) {
    }

    public void onBlockDispenseEvent(BlockDispenseEvent event) {
    }

    public void onBlockExplodeEvent(BlockExplodeEvent event) {
    }

    public void onBlockRedstoneEvent(BlockRedstoneEvent event) {
    }

    public void onBlockIgniteEvent(BlockIgniteEvent event, @NotNull Block block) {
    }

    public TNTPrimed onTNTPrime(TNTPrimeEvent event, TNTPrimed tntPrimed) {
        return tntPrimed;
    }

    public void onCanBuild(CustomBlockCanBuildEvent event) {
    }

    public void onBuild(CustomBlockBuildEvent event) {
    }

    public void onDestroy(CustomBlockDestroyEvent event) {
    }

    public void onCanDestroy(CustomBlockCanDestroyEvent event) {
    }

    public void onLoad(DataBlockLoadEvent event) {
    }

    public void onCanMove(DataBlockCanMoveEvent event) {
    }

    public void onMove(DataBlockMoveEvent event) {
    }

    public static class BlockComponentBuilder {
        private final CustomBlockType type;

        public BlockComponentBuilder(@NotNull CustomBlockType type) {
            this.type = type;
        }

        public BlockComponent build(RConfig rConfig) throws InvalidConfiguration {
            try {
                String className = rConfig.getString("name", null, "[Mid = " + type.getId() + "] component's name not found");
                if (className.isEmpty())
                    throw new InvalidConfiguration("[Mid = " + type.getId() + "] component's name is empty");
                Class<? extends BlockComponent> cls = components.get(className);
                BlockComponent blockComponent = cls.getConstructor().newInstance();
                blockComponent.type = type;
                blockComponent.load(rConfig);
                return blockComponent;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new InvalidConfiguration(e.getMessage());
            }
        }
    }
}
