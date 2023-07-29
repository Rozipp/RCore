package ua.rozipp.core.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import ua.rozipp.core.LocaleHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ComponentUtils {

    public static String componentedToString(Component component){
        StringBuilder sb = new StringBuilder();
        readComponent(sb, component, LocaleHelper.getServerLocale());
        return sb.toString();
    }

    public static String componentedToString(Component component, Locale locale){
        StringBuilder sb = new StringBuilder();
        readComponent(sb, component, locale);
        return sb.toString();
    }

    private static StringBuilder readComponent(StringBuilder sb, Component component, Locale locale){
        if (component instanceof TextComponent)
            sb.append(((TextComponent) component).content());
        if (component instanceof TranslatableComponent) {
            TranslatableComponent tc = (TranslatableComponent) component;
            List<String> arg = new ArrayList<>();
            for (Component c : tc.args()) arg.add(componentedToString(c, locale));

            sb.append(LocaleHelper.translate(tc.key(), arg.toArray(new String[0])));
        }
        if (!component.children().isEmpty())
            for (Component c : component.children()) {
                sb = readComponent(sb, c, locale);
            }
        return sb;
    }
}
