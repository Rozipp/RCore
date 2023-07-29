package ua.rozipp.core.blocks;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;

import java.util.List;
import java.util.Map;

public class ComponentedCustomBlockBuilder extends CustomBlockBuilder {
    protected List<RConfig> components;

    public ComponentedCustomBlockBuilder loadConfig(RConfig b) throws InvalidConfiguration {
        super.loadConfig(b);
        components = b.getRConfigList("components");
        return this;
    }

    @Override
    public ComponentedCustomBlock build(Plugin plugin) {
        ComponentedCustomBlock customBlock = new ComponentedCustomBlock(new NamespacedKey(plugin, bid), NamespacedKey.fromString(mid, plugin));
        setValues(customBlock);
        return customBlock;
    }

    @Override
    public String toString() {
        return "ComponentedCustomBlockBuilder(bid=" + this.mid + ", mid=" + this.mid + ", material=" + this.material + ")";
    }

    public ComponentedCustomBlock setValues(ComponentedCustomBlock cblock) {
        super.setValues(cblock);
        cblock.addComponents(components);
        return cblock;
    }
}
