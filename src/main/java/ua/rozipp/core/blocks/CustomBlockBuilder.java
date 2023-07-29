package ua.rozipp.core.blocks;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;

public class CustomBlockBuilder {

    protected String bid;
    protected String mid;
    protected Material material;

    public CustomBlockBuilder loadConfig(RConfig b) throws InvalidConfiguration {
        bid = b.getString("id", null, "Material id is Empty");
        mid = b.getString("mid", "", "");

        String materialName = b.getString("material", "", "");
        material = Material.getMaterial(materialName.toUpperCase());

        return this;
    }
    public CustomBlock build(Plugin plugin) {
        return setValues(new CustomBlock(new NamespacedKey(plugin, bid), NamespacedKey.fromString(mid, plugin), material));
    }

    @Override
    public String toString() {
        return "CustomBlockBuilder(bid=" + this.mid + ", mid=" + this.mid + ", material=" + this.material + ")";
    }

    public CustomBlock setValues(CustomBlock cblock) {
        return cblock;
    }
}
