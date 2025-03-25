package ua.rozipp.core.items;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.config.ConfigMaterialCategory;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.itemscomponents.ItemComponent;

import java.util.ArrayList;
import java.util.List;

public class CustomMaterialBuilder {
    private final @NotNull Key mid;
    private final @NotNull Material material;
    private final @NotNull String name;
    private @Nullable List<String> lore;
    private Color color;
    private boolean glow;
    private int tradeValue;
    private boolean vanilla;
    private String category;
    private int tier;
    public List<ItemComponent> components = new ArrayList<>();

    CustomMaterialBuilder(@NotNull Key mid, @NotNull Material material, @NotNull String name) {
        this.mid = mid;
        this.material = material;
        this.name = name;
    }

    public static void loadAll(RConfig cfg) throws InvalidConfiguration {
        if (cfg == null) return;
        List<RConfig> configMaterials = cfg.getRConfigList("materials");
        int count = 0;
        for (RConfig rConfig : configMaterials) {
            try {
                load(rConfig);
                count++;
            } catch (InvalidConfiguration e) {
                LogHelper.error(e.getComponent());
            }
        }
        LogHelper.info("Loaded " + count + " Materials.");
    }

    public static CustomMaterial load(RConfig rConfig) throws InvalidConfiguration {
        Key mid = null;
        String id = rConfig.getString("id", null, "CustomMaterial id is Empty");
        try {
            mid = new NamespacedKey(rConfig.getPlugin(), id);
        } catch (IllegalArgumentException e) {
            throw new InvalidConfiguration(
                    Component.text("[Mid = " + id + "] ", NamedTextColor.DARK_PURPLE).append(Component.text(e.getMessage(), NamedTextColor.WHITE)));
        }

        String materialName = rConfig.getString("material");
        if (materialName == null) throw new InvalidConfiguration(
                Component.text("[Mid = " + mid + "] ", NamedTextColor.DARK_PURPLE).append(Component.text("Material not found", NamedTextColor.WHITE)));

        Material material = Material.getMaterial(materialName.toUpperCase());
        if (material == null) throw new InvalidConfiguration(
                Component.text("[Mid = " + mid + "] ", NamedTextColor.DARK_PURPLE).append(Component.text("Material \"" + materialName + "\" not found", NamedTextColor.WHITE)));

        String name = rConfig.getString("name");
        if (name == null) throw new InvalidConfiguration(
                Component.text("[Mid = " + mid + "] ", NamedTextColor.DARK_PURPLE).append(Component.text("Name is Empty", NamedTextColor.WHITE)));

        CustomMaterialBuilder builder = CustomMaterial.builder(mid, material, name)
                .category(rConfig.getString("category", ""))
                .vanilla(rConfig.getBoolean("vanilla", false))
                .glow(rConfig.getBoolean("glow", false))
                .tier(rConfig.getInt("tier", 0))
                .tradeValue(rConfig.getInt("tradeValue", -1));

        List<String> configLore = rConfig.getList("lore", String.class, new ArrayList<>(), null);
        if (!configLore.isEmpty()) builder.lore(configLore);
        builder.color(rConfig.getString("color"));
        CustomMaterial cmat = builder.build();
        cmat.buildComponents(rConfig.getRConfigList("components"));
        ConfigMaterialCategory.addMaterial(cmat);
        return cmat;
    }

    public CustomMaterialBuilder lore(@Nullable List<String> lore) {
        this.lore = lore;
        return this;
    }

    public CustomMaterialBuilder glow(boolean glow) {
        this.glow = glow;
        return this;
    }

    public CustomMaterialBuilder tradeValue(int tradeValue) {
        this.tradeValue = tradeValue;
        return this;
    }

    public CustomMaterialBuilder vanilla(boolean vanilla) {
        this.vanilla = vanilla;
        return this;
    }

    public CustomMaterialBuilder category(String category) {
        this.category = category;
        return this;
    }

    public CustomMaterialBuilder color(String colorCode) throws InvalidConfiguration {
        try {
            if (colorCode != null)
                color = Color.fromRGB(Integer.parseInt(colorCode, 16));
        } catch (Exception e) {
            throw new InvalidConfiguration(
                    Component.text("[Mid = " + mid + "] ", NamedTextColor.DARK_PURPLE).append(Component.text("Decode color error: " + e.getMessage(), NamedTextColor.WHITE)));
        }
        return this;
    }

    public CustomMaterialBuilder tier(int tier) {
        this.tier = tier;
        return this;
    }

    public CustomMaterialBuilder addComponent(ItemComponent component) {
        if (component != null) {
            this.components.add(component);
        }
        return this;
    }

    public CustomMaterial build() {
        CustomMaterial customMaterial = new CustomMaterial(this.mid, this.material, this.name);
        customMaterial.lore = this.lore;
        customMaterial.color = this.color;
        customMaterial.glow = this.glow;
        customMaterial.tradeValue = this.tradeValue;
        customMaterial.vanilla = this.vanilla;
        customMaterial.category = this.category;
        customMaterial.tier = this.tier;
        components.forEach(customMaterial::addComponent);
        ConfigMaterialCategory.addMaterial(customMaterial);
        return customMaterial;
    }

}
