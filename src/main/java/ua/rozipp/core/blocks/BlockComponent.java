package ua.rozipp.core.blocks;

import org.bukkit.event.block.*;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class BlockComponent {
    private final static Map<String, Class<? extends BlockComponent>> components = new HashMap<>();

    static {
    }

    public static Class<? extends BlockComponent> getBlockComponentClass(String name) {
        return components.get(name);
    }

    public static void register(Class<? extends BlockComponent> aClass){
        components.put(aClass.getSimpleName(), aClass);
    }

    public static BlockComponent buildComponent(RConfig compInfo) throws InvalidConfiguration {
        String name = compInfo.getString("name", null, "Not found component name in CustomBlock");
        if (name == null ||name.isEmpty()) return null;
        Class<? extends BlockComponent> cls = components.get(name);
        BlockComponent ic = null;
        if (cls != null)
            try {
                Class<?>[] partypes = {RConfig.class};
                Object[] arglist = {compInfo};
                ic = cls.getConstructor(partypes).newInstance(arglist);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        return ic;
    }

    private final String name;

    public BlockComponent(RConfig compInfo) {
        try {
            this.name = compInfo.getString("name", null, "Not found component name in CustomBlock");
        } catch (InvalidConfiguration e) {
            throw new RuntimeException(e);
        }
    }

    public String getName(){
        return name;
    }


    public void onBlockBreak(BlockBreakEvent event) {
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
}
