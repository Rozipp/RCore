package ua.rozipp.core.blocks;

import lombok.Getter;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ua.rozipp.core.LogHelper;
import ua.rozipp.core.PluginHelper;
import ua.rozipp.core.blockscomponents.BlockComponent;
import ua.rozipp.core.blockscomponents.BlockHologram;
import ua.rozipp.core.config.RConfig;
import ua.rozipp.core.exception.InvalidConfiguration;
import ua.rozipp.core.items.CustomMaterial;
import ua.rozipp.core.items.ItemHelper;

import java.util.*;

public class CustomBlockType {

    @Getter
    private final Key id;
    @Getter
    private final @NotNull Material blockMaterial;
    @Getter
    private final Key itemId;
    public final Map<Class<? extends BlockComponent>, BlockComponent> components = new HashMap<>();

    public CustomBlockType(@NotNull Key id, Key itemId) {
        this(id, itemId, null);
    }

    public CustomBlockType(@NotNull Key id, Key itemId, Material blockMaterial) {
        this.id = id;
        CustomMaterial customMaterial = CustomMaterial.getCustomMaterial(itemId);
        this.blockMaterial = (blockMaterial != null) ?
                blockMaterial :
                (customMaterial != null) ?
                        customMaterial.getMaterial() :
                        Material.WHITE_WOOL;
        this.itemId = (customMaterial == null) ? this.blockMaterial.getKey() : itemId;
    }

    public static CustomBlockTypeBuilder builder(Key key) {
        return new CustomBlockTypeBuilder(key, key);
    }

    public static CustomBlockTypeBuilder builder(Key key, Key itemKey) {
        return new CustomBlockTypeBuilder(key, itemKey);
    }

    public void addComponent(BlockComponent bc) {
        this.components.put(bc.getClass(), bc);
    }

    public void addComponents(List<RConfig> components) {
        if (components != null)
            for (RConfig compInfo : components)
                try {
                    addComponent(BlockComponent.builder(this).build(compInfo));
                } catch (InvalidConfiguration e) {
                    LogHelper.error("When loading components for the CustomBlock '" + getId() + "', I received the message: '" + e.getMessage() + "'");
                }
    }

    public <T extends BlockComponent> boolean hasComponent(Class<T> aClass) {
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

    public ItemStack getItem() {
        return ItemHelper.createItemStack(getItemId(), 1);
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof CustomBlockType)) return false;
        return ((CustomBlockType) o).getId().equals(this.getId());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + id.asString() + "]";
    }

    public Collection<ItemStack> getDrops() {
        List<ItemStack> list = new ArrayList<>();
        list.add(getItem());
        return list;
    }

    public static class CustomBlockTypeBuilder {
        private final Key id;
        private Material blockMaterial;
        private final Key itemId;
        private RConfig rConfig;
        public List<BlockComponent> components = new ArrayList<>();

        CustomBlockTypeBuilder(@NotNull Key id, Key itemId) {
            this.id = id;
            this.itemId = itemId;
        }

        public CustomBlockTypeBuilder blockMaterial(Material blockMaterial) {
            this.blockMaterial = blockMaterial;
            return this;
        }

        public CustomBlockTypeBuilder addComponent(BlockComponent component) {
            components.add(component);
            return this;
        }

        public CustomBlockType build() {
            CustomBlockType type = new CustomBlockType(id, itemId, blockMaterial);
            components.forEach(type::addComponent);
            PluginHelper.getCustomBlockRegistry().register(type);
            return type;
        }

        public CustomBlockType load(RConfig rConfig) throws InvalidConfiguration {
            if (blockMaterial == null) {
                String materialName = rConfig.getString("material", "");
                if (materialName != null && !materialName.isEmpty())
                    blockMaterial = Material.getMaterial(materialName.toUpperCase());
            }

            CustomBlockType type = new CustomBlockType(id, itemId, blockMaterial);
            type.addComponents(rConfig.getRConfigList("blockcomponents"));
            type.addComponent(new BlockHologram(rConfig.getString("holographicText", "", ""), ""));
            PluginHelper.getCustomBlockRegistry().register(type);
            return type;
        }

    }
}
