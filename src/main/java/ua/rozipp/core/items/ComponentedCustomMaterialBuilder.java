package ua.rozipp.core.items;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;

import java.util.List;
import java.util.Map;

public class ComponentedCustomMaterialBuilder extends CustomMaterialBuilder {
    protected List<RConfig> components;

    public ComponentedCustomMaterialBuilder loadConfig(RConfig b) throws InvalidConfiguration {
        super.loadConfig(b);
        components = b.getRConfigList("components");
        return this;
    }

    public void setValues(ComponentedCustomMaterial cmat) {
        super.setValues(cmat);
        cmat.addComponents(components);
    }

    @Override
    public ComponentedCustomMaterial build(Plugin plugin) {
        ComponentedCustomMaterial cmat = new ComponentedCustomMaterial(new NamespacedKey(plugin, mid), material, name);
        setValues(cmat);
        return cmat;
    }
}
