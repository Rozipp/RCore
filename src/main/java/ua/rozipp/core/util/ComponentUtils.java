package ua.rozipp.core.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import ua.rozipp.core.LocaleHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ComponentUtils {

    public static String componentToString(Component component) {
        return componentToString(component, LocaleHelper.getServerLocale());
    }

    public static String componentToString(Component component, Locale locale) {
        StringBuilder sb = new StringBuilder();
        readComponent(sb, component, locale);
        return sb.toString();
    }

    private static StringBuilder readComponent(StringBuilder sb, Component component, Locale locale) {
        if (component instanceof TextComponent)
            sb.append(((TextComponent) component).content());
        else {
            if (component instanceof TranslatableComponent) {
                TranslatableComponent tc = (TranslatableComponent) component;
                List<String> arg = new ArrayList<>();
                for (Component c : tc.args()) arg.add(componentToString(c, locale));

                sb.append(LocaleHelper.translate(locale, tc.key(), arg.toArray(new String[0])));
            }
        }
        if (!component.children().isEmpty())
            for (Component c : component.children()) {
                sb = readComponent(sb, c, locale);
            }
        return sb;
    }

    public static Component translateComponent(Component component) {
        return translateComponent(component, Locale.getDefault());
    }

    public static Component translateComponent(Component component, Locale locale) {
        if (component instanceof TranslatableComponent) {
            TranslatableComponent tc = (TranslatableComponent) component;
            List<String> arg = new ArrayList<>();
            for (Component c : tc.args()) arg.add(componentToString(c, locale));

            String s = LocaleHelper.translate(locale, tc.key(), arg.toArray(new String[0]));
            Component result = Component.text(s, tc.style());
            if (!component.children().isEmpty())
                for (Component c : component.children()) {
                    result = result.append(translateComponent(c, locale));
                }
            return result;
        } else {
            List<Component> list = new ArrayList<>();
            if (!component.children().isEmpty())
                for (Component c : component.children()) {
                    list.add(translateComponent(c, locale));
                }
            return (list.isEmpty()) ? component : component.children(list);
        }
    }

}
