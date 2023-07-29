package ua.rozipp.core.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ua.rozipp.core.items.ItemStackBuilder;

public class GuiItemBuilder extends ItemStackBuilder {

    protected GuiItemBuilder(ItemStack stack) {
        super(stack);
    }

    protected GuiItemBuilder(Material material) {
        super(material);
    }

    public static GuiItemBuilder guiItemBuilder(ItemStack stack) {
        return (new GuiItemBuilder(stack));
    }

    public static GuiItemBuilder guiItemBuilder(Material material) {
        return (new GuiItemBuilder(material));
    }

    @Override
    public GuiItemBuilder setName(String name) {
        super.setName(name);
        return this;
    }

    public GuiItemBuilder name(Component name) {
        super.name(name);
        return this;
    }

    public GuiItemBuilder setAction(GuiAction action) {
        this.addActionData("action", action.getName());
        return this;
    }

    public GuiItemBuilder addActionData(String key, String value) {
        this.addTag(GuiHelper.TAG_GUI + "." + key, value);
        this.addLore("GUI_" + key + " " + value); // TODO for debag
        return this;
    }

    /**
     * Добавляет предмету action "CallbackGui", По нажатию на GuiItemBuilder возвращает на GuiInventory.execute(String... strings) строку data
     */
    public GuiItemBuilder setCallbackGui(String data) {
        this.setAction(GuiHelper.CALLBACK_GUI);
        this.addActionData(GuiHelper.CALLBACK_GUI.argName1(), data);
        return this;
    }

    /**
     * Добавляеть предмету action "OpenInventory". По нажатию на GuiItemBuilder открываеться инвентарь с именем "className" которому передається параметр "arg"
     */
    public GuiItemBuilder setOpenInventory(Class<? extends GuiInventory> cls, String arg) {
        this.setAction(GuiHelper.OPEN_INVENTORY);
        this.addActionData(GuiHelper.OPEN_INVENTORY.argName1(), cls.getSimpleName());
        this.addActionData(GuiHelper.OPEN_INVENTORY.argName2(), arg);
        return this;
    }

}
