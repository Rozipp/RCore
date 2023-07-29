package ua.rozipp.core.items;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;

import java.util.ArrayList;
import java.util.List;

public class CustomMaterialBuilder {
    protected String mid;
    protected Material material;
    protected String name;

    protected String category;
    protected boolean vanilla = false;
    protected boolean shiny = false;
    protected int tradeValue = 0;
    protected List<String> lore;

    public CustomMaterialBuilder loadConfig(RConfig b) throws InvalidConfiguration {
        mid = b.getString("id", null, "Material id is Empty");

        String materialName = b.getString("material", "", "");
        material = Material.getMaterial(materialName.toUpperCase());
        if (material == null) throw new NullArgumentException("[Mid = " + mid + "] Material " + material + "not found");

        name = b.getString("name", null, "[Mid = " + mid + "] Name is Empty");

        category = b.getString("category", "", "");
        vanilla = b.getBoolean("vanilla", vanilla, "");
        shiny = b.getBoolean("shiny", shiny, "");
        tradeValue = b.getInt("tradeValue", -1, "");

        List<String> configLore = b.getList("lore", String.class, new ArrayList<>(), null);
        if (!configLore.isEmpty()) {
            lore = configLore;
        }

        return this;
    }

    public CustomMaterial build(Plugin plugin) {
        return setValues(new CustomMaterial(new NamespacedKey(plugin, mid), material, name));
    }

    public String toString() {
        return "CustomMaterialBuilder(mid=" + this.mid + ", material=" + this.material + ", name=" + this.name +")";
    }

    public CustomMaterial setValues(CustomMaterial cmat) {
        cmat.category = category;
        cmat.vanilla = vanilla;
        cmat.tradeValue = tradeValue;
        cmat.shiny = shiny;
        cmat.lore = lore;
        return cmat;
    }
}
